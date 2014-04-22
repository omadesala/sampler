package com.cs.omp;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.linear.Array2DRowFieldMatrix;
import org.apache.commons.math3.linear.FieldMatrix;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.apache.commons.math3.util.FastMath;
import org.jscience.mathematics.vector.ComplexMatrix;

import Jama.Matrix;

import com.probablity.utils.MatrixUtils;

public class OMP {

    private int sparsity; // k-sparsity
    private int length; // the length of signal
    private int countOfMeasure; // the count of measurement.(M>=K*log(N/K)

    private Matrix phi; // measure matrix
    private FieldMatrix<Complex> psi; // transform matrix for raw signal. here
                                      // is fft.

    private Matrix input; // the raw signal of input.
    private Matrix measurement; // the matrix for measurement.

    private Matrix measureResult;// result by random sample.
    private Matrix residual;// result by random sample.
    private FieldMatrix<Complex> augT;// result by random sample.

    // frequency
    private int f1 = 50;
    private int f2 = 100;
    private int f3 = 200;
    private int f4 = 400;

    private int fs = 800;// rate of sample
    private float ts = 1 / ((float) fs);// rate of sample

    private int loopCount;// iterator times

    public OMP() {

        this.length = 256;
        this.sparsity = 7;
        this.input = getSignal();
        this.countOfMeasure = 64;
        this.loopCount = 2 * this.sparsity;
    }

    public OMP(Matrix data) {
        this.input = data;
    }

    public void process() {

        // step 1. get the random sample
        this.phi = Matrix.random(countOfMeasure, this.length);

        this.measureResult = this.phi.times(this.input.transpose());

        // step 2. orthogonality match pursuit .

        this.psi = MatrixUtils.fft(MatrixUtils.getUnitMatrix(Matrix.random(
                this.length, this.length)));

        FieldMatrix<Complex> measure = MatrixUtils.toComplex(this.phi);
        FieldMatrix<Complex> restoryMatrix = measure.multiply(this.psi
                .transpose());

        MatrixUtils.printMatrix(restoryMatrix);

        System.out.println("row dim: " + restoryMatrix.getRowDimension());
        System.out.println("col dim: " + restoryMatrix.getColumnDimension());

        Matrix hatOfy = new Matrix(1, this.length);
        this.residual = measureResult.copy();

        // start to iterate.
        Matrix aug_y = new Matrix(this.countOfMeasure, 1);

        Matrix measureY = new Matrix(this.measureResult.getRowDimension(), 1);
        this.augT = new Array2DRowFieldMatrix<Complex>();

        new Matrix(this.measurement.getRowDimension(),
                this.measurement.getColumnDimension());

        for (int i = 0; i < this.loopCount; i++) {
            for (int j = 0; j < this.length; j++) {
                Complex[] matrixColumn = MatrixUtils.getMatrixColumn(
                        restoryMatrix, j);
                FieldMatrix<Complex> column = new Array2DRowFieldMatrix<Complex>(
                        matrixColumn);

                FieldMatrix<Complex> innerMultiply = column.transpose()
                        .multiply(MatrixUtils.toComplex(residual));
                Complex entry = innerMultiply.getEntry(0, 0);
                double d = entry.abs();

                measureY.set(j, 0, d);

            }

            int pos = 0;
            double maxValue = -1.;
            for (int k = 0; k < measureY.getRowDimension(); k++) {
                double d = measureY.get(k, 0);
                if (d > maxValue) {
                    maxValue = d;
                    pos = k;
                }
            }

            MatrixUtils.setMatrixColumn(this.augT,
                    MatrixUtils.getMatrixColumn(this.measurement, pos), i);

            // remove T column of row
            Matrix zero = new Matrix(this.measurement.getRowDimension(), 1);
            MatrixUtils.setMatrixColumn(this.measurement, zero, pos);

            aug_y = this.augT.transpose().times(this.augT).inverse()
                    .times(this.augT.transpose()).times(this.measurement);

            this.residual = this.measurement.minus(this.augT.times(aug_y));

            // aug_y=(Aug_t'*Aug_t)^(-1)*Aug_t'*s; % 最小二乘,使残差最小
            // r_n=s-Aug_t*aug_y; % 残差

        }

        // double[][] array = aug_y.getArray();
        // Complex[] transform = fft.transform(array[0], TransformType.INVERSE);

    }

    public double getMod(Complex complex) {

        double real = complex.getReal();
        double imaginary = complex.getImaginary();
        double mod = Math.sqrt(real * real + imaginary * imaginary);

        return mod;
    }

    // %% 3. 正交匹配追踪法重构信号(本质上是L_1范数最优化问题)
    // %匹配追踪：找到一个其标记看上去与收集到的数据相关的小波；在数据中去除这个标记的所有印迹；不断重复直到我们能用小波标记“解释”收集到的所有数据。
    //
    // m=2*K; % 算法迭代次数(m>=K)，设x是K-sparse的
    // Psi=fft(eye(N,N))/sqrt(N); % 傅里叶正变换矩阵
    // T=Phi*Psi'; % 恢复矩阵(测量矩阵*正交反变换矩阵)
    //
    // hat_y=zeros(1,N); % 待重构的谱域(变换域)向量
    // Aug_t=[]; % 增量矩阵(初始值为空矩阵)
    // r_n=s; % 残差值
    //
    // for times=1:m; % 迭代次数(有噪声的情况下,该迭代次数为K)
    // for col=1:N; % 恢复矩阵的所有列向量
    // product(col)=abs(T(:,col)'*r_n); % 恢复矩阵的列向量和残差的投影系数(内积值)
    // end
    // [val,pos]=max(product); % 最大投影系数对应的位置，即找到一个其标记看上去与收集到的数据相关的小波
    // Aug_t=[Aug_t,T(:,pos)]; % 矩阵扩充
    //
    // T(:,pos)=zeros(M,1); % 选中的列置零（实质上应该去掉，为了简单我把它置零），在数据中去除这个标记的所有印迹
    // aug_y=(Aug_t'*Aug_t)^(-1)*Aug_t'*s; % 最小二乘,使残差最小
    // r_n=s-Aug_t*aug_y; % 残差
    // pos_array(times)=pos; % 纪录最大投影系数的位置
    // end
    // hat_y(pos_array)=aug_y; % 重构的谱域向量
    // hat_x=real(Psi'*hat_y.'); % 做逆傅里叶变换重构得到时域信号
    //
    // %% 4. 恢复信号和原始信号对比
    // figure(1);
    // hold on;
    // plot(hat_x,'k.-') % 重建信号
    // plot(x,'r') % 原始信号
    // legend('Recovery','Original')
    // norm(hat_x.'-x)/norm(x) % 重构误差

    private Matrix getSignal() {

        Matrix signal = new Matrix(1, this.length);

        for (int i = 0; i < this.length; i++) {

            double s = 0.3 * Math.cos(2 * Math.PI * f1 * i * ts) + 0.6
                    * Math.cos(2 * Math.PI * f2 * i * ts) + 0.1
                    * Math.cos(2 * Math.PI * f3 * i * ts) + 0.9
                    * Math.cos(2 * Math.PI * f4 * i * ts);
            signal.set(0, i, s);
        }

        return signal;
    }
}

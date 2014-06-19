package com.svm;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

/**
 * 
 * @ClassName: SimplifiedSmo
 * @Description:http://blog.csdn.net/mutex86/article/details/10001523,http://blog.csdn.net/techq/article/details/6171688
 */
public class SimplifiedSmo {

    private Random random = new Random();

    /**
     * all alpha value at the boundary line
     */
    private HashSet<Integer> boundAlpha = new HashSet<Integer>();

    /**
     * samples for input ,there are M samples and each sample have N
     * attribute,so the input is x[M][N]
     */
    private double x[][];

    /**
     * kernel function matrix
     */
    double kernel[][];

    /**
     * lagrange multiplier, each sample with an alpha, so a[M] = x.length;
     */
    double a[];
    /**
     * target value for classify, for binary classify, y is -1 or 1.
     */
    int y[];

    /**
     * y = w*x+b ; so b is scalar , w x and y is vector.
     */
    double b = 0.0;

    /**
     * 
     * @Description: train svm model with input and lables
     * @param @param x samples with attribute
     * @param @param y samples lables
     * @param @return the trained model
     * @return SVMModel
     * @throws
     */
    private SVMModel train(double x[][], int y[]) {
        this.x = x;
        this.y = y;
        kernel = new double[x.length][x.length];
        initiateKernel(x.length);

        /*
         * default input parameter C: regularization parameter, tol: numerical
         * tolerance, max passes
         */
        double C = 1; // regularization value
        double tol = 0.01;// tolerance value
        int maxPasses = 5; // max iter_times without lagrange multiplier changed

        /*
         * initial a[], b, passes
         */

        double a[] = new double[x.length];// lagrange multiplier
        this.a = a;

        // initialize lagrange multiplier with zero
        for (int i = 0; i < x.length; i++) {
            a[i] = 0;
        }
        int passes = 0;

        while (passes < maxPasses) {
            // 表示改变乘子的次数（基本上是成对改变的）
            // how many times the multiplier changed. a pair alpha will be
            // changed each time.
            int num_changed_alphas = 0;
            for (int i = 0; i < x.length; i++) {
                // 表示特定阶段由a和b所决定的输出与真实yi的误差
                // 参照公式(7)
                double Ei = getE(i);
                /**
                 * 把违背KKT条件的ai作为第一个<br/>
                 * 满足KKT条件的情况是：<br/>
                 * yi*f(i) >= 1 and alpha == 0 (正确分类)<br/>
                 * yi*f(i) == 1 and 0<alpha < C (在边界上的支持向量) <br/>
                 * yi*f(i) <= 1 and alpha == C (在边界之间) <br/>
                 * 
                 * ri = y[i] * Ei =y[i] * (f(i) - y[i])<br/>
                 * = y[i] * f(i) - y[i]^2 >= 0 <br/>
                 * 
                 * 如果ri < 0并且alpha < C 则违反了KKT条件 因为原本ri < 0 应该对应的是alpha = C
                 * 同理，ri > 0并且alpha > 0则违反了KKT条件 因为原本ri > 0对应的应该是alpha =0
                 * 
                 * 这里只更新那些违背了KKT条件的拉格朗日乘子
                 */
                if ((y[i] * Ei < -tol && a[i] < C) || (y[i] * Ei > tol && a[i] > 0)) {
                    /*
                     * ui*yi=1边界上的点 0 < a[i] < C 找MAX|E1 - E2|
                     */
                    int j;
                    /*
                     * boundAlpha表示x点处于边界上所对应的 拉格朗日乘子a的集合
                     */
                    if (this.boundAlpha.size() > 0) {
                        j = findMax(Ei, this.boundAlpha);
                    } else
                        // 如果边界上没有，就随便选一个j != i的aj
                        j = RandomSelect(i);

                    double Ej = getE(j);

                    // 保存当前的ai和aj
                    double oldAi = a[i];
                    double oldAj = a[j];

                    /*
                     * 计算乘子的范围U, V
                     */
                    double L, H;
                    if (y[i] != y[j]) {
                        L = Math.max(0, a[j] - a[i]);
                        H = Math.min(C, C - a[i] + a[j]);
                    } else {
                        L = Math.max(0, a[i] + a[j] - C);
                        H = Math.min(0, a[i] + a[j]);
                    }

                    /*
                     * 如果eta等于0或者大于0 则表明a最优值应该在L或者U上
                     */
                    double eta = 2 * k(i, j) - k(i, i) - k(j, j);// 公式(3)

                    if (eta >= 0)
                        continue;

                    a[j] = a[j] - y[j] * (Ei - Ej) / eta;// 公式(2)
                    if (0 < a[j] && a[j] < C)
                        this.boundAlpha.add(j);

                    if (a[j] < L)
                        a[j] = L;
                    else if (a[j] > H)
                        a[j] = H;

                    if (Math.abs(a[j] - oldAj) < 1e-5)
                        continue;
                    a[i] = a[i] + y[i] * y[j] * (oldAj - a[j]);
                    if (0 < a[i] && a[i] < C)
                        this.boundAlpha.add(i);

                    /*
                     * 计算b1， b2
                     */
                    double b1 = b - Ei - y[i] * (a[i] - oldAi) * k(i, i) - y[j] * (a[j] - oldAj) * k(i, j);
                    double b2 = b - Ej - y[i] * (a[i] - oldAi) * k(i, j) - y[j] * (a[j] - oldAj) * k(j, j);

                    if (0 < a[i] && a[i] < C)
                        b = b1;
                    else if (0 < a[j] && a[j] < C)
                        b = b2;
                    else
                        b = (b1 + b2) / 2;

                    num_changed_alphas = num_changed_alphas + 1;
                }
            }
            if (num_changed_alphas == 0) {
                passes++;
            } else
                passes = 0;
        }

        return new SVMModel(a, y, b);
    }

    private int findMax(double Ei, HashSet<Integer> boundAlpha2) {
        
        double max = 0;
        int maxIndex = -1;
        
        for (Iterator<Integer> iterator = boundAlpha2.iterator(); iterator.hasNext();) {
            Integer j = (Integer) iterator.next();
            double Ej = getE(j);
            if (Math.abs(Ei - Ej) > max) {
                max = Math.abs(Ei - Ej);
                maxIndex = j;
            }
        }
        return maxIndex;
    }

    private double predict(SVMModel model, double x[][], int y[]) {
        double probability = 0;
        int correctCnt = 0;
        int total = y.length;

        for (int i = 0; i < total; i++) {
            // 原来训练矩阵的维数（长度）
            int len = model.y.length;
            double sum = 0;
            for (int j = 0; j < len; j++) {
                sum += model.y[j] * model.a[j] * k(j, i);
            }
            sum += model.b;
            if ((sum > 0 && y[i] > 0) || (sum < 0 && y[i] < 0))
                correctCnt++;
        }
        probability = (double) correctCnt / (double) total;
        return probability;
    }

    private void initiateKernel(int length) {
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                kernel[i][j] = k(i, j);
            }
        }
    }

    /*
     * simple kernel(i, j) = xTx
     */
    private double k(int i, int j) {
        double sum = 0.0;
        // System.out.println("x.length:" + x.length + "x[i].length" +
        // x[i].length);
        for (int t = 0; t < x[i].length; t++) {
            sum += x[i][t] * x[j][t];
        }
        return sum;
    }

    /*
     * select j which is not equal with i
     */
    private int RandomSelect(int i) {
        int j;
        do {
            j = random.nextInt(x.length);
        } while (i == j);
        return j;
    }

    /**
     * 
     * @Description: calculate the w*x+b = \sum_{i=0}^{m} a[i]*y[i]*<xi,x>
     * @param @param j
     * @param @return
     * @return double
     * @throws
     */
    private double f(int j) {
        double sum = 0;
        for (int i = 0; i < x.length; i++) {
            sum += a[i] * y[i] * kernel[i][j];
        }

        return sum + this.b;
    }

    /**
     * 
     * @Description: the difference between estimate with real classify
     * @param @param i the index of sample at input
     * @param @return error value
     * @return double
     * @throws
     */
    private double getE(int i) {
        
        return f(i) - y[i];
        
    }

    public static void main(String[] args) {
        SimplifiedSmo simplifiedSMO = new SimplifiedSmo();
        SVMFileReader reader = new SVMFileReader("src/main/resources/svm/heart_scale");
        SVMData svmData = reader.getSVMData(50);

        System.out.println("开始训练...");
        SVMModel model = simplifiedSMO.train(svmData.getX(), svmData.getY());
        System.out.println("训练结束");
        // 开始预测
        // 数据是原来用于训练的数据
        System.out.println("开始预测...");
        double probability = simplifiedSMO.predict(model, svmData.getX(), svmData.getY());
        System.out.println("预测正确率为：" + probability);
    }
}
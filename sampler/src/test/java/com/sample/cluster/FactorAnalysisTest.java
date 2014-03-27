package com.sample.cluster;

import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.util.Queue;
import java.util.Random;
import java.util.Vector;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import Jama.Matrix;

import com.probablity.utils.MatrixUtils;
import com.probablity.utils.ReflectUtil;
import com.sample.distribution.Distribution;
import com.sample.distribution.implement.TwoDimGaussDistribution;
import com.sample.sampler.ISampler;
import com.sample.sampler.implement.inverse.TwoDimGaussSampler;

public class FactorAnalysisTest {

    private FactorAnalysis factorAnalysis;

    @Before
    public void setUp() throws Exception {

        Matrix meanZ = Matrix.random(1, 1);
        Matrix meanX = Matrix.random(1, 1);
        Matrix varZZ = Matrix.random(1, 1);
        Matrix varZX = Matrix.random(1, 1);
        Matrix varXX = Matrix.random(1, 1);

        double[][] datas = new double[2][4];

        datas[0][0] = 1.1;
        datas[0][1] = 2.1;
        datas[0][2] = 1.2;
        datas[0][3] = 2.2;

        datas[1][0] = 1.1;
        datas[1][1] = 2.1;
        datas[1][2] = 1.2;
        datas[1][3] = 2.2;

        Matrix inputMatrix = new Matrix(datas);

        factorAnalysis = new FactorAnalysis.Builder().setMean1(meanZ).setMean2(meanX).setVar11(varZZ).setVar12(varZX)
                .setVar22(varXX).setDatas(inputMatrix).build();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testUpdateMean() {

        try {
            Matrix meanMatrix = ((Matrix) ReflectUtil.getClassMemberMethod(this.factorAnalysis, "updateMean").invoke(
                    this.factorAnalysis, null));

            Assert.assertEquals(1.65, meanMatrix.get(0, 0), 1e-5);
            Assert.assertEquals(1.65, meanMatrix.get(1, 0), 1e-5);

        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testupdateMeanZConditionXi() {

        try {
            Matrix meanMatrix = ((Matrix) ReflectUtil.getClassMemberMethod(this.factorAnalysis, "updateMean").invoke(
                    this.factorAnalysis, null));
            MatrixUtils.printMatrix(meanMatrix);

        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {

            e.printStackTrace();
        }

    }

    @Test
    public void testGenerateData() {

        MatrixUtils.printMatrix(generateData());
    }

    @Test
    @Ignore
    public void test() {
        fail("Not yet implemented");
    }

    private Matrix generateData() {

        double[][] A = new double[2][1];
        A[0][0] = 1.;
        A[1][0] = 2.;
        Matrix lambda = new Matrix(A);

        double[][] errMean = new double[2][1];
        errMean[0][0] = 0.;
        errMean[1][0] = 0.;
        Matrix mu1 = new Matrix(errMean);

        double[][] errVar = new double[2][2];
        errVar[0][0] = 1.;
        errVar[0][1] = 0.;
        errVar[1][0] = 0.;
        errVar[1][0] = 1.;
        Matrix sigma1 = new Matrix(errVar);;

        ISampler<Vector<Double>> sampler = new TwoDimGaussSampler(mu1, sigma1);
        sampler.doSample();
        Queue<Vector<Double>> datas = sampler.getSampleValues();

        Matrix result = new Matrix(2, datas.size());

        for (int i = 0; i < datas.size(); i++) {

            Matrix pointOfMatrix = MatrixUtils.getPointOfMatrix(datas.poll());
            System.out.println("i = " + i);
            MatrixUtils.printMatrix(pointOfMatrix);

            MatrixUtils.setMatrixColumn(result, pointOfMatrix, i);
        }

        int length = 20;
        for (int i = 0; i < length; i++) {

            Random random = new Random();
            double z = random.nextGaussian();

            Matrix times = lambda.times(z);

        }

        return result;

    }

}

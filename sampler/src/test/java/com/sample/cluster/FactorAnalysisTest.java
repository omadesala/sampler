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

import com.probablity.utils.Constant;
import com.probablity.utils.GnuPlotDisplay;
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

        int zDim = 1;
        int xDim = 2;
        int datalength = 4;

        Matrix meanZ = Matrix.random(zDim, 1);
        Matrix meanX = Matrix.random(xDim, 1);
        Matrix varZZ = Matrix.random(zDim, zDim);
        Matrix varZX = Matrix.random(zDim, xDim);
        Matrix varXX = Matrix.random(xDim, xDim);

        System.out.println("meanX row:" + meanX.getRowDimension());

        double[][] datas = new double[xDim][datalength];

        datas[0][0] = 1.1;
        datas[0][1] = 2.1;
        datas[0][2] = 1.2;
        datas[0][3] = 2.2;

        datas[1][0] = 1.1;
        datas[1][1] = 2.1;
        datas[1][2] = 1.2;
        datas[1][3] = 2.2;

        factorAnalysis = new FactorAnalysis.Builder().setMeanZ(meanZ).setMeanX(meanX).setVar11(varZZ).setVar12(varZX)
                .setVar22(varXX).setDatas(generateData()).build();
        //
        // Matrix inputMatrix = new Matrix(datas);
        // factorAnalysis = new
        // FactorAnalysis.Builder().setMeanZ(meanZ).setMeanX(meanX).setVar11(varZZ).setVar12(varZX)
        // .setVar22(varXX).setDatas(inputMatrix).build();
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

        Matrix generateData = generateData();

        try {
            Matrix point0 = MatrixUtils.getMatrixColumn(generateData, 0);
            Matrix meanMatrix = ((Matrix) ReflectUtil.getClassMemberMethod(this.factorAnalysis,
                    "updateMeanZConditionXi").invoke(this.factorAnalysis, point0));
            MatrixUtils.printMatrix(meanMatrix);
            Assert.assertNotNull(meanMatrix);

        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {

            e.printStackTrace();
            fail("exception happened");
        }

    }

    @Test
    public void testupdateSigmaZConditionXi() {

        // Matrix generateData = generateData();

        try {
            // Matrix point0 = MatrixUtils.getMatrixColumn(generateData, 0);
            Matrix meanMatrix = ((Matrix) ReflectUtil.getClassMemberMethod(this.factorAnalysis,
                    "updateSigmaZConditionXi").invoke(this.factorAnalysis, null));

        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {

            e.printStackTrace();
        }

    }

    @Test
    public void testTrainOnce() {

        // Matrix generateData = generateData();
        try {
            // Matrix point0 = MatrixUtils.getMatrixColumn(generateData, 0);
            Matrix meanMatrix = ((Matrix) ReflectUtil.getClassMemberMethod(this.factorAnalysis, "trainOnce").invoke(
                    this.factorAnalysis));
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTrain() {

        this.factorAnalysis.train();

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
        A[0][0] = 3.;
        A[1][0] = 1.;
        Matrix lambda = new Matrix(A);

        double[][] errMean = new double[2][1];
        errMean[0][0] = 0.;
        errMean[1][0] = 0.;
        Matrix mu1 = new Matrix(errMean);

        double[][] errVar = new double[2][2];
        errVar[0][0] = 1.;
        errVar[0][1] = 0.;
        errVar[1][0] = 0.;
        errVar[1][1] = 1.;
        Matrix sigma1 = new Matrix(errVar);;

        ISampler<Vector<Double>> sampler = new TwoDimGaussSampler(mu1, sigma1);
        sampler.doSample();
        Queue<Vector<Double>> datas = sampler.getSampleValues();

        int datalength = datas.size();

        Matrix result = new Matrix(2, datalength);

        for (int i = 0; i < datalength; i++) {

            Matrix pointOfMatrix = MatrixUtils.getPointOfMatrix(datas.poll());
            MatrixUtils.setMatrixColumn(result, pointOfMatrix, i);
        }

        // GnuPlotDisplay.display2D(result.transpose().getArray());

        int length = 100;
        Matrix sample = new Matrix(2, length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {

            Matrix error = MatrixUtils.getMatrixColumn(result, i);
            double z = random.nextGaussian();
            Matrix point = lambda.times(z).plus(error);

            MatrixUtils.setMatrixColumn(sample, point, i);

        }

        // GnuPlotDisplay.display2D(sample.transpose().getArray());

        return sample;

    }
}

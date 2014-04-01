package com.sample.cluster;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.probablity.utils.MatrixUtils;
import com.probablity.utils.ReflectUtil;

import Jama.Matrix;

public class PcaAnalysisTest {

    private PcaAnalysis pcaAnalysis = null;

    @Before
    public void setUp() throws Exception {

        double[][] A = new double[2][2];
        A[0][0] = 1.;
        A[0][1] = 0.5;
        A[1][0] = 0.5;
        A[1][1] = 1;
        Matrix data = new Matrix(A);
        Matrix input = Matrix.random(4, 10);
        pcaAnalysis = new PcaAnalysis.Builder().setData(input).build();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testPCA() {

        pcaAnalysis.process();
        Matrix output = pcaAnalysis.getOutput();
        Assert.assertNotNull(output);
    }

    @Test
    public void testPcaWithSVD() {

        pcaAnalysis.processWithSVD();
        Matrix output = pcaAnalysis.getOutput();
        Assert.assertNotNull(output);
    }

    @Test
    public void testPreProcess() {

        Method preProcess = ReflectUtil.getClassMemberMethod(pcaAnalysis, "preProcess");

        try {
            preProcess.invoke(pcaAnalysis);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    @Test
    @Ignore
    public void test() {
        fail("Not yet implemented");
    }

}

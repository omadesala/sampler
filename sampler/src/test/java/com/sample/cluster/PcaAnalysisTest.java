package com.sample.cluster;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import Jama.Matrix;

public class PcaAnalysisTest {

    private PcaAnalysis pcaAnalysis = null;

    @Before
    public void setUp() throws Exception {

        double[][] A = new double[2][2];
        A[0][0] = 4;
        A[0][1] = 1;
        A[1][0] = 1;
        A[1][1] = 1;
        Matrix data = new Matrix(A);
        pcaAnalysis = new PcaAnalysis.Builder().setData(data).build();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testPCA() {

        pcaAnalysis.process();
    }

    @Test
    @Ignore
    public void test() {
        fail("Not yet implemented");
    }

}

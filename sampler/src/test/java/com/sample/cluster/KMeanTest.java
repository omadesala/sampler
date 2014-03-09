package com.sample.cluster;

import java.util.List;
import java.util.Vector;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.probablity.utils.CollectionUtils;
import com.probablity.utils.MatrixUtils;
import com.sample.ui.MixGaussianSample;

import Jama.Matrix;

public class KMeanTest {

    private KMean kmean = null;
    private Matrix input = null;

    @Before
    public void setUp() {

        MixGaussianSample mix = new MixGaussianSample();
        mix.start();

        List<Vector<Double>> samples = CollectionUtils
                .getList(mix.getSamples());
        input = MatrixUtils.getMatrix(samples);

        // cluster is 3 and data dimesion is 2.
        kmean = new KMean(3, 2);

    }

    @Test
    @Ignore
    public void testStepE() {

        kmean.init(input);
        int num = kmean.getClusterNumber();
        kmean.stepE();
        Matrix getiPointBelongClusterK = kmean.getiPointBelongClusterK();

        for (int i = 0; i < getiPointBelongClusterK.getColumnDimension(); i++) {
            Double rowMatrixElementAt = MatrixUtils.getRowMatrixElementAt(
                    getiPointBelongClusterK, i);
            Assert.assertTrue(rowMatrixElementAt >= 0
                    && rowMatrixElementAt < num);
        }

    }

    @Test
    public void Train() {

        this.kmean.init(input);
        this.kmean.train();
        
        
    }
}

package com.sample.cluster;

import java.util.List;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import Jama.Matrix;

import com.probablity.utils.CollectionUtils;
import com.probablity.utils.MatrixUtils;
import com.sample.ui.MixGaussianSample;

public class FirstKmeanAfterGMM {

    private KMean kmean = null;
    private MixGanssianEM gmm = null;

    private Matrix input = null;
    private int componentNum;
    private int dataDimension;

    @Before
    public void setUp() {
        MixGaussianSample mix = new MixGaussianSample();
        mix.start();

        List<Vector<Double>> samples = CollectionUtils
                .getList(mix.getSamples());
        this.input = MatrixUtils.getMatrix(samples);

        this.componentNum = 3;
        this.dataDimension = 2;
        this.kmean = new KMean(componentNum, dataDimension);

    }

    @Test
    public void trainFirstKmeanAfterGMM() {

        this.kmean.init(this.input);
        this.kmean.train();
        this.gmm = new MixGanssianEM(this.kmean.getMean(),
                this.kmean.getCovariance(), this.componentNum);

        this.gmm.train(this.input);
        this.gmm.printInfo();
    }
}

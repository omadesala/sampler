package com.sample.classify;

import java.util.List;
import java.util.Vector;

import org.junit.Test;

import Jama.Matrix;

import com.probablity.utils.CollectionUtils;
import com.probablity.utils.MatrixUtils;
import com.sample.ui.MixGaussianSample;

public class MixGanssianEMTest {

    MixGanssianEM mixGanssianEM = new MixGanssianEM();

    @Test
    public void testTrain() {

        MixGaussianSample mix = new MixGaussianSample();

        mix.start();

        System.out.println("print samples mix: ");
        List<Vector<Double>> samples = CollectionUtils.getList(mix.getSamples());

        Matrix matrix = MatrixUtils.getMatrix(samples);
        System.out.println("print  mix row: " + matrix.getRowDimension());
        System.out.println("print  mix col: " + matrix.getColumnDimension());

        mixGanssianEM.train(matrix);
    }

}

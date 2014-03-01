package com.sample.sampler.implement.inverse;

import java.util.List;
import java.util.Queue;
import java.util.Vector;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import Jama.Matrix;

import com.probablity.utils.CollectionUtils;
import com.probablity.utils.GnuPlotDisplay;
import com.probablity.utils.MatrixUtils;
import com.sample.sampler.ISampler;

public class TwoDimGaussSamplerTest {

    ISampler<Vector<Double>> sampler = null;

    @Before
    public void setUp() throws Exception {

        sampler = new TwoDimGaussSampler();
        sampler.doSample();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetSampleValues() {

        System.out.println("testGetSampleValues");
        Queue<Vector<Double>> sampleValues = sampler.getSampleValues();
        Assert.assertEquals(sampler.getSamplePointNum(), sampleValues.size());

        List<Vector<Double>> list = CollectionUtils.getList(sampleValues);
        Matrix matrix = MatrixUtils.getMatrix(list);
        Assert.assertEquals(sampler.getSamplePointNum(),
                matrix.getColumnDimension());

        GnuPlotDisplay.display2D(CollectionUtils.getColumnRowArray(matrix));
    }
}

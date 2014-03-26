package com.sample.cluster;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import Jama.Matrix;

import com.probablity.utils.ReflectUtil;

public class FactorAnalysisTest {

    private FactorAnalysis factorAnalysis;

    @Before
    public void setUp() throws Exception {

        Matrix mean1 = Matrix.random(1, 1);
        Matrix mean2 = Matrix.random(1, 1);;
        Matrix var11 = Matrix.random(1, 1);;
        Matrix var12 = Matrix.random(1, 1);;
        Matrix var22 = Matrix.random(1, 1);;
        factorAnalysis = new FactorAnalysis.Builder().setMean1(mean1).setMean2(mean2).setVar11(var11).setVar12(var12)
                .setVar22(var22).build();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testUpdateMean() {

        Method updateMean = ReflectUtil.getClassMemberMethod(this.factorAnalysis, "updateMean");

        try {
            @SuppressWarnings("unused")
            Object invoke = updateMean.invoke(this.factorAnalysis, null);
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

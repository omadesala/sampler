/*
 *  $URL: https://athena.redprairie.com/svn/prod/devtools/trunk/bootstrap/eclipse/codetemplates.xml $
 *  $Author: mlange $
 *  $Date: 2009-06-19 11:49:22 +0800 (Fri, 19 Jun 2009) $
 *
 *  $Copyright-Start$
 *
 *  Copyright (c) 2014
 *  RedPrairie Corporation
 *  All Rights Reserved
 *
 *  This software is furnished under a corporate license for use on a
 *  single computer system and can be copied (with inclusion of the
 *  above copyright) only for use on such a system.
 *
 *  The information in this document is subject to change without notice
 *  and should not be construed as a commitment by RedPrairie Corporation.
 *
 *  RedPrairie Corporation assumes no responsibility for the use of the
 *  software described in this document on equipment which has not been
 *  supplied or approved by RedPrairie Corporation.
 *
 *  $Copyright-End$
 */

package com.sample.distribution.implement;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.sample.distribution.Distribution;

public class GaussDistributionTest {

    private Distribution distribution = new GaussDistribution();

    @Test
    public void testDensityFunction() {

        // Distribution proposal = new GaussDistribution();
        distribution.setMean(0);
        distribution.setVariance(0.05);

        ArrayList<Double> result = Lists.newArrayList();
        for (int i = 0; i < 100; i++) {
            result.add(distribution.densityFunction(0.2 * i));
            System.out.println("i== " + i + "value: " + result.get(i));
        }

    }

    @Test
    public void testSampleOnePoint() {
        fail("Not yet implemented");
    }

}

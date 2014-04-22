package com.cs.omp;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OMPTest {

    private OMP omp;

    @Before
    public void setUp() throws Exception {
        this.omp = new OMP();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testProcess() {
        omp.process();
    }

    @Test
    public void testGetMod() {
        fail("Not yet implemented");
    }

}

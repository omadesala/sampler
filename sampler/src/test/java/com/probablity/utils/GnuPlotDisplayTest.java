package com.probablity.utils;

import org.junit.Test;

public class GnuPlotDisplayTest {

    @Test(expected = NullPointerException.class)
    public void testDisplay3DNullInput() {
        GnuPlotDisplay.display3D(null);
    }

    @Test
    public void testDisplay3D() {
        GnuPlotDisplay.display3D(new double[2][2]);
    }

}

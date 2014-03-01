package com.probablity.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Vector;

import Jama.Matrix;

import com.google.common.base.Preconditions;
import com.panayotis.gnuplot.GNUPlotParameters;
import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.plot.DataSetPlot;

public final class GnuPlotDisplay {

    private GnuPlotDisplay() {

    }

    public static void display2D(double[][] datas) {

        GNUPlotParameters param = new GNUPlotParameters(false);

        ArrayList<String> preInit = param.getPreInit();

        // preInit.add("set contour base");// draw contour
        preInit.add("set xrange [-5:5]");// draw contour
        preInit.add("set yrange [-5:5]");// draw contour
        preInit.add("set size square");// draw contour

        JavaPlot p = new JavaPlot(param, Constant.GNUPLOT_PATH, null);

        p.setTitle("two dim gaussian Sample Demo");
        p.getAxis("x").setLabel("X1 axis", "Arial", 20);
        p.getAxis("y").setLabel("X2 axis");

        System.out.println("point num " + datas.length);

        DataSetPlot s = new DataSetPlot(datas);
        p.addPlot(s);

        p.addPlot("0; pause 1000;");
        p.plot();
    }

    public static void display3D(double[][] samples) {

        Preconditions.checkNotNull(samples);
        GNUPlotParameters param = new GNUPlotParameters(false);

        ArrayList<String> preInit = param.getPreInit();

        preInit.add("set contour base");// draw contour
        preInit.add("set xrange [-5:5]");
        preInit.add("set yrange [-5:5]");
        // preInit.add("set size square");

        preInit.add("set surface");
        preInit.add("set isosamples 50");
        // preInit.add("set view 25,20");

        JavaPlot p = new JavaPlot(param, Constant.GNUPLOT_PATH, null);

        p.setTitle("two dim gaussian Sample Demo");
        p.getAxis("x").setLabel("X1 axis", "Arial", 20);
        p.getAxis("y").setLabel("X2 axis");
        p.newGraph3D();
        p.addPlot(samples);

        // double[][] plot3d = { { 1, 1.1, 3 }, { 2, 2.2, 3 }, { 3, 3.3, 3.4 },
        // { 4, 4.3, 5 }, };
        // p.addPlot(plot3d);

        // p.newGraph3D();
        // p.addPlot("sin(x)*sin(y); pause 1000;");

        p.newGraph3D();
        p.addPlot("0; pause 1000;");
        p.plot();
    }

}

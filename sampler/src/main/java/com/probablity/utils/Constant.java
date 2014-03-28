package com.probablity.utils;

public class Constant {

    public static String GNUPLOT_PATH = "E:/gnuplot/bin/gnuplot.exe";
    // public static final String GNUPLOT_PATH = "/usr/bin/gnuplot";
    static {

        String os = System.getProperty("os.name").toLowerCase();
        if (os.startsWith("linux")) {
            GNUPLOT_PATH = "/usr/bin/gnuplot";
        } else if (os.startsWith("win")) {
            GNUPLOT_PATH = "E:/gnuplot/bin/gnuplot.exe";
        }
    }
}

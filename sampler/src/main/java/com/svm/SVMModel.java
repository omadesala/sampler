package com.svm;

public class SVMModel {
    public double a[];
    public double b;
    public int y[];

    public SVMModel(double[] a, int y[], double b) {
        super();
        this.a = a;
        this.b = b;
        this.y = y;
    }
}

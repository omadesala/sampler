package com.probablity.utils;

import static org.junit.Assert.fail;

import java.util.List;
import java.util.Vector;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import Jama.Matrix;

public class MatrixUtilsTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetPointOfMatrix() {

        Vector<Double> input = new Vector<Double>();

        input.addElement(1.1);
        input.addElement(2.1);
        input.addElement(3.1);

        Matrix pointOfMatrix = MatrixUtils.getPointOfMatrix(input);

        int rowDimension = pointOfMatrix.getRowDimension();
        int columnDimension = pointOfMatrix.getColumnDimension();

        Assert.assertEquals(3, rowDimension);
        Assert.assertEquals(1, columnDimension);

        MatrixUtils.printMatrix(pointOfMatrix);

    }

    @Test(expected = NullPointerException.class)
    public void testGetPointOfMatrixNullInput() {

        MatrixUtils.getPointOfMatrix(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetPointOfMatrixEmpty() {

        Vector<Double> input = new Vector<Double>();
        MatrixUtils.getPointOfMatrix(input);

    }

    @Test
    public void testGetListVector() {

        // 1.1 1.2 1.3
        // 2.1 2.2 2.3
        // 3.1 3.2 3.3

        double[][] a = new double[3][3];
        a[0][0] = 1.1;
        a[0][1] = 1.2;
        a[0][2] = 1.3;

        a[1][0] = 2.1;
        a[1][1] = 2.2;
        a[1][2] = 2.3;

        a[2][0] = 3.1;
        a[2][1] = 3.2;
        a[2][2] = 3.3;

        Matrix input = new Matrix(a);

        MatrixUtils.printMatrix(input);

        List<Vector<Double>> listVector = MatrixUtils.getListVector(input);

        Assert.assertEquals(3, listVector.size());
        Vector<Double> vector = listVector.get(1);

        Assert.assertEquals(1.2, vector.get(0).doubleValue(), 0.0001);
        Assert.assertEquals(2.2, vector.get(1).doubleValue(), 0.0001);
        Assert.assertEquals(3.2, vector.get(2).doubleValue(), 0.0001);

    }

    @Test
    public void testGetMatrixColumn() {

        double[][] a = new double[3][3];
        a[0][0] = 1.1;
        a[0][1] = 1.2;
        a[0][2] = 1.3;

        a[1][0] = 2.1;
        a[1][1] = 2.2;
        a[1][2] = 2.3;

        a[2][0] = 3.1;
        a[2][1] = 3.2;
        a[2][2] = 3.3;

        Matrix input = new Matrix(a);
        Matrix matrixColumn = MatrixUtils.getMatrixColumn(input, 0);
        MatrixUtils.printMatrix(matrixColumn);

        Assert.assertEquals(1.1, matrixColumn.get(0, 0), 0.00001);
        Assert.assertEquals(2.1, matrixColumn.get(1, 0), 0.00001);
        Assert.assertEquals(3.1, matrixColumn.get(2, 0), 0.00001);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMatrixColumnInvalidIndex() {

        double[][] a = new double[3][3];
        a[0][0] = 1.1;
        a[0][1] = 1.2;
        a[0][2] = 1.3;

        a[1][0] = 2.1;
        a[1][1] = 2.2;
        a[1][2] = 2.3;

        a[2][0] = 3.1;
        a[2][1] = 3.2;
        a[2][2] = 3.3;

        Matrix input = new Matrix(a);
        MatrixUtils.getMatrixColumn(input, 3);

    }

    @Test(expected = NullPointerException.class)
    public void testGetMatrixColumnNullMatrix() {

        MatrixUtils.getMatrixColumn(null, 3);
    }

    @Test
    public void testGetMatrixRow() {

        double[][] a = new double[3][3];
        a[0][0] = 1.1;
        a[0][1] = 1.2;
        a[0][2] = 1.3;

        a[1][0] = 2.1;
        a[1][1] = 2.2;
        a[1][2] = 2.3;

        a[2][0] = 3.1;
        a[2][1] = 3.2;
        a[2][2] = 3.3;

        Matrix input = new Matrix(a);
        Matrix matrixRow = MatrixUtils.getMatrixRow(input, 1);

        System.out.println("testGetMatrixRow");
        MatrixUtils.printMatrix(matrixRow);

        Assert.assertEquals(2.1, matrixRow.get(0, 0), 0.00001);
        Assert.assertEquals(2.2, matrixRow.get(0, 1), 0.00001);
        Assert.assertEquals(2.3, matrixRow.get(0, 2), 0.00001);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMatrixRowInvalidIndex() {

        double[][] a = new double[3][3];
        a[0][0] = 1.1;
        a[0][1] = 1.2;
        a[0][2] = 1.3;

        a[1][0] = 2.1;
        a[1][1] = 2.2;
        a[1][2] = 2.3;

        a[2][0] = 3.1;
        a[2][1] = 3.2;
        a[2][2] = 3.3;

        MatrixUtils.getMatrixRow(new Matrix(a), 3);

    }

    @Test(expected = NullPointerException.class)
    public void testGetMatrixRowNullMatrix() {
        MatrixUtils.getMatrixRow(null, 0);
    }

    @Test
    public void testGetSumOfMatrixRow() {
        double[][] a = new double[1][3];
        a[0][0] = 1.1;
        a[0][1] = 1.2;
        a[0][2] = 1.3;

        Double actual = MatrixUtils.getSumOfMatrixRow(new Matrix(a));
        Assert.assertEquals(3.6, actual, 0.00001);
    }

    @Test(expected = NullPointerException.class)
    public void testGetSumOfMatrixRowNullMatrix() {

        MatrixUtils.getSumOfMatrixRow(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSumOfMatrixRowWithColumMatrix() {

        double[][] a = new double[3][1];
        a[0][0] = 1.1;
        a[1][0] = 1.2;
        a[2][0] = 1.3;

        MatrixUtils.getSumOfMatrixRow(new Matrix(a));

    }

    @Test
    @Ignore
    public void testGetSumOfMatrixColumn() {
        fail("Not yet implemented");
    }

    @Test
    @Ignore
    public void testGetMatrixColumnElementAt() {
        fail("Not yet implemented");
    }

    @Test
    @Ignore
    public void testGetMatrixRowElementAt() {
        fail("Not yet implemented");
    }

    @Test
    @Ignore
    public void testPrintMatrix() {
        fail("Not yet implemented");
    }

}

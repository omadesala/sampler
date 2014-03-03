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

        System.out.println("testGetPointOfMatrix");
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

        System.out.println("testGetPointOfMatrixNullInput");

        MatrixUtils.getPointOfMatrix(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetPointOfMatrixEmpty() {

        System.out.println("testGetPointOfMatrixEmpty");
        Vector<Double> input = new Vector<Double>();
        MatrixUtils.getPointOfMatrix(input);

    }

    @Test
    public void testGetListVector() {

        System.out.println("testGetListVector");
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
    public void testGetMatrix() {

        System.out.println("testGetMatrix");
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

        Matrix matrix = MatrixUtils.getMatrix(listVector);

        Assert.assertEquals(3, matrix.getRowDimension());
        Assert.assertEquals(3, matrix.getColumnDimension());

        MatrixUtils.printMatrix(matrix);

    }

    @Test
    public void testGetMatrixColumn() {

        System.out.println("testGetMatrixColumn");
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

        System.out.println("testGetMatrixColumnInvalidIndex");
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

        System.out.println("testGetMatrixColumnNullMatrix");
        MatrixUtils.getMatrixColumn(null, 3);
    }

    @Test
    public void testGetMatrixRow() {

        System.out.println("testGetMatrixRow");
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

        MatrixUtils.printMatrix(matrixRow);

        Assert.assertEquals(2.1, matrixRow.get(0, 0), 0.00001);
        Assert.assertEquals(2.2, matrixRow.get(0, 1), 0.00001);
        Assert.assertEquals(2.3, matrixRow.get(0, 2), 0.00001);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMatrixRowInvalidIndex() {

        System.out.println("testGetMatrixRowInvalidIndex");
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
        System.out.println("testGetMatrixRowNullMatrix");
        MatrixUtils.getMatrixRow(null, 0);
    }

    @Test
    public void testGetSumOfMatrixRow() {
        System.out.println("testGetSumOfMatrixRow");
        double[][] a = new double[1][3];
        a[0][0] = 1.1;
        a[0][1] = 1.2;
        a[0][2] = 1.3;

        Double actual = MatrixUtils.getSumOfMatrixRow(new Matrix(a));
        Assert.assertEquals(3.6, actual, 0.00001);
    }

    @Test(expected = NullPointerException.class)
    public void testGetSumOfMatrixRowNullMatrix() {

        System.out.println("testGetSumOfMatrixRowNullMatrix");
        MatrixUtils.getSumOfMatrixRow(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSumOfMatrixRowWithColumMatrix() {

        System.out.println("testGetSumOfMatrixRowWithColumMatrix");
        double[][] a = new double[3][1];
        a[0][0] = 1.1;
        a[1][0] = 1.2;
        a[2][0] = 1.3;

        MatrixUtils.getSumOfMatrixRow(new Matrix(a));

    }

    @Test
    public void testGetSumOfMatrixColumn() {

        System.out.println("testGetSumOfMatrixColumn");
        double[][] a = new double[3][1];
        a[0][0] = 1.1;
        a[1][0] = 1.2;
        a[2][0] = 1.3;

        Double sumOfMatrixColumn = MatrixUtils.getSumOfMatrixColumn(new Matrix(a));

        Assert.assertEquals(3.6, sumOfMatrixColumn, 0.00001);

    }

    @Test(expected = NullPointerException.class)
    public void testGetSumOfMatrixColumnNullInput() {

        System.out.println("testGetSumOfMatrixColumnNullInput");
        MatrixUtils.getSumOfMatrixColumn(null);
    }

    @Test
    public void testgetRowMatrixElementAt() {

        System.out.println("testgetRowMatrixElementAt");

        double[][] a = new double[1][3];
        a[0][0] = 1.1;
        a[0][1] = 1.2;
        a[0][2] = 1.3;

        Double column0 = MatrixUtils.getRowMatrixElementAt(new Matrix(a), 0);
        Assert.assertEquals(1.1, column0, 0.00001);

        Double column1 = MatrixUtils.getRowMatrixElementAt(new Matrix(a), 1);
        Assert.assertEquals(1.2, column1, 0.00001);

        Double column2 = MatrixUtils.getRowMatrixElementAt(new Matrix(a), 2);
        Assert.assertEquals(1.3, column2, 0.00001);

    }

    @Test(expected = NullPointerException.class)
    public void testgetRowMatrixElementAtNullInput() {

        System.out.println("testgetRowMatrixElementAtNullInput");
        MatrixUtils.getRowMatrixElementAt(null, 0);

    }

    @Test(expected = IllegalStateException.class)
    public void testgetRowMatrixElementAtInvalidIndex() {

        System.out.println("testgetRowMatrixElementAtInvalidIndex");

        double[][] a = new double[1][3];
        a[0][0] = 1.1;
        a[0][1] = 1.2;
        a[0][2] = 1.3;

        Matrix matrix = new Matrix(a);

        MatrixUtils.getRowMatrixElementAt(matrix, 4);

    }

    @Test
    public void testgetColumnMatrixElementAt() {

        System.out.println("testgetColumnMatrixElementAt");

        double[][] a = new double[3][1];
        a[0][0] = 1.1;
        a[1][0] = 1.2;
        a[2][0] = 1.3;

        Matrix input = new Matrix(a);

        Double row0 = MatrixUtils.getColumnMatrixElementAt(input, 0);
        Double row1 = MatrixUtils.getColumnMatrixElementAt(input, 1);
        Double row2 = MatrixUtils.getColumnMatrixElementAt(input, 2);

        Assert.assertEquals(1.1, row0, 0.00001);
        Assert.assertEquals(1.2, row1, 0.00001);
        Assert.assertEquals(1.3, row2, 0.00001);

    }

    @Test(expected = NullPointerException.class)
    public void testgetColumnMatrixElementAtNullInput() {

        System.out.println("testgetColumnMatrixElementAtNullInput");
        MatrixUtils.getColumnMatrixElementAt(null, 0);

    }

    @Test(expected = IllegalStateException.class)
    public void testgetColumnMatrixElementAtInvalidIndex() {

        System.out.println("testgetColumnMatrixElementAtInvalidIndex");

        double[][] a = new double[3][1];
        a[0][0] = 1.1;
        a[1][0] = 1.2;
        a[2][0] = 1.3;

        Matrix input = new Matrix(a);

        MatrixUtils.getColumnMatrixElementAt(input, 5);

    }

    @Test
    public void testMatrixIsColumnIsTrue() {

        System.out.println("testMatrixIsColumn");

        double[][] a = new double[3][1];
        a[0][0] = 1.1;
        a[1][0] = 1.2;
        a[2][0] = 1.3;

        boolean column = MatrixUtils.isColumn(new Matrix(a));
        Assert.assertEquals(true, column);

    }

    @Test
    public void testMatrixIsRowIsTrue() {

        System.out.println("testMatrixIsRowIsTrue");

        double[][] a = new double[1][3];
        a[0][0] = 1.1;
        a[0][1] = 1.2;
        a[0][2] = 1.3;

        boolean isRow = MatrixUtils.isRow(new Matrix(a));
        Assert.assertEquals(true, isRow);

    }

    @Test
    public void testMatrixIsColumnFalse() {

        System.out.println("testMatrixIsColumnFalse");

        double[][] a = new double[1][3];
        a[0][0] = 1.1;
        a[0][1] = 1.2;
        a[0][2] = 1.3;

        boolean column = MatrixUtils.isColumn(new Matrix(a));
        Assert.assertEquals(false, column);

        double[][] b = new double[2][2];
        b[0][0] = 1.1;
        b[0][1] = 1.2;
        b[1][0] = 1.3;
        b[1][1] = 1.3;

        column = MatrixUtils.isColumn(new Matrix(b));
        Assert.assertEquals(false, column);

    }

    @Test
    public void testMatrixIsRowFalse() {

        System.out.println("testMatrixIsRowFalse");

        double[][] a = new double[3][1];
        a[0][0] = 1.1;
        a[1][0] = 1.2;
        a[2][0] = 1.3;

        boolean isRow = MatrixUtils.isRow(new Matrix(a));
        Assert.assertEquals(false, isRow);

        double[][] b = new double[2][2];
        b[0][0] = 1.1;
        b[0][1] = 1.2;
        b[1][0] = 1.3;
        b[1][1] = 1.3;

        isRow = MatrixUtils.isColumn(new Matrix(b));
        Assert.assertEquals(false, isRow);

    }

    @Test(expected = NullPointerException.class)
    public void testMatrixIsColumnNullInput() {

        System.out.println("testMatrixIsColumnNullInput");
        MatrixUtils.isColumn(null);

    }

    @Test(expected = NullPointerException.class)
    public void testMatrixIsRowNullInput() {

        System.out.println("testMatrixIsRowNullInput");
        MatrixUtils.isRow(null);

    }

    @Test
    public void testrowMultiColumn() {

        System.out.println("testMatrixIsRowNullInput");

        double[][] row = new double[1][3];
        row[0][0] = 1.1;
        row[0][1] = 1.2;
        row[0][2] = 1.3;
        double[][] col = new double[3][1];
        col[0][0] = 1.1;
        col[1][0] = 1.2;
        col[2][0] = 1.3;

        Matrix rowMatrix = new Matrix(row);
        Matrix colMatrix = new Matrix(col);
        Double prod = MatrixUtils.rowMultiColumn(rowMatrix, colMatrix);

        Assert.assertEquals(4.34, prod, 0.00001);
    }

    @Test
    public void testPrintVectorPoint() {

        Vector<Double> point = new Vector<Double>();
        point.add(0.1);
        point.add(0.2);
        point.add(0.3);

        Long dimension = MatrixUtils.printVectorPoint(point);

        Assert.assertEquals(3, dimension, 0.00001);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintVectorPointNullInput() {

        MatrixUtils.printVectorPoint(null);
    }

    @Test
    @Ignore
    public void testPrintMatrix() {
        fail("Not yet implemented");
    }

}

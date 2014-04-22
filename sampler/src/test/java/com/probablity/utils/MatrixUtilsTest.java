package com.probablity.utils;

import static org.junit.Assert.fail;

import java.util.List;
import java.util.Vector;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.linear.FieldMatrix;
import org.jscience.mathematics.vector.ComplexMatrix;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import Jama.Matrix;

import com.google.common.collect.Lists;

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

        List<Vector<Double>> listVector = MatrixUtils.getListVector(input);

        Assert.assertEquals(3, listVector.size());
        Vector<Double> vector = listVector.get(1);

        Assert.assertEquals(1.2, vector.get(0).doubleValue(), 0.0001);
        Assert.assertEquals(2.2, vector.get(1).doubleValue(), 0.0001);
        Assert.assertEquals(3.2, vector.get(2).doubleValue(), 0.0001);

    }

    @Test
    public void testGetMatrix() {

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

        List<Vector<Double>> listVector = MatrixUtils.getListVector(input);

        Matrix matrix = MatrixUtils.getMatrix(listVector);

        Assert.assertEquals(3, matrix.getRowDimension());
        Assert.assertEquals(3, matrix.getColumnDimension());

    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMatrixWithEmpty() {
        List<Vector<Double>> listVector = Lists.newArrayList();
        MatrixUtils.getMatrix(listVector);
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

        Assert.assertEquals(1.1, matrixColumn.get(0, 0), 0.00001);
        Assert.assertEquals(2.1, matrixColumn.get(1, 0), 0.00001);
        Assert.assertEquals(3.1, matrixColumn.get(2, 0), 0.00001);

    }

    @Test
    public void testSetMatrixColumn() {

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

        Matrix column = new Matrix(3, 1);
        column.set(0, 0, 2.6);
        column.set(1, 0, 2.7);
        column.set(2, 0, 2.8);

        Matrix matrixColumn = MatrixUtils.setMatrixColumn(input, column, 0);
        // MatrixUtils.printMatrix(matrixColumn);

        Assert.assertEquals(2.6, matrixColumn.get(0, 0), 0.00001);
        Assert.assertEquals(2.7, matrixColumn.get(1, 0), 0.00001);
        Assert.assertEquals(2.8, matrixColumn.get(2, 0), 0.00001);

        matrixColumn = MatrixUtils.setMatrixColumn(input, column, 1);
        // MatrixUtils.printMatrix(matrixColumn);

        Assert.assertEquals(2.6, matrixColumn.get(0, 1), 0.00001);
        Assert.assertEquals(2.7, matrixColumn.get(1, 1), 0.00001);
        Assert.assertEquals(2.8, matrixColumn.get(2, 1), 0.00001);

        matrixColumn = MatrixUtils.setMatrixColumn(input, column, 3);
        // matrixColumn = MatrixUtils.setMatrixColumn(input, column, 2);
        // MatrixUtils.printMatrix(matrixColumn);

        Assert.assertEquals(2.6, matrixColumn.get(0, 2), 0.00001);
        Assert.assertEquals(2.7, matrixColumn.get(1, 2), 0.00001);
        Assert.assertEquals(2.8, matrixColumn.get(2, 2), 0.00001);

    }

    @Test
    public void testSetMatrixRow() {

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

        Matrix row = new Matrix(1, 3);
        row.set(0, 0, 2.6);
        row.set(0, 1, 2.7);
        row.set(0, 2, 2.8);

        Matrix matrixRow = MatrixUtils.setMatrixRow(input, row, 0);
        // MatrixUtils.printMatrix(matrixColumn);

        Assert.assertEquals(2.6, matrixRow.get(0, 0), 1e-5);
        Assert.assertEquals(2.7, matrixRow.get(0, 1), 1e-5);
        Assert.assertEquals(2.8, matrixRow.get(0, 2), 1e-5);

        matrixRow = MatrixUtils.setMatrixRow(input, row, 1);
        // MatrixUtils.printMatrix(matrixColumn);

        Assert.assertEquals(2.6, matrixRow.get(1, 0), 1e-5);
        Assert.assertEquals(2.7, matrixRow.get(1, 1), 1e-5);
        Assert.assertEquals(2.8, matrixRow.get(1, 2), 1e-5);

        matrixRow = MatrixUtils.setMatrixRow(input, row, 2);

        Assert.assertEquals(2.6, matrixRow.get(2, 0), 1e-5);
        Assert.assertEquals(2.7, matrixRow.get(2, 1), 1e-5);
        Assert.assertEquals(2.8, matrixRow.get(2, 2), 1e-5);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMatrixRowInvalidIndex() {

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

        Matrix row = new Matrix(1, 3);
        row.set(0, 0, 2.6);
        row.set(0, 1, 2.7);
        row.set(0, 2, 2.8);

        Matrix matrixRow = MatrixUtils.setMatrixRow(input, row, 3);
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
    public void testGetSumOfMatrixColumn() {

        double[][] a = new double[3][1];
        a[0][0] = 1.1;
        a[1][0] = 1.2;
        a[2][0] = 1.3;

        Double sumOfMatrixColumn = MatrixUtils.getSumOfMatrixColumn(new Matrix(a));

        Assert.assertEquals(3.6, sumOfMatrixColumn, 0.00001);

    }

    @Test(expected = NullPointerException.class)
    public void testGetSumOfMatrixColumnNullInput() {

        MatrixUtils.getSumOfMatrixColumn(null);
    }

    @Test
    public void testgetRowMatrixElementAt() {

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

        MatrixUtils.getRowMatrixElementAt(null, 0);

    }

    @Test(expected = IllegalStateException.class)
    public void testgetRowMatrixElementAtInvalidIndex() {

        double[][] a = new double[1][3];
        a[0][0] = 1.1;
        a[0][1] = 1.2;
        a[0][2] = 1.3;

        Matrix matrix = new Matrix(a);

        MatrixUtils.getRowMatrixElementAt(matrix, 4);

    }

    @Test
    public void testgetColumnMatrixElementAt() {

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

        MatrixUtils.getColumnMatrixElementAt(null, 0);

    }

    @Test(expected = IllegalStateException.class)
    public void testgetColumnMatrixElementAtInvalidIndex() {

        double[][] a = new double[3][1];
        a[0][0] = 1.1;
        a[1][0] = 1.2;
        a[2][0] = 1.3;

        Matrix input = new Matrix(a);

        MatrixUtils.getColumnMatrixElementAt(input, 5);

    }

    @Test
    public void testMatrixIsColumnIsTrue() {

        double[][] a = new double[3][1];
        a[0][0] = 1.1;
        a[1][0] = 1.2;
        a[2][0] = 1.3;

        boolean column = MatrixUtils.isColumn(new Matrix(a));
        Assert.assertEquals(true, column);

    }

    @Test
    public void testMatrixIsRowIsTrue() {

        double[][] a = new double[1][3];
        a[0][0] = 1.1;
        a[0][1] = 1.2;
        a[0][2] = 1.3;

        boolean isRow = MatrixUtils.isRow(new Matrix(a));
        Assert.assertEquals(true, isRow);

    }

    @Test
    public void testMatrixIsColumnFalse() {

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

        MatrixUtils.isColumn(null);

    }

    @Test(expected = NullPointerException.class)
    public void testMatrixIsRowNullInput() {

        MatrixUtils.isRow(null);

    }

    @Test
    public void testrowMultiColumn() {

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

    @Test(expected = IllegalArgumentException.class)
    public void getCovarianceMatrixNullInput() {
        MatrixUtils.getCovarianceMatrix(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getCovarianceMatrixEmptyInput() {
        MatrixUtils.getCovarianceMatrix(new Matrix(0, 0));
    }

    @Test
    public void getCovarianceMatrix() {

        double[][] b = new double[2][4];
        b[0][0] = 1;
        b[0][1] = 2;
        b[0][2] = 3;
        b[0][3] = 4;

        b[1][0] = 4;
        b[1][1] = 3;
        b[1][2] = 2;
        b[1][3] = 1;

        Matrix matrix = new Matrix(b);

        MatrixUtils.printMatrix(matrix);

        Matrix covarianceMatrix = MatrixUtils.getCovarianceMatrix(matrix);
        Assert.assertNotNull(covarianceMatrix);
        Assert.assertEquals(2, covarianceMatrix.getRowDimension());
        Assert.assertEquals(2, covarianceMatrix.getColumnDimension());

        System.out.println("print covariance matrix");
        MatrixUtils.printMatrix(covarianceMatrix);
        System.out.println("print covariance matrix end");

    }

    @Test
    public void isEmptyOrNull() {

        boolean result = MatrixUtils.isEmptyOrNull(null);
        Assert.assertTrue(result);
        result = MatrixUtils.isEmptyOrNull(new Matrix(0, 0));
        Assert.assertTrue(result);
        result = MatrixUtils.isEmptyOrNull(Matrix.random(1, 1));
        Assert.assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void isSquareMatrixNullInput() {
        MatrixUtils.isSquareMatrix(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void isSquareMatrixEmptyInput() {
        Matrix input = Matrix.random(0, 0);
        MatrixUtils.isSquareMatrix(input);
    }

    @Test
    public void isSquareMatrix() {
        Matrix input = Matrix.random(3, 3);
        boolean isSquare = MatrixUtils.isSquareMatrix(input);
        Assert.assertTrue(isSquare);
    }

    @Test
    public void matrixIsEmpty() {

        Matrix inputMatrix = new Matrix(2, 2);
        boolean result = MatrixUtils.isZeroMatrix(inputMatrix);
        Assert.assertTrue(result);
        inputMatrix = Matrix.random(2, 2);
        result = MatrixUtils.isZeroMatrix(inputMatrix);
        Assert.assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void matrixIsEmptyInvalidInput() {
        MatrixUtils.isZeroMatrix(null);
    }

    @Test
    public void testgetUnitMatrix() {

        Matrix input = Matrix.random(2, 2);
        Matrix unit = MatrixUtils.getUnitMatrix(input);
        Assert.assertNotNull(unit);
        Assert.assertEquals(1., unit.get(0, 0), 1e-5);
        Assert.assertEquals(1., unit.get(1, 1), 1e-5);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testgetUnitMatrixNullInput() {
        MatrixUtils.getUnitMatrix(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testgetUnitMatrixNotSquareMatrix() {

        MatrixUtils.getUnitMatrix(Matrix.random(2, 3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintMatrixInfoNullInput() {
        Matrix input = null;
        MatrixUtils.printMatrixInfo(input, "test");

    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintMatrixInfoNullMessage() {
        Matrix input = Matrix.random(2, 3);
        MatrixUtils.printMatrixInfo(input, null);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testgetMatrixMeanNullInput() {
        MatrixUtils.getMatrixMean(null);
    }

    @Test
    public void testgetMatrixMean() {

        double[][] a = new double[2][2];
        a[0][0] = 1;
        a[0][1] = 3;
        a[1][0] = 2;
        a[1][1] = 6;

        Matrix matrixMean = MatrixUtils.getMatrixMean(new Matrix(a));
        Assert.assertEquals(2, MatrixUtils.getColumnMatrixElementAt(matrixMean, 0), 1e-5);
        Assert.assertEquals(4, MatrixUtils.getColumnMatrixElementAt(matrixMean, 1), 1e-5);
    }

    @Test
    public void testComplex() {

        Complex c = Complex.valueOf(1., 1.);
        double magnitude = c.abs();
        System.out.println("abs: " + magnitude);

        Assert.assertEquals(Math.sqrt(2.), magnitude, 1e-5);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testRealMatrixToComplexMatrix() {

        // Matrix real = new Matrix(2, 2);
        Matrix real = null;

        FieldMatrix<Complex> complex = MatrixUtils.toComplex(real);
        MatrixUtils.printMatrix(complex);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintComplexMatrix() {

        FieldMatrix<Complex> complexMatrix = null;
        MatrixUtils.printMatrix(complexMatrix);

    }

    @Test
    public void testMatrixFFT() {

        Matrix data = null;
        ComplexMatrix comp = MatrixUtils.fft(data);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testgetMatrixColumnAsArray() {

        Matrix data = null;
        int index = 0;
        MatrixUtils.getColumnAsArray(data, index);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testgetMatrixRowAsArray() {

        Matrix data = null;
        int index = 0;
        MatrixUtils.getRowAsArray(data, index);
    }

    @Test
    @Ignore
    public void testPrintMatrix() {
        fail("Not yet implemented");
    }

}

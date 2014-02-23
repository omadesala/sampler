package com.sample.distribution.implement;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.probablity.utils.MatrixUtils;

import Jama.Matrix;

public class MatrixTest {

    private Matrix matrix = Matrix.random(2, 10);;

    @Before
    public void setUp() {

        // matrix = Matrix.random(2, 10);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void getMatrixColumn() {

        int rowDimension = matrix.getRowDimension();
        int columnDimension = matrix.getColumnDimension();

        assertEquals(2, rowDimension);
        assertEquals(10, columnDimension);
        MatrixUtils.printMatrix(matrix);
        System.out.println("##############################");
        MatrixUtils.printMatrix(MatrixUtils.getMatrixColumn(matrix, 2));
    }

    @Test
    public void printMatrix() {

        MatrixUtils.printMatrix(matrix);
    }

    @Test
    public void printDataPoints() {

        System.out.println("printDataPoints print matrix");
        MatrixUtils.printMatrix(matrix);
        List<Vector<Double>> vector = MatrixUtils.getListVector(matrix);
        System.out.println("vector size: " + vector.size());
        for (int i = 0; i < vector.size(); i++) {
            Vector<Double> point = vector.get(i);
            System.out.println("print point " + i + "  size:  " + point.size());
            for (int j = 0; j < point.size(); j++) {
                System.out.print(" " + point.get(j) + " ");
            }
        }
    }

    @Test
    public void getMatrixColumnByUtils() {

        int rowDimension = matrix.getRowDimension();
        int columnDimension = matrix.getColumnDimension();

        assertEquals(2, rowDimension);
        assertEquals(10, columnDimension);

        MatrixUtils.getMatrixColumn(matrix, 0);
    }

    @Test
    public void getMatrixRowByUtils() {

        int rowDimension = matrix.getRowDimension();
        int columnDimension = matrix.getColumnDimension();

        assertEquals(2, rowDimension);
        assertEquals(10, columnDimension);

        Matrix matrixRow = MatrixUtils.getMatrixRow(matrix, 1);

        System.out.println("getMatrixRowByUtils: ");
        MatrixUtils.printMatrix(matrix);
        System.out.println("print matrix row");
        MatrixUtils.printMatrix(matrixRow);

    }

    @Test
    public void printMatrixColumn() {

        int rowDimension = matrix.getRowDimension();
        int columnDimension = matrix.getColumnDimension();

        assertEquals(2, rowDimension);
        assertEquals(10, columnDimension);

        Matrix matrix2 = matrix.getMatrix(0, 0, 0, columnDimension - 1);

        assertEquals(10, matrix2.getColumnDimension());
        assertEquals(1, matrix2.getRowDimension());

        // for (int i = 0; i < matrix2.getColumnDimension(); i++) {
        // System.out.println(i + "=" + matrix2.get(0, i));
        //
        // }

    }
}

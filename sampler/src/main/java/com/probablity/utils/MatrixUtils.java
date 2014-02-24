package com.probablity.utils;

import java.util.List;
import java.util.Vector;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import Jama.Matrix;

public class MatrixUtils {

    private MatrixUtils() {
    }

    /**
     * @Title: getMatrix
     * @Description: get the input point as the column matrix such as N x 1
     * @param input data point
     * @return Matrix Column is one ,and arrow is N, N is the size of input
     * @throws
     */
    public static Matrix getPointOfMatrix(Vector<Double> input) {

        Matrix matrix = new Matrix(input.size(), 1);
        for (int i = 0; i < input.size(); i++) {
            matrix.set(i, 0, input.get(i));
        }
        return matrix;
    }

    public static List<Vector<Double>> getListVector(Matrix input) {

        Preconditions.checkNotNull(input);

        int rowDimension = input.getRowDimension();
        int columnDimension = input.getColumnDimension();

        Preconditions.checkState(rowDimension > 0 && columnDimension > 0);

        List<Vector<Double>> datas = Lists.newArrayList();

        for (int col = 0; col < columnDimension; col++) {

            Vector<Double> point = new Vector<Double>();
            for (int row = 0; row < rowDimension; row++) {
                point.add(input.get(row, col));
            }
            datas.add(point);
        }

        return datas;
    }

    /**
     * @Title: getMatrixColumn
     * @Description: get the k-th column form input matrix
     * @param input input matrix
     * @param columnIndex column index
     * @return Matrix 返回类型
     * @throws
     */
    public static Matrix getMatrixColumn(Matrix input, int columnIndex) {

        int columnNumber = input.getColumnDimension();
        int rowNumber = input.getRowDimension();

        Preconditions.checkNotNull(input);
        Preconditions.checkArgument(columnIndex >= 0
                && columnIndex < columnNumber);

        return input.getMatrix(0, rowNumber - 1, columnIndex, columnIndex);

    }

    public static Matrix getMatrixRow(Matrix input, int rowIndex) {

        int columnNumber = input.getColumnDimension();
        int rowNumber = input.getRowDimension();

        Preconditions.checkNotNull(input);
        Preconditions.checkArgument(rowIndex >= 0 && rowIndex < rowNumber);

        return input.getMatrix(rowIndex, rowIndex, 0, columnNumber - 1);

    }

    public static Double getSumOfMatrixRow(Matrix row) {

        Preconditions.checkNotNull(row);
        Preconditions.checkArgument(row.getColumnDimension() > 1
                && row.getRowDimension() == 1);

        Double sum = 0.;
        for (int i = 0; i < row.getColumnDimension(); i++) {
            sum += row.get(0, i);
        }

        return sum;

    }

    public static Double getSumOfMatrixColumn(Matrix col) {

        Preconditions.checkNotNull(col);
        Preconditions.checkArgument(col.getRowDimension() > 1
                && col.getColumnDimension() == 1);

        Double sum = 0.;
        for (int i = 0; i < col.getRowDimension(); i++) {
            sum += col.get(i, 0);
        }

        return sum;

    }

    public static Double getMatrixColumnElementAt(Matrix col, Integer index) {

        Preconditions.checkNotNull(col);
        Preconditions.checkState(col.getRowDimension() == 1
                && col.getColumnDimension() > 1
                && (index >= 0 && index < col.getColumnDimension()));

        Double element = col.get(0, index);

        return element;

    }

    public static Double getMatrixRowElementAt(Matrix row, Integer index) {

        Preconditions.checkNotNull(row);
        Preconditions.checkState(row.getRowDimension() > 1
                && row.getColumnDimension() == 1
                && (index >= 0 && index < row.getColumnDimension()));

        Double element = row.get(index, 0);

        return element;

    }

    public static void printMatrix(Matrix input) {

        int columnNumber = input.getColumnDimension();
        int rowNumber = input.getRowDimension();

        for (int i = 0; i < rowNumber; i++) {
            for (int j = 0; j < columnNumber; j++) {
                System.out.print("  " + input.get(i, j) + "  ");
            }
            System.out.println("  ");
        }

    }

}

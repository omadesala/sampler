package com.probablity.utils;

import java.util.List;
import java.util.Vector;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;

import Jama.Matrix;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

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

        Preconditions.checkState(!input.isEmpty());

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

    public static Double getRowMatrixElementAt(Matrix rowMat, Integer index) {

        Preconditions.checkNotNull(rowMat);
        Preconditions.checkState(rowMat.getRowDimension() == 1
                && rowMat.getColumnDimension() > 1
                && (index >= 0 && index < rowMat.getColumnDimension()));

        Double element = rowMat.get(0, index);

        return element;

    }

    public static Double getColumnMatrixElementAt(Matrix colMat, Integer index) {

        Preconditions.checkNotNull(colMat);
        Preconditions.checkState(colMat.getRowDimension() > 1
                && colMat.getColumnDimension() == 1
                && (index >= 0 && index < colMat.getRowDimension()));

        Double element = colMat.get(index, 0);

        return element;

    }

    public static void printMatrix(Matrix input) {

        Preconditions.checkArgument(input != null, "input shoud not be null");
        Preconditions.checkArgument(
                input.getRowDimension() > 0 && input.getColumnDimension() > 0,
                "input shoud not be empty");

        int row = input.getRowDimension();
        int col = input.getColumnDimension();

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                System.out.print("  " + input.get(i, j) + "  ");
            }
            System.out.println("  ");
        }

    }

    public static boolean isColumn(Matrix input) {

        Preconditions.checkNotNull(input);

        return input.getColumnDimension() == 1 && input.getRowDimension() >= 1;

    }

    public static boolean isRow(Matrix input) {

        Preconditions.checkNotNull(input);

        return input.getRowDimension() == 1 && input.getColumnDimension() >= 1;

    }

    public static Double rowMultiColumn(Matrix row, Matrix col) {

        Preconditions.checkArgument(MatrixUtils.isRow(row)
                && MatrixUtils.isColumn(col));

        return row.times(col).get(0, 0);

    }

    public static Matrix getMatrix(List<Vector<Double>> listVector) {

        Preconditions.checkNotNull(listVector);

        Preconditions.checkArgument(listVector.size() > 0);

        Vector<Double> point1 = listVector.get(0);
        int length = listVector.size();
        int dimension = point1.size();

        Matrix resultMatrix = new Matrix(dimension, length);

        for (int i = 0; i < length; i++) {
            Matrix pointOfMatrix = MatrixUtils.getPointOfMatrix(listVector
                    .get(i));

            resultMatrix.setMatrix(0, dimension - 1, i, i, pointOfMatrix);
        }

        return resultMatrix;
    }

    public static Long printVectorPoint(Vector<Double> point) {

        Preconditions.checkArgument(point != null);

        Long dim = 0l;

        for (int i = 0; i < point.size(); i++) {
            dim++;

            System.out.println("" + point.get(i));
        }

        return dim;

    }

    public static Matrix setMatrixColumn(Matrix input, Matrix column,
                                         int insetIndex) {

        Preconditions.checkNotNull(input, "input matrix should not be null");
        Preconditions.checkNotNull(column, "column matrix should not be null");

        Preconditions.checkArgument(MatrixUtils.isColumn(column),
                "the column is not a column matrix ");
        Preconditions.checkArgument(
                input.getRowDimension() == column.getRowDimension(),
                "column row size not equal input matrix row");
        Preconditions.checkArgument(
                insetIndex >= 0 && insetIndex < input.getColumnDimension(),
                "the index is not correct");
        // Preconditions.checkArgument(insetIndex >= 0 && insetIndex <=
        // column.getRowDimension());

        input.setMatrix(0, input.getRowDimension() - 1, insetIndex, insetIndex,
                column);

        return input;

    }

    /**
     * @Title: getCovarianceMatrix
     * @Description: return the covariance matrix
     * @param @param matrix col is the number of data ,row is the dimension of
     *        each data point
     * @param @return
     * @return Matrix ��������
     * @throws
     */
    public static Matrix getCovarianceMatrix(Matrix matrix) {

        Preconditions.checkArgument(matrix != null, "input should not be null");

        Preconditions
                .checkArgument(
                        matrix.getColumnDimension() > 0
                                && matrix.getRowDimension() > 0,
                        "input should not be empty matrix");

        Covariance covariance = new Covariance(matrix.transpose().getArray());
        RealMatrix covarianceMatrix = covariance.getCovarianceMatrix();
        return new Matrix(covarianceMatrix.getData());

    }

    public static boolean isEmptyOrNull(Matrix inputMatrix) {

        if (inputMatrix == null) {
            return true;
        }
        if (inputMatrix.getRowDimension() == 0
                && inputMatrix.getColumnDimension() == 0) {
            return true;
        }
        return false;
    }

    public static boolean isSquareMatrix(Matrix input) {

        Preconditions.checkArgument(!MatrixUtils.isEmptyOrNull(input),
                "input should not be null or empty");

        if (input.getColumnDimension() == input.getRowDimension()) {
            return true;
        }
        return false;
    }

    public static boolean isZeroMatrix(Matrix inputMatrix) {
        Preconditions.checkArgument(!MatrixUtils.isEmptyOrNull(inputMatrix));

        for (int i = 0; i < inputMatrix.getRowDimension(); i++) {
            for (int j = 0; j < inputMatrix.getColumnDimension(); j++) {
                if (inputMatrix.get(i, j) != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public static Matrix getUnitMatrix(Matrix input) {

        Preconditions.checkArgument(input != null, "input should not be null");
        Preconditions.checkArgument(MatrixUtils.isSquareMatrix(input),
                "input should  be square matrix");

        Matrix result = new Matrix(input.getRowDimension(),
                input.getColumnDimension());

        for (int i = 0; i < input.getColumnDimension(); i++) {
            result.set(i, i, 1.);
        }

        return result;

    }

    public static void printMatrixInfo(Matrix input, Object errorMessage) {

        Preconditions.checkArgument(input != null, "input  should not be null");
        Preconditions.checkArgument(errorMessage != null,
                "msg  should not be null");

        String valueOf = String.valueOf(errorMessage);
        System.out.println(valueOf);
        System.out.println("row: " + input.getRowDimension());
        System.out.println("col: " + input.getColumnDimension());

    }
}

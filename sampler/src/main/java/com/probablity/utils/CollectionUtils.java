package com.probablity.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import Jama.Matrix;

import com.google.common.base.Preconditions;

public class CollectionUtils {

    public static <T> List<T> getList(Queue<T> queue) {

        Preconditions.checkArgument(queue != null, "the input is null ,please set the queue first");
        Preconditions.checkArgument(queue.size() > 0, "the input is empty ,please set the queue first");

        List<T> ret = new ArrayList<T>();
        for (T t : queue) {
            ret.add(t);
        }
        return ret;
    }

    public static double[][] getColumnRowArray(Matrix input) {

        Preconditions.checkNotNull(input);

        int rowDimension = input.getRowDimension();
        int columnDimension = input.getColumnDimension();

        double[][] ret = new double[columnDimension][rowDimension];

        for (int i = 0; i < columnDimension; i++) {
            for (int j = 0; j < rowDimension; j++) {
                ret[i][j] = input.get(j, i);
            }
        }
        return ret;
    }
}

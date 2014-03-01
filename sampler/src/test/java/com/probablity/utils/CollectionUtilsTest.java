package com.probablity.utils;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import Jama.Matrix;

public class CollectionUtilsTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetList() {

        int count = 100;
        Queue<Integer> queue = new ArrayBlockingQueue<Integer>(count);
        for (Integer i = 0; i < count; i++) {
            queue.add(i);
        }

        List<Integer> list = CollectionUtils.getList(queue);

        Assert.assertEquals(count, list.size());

    }

    @Test
    public void testgetColumnRowArray() {

        System.out.println("testgetColumnRowArray");
        double[][] data = new double[2][3];

        // 1 2 3
        // 4 5 6
        data[0][0] = 1.1;
        data[1][0] = 1.2;

        data[0][1] = 2.1;
        data[1][1] = 2.2;

        data[0][2] = 3.1;
        data[1][2] = 3.2;

        Matrix m = new Matrix(data);

        double[][] columnRowArray = CollectionUtils.getColumnRowArray(m);

        Matrix retMatrix = new Matrix(columnRowArray);

        Assert.assertEquals(2, retMatrix.getColumnDimension());
        Assert.assertEquals(3, retMatrix.getRowDimension());

    }

}

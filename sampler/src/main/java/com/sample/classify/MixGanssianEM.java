package com.sample.classify;

import java.util.ArrayList;
import java.util.List;

import Jama.Matrix;

import com.sample.distribution.Distribution;
import com.sample.distribution.implement.MVNDistribution;

/**
 * 
 * @ClassName: MixGanssianEM
 * @Description: this calss is use EM (expected maximise) to estimate the
 *               parameter of mixtured gaussian model
 * @author omadesala@msn.com
 * @date 2014-1-29 上午9:41:46
 * 
 */
public class MixGanssianEM {

	private List<Matrix> mean = new ArrayList<Matrix>();
	private List<Matrix> var = new ArrayList<Matrix>();;
	private Matrix component;
	private Double epson = 1e-6;

	MixGanssianEM() {

		this.mean.add(new Matrix(2, 1));
		this.mean.add(new Matrix(2, 1));
		this.mean.add(new Matrix(2, 1));

		this.var.add(new Matrix(2, 2));
		this.var.add(new Matrix(2, 2));
		this.var.add(new Matrix(2, 2));

		this.component = new Matrix(1, 3);

	}

	MixGanssianEM(List<Matrix> mu, List<Matrix> var, Matrix component) {

		this.mean = mu;
		this.var = var;
		this.component = component;
	}

	public void EM() {

		InitialContext();

		// the times is just for test
		// the convergence condition need optimal
		for (int i = 0; i < 1000; i++) {
			E();
			M();
		}

	}

	private void M() {

	}

	/**
	 * 
	 * @Title: E
	 * @Description: calcute the x point is sample from k component
	 * @param
	 * @return void 返回类型
	 * @throws
	 */
	private void E() {

		// for each component

		for (int i = 0; i < component.getColumnDimension(); i++) {

			// P(c_k|x_i)
			Distribution mvn = new MVNDistribution(mean.get(i), var.get(i));

		}

		//
		// Double pck_xi =

	}

	private void InitialContext() {

		/**
		 * initial component
		 */
		int componentDimension = component.getColumnDimension();
		for (int k = 0; k < componentDimension; k++) {
			component.set(1, k, 1. / componentDimension);
		}

		/**
		 * initial mean vector
		 */
		int length = mean.size();
		for (int k = 0; k < length; k++) {

			Matrix matrix = mean.get(k);

			for (int i = 0; i < matrix.getRowDimension(); i++) {
				matrix.set(i, 0, 1);
			}
		}

		/**
		 * initial variance matrix
		 */
		for (int k = 0; k < length; k++) {

			Matrix matrix = var.get(k);
			for (int i = 0; i < matrix.getRowDimension(); i++) {
				for (int j = 0; j < matrix.getColumnDimension(); j++) {
					matrix.set(i, j, 1.);
				}
			}
		}
	}
}

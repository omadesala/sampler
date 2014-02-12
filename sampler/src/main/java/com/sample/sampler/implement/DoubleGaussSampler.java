package com.sample.sampler.implement;

import java.util.Vector;

import com.sample.distribution.Distribution;
import com.sample.distribution.implement.GaussDistribution;
import com.sample.distribution.implement.MixGaussDistribution;
import com.sample.sampler.ISampler;

/**
 * just only implement ISampler can builder all kinds of distribution.</br>
 * <p>
 * first, implement a target distribution.
 * </p>
 * <p>
 * second,implement the proposal distribution.
 * </p>
 * <p>
 * third,choose the constant a for sampler
 * </p>
 */
public class DoubleGaussSampler extends ISampler<Vector<Double>> {

	static final long serialVersionUID = 1L;

	public DoubleGaussSampler() {
		super();
		setTargetDistribution(new MixGaussDistribution());
		Distribution proposalDistribution = new GaussDistribution();
		setProposalDistribution(proposalDistribution);
		// setProposalDistribution(new UniformDistribution());
	}

	@Override
	public void doSample() {

		int k = 0;
		while (k < getSamplePointNum()) {

			Double a = calculateAlpha();
			Vector<Double> x = getProposalDistribution().sampleOnePoint();
			// a uniform variable is simulated
			Double u = Math.random();
			// acceptance or rejection

			if (u < getTargetDistribution().densityFunction(x)
					/ (a * getProposalDistribution().densityFunction(x))) {

				Vector<Double> dataItem = new Vector<Double>();
				dataItem.add(10. * (x.firstElement() + 5.));

				getSampleValues().add(dataItem);
			}
			k++;
		}

		System.out.println("accept num:" + getSampleValues().size());

	}

	/**
	 * this is the constant for accept-reject sample.
	 * 
	 * @return
	 */
	private double calculateAlpha() {
		double a = 10. / Math.sqrt(2. * Math.PI);
		return a;
	}

}
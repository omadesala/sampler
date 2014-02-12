package com.sample.sampler.implement;

import java.util.Iterator;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;

import com.sample.distribution.Distribution;
import com.sample.distribution.implement.GaussDistribution;
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
public class GaussMHSampler extends ISampler<Vector<Double>> {

	static final long serialVersionUID = 1L;

	public GaussMHSampler() {
		super();

		// setTargetDistribution(new StandardGaussDistribution());
		Distribution targetDistribution = new GaussDistribution();
		targetDistribution.setMean(2);
		targetDistribution.setVariance(1);

		setTargetDistribution(targetDistribution);

		/**
		 * about the proposal distribution, the guass distribution will have
		 * different efficient vary different vairance. in this sample,
		 * 0.05,0.5,5 will have different result.
		 */
		Distribution proposal = new GaussDistribution();
		proposal.setMean(0);
		proposal.setVariance(0.8);
		setProposalDistribution(proposal);
	}

	@Override
	public void doSample() {

		burnIn();
		Handler();
	}

	private void Handler() {

		Queue<Vector<Double>> sampleValues = getSampleValues();
		Queue<Vector<Double>> resultDoubles = new ArrayBlockingQueue<Vector<Double>>(
				getSamplePointNum());
		Iterator<Vector<Double>> iterator = sampleValues.iterator();

		while (iterator.hasNext()) {

			Vector<Double> dataItem = iterator.next();
			Double data = 10. * dataItem.firstElement() + 5.;

			Vector<Double> newData = new Vector<Double>();
			newData.add(data);

			resultDoubles.add(newData);
		}

		setSampleValues(resultDoubles);

	}

	private void burnIn() {

		/**
		 * first, set the x0 to a initial value;
		 */

		Queue<Vector<Double>> sampleValues = getSampleValues();

		Vector<Double> x0 = new Vector<Double>();
		x0.add(0.5);

		sampleValues.add(x0);

		for (int i = 0; i < 250000; i++) {

			/**
			 * get the next point according to proposal distribution.
			 */

			Distribution proposalDistribution = getProposalDistribution();

			proposalDistribution.setMean(x0.firstElement());

			Vector<Double> x_t = new Vector<Double>();
			x_t.add(proposalDistribution.sampleOnePoint().firstElement());

			Double p_xt = getTargetDistribution().densityFunction(x_t);
			Double p_x0 = getTargetDistribution().densityFunction(x0);

			Double accept_ratio = Math.min(1, p_xt / p_x0);

			Double uniform = Math.random();

			if (uniform < accept_ratio) {

				if (!sampleValues.offer(x_t)) {
					// queue is full, remove head and re-add it.
					sampleValues.remove();
					sampleValues.offer(x_t);
				}

				x0 = x_t;
			} else {
				if (!sampleValues.offer(x0)) {
					// queue is full, remove head and re-add it.
					sampleValues.remove();
					sampleValues.offer(x0);
				}
			}

		}

	}
}
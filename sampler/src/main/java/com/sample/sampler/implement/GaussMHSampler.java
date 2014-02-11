package com.sample.sampler.implement;

import java.util.Iterator;
import java.util.Queue;
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
public class GaussMHSampler extends ISampler<Double> {

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

        Queue<Double> sampleValues = getSampleValues();
        Queue<Double> resultDoubles = new ArrayBlockingQueue<Double>(
            getSamplePointNum());
        Iterator<Double> iterator = sampleValues.iterator();

        while (iterator.hasNext()) {
            resultDoubles.add(10. * (iterator.next() + 5.));
        }

        setSampleValues(resultDoubles);

    }

    private void burnIn() {

        /**
         * first, set the x0 to a initial value;
         */

        Queue<Double> sampleValues = getSampleValues();

        Double x0 = 0.5;
        sampleValues.add(x0);

        for (int i = 0; i < 250000; i++) {

            /**
             * get the next point according to proposal distribution.
             */

            Distribution proposalDistribution = getProposalDistribution();
            proposalDistribution.setMean(x0);

            double x_t = proposalDistribution.sampleOnePoint();

            double p_xt = getTargetDistribution().densityFunction(x_t);
            double p_x0 = getTargetDistribution().densityFunction(x0);

            double accept_ratio = Math.min(1, p_xt / p_x0);

            double uniform = Math.random();

            if (uniform < accept_ratio) {

                if (!sampleValues.offer(x_t)) {
                    // queue is full, remove head and re-add it.
                    sampleValues.remove();
                    sampleValues.offer(x_t);
                }

                x0 = x_t;
            }
            else {
                if (!sampleValues.offer(x0)) {
                    // queue is full, remove head and re-add it.
                    sampleValues.remove();
                    sampleValues.offer(x0);
                }
            }

        }

    }
}
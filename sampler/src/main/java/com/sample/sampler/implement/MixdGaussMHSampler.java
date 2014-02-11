package com.sample.sampler.implement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
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
public class MixdGaussMHSampler extends ISampler<Double> {

    static final long serialVersionUID = 1L;
    private Random random = new Random();
    ArrayList<Double> values = Lists.newArrayList();

    public MixdGaussMHSampler() {
        super();
        setTargetDistribution(new MixGaussDistribution());

        /**
         * about the proposal distribution, the guass distribution will have
         * different efficient with different vairance. in this sample, user can
         * try different variance to get the effective
         */
        Distribution proposal = new GaussDistribution();
        proposal.setMean(0);
        proposal.setVariance(50);
        setProposalDistribution(proposal);
    }

    @Override
    public void doSample() {

        burnIn();

        Handler();

    }

    /**
     * assume that the value is belong to interval [-5,5] and we display 100
     * rectangle , to calculate the number in each rectangle, we expand interval
     * to [0,100] ([-5]+5)*10->[0] ,([ 5]+5)*10->[100],because of the width is
     * 500 pixels, so each rectangle width is 5 pixels.
     */
    private void Handler() {

        Queue<Double> resultDoubles = new ArrayBlockingQueue<Double>(
            getSamplePointNum());
        Iterator<Double> iterator = getSampleValues().iterator();

        while (iterator.hasNext()) {

            Double next = iterator.next();
            double e = 10. * (next + 5.);
            resultDoubles.add(e);
        }

        setSampleValues(resultDoubles);

        System.out.println("list size:" + values.size());

        List<Double> subList = values.subList(values.size()
                - getSamplePointNum(), values.size() - 1);
        ArrayList<Double> newArrayList = Lists.newArrayList();
        // for test

        for (Double pt : subList) {
            newArrayList.add(10. * (pt + 5.));
        }
        Queue<Double> queue = Queues.newArrayDeque();
        queue.addAll(newArrayList);
        setSampleValues(queue);
    }

    private void burnIn() {

        /**
         * first, set the x0 to a initial value;
         */

        Queue<Double> sampleValues = getSampleValues();

        Double curValue = 0.5;
        sampleValues.add(curValue);

        for (int i = 0; i < 50000; i++) {

            /**
             * get the next point according to proposal distribution.
             */

            Distribution proposalDistribution = getProposalDistribution();
            proposalDistribution.setMean(curValue);

            double nextValue = proposalDistribution.sampleOnePoint();

            double pdf_nextPoint = getTargetDistribution().densityFunction(
                nextValue);
            double pdf_curPoint = getTargetDistribution().densityFunction(
                curValue);

            double accept_ratio = Math.min(1, pdf_nextPoint / pdf_curPoint);

            double uniform = random.nextDouble();

            if (uniform < accept_ratio) {
                /**
                 * accept the new status
                 */
                curValue = nextValue;

            }

            values.add(curValue);

            /**
             * save data
             */
            if (i < getSamplePointNum()) {
                sampleValues.offer(curValue);
            }
            else {
                // queue is full, remove head and re-add it.
                sampleValues.remove();
                sampleValues.offer(curValue);

            }

        }

    }
}
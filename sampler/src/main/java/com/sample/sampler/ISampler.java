package com.sample.sampler;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import com.sample.distribution.Distribution;

public abstract class ISampler<T> {

    // sample parameter
    private int samplePointNum = 1000; // number of points, the sample point
    protected int maxValue = 0; // maximum of the distribution values
    // private List<T> sampleValues = new LinkedList<T>();
    private Queue<T> sampleValues = new ArrayBlockingQueue<T>(samplePointNum);

    private Distribution targetDistribution = null;
    private Distribution proposalDistribution = null;

    /**
     * 
     * @Description: sample
     * @param 参数描述
     * @throws
     */
    public abstract void doSample();

    public Distribution getTargetDistribution() {
        return targetDistribution;
    }

    public void setTargetDistribution(Distribution targetDistribution) {
        this.targetDistribution = targetDistribution;
    }

    public Distribution getProposalDistribution() {
        return proposalDistribution;
    }

    public Queue<T> getSampleValues() {

        return sampleValues;
    }

    public void setProposalDistribution(Distribution proposalDistribution) {
        this.proposalDistribution = proposalDistribution;
    }

    public int getSamplePointNum() {
        return samplePointNum;
    }

    public void setSamplePointNum(int samplePointNum) {
        this.samplePointNum = samplePointNum;
    }

    public void setSampleValues(Queue<T> sampleValues) {
        this.sampleValues = sampleValues;
    }

}

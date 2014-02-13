package com.sample.distribution.implement;

import java.util.Random;

import com.sample.distribution.Distribution;

/**
 * the target distribution is gauss distribution f(x)=
 * 1/(sqrt(2*pi)*variance)exp(-(x-mean)^2/(2*variance^2))
 */
public class GaussDistribution extends Distribution {

    private Random random = new Random();

    @Override
    public double densityFunction(double... x) {

        double constant = 1 / (Math.sqrt(2. * Math.PI) * variance);

        return constant
                * Math.exp(-(x[0] - mean) * (x[0] - mean)
                        / (2. * variance * variance));
    }

    @Override
    public double sampleOnePoint(double... x) {

        /**
         * there are some mess code problem.
         */
        /**
         * X��N(��,��^2),��Y=(X-��)/�ҡ�N(0,1).
         * <p>
         * proof: because X��N(��,��^2), so
         * P(x)=(2��)^(-1/2)*��^(-1)*exp{[-(x-��)^2]/(2��^2)}. (note��F(y)is cfd
         * about Y��Fx(x)is cfd about X��
         * <p>
         * F(y)=P(Y��y)=P((X-��)/�ҡ�y) =P(X�ܦ�y+��)=Fx(��y+��) so,
         * p(y)=F'(y)=F'x(��y+��)*��=P(��y+��)*�� =[(2��)^(-1/2)]*e^[-��x^2)/2].
         * proof finish��Y��N(0,1).
         * 
         * </p>
         * <p>
         * for get the correct distribution from N(0,1),according to
         * (X-��)/�ҡ�N(0,1), so we can get the x~N(0,1), x*��+��~N(��,��^2)
         * 
         * F(y)=P(Y��y)=P(x*��+�̡�y) = P(X��(y-��)/��)=Fx((y-��)/��)
         * p(y)=F'(y)=F'x((y-��)/��)*((y-��)/��)' = P((y-��)/��)*(1/��)
         * 
         * because,P(x)=[(2��)^(-1/2)]*e^[-��x^2)/2]
         * 
         * P((y-��)/��)=(2��)^(-1/2)]*e^[-((y-��)/��)^2)/2]
         * =(2��)^(-1/2)]*e^[-(y-��)^2/)/2*��^2]
         * 
         * P((y-��)/��)*(1/��) =(2��)^(-1/2)]*e^[-(y-��)^2/)/2��^2]*(1/��)
         * =(2��)^(-1/2)*��^(-1)*e^[-(x-��)^2]/(2��^2)]
         * 
         * </p>
         * 
         */
        return random.nextGaussian() * variance + mean;

    }
}

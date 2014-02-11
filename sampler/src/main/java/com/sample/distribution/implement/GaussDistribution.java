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
         * X‵N(米,考^2),寀Y=(X-米)/考‵N(0,1).
         * <p>
         * proof: because X‵N(米,考^2), so
         * P(x)=(2羽)^(-1/2)*考^(-1)*exp{[-(x-米)^2]/(2考^2)}. (noteㄩF(y)is cfd
         * about YㄛFx(x)is cfd about Xㄘ
         * <p>
         * F(y)=P(Y≒y)=P((X-米)/考≒y) =P(X≒考y+米)=Fx(考y+米) so,
         * p(y)=F'(y)=F'x(考y+米)*考=P(考y+米)*考 =[(2羽)^(-1/2)]*e^[-ㄗx^2)/2]. proof
         * finishㄛY‵N(0,1).
         *
         * </p>
         * <p>
         * for get the correct distribution from N(0,1),according to
         * (X-米)/考‵N(0,1), so we can get the x~N(0,1), x*考+米~N(米,考^2)
         *
         * F(y)=P(Y≒y)=P(x*考+米≒y) = P(X≒(y-米)/考)=Fx((y-米)/考)
         * p(y)=F'(y)=F'x((y-米)/考)*((y-米)/考)' = P((y-米)/考)*(1/考)
         *
         * because,P(x)=[(2羽)^(-1/2)]*e^[-ㄗx^2)/2]
         *
         * P((y-米)/考)=(2羽)^(-1/2)]*e^[-((y-米)/考)^2)/2]
         * =(2羽)^(-1/2)]*e^[-(y-米)^2/)/2*考^2]
         *
         * P((y-米)/考)*(1/考) =(2羽)^(-1/2)]*e^[-(y-米)^2/)/2考^2]*(1/考)
         * =(2羽)^(-1/2)*考^(-1)*e^[-(x-米)^2]/(2考^2)]
         *
         * </p>
         *
         */
        return random.nextGaussian() * variance + mean;

    }
}

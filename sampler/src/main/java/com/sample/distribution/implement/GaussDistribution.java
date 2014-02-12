package com.sample.distribution.implement;

import java.util.Random;
import java.util.Vector;

import com.google.common.base.Preconditions;
import com.sample.distribution.Distribution;

/**
 * the target distribution is gauss distribution f(x)=
 * 1/(sqrt(2*pi)*variance)exp(-(x-mean)^2/(2*variance^2))
 */
public class GaussDistribution extends Distribution {

	private Random random = new Random();

	@Override
	public double densityFunction(Vector<Double> x) {

		double constant = 1 / (Math.sqrt(2. * Math.PI) * variance);

		return constant
				* Math.exp(-(x.firstElement() - mean)
						* (x.firstElement() - mean)
						/ (2. * variance * variance));
	}

	@Override
	public Vector<Double> sampleOnePoint(double... x) {

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

		Vector<Double> point = new Vector<Double>();
		point.add(random.nextGaussian() * variance + mean);
		return point;

	}

	/**
	 * (準 Javadoc)
	 * <p>
	 * Title: setParameter
	 * </p>
	 * <p>
	 * Description:0 for mean,1 for variance
	 * </p>
	 * 
	 * @param param
	 * @see com.sample.distribution.Distribution#setParameter(double[])
	 */
	@Override
	public void setParameter(Vector<Double> param) {
		Preconditions.checkNotNull(param);

		mean = param.firstElement();
		variance = param.elementAt(1);

	}
}

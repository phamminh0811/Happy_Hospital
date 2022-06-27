package algorithm;

public class NormalDistribution implements Distribution{
	private double _min;
	private double _max;
	private double _mean;
	private double _sd;
	private double _variance;
	private  DistributionType _type;
	private double _rng11;
	
	public NormalDistribution(double rng11, double mean, double sd) {
		this._rng11 = rng11;
		this._min = Double.NEGATIVE_INFINITY;
		this._max = Double.POSITIVE_INFINITY;
		this._mean = mean;
		this._sd = sd;
		this._variance = sd*sd;
		this._type = DistributionType.Continuous;
	}

	public double min() {
		return _min;
	}

	public double max() {
		return _max;
	}

	public double mean() {
		return _mean;
	}

	public double variance() {
		return _variance;
	}

	public DistributionType type() {
		return _type;
	}
	public double random() {
		double M = 1/(this._sd*Math.sqrt(Math.PI*2));
		double x = this._rng11 - this.mean();
		double w = Math.exp(-x*x/(2*this._variance));
		return M*w;
	}
}

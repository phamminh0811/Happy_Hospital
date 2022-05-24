package algorithm;

public class BimodalDistribution implements Distribution {
	private double _rng01;
	private double _min;
	private double _max;
	private double _mean;
	private double _variance;
	private DistributionType _type;
	private double _p;

	public BimodalDistribution(double rng01, double lambda) {
		this._rng01 = rng01;
		this._min = 0;
		this._max = Double.POSITIVE_INFINITY;
		this._mean = lambda;
		this._variance = lambda;
		this._type = DistributionType.Discrete;
		double abs = Math.abs(lambda);
		if (abs < 1 && abs != 0)
			this._p = abs;
		else if (abs == 0)
			this._p = 0.6;
		else
			this._p = 1 / abs;
	}

	public double min() {
		return this._min;
	}

	public double max() {
		return this._max;
	}

	public double mean() {
		return this._mean;
	}

	public double variance() {
		return this._variance;
	}

	public DistributionType type() {
		return this._type;
	}

	public double random() {
		double N = 3628800; // n!
		double x = Math.floor(this._rng01 * 9);
		double px = Math.pow(this._p, x);
		double qx = Math.pow(1 - this._p, 10 - x);
		double M = 1;
		for (int i = 1; i <= x; i++) {
			M = M * i * (10 - i);
		}
		return (N / M) * px * qx;
	}
}

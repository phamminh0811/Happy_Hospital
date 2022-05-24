package algorithm;

public class PoissonDistribution implements Distribution {
	private double _rng01;
	private double _min;
	private double _max;
	private double _mean;
	private double _variance;
	private DistributionType _type;
	private double _L;

	public PoissonDistribution(double rng01, double lambda) {
		this._rng01 = rng01;
		this._min = 0;
		this._max = Double.POSITIVE_INFINITY;
		this._mean = lambda;
		this._variance = lambda;
		this._type = DistributionType.Discrete;
		// Knuth's algorithm
		this._L = Math.exp(-lambda);
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

	public double random(){
        double k = 0;
        double p = 1;
        while (true) {
            // FIXME This should be [0,1] not [0,1)
            p = p * this._rng01;
            if (p <= this._L) {
                break;
            }
            k++;
        }
        return p;
    }
}

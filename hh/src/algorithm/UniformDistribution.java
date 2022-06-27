package algorithm;

public class UniformDistribution implements Distribution {
	private double _min;
	private double _max;
	private double _range;
	private double _mean;
	private double _variance;
	private double _rng01;
	private DistributionType _type;
	
	public UniformDistribution(double rng01, double min, double max) {
		this._rng01 = rng01;
		this._min = min;
		this._max = max;
		this._range = max - min;
		this._mean = min+this._range/2;
		this._variance = ((max-min)*(max-min));
		this._type = DistributionType.Continuous;
	}
	public double random() {
		return this._min+this._rng01*this._range;
	}
}

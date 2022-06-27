package algorithm;

public class ExponentialDistribution implements Distribution{
    private double _rng01;
    private double _min;
    private double _max;
    private double _mean;
    private double _variance;
    private DistributionType _type;

    public ExponentialDistribution(double rng01, double lambda) {
        this._rng01 = rng01;
        this._min = 0;
        this._max = Double.POSITIVE_INFINITY;
        this._mean = 1 / lambda;
        this._variance = Math.pow(lambda, -2);
        this._type = DistributionType.Continuous;
    }

    public double min(){
        return this._min;
    }

    public double max(){
        return this._max;
    }

    public double mean(){
        return this._mean;
    }

    public double variance(){
        return this._variance;
    }

    public DistributionType type(){
        return this._type;
    }

    public double random(){
        return -1 * Math.log(this._rng01) * this._mean;
    }
}

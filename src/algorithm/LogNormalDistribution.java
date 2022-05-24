package algorithm;

public class LogNormalDistribution implements Distribution{
    private double _rng11;
    private double _min;
    private double _max;
    private double _mean;
    private double _variance;
    private DistributionType _type;
    private NormalDistribution _nf;

    public LogNormalDistribution(double rng11, double mu, double sigma){
        this._rng11 = rng11;
        this._min = 0;
        this._max = Double.POSITIVE_INFINITY;
        this._mean = Math.exp(mu + ((sigma * sigma) / 2));
        this._variance = (Math.exp(sigma * sigma) - 1) * Math.exp(2 * mu + sigma * sigma);
        this._type = DistributionType.Continuous;
        this._nf = new NormalDistribution(rng11, mu, sigma);
    }

    double min(){
        return this._min;
    }

    double max(){
        return this._max;
    }

    double mean(){
        return this._mean;
    }

    double variance(){
        return this._variance;
    }

    DistributionType type(){
        return this._type;
    }

    public double random(){
        return Math.exp(this._nf.random());
    }
}

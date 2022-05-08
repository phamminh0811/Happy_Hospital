package algorithm.distribution;
import algorithm.distribution.Distribution;
import algorithm.distribution.DistributionType;

public class UniformDistribution extends Distribution{

    private double _min;
    private double _max;
    private double _range;
    private double _mean;
    private double _variance;
    private DistributionType _type;

    public UniformDistribution(double min, double max){
        this._min = min;
        this._max = max;
        this._range = max - min;
        this._mean = min +this._range/2;
        this._variance = ((max-min)*(max-min)) /12;
        this._type = DistributionType.CONTINUOUS;
    }
    
}

package algorithm.probts;
import algorithm.distribution.DistributionType;

public interface ProbTs{
    private double _rng01(){
        return Math.random();
    };
    private double _rng11(){
        double rng = _rng01();
        return rng;
    };

}


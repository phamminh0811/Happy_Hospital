package algorithm;

public class Prob {
	private double _rng01;
	private double _rng11;
	public Prob() {
		double rng = Math.random();
		this._rng01 = rng;

		this._rng11 = rng*((long)1<<32)/((long)1<<33)*2;
	}
	public UniformDistribution uniform(){
		double min = 0;
		double max = 1;
		return new UniformDistribution(this._rng01, min, max);
	}

	public NormalDistribution normal(){
		double mean = 0;
		double sd = 1;
		return new NormalDistribution(this._rng11, mean, sd);
	}

	public ExponentialDistribution exponential(){
		double lambda = 1;
		return new ExponentialDistribution(this._rng01, lambda);
	}

	public LogNormalDistribution logNormal(){
		double mu = 0;
		double sigma = 1;
		return new LogNormalDistribution(this._rng11, mu, sigma);
	}

	public PoissonDistribution poisson(){
		double lambda = 1;
		return new PoissonDistribution(this._rng01, lambda);
	}

	public BimodalDistribution bimodal(){
		double lambda = 1;
		return new BimodalDistribution(this._rng01, lambda);
	}
	
}

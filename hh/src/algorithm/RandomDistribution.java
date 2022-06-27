package algorithm;

public class RandomDistribution {
	private String _name;
	public double getProbability() {
		Prob prob = new Prob();
		double ran = Math.random();
		switch((int)Math.floor(ran*4)) {
		case 0:
			PoissonDistribution poisson = prob.poisson(); //Math.random();
			this._name = "Poisson";
			return poisson.random();
		case 1:
			UniformDistribution uniform = prob.uniform();
			this._name = "Uniform";
			return uniform.random();
		case 2:
			this._name = "Bimodal";
			BimodalDistribution bimodal = prob.bimodal();
			return bimodal.random();
		}
		this._name = "Normal";
        NormalDistribution normal = prob.normal();
        return normal.random();
	}
	public String getName(){
        return this._name;
    }
}

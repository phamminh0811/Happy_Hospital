package statistic;

public class WaitingDuration {
	public double begin = -1;
	public double end = -1;
	public double duration = 0;

	public WaitingDuration(double begin, double end, double duration) {
		this.begin = begin;
		this.end = end;
		this.duration = duration;
	}
}

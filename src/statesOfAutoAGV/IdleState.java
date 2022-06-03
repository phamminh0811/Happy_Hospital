package statesOfAutoAGV;

import java.time.Instant;

import application.Main;
import config.Constant;
import config.Performance;
import model.Agv;

public class IdleState extends HybridState {
	private double _start;
	private boolean _calculated;

	public IdleState(double start) {
		super();
		this._start = start;
		this._calculated = false;
	}

	public void move(Agv agv) {
		if (Performance.now() - this._start < Constant.DURATION * 1000) {
			if (!this._calculated) {
				this._calculated = true;
				double finish = this._start / 1000;
				double expectedTime = agv.getExpectedTime();
				if (finish >= expectedTime - Constant.DURATION && finish <= expectedTime + Constant.DURATION) {
					return;
				} else {
					var diff = Math.max(expectedTime - Constant.DURATION - finish,
							finish - expectedTime - Constant.DURATION);
					var lateness = Constant.getLateness(diff);
//    Main.harmfullness = mainScene.harmfullness + lateness; 
				}
			}
			return;
		} else {
            
		}

	}
}

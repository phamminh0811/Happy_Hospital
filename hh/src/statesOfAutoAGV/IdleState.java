package statesOfAutoAGV;

import java.time.Instant;

import application.Main;
import classes.Agv;
import classes.AutoAgv;
import config.Constant;
import config.Performance;

public class IdleState extends HybridState {
	private double _start;
	private boolean _calculated;

	public IdleState(double start) {
		super();
		this._start = start;
		this._calculated = false;
	}

	public void move(AutoAgv agv) {
		if (Performance.now() - this._start < Constant.DURATION * 1000) {
			if (!this._calculated) {
				this._calculated = true;
				double finish = this._start / 1000;
				double expectedTime = agv.getExpectedTime();
				Main mainScene = agv.scene;
				if (finish >= expectedTime - Constant.DURATION && finish <= expectedTime + Constant.DURATION) {
					return;
				} else {
					double diff = Math.max(expectedTime - Constant.DURATION - finish,
							finish - expectedTime - Constant.DURATION);
					double lateness = Constant.getLateness(diff);
					mainScene.setHarmfullness(mainScene.getHarmfullness() + lateness);
				}
			}
			return;
		} else {
			 if(agv != null) {
	                agv.firstText.destroy();
	                agv.eraseDeadline(agv.scene.timeTable);
	                agv.hybridState = new RunningState(true);
	                agv.changeTarget();
	            }
		}

	}
}

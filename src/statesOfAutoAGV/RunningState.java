package statesOfAutoAGV;

import config.Constant;
import config.Performance;
import model.AutoAgv;
import model.Node2D;
import model.StateOfNode2D;
import model.Vector2;

public class RunningState extends HybridState {
	public boolean _isLastMoving;
	private boolean _agvIsDestroyed;
	public RunningState(boolean isLastMoving) {
		super();
		this._isLastMoving = isLastMoving;
		this._agvIsDestroyed = false;
	}
	public RunningState() {
		super();
		this._isLastMoving = false;
		this._agvIsDestroyed = false;
	}
	public void move(AutoAgv agv) {
		if(this._agvIsDestroyed) return ;
		if(agv.path == null) return ;
		if(agv.cur == agv.path.size() - 1){
			agv.velocity = Vector2.zero;
			if(this._isLastMoving) {
				
			}else {
				agv.hybridState = new IdleState(Performance.now());
			}
			return ;
		}
		if(agv.cur + 1 >= agv.path.size()) {
            System.out.println("Loi roi do: "+ (agv.cur + 1));
        }
        Node2D nodeNext = agv.graph.nodes[(int)agv.path.elementAt((int)agv.cur+1).x][(int)agv.path.elementAt((int)agv.cur+1).y];
        double shortestDistance = Constant.minDistance(agv, agv.collidedActors);
        if (nodeNext.state == StateOfNode2D.BUSY || shortestDistance < Constant.SAFE_DISTANCE) {
            agv.velocity = Vector2.zero;
            if (agv.waitT != 0) return;
            agv.waitT = Performance.now();
//            (agv.scene as MainScene).forcasting?.
//                addDuration(agv.getAgvID(), new WaitingDuration(Math.floor(agv.waitT/1000)));
        }
	}
}

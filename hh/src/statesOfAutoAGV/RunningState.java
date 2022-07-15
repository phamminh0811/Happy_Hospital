package statesOfAutoAGV;

import application.MainScene;
import classes.AutoAgv;
import classes.Node2D;
import classes.StateOfNode2D;
import classes.Vector2;
import classes.game.Physics;
import config.Constant;
import config.Performance;
import statistic.WaitingDuration;
import static config.Config.*;

import java.util.ArrayList;

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
		if (this._agvIsDestroyed) // || this._isEliminated)
			return;
		// nếu không có đường đi đến đích thì không làm gì
		if (agv.path == null) {
			return;
		}
		// nếu đã đến đích thì không làm gì
		if (agv.cur == agv.path.size() - 1) {
			agv.setVelocity(Vector2.zero);
			if (this._isLastMoving) {
				MainScene mainScene = agv.scene;
				mainScene.autoAgvs.remove(agv);
				mainScene.forcasting.rememberDoneAutoAgv(agv.getAgvID());
				this._agvIsDestroyed = true;
				agv.destroy();
				return;
			} else {
				agv.hybridState = new IdleState(Performance.now());
			}
			return;
		}
		// nodeNext: nút tiếp theo cần đến
		if (agv.cur + 1 >= agv.path.size()) {
			System.out.println("Loi roi do: " + (agv.cur + 1));
		}
		Node2D nodeNext = agv.graph.nodes[agv.path.get((int) (agv.cur + 1)).x][agv.path.get((int) (agv.cur + 1)).y];
		double shortestDistance = Constant.minDistance(agv, agv.collidedActors);

		boolean overlap = false;
		for (int i = 0; i < agv.scene.agents.size(); i++) {
			if (Vector2.distance(agv.getPosition(), agv.scene.agents.get(i).getPosition()) < Constant.SAFE_DISTANCE) {
				overlap = true;
				break;
			}
		}
		
		if (nodeNext.state == StateOfNode2D.BUSY || shortestDistance < Constant.SAFE_DISTANCE) {
			overlap = true;
			agv.setVelocity(Vector2.zero);
			if (agv.waitT != 0)
				return;
			agv.waitT = Performance.now();
			agv.scene.forcasting.addDuration(agv.getAgvID(), new WaitingDuration(Math.floor(agv.waitT / 1000), -1, 0));
		} else {

			if (shortestDistance >= Constant.SAFE_DISTANCE) {
//				agv.collidedActors.clear();
			}

			if (agv.waitT != 0) {
				agv.curNode.setU((Performance.now() - agv.waitT) / 1000);
				agv.scene.forcasting.updateDuration(agv.getAgvID(), (int) Math.floor(agv.waitT / 1000),
						(int) Math.floor(Performance.now() / 1000));
				agv.waitT = 0;
			}
			if (Math.abs(agv.getTranslateX() - nodeNext.x * TILE_WIDTH) > 1
					|| Math.abs(agv.getTranslateY() - nodeNext.y * TILE_HEIGHT) > 1) {
				if (overlap == true)
					agv.setVelocity(Vector2.zero);
				else
					agv.scene.physics.moveTo(agv, new Vector2(nodeNext.x * TILE_WIDTH, nodeNext.y * TILE_HEIGHT), 0.6);
			} else {
				agv.curNode.setState(StateOfNode2D.EMPTY);
				agv.curNode = nodeNext;
				agv.curNode.setState(StateOfNode2D.BUSY);
				agv.cur++;
				agv.setX(agv.curNode.x * TILE_WIDTH);
				agv.setY(agv.curNode.y * TILE_HEIGHT);
				agv.setVelocity(Vector2.zero);
				agv.sobuocdichuyen++;
				if (agv.sobuocdichuyen % 10 == 0 || Performance.now() - agv.thoigiandichuyen > 10000) {
					agv.thoigiandichuyen = Performance.now();
					agv.cur = 0;
					agv.path = agv.calPathAStar(agv.curNode, agv.endNode);
				}
			}
		}

	}
}

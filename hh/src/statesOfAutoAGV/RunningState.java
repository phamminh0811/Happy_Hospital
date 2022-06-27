package statesOfAutoAGV;

import application.Main;
import classes.AutoAgv;
import classes.Node2D;
import classes.StateOfNode2D;
import classes.Vector2;
import config.Constant;
import config.Performance;
import statistic.WaitingDuration;

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
		if(this._agvIsDestroyed) //|| this._isEliminated)
            return;
        // nếu không có đường đi đến đích thì không làm gì
        if (agv.path == null) {
            return;
          }
        // nếu đã đến đích thì không làm gì
        if (agv.cur == agv.path.size() - 1) {
            agv.setVelocity(Vector2.zero);
            if(this._isLastMoving){
                Main mainScene = agv.scene;
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
        if(agv.cur + 1 >= agv.path.size()) {
            System.out.println("Loi roi do: "+ (agv.cur + 1));
        }
        Node2D nodeNext = agv.graph.nodes[agv.path.get((int) (agv.cur + 1)).x][agv.path.get((int) (agv.cur + 1)).y];
        //Khoảng cách của autoAgv với các actors khác đã va chạm
        double shortestDistance = Constant.minDistance(agv, agv.collidedActors);

        /**
         * nếu nút tiếp theo đang ở trạng thái bận
        * thì Agv chuyển sang trạng thái chờ
        */
        if (nodeNext.state == StateOfNode2D.BUSY || shortestDistance < Constant.SAFE_DISTANCE) {
            agv.setVelocity(Vector2.zero);
            if (agv.waitT != 0) return;
            agv.waitT = Performance.now();
            agv.scene.forcasting.addDuration(agv.getAgvID(), new WaitingDuration(Math.floor(agv.waitT/1000), -1, 0));
        } else {
            /*
             * Nếu tất cả các actor đều cách autoAgv một khoảng cách an toàn
            */
            if(shortestDistance >= Constant.SAFE_DISTANCE) {
                //Thì gỡ hết các actors trong danh sách đã gây ra va chạm
                agv.collidedActors.clear();
            }
            /**
             * nếu Agv từ trạng thái chờ -> di chuyển
                * thì cập nhật u cho node hiện tại
                */
            if (agv.waitT != 0) {
                agv.curNode.setU((Performance.now() - agv.waitT) / 1000);
                agv.scene.forcasting.updateDuration(agv.getAgvID(), (int)Math.floor(agv.waitT/1000), (int) Math.floor(Performance.now()/1000));
                agv.waitT = 0;
            }
            // di chuyển đến nút tiếp theo
            if (Math.abs(agv.x - nodeNext.x * 20) > 1 || Math.abs(agv.y - nodeNext.y * 20) > 1) {
                agv.scene.physics.moveTo(agv, new Vector2(nodeNext.x * 20, nodeNext.y * 20), 20);
            } else {
                /**
                 * Khi đã đến nút tiếp theo thì cập nhật trạng thái
                * cho nút trước đó, nút hiện tại và Agv
                */
                agv.curNode.setState(StateOfNode2D.EMPTY);
                agv.curNode = nodeNext;
                agv.curNode.setState(StateOfNode2D.BUSY);
                agv.cur++;
                agv.setX(agv.curNode.x * 20);
                agv.setY(agv.curNode.y * 20);
                agv.setVelocity(Vector2.zero);
                agv.sobuocdichuyen++;
                // cap nhat lai duong di Agv moi 10 buoc di chuyen;
                // hoac sau 10s di chuyen
                if (agv.sobuocdichuyen % 10 == 0 || Performance.now() - agv.thoigiandichuyen > 10000
                ) {
                    agv.thoigiandichuyen = Performance.now();
                    agv.cur = 0;
                    agv.path = agv.calPathAStar(agv.curNode, agv.endNode);
                }
            }
        }
  
    }
}

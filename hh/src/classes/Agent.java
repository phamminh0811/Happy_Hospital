package classes;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;
import static config.Config.*;
import algorithm.AStar;
import application.Main;

public class Agent extends Actor {
	public  Position startPos;
	public  Position endPos;
	public  ArrayList<Position> groundPos;
	public  ArrayList<Position> path;
	public  ArrayList<Position> vertexs;
	public  Text endText;
	public  Text agentText;
	public  AStar astar;
	public  int next = 1;
	public  int id;
	public boolean isOverlap = false;
	public double speed;

	public Agent(Main scene, Position startPos, Position endPos, ArrayList<Position> groundPos, int id) {
		super(scene, (int) startPos.x, (int) startPos.y, "agent");
		this.startPos = startPos;
		this.endPos = endPos;
		this.groundPos = groundPos;
		this.path = new ArrayList<Position>();
		this.vertexs = new ArrayList<Position>();
		this.id = id;
		this.speed = 0.2;

		this.endText = new Text(this.scene, endPos.x * TILE_WIDTH + 6, endPos.y * TILE_HEIGHT , String.valueOf(id), "");
		this.endText.getStyleClass().add("agent-end-text");
		
		this.agentText = new Text(this.scene, startPos.x, startPos.y, String.valueOf(id), "");
		this.agentText.getStyleClass().add("agent-text");
		
		this.astar = new AStar(52, 28, startPos, endPos, groundPos);
		this.path = this.astar.cal();
		this.initVertexs();
	}

	public void goToDestinationByVertexs() {
		if (this.next == this.vertexs.size()) {
			this.setTranslateX(this.vertexs.get(this.vertexs.size() - 1).x * TILE_WIDTH);
			this.setTranslateY(this.vertexs.get(this.vertexs.size() - 1).y * TILE_HEIGHT);
			this.velocity = Vector2.zero;
			this.eliminate();
			return;
		}
		if (Math.abs(this.vertexs.get(this.next).x * 20 - this.getTranslateX()) > 1
				|| Math.abs(this.vertexs.get(this.next).y * 20 - this.getTranslateY()) > 1) {
			this.scene.physics.moveTo(this, new Vector2(this.vertexs.get(this.next).x * 20, this.vertexs.get(this.next).y * 20),
					this.speed);
			this.agentText.setPosition(this.getTranslateX(), this.getTranslateY() - 10);
		} else {
			this.next++;
		}
	}

	public void eliminate() {
		this.agentText.destroy();
		this.endText.destroy();
		this.destroy();
	}

	public void addRandomVertexs(Position start, Position end) {
		double dis = Math.sqrt((start.x - end.x) * (start.x - end.x) + (start.y - end.y) * (start.y - end.y));
		int num = (int) Math.ceil((dis * 20) / 31.25);
		for (int i = 1; i < num; i++) {
			while (true) {
				Position rV = new Position(((end.x - start.x) / num) * i + start.x + (Math.random() - 0.5),
						((end.y - start.y) / num) * i + start.y + (Math.random() - 0.5));
				Position _1, _2, _3, _4;
				boolean b_1 = false, b_2 = false, b_3 = false, b_4 = false;
				_1 = new Position(rV.x, rV.y);
				_2 = new Position(rV.x + 1, rV.y);
				_3 = new Position(rV.x + 1, rV.y + 1);
				_4 = new Position(rV.x, rV.y + 1);

				for (int j = 0; j < this.groundPos.size(); j++) {
					Position p = this.groundPos.get(j);
					if (_1.x < p.x + 1 && _1.y < p.y + 1 && _1.x >= p.x && _1.y >= p.y) {
						b_1 = true;
					}
					if (_2.x < p.x + 1 && _2.y < p.y + 1 && _2.x >= p.x && _2.y >= p.y) {
						b_2 = true;
					}
					if (_3.x < p.x + 1 && _3.y < p.y + 1 && _3.x >= p.x && _3.y >= p.y) {
						b_3 = true;
					}
					if (_4.x < p.x + 1 && _4.y < p.y + 1 && _4.x >= p.x && _4.y >= p.y) {
						b_4 = true;
					}
				}
				if (b_1 && b_2 && b_3 && b_4) {
					this.vertexs.add(rV);
					break;
				}
			}
		}
	}

	public void initVertexs() {
		if (this.path != null) {
			this.vertexs.add(this.path.get(0));
			for (int cur = 2; cur < this.path.size(); cur++) {
				if ((this.path.get(cur).x == this.path.get(cur - 1).x
						&& this.path.get(cur).x == this.path.get(cur - 2).x)
						|| (this.path.get(cur).y == this.path.get(cur - 1).y
								&& this.path.get(cur).y == this.path.get(cur - 2).y)) {
					continue;
				}

				Position curV = this.vertexs.get(vertexs.size() - 1);
				Position nextV = this.path.get(cur - 1);
				this.addRandomVertexs(curV, nextV);
				this.vertexs.add(nextV);
			}
			this.addRandomVertexs(this.vertexs.get(this.vertexs.size() - 1), this.path.get(this.path.size() - 1));
			this.vertexs.add(this.path.get(this.path.size() - 1));
		}
	}

	public void preUpdate() {
		this.goToDestinationByVertexs();
	}

	public Position getStartPos() {
		return this.startPos;
	}

	public Position getEndPos() {
		return this.endPos;
	}

	public int getID() {
		return this.id;
	}

}

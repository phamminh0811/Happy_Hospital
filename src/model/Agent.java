package model;

import java.util.Stack;

import algorithm.AStar;

public class Agent extends Actor {
	private Position startPos;
	private Position endPos;
	private Stack<Position> groundPos;
	private Stack<Position> path;
	private Stack<Position> vertexs;
	private TextClass endText;
	private TextClass agentText;
	private AStar astar;
	private int next = 1;
	private int id;
	public boolean isOverlap = false;
	public double speed = 38;

	public Agent(Position startPos, Position endPos, Stack<Position> groundPos, int id) {
		super(startPos.x * 32, startPos.y * 32, "tile_spr");
		this.startPos = startPos;
		this.endPos = endPos;
		this.groundPos = groundPos;
		this.path = new Stack<>();
		this.vertexs = new Stack<>();
		this.id = id;
		this.speed = Math.floor(Math.random() * (this.speed - 10)) + 10;

		this.endText = new TextClass(endPos.x * 32 + 6, endPos.y * 32, Integer.toString(id), 28);
		this.agentText = new TextClass(startPos.x * 32, startPos.y * 32 - 16, Integer.toString(id), 28);

		this.astar = new AStar(52, 28, startPos, endPos, groundPos);
		this.path = this.astar.cal();

		this.initVertexs();

		this.position = Vector2.zero;
	}

	public void goToDestinationByVertexs() {
		if (this.next == this.vertexs.size()) {
			this.agentText.setText("DONE");
			this.agentText.setFontSize(12);
			this.agentText.setX(this.x - 1);
			this.x = this.vertexs.elementAt(this.vertexs.size() - 1).x * 32;
			this.y = this.vertexs.elementAt(this.vertexs.size() - 1).y * 32;
			this.velocity = Vector2.zero;
			this.eliminate();
			return;
		}
		if (Math.abs(this.vertexs.elementAt(this.next).x * 32 - this.x) > 1
				|| Math.abs(this.vertexs.elementAt(this.next).y * 32 - this.y) > 1) {
			this.moveTo(new Vector2(this.vertexs.elementAt(this.next).x * 32, this.vertexs.elementAt(this.next).y * 32),
					this.speed);
			this.agentText.setX(this.x);
			this.agentText.setY(this.y - 16);
		} else {
			this.next++;
		}
	}

	private void eliminate() {
		// TODO Auto-generated method stub

	}

	public void addRandomVertexs(Position start, Position end) {
		double dis = Math.sqrt((start.x - end.x) * (start.x - end.x) + (start.y - end.y) * (start.y - end.y));
		int num = (int) Math.ceil((dis * 32) / 50);
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
					Position p = this.groundPos.elementAt(j);
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
					this.vertexs.push(rV);
					break;
				}
			}
		}
	}

	public void initVertexs() {
		if (this.path.size() != 0) {
			this.vertexs.push(this.path.elementAt(0));
			for (int cur = 2; cur < this.path.size(); cur++) {
				if ((this.path.elementAt(cur).x == this.path.elementAt(cur - 1).x
						&& this.path.elementAt(cur).x == this.path.elementAt(cur - 2).x)
						|| (this.path.elementAt(cur).y == this.path.elementAt(cur - 1).y
								&& this.path.elementAt(cur).y == this.path.elementAt(cur - 2).y)) {
					continue;
				}

				Position curV = this.vertexs.elementAt(vertexs.size() - 1);
				Position nextV = this.path.elementAt(cur - 1);
				this.addRandomVertexs(curV, nextV);
				this.vertexs.push(nextV);
			}
			this.addRandomVertexs(this.vertexs.elementAt(this.vertexs.size() - 1),
					this.path.elementAt(this.path.size() - 1));
			this.vertexs.push(this.path.elementAt(this.path.size() - 1));
		}
	}

	void preUpdate() {

		this.goToDestinationByVertexs();
	}

	public Position getStartPos() {
		return this.startPos;
	}

	public Position getEndPos() {
		return this.endPos;
	}

	public int getId() {
		return this.id;
	}

}

package model;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Stack;

import javax.print.attribute.standard.DateTimeAtCompleted;

import statesOfAutoAGV.HybridState;
import statesOfAutoAGV.RunningState;

public class AutoAgv extends Actor {
	public Graph graph;
	public Stack<Node2D> path;
	public Node2D curNode, endNode;
	public double cur, waitT;
	public int sobuocdichuyen;
	public double thoigiandichuyen;
	public HybridState hybridState;
	public double endX;
	public double endY;
	public Text firstText;
	public double startX;
	public double startY;

	public AutoAgv(double x, double y, double endX, double endY, Graph graph) {
		super(x * 32, y * 32, "agv");
		this.startX = x * 32;
		this.startY = y * 32;
		this.endX = endX * 32;
		this.endY = endY * 32;

		this.graph = graph;
		this.setPosition(Vector2.zero);
		this.cur = 0;
		this.waitT = 0;
		this.curNode = this.graph.nodes[(int) x][(int) y];
		this.curNode.setState(StateOfNode2D.BUSY);
		this.endNode = this.graph.nodes[(int) endX][(int) endY];
		this.firstText = new Text(endX * 32, endY * 32, "DES", 16);
		this.path = this.calPathAStar(curNode, endNode);
		this.sobuocdichuyen = 0;
		this.thoigiandichuyen = Instant.now().getEpochSecond();
		this.estimateArrivalTime(x * 32, y * 32, endX * 32, endY * 32);
		this.hybridState = new RunningState();
	}
	protected void preUpdate(double time, double delta) {
		this.hybridState.move(this);
	}
	public Stack<Node2D> calPathAStar(Node2D start, Node2D end) {
		return this.graph.calPathAStar(start, end);
	}
	public void chageTarget() {
		
	}

}

package classes;

import java.util.ArrayList;

import application.Main;
import config.Performance;
import javafx.scene.control.ScrollPane;
import statesOfAutoAGV.HybridState;
import statesOfAutoAGV.RunningState;

public class AutoAgv extends Actor {
	public Graph graph;
	public ArrayList<Node2D> path;
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

	public AutoAgv(Main scene, int x, int y, int endX, int endY, Graph graph) {
		super(scene, x, y, "agv");
		this.startX = x * 20;
		this.startY = y * 20;
		this.endX = endX * 20;
		this.endY = endY * 20;

		this.graph = graph;

		this.cur = 0;
		this.waitT = 0;
		this.curNode = this.graph.nodes[x][y];
		this.curNode.setState(StateOfNode2D.BUSY);
		this.endNode = this.graph.nodes[endX][endY];

		this.firstText = new Text(this.scene, this.endX, this.endY, "DES", "");
		this.firstText.getStyleClass().add("auto-agv-des");

		this.path = this.calPathAStar(this.curNode, this.endNode);

		System.out.println(this.curNode + "-" + this.endNode);
		this.sobuocdichuyen = 0;
		this.thoigiandichuyen = Performance.now();
		this.estimateArrivalTime(x * 20, y * 20, endX * 20, endY * 20);
		this.hybridState = new RunningState(false);
	}

	public void preUpdate(double time, double delta) {
		if (this.hybridState == null)
			return;
		this.hybridState.move(this);
	}

	public ArrayList<Node2D> calPathAStar(Node2D start, Node2D end) {
		return this.graph.calPathAStar(start, end);
	}

	public void changeTarget() {
		Main scene = this.scene;
		int[] agvsToGate1 = scene.mapOfExits.get("Gate1");
		int[] agvsToGate2 = scene.mapOfExits.get("Gate2");
		String choosenGate = agvsToGate1[2] < agvsToGate2[2] ? "Gate1" : "Gate2";
		int[] newArray = scene.mapOfExits.get(choosenGate);
		newArray[2]++;
		scene.mapOfExits.put(choosenGate, newArray);

		this.startX = this.endX;
		this.startY = this.endY;

		int xEnd = newArray[0];
		int yEnd = newArray[1];
		this.endX = xEnd * 20;
		this.endY = yEnd * 20;

		int finalAGVs = scene.mapOfExits.get(choosenGate)[2];

		this.endNode = this.graph.nodes[xEnd][yEnd];
		this.firstText = new Text(this.scene, xEnd, yEnd, "DES_" + finalAGVs,
				"-fx-font-family: \"Courier New\";" + "-fx-font-weight: 900;" + " -fx-fill: red;"
						+ " -fx-stroke: black;" + " -fx-stroke-width: 1;" + " -fx-font-size: 18px");
		this.path = this.calPathAStar(this.curNode, this.endNode);
		this.cur = 0;
		this.sobuocdichuyen = 0;
		this.thoigiandichuyen = Performance.now();
		this.estimateArrivalTime(20 * this.startX, 20 * this.startY, this.endX * 20, this.endY * 20);
	}


}

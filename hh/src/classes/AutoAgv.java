package classes;

import static config.Config.TILE_HEIGHT;
import static config.Config.TILE_WIDTH;

import java.util.ArrayList;

import application.MainScene;
import config.Performance;
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
	public XText firstText;
	
	public double startX;
	
	public double startY;

	public AutoAgv(MainScene scene, int x, int y, int endX, int endY, Graph graph) {
		super(scene, x, y, "agv");
		this.startX = x * TILE_WIDTH;
		this.startY = y * TILE_HEIGHT;
		this.endX = endX * TILE_WIDTH;
		this.endY = endY * TILE_HEIGHT;

		this.graph = graph;

		this.cur = 0;
		this.waitT = 0;
		this.curNode = this.graph.nodes[x][y];
		this.curNode.setState(StateOfNode2D.BUSY);
		this.endNode = this.graph.nodes[endX][endY];

		this.firstText = new XText(this.scene, this.endX, this.endY - 5, "DES", "");
		this.firstText.getStyleClass().add("auto-agv-des");

		this.path = this.calPathAStar(this.curNode, this.endNode);
		this.sobuocdichuyen = 0;
		this.thoigiandichuyen = Performance.now();
		this.estimateArrivalTime(x * TILE_WIDTH, y * TILE_HEIGHT, endX * TILE_WIDTH, endY * TILE_HEIGHT);
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
		MainScene mainScene = this.scene;
		int[] agvsToGate1 = mainScene.mapOfExits.get("Gate1");
		int[] agvsToGate2 = mainScene.mapOfExits.get("Gate2");
		String choosenGate = agvsToGate1[2] < agvsToGate2[2] ? "Gate1" : "Gate2";
		int[] newArray = mainScene.mapOfExits.get(choosenGate);
		newArray[2]++;
		mainScene.mapOfExits.put(choosenGate, newArray);

		this.startX = this.endX;
		this.startY = this.endY;

		int xEnd = newArray[0];
		int yEnd = newArray[1];

		this.endX = xEnd * TILE_WIDTH;
		this.endY = yEnd * TILE_HEIGHT;

		int finalAGVs = (mainScene.mapOfExits.get(choosenGate))[2];

		this.endNode = this.graph.nodes[xEnd][yEnd];
		this.firstText = new XText(this.scene, xEnd - 2, yEnd - 1, "DES_" + finalAGVs, "");
		this.firstText.getStyleClass().add("auto-agv-des");
		this.path = this.calPathAStar(this.curNode, this.endNode);
		this.cur = 0;
		this.sobuocdichuyen = 0;
		this.thoigiandichuyen = Performance.now();
		this.estimateArrivalTime(this.startX * TILE_WIDTH, this.startY*TILE_HEIGHT, this.endX * TILE_WIDTH, this.endY * TILE_HEIGHT);
	}
}

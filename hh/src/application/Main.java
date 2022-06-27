package application;

import static config.Config.TILE_HEIGHT;
import static config.Config.TILE_WIDTH;
import static config.Config.TITLE;
import static config.Map.DOOR_LAYER;
import static config.Map.PATH_LAYER;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import algorithm.RandomDistribution;
import classes.Agent;
import classes.Agv;
import classes.AutoAgv;
import classes.EmergencyGraph;
import classes.Graph;
import classes.KeyHandler;
import classes.Position;
import classes.Sprite;
import classes.Text;
import classes.Tile;
import classes.Tilemap;
import classes.TilemapLayer;
import config.Constant;
import config.Constant.ModeOfPathPlanning;
import game.Physics;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import statistic.Forcasting;

public class Main extends Application {

	public static long startTimeMilis;
	public BorderPane root = new BorderPane();
	public Scene mainScene = new Scene(root);
	public GridPane gridPane;
	public StackPane stackPane;
	public KeyHandler keyHandler;
	public Physics physics;
	private Agv agv;
	public Set<AutoAgv> autoAgvs = new HashSet<AutoAgv>();
	private HashMap<String, ArrayList<Tile>> tileSet;
	private Tilemap map = new Tilemap();
	private TilemapLayer groundLayer;
	private TilemapLayer elevatorLayer;
	private TilemapLayer roomLayer;
	private TilemapLayer gateLayer;
	private TilemapLayer wallLayer;
	private TilemapLayer doorLayer;
	private TilemapLayer pathLayer;
	public TilemapLayer noPathLayer;
	private TilemapLayer bedLayer;

	private Graph spaceGraph;
	private EmergencyGraph emergencyGraph;
	private ArrayList<ArrayList<ArrayList<Position>>> adjacencyList = new ArrayList<>();

	private ArrayList<Position> groundPos = new ArrayList<>();
	private ArrayList<Position> doorPos = new ArrayList<>();
	private ArrayList<Position> wallPos = new ArrayList<>();
	private ArrayList<Position> pathPos = new ArrayList<>();
	private ArrayList<Position> roomPos = new ArrayList<>();
	private ArrayList<Position> elevatorPos = new ArrayList<>();
	private ArrayList<Position> noPathPos = new ArrayList<>();
	public static boolean is = false;
	private double sec = 0;
	private Label timeText = new Label(Constant.secondsToHMS(sec));
	public VBox timeTable;
	private Label harmfulTable;
	private double _harmfullness;
	private SimpleStringProperty harmfullness_string = new SimpleStringProperty("H.ness: ");
	public List<Agent> agents = new ArrayList<Agent>();
	private int MAX_AGENT = 10;
	public int count = 0;
	public Forcasting forcasting = new Forcasting();	
	private Button saveButton;
	private Button loadButton;
	public HashMap<String, int[]> mapOfExits = new HashMap<>() {
		{
			put("Gate1", new int[] { 50, 13, 0 });
			put("Gate2", new int[] { 50, 14, 0 });
		}
	};
	private TextField setAgentInput;
	private Button setAgentButton;
	public VBox desTable;

	public HashSet<Agent> removedAgent = new HashSet<>();
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		for (int i = 0; i < 52; i++) {
			this.adjacencyList.add(new ArrayList<>());
			for (int j = 0; j < 28; j++)
				this.adjacencyList.get(i).add(new ArrayList<>());
			for (int j = 0; j < 28; j++) {
				this.adjacencyList.get(i).set(j, new ArrayList<>());
			}
		}
		physics = new Physics(this);
		gridPane = addGridPane();
		stackPane = new StackPane(gridPane);
		gridPane.setStyle("-fx-background-color:#ECF0F1");

		this.preLoad();
		this.create();

		startTimeMilis = System.currentTimeMillis();
		primaryStage.setTitle(TITLE);
		primaryStage.setScene(mainScene);
		root.setCenter(stackPane);
		this.addButton();
		keyHandler = new KeyHandler(mainScene);
		keyHandler.listen();

		updateTimeText();
		updateHfn();

		new AnimationTimer() {
			@Override
			public void handle(long arg0) {
				update();
				physics.update();
			}
		}.start();
		primaryStage.show();
	}

	void updateTimeText() {
		Timeline timeLine = new Timeline();
		timeLine.getKeyFrames().add(new KeyFrame(Duration.seconds(1), e -> {
			timeText.textProperty().bind(new SimpleStringProperty(Constant.secondsToHMS(sec)));
			sec++;
		}));
		timeLine.setCycleCount(Timeline.INDEFINITE);
		timeLine.play();
	}

	void updateHfn() {

	}

	public void preLoad() {
		mainScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		for (int i = 0; i < 52; i++) {
			for (int j = 0; j < 28; j++) {
				if (config.Map.GROUND_LAYER[j][i] == 1)
					groundPos.add(new Position(i, j));
				if (config.Map.DOOR_LAYER[j][i] != 0) {
					doorPos.add(new Position(i, j));
				}
				if (config.Map.WALL_LAYER[j][i] != 0) {
					wallPos.add(new Position(i, j));
				}
				if (config.Map.PATH_LAYER[j][i] != 0) {
					pathPos.add(new Position(i, j));
				}
				if (config.Map.WALL_LAYER[j][i] != 0) {
					wallPos.add(new Position(i, j));
				}
				if (config.Map.ROOM_LAYER[j][i] != 0) {
					roomPos.add(new Position(i, j));
				}
				if (config.Map.ELEVATOR_LAYER[j][i] != 0) {
					elevatorPos.add(new Position(i, j));
				}
				if (config.Map.NO_PATH_LAYER[j][i] != 0) {
					noPathPos.add(new Position(i, j));
				}
			}
		}
		// ----

		ArrayList<Tile> pathTiles = new ArrayList<>();
		ArrayList<Tile> roomTiles = new ArrayList<>();
		ArrayList<Tile> elevatorTiles = new ArrayList<>();
		ArrayList<Tile> noPathTiles = new ArrayList<>();

		pathPos.forEach(p -> {
			String dir = "none";
			String type = "";
			switch (PATH_LAYER[(int) p.y][(int) p.x]) {
			case 28:
				type = "right";
				dir = "right";
				break;
			case 36:
				dir = "left";
				type = "left";
				break;
			case 12:
				dir = "up";
				type = "top";
				break;
			case 20:
				dir = "down";
				type = "bottom";
				break;
			case 42:
				type = "up-right-fork";
				break;
			case 41:
				type = "down-right-fork";
				break;
			case 35:
				type = "down-left-fork";
				break;
			case 43:
				type = "up-left-fork";
				break;
			case 58:
				type = "left-3-fork";
				break;
			case 57:
				type = "right-3-fork";
				break;
			case 50:
				type = "up-3-fork";
				break;
			case 49:
				type = "down-3-fork";
				break;
			case 59:
				type = "4-fork";
				break;
			}

			Tile tile = new Tile((int) p.x, (int) p.y, new Image("file:sprites/" + type + ".png"), dir);
			tile.direction = dir;
			pathTiles.add(tile);
		});
		pathLayer = new TilemapLayer(this, pathTiles, "path");

		roomPos.forEach(p -> {
			Tile tile = new Tile((int) p.x, (int) p.y, new Image("file:sprites/room.png"), "room");
			roomTiles.add(tile);
			tile.place(gridPane);
		});
		roomLayer = new TilemapLayer(this, roomTiles, "room");

		elevatorPos.forEach(p -> {
			Tile tile = new Tile((int) p.x, (int) p.y, new Image("file:sprites/elevator.png"), "elevator");
			elevatorTiles.add(tile);
		});
		elevatorLayer = new TilemapLayer(this, elevatorTiles, "elevator");

		noPathPos.forEach(p -> {
			Tile tile = new Tile((int) p.x, (int) p.y, null, "no-path");
			noPathTiles.add(tile);
		});
		noPathLayer = new TilemapLayer(this, noPathTiles, "no-path");

		doorPos.forEach(p -> {
			String dir = "";
			switch (DOOR_LAYER[(int) p.y][(int) p.x]) {
			case 19:
				dir = "front-door";
				break;
			case 26:
				dir = "behind-door";
				break;
			}
			Tile tile = new Tile((int) p.x, (int) p.y, new Image("file:sprites/" + dir + ".png"), dir);
			tile.place(gridPane);
		});

		drawBackground();
	}

	public GridPane getGridPane() {
		return this.gridPane;
	}

	void drawBackground() {
		wallPos.forEach(e -> {
			Tile tile = new Tile((int) e.x, (int) e.y, new Image("file:sprites/wall.png"), "wall");
			tile.place(gridPane);
		});
		pathLayer.getTiles().forEach(e -> {
			e.place(gridPane);
		});
		elevatorLayer.getTiles().forEach(e -> {
			e.place(gridPane);
		});
	}

	public void update() {
		this.getGraph().updateState();
		agv.update();
		this.forcasting.calculate();
	}

	private GridPane addGridPane() {
		GridPane gridPane = new GridPane();
		for (int i = 0; i < 52; i++) {
			ColumnConstraints cc = new ColumnConstraints(20);
			gridPane.getColumnConstraints().add(cc);
		}

		for (int i = 0; i < 28; i++) {
			RowConstraints rc = new RowConstraints(20);
			gridPane.getRowConstraints().add(rc);
		}
		return gridPane;
	}

	private Node addHBox() {
		this.saveButton = new Button("Save data");
		this.loadButton = new Button("Load data");
		this.setAgentInput = new TextField();
		this.setAgentInput.setPromptText("Number of Agents");
		this.setAgentButton = new Button("Apply");

		this.setAgentButton.setOnAction(e -> {
			String s = this.setAgentInput.getText().trim();
			try {
				int numAgent = Integer.parseInt(s);
				this.setMaxAgents(numAgent);
				this.setAgentInput.setText("");
			} catch (Exception ex) {

			}
		});

		Region region = new Region();
		HBox.setHgrow(region, Priority.ALWAYS);

		HBox hbox = new HBox();
		hbox.setSpacing(4);
		hbox.setStyle("-fx-padding: 4px;-fx-background-color: #D0D3D4");

		hbox.getChildren().addAll(this.setAgentInput, this.setAgentButton, region, this.saveButton, this.loadButton);
		return hbox;
	}

	private Node addVBox() {
		VBox vbox = new VBox();
		vbox.setStyle("-fx-alignment: center;-fx-padding: 4px;-fx-background-color: #D0D3D4");
		Text header = new Text(this, 0, 0, "AGV Deadline", "");
		header.setStyle("-fx-fill: #FF0000;-fx-font-family: \"Courier New\";-fx-font-weight: bold;-fx-font-size: 14px");

		ScrollPane table = new ScrollPane();
		table.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		timeTable = new VBox();
		timeTable.setStyle("-fx-background-color: #ffffff");
		table.setContent(timeTable);
		vbox.getChildren().addAll(header, timeTable);
		return vbox;
	}

	private Node addBHBox() {
		VBox vbox = new VBox();
		vbox.setStyle("-fx-padding: 4px;-fx-background-color: #D0D3D4");
		this.harmfulTable = new Label();
		this.harmfulTable.textProperty().bind(this.harmfullness_string);
		this.harmfulTable.getStyleClass().add("hfn");

		this.timeText = new Label();
		this.timeText.getStyleClass().add("hfn");

		vbox.getChildren().addAll(timeText, harmfulTable);
		return vbox;

	}

	private void setMaxAgents(int numAgent) {
		this.MAX_AGENT = numAgent;
	}

	public void setHarmfullness(double value) {
		this._harmfullness = value;
		this.harmfulTable.textProperty().bind(new SimpleStringProperty(String.valueOf(this._harmfullness)));
	}

	public double getHarmfullness() {
		return this._harmfullness;
	}

	public void initGraph() {
		if (Constant.MODE() == ModeOfPathPlanning.FRANSEN) {
			this.spaceGraph = new Graph(52, 28, this.adjacencyList, this.pathPos);
		} else {
			this.emergencyGraph = new EmergencyGraph(52, 28, this.adjacencyList, this.pathPos);
		}
	}

	private void createRandomAutoAgv() {
		int r = (int) Math.floor(Math.random() * this.pathPos.size());
		while (!Constant.validDestination((int) this.pathPos.get(r).x, (int) this.pathPos.get(r).y, 1, 13)) {
			r = (int) Math.floor(Math.random() * this.pathPos.size());
		}
		if (this.getGraph() != null) {
			AutoAgv tempAgv = new AutoAgv(this, 1, 13, (int) this.pathPos.get(r).x, (int) this.pathPos.get(r).y,
					this.getGraph());
//			tempAgv.writeDeadline(this.timeTable);
//            if(des) {
//                while (des.childNodes.length >= 1) {
//                    des.firstChild && des.removeChild(des.firstChild);
//                }
//
//                des.appendChild(des.ownerDocument.createTextNode(this.timeTable.text));
//
//            }
			this.autoAgvs.add(tempAgv);
		}
	}

	private boolean checkTilesUndirection(Tile tileA, Tile tileB) {
		if (tileA.xPlace == tileB.xPlace && tileA.yPlace == tileB.yPlace + 1) {
			if (tileB.getDirection() == "top")
				return true;
		}
		if (tileA.xPlace + 1 == tileB.xPlace && tileA.yPlace == tileB.yPlace) {
			if (tileB.getDirection() == "right")
				return true;
		}
		if (tileA.xPlace == tileB.xPlace && tileA.yPlace + 1 == tileB.yPlace) {
			if (tileB.getDirection() == "bottom")
				return true;
		}
		if (tileA.xPlace == tileB.xPlace + 1 && tileA.yPlace == tileB.yPlace) {
			if (tileB.getDirection() == "left")
				return true;
		}
		return false;

	}

	private boolean checkTilesNeighbor(Tile tileA, Tile tileB) {
		if (tileA.direction == "none") {
			if (this.checkTilesUndirection(tileA, tileB))
				return true;
		} else {
			if (tileA.direction == "top") {
				if (tileA.xPlace == tileB.xPlace && tileA.yPlace == tileB.yPlace + 1)
					return true;
			}
			if (tileA.direction == "right") {
				if (tileA.xPlace + 1 == tileB.xPlace && tileA.yPlace == tileB.yPlace)
					return true;
			}
			if (tileA.direction == "bottom") {
				if (tileA.xPlace == tileB.xPlace && tileA.yPlace + 1 == tileB.yPlace)
					return true;
			}
			if (tileA.direction == "left") {
				if (tileA.xPlace == tileB.xPlace + 1 && tileA.yPlace == tileB.yPlace)
					return true;
			}
		}
		return false;
	}

	public void create() {
		this.initMap();
		this.createAdjList();
		this.initGraph();
		this.spaceGraph = new Graph(52, 28, this.adjacencyList, this.pathPos);
		this.emergencyGraph = new EmergencyGraph(52, 28, this.adjacencyList, this.pathPos);
		int r = (int) Math.floor(Math.random() * this.pathPos.size());
		while (!Constant.validDestination(this.pathPos.get(r).x, this.pathPos.get(r).y, 1, 14)) {
			r = (int) Math.floor(Math.random() * this.pathPos.size());
		}
		this.agv = new Agv(this, 1, 14, (int) this.pathPos.get(r).x, (int) this.pathPos.get(r).y, this.pathLayer);
		this.createRandomAutoAgv();
		this.createAgents(10, 1000);
	}

	private void createAdjList() {
		ArrayList<Tile> tiles = this.pathLayer.getTiles();
		for (int i = 0; i < tiles.size(); i++) {
			for (int j = 0; j < tiles.size(); j++) {
				if (i != j) {
					if (this.checkTilesNeighbor(tiles.get(i), tiles.get(j))) {
						Position p = new Position(tiles.get(j).xPlace, tiles.get(j).yPlace);
						this.adjacencyList.get(tiles.get(i).xPlace).get(tiles.get(i).yPlace).add(p);
					}
				}
			}
		}
	}

	void createAgents(int numAgentInit, long time) {
		ArrayList<Integer> randoms = new ArrayList<>();
		while (randoms.size() < (numAgentInit << 1)) {
			int r = (int) Math.floor(Math.random() * this.doorPos.size());
			if (!randoms.contains(r))
				randoms.add(r);
		}
		this.agents = new CopyOnWriteArrayList<>();
		for (int i = 0; i < numAgentInit; i++) {
			Agent agent = new Agent(this, this.doorPos.get(randoms.get(i)),
					this.doorPos.get(randoms.get(i + numAgentInit)), this.groundPos,
					(int) Math.floor((Math.random() * 100)));
			this.agents.add(agent);
		}
//		if (this.getGraph() != null) {
//			this.getGraph().setAgents(this.agents);
//		}
		// thêm ngẫu nhiên agent vào môi trường
		Timeline timeLine = new Timeline(new KeyFrame(Duration.millis(time), e -> {
			addAgent();
		}));
		timeLine.setCycleCount(Timeline.INDEFINITE);
		timeLine.play();
		// end setInterval
	}

	public void addAgent() {
		if (this.agents.size() >= this.MAX_AGENT)
			return;
		RandomDistribution rand = new RandomDistribution();
		double ran = rand.getProbability();
		System.out.println(ran);
		if (ran > 1)
			System.out.println(rand.getName() + " " + ran);
		if (ran > 0.37)
			return;
		int r1 = (int) Math.floor(Math.random() * this.doorPos.size());
		int r2 = (int) Math.floor(Math.random() * this.doorPos.size());
		Agent agent = new Agent(this, this.doorPos.get(r1), this.doorPos.get(r2), this.groundPos,
				(int) Math.floor(Math.random() * 100));
//        this.physics.add.overlap(this.addddddgv, agent, () => {
//                agent.handleOverlap();
//        this.agv.handleOverlap();
//      });
//        this.autoAgvs.forEach(
//                (item) => {
//                item && this.physics.add.overlap(agent, item, () => {
//                        item.freeze(agent);
//          });
//        }
//      );
		
		this.agents.add(agent);
		if (this.getGraph() != null)
			this.getGraph().setAgents(this.agents);
		this.count++;
		if (this.count == 2) {
			this.createRandomAutoAgv();
			this.count = 0;
		}
	}

	private void initMap() {

	}

	public Graph getGraph() {
		if (Constant.MODE() == ModeOfPathPlanning.FRANSEN) {
			return this.spaceGraph;
		} else {
			return this.emergencyGraph;
		}
	}

	public void addButton() {
		root.setTop(addHBox());
		root.setBottom(addBHBox());
		root.setRight(addVBox());
		root.getCenter().setFocusTraversable(true);
	}

	public void add(Sprite sprite) {
		this.gridPane.getChildren().add(sprite);
		sprite.setTranslateX(sprite.x * TILE_WIDTH);
		sprite.setTranslateY(sprite.y * TILE_HEIGHT);
	}

	public void add(Text text) {
		this.gridPane.add(text, text.i, text.j);
		text.move();
	}

	public void destroy(Sprite sprite) {
		this.gridPane.getChildren().remove(sprite);
		this.agents.remove(sprite);
//		else if (sprite instanceof AutoAgv)
//			this.autoAgvs.remove(sprite);
	}

	public void destroy(Text text) {
		this.gridPane.getChildren().remove(text);
	}

	public Agv getAgv() {
		return this.agv;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
package application;

import static config.Config.TILE_HEIGHT;
import static config.Config.TILE_WIDTH;
import static config.Config.TITLE;
import static config.Map.DOOR_LAYER;
import static config.Map.PATH_LAYER;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import algorithm.RandomDistribution;
import classes.Agent;
import classes.Agv;
import classes.AutoAgv;
import classes.EmergencyGraph;
import classes.Graph;
import classes.IDestroyable;
import classes.KeyHandler;
import classes.LambdaExpression;
import classes.Position;
import classes.Sprite;
import classes.XText;
import classes.game.Physics;
import classes.simplified_classes.SAgent;
import classes.simplified_classes.SAgv;
import classes.simplified_classes.SaveObject;
import classes.tilemaps.Tile;
import classes.tilemaps.Tile.Direction;
import classes.tilemaps.Tilemap;
import classes.tilemaps.TilemapLayer;
import classes.window.Prompt;
import config.Constant;
import config.Constant.ModeOfPathPlanning;
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
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import statistic.Forcasting;

public class MainScene extends Application {
	public MainScene() {
	};

	public static long startTimeMilis;
	private Stage primaryStage;
	public BorderPane root = new BorderPane();
	public Scene mainScene = new Scene(root);
	public GridPane gridPane;
	public StackPane stackPane;
	public KeyHandler keyHandler;
	public Physics physics;
	private Agv agv;
	public Set<AutoAgv> autoAgvs = new CopyOnWriteArraySet<>();
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
	private ArrayList<Position> gatePos = new ArrayList<>();

	private double sec = 0;
	private Label timeText = new Label(Constant.secondsToHMS(sec));
	public VBox timeTable;
	private Label harmfulTable;
	private double _harmfullness;
	public List<Agent> agents = new CopyOnWriteArrayList<Agent>();
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

	ObjectMapper mapper = new ObjectMapper();
	String mapData;

	@Override
	public void start(Stage primaryStage) throws Exception {
		mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);

		this.primaryStage = primaryStage;
		for (int i = 0; i < 52; i++) {
			this.adjacencyList.add(new ArrayList<>());
			for (int j = 0; j < 28; j++)
				this.adjacencyList.get(i).add(new ArrayList<>());
			for (int j = 0; j < 28; j++) {
				this.adjacencyList.get(i).set(j, new ArrayList<>());
			}
		}
		this.physics = new Physics(this);
		this.gridPane = addGridPane();
		this.stackPane = new StackPane(gridPane);
		this.gridPane.setStyle("-fx-background-color:#ECF0F1");

		this.primaryStage.setTitle(TITLE);
		this.primaryStage.setScene(mainScene);

		this.root.setCenter(stackPane);
		this.addButton();

		this.preLoad();
		this.create();

//		this.keyHandler ;
		this.keyHandler = new KeyHandler(mainScene);
		this.keyHandler.listen();

		startTimeMilis = System.currentTimeMillis();

		AnimationTimer update = new AnimationTimer() {

			@Override
			public void handle(long arg0) {
				fixedUpdate();
			}
		};
		update.start();
		this.primaryStage.show();
	}

	private void fixedUpdate() {
		this.getGraph().updateState();
		agv.update();
		physics.update();
		this.forcasting.calculate();
	}

	private void updateTimeText() {
		Timeline timeLine = new Timeline();
		timeLine.getKeyFrames().add(new KeyFrame(Duration.seconds(1), e -> {
			timeText.textProperty().bind(new SimpleStringProperty(Constant.secondsToHMS(sec)));
			sec++;
		}));
		timeLine.setCycleCount(Timeline.INDEFINITE);
		timeLine.play();
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
				if (config.Map.GATE_LAYER[j][i] != 0) {
					gatePos.add(new Position(i, j));
				}
			}
		}
		// ----

		ArrayList<Tile> pathTiles = new ArrayList<>();
		ArrayList<Tile> roomTiles = new ArrayList<>();
		ArrayList<Tile> elevatorTiles = new ArrayList<>();
		ArrayList<Tile> noPathTiles = new ArrayList<>();

		pathPos.forEach(p -> {
			Direction dir = Direction.NONE;
			String type = "";
			switch (PATH_LAYER[(int) p.y][(int) p.x]) {
			case 28:
				type = "right";
				dir = Direction.RIGHT;
				break;
			case 36:
				type = "left";
				dir = Direction.LEFT;
				break;
			case 12:
				type = "top";
				dir = Direction.TOP;
				break;
			case 20:
				type = "bottom";
				dir = Direction.BOTTOM;
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

			Tile tile = new Tile((int) p.x, (int) p.y, new Image("file:sprites/" + type + ".png"), String.valueOf(dir));
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
		gatePos.forEach(p -> {
			Tile tile = new Tile((int) p.x, (int) p.y, new Image("file:sprites/gate.png"), "gate");
			tile.place(gridPane);
			doorPos.add(p);
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

	private GridPane addGridPane() {
		GridPane gridPane = new GridPane();
		for (int i = 0; i < 52; i++) {
			ColumnConstraints cc = new ColumnConstraints(32);
			gridPane.getColumnConstraints().add(cc);
		}

		for (int i = 0; i < 28; i++) {
			RowConstraints rc = new RowConstraints(32);
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
				ex.printStackTrace();
			}
		});
		this.saveButton.setOnAction(e -> {
			this.handleClickSaveButton();
		});
		this.loadButton.setOnAction(e -> {
			this.handleClickLoadButton();
		});

		Region region = new Region();
		HBox.setHgrow(region, Priority.ALWAYS);

		HBox hbox = new HBox();
		hbox.setSpacing(12);
		hbox.setStyle("-fx-padding: 8px;-fx-background-color: #D0D3D4; -fx-font-size: 16px;");

		hbox.getChildren().addAll(this.setAgentInput, this.setAgentButton, region, this.saveButton, this.loadButton);
		return hbox;
	}

	private Node addVBox() {
		VBox vbox = new VBox();
		vbox.setStyle("-fx-padding: 4px;-fx-background-color: #D0D3D4");
		XText header = new XText(this, 0, 0, "AGV Deadline", "");
		header.getStyleClass().add("header");

		ScrollPane table = new ScrollPane();
		table.setPrefSize(150, 300);
		timeTable = new VBox();
		timeTable.setStyle("-fx-padding: 4px");
		table.setContent(timeTable);
		vbox.getChildren().addAll(header, table);
		return vbox;
	}

	private Node addBHBox() {
		VBox vbox = new VBox();
		vbox.setStyle("-fx-padding: 4px;-fx-background-color: #D0D3D4");
		this.harmfulTable = new Label();
		this.harmfulTable.textProperty().bind(new SimpleStringProperty("H.ness: "));
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
		this.harmfulTable.textProperty()
				.bind(new SimpleStringProperty("H.ness: " + String.format("%,.3f", this._harmfullness)));
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

	private void handleClickSaveButton() {
		Prompt error = Prompt.error("Save data", "Lưu map không thành công!");
		Prompt alert = Prompt.confirm("Save data", "Đã lưu map thành công!");

		ArrayList<SAgent> sagents = new ArrayList<>();
		for (int i = 0; i < this.agents.size(); i++) {
			SAgent sagent = new SAgent(this.agents.get(i));
			sagents.add(sagent);
		}
		try {
			mapData = mapper.writeValueAsString(new SaveObject(new SAgv(this.agv), sagents));
			DirectoryChooser directoryChooser = new DirectoryChooser();
			String saveDir = directoryChooser.showDialog(primaryStage).getAbsolutePath() + "\\save.json";
			File save = new File(saveDir);
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(save));
				bw.write(mapData);
				bw.close();
				alert.showAndWait();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (JsonProcessingException e) {
			error.showAndWait();
			e.printStackTrace();
		}
	}

	private void handleClickLoadButton() {
		Prompt error = Prompt.error("Load data", "File không đúng định dạng. Vui lòng chọn file .json!");
		Prompt success = Prompt.confirm("Load data", "Đã tải map thành công!");
		Prompt fileError = Prompt.error("Load data", "Tải file không thành công!");

		File file = new FileChooser().showOpenDialog(primaryStage);
		String fileName = file.getAbsolutePath();
		String fileExtension = "";
		int i = -1;
		i = fileName.lastIndexOf('.');
		if (i > 0)
			fileExtension = fileName.substring(i + 1);
		if (!fileExtension.equals("json") || i < 0) {
			error.showAndWait();
			return;
		}
		try {
			SaveObject saveObject = null;
			try {
				saveObject = mapper.readValue(file, SaveObject.class);
			} catch (Exception e) {
				fileError.showAndWait();
				e.printStackTrace();
			}
			if (saveObject != null) {
				SAgv sagv = saveObject.agv;
				ArrayList<SAgent> sagents = saveObject.agents;
				this.agv.setTranslateX(sagv.x);
				this.agv.setTranslateY(sagv.y);
				for (Agent agent : this.agents) {
					agent.eliminate();
				}
				for (int j = 0; j < sagents.size(); j++) {
					Position sp = new Position(sagents.get(j).startPos.x, sagents.get(j).startPos.y);
					Position ep = new Position(sagents.get(j).endPos.x, sagents.get(j).endPos.y);
					int id = sagents.get(j).id;
					this.agents.add(new Agent(this, sp, ep, groundPos, id));
				}
				success.showAndWait();
			}
		} catch (Exception e) {
			fileError.showAndWait();
			e.printStackTrace();
		}
	}

	private boolean checkTilesUnDirection(Tile tileA, Tile tileB) {
		if (tileA.tileX == tileB.tileX && tileA.tileY == tileB.tileY + 1) {
			if (tileB.direction == Direction.TOP)
				return true;
		}
		if (tileA.tileX + 1 == tileB.tileX && tileA.tileY == tileB.tileY) {
			if (tileB.direction == Direction.RIGHT)
				return true;
		}
		if (tileA.tileX == tileB.tileX && tileA.tileY + 1 == tileB.tileY) {
			if (tileB.direction == Direction.BOTTOM)
				return true;
		}
		if (tileA.tileX == tileB.tileX + 1 && tileA.tileY == tileB.tileY) {
			if (tileB.direction == Direction.LEFT)
				return true;
		}
		return false;

	}

	private boolean checkTilesNeighbor(Tile tileA, Tile tileB) {
		if (tileA.direction == Direction.NONE) {
			if (this.checkTilesUnDirection(tileA, tileB))
				return true;
		} else {
			if (tileA.direction == Direction.TOP) {
				if (tileA.tileX == tileB.tileX && tileA.tileY == tileB.tileY + 1)
					return true;
			}
			if (tileA.direction == Direction.RIGHT) {
				if (tileA.tileX + 1 == tileB.tileX && tileA.tileY == tileB.tileY)
					return true;
			}
			if (tileA.direction == Direction.BOTTOM) {
				if (tileA.tileX == tileB.tileX && tileA.tileY + 1 == tileB.tileY)
					return true;
			}
			if (tileA.direction == Direction.LEFT) {
				if (tileA.tileX == tileB.tileX + 1 && tileA.tileY == tileB.tileY)
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
		if (this.getGraph() != null)
			this.getGraph().setMAgv(this.agv);
		updateTimeText();
	}

	void createAgents(int numAgentInit, long time) {
		ArrayList<Integer> randoms = new ArrayList<>();
		while (randoms.size() < (numAgentInit * 2)) {
			int r = (int) Math.floor(Math.random() * this.doorPos.size());
			if (!randoms.contains(r))
				randoms.add(r);
		}
		this.agents = new CopyOnWriteArrayList<>();
		for (int i = 0; i < numAgentInit; i++) {
			Agent agent = new Agent(this, this.doorPos.get(randoms.get(i)),
					this.doorPos.get(randoms.get(i + numAgentInit)), this.groundPos,
					(int) Math.floor((Math.random() * 100)));
			this.physics.addOverlap(this.agv, agent, new LambdaExpression() {
				@Override
				public void expression() {
					agent.handleOverlap();
					agv.handleOverlap();
				}
			});
//			this.autoAgvs.forEach(item -> {
//				if (item != null) {
//					this.physics.addOverlap(item, agent, new LambdaExpression() {
//						@Override
//						public void expression() {
//							item.freeze(agent);
//						}
//					});
//				}
//			});
			
			this.agents.add(agent);
		}
		if (this.getGraph() != null) {
			this.getGraph().setAgents(this.agents);
		}
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
		if (ran > 1)
			System.out.println(rand.getName() + " " + ran);
		if (ran > 0.37)
			return;
		int r1 = (int) Math.floor(Math.random() * this.doorPos.size());
		int r2 = (int) Math.floor(Math.random() * this.doorPos.size());
		Agent agent = new Agent(this, this.doorPos.get(r1), this.doorPos.get(r2), this.groundPos,
				(int) Math.floor(Math.random() * 100));

		this.physics.addOverlap(this.agv, agent, new LambdaExpression() {
			@Override
			public void expression() {
				agent.handleOverlap();
				agv.handleOverlap();
			}
		});
		
		this.agents.add(agent);
		if (this.getGraph() != null)
			this.getGraph().setAgents(this.agents);
		this.count++;
		if (this.count == 2) {
			this.createRandomAutoAgv();
			this.count = 0;
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
			tempAgv.writeDeadline(this.timeTable);
			this.autoAgvs.forEach(autoAgv -> {
				tempAgv.collidedActors.add(autoAgv);
			});
			this.autoAgvs.add(tempAgv);
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

	public void destroyAgentHandler(Agent agent) {
		this.agents.remove(agent);
		if (this.getGraph() != null)
			this.getGraph().removeAgent(agent);
//		this.autoAgvs.forEach(e -> {
//			e.collidedActors.remove(agent);
//		});
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

	public void add(XText text) {
		this.gridPane.add(text, text.i, text.j);
		text.move();
	}

	public void destroy(IDestroyable obj) {
		this.gridPane.getChildren().remove(obj);
//		if (obj instanceof AutoAgv)
//			this.autoAgvs.remove(obj);
	}

	public Agv getAgv() {
		return this.agv;
	}

	private void createAdjList() {
		ArrayList<Tile> tiles = this.pathLayer.getTiles();
		for (int i = 0; i < tiles.size(); i++) {
			for (int j = 0; j < tiles.size(); j++) {
				if (i != j) {
					if (this.checkTilesNeighbor(tiles.get(i), tiles.get(j))) {
						Position p = new Position(tiles.get(j).tileX, tiles.get(j).tileY);
						this.adjacencyList.get(tiles.get(i).tileX).get(tiles.get(i).tileY).add(p);
					}
				}
			}
		}
	}
}
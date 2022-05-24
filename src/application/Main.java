package application;

import static config.Config.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Agent;
import model.Agv;
import model.AutoAgv;
import model.EmergencyGraph;
import model.Graph;
import model.KeyHandler;
import model.Position;
import model.Sprite;
import model.Tile;
import model.Vector2;
import statistic.Forcasting;

public class Main extends Application {
	public static long startTimeMilis;
	BorderPane root = new BorderPane();
	Scene mainScene = new Scene(root);
	Canvas canvas;
	GraphicsContext context;
	public static Tile[][] tiles = new Tile[MAP_HEIGHT][MAP_WIDTH];

	KeyHandler keyHandler = new KeyHandler(mainScene);

	Agv agv = new Agv();
	
	private Agv _agv;
	public Set<AutoAgv> autoAgvs;
	private Sprite map;
	private Sprite tileset;
	private Sprite groundLayer;
	private Sprite elevatorLayer;
	private Sprite roomLayer;
	private Sprite gateLayer;
	private Sprite wallLayer;
	private Sprite doorLayer;
	private Sprite pathLayer;
	private Sprite noPathLayer;
	private Sprite bedLayer;

	private Position[] groundPos;
	private Position[] pathPos;
	private Position[][][] danhsachke;
	// private savebutton, loadbutton, mapdata
	private Graph spaceGraph;
	private EmergencyGraph emergencyGraph;
	private Position[] doorPos;
	private Text timeText;
	private double sec=0;
	public Text timeTable;
	private Text harmfulTable;
	private double _harmfullness;
	private Agent[] agent;
	private int MAX_AGENT = 10;
	//private desDom
	private Map<String, int[]> mapOfExits = new HashMap<>();
	public int count = 0;
	public Forcasting forcasting;
	
	@Override
	public void start(Stage mainStage) {
		startTimeMilis = System.currentTimeMillis();
		try {
			agv.setPosition(new Vector2(1 * TILE_WIDTH, 13 * TILE_HEIGHT));
			mainStage.setTitle(TITLE);
			mainStage.setScene(mainScene);
			canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
			context = canvas.getGraphicsContext2D();
			root.setCenter(canvas);
			keyHandler.listen();

			new AnimationTimer() {
				@Override
				public void handle(long arg0) {
					update();
				}
			}.start();

			// ---------------
			mainStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void update() {
//		Thread d = new Thread();
//		try {
//			d.sleep(30);
//		} catch (InterruptedException e) {
//			
//			e.printStackTrace();
//		}
		drawBackground();
		agv.render(context);
		agv.update();
	}

	List<Integer> arrayIntegers = new ArrayList<>(Arrays.asList(58, 42, 43, 50, 49, 57, 35, 41, 59));

	void drawBackground() {
		for (int i = 0; i < MAP_HEIGHT; i++) {
			for (int j = 0; j < MAP_WIDTH; j++) {
				Color tileColor;
				String dir = "";
				switch (m[i][j]) {
				case 11:
					tileColor = Color.GRAY;
					break;
				case 0:
					tileColor = Color.GHOSTWHITE;
					dir = "no";
					break;
				case 28:
					tileColor = Color.LIMEGREEN;
					dir = "right";
					break;
				case 22:
					tileColor = Color.YELLOW;
					break;
				case 12:
					tileColor = Color.RED;
					dir = "top";
					break;
				case 36:
					tileColor = Color.DARKORANGE;
					dir = "left";
					break;
				case 20:
					tileColor = Color.CORNFLOWERBLUE;
					dir = "bottom";
					break;
				case 33:
					tileColor = Color.GREENYELLOW;
					break;
				default:
					tileColor = Color.BLACK;
					dir = "any";
					break;
				}
				tiles[i][j] = new Tile(j * TILE_WIDTH, i * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT, tileColor, dir);
				tiles[i][j].render(context);
			}
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
	
}

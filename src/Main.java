
import static config.Config.*;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
    BorderPane root = new BorderPane();
    Scene mainScene = new Scene(root);
    Canvas canvas;
    GraphicsContext context;
    Image image;
    public static Tile[][] tiles = new Tile[MAP_HEIGHT][MAP_WIDTH];

    @Override
    public void start(Stage mainStage) {
        try {
            mainStage.setTitle(TITLE);
            mainStage.setScene(mainScene);
            canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
            context = canvas.getGraphicsContext2D();
            root.setCenter(canvas);
            new AnimationTimer() {
                @Override
                public void handle(long arg0) {
                    drawBackground();
                }
            }.start();
            mainStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void drawBackground() {
        for (int i = 0; i < MAP_HEIGHT; i++) {
            for (int j = 0; j < MAP_WIDTH; j++) {
                Color tileColor = Color.GHOSTWHITE;
                String dir = "";
                String fileName = "";
                Boolean has_file = false;
                switch (MAP_WALL[i][j]) {
                    case 4:
                        has_file = true;
                        fileName = "file:sprites/tile003.png";
                        break;
                    case 0:
                        break;
                    default:
                        tileColor = Color.BLACK;
                        break;
                }
                switch (MAP_ROOM[i][j]) {
                    case 9:
                        tileColor = Color.YELLOW;
                    case 0:
                        break;
                    default:
                        tileColor = Color.BLACK;
                        break;
                }
                switch (MAP_ELEVATOR[i][j]) {
                    case 2:
                        has_file = true;
                        fileName = "file:sprites/tile001.png";
                        break;
                    case 0:
                        break;
                    default:
                        tileColor = Color.BLACK;
                        break;
                }
                switch (MAP_GATE[i][j]) {
                    case 3:
                        has_file = true;
                        fileName = "file:sprites/tile002.png";
                        break;
                    case 0:
                        break;
                    default:
                        tileColor = Color.BLACK;
                        break;
                }
                switch (MAP_BED[i][j]) {
                    case 10:
                        has_file = true;
                        fileName = "file:sprites/tile005.png";
                        break;
                    case 0:
                        break;
                    default:
                        tileColor = Color.BLACK;
                        break;
                }
                switch (MAP_PATH[i][j]) {
                    case 11:
                        // wall
                        break;
                    case 0:
                        // ground
                        break;
                    case 28:
                        has_file = true;
                        fileName = "file:sprites/tile015.png";
                        dir = "right";
                        break;
                    case 22:
                        // room
                        break;
                    case 12:
                        has_file = true;
                        fileName = "file:sprites/tile007.png";
                        dir = "top";
                        break;
                    case 36:
                        has_file = true;
                        fileName = "file:sprites/tile019.png";
                        dir = "left";
                        break;
                    case 20:
                        has_file = true;
                        fileName = "file:sprites/tile011.png";
                        dir = "bottom";
                        break;
                    case 33:
                        // elevator
                        break;
                    case 42:
                        has_file = true;
                        fileName = "file:sprites/tile021.png";
                        break;
                    case 43:
                        has_file = true;
                        fileName = "file:sprites/tile022.png";
                        break;
                    case 41:
                        has_file = true;
                        fileName = "file:sprites/tile020.png";
                        break;
                    case 35:
                        has_file = true;
                        fileName = "file:sprites/tile018.png";
                        break;   
                    case 58:
                        has_file = true;
                        fileName = "file:sprites/tile026.png";
                        break;
                    case 57:
                        has_file = true;
                        fileName = "file:sprites/tile025.png";
                        break;
                    case 50:
                        has_file = true;
                        fileName = "file:sprites/tile024.png";
                        break;
                    case 49:
                        has_file = true;
                        fileName = "file:sprites/tile023.png";
                        break;
                    case 59:
                        has_file = true;
                        fileName = "file:sprites/tile027.png";
                        break;
                    default:
                        tileColor = Color.BLACK;
                        break;
                }

                switch (MAP_DOOR[i][j]){
                    case 19: 
                        image = new Image("file:sprites/tile010.png");
                        context.drawImage(image, j * TILE_WIDTH, i * TILE_HEIGHT);
                        break;
                    case 26:
                        image = new Image("file:sprites/tile013.png");
                        context.drawImage(image, j * TILE_WIDTH, i * TILE_HEIGHT);
                        break;
                    case 0:
                        break;
                    default:
                        tileColor = Color.BLACK;
                        break;
                }
                    if (!has_file) {
                        tiles[i][j] = new Tile(j * TILE_WIDTH, i * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT, tileColor, dir);
                    } else {
                        tiles[i][j] = new Tile(j * TILE_WIDTH, i * TILE_HEIGHT, fileName, dir);
                    }
                    tiles[i][j].render(context);
                
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

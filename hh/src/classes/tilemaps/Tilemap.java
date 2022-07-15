package classes.tilemaps;

import java.util.ArrayList;
import java.util.HashMap;

import application.MainScene;
import classes.tilemaps.Tile;
public class Tilemap {
    public MainScene scene;

    public Tilemap() {
    }
    public Tilemap(MainScene scene) {
    	setScene(scene);
    }
    public void setScene(MainScene scene) {
        this.scene = scene;
    }

 
}

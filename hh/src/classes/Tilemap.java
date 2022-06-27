package classes;

import java.util.ArrayList;
import java.util.HashMap;

import application.Main;
import classes.Tile;
public class Tilemap {
    public Main scene;

    public Tilemap() {
    }
    public Tilemap(Main scene) {
    	setScene(scene);
    }
    public void setScene(Main scene) {
        this.scene = scene;
    }

 
}

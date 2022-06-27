package classes;

import java.util.ArrayList;

import application.Main;
import classes.Tile;
import static config.Config.*;

public class TilemapLayer {
	public Tile[][] tiles2D;
	private ArrayList<Tile> tiles = new ArrayList<Tile>();

	public TilemapLayer(Main scene, ArrayList<Tile> tiles, String type) {
		this.tiles = tiles;
		this.tiles2D = new Tile[52][28];
		for (Tile tile : tiles) {
			this.tiles2D[tile.xPlace][tile.yPlace] = tile;
		}
	}

	public ArrayList<Tile> getTiles() {
		return this.tiles;
	}

	public ArrayList<Tile> getTilesWithinXY(double x, double y, double rangeX, double rangeY) {
        ArrayList<Tile> ans = new ArrayList<>();
        int indexX = (int) ((x + 10) / 20);
        int indexY = (int) ((y + 10) / 20);

        double startX = x + 1.0;
        double endX = x + 19 ;
        double startY = y + 1;
        double endY = y + 19;

        for(int i = -1;i <=1;i++) {
            for(int j = -1;j <= 1;j++) {
                Tile temp = this.tiles2D[indexX + i][indexY + j];
                if(temp != null) {
                    if(((temp.x <= startX && temp.x + 20 >= startX) || (temp.x <= endX && temp.x + 20 >= endX))
                            && ((temp.y <= startY && temp.y + 20 >= startY) || (temp.y <= endY && temp.y + 20 >= endY))) {
                        ans.add(temp);
                    }
                }
            }
        }
        return ans;
	}
}

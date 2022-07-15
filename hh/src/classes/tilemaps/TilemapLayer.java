package classes.tilemaps;

import java.util.ArrayList;

import application.MainScene;
import classes.tilemaps.Tile;

import static config.Config.*;

public class TilemapLayer {
	public Tile[][] tiles2D;
	private ArrayList<Tile> tiles = new ArrayList<Tile>();

	public TilemapLayer(MainScene scene, ArrayList<Tile> tiles, String type) {
		this.tiles = tiles;
		this.tiles2D = new Tile[52][28];
		for (Tile tile : tiles) {
			this.tiles2D[tile.tileX][tile.tileY] = tile;
		}
	}

	public ArrayList<Tile> getTiles() {
		return this.tiles;
	}

	public ArrayList<Tile> getTilesWithinXY(double x, double y, double rangeX, double rangeY) {
		ArrayList<Tile> ans = new ArrayList<>();
		int x_th = (int) ((x + TILE_WIDTH/2) / TILE_WIDTH);
		int y_th = (int) ((y + TILE_HEIGHT/2) / TILE_HEIGHT);

		double startX = x + 1;
		double endX = x + TILE_WIDTH-1;
		double startY = y + 1;
		double endY = y + TILE_HEIGHT-1;

		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				Tile tile = this.tiles2D[x_th + i][y_th + j];
				if (tile != null) {
					if (((tile.x <= startX && startX <= tile.x + TILE_WIDTH) || (tile.x <= endX && endX <= tile.x + TILE_WIDTH))
							&& ((tile.y <= startY && startY <= tile.y + TILE_HEIGHT)
									|| (tile.y <= endY && endY <= tile.y + TILE_HEIGHT))) {
						ans.add(tile);
					}
				}
			}
		}
		return ans;
	}
}

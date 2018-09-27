package island.entities;

import java.util.ArrayList;
import java.util.Date;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Island extends Entity {

	public static int TILE_SIZE = 50;
	public static int NUM_TILES = 10;
	public static int RESOURCE_COOLDOWN = 1000;

	public static enum TileType {
		WATER, GRASS, WOOD, STONE, BUILDING
	};

	public TileType[][] tiles;
	public ArrayList<Resource> resources;
	public long lastResource;

	public Island(int x, int y) {
		super(x, y, TILE_SIZE * NUM_TILES, TILE_SIZE * NUM_TILES);
		tiles = new TileType[NUM_TILES][NUM_TILES];
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[i].length; j++) {
				tiles[i][j] = TileType.GRASS;
			}
		}
		resources = new ArrayList<Resource>();
		lastResource = new Date().getTime();
	}

	public void render(GraphicsContext gc, int offsetX, int offsetY) {
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[i].length; j++) {
				switch (tiles[i][j]) {
				case WATER:
					gc.setFill(Color.BLUE);
					gc.fillRect(x + j * TILE_SIZE - offsetX, y + i * TILE_SIZE - offsetY, TILE_SIZE, TILE_SIZE);
					break;
				case GRASS:
					gc.setFill(Color.GREEN);
					gc.fillRect(x + j * TILE_SIZE - offsetX, y + i * TILE_SIZE - offsetY, TILE_SIZE, TILE_SIZE);
					break;
				case WOOD:
					gc.setFill(Color.BROWN);
					gc.fillRect(x + j * TILE_SIZE - offsetX, y + i * TILE_SIZE - offsetY, TILE_SIZE, TILE_SIZE);
					break;
				case STONE:
					gc.setFill(Color.GREY);
					gc.fillRect(x + j * TILE_SIZE - offsetX, y + i * TILE_SIZE - offsetY, TILE_SIZE, TILE_SIZE);
					break;
				case BUILDING:
					gc.setFill(Color.BLACK);
					gc.fillRect(x + j * TILE_SIZE - offsetX, y + i * TILE_SIZE - offsetY, TILE_SIZE, TILE_SIZE);
					break;
				default:
					break;
				}
				gc.setStroke(Color.RED);
				gc.strokeRect(x + j * TILE_SIZE - offsetX, y + i * TILE_SIZE - offsetY, TILE_SIZE, TILE_SIZE);

			}
		}
	}

	public void update() {
		if (new Date().getTime() - lastResource > RESOURCE_COOLDOWN) {
			generateResource();
		}
	}

	private void generateResource() {
		if (Math.random() * 10 < 5) {
			lastResource = new Date().getTime();
			return;
		}
		int row, col;
		do {
			row = (int) (Math.random() * NUM_TILES);
			col = (int) (Math.random() * NUM_TILES);
		} while (tiles[row][col] != TileType.GRASS);

		if (Math.random() * 10 < 5) {
			resources.add(new Resource(row, col, TileType.STONE));
			tiles[row][col] = TileType.STONE;
		} else {
			resources.add(new Resource(row, col, TileType.WOOD));
			tiles[row][col] = TileType.WOOD;
		}

		lastResource = new Date().getTime();
	}

	public void click(Player p, int x, int y) {
		for (int i = 0; i < resources.size(); i++) {
			Resource resource = resources.get(i);
			if (resource.row == y && resource.col == x) {
				if (resource.damage(p)) {
					tiles[resource.row][resource.col] = TileType.GRASS;
					resources.remove(i);
				}
			}
		}
	}

	public void interact(Player p, int x, int y) {
		// int gridX = x / TILE_SIZE;
		// int gridY = y / TILE_SIZE;
		return;
	}

	public boolean is(TileType t, int x, int y) {
		int gridX = (x - this.x) / TILE_SIZE;
		int gridY = (y - this.y) / TILE_SIZE;

		if (gridX >= NUM_TILES || gridY >= NUM_TILES || gridX < 0 || gridY < 0)
			return false;

		if (tiles[gridY][gridX] == t)
			return true;
		return false;
	}

	public boolean isGround(int x, int y) {
		return is(TileType.GRASS, x, y);
	}

	private class Resource {
		int row, col;
		int health;
		TileType type;

		public Resource(int row, int col, TileType type) {
			this.row = row;
			this.col = col;
			this.type = type;
			health = 3;
		}

		public boolean damage(Player p) {
			health--;
			if (health == 0) {
				switch (type) {
				case STONE:
					p.addItem("stone", (int) Math.floor(Math.random() * 4 + 1));
					break;
				case WOOD:
					p.addItem("wood", (int) Math.floor(Math.random() * 4 + 1));
					break;
				default:
					break;
				}
				return true;
			} else
				return false;
		}
	}
}

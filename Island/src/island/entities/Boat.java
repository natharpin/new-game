package island.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Boat extends Entity {
	
	public Boat(int x, int y){
		super(x, y);
	}
	
	public void render(GraphicsContext gc, int offsetX, int offsetY){
		gc.setFill(Color.BROWN);
		gc.fillRect(x - offsetX, y - offsetY, Player.PLAYER_SIZE + 10, Player.PLAYER_SIZE + 10);
		gc.setStroke(Color.BLACK);
		gc.strokeRect(x - offsetX, y - offsetY, Player.PLAYER_SIZE + 10, Player.PLAYER_SIZE + 10);
	}
	
	public void update(int x, int y){
		this.x = x;
		this.y = y;
	}
}

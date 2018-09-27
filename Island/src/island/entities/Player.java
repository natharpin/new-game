package island.entities;

import java.util.ArrayList;

import island.datastructures.Tuple;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.input.KeyCode;

public class Player extends Entity{
	
	public static int PLAYER_SIZE = 20;
	private int playerSpeed = 2;
	private int maxSpeed = 6;
	private ArrayList<Tuple<String, Integer>> inventory;
	public Boat boat;
	private boolean onBoat;
	
	
	public Player(int x, int y){
		super(x, y, PLAYER_SIZE, PLAYER_SIZE);
		inventory = new ArrayList<Tuple<String, Integer>>();
		onBoat = false;
		boat = new Boat(-PLAYER_SIZE - 10, 0);
	}
	
	public void render(GraphicsContext gc, int offsetX, int offsetY){
		boat.render(gc, offsetX, offsetY);
		gc.setFill(Color.LIGHTBLUE);
		gc.fillRect(x - offsetX, y - offsetY, PLAYER_SIZE, PLAYER_SIZE);
		renderInventory(gc);
	}
	
	private void renderInventory(GraphicsContext gc){
		int inventoryRow = 10;
		gc.setFill(Color.GREY);
		gc.fillRect(0, 0, 100, 100);
		gc.setFill(Color.BLACK);
		for(Tuple<String, Integer> tup : inventory){
			gc.fillText(tup.key + ": " + tup.value, 0, inventoryRow);
			inventoryRow+= 10;
		}
	}
	
	public void update(Island[] islands){
		
		for(Island isle : islands){
			if(isle == null) continue;
			if(this.intersects(isle) && isle.contains(x + vx, y + vy)){
				if(isle.is(Island.TileType.WATER, x + vx, y + vy) && onBoat){
					x+=vx;
					y+=vy;
					boat.update(x - 5, y - 5);
				}
				if(isle.isGround(x + vx, y + vy) && !onBoat){
					x+=vx;
					y+=vy;
				}
				return;
			}
		}
		
		if(onBoat){
			x+=vx;
			y+=vy;
			boat.update(x - 5, y - 5);
		}
	}
	
	public void acceptKey(KeyCode code){
		if(code == KeyCode.W && Math.abs(vy) < maxSpeed)
			vy += -playerSpeed;
		else if(code == KeyCode.S && Math.abs(vy) < maxSpeed)
			vy += playerSpeed;
		else if(code == KeyCode.A && Math.abs(vx) < maxSpeed)
			vx += -playerSpeed;
		else if(code == KeyCode.D && Math.abs(vx) < maxSpeed)
			vx += playerSpeed;
	}	
	
	public void releaseKey(KeyCode code){
		if(code == KeyCode.W)
			vy = 0;
		else if(code == KeyCode.S)
			vy = 0;
		else if(code == KeyCode.A)
			vx = 0;
		else if(code == KeyCode.D)
			vx = 0;
	}
	
	public void addItem(String item, int amt){
		for(Tuple<String, Integer> tup : inventory){
			if(tup.key == item){
				tup.value += amt;
				return;
			}
		}
		inventory.add(new Tuple<String, Integer>(item, amt));
	}
	
	public void embark(){
		x = boat.getX();
		y = boat.getY();
		onBoat = true;
	}
}

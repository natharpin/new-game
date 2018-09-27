package island.entities;

import java.util.ArrayList;

import island.datastructures.Recipe;
import javafx.scene.canvas.GraphicsContext;

public class Building extends InteractableObject {
	
	public ArrayList<Recipe> recipes;

	public Building(int x, int y) {
		super(x, y);
		recipes = new ArrayList<Recipe>();
	}

	@Override
	public void interact(Island[] isle, Player p) {
		
		
	}

	@Override
	public void render(GraphicsContext gc, int offsetX, int offsetY) {
		
	}
	
}

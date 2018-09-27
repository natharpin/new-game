package island.entities;

public abstract class InteractableObject extends Entity {

	public InteractableObject(int x, int y) {
		super(x, y);
	}
	
	public InteractableObject(int x, int y, int length, int width) {
		super(x, y, length, width);
	}

	public abstract void interact(Island[] isle, Player p);
}

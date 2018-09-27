package island.entities;

import java.util.function.Supplier;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Button extends Entity {

	public String text;
	public Supplier<String> onClick;
	
	public Button(String text, Supplier<String> onClick, int x, int y){
		super(x - 25, y - 50);
		this.text = text;
		this.onClick = onClick;
		this.length = 100;
		this.width = 50;
	}
	
	public void render(GraphicsContext gc, int offsetX, int offsetY){
		gc.setFill(Color.DARKGREY);
		gc.fillRect(x, y, length, width);
		gc.setFill(Color.BLACK);
		gc.fillText(text, x + 25, y + 25);
	}
}

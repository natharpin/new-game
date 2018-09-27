package island.window;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.*;

import island.datastructures.Tuple;
import island.entities.Button;
import island.entities.Island;
import island.entities.Player;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.input.KeyCode;

public class Driver extends Application {
	private GraphicsContext gc;

	Island[] islands;
	Player player;
	int reticleX, reticleY, reticleGridX, reticleGridY;
	int offsetX, offsetY;
	int debugPlayerX, debugPlayerY;
	boolean reticleVisible, menuOpen;
	ArrayList<Button> buttons;
	
	/**
	 * Set up initial data structures/values
	 */
	void initialize() {
		islands = new Island[9];
		islands[0] = new Island(0,0);
		player = new Player(0,0);
		reticleVisible = false;
		menuOpen = false;
		buttons = new ArrayList<Button>();
	}

	void centerScreen(){
		offsetX = player.getX() - WindowSettings.WIDTH / 2;
		offsetY = player.getY() - WindowSettings.HEIGHT / 2;
	}
	
	int floor(int to, int num){
		if(num > 0)
			return num - (num % to);
		else if(num < 0)
			return num - (to + (num % to));
		else
			return 0;
	}
	
	void placeReticle(double x, double y){
		int alignedX = (int) (x + offsetX);
		int alignedY = (int) (y + offsetY);
		
		reticleX = floor(Island.TILE_SIZE, alignedX);
		reticleY = floor(Island.TILE_SIZE, alignedY);
		
		reticleGridX = reticleX / Island.TILE_SIZE;
		reticleGridY = reticleY / Island.TILE_SIZE;
		
		int playerGridX = player.getX() / Island.TILE_SIZE;
		int playerGridY = player.getY() / Island.TILE_SIZE;
		
		if(Math.abs(playerGridX - reticleGridX) > 1 || Math.abs(playerGridY - reticleGridY) > 1)
			reticleVisible = false;
		else
			reticleVisible = true;
	}
	
	void setHandlers(Scene scene) {
		scene.setOnMousePressed(e -> {
			if(menuOpen){
				buttons.forEach(button -> { 
					if(button.contains((int)e.getX(), (int)e.getY())){
						button.onClick.get();
					}});
			}
			else if(reticleVisible){
				int x = (int) e.getX() + offsetX;
				int gridX = floor(Island.TILE_SIZE, x) / Island.TILE_SIZE;
				int y = (int) e.getY() + offsetY;
				int gridY = floor(Island.TILE_SIZE, y) / Island.TILE_SIZE;
				for(Island i : islands){
					if(i == null) continue;
					if(i.contains(x, y))
						i.click(player, gridX, y=gridY);
				}
			}
		});

		scene.setOnMouseMoved(e -> {
			if(!menuOpen)
				placeReticle(e.getX(), e.getY());
		});

		scene.setOnKeyPressed(e -> {
			if(e.getCode() == KeyCode.E && !menuOpen){
				for(Island i : islands){
					if(i == null) continue;
					if(i.contains(reticleX, reticleY)){
						i.interact(player, reticleX, reticleY);
						return;
					}
				}
				int boatGridX = floor(Island.TILE_SIZE, player.boat.getX()) / Island.TILE_SIZE;
				int boatGridY = floor(Island.TILE_SIZE, player.boat.getY()) / Island.TILE_SIZE;
				if(reticleGridX == boatGridX && reticleGridY == boatGridY)
					player.embark();
			}
			else if(e.getCode() == KeyCode.TAB && !menuOpen){
				menuOpen = true;
				//player.displayInventory();
			}
			else if(e.getCode() == KeyCode.ESCAPE){
				menuOpen = !menuOpen;
			}
			else if(!menuOpen)
				player.acceptKey(e.getCode());
		});

		scene.setOnKeyReleased(e -> {
			player.releaseKey(e.getCode());
		});

	}

	/**
	 * Update variables for one time step
	 */
	public void update() {
		if(menuOpen) return;
		for(Island i : islands){
			if(i == null) continue;
			i.update();
		}
		player.update(islands);
		centerScreen();
	}

	/**
	 * Draw the game world
	 */
	void render(GraphicsContext gc) {
		gc.setFill(Color.DARKBLUE);
		gc.fillRect(0,0,WindowSettings.WIDTH,WindowSettings.HEIGHT);
		for(Island i : islands){
			if(i == null) continue;
			i.render(gc, offsetX, offsetY);
		}
		if(reticleVisible){
			gc.setStroke(Color.YELLOW);
			gc.strokeRect(reticleX - offsetX, reticleY - offsetY, Island.TILE_SIZE, Island.TILE_SIZE);
		}
		player.render(gc, offsetX, offsetY);
		if(menuOpen)
			renderMenu(gc);
	}
	
	String exit(){
		System.exit(0);
		return "";
	}
	
	void renderMenu(GraphicsContext gc){
		buttons.add(new Button("Quit", () -> exit(), WindowSettings.WIDTH / 2, WindowSettings.HEIGHT / 2));
		gc.setFill(Color.rgb(200,200,200,.5));
		gc.fillRect(0, 0, WindowSettings.WIDTH, WindowSettings.HEIGHT);
		gc.setFill(Color.BLACK);
		buttons.forEach(button -> button.render(gc, 0, 0));
	}

	/*
	 * Begin boiler-plate code... [Animation and events with initialization]
	 */
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage theStage) {
		theStage.setTitle(WindowSettings.appName);

		Group root = new Group();
		Scene theScene = new Scene(root);
		theStage.setScene(theScene);

		Canvas canvas = new Canvas(WindowSettings.WIDTH, WindowSettings.HEIGHT);
		root.getChildren().add(canvas);

		gc = canvas.getGraphicsContext2D();

		// Initial setup
		initialize();
		setHandlers(theScene);

		// Setup and start animation loop (Timeline)
		KeyFrame kf = new KeyFrame(Duration.millis(1000 / WindowSettings.FPS), e -> {
			// update position
			update();
			// draw frame
			render(gc);
		});
		Timeline mainLoop = new Timeline(kf);
		mainLoop.setCycleCount(Animation.INDEFINITE);
		mainLoop.play();

		theStage.show();
	}
	/*
	 * ... End boiler-plate code
	 */
}

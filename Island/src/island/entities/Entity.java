package island.entities;

import javafx.scene.canvas.GraphicsContext;

public abstract class Entity {

	protected int x, y, vx, vy, length, width;
	
	public Entity(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public Entity(int x, int y, int length, int width){
		this.x = x;
		this.y = y;
		this.length = length;
		this.width = width;
	}
	
	public abstract void render(GraphicsContext gc, int offsetX, int offsetY);
	public void update(){
		x+=vx;
		y+=vy;
	}
	public boolean intersects(Entity e){
		if(e.contains(x, y) || e.contains(x + length, y) || e.contains(x, y + width) || e.contains(x + length, y + width)) return true;
		if(this.contains(e.getX(), e.getY()) || this.contains(e.getX() + e.getLength(), e.getY()) || this.contains(e.getX(), e.getY() + e.getWidth()) || this.contains(e.getX() + e.getLength(), e.getY() + e.getWidth())) return true;
		return false;
	}
	
	public boolean contains(int ax, int ay){
		if(ax < x || ax > x + width) return false;
		if(ay < y || ay > y + length) return false;
		return true;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public int getLength(){
		return length;
	}
	
	public int getWidth(){
		return width;
	}
	
	public void setX(int a){
		x = a;
	}
	
	public void setY(int a){
		y = a;
	}
	
	public void addV(int dvx, int dvy){
		vx+=dvx;
		vy+=dvy;
	}
	
}

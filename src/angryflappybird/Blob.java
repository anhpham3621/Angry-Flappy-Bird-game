package angryflappybird;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Blob extends SpriteAbstract{
	
	public Image image;
	public double positionX;
	public double positionY;
	public double velocityX;
	public double velocityY;
	public double width;
	public double height;
	
	public Blob() {
		super();
	}
	
	public Blob(double pX, double pY, Image img) {
		super(pX, pY, img);
	}

	public void setVelocity(double vX, double vY) {
		super.setVelocity(vX, vY);
	}
	
	public void setPositionXY(double pX, double pY) {
		super.setPositionXY(pX, pY);
	}
	
	public double getPositionX() {
		return super.getPositionX();
	}
	
	public double getPositionY() {
		return super.getPositionX();
	}
	
	public void update(double time) {
		super.update(time);
	}
	
	public void setImage(Image img) {
		super.setImage(img);
	}
	
	public void render(GraphicsContext gc) {
		super.render(gc);
	}
	
    public boolean intersectsPipe(Pipe p) {
        return p.getBoundary().intersects(this.getBoundary());
    }
    
    public boolean intersectsPig(Pig p) {
    	return p.getBoundary().intersects(this.getBoundary());
    }
    
    public boolean intersectsSprite(Sprite e) {
    	return super.intersectsSprite(e);
    }
}

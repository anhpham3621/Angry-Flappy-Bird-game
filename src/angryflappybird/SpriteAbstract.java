package angryflappybird;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class SpriteAbstract {
	
	public Image image;
	public double positionX;
	public double positionY;
	public double velocityX;
	public double velocityY;
	public double width;
	public double height;
	
	public SpriteAbstract() {
		this.positionX = 0;
		this.positionY = 0;
		this.velocityX = 0;
		this.velocityY = 0;
	}
	
    public SpriteAbstract(double pX, double pY, Image image) {
    	setPositionXY(pX, pY);
        setImage(image);
        this.velocityX = 0;
        this.velocityY = 0;
    }
    
    public SpriteAbstract(int bLOB_POS_X, int bLOB_POS_Y, Image image2) {
		// TODO Auto-generated constructor stub
    	setPositionXY(bLOB_POS_X, bLOB_POS_Y);
        setImage(image2);
        this.velocityX = 0;
        this.velocityY = 0;
	}
    
	public void setImage(Image image) {
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
    }

    public void setPositionXY(double positionX, double positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public void setVelocity(double velocityX, double velocityY) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }
    
    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }
	

    public double getVelocityX() {
        return velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }
    
    public void render(GraphicsContext gc) {
        gc.drawImage(image, positionX, positionY);
    }

    public Rectangle2D getBoundary() {
        return new Rectangle2D(positionX, positionY, width, height);
    }

    public boolean intersectsSprite(Sprite s) {
        return s.getBoundary().intersects(this.getBoundary());
    }

    public void update(double time) {
        positionX += velocityX * time;
        positionY += velocityY * time;
    }
}

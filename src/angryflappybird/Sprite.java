/**
 * @author Emmanuella Umoye, Keisha Modi, Anh Pham
 * @date 12/17/2023
 */
package angryflappybird;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;

/**
 * This class is used to create and manage sprite objects such as pipes, pigs etc in
 * the AngryFlappyBird game.
 */
public class Sprite {  
	
    private Image image;
    private double positionX;
    private double positionY;
    private double velocityX;
    private double velocityY;
    private double width;
    private double height;
    private AudioClip collisionSound;
    private boolean isVisible = true;
    private boolean isDisabled = false;
    private String IMAGE_DIR = "../resources/images/";

    public Sprite() {
        this.positionX = 0;
        this.positionY = 0;
        this.velocityX = 0;
        this.velocityY = 0;
    }
    
    public void init() {
    	positionX = 50;
		positionY = 50;
		velocityX = 50;
		velocityY = 50;
    }
    
    public Sprite(double pX, double pY, Image image) {
    	setPositionXY(pX, pY);
        setImage(image);
        this.velocityX = 0;
        this.velocityY = 0;
    }

    public Sprite(int bLOB_POS_X, int bLOB_POS_Y, Image image2) {
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

    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public void setVelocity(double velocityX, double velocityY) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    public void addVelocity(double x, double y) {
        this.velocityX += x;
        this.velocityY += y;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public double getWidth() {
        return width;
    }

    public void render(GraphicsContext gc) {
        gc.drawImage(image, positionX, positionY);
    }

    public Rectangle2D getBoundary() {
        return new Rectangle2D(positionX, positionY, width, height);
    }

    public boolean intersectsPipe(Sprite uPipe) {
        return uPipe.getBoundary().intersects(this.getBoundary());
    }

    public void update(double time) {
        positionX += velocityX * time;
        positionY += velocityY * time;
    }
    
    public void setVisible(boolean visible) {
    	isVisible = visible;
    }
    
    public boolean isVisible() {
    	return isVisible;
    }

	public boolean intersectsSprite(Sprite egg) {
        return egg.getBoundary().intersects(this.getBoundary());
	}
}

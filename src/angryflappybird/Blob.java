package angryflappybird;

import javafx.animation.TranslateTransition;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class Blob extends SpriteAbstract{
	
	public Image image;
	public double positionX;
	public double positionY;
	public double velocityX;
	public double velocityY;
	public double width;
	public double height;
	private MediaPlayer collisionSound;
	private String blob_image_name;
	
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
    
    public boolean intersectsPig(Sprite pig) {
    	return pig.getBoundary().intersects(this.getBoundary());
    }
    
    public boolean intersectsSprite(Sprite e) {
    	return super.intersectsSprite(e);
    }
    
    public void setCollisionSound(MediaPlayer media) {
        collisionSound = media;
    }
    
    public void playCollisionSound() {
        collisionSound.play();
    }
    
    public void stopCollisionSound() {
    	collisionSound.stop();
    }
    
    public void applyBounceAnimation() {
    	//System.out.println("the bouncer is bouncing");
        // Create a TranslateTransition for the bounce effect
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(5));

        // Set the initial and final positions for the bounce effect
//        translateTransition.setFromY(10);
//        translateTransition.setToY(30); // Adjust the bounce height as needed
        
        translateTransition.setFromX(300);
        translateTransition.setToX(100);


        // Set the number of cycles for the bounce effect
        translateTransition.setCycleCount(TranslateTransition.INDEFINITE);

        // Set the auto-reverse property to true for a smoother bounce effect
        translateTransition.setAutoReverse(true);

        // Play the animation
        translateTransition.play();
    }
    
    /**
     * Sets the image name of the blob
     * @param image_name
     */
    void setImageName(String image_name) {
    	blob_image_name = image_name;
    }
    
    /**
     * gets the name of the image
     * @return
     */
    public String blobGetName() {
    	return blob_image_name;
    }
}

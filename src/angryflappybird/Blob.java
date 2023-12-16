/**
 * @author Emmanuella Umoye, Keisha Modi, Anh Pham
 */
package angryflappybird;

import javafx.animation.TranslateTransition;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 * The blob class is a child class of the sprite class. It is responsible for defining the blob properties
 */
public class Blob extends Sprite{
	
	//Initializing blob instance properties
	public Image image;
	public double positionX;
	public double positionY;
	public double velocityX;
	public double velocityY;
	public double width;
	public double height;
	private MediaPlayer sound;
	private String blob_image_name;
	
	/**
	 * @param None
	 * Declaring a blob with default properties set to 0 or empty character
	 */
	public Blob() {
		super();
	}
	
	/**
	 * @param pX
	 * @param pY
	 * @param img
	 * This blob constructor assigns X-axis, Y-axis and an image to the blob.
	 */
	public Blob(double pX, double pY, Image img) {
		super(pX, pY, img);
	}
	
	/**
	 * init method is for unittests ONLY
	 */
	public void init() {
		super.init();
	}

	/**
	 * @param vX
	 * @param vY
	 * setVelocity sets the velocity of the blob
	 */
	public void setVelocity(double vX, double vY) {
		super.setVelocity(vX, vY);
	}
	
	/**
	 * @param pX
	 * @param pY
	 * setPositionXY sets the X and Y axis of the blob
	 */
	public void setPositionXY(double pX, double pY) {
		super.setPositionXY(pX, pY);
	}
	
	/**
	 * @param none
	 * getPositionX returns the X-axis position of the blob
	 */
	public double getPositionX() {
		return super.getPositionX();
	}
	
	/**
	 * @param none
	 * getPositionY returns the Y-axis position of the blob
	 */
	public double getPositionY() {
		return super.getPositionX();
	}
	
	/** 
	 * @param time
	 * update updates the time frequency for the next velocity of the 
	 */
	public void update(double time) {
		super.update(time);
	}
	
	/**
	 * @param img
	 * setImage sets the image for the blob
	 */
	public void setImage(Image img) {
		super.setImage(img);
	}
	
	/**
	 * @param None
	 * render renders the graphic on the scene
	 */
	public void render(GraphicsContext gc) {
		super.render(gc);
	}
    
    /**
     * @param pig
     * @return True if the pig intersects the blob
     * intersectsPig checks for an intersection of the blob with the pig. It returns true if there is an
     * intersection. False otherwise
     */
    public boolean intersectsPig(Sprite pig) {
    	return pig.getBoundary().intersects(this.getBoundary());
    }
    
    
    /**
     * @param Sprite
     * @return boolean
     * intersectsSprite checks for an intersection of the blob with the another sprite(e.g pipes, egg).
     * It returns true if there is an intersection. False otherwise
     */
    public boolean intersectsSprite(Sprite e) {
    	return super.intersectsSprite(e);
    }
    
    /**
     * @param media
     * setCollisionSound sets the sound of the game
     */
    public void setSound(MediaPlayer media) {
        sound = media;
    }
    
    /**
     * @param none
     * playSound plays the sound of the game
     */
    public void playSound() {
        sound.play();
    }
    
    /**
     * @param None
     * stopSound stops the game sound
     */
    public void stopSound() {
    	sound.stop();
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

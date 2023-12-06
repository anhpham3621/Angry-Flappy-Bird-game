package angryflappybird;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Pig extends SpriteAbstract{

	



		
		public Image image;
		public double positionX;
		public double positionY;
		public double velocityX;
		public double velocityY;
		public double width;
		public double height;
		
		public Pig() {
			super();
		}
		
		public Pig(double pX, double pY, Image img) {
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
		
		public Rectangle2D getBoundary() {
			return super.getBoundary();
		}
		


}

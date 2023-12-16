/**
 * 
 */
package angryflappybird;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The SpriteTest is responsible for testing out all methods in the unittest file
 */
class SpriteTest extends Sprite {

	/**
	 * Test method for setting the XY-axis position of the sprite
	 */
	@Test
	final void testSetPositionXY() {
		Sprite sprite = new Sprite();
		sprite.init();
		assertEquals(sprite.getPositionX(), 50);
		assertEquals(sprite.getPositionY(), 50);
	}

	/**
	 * Test method for setting the sprite velocity
	 */
	@Test
	final void testSetVelocity() {
		Sprite sprite = new Sprite();
		sprite.init();
		assertEquals(sprite.getVelocityX(), 50);
		assertEquals(sprite.getVelocityY(), 50);	}

	/**
	 * Test method for getting the X-axis position of the sprite
	 */
	@Test
	final void testGetPositionX() {
		Sprite sprite = new Sprite();
		sprite.init();
		assertEquals(50, sprite.getPositionX());
	}

	/**
	 * Test method for getting the Y-axis position of the sprite
	 */
	@Test
	final void testGetPositionY() {
		Sprite sprite = new Sprite();
		sprite.init();
		assertEquals(50, sprite.getPositionY());
	}

	/**
	 * Test method for getting the X-axis position of the sprite
	 */
	@Test
	final void testGetVelocityX() {
		Sprite sprite = new Sprite();
		sprite.init();
		assertEquals(50, sprite.getVelocityX());
	}

	/**
	 * Test method for getting the downward velocity of the sprite
	 */
	@Test
	final void testGetVelocityY() {
		Sprite sprite = new Sprite();
		sprite.init();
		assertEquals(50, sprite.getVelocityY());
	}


	/**
	 * Test method for updating the Sprite's X and Y axis position
	 */
	@Test
	final void testUpdate() {
		Sprite sprite = new Sprite();
		sprite.init();
		double oldPosX = sprite.getPositionX();
		double oldPosY = sprite.getPositionY();
		sprite.update(50);
		double newPosX = sprite.getPositionX();
		double newPosY = sprite.getPositionY();
		oldPosX = 2550;
		oldPosY = 2550;
		assertEquals(oldPosX, newPosX);
		assertEquals(oldPosY, newPosY);
	}
}
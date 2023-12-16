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
 * This is a unittest for the SpriteAbstractclass
 */
class SpriteAbstractTest extends SpriteAbstract {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		SpriteAbstract sprite = new SpriteAbstract();
		sprite.init();
	}

	/**
	 * Test method for {@link angryflappybird.SpriteAbstract#setPositionXY(double, double)}.
	 */
	@Test
	final void testSetPositionXY() {
		SpriteAbstract sprite = new SpriteAbstract();
		sprite.init();
		assertEquals(sprite.getPositionX(), 50);
		assertEquals(sprite.getPositionY(), 50);
	}

	/**
	 * Test method for {@link angryflappybird.SpriteAbstract#setVelocity(double, double)}.
	 */
	@Test
	final void testSetVelocity() {
		SpriteAbstract sprite = new SpriteAbstract();
		sprite.init();
		assertEquals(sprite.getVelocityX(), 50);
		assertEquals(sprite.getVelocityY(), 50);	}

	/**
	 * Test method for {@link angryflappybird.SpriteAbstract#getPositionX()}.
	 */
	@Test
	final void testGetPositionX() {
		SpriteAbstract sprite = new SpriteAbstract();
		sprite.init();
		assertEquals(50, sprite.getPositionX());
	}

	/**
	 * Test method for {@link angryflappybird.SpriteAbstract#getPositionY()}.
	 */
	@Test
	final void testGetPositionY() {
		SpriteAbstract sprite = new SpriteAbstract();
		sprite.init();
		assertEquals(50, sprite.getPositionY());
	}

	/**
	 * Test method for {@link angryflappybird.SpriteAbstract#getVelocityX()}.
	 */
	@Test
	final void testGetVelocityX() {
		SpriteAbstract sprite = new SpriteAbstract();
		sprite.init();
		assertEquals(50, sprite.getVelocityX());
	}

	/**
	 * Test method for {@link angryflappybird.SpriteAbstract#getVelocityY()}.
	 */
	@Test
	final void testGetVelocityY() {
		SpriteAbstract sprite = new SpriteAbstract();
		sprite.init();
		assertEquals(50, sprite.getVelocityY());
	}


	/**
	 * Test method for {@link angryflappybird.SpriteAbstract#update(double)}.
	 */
	@Test
	final void testUpdate() {
		SpriteAbstract sprite = new SpriteAbstract();
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
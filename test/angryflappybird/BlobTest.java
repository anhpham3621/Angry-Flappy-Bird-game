package angryflappybird;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * This serves as the unittest for non-ui related blob methods
 */
class BlobTest {
	
	/**
	 * Test method for getting the X-axis position of the sprite
	 */
	@Test
	final void testGetPositionX() {
		Blob blob = new Blob();
		blob.init();
		assertEquals(50, blob.getPositionX());
	}
	
	/**
	 * Test method for getting the Y-axis position of the sprite
	 */
	@Test
	final void testGetPositionY() {
		Blob blob = new Blob();
		blob.init();
		assertEquals(50, blob.getPositionY());
	}

	/**
	 * Tests the getVelocityX method of the blob class
	 */
	@Test
	final void testGetVelocityX() {
		Blob blob = new Blob();
		blob.init();
		assertEquals(50, blob.getVelocityX());
	}
	
	/**
	 * Tests the getVelocityY method of the blob class
	 */
	@Test
	final void testGetVelocityY() {
		Blob blob = new Blob();
		blob.init();
		assertEquals(50, blob.getVelocityY());
	}
	
	/**
	 * tests the setVelocity method of the blob class.
	 */
	@Test
	final void testSetVelocity() {
		Blob blob = new Blob();
		blob.init();
		blob.setVelocity(50, 50);
		assertEquals(50, blob.getVelocityX());
	}
	
	/**
	 * Tests the update method for updating the X and Y position
	 */
	@Test
	final void testUpdate() {
		Blob blob = new Blob();
		blob.init();
		double oldPosX = blob.getPositionX();
		double oldPosY = blob.getPositionY();
		blob.update(50);
		double newPosX = blob.getPositionX();
		double newPosY = blob.getPositionY();
		oldPosX = 2550;
		oldPosY = 2550;
		assertEquals(oldPosX, newPosX);
		assertEquals(oldPosY, newPosY);
	}

	/**
	 * tests the BlobGetName method
	 */
	@Test
	final void testBlobGetName() {
		Blob blob = new Blob();
		blob.init();
		String blobName = "blob";
		blob.setImageName(blobName);
		assertEquals(blob.blobGetName(), "blob");
	}

}

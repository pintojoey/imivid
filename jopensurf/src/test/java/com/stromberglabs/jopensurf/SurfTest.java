package com.stromberglabs.jopensurf;

import static java.lang.Thread.currentThread;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class SurfTest {

	/**
	 * This is a simple function that just tests that the high level output
	 * from lenna.png . I am using this to test refactoring so that I can insure
	 * that the output of the algorithm didn't change
	 *
	 * @throws Exception
	 */
	@Test
	public void testSURFOutput() throws Exception {
        InputStream originalImage = currentThread().getContextClassLoader().getResourceAsStream("example/lenna_surf_test.bin");
        InputStream imageStream = currentThread().getContextClassLoader().getResourceAsStream("example/lenna.png");
		Surf original = Surf.readInputStream(originalImage);
		Surf current = new Surf(ImageIO.read(imageStream));
		assertTrue(original.isEquivalentTo(current));
	}
}

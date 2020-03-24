package GUI.drawing;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImagesUtility {
	private final String INFANTRY_IMAGE_PATH = "src/images/infantry.jpg";
	
	private static Image infantryImage;

	public ImagesUtility() {
		loadImages();
	}

	private void loadImages() {
		loadImage(infantryImage, INFANTRY_IMAGE_PATH);
	}

	private void loadImage(Image image, String path) {
		try {
			image = ImageIO.read(new File(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

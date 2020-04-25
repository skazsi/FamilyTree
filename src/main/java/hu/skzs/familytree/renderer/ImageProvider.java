package hu.skzs.familytree.renderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;

import hu.skzs.familytree.Person;

class ImageProvider {

	@Value("${renderer.imageSize}")
	private int imageSize;

	private Image noImage;

	Image getImage(Person person) {
		byte[] imageBytes = person.getImage();

		if (imageBytes != null) {
			return getImage(new ByteArrayInputStream(imageBytes));
		}

		if (noImage == null) {
			noImage = getImage(ImageProvider.class.getResourceAsStream("noimage.jpg"));
		}
		return noImage;
	}

	private Image getImage(InputStream inputStream) {
		try {
			BufferedImage bufferedImage = ImageIO.read(inputStream);
			bufferedImage = cropImageToSquare(bufferedImage);
			bufferedImage = cropImageToSquare(bufferedImage);
			bufferedImage = resize(bufferedImage);
			bufferedImage = cropToCircle(bufferedImage);
			return decorateImage(bufferedImage);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private BufferedImage cropImageToSquare(BufferedImage bufferedImage) {
		if (bufferedImage.getWidth() > bufferedImage.getHeight()) {
			int position = (bufferedImage.getWidth() - bufferedImage.getHeight()) / 2;
			return bufferedImage.getSubimage(position, 0, bufferedImage.getHeight(), bufferedImage.getHeight());
		}

		int position = (bufferedImage.getHeight() - bufferedImage.getWidth()) / 2;
		return bufferedImage.getSubimage(0, position, bufferedImage.getWidth(), bufferedImage.getWidth());
	}

	private BufferedImage resize(BufferedImage bufferedImage) {
		Image image = bufferedImage.getScaledInstance(imageSize, imageSize, Image.SCALE_SMOOTH);
		BufferedImage resizedBufferedImage = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = resizedBufferedImage.createGraphics();
		graphics.drawImage(image, 0, 0, null);
		graphics.dispose();
		return resizedBufferedImage;
	}

	private BufferedImage cropToCircle(BufferedImage bufferedImage) {
		ImageFilter filter = new RGBImageFilter() {

			private double threshold = Math.pow((imageSize - 1) / 2, 2);

			@Override
			public final int filterRGB(int x, int y, int rgb) {
				int h = x - imageSize / 2;
				int v = y - imageSize / 2;

				if (Math.pow(h, 2) + Math.pow(v, 2) > threshold) {
					return 0x00FFFFFF & rgb;
				} else {
					return rgb;
				}
			}
		};

		ImageProducer imageProducer = new FilteredImageSource(bufferedImage.getSource(), filter);
		Image image = Toolkit.getDefaultToolkit().createImage(imageProducer);

		BufferedImage croppedBufferedImage = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = croppedBufferedImage.createGraphics();
		graphics.drawImage(image, 0, 0, null);
		graphics.dispose();
		return croppedBufferedImage;
	}

	private BufferedImage decorateImage(BufferedImage bufferedImage) {
		Graphics2D graphics = bufferedImage.createGraphics();
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setRenderingHints(rh);
		graphics.setStroke(new BasicStroke(1));
		graphics.setColor(new Color(255, 255, 255));
		graphics.drawOval(1, 1, bufferedImage.getWidth() - 3, bufferedImage.getHeight() - 3);
		graphics.setColor(Color.blue);
		graphics.drawOval(0, 0, bufferedImage.getWidth() - 1, bufferedImage.getHeight() - 1);
		return bufferedImage;
	}
}

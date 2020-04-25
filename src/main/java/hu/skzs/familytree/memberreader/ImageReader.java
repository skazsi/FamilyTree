package hu.skzs.familytree.memberreader;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ImageReader {

	private static final Logger LOGGER = LoggerFactory.getLogger(ImageReader.class);

	void readImage(String folder, CsvMember csvMember) {
		File imageFile = new File(folder, csvMember.getId() + ".jpg");

		if (imageFile.exists()) {
			LOGGER.info("Reading up {} image for {}", imageFile, csvMember);
			try {
				csvMember.setImage(FileUtils.readFileToByteArray(imageFile));
			} catch (IOException e) {
				LOGGER.error("Unable to read " + imageFile);
			}

		}
	}
}

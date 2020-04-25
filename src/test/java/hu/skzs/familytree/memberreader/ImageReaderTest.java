package hu.skzs.familytree.memberreader;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ImageReaderTest {

	@TempDir
	File tempDir;

	private File imageFile;

	@Mock
	private CsvMember csvMember;

	@BeforeEach
	void beforeEach() throws IOException {
		imageFile = new File(tempDir, "foo.jpg");
		FileUtils.writeByteArrayToFile(imageFile, new byte[] { 42, 43, 44, 45, 46 });
	}

	@Test
	void readImage() {
		// Given
		ImageReader underTest = new ImageReader();

		given(csvMember.getId()).willReturn("foo");

		// When
		underTest.readImage(tempDir.getAbsolutePath(), csvMember);

		// Then
		then(csvMember).should().setImage(new byte[] { 42, 43, 44, 45, 46 });
	}

	@Test
	void readImage_NoSuchFile() {
		// Given
		ImageReader underTest = new ImageReader();

		given(csvMember.getId()).willReturn("bar");

		// When
		underTest.readImage(tempDir.getAbsolutePath(), csvMember);

		// Then
		then(csvMember).shouldHaveNoMoreInteractions();
	}
}

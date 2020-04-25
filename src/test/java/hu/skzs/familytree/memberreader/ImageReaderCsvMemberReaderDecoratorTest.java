package hu.skzs.familytree.memberreader;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ImageReaderCsvMemberReaderDecoratorTest {

	@Mock
	private CsvMemberReader decorated;

	@Mock
	private ImageReader imageReader;

	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	private File csvFile;

	@Mock
	private CsvMember csvMember1, csvMember2;

	@Test
	void constructor_NullDecorated() {
		// When
		Throwable throwable = catchThrowable(() -> new ImageReaderCsvMemberReaderDecorator(null, imageReader));

		// Then
		then(throwable).isInstanceOf(NullPointerException.class);
	}

	@Test
	void constructor_NullImageReader() {
		// When
		Throwable throwable = catchThrowable(() -> new ImageReaderCsvMemberReaderDecorator(decorated, null));

		// Then
		then(throwable).isInstanceOf(NullPointerException.class);
	}

	@Test
	void read() {
		// Given
		Map<String, CsvMember> expected = new HashMap<>();
		expected.put("foo", csvMember1);
		expected.put("bar", csvMember2);
		given(decorated.read(csvFile)).willReturn(expected);

		given(csvFile.getAbsoluteFile().getParent()).willReturn("parent folder");

		ImageReaderCsvMemberReaderDecorator underTest = new ImageReaderCsvMemberReaderDecorator(decorated, imageReader);

		// When

		Map<String, CsvMember> actual = underTest.read(csvFile);

		// Then
		then(actual).isEqualTo(expected);

		BDDMockito.then(imageReader).should().readImage("parent folder", csvMember1);
		BDDMockito.then(imageReader).should().readImage("parent folder", csvMember2);
	}
}

package hu.skzs.familytree.memberreader;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberReaderTest {

	@Mock
	private CsvMemberReader csvMemberReader;

	@Mock
	private ImageReader imageReader;

	@Test
	void constructor_NullCsvMemberReader() {
		// When
		Throwable throwable = catchThrowable(() -> new MemberReader(null));

		// Then
		then(throwable).isInstanceOf(NullPointerException.class);
	}
}

package hu.skzs.familytree.memberreader;

import static org.assertj.core.api.BDDAssertions.then;

import java.io.File;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DefaultCsvMemberReaderTest {

	@TempDir
	File tempDir;

	private File csvFile;

	@BeforeEach
	void beforeEach() throws IOException {
		csvFile = new File(tempDir, "test.csv");
	}

	@Test
	void read() {
		// Given
		givenCsvLines("id,partnerId,name,motherId,fatherId,birthDate,birthPlace,died",
				"john,jane,John Doe,,,1950-01-01,John's Birthplace,2000-12-30",
				"jane,john,Jane Doe,,,1960-02-02,Jane's Birthplace,",
				"bobby,,Bobby,jane,john,1990-03-03,Bobby's Birthplace,");

		DefaultCsvMemberReader underTest = new DefaultCsvMemberReader();

		// When
		Map<String, CsvMember> csvMembers = underTest.read(csvFile);

		// Then
		then(csvMembers.size()).isEqualTo(3);

		then(csvMembers.get("john"))
		.extracting("id", "partnerId", "name", "motherId", "fatherId", "birthDate", "birthPlace", "died")
		.containsExactly("john", "jane", "John Doe", null, null, new GregorianCalendar(1950, 0, 1).getTime(),
				"John's Birthplace", new GregorianCalendar(2000, 11, 30).getTime());

		then(csvMembers.get("jane"))
		.extracting("id", "partnerId", "name", "motherId", "fatherId", "birthDate", "birthPlace", "died")
		.containsExactly("jane", "john", "Jane Doe", null, null, new GregorianCalendar(1960, 1, 2).getTime(),
				"Jane's Birthplace", null);

		then(csvMembers.get("bobby"))
		.extracting("id", "partnerId", "name", "motherId", "fatherId", "birthDate", "birthPlace", "died")
		.containsExactly("bobby", null, "Bobby", "jane", "john", new GregorianCalendar(1990, 2, 3).getTime(),
				"Bobby's Birthplace", null);
	}

	private void givenCsvLines(String... lines) {
		try {
			FileUtils.writeLines(csvFile, "UTF-8", Arrays.asList(lines));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}
}

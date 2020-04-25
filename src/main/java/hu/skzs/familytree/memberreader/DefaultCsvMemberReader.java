package hu.skzs.familytree.memberreader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

class DefaultCsvMemberReader implements CsvMemberReader {

	@Override
	public Map<String, CsvMember> read(File csvFile) {
		try (Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile), "UTF-8"))) {

			CsvToBean<CsvMember> csvToMembers = new CsvToBeanBuilder<CsvMember>(reader).withType(CsvMember.class)
					.withIgnoreLeadingWhiteSpace(true).build();

			Map<String, CsvMember> csvMembers = new HashMap<>();
			csvToMembers.parse().forEach(csvMember -> csvMembers.put(csvMember.getId(), csvMember));
			return csvMembers;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}

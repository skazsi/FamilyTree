package hu.skzs.familytree.memberreader;

import java.io.File;
import java.util.Map;

interface CsvMemberReader {

	Map<String, CsvMember> read(File csvFile);
}

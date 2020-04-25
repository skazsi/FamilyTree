package hu.skzs.familytree.memberreader;

import java.io.File;
import java.util.Map;
import java.util.Objects;

class ImageReaderCsvMemberReaderDecorator implements CsvMemberReader {

	private CsvMemberReader decorated;
	private ImageReader imageReader;

	public ImageReaderCsvMemberReaderDecorator(CsvMemberReader decorated, ImageReader imageReader) {
		this.decorated = Objects.requireNonNull(decorated);
		this.imageReader = Objects.requireNonNull(imageReader);
	}

	@Override
	public Map<String, CsvMember> read(File csvFile) {
		Map<String, CsvMember> csvMembers = decorated.read(csvFile);
		String folder = csvFile.getAbsoluteFile().getParent();
		csvMembers.values().forEach((csvMember) -> imageReader.readImage(folder, csvMember));
		return csvMembers;
	}
}

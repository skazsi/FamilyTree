package hu.skzs.familytree.memberreader;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MemberReaderConfiguration {

	@Bean
	public MemberReader memberReader() {
		return new MemberReader(
				new ImageReaderCsvMemberReaderDecorator(new DefaultCsvMemberReader(), new ImageReader()));
	}
}

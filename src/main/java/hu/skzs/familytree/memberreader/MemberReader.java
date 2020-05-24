package hu.skzs.familytree.memberreader;

import java.io.File;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.skzs.familytree.Couple;
import hu.skzs.familytree.Member;
import hu.skzs.familytree.MemberComparator;
import hu.skzs.familytree.Person;

public class MemberReader {

	private static final Logger LOGGER = LoggerFactory.getLogger(MemberReader.class);

	private final CsvMemberReader csvMemberReader;

	public MemberReader(CsvMemberReader csvMemberReader) {
		this.csvMemberReader = Objects.requireNonNull(csvMemberReader);
	}

	public Set<Member> read(File csvFile) {
		Set<Member> members = new TreeSet<>(new MemberComparator());

		Map<String, CsvMember> csvMembers = csvMemberReader.read(csvFile);

		CsvMember csvMember = null;
		while ((csvMember = getTopLevelMember(csvMembers)) != null) {
			Couple couple = new Couple();
			couple.setPerson(convert(csvMember));
			couple.setPartner(convert(csvMembers.get(csvMember.getPartnerId())));
			LOGGER.info("Top level member found {}", couple);
			members.add(couple);

			csvMembers.remove(csvMember.getId());
			LOGGER.debug("Removing the '{}' member from the CSV members", csvMember.getId());
			csvMembers.remove(csvMember.getPartnerId());
			LOGGER.debug("Removing the '{}' member from the CSV members", csvMember.getPartnerId());

			addDescendant(csvMember, couple, csvMembers);
		}

		csvMembers.values().forEach(csvMember2 -> System.out.println(csvMember2));

		return members;
	}

	private CsvMember getTopLevelMember(Map<String, CsvMember> csvMembers) {
		for (CsvMember csvMember : csvMembers.values()) {
			if (csvMember.getMotherId() == null && csvMember.getFatherId() == null && csvMember.getPartnerId() != null
					&& csvMembers.get(csvMember.getPartnerId()).getMotherId() == null
					&& csvMembers.get(csvMember.getPartnerId()).getFatherId() == null) {
				return csvMember;
			}
		}
		return null;
	}

	private void addDescendant(CsvMember csvMember, Couple couple, Map<String, CsvMember> csvMembers) {
		CsvMember csvDescendant = null;
		while ((csvDescendant = getDescendant(csvMember.getId(), csvMember.getPartnerId(), csvMembers)) != null) {
			if (csvDescendant.getPartnerId() != null) {
				Couple descendantCouple = new Couple();
				descendantCouple.setPerson(convert(csvDescendant));
				descendantCouple.setPartner(convert(csvMembers.get(csvDescendant.getPartnerId())));
				LOGGER.info("Descendant couple found {}", descendantCouple);
				couple.addDescendant(descendantCouple);

				csvMembers.remove(csvDescendant.getId());
				LOGGER.debug("Removing the '{}' member from the CSV members", csvDescendant.getId());
				csvMembers.remove(csvDescendant.getPartnerId());
				LOGGER.debug("Removing the '{}' member from the CSV members", csvDescendant.getPartnerId());

				addDescendant(csvDescendant, descendantCouple, csvMembers);

			} else {
				Person descendantPerson = convert(csvDescendant);
				LOGGER.info("Descendant person found {}", descendantPerson);
				couple.addDescendant(descendantPerson);

				csvMembers.remove(csvDescendant.getId());
				LOGGER.debug("Removing the '{}' member from the CSV members", csvDescendant.getId());
			}
		}
	}

	private CsvMember getDescendant(String id, String partnerId, Map<String, CsvMember> csvMembers) {
		for (CsvMember csvMember : csvMembers.values()) {
			if (id.equals(csvMember.getMotherId()) && partnerId.equals(csvMember.getFatherId())
					|| partnerId.equals(csvMember.getMotherId()) && id.equals(csvMember.getFatherId())) {
				return csvMember;
			}
		}
		return null;
	}

	private Person convert(CsvMember csvMember) {
		Person person = new Person();
		person.setName(csvMember.getName());
		person.setBirthDate(csvMember.getBirthDate());
		person.setBirthPlace(csvMember.getBirthPlace());
		person.setDied(csvMember.getDied());
		person.setImage(csvMember.getImage());
		return person;
	}
}

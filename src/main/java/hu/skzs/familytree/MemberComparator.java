package hu.skzs.familytree;

import java.util.Comparator;
import java.util.Date;

public class MemberComparator implements Comparator<Member> {

	@Override
	public int compare(Member member1, Member member2) {
		int dateCompareTo = getDate(member1).compareTo(getDate(member2));
		if (dateCompareTo != 0) {
			return dateCompareTo;
		}
		return getName(member1).compareTo(getName(member2));
	}

	private Date getDate(Member member) {
		if (member instanceof Person) {
			return ((Person) member).getBirthDate();
		}
		if (member instanceof Couple) {
			return ((Couple) member).getPerson().getBirthDate();
		}
		throw new RuntimeException("Unsupported member");
	}

	private String getName(Member member) {
		if (member instanceof Person) {
			return ((Person) member).getName();
		}
		if (member instanceof Couple) {
			return ((Couple) member).getPerson().getName();
		}
		throw new RuntimeException("Unsupported member");
	}

}

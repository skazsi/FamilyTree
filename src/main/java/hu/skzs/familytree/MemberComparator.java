package hu.skzs.familytree;

import java.util.Comparator;
import java.util.Date;

public class MemberComparator implements Comparator<Member> {

	@Override
	public int compare(Member member1, Member member2) {
		return getDate(member1).compareTo(getDate(member2));
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

}

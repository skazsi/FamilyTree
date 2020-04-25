package hu.skzs.familytree;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class Couple implements Member {

	private Person person;
	private Person partner;
	private List<Member> descendants = new ArrayList<>();

	public void setPerson(Person person) {
		this.person = person;
	}

	public Person getPerson() {
		return person;
	}

	public void setPartner(Person partner) {
		this.partner = partner;
	}

	public Person getPartner() {
		return partner;
	}

	public void addDescendant(Member descendant) {
		this.descendants.add(descendant);
	}

	public List<Member> getDescendants() {
		return descendants;
	}

	@Override
	public String toString() {
		StringJoiner stringJoiner = new StringJoiner(",", Couple.class.getSimpleName() + "[", "]");
		stringJoiner.add("person=" + person);
		stringJoiner.add("partner=" + partner);
		stringJoiner.add("descendants=" + descendants);
		return stringJoiner.toString();
	}

}

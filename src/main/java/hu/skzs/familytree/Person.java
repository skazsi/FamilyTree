package hu.skzs.familytree;

import java.util.Date;
import java.util.StringJoiner;

import com.opencsv.bean.CsvDate;

public class Person implements Member {

	private String name;

	private Couple parents;

	@CsvDate(value = "yyyy-MM-dd")
	private Date birthDate;

	private String birthPlace;

	private Date died;

	private byte[] image;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setParents(Couple parents) {
		this.parents = parents;
	}

	public Couple getParents() {
		return parents;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthPlace(String birthPlace) {
		this.birthPlace = birthPlace;
	}

	public String getBirthPlace() {
		return birthPlace;
	}

	public void setDied(Date died) {
		this.died = died;
	}

	public Date getDied() {
		return died;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public byte[] getImage() {
		return image;
	}

	@Override
	public String toString() {
		StringJoiner stringJoiner = new StringJoiner(",", Person.class.getSimpleName() + "[", "]");
		stringJoiner.add("name=" + name);
		stringJoiner.add("parents=" + parents);
		stringJoiner.add("birthDate=" + birthDate.toString());
		stringJoiner.add("birthPlace=" + birthPlace);
		stringJoiner.add("died=" + (died != null ? died.toString() : ""));
		stringJoiner.add("image=" + (image != null ? "<yes>" : "<no>"));
		return stringJoiner.toString();
	}

}

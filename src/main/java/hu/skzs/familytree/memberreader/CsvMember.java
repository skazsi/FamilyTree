package hu.skzs.familytree.memberreader;

import java.util.Date;
import java.util.StringJoiner;

import org.springframework.util.StringUtils;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;

public class CsvMember {

	@CsvBindByName
	private String id;

	@CsvBindByName
	private String partnerId;

	@CsvBindByName
	private String name;

	@CsvBindByName
	private String motherId;

	@CsvBindByName
	private String fatherId;

	@CsvDate(value = "yyyy-MM-dd")
	@CsvBindByName
	private Date birthDate;

	@CsvBindByName
	private String birthPlace;

	@CsvDate(value = "yyyy-MM-dd")
	@CsvBindByName
	private Date died;

	private byte[] image;

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setPartnerId(String partnerId) {
		if (StringUtils.hasLength(partnerId)) {
			this.partnerId = partnerId;
		}
	}

	public String getPartnerId() {
		return partnerId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setMotherId(String motherId) {
		if (StringUtils.hasLength(motherId)) {
			this.motherId = motherId;
		}
	}

	public String getMotherId() {
		return motherId;
	}

	public void setFatherId(String fatherId) {
		if (StringUtils.hasLength(fatherId)) {
			this.fatherId = fatherId;
		}
	}

	public String getFatherId() {
		return fatherId;
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
		StringJoiner stringJoiner = new StringJoiner(",", CsvMember.class.getSimpleName() + "[", "]");
		stringJoiner.add("id=" + id);
		stringJoiner.add("partnerId=" + partnerId);
		stringJoiner.add("name=" + name);
		stringJoiner.add("motherId=" + motherId);
		stringJoiner.add("fatherId=" + fatherId);
		stringJoiner.add("birthDate=" + birthDate.toString());
		stringJoiner.add("birthPlace=" + birthPlace);
		stringJoiner.add("died=" + (died != null ? died.toString() : ""));
		return stringJoiner.toString();
	}

}

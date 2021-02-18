package csvprocessor;

import static org.apache.commons.lang3.StringUtils.isEmpty;;

public class Enrollee {
	private final String userId;
	private final String firstName;
	private final String lastName;
	private final int version;
	private final String insuranceCompany;
	
	private Enrollee(String[] splitted) {
		userId = splitted[0];
		firstName = splitted[1];
		lastName = splitted[2];
		version = Integer.valueOf(splitted[3]);
		insuranceCompany = splitted[4];
	}
	
	public static Enrollee factoryEnrollees(String line) {
		if(isEmpty(line)) return null;
		String[] splitted =  line.split(",");
		if(splitted.length < 5) return null;
		return new Enrollee(splitted);
	}

	public String getUserId() {
		return userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public int getVersion() {
		return version;
	}

	public String getInsuranceCompany() {
		return insuranceCompany;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(!(obj instanceof Enrollee)) return false;
		Enrollee otherObj = (Enrollee) obj;
		return this.insuranceCompany.equals(otherObj.insuranceCompany) && this.userId.equals(otherObj.userId) && this.version == otherObj.version;
	}
	
	public boolean equalsNotVersion(Object obj) {
		if(obj == null) return false;
		if(!(obj instanceof Enrollee)) return false;
		Enrollee otherObj = (Enrollee) obj;
		return this.insuranceCompany.equals(otherObj.insuranceCompany) && this.userId.equals(otherObj.userId);
	}
	
	@Override
    public int hashCode() {
		return insuranceCompany.hashCode() * userId.hashCode() * version ;
	}
}
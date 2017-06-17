package net.sf.juoserver.model;

import java.io.Serializable;

public class City implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name, locationName; //TODO: can there be more locations per city?
	public City(String name, String locationName) {
		super();
		this.name = name;
		this.locationName = locationName;
	}
	public String getName() {
		return name;
	}
	public String getLocationName() {
		return locationName;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((locationName == null) ? 0 : locationName.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		City other = (City) obj;
		if (locationName == null) {
			if (other.locationName != null)
				return false;
		} else if (!locationName.equals(other.locationName))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "City [locationName=" + locationName + ", name=" + name + "]";
	}
}

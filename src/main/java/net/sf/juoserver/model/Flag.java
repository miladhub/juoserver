package net.sf.juoserver.model;

import java.io.Serializable;

public class Flag implements Serializable {
	private static final long serialVersionUID = 1L;
	private int value;
	public Flag(int value) {
		super();
		this.value = value;
	}
	public int getValue() {
		return value;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + value;
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
		Flag other = (Flag) obj;
		if (value != other.value)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Flag [value=" + value + "]";
	}
}

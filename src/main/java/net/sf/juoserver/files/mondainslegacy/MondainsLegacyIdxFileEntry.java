package net.sf.juoserver.files.mondainslegacy;

import net.sf.juoserver.api.IdxFileEntry;

public class MondainsLegacyIdxFileEntry implements IdxFileEntry {
	/**
	 * 0-based entry index.
	 */
	private int index;
	/**
	 * Starting position within the data file.
	 */
	private int start;
	/**
	 * Length of the portion within the data file.
	 */
	private int length;

	public MondainsLegacyIdxFileEntry(int start, int length) {
		super();
		this.start = start;
		this.length = length;
	}

	@Override
	public IdxFileEntry index(int index) {
		this.index = index;
		return this;
	}

	@Override
	public int getIndex() {
		return index;
	}

	@Override
	public int getStart() {
		return start;
	}

	@Override
	public int getLength() {
		return length;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + index;
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
		MondainsLegacyIdxFileEntry other = (MondainsLegacyIdxFileEntry) obj;
		if (index != other.index)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "IdxFileEntryImpl [index=" + index + ", start=" + start
				+ ", length=" + length + "]";
	}
}

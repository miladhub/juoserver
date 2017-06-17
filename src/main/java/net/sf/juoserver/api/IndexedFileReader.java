package net.sf.juoserver.api;

import java.util.List;

/**
 * Contract for a file reader that uses {@link IdxFileEntry} as the lookup
 * object type.
 * 
 * @param <T>
 *            type of output (result) object
 */
public interface IndexedFileReader<T> extends GenericFileReader<IdxFileEntry, T> {
	/**
	 * Retrieves all the outputs in the file.
	 * 
	 * @return all the outputs in the index file
	 */
	public List<T> getAllEntries();
}

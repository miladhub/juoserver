package net.sf.juoserver.api;

import java.util.List;

/**
 * Contract for an index file reader.
 */
public interface IdxFileReader extends GenericFileReader<Integer, IdxFileEntry> {
	/**
	 * @param index
	 *            position
	 * @return the {@link IdxFileEntry} at the specified position
	 */
	@Override
	IdxFileEntry getEntryAt(Integer index);

	/**
	 * @return all the {@link IdxFileEntry}s in the index file
	 */
	List<IdxFileEntry> getAllEntries();
}

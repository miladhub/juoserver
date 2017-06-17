package net.sf.juoserver.api;

/**
 * Index file entry.
 */
public interface IdxFileEntry {
	IdxFileEntry index(int index);

	int getIndex();

	int getStart();

	int getLength();
}

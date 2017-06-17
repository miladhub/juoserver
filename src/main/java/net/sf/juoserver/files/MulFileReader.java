package net.sf.juoserver.files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import net.sf.juoserver.api.GenericFileEntryEncoder;
import net.sf.juoserver.api.IdxFileEntry;
import net.sf.juoserver.api.IdxFileReader;
import net.sf.juoserver.api.IndexedFileReader;

/**
 * MUL (data) files {@link IndexedFileReader} acting as a <b>bridge</b> towards
 * a specific {@link IdxFileReader} and {@link GenericFileEntryEncoder}.
 * 
 * @param <T>
 *            type of the object to be retrieved from the data file
 */
public class MulFileReader<T> implements IndexedFileReader<T> {
	private RandomAccessFile raf;
	private IdxFileReader idxFileReader;
	private GenericFileEntryEncoder<T> encoder;

	public MulFileReader(File file, IdxFileReader idxFileReader,
			GenericFileEntryEncoder<T> encoder) throws FileNotFoundException {
		this.raf = new RandomAccessFile(file, "r");
		this.idxFileReader = idxFileReader;
		this.encoder = encoder;
	}

	@Override
	public List<T> getAllEntries() {
		List<T> entries = new ArrayList<T>();
		for (IdxFileEntry idx : idxFileReader.getAllEntries()) {
			entries.add(getEntryAt(idx));
		}
		return entries;
	}

	@Override
	public T getEntryAt(IdxFileEntry entry) {
		if (entry == null) {
			throw new IllegalArgumentException("index entry cannot be null");
		}
		// TODO: (LRU?-) cache entries
		try {
			raf.seek(entry.getStart());
		} catch (IOException e) {
			throw new FileReaderException(e);
		}
		byte[] buffer = new byte[entry.getLength()];
		try {
			raf.read(buffer, 0, entry.getLength());
		} catch (IOException e) {
			throw new FileReaderException(e);
		}
		return encoder.encode(buffer);
	}
}

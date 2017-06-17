package net.sf.juoserver.files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import net.sf.juoserver.api.IdxFileEntry;
import net.sf.juoserver.api.IdxFileReader;


/**
 * Base {@link IdxFileReader} acting as a <b>bridge</b> towards a specific
 * {@link IdxFileEntryEncoder}.
 */
public class BaseIdxFileReader implements IdxFileReader {
	private RandomAccessFile raf;
	private int blockSize;
	private IdxFileEntryEncoder entryEncoder;

	public BaseIdxFileReader(File file, int blockSize,
			IdxFileEntryEncoder entryEncoder) throws FileNotFoundException {
		super();
		this.raf = new RandomAccessFile(file, "r");
		if (blockSize <= 0) {
			throw new IllegalArgumentException("blockSize <= 0");
		}
		this.blockSize = blockSize;
		this.entryEncoder = entryEncoder;
	}

	@Override
	public List<IdxFileEntry> getAllEntries() {
		List<IdxFileEntry> entries = new ArrayList<IdxFileEntry>();
		IdxFileEntry entry;
		for (int i = 0; (entry = getEntryAt(i)) != null; i++) {
			entries.add(entry);
		}
		return entries;
	}

	@Override
	public IdxFileEntry getEntryAt(Integer index) {
		if (index == null) {
			throw new IllegalArgumentException("index cannot be null");
		}
		// TODO: (LRU?-) cache entries
		try {
			raf.seek(index * blockSize);
		} catch (IOException e) {
			throw new FileReaderException("Error while seeking position", e);
		}
		byte[] buffer = new byte[blockSize];
		try {
			int nread = raf.read(buffer, 0, blockSize);
			if (nread != blockSize) {
				return null;
			}
		} catch (IOException e) {
			throw new FileReaderException("Error while reading file", e);
		}
		return entryEncoder.encode(buffer).index(index);
	}
}

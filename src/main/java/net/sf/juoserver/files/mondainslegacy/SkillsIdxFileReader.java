package net.sf.juoserver.files.mondainslegacy;

import java.io.File;
import java.io.FileNotFoundException;

import net.sf.juoserver.files.BaseIdxFileReader;

/**
 * Skills index file reader.
 */
class SkillsIdxFileReader extends BaseIdxFileReader {
	public static final int IDX_BLOCK_SIZE = 12;

	public SkillsIdxFileReader(File file) throws FileNotFoundException {
		super(file, IDX_BLOCK_SIZE, new SkillsIdxFileEntryEncoder());
	}
}

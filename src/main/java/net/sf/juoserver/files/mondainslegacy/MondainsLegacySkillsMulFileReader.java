package net.sf.juoserver.files.mondainslegacy;

import java.io.File;
import java.io.FileNotFoundException;

import net.sf.juoserver.api.IdxFileReader;
import net.sf.juoserver.api.SkillsFileEntry;
import net.sf.juoserver.api.SkillsMulFileReader;
import net.sf.juoserver.files.MulFileReader;

class MondainsLegacySkillsMulFileReader extends MulFileReader<SkillsFileEntry>
		implements SkillsMulFileReader {
	public MondainsLegacySkillsMulFileReader(File file, IdxFileReader idxFileReader)
			throws FileNotFoundException {
		super(file, idxFileReader, new SkillsMulFileEntryEncoder());
	}
}

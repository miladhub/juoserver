package net.sf.juoserver.files.mondainslegacy;

import java.io.File;
import java.io.FileNotFoundException;

import net.sf.juoserver.api.FileReadersFactory;
import net.sf.juoserver.api.IdxFileReader;
import net.sf.juoserver.api.MapFileReader;
import net.sf.juoserver.api.SkillsMulFileReader;

public final class MondainsLegacyFileReadersFactory implements FileReadersFactory {
	@Override
	public SkillsIdxFileReader createSkillsIdxFileReader(File file)
			throws FileNotFoundException {
		return new SkillsIdxFileReader(file);
	}

	@Override
	public MapFileReader createMapFileReader(File mapFile,
			int mapHeight) throws FileNotFoundException {
		return new MondainsLegacyMapFileReader(mapFile, mapHeight);
	}
	
	@Override
	public SkillsMulFileReader createSkillsMulFileReader(File file,
			IdxFileReader idxFileReader) throws FileNotFoundException {
		return new MondainsLegacySkillsMulFileReader(file, idxFileReader);
	}
}

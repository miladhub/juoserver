package net.sf.juoserver.api;

import java.io.File;
import java.io.FileNotFoundException;

public interface FileReadersFactory {
	IdxFileReader createSkillsIdxFileReader(File file) throws FileNotFoundException;
	
	SkillsMulFileReader createSkillsMulFileReader(File file,
			IdxFileReader idxFileReader) throws FileNotFoundException;

	MapFileReader createMapFileReader(File mapFile, int mapHeight) throws FileNotFoundException;
}

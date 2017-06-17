package net.sf.juoserver.files.mondainslegacy;

import java.util.Arrays;

import net.sf.juoserver.api.GenericFileEntryEncoder;
import net.sf.juoserver.api.SkillsFileEntry;

/**
 * Encoder for the skills data file (namely, <tt>skills.mul</tt>).
 */
class SkillsMulFileEntryEncoder implements GenericFileEntryEncoder<SkillsFileEntry> {
	@Override
	public SkillsFileEntry encode(byte[] contents) {
		// First byte is 1 if there is a button next to the name,
		// 0 if there is not. Last byte is always zero.
		return new MondainsLegacySkillsFileEntry(
				new String(Arrays.copyOfRange(contents, 1, contents.length - 1)),
				contents[0] == 1);
	}
}

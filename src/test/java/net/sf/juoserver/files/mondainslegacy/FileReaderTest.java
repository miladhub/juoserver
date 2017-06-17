package net.sf.juoserver.files.mondainslegacy;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.List;


import net.sf.juoserver.api.FileReadersFactory;
import net.sf.juoserver.api.IdxFileEntry;
import net.sf.juoserver.api.IdxFileReader;
import net.sf.juoserver.api.SkillsFileEntry;
import net.sf.juoserver.files.mondainslegacy.MondainsLegacyFileReadersFactory;
import net.sf.juoserver.files.mondainslegacy.MondainsLegacySkillsMulFileReader;

import org.junit.Before;
import org.junit.Test;

public class FileReaderTest {
	private String[] skills = { "Alchemy", "Anatomy", "Animal lore",
			"Item Identify", "Arms lore", "Parrying", "Begging",
			"Blacksmithing", "Bowcraft", "Peacemaking", "Camping", "Carpentry",
			"Cartography", "Cooking", "Detecting Hidden", "Enticement ",
			"Evaluate intelligence", "Healing", "Fishing",
			"Forensic Evaluation", "Herding", "Hiding", "Provocation",
			"Inscription", "Lockpicking", "Magery", "Magic Resistance",
			"Tactics", "Snooping", "Musicianship", "Poisoning", "Archery",
			"Spirit Speaking", "Stealing", "Tailoring", "Animal Taming",
			"Taste Identification", "Tinkering", "Tracking", "Veterinary",
			"Swordsmanship", "Macefighting", "Fencing", "Wrestling",
			"Lumberjacking", "Mining", "Meditation", "Stealth", "Remove Traps",
			"Nrcromancy", "Focus", "Warrior Magery", "Bushido", "Ninjitsu",
			"SpellWeaving" };
	
	private FileReadersFactory fileReadersFactory;
	
	@Before
	public void createFactory() {
		fileReadersFactory = new MondainsLegacyFileReadersFactory();
	}
	
	
	@Test
	public void getIdxEntries() throws IOException {
		IdxFileReader ifr = fileReadersFactory.createSkillsIdxFileReader(new File("src/test/resources/myskills.idx"));
		
		List<IdxFileEntry> entries = ifr.getAllEntries();
		assertNotNull(entries);
		assertEquals(skills.length, entries.size());
		
		RandomAccessFile raf = new RandomAccessFile(
				new File("src/test/resources/myskills.mul"), "r");
		
		int i = 0, nextStart = 0;
		for (IdxFileEntry entry : entries) {
			int start = entry.getStart();
			assertTrue("Bad start at #" + i, start >= 0);
			
			int length = entry.getLength();
			assertTrue("Bad length at #" + i, length > 0);
			
			if (i > 0) {
				assertEquals(nextStart, start);
			}
			nextStart = start + length;
			
			raf.seek(entry.getStart());
			byte[] buffer = new byte[entry.getLength()];
			raf.read(buffer, 0, entry.getLength());
			
			assertEquals(i, entry.getIndex());
			
			// First byte is 1 if there is a button next to the name,
			// 0 if there is not. Last byte is always zero.
			assertEquals(skills[i++], 
					new String(Arrays.copyOfRange(buffer, 1, buffer.length - 1)));
		}
	}
	@Test
	public void getIdxEntryAt() throws IOException {
		IdxFileReader ifr = fileReadersFactory.createSkillsIdxFileReader(new File("src/test/resources/myskills.idx"));
		
		int index = 9;
		IdxFileEntry peaceMakingEntry = ifr.getEntryAt(index);
		
		RandomAccessFile raf = new RandomAccessFile(
				new File("src/test/resources/myskills.mul"), "r");
		
		raf.seek(peaceMakingEntry.getStart());
		byte[] buffer = new byte[peaceMakingEntry.getLength()];
		raf.read(buffer, 0, peaceMakingEntry.getLength());
		assertEquals("Peacemaking",
				new String(Arrays.copyOfRange(buffer, 1, buffer.length - 1)));
	}
	@Test
	public void getMulEntryAt() throws FileNotFoundException {
		IdxFileReader idxReader = fileReadersFactory.createSkillsIdxFileReader(new File("src/test/resources/myskills.idx"));
		MondainsLegacySkillsMulFileReader mulReader = new MondainsLegacySkillsMulFileReader(new File("src/test/resources/myskills.mul"),
				idxReader);
		
		SkillsFileEntry peaceMaking = mulReader.getEntryAt(idxReader.getEntryAt(9));
		assertEquals("Peacemaking", peaceMaking.getSkillName());
		assertTrue(peaceMaking.isHasButton());
		
		SkillsFileEntry magery = mulReader.getEntryAt(idxReader.getEntryAt(25));
		assertEquals("Magery", magery.getSkillName());
		assertFalse(magery.isHasButton());
	}
	@Test
	public void getMulEntries() throws IOException {
		IdxFileReader idxReader = fileReadersFactory.createSkillsIdxFileReader(new File("src/test/resources/myskills.idx"));
		MondainsLegacySkillsMulFileReader mulReader = new MondainsLegacySkillsMulFileReader(new File("src/test/resources/myskills.mul"),
				idxReader);
		
		List<SkillsFileEntry> entries = mulReader.getAllEntries();
		assertNotNull(entries);
		assertEquals(skills.length, entries.size());
		
		int i = 0;
		for (SkillsFileEntry entry : entries) {
			assertEquals(skills[i++], entry.getSkillName());
		}
	}
}

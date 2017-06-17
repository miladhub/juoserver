package net.sf.juoserver.files;

import net.sf.juoserver.api.GenericFileEntryEncoder;
import net.sf.juoserver.api.IdxFileEntry;
import net.sf.juoserver.files.mondainslegacy.MondainsLegacyIdxFileEntry;

/**
 * Contract for a class capable of encoding
 * {@link MondainsLegacyIdxFileEntry}s starting from byte
 * arrays.
 */
public interface IdxFileEntryEncoder extends GenericFileEntryEncoder<IdxFileEntry> {}

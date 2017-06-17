package net.sf.juoserver.api;

/**
 * Contract for a file reader.
 * 
 * @param <Lookup>
 *            type of lookup object - e.g., <tt>int</tt>
 * @param <Output>
 *            type of output (result) object
 */
public interface GenericFileReader<Lookup, Output> {
	/**
	 * Retrieves the output at the specified position
	 * 
	 * @param index
	 *            position
	 * @return the output at the specified position
	 */
	Output getEntryAt(Lookup index);
}

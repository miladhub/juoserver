package net.sf.juoserver.api;

public interface GenericFileEntryEncoder<T> {
	public T encode(byte[] contents);
}

package net.sf.juoserver.configuration;

public class ConfigurationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ConfigurationException() {
		super();
	}

	public ConfigurationException(String msg, Throwable t) {
		super(msg, t);
	}

	public ConfigurationException(String msg) {
		super(msg);
	}

	public ConfigurationException(Throwable msg) {
		super(msg);
	}

}

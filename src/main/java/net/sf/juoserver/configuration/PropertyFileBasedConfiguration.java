package net.sf.juoserver.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;


import net.sf.juoserver.api.Configuration;

import org.apache.log4j.Logger;

public class PropertyFileBasedConfiguration implements Configuration {
	private static final Logger logger = Logger.getLogger(PropertyFileBasedConfiguration.class);

	public static final String PROP_FILE_NAME = "juoserver.properties";
	private static final String PROP_FILE_DEFAULT_PATH =
		System.getProperty("user.home") + File.separator + ".juoserver" + 
		File.separator + PROP_FILE_NAME;
	private static final String PROP_FILE_PATH = "juoserver.propFilePath";

	private static final String DEFAULT_SERVER_NAME = "JUOServer";
	private static final String DEFAULT_SERVER_PORT = "7775";
	private static final String DEFAULT_SERVER_HOST = "localhost";
	private static final String DEFAULT_PACKET_LOGGING_ENABLED = "false";

	private String propFilePath;
	private Properties props;

	public PropertyFileBasedConfiguration() {
		this.propFilePath = System.getProperty(PROP_FILE_PATH);
		
		// Classic filename fall-back
		if (propFilePath == null) {
			logger.info("System property '" +
					PROP_FILE_PATH + "' is not set, using default path '" +
					PROP_FILE_DEFAULT_PATH + "'.");
			propFilePath = PROP_FILE_DEFAULT_PATH;
		}
		
		props = new Properties();
		try {
			FileInputStream propFile = PropertyFileBasedConfiguration.findPropertiesFile(propFilePath);
			if (propFile == null) {
				throw new IllegalStateException("Could not find file " + propFilePath);
			}
			props.load(propFile);
		} catch (FileNotFoundException e) {
			throw new IllegalStateException("Could not find file " + propFilePath);
		} catch (IOException e) {
			throw new IllegalStateException("Could not read file " + propFilePath);
		}
	}

	@Override
	public String getSkillsIdxPath() {
		return getUOPath() + File.separator + props.getProperty("skills.idxFileName");
	}

	@Override
	public String getMulPath() {
		return getUOPath() + File.separator + props.getProperty("skills.mulFileName");
	}

	@Override
	public String getUOPath() {
		String uoPath = props.getProperty("uopath");
		if (uoPath == null || !new File(uoPath).exists()
				|| !new File(uoPath).isDirectory()) {
			throw new ConfigurationException("property 'uopath' not defined, "
					+ "or does path does not point to an existing directory.");
		}
		return uoPath;
	}

	@Override
	public int getServerPort() {
		return Integer.valueOf(getProperty("server.port", DEFAULT_SERVER_PORT));
	}

	@Override
	public String getServerName() {
		return getProperty("server.name", DEFAULT_SERVER_NAME);
	}

	@Override
	public String getServerHost() {
		return getProperty("server.host", DEFAULT_SERVER_HOST);
	}

	private String getProperty(String name, String defaultValue) {
		String value = props.getProperty(name);
		if (value == null) {
			logger.debug("Using default value for property '" + name + "': " + defaultValue);
			return defaultValue;
		}
		return value;
	}

	public static FileInputStream findPropertiesFile(String propFilePath) {
		if (new File(propFilePath).exists()) {
			try {
				return new FileInputStream(propFilePath);
			} catch (FileNotFoundException e) {
				throw new IllegalStateException("File " + PROP_FILE_NAME + " exists but could not be found!", e);
			}
		}
		
		JOptionPane.showMessageDialog(
				null, "File " + PROP_FILE_NAME
				+ " not found, insert its full path - e.g., /juoserver/juoserver.properties",
				PROP_FILE_NAME + " not found!",
				JOptionPane.INFORMATION_MESSAGE);
		
		JFileChooser chooser = new JFileChooser();
		int status = chooser.showOpenDialog(null);
		if (JFileChooser.APPROVE_OPTION != status) {
			return null;
		}
	
		try {
			return new FileInputStream(chooser.getSelectedFile());
		} catch (FileNotFoundException e) {
			throw new IllegalStateException("Could not find file "
					+ propFilePath, e);
		}
	}

	@Override
	public boolean isPacketLoggingEnabled() {
		return Boolean.parseBoolean(getProperty("packet.logging", DEFAULT_PACKET_LOGGING_ENABLED));
	}
}
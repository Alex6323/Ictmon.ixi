package com.ictmon.ixi.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.*;

public class Properties extends java.util.Properties {

	private final static Logger LOGGER = LogManager.getLogger(Properties.class);
	
	public Properties() {
		setRequiredProps();	
	}

	public Properties(final String propertiesFilePath) {
		
		File propertiesLocation = new File(Constants.PROPERTIES_LOCATION);
		if (propertiesLocation.mkdirs()) {
			LOGGER.info(String.format("Created properties folder at:\n%s", propertiesLocation.getAbsolutePath()));
		}

		load(propertiesFilePath);
		setRequiredProps();
		validateProps();
	}

	public int getZmqPort() {
		return Integer.parseInt(getProperty(Constants.ZMQ_PORT_PROPERTY, Integer.toString(Constants.DEFAULT_ZMQ_PORT)).trim());
	}

	public void setZmqPort(final int zmqPort) {
		put(Constants.ZMQ_PORT_PROPERTY, Integer.toString(zmqPort));
	}

    public void load(final String propertiesFilePath) {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(propertiesFilePath);
            load(inputStream);

        } catch (final FileNotFoundException e) {
            LOGGER.info(String.format("Could not read properties file '%s', therefore a new one will be created.", propertiesFilePath));

        } catch (final IOException e) {
            LOGGER.error(String.format("Failed to open input stream of file: '%s'", propertiesFilePath));
            e.printStackTrace();

        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    LOGGER.error(String.format("Failed to close input stream of file: '%s'", propertiesFilePath));
                    e.printStackTrace();
                }
            }
        }
    }

    public void store(final String propertiesFilePath) {
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(propertiesFilePath);
            store(outputStream, null);

        } catch (final FileNotFoundException e) {
            LOGGER.error(String.format("Failed to open output stream of file: '%s'", propertiesFilePath));
            e.printStackTrace();

        } catch (final IOException e) {
            LOGGER.error(String.format("Failed to save properties to file: '%s'", propertiesFilePath));
            e.printStackTrace();

        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();

                } catch (IOException e) {
                    LOGGER.error(String.format("Failed to close output stream of file: '%s'", propertiesFilePath));
                    e.printStackTrace();
                }
            }
        }
    }

	private void setRequiredProps() {
		if (get(Constants.ZMQ_PORT_PROPERTY) == null) {
			put(Constants.ZMQ_PORT_PROPERTY, Integer.toString(Constants.DEFAULT_ZMQ_PORT));
		}
	}

	private void validateProps() {
		int port = getZmqPort();
		if (port <= 1024 || port > 65535) {
			LOGGER.info("Specified port '%d' exceeds the allowed range. Using default port '%d'.", port, Constants.DEFAULT_ZMQ_PORT);

			setZmqPort(Constants.DEFAULT_ZMQ_PORT);
		} 
	}
}

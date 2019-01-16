package com.ictmon.ixi.utils;

import java.io.*;
import java.util.*;

public class Properties extends java.util.Properties {
	
	//Property names
	private final static String ZMQ_PORT = "zmq_port";

	//Property defaults
	private final static int DEFAULT_ZMQ_PORT = 5561;

	public Properties() {
		setRequiredProps();	
	}

	public Properties(final String propertiesFilePath) {
		
		File propertiesLocation = new File(Constants.PROPERTIES_LOCATION);
		if (propertiesLocation.mkdirs()) {
			//LOGGER: create properties folder
		}

		load(propertiesFilePath);
		setRequiredProps();
	}

	public int getZmqPort() {
		return Integer.parseInt(getProperty(ZMQ_PORT, Integer.toString(DEFAULT_ZMQ_PORT)).trim());
	}

	public void setZmqPort(final int zmqPort) {
		put(ZMQ_PORT, Integer.toString(zmqPort));
	}
    public void load(final String propertiesFilePath) {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(propertiesFilePath);
            load(inputStream);

        } catch (final FileNotFoundException e) {
            //LOGGER.info(String.format("Could not read properties file '%s', therefore a new one will be created.",
                    //propertiesFilePath));
        } catch (final IOException e) {
            //LOGGER.error(String.format("Failed to open input stream of file: '%s'",
                    //propertiesFilePath));
            e.printStackTrace();

        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    //LOGGER.error(String.format("Failed to close input stream of file: '%s'",
                            //propertiesFilePath));
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
            //LOGGER.error(String.format("Failed to open output stream of file: '%s'",
            //        propertiesFilePath));
            e.printStackTrace();

        } catch (final IOException e) {
            //LOGGER.error(String.format("Failed to save properties to file: '%s'",
            //        propertiesFilePath));
            e.printStackTrace();

        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    //LOGGER.error(String.format("Failed to close output stream of file: '%s'",
                    //        propertiesFilePath));
                    e.printStackTrace();
                }
            }
        }
    }

	private void setRequiredProps() {
		if (get(ZMQ_PORT) == null) {
			put(ZMQ_PORT, DEFAULT_ZMQ_PORT);
		}
	}
}

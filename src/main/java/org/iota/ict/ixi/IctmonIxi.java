package org.iota.ict.ixi;

import com.ictmon.ixi.IctmonIxiGossipListener;
import com.ictmon.ixi.utils.Constants;
import com.ictmon.ixi.utils.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

enum STATE {
	TERMINATED,
	INITIALIZING,
	RUNNING,
	TERMINATING	
}

public class IctmonIxi extends IxiModule {
	
	private final static Logger LOGGER = LogManager.getLogger(IctmonIxi.class);
	private STATE state = STATE.TERMINATED;
	private Properties properties;
	
	public IctmonIxi(final Ixi ixi) {
		super(ixi);
	}	
	
	@Override
	public void terminate() {
		state = STATE.TERMINATING;
		LOGGER.info("Terminating Ictmon.ixi...");
		super.terminate();
		state = STATE.TERMINATED;
		LOGGER.info("Ictmon.ixi terminated.");
	}

	@Override
	public void run() {
		state = STATE.INITIALIZING;

		LOGGER.info(String.format("Ictmon.ixi %s: Starting...", Constants.VERSION));
		properties = new Properties(Constants.PROPERTIES_FILE);
		properties.store(Constants.PROPERTIES_FILE);		

		state = STATE.RUNNING;

		ixi.addGossipListener(new IctmonIxiGossipListener(properties.getZmqPort()));

		LOGGER.info(String.format("Ictmon.ixi %s: Started on port: %d", Constants.VERSION, properties.getZmqPort()));
	}

	public boolean isRunning() {
		return state == STATE.RUNNING;
	}
}

package org.iota.ict.ixi;

import com.ictmon.ixi.IctmonIxiGossipListener;
import com.ictmon.ixi.utils.Constants;
import com.ictmon.ixi.utils.Properties;

enum STATE {
	TERMINATED,
	INITIALIZING,
	RUNNING,
	TERMINATING	
}


public class IctmonIxi extends IxiModule {
	private STATE state = STATE.TERMINATED;
	private Properties properties;
	
	public IctmonIxi(final Ixi ixi) {
		super(ixi);
	}	
	
	@Override
	public void terminate() {
		state = STATE.TERMINATING;
		//LOGGER
		super.terminate();
		state = STATE.TERMINATED;
		//LOGGER
	}

	@Override
	public void run() {
		state = STATE.INITIALIZING;

		//LOGGER: Ictmon.ixi starting...
		properties = new Properties(Constants.PROPERTIES_FILE);
		properties.store(Constants.PROPERTIES_FILE);		

		state = STATE.RUNNING;

		ixi.addGossipListener(new IctmonIxiGossipListener(properties.getZmqPort()));

		//LOGGER: Ictmon.ixi started on port ....
	}

	public boolean isRunning() {
		return state == STATE.RUNNING;
	}
}

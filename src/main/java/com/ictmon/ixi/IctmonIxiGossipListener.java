package com.ictmon.ixi;

import org.iota.ict.model.Transaction;
import org.iota.ict.network.event.GossipEvent;
import org.iota.ict.network.event.GossipFilter;
import org.iota.ict.network.event.GossipListener;
import org.zeromq.ZMQ;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IctmonIxiGossipListener extends GossipListener {

	private final static Logger LOGGER = LogManager.getLogger(IctmonIxiGossipListener.class);
	private final GossipFilter filter = new GossipFilter();
	private final ZMQ.Context context = ZMQ.context(1);
	private final ZMQ.Socket publisher = context.socket(ZMQ.PUB);
	private final StringBuilder data = new StringBuilder(1000);

	public IctmonIxiGossipListener(final int zmqPort) {
		filter.setWatchingAll(true);

		try {
			publisher.bind("tcp://*:" + zmqPort);
			
			// necessary?
			Thread.sleep(500);
	
		} catch (Exception e) {
			LOGGER.error("Failed to bind publisher to port '%d'.", zmqPort);
		}

	}

	@Override
	public void onGossipEvent(final GossipEvent event) {

		//should not be required, because we want it all
		//if (!filter.passes(event.getTransaction())) return;
		
		//System.out.printf(".");
		if (event.isOwnTransaction()) {
			handleOutbound(event.getTransaction());
		} else {
			handleInbound(event.getTransaction());
		}
	}

	private void handleOutbound(final Transaction tx) {
		trySendData("out".getBytes(), tx);
	}

	private void handleInbound(final Transaction tx) {
		trySendData("in".getBytes(), tx);
	}

	private void trySendData(final byte[] topic, final Transaction tx) {
		data.setLength(0);

		data.append(tx.hash);
		data.append(" ");
		data.append(tx.address);
		data.append(" ");
		data.append(tx.attachmentTimestamp);
		data.append(" ");
		data.append(tx.tag);
		data.append(" ");
		data.append(tx.value);
	
		try {
			publisher.sendMore(topic);
			publisher.send(data.toString());

		} catch (Exception e) {
			LOGGER.error("Failed publishing data.");
		}
	}
}

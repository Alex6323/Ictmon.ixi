package com.ictmon.ixi;

import org.iota.ict.model.Transaction;
import org.iota.ict.network.event.GossipEvent;
import org.iota.ict.network.event.GossipFilter;
import org.iota.ict.network.event.GossipListener;
import org.zeromq.ZMQ;

public class IctmonIxiGossipListener extends GossipListener {
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
			// TODO: logger
		}

	}

	@Override
	public void onGossipEvent(final GossipEvent event) {
		//should not be required
		//if (!filter.passes(event.getTransaction())) return;
	
		if (event.isOwnTransaction()) {
			handleOutbound(event.getTransaction());
		} else {
			handleInbound(event.getTransaction());
		}
	}

	private void handleOutbound(final Transaction tx) {
		//trySendData("out".getBytes("UTF-8"), tx);
		trySendData("out".getBytes(), tx);
	}

	private void handleInbound(final Transaction tx) {
		//trySendData("in".getBytes("UTF-8"), tx);
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
			//TODO: add logger
		}
	}
}

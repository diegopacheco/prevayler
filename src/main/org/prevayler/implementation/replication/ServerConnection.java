//Prevayler(TM) - The Free-Software Prevalence Layer.
//Copyright (C) 2001-2003 Klaus Wuestefeld
//This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

package org.prevayler.implementation.replication;

import org.prevayler.foundation.serialization.Serializer;
import org.prevayler.implementation.TransactionCapsule;
import org.prevayler.implementation.TransactionTimestamp;
import org.prevayler.implementation.publishing.POBox;
import org.prevayler.implementation.publishing.TransactionPublisher;
import org.prevayler.implementation.publishing.TransactionSubscriber;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;


/** Reserved for future implementation.
 */
class ServerConnection extends Thread implements TransactionSubscriber {

	static final String SUBSCRIBER_UP_TO_DATE = "SubscriberUpToDate";
	static final String REMOTE_TRANSACTION = "RemoteTransaction";
	static final String CLOCK_TICK = "ClockTick";

	private final TransactionPublisher _publisher;
	private TransactionCapsule _remoteTransactionCapsule;

	private final ObjectOutputStream _toRemote;
	private final ObjectInputStream _fromRemote;

	private final Serializer _journalSerializer;


	ServerConnection(TransactionPublisher publisher, Socket remoteSocket, Serializer journalSerializer) throws IOException {
		_journalSerializer = journalSerializer;
		_publisher = publisher;
		_fromRemote = new ObjectInputStream(remoteSocket.getInputStream());
		_toRemote = new ObjectOutputStream(remoteSocket.getOutputStream());
		setDaemon(true);
		start();
	}


	public void run() {
		try {		
			long initialTransaction = ((Long)_fromRemote.readObject()).longValue();
			_publisher.addSubscriber(new POBox(this), initialTransaction);
			send(SUBSCRIBER_UP_TO_DATE);
			
			sendClockTicks();
			while (true) publishRemoteTransaction();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}


	private void sendClockTicks() {
		Thread clockTickSender = new Thread() {
			public void run() {
				try {
					while (true) {
						synchronized (_toRemote) {
							_toRemote.writeObject(CLOCK_TICK);
							_toRemote.writeObject(_publisher.clock().time());
						}
						Thread.sleep(1000);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		};
		clockTickSender.setDaemon(true);
		clockTickSender.start();
	}


	void publishRemoteTransaction() throws Exception {
		_remoteTransactionCapsule = (TransactionCapsule)_fromRemote.readObject();
		try {
			_publisher.publish(_remoteTransactionCapsule);
		} catch (RuntimeException rx) {
			send(rx);
		} catch (Error error) {
			send(error);
		}
	}


	public void receive(TransactionTimestamp transactionTimestamp) {
		TransactionCapsule transactionCapsule = transactionTimestamp.capsule();
		long systemVersion = transactionTimestamp.systemVersion();
		Date executionTime = transactionTimestamp.executionTime();

		try {
			synchronized (_toRemote) {
				_toRemote.writeObject(transactionCapsule == _remoteTransactionCapsule
					? (Object)REMOTE_TRANSACTION
					: transactionCapsule
				);
				_toRemote.writeObject(executionTime);
				_toRemote.writeLong(systemVersion);
				_toRemote.flush();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}


	private void send(Object object) {
		synchronized (_toRemote) {
			try {
				_toRemote.writeObject(object);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}

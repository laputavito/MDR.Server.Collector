package mdr.server.collector.util;

import java.net.DatagramSocket;
import java.net.DatagramPacket;

public class SyslogListenerUdp /*extends AbstractSyslogListener*/{
	
	protected DatagramSocket socket;
	protected int port;
	private Listener listener;
	
	private final int BUF_SIZE = 1024;

	private byte[] buf = new byte[BUF_SIZE];

	protected volatile boolean running = false;

	public SyslogListenerUdp(int port) {
		setPort(port);
		
	}

	public void setPort(int port) {
		if (port < 1 || port > 65535)
			throw new IllegalArgumentException("1 <= port <= 65535");

		this.port = port;
	}
	
	public void start() {
		running = true;
		initListener();
	}

	public void stop() {
		running = false;
	}

	private void receivePacket() {

		try {
			DatagramPacket packet = new DatagramPacket(buf, BUF_SIZE);

			socket.receive(packet);
		
			byte[] received = new byte[packet.getLength()];

			//copy received packet from buf to received
			System.arraycopy(buf, 0, received, 0, packet.getLength()); 

			//queuePacket(new SyslogPacket(received,packet.getAddress().getHostAddress()));
	
		} catch (java.io.IOException e) {
			return;
		}
	}

	protected void initListener() {
		if ( listener == null ) {
			listener = new Listener();
			(new Thread(listener, "UDP Syslog Listener port" + port)).start();
		}
	}

	private class Listener implements Runnable {
		
		public void run() {
			init();

			while (running) 
				//blocks.
				receivePacket();	

			stop();
		}

		private void init() {
			if ( socket != null && socket.isBound() )
				socket.close();

			try {

				socket = new DatagramSocket(port);
			} catch (java.net.SocketException e) {
				running = false;
			}
		}

		private void stop() {
			try {
				socket.close();
			} catch (Exception e) {
				//can't do much if it can't close...
			}

			socket = null;
		}
	}
}

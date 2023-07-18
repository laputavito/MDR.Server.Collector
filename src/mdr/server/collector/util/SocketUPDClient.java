package mdr.server.collector.util;

import org.productivity.java.syslog4j.Syslog; 
import org.productivity.java.syslog4j.SyslogConfigIF; 
import org.productivity.java.syslog4j.SyslogIF; 
import org.productivity.java.syslog4j.impl.net.tcp.TCPNetSyslogConfig; 
import org.productivity.java.syslog4j.impl.net.udp.UDPNetSyslogConfig; 
import org.productivity.java.syslog4j.util.SyslogUtility; 

public class SocketUPDClient {
	private static SocketUPDClient instance;
	private static SyslogConfigIF config = null; 
	private static SyslogIF client = null; 
	
	private SocketUPDClient() throws Exception{
		 initialize();
	}
	
	private void initialize() throws Exception{
		
		
		String protocol = "udp";
		String host = CommonUtil.getPropertiesInfo("ip", Config.Path.ConfigFilePath);
		String name = CommonUtil.getPropertiesInfo("udpname", Config.Path.ConfigFilePath);
		int port = Integer.parseInt(CommonUtil.getPropertiesInfo("udpport", Config.Path.ConfigFilePath));
		
		config = (protocol.equals("tcp")) ? new TCPNetSyslogConfig(host, port) : new UDPNetSyslogConfig(host, port); 
		client = Syslog.createInstance(name, config);
	}
	
	public static SocketUPDClient getInstance() throws Exception{
		if(instance == null){
			 instance = new SocketUPDClient();
		}
		
		return instance;
	}
	
	public synchronized void sendSysLog(String sysLog){
		System.out.println(sysLog);
		client.info(sysLog);
	}
}

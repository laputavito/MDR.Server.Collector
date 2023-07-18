package mdr.server.collector.util;

import java.net.DatagramSocket;

public class Monitor {
	private static DatagramSocket isRun;
	public static boolean monitoring(int portnum) throws Exception{
		try{
			isRun = new DatagramSocket(portnum);
			System.out.println("Socket used : " + portnum);
			return true;
		}catch(Exception e){
			Log.TraceLog(e.getMessage());
			return false;
		}
	}
	
	public static void close(){
		if(isRun != null){
			isRun.close();
		}
	}
}

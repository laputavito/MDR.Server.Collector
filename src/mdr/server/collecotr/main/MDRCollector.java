package mdr.server.collecotr.main;

import java.util.ArrayList;
import java.util.List;

import mdr.server.collector.db.ConnectionPool;
import mdr.server.collector.util.CommonUtil;
import mdr.server.collector.util.Config;
import mdr.server.collector.util.Log;
import mdr.server.collector.util.Monitor;
import mdr.server.collector.util.SFTPFileTransfer;
import mdr.server.collector.util.SocketUPDClient;
import mdr.server.collector.vo.ConnectionInfoVO;

public class MDRCollector {

	public static void main(String[] args) throws Exception {

		boolean isExec = false;
		String sDebug = "";
		isExec = true;
		
		// Monitor.monitoring(Integer.parseInt(CommonUtil.getPropertiesInfo("exec_port",
		// Config.Path.ConfigFilePath)));
		
		if (!isExec) {
			System.out.println("이전 배치 실행중.... 종료됨");
			Log.TraceLog("이전 배치 실행중.... - Done!!");
			System.exit(0);
		} else {
			Config.Status.isDebug = Boolean.parseBoolean(CommonUtil.getPropertiesInfo("isDebug"));
					
			System.out.println("배치 Thread 실행");
			Log.TraceLog("배치 Thread 실행", "INFO");
					
			// CommonUtil.udpClinet = SocketUPDClient.getInstance();
		
			ArrayList<Thread> threadList = new ArrayList<Thread>();
			List<Runnable> threads = new ArrayList<Runnable>();

			// threads.add(new MataInfoUpdateThread());
			// threads.add(new StatusThread());

			threads.add(new CollectorMultiDemon());

			for (Runnable th : threads) {
				Thread thread = new Thread(th);
				thread.start();
				threadList.add(thread);
			}
			
//			threads.add(new CollectorDemon());
//
//			for (Runnable th : threads) {
//				Thread thread = new Thread(th);
//				thread.start();
//				threadList.add(thread);
//			}

			for (Thread t : threadList) {
				t.join(); // 쓰레드의 처리가 끝날때까지 기다립니다.
			}
		}
	}
}
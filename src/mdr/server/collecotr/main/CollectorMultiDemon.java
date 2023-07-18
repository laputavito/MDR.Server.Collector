package mdr.server.collecotr.main;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import mdr.server.collector.db.ConnectionPLDM;
import mdr.server.collector.util.CommonUtil;
import mdr.server.collector.util.Config;
import mdr.server.collector.util.Log;
import mdr.server.collector.util.OSValidator;

public class CollectorMultiDemon implements Runnable {
	private boolean isRun = true;
	private boolean isCollect = true;
	private String logFilePathFullName = Config.Path.LogFilePath;
	private static Connection con = null;
	private static PreparedStatement pstmt = null;
	
	public void run() {
		// TODO Auto-generated method stub
		try {
			Log.TraceLog("Collector Demon이 시작되었습니다...", "INFO");
			
			while (isRun) {
				isCollect = Boolean.parseBoolean(CommonUtil.getPropertiesInfo("isCollect"));
				if(isCollect){
					try {
						double testDle = 1.0;
						int testInt = 0;
						String testStr = "100";

						testInt = (int) (testDle + 0.5);
						testInt = Integer.parseInt(testStr);
						
							String collect_sol = "";

							Config.Path.connectionPLDM = ConnectionPLDM.getPLDMInstance();
							
							con = Config.Path.connectionPLDM.getConnection();
							if(con == null){
								new Exception("DB Connection Error...!!");
							}
							
							Config.Path.connectionPLDM.releaseConnection(con);
							
							StringBuffer query = new StringBuffer();

							query = new StringBuffer();

							query.append("delete from public.org_user where emp_no <> 'admin';");
							pstmt = con.prepareStatement(query.toString());
							pstmt.executeUpdate();

							query = new StringBuffer();
							
//							query.append("truncate table public.org_group ");
//							pstmt = con.prepareStatement(query.toString());
//							pstmt.executeUpdate();
							
							query.append("delete from public.org_group where org_code <> '1000';");
							pstmt = con.prepareStatement(query.toString());
							pstmt.executeUpdate();

							
							query = new StringBuffer();

							query.append("truncate table public.user_mstr");
							pstmt = con.prepareStatement(query.toString());
							pstmt.executeUpdate();
							
							CollectorInsa.CollectorInsa("INSA");
							Thread.sleep(3000);
							
							query = new StringBuffer();
							
				    		query.append("select sol_id from public.sec_sol_info "
				    				+ "where use_indc = true "
				    				+ "order by sol_id");
				    		
							//query.append(query);
							pstmt = con.prepareStatement(query.toString());
							
							ResultSet rs = pstmt.executeQuery();

							
							while(rs.next()){
								collect_sol = rs.getString(1);

								CollectorProc.CollectorProc(collect_sol);
								Thread.sleep(3000);
										
							}
							if(rs != null){ rs = null; }
							if(pstmt != null){ pstmt = null; }
							
				    		
//							Config.Path.connectionPool.releaseConnection(con);
							
//							CollectorProc.CollectorProc("ADF");
//							Thread.sleep(3000);
//							CollectorProc.CollectorProc("ALYAC");
//							Thread.sleep(3000);
//							CollectorProc.CollectorProc("APCG");
//							Thread.sleep(3000);
//							CollectorProc.CollectorProc("AVAC");
//							Thread.sleep(3000);
//							CollectorProc.CollectorProc("DBI");
//							Thread.sleep(3000);
//							CollectorProc.CollectorProc("FDRM");
//							Thread.sleep(3000);
//							CollectorProc.CollectorProc("KDLP");
//							Thread.sleep(3000);
//							CollectorProc.CollectorProc("GDLP");
//							Thread.sleep(3000);
//							CollectorProc.CollectorProc("MNAC");
//							Thread.sleep(3000);
//							CollectorProc.CollectorProc("PDRM");
//							Thread.sleep(3000);
//							CollectorProc.CollectorProc("PI");
//							Thread.sleep(3000);
//							CollectorProc.CollectorProc("SNB");
//							Thread.sleep(3000);
//							CollectorProc.CollectorProc("SPMF");
//							Thread.sleep(3000);
//							CollectorProc.CollectorProc("SPRT");
//							Thread.sleep(3000);
//							CollectorProc.CollectorProc("STG");
//							Thread.sleep(3000);
//							CollectorProc.CollectorProc("USB");
						
							System.out.println("Log data collect end.");
							Log.TraceLog("Log data collect end.", "INFO");
							
					} catch (Exception e) {
						// TODO Auto-generated catch block
						Log.TraceLog(e.toString(), "DEBUG");
						e.printStackTrace();
						
					}finally{
						
						//sftpTransfer.close();
					}

				}
				
				int collect_interval = 1000 * Integer.parseInt(CommonUtil.getPropertiesInfo("collect_interval"));
				if(collect_interval == 0){
					break;
				}
				Thread.sleep(collect_interval);//쓰레드를 잠시 멈춤
				isRun = Boolean.parseBoolean(CommonUtil.getPropertiesInfo("isRun"));
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.TraceLog(e.getStackTrace());
			e.printStackTrace();

		}
	}
}
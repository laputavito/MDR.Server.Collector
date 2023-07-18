package mdr.server.collecotr.main;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import mdr.server.collector.dao.LogCollection;
import mdr.server.collector.dao.LogInsert;
import mdr.server.collector.db.ConnectionPLDM;
import mdr.server.collector.db.ConnectionPool;
import mdr.server.collector.util.CommonUtil;
import mdr.server.collector.util.Config;
import mdr.server.collector.util.DateUtil;
import mdr.server.collector.util.Log;
import mdr.server.collector.util.LogFileWriter;
import mdr.server.collector.util.OSValidator;
import mdr.server.collector.util.SFTPFileTransfer;

import java.io.Reader;
import java.nio.CharBuffer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CollectorInsa {
	private static boolean isRun = true;
	private static boolean isCollect = true;
	private static String logFilePathFullName = Config.Path.LogFilePath;
	private static Connection con = null;
	private static PreparedStatement pstmt = null;

	public static void CollectorInsa(String SolName) {
		// TODO Auto-generated method stub
		try {
			//Log.TraceLog("Collector Demon이 시작되었습니다...", "INFO");
			
			Config.Path.ConfigFilePath = OSValidator.OS_Type().equals("LINUX") ? System.getProperty("user.dir") + "/METADATA/" + SolName + "/config.properties" : System.getProperty("user.dir") + "\\METADATA\\" + SolName + "\\config.properties";
			Config.Path.MetadataPath = OSValidator.OS_Type().equals("LINUX") ? System.getProperty("user.dir") + "/METADATA/" + SolName + "/metadata.properties" : System.getProperty("user.dir") + "\\METADATA\\" + SolName + "\\metadata.properties";
			Config.Path.ConditionValuePath = OSValidator.OS_Type().equals("LINUX") ? System.getProperty("user.dir") + "/METADATA/" + SolName + "/condition.properties" : System.getProperty("user.dir") + "\\METADATA\\" + SolName + "\\condition.properties";

			Config.Path.connectionPool = ConnectionPool.getInstance();
			Config.Path.connectionPLDM = ConnectionPLDM.getPLDMInstance();

			con = Config.Path.connectionPLDM.getConnection();

			System.out.println("[ " + SolName + " ] Data Proc Start");
			
			while (isRun) {
				isCollect = Boolean.parseBoolean(CommonUtil.getPropertiesInfo("isCollect"));
				if(isCollect){
					try {
						//sftpTransfer = new SFTPFileTransfer();
						
						//String delimiter = CommonUtil.getPropertiesInfo("delimiter");
						//테이블 조회 수
						int tableCnt = Integer.parseInt(CommonUtil.getPropertiesInfo("collect_count"));
						//int countVal = 1;
						int logCount = 0;
						int limit_count = 0;
						int totCount = 0;
//						String mdr_empno = "";
//						String mdr_ip = "";
//						String mdr_mac = "";
//						String mdr_org_logdate = "";
						Date sDate = new Date();
						Date eDate = new Date();
						boolean bInsert = false;

						limit_count = Integer.parseInt(CommonUtil.getPropertiesInfo("loglimit"));

						for(int i=1;i<=tableCnt;i++){
							
							//logFilePathFullName = Config.Path.LogFilePath;
							
							/**
							 * 로그 조회 테이블 정보 읽음
							 */
							String collect_query = CommonUtil.getPropertiesInfo("collect_query" + i);
							String condition_column = CommonUtil.getPropertiesInfo("condition_column" + i);
							String tableName = CommonUtil.getPropertiesInfo("collect_tablename" + i);
							String condition_query = CommonUtil.getPropertiesInfo("condition_query" + i);
							String condition_last_value = CommonUtil.getPropertiesInfo(tableName, Config.Path.ConditionValuePath);
							String copy_query = CommonUtil.getPropertiesInfo("copy_query" + i);
							String emp_no_title = CommonUtil.getPropertiesInfo("emp_no" + i);
							String emp_ip_title = CommonUtil.getPropertiesInfo("emp_ip" + i);
							String emp_mac_title = CommonUtil.getPropertiesInfo("emp_mac" + i);
							String log_org_date_title = CommonUtil.getPropertiesInfo("log_org_date" + i);
							StringBuffer logBuffer = new StringBuffer();
							StringBuffer logInsert = new StringBuffer();

//							if(condition_last_value == null || condition_last_value.isEmpty()){
//								condition_last_value = DateUtil.getDateAdd(Calendar.DATE, -1);
//							}
//							System.out.println("[ condition query ] " + condition_query + String.format(" [ condition value ] '%s'",condition_last_value));
//							Log.TraceLog("[ condition query ] " + condition_query + String.format(" [ condition value ] '%s'",condition_last_value), "DEBUG");
//							condition_query = condition_query.replace("$$condition_value$$", String.format("'%s'",condition_last_value));
//							if(!condition_query.equals("")){
//								collect_query = collect_query + " WHERE " + condition_query + " ORDER BY " + condition_column;
//							}
						
							System.out.println("[ collect_query ] " + collect_query);
							Log.TraceLog("[ collect_query ] " + collect_query, "DEBUG") ;
							/**
							 * 로그 저장 정보 및 파일명 
							 */
							
							SimpleDateFormat formatter1 = new SimpleDateFormat ("yyyyMMddHHmmss");
							String logDate = formatter1.format(new Date());
							logFilePathFullName = logFilePathFullName + String.format("%s_%s.log", logDate, tableName);
							 
							String condition_value = "";
							/**
							 * 로그 조회
							 */
							List<LinkedHashMap<String, String>> logList = LogCollection.getLogCollect(collect_query);

							if(logList.size() > 0){
								Log.TraceLog("Data Parsing Start", "DEBUG");
								sDate = new Date();
								for(LinkedHashMap<String, String> row:logList){
									logCount++;
									totCount++;
									logBuffer = new StringBuffer();
									
//									logBuffer.append(String.format("%s%s%s", "\"", row.get(emp_no_title), "\"|"));
//									logBuffer.append(String.format("%s%s%s", "\"", row.get(emp_ip_title), "\"|"));
//									logBuffer.append(String.format("%s%s%s", "\"", row.get(emp_mac_title), "\"|"));
//									logBuffer.append(String.format("%s%s%s", "\"", row.get(log_org_date_title), "\"|"));

									for(String key:row.keySet()){
										
										//if (key.equals("dprtid"))
										//{
										//	String org_level = "";
										//	String org_position[];
										//	//org_level = row.get(key);
										//	int lvl_cnt = 0;

										//	if (org_level != null) 
										//	{
										//		org_position = org_level.split("\\\\");
										//		lvl_cnt = org_position.length;
										//	}
										//	else
										//	{
										//		lvl_cnt = 0;
										//	}
										//	
										//	logBuffer.append(String.format("%s%s%s", "\"", lvl_cnt, "\"|"));
										//}
										//else
										//{
										//	logBuffer.append(String.format("%s%s%s", "\"", row.get(key), "\"|"));
										//}
										
										if (key.equals("status"))
										{
											if(row.get(key).equals("9100"))
											{
												logBuffer.append(String.format("%s%s%s", "\"", "1", "\"|"));
											}
											else
											{
												logBuffer.append(String.format("%s%s%s", "\"", "0", "\"|"));
											}
										}
										else
										{
											logBuffer.append(String.format("%s%s%s", "\"", row.get(key), "\"|"));
										}


									}
									// 마지막 문자 '|' 삭제
									logBuffer.deleteCharAt(logBuffer.length() - 1);
									// 마지막 조회 일자 저장

									logInsert.append(logBuffer.toString() + "\r\n");
									
									if(logCount%limit_count == 0){
										bInsert = LogInsert.setLogInsert(copy_query, logInsert);
										logInsert = new StringBuffer();
										logCount = 0;
									}
								}

								if(logCount > 0){
									bInsert = LogInsert.setLogInsert(copy_query, logInsert);
								}
								eDate = new Date();
								Log.TraceLog("Data Insert Stop - Total Count : " + totCount, "DEBUG");
								logCount = 0;
								totCount = 0;

								/**
								 * 로그 파일 저장
								 */
								//String logContents = "";
								//logContents += String.format("%sLog_Begin%s", delimiter, delimiter);
								//logContents += logHead.toString();
								//logContents += logBody.toString();
								//logContents += String.format("%sLog_End%s", delimiter, delimiter);
								//LogFileWriter.LogWriter(logContents, logFilePathFullName);
								/**
								 * 로그 저장 후 로그파일 SFTP 서버 전송
								 */
								
								/*try{
									sftpTransfer.init();
									System.out.println(CommonUtil.getPropertiesInfo("suploadpath") + "][" +logFilePathFullName );
									sftpTransfer.upload(CommonUtil.getPropertiesInfo("suploadpath"), new File(logFilePathFullName));
									sftpTransfer.disconnection();	
								}catch(Exception ex){
									Log.TraceLog("[FTP 전송 에러]" +  ex.toString());
									System.out.println("FTP 전송 에러.....");
								}*/
								
								//CommonUtil.udpClinet.sendSysLog("Collect Completed.....");

								/**
								 * 검색조건 (마지막 날짜 정보) 저장
								 */
								condition_value = sDate.toString();

								if (bInsert) {
									System.out.println("[ " + tableName + " ] Condition_value : " + condition_value);
									CommonUtil.setPropertiesInfo(tableName, condition_value, Config.Path.ConditionValuePath);
								}
								
							}else{
								condition_value = condition_last_value;
								CommonUtil.setPropertiesInfo(tableName, condition_value, Config.Path.ConditionValuePath);
								Log.TraceLog("[ " + tableName + " ] Log 수집 데이터 없음..... 파일생성 및 전송 미처리...", "INFO");
								System.out.println("[ " + tableName + " ] Log 수집 데이터 없음..... 파일생성 및 전송 미처리...");
							}


						}
							
				      	StringBuffer query = new StringBuffer();
						boolean org_last = false;

				      	query = new StringBuffer();

				      	query.append("select add_info1 from code_info where majr_code = 'C01' and minr_code = 'ORGROOT' ");

						pstmt = con.prepareStatement(query.toString());
						
						ResultSet rs = pstmt.executeQuery();
						
						String orgroot = "";
						
						while (rs.next())
						{
							orgroot = rs.getString(1);
						}
						

				      	query = new StringBuffer();

				      	query.append("select org_code from org_group where org_code <> '" + orgroot + "' ");

						pstmt = con.prepareStatement(query.toString());
						
						ResultSet rs_org = pstmt.executeQuery();

						int org_level = 0;

				      	while (rs_org.next())
						{
				      		String org_code = "";
							String upper_org_code = "";
				      		org_code = rs_org.getString(1);
				      		boolean org_level_end = true;
							org_level = 0;

			      			String org_next_code = "";
			      			
			      			org_next_code = org_code;

				      		while (org_level_end)
				      		{
						      	query = new StringBuffer();
	
						      	query.append("select upper_org_code from org_group where org_code = '" + org_next_code + "' ");
	
								pstmt = con.prepareStatement(query.toString());
								
								ResultSet rs_upper = pstmt.executeQuery();

								while (rs_upper.next())
								{
									upper_org_code = rs_upper.getString(1);
								}
								if (upper_org_code.equals(orgroot))
								{
									org_level++;
									org_level_end = false;
								}
								else
								{
									org_level++;
								}

								org_next_code = upper_org_code;
				      		}
								
					      	query = new StringBuffer();

					      	query.append("Update org_group set org_level = " + org_level + " where org_code = '" + org_code + "' ");

							pstmt = con.prepareStatement(query.toString());
 							pstmt.executeUpdate();

						}
						
						
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
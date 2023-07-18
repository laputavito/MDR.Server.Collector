package mdr.server.collecotr.main;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
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

public class CollectorProc {
	private static boolean isRun = true;
	private static boolean isCollect = true;
	private static String logFilePathFullName = Config.Path.LogFilePath;
	private static Connection con = null;


	public static void CollectorProc(String SolName) {
		// TODO Auto-generated method stub
		try {
			//Log.TraceLog("Collector Demon이 시작되었습니다...", "INFO");
			
			Config.Path.ConfigFilePath = OSValidator.OS_Type().equals("LINUX") ? System.getProperty("user.dir") + "/METADATA/" + SolName + "/config.properties" : System.getProperty("user.dir") + "\\METADATA\\" + SolName + "\\config.properties";
			Config.Path.MetadataPath = OSValidator.OS_Type().equals("LINUX") ? System.getProperty("user.dir") + "/METADATA/" + SolName + "/metadata.properties" : System.getProperty("user.dir") + "\\METADATA\\" + SolName + "\\metadata.properties";
			Config.Path.ConditionValuePath = OSValidator.OS_Type().equals("LINUX") ? System.getProperty("user.dir") + "/METADATA/" + SolName + "/condition.properties" : System.getProperty("user.dir") + "\\METADATA\\" + SolName + "\\condition.properties";

			Config.Path.connectionPool = ConnectionPool.getInstance();
			Config.Path.connectionPLDM = ConnectionPLDM.getPLDMInstance();
			
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

							if(condition_last_value == null || condition_last_value.isEmpty()){
								condition_last_value = DateUtil.getDateAdd(Calendar.DATE, -1);
							}
							System.out.println("[ condition query ] " + condition_query + String.format(" [ condition value ] '%s'",condition_last_value));
							Log.TraceLog("[ condition query ] " + condition_query + String.format(" [ condition value ] '%s'",condition_last_value), "DEBUG");
							condition_query = condition_query.replace("$$condition_value$$", String.format("'%s'",condition_last_value));
							if(!condition_query.equals("")){
								collect_query = collect_query + " WHERE " + condition_query + " ORDER BY " + condition_column;
							}
						
							System.out.println("[ collect_query ] " + collect_query);
							Log.TraceLog("[ collect_query ] " + collect_query, "DEBUG") ;
							/**
							 * 로그 저장 정보 및 파일명 
							 */
							
							LocalDateTime logT = LocalDateTime.now();
							logT = logT.minusDays(1);
							SimpleDateFormat formatter1 = new SimpleDateFormat ("yyyyMMddHHmmss");
							SimpleDateFormat formatter2 = new SimpleDateFormat ("yyyy-MM-dd");
							String logDate = formatter1.format(new Date());
							//String logToday = formatter2.format(logT - 1);
							logFilePathFullName = logFilePathFullName + String.format("%s_%s.log", logDate, tableName);
							 
							String condition_value = "";
							/**
							 * 로그 조회
							 */
							List<LinkedHashMap<String, String>> logList = LogCollection.getLogCollect(collect_query);

					      	StringBuffer query = new StringBuffer();

							if(logList.size() > 0){
								Log.TraceLog("Data Parsing Start", "DEBUG");
								sDate = new Date();
								for(LinkedHashMap<String, String> row:logList){
									logCount++;
									totCount++;
									logBuffer = new StringBuffer();
									
							      	query = new StringBuffer();

							      	if (emp_no_title.equals("N/A"))
							      	{
							      		query.append("select sldm_empno, sldm_mac, sldm_ip from user_mstr where user_mstr.sldm_ip = '" + row.get(emp_ip_title) + "';");

							      		PreparedStatement pstmt = null;

										con = Config.Path.connectionPLDM.getConnection();

										pstmt = con.prepareStatement(query.toString());
										
										ResultSet rs = pstmt.executeQuery();

										String sldm_empno = "";
										String sldm_mac = "";
										String sldm_ip = "";
										
										while(rs.next()){
											sldm_empno = rs.getString(1);
											sldm_mac = rs.getString(2);
											sldm_ip = rs.getString(3);
											
											logBuffer.append(String.format("%s%s%s", "\"", sldm_empno, "\"|"));
											logBuffer.append(String.format("%s%s%s", "\"", sldm_ip, "\"|"));
											logBuffer.append(String.format("%s%s%s", "\"", sldm_mac, "\"|"));
											//logBuffer.append(String.format("%s%s%s", "\"", row.get(log_org_date_title), "\"|"));
											if(SolName.equals("GPCG"))
											{
												logBuffer.append(String.format("%s%s%s", "\"", logT.toString().substring(0,10), "\"|"));
											}
											else
											{
												logBuffer.append(String.format("%s%s%s", "\"", row.get(log_org_date_title), "\"|"));
											}
											
											for(String key:row.keySet()){
												logBuffer.append(String.format("%s%s%s", "\"", row.get(key), "\"|"));
											}
											// 마지막 문자 '|' 삭제
											logBuffer.deleteCharAt(logBuffer.length() - 1);
											// 마지막 조회 일자 저장
											condition_value = row.get(condition_column);

											logInsert.append(logBuffer.toString() + "\r\n");
											
											break;
										}

										Config.Path.connectionPLDM.releaseConnection(con);



							      	}
							      	else
							      	{
										logBuffer.append(String.format("%s%s%s", "\"", row.get(emp_no_title), "\"|"));
										logBuffer.append(String.format("%s%s%s", "\"", row.get(emp_ip_title), "\"|"));
										logBuffer.append(String.format("%s%s%s", "\"", row.get(emp_mac_title), "\"|"));
										//logBuffer.append(String.format("%s%s%s", "\"", row.get(log_org_date_title), "\"|"));
										if(SolName.equals("GPCG"))
										{
											logBuffer.append(String.format("%s%s%s", "\"", logT.toString().substring(0,10), "\"|"));
										}
										else
										{
											logBuffer.append(String.format("%s%s%s", "\"", row.get(log_org_date_title), "\"|"));
										}										
										for(String key:row.keySet()){
											logBuffer.append(String.format("%s%s%s", "\"", row.get(key), "\"|"));
										}
										// 마지막 문자 '|' 삭제
										logBuffer.deleteCharAt(logBuffer.length() - 1);
										// 마지막 조회 일자 저장
										condition_value = row.get(condition_column);

										logInsert.append(logBuffer.toString() + "\r\n");
							      	}
							      	

									
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
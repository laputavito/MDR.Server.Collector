package mdr.server.collector.util;

import mdr.server.collector.db.ConnectionPLDM;
import mdr.server.collector.db.ConnectionPool;
/**
 * Config
 * @author  s2info
 * @since 2017.08.08
 * @version 1.0
 *  
 * <pre>
 * << 개정이력(Modification Information) >>
 * 
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2017.08.08  김재락          최초 생성 
 *  
 *  </pre>
 */
public class Config {
	public static class Status{
		public static boolean isDebug = true;
	}
	public static class Path{
		/**
		 * ConnectionPool 전역변수선언
		 */
		public static ConnectionPool connectionPool = null;
		
		public static ConnectionPLDM connectionPLDM = null;
		
		public static String ProcDate = "";
		
		/**
		 * Agent서버 정보 저장 프로퍼티
		 */
		public static String ConfigFilePath = OSValidator.OS_Type().equals("LINUX") ? System.getProperty("user.dir") + "/METADATA/config.properties" : System.getProperty("user.dir") + "\\METADATA\\config.properties";
		/**
		 * Meta정보 저장 프로퍼티
		 */
		public static String MetadataPath = OSValidator.OS_Type().equals("LINUX") ? System.getProperty("user.dir") + "/METADATA/metadata.properties" : System.getProperty("user.dir") + "\\METADATA\\metadata.properties";
		/**
		 * 로그 조회  조건(마지막 로그 날짜) 저장 경로
		 */
		public static String ConditionValuePath = OSValidator.OS_Type().equals("LINUX") ? System.getProperty("user.dir") + "/METADATA/condition.properties" : System.getProperty("user.dir") + "\\METADATA\\condition.properties";
		/**
		 * 로그파일 저장 경로
		 */
		public static String LogFilePath = OSValidator.OS_Type().equals("LINUX") ? System.getProperty("user.dir") + "/LogData/" : System.getProperty("user.dir") + "\\LogData\\";
		/**
		 * 로그 저장 경로
		 */
		public static String LogPath = OSValidator.OS_Type().equals("LINUX") ? System.getProperty("user.dir") + "/Log/" : System.getProperty("user.dir") + "\\Log\\";

	}
}

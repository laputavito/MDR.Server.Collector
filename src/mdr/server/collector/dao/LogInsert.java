package mdr.server.collector.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import org.postgresql.copy.*;
import org.postgresql.*;
import org.postgresql.core.BaseConnection;

import mdr.server.collector.util.Config;
import mdr.server.collector.util.Log;

/**
 * 보안 관련  DB 로그수집 DAO
 * @author JINNEY
 *
 */
public class LogInsert {
	private static Connection con = null;
	private static PGConnection pgcon = null;
	private static PreparedStatement pstmt = null;
	
	public static boolean setLogInsert(String copy_query, StringBuffer logInsert) throws Exception{
		try
		{
			//InputStream csvStr = null;
			byte[] CopyData = null;
			con = Config.Path.connectionPLDM.getConnection();
  		    if(con == null){
				new Exception("DB Connection Error...!!");
			}

  		    con.setAutoCommit(false);

			CopyManager copyManager = new CopyManager((BaseConnection) con);
			int pCnt = 0;
			
			Config.Path.connectionPLDM.releaseConnection(con);
			String Insert_Head = "";
			String Insert_query = logInsert.toString();
			String UTF8Ins_query = "";
//			UTF8Ins_query = enco Insert_query
			StringBuffer query = new StringBuffer();
			
			Insert_Head = copy_query + " FROM STDIN WITH csv DELIMITER '|' "	+ "NULL '' ";
			

			CopyData = Insert_query.getBytes("UTF-8");
//			Log.TraceLog(Insert_query,"DEBUG");
//			Log.TraceLog(CopyData.toString(),"DEBUG");

			CopyIn ci = copyManager.copyIn(Insert_Head);
			ci.writeToCopy(CopyData, 0, CopyData.length);
			ci.endCopy();
			con.commit();

			return true;

		}catch(Exception e){
			e.printStackTrace();
			con.rollback();
			Log.TraceLog("CopyData Error : " + e.getMessage(), "DEBUG");
			return false;
		}
	}
}

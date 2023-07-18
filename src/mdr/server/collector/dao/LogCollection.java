package mdr.server.collector.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import mdr.server.collector.util.Config;
import mdr.server.collector.util.Log;

/**
 * 보안 관련  DB 로그수집 DAO
 * @author JINNEY
 *
 */
public class LogCollection {
	private static Connection con = null;
	private static PreparedStatement pstmt = null;
	
	
	public static List<LinkedHashMap<String, String>> getLogCollect(String collect_query) throws Exception{
		try
		{
			con = Config.Path.connectionPool.getConnection();
			if(con == null){
				new Exception("DB Connection Error...!!");
			}
			int pCnt = 0;
			
			Config.Path.connectionPool.releaseConnection(con);
			
			StringBuffer query = new StringBuffer();
			
			query.append(collect_query);
			pstmt = con.prepareStatement(query.toString());
			//pstmt.setString(++pCnt, empno);
			//pstmt.setString(++pCnt, apprlinecode);
			
			ResultSet rs = pstmt.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCnt = rsmd.getColumnCount();
			List<LinkedHashMap<String, String>> list = new ArrayList<LinkedHashMap<String, String>>();
			
			while(rs.next()){
				LinkedHashMap<String, String> info = new LinkedHashMap<String, String>();
				for(int i=1;i<=columnCnt;i++){
					info.put(rsmd.getColumnName(i) , rs.getString(rsmd.getColumnName(i)));
				}
				list.add(info);
//				Log.TraceLog(info.toString(),"DEBUG");
			}
			if(rs != null){ rs = null; }
			if(pstmt != null){ pstmt = null; }
			
			Config.Path.connectionPool.releaseConnection(con);
			return list;
		}catch(Exception e){
			e.printStackTrace();
			Log.TraceLog(e.toString());
			return null;
		}
	}
}

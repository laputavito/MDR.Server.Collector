package mdr.server.collector.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Vector;

import mdr.server.collector.vo.ConnectionInfoVO;


public class ConnectionPool {
	private static ConnectionPool instance;    
	
	 private int count; 
	 private Vector free;     
	 private Vector used; 
	 private static String url;
	 private static String id;
	 private static String pw;
	 private static String driverClassName;
	private ConnectionPool() throws Exception{
		 count = 5;
		 free = new Vector();
		 used = new Vector();
		 initialize();
	 }
	 
	 public static ConnectionPool getInstance() throws Exception{
		 ConnectionInfoVO connectionInfo = ConnectionInfoVO.getInstance();
		 url = ConnectionInfoVO.getUrl();
		 id = ConnectionInfoVO.getId();
		 pw = ConnectionInfoVO.getPw();
		 driverClassName = ConnectionInfoVO.getDriverClassName();
		 instance = new ConnectionPool();
//		 if(instance == null){
//			 instance = new ConnectionPool();
//		 }
		 return instance;
	 }

	 private void initialize() throws Exception{  
		 	//System.out.println(driverClassName + "][" + url + "][" + id + "][" + pw);
			 Class.forName(driverClassName);
			 for(int i=0; i<count; i++){
				 //con = DriverManager.getConnection(url, id, pw);
				 Connection con = DriverManager.getConnection(url, id, pw);
				 free.addElement(con);
			 }
	 }

	 public synchronized Connection getConnection() throws Exception{ 
		 Connection con = null;
		 if(!free.isEmpty()){
			 con = (Connection) free.lastElement();     }else{
					 wait();
					 con = (Connection) free.lastElement();
				
			 }
		 free.removeElement(con);  
		 used.addElement(con);  
		 return con;
	 }

	 public synchronized void releaseConnection(Connection con){
		 used.removeElement(con);    
		 free.addElement(con);
		 notify();
	 }

	 public void closeAll(){   
		 try{
			 for(int i=0; i<free.size(); i++){
				 Connection con = (Connection)free.elementAt(i);
				 free.removeElement(con);
				 if(con != null)
				 con.close();
			 }
		 }catch(SQLException e){
			 e.printStackTrace();
		 }
	 }
}

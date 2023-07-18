package mdr.server.collector.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Log {
	 public static String m_FileName = "log";     
     private static FileWriter objfile = null;
     public static String m_PathName =  ""; //System.getProperty("user.dir") + "\\Log\\"; 
     public static boolean isDebug = true;
     public static void TraceLog(String log) throws Exception
     {
         int i                 = 0;
         String stPath         = "";
         String stFileName     = "";

         m_PathName = CommonUtil.getPropertiesInfo("log_path", Config.Path.ConfigFilePath);
         //String m_PathName = "c://VRS//log//";  
      	 stPath     = m_PathName;
         stFileName = m_FileName;
         SimpleDateFormat formatter1 = new SimpleDateFormat ("yyyy-MM-dd");
         SimpleDateFormat formatter2 = new SimpleDateFormat ("HH:mm:ss");
         String stDate = formatter1.format(new Date());
         String stTime = formatter2.format(new Date());
         StringBuffer bufLogPath  = new StringBuffer();       
         bufLogPath.append(stPath);
         bufLogPath.append(stFileName);
         bufLogPath.append("_");
         bufLogPath.append(stDate.replaceAll("-", ""));
         bufLogPath.append(".log") ;
         StringBuffer bufLogMsg = new StringBuffer(); 
         bufLogMsg.append("[");
         bufLogMsg.append(stDate + " " +stTime);
         bufLogMsg.append("] ");             
         bufLogMsg.append(log);

         try{
        	 objfile = new FileWriter( bufLogPath.toString(), true);
        	 objfile.write(bufLogMsg.toString());
        	 objfile.write("\r\n");
         }catch(IOException e){
        	 e.printStackTrace();
         }
         finally
         {
        	 try{
        		 objfile.close();
        	 }catch(Exception e1){}
         }
     }
     
     public static void TraceLog(String log, String debug_mode) throws Exception
     {
         int i                 = 0;
         String stPath         = "";
         String stFileName     = "";

         m_PathName = Config.Path.LogPath;

         //String m_PathName = "c://VRS//log//";  
      	 stPath     = m_PathName;
         stFileName = m_FileName;
         SimpleDateFormat formatter1 = new SimpleDateFormat ("yyyy-MM-dd");
         SimpleDateFormat formatter2 = new SimpleDateFormat ("HH:mm:ss");
         String stDate = formatter1.format(new Date());
         String stTime = formatter2.format(new Date());
         StringBuffer bufLogPath  = new StringBuffer();       
         bufLogPath.append(stPath);
         bufLogPath.append(stFileName);
         bufLogPath.append("_");
         bufLogPath.append(stDate.replaceAll("-", ""));
         bufLogPath.append(".log") ;
         StringBuffer bufLogMsg = new StringBuffer(); 
         bufLogMsg.append("[");
         bufLogMsg.append(stDate + " " +stTime);
         bufLogMsg.append("] ");             
         bufLogMsg.append(log);

         try{
             if(debug_mode.equals("DEBUG")) {
            	 if(Config.Status.isDebug){
                	 objfile = new FileWriter( bufLogPath.toString(), true);
                	 objfile.write(bufLogMsg.toString());
                	 objfile.write("\r\n");
            	 }
             }else{
            	 objfile = new FileWriter( bufLogPath.toString(), true);
            	 objfile.write(bufLogMsg.toString());
            	 objfile.write("\r\n");
             }

         }catch(IOException e){
        	 e.printStackTrace();
         }
         finally
         {
        	 try{
        		 objfile.close();
        	 }catch(Exception e1){}
         }
     }
    
     public static void TraceLog(StackTraceElement[] stackTraceElements){
    	 int i                 = 0;
         String stPath         = "";
         String stFileName     = "";
         
         //String m_PathName = "c://VRS//log//";  
      	 stPath     = m_PathName;
         stFileName = m_FileName;
         SimpleDateFormat formatter1 = new SimpleDateFormat ("yyyy-MM-dd");
         SimpleDateFormat formatter2 = new SimpleDateFormat ("HH:mm:ss");
         String stDate = formatter1.format(new Date());
         String stTime = formatter2.format(new Date());
         StringBuffer bufLogPath  = new StringBuffer();       
         bufLogPath.append(stPath);
         bufLogPath.append(stFileName);
         bufLogPath.append("_");
         bufLogPath.append(stDate.replaceAll("-", ""));
         bufLogPath.append(".log") ;
         StringBuffer bufLogMsg = new StringBuffer(); 
         bufLogMsg.append("[");
         bufLogMsg.append(stDate + " " +stTime);
         bufLogMsg.append("] ");   
         for(StackTraceElement log:stackTraceElements){
        	 bufLogMsg.append(log);
        	 bufLogMsg.append("\r\n");   
         }
        
         try{
        	 objfile = new FileWriter( bufLogPath.toString(), true);
        	 objfile.write(bufLogMsg.toString());
        	 objfile.write("\r\n");
         }catch(IOException e){
        	 e.printStackTrace();
         }
         finally
         {
        	 try{
        		 objfile.close();
        	 }catch(Exception e1){}
         }
     }
}

package mdr.server.collector.util;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * 수집 로그 파일 저장(xxx.log)
 * @author JINNEY
 *
 */
public class LogFileWriter {
	private static BufferedWriter logfile = null;
	public static void LogWriter(String log, String filePath)
    {
		//Writer outFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("파일명"), "utf-8"));
		//BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		/*
		 * Writer fstream = null;
			BufferedWriter out = null;
			try {
    		fstream = new OutputStreamWriter(new FileOutputStream(mergedFile), StandardCharsets.UTF_8);
		 * */
        try{
        	logfile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "UTF8"));
        	//logfile = new BufferedWriter(new FileWriter(filePath));
        	
        	//logfile = new FileWriter( filePath, true);
        	logfile.write(log);
        }catch(IOException e){
       	 e.printStackTrace();
        }
        finally
        {
       	 try{
       		logfile.close();
       	 }catch(Exception e1){}
        }
    }
}

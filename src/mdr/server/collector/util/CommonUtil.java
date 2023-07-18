package mdr.server.collector.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * CommonUtil
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
public class CommonUtil {
	public static SocketUPDClient udpClinet = null;
	/**
	 * Meta Data Properties 정보 조회
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String getPropertiesInfo(String key) throws Exception{
		
		Properties p = new Properties();
		FileInputStream in = new FileInputStream(Config.Path.MetadataPath); 
		p.load(in); 
		in.close();
		
		return p.getProperty(key);
	}
	/**
	 * Properties 정보 조회
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String getPropertiesInfo(String key, String ConfigPath) throws Exception{
		
		Properties p = new Properties();
		FileInputStream in = new FileInputStream(ConfigPath); 
		p.load(in); 
		in.close();
		
		return p.getProperty(key);
	}
	/**
	 * 검색조건 설정 정보 저장
	 * @param key
	 * @param value
	 * @throws Exception
	 */
	public static void setPropertiesInfo(String key, String value, String ConfigPath) throws Exception{
		Properties p = new Properties();
		FileInputStream out = new FileInputStream(ConfigPath);
		p.load(out);
		p.setProperty(key, value);

		p.store(new FileOutputStream(ConfigPath), "");

	}
}

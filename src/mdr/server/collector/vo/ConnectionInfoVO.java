package mdr.server.collector.vo;

import mdr.server.collector.util.CommonUtil;

public class ConnectionInfoVO {
	private static ConnectionInfoVO connectionInfoVO = new ConnectionInfoVO();
	
	public static ConnectionInfoVO getInstance() throws Exception{
		url = CommonUtil.getPropertiesInfo("url");		
		id =  CommonUtil.getPropertiesInfo("id");
		pw =  CommonUtil.getPropertiesInfo("pw");
		driverClassName = CommonUtil.getPropertiesInfo("driverClassName");
		return connectionInfoVO;
	}
	
	private static String url;
	private static String id;
	private static String pw;
	private static String driverClassName;

	public static String getUrl() {
		return url;
	}
	public static void setUrl(String url) {
		ConnectionInfoVO.url = url;
	}
	public static String getId() {
		return id;
	}
	public static void setId(String id) {
		ConnectionInfoVO.id = id;
	}
	public static String getPw() {
		return pw;
	}
	public static void setPw(String pw) {
		ConnectionInfoVO.pw = pw;
	}
	public static String getDriverClassName() {
		return driverClassName;
	}
	public static void setDriverClassName(String driverClassName) {
		ConnectionInfoVO.driverClassName = driverClassName;
	}
	
	
	
}

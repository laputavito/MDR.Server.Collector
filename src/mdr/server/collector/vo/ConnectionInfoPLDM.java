package mdr.server.collector.vo;

import mdr.server.collector.util.CommonUtil;

public class ConnectionInfoPLDM {
	private static ConnectionInfoPLDM connectionInfoVO = new ConnectionInfoPLDM();
	
	public static ConnectionInfoPLDM getInstance() throws Exception{
		url = CommonUtil.getPropertiesInfo("PLDMurl");		
		id =  CommonUtil.getPropertiesInfo("PLDMid");
		pw =  CommonUtil.getPropertiesInfo("PLDMpw");
		driverClassName = CommonUtil.getPropertiesInfo("PLDMdriverClassName");
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
		ConnectionInfoPLDM.url = url;
	}
	public static String getId() {
		return id;
	}
	public static void setId(String id) {
		ConnectionInfoPLDM.id = id;
	}
	public static String getPw() {
		return pw;
	}
	public static void setPw(String pw) {
		ConnectionInfoPLDM.pw = pw;
	}
	public static String getDriverClassName() {
		return driverClassName;
	}
	public static void setDriverClassName(String driverClassName) {
		ConnectionInfoPLDM.driverClassName = driverClassName;
	}
	
	
	
}

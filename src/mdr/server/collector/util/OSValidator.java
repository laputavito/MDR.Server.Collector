package mdr.server.collector.util;

public class OSValidator {
	 public static String OS_Type() {
		 String OS = System.getProperty("os.name").toLowerCase();
		 if (isWindows(OS)) {
			 //System.out.println("This is Windows");
			 return "WINDOWS";
		 } else if (isMac(OS)) {
			 //System.out.println("This is Mac");
			 return "MAC";
		 } else if (isUnix(OS)) {
			 //System.out.println("This is Unix or Linux");
			 return "LINUX";
		 } else if (isSolaris(OS)) {
			 //System.out.println("This is Solaris");
			 return "SOLARIS";
		 } else {
			 System.out.println("Your OS is not support!!");
			 return "";
		 }
	 }
	 public static boolean isWindows(String OS) {
		 return (OS.indexOf("win") >= 0);
	 }
	 public static boolean isMac(String OS) {
		 return (OS.indexOf("mac") >= 0);
	 }
	 public static boolean isUnix(String OS) {
		 return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
	 }
	 public static boolean isSolaris(String OS) {
		 return (OS.indexOf("sunos") >= 0);
	 }

}

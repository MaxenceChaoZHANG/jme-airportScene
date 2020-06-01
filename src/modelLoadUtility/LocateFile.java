package modelLoadUtility;

import java.io.File;


public class LocateFile {
	
	public static boolean LocateObjFile(String filePath){
		
		File existFile=new File("assets/"+filePath);
		if(existFile.exists()) {
			return true;
		 }else
		 {	 
		 return false;
		 }
	}
	
	
	public static String LocateImageFile(String filePath){
		
		String result=null;
		
		filePath=filePath.substring(0,filePath.length()-3);
		
		File existFile=new File("assets/"+filePath+"png");
		if(existFile.exists()) {
			result=filePath+"png";
		 }
		existFile=new File("assets/"+filePath+"dds");
		if(existFile.exists()) {
			result=filePath+"dds";
		 }
		return result;
	
	}
   
	
	public static void main(String[] args) {
		File existFile=new File("assets/ZBAA/custom pol/U5_right.png");
		System.out.println(existFile.exists());
	}
}

package DSFAnalyser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;



public class DSF2TEXT {  
    //����һ�����ط���  
    public native String[] ReadDSFfile(String path);  
    public static String[] DSF;

    
    public static String[] dsf2txttool(String file) throws Throwable {
    	System.loadLibrary("assets/DSFTool");  
        DSF2TEXT d2t = new DSF2TEXT();  
        String[] dsf=d2t.ReadDSFfile(file); 
        d2t.finalize();
        return dsf;
    }
    public static void main(String[] args) throws Throwable {  
      
       String[] dsf=dsf2txttool("D:\\eclipse-workspace\\JmeAirportScene\\+39+117.dsf"); 
       //չʾ������DSF����     
 	   File f = new File("2.txt");
 	   OutputStream out = new FileOutputStream(f,true);
    	for(int i =0; i<dsf.length; i++){
          //���ļ���д������
          out.write(dsf[i].getBytes()); 
    	}
    	out.close();

    }  
}





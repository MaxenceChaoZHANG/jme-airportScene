package DSFAnalyser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class TextReader {
   public static String[] txtReader(String filePath ) throws IOException {
	   
	   ArrayList<String> list=new ArrayList<String>();
	   File fin =new File(filePath);
	   
	   BufferedReader br = new BufferedReader(new FileReader(fin));  
	   String line = null;  
	   
	   while ((line = br.readLine()) != null) {  
		  list.add(line);
	   }
	   br.close();
	   String[] strings = new String[list.size()];
	   list.toArray(strings);
	   return strings;
	   
	   
   }
}

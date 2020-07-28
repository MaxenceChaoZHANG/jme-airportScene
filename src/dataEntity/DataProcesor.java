package dataEntity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class DataProcesor {
	
	/**
	 * 读取天气数据
	 * @param filePath 文件路径
	 * @return ArrayList<WeatherData> 返回天气数据数组
	 * @throws IOException
	 */
   public static ArrayList<WeatherData> readWeatherData(String filePath) throws IOException {
		
		File file =new File(filePath);
		FileInputStream fis = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis,"UTF-8"); //指定以UTF-8编码读入
		BufferedReader br = new BufferedReader(isr);
		
		ArrayList<WeatherData> WeatherData=new ArrayList<WeatherData>();
		WeatherData data=null;
		String line = null;
		int lineNumber=0;
		
		while ((line=br.readLine())!= null) {
			lineNumber++;  
			if(lineNumber!=1)//排除数据第一行，该行为数据标题
			 {
				data=new WeatherData();
				line=line.replace("|", "");//根据“|”拆分字符串
				String[] strs=line.trim().split("\\s+");//trim去除前面的空白字符，split根据空白字符分割
//				for(int i=0;i<strs.length;i++) {
//					System.out.println(strs[i]);
//				}
				data.setPressure(Float.parseFloat(strs[0]));
				data.setTemperature(Float.parseFloat(strs[1]));
				data.setWindSpeed(Float.parseFloat(strs[2]));
				data.setWindDirection(Float.parseFloat(strs[3]));
				data.setTurbulence(Float.parseFloat(strs[4]));
				data.setPrecipitation(Float.parseFloat(strs[5]));
				data.setHail(Float.parseFloat(strs[6]));
				WeatherData.add(data);
             }
		}//结束while循环，结束数据读取
		br.close(); 
		isr.close();
		fis.close();
		return WeatherData;
    }
   
   /**
    * 测试读取数据的函数
    * 
    */
   public static void main(String[] args) {
		ArrayList<WeatherData> WeatherData=null;
		try {
		   WeatherData=readWeatherData("weatherData/AllWeatherTest.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(WeatherData.size());   
		for(int i=0;i<WeatherData.size();i++) {
			System.out.println(WeatherData.get(i));
		}
	
   }

}

package dataEntity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class DataProcesor {
	
	/**
	 * ��ȡ��������
	 * @param filePath �ļ�·��
	 * @return ArrayList<WeatherData> ����������������
	 * @throws IOException
	 */
   public static ArrayList<WeatherData> readWeatherData(String filePath) throws IOException {
		
		File file =new File(filePath);
		FileInputStream fis = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis,"UTF-8"); //ָ����UTF-8�������
		BufferedReader br = new BufferedReader(isr);
		
		ArrayList<WeatherData> WeatherData=new ArrayList<WeatherData>();
		WeatherData data=null;
		String line = null;
		int lineNumber=0;
		
		while ((line=br.readLine())!= null) {
			lineNumber++;  
			if(lineNumber!=1)//�ų����ݵ�һ�У�����Ϊ���ݱ���
			 {
				data=new WeatherData();
				line=line.replace("|", "");//���ݡ�|������ַ���
				String[] strs=line.trim().split("\\s+");//trimȥ��ǰ��Ŀհ��ַ���split���ݿհ��ַ��ָ�
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
		}//����whileѭ�����������ݶ�ȡ
		br.close(); 
		isr.close();
		fis.close();
		return WeatherData;
    }
   
   /**
    * ���Զ�ȡ���ݵĺ���
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

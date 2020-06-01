package ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.dom4j.*;
import org.dom4j.io.*;

import dataEntity.AirportInfo;

public class XMLUtility {
	
	
	/**
     * ��ȡXML�ĵ�
     */
	public static ArrayList<AirportInfo> ReadXML() throws DocumentException {
	    //1. ����DOM4J����������
	    SAXReader reader = new SAXReader();
	   
	    Document doc = reader.read(new File("xml/AirportInfo.xml"));
	    Element rootElement = doc.getRootElement();
	    Iterator<Element> iter1 = rootElement.elementIterator();

	    ArrayList<AirportInfo> airports = new ArrayList<>();
	    AirportInfo airport = null;

	    while (iter1.hasNext()) {
	    	airport = new AirportInfo();
	        Element element = iter1.next();
	        airport.setId(  Integer.parseInt( element.attributeValue("airportId").trim() )  );
	        airport.setName( element.elementText("name"));
	        airport.setICAO_Code(element.elementText("icao_code"));
	        airport.setLontitude(Float.parseFloat(element.elementText("lontitude")));
	        airport.setLatitude(Float.parseFloat(element.elementText("latitude")));
	        airport.setFilePath(element.elementText("file_path"));
	       
	        airports.add(airport);
	    }
	    for (AirportInfo air : airports) {
	        System.out.println(air);
	    }
		return airports;

	}
	
	/**
     * ��ȡXML�ĵ�
     * @param id ����id���һ���
     */
	public static AirportInfo ReadXML(int id) throws DocumentException {

	    SAXReader reader = new SAXReader();
	    Document doc = reader.read(new File("xml/AirportInfo.xml"));
	    
	    Element rootElement = doc.getRootElement();
	    Iterator<Element> iter1 = rootElement.elementIterator();

	    AirportInfo airport = null;

	    while (iter1.hasNext()) {
	        Element element = iter1.next();
	        if(  id ==  Integer.parseInt( element.attributeValue("airportId").trim())  )
	        {
	        	airport=new AirportInfo();
		        airport.setId(Integer.parseInt( element.attributeValue("airportId").trim()) );
		        airport.setName( element.elementText("name"));
		        airport.setICAO_Code(element.elementText("icao_code"));
		        airport.setLontitude(Float.parseFloat(element.elementText("lontitude")));
		        airport.setLatitude(Float.parseFloat(element.elementText("latitude")));
		        airport.setFilePath(element.elementText("file_path"));
		        break;
	        }
	    }

	    System.out.println(airport);

		return airport;

	}
	
	/**
     * ����XML�ڵ�
     * @param id ����id���Ҷ�Ӧ�ڵ�
     */
    public static void UpdateXML(AirportInfo airport) throws DocumentException, IOException{
        //����Document���󣬶�ȡ�Ѵ��ڵ�Xml�ļ�person.xml
        Document doc=new SAXReader().read(new File("xml/AirportInfo.xml"));
        Element rootElement = doc.getRootElement();
        Iterator<Element> iter1 = rootElement.elementIterator();

        while (iter1.hasNext()) {
      
	        Element element = iter1.next();
	        if(  airport.getId() ==  Integer.parseInt( element.attributeValue("airportId").trim())  )
	        {
		        element.element("name").setText(airport.getName());
		        element.element("icao_code").setText(airport.getICAO_Code());
		        element.element("lontitude").setText(airport.getLontitude()+"");
		        element.element("latitude").setText(airport.getLatitude()+"");
		        element.element("file_path").setText(airport.getFilePath());
		        
		        break;
	        }
	    }

        //ָ���ļ������λ��
        FileOutputStream out =new FileOutputStream("xml/AirportInfo.xml");
        // ָ���ı���д���ĸ�ʽ��
        OutputFormat format=OutputFormat.createPrettyPrint();   //Ư����ʽ���пո���
        format.setEncoding("UTF-8");
        //1.����д������
        XMLWriter writer=new XMLWriter(out,format);
        //2.д��Document����
        writer.write(doc);
        //3.�ر���
        writer.close();
    }
    
    /**
     * ���ӣ��ĵ�����ǩ������ 
     * @param airportName  ��������
     * @param icao_code  ��������
     * @param lon  ��������
     * @param lat  ��������
     * @param filepath  �ؾ��ļ�·��
     */
    public static void AddXML(AirportInfo airportInfo) throws DocumentException, IOException {
    	Document doc=new SAXReader().read(new File("xml/AirportInfo.xml"));
    	
        Element rootElement = doc.getRootElement();
        //��������airport,Ѱ�����id
        Iterator<Element> iter1 = rootElement.elementIterator();
        int id=0;
        while (iter1.hasNext()) {
      
	        Element element = iter1.next();
	        if(  id <  Integer.parseInt( element.attributeValue("airportId").trim())  )
	        {
		        id=Integer.parseInt( element.attributeValue("airportId").trim());
	        }
	    }
        //����idΪ��ǰidֵ��1
        id++;
    	//���airport���ڵ�
        Element airport=rootElement.addElement("airport");
        airport.addAttribute("airportId", id+"");
	        //��������ӽڵ�
	        Element name=airport.addElement("name");
	        name.addText(airportInfo.getName());
	        //��������ӽڵ�
	        Element icao=airport.addElement("icao_code");
	        icao.addText(airportInfo.getICAO_Code());
	         //��������ӽڵ�
	        Element lon=airport.addElement("lontitude");
	        lon.addText(airportInfo.getLontitude()+"");
	        //��������ӽڵ�
	        Element lat=airport.addElement("latitude");
	        lat.addText(airportInfo.getLatitude()+"");
	      //��������ӽڵ�
	        Element path=airport.addElement("file_path");
	        path.addText(airportInfo.getFilePath());
     
        //ָ���ļ������λ��
        FileOutputStream out =new FileOutputStream("xml/AirportInfo.xml"); 
        // ָ���ı���д���ĸ�ʽ��
        OutputFormat format=OutputFormat.createPrettyPrint();   //Ư����ʽ���пո���
        format.setEncoding("UTF-8");
        //1.����д������
        XMLWriter writer=new XMLWriter(out,format); 
        //2.д��Document����
        writer.write(doc);
        //3.�ر���
        writer.close();
    }

	/**
     * ɾ��XML�ڵ�
     * @param id  ����idɾ��
     */
    public static void DeleteXML(int id) throws DocumentException, IOException{
        //����Document���󣬶�ȡ�Ѵ��ڵ�Xml�ļ�person.xml
        Document doc=new SAXReader().read(new File("xml/AirportInfo.xml"));

        Element rootElement = doc.getRootElement();
        Iterator<Element> iter1 = rootElement.elementIterator();
        
        while (iter1.hasNext()) {
	        Element element = iter1.next();
	        if(  id ==  Integer.parseInt( element.attributeValue("airportId").trim())  )
	        {
	           element.detach();
	           break;
	        }
        }
        //ָ���ļ������λ��
        FileOutputStream out =new FileOutputStream("xml/AirportInfo.xml");
        // ָ���ı���д���ĸ�ʽ��
        OutputFormat format=OutputFormat.createPrettyPrint();   //Ư����ʽ���пո���
        format.setEncoding("UTF-8");
        //1.����д������
        XMLWriter writer=new XMLWriter(out,format);
        //2.д��Document����
        writer.write(doc);
        //3.�ر���
        writer.close();
    }

	 public static void main(String[] args) {

				try {
					//DeleteXML(2);
//					AirportInfo airport=new AirportInfo(4, "�Ϻ����Ź��ʻ���", "ZSSS",121.328559644f,31.203323129f, "xiuxiu");
//					UpdateXML(airport);
					ReadXML();
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
//					catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
	}
}

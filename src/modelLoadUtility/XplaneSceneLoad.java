package modelLoadUtility;
import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetManager;
import com.jme3.asset.AssetNotFoundException;
import com.jme3.asset.ModelKey;
import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.texture.Texture;
import com.jme3.util.BufferUtils;

import DSFAnalyser.TextReader;
import dataEntity.AirportInfo;
import pathSmooth.Mercator;
/**
 * ��������ģ��
 * ����dsf�ļ����ݼ���ģ�ͺ͵���
 * @author zhangchao
 *
 */
public class XplaneSceneLoad {
	
	private   ArrayList<String> objectPath=new ArrayList<String>();
	private  ArrayList<String> polPath=new ArrayList<String>();
	   
	private   ArrayList<Integer> index_obj=new ArrayList<Integer>();
	private  ArrayList<Integer> index_pol=new ArrayList<Integer>();
	   
	private   ArrayList<Float> object_longitude=new ArrayList<Float>();
	private   ArrayList<Float> object_latitude=new ArrayList<Float>();
	private   ArrayList<Float> object_heading=new ArrayList<Float>();
	   
	private   ArrayList<Float> pol_longitude=new ArrayList<Float>();
	private   ArrayList<Float> pol_latitude=new ArrayList<Float>();
	private   ArrayList<Float> pol_uv=new ArrayList<Float>();
	
	//�����ĸ��ֶ���
	private AssetManager assetManager;
	private Node Terrain;
	private Node Model;
	
	//����������
	private double lon=0;
	private double lat=0;
	private String[] sceneContent=null;
	private String airportPath=null;
	
	/*
	 * ���캯��
	 */
	public XplaneSceneLoad()
	{
	}
	public XplaneSceneLoad(Node model,Node terrain,AssetManager assetManager)
	{
		this.assetManager=assetManager;
		this.Model=model;
		this.Terrain=terrain;
	}
	
	/**
	 * ��������������ɵؾ�����
	 */
	public void loadScene(AirportInfo airportinfo) {
		System.out.println("����loadScene");
		String[] temp=airportinfo.getFilePath().split("\\.");
    	switch (temp[1]) {
			case "txt": {
				try {
					sceneContent = TextReader.txtReader(airportinfo.getFilePath());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(sceneContent==null)
	    		System.out.println("��ȡtxt�ؾ��ļ�ʧ��");
				break;
			}
			case "dsf":{
				try {
					sceneContent = DSFAnalyser.DSF2TEXT.dsf2txttool(airportinfo.getFilePath());
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(sceneContent==null)
				System.out.println("��ȡdsf�ؾ��ļ�ʧ��");
				break;
			}
			 default:{
				System.out.println("����ʶ�𱻸������ļ�");
				break;
			}
     }//end switch
    	 if(sceneContent!=null) {
    		String[] temp2=airportinfo.getFilePath().split("/");
    		airportPath=temp2[1]+"/";
    		lon=airportinfo.getLontitude();
    		lat=airportinfo.getLatitude();
    		loadScene();
    	 }else {
    		 System.out.println("����ʧ�ܣ�");
    	 }

	}
	
    /**
     * ��������������ɵؾ�����
     */	
	public void loadScene(){
		
		  dsfParser(sceneContent);
		  loadModels();
		  loadTerrain();
		  //�����ڴ棨���飩
		  objectPath.clear();
		  polPath.clear();
		  index_obj.clear();
		  index_pol.clear();
		  object_longitude.clear();
		  object_latitude.clear();
		  object_heading.clear();
		  pol_longitude.clear();
		  pol_latitude.clear();
		  pol_uv.clear();
	}
		
	/**
	 * ���ػ���ģ��
	 */
	private void loadModels() {
	  String objpath;
	  int index;
	  double longitude,latitude,heading;
	  Spatial model;
	  double[] coord_xy;
      //���ĵ�
	  double[] center_xy=Mercator.lonLat2Mercator(lon, lat);
	  
	  for(int i=0;i<index_obj.size();i++) {
		  index=index_obj.get(i);
		  objpath=airportPath+objectPath.get(index);
		  //���ж��ļ��Ƿ����
		  if(LocateFile.LocateObjFile(objpath)) {
			  longitude=object_longitude.get(i);
			  latitude=object_latitude.get(i);
			  heading=object_heading.get(i);
			  //����ת��
			  coord_xy=Mercator.lonLat2Mercator(longitude, latitude);
			  
//			  System.out.println("loadModels����"+objpath);
			  model = assetManager.loadAsset(new ModelKey(objpath));
//			  System.out.println(objpath);
			  model.rotate(0.0f,(float)Math.toRadians(heading),0.0f).move( (float)(coord_xy[0]-center_xy[0]), 0.0f, (float)(-(coord_xy[1]-center_xy[1])));
			  Model.attachChild(model);  
//			  System.out.println("success model");
		  }
	  }//����forѭ��
		
	}
	/*
	 * ����������ͼ��������ͼ��
	 */
	private void loadTerrain() {
//		System.out.println("start terrain");
		  //��ʱ������ݵı���
		  String polpath;
		  int index;
		  double longitude,latitude;
//		  Spatial terrain;
		  double[] coord_xy;
          // ���������껺��
          FloatBuffer uv ;
          FloatBuffer vb ;
          ShortBuffer ib;
          //���ڴ������εļ����塢���񡢲��ʡ���ͼ
          Geometry geom;
          Material mat;
          Texture tex;
          Mesh mesh;
          //���ľ�γ������ת��
          double[] center_xy=Mercator.lonLat2Mercator(lon, lat);
          
		  for(int i=0;i<index_pol.size();i++) {
			  //ȡֵ
			  index=index_pol.get(i);
			  polpath=airportPath+polPath.get(index);
			 //�����ж�dds��pngͼƬ�Ƿ����		
			  polpath=LocateFile.LocateImageFile(polpath);
			  //�����ʼ��
	          uv = BufferUtils.createFloatBuffer(8);
              vb = BufferUtils.createFloatBuffer(12);
              ib = BufferUtils.createShortBuffer(6);
              //��ͼ�ļ����ڣ����м���
              if(polpath!=null) {

		              //����ȡ���ĸ�������Ϣ���浽������
		              for(int n=0;n<4;n++) {
			              longitude=pol_longitude.get(4*i+n);
						  latitude=pol_latitude.get(4*i+n);
			              //����ת��
						  coord_xy=Mercator.lonLat2Mercator(longitude, latitude);
						  //�洢ת���󶥵�����
						  vb.put((float)(coord_xy[0]-center_xy[0]));
						  vb.put(0.0f);
						  vb.put((float)-(coord_xy[1]-center_xy[1]));
						 
		              }  
		              //�洢uv��ͼ����
					  uv.put((float)pol_uv.get(8*i));
					  uv.put((float)pol_uv.get(8*i+1));
					  uv.put((float)pol_uv.get(8*i+2));
					  uv.put((float)pol_uv.get(8*i+3));
					  uv.put((float)pol_uv.get(8*i+4));
					  uv.put((float)pol_uv.get(8*i+5));
					  uv.put((float)pol_uv.get(8*i+6));
					  uv.put((float)pol_uv.get(8*i+7));
                      //�洢��������
				      ib.put((short) 0);
				      ib.put((short) 1);
				      ib.put((short) 2);
				      ib.put((short) 0);
				      ib.put((short) 2);
				      ib.put((short) 3);
				      // ��Buffer��дģʽ�л�����ģʽ
					  vb.flip();
				      uv.flip();
				      ib.flip();
				      //��������
				      mesh = new Mesh();
				      mesh.setBuffer(Type.Position, 3, vb);
				      mesh.setBuffer(Type.TexCoord, 2, uv);
				      mesh.setBuffer(Type.Index, 3, ib);
				      mesh.setStatic();
				      mesh.updateCounts();
				      mesh.updateBound();
				      //���ز���
				      mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
				    //͸���Ȳ�����ֵ
//				      mat.setFloat("AlphaDiscardThreshold", 0.1f);
				      
//				      TextureKey key=new TextureKey(polpath);
//				      key.setGenerateMips(true);
//				      tex = (Texture) assetManager.loadAsset(key);
				      
				      tex = assetManager.loadTexture(polpath);
//				      tex.setMinFilter(Texture.MinFilter.BilinearNoMipMaps);
//				      tex.setMinFilter(Texture.MinFilter.NearestNoMipMaps);
//				      tex.setMagFilter(Texture.MagFilter.Bilinear);
//				        System.out.println(tex.getName()+" "+tex.getMagFilter());
//				        System.out.println(tex.getName()+" "+tex.getMinFilter());
//				        System.out.println(tex.getName()+" "+tex.getMinFilter().usesMipMapLevels());
				      mat.setTexture("ColorMap", tex);
				    //�������������˫����Ⱦ
					  mat.getAdditionalRenderState().setFaceCullMode(com.jme3.material.RenderState.FaceCullMode.Off);
				      //��Ч����
//				      mat.setBoolean("UseAlpha",true);
				      //͸������
//				      mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
				      //ģ��Ϊ����ɫ
//				      mat.getAdditionalRenderState().setBlendMode(BlendMode.AlphaAdditive);
//				      mat.getAdditionalRenderState().setBlendMode(BlendMode.Additive);
				      //������������
				      geom = new Geometry(polpath);
				      geom.setMesh(mesh);
				      geom.setMaterial(mat);
//				      geom.setQueueBucket(Bucket.Translucent);
//				      geom.setQueueBucket(Bucket.Transparent);
//				      geom.rotate((float)Math.PI,0f,0f);
				      
					  Terrain.attachChild(geom);  
//					  System.out.println("success teerain");
              }//existFlag�жϽ���

		  }//����forѭ���������ļ���
	}
	

	
	/**
	 * dsf�ļ����ݽ����������Ϣ�洢��List��
	 */
	private void dsfParser (String[] dsfContent){
		   
	   int polygon=0;
       boolean polBegin=false;
	   String line = null;  
	   
	   for(int i=0;i<dsfContent.length;i++) { 
		   
		      line=dsfContent[i];
		      
		     //ƥ��object�ļ�·��
			  Pattern p1=Pattern.compile("OBJECT_DEF");  
			  Matcher m1=p1.matcher(line);  
			  if( m1.find())
			  {
				  line=line.substring(11);
				  line=line.replaceAll("\r|\n", "");
				  line=line.replace("\\\\", "/");
				   objectPath.add(line);
//				  System.out.println(line);
				  continue;
			  }
			  
			  //ƥ��polygon�ļ�·��
			   p1=Pattern.compile("POLYGON_DEF");  
			   m1=p1.matcher(line); 
			  if( m1.find())
			  {
				  line=line.substring(12);
				  line=line.replaceAll("\r|\n", "");
				  line=line.replaceAll("\\\\", "/");
//				  System.out.println(line);
				  polPath.add(line);

				  continue;
			  }
			  
			  //ƥ��obj�ļ�ʹ����������ꡢ������
			  p1=Pattern.compile("OBJECT \\d");  
			  m1=p1.matcher(line);
			  if( m1.find())
			  {
				  //ȥ����ʶ��
				  line=line.substring(7);
				  p1=Pattern.compile("\\d+ ");  
				  m1=p1.matcher(line);
				  if(m1.find()) {
				  String temp=m1.group();
                  //��¼obj�Ľ�����
				  index_obj.add(Integer.parseInt(temp.trim()));
				  line=line.substring(temp.length());
                  //��¼objģ��λ��
				  String[] strs=line.split(" ");
				  object_longitude.add(Float.parseFloat(strs[0].trim()));
				  object_latitude.add(Float.parseFloat(strs[1].trim()));
				  object_heading.add(Float.parseFloat(strs[2].trim()));
				  }
				  continue;
			  }
			  
			  //ƥ��polygon�ļ�̧ͷ�����õ�polygon�ļ�����
			  p1=Pattern.compile("BEGIN_POLYGON \\d+ 65535");  
			  m1=p1.matcher(line);
			  if( m1.find())
			  {
				  line=line.substring(14);
				  p1=Pattern.compile("\\d+ ");  
				  m1=p1.matcher(line);
				  if(m1.find()) {
				  String temp=m1.group();
				  index_pol.add(Integer.parseInt(temp.trim()));
				  }			  
				  polBegin=true;
				  polygon=0;
				  continue;		 
			  }			  
				 
			  //ƥ��polygon�ļ�������Ϣ������¼������
			  p1=Pattern.compile("POLYGON_POINT ");  
			  m1=p1.matcher(line);
			  if( m1.find()&&polBegin)
			  {
//				  System.out.println(line);
				  line=line.substring(14);
				  String[] strs=line.split(" ");
				  pol_longitude.add(Float.parseFloat(strs[0].trim()));
				  pol_latitude.add(Float.parseFloat(strs[1].trim()));
				  pol_uv.add(Float.parseFloat(strs[2].trim()));
				  pol_uv.add(Float.parseFloat(strs[3].trim()));
				  polygon++;
				  if(polygon==4) {
					  polygon=0;
					  polBegin=false;
					  
				  }
			  }

	       }//����whileѭ��
  }
	
	
	
	public static void main(String[] args) throws Throwable {
		String[] dsf=DSFAnalyser.DSF2TEXT.dsf2txttool("D:\\eclipse-workspace\\JmeAirportScene\\assets\\ZBAA\\Earth nav data\\+40+110\\+40+116.dsf"); 
		for(int i=0;i<1000;i++) {
			System.out.print(dsf[i]);
		}
//		XplaneSceneLoad load=new XplaneSceneLoad();
//		load.dsfParser(dsf);
	}


}

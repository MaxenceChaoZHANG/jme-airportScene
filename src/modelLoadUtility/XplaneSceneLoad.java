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
 * 场景加载模块
 * 根据dsf文件内容加载模型和地形
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
	
	//拷贝的各种对象
	private AssetManager assetManager;
	private Node Terrain;
	private Node Model;
	
	//天津滨海机场
	private double lon=0;
	private double lat=0;
	private String[] sceneContent=null;
	private String airportPath=null;
	
	/*
	 * 构造函数
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
	 * 调用其他方法完成地景加载
	 */
	public void loadScene(AirportInfo airportinfo) {
		System.out.println("进入loadScene");
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
	    		System.out.println("读取txt地景文件失败");
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
				System.out.println("读取dsf地景文件失败");
				break;
			}
			 default:{
				System.out.println("不能识别被该类型文件");
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
    		 System.out.println("加载失败！");
    	 }

	}
	
    /**
     * 调用其他方法完成地景加载
     */	
	public void loadScene(){
		
		  dsfParser(sceneContent);
		  loadModels();
		  loadTerrain();
		  //清理内存（数组）
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
	 * 加载机场模型
	 */
	private void loadModels() {
	  String objpath;
	  int index;
	  double longitude,latitude,heading;
	  Spatial model;
	  double[] coord_xy;
      //中心点
	  double[] center_xy=Mercator.lonLat2Mercator(lon, lat);
	  
	  for(int i=0;i<index_obj.size();i++) {
		  index=index_obj.get(i);
		  objpath=airportPath+objectPath.get(index);
		  //先判断文件是否存在
		  if(LocateFile.LocateObjFile(objpath)) {
			  longitude=object_longitude.get(i);
			  latitude=object_latitude.get(i);
			  heading=object_heading.get(i);
			  //坐标转换
			  coord_xy=Mercator.lonLat2Mercator(longitude, latitude);
			  
//			  System.out.println("loadModels方法"+objpath);
			  model = assetManager.loadAsset(new ModelKey(objpath));
//			  System.out.println(objpath);
			  model.rotate(0.0f,(float)Math.toRadians(heading),0.0f).move( (float)(coord_xy[0]-center_xy[0]), 0.0f, (float)(-(coord_xy[1]-center_xy[1])));
			  Model.attachChild(model);  
//			  System.out.println("success model");
		  }
	  }//结束for循环
		
	}
	/*
	 * 加载卫星贴图（地形贴图）
	 */
	private void loadTerrain() {
//		System.out.println("start terrain");
		  //临时存放数据的变量
		  String polpath;
		  int index;
		  double longitude,latitude;
//		  Spatial terrain;
		  double[] coord_xy;
          // 纹理顶点坐标缓存
          FloatBuffer uv ;
          FloatBuffer vb ;
          ShortBuffer ib;
          //用于创建地形的几何体、网格、材质、贴图
          Geometry geom;
          Material mat;
          Texture tex;
          Mesh mesh;
          //中心经纬度坐标转换
          double[] center_xy=Mercator.lonLat2Mercator(lon, lat);
          
		  for(int i=0;i<index_pol.size();i++) {
			  //取值
			  index=index_pol.get(i);
			  polpath=airportPath+polPath.get(index);
			 //以下判断dds或png图片是否存在		
			  polpath=LocateFile.LocateImageFile(polpath);
			  //缓存初始化
	          uv = BufferUtils.createFloatBuffer(8);
              vb = BufferUtils.createFloatBuffer(12);
              ib = BufferUtils.createShortBuffer(6);
              //贴图文件存在，进行加载
              if(polpath!=null) {

		              //遍历取出四个顶点信息，存到缓存中
		              for(int n=0;n<4;n++) {
			              longitude=pol_longitude.get(4*i+n);
						  latitude=pol_latitude.get(4*i+n);
			              //坐标转换
						  coord_xy=Mercator.lonLat2Mercator(longitude, latitude);
						  //存储转换后顶点坐标
						  vb.put((float)(coord_xy[0]-center_xy[0]));
						  vb.put(0.0f);
						  vb.put((float)-(coord_xy[1]-center_xy[1]));
						 
		              }  
		              //存储uv贴图坐标
					  uv.put((float)pol_uv.get(8*i));
					  uv.put((float)pol_uv.get(8*i+1));
					  uv.put((float)pol_uv.get(8*i+2));
					  uv.put((float)pol_uv.get(8*i+3));
					  uv.put((float)pol_uv.get(8*i+4));
					  uv.put((float)pol_uv.get(8*i+5));
					  uv.put((float)pol_uv.get(8*i+6));
					  uv.put((float)pol_uv.get(8*i+7));
                      //存储顶点索引
				      ib.put((short) 0);
				      ib.put((short) 1);
				      ib.put((short) 2);
				      ib.put((short) 0);
				      ib.put((short) 2);
				      ib.put((short) 3);
				      // 将Buffer从写模式切换到读模式
					  vb.flip();
				      uv.flip();
				      ib.flip();
				      //构建网格
				      mesh = new Mesh();
				      mesh.setBuffer(Type.Position, 3, vb);
				      mesh.setBuffer(Type.TexCoord, 2, uv);
				      mesh.setBuffer(Type.Index, 3, ib);
				      mesh.setStatic();
				      mesh.updateCounts();
				      mesh.updateBound();
				      //加载材质
				      mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
				    //透明度测试阈值
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
				    //设置三角形面的双面渲染
					  mat.getAdditionalRenderState().setFaceCullMode(com.jme3.material.RenderState.FaceCullMode.Off);
				      //无效命令
//				      mat.setBoolean("UseAlpha",true);
				      //透明设置
//				      mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
				      //模型为背景色
//				      mat.getAdditionalRenderState().setBlendMode(BlendMode.AlphaAdditive);
//				      mat.getAdditionalRenderState().setBlendMode(BlendMode.Additive);
				      //创建几何形体
				      geom = new Geometry(polpath);
				      geom.setMesh(mesh);
				      geom.setMaterial(mat);
//				      geom.setQueueBucket(Bucket.Translucent);
//				      geom.setQueueBucket(Bucket.Transparent);
//				      geom.rotate((float)Math.PI,0f,0f);
				      
					  Terrain.attachChild(geom);  
//					  System.out.println("success teerain");
              }//existFlag判断结束

		  }//结束for循环（地形文件）
	}
	

	
	/**
	 * dsf文件内容解读，并将信息存储到List中
	 */
	private void dsfParser (String[] dsfContent){
		   
	   int polygon=0;
       boolean polBegin=false;
	   String line = null;  
	   
	   for(int i=0;i<dsfContent.length;i++) { 
		   
		      line=dsfContent[i];
		      
		     //匹配object文件路径
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
			  
			  //匹配polygon文件路径
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
			  
			  //匹配obj文件使用情况，坐标、个数等
			  p1=Pattern.compile("OBJECT \\d");  
			  m1=p1.matcher(line);
			  if( m1.find())
			  {
				  //去掉标识符
				  line=line.substring(7);
				  p1=Pattern.compile("\\d+ ");  
				  m1=p1.matcher(line);
				  if(m1.find()) {
				  String temp=m1.group();
                  //记录obj文建索引
				  index_obj.add(Integer.parseInt(temp.trim()));
				  line=line.substring(temp.length());
                  //记录obj模型位置
				  String[] strs=line.split(" ");
				  object_longitude.add(Float.parseFloat(strs[0].trim()));
				  object_latitude.add(Float.parseFloat(strs[1].trim()));
				  object_heading.add(Float.parseFloat(strs[2].trim()));
				  }
				  continue;
			  }
			  
			  //匹配polygon文件抬头，并得到polygon文件索引
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
				 
			  //匹配polygon文件坐标信息，并记录顶点数
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

	       }//结束while循环
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

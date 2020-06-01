package modelLoadUtility;


import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetLoader;
import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.texture.Texture;
import com.jme3.util.BufferUtils;

/**
 * 解析.obj(Xplane)格式的模型文件
 * @author zhangcaho
 *
 */
public class XplaneObjLoader implements AssetLoader {

    private List<Float> vertex = new ArrayList<Float>();
    private List<Float> normal = new ArrayList<Float>();
    private List<Float> texCoord = new ArrayList<Float>();
    private List<Integer> face = new ArrayList<Integer>();
    
    private List<LODInfo> lods=new ArrayList<LODInfo>(); 
    private List<Integer> tris=new ArrayList<Integer>();
    
    private String TexturePath =new String();
    private AssetManager assetManager;
    private AssetKey key;
    
    
    class LODInfo{
    	int lodstart;
    	int lodend;
    	List<Integer> tris = new ArrayList<Integer>();
    	
    	public LODInfo(int lodstart, int losend) {
			this.lodstart = lodstart;
			this.lodend = losend;
		}
    	
		public int getLodstart() {
			return lodstart;
		}
		public void setLodstart(int lodstart) {
			this.lodstart = lodstart;
		}
		public int getLosend() {
			return lodend;
		}
		public void setLosend(int losend) {
			this.lodend = losend;
		}
		public List<Integer> getTris() {
			return tris;
		}
		public void addTris(int tri) {
			this.tris.add(tri);
		}    	
    }
    
    public Object load(AssetInfo assetInfo) throws IOException {
        assetManager = assetInfo.getManager();
        key = assetInfo.getKey();
        
        // 解析文件数据
        xplaneObjParse(assetInfo);
        
        Geometry geom =buildGeometry();
        
        // 清空数据
        vertex.clear();
        texCoord.clear();
        normal.clear();
        face.clear();
        lods.clear();
        tris.clear();
        
        return geom;
    }

    /**
     * 解析文件数据
     * @param assetInfo
     * @throws IOException
     */
    private void xplaneObjParse(AssetInfo assetInfo) throws IOException {
        // 打开文件流
        InputStream in = assetInfo.openStream();
        Scanner scanner = new Scanner(in);

        // 逐行解析文件
        String line;
        LODInfo lodinfo = null;
        while(scanner.hasNextLine()) {
            line = scanner.nextLine();

			    //匹配贴图文件信息
			    Pattern p=Pattern.compile("TEXTURE\\s+");  
			    Matcher  m=p.matcher(line); 
				  if( m.find())
				  {   
					//按空格、制表符分开
					  String[] strs=line.split("\\s+");
					//贴图路径构造
//					  System.out.println(key+line);
					  TexturePath=key.getFolder()+strs[1];
//					  System.out.println("xplaneParse方法"+TexturePath);
					  continue;
				  }

			     //匹配图形顶点
				   p=Pattern.compile("VT");  
				   m=p.matcher(line);  
				  if( m.find())
				  {  
					  //按空格、制表符分开
					  String[] strs=line.split("\\s+");
					  //提取顶点
					  float a = Float.valueOf(strs[1].trim());
		              float b = Float.valueOf(strs[2].trim());
		              float c = Float.valueOf(strs[3].trim());
		              vertex.add(a);
		              vertex.add(b);
		              vertex.add(c);
		            //提取法向量
		               a = Float.valueOf(strs[4].trim());
		               b = Float.valueOf(strs[5].trim());
		               c = Float.valueOf(strs[6].trim());
		              normal.add(a);
			          normal.add(b);
			          normal.add(c);
			        //提取贴图坐标
			           a = Float.valueOf(strs[7].trim());
		               b = Float.valueOf(strs[8].trim());
			          texCoord.add(a);
			          texCoord.add(b);
					  continue;
				  }
				  //匹配顶点索引
				  p=Pattern.compile("IDX");  
				  m=p.matcher(line); 
				  if( m.find())
				  {   
					//按空格、制表符分开
					  String[] strs=line.split("\\s+");
					  int idx=0;
					  for(int i=1;i<strs.length;i++)
					  { 
						  idx = Integer.valueOf(strs[i].trim());
						  face.add(idx);
//				          System.out.print(strs[i]+" ");
					  }
//					  System.out.println(" ");
					  continue;
				  }
				  
				  //匹配LOD
				  p=Pattern.compile("ATTR_LOD\\s+");  
				  m=p.matcher(line); 
				  if( m.find())
				  {   
					//按空格、制表符分开

					  String[] strs=line.split("\\s+");
					  int lodstart=Integer.parseInt(strs[1].trim());
					  int lodend=Integer.parseInt(strs[2].trim());
					  lodinfo=new LODInfo(lodstart, lodend);
					  lods.add(lodinfo);
					  continue;
				  }
				  
				  //匹配TRIS
				  p=Pattern.compile("\\s*TRIS\\s+");  
				  m=p.matcher(line); 
				  if( m.find())
				  {   
					//先去前后空格，再按空格、制表符分开
					  String[] strs=line.trim().split("\\s+");
					  int tri;
					  for(int i=1;i<strs.length;i++)
					  {
//						  System.out.println(strs[i]);
						  tri=Integer.parseInt(strs[i].trim());
						  tris.add(tri);
						  if(lodinfo!=null)
						  lodinfo.addTris(tri);
					  }
					  if(lodinfo!=null)
//						  System.out.println("打印lod信息"+lodinfo.tris);
					  continue;
				  }

		       }//结束while循环
        
        // 关闭文件流
        scanner.close();
        in.close();
    }
    
    /**
     * 生成网格
     * @return
     */
    private Geometry buildGeometry() {
    	
    	List<Integer> temptris;
    	int geoStart = 0;
    	int total = 0;
    	int n;
    	
    	if(lods.size()!=0) {
    	  //提取第一LOD的几何信息
    	  temptris=lods.get(0).getTris();
    	}else {
    	  temptris=tris;
    	}
    	n=temptris.size();
    	
        	//下面整形参量为顶点索引的开始和结束

        geoStart=tris.get(0);
	    for(int i=0;i<n/2;i++) {
	      total=temptris.get(i*2+1)+total;
	     }

    	
//    	System.out.println(geoStart+"  "+total);
        // 生成3D网格
        Mesh mesh = buildMesh(geoStart,total);
        
        // 创建几何体
        Geometry geom = new Geometry(key.getName());
        geom.setMesh(mesh);
        
        // 加载材质
        Material mat = buildMaterial();
        geom.setMaterial(mat);
        //将Geometry添加到透明物体队列，以确保正确的绘制循序
        geom.setQueueBucket(Bucket.Translucent);
//        geom.setQueueBucket(Bucket.Transparent);
        
		return geom;
    	
    }
    
    /**
     * 生成材质
     * @return
     */
    private Material buildMaterial() {
    	
    	// 创建材质，使我们可以看见这图形
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");  
        //透明度测试阈值
        mat.setFloat("AlphaDiscardThreshold", 0.1f);
        //判断贴图文件格式(有时路径为dds格式，但实际只有png格式)
	    if((TexturePath=LocateFile.LocateImageFile(TexturePath))!=null) {
			// 设置纹理贴图 
//	    	 TextureKey key=new TextureKey(TexturePath);
//		      key.setGenerateMips(true);
//		     Texture tex = (Texture) assetManager.loadAsset(key);
	    	
	        Texture tex = assetManager.loadTexture(TexturePath);

//	        tex.setMinFilter(Texture.MinFilter.NearestNearestMipMap);
//	        tex.setMinFilter(Texture.MinFilter.BilinearNoMipMaps);
//		    tex.setMagFilter(Texture.MagFilter.Bilinear);
	        mat.setTexture("ColorMap", tex);
//	        System.out.println(tex.getName()+" "+tex.getMagFilter());
//	        System.out.println(tex.getName()+" "+tex.getMinFilter());
//	        System.out.println(tex.getName()+" "+tex.getMinFilter().usesMipMapLevels());
		}	
	    
		//设置三角形面的双面渲染
		mat.getAdditionalRenderState().setFaceCullMode(com.jme3.material.RenderState.FaceCullMode.Off);
          //无效命令
//	      mat.setBoolean("UseAlpha",true);
	      //透明设置
	      mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
	      //模型为背景色
//	      mat.getAdditionalRenderState().setBlendMode(BlendMode.AlphaAdditive);
//	      mat.getAdditionalRenderState().setBlendMode(BlendMode.Additive);
		return mat;
    	
    }
    private Mesh buildMesh(int geoStart,int total) {
        // 顶点缓存
        int count = vertex.size();
        FloatBuffer vb = BufferUtils.createFloatBuffer(count);
        for(int i=0; i<count; i++) {
            vb.put(vertex.get(i).floatValue());
        }
        vb.flip();
        
     // 法向量缓存
        count = normal.size();
        FloatBuffer vector = BufferUtils.createFloatBuffer(count);
        for(int i=0; i<count; i++) {
            vector.put(normal.get(i).floatValue());
        }
        vector.flip();
        
        // 纹理坐标缓存
        count = texCoord.size();
        FloatBuffer uv = BufferUtils.createFloatBuffer(count);
        for(int i=0; i<count; i++) {
            uv.put(texCoord.get(i).floatValue());
        }
        uv.flip();
        
        // 索引缓存
        int geoEnd = total-geoStart-1;
        ShortBuffer ib = BufferUtils.createShortBuffer(total);
        for(int i=geoStart; i<=geoEnd; i++) {
            ib.put(face.get(i).shortValue());
        }
        ib.flip();
        
        // 创建jME3的网格
        Mesh mesh = new Mesh();
        mesh.setBuffer(Type.Position, 3, vb);
        mesh.setBuffer(Type.Normal, 3, vector);
        mesh.setBuffer(Type.TexCoord, 2, uv);
        mesh.setBuffer(Type.Index, 3, ib);
        mesh.setStatic();
        
        mesh.updateCounts();
        mesh.updateBound();
//        System.out.println("三角形面数："+mesh.getTriangleCount());;
        return mesh;
    }
}

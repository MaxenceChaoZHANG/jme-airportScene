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
 * ����.obj(Xplane)��ʽ��ģ���ļ�
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
        
        // �����ļ�����
        xplaneObjParse(assetInfo);
        
        Geometry geom =buildGeometry();
        
        // �������
        vertex.clear();
        texCoord.clear();
        normal.clear();
        face.clear();
        lods.clear();
        tris.clear();
        
        return geom;
    }

    /**
     * �����ļ�����
     * @param assetInfo
     * @throws IOException
     */
    private void xplaneObjParse(AssetInfo assetInfo) throws IOException {
        // ���ļ���
        InputStream in = assetInfo.openStream();
        Scanner scanner = new Scanner(in);

        // ���н����ļ�
        String line;
        LODInfo lodinfo = null;
        while(scanner.hasNextLine()) {
            line = scanner.nextLine();

			    //ƥ����ͼ�ļ���Ϣ
			    Pattern p=Pattern.compile("TEXTURE\\s+");  
			    Matcher  m=p.matcher(line); 
				  if( m.find())
				  {   
					//���ո��Ʊ���ֿ�
					  String[] strs=line.split("\\s+");
					//��ͼ·������
//					  System.out.println(key+line);
					  TexturePath=key.getFolder()+strs[1];
//					  System.out.println("xplaneParse����"+TexturePath);
					  continue;
				  }

			     //ƥ��ͼ�ζ���
				   p=Pattern.compile("VT");  
				   m=p.matcher(line);  
				  if( m.find())
				  {  
					  //���ո��Ʊ���ֿ�
					  String[] strs=line.split("\\s+");
					  //��ȡ����
					  float a = Float.valueOf(strs[1].trim());
		              float b = Float.valueOf(strs[2].trim());
		              float c = Float.valueOf(strs[3].trim());
		              vertex.add(a);
		              vertex.add(b);
		              vertex.add(c);
		            //��ȡ������
		               a = Float.valueOf(strs[4].trim());
		               b = Float.valueOf(strs[5].trim());
		               c = Float.valueOf(strs[6].trim());
		              normal.add(a);
			          normal.add(b);
			          normal.add(c);
			        //��ȡ��ͼ����
			           a = Float.valueOf(strs[7].trim());
		               b = Float.valueOf(strs[8].trim());
			          texCoord.add(a);
			          texCoord.add(b);
					  continue;
				  }
				  //ƥ�䶥������
				  p=Pattern.compile("IDX");  
				  m=p.matcher(line); 
				  if( m.find())
				  {   
					//���ո��Ʊ���ֿ�
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
				  
				  //ƥ��LOD
				  p=Pattern.compile("ATTR_LOD\\s+");  
				  m=p.matcher(line); 
				  if( m.find())
				  {   
					//���ո��Ʊ���ֿ�

					  String[] strs=line.split("\\s+");
					  int lodstart=Integer.parseInt(strs[1].trim());
					  int lodend=Integer.parseInt(strs[2].trim());
					  lodinfo=new LODInfo(lodstart, lodend);
					  lods.add(lodinfo);
					  continue;
				  }
				  
				  //ƥ��TRIS
				  p=Pattern.compile("\\s*TRIS\\s+");  
				  m=p.matcher(line); 
				  if( m.find())
				  {   
					//��ȥǰ��ո��ٰ��ո��Ʊ���ֿ�
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
//						  System.out.println("��ӡlod��Ϣ"+lodinfo.tris);
					  continue;
				  }

		       }//����whileѭ��
        
        // �ر��ļ���
        scanner.close();
        in.close();
    }
    
    /**
     * ��������
     * @return
     */
    private Geometry buildGeometry() {
    	
    	List<Integer> temptris;
    	int geoStart = 0;
    	int total = 0;
    	int n;
    	
    	if(lods.size()!=0) {
    	  //��ȡ��һLOD�ļ�����Ϣ
    	  temptris=lods.get(0).getTris();
    	}else {
    	  temptris=tris;
    	}
    	n=temptris.size();
    	
        	//�������β���Ϊ���������Ŀ�ʼ�ͽ���

        geoStart=tris.get(0);
	    for(int i=0;i<n/2;i++) {
	      total=temptris.get(i*2+1)+total;
	     }

    	
//    	System.out.println(geoStart+"  "+total);
        // ����3D����
        Mesh mesh = buildMesh(geoStart,total);
        
        // ����������
        Geometry geom = new Geometry(key.getName());
        geom.setMesh(mesh);
        
        // ���ز���
        Material mat = buildMaterial();
        geom.setMaterial(mat);
        //��Geometry��ӵ�͸��������У���ȷ����ȷ�Ļ���ѭ��
        geom.setQueueBucket(Bucket.Translucent);
//        geom.setQueueBucket(Bucket.Transparent);
        
		return geom;
    	
    }
    
    /**
     * ���ɲ���
     * @return
     */
    private Material buildMaterial() {
    	
    	// �������ʣ�ʹ���ǿ��Կ�����ͼ��
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");  
        //͸���Ȳ�����ֵ
        mat.setFloat("AlphaDiscardThreshold", 0.1f);
        //�ж���ͼ�ļ���ʽ(��ʱ·��Ϊdds��ʽ����ʵ��ֻ��png��ʽ)
	    if((TexturePath=LocateFile.LocateImageFile(TexturePath))!=null) {
			// ����������ͼ 
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
	    
		//�������������˫����Ⱦ
		mat.getAdditionalRenderState().setFaceCullMode(com.jme3.material.RenderState.FaceCullMode.Off);
          //��Ч����
//	      mat.setBoolean("UseAlpha",true);
	      //͸������
	      mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
	      //ģ��Ϊ����ɫ
//	      mat.getAdditionalRenderState().setBlendMode(BlendMode.AlphaAdditive);
//	      mat.getAdditionalRenderState().setBlendMode(BlendMode.Additive);
		return mat;
    	
    }
    private Mesh buildMesh(int geoStart,int total) {
        // ���㻺��
        int count = vertex.size();
        FloatBuffer vb = BufferUtils.createFloatBuffer(count);
        for(int i=0; i<count; i++) {
            vb.put(vertex.get(i).floatValue());
        }
        vb.flip();
        
     // ����������
        count = normal.size();
        FloatBuffer vector = BufferUtils.createFloatBuffer(count);
        for(int i=0; i<count; i++) {
            vector.put(normal.get(i).floatValue());
        }
        vector.flip();
        
        // �������껺��
        count = texCoord.size();
        FloatBuffer uv = BufferUtils.createFloatBuffer(count);
        for(int i=0; i<count; i++) {
            uv.put(texCoord.get(i).floatValue());
        }
        uv.flip();
        
        // ��������
        int geoEnd = total-geoStart-1;
        ShortBuffer ib = BufferUtils.createShortBuffer(total);
        for(int i=geoStart; i<=geoEnd; i++) {
            ib.put(face.get(i).shortValue());
        }
        ib.flip();
        
        // ����jME3������
        Mesh mesh = new Mesh();
        mesh.setBuffer(Type.Position, 3, vb);
        mesh.setBuffer(Type.Normal, 3, vector);
        mesh.setBuffer(Type.TexCoord, 2, uv);
        mesh.setBuffer(Type.Index, 3, ib);
        mesh.setStatic();
        
        mesh.updateCounts();
        mesh.updateBound();
//        System.out.println("������������"+mesh.getTriangleCount());;
        return mesh;
    }
}

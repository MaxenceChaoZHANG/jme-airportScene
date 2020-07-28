package jmeDemo.Hello;


import com.jme3.app.SimpleApplication;
import com.jme3.asset.ModelKey;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.shape.Torus;
import com.jme3.system.AppSettings;
import com.jme3.system.awt.AwtPanelsContext;
import atmosphere.NewAtmosphereState;
import modelLoadUtility.XplaneObjLoader;



/**
 * ������
 * @author zhangchao
 *
 */
public class App extends SimpleApplication {

    @Override
    public void simpleInitApp() {
        // ע���ʲ�·��
        assetManager.registerLocator("assets", FileLocator.class);
        assetManager.registerLoader(XplaneObjLoader.class, "obj");
        flyCam.setDragToRotate(true);
        //����Բ��
//        Torus mesh=new Torus(5,20,0.3f,1.5f);
//        Geometry geom = new Geometry("Box", mesh);
//        Material mat=new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
//        mat.setColor("Color", ColorRGBA.Blue);
//        geom.setMaterial(mat);
        
        
        Spatial spatial=assetManager.loadAsset(new ModelKey("Models/aircraft/jets/heavy/b737-800/delta/object.obj"));
        
        // ������
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(new ColorRGBA(0.7f, 0.7f, 0.7f, 1f));
        // ����
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-1, -2, -3).normalizeLocal());
        //���ñ���ɫ��ɫ
        viewPort.setBackgroundColor(ColorRGBA.White);
        
//		createArrow(new Vector3f(40, 0, 0), ColorRGBA.Green);
//	    createArrow(new Vector3f(0, 40, 0), ColorRGBA.Red);
//	    createArrow(new Vector3f(0, 0, 40), ColorRGBA.Blue);
        
        rootNode.attachChild(spatial);
        rootNode.addLight(ambient);
        rootNode.addLight(sun);    
    }

    
    
	  private void createArrow(Vector3f vec3, ColorRGBA color) {
	        // �������ʣ��趨��ͷ����ɫ
	        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
	        mat.setColor("Color", color);
	        // �����������壬Ӧ�ü�ͷ����
	        Geometry geom = new Geometry("arrow", new Arrow(vec3));
	        geom.setMaterial(mat);
	        geom.setLocalTranslation(0.0f, 2.0f, 0.0f);
	        // ��ӵ�������
	        rootNode.attachChild(geom);
	    }
	  
    public static void main(String[] args) {
        App app = new App();
        app.setShowSettings(false);
        app.start();   
    }
    

}





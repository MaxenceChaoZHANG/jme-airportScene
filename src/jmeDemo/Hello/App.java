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
 * 主程序
 * @author zhangchao
 *
 */
public class App extends SimpleApplication {

    @Override
    public void simpleInitApp() {
        // 注册资产路径
        assetManager.registerLocator("assets", FileLocator.class);
        assetManager.registerLoader(XplaneObjLoader.class, "obj");
        flyCam.setDragToRotate(true);
        //创建圆环
//        Torus mesh=new Torus(5,20,0.3f,1.5f);
//        Geometry geom = new Geometry("Box", mesh);
//        Material mat=new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
//        mat.setColor("Color", ColorRGBA.Blue);
//        geom.setMaterial(mat);
        
        
        Spatial spatial=assetManager.loadAsset(new ModelKey("Models/aircraft/jets/heavy/b737-800/delta/object.obj"));
        
        // 环境光
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(new ColorRGBA(0.7f, 0.7f, 0.7f, 1f));
        // 阳光
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-1, -2, -3).normalizeLocal());
        //设置背景色白色
        viewPort.setBackgroundColor(ColorRGBA.White);
        
//		createArrow(new Vector3f(40, 0, 0), ColorRGBA.Green);
//	    createArrow(new Vector3f(0, 40, 0), ColorRGBA.Red);
//	    createArrow(new Vector3f(0, 0, 40), ColorRGBA.Blue);
        
        rootNode.attachChild(spatial);
        rootNode.addLight(ambient);
        rootNode.addLight(sun);    
    }

    
    
	  private void createArrow(Vector3f vec3, ColorRGBA color) {
	        // 创建材质，设定箭头的颜色
	        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
	        mat.setColor("Color", color);
	        // 创建几何物体，应用箭头网格。
	        Geometry geom = new Geometry("arrow", new Arrow(vec3));
	        geom.setMaterial(mat);
	        geom.setLocalTranslation(0.0f, 2.0f, 0.0f);
	        // 添加到场景中
	        rootNode.attachChild(geom);
	    }
	  
    public static void main(String[] args) {
        App app = new App();
        app.setShowSettings(false);
        app.start();   
    }
    

}





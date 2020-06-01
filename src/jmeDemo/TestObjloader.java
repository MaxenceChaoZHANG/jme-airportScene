package jmeDemo;

import com.jme3.app.SimpleApplication;

import com.jme3.asset.plugins.FileLocator;
import com.jme3.bullet.BulletAppState;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import atmosphere.NewAtmosphereState;
import modelLoadUtility.XplaneObjLoader;



/**
 * 主程序
 * @author zhangchao
 *
 */
public class TestObjloader extends SimpleApplication {

    @Override
    public void simpleInitApp() {
    	
        // 注册资产路径
        assetManager.registerLocator("assets", FileLocator.class);
        // 注册解析器
        assetManager.registerLoader(XplaneObjLoader.class, "obj");
        viewPort.setBackgroundColor(ColorRGBA.LightGray);

        //设置相机参数
//        cam.setLocation(new Vector3f(0f,25f,0f));
        flyCam.setDragToRotate(true);
        flyCam.setMoveSpeed(100);
        flyCam.setZoomSpeed(10);
     
        // 环境光
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(new ColorRGBA(0.7f, 0.7f, 0.7f, 1f));

        // 阳光
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-1, -2, -3).normalizeLocal());
        
        
        Spatial model = assetManager.loadModel("ZBAA/objects/T3M.obj");
        
        rootNode.addLight(ambient);
        rootNode.addLight(sun);
        rootNode.attachChild(model);
    }

    public static void main(String[] args) {
    	TestObjloader app = new TestObjloader();
        app.start();
       
    }

}

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
 * ������
 * @author zhangchao
 *
 */
public class TestObjloader extends SimpleApplication {

    @Override
    public void simpleInitApp() {
    	
        // ע���ʲ�·��
        assetManager.registerLocator("assets", FileLocator.class);
        // ע�������
        assetManager.registerLoader(XplaneObjLoader.class, "obj");
        viewPort.setBackgroundColor(ColorRGBA.LightGray);

        //�����������
//        cam.setLocation(new Vector3f(0f,25f,0f));
        flyCam.setDragToRotate(true);
        flyCam.setMoveSpeed(100);
        flyCam.setZoomSpeed(10);
     
        // ������
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(new ColorRGBA(0.7f, 0.7f, 0.7f, 1f));

        // ����
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

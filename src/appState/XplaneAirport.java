package appState;


import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import com.jme3.system.awt.AwtPanelsContext;
import com.jme3.util.BufferUtils;

import atmosphere.NewAtmosphereState;
import modelLoadUtility.XplaneObjLoader;



/**
 * ������
 * @author zhangchao
 *
 */
public class XplaneAirport extends SimpleApplication {

    @Override
    public void simpleInitApp() {
        // ע���ʲ�·��
        assetManager.registerLocator("assets", FileLocator.class);
        // ע�������
        assetManager.registerLoader(XplaneObjLoader.class, "obj");
        BufferUtils.setTrackDirectMemoryEnabled(true);
    
        //���������ڵ�
        Node Scene=new Node("Scene");
        Node Terrain=new Node("Terrain");
        Node Model=new Node("Model");
        Scene.attachChild(Model);
        Scene.attachChild(Terrain);
        rootNode.attachChild(Scene);
        
        flyCam.setDragToRotate(true);
        //����appState
        stateManager.attachAll(
        		               new SceneAppState(),
	                           new NewAtmosphereState(rootNode), 
        					   new UIState(),
        		               new SkyAppState(),
	                           new PickAppState(),
        		               new MultipleWindowState(),
        		               new AnimState(),
        		               new MiniMapState(rootNode,1200,200),
        		               new XplaneWeatherState()
        		               );          
        // ������
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(new ColorRGBA(0.7f, 0.7f, 0.7f, 1f));
        // ����
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-1, -2, -3).normalizeLocal());
        
        rootNode.addLight(ambient);
        rootNode.addLight(sun);    
        Geometry geom=new Geometry();
   
    }

    public static void main(String[] args) {
        XplaneAirport app = new XplaneAirport();
		app.setShowSettings(false);
		AppSettings settings = new AppSettings(true);
		settings.setCustomRenderer(AwtPanelsContext.class);
		app.setSettings(settings);		
        app.start();   
    }
    

}




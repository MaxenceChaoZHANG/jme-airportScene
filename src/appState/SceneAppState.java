package appState;
import java.io.IOException;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.asset.ModelKey;
import com.jme3.asset.plugins.ZipLocator;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.KeyInput;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import DSFAnalyser.TextReader;
import dataEntity.AirportInfo;
import modelLoadUtility.XplaneSceneLoad;

/**
 * 场景管理模块
 * @author zhangchao
 *
 */
public class SceneAppState extends BaseAppState {

    private Node rootNode;
    private Node Scene;
    private Node Terrain;
    private Node Model;
    private AirportInfo airportinfo;

    private AssetManager assetManager;
    private XplaneSceneLoad xplaneSceneload;
    

    @Override
    protected void initialize(Application app) {
    	
        this.assetManager = app.getAssetManager();
        this.rootNode = ((SimpleApplication) getApplication()).getRootNode();
        this.Scene=(Node) ((SimpleApplication) getApplication()).getRootNode().getChild("Scene");
        this.Terrain=(Node) ((SimpleApplication) getApplication()).getRootNode().getChild("Terrain");
        this.Model=(Node) ((SimpleApplication) getApplication()).getRootNode().getChild("Model");
        this.xplaneSceneload=new XplaneSceneLoad(Model,Terrain,assetManager);
    }
    
    public void sceneLoad(AirportInfo airportinfo) {

    	this.airportinfo=airportinfo;
		getApplication().enqueue(new Runnable() {
			public void run() {
		      xplaneSceneload.loadScene(airportinfo);
			  }
		});
    }
    

    public void changScene(AirportInfo airportinfo) {

    	this.airportinfo=airportinfo;
    	Terrain.detachAllChildren();
    	Model.detachAllChildren();
    	sceneLoad(airportinfo);
    }

    @Override
    public void update(float tpf) {


    }
    
    @Override
    protected void cleanup(Application app) {
    }

    @Override
    protected void onEnable() {
        rootNode.attachChild(Scene);

    }

    @Override
    protected void onDisable() {
        Scene.removeFromParent();
    }
    
    
    public static void main(String[] args) {
    	String path="assets/ZBAA/Earth nav data/+40+110/+40+116.txt";
    	String[] temp=path.split("/");
    	System.out.println(temp[1]+"/");
		
	}

}


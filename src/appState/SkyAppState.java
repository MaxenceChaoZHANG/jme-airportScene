package appState;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.util.SkyFactory;

public class SkyAppState extends BaseAppState {
	
    private Node rootNode;
    private AssetManager assetManager;
    
    Spatial sky;

	@Override
	protected void initialize(Application app) {
		// TODO Auto-generated method stub
		this.assetManager = app.getAssetManager();
	    this.rootNode = ((SimpleApplication) getApplication()).getRootNode();

        sky = SkyFactory.createSky(assetManager, "Textures/Sky/SkySphereMap.jpg",
                SkyFactory.EnvMapType.SphereMap);
        rootNode.attachChild(sky);

	}

	@Override
	protected void cleanup(Application app) {
		// TODO Auto-generated method stub
       
	}

	@Override
	protected void onEnable() {
		// TODO Auto-generated method stub
		rootNode.attachChild(sky);
	}

	@Override
	protected void onDisable() {
		// TODO Auto-generated method stub
		sky.removeFromParent();
	}

}

package appState;
import java.awt.EventQueue;

import javax.swing.JFrame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.app.state.BaseAppState;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.FrameBuffer;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;

import ui.SettingFrame;

public class UIState extends BaseAppState {

   private JFrame settingFrame;
   private AppStateManager stateManager;
    /**
     * 创建用户界面
     * @param mapRoot  地图场景
     */
    public UIState() {
    	

    }
    
    @Override
    protected void initialize(Application app) {
    	
       this.stateManager=app.getStateManager();
       EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					settingFrame=new SettingFrame(stateManager);
					settingFrame.setLocation(1400,100);
					settingFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
       
       
        
    }


    @Override
    protected void cleanup(Application app) {
        
    }

    @Override
    protected void onEnable() {
       
    }

    @Override
    protected void onDisable() {
        
    }

   
    @Override
    public void update(float tpf) {


    }

   

}

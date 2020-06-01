package jmeDemo.Test;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Spline.SplineType;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;


public class TestMotionPath{
    
	private AssetManager assetManager;
	private Node rootNode;
	private InputManager inputManager;
	
    private Spatial aircraft;
    private boolean active = true;
    private boolean playing = false;
    private MotionPath path;
    private MotionEvent motionControl;

    public TestMotionPath(Application app) {
        this.assetManager = app.getAssetManager();
        this.rootNode = ((SimpleApplication) app).getRootNode();
        this.inputManager=app.getInputManager();
    	
    }

    public void createMotion() {

        path = new MotionPath();
        
        path.addWayPoint(new Vector3f(10, 3, 0));
        path.addWayPoint(new Vector3f(10, 3, 10));
        path.addWayPoint(new Vector3f(-40, 3, 10));
        path.addWayPoint(new Vector3f(-40, 3, 0));
        path.addWayPoint(new Vector3f(-40, 8, 0));
        path.addWayPoint(new Vector3f(10, 8, 0));
        path.addWayPoint(new Vector3f(10, 8, 10));
        path.addWayPoint(new Vector3f(15, 8, 10));
        path.enableDebugShape(assetManager, rootNode);
      

        motionControl = new MotionEvent(aircraft,path);
        motionControl.setDirectionType(MotionEvent.Direction.PathAndRotation);
        motionControl.setRotation(new Quaternion().fromAngleNormalAxis(-FastMath.HALF_PI, Vector3f.UNIT_Y));
        motionControl.setInitialDuration(10f);
        motionControl.setSpeed(2f);     
        
        initInputs();
    }

    private void initInputs() {
        inputManager.addMapping("display_hidePath", new KeyTrigger(KeyInput.KEY_P));
        inputManager.addMapping("SwitchPathInterpolation", new KeyTrigger(KeyInput.KEY_I));
        inputManager.addMapping("tensionUp", new KeyTrigger(KeyInput.KEY_U));
        inputManager.addMapping("tensionDown", new KeyTrigger(KeyInput.KEY_J));
        inputManager.addMapping("play_stop", new KeyTrigger(KeyInput.KEY_SPACE));
        ActionListener acl = new ActionListener() {

            public void onAction(String name, boolean keyPressed, float tpf) {
                if (name.equals("display_hidePath") && keyPressed) {
                    if (active) {
                        active = false;
                        path.disableDebugShape();
                    } else {
                        active = true;
                        path.enableDebugShape(assetManager, rootNode);
                    }
                }
                if (name.equals("play_stop") && keyPressed) {
                    if (playing) {
                        playing = false;
                        motionControl.stop();
                    } else {
                        playing = true;
                        motionControl.play();
                    }
                }

                if (name.equals("SwitchPathInterpolation") && keyPressed) {
                    if (path.getPathSplineType() == SplineType.CatmullRom){
                        path.setPathSplineType(SplineType.Linear);
                    } else {
                        path.setPathSplineType(SplineType.CatmullRom);
                    }
                }

                if (name.equals("tensionUp") && keyPressed) {
                    path.setCurveTension(path.getCurveTension() + 0.1f);
                    System.err.println("Tension : " + path.getCurveTension());
                }
                if (name.equals("tensionDown") && keyPressed) {
                    path.setCurveTension(path.getCurveTension() - 0.1f);
                    System.err.println("Tension : " + path.getCurveTension());
                }


            }
        };

        inputManager.addListener(acl, "display_hidePath", "play_stop", "SwitchPathInterpolation", "tensionUp", "tensionDown");

    }
}

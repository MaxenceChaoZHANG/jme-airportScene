package jmeDemo;



import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;

public class TestSwing extends SimpleApplication {

	@Override
	public void simpleUpdate(float tpf) {
		// TODO Auto-generated method stub
		super.simpleUpdate(tpf);
	}

	@Override
	public void simpleInitApp() {
		// TODO Auto-generated method stub
		
		flyCam.setDragToRotate(true);

		
		//创建几何形体
		Box box =new Box(1,1,1);
		Geometry geom =new Geometry("box",box);
		//创建材质
		Material mat =new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", new ColorRGBA(1,0,0,1));
		geom.setMaterial(mat);
		//创建灯光
		DirectionalLight sun = new DirectionalLight();
		AmbientLight ambient = new AmbientLight();
		ColorRGBA lightColor = new ColorRGBA(1.0f,1.0f,1.0f,1.0f);
        sun.setColor(lightColor);
        sun.setDirection(new Vector3f(-1, -2, -3));
        ambient.setColor(lightColor.mult(0.4f));
		//加载模型
		Spatial model = assetManager.loadModel("Models/AutoGate-32m-steel.obj");
		
		
		rootNode.addLight(ambient);
		rootNode.addLight(sun);
		rootNode.attachChild(model);
		rootNode.attachChild(geom);

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		  java.awt.EventQueue.invokeLater(new Runnable() {
		      public void run() {
		    	  AppSettings settings = new AppSettings(true);
		    	  settings.setWidth(640);
		    	  settings.setHeight(480);
		    	  
		    	  TestSwing canvasApplication = new TestSwing();
		    	  canvasApplication.setSettings(settings);
		    	  canvasApplication.createCanvas(); // create canvas!
		    	  JmeCanvasContext ctx = (JmeCanvasContext) canvasApplication.getContext();
		    	  ctx.setSystemListener(canvasApplication);
		    	  Dimension dim = new Dimension(640, 480);
		    	  ctx.getCanvas().setPreferredSize(dim);
		    	  
		    	  JFrame window = new JFrame("Swing Application");
		    	  window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    	  
		    	  JPanel panel = new JPanel(new FlowLayout()); // a panel
		    	// add all your Swing components ...
		    	panel.add(new JButton("Some Swing Component"));
		    	
		    	// add the JME canvas
		    	panel.add(ctx.getCanvas());
		    	
		    	window.add(panel);
		    	window.pack();
		    	window.setVisible(true);
		    	
		    	canvasApplication.startCanvas();
		      }
		    });

	}

}

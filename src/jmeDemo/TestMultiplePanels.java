package jmeDemo;

import com.jme3.app.SimpleApplication;
import com.jme3.input.FlyByCamera;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.jme3.system.awt.AwtPanel;
import com.jme3.system.awt.AwtPanelsContext;
import com.jme3.system.awt.PaintMode;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class TestMultiplePanels extends SimpleApplication {
	private static TestMultiplePanels app;
	private static AwtPanel panel1, panel2;
	private static int panelsClosed = 0;
	private static AwtPanelsContext ctx;

	private static void createWindowForPanel(final AwtPanel panel, final int location)
	{
			JFrame frame = new JFrame("Render Display " + location);
			frame.getContentPane().setLayout(new BorderLayout());
			frame.getContentPane().add(panel, BorderLayout.CENTER);
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			
			frame.addWindowListener(new WindowAdapter() {
			
			    @Override
			    public void windowClosed(WindowEvent e) {
			        if (++panelsClosed == 2){
			        app.stop();
			        }
			    }
			
			    @Override
			    public void windowActivated(WindowEvent e) {
			    	//设置当前panel为输入源
			        ctx.setInputSource(panel);
			        //当前panel请求获取焦点
			        panel.requestFocusInWindow();            
			    }
			    
			});
			
			frame.pack();
			//设置窗口位置（左上--x，y）
			frame.setLocation(location, 100);
			frame.setVisible(true);
	}

	public static void main(String[] args){
	
		app = new TestMultiplePanels();
		app.setShowSettings(false);
		AppSettings settings = new AppSettings(true);
		settings.setCustomRenderer(AwtPanelsContext.class);
		settings.setFrameRate(60);
		app.setSettings(settings);
		app.start();
		ctx = (AwtPanelsContext) app.getContext();
		SwingUtilities.invokeLater(new Runnable(){
		@Override
		public void run()
		{
		panel1 = ctx.createPanel(PaintMode.Accelerated);
		panel1.setPreferredSize(new Dimension(400, 300));
		
		        panel2 = ctx.createPanel(PaintMode.Accelerated);
		        panel2.setPreferredSize(new Dimension(400, 300));
		
		        createWindowForPanel(panel1, 100);
		        createWindowForPanel(panel2, 500);
		    }
		});
	}

	@Override
	public void simpleInitApp()
	{
		Node rootNodeView1 = new Node("rootNode View 1");
		Node rootNodeView2 = new Node("rootNode View 2");
		rootNode.attachChild(rootNodeView1);
		rootNode.attachChild(rootNodeView2);
		
		flyCam.setEnabled(false);
		
		Box b = new Box(1, 1, 1);
		Geometry geom1 = new Geometry("Box1", b);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", ColorRGBA.Blue);
		geom1.setMaterial(mat);
		rootNodeView1.attachChild(geom1);
		
		Box b2 = new Box( 1, 1, 1);
		Geometry geom2 = new Geometry("Box2", b2);
		Material mat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat2.setColor("Color", ColorRGBA.Red);
		geom2.setMaterial(mat2);
		geom2.setLocalTranslation(0.2f, 0.2f, 0.2f);
		rootNodeView2.attachChild(geom2);
		
		Camera cam1 = new Camera(cam.getWidth(), cam.getHeight());
		cam1.copyFrom(cam);
		final FlyByCamera flyCam1 = new FlyByCamera(cam1);
		flyCam1.setMoveSpeed(5);
		//注册输入


		flyCam1.setDragToRotate(true);
		flyCam1.setEnabled(false);   
		//摄像机窗口，宽度，高度
		Camera cam2 = new Camera(cam.getWidth(), cam.getHeight());
		cam2.copyFrom(cam);  
		final FlyByCamera flyCam2 = new FlyByCamera(cam2);
		flyCam2.setMoveSpeed(5);
		//注册输入
		flyCam2.registerWithInput(inputManager);
		flyCam2.setDragToRotate(true);
		flyCam2.setEnabled(true);    
		
		ViewPort vp1 = renderManager.createMainView("view1", cam1);
		vp1.setClearFlags(true, true, true);
		vp1.attachScene(rootNodeView1);
		//是否为主视图
		panel1.attachTo(false, vp1);
		
		ViewPort vp2 = renderManager.createMainView("view2", cam2);
//		vp2.setClearFlags(true, true, true);
		vp2.attachScene(rootNodeView2);
		panel2.attachTo(true, vp2);
		
		panel1.addFocusListener(new AwtPanelFocus(panel1, flyCam1));
		panel2.addFocusListener(new AwtPanelFocus(panel2, flyCam2));
		
		rootNodeView1.updateLogicalState(speed);
		rootNodeView1.updateGeometricState();
		rootNodeView2.updateLogicalState(speed);
		rootNodeView2.updateGeometricState();
		}
	}

	class AwtPanelFocus implements FocusListener
	{
		AwtPanel panel;
		FlyByCamera flyCam;
	
		public AwtPanelFocus(AwtPanel panel, FlyByCamera flyCam)
		{
		    this.panel = panel;
		    this.flyCam = flyCam;
		}
		
		@Override
		public void focusGained(FocusEvent e)
		{
		    flyCam.setEnabled(true);
		}
		
		@Override
		public void focusLost(FocusEvent e) 
		{
		    flyCam.setEnabled(false);
		}
	}
	
	
	
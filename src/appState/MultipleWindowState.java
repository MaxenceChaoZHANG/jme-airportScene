package appState;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.system.awt.AwtPanel;
import com.jme3.system.awt.AwtPanelsContext;
import com.jme3.system.awt.PaintMode;


public class MultipleWindowState extends BaseAppState {
	
	
	final private static CountDownLatch panelsAreReady = new CountDownLatch(1);
	private static AwtPanel leftPanel, mainPanel,rightPanel;
	private static JFrame leftJFrame,mainJFrame,rightJFrame;
	private static AwtPanelsContext ctx;
	
	private InputManager inputManager;
	private RenderManager renderManager;
	private Camera cam,leftCam,rightCam;
	private Camera currentCam;
	private FlyByCamera flyCam,leftFlyCam,rightFlyCam;
	private ViewPort viewPort;
	private ViewPort guiViewPort;
	private ViewPort leftViewPort,rightViewPort;
	private Node rootNode;
	private Map<String,Camera> camMap = new HashMap<String,Camera>();	
	private Map<String,FlyByCamera> flyCamMap = new HashMap<String,FlyByCamera>();
	
	
	public  Camera getCurrentCam() {
		return currentCam;
	}

    public MultipleWindowState() {

    }

    @Override
    protected void initialize(Application app) {
    	
    	
    	this.cam=app.getCamera();
    	this.flyCam=((SimpleApplication)app).getFlyByCamera();
    	this.viewPort=app.getViewPort();
    	this.guiViewPort=app.getGuiViewPort();
        this.rootNode=((SimpleApplication)app).getRootNode();
        this.ctx = (AwtPanelsContext) ((SimpleApplication)app).getContext();        
        this.inputManager=app.getInputManager();
    	this.renderManager=app.getRenderManager();
    	
		SwingUtilities.invokeLater(new Runnable(){
		    @Override
			public void run()
			{	
			/* 
			 * Sleep 2 seconds to ensure there's no race condition.
	         * The sleep is not required for correctness.
	         */
	        try {
	            Thread.sleep(2000);
	        } catch (InterruptedException exception) {
	            return;
	        }
	        
				leftPanel = ctx.createPanel(PaintMode.Accelerated);
				leftPanel.setName("Left");
				leftPanel.setPreferredSize(new Dimension(800, 600));
		
				mainPanel = ctx.createPanel(PaintMode.Accelerated);
				mainPanel.setName("Main");
		        mainPanel.setPreferredSize(new Dimension(800, 600));
		        
		        rightPanel = ctx.createPanel(PaintMode.Accelerated);
		        rightPanel.setPreferredSize(new Dimension(800, 600));
		        rightPanel.setName("Right");
		        
		        ctx.setInputSource(leftPanel);
		
		        leftJFrame=createWindowForPanel(leftPanel, 0);
		        mainJFrame=createWindowForPanel(mainPanel, 300);
		        rightJFrame=createWindowForPanel(rightPanel, 600);   
		        /*
                 * Both panels are ready.
                 */
                panelsAreReady.countDown();
			    }
			});
        /*
         * Wait until both AWT panels are ready.
         */	
        try {
            panelsAreReady.await();
        } catch (InterruptedException exception) {
            throw new RuntimeException("Interrupted while waiting for panels", exception);
        }
		
        //设置默认（主）相机参数
        cam.setLocation(new Vector3f(0f,15f,0f));
        cam.setFrustumPerspective(45.0f, 1, 1,1000);
        cam.setName("mainCam");
//        System.out.println(cam.getFrustumBottom());
//        System.out.println(cam.getFrustumLeft());
//        System.out.println(cam.getFrustumRight());
//        System.out.println(cam.getFrustumTop());
//        System.out.println(cam.getFrustumNear());
//        System.out.println(cam.getFrustumFar());
//        System.out.println(cam.getDirection());
        //设置默认（主）飞行相机参数
        flyCam.setDragToRotate(true);
        flyCam.setEnabled(false);
        flyCam.setMoveSpeed(50);
        flyCam.setZoomSpeed(10); 
        //设置默认视口和gui视口参数
        viewPort.setClearFlags(true, true, true); 
        viewPort.setBackgroundColor(ColorRGBA.Gray);
//        guiViewPort.setClearFlags(true, true, true);
        
        createLeftView();
        createRightView();
		//将视口添加到panel中
		leftPanel.attachTo(false, leftViewPort);
		mainPanel.attachTo(true, viewPort,guiViewPort);
		rightPanel.attachTo(false, rightViewPort);
		//添加到map中
		camMap.put(leftPanel.getName(), leftCam);
		camMap.put(mainPanel.getName(), cam);
		camMap.put(rightPanel.getName(), rightCam);
		
		flyCamMap.put(leftPanel.getName(), leftFlyCam);
		flyCamMap.put(mainPanel.getName(), flyCam);
		flyCamMap.put(rightPanel.getName(), rightFlyCam);
		 
//		leftPanel.addFocusListener(new AwtPanelFocus(leftPanel, leftFlyCam));
//		mainPanel.addFocusListener(new AwtPanelFocus(mainPanel, flyCam));
//		rightPanel.addFocusListener(new AwtPanelFocus(rightPanel, rightFlyCam));
    }
    private void createLeftView() {
		//创建左相机
        leftCam =cam.clone();
        leftCam.setName("leftCam");
		leftFlyCam = new FlyByCamera(leftCam);
		leftFlyCam.setMoveSpeed(50);
		leftFlyCam.registerWithInput(inputManager);
		leftFlyCam.setDragToRotate(true);
		leftFlyCam.setEnabled(false);   		
		//根据第二相机擦行间第二视口
		leftViewPort = renderManager.createMainView("left", leftCam);
		leftViewPort.setClearFlags(true, true, true);
		leftViewPort.attachScene(rootNode);
		leftViewPort.setBackgroundColor(ColorRGBA.Gray);
    	
    }
    
    private void createRightView() {
		//创建右相机
        rightCam =cam.clone();
        rightCam.setName("rightCam");
		rightFlyCam = new FlyByCamera(rightCam);
		rightFlyCam.setMoveSpeed(50);
		rightFlyCam.registerWithInput(inputManager);
		rightFlyCam.setDragToRotate(true);
		rightFlyCam.setEnabled(false);   		
		//根据第二相机擦行间第二视口
		rightViewPort = renderManager.createMainView("right", rightCam);
		rightViewPort.setClearFlags(true, true, true);
		rightViewPort.attachScene(rootNode);
		rightViewPort.setBackgroundColor(ColorRGBA.Gray);
    	
    }

    @Override
    protected void cleanup(Application app) {
    	camMap.clear();
        getApplication().getRenderManager().removeMainView(leftViewPort);
        getApplication().getRenderManager().removeMainView(rightViewPort);
    }

    @Override
    protected void onEnable() {
        
    }

    @Override
    protected void onDisable() {
    	getApplication().getRenderManager().removeMainView(leftViewPort);
        getApplication().getRenderManager().removeMainView(rightViewPort);
    }
    
     public void leftWindowEnable(boolean flag) {
    	if(flag){
            leftJFrame.setVisible(true);
    	}else {
    		leftJFrame.setVisible(false);
    	}    	
    }
    
     public void rightWindowEnable(boolean flag) {
    	if(flag){
    		rightJFrame.setVisible(true);
    	}else {
    		rightJFrame.setVisible(false);
    	}	
    }
    
    
    private final Quaternion camRot = new Quaternion();
    private Vector3f camLocation = new Vector3f();
    private final float[] angles = new float[3]; 
    @Override
    public void update(float tpf) {
   
    	if(currentCam!=null) {
	        currentCam.getRotation().toAngles(angles);
	        camLocation=currentCam.getLocation();
	        if(currentCam.getName()=="leftCam") {        	
	        	camRot.fromAngles(angles[0], angles[1]-FastMath.PI/4, angles[2]);
	            cam.setRotation(camRot);
	            cam.setLocation(camLocation);  
	            
	            camRot.fromAngles(angles[0], angles[1]-FastMath.HALF_PI, angles[2]);
	            rightCam.setRotation(camRot);
	            rightCam.setLocation(camLocation);  
	
	        }
	        if(currentCam.getName()=="mainCam") {        	
	        	camRot.fromAngles(angles[0], angles[1]+FastMath.PI/4, angles[2]);
	            leftCam.setRotation(camRot);
	            leftCam.setLocation(camLocation);  
	            
	            camRot.fromAngles(angles[0], angles[1]-FastMath.PI/4, angles[2]);
	            rightCam.setRotation(camRot);
	            rightCam.setLocation(camLocation);   
	
	        }
	        if(currentCam.getName()=="rightCam") {        	
	        	camRot.fromAngles(angles[0], angles[1]+FastMath.PI/4, angles[2]);
	            cam.setRotation(camRot);
	            cam.setLocation(camLocation); 
	            
	            camRot.fromAngles(angles[0], angles[1]+FastMath.HALF_PI, angles[2]);
	            leftCam.setRotation(camRot);
	            leftCam.setLocation(camLocation);  
	        }
    	}
      
    }
    
    private  JFrame createWindowForPanel(final AwtPanel panel, final int location)
	{
			JFrame frame = new JFrame(panel.getName());
			frame.setName(panel.getName());
			frame.getContentPane().setLayout(new BorderLayout());
			frame.getContentPane().add(panel, BorderLayout.CENTER);
			frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			
			frame.addWindowListener(new WindowAdapter() {			
			    @Override
			    public void windowClosing(WindowEvent e) {
			    	
			        if (e.getWindow().getName()=="Left"){
			        	leftWindowEnable(false);
			        }
			        if (e.getWindow().getName()=="Right"){
			        	rightWindowEnable(false);
				        }
			        if (e.getWindow().getName()=="Main"){
				         getApplication().stop();
				         System.exit(0);
				        }
			    }	
			    @Override
			    public void windowActivated(WindowEvent e) {
			    	//设置当前panel为输入源
			        ctx.setInputSource(panel);
			        //当前panel请求获取焦点
//			        panel.requestFocusInWindow();  
			        currentCam=camMap.get(panel.getName());
			        
			        if(flyCamMap.get(panel.getName())!=null)
			        {
			        	flyCamMap.get(panel.getName()).setEnabled(true);
			        }        		
//			        System.out.println(currentCamName);
			    }
			    
			    @Override
			    public void windowDeactivated(WindowEvent e) {
			    	if(flyCamMap.get(panel.getName())!=null)
		        	{
		        	 flyCamMap.get(panel.getName()).setEnabled(false);
		        	}
			    }
			    
			});			
			frame.pack();
			//设置窗口位置（左上--x，y）
			frame.setLocation(location, 100);
			frame.setVisible(true);
			return frame;
	}


}

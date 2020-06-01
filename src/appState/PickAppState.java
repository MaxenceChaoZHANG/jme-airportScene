package appState;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.RawInputListener;
import com.jme3.input.event.JoyAxisEvent;
import com.jme3.input.event.JoyButtonEvent;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.input.event.TouchEvent;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.shape.Sphere;

public class PickAppState extends BaseAppState {
	private Node shootables;
	private Node rootNode;
	private Geometry mark;
	private InputManager inputManager;
	private AssetManager assetManager;
	private Geometry PICKED;
	private boolean OfforOn=true;
	//拾取对象的移动速度和旋转速度
	private float moveSpeed=1.0f;
	private float rotationSpeed=0.1f;

	public float getMoveSpeed() {
		return moveSpeed;
	}

	public void setMoveSpeed(float moveSpeed) {
		this.moveSpeed = moveSpeed;
	}

	public float getRotationSpeed() {
		return rotationSpeed;
	}

	public void setRotationSpeed(float rotationSpeed) {
		this.rotationSpeed = rotationSpeed;
	}

	@Override
	protected void initialize(Application app) {
		// TODO Auto-generated method stub
		this.rootNode=((SimpleApplication) app).getRootNode();
		this.shootables =(Node) ((SimpleApplication) app).getRootNode().getChild("Model");
		this.inputManager=app.getInputManager();
		this.assetManager=app.getAssetManager();

		initKeys();       // load custom key mappings
		initMark();       // a red sphere to mark the hit

	}
	
	  /** Declaring the "Shoot" action and mapping to its triggers. */
	  private void initKeys() {
		  //物体控制触发器--方向键
		  inputManager.addRawInputListener(rawInputListener);
		  //建立坐标系xyz
//		  createArrow(new Vector3f(40, 0, 0), ColorRGBA.Green);
//	      createArrow(new Vector3f(0, 40, 0), ColorRGBA.Red);
//	      createArrow(new Vector3f(0, 0, 40), ColorRGBA.Blue);
	  }
	  
	  private void createArrow(Vector3f vec3, ColorRGBA color) {
	        // 创建材质，设定箭头的颜色
	        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
	        mat.setColor("Color", color);
	        // 创建几何物体，应用箭头网格。
	        Geometry geom = new Geometry("arrow", new Arrow(vec3));
	        geom.setMaterial(mat);
	        geom.setLocalTranslation(0.0f, 2.0f, 0.0f);
	        // 添加到场景中
	        rootNode.attachChild(geom);
	    }
	  
	  /** 
	   * 创建红色小球用于拾取标识
	   */
	protected void initMark() {
	    Sphere sphere = new Sphere(30, 30, 2f);
	    mark = new Geometry("BOOM!", sphere);
	    Material mark_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
	    mark_mat.setColor("Color", ColorRGBA.Red);
	    mark.setMaterial(mark_mat);
	 }
	
	/** 
	 * 原始输入监听器
	 */
	 private RawInputListener rawInputListener=new RawInputListener()  {
	  	/**键盘输入事件*/
	  	@Override
	  	public void onKeyEvent(KeyInputEvent evt) {
	  		int keyCode = evt.getKeyCode();
	  		boolean isPressed = evt.isPressed();
	  		
	  		if (isPressed) {
	  			switch (keyCode) {
	  			case KeyInput.KEY_UP: {
	  				moveGeometry(true,1.0f);
	  				break;
	  			}
	  			case KeyInput.KEY_DOWN:{
	  				moveGeometry(true,-1.0f);
	  				break;
	  			}
	  			case KeyInput.KEY_LEFT:{
	  				moveGeometry(false,-1.0f);
	  				break;
	  			}
	  			case KeyInput.KEY_RIGHT:{
	  				moveGeometry(false,1.0f);
	  				break;
	  			}
	  			case KeyInput.KEY_B:{
	  				rotateGeometry(1.0f);
	  				break;
	  			}
	  			case KeyInput.KEY_N:{
	  				rotateGeometry(-1.0f);
	  				break;
	  			}
	  			case KeyInput.KEY_P:{
	  				if(getApplication().getStateManager().getState(AnimState.class)!=null)
	  					getApplication().getStateManager().getState(AnimState.class).playAnim();
	  				break;
	  			}
	  			}
	  		}
	  	}
	  	/**鼠标输入事件*/
	  	@Override
		public void onMouseButtonEvent(MouseButtonEvent evt) {
	  	//1=鼠标右键，0=鼠标左键，2等于鼠标中键
	  		int keyCode = evt.getButtonIndex();
	  		boolean isPressed = evt.isPressed();
	  		if(isPressed&&keyCode==1)
	  		 { pick();}
		}
	  	/** 其他输入事件*/
	  	@Override
	  	public void onMouseMotionEvent(MouseMotionEvent evt) {}
		@Override
		public void beginInput() {}
		@Override
		public void endInput() {}
		@Override
		public void onJoyAxisEvent(JoyAxisEvent arg0) {}
		@Override
		public void onJoyButtonEvent(JoyButtonEvent arg0) {}
		@Override
		public void onTouchEvent(TouchEvent arg0) {}
	  };
	  
	   /**
	     * 左右或前后平移物体
	     * @param sideways true：前后  false：左右
	     */
	    protected void moveGeometry(boolean sideways,float value){
	    	if(!OfforOn)
	    		return;
	    	if(PICKED==null) {
		  		System.out.println("提示：没有物体被选中，操作无效。请右键选择物体。");
	    	}else {
	        Vector3f vel = new Vector3f(1.0f,0.0f,0.0f);
	        Vector3f pos = PICKED.getLocalTranslation().clone();
	        if (sideways){
	        	vel=new Vector3f(0.0f,0.0f,-1.0f);
	        }
	        vel.multLocal( moveSpeed*value);
	        pos.addLocal(vel);
	        PICKED.setLocalTranslation(pos);
	    	}
	    }
	    
	    /**
	     * 将物体饶y轴旋转
	     * @param value 顺时针 逆时针
	     */
	    protected void rotateGeometry(float value){
	    	if(!OfforOn)
	    		return;
	    	if(PICKED==null) {
		  		System.out.println("提示：没有物体被选中，操作无效。请右键选择物体。");
	    	}else {
	        Quaternion q1 =PICKED.getLocalRotation();
	        float[] angles =new float[3];
	        q1.toAngles(angles);

	        angles[1]=angles[1]+rotationSpeed * value;
	        q1.fromAngles(angles[0],angles[1],angles[2]);

	        PICKED.setLocalRotation(q1);
	    	}
	    }
	    
	  
	  	/**
	  	 * 鼠标左键触发拾取操作处理函数
	  	 */
	    public void pick() {
	    	if(!OfforOn)
	    		return;
	        // 1. 定义碰撞检测结果集 
	        CollisionResults results = new CollisionResults();
	        // 2.获取鼠标坐标，并转换为3d空间坐标
	        Vector2f screenCoord = inputManager.getCursorPosition();
//	        System.out.println("鼠标坐标：" +screenCoord.x+" " +screenCoord.y);	        
	        Camera camtemp;
	        MultipleWindowState multipleWindowState=getApplication().getStateManager().getState(MultipleWindowState.class);
	        if(multipleWindowState!=null) {
	        	camtemp=multipleWindowState.getCurrentCam();
	        }else {
	        	camtemp=getApplication().getCamera();
	        }
	        System.out.println();
            Vector3f worldCoord = camtemp.getWorldCoordinates(screenCoord, 1f);
            // 计算射线方向
            Vector3f dir = worldCoord.subtract(camtemp.getLocation());
            dir.normalizeLocal();
	        Ray ray = new Ray(camtemp.getLocation(), dir);
	        // 3. 与shootables碰撞加测
	        shootables.collideWith(ray, results);
	        // 4. 打印所有结果
	        System.out.println("----- 碰撞检测结果： " + results.size() + "个拾取结果-----");
//	        for (int i = 0; i < results.size(); i++) {
//	          // For each hit, we know distance, impact point, name of geometry.
//	          float dist = results.getCollision(i).getDistance();
//	          Vector3f pt = results.getCollision(i).getContactPoint();
//	          String hit = results.getCollision(i).getGeometry().getName();
//	          System.out.println("* 拾取结果 #" + i);
//	          System.out.println("对象：" + hit + " 位置点 ：" + pt + " 距离：" + dist );
//	        }
	        // 5. 使用最佳的结果
	        if (results.size() > 0) {
	          // 获取最近的结果，即最佳结果
	          CollisionResult closest = results.getClosestCollision();
	          // 将红色小球标识移动到该位置
	          mark.setLocalTranslation(closest.getContactPoint());
	          float dist = closest.getDistance();
	          Vector3f pt = closest.getContactPoint();
	          String hit = closest.getGeometry().getName();
	          //讲对象引用赋值给PICKED
	          PICKED=closest.getGeometry();
	          System.out.println("# 最佳结果 #");
	          System.out.println("对象：" + hit );
	          System.out.println("位置点 ：" + pt);
	          System.out.println("距离：" + dist);
	          rootNode.attachChild(mark);
	        } else {
	          // 无拾取结果，则将红色标志（红色小球）移除
	          rootNode.detachChild(mark);
	          PICKED=null;
	        }
	      }

	@Override
	protected void onDisable() {
		inputManager.removeRawInputListener(rawInputListener);
		PICKED=null;
		mark.removeFromParent();
		OfforOn=false;
	}

	@Override
	protected void onEnable() {
		inputManager.addRawInputListener(rawInputListener);
		OfforOn=true;
	}
	
	@Override
	protected void cleanup(Application arg0) {
		// TODO Auto-generated method stub
		inputManager.removeRawInputListener(rawInputListener);
		PICKED=null;
		mark.removeFromParent();
	}

}

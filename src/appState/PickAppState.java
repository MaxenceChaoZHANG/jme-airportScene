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
	//ʰȡ������ƶ��ٶȺ���ת�ٶ�
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
		  //������ƴ�����--�����
		  inputManager.addRawInputListener(rawInputListener);
		  //��������ϵxyz
//		  createArrow(new Vector3f(40, 0, 0), ColorRGBA.Green);
//	      createArrow(new Vector3f(0, 40, 0), ColorRGBA.Red);
//	      createArrow(new Vector3f(0, 0, 40), ColorRGBA.Blue);
	  }
	  
	  private void createArrow(Vector3f vec3, ColorRGBA color) {
	        // �������ʣ��趨��ͷ����ɫ
	        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
	        mat.setColor("Color", color);
	        // �����������壬Ӧ�ü�ͷ����
	        Geometry geom = new Geometry("arrow", new Arrow(vec3));
	        geom.setMaterial(mat);
	        geom.setLocalTranslation(0.0f, 2.0f, 0.0f);
	        // ��ӵ�������
	        rootNode.attachChild(geom);
	    }
	  
	  /** 
	   * ������ɫС������ʰȡ��ʶ
	   */
	protected void initMark() {
	    Sphere sphere = new Sphere(30, 30, 2f);
	    mark = new Geometry("BOOM!", sphere);
	    Material mark_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
	    mark_mat.setColor("Color", ColorRGBA.Red);
	    mark.setMaterial(mark_mat);
	 }
	
	/** 
	 * ԭʼ���������
	 */
	 private RawInputListener rawInputListener=new RawInputListener()  {
	  	/**���������¼�*/
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
	  	/**��������¼�*/
	  	@Override
		public void onMouseButtonEvent(MouseButtonEvent evt) {
	  	//1=����Ҽ���0=��������2��������м�
	  		int keyCode = evt.getButtonIndex();
	  		boolean isPressed = evt.isPressed();
	  		if(isPressed&&keyCode==1)
	  		 { pick();}
		}
	  	/** ���������¼�*/
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
	     * ���һ�ǰ��ƽ������
	     * @param sideways true��ǰ��  false������
	     */
	    protected void moveGeometry(boolean sideways,float value){
	    	if(!OfforOn)
	    		return;
	    	if(PICKED==null) {
		  		System.out.println("��ʾ��û�����屻ѡ�У�������Ч�����Ҽ�ѡ�����塣");
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
	     * ��������y����ת
	     * @param value ˳ʱ�� ��ʱ��
	     */
	    protected void rotateGeometry(float value){
	    	if(!OfforOn)
	    		return;
	    	if(PICKED==null) {
		  		System.out.println("��ʾ��û�����屻ѡ�У�������Ч�����Ҽ�ѡ�����塣");
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
	  	 * ����������ʰȡ����������
	  	 */
	    public void pick() {
	    	if(!OfforOn)
	    		return;
	        // 1. ������ײ������� 
	        CollisionResults results = new CollisionResults();
	        // 2.��ȡ������꣬��ת��Ϊ3d�ռ�����
	        Vector2f screenCoord = inputManager.getCursorPosition();
//	        System.out.println("������꣺" +screenCoord.x+" " +screenCoord.y);	        
	        Camera camtemp;
	        MultipleWindowState multipleWindowState=getApplication().getStateManager().getState(MultipleWindowState.class);
	        if(multipleWindowState!=null) {
	        	camtemp=multipleWindowState.getCurrentCam();
	        }else {
	        	camtemp=getApplication().getCamera();
	        }
	        System.out.println();
            Vector3f worldCoord = camtemp.getWorldCoordinates(screenCoord, 1f);
            // �������߷���
            Vector3f dir = worldCoord.subtract(camtemp.getLocation());
            dir.normalizeLocal();
	        Ray ray = new Ray(camtemp.getLocation(), dir);
	        // 3. ��shootables��ײ�Ӳ�
	        shootables.collideWith(ray, results);
	        // 4. ��ӡ���н��
	        System.out.println("----- ��ײ������� " + results.size() + "��ʰȡ���-----");
//	        for (int i = 0; i < results.size(); i++) {
//	          // For each hit, we know distance, impact point, name of geometry.
//	          float dist = results.getCollision(i).getDistance();
//	          Vector3f pt = results.getCollision(i).getContactPoint();
//	          String hit = results.getCollision(i).getGeometry().getName();
//	          System.out.println("* ʰȡ��� #" + i);
//	          System.out.println("����" + hit + " λ�õ� ��" + pt + " ���룺" + dist );
//	        }
	        // 5. ʹ����ѵĽ��
	        if (results.size() > 0) {
	          // ��ȡ����Ľ��������ѽ��
	          CollisionResult closest = results.getClosestCollision();
	          // ����ɫС���ʶ�ƶ�����λ��
	          mark.setLocalTranslation(closest.getContactPoint());
	          float dist = closest.getDistance();
	          Vector3f pt = closest.getContactPoint();
	          String hit = closest.getGeometry().getName();
	          //���������ø�ֵ��PICKED
	          PICKED=closest.getGeometry();
	          System.out.println("# ��ѽ�� #");
	          System.out.println("����" + hit );
	          System.out.println("λ�õ� ��" + pt);
	          System.out.println("���룺" + dist);
	          rootNode.attachChild(mark);
	        } else {
	          // ��ʰȡ������򽫺�ɫ��־����ɫС���Ƴ�
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

package jmeDemo.Hello;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.ZipLocator;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 * ��ʾ��ɫ�ڵ�ͼ���������ߡ�
 * 
 * @author yanmaoyuan
 *
 */
public class HelloCollision extends SimpleApplication implements ActionListener {
    /**
     * ��ʾ������BulletAppState��debug��״����F1��������
     */
    public final static String DEBUG = "debug";

    // ǰ�������ҡ���Ծ
    public final static String FORWARD = "forward";
    public final static String BACKWARD = "backward";
    public final static String LEFT = "left";
    public final static String RIGHT = "right";
    public final static String JUMP = "jump";

    private BulletAppState bulletAppState;

    // ��ɫ������
    private CharacterControl player;
    private Vector3f walkDirection = new Vector3f();
    private boolean left = false, right = false, up = false, down = false;

    // ��ʱ���������ڱ���������ķ��򡣱�����simpleUpdate���ظ���������
    private Vector3f camDir = new Vector3f();
    private Vector3f camLeft = new Vector3f();

    @Override
    public void simpleInitApp() {
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);

        // ��ʼ������
        initKeys();

        // ��ʼ������
        initLight();

        // ��ʼ������
        initScene();

        // ��ʼ�����
        initPlayer();
    }

    @Override
    public void simpleUpdate(float tpf) {
        camDir.set(cam.getDirection()).multLocal(0.6f);
        camLeft.set(cam.getLeft()).multLocal(0.4f);
        walkDirection.set(0, 0, 0);

        // �����˶�����
        boolean changed = false;
        if (left) {
            walkDirection.addLocal(camLeft);
            changed = true;
        }
        if (right) {
            walkDirection.addLocal(camLeft.negate());
            changed = true;
        }
        if (up) {
            walkDirection.addLocal(camDir);
            changed = true;
        }
        if (down) {
            walkDirection.addLocal(camDir.negate());
            changed = true;
        }
        if (changed) {
            walkDirection.y = 0;// �������ٶȵķ���������ˮƽ���ϡ�
            walkDirection.normalizeLocal();// ��λ��
            walkDirection.multLocal(0.5f);// �ı�����
        }

        player.setWalkDirection(walkDirection);
        cam.setLocation(player.getPhysicsLocation());
    }

    /**
     * ��ʼ����������
     */
    private void initKeys() {
        inputManager.addMapping(DEBUG, new KeyTrigger(KeyInput.KEY_F1));
        inputManager.addMapping(LEFT, new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping(RIGHT, new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping(FORWARD, new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping(BACKWARD, new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping(JUMP, new KeyTrigger(KeyInput.KEY_SPACE));

        inputManager.addListener(this, DEBUG, LEFT, RIGHT, FORWARD, BACKWARD, JUMP);
    }

    public void onAction(String name, boolean isPressed, float tpf) {
        if (DEBUG.equals(name) && isPressed) {
            boolean debugEnabled = bulletAppState.isDebugEnabled();
            bulletAppState.setDebugEnabled(!debugEnabled);
        } else if (LEFT.equals(name)) {
            left = isPressed;
        } else if (RIGHT.equals(name)) {
            right = isPressed;
        } else if (FORWARD.equals(name)) {
            up = isPressed;
        } else if (BACKWARD.equals(name)) {
            down = isPressed;
        } else if (JUMP.equals(name) && isPressed) {
            player.jump(new Vector3f(0.0f,2.0f,0.0f));

        }
    }

    /**
     * ��ʼ������
     */
    private void initLight() {
        // ������
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(new ColorRGBA(0.3f, 0.3f, 0.3f, 1f));

        // ����
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-1, -2, -3).normalizeLocal());

        rootNode.addLight(ambient);
        rootNode.addLight(sun);
    }

    /**
     * ��ʼ������
     */
    private void initScene() {
        // ��zip�ļ��м��ص�ͼ����
        assetManager.registerLocator("town.zip", ZipLocator.class);
        Spatial sceneModel = assetManager.loadModel("main.scene");

        // Ϊ��ͼ������ȷ������״
        CollisionShape sceneShape = CollisionShapeFactory.createMeshShape(sceneModel);
        RigidBodyControl landscape = new RigidBodyControl(sceneShape, 0);
        sceneModel.addControl(landscape);

        rootNode.attachChild(sceneModel);
        bulletAppState.getPhysicsSpace().add(landscape);
    }

    /**
     * ��ʼ�����
     */

	private void initPlayer() {
        // ʹ�ý�������Ϊ��ҵ���ײ��״
        CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(0.3f, 1.8f, 1);
        float stepHeight = 0.5f;// �����ֱ�ӵ��϶�ߵ�̨�ף�

        // ʹ��CharacterControl�������������
        player = new CharacterControl(capsuleShape, stepHeight);
        player.setJumpSpeed(10);// �����ٶ�
        player.setFallSpeed(55);// ׹���ٶ�
//        player.setGravity(9.8f * 3);// �������ٶ�
        player.setGravity(new Vector3f(0.0f,-9.8f,0.0f));
        player.setPhysicsLocation(new Vector3f(0, 10, 0));// λ��

        bulletAppState.getPhysicsSpace().add(player);
    }

    public static void main(String[] args) {
        HelloCollision app = new HelloCollision();
        app.setShowSettings(false);
        app.start();
    }
}
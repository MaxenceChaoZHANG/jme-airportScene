package jmeDemo.Hello;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Sphere.TextureMode;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;

/**
 * ��������С����שǽ��
 * 
 * @author yanmaoyuan
 *
 */
public class HelloPhysics extends SimpleApplication implements ActionListener {

    /**
     * ���𣬷���С��������������
     */
    public final static String FIRE = "fire";
    /**
     * ��ʾ������BulletAppState��debug��״�����ո��������
     */
    public final static String DEBUG = "debug";

    /** ש��ĳߴ� */
    private static final float brickLength = 0.48f;
    private static final float brickWidth = 0.24f;
    private static final float brickHeight = 0.12f;

    private BulletAppState bulletAppState;

    @Override
    public void simpleInitApp() {
        cam.setLocation(new Vector3f(0, 4f, 6f));
        cam.lookAt(new Vector3f(2, 2, 0), Vector3f.UNIT_Y);

        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);

        // ��ʼ������
        initKeys();

        // ��ʼ������
        initLight();

        // ��ʼ������
        initScene();
    }

    public void onAction(String name, boolean isPressed, float tpf) {
        if (isPressed) {
            if (FIRE.equals(name)) {
                shootBall();
            } else if (DEBUG.equals(name)) {
                boolean debugEnabled = bulletAppState.isDebugEnabled();
                bulletAppState.setDebugEnabled(!debugEnabled);
            }
        }
    }

    /**
     * ��ʼ����������
     */
    private void initKeys() {
        inputManager.addMapping(FIRE, new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping(DEBUG, new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(this, FIRE, DEBUG);
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
        makeFloor();
        makeWall();
    }

    /**
     * �����ذ�
     */
    private void makeFloor() {
        // ����
        Box floor = new Box(10f, 0.1f, 5f);
        floor.scaleTextureCoordinates(new Vector2f(3, 6));

        // ����
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture tex = assetManager.loadTexture("Textures/Terrain/Pond/Pond.jpg");
        tex.setWrap(WrapMode.Repeat);
        mat.setTexture("ColorMap", tex);

        // ������
        Geometry geom = new Geometry("floor", floor);
        geom.setMaterial(mat);
        geom.setLocalTranslation(0, -0.1f, 0);// ���ذ�����һ�����룬�ñ����xozƽ���غϡ�

        // ����
        RigidBodyControl rigidBody = new RigidBodyControl(0);
        geom.addControl(rigidBody);
        rigidBody.setCollisionShape(new BoxCollisionShape(new Vector3f(10f, 0.1f, 5f)));

        rootNode.attachChild(geom);
        bulletAppState.getPhysicsSpace().add(rigidBody);
    }

    /**
     * ����һ��ǽ
     */
    private void makeWall() {
        // ����forѭ������һ�����ڶ�ש����ɵ�ǽ�塣
        float startpt = brickLength / 4;
        float height = 0;
        for (int j = 0; j < 15; j++) {
            for (int i = 0; i < 6; i++) {
                Vector3f vt = new Vector3f(i * brickLength * 2 + startpt, brickHeight + height, 0);
                makeBrick(vt);
            }
            startpt = -startpt;
            height += 2 * brickHeight;
        }
    }

    /**
     * ��ָ��λ�÷���һ������ש��
     * 
     * @param loc
     *            ש���λ��
     */
    private void makeBrick(Vector3f loc) {
        // ����
        Box box = new Box(brickLength, brickHeight, brickWidth);
        box.scaleTextureCoordinates(new Vector2f(1f, .5f));

        // ����
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture tex = assetManager.loadTexture("Textures/Terrain/BrickWall/BrickWall.jpg");
        mat.setTexture("ColorMap", tex);

        // ������
        Geometry geom = new Geometry("brick", box);
        geom.setMaterial(mat);
        geom.setLocalTranslation(loc);// ��ש�����ָ��λ��

        // ����
        RigidBodyControl rigidBody = new RigidBodyControl(2f);
        geom.addControl(rigidBody);
        rigidBody.setCollisionShape(new BoxCollisionShape(new Vector3f(brickLength, brickHeight, brickWidth)));

        rootNode.attachChild(geom);
        bulletAppState.getPhysicsSpace().add(rigidBody);
    }

    /**
     * �����������λ�÷���һ��С�򣬳��ٶȷ��������������һ�¡�
     */
    private void shootBall() {
        // ����
        Sphere sphere = new Sphere(32, 32, 0.4f, true, false);
        sphere.setTextureMode(TextureMode.Projected);

        // ����
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture tex = assetManager.loadTexture("Textures/Terrain/Rock/Rock.PNG");
        mat.setTexture("ColorMap", tex);

        // ������
        Geometry geom = new Geometry("cannon ball", sphere);
        geom.setMaterial(mat);

        // ����
        RigidBodyControl rigidBody = new RigidBodyControl(1f);
        geom.addControl(rigidBody);
        rigidBody.setCollisionShape(new SphereCollisionShape(0.4f));
        rigidBody.setPhysicsLocation(cam.getLocation());// λ��
        rigidBody.setLinearVelocity(cam.getDirection().mult(25));// ���ٶ�

        rootNode.attachChild(geom);
        bulletAppState.getPhysicsSpace().add(rigidBody);
    }

    public static void main(String[] args) {
        HelloPhysics app = new HelloPhysics();
        app.start();
    }
}
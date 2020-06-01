package scene;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.input.ChaseCamera;
import com.jme3.input.InputManager;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 * ��ɫ����ģ��
 * 
 * @author yanmaoyuan
 *
 */
public class CharacterAppState extends BaseAppState implements AnimEventListener {

    /**
     * ��ɫ�ĳߴ�
     */
    private float radius = 0.3f;// ���Ұ뾶0.3��
    private float height = 1.8f;// �������1.8��
    private float stepHeight = 0.5f;// ��ɫ����0.5��

    /**
     * ��ɫ���ģ��
     */
    private Node character;
    private Spatial model;// ��ɫģ��
    private Node camNode;// ����������ڵ�
    private CharacterControl player;// ��ɫ������

    /**
     * ���ڼ����ɫ���߷���ı���
     */
    private Vector3f walkDir = new Vector3f();
    private Vector3f camDir = new Vector3f();
    private Quaternion camRot = new Quaternion();

    /**
     * ����������
     */
    private AnimControl animControl;
    private AnimChannel animChannel;

    /**
     * ȫ�ֶ���
     */
    private Camera cam;
    private AssetManager assetManager;
    private InputManager inputManager;
    private AppStateManager stateManager;

    @Override
    protected void initialize(Application app) {
        this.cam = app.getCamera();
        this.assetManager = app.getAssetManager();
        this.inputManager = app.getInputManager();
        this.stateManager = app.getStateManager();

        initCharacter();// ��ɫģ��
        initPhysics();// �������
        initAnimation();// ��������
        initChaseCamera();//�����˳������

    }

    @Override
    public void update(float tpf) {

        if (walkDir.lengthSquared() != 0) {
            // �����������ˮƽ��ķ���
            camDir.set(cam.getDirection());
            camDir.y = 0;
            camDir.normalizeLocal();

            // ������������򣬼�����ת�Ƕ�
            camRot.lookAt(camDir, Vector3f.UNIT_Y);

            // ʹ�ø���ת���ı����߷���
            camRot.mult(walkDir, camDir);

            // �ı���ҵĳ���// ��ɫ������
            player.setViewDirection(camDir);

            // �����ٶȴ�С
            camDir.multLocal(0.1f);
        } else {
            camDir.set(0, 0, 0);
        }
        //���ý�ɫ���ߣ����Ե��� player.setWalkDirection() ������
        //���߷���Ĳ�����һ��3D�����������ķ������ߵķ��������ĳ����������ߵ��ٶȣ���λ�� m/s��
        player.setWalkDirection(camDir);
        cam.setLocation(camNode.getWorldTranslation());
    }

    /**
     * ��ʼ����ɫ�ڵ�
     */
    private void initCharacter() {
        this.character = new Node("Character");
        character.setLocalTranslation(0, height / 2 + radius+1, 0);

        // ����ģ��
        this.model = assetManager.loadModel("Models/Jaime/Jaime.j3o");
        character.attachChild(model);// �ҵ���ɫ���ڵ���

        model.setLocalTranslation(0, -(height / 2 + radius), 0);
        model.scale(1.8f);

        // ����һ�������ڵ㣬���������������λ�á�
        this.camNode = new Node("Camera");
        character.attachChild(camNode);
        camNode.setLocalTranslation(0, height / 2, radius);// ���˽ڵ�����һ�ξ��룬ʹ�����λ�ڽ�ɫ��ͷ����
    }

    /**
     * Ϊ��ɫ������������
     */
    private void initPhysics() {
        // ʹ�ý�������Ϊ��ҵ���ײ��״
        CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(radius, height, 1);

        // ʹ��CharacterControl�������������
        this.player = new CharacterControl(capsuleShape, stepHeight);
        character.addControl(player);// �󶨽�ɫ������

        player.setJumpSpeed(10);// �����ٶ�
        player.setFallSpeed(55);// ׹���ٶ�
        player.setGravity(new Vector3f(0.0f,-9.8f,0.0f));// �������ٶ�
        player.setPhysicsLocation(new Vector3f(0, height / 2 + radius, 0));// λ��

        stateManager.getState(BulletAppState.class).getPhysicsSpace().add(player);
    }

    /**
     * ��ʼ����ɫ����
     */
    private void initAnimation() {
        animControl = model.getControl(AnimControl.class);
        animChannel = animControl.createChannel();

        animControl.addListener(this);
        animChannel.setAnim("Idle");
    }
    
    /**
     * �����˳������
     */
    private void initChaseCamera() {
        ChaseCamera chaseCam = new ChaseCamera(cam, camNode, inputManager);
        chaseCam.setInvertVerticalAxis(true);// ��ֱ��ת
        chaseCam.setMinDistance(0.1f);// ����뽹����������
        chaseCam.setDefaultDistance(10f);// Ĭ�Ͼ���
    }

    @Override
    protected void onEnable() {
        SimpleApplication simpleApp = (SimpleApplication) getApplication();
        simpleApp.getRootNode().attachChild(character);
    }

    @Override
    protected void onDisable() {
        character.removeFromParent();
    }

    /**
     * �ý�ɫ������
     */
    public void jump() {
        if (player.onGround()) {
            player.jump(new Vector3f(0.0f,2.0f,0.0f));
            
            animChannel.setAnim("JumpStart");
            animChannel.setLoopMode(LoopMode.DontLoop);
            animChannel.setSpeed(1.8f);
        }
    }

    /**
     * �ý�ɫ��·
     * 
     * @param dir
     */
    public void walk(Vector3f dir) {
        if (dir != null) {
            
            if (walkDir.lengthSquared() == 0) {
                animChannel.setAnim("Walk");
                animChannel.setSpeed(3f);
            }
            
            dir.normalizeLocal();

            walkDir.set(dir);
        }
    }

    /**
     * �ý�ɫͣ����
     */
    public void idle() {
        walkDir.set(0, 0, 0);
        if (player.onGround()) {
            animChannel.setAnim("Idle");
        }
    }

    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
        if ("JumpStart".equals(animName)) {
            // �����������������󣬽����Ų��š��ŵء�������
            channel.setAnim("JumpEnd");
            channel.setLoopMode(LoopMode.DontLoop);
            channel.setSpeed(1.8f);

        } else if ("JumpEnd".equals(animName)) {
            // ���ŵء��󣬸��ݰ���״̬�����š����ߡ������á�������
            if (walkDir.lengthSquared() != 0) {
                channel.setAnim("Walk");
                channel.setLoopMode(LoopMode.Loop);
                channel.setSpeed(3f);
            } else {
                channel.setAnim("Idle");
                channel.setLoopMode(LoopMode.Loop);
            }
        }
    }

    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
    }

    @Override
    protected void cleanup(Application app) {
    }
}

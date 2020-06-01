package scene;

import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.bullet.BulletAppState;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

/**
 * ��ʾʹ�õ����˳ƿ���Jaime�ڵ�ͼ���������ߡ�
 * 
 * @author yanmaoyuan
 *
 */
public class Main extends SimpleApplication {

    public Main() {
        super(new StatsAppState());
    }

    @Override
    public void simpleInitApp() {
        stateManager.attachAll(new BulletAppState(), 
                new SceneAppState(), 
                new CharacterAppState(),
                new InputAppState());
        
        // ������
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(new ColorRGBA(0.7f, 0.7f, 0.7f, 1f));

        // ����
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-1, -2, -3).normalizeLocal());

        rootNode.addLight(ambient);
        rootNode.addLight(sun);
    }

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }
}
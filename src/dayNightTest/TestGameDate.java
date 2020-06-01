package dayNightTest;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.shadow.BasicShadowRenderer;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.util.TangentBinormalGenerator;

/**
 * 游戏时间以及昼夜系统测试
 * @author yanmaoyuan
 *
 */
public class TestGameDate extends SimpleApplication {

	private GameDate gameDate;// 游戏时间
	private DirectionalLight sunLight;// 太阳光
	private BitmapText gui;// 用来显示游戏时间

	private Geometry sunBox;// 用一个小方块来模拟代表太阳
	@Override
	public void simpleInitApp() {
		// 初始化游戏时间
		gameDate = new GameDate();

		// 初始化镜头
        cam.setLocation(new Vector3f(27.492603f, 29.138166f, -13.232513f));
        cam.setRotation(new Quaternion(0.25168246f, -0.10547892f, 0.02760565f, 0.96164864f));
        flyCam.setMoveSpeed(30);

        // 我们创建一个红色小方块，用来代表太阳，它会根据时间的变化而移动。
        Box box = new Box(5,5,5);
        sunBox = new Geometry("Box", box);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Red);
        sunBox.setMaterial(mat);
        sunBox.setShadowMode(ShadowMode.Off);
        rootNode.attachChild(sunBox);

        setupGui();
        setupLighting();
        setupFloor();
        setupSignpost();

	}

	/**
	 * 创建gui，用来显示时间
	 */
	public void setupGui() {
		BitmapFont guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
		gui = new BitmapText(guiFont, false);
		gui.setText("00:00");
		guiNode.attachChild(gui);

		// 把gui放在屏幕顶部居中
		float width = (settings.getWidth() - gui.getLineWidth())/2;
		float height = settings.getHeight();
		gui.setLocalTranslation(width, height, 0);

	}

	/**
	 * 创建光源
	 */
	public void setupLighting() {
		// 阳光
		sunLight = new DirectionalLight();
		sunLight.setColor(ColorRGBA.White.clone());
		sunLight.setDirection(gameDate.getSunDirection());
		rootNode.addLight(sunLight);

		// 设置一个很淡的环境光
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(0.3f));
        rootNode.addLight(al);

        rootNode.setShadowMode(ShadowMode.CastAndReceive);

        // 阳光产生影子全靠这玩意了！
        DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(assetManager, 512, 4);
        dlsr.setLight(sunLight);
        viewPort.addProcessor(dlsr);
	}

	/**
	 * 创建一个地板，这样我们才能看见影子。
	 */
    public void setupFloor() {
        Material mat = assetManager.loadMaterial("Textures/Terrain/Pond/Pond.j3m");
        mat.getTextureParam("DiffuseMap").getTextureValue().setWrap(WrapMode.Repeat);
        mat.getTextureParam("NormalMap").getTextureValue().setWrap(WrapMode.Repeat);
        mat.setBoolean("UseMaterialColors", true);
        mat.setColor("Diffuse", ColorRGBA.White.clone());
        mat.setColor("Ambient", ColorRGBA.White.clone());
        // mat.setColor("Specular", ColorRGBA.White.clone());
        // mat.getTextureParam("ParallaxMap").getTextureValue().setWrap(WrapMode.Repeat);
        mat.setFloat("Shininess", 0);
        // mat.setBoolean("VertexLighting", true);

        Box floor = new Box(100, 1f, 100);
        TangentBinormalGenerator.generate(floor);
        floor.scaleTextureCoordinates(new Vector2f(5, 5));
        Geometry floorGeom = new Geometry("Floor", floor);
        floorGeom.setMaterial(mat);
        floorGeom.setShadowMode(ShadowMode.Receive);// 地板只接受影子，不产生影子。
        rootNode.attachChild(floorGeom);
    }

    /**
     * 创建一个sign，我们可以看到它在阳光下的影子。
     */
    public void setupSignpost() {
        Spatial signpost = assetManager.loadModel("Models/Sign Post/Sign Post.mesh.xml");
        Material mat = assetManager.loadMaterial("Models/Sign Post/Sign Post.j3m");
        signpost.setMaterial(mat);

        signpost.rotate(0, FastMath.HALF_PI, 0);
        signpost.setLocalTranslation(0, 3.5f, 0);
        signpost.setLocalScale(4);
        signpost.setShadowMode(ShadowMode.CastAndReceive);
        TangentBinormalGenerator.generate(signpost);
        rootNode.attachChild(signpost);
    }

	@Override
	public void simpleUpdate(float tpf) {
		// 更新游戏时间
		gameDate.update();
		// 更新gui，显示当前时间
		gui.setText(String.format("%02d:%02d", gameDate.getHour(), gameDate.getMinute()));
		// 更新阳光亮度
		float power = gameDate.getLightPower();
		sunLight.setColor(ColorRGBA.White.clone().mult(power));
		// 更新光照角度
		sunLight.setDirection(gameDate.getSunDirection());


		sunBox.setLocalTranslation(gameDate.getSunDirection().mult(-100f));
	}

	public static void main(String[] args) {
		TestGameDate app = new TestGameDate();
		app.start();
	}

}

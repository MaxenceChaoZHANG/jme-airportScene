package jmeDemo.Hello;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.scene.Spatial;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.geomipmap.lodcalc.DistanceLodCalculator;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import com.jme3.water.WaterFilter;

/**
 * ͨ���߶�ͼ���ص��Ρ�
 *
 * @author yanmaoyuan
 *
 */
public class HelloTerrain extends SimpleApplication {

    @Override
    public void simpleInitApp() {

        cam.setLocation(new Vector3f(-100, 80, 50));

        flyCam.setMoveSpeed(20f);

        initLight();

        initSky();

        initWater();

        initTerrain();
    }

    /**
     * ��ʼ���ƹ�
     */
    private void initLight() {
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(new ColorRGBA(0.298f, 0.2392f, 0.2745f, 1f));
        rootNode.addLight(ambient);

        DirectionalLight light = new DirectionalLight();
        light.setDirection((new Vector3f(0.097551f, -0.733139f, -0.673046f)).normalize());
        light.setColor(new ColorRGBA(1, 1, 1, 1));
        rootNode.addLight(light);
    }

    /**
     * ��ʼ�����
     */
    private void initSky() {
        Spatial sky = SkyFactory.createSky(assetManager, "Textures/Sky/SkySphereMap.jpg",
                SkyFactory.EnvMapType.SphereMap);
        rootNode.attachChild(sky);
    }

    /**
     * ��ʼ��ˮ��
     */
    private void initWater() {
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        viewPort.addProcessor(fpp);

        // ˮ
        WaterFilter waterFilter = new WaterFilter();
        waterFilter.setWaterHeight(50f);// ˮ��߶�
        waterFilter.setWaterTransparency(0.2f);// ͸����
        waterFilter.setWaterColor(new ColorRGBA(0.4314f, 0.9373f, 0.8431f, 1f));// ˮ����ɫ

        fpp.addFilter(waterFilter);
    }

    /**
     * ��ʼ������
     */
    private void initTerrain() {

        // ���ص��εĸ߶�ͼ
        Texture heightMapImage = assetManager.loadTexture("Scenes/DefaulMap/default.png");

        // ����ͼ�����ݣ����ɸ߶�ͼ
        ImageBasedHeightMap heightmap = new ImageBasedHeightMap(heightMapImage.getImage(), 1f);
        heightmap.load();

        // ��˹ƽ��
        GaussianBlur gaussianBlur = new GaussianBlur();

        float[] heightData = heightmap.getHeightMap();
        int width = heightMapImage.getImage().getWidth();
        int height = heightMapImage.getImage().getHeight();

        heightData = gaussianBlur.filter(heightData, width, height);

        /*
         * ���ݸ߶�ͼ����ʵ�ʵĵ��Ρ��õ��α��ֽ�ɱ߳�65(64*64)�ľ������飬�����Ż����񡣸߶�ͼ�ı߳�Ϊ 257���ֱ��� 256*256��
         */
        TerrainQuad terrain = new TerrainQuad("terrain", 65, 257, heightmap.getHeightMap());

        // ���ϸ��
        TerrainLodControl control = new TerrainLodControl(terrain, getCamera());
        control.setLodCalculator(new DistanceLodCalculator(65, 2.7f));
        terrain.addControl(control);

        // ���β���
        terrain.setMaterial(assetManager.loadMaterial("Scenes/DefaulMap/default.j3m"));

        terrain.setLocalTranslation(0, -100, 0);
        rootNode.attachChild(terrain);
    }

    public static void main(String[] args) {
        HelloTerrain app = new HelloTerrain();
        app.start();
    }

}
package appState;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.FrameBuffer;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;

public class MiniMapState extends BaseAppState {

    private final int minimapSize;
    private final float scale;

    private Camera mapCam;
    private ViewPort mapViewport;
    private Geometry minimap;

    private Node mapRoot;
    private Node guiNode;

    /**
     * 创建迷你地图
     * @param mapRoot  地图场景
     * @param scale 地图范围（scale*1*1）
     * @param size  地图大小
     */
    public MiniMapState(Node mapRoot, float scale, int size) {
        this.mapRoot = mapRoot;
        this.scale = scale;
        this.minimapSize = size;
    }

    public Node getMapRoot() {
        return mapRoot;
    }

    public ViewPort getViewport() {
        return mapViewport;
    }

    @Override
    protected void initialize(Application app) {
     
        mapCam = new Camera(minimapSize, minimapSize);
        mapCam.setParallelProjection(true);
        mapCam.setFrustumNear(1);
        mapCam.setFrustumFar(900);
        //初始视域1*1，根据scale放大
        setFiledOfView(mapCam, scale);        
//        System.out.println(mapCam.getFrustumBottom());
//        System.out.println(mapCam.getFrustumLeft());
//        System.out.println(mapCam.getFrustumRight());
//        System.out.println(mapCam.getFrustumTop());
        mapCam.setLocation(new Vector3f(0,672, 0));
        mapCam.lookAt(new Vector3f(0, -1, 0), Vector3f.UNIT_Y);
        
        mapViewport = app.getRenderManager().createMainView("Offscreen View", mapCam);
        mapViewport.setClearFlags(true, true, true);
        mapViewport.setBackgroundColor(ColorRGBA.DarkGray);
        
        Texture2D offTex = new Texture2D(minimapSize, minimapSize, Image.Format.RGBA8);
        offTex.setMinFilter(Texture.MinFilter.Trilinear);
        offTex.setMagFilter(Texture.MagFilter.Bilinear);
        
        FrameBuffer offBuffer = new FrameBuffer(minimapSize, minimapSize, 1);
        offBuffer.setDepthBuffer(Image.Format.Depth);
        offBuffer.setColorTexture(offTex);

        mapViewport.setOutputFrameBuffer(offBuffer);
        mapViewport.attachScene(mapRoot);
  
        minimap = new Geometry("MiniMap", new Quad(minimapSize, minimapSize));
        minimap.setMaterial(new Material(app.getAssetManager(), "MatDefs/MiniMap/MiniMap.j3md"));
        minimap.getMaterial().setTexture("ColorMap", offTex);
        minimap.getMaterial().setTexture("Mask", app.getAssetManager().loadTexture("Textures/MiniMap/circle-mask.png"));
        minimap.getMaterial().setTexture("Overlay", app.getAssetManager().loadTexture("Textures/MiniMap/circle-overlay.png"));
        minimap.setLocalTranslation(
                app.getCamera().getWidth() - minimapSize - 20,
                app.getCamera().getHeight() - minimapSize - 20,
                1
        );

        guiNode = ((SimpleApplication)app).getGuiNode();
        guiNode.attachChild(minimap);
    }


    @Override
    protected void cleanup(Application app) {
        getApplication().getRenderManager().removeMainView(mapViewport);
    }

    @Override
    protected void onEnable() {
        guiNode.attachChild(minimap);
    }

    @Override
    protected void onDisable() {
        minimap.removeFromParent();
    }

    private final Quaternion mapRot = new Quaternion();
    private final float[] angles = new float[3];

    @Override
    public void update(float tpf) {

        getApplication().getCamera().getRotation().toAngles(angles);

        mapRot.fromAngles(FastMath.HALF_PI, angles[1], 0);

        mapCam.setRotation(mapRot);

        mapCam.setLocation(new Vector3f(
                getApplication().getCamera().getLocation().x,
               672,
                getApplication().getCamera().getLocation().z
        ));
        
        minimap.setLocalTranslation(
        		getApplication().getCamera().getWidth() - minimapSize - 20,
        		getApplication().getCamera().getHeight() - minimapSize - 20,
                1
        );

    }

    private void setFiledOfView(Camera camera, float factor) {
        float bottom = camera.getFrustumBottom();
        camera.setFrustumBottom(bottom * factor);
        float left = camera.getFrustumLeft();
        camera.setFrustumLeft(left * factor);
        float right = camera.getFrustumRight();
        camera.setFrustumRight(right * factor);
        float top = camera.getFrustumTop();
        camera.setFrustumTop(top * factor);
        
    }

}

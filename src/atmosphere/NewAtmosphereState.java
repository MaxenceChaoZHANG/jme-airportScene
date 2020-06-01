package atmosphere;

import atmosphere.sky.ScatteringParameters;
import atmosphere.stars.StarsDome;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.*;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.BillboardControl;
import com.jme3.scene.shape.Dome;
import com.jme3.texture.Texture;
/*
 * 包含cloudsetting starsetting 内部类
 * 使用自定义shader
 */

public class NewAtmosphereState extends BaseAppState {

    private Node node = new Node("Sky");
    private Geometry skyGeometry;
    private Material material;

    // sky 散射参数
    private ScatteringParameters sParams;
    private Vector3f lightDir;
    private boolean HDR = false;

    // calendar-related
    private final SimplePositionProvider positionProvider;
    private final ColorGradients colorGradients;
    //内部类实例，云朵和星星参数
    private CloudSettings cloudSettings;
    private StarSettings starSettings;
    //月亮（太阳也使用该对象，仅仅是光线颜色方向等有所区别）---错误
    private Moon moon;
    //星空
    private StarsDome starsDome;
    
    //app根节点
    private Node worldNode;

    private final ColorRGBA sunColor = new ColorRGBA();
    private final ColorRGBA ambientColor = new ColorRGBA();

    private DirectionalLight directionalLight = new DirectionalLight(new Vector3f());
    private AmbientLight ambientLight = new AmbientLight(ambientColor);

    public NewAtmosphereState(Node worldNode) {

        positionProvider = new SimplePositionProvider();
      //时间流速
        positionProvider.getCalendar().setTimeMult(140);
        //早晨5点
//        positionProvider.getCalendar().setHour(5);
//        positionProvider.getCalendar().setDay(15);
        System.out.println("dayInyear:"+positionProvider.getCalendar().getDayInYear());
        System.out.println("moon phase:"+positionProvider.getMoonPhase());
        //positionProvider.getCalendar().setHour(40);
        
        colorGradients = new ColorGradients();

        // positionProvider.setDeclination(50);
        this.worldNode = worldNode;
    }

    public Calendar getCalendar() {
        return positionProvider.getCalendar();
    }

    public CloudSettings getCloudSettings() {
        return cloudSettings;
    }

    public StarSettings getStarSettings() {
        return starSettings;
    }

    public Moon getMoon() {
        return this.moon;
    }

    public SimplePositionProvider getPositionProvider() {
        return positionProvider;
    }

    public DirectionalLight getDirectionalLight() {
        return directionalLight;
    }

    public AmbientLight getAmbientLight() {
        return ambientLight;
    }

    @Override
    protected void initialize(Application app) {

        // create the dome geometry
        int planes = 32;
        int radialSamples = 32;
        int radius = 950;
        //中心，高度上划分，圆周划分，仅内部可见（圆顶）
        Dome shape = new Dome(new Vector3f(), planes,radialSamples, radius, true);
        this.skyGeometry = new Geometry("Sky", shape);
        
        starsDome = new StarsDome(app.getAssetManager(), 940, planes, planes);

        // initialize the material自定义天空shader
        this.material = new Material(app.getAssetManager(), "MatDefs/Sky.j3md");

        // clouds
        Texture noiseTex = app.getAssetManager().loadTexture("Textures/Noise/PerlinNoise_255.png");
        noiseTex.setWrap(Texture.WrapMode.Repeat);
        this.cloudSettings = new CloudSettings(noiseTex);

        Texture starColorsTex = app.getAssetManager().loadTexture("Textures/Noise/RandomColors.png");
        starColorsTex.setWrap(Texture.WrapMode.Repeat);
        this.starSettings = new StarSettings(starColorsTex);


        // sky
        // 包含在散射着色器中使用的参数
        sParams = new ScatteringParameters();
        //单位化向量
        lightDir = new Vector3f(-1,-1,-1).normalizeLocal();

        // Values used to calculate atmospheric depth.
        float Scale = 1f / (sParams.getOuterRadius() - sParams.getInnerRadius());
        float ScaleDepth = (sParams.getOuterRadius() - sParams.getInnerRadius()) / 2f;
        float ScaleOverScaleDepth = Scale / ScaleDepth;

        // Rayleigh scattering constant.
        float Kr4PI  = sParams.getRayleighMultiplier() * 4f * FastMath.PI;
        float KrESun = sParams.getRayleighMultiplier() * sParams.getSunIntensity();

        // Mie scattering constant.
        float Km4PI  = sParams.getMieMultiplier() * 4f * FastMath.PI;
        float KmESun = sParams.getMieMultiplier() * sParams.getSunIntensity();

        // Wavelengths
        Vector3f invWaveLength = new Vector3f(
                FastMath.pow(sParams.getWaveLength().x, -4f),
                FastMath.pow(sParams.getWaveLength().y, -4f),
                FastMath.pow(sParams.getWaveLength().z, -4f));


        material.setFloat("Scale", Scale);

        // we don't need to put these in containers since they don't even change once they're set.

        material.setFloat("ScaleDepth", ScaleDepth);
        material.setFloat("ScaleOverScaleDepth", ScaleOverScaleDepth);
        material.setFloat("InnerRadius", sParams.getInnerRadius());
        material.setVector3("CameraPos",new Vector3f(0, sParams.getInnerRadius() + (sParams.getOuterRadius() - sParams.getInnerRadius()) * sParams.getHeightPosition(), 0));
        material.setFloat("Kr4PI", Kr4PI);
        material.setFloat("KrESun", KrESun);
        material.setFloat("Km4PI", Km4PI);
        material.setFloat("KmESun", KmESun);
        material.setInt("NumberOfSamples", sParams.getNumberOfSamples());
        material.setFloat("Samples", (float)sParams.getNumberOfSamples());
        material.setVector3("InvWaveLength", invWaveLength);
        material.setFloat("G", sParams.getG());//phase function
        material.setFloat("G2", sParams.getG() * sParams.getG());
        material.setFloat("Exposure", sParams.getExposure());

        material.setBoolean("HDR", HDR);
        material.setVector3("LightDir", lightDir);
        //太阳光颜色 、环境光颜色
        material.setColor("SunColor", sunColor);
        material.setColor("AmbientColor", ambientColor);

        // END
        this.skyGeometry.setMaterial(material);
        //设置阴影模式
        this.skyGeometry.setShadowMode(RenderQueue.ShadowMode.Off);
        
        //定义月亮
        moon = new Moon(app.getAssetManager());
        //广告牌控制
        moon.getGeometry().addControl(new BillboardControl());

        this.node.attachChild(skyGeometry);
        this.node.attachChild(moon.getGeometry());
//        this.node.attachChild(starsDome.getGeometry());
    }

    @Override
    protected void cleanup(Application app) {

    }

    @Override
    protected void onEnable() {
    	//node下有 moon 、sky
        ((SimpleApplication)getApplication()).getRootNode().attachChild(this.node);
        //worldNode=rootNode
        worldNode.addLight(directionalLight);
        worldNode.addLight(ambientLight);
    }

    @Override
    protected void onDisable() {
        this.node.removeFromParent();
        worldNode.removeLight(directionalLight);
        worldNode.removeLight(ambientLight);
    }

    @Override
    public void update(float tpf) {

        positionProvider.update(tpf);

        Vector3f sunDirection = positionProvider.getSunDirection();
//        System.out.println("太阳"+sunDirection);
//        sunDirection.y=-sunDirection.y;
        // directionalLight.setDirection(sunDirection.negate());
        //着色器中使用太阳位置，确保角度在0-90
        material.setVector3("LightDir", sunDirection.negate());
        //位置=sA 与时间有关，太阳位置的高度，由太阳方向换算得到
        float position = -sunDirection.y / positionProvider.getMaxHeight();

//        moon.getGeometry().setLocalTranslation(positionProvider.getMoonDirection().mult(850f));
        moon.getGeometry().setLocalTranslation(positionProvider.getMoonDirection().mult(850f));
        moon.setPhase(positionProvider.getMoonPhase());;
        // not certain we actually need these in the shader...
        // the sun color gets bright way too late (around 8-9am instead of 5-6am
        sunColor.set(colorGradients.getSunColor(position + 0.3f));

        // clamp the position so that the ambient never gets too low.
        // we're effectively settings the minimum ambient brightness.
        ambientColor.set( colorGradients.getSkyAmbientColor( FastMath.clamp(position, 0.5f, 1) ) );
        ambientLight.setColor(ambientColor);

        //根据时间显示太阳和月亮
        // The sun goes clockwise, the moon goes anti-clockwise.
        // At 6am and 6pm they cross paths.
        // This is when we will "switch" the direction that the directional light follows.

        if ( getCalendar().getHour() > 5 && getCalendar().getHour() < 18 ) {
//            directionalLight.setDirection(positionProvider.getSunDirection().negate());
            directionalLight.setDirection(positionProvider.getSunDirection());
            directionalLight.setColor(sunColor);
        }
        else {
            directionalLight.setDirection(positionProvider.getMoonDirection().negate());
            directionalLight.setColor(sunColor.mult(0.5f));
        }
        
        if ( getCalendar().getHour() > 5 && getCalendar().getHour() < 18 ) {
        	starsDome.getGeometry().removeFromParent();
        }
        else {
        	this.node.attachChild(starsDome.getGeometry());
        }
        node.setLocalTranslation(getApplication().getCamera().getLocation().x, 0, getApplication().getCamera().getLocation().z);

    }

    /**
     * Usually follows the player around...
     * @param location
     */
    public void setLocation(Vector3f location) {
        node.setLocalTranslation(location.x, 0, location.z);
    }
    
    
    /**
     * 内部类
     * StarSettings
     */

    public class StarSettings {
        //星星的参数
        // vec3 (amount, brightness, maxSize)
//        private final Vector3f stars = new Vector3f();
        private final Vector3f stars = new Vector3f(1.0f,0.1f,0.5f);
        //Texture starColorsTex = app.getAssetManager().loadTexture("Textures/Noise/RandomColors.png");
        //starColorsTex.setWrap(Texture.WrapMode.Repeat);
        public StarSettings(Texture starColors) {

            stars.set(.001f, .8f, 312.750f);
            material.setTexture("StarColors", starColors);
            material.setVector3("Stars", stars);
        }

        public float getAmount() { return this.stars.getX(); }
        public void setAmount(float amount) { this.stars.setX(amount); }

        public float getBrightness() { return this.stars.getY(); }
        public void setBrightness(float brightness) { this.stars.setY(brightness); }

        public float getSize() { return this.stars.getZ(); }
        public void setSize(float size) { this.stars.setZ(size); }

    }//结束StarSettings
    
    
    /**
     * 内部类
     * CloudSettings
     */
    public class CloudSettings {

        // wind speed affects how fast the clouds move in a given direction.
        // morph speed has a linear association with wind speed.

        // wind speed  百分比 间接影响speed和maxMorphSpeed
        private float windSpeed = 1.0f;

        private final float maxSpeed = 0.00769230769230772f * 2.0f;
        private final Vector2f direction = new Vector2f(1, 1).normalizeLocal();
        private final Vector2f speed = new Vector2f();
        //变形速度
        // morph speed
        private float maxMorphSpeed = 2.7f;

        private final float col = 0.5f;//云的颜色-灰 
//        private final ColorRGBA color = new ColorRGBA(col, col, col, 1.0f);
        private final ColorRGBA color = new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f);
        private int octaves = 8;

        private final float minCover = 0.6476190476190475f;
        private final float maxCover = 0.7746031746031746f;
        private float cloudCover = 0.5f;

        // x: scale
        // y: cloud cover
        // z: brightness
        // w: morphspeed
        private final Vector4f cloudSettings = new Vector4f( 0.25f, 0.72f, 0.056f, 2.7f );

        private boolean reverseMorph = true;
        //Texture noiseTex = app.getAssetManager().loadTexture("Textures/Noise/PerlinNoise_255.png");
        //noiseTex.setWrap(Texture.WrapMode.Repeat);
        public CloudSettings(Texture cloudNoise) {

            // All the settings should be in a container object, so they should all be bound.

            material.setTexture("Cloud_Noise", cloudNoise);
            material.setColor("Cloud_Color", this.color);
            material.setVector2("Cloud_Speed", this.speed);
            material.setVector4("Cloud_Settings", this.cloudSettings);

            material.setFloat("Cloud_MorphDirection", reverseMorph ? 1f : -1f);

            setOctaves(octaves);
            updateWindSpeed();
        }

        private void updateWindSpeed() {
            speed.set(direction.mult(maxSpeed).multLocal(windSpeed));
        }

        public float getWindSpeed() { return this.windSpeed; }

        /**
         * Defines the speed the clouds move and morph over time.
         * @param windSpeed a value between 0.0 (no wind) and 1.0 (full wind).
         */
        public void setWindSpeed(float windSpeed) {
            // speed and morph speed

            // max morph speed is around 2.7;
            // max movement speed is around
            this.windSpeed = windSpeed;
            this.cloudSettings.setW(this.maxMorphSpeed * windSpeed);
            updateWindSpeed();
        }

        public Vector2f getWindDirection() { return direction; }
        
        public void setWindDirection(Vector2f direction) {
            this.direction.set(direction);
            updateWindSpeed();
        }

        public ColorRGBA getColor() { return color; }
        public void setColor(ColorRGBA color) { this.color.set(color); }

        public float getScale() { return cloudSettings.x; }
        public void setScale(float scale) { cloudSettings.setX(scale); }

        public float getCover() { return cloudCover; }

        /**
         * Set the cloud cover. More cover makes more clouds.
         * @param cover a value between 0.0 (no clouds) and 1.0 (completely cloudy).
         */
        public void setCover(float cover) {
            this.cloudCover = cover;
            //比例转换
            float actualCover = map(cover, 0, 1, minCover, maxCover);
            cloudSettings.setY(actualCover);
        }

        public float getBrightness() { return cloudSettings.z; }
        public void setBrightness(float brightness) { cloudSettings.setZ(brightness); }

        public float getMorphSpeed() { return cloudSettings.w; }
        public void setMorphSpeed(float morphSpeed) { this.cloudSettings.setW(morphSpeed); }

        public int getOctaves() { return octaves; }
        public void setOctaves(int octaves) {
            this.octaves = octaves;
            material.setInt("Cloud_Octaves", octaves);
        }

        private float map(float value, float oldMin, float oldMax, float newMin, float newMax) {
            return (((value - oldMin) * (newMax - newMin)) / (oldMax - oldMin)) + newMin;
        }

    }//结束cloudSettings

}

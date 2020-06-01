package atmosphere;


import atmosphere.clouds.CloudsDome;
import atmosphere.sky.SkyDome;
import atmosphere.stars.StarsDome;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.control.BillboardControl;

/**
 * A state that handles the atmosphere color, clouds and stars.
 * Includes a directional (sun) and ambient light.
 */
public class SkyState extends BaseAppState {

    private final Node node = new Node("Sky");

    private final SimplePositionProvider positionProvider;
    private final ColorGradients colorGradients;

    private SkyDome skyDome;
    private StarsDome starsDome;
    private CloudsDome cloudsDome;

    private final Sun sun;
    private Moon moon;
    private AmbientLight ambientLight;

    // the varying colors of the clouds from day to night
    ColorRGBA cloudsColor = new ColorRGBA();
    ColorRGBA cloudsDayColor = ColorRGBA.White.clone();
    ColorRGBA cloudsNightColor = ColorRGBA.DarkGray.clone();

    public SkyState() {
        positionProvider = new SimplePositionProvider();
        positionProvider.getCalendar().setHour(12);
        positionProvider.getCalendar().setTimeMult(480);
        colorGradients = new ColorGradients();

        positionProvider.setDeclination(50);

        sun = new Sun();
        this.ambientLight = new AmbientLight();
    }

    public Calendar getCalendar() {
        return positionProvider.getCalendar();
    }

    public Sun getSun() {
        return sun;
    }

    public AmbientLight getAmbientLight() {
        return ambientLight;
    }

    public SimplePositionProvider getPositionProvider() {
        return positionProvider;
    }

    public StarsDome getStarsDome() {
        return starsDome;
    }

    public CloudsDome getCloudsDome() {
        return cloudsDome;
    }

    @Override
    protected void initialize(Application app) {

        int planes = 32;

        skyDome = new SkyDome(app.getAssetManager(), 960, planes, planes);
        starsDome = new StarsDome(app.getAssetManager(), 950, planes, planes);
        cloudsDome = new CloudsDome(app.getAssetManager(), 900, planes, planes);

        cloudsDome.getConfig().setCover(0.533f);
        cloudsDome.getConfig().setScale(0.25f);
        cloudsDome.getConfig().setMorphSpeed(1.0f);

        Node rootNode = ((SimpleApplication)app).getRootNode();

        rootNode.addLight(sun.getLight());
        rootNode.addLight(ambientLight);

        moon = new Moon(app.getAssetManager());
        moon.getGeometry().addControl(new BillboardControl());

        node.attachChild(skyDome.getGeometry());
        node.attachChild(starsDome.getGeometry());
        node.attachChild(moon.getGeometry());
        node.attachChild(cloudsDome.getGeometry());


    }

    @Override
    protected void cleanup(Application app) {

    }

    @Override
    protected void onEnable() {
        ((SimpleApplication)getApplication()).getRootNode().attachChild(node);
    }

    @Override
    protected void onDisable() {
        node.removeFromParent();
    }

    @Override
    public void update(float tpf) {
        positionProvider.update(tpf);

        // y: returns 1 when the sun is at its highest, -1 at its lowest.
        Vector3f sunDirection = positionProvider.getSunDirection();
        // System.out.println(sunDirection);

        // by altering this we alter the color of the sun.
        float position = sunDirection.y / positionProvider.getMaxHeight();// * 0.5f + 0.3f;

        skyDome.getMaterial().setVector3("LightDir", sunDirection);

        // these are cloud-related that we need to put in.
        // skyDome.getMaterial().setColor("SunColor", colorGradients.getSunColor(position));
        // skyDome.getMaterial().setColor("AmbientColor", colorGradients.getSkyAmbientColor(FastMath.clamp(position + 0.1f, 0, 1)));

        moon.getGeometry().setLocalTranslation(sunDirection.mult(-850f)
                        .addLocal(getApplication().getCamera().getLocation().x, 0, getApplication().getCamera().getLocation().z));

        // as the cloud cover increases, the strength of the sun (color) decreases.
        float cloudCoverAmbientReduction = map(cloudsDome.getConfig().getCover(),
                0.6f, 0.8f,
                0.0f, 0.75f);

        // System.out.println("Sun Reduction: " + cloudCoverAmbientReduction);

        // ranges from 0 to 1 based on the height of the sun from the horizon.
        // at night it will be zero and at mid-day it will be somewhere near 1 (depending on the time of year).
        float sunHeightMult = FastMath.clamp(sunDirection.y - cloudCoverAmbientReduction, 0, 1);



        sun.setColor(colorGradients.getSunColor(position).mult(sunHeightMult));
        sun.setDirection(sunDirection.negate());

        // the stars, clouds and material fog need to change color at night.
        // - stars should be bright at night and dim in the day.
        // - clouds should be white in the day and a kind of grey at night.
        // - material fog should be darker at night.
        // for all of these: we should set a day and night color and mix them.

        // we want the stars to be the brightest at night and near-invisible at day.
        float starBrightness = 1.0f - sunHeightMult;

        this.starsDome.setOverallBrightness(starBrightness);

        cloudsColor.interpolateLocal(cloudsDayColor, cloudsNightColor, starBrightness);
        cloudsDome.getConfig().setColor(cloudsColor);

        // we don't want it to be completely dark at night, just "almost".
        float ambientColorMult = FastMath.clamp(sunHeightMult, 0.1f, 0.4f);

        ColorRGBA ambientColor = colorGradients.getSkyAmbientColor(position);//.mult(ambientColorMult);
        this.ambientLight.setColor(ambientColor);
    }

    // switches one range to another.
    private float map(float value, float oldMin, float oldMax, float newMin, float newMax) {
        return (((value - oldMin) * (newMax - newMin)) / (oldMax - oldMin)) + newMin;
    }

    public void setLocation(Vector3f location) {
        node.setLocalTranslation(location.x, 0, location.z);
    }



}

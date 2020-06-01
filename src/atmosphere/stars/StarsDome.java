package atmosphere.stars;


import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Dome;

/**
 * Created by James on 12/05/2017.
 */
public class StarsDome {

    private final Geometry starsGeometry;
    private StarLayer[] starLayers = new StarLayer[4];
    private float overallBrightness = 1.0f;

    public StarsDome(AssetManager assetManager, float radius, int horizontalPlanes, int radialSamples) {

        Dome shape = new Dome(new Vector3f(), horizontalPlanes,radialSamples,radius, true);
        this.starsGeometry = new Geometry("Stars", shape);

        Material material = new Material(assetManager, "MatDefs/Stars/Stars.j3md");

        for (int i = 0; i < starLayers.length; i++) {
            this.starLayers[i] = new StarLayer(i, material);
        }

        // Vector2f direction = new Vector2f(1, 1);

        starLayers[0].setAmount(.001f);
        starLayers[0].setBrightness(.8f);
        starLayers[0].setSize(112.750f);
        starLayers[0].setColor(new ColorRGBA(1.0f, .64f, 0.64f, 1.0f));
        starLayers[0].setSpeed(new Vector2f(16.2f, 12f));

        starLayers[1].setAmount(.001f);
        starLayers[1].setBrightness(.8f);
        starLayers[1].setSize(100.750f);
        starLayers[1].setColor(new ColorRGBA(.64f, 1.0f, 0.64f, 1.0f));
        starLayers[1].setSpeed(new Vector2f(12f, 12f));

        starLayers[2].setAmount(.001f);
        starLayers[2].setBrightness(.8f);
        starLayers[2].setSize(112.750f);
        starLayers[2].setColor(new ColorRGBA(.64f, .64f, 1f, 1.0f));
        starLayers[2].setSpeed(new Vector2f(14f, 12f));

        starLayers[3].setAmount(.003f);
        starLayers[3].setBrightness(.8f);
        starLayers[3].setSize(112.750f);
        starLayers[3].setColor(new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f));
        starLayers[3].setSpeed(new Vector2f(9f, 12f));

        material.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        this.starsGeometry.setQueueBucket(RenderQueue.Bucket.Transparent);

        this.starsGeometry.setMaterial(material);
        this.starsGeometry.setShadowMode(RenderQueue.ShadowMode.Off);
    }

    public Geometry getGeometry() {
        return this.starsGeometry;
    }

    public StarLayer getLayer(int layer) {
        return this.starLayers[layer];
    }

    public float getOverallBrightness() {
        return this.overallBrightness;
    }

    public void setOverallBrightness(float overallBrightness) {
        this.overallBrightness = overallBrightness;

        for (int i = 0; i < starLayers.length; i++) {
            starLayers[i].setBrightnessOverall(starLayers[i].getBrightness() * this.overallBrightness);
        }
    }

}

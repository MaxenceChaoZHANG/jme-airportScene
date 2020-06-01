package atmosphere.clouds;


import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Dome;
import com.jme3.texture.Texture;

/**
 * Created by James on 10/05/2017.
 */
public class CloudsDome {

    Material material;
    private Geometry cloudsGeometry;

    ColorRGBA color = ColorRGBA.White.clone();
    float scale = 1.0f;
    Vector2f speed = new Vector2f(0.006923076923076921f, 0.00769230769230772f);
    float cover = 0.633f;
    float brightness = 0.126f;
    float morphSpeed = 2.7f;
    boolean reverseMorph = false;

    private CloudConfig cloudConfig;

    public CloudsDome(AssetManager assetManager, float radius, int horizontalPlanes, int radialSamples) {

        Dome shape = new Dome(new Vector3f(), horizontalPlanes,radialSamples,radius, true);
        this.cloudsGeometry = new Geometry("Clouds", shape);

        // Texture noiseTex = assetManager.loadTexture("/Textures/grayscale-noise.png");
        Texture noiseTex = assetManager.loadTexture("Textures/Moon/PerlinNoise_255.png");
        noiseTex.setWrap(Texture.WrapMode.Repeat);

        this.material = new Material(assetManager, "MatDefs/Clouds/NoiseClouds.j3md");
        this.material.setTexture("Noise", noiseTex);
        this.material.setColor("Color", this.color);
        this.material.setFloat("Scale", this.scale);
        this.material.setVector2("Speed", this.speed);
        this.material.setFloat("Cover", this.cover);
        this.material.setFloat("Brightness", this.brightness);
        this.material.setFloat("MorphSpeed", this.morphSpeed);
        this.material.setFloat("MorphDirection", reverseMorph ? 1f : -1f);
        this.material.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.AlphaAdditive);

        this.cloudsGeometry.setMaterial(material);
        this.cloudsGeometry.setQueueBucket(RenderQueue.Bucket.Transparent);
        this.cloudsGeometry.setShadowMode(RenderQueue.ShadowMode.Off);

        this.cloudConfig = new CloudConfig(this);
    }

    public Geometry getGeometry() {
        return this.cloudsGeometry;
    }

    public CloudConfig getConfig() {
        return this.cloudConfig;
    }
}

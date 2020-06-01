package atmosphere.sky;


import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Dome;

/**
 * Created by James on 13/05/2017.
 */
public class SkyDome {

    private Vector3f lightDir;
    private boolean HDR = false;
    private ScatteringParameters sParams;
    private Material material;
    private Geometry skyGeometry;

    public SkyDome(AssetManager assetManager, float radius, int horizontalPlanes, int radialSamples) {


        sParams = new ScatteringParameters();
        lightDir = new Vector3f(-1,-1,-1).normalizeLocal();

        // this.domeRadius = domeRadius;
        // ------ Sky geometry and material ------
        Dome dome = new Dome(new Vector3f(), horizontalPlanes, radialSamples, radius, true);
        skyGeometry = new Geometry("Sky",dome);
        material = new Material(assetManager, "MatDefs/Sky/SkyBase.j3md");
        skyGeometry.setMaterial(material);
        skyGeometry.setQueueBucket(RenderQueue.Bucket.Transparent);
        material.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        // skyDome.setCullHint(Spatial.CullHint.Never);

        initMaterial(true);
        this.skyGeometry.setShadowMode(RenderQueue.ShadowMode.Off);
    }

    private void initMaterial(boolean reset){

        // Values used to calculate atmospheric depth.
        float Scale = 1f / (sParams.getOuterRadius() - sParams.getInnerRadius());
        float ScaleDepth = (sParams.getOuterRadius() - sParams.getInnerRadius()) / 2f;
        float ScaleOverScaleDepth = Scale / ScaleDepth;

        // Rayleigh scattering constant.
        float Kr4PI  = sParams.getRayleighMultiplier() * 4f * FastMath.PI,
                KrESun = sParams.getRayleighMultiplier() * sParams.getSunIntensity();
        // Mie scattering constant.
        float Km4PI  = sParams.getMieMultiplier() * 4f * FastMath.PI,
                KmESun = sParams.getMieMultiplier() * sParams.getSunIntensity();

        // Wavelengths
        Vector3f invWaveLength = new Vector3f(FastMath.pow(sParams.getWaveLength().x, -4f),
                FastMath.pow(sParams.getWaveLength().y, -4f),
                FastMath.pow(sParams.getWaveLength().z, -4f));


        material.setFloat("Scale", Scale);
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
        material.setFloat("G", sParams.getG());
        material.setFloat("G2", sParams.getG() * sParams.getG());
        material.setFloat("Exposure", sParams.getExposure());

        if(!reset){
            material.setBoolean("HDR", HDR);
            material.setVector3("LightDir", lightDir);
        }
    }

    public ScatteringParameters getScatteringParams() {
        return this.sParams;
    }

    public Geometry getGeometry() {
        return this.skyGeometry;
    }

    public Material getMaterial() {
        return this.material;
    }




}

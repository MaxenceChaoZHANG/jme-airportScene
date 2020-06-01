package atmosphere.clouds;


import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

/**
 * Created by James on 20/05/2017.
 */
public class CloudConfig {

    private final CloudsDome cloudsDome;

    CloudConfig(CloudsDome cloudsDome) {
        this.cloudsDome = cloudsDome;
    }

    ColorRGBA getColor() {
        return cloudsDome.color;
    }

    public void setColor(ColorRGBA color) {
        cloudsDome.color.set(color);
        cloudsDome.material.setColor("Color", cloudsDome.color);
    }

    public void setColor(float r, float g, float b, float a) {
        cloudsDome.color.set(r, g, b, a);
    }

    public float getColorR() {
        return cloudsDome.color.r;
    }

    public void setColorR(float colorR) {
        cloudsDome.color.r = colorR;
        setColor(cloudsDome.color);
    }

    public float getColorG() {
        return cloudsDome.color.g;
    }

    public void setColorG(float colorG) {
        cloudsDome.color.g = colorG;
        setColor(cloudsDome.color);
    }

    public float getColorB() {
        return cloudsDome.color.b;
    }

    public void setColorB(float colorB) {
        cloudsDome.color.b = colorB;
        setColor(cloudsDome.color);
    }


    public float getScale() {
        return cloudsDome.scale;
    }

    public void setScale(float scale) {
        cloudsDome.scale = scale;
        cloudsDome.material.setFloat("Scale", cloudsDome.scale);
    }

    public Vector2f getSpeed() {
        return cloudsDome.speed;
    }

    public void setSpeed(Vector2f speed) {
        cloudsDome.speed.set(speed);
        cloudsDome.material.setVector2("Speed", cloudsDome.speed);
    }

    public float getSpeedX() {
        return cloudsDome.speed.x;
    }

    public void setSpeedX(float speedX) {
        setSpeed(cloudsDome.speed.clone().setX(speedX));
    }

    public float getSpeedY() {
        return cloudsDome.speed.y;
    }

    public void setSpeedY(float speedY) {
        setSpeed(cloudsDome.speed.clone().setY(speedY));
    }

    public float getCover() {
        return cloudsDome.cover;
    }

    public void setCover(float cover) {

        float val = (cover / 4f) + .5f;

        cloudsDome.cover = val;
        cloudsDome.material.setFloat("Cover", cloudsDome.cover);
        //cloudCoverageListeners.forEach(e -> e.cloudCoverageChanged(cover));
    }

    public float getBrightness() {
        return cloudsDome.brightness;
    }

    public void setBrightness(float brightness) {
        cloudsDome.brightness = brightness;
        cloudsDome.material.setFloat("Brightness", cloudsDome.brightness);
    }

    public float getMorphSpeed() {
        return cloudsDome.morphSpeed;
    }

    public void setMorphSpeed(float morphSpeed) {
        cloudsDome.morphSpeed = morphSpeed;
        cloudsDome.material.setFloat("MorphSpeed", cloudsDome.morphSpeed);
    }

    public void setReverseMorph(boolean reverseMorph) {
        cloudsDome.reverseMorph = reverseMorph;
        cloudsDome.material.setFloat("MorphDirection", reverseMorph ? 1f : -1f);
    }

    public boolean getReverseMorph() {
        return cloudsDome.reverseMorph;
    }

}

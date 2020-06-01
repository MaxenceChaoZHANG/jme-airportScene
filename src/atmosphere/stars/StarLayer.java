package atmosphere.stars;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

/**
 * Created by James on 13/05/2017.
 */
public class StarLayer {

    private final int layer;
    private final Material material;

    private float amount;
    private float brightness;
    private float size;

    private ColorRGBA color;
    private Vector2f speed;

    public StarLayer(int layer, Material material) {
        this.layer = layer;
        this.material = material;

        this.speed = new Vector2f();
    }

    public ColorRGBA getColor() {
        return color;
    }

    public void setColor(ColorRGBA color) {
        this.color = color;
        material.setColor("Color_" + layer, this.color);
    }

    public float getColorR() {
        return this.color.r;
    }

    public void setColorR(float colorR) {
        this.color.r = colorR;
        setColor(this.color);
    }

    public float getColorG() {
        return this.color.g;
    }

    public void setColorG(float colorG) {
        this.color.g = colorG;
        setColor(this.color);
    }

    public float getColorB() {
        return this.color.b;
    }

    public void setColorB(float colorB) {
        this.color.b = colorB;
        setColor(this.color);
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
        this.material.setVector3("Stars_" + layer, new Vector3f(this.amount, this.brightness, this.size));
    }

    public float getBrightness() {
        return brightness;
    }

    public void setBrightness(float brightness) {
        this.brightness = brightness;
        this.material.setVector3("Stars_" + layer, new Vector3f(this.amount, this.brightness, this.size));
    }

    public void setBrightnessOverall(float overall) {
        this.material.setVector3("Stars_" + layer, new Vector3f(this.amount, overall, this.size));
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
        this.material.setVector3("Stars_" + layer, new Vector3f(this.amount, this.brightness, this.size));
    }

    public Vector2f getSpeed() {
        return speed;
    }

    public void setSpeed(Vector2f speed) {
        this.speed.set(speed); // = speed;
         material.setVector2("Speed_" + layer, this.speed);
    }

    public float getSpeedX() {
        return this.speed.x;
    }

    public void setSpeedX(float speedX) {
        setSpeed(this.speed.clone().setX(speedX));
    }

    public float getSpeedY() {
        return this.speed.y;
    }

    public void setSpeedY(float speedY) {
        setSpeed(this.speed.clone().setY(speedY));
    }


}

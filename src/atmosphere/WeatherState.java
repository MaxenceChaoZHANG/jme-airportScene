package atmosphere;


import fastnoise.FastNoise;
import fastnoise.NoiseLayer;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.Vector2f;
import java.util.HashSet;
import java.util.Set;

/**
 * Controls and manipulates the weather of a skystate.
 */
public class WeatherState extends BaseAppState {

    private final Set<FloatValueListener> windListeners = new HashSet<>();
    //?
    private final NoiseLayer noiseLayer;
    private final NewAtmosphereState atmosphereState;
    // private final SkyState skyState;

    private boolean autoWeather = true;

    public WeatherState(NewAtmosphereState atmosphereState) {
        this.atmosphereState = atmosphereState;

        noiseLayer = new NoiseLayer();
        noiseLayer.setNoiseType(FastNoise.NoiseType.SimplexFractal);

        // add a wind listener for our clouds
        addWindListener((oldValue, newValue) -> atmosphereState.getCloudSettings().setWindSpeed(newValue));
    }

    public boolean isAutoWeather() { return autoWeather; }
    public void setAutoWeather(boolean autoWeather) { this.autoWeather = autoWeather; }

    public boolean addWindListener(FloatValueListener listener) { return this.windListeners.add(listener); }
    public boolean removeWindListener(FloatValueListener listener) { return this.windListeners.remove(listener); }

    @Override
    protected void initialize(Application app) {

    }

    @Override
    protected void cleanup(Application app) {
        this.windListeners.clear();
    }

    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

    }

    private final Vector2f weatherCoord = new Vector2f(0, 72);
    private float windLevel = 0;

    float time = 0;

    @Override
    public void update(float tpf) {

        if (!autoWeather) {
            return;
        }

        time += tpf;

        /*
        double value = atmosphereState.getCalendar().getYear()
                + atmosphereState.getCalendar().getDayInYear()
                + atmosphereState.getCalendar().getHour()
                + atmosphereState.getCalendar().getMinute()
                + atmosphereState.getCalendar().getSecond();

         */

        // so the thinking here is get a length of time and use that as the x-coord of the noise map.
        // +1 = very sunny. virtually zero wind.
        //  0 = average
        // -1 = stormy. high wind

        // float timescale = System.currentTimeMillis() * 0.1f;

        weatherCoord.setX(time);

        // noise goes from -1 to 1, we just want 0-1
        final float newWindLevel = (noiseLayer.evaluate(weatherCoord) + 1) * 0.5f;

        windListeners.forEach(listener -> listener.valueChanged(windLevel, newWindLevel));
        windLevel = newWindLevel;

        //System.out.println(windLevel);

        // clouds
        // very cloudy = -1 and not a cloud in the atmosphere = 1
        // which translates to 0.7 for very cloudy and 0.3 for not cloudy
        // float cloudiness = 1.0f - map(noise, -1, 1, 0.3f, 0.7f);
        //skyState.getCloudsDome().getConfig().setCover(cloudiness);

        //System.out.println("Noise: " + noise + " >> Cloudiness: " + cloudiness);

    }

    // switches one range to another.
    private float map(float value, float oldMin, float oldMax, float newMin, float newMax) {
        return ( ( (value - oldMin) * (newMax - newMin) ) / (oldMax - oldMin) ) + newMin;
    }

}

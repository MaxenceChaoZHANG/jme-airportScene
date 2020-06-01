package atmosphere;

import com.jme3.app.SimpleApplication;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.TranslucentBucketFilter;
import com.jme3.system.AppSettings;
import com.jme3.water.WaterFilter;


public class Main extends SimpleApplication {

    public static void main(String... args) {

        Main main = new Main();

        AppSettings appSettings = new AppSettings(true);
        // appSettings.setResolution(1280, 720);
        // appSettings.setFrameRate(120);
        appSettings.setTitle("Test :: Sky");

        main.setSettings(appSettings);
        main.setShowSettings(true);
        main.setPauseOnLostFocus(false);

        main.start();

    }

    private WaterFilter waterFilter;
    private NewAtmosphereState atmosphereState;

    @Override
    public void simpleInitApp() {

        cam.setFrustumFar(10000);
        flyCam.setMoveSpeed(50);
        flyCam.setDragToRotate(true);
        cam.setLocation(new Vector3f(0, 10, 0));

//         SkyState skyState = new SkyState();
        atmosphereState = new NewAtmosphereState(rootNode);
        stateManager.attach(atmosphereState);

//        WeatherState weatherState = new WeatherState(atmosphereState);
//        stateManager.attach(weatherState);
        
        
        
//        stateManager.attach(skyState);
        
        waterFilter = new WaterFilter();
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);

        fpp.addFilter(waterFilter);
        fpp.addFilter(new TranslucentBucketFilter());
        //viewPort.addProcessor(fpp);

    }



    @Override
    public void simpleUpdate(float tpf) {
//        waterFilter.setLightDirection(getStateManager().getState(SkyState.class).getSun().getDirection());

    }



}

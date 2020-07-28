package appState;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.effect.shapes.EmitterSphereShape;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.FogFilter;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;

import dataEntity.DataProcesor;
import dataEntity.WeatherData;



public class XplaneWeatherState extends BaseAppState {
	
	private ParticleEmitter snow;
	private ParticleEmitter hail;
	private ParticleEmitter rain;
	private float windFactor=0.05f;
	
//	private FilterPostProcessor fpp;
	
//	private ViewPort viewPort,leftViewPort,rightViewPort;
	private AssetManager assetManager;
	private Node rootNode;
//	private FogFilter fogFilter;
	
//	private EmitterBoxShape shape;
	private EmitterSphereShape sphere;
	
    private Timer timer;
    
    private ArrayList<WeatherData> data;
    private int currentDataIndex;
    private WeatherData currentData;
    private Boolean dataIsChanged;
    


	@Override
	protected void initialize(Application app) {
		// TODO Auto-generated method stub
		this.assetManager=app.getAssetManager();
//		this.viewPort=app.getViewPort();
		
//		if(app.getStateManager().getState(MultipleWindowState.class)!=null)
//		{
//		 leftViewPort=app.getStateManager().getState(MultipleWindowState.class).getLeftViewPort();
//		 rightViewPort=app.getStateManager().getState(MultipleWindowState.class).getRightViewPort();}

		this.rootNode=((SimpleApplication) app).getRootNode();
//		this.fpp = new FilterPostProcessor(assetManager);
		this.timer=new Timer("DataTimer");
		this.currentDataIndex=0;
		this.dataIsChanged=false;
		//��ȡ�������ݣ��õ���������
		try {
			this.data=DataProcesor.readWeatherData("weatherData/AllWeatherTest2.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//��ʼ����ʱ����ÿ10�������������
		timer.schedule(new TimerTask() {
            @Override
            public void run() {
            	currentDataIndex++;
            	if(currentDataIndex>data.size()-1)
            		currentDataIndex=0;
            	currentData=data.get(currentDataIndex);
            	dataIsChanged=true;
            }
        }, 1000,2000);//1000ms���ӳ�����ʱ�䣬10000ms�Ƕ�ʱ�������ڣ�ÿ10sִ��һ��
		
//		shape=new EmitterBoxShape(new Vector3f(10,10,10),new Vector3f(100,100,100));
		
		//��ʼ��������������
		this.sphere=new EmitterSphereShape(new Vector3f(0,0,0),100);
		//��ʼ������
		initRain();
		initSnow();
		initHail();
//		initFog();
	}
	
    @Override
    public void update(float tpf) {

        snow.setLocalTranslation(getApplication().getCamera().getLocation()); 
        hail.setLocalTranslation(getApplication().getCamera().getLocation()); 
        rain.setLocalTranslation(getApplication().getCamera().getLocation()); 
        
        if(dataIsChanged) {
        	
        	
          //���紵���ķ���ת��Ϊ�紵��ķ���
      	  float winddir=currentData.getWindDirection();
      	  if(winddir>180) {
      		  winddir=winddir-180; 
      	  }else {
      		  winddir=winddir-180+360;        		  
      	  } 
      	 //��ת��Ϊ��ÿ��
      	  float windspeed=currentData.getWindSpeed()*0.5144f;
      	  
        	//�¶ȴ����㣬��������㣬��Ϊ����
        	if(currentData.getTemperature()>0&&currentData.getPrecipitation()>0)
        	{
        	  //������Ĳ���
//        	 System.out.println("������"+currentData.getPrecipitation()*10000);
//        	 System.out.println("��ʼ�ٶ�"+new Vector3f(windspeed*FastMath.sin(winddir*FastMath.DEG_TO_RAD)*windFactor,-0.1f, -windspeed*FastMath.cos(winddir*FastMath.DEG_TO_RAD)*windFactor));
 	          rain.setParticlesPerSec(currentData.getPrecipitation()*10000);
        	  rain.getParticleInfluencer().setInitialVelocity(  new Vector3f(windspeed*FastMath.sin(winddir*FastMath.DEG_TO_RAD)*windFactor,-0.1f, -windspeed*FastMath.cos(winddir*FastMath.DEG_TO_RAD)*windFactor)  );
        	  //���������ӣ����Ƴ������͵�ѩ����
        	  if(!rootNode.hasChild(rain)) {
        	  rootNode.attachChild(rain);
        	  rootNode.detachChild(hail);
        	  rootNode.detachChild(snow);
        	  }
        	}
        	//�¶�С���㣬��������㣬��Ϊ��ѩ
        	if(currentData.getTemperature()<0&&currentData.getPrecipitation()>0)
        	{
              //����ѩ�Ĳ���
     	      snow.setParticlesPerSec(currentData.getPrecipitation()*10000);
              snow.getParticleInfluencer().setInitialVelocity(  new Vector3f(windspeed*FastMath.sin(winddir*FastMath.DEG_TO_RAD)*windFactor,-0.1f, -windspeed*FastMath.cos(winddir*FastMath.DEG_TO_RAD)*windFactor)  );
             //���ѩ�����ӣ����Ƴ��������������
              if(!rootNode.hasChild(snow)) {
              rootNode.attachChild(snow);
              rootNode.detachChild(hail);
              rootNode.detachChild(rain);
              }
        	}
        	//����s
        	if(currentData.getHail()>0)
        	{
     
              //����ѩ�Ĳ���
     	      hail.setParticlesPerSec(currentData.getHail()*10000);
              hail.getParticleInfluencer().setInitialVelocity(  new Vector3f(windspeed*FastMath.sin(winddir*FastMath.DEG_TO_RAD)*windFactor,-0.1f, -windspeed*FastMath.cos(winddir*FastMath.DEG_TO_RAD)*windFactor)  );
             //���ѩ�����ӣ����Ƴ��������������
              if(!rootNode.hasChild(hail)) {
              rootNode.attachChild(hail);
              rootNode.detachChild(snow);
              rootNode.detachChild(rain);
              }
        	}
        	dataIsChanged=false;
        	
        	
        }

    }
	
//	protected void initFog() {
//        fpp.setNumSamples(4);
//        viewPort.addProcessor(fpp);
//        fogFilter = new FogFilter(ColorRGBA.White, 0.7f, 10f); 
//        fpp.addFilter(fogFilter);	
//	}
	
	protected void initSnow() {
		snow = new ParticleEmitter("snow", ParticleMesh.Type.Triangle,5000);
		
		snow.setShape(sphere);
		snow.setParticlesPerSec(0);
		Material matsnow = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
		matsnow.setTexture("Texture", assetManager.loadTexture("Textures/Weather/snow.png"));
		snow.setMaterial(matsnow);
		snow.setImagesX(3);
		snow.setImagesY(2);
		snow.setSelectRandomImage(true);
		
		snow.setLowLife(15f);
		snow.setHighLife(17f);
		
		snow.setStartColor(new ColorRGBA(0,0,0,1)); 
		snow.setEndColor(new ColorRGBA(255f,255f,255f,0));
		
		snow.setStartSize(0.152f);
		snow.setEndSize(0.08f);
		
		//snow.getParticleInfluencer().setInitialVelocity(new Vector3f(windspeed*Math.cos(windddir),0f, windspeed*Math.sin(winddir)));
		snow.getParticleInfluencer().setInitialVelocity(new Vector3f(0,-0.1f,0));
		snow.getParticleInfluencer().setVelocityVariation(1f);
		
		snow.setGravity(0, 9.8f, 0);
		
//		rootNode.attachChild(snow);
		
	}
	
	protected void initRain() {
	    rain = new ParticleEmitter("rain", ParticleMesh.Type.Triangle,5000);
	    
        rain.setParticlesPerSec(0);
        rain.setShape(sphere);
        
        Material matrain = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        matrain.setTexture("Texture", assetManager.loadTexture("Textures/Weather/rain.png"));
        rain.setMaterial(matrain);
        rain.setImagesX(3);
        rain.setImagesY(3);
        rain.setSelectRandomImage(true);
        
        rain.setLowLife(15f);
        rain.setHighLife(17f);
        
        rain.setStartColor(new ColorRGBA(0,0,0,1)); 
		rain.setEndColor(new ColorRGBA(255f,255f,255f,0));
		
		rain.setStartSize(0.2f);
		rain.setEndSize(0.1f);
		//rain1.getParticleInfluencer().setInitialVelocity(new Vector3f(windspeed*Math.cos(windddir),0f, windspeed*Math.sin(winddir)));
		rain.getParticleInfluencer().setInitialVelocity(new Vector3f(0,-0.1f,0));
		rain.getParticleInfluencer().setVelocityVariation(1f);
		
		rain.setGravity(0, 9.8f, 0);
		
//		rootNode.attachChild(rain);

	}
	
	protected void initHail() {

        hail = new ParticleEmitter("hail", ParticleMesh.Type.Triangle,5000);
        hail.setShape(sphere);
        
	    hail.setParticlesPerSec(0);
	    
	    Material mathail = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
	    mathail.setTexture("Texture", assetManager.loadTexture("Textures/Weather/hail.png"));
	    hail.setMaterial(mathail);
	    hail.setImagesX(2);
	    hail.setImagesY(2);
	    hail.setSelectRandomImage(true);
	    
	    hail.setLowLife(15f);
	    hail.setHighLife(17f);
	    
	    hail.setStartColor(new ColorRGBA(0,0,0,1)); 
	    hail.setEndColor(new ColorRGBA(255f,255f,255f,0));
	    
	    hail.setStartSize(0.12f);
	    hail.setEndSize(0.08f);
	    //hail1.getParticleInfluencer().setInitialVelocity(new Vector3f(windspeed*Math.cos(windddir),0f, windspeed*Math.sin(winddir)));
	    hail.getParticleInfluencer().setInitialVelocity(new Vector3f(0,-0.1f,0));
	    hail.getParticleInfluencer().setVelocityVariation(1f);
	    hail.setGravity(0, 9.8f, 0);
	    
//        rootNode.attachChild(hail);		
	}
	

	@Override
	protected void onDisable() {
		// TODO Auto-generated method stub
		rootNode.detachChild(hail);
        rootNode.detachChild(snow);
        rootNode.detachChild(rain);

	}

	@Override
	protected void onEnable() {
		// TODO Auto-generated method stub

	}
	
	@Override
	protected void cleanup(Application app) {
		// TODO Auto-generated method stub

	}

}

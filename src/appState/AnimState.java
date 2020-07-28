package appState;

import java.io.IOException;
import java.util.ArrayList;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.asset.ModelKey;
import com.jme3.scene.Node;
import animation.AnimationBuilder;
import dataEntity.AirportInfo;
import dataEntity.Track;
import animation.AnimationBuilder;

public class AnimState extends BaseAppState {
	
	private AssetManager assetManager;
	private AnimationBuilder animationBuilder;
	private Node  rootNode; 
	
	private ArrayList<Track> tracks;
	//private String trackData="C:\\Users\\HP\\Desktop\\工作报告\\Track_de_1.txt";
	private String trackData="track/Track_de_1.txt";
	 //当前机场信息
	private AirportInfo currentAirportInfo;

	 //当前机场信息的get,set方法
	public AirportInfo getCurrentAirportInfo() {
		return currentAirportInfo;
	}
	//在AirportSetting中调用
	public void setCurrentAirportInfo(AirportInfo currentAirportInfo) {
		this.currentAirportInfo = currentAirportInfo;
		animationBuilder.setLonLat(currentAirportInfo.getLontitude(), currentAirportInfo.getLatitude());
	}

	
	@Override
	protected void initialize(Application arg0) {
		
	   this.assetManager=getApplication().getAssetManager();
	   this.animationBuilder=new AnimationBuilder();
	   this.rootNode=((SimpleApplication) getApplication()).getRootNode();
	   
	}
	
	public void playAnim() {
		
		animationBuilder.setLonLat(currentAirportInfo.getLontitude(), currentAirportInfo.getLatitude());
		
		readTrack();
		
		if(tracks!=null) {

			buildAnimations();
			for(int i=0;i<tracks.size();i++)
			{
				Track temp=tracks.get(i);			
				temp.setModel(assetManager.loadAsset(new ModelKey("Models/aircraft/jets/heavy/b737-800/delta/object.obj")));
				((Node) rootNode.getChild("Model")).attachChild(temp.getModel());
				temp.playAnim();
			}
		}

	}
	
	
	public void readTrack() {
		
		tracks=new ArrayList<Track>();
		try {
			tracks=pathSmooth.ReadData.readData(trackData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
    public void buildAnimations() {
    	System.out.println("进入buildAnimation");
    	for(int i=0;i<tracks.size();i++)
    	{
    		Track track=tracks.get(i);   		
    		animationBuilder.setTrackPoints(track.getTrackPoints());
    		track.setAnimation(animationBuilder.buildAnimation());
    	}
    }
    
    public void buildAnimation(int trackindex) {
    	
    		Track track=tracks.get(trackindex); 
    		if(track!=null) {
    		animationBuilder.setTrackPoints(  pathSmooth.DouglasPeuckerUtil.DouglasPeucker(track.getTrackPoints(),30) );
    		System.out.println(animationBuilder.trackPoints);
    		track.setAnimation(animationBuilder.buildAnimation());
    		}else {
    			System.out.println("索引号为："+trackindex+"的路径不存在");
    		}
    }
    
    
    
    

	@Override
	protected void onDisable() {
		

	}

	@Override
	protected void onEnable() {
		

	}
	
	@Override
	protected void cleanup(Application arg0) {
		

	}

}

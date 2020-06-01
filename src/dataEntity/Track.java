package dataEntity;

import java.util.ArrayList;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.Animation;
import com.jme3.scene.Spatial;

public class Track {

	private String aircraftAddress;//ICAO 24Î»·É»úµØÖ·Âë
	private String flightNumber;//º½°àºÅ
	private String transponderId;//4Î»Ó¦´ð»ú±àÂë
	private ArrayList<TrackPoint> trackPoints=new ArrayList<TrackPoint>();
	
	private Spatial model ;
	
	private Animation animation;
	
	private AnimControl animControl;
	private AnimChannel animChannel;
	
	public void setAnimation(Animation animation) {
		System.out.println("¶¯»­Ãû³Æ"+animation.getName());
		this.animation=animation;
		animControl = new AnimControl();
	    animControl.addAnim(this.animation);   
        animChannel=animControl.createChannel();     
	}
	
	public Animation getAnimation() {
		return animation;
	}
	
	//run animation
	public void playAnim() {
		if(animation!=null)		
        animChannel.setAnim("anim");
		
	}
	
	public void setModel(Spatial spatial) {
		this.model=spatial;
		model.addControl(animControl);
	}
	
	public Spatial getModel() {
		return model;
	}
	
	
	public ArrayList<TrackPoint> getTrackPoints() {
		return trackPoints;
	}
	public void setTrackPoints(ArrayList<TrackPoint> trackPoints) {
		this.trackPoints = trackPoints;
	}
	public String getAircraftAddress() {
		return aircraftAddress;
	}
	public void setAircraftAddress(String aircraftAddress) {
		this.aircraftAddress = aircraftAddress;
	}
	public String getFlightNumber() {
		return flightNumber;
	}
	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}
	public String getTransponderId() {
		return transponderId;
	}
	public void setTransponderId(String transponderId) {
		this.transponderId = transponderId;
	}
	
	public int getTrackpointSize() {
		return trackPoints.size();
	}
	
	public TrackPoint getTrackpointByIndex(int index) {
		return trackPoints.get(index);
	}
	
	public void addTrackpoint(TrackPoint trackpoint) {
		this.trackPoints.add(trackpoint);
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String str="º½°àºÅ£º"+flightNumber+",ICAOµØÖ·Âë£º"+aircraftAddress+",Ó¦´ð»ú±àÂë£º"+transponderId+"\n";
		for(int i=0;i<trackPoints.size();i++) {
			str+=trackPoints.get(i).toString();
		}
		return str;
	}
	@Override
	public boolean equals(Object obj) {
	    boolean flag = false;
	    if (obj instanceof Track) {
	        Track track = (Track) obj;
	        flag = (this.aircraftAddress.equals(track.aircraftAddress))
	        		&& (this.flightNumber.equals(track.flightNumber));
	    }
	    return flag;
	}
}

package dataEntity;

import java.util.ArrayList;

public class Road {
	
	private ArrayList<RoadPoint> roadPoints;
	private ArrayList<RoadWay> roadWays;
	
	public Road() {
		super();
		this.roadPoints = new ArrayList<RoadPoint>();
		this.roadWays = new ArrayList<RoadWay>();
	}
	
	public void addRoadPoint(RoadPoint roadPoint) {
		roadPoints.add(roadPoint);
		
	}
	
    public void addRoadWay(RoadWay roadWay) {
    	roadWays.add(roadWay);
	}
	
    
	public ArrayList<RoadPoint> getRoadPoints() {
		return roadPoints;
	}

	public ArrayList<RoadWay> getRoadWays() {
		return roadWays;
	}



}

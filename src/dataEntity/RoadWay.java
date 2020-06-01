package dataEntity;

import java.util.ArrayList;

public class RoadWay {
	
	private long id;
	private ArrayList<RoadEdge> roadEdges;

	public RoadWay(long id) {
		super();
		this.id = id;
		roadEdges=new ArrayList<RoadEdge>();
	}
	
	public void addRoadEdge(RoadEdge roadEdge) {
		roadEdges.add(roadEdge);
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public ArrayList<RoadEdge> getRoadEdges() {
		return roadEdges;
	}
	public void setRoadEdges(ArrayList<RoadEdge> roadEdges) {
		this.roadEdges = roadEdges;
	}
}

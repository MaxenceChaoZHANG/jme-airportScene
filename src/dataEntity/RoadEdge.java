package dataEntity;

public class RoadEdge {
	
	private long id1;
	private long id2;
	
	private RoadPoint p1;
	private RoadPoint p2;
	
	private double dist;
	
    public double getDist() {
		return dist;
	}


	public void computeDist() {
    	dist=Math.sqrt(
    			Math.pow(p1.getLatitude()-p2.getLatitude(),2)+
    			Math.pow(p1.getLongitude()-p2.getLongitude(),2));
//    	System.out.println(dist);
    }


	public RoadEdge() {
		super();
	}
	
	public RoadEdge(long id1, long id2) {
		super();
		this.id1 = id1;
		this.id2 = id2;
	}
	
	public RoadPoint getP1() {
		return p1;
	}

	public void setP1(RoadPoint p1) {
		this.p1 = p1;
	}

	public RoadPoint getP2() {
		return p2;
	}

	public void setP2(RoadPoint p2) {
		this.p2 = p2;
	}
	
	public long getId1() {
		return id1;
	}
	public void setId1(long id1) {
		this.id1 = id1;
	}
	
	public long getId2() {
		return id2;
	}
	public void setId2(long id2) {
		this.id2 = id2;
	}


}

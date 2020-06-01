package dataEntity;

public class RoadPoint {
	
	private double longitude;
	private double latitude;
	private String geoHash;
	private long id;
	
	public RoadPoint() {
		super();
	}
	
	public RoadPoint(long id,double longitude, double latitude) {
		super();
		this.id=id;
		this.longitude = longitude;
		this.latitude = latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	public String getGeoHash() {
		return geoHash;
	}

	public void setGeoHash(String geoHash) {
		this.geoHash = geoHash;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return longitude+" "+latitude;
	}

}

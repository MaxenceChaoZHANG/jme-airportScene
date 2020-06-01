package dataEntity;

public class AirportInfo implements Comparable<AirportInfo>{
	private int id;
	private String Name;
	private String ICAO_Code;
	private double Lontitude;
	private double Latitude;
	private String FilePath;
	
	public AirportInfo() {

	}

	public AirportInfo(int id, String name, String iCAO_Code, double lontitude, double latitude, String filePath) {
		super();
		this.id = id;
		Name = name;
		ICAO_Code = iCAO_Code;
		Lontitude = lontitude;
		Latitude = latitude;
		FilePath = filePath;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getICAO_Code() {
		return ICAO_Code;
	}
	public void setICAO_Code(String iCAO_Code) {
		ICAO_Code = iCAO_Code;
	}
	public double getLontitude() {
		return Lontitude;
	}
	public void setLontitude(double lontitude) {
		Lontitude = lontitude;
	}
	public double getLatitude() {
		return Latitude;
	}
	public void setLatitude(double latitude) {
		Latitude = latitude;
	}
	public String getFilePath() {
		return FilePath;
	}
	public void setFilePath(String filePath) {
		FilePath = filePath;
	}
	
	@Override
    public int compareTo(AirportInfo a) {
		int i=(this.ICAO_Code).compareTo(a.ICAO_Code);
		if(i>0)
        return 1;
		return -1;
    }
	
   @Override
   public String toString() {
//     return "Airport [ID=" + id +", Name=" + Name + ", ICAO_Code=" + ICAO_Code + ", Lontitude=" + Lontitude + ", Latitude=" + Latitude + ", FilePath=" + FilePath+ "]";
	   return   ICAO_Code +"---" +Name;
     }

	

}

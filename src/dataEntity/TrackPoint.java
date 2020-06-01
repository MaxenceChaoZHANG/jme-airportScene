package dataEntity;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TrackPoint implements Serializable{
	
	private double longitude;
	private double latitude;
	private double altitude;//ft
	private double heading;
	private double groundSpeed;//knots
	private double climbRate;//ft/min
	private double x;
	private double y;
	private Date date;
	
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
	
	public double getAltitude() {
		return altitude;
	}
	
	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}
	
	public double getHeading() {
		return heading;
	}
	public void setHeading(double heading) {
		this.heading = heading;
	}
	
	public double getGroundSpeed() {
		return groundSpeed;
	}
	public void setGroundSpeed(double groundSpeed) {
		this.groundSpeed = groundSpeed;
	}
	
	public double getClimbRate() {
		return climbRate;
	}
	public void setClimbRate(double climbRate) {
		this.climbRate = climbRate;
	}
	
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	public void setDate(String str) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    try {
	    	this.date = format.parse(str);
	    } catch (ParseException e) {
	        e.printStackTrace();
	    }
	}

	public String toString() {
		// TODO Auto-generated method stub
		 SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String str="时间："+formate.format(date)+"经度："+longitude+",纬度："+latitude+",高度："+altitude+",航向："+heading+",地速："+groundSpeed+",爬升率："+climbRate+"\n";
		 String str=longitude+"  "+latitude+"\n";
//		 String str=heading+"\n";
		 return str;
	}
	
	
	
	
}

package dataEntity;

public class WeatherData {
	private float pressure;//气压（单位：英寸汞柱）
	private float temperature;//温度（单位：摄氏度）
	private float windSpeed;//风速（单位：节）
	private float windDirection;//风向（单位：度。由北向南为0.0度，由西向东为270.0度）
	private float turbulence;//湍流量
	private float precipitation;//降雨量
	private float hail;//冰雹量
	
	public float getPressure() {
		return pressure;
	}
	public void setPressure(float pressure) {
		this.pressure = pressure;
	}
	public float getTemperature() {
		return temperature;
	}
	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}
	public float getWindSpeed() {
		return windSpeed;
	}
	public void setWindSpeed(float windSpeed) {
		this.windSpeed = windSpeed;
	}
	public float getWindDirection() {
		return windDirection;
	}
	public void setWindDirection(float windDirection) {
		this.windDirection = windDirection;
	}
	public float getTurbulence() {
		return turbulence;
	}
	public void setTurbulence(float turbulence) {
		this.turbulence = turbulence;
	}
	public float getPrecipitation() {
		return precipitation;
	}
	public void setPrecipitation(float precipitation) {
		this.precipitation = precipitation;
	}
	public float getHail() {
		return hail;
	}
	public void setHail(float hail) {
		this.hail = hail;
	}
	@Override
	public String toString() {
		return "WeatherData [pressure=" + pressure + ", temperature=" + temperature + ", windSpeed=" + windSpeed
				+ ", windDirection=" + windDirection + ", turbulence=" + turbulence + ", precipitation=" + precipitation
				+ ", hail=" + hail + "]";
	}

}

package dataEntity;

public class WeatherData {
	private float pressure;//��ѹ����λ��Ӣ�繯����
	private float temperature;//�¶ȣ���λ�����϶ȣ�
	private float windSpeed;//���٣���λ���ڣ�
	private float windDirection;//���򣨵�λ���ȡ��ɱ�����Ϊ0.0�ȣ�������Ϊ270.0�ȣ�
	private float turbulence;//������
	private float precipitation;//������
	private float hail;//������
	
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

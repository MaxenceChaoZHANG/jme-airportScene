package dayNightTest;


import com.jme3.math.Vector3f;

/**
 * ��Ϸ�е�ʱ��ϵͳ
 * @author yanmaoyuan
 *
 */
public class GameDate {

	// ���Ǽ�����Ϸ��ʱ���������ٶ�����ʵ��30����
	public final static int GAME_MINUTE = 2;// ��Ϸ1����	= ��ʵ2��
	public final static int GAME_HOUR = 120;// ��Ϸ1Сʱ	= 60��Ϸ���� = ��ʵ2����
	public final static int GAME_DAY = 2880;// ��Ϸ1��	= 24��ϷСʱ = ��ʵ48����
	public final static int GAME_MONTH = 86400;// ��Ϸ1�� =30��Ϸ�� = ��ʵ1��
	public final static int GAME_YEAR = 1036800;// ��Ϸ1�� = 12��Ϸ�� = ��ʵ12��


	// ����Ƴཻ��Ϊ23��26��
	private final static double ECLIPTIC_OBLIQUITY = Math.PI * 23.5f / 180f;
	// ���������γ��Ϊ30��N������ʱ����12��̫���߶�Ϊ60��
	private final static double WUHAN_LATITUDE = Math.PI * 60f / 180f;

	private final static double SEC_DEG = 2 * Math.PI / GAME_DAY;// ����ÿ��ת���ĽǶ�

	private final static double SIN_HOUR_DEG = Math.sin(Math.PI / 12);// �ճ�һСʱ�ĸ߶�

	private long totalSec;// ��Ϸ�ӿ�ʼ�������ܹ�����������

	private int year_sec;
	private int month_sec;
	private int date_sec;
	private int hour_sec;

	private int year;// ���>=0
	private int month;// �·� [0~11]
	private int date;// ���� [0~29]
	private int day;// һ���еĵڼ���[0~359]

	private int hour;// Сʱ[0~23]
	private int minute;// ����[0~59]

	private double alpha;// ���Ǽ���6�����ճ���������ʱ�������6���ӵ�λ�á�

	private double theta;// ���Ǽ���һ��ÿ��������12������ĸ߶�Ϊ��

	public GameDate() {
		totalSec = 0l;
		sunDirection = new Vector3f();

		updateTime();
	}
	public GameDate(long lastTime) {
		totalSec = lastTime;
		sunDirection = new Vector3f();

		updateTime();
	}

	public void update() {
		totalSec++;
		updateTime();
	}

	private void updateTime() {

		//////////// ������
		year_sec = (int) (totalSec % GAME_YEAR);// ��Ϸ�е�һ����˶����롣
		year = (int) (totalSec / GAME_YEAR);// �����˼����ˣ�
		day = year_sec / GAME_DAY;// ����һ��ĵڼ��죿

		month_sec = year_sec % GAME_MONTH;// ��Ϸ��һ���¹��˶�����
		month = year_sec/GAME_MONTH;// �����˼������ˣ�

		date_sec = month_sec % GAME_DAY;// ��Ϸ�е�һ����˶�����
		date = month_sec/GAME_DAY;// �����˼����ˣ�

		/////////////ʱ����
		hour_sec = date_sec % GAME_HOUR;// ��Ϸ�е�һСʱ���˶�����
		hour = date_sec / GAME_HOUR;// ������˼�Сʱ�ˣ�

		minute = (int) (hour_sec / GAME_MINUTE);// һСʱ���˼������ˣ�


		// ���Ǽ���6�����ճ���������ʱ�������6���ӵ�λ�á�
		alpha = SEC_DEG * (date_sec - GAME_HOUR * 6);// ����һ���ʱ�䣬����ʱ�ӵĽǶ�
		updateDayAndNight();

		theta = getTheta();// ���ݵ���ת�ĽǶȣ��������ո߶ȡ�
		updateSunDirection();
	}

	/**
	 * ��Ϸ�ӿ�ʼ�������ܹ�����������
	 * @return
	 */
	public long currentTimeInSecond() {
		return totalSec;
	}

	/**
	 * ����������̫���߶ȡ�̫��ÿ������ĸ߶ȶ���һ���������ת���仯��
	 */
	public double getTheta() {

		// �����Ǵ����µ��нڣ��������Ҫ����45��
		double year_angle = Math.PI * 2 * (day - 45) / 360;

		// �����ʵ�����սǶ�Ϊ
		this.theta = WUHAN_LATITUDE + ECLIPTIC_OBLIQUITY * Math.sin(year_angle);

		return theta;
	}
	private float lightPower;// ����ǿ��
	private boolean isDay;// �Ƿ��ǰ���
	public void updateDayAndNight() {
		// ��������ǿ��
		// �ճ�������ʱ��̫�������Ȼὥ�䣬���߶ȴﵽPI/6��ʱ������������ˡ�
		// ���ճ�ʱ����ǰ1��Сʱ��������ʱ���ƺ�1��Сʱ��
		lightPower = (float) ((Math.sin(alpha) + SIN_HOUR_DEG) * 2);
		if (lightPower > 1f)
			lightPower = 1f;

		if (lightPower < 0) {// ̫��������
			lightPower = 0f;
			if (isDay)
				isDay = false;// ��ҹ
		} else {
			if (!isDay)
				isDay = true;// ����
		}
	}
	public float getLightPower() {
		return lightPower;
	}
	public boolean isDay() {
		return isDay;
	}
	private Vector3f sunDirection;// ���սǶ�
	/**
	 * ������������սǶ�
	 */
	public void updateSunDirection() {
		double x = -Math.cos(alpha);
		double y = -Math.sin(alpha) * Math.sin(theta);
		double z = -Math.sin(alpha) * Math.cos(theta);

		sunDirection.set((float) x, (float) y, (float) z);
	}
	public Vector3f getSunDirection() {
		return sunDirection;
	}
	public int getYear() {
		return year;
	}
	public int getMonth() {
		return month;
	}
	public int getDate() {
		return date;
	}
	public int getHour() {
		return hour;
	}
	public int getMinute() {
		return minute;
	}
}


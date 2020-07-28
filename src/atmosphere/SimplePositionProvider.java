package atmosphere;

/*
 * Copyright (c) 2012, Andreas Olofsson
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;


/**
 * Provides simple positions for sun and moon, as well as the current moon-phase.
 * This system is efficient and allows the use of only one directional light.
 *
 * @author Andreas
 */
public class SimplePositionProvider implements PositionProvider  {
	
	// 地球黄赤交角为23°26′
   private final static float ECLIPTIC_OBLIQUITY = FastMath.PI * 23.5f / 180f;


    protected Vector3f sunDir = new Vector3f(0,1,0);
    protected Vector3f moonDir = new Vector3f(0,-1,0);

    // Defaults to 29 days which is about the length of a real synodic month.
    protected int daysInMoonCycle = 29;
    protected int moonDaysCount = 0;
    //7个月相（对应7个字符串）
    protected float moonPhase = 0;

    protected Calendar calendar;
    // The time when the sun position is at its highest (equal to declination).
    //正午时刻
    protected float solarNoon = 14;

    // Declination is the maximum height of the sun. A declination of 0
    // means the sun climbs all the way up to zenith. A value of 90 (degrees)
    // means the sun just floats around the horizon.
 // 假设世界的纬度为30°N，春分时中午12点太阳高度为60°
    protected float declination = 30; //Default 30 degrees.
    //Cosine and sine of the declination (in radians).
    protected float cDecRad, sDecRad;
    
    

    protected int oldHour;

    public SimplePositionProvider(){
        //Year, month, day, hour, minute, multiplier.
    	//Calendar(int year, int month, int day, int hour, int minute, float tMult)
        calendar = new Calendar(2020,2,2,18,0,1000000);
        //月亮周期29天，计算当月处于29天中哪一天
        moonDaysCount = calendar.getDayInYear() % daysInMoonCycle;
        updateMoonPhase();
        calcDecVals();
        update(0);
    }
    
    
    //单位变换
    //夹角正余弦计算
    private void calcDecVals(){
        // Angle in radians.
        float ang = (90 - declination)* FastMath.PI/180f;
        cDecRad = FastMath.cos(ang);
        sDecRad = FastMath.sin(ang);
    }
     /*
      * updateSunDirection()
        updateMoonDirection()
        updateMoonPhase()
      */
    @Override
    public void update(float tpf) {

        calendar.update(tpf);

        updateSunHeight();
        updateSunDirection();
        updateMoonDirection();

        int hour = calendar.getHour();

        // Doing this at mid day to avoid updating the moon texture during night.
        if(hour == 12 && oldHour != hour){
            moonDaysCount++;
            if(moonDaysCount > daysInMoonCycle){
                moonDaysCount = 1;
            }
            updateMoonPhase();

        }
        oldHour = hour;
    }
    
  //更新太阳高度角
    private void updateSunHeight() {
		// 春分是春三月的中节，因此日期要回退45天Angle in radians.
		float year_angle = FastMath.PI * 2 * (calendar.getDayInYear() - 79) / 365;
		// 世界的实际正午高度角为
        // Angle in radians.
        float ang = (90 - declination)* FastMath.PI/180f+ECLIPTIC_OBLIQUITY * FastMath.sin(year_angle);
        cDecRad = FastMath.cos(ang);
        sDecRad = FastMath.sin(ang);

    }
    
//提供月亮相对于天空穹的位置，并非光线方向
    private void updateMoonDirection() {

        int hour = calendar.getHour();
        float time = (float) (hour + calendar.getMinute()/60f);
        //-3/2*pi--pi/2
        float ang = time / 12f * FastMath.PI - FastMath.PI *0.5f;

        float sA = FastMath.sin(-ang);//区别
        moonDir.x = -FastMath.cos(-ang);//区别
        moonDir.y = sA*sDecRad;
        moonDir.z = -sA*cDecRad;
//        moonDir.x = FastMath.cos(-ang);//区别
//        moonDir.y = sA*sDecRad;
//        moonDir.z = sA*cDecRad;
    }
    
    
  //提供太阳光方向
    private void updateSunDirection() {

        int hour = calendar.getHour();
        float time = (float) (hour + calendar.getMinute()/60f);
        float ang = time / 12f * FastMath.PI - FastMath.PI *0.5f;

        float sA = FastMath.sin(ang);
//        sunDir.x = -FastMath.cos(ang);
//        sunDir.y = sA*cDecRad;
//        sunDir.z = -sA*sDecRad;
        sunDir.x = -FastMath.cos(ang);
        sunDir.y = -sA*sDecRad;
        sunDir.z = -sA*cDecRad;
    }

    protected void updateMoonPhase(){
    	//daysInMoonCycle=29
    	//初略估计在哪一个月相
        moonPhase =    (moonDaysCount % daysInMoonCycle) / (float)daysInMoonCycle * 7f   ;
    }

    @Override
    public Vector3f getSunDirection() {
        return sunDir;
    }

    @Override
    public Vector3f getMoonDirection() {
        return moonDir;
    }

    @Override
    public float getMoonPhase() {
        return moonPhase;
    }

    @Override
    public Calendar getCalendar() {
        return calendar;
    }

    /**
     * Set the number of days in a synodic month ("moon" month). This is
     * the time during which the moon will complete a full cycle.
     *
     * @param days
     *月亮的周期--phase1·	
     */
    public void setDaysInSynodicMonth(int days){
        this.daysInMoonCycle = days;
    }

    /**
     * Set the declination value of the sun. The value is between 0
     * and 90, and the lower it is, the higher the sun will climb.
     * A value of 0 means the sun will pass zenith(顶点) at solar noon.
     *
     * @param declination
     */
    public void setDeclination(float declination){
    	//将declination置于0-90
        this.declination = FastMath.clamp(declination,0,90);
        calcDecVals();
    }

    @Override
    public float getMaxHeight() {
        return sDecRad;
    }

    @Override
    public float getMinHeight() {
        return -sDecRad;
    }
    // Get the current moon phase as a string.
    public String getMoonPhaseVerbose(){
        String str;
        switch((int)moonPhase){
            case 0:
                str = "New Moon";
                break;
            case 1:
                str = "Waxing Crescent";//峨眉月
                break;
            case 2:
                str = "First Quarter Moon";
                break;
            case 3:
                str = "Waxing Gibbous";//盈凸月
                break;
            case 4:
                str = "Full Moon";
                break;
            case 5:
                str = "Waning Gibbous";//亏凸月
                break;
            case 6:
                str = "Third Quarter Moon";
                break;
            case 7:
                str = "Waning Crescent";//残月
                break;
            default:
                str = "No Moonphase!";
                break;

        }
        return str;
    }
}

package animation;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.vecmath.Vector3d;

import com.jme3.animation.Animation;
import com.jme3.animation.AnimationFactory;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

import dataEntity.AirportInfo;
import dataEntity.TrackPoint;
import pathSmooth.Mercator;

public class AnimationBuilder {

        private AnimationFactory animationFactory;
        public ArrayList<TrackPoint> trackPoints;
        private float animTime;
        
        //��ת�ؼ�֡��ر���
        private int rotationKeyFrames;
		protected float[] rotKeyFramesTimes;
        protected float[] keyFramesHeading;
        protected float[] keyFramesPitch;
        protected float[] keyFramesBank;
        //ƽ�ƹؼ�֡��ر���
        private int transKeyFrames;
        protected float[] transKeyFramesTimes;
        protected Vector3f[] keyFramesTranslation;

        
    	//����������
//    	float lon=117.350099184f;
//    	float lat=39.129502365f;
    	double lon;
    	double lat;
    	double[] temp;
    	
    	public void setLonLat(double lon,double lat) {
    		this.lon=lon;
    		this.lat=lat;
    		temp=Mercator.lonLat2Mercator(lon, lat);
    	}
    	
    	
        
        public AnimationBuilder() {

        }
        
        public AnimationBuilder(ArrayList<TrackPoint> trackPoints) {
        	this.trackPoints=trackPoints;
        }
        
        public void setTrackPoints(ArrayList<TrackPoint> trackPoints) {
        	this.trackPoints=trackPoints;
        }
        
        public Animation buildAnimation() {

        	computeTime();
        	computePosition();
        	computeRotation();
        	animationFactory = new AnimationFactory(animTime, "anim", 30);
        	
        	for(int i=0;i<rotationKeyFrames;i++) {
	        //Creating a rotation keyFrame
	        animationFactory.addTimeRotationAngles(rotKeyFramesTimes[i], keyFramesPitch[i],keyFramesHeading[i], keyFramesBank[i]);
        	}
        	
        	
        	for(int i=0;i<transKeyFrames;i++) {
	        //creating a translation keyFrame         
	        animationFactory.addTimeTranslation(transKeyFramesTimes[i], keyFramesTranslation[i]);
	        
        	}
        	
        	System.out.println("ԭʼ�켣������"+trackPoints.size());
        	System.out.println("λ�ƹؼ�֡����"+transKeyFrames);
        	System.out.println("��ת�ؼ�֡����"+rotationKeyFrames);
        	Animation animation= animationFactory.buildAnimation();
        	//�������
        	animationFactory=null;
//        	trackPoints.clear();
        	animTime=0;
        	rotationKeyFrames=0;
        	transKeyFrames=0;
        	rotKeyFramesTimes=null;
        	transKeyFramesTimes=null;
        	keyFramesTranslation=null;
        	keyFramesHeading=null;
            keyFramesPitch=null;
            keyFramesBank=null;
        	
        	return animation;
        }
        
        
        
        /**
         * ���㶯����ʱ��
         */
        protected void computeTime() {
        	    //���㶯����ʱ��
        		Long t1=trackPoints.get(0).getDate().getTime();
    			Long t2=trackPoints.get(trackPoints.size()-1).getDate().getTime();
    			animTime= (t2-t1)/1000.0f;//��
         }
        
        
        /**
         * ʹ���㷨����켣������ȡλ�ƹؼ�֡
         */
        protected void computePosition()  {
        	double xy[];
        	double longitude;
        	double latitude;
        	double altitude; 
        	//----------------------����translation�ؼ�֡����-------------------------------//
        	ArrayList<TrackPoint> copy=new ArrayList<TrackPoint>();
			try {
				copy = deepCopy(trackPoints);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
			    e.printStackTrace();
			}
			//ȥ��λ���ظ��� 
			System.out.println("ԭʼ");
			System.out.println(copy);
			copy=pathSmooth.RemoveDuplicatesUtil.RemoveDuplicates(copy);
			System.out.println("ȥ��");
			System.out.println(copy);
			//�Ƕ��ж��㷨���򻯹�켣
			copy=pathSmooth.AngleSimplifyUtil.DeleteErrorPoint(copy,70);
//			System.out.println("�۷��㴦��");
//			System.out.println(copy);
			
			//������˹����㷨���򻯹켣����ֵ��λm
			copy=pathSmooth.DouglasPeuckerUtil.DouglasPeucker(copy,20);
//			System.out.println("�켣�򻯴���");
//			System.out.println(copy);
        	
        	transKeyFrames=copy.size();
        	//����򻯺�ƽ��֡����Ͷ�Ӧ��ʱ�����鲢��ֵ
        	transKeyFramesTimes=new float[transKeyFrames];
        	keyFramesTranslation=new Vector3f[transKeyFrames];
        	//��һ���ؼ���ʱ��
        	Long t1=trackPoints.get(0).getDate().getTime();
        	
        	for(int k=0;k<transKeyFrames;k++) {
  			 latitude=copy.get(k).getLatitude();
  			 longitude= copy.get(k).getLongitude();
  			 xy=Mercator.lonLat2Mercator(longitude, latitude);//����ת��
  			 
  			 altitude= copy.get(k).getAltitude()*0.3048f;//Ӣ��ת��
  			 
//  			 System.out.println(xy[0]-temp[0]+","+(-xy[1]+temp[1]));
  			 
  			 keyFramesTranslation[k]=new Vector3f(  (float)(xy[0]-temp[0]),(float)altitude,(float)(-xy[1]+temp[1]));
  			 
  			 transKeyFramesTimes[k]=(copy.get(k).getDate().getTime()-t1)/1000.0f;
  			}
        	copy.clear();

         }

        
        /**
         * ��ȡ��ת�ؼ�֡
         * heading������pitch
         * ������bank����
         */
        protected void computeRotation() {
        	
        	//��һ���ؼ���ʱ��
        	Long t1=trackPoints.get(0).getDate().getTime();
        	
         	//----------------------����rotation�ؼ�֡����-------------------------------//
        	ArrayList<TrackPoint> copy=new ArrayList<TrackPoint>();
			try {
				copy = deepCopy(trackPoints);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			copy=pathSmooth.RotationSimplifyUtil.RotationSimplify(copy,1);
			rotationKeyFrames=copy.size();
        	//������ת֡���鲢��ֵ
            keyFramesHeading=new float[rotationKeyFrames];
            keyFramesPitch=new float[rotationKeyFrames];
            keyFramesBank=new float[rotationKeyFrames];
            rotKeyFramesTimes=new float[rotationKeyFrames];
            
//            System.out.println(rotationKeyFrames);
            
        	for(int k=0;k<rotationKeyFrames-1;k++) {
        		
        	 keyFramesHeading[k]=  (float) (- FastMath.DEG_TO_RAD*(copy.get(k).getHeading()));//ע�⸺��
        	 
//        	 System.out.println(keyFramesHeading[k]*FastMath.RAD_TO_DEG);
        	 
        	 keyFramesPitch[k]=(float) computePitch(copy.get(k), copy.get(k+1));
        	 
//        	 System.out.println("�����ǣ� "+keyFramesPitch[k]);
        	 
  			 keyFramesBank[k]=0.0f;
  			 rotKeyFramesTimes[k]=(copy.get(k).getDate().getTime()-t1)/1000.0f;
  			}

        	//���һ֡����
        	keyFramesHeading[rotationKeyFrames-1]= (float) (- FastMath.DEG_TO_RAD*(copy.get(rotationKeyFrames-1).getHeading()));//ע�⸺��
        	
       	    keyFramesPitch[rotationKeyFrames-1]=keyFramesPitch[rotationKeyFrames-2];
       	    
 			keyFramesBank[rotationKeyFrames-1]=0.0f;
 			
 			rotKeyFramesTimes[rotationKeyFrames-1]=(copy.get(rotationKeyFrames-1).getDate().getTime()-t1)/1000.0f;
 			
// 			System.out.println("--------------------------------");
 			
 			for(int k=0;k<rotationKeyFrames;k++) {
 				
// 				System.out.println(rotKeyFramesTimes[k]+","+FastMath.RAD_TO_DEG*keyFramesHeading[k]);
 				
 			}
 			
 			copy.clear();
         }
        

        /**
         * ����������Ƹ�����pitch
         */
        protected double computePitch(Vector3d v1,Vector3d v2) {
			return Math.atan(   (v2.y-v1.y)/Math.sqrt(   Math.pow(v2.x-v1.x, 2)+Math.pow(v2.z-v1.z, 2)  )   );
        }
        protected double computePitch(TrackPoint p1,TrackPoint p2) {
        	double longitude;
        	double latitude;
        	double altitude; 
        	double xy[];
        	Vector3d v1;
			Vector3d v2;
        	
        	latitude=p1.getLatitude();
 			longitude= p1.getLongitude();
 			xy=Mercator.lonLat2Mercator(longitude, latitude);//����ת�� 
 			altitude= p1.getAltitude()*0.3048f;//Ӣ��ת��
 			v1=new Vector3d(xy[0]-temp[0],altitude,-xy[1]+temp[1]);
 			
 			latitude=p2.getLatitude();
 			longitude= p2.getLongitude();
 			xy=Mercator.lonLat2Mercator(longitude, latitude);//����ת�� 
 			altitude= p2.getAltitude()*0.3048f;//Ӣ��ת��
 			v2=new Vector3d(xy[0]-temp[0],altitude,-xy[1]+temp[1]);
 			
 			
			return computePitch(v1,v2);
        }
        
 
        /**
         * ʹ�����л��ֶΣ���ɹ켣��ĸ���
         */
        public static <T> ArrayList<T> deepCopy(ArrayList<T> src) throws IOException, ClassNotFoundException {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(src);

            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteIn);

            ArrayList<T> copy_list = (ArrayList<T>) in.readObject();
            return copy_list;
        }


}


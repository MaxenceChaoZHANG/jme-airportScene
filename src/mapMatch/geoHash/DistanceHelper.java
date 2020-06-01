package mapMatch.geoHash;

import pathSmooth.Mercator;

public class DistanceHelper {
    private final static double Earth_Radius = 6378.137f;

    public static double distance(double lat1, double lng1, double lat2, double lng2) {
     
       	double[] xy1=Mercator.lonLat2Mercator(lng1, lat1);
    	double[] xy2=Mercator.lonLat2Mercator(lng2, lat2);
    	double distance= Math.sqrt(Math.pow(xy1[0]-xy2[0],2)+Math.pow(xy1[1]-xy2[1],2));
		return distance;    
		
//		double x1 = Math.cos(lat1) * Math.cos(lng1);
//        double y1 = Math.cos(lat1) * Math.sin(lng1);
//        double z1 = Math.sin(lat1);
//        double x2 = Math.cos(lat2) * Math.cos(lng2);
//        double y2 = Math.cos(lat2) * Math.sin(lng2);
//        double z2 = Math.sin(lat2);
//        double lineDistance =
//                Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) + (z1 - z2) * (z1 - z2));
//        double realDistance = Earth_Radius * Math.PI * 2 * Math.asin(0.5 * lineDistance) / 180;
//        return realDistance;
    }

    public static void main(String[] args) {
        System.out.println(DistanceHelper.distance(39.992907, 116.391728, 39.985336, 116.37736));
    }
}

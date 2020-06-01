package mapMatch.geoHash;

import java.util.ArrayList;
import java.util.List;

public class MyGeoHashHelper {
	
	
	public static double Max_Lat = 90;
    public static double Min_Lat = -90;
    public static double Max_Lng = 180;
    public static double Min_Lng = -180;
    public static int length = 20;
    private static double latUnit = (Max_Lat - Min_Lat) / (1 << 20);//����20λ
    private static double lngUnit = (Max_Lng - Min_Lng) / (1 << 20);
    private final String[] base32Lookup =
            {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "b", "c", "d", "e", "f", "g", "h",
                    "j", "k", "m", "n", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};

    
    /**
     *�ݹ�����01����
     * @param str
     * @return
     */
    private void convert(double min, double max, double value, List<Character> list) {
        if (list.size() > (length - 1)) {
            return;
        }
        double mid = (max + min) / 2;
        if (value < mid) {
            list.add('0');
            convert(min, mid, value, list);
        } else {
            list.add('1');
            convert(mid, max, value, list);
        }
    }

    /**
     *����01���н��б���
     * @param str 01����
     * @return
     */
    private String base32Encode(final String str) {
        String unit = "";
        StringBuilder sb = new StringBuilder();
        for (int start = 0; start < str.length(); start = start + 5) {
            unit = str.substring(start, start + 5);
            //����ʮ����ת���������base32lookup�в����ַ�
            sb.append(base32Lookup[convertToIndex(unit)]);
        }
        return sb.toString();
    }
   /**
    * 5λ������תΪʮ������
    * @param str ������
    * @return ʮ������
    */
    private int convertToIndex(String str) {
        int length = str.length();
        int result = 0;
        for (int index = 0; index < length; index++) {
            result += str.charAt(index) == '0' ? 0 : 1 << (length - 1 - index);
        }
        return result;
    }

    /**
     * ���뺯��,γ��20λ���룬����20λ����
     * @param 
     * @return
     */
    public String encode(double lat, double lng) {
        List<Character> latList = new ArrayList<Character>();
        List<Character> lngList = new ArrayList<Character>();
        length=20;
        convert(Min_Lat, Max_Lat, lat, latList);
        convert(Min_Lng, Max_Lng, lng, lngList);
        StringBuilder sb = new StringBuilder();
        for (int index = 0; index < latList.size(); index++) {
        	//��γ�ȵı���ϲ�,����Ϊ��γ�ȣ�ż��Ϊ�Ǿ���
            sb.append(lngList.get(index)).append(latList.get(index));
        }
        return base32Encode(sb.toString());
    }
    /**
     * ���뺯��,γ��17λ���룬����18λ����
     * @param 
     * @return
     */
    public String encodeWith7Char(double lat, double lng) {
        List<Character> latList = new ArrayList<Character>();
        List<Character> lngList = new ArrayList<Character>();
        length=17;
        convert(Min_Lat, Max_Lat, lat, latList);
        length=18;
        convert(Min_Lng, Max_Lng, lng, lngList);
        StringBuilder sb = new StringBuilder();
        for (int index = 0; index < latList.size(); index++) {
        	//��γ�ȵı���ϲ�,����Ϊ��γ�ȣ�ż��Ϊ�Ǿ���
            sb.append(lngList.get(index)).append(latList.get(index));
        }
        sb.append(lngList.get(latList.size()));
        return base32Encode(sb.toString());
    }

    
    /**
     * ������Χ��geoHash,γ��20λ���룬����20λ����
     * @param 
     * @return
     */
    public List<String> around(double lat, double lng) {
    	latUnit = (Max_Lat - Min_Lat) / (1 << 20);//����20λ
    	lngUnit = (Max_Lng - Min_Lng) / (1 << 20);
        List<String> list = new ArrayList<String>();
        list.add(encode(lat, lng));
        list.add(encode(lat + latUnit, lng));
        list.add(encode(lat - latUnit, lng));
        list.add(encode(lat, lng + lngUnit));
        list.add(encode(lat, lng - lngUnit));
        list.add(encode(lat + latUnit, lng + lngUnit));
        list.add(encode(lat + latUnit, lng - lngUnit));
        list.add(encode(lat - latUnit, lng + lngUnit));
        list.add(encode(lat - latUnit, lng - lngUnit));
        return list;
    }
    
    /**
     * ������Χ��geoHash,γ��17λ���룬����18λ����
     * @param 
     * @return
     */
    public List<String> aroundWith7Char(double lat, double lng) {
    	latUnit = (Max_Lat - Min_Lat) / (1 << 17);//����20λ
    	lngUnit = (Max_Lng - Min_Lng) / (1 << 18);
        List<String> list = new ArrayList<String>();
        list.add(encodeWith7Char(lat, lng));
        list.add(encodeWith7Char(lat + latUnit, lng));
        list.add(encodeWith7Char(lat - latUnit, lng));
        list.add(encodeWith7Char(lat, lng + lngUnit));
        list.add(encodeWith7Char(lat, lng - lngUnit));
        list.add(encodeWith7Char(lat + latUnit, lng + lngUnit));
        list.add(encodeWith7Char(lat + latUnit, lng - lngUnit));
        list.add(encodeWith7Char(lat - latUnit, lng + lngUnit));
        list.add(encodeWith7Char(lat - latUnit, lng - lngUnit));
        return list;
    }

}

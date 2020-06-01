package mapMatch.geoHash;

import java.util.ArrayList;
import java.util.List;

public class GeoHashHelper {
    public final double Max_Lat = 90;
    public final double Min_Lat = -90;
    public final double Max_Lng = 180;
    public final double Min_Lng = -180;
    private final int length = 20;
    private final double latUnit = (Max_Lat - Min_Lat) / (1 << 20);//左移20位
    private final double lngUnit = (Max_Lng - Min_Lng) / (1 << 20);
    private final String[] base32Lookup =
            {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "b", "c", "d", "e", "f", "g", "h",
                    "j", "k", "m", "n", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};

    
    /**
     *递归生成01序列
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
     *根据01序列进行编码
     * @param str 01序列
     * @return
     */
    private String base32Encode(final String str) {
        String unit = "";
        StringBuilder sb = new StringBuilder();
        for (int start = 0; start < str.length(); start = start + 5) {
            unit = str.substring(start, start + 5);
            //根据十进制转换结果，在base32lookup中查找字符
            sb.append(base32Lookup[convertToIndex(unit)]);
        }
        return sb.toString();
    }
   /**
    * 5位二进制转为十进制数
    * @param str 二进制
    * @return 十进制数
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
     * 编码函数
     * @param 
     * @return
     */
    public String encode(double lat, double lng) {
        List<Character> latList = new ArrayList<Character>();
        List<Character> lngList = new ArrayList<Character>();
        convert(Min_Lat, Max_Lat, lat, latList);
        convert(Min_Lng, Max_Lng, lng, lngList);
        StringBuilder sb = new StringBuilder();
        for (int index = 0; index < latList.size(); index++) {
        	//经纬度的编码合并,奇数为是纬度，偶数为是经度
            sb.append(lngList.get(index)).append(latList.get(index));
        }
        return base32Encode(sb.toString());
    }

    
    /**
     * 生成周围的geoHash
     * @param 
     * @return
     */
    public List<String> around(double lat, double lng) {
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
}
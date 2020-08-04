package mapMatch;

    class Program
    {

    	static double EARTH_RADIUS = 6371.0;//km ����뾶 ƽ��ֵ��ǧ��
    	
        public static double HaverSin(double theta)
        {
            double v = Math.sin(theta / 2);
            return v * v;
        }
        // <summary>
        // �����ľ���1��γ��1������2��γ��2. ����2����γ��֮��ľ��롣
        // </summary>
        // <param name="lat1">����1</param>
        // <param name="lon1">γ��1</param>
        // <param name="lat2">����2</param>
        // <param name="lon2">γ��2</param>
        // <returns>���루���ǧ�ף�</returns>
        public static double Distance(double lat1,double lon1, double lat2,double lon2)
        {
            //��haversine��ʽ�������������ľ��롣
            //��γ��ת���ɻ���
            lat1 = ConvertDegreesToRadians(lat1);
            lon1 = ConvertDegreesToRadians(lon1);
            lat2 = ConvertDegreesToRadians(lat2);
            lon2 = ConvertDegreesToRadians(lon2);

            //��ֵ
            double vLon = Math.abs(lon1 - lon2);
            double vLat = Math.abs(lat1 - lat2);
            //h is the great circle distance in radians, great circle����һ�������ϵ����棬����Բ�ļ������ĵ�һ���ܳ�����Բ��
            double h = HaverSin(vLat) + Math.cos(lat1) * Math.cos(lat2) * HaverSin(vLon);

            double distance = 2 * EARTH_RADIUS * Math.asin(Math.sqrt(h));

            return distance;
        }

        //<summary>
        //���ǶȻ���Ϊ���ȡ�
        // </summary>
        // <param name="degrees">�Ƕ�</param>
        // <returns>����</returns>
        public static double ConvertDegreesToRadians(double degrees)
        {
            return degrees * Math.PI / 180;
        }

        public static double ConvertRadiansToDegrees(double radian)
        {
            return radian * 180.0 / Math.PI;
        }

    }

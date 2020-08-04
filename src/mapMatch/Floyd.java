package mapMatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.dom4j.DocumentException;

import dataEntity.Road;
import dataEntity.RoadEdge;
import dataEntity.RoadPoint;
import dataEntity.RoadWay;
import mapMatch.road.RoadXMLUtility;

public class Floyd {

	public static void main(String[] args) {

		Floyd graph = new Floyd();
		// ���ø��������㷨
		graph.floyd();
//		graph.show();
		System.out.println(graph.findTheRoad(500, 500));
	}

	private Road road;// ��ȡ·������
	private ArrayList<RoadPoint> roadPoints;
	private ArrayList<RoadWay> roadWays;
	private ArrayList<RoadEdge> roadEdges;

	private long[] vertex; // ��Ŷ��������
	private ArrayList<Long> vertexList = new ArrayList<Long>();
	private double[][] dis; // ���棬�Ӹ��������������������ľ��룬���Ľ����Ҳ�Ǳ����ڸ�����
	private int[][] pre;// ���浽��Ŀ�궥���ǰ������

	private double INF = java.lang.Double.MAX_VALUE;

	public Floyd(int length, double[][] matrix, long[] vertex) {
		this.vertex = vertex;
		this.dis = matrix;
		this.pre = new int[length][length];
		// ��pre�����ʼ��, ע���ŵ���ǰ��������±�
		for (int i = 0; i < length; i++) {
			Arrays.fill(pre[i], i);
		}
	}

	public Floyd(ArrayList<RoadPoint> roadPoints, ArrayList<RoadEdge> roadEdges) {
		// ��ȡ·������
		this.roadPoints = roadPoints;
		this.roadEdges = roadEdges;
		// ��ʼ��vertex�����dis,pre�������
		init();
	}

	public Floyd() {
		// ��ȡ·������
		road = null;
		try {
			road = RoadXMLUtility.ReadXML();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		roadPoints = road.getRoadPoints();
		roadWays = road.getRoadWays();
		// ��RoadWay�г�ȡ���е�RoadEdge
		roadEdges = new ArrayList<RoadEdge>();
		int wayNumber = roadWays.size();
		for (int i = 0; i < wayNumber; i++) {
			roadEdges.addAll(roadWays.get(i).getRoadEdges());
		}
		// ��ʼ��vertex�����dis,pre�������
        init();
	}

	/** ��ʼ��vertex�����dis,pre�������
	 * 
	 */
	public void init() {
		// ��ʼ��vertex����
		int pointNumber = roadPoints.size();
		vertex = new long[pointNumber];
		for (int i = 0; i < pointNumber; i++) {
			vertex[i] = roadPoints.get(i).getId();
		}
		// ��ʼ��dis����
		int edgesNumber = roadEdges.size();
		for (long s : vertex) {
			vertexList.add(s);
		}
		dis = new double[pointNumber][pointNumber];
		
		// �Ծ����������ݽ��г�ʼ��
		for (int i = 0; i < pointNumber; i++) {
			for (int j = 0; j < pointNumber; j++) {
				if (i == j) {
					dis[i][j] =  0;
				}else {
					dis[i][j] =  INF;
				}
			}
		}

		RoadEdge tempEdge = null;
		int m = 0, n = 0;
		// ���ݱ߳�ʼ������
		for (int i = 0; i < edgesNumber; i++) {
			tempEdge = roadEdges.get(i);
			if (vertexList.contains(tempEdge.getId1())) {
				m = vertexList.indexOf(tempEdge.getId1());
			}
			if (vertexList.contains(tempEdge.getId2())) {
				n = vertexList.indexOf(tempEdge.getId2());
			}
			dis[m][n] = tempEdge.getDist();
			dis[n][m] = tempEdge.getDist();
		}

		// ��ʼ��pre����
		pre = new int[pointNumber][pointNumber];
		for (int i = 0; i < pointNumber; i++) {
			Arrays.fill(pre[i], i);
		}


	}

	/**
	 * ��ʾpre�����dis����
	 * 
	 */
	public void show() {
		// Ϊ����ʾ�����Ķ��������Ż�һ�����
		for (int k = 0; k < dis.length; k++) {
			// �Ƚ�pre���������һ��
//			for (int i = 0; i < dis.length; i++) {
//				System.out.print(vertex[pre[k][i]] + " ");
//			}
//			System.out.println();
			// ���dis�����һ������
			for (int i = 0; i < dis.length; i++) {
				if (dis[k][i] == INF)
					continue;
				System.out.println("(" + vertex[k] + "��" + vertex[i] + "�����·����" + dis[k][i] + ") ");
//				System.out.print( dis[k][i] +" ");
			}
//			System.out.println();
//			System.out.println();
		}
	}
	/**
	 * Ѱ�Ҿ������start����ӽ�s�������㣬�����±�
	 * @return 
	 * 
	 */
    public  ArrayList<Integer> findNearstPoint(int start,double s) {
     	ArrayList<Integer> result =new ArrayList<Integer>();
     	
     	ArrayList<Double> result1 =new ArrayList<Double>();
     	
    	for(int i=0;i<dis.length;i++) {
    		if(dis[start][i]>s) {
    			result1.add(dis[start][i]);
    		}
    	}
    	Collections.sort(result1);
    	
    	for(int i=0;i<result1.size();i++) {
    		for(int j=0;j<dis.length;j++) {
        		if(dis[start][j]==result1.get(i)) {
        			result.add(j);
        			break;
        		}
        	}	
    	}
    	
		return result;
    }
    
    public ArrayList<Double> findNearstDis(int start,double s) {
    	ArrayList<Double> result =new ArrayList<Double>();
 	
    	for(int i=0;i<dis.length;i++) {
    		if(dis[start][i]>s) {
    			result.add(dis[start][i]);
    		}
    	}
//    	System.out.println(result.get(0)>result.get(1));
    	Collections.sort(result);
    	
		return result;
    }
    
    
    
    public double findDis(int start,int end) {
    	return dis[start][end];
    }
    /**
	 * ����������������㷨��·��
	 * 
	 * @param start ��ʼ�㣬ע��������Ǵ��㿪ʼ������ ����� 0��3
	 * @param end   �����㣬Ҳ�Ǵ��㿪ʼ������
	 * @return List���� ���ص�ֵҲ�Ǵ��㿪ʼ�����ġ�
	 */
	public ArrayList<Integer> findTheRoad(int start, int end) {

		ArrayList<Integer> indexList = new ArrayList<Integer>();
		indexList.add(start);
		int temp = pre[end][start];
		while (temp != end) {
			indexList.add(temp);
			temp = pre[end][temp];
		}
		indexList.add(end);

		return indexList;
	}
	public ArrayList<RoadPoint> findTheRoad(RoadPoint start, RoadPoint end) {

		ArrayList<RoadPoint> list = new ArrayList<RoadPoint>();

		ArrayList<Integer> indexList = findTheRoad(roadPoints.indexOf(start), roadPoints.indexOf(end));

		for (int i = 0; i < indexList.size(); i++) {
			list.add(roadPoints.get(indexList.get(i)));
		}
		return list;
	}

	/**
	 * ���������㷨, �Ƚ�������⣬��������ʵ��
	 * 
	 */
	public void floyd() {
		double len = 0; // �����������
		// ���м䶥������� k �����м䶥����±� [A, B, C, D, E, F, G]
		for (int k = 0; k < dis.length; k++) { //
			// ��i���㿪ʼ���� [A, B, C, D, E, F, G]
			for (int i = 0; i < dis.length; i++) {
				// ����j���� // [A, B, C, D, E, F, G]
				if (dis[i][k] == INF)
					continue;
				for (int j = 0; j <= i; j++) {
					// => �����i ������������� k�м䶥�㣬���� j �������
					if (dis[k][j] == INF)
						continue;
					len = dis[i][k] + dis[k][j];
					if (len < dis[i][j]) {// ���lenС�� dis[i][j]
						dis[i][j] = len;// ���¾���
						dis[j][i] = len;
						pre[i][j] = pre[k][j];
						pre[j][i] = pre[k][i];// ����ǰ������

					}
				}
			}
		}
	}
	
}

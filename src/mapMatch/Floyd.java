package mapMatch;

import java.util.ArrayList;
import java.util.Arrays;

import org.dom4j.DocumentException;

import dataEntity.Road;
import dataEntity.RoadEdge;
import dataEntity.RoadPoint;
import dataEntity.RoadWay;
import mapMatch.road.RoadXMLUtility;

public class Floyd {

	public static void main(String[] args) {

		Floyd graph = new Floyd();
		// 调用弗洛伊德算法
		graph.floyd();
//		graph.show();
		System.out.println(graph.findTheRoad(500, 500));
	}

	private Road road;// 读取路网数据
	private ArrayList<RoadPoint> roadPoints;
	private ArrayList<RoadWay> roadWays;
	private ArrayList<RoadEdge> roadEdges;

	private long[] vertex; // 存放顶点的数组
	private ArrayList<Long> vertexList = new ArrayList<Long>();
	private double[][] dis; // 保存，从各个顶点出发到其它顶点的距离，最后的结果，也是保留在该数组
	private int[][] pre;// 保存到达目标顶点的前驱顶点

	private double INF = java.lang.Double.MAX_VALUE;

	public Floyd(int length, double[][] matrix, long[] vertex) {
		this.vertex = vertex;
		this.dis = matrix;
		this.pre = new int[length][length];
		// 对pre数组初始化, 注意存放的是前驱顶点的下标
		for (int i = 0; i < length; i++) {
			Arrays.fill(pre[i], i);
		}
	}

	public Floyd(ArrayList<RoadPoint> roadPoints, ArrayList<RoadEdge> roadEdges) {
		// 获取路网数据
		this.roadPoints = roadPoints;
		this.roadEdges = roadEdges;
		// 初始化vertex数组和dis,pre矩阵参数
		init();
	}

	public Floyd() {
		// 获取路网数据
		road = null;
		try {
			road = RoadXMLUtility.ReadXML();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		roadPoints = road.getRoadPoints();
		roadWays = road.getRoadWays();
		// 从RoadWay中抽取所有的RoadEdge
		roadEdges = new ArrayList<RoadEdge>();
		int wayNumber = roadWays.size();
		for (int i = 0; i < wayNumber; i++) {
			roadEdges.addAll(roadWays.get(i).getRoadEdges());
		}
		// 初始化vertex数组和dis,pre矩阵参数
        init();
	}

	/** 初始化vertex数组和dis,pre矩阵参数
	 * 
	 */
	public void init() {
		// 初始化vertex参数
		int pointNumber = roadPoints.size();
		vertex = new long[pointNumber];
		for (int i = 0; i < pointNumber; i++) {
			vertex[i] = roadPoints.get(i).getId();
		}
		// 初始化dis矩阵
		int edgesNumber = roadEdges.size();
		for (long s : vertex) {
			vertexList.add(s);
		}
		dis = new double[pointNumber][pointNumber];
		
		// 对矩阵其他数据进行初始化
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
		// 根据边初始化矩阵
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

		// 初始化pre参数
		pre = new int[pointNumber][pointNumber];
		for (int i = 0; i < pointNumber; i++) {
			Arrays.fill(pre[i], i);
		}

	}

	/**
	 * 显示pre数组和dis数组
	 * 
	 */
	public void show() {
		// 为了显示便于阅读，我们优化一下输出
		for (int k = 0; k < dis.length; k++) {
			// 先将pre数组输出的一行
//			for (int i = 0; i < dis.length; i++) {
//				System.out.print(vertex[pre[k][i]] + " ");
//			}
//			System.out.println();
			// 输出dis数组的一行数据
			for (int i = 0; i < dis.length; i++) {
				if (dis[k][i] == INF)
					continue;
				System.out.println("(" + vertex[k] + "到" + vertex[i] + "的最短路径是" + dis[k][i] + ") ");
//				System.out.print( dis[k][i] +" ");
			}
//			System.out.println();
//			System.out.println();
		}
	}
	/**
	 * 寻找距离起点start距离接近s的两个点，返回下标
	 * 
	 */
    public int[] findNearstPoint(int start,double s) {
    	double  d1=0; //存储较大的距离值
    	double	d2=0;
    	int result1=-1;//存储较大的距离值对应的索引
    	int result2=-1;
    	
    	for(int i=0;i<dis.length;i++) {
    		if(dis[start][i]>s) {
    			if(d1==0) {
    				d1=dis[start][i];
    				result1=i;
    				continue;
    			}
    			if(d2==0) {
    				d2=dis[start][i];
    				result2=i;
    				continue;
    			}
    			if(d1<d2) {
    				double temp=d1;
    				int tempIndex=result1;
    				d1=d2;
    				result1=result2;
    				d2=temp;
    				result2=tempIndex;
    			}
    			//如果小于d1
    			if(dis[start][i]<d1) {
    				d1=dis[start][i];
    				result1=i;
    			}
    		}
    	}
    	//确保d1>d2
    	if(d1<d2) {
			double temp=d1;
			int tempIndex=result1;
			d1=d2;
			result1=result2;
			d2=temp;
			result2=tempIndex;
		}
    	
		return new int[] {result1,result2};
    }
    
    public double[] findNearstDis(int start,double s) {
    	double  d1=0; //存储较大的距离值
    	double	d2=0;
    	int result1=-1;//存储较大的距离值对应的索引
    	int result2=-1;
    	
    	for(int i=0;i<dis.length;i++) {
    		if(dis[start][i]>s) {
    			if(d1==0) {
    				d1=dis[start][i];
    				result1=i;
    				continue;
    			}
    			if(d2==0) {
    				d2=dis[start][i];
    				result2=i;
    				continue;
    			}
    			if(d1<d2) {
    				double temp=d1;
    				int tempIndex=result1;
    				d1=d2;
    				result1=result2;
    				d2=temp;
    				result2=tempIndex;
    			}
    			//如果小于d1,当前d1舍去
    			if(dis[start][i]<d1) {
    				d1=dis[start][i];
    				result1=i;
    			}
    		}
    	}
    	//确保d1>d2
    	if(d1<d2) {
			double temp=d1;
			int tempIndex=result1;
			d1=d2;
			result1=result2;
			d2=temp;
			result2=tempIndex;
		}
    	
		return new double[] {d1,d2};
    }
    
    
    
    public double findDis(int start,int end) {
    	return dis[start][end];
    }
    /**
	 * 用于输出弗洛伊德算法的路径
	 * 
	 * @param start 开始点，注意这个点是从零开始计数的 例如从 0到3
	 * @param end   结束点，也是从零开始计数的
	 * @return List数组 返回的值也是从零开始计数的。
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
	 * 弗洛伊德算法, 比较容易理解，而且容易实现
	 * 
	 */
	public void floyd() {
		double len = 0; // 变量保存距离
		// 对中间顶点遍历， k 就是中间顶点的下标 [A, B, C, D, E, F, G]
		for (int k = 0; k < dis.length; k++) { //
			// 从i顶点开始出发 [A, B, C, D, E, F, G]
			for (int i = 0; i < dis.length; i++) {
				// 到达j顶点 // [A, B, C, D, E, F, G]
				if (dis[i][k] == INF)
					continue;
				for (int j = 0; j <= i; j++) {
					// => 求出从i 顶点出发，经过 k中间顶点，到达 j 顶点距离
					if (dis[k][j] == INF)
						continue;
					len = dis[i][k] + dis[k][j];
					if (len < dis[i][j]) {// 如果len小于 dis[i][j]
						dis[i][j] = len;// 更新距离
						dis[j][i] = len;
						pre[i][j] = pre[k][j];
						pre[j][i] = pre[k][j];// 更新前驱顶点
					}
				}
			}
		}
	}
}

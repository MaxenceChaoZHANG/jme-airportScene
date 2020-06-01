package jmeDemo.Test;

	import com.jme3.app.SimpleApplication;
	import com.jme3.light.AmbientLight;
	import com.jme3.light.DirectionalLight;
	import com.jme3.material.Material;
	import com.jme3.math.ColorRGBA;
	import com.jme3.math.Vector3f;
	import com.jme3.scene.Geometry;
	import com.jme3.scene.Mesh;
	import com.jme3.scene.Node;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.shape.Sphere;

	/**
	 * ����ͼ���ڵ�
	 * @author yanmaoyuan
	 *
	 */
	public class HelloNode extends SimpleApplication {
        private float angle=0;
		@Override
		public void simpleInitApp() {
			// ��������
			Mesh mesh = new Sphere(16, 24, 1);
			flyCam.setDragToRotate(true);

			// ����2������
			Geometry geomA = new Geometry("��ɫ����", mesh);
			geomA.setMaterial(newLightingMaterial(ColorRGBA.Red));

			Geometry geomB = new Geometry("��ɫ����", mesh);
			geomB.setMaterial(newLightingMaterial(ColorRGBA.Cyan));

			// ������������ӵ�һ��Node�ڵ���
			Node nodeA = new Node("1");
			nodeA.attachChild(geomA);
			nodeA.move(0,0,0);
			Node nodeB = new Node("2");
			
			nodeB.move(3, 0, 0);
			nodeB.attachChild(geomB);



			// ������ڵ���ӵ�����ͼ��
			nodeA.attachChild(nodeB);
			rootNode.attachChild(nodeA);
			  createArrow(new Vector3f(40, 0, 0), ColorRGBA.Green);
		      createArrow(new Vector3f(0, 40, 0), ColorRGBA.Red);
		      createArrow(new Vector3f(0, 0, 40), ColorRGBA.Blue);

			// ��ӹ�Դ
			addLight();
		}
		
		  private void createArrow(Vector3f vec3, ColorRGBA color) {
		        // �������ʣ��趨��ͷ����ɫ
		        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		        mat.setColor("Color", color);
		        // �����������壬Ӧ�ü�ͷ����
		        Geometry geom = new Geometry("arrow", new Arrow(vec3));
		        geom.setMaterial(mat);
		        geom.setLocalTranslation(0.0f, 2.0f, 0.0f);
		        // ��ӵ�������
		        rootNode.attachChild(geom);
		    }

		@Override
		public void simpleUpdate(float tpf) {
			// TODO Auto-generated method stub
			super.simpleUpdate(tpf);
			angle=angle+tpf/1000;
			rootNode.getChild("1").rotate(0,angle, 0);
		}

		/**
		 * ����һ���й����
		 * @param color
		 * @return
		 */
		private Material newLightingMaterial(ColorRGBA color) {
			// ��������
			Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");

			mat.setColor("Diffuse", color);
			mat.setColor("Ambient", color);
			mat.setColor("Specular", ColorRGBA.White);
	        mat.setFloat("Shininess", 24);
	        mat.setBoolean("UseMaterialColors", true);

			return mat;
		}

	    /**
	     * ��ӹ�Դ
	     */
	    private void addLight() {
	        // �����
	        DirectionalLight sun = new DirectionalLight();
	        sun.setDirection(new Vector3f(-1, -2, -3));

	        // ������
	        AmbientLight ambient = new AmbientLight();

	        // ������������
	        ColorRGBA lightColor = new ColorRGBA();
	        sun.setColor(lightColor.mult(0.8f));
	        ambient.setColor(lightColor.mult(0.2f));

	        // #3 ��ģ�ͺ͹�Դ��ӵ�����ͼ��
	        rootNode.addLight(sun);
	        rootNode.addLight(ambient);
	    }

		public static void main(String[] args) {
			HelloNode app = new HelloNode();
			app.setShowSettings(false);
			app.start();
		}

	}
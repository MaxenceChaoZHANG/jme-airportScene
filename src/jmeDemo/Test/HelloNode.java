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
	 * 场景图、节点
	 * @author yanmaoyuan
	 *
	 */
	public class HelloNode extends SimpleApplication {
        private float angle=0;
		@Override
		public void simpleInitApp() {
			// 球体网格
			Mesh mesh = new Sphere(16, 24, 1);
			flyCam.setDragToRotate(true);

			// 创建2个球体
			Geometry geomA = new Geometry("红色气球", mesh);
			geomA.setMaterial(newLightingMaterial(ColorRGBA.Red));

			Geometry geomB = new Geometry("青色气球", mesh);
			geomB.setMaterial(newLightingMaterial(ColorRGBA.Cyan));

			// 将两个球体添加到一个Node节点中
			Node nodeA = new Node("1");
			nodeA.attachChild(geomA);
			nodeA.move(0,0,0);
			Node nodeB = new Node("2");
			
			nodeB.move(3, 0, 0);
			nodeB.attachChild(geomB);



			// 将这个节点添加到场景图中
			nodeA.attachChild(nodeB);
			rootNode.attachChild(nodeA);
			  createArrow(new Vector3f(40, 0, 0), ColorRGBA.Green);
		      createArrow(new Vector3f(0, 40, 0), ColorRGBA.Red);
		      createArrow(new Vector3f(0, 0, 40), ColorRGBA.Blue);

			// 添加光源
			addLight();
		}
		
		  private void createArrow(Vector3f vec3, ColorRGBA color) {
		        // 创建材质，设定箭头的颜色
		        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		        mat.setColor("Color", color);
		        // 创建几何物体，应用箭头网格。
		        Geometry geom = new Geometry("arrow", new Arrow(vec3));
		        geom.setMaterial(mat);
		        geom.setLocalTranslation(0.0f, 2.0f, 0.0f);
		        // 添加到场景中
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
		 * 创建一个感光材质
		 * @param color
		 * @return
		 */
		private Material newLightingMaterial(ColorRGBA color) {
			// 创建材质
			Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");

			mat.setColor("Diffuse", color);
			mat.setColor("Ambient", color);
			mat.setColor("Specular", ColorRGBA.White);
	        mat.setFloat("Shininess", 24);
	        mat.setBoolean("UseMaterialColors", true);

			return mat;
		}

	    /**
	     * 添加光源
	     */
	    private void addLight() {
	        // 定向光
	        DirectionalLight sun = new DirectionalLight();
	        sun.setDirection(new Vector3f(-1, -2, -3));

	        // 环境光
	        AmbientLight ambient = new AmbientLight();

	        // 调整光照亮度
	        ColorRGBA lightColor = new ColorRGBA();
	        sun.setColor(lightColor.mult(0.8f));
	        ambient.setColor(lightColor.mult(0.2f));

	        // #3 将模型和光源添加到场景图中
	        rootNode.addLight(sun);
	        rootNode.addLight(ambient);
	    }

		public static void main(String[] args) {
			HelloNode app = new HelloNode();
			app.setShowSettings(false);
			app.start();
		}

	}
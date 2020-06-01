package jmeDemo.Test;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.texture.Texture;
import com.jme3.util.BufferUtils;

/**
 * How to create custom meshes by specifying vertices
 * We render the mesh in three different ways, once with a solid blue color,
 * once with vertex colors, and once with a wireframe material.
 * @author KayTrance
 */
public class TestCustomMesh extends SimpleApplication {

    public static void main(String[] args){
        TestCustomMesh app = new TestCustomMesh();
        app.start();
    }

    @Override
    public void simpleInitApp() {
      
        Mesh m = new Mesh();

        // Vertex positions in space
        Vector3f [] vertices = new Vector3f[4];
        vertices[0] = new Vector3f(0,0,0);
        vertices[1] = new Vector3f(3,0,0);
        vertices[2] = new Vector3f(0,3,0);
        vertices[3] = new Vector3f(3,3,0);

        // Texture coordinates
        Vector2f [] texCoord = new Vector2f[4];
        texCoord[0] = new Vector2f(0,0);
        texCoord[1] = new Vector2f(1,0);
        texCoord[2] = new Vector2f(0,1);
        texCoord[3] = new Vector2f(1,1);
        float[] uv ={
	        	0f,0f,1f,0f,0f,1f,1f,1f
	        	
	        };
        // Indexes. We define the order in which mesh should be constructed
        short[] indexes = {2, 0, 1, 1, 3, 2};

        // Setting buffers
        m.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        m.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(uv));
        m.setBuffer(Type.Index, 1, BufferUtils.createShortBuffer(indexes));
        m.updateBound();

        // *************************************************************************
        // First mesh uses one solid color
        // *************************************************************************

        // Creating a geometry, and apply a single color material to it
        Geometry geom = new Geometry("OurMesh", m);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture tex = assetManager.loadTexture("ZBAA/Beijing_Ortho/beijing3_01.dds");
	       mat.setTexture("ColorMap", tex);
//        mat.setColor("Color", ColorRGBA.Blue);
        geom.setMaterial(mat);
        geom.rotate(-45, 0, 0);

        // Attaching our geometry to the root node.
        rootNode.attachChild(geom);

        
    }
}

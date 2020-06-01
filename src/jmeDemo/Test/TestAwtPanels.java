package jmeDemo.Test;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.jme3.system.awt.AwtPanel;
import com.jme3.system.awt.AwtPanelsContext;
import com.jme3.system.awt.PaintMode;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class TestAwtPanels extends SimpleApplication {

    final private static CountDownLatch panelsAreReady = new CountDownLatch(1);
    private static TestAwtPanels app;
    private static AwtPanel panel, panel2;
    private static int panelsClosed = 0;
    
    private static void createWindowForPanel(AwtPanel panel, int location){
        JFrame frame = new JFrame("Render Display " + location);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (++panelsClosed == 2){
                    app.stop();
                }
            }
        });
        frame.pack();
        frame.setLocation(location, Toolkit.getDefaultToolkit().getScreenSize().height - 400);
        frame.setVisible(true);
    }
    
    public static void main(String[] args){
        Logger.getLogger("com.jme3").setLevel(Level.WARNING);
        
        app = new TestAwtPanels();
        app.setShowSettings(false);
        AppSettings settings = new AppSettings(true);
        settings.setCustomRenderer(AwtPanelsContext.class);
        settings.setFrameRate(60);
        app.setSettings(settings);
        app.start();
        
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                /*
                 * Sleep 2 seconds to ensure there's no race condition.
                 * The sleep is not required for correctness.
                 */
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException exception) {
                    return;
                }

                final AwtPanelsContext ctx = (AwtPanelsContext) app.getContext();
                panel = ctx.createPanel(PaintMode.Accelerated);
                panel.setPreferredSize(new Dimension(400, 300));
//                ctx.setInputSource(panel);
                
                panel2 = ctx.createPanel(PaintMode.Accelerated);
                panel2.setPreferredSize(new Dimension(400, 300));
                 ctx.setInputSource(panel2);
                
                createWindowForPanel(panel, 300);
                createWindowForPanel(panel2, 700);
                /*
                 * Both panels are ready.
                 */
                panelsAreReady.countDown();
            }
        });
    }
    
    @Override
    public void simpleInitApp() {
        flyCam.setDragToRotate(true);
        
        Box b = new Box(10, 10, 10);
        Geometry geom = new Geometry("Box", b);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", assetManager.loadTexture("Interface/Logo/Monkey.jpg"));
        geom.setMaterial(mat);
        rootNode.attachChild(geom);
        

        /*
         * Wait until both AWT panels are ready.
         */
        try {
            panelsAreReady.await();
        } catch (InterruptedException exception) {
            throw new RuntimeException("Interrupted while waiting for panels", exception);
        }
        
     // Setup second view
        Camera cam2 = cam.clone();
//        cam2.setViewPort(0f, 0.5f, 0f, 0.5f);
        cam2.setLocation(new Vector3f(-0.10947256f, 1.5760219f, 4.81758f));
        cam2.setRotation(new Quaternion(0.0010108891f, 0.99857414f, -0.04928594f, 0.020481428f));
        
        ViewPort view2 = renderManager.createMainView("Bottom Left", cam2);
        view2.setClearFlags(true, true, true);
        view2.attachScene(rootNode);

        panel.attachTo(true, viewPort,guiViewPort);
        guiViewPort.setClearFlags(true, true, true);
        panel2.attachTo(false, view2);
        
        
//        List<Spatial> nodes=guiNode.getChildren();
//        for(int i=0;i<nodes.size();i++) {
//        	System.out.println(nodes.get(i).getClass().getName());
//        }
    }
}




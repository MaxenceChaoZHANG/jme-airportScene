package jmeDemo.Hello;

import com.jme3.app.SimpleApplication;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.style.BaseStyles;

/**
 * Lemur GUI
 * @author yanmaoyuan
 *
 */
public class HelloLemur extends SimpleApplication {

	public static void main(String[] args) {
		// ��������
		HelloLemur app = new HelloLemur();
		app.start();
	}

	@Override
	public void simpleInitApp() {

		// ��ʼ��Lemur GUI
		GuiGlobals.initialize(this);

		// ���� 'glass' ��ʽ
		BaseStyles.loadGlassStyle();

		// ��'glass'����ΪGUIĬ����ʽ
		GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");

		// ����һ��Container��Ϊ����������GUIԪ�ص�����
		Container myWindow = new Container();
		guiNode.attachChild(myWindow);

		// ���ô�������Ļ�ϵ�����
		// ע�⣺Lemur��GUIԪ�����Կؼ����Ͻ�Ϊԭ�㣬���ҡ��������ɵġ�
		// Ȼ������Ϊһ��Spatial������GuiNode�е�����ԭ����Ȼ����Ļ�����½ǡ�
		myWindow.setLocalTranslation(300, 300, 0);

		// ���һ��Label�ؼ�
		myWindow.addChild(new Label("Hello, World."));
		
		// ���һ��Button�ؼ�
		Button clickMe = myWindow.addChild(new Button("Click Me"));
		clickMe.addClickCommands(new Command<Button>() {
			public void execute(Button source) {
				System.out.println("The world is yours.");
			}
		});
	}

}
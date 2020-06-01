package ui;


import java.awt.*;
import javax.swing.*;
 
class MyFrame extends JFrame{
	JButton button = new JButton("start");
	JLabel label = new JLabel("The value will decrease!");
	public MyFrame() {
		this.setTitle("²âÊÔ InvokeLater");
		this.setSize(300, 200);
		label.setFont(new Font("", 0, 24));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(button, BorderLayout.NORTH);
		getContentPane().add(label, BorderLayout.CENTER);
		
		button.addActionListener(e->{
			new Thread(()->{
				for(int i = 10; i >= 0; i--) {
					final int j = i;
					SwingUtilities.invokeLater(()->{
						label.setText(""+j);
					});
					try {Thread.sleep(300);}catch (Exception ex) {}
				}
			}).start();
		});
	}
}
 
public class InvokeLaterDemo {
	public static void main(String[] args) {
		new MyFrame().setVisible(true);
	}
}

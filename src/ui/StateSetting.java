package ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jme3.app.state.AppStateManager;

import appState.MiniMapState;
import appState.MultipleWindowState;
import appState.PickAppState;
import appState.SkyAppState;
import appState.XplaneWeatherState;
import atmosphere.NewAtmosphereState;

public class StateSetting extends JPanel {
	
	private AppStateManager stateManager;

	/**
	 * Create the panel.
	 */
	public StateSetting(AppStateManager stateManager) {
		this.stateManager=stateManager;
		setSize(new Dimension(450, 300));
		setLayout(null);
		
		JLabel lableStateSetting = new JLabel("\u5F00\u542F/\u5173\u95ED\u529F\u80FD");
		lableStateSetting.setBounds(14, 13, 98, 18);
		add(lableStateSetting);
		
		JCheckBox chkSimpleSky = new JCheckBox("\u7B80\u5355\u5929\u7A7A");
		chkSimpleSky.setBounds(31, 54, 89, 27);
		add(chkSimpleSky);
		
		JCheckBox chkFog = new JCheckBox("\u96FE");
		chkFog.setBounds(275, 118, 43, 27);
		add(chkFog);
		
		JCheckBox chkComplicateSky = new JCheckBox("\u590D\u6742\u5929\u7A7A");
		chkComplicateSky.setBounds(31, 86, 89, 27);
		add(chkComplicateSky);
		
		JCheckBox chkMiniMap = new JCheckBox("\u5C0F\u5730\u56FE");
		chkMiniMap.setBounds(31, 118, 73, 27);
		add(chkMiniMap);
		
		JCheckBox chkWeather = new JCheckBox("\u5929\u6C14\uFF08\u96E8\u3001\u96EA\u3001\u51B0\u96F9\uFF09");
		chkWeather.setBounds(275, 86, 169, 27);
		add(chkWeather);
		
		JCheckBox chkLeftWindow = new JCheckBox("\u5DE6\u7A97\u53E3");
		chkLeftWindow.setBounds(31, 150, 73, 27);
		add(chkLeftWindow);
		
		JCheckBox chkRightWindow = new JCheckBox("\u53F3\u7A97\u53E3");
		chkRightWindow.setBounds(31, 182, 73, 27);
		add(chkRightWindow);
		
		JCheckBox chkMapPick = new JCheckBox("\u5730\u56FE\u62FE\u53D6");
		chkMapPick.setBounds(275, 54, 89, 27);
		add(chkMapPick);
		
		if (stateManager.getState(SkyAppState.class)!=null)
			chkSimpleSky.setSelected(true);	
		if (stateManager.getState(NewAtmosphereState.class)!=null)
			chkComplicateSky.setSelected(true);
		if (stateManager.getState(MiniMapState.class)!=null)
			chkMiniMap.setSelected(true);
		if (stateManager.getState(MultipleWindowState.class)!=null)
	     {  chkLeftWindow.setSelected(true);
		    chkRightWindow.setSelected(true); }
		if (stateManager.getState(PickAppState.class)!=null)
		    chkMapPick.setSelected(true);
		if (stateManager.getState(XplaneWeatherState.class)!=null)
		    chkWeather.setSelected(true);
		
		JButton btnApply = new JButton("\u5E94\u7528");
		btnApply.setBounds(314, 246, 63, 27);
		btnApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (stateManager.getState(SkyAppState.class)!=null)
				{stateManager.getState(SkyAppState.class).setEnabled(chkSimpleSky.isSelected());}
				
				if (stateManager.getState(MiniMapState.class)!=null)
				{stateManager.getState(MiniMapState.class).setEnabled(chkMiniMap.isSelected());}
				
				if (stateManager.getState(PickAppState.class)!=null)
				{stateManager.getState(PickAppState.class).setEnabled(chkMapPick.isSelected());}
				
				if (stateManager.getState(MultipleWindowState.class)!=null)
				{stateManager.getState(MultipleWindowState.class).leftWindowEnable(chkLeftWindow.isSelected());
				stateManager.getState(MultipleWindowState.class).rightWindowEnable(chkRightWindow.isSelected());}
				
				if (stateManager.getState(NewAtmosphereState.class)!=null)
				{stateManager.getState(NewAtmosphereState.class).setEnabled(chkComplicateSky.isSelected());}
				
				if (stateManager.getState(XplaneWeatherState.class)!=null)
				{stateManager.getState(XplaneWeatherState.class).setEnabled(chkWeather.isSelected());}
				
				
			}
		});
		add(btnApply);

	}

}

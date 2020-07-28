package ui;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

import javax.swing.JTextField;

import org.dom4j.DocumentException;

import com.jme3.app.state.AppStateManager;

import appState.AnimState;
import appState.PickAppState;
import appState.SceneAppState;
import appState.SkyAppState;
import dataEntity.AirportInfo;

import java.awt.Dimension;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AirportSetting extends JPanel {
	
	private ArrayList<AirportInfo> airportInfos;
	private AppStateManager stateManager;
	private SortedComboBoxModel model ;
	
	private JComboBox cbBSelect;
	private JTextField txtName;
	private JTextField txtCode;
	private JTextField txtLontitude;
	private JTextField txtLatitude;
	private JTextField txtScene;
	
	

	/**
	 * Create the panel.
	 */
	public AirportSetting(ArrayList<AirportInfo> airportInfos,AppStateManager stateManager) {
		this.stateManager=stateManager;
		this.airportInfos=airportInfos;
		
		setSize(new Dimension(450, 300));
		setLayout(null);
		
		JLabel labelInformation = new JLabel("\u673A\u573A\u4FE1\u606F");
		labelInformation.setBounds(14, 13, 60, 18);
		add(labelInformation);
		
		JLabel labelSelect = new JLabel("\u9009\u62E9\u673A\u573A\uFF1A");
		labelSelect.setBounds(70, 47, 75, 18);
		add(labelSelect);
		
		model = new SortedComboBoxModel(airportInfos.toArray());
		cbBSelect = new JComboBox(model);
		cbBSelect.setBounds(151, 44, 218, 24);
		cbBSelect.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
			AirportInfo airportinfo = (AirportInfo) e.getItem();
			//更新AnimState中的AirportInfo
			if (stateManager.getState(AnimState.class)!=null) {
			stateManager.getState(AnimState.class).setCurrentAirportInfo(airportinfo);
			}
			
		      txtName.setText(airportinfo.getName());
		      txtCode.setText(airportinfo.getICAO_Code());
		      txtLontitude.setText(airportinfo.getLontitude()+"");
		      txtLatitude.setText(airportinfo.getLatitude()+"");
		      txtScene.setText(airportinfo.getFilePath());
		    }
		});
		cbBSelect.setSelectedIndex(0);
		add(cbBSelect);
		
		//lable机场名字
		JLabel labelName = new JLabel("\u673A\u573A\u540D\u79F0\uFF1A");
		labelName.setBounds(70, 102, 75, 18);
		add(labelName);
		//text机场名字
		txtName = new JTextField();
		txtName.setBounds(151, 99, 218, 24);
		add(txtName);
		txtName.setColumns(10);
		
		
		//lable机场代码
		JLabel labelCode = new JLabel("ICAO\u4EE3\u7801\uFF1A");
		labelCode.setBounds(70, 131, 77, 18);
		add(labelCode);
		//text机场代码
		txtCode = new JTextField();
		txtCode.setBounds(151, 128, 218, 24);
		add(txtCode);
		txtCode.setColumns(10);
		
		
		//lable机场中心经度
		JLabel labelLontitude = new JLabel("\u673A\u573A\u7ECF\u5EA6\uFF1A");
		labelLontitude.setBounds(72, 158, 75, 18);
		add(labelLontitude);
		//text机场中心经度
		txtLontitude = new JTextField();
		txtLontitude.setBounds(151, 155, 218, 24);
		add(txtLontitude);
		txtLontitude.setColumns(10);
		
		
		//lable机场中心纬度
		JLabel labelLatitude = new JLabel("\u673A\u573A\u7EAC\u5EA6\uFF1A");
		labelLatitude.setBounds(72, 186, 75, 18);
		add(labelLatitude);
		//text机场中心纬度
		txtLatitude = new JTextField();
		txtLatitude.setBounds(151, 183, 218, 24);
		add(txtLatitude);
		txtLatitude.setColumns(10);
		
		
		
		//lable机场地景文件
		JLabel labelScene = new JLabel("\u5730\u666F\u6587\u4EF6\uFF1A");
		labelScene.setBounds(72, 213, 75, 18);
		add(labelScene);
		//text机场地景文件
		txtScene = new JTextField();
		txtScene.setBounds(151, 210, 218, 24);
		add(txtScene);
		txtScene.setColumns(10);
		
		JButton btnConfirm = new JButton("\u786E\u8BA4\u4FEE\u6539");
		btnConfirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AirportInfo airportinfo=(AirportInfo)cbBSelect.getSelectedItem();
				//更新化AnimState中的AirportInfo
				if (stateManager.getState(AnimState.class)!=null) {
				stateManager.getState(AnimState.class).setCurrentAirportInfo(airportinfo);
			    }
				
				airportinfo.setName(txtName.getText());
				airportinfo.setICAO_Code(txtCode.getText());
				airportinfo.setLontitude(Float.parseFloat(txtLontitude.getText()));
				airportinfo.setLatitude(Float.parseFloat(txtLatitude.getText()));
				airportinfo.setFilePath(txtScene.getText());
				try {
					XMLUtility.UpdateXML(airportinfo);
				} catch (DocumentException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnConfirm.setBounds(201, 247, 93, 27);
		add(btnConfirm);
		
		JButton btnReload = new JButton("\u91CD\u65B0\u8F7D\u5165");
		btnReload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (stateManager.getState(SceneAppState.class)!=null)
				{stateManager.getState(SceneAppState.class).changScene( (AirportInfo)cbBSelect.getSelectedItem() );}
			}
		});
		btnReload.setBounds(308, 247, 93, 27);
		add(btnReload);
		
		
		 txtName.setText(((AirportInfo)cbBSelect.getSelectedItem()).getName());
	     txtCode.setText(((AirportInfo)cbBSelect.getSelectedItem()).getICAO_Code());
	     txtLontitude.setText(((AirportInfo)cbBSelect.getSelectedItem()).getLontitude()+"");
	     txtLatitude.setText(((AirportInfo)cbBSelect.getSelectedItem()).getLatitude()+"");
	     txtScene.setText(((AirportInfo)cbBSelect.getSelectedItem()).getFilePath());

	}
	
	class SortedComboBoxModel extends DefaultComboBoxModel {
		  public SortedComboBoxModel() {
		    super();
		  }
		  public SortedComboBoxModel(Object[] items) {
		    Arrays.sort(items);
		    int size = items.length;
		    for (int i = 0; i < size; i++) {
		      super.addElement(items[i]);
		    }
		    setSelectedItem(items[0]);
		  }

		  public SortedComboBoxModel(Vector items) {
		    Collections.sort(items);
		    int size = items.size();
		    for (int i = 0; i < size; i++) {
		      super.addElement(items.elementAt(i));
		    }
		    setSelectedItem(items.elementAt(0));
		  }

		  @Override
		  public void addElement(Object element) {
		    insertElementAt(element, 0);
		  }

		  @Override
		  public void insertElementAt(Object element, int index) {
		    int size = getSize();
		    for (index = 0; index < size; index++) {
		      Comparable c = (Comparable) getElementAt(index);
		      if (c.compareTo(element) > 0) {
		        break;
		      }
		    }
		    super.insertElementAt(element, index);
		  }
	
    }
	
	
}

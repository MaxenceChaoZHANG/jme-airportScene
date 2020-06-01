package appState;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import com.jme3.input.FlyByCamera;
import com.jme3.system.awt.AwtPanel;

public class AwtPanelFocus implements FocusListener
{
	AwtPanel panel;
	FlyByCamera flyCam;

	public AwtPanelFocus(AwtPanel panel, FlyByCamera flyCam)
	{
	    this.panel = panel;
	    this.flyCam = flyCam;
	}
	
	@Override
	public void focusGained(FocusEvent e)
	{
	    flyCam.setEnabled(true);
	}
	
	@Override
	public void focusLost(FocusEvent e) 
	{
	    flyCam.setEnabled(false);
	}
}
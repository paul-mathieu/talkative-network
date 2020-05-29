package ePapotage.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import ePapotage.Concierge;
import ePapotage.ePapotage;

public class ConciergeFrame extends JFrame {

	// This is not necessary because we don't want to serialize this class, but we
	// let it not to get the error
	private static final long serialVersionUID = 1L;
		
	private JTextArea chatDisplay;
	private JScrollPane displayScrollPanel;
	
	//The Concierge attached to this ConciergeFrame
	private Concierge concierge;
	
	//The password which is hashed with md5
	private String password;
	
	//The constructor which takes only a Concierge
	public ConciergeFrame(Concierge concierge) {

		//We set the Concierge of the current ConciergeFrame to the concierge given in arguments
		this.concierge = concierge;

		//Basic GUI things
		this.setTitle(concierge.getName());
		this.setSize(800, 600);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    this.setLocationRelativeTo(null);
	    this.setResizable(false);
	   	    
	    //Content of the frame
	    
	    //The chat display where we set a default message (and which is not editable by the user)
	    this.chatDisplay = new JTextArea("");
	    this.chatDisplay.setEditable(false);
	    
	    //A Scroll panel with is usefull to show too long messages for exemple
	    this.displayScrollPanel = new JScrollPane (this.chatDisplay, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.displayScrollPanel.setPreferredSize(new Dimension(594, 500));
	    	    
	    this.add(this.displayScrollPanel, BorderLayout.NORTH);
	}
	
	//Write the logs to the chat display
	public void writeMessage(String log) {
		this.chatDisplay.setText(this.chatDisplay.getText() + "\n" + ePapotage.getDate() + ": " + log);
	}
	
	public Concierge getConcierge() {
		return concierge;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}

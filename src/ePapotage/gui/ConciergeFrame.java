package ePapotage.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import ePapotage.Concierge;
import ePapotage.ePapotage;


/**
 * The concierge has a rather simple interface.
 * This interface is created when the application is launched, but it is only visible when the administrator logs out.
 * If the concierge logs out, his window becomes hidden again.
 * On this interface are most of the events that have occurred since the application was last launched.
 * These events are displayed in the form of logs and tell the concierge what is happening on the application.
 * You can find the following:
 * - The login of a user and his name
 * - The name of a user who has just tried to connect but who has forgotten his password
 * - The name of a user who has just been added to the listened to list of another user
 * - The sending of a message by a user, with the total number of messages that the user has sent as well as the total
 *     number of messages sent by the application since the very first message was sent
 * - The name of a user who just tried to send a message without content
 */
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
		this.setSize(800, 500);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    this.setLocationRelativeTo(null);
	    this.setResizable(false);
	   	    
	    // Content of the frame
	    
	    // The chat display where we set a default message (and which is not editable by the user)
	    this.chatDisplay = new JTextArea("");
	    this.chatDisplay.setEditable(false);
	    
	    // A Scroll panel with is usefull to show too long messages for exemple
	    this.displayScrollPanel = new JScrollPane (this.chatDisplay, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.displayScrollPanel.setPreferredSize(new Dimension(594, 500));
	    	    
	    this.add(this.displayScrollPanel, BorderLayout.NORTH);
	}
	
	// Write the logs to the chat display
	public void writeMessage(String log) {
		this.chatDisplay.setText(this.chatDisplay.getText() + "\n" + ePapotage.getDate() + ": " + log);
	}

}

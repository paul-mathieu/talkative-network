package ePapotage.gui;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;

import ePapotage.Bavard;
import ePapotage.ePapotage;
import ePapotage.PapotageListener;
import org.xml.sax.SAXException;

//We are implementing PapotageListener, because we want to register it to a Concierge
public class BavardFrame extends JFrame implements PapotageListener {

	// This is not necessary because we don't want to serialize this class, but we
	// let it not to get the warning
	private static final long serialVersionUID = 1L;

	// All the variables

	private JPanel writeAndSendPanel;
	private JTextArea chatDisplay;
	private JTextField chatWriter;
	private JButton sendButton;
	private JPanel sendPanel;
	private JScrollPane displayScrollPanel;

	private Bavard bavard;
	private String bavardUsername;
	private BavardFrame bavardFrame;
	// The password which is hashed with md5
	private String password;

	// The constructor needs a bavard and a name
	public BavardFrame(String bavardUsername) {

		// This is used to get the instance of the object inside the Listeners
		this.bavardFrame = this;
		this.bavardUsername = bavardUsername;

		// Basic things
		this.setTitle("ePapotage - " + this.bavardUsername + "'s Conversations");
		this.setSize(600, 400);
		this.setLocationRelativeTo(null);
		this.setResizable(false);

		// We don't want the program to finish, just the window to close, this is why we
		// are using DISPOSE_ON_CLOSE instead of EXIT_ON_CLOSE
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		// If the person closes the window we inform the Concierges who are listening to
		// the bavard, that his window has been closed
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosed(WindowEvent windowEvent) {
				if (ePapotage.getConcierge().getListeners().contains(bavardFrame)) {
					ePapotage.removeBavard(bavardUsername);
				}
			}
		});

		// Content of the frame, no need to explain

		this.chatDisplay = new JTextArea();
		this.chatDisplay.setEditable(false);

		JScrollPane displayScrollPanel = new JScrollPane(this.chatDisplay, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		displayScrollPanel.setPreferredSize(new Dimension(594, 321));

		this.chatWriter = new JTextField();
		this.chatWriter.setBounds(25, 370, 475, 20);

		this.sendButton = new JButton("Send");
		this.sendButton.setBounds(525, 370, 50, 20);

		this.writeAndSendPanel = new JPanel();
		this.writeAndSendPanel.setPreferredSize(new Dimension(10, 10));
		this.sendPanel = new JPanel();

		JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);

		JMenu menuBavardFrame = new JMenu("Settings");
		menuBar.add(menuBavardFrame);

		JMenu listBavardsConnected = new JMenu("List of Bavards connected");
		menuBavardFrame.add(listBavardsConnected);

		JMenuItem connectToBavard = new JMenuItem("Add a Bavard to listen");
		menuBavardFrame.add(connectToBavard);

		JMenuItem disconnectToBavard = new JMenuItem("Take off a Bavard to listen");
		menuBavardFrame.add(disconnectToBavard);

		connectToBavard.addActionListener(e -> {
			try {
				new ConnectGUI("connect", this.getBavardUsername());
			} catch (ParserConfigurationException | IOException | SAXException ex) {
				ex.printStackTrace();
			}
		});

		disconnectToBavard.addActionListener(e -> {
			try {
				new ConnectGUI("disconnect", this.getBavardUsername());
			} catch (ParserConfigurationException | IOException | SAXException ex) {
				ex.printStackTrace();
			}
		});

//		listBavardsConnected


//		jCheckBoxMenuItem.addActionListener(e -> {
//			// Here we choose to connect or not
//			if (jCheckBoxMenuItem.getState()) {
//				ePapotage.getConcierge().addListener(bavard);
//				ePapotage.getConcierge().addListener(bavardFrame);
//				ePapotage.getConcierge().getConciergeFrame().writeLogs(this.getBavardUsername() + " is now following you");
//			} else {
//				ePapotage.getConcierge().removeListener(bavard);
//				ePapotage.getConcierge().removeListener(bavardFrame);
//				ePapotage.getConcierge().getConciergeFrame().writeLogs(this.getBavardUsername() + " is no longer following you");
//			}
//		});

		// Set set the Bavard of the BavardFrame
		this.bavard = bavard;

		// We are now using a GroupLayout which is pretty hard to explain and to deal
		// with, but we finally succeed :D

		writeAndSendPanel.setLayout(new BoxLayout(writeAndSendPanel, BoxLayout.Y_AXIS));
		writeAndSendPanel.add(displayScrollPanel);
		writeAndSendPanel.add(sendPanel);

		GroupLayout groupLayout = new GroupLayout(sendPanel);
		groupLayout.setAutoCreateGaps(true);
		groupLayout.setAutoCreateContainerGaps(true);
		sendPanel.setLayout(groupLayout);

		groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup().addComponent(this.chatWriter)
				.addComponent(this.sendButton));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(this.chatWriter).addComponent(this.sendButton));

		this.setContentPane(writeAndSendPanel);

		// We add an ActionListener to the button used to send messages
		this.sendButton.addActionListener(e -> {

			// If the message contains something
			if (!chatWriter.getText().isEmpty()) {

				// We loop through all the Concierge and "tell" to the them which this bavard is
				// connected to send message to their listeners and then write logs (to keep a
				// trace)
				this.sendMessage(this.chatWriter.getText());

				// We reset the text of the chat writer
				this.chatWriter.setText("");
			}

		});

		// We set a default button, thanks to this we only need to press enter and the
		// message will be sent
		this.writeAndSendPanel.getRootPane().setDefaultButton(this.sendButton);

	}

	// ======================================================
	//   Main methods
	// ======================================================

	@Override
	public void sendMessage(String text) {
		this.getBavard().sendMessage(chatWriter.getText());
	}

	// ======================================================
	//   Getters
	// ======================================================

	public JPanel getWriteAndSendPanel() {return this.writeAndSendPanel;}

	public JTextArea getChatDisplay() {return this.chatDisplay;}

	public JTextField getChatWriter() {return this.chatWriter;}

	public JButton getSendButton() {return this.sendButton;}

	public JScrollPane getDisplayScrollPanel() {return displayScrollPanel;}

	public JPanel getSendPanel() {return this.sendPanel;}

	public Bavard getBavard() {return bavard;}

	public String getBavardUsername() {return this.bavardUsername;}

	// ======================================================
	//   Setters
	// ======================================================

	public void setWriteAndSendPanel(JPanel writeAndSendPanel) {this.writeAndSendPanel = writeAndSendPanel;}

	public void setChatDisplay(JTextArea chatDisplay) {this.chatDisplay = chatDisplay;}

	public void setChatWriter(JTextField chatWriter) {this.chatWriter = chatWriter;}

	public void setSendButton(JButton sendButton) {this.sendButton = sendButton;}

	public void setSendPanel(JPanel sendPanel) {this.sendPanel = sendPanel;}

	public void setDisplayScrollPanel(JScrollPane displayScrollPanel) {this.displayScrollPanel = displayScrollPanel;}

	public void setBavardFrame(BavardFrame bavardFrame) {this.bavardFrame = bavardFrame;}

}
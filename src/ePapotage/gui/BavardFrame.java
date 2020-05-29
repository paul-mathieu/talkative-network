package ePapotage.gui;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Objects;

import javax.swing.*;
import javax.swing.event.MenuListener;
import javax.xml.parsers.ParserConfigurationException;

import ePapotage.ePapotage;
import ePapotage.PapotageListener;
import ePapotage.PapotageEvent;
import org.xml.sax.SAXException;
import javax.swing.event.*;

//We are implementing PapotageListener, because we want to register it to a Concierge
public class BavardFrame extends JFrame implements PapotageListener, MenuListener {

	// This is not necessary because we don't want to serialize this class, but we
	// let it not to get the warning
	private static final long serialVersionUID = 1L;

	// All the variables

	private JPanel writeAndSendPanel;
	private JTextArea chatDisplay;
	private JTextField chatWriter;
	private JButton sendButton;
	private JPanel sendPanel;
	private JMenu listBavardsConnected;

	private String bavardUsername;
	private BavardFrame bavardFrame;

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
				ePapotage.removeBavard(bavardUsername);
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

		listBavardsConnected = new JMenu("List of Bavards connected");
		menuBavardFrame.add(listBavardsConnected);
		listBavardsConnected.setMnemonic(KeyEvent.VK_M);
		listBavardsConnected.addMenuListener(this);

		JMenuItem connectToBavard = new JMenuItem("Add a Bavard to listen");
		menuBavardFrame.add(connectToBavard);

		JMenuItem disconnectToBavard = new JMenuItem("Take off a Bavard to listen");
		menuBavardFrame.add(disconnectToBavard);

		connectToBavard.addActionListener(e -> connectToGui("connect"));

		disconnectToBavard.addActionListener(e -> connectToGui("disconnect"));

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
		this.sendButton.addActionListener(e -> this.writeSendMessageLogToConcierge());

		// We set a default button, thanks to this we only need to press enter and the
		// message will be sent
		this.writeAndSendPanel.getRootPane().setDefaultButton(this.sendButton);

	}


	// ======================================================
	//   Main methods
	// ======================================================

	@Override
	public void sendMessage(String text) {
		Objects.requireNonNull(ePapotage.getBavardFromName(this.getBavardUsername())).sendMessage(text);
	}

	public void writeMessage(String username, String date, String message) {
		String actualDate = ePapotage.getDate();

		String newDate = actualDate.substring(0, 10).equals(date.substring(0, 10)) ? date.substring(11) : date;

		String allMessages = this.chatDisplay.getText();
		this.chatDisplay.setText(allMessages + "\n" + username + " (" + newDate + "): " + message);
	}


	// ======================================================
	//   Getters
	// ======================================================

	public String getBavardUsername() {return this.bavardUsername;}

	// ======================================================
	//   Setters
	// ======================================================

	public void setBavardsConnectedList(){
		JMenuItem bavardName;
		this.listBavardsConnected.removeAll();
		for (String b: ePapotage.getNameBavardsConnected()) {
			bavardName = new JMenuItem(b);
			this.listBavardsConnected.add(bavardName);
		}
	}

	// ======================================================
	//   Action Listener Methods
	// ======================================================

	private void connectToGui(String typeConnection){
		if (typeConnection.equals("connect") || typeConnection.equals("disconnect")){
			try {
				new ConnectGUI(typeConnection, this.getBavardUsername());
			} catch (ParserConfigurationException | IOException | SAXException ex) {
				ex.printStackTrace();
			}
		}
	}

	private void writeSendMessageLogToConcierge() {
		// If the message contains something
		if (!chatWriter.getText().isEmpty()) {

			// We loop through all the Concierge and "tell" to the them which this bavard is
			// connected to send message to their listeners and then write logs (to keep a
			// trace)
			System.out.println("Content: " + this.chatWriter.getText());
			this.sendMessage(this.chatWriter.getText());
			String log = "The Bavard " + this.getBavardUsername() + " has just sent a message.";
			try {
				log = log + "\n" +
						"He has sent a total of " + ePapotage.getConcierge().getNumberMessages(this.getBavardUsername()) + " messages since he created his account.\n" +
						"Since the first launch of the application, " + ePapotage.getConcierge().getNumberMessages() + " messages have been sent.";
			} catch (IOException | SAXException | ParserConfigurationException ex) {
				ex.printStackTrace();
			}
			ePapotage.getConcierge().getConciergeFrame().writeMessage(log);

			// We reset the text of the chat writer
			this.chatWriter.setText("");
		} else {
			String log = "The Bavard " + this.getBavardUsername() + " has just tried to send a message without content.";
			ePapotage.getConcierge().getConciergeFrame().writeMessage(log);
		}
	}

	// ======================================================
	//   Override methods of MenuListener
	// ======================================================

	@Override
	public void menuSelected(MenuEvent menuEvent) {
		this.setBavardsConnectedList();
	}

	@Override
	public void menuDeselected(MenuEvent menuEvent) {}

	@Override
	public void menuCanceled(MenuEvent menuEvent) {}
}
package ePapotage.gui;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;

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

	public void setWriteAndSendPanel(JPanel writeAndSendPanel) {this.writeAndSendPanel = writeAndSendPanel;}

	public void setChatDisplay(JTextArea chatDisplay) {this.chatDisplay = chatDisplay;}

	public void setChatWriter(JTextField chatWriter) {this.chatWriter = chatWriter;}

	public void setSendButton(JButton sendButton) {this.sendButton = sendButton;}

	public void setSendPanel(JPanel sendPanel) {this.sendPanel = sendPanel;}

	public void setDisplayScrollPanel(JScrollPane displayScrollPanel) {this.displayScrollPanel = displayScrollPanel;}

	public void setBavardFrame(BavardFrame bavardFrame) {this.bavardFrame = bavardFrame;}

}
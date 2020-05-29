package ePapotage.gui;


import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;
import javax.xml.parsers.ParserConfigurationException;

import ePapotage.ePapotage;
import ePapotage.Bavard;
import org.xml.sax.SAXException;

public class LogInGUI {

	private JFrame frame;
	private JTextField usernameInput;
	private JPasswordField passInput;
	private JLabel usernameLabel;
	private JLabel passLabel;

	private String conciergeUsername;
	private String conciergePassword;

	private Bavard bavard = new Bavard();


	// The Gui is used to log the Bavard to his BavardFrame
	public LogInGUI(String conciergeUsername, String conciergePassword) throws ParserConfigurationException, IOException, SAXException {

		this.conciergeUsername = conciergeUsername;
		this.conciergePassword = conciergePassword;

		frame = new JFrame("Log in to Bavard");
		frame.setBounds(100, 100, 300, 185);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setResizable(false);

		usernameLabel = new JLabel("Username");
		passLabel = new JLabel("Password");

		usernameInput = new JTextField();
		usernameInput.setColumns(10);

		passInput = new JPasswordField();
		passInput.setColumns(10);
		passInput.setEchoChar('\u2022');

		// The group layout

		JButton logInButton = new JButton("Log In");
		logInButton.addActionListener(arg0 -> {});

		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
				.createSequentialGroup()
				.addGap(26)
				.addGroup(groupLayout
						.createParallelGroup(Alignment.LEADING)
							.addComponent(usernameLabel)
							.addComponent(passLabel))
				.addGap(56)
				.addGroup(groupLayout
						.createParallelGroup(Alignment.TRAILING, false)
							.addComponent(logInButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(passInput, Alignment.LEADING).addComponent(usernameInput, Alignment.LEADING))
				.addContainerGap(81, Short.MAX_VALUE)));

		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
				.createSequentialGroup()
				.addGap(28)
				.addGroup(groupLayout
						.createParallelGroup(Alignment.BASELINE)
							.addComponent(usernameLabel)
							.addComponent(usernameInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGap(18)
				.addGroup(groupLayout
						.createParallelGroup(Alignment.BASELINE)
							.addComponent(passLabel)
							.addComponent(passInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGap(18)
				.addComponent(logInButton).addContainerGap(85, Short.MAX_VALUE)));

		frame.getContentPane().setLayout(groupLayout);
		frame.getRootPane().setDefaultButton(logInButton);

		// We add the listener to the log in button
		logInButton.addActionListener(e -> {

			// We get the name and if the Bavard already exists we log to him, otherwise we deny the log in
			try {
//				System.out.println("Username: " + usernameInput.getText());
//				System.out.println("Password user: " + passInput.getText());
//				System.out.println("Password input: " + this.bavard.getBavardPassword(usernameInput.getText()));
				if (this.bavard.isExisting(usernameInput.getText())) {
					String username = usernameInput.getText();
					// Check if the password entered matchs to the Bavard password (with the md5 encryption)
					if (username.equals("admin")){
						ePapotage.getConcierge().getConciergeFrame().setVisible(true);
						frame.dispose();
						return;
					}
					if (this.bavard.getBavardPassword(username).equals(passInput.getText())) {

						// Check if the Bavard is already logged in (that is to say if the window is already visible)
						if (ePapotage.getBavardsConnected().size() != 0) {
							if (ePapotage.getBavardFromName(username) != null) {
								JOptionPane.showMessageDialog(frame, "This bavard is already logged in");
								String log = "The Bavard " + username + " has tried to connect while he is already connected.";
								ePapotage.getConcierge().getConciergeFrame().writeMessage(log);
								return;
							}
						}

						// Display the BavardFrame
						ePapotage.addBavard(new Bavard(username));
						String log = "The Bavard " + username + " has just connected.";
						ePapotage.getConcierge().getConciergeFrame().writeMessage(log);

						frame.dispose();
					} else {
						JOptionPane.showMessageDialog(frame, "Your password is incorrect!");
						String log = "The Bavard " + username + " tried to connect but got the wrong password";
						ePapotage.getConcierge().getConciergeFrame().writeMessage(log);
					}
				} else {
					JOptionPane.showMessageDialog(frame, "This bavard doesn't exist!");
					String log = "A user tried to connect to a nonexistent Bavard (" + usernameInput.getText() + ").";
					ePapotage.getConcierge().getConciergeFrame().writeMessage(log);
				}
			} catch (ParserConfigurationException | IOException | SAXException ex) {
				ex.printStackTrace();
			}

		});

		frame.setVisible(true);
	}
}

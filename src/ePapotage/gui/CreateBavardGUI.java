package ePapotage.gui;

import ePapotage.ePapotage;
import ePapotage.Bavard;
import ePapotage.Concierge;
import org.apache.commons.codec.digest.DigestUtils;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

public class CreateBavardGUI {

	// Variables
	private JFrame frame;
	private JPasswordField confirmInput;
	private JTextField usernameInput;
	private JPasswordField passInput;
	private JLabel usernameLabel;
	private JLabel passLabel;
	private JLabel confirmLabel;

	private Bavard bavard = new Bavard();

	// This Gui is used to create a new Bavard with a username and two fields for
	// the password
	public CreateBavardGUI() {

		frame = new JFrame("Create Bavard");
		frame.setBounds(100, 100, 300, 250);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setResizable(false);

		usernameLabel = new JLabel("Username");
		passLabel = new JLabel("Password");
		confirmLabel = new JLabel("Confirm Password");

		usernameInput = new JTextField();
		usernameInput.setColumns(10);

		passInput = new JPasswordField();
		passInput.setColumns(10);
		passInput.setEchoChar('\u2022');

		confirmInput = new JPasswordField();
		confirmInput.setColumns(10);
		confirmInput.setEchoChar('\u2022');

		// The group layout

		JButton registerButton = new JButton("Register Bavard");
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup().addGap(26)
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(confirmLabel)
								.addComponent(usernameLabel).addComponent(passLabel))
						.addGap(18)
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
								.addComponent(registerButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addComponent(confirmInput).addComponent(passInput).addComponent(usernameInput))
						.addContainerGap(78, Short.MAX_VALUE)));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
				.createSequentialGroup().addGap(28)
				.addGroup(groupLayout
						.createParallelGroup(Alignment.BASELINE).addComponent(usernameLabel).addComponent(usernameInput,
								GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGap(18)
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(passLabel).addComponent(
						passInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGap(18)
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(confirmLabel).addComponent(
						confirmInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGap(32).addComponent(registerButton).addContainerGap(33, Short.MAX_VALUE)));

		frame.getContentPane().setLayout(groupLayout);

		frame.setVisible(true);

		// We add an ActionListener when the register button is clicked
		registerButton.addActionListener(e -> this.createBavard());

		// Set the default button
		frame.getRootPane().setDefaultButton(registerButton);
	}

	// ======================================================
	//   Action Listener Methods
	// ======================================================

	public void createBavard(){

		// We check that all the fields are not empty
		if (usernameInput.getText().isEmpty()) {
			JOptionPane.showMessageDialog(frame, "Incorrect username!");
			return;
		}

		if (passInput.getText().isEmpty()) {
			JOptionPane.showMessageDialog(frame, "Incorrect password!");
			return;
		}

		// We check that the Barvard does not exist yet
		try {
			if (!this.bavard.isExisting(usernameInput.getText())) {

				// Check if the two passwords are the same (we are using md5 as the encryption method)
//					String password = DigestUtils.md5Hex(passInput.getText());
//					String passwordConfirm = DigestUtils.md5Hex(confirmInput.getText());
				String password = passInput.getText();
				String passwordConfirm = confirmInput.getText();
				System.out.println(password);

				String username = usernameInput.getText();

				if (passwordConfirm.equals(password)) {
					// === For the GUI ===
					String log = "The Bavard " + usernameInput.getText() + " just created an account.";
					ePapotage.getConcierge().getConciergeFrame().writeMessage(log);

					// === For the xml file ===
					Concierge.addBavard(username, password);

					frame.dispose();
				} else {
					JOptionPane.showMessageDialog(frame, "The passwords don't match !");
				}
			} else {
				JOptionPane.showMessageDialog(frame, "This bavard already exists !");
			}
		} catch (ParserConfigurationException | IOException | SAXException | TransformerException ex) {
			ex.printStackTrace();
		}

	}

}

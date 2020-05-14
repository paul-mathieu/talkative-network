package ePapotage.gui;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import ePapotage.ePapotage;
import org.xml.sax.SAXException;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class AdministratorGUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private String conciergeUsername = "concierge";
	private String conciergePassword = "concierge123";

	public AdministratorGUI() {

		new JFrame();
		this.setTitle("ePapotage - Stay Connected");
		this.setBounds(0, 0, 600, 650);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);

		// === background ===
		JLabel imgLabel = new JLabel(new ImageIcon(ePapotage.class.getResource("/ePapotage/resources/logo_signed_low.png")));
		this.add(imgLabel, BorderLayout.CENTER);

		// === menubar ===
		JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);

		//   - title of the list
		JMenu accountMenu = new JMenu("Account");
		menuBar.add(accountMenu);
		//   - content of the list
		JMenuItem userAdd = new JMenuItem("Add a bavard");
		accountMenu.add(userAdd);
		JMenuItem userLogin = new JMenuItem("Sign in");
		accountMenu.add(userLogin);

		//   - title of the list
		JMenu settingsMenu = new JMenu("Settings");
		menuBar.add(settingsMenu);
		//   - content of the list
		JMenuItem settingsOpenData = new JMenuItem("Open data folder");
		settingsMenu.add(settingsOpenData);
		JMenuItem settingsOpenGithub = new JMenuItem("Open paul-mathieu's Github");
		settingsMenu.add(settingsOpenGithub);


		// When the Administrator wants to add a bavard
		//Open a CreateBavardGUI (open Bavard and Concierge Window)
		userAdd.addActionListener(e -> new CreateBavardGUI());

		// When the administrator wants to log to an existing bavard
		userLogin.addActionListener(e -> {
			//Open a LogInToConciergeGUI
			try {
				new LogInGUI(conciergeUsername, conciergePassword);
			} catch (ParserConfigurationException | IOException | SAXException ex) {
				ex.printStackTrace();
			}
		});

		settingsOpenData.addActionListener(e -> {
			try {
				System.out.println(System.getProperty("user.dir") + "\\ePapotage\\data");
				Runtime.getRuntime().exec("explorer.exe " + System.getProperty("user.dir") + "\\src\\ePapotage\\data");
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		});

		settingsOpenGithub.addActionListener(e -> {
			try {
				if (Desktop.isDesktopSupported()) {
					Desktop desktop = Desktop.getDesktop();
					URI uri = new URI("https://github.com/paul-mathieu");
					desktop.browse(uri);
				}
			} catch (IOException | URISyntaxException ex) {
				ex.printStackTrace();
			}
		});

		this.setVisible(true);
	}	

}

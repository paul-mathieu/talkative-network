package ePapotage.gui;

import javax.swing.*;
import ePapotage.ePapotage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * This object is the main window of the application. This is the one displayed when the application is launched.
 * The menu "Account" allows you to create an account or connect to an existing account.
 * The menu "Settings" allows you to open the folder containing the data storage files but also to open the link to my GitHub page.
 * The background is an image available in the resource directory.
 */
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
		userLogin.addActionListener(e -> new LogInGUI(conciergeUsername, conciergePassword));

		settingsOpenData.addActionListener(e -> this.openDataDirectory());

		settingsOpenGithub.addActionListener(e -> this.openGitHubAccount());

		this.setVisible(true);
	}


	// ======================================================
	//   Action Listener Methods
	// ======================================================

	public void openDataDirectory(){
		try {
			System.out.println(System.getProperty("user.dir") + "\\ePapotage\\data");
			Runtime.getRuntime().exec("explorer.exe " + System.getProperty("user.dir") + "\\src\\ePapotage\\data");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void openGitHubAccount(){
		try {
			if (Desktop.isDesktopSupported()) {
				Desktop desktop = Desktop.getDesktop();
				URI uri = new URI("https://github.com/paul-mathieu");
				desktop.browse(uri);
			}
		} catch (IOException | URISyntaxException ex) {
			ex.printStackTrace();
		}
	}


}

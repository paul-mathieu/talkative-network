package ePapotage.gui;

import ePapotage.Bavard;
import ePapotage.ePapotage;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

public class ConnectGUI {

    private JFrame frame;
    private JTextField usernameInput;
    private JPasswordField passInput;
    private JLabel usernameLabel;
    private JLabel passLabel;

    private String requestUsername;
    private String requestType;
    private String title;
    private String buttonText;

    private Bavard bavard = new Bavard();


    // The Gui is used to log the Bavard to his BavardFrame
    public ConnectGUI(String requestType, String requestUsername) throws ParserConfigurationException, IOException, SAXException {

        this.requestType = requestType;
        this.requestUsername = requestUsername;

        this.setTitle();

        frame = new JFrame(this.title);
        frame.setBounds(100, 100, 300, 155);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);

        usernameLabel = new JLabel("Username");

        usernameInput = new JTextField();
        usernameInput.setColumns(10);


        // The group layout

        JButton connectButton = new JButton(this.getButtonText());

        GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
        groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(groupLayout
                .createSequentialGroup()
                .addGap(26)
                .addGroup(groupLayout
                        .createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(usernameLabel))
                .addGap(56)
                .addGroup(groupLayout
                        .createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                        .addComponent(connectButton, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(usernameInput, GroupLayout.Alignment.LEADING))
                .addContainerGap(81, Short.MAX_VALUE)));

        groupLayout.setVerticalGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(groupLayout
                .createSequentialGroup()
                .addGap(28)
                .addGroup(groupLayout
                        .createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(usernameLabel)
                        .addComponent(usernameInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGap(18)
                .addComponent(connectButton).addContainerGap(85, Short.MAX_VALUE)));

        frame.getContentPane().setLayout(groupLayout);
        frame.getRootPane().setDefaultButton(connectButton);

        // We add the listener to the log in button
        connectButton.addActionListener(e -> this.connectionToBavard());

        frame.setVisible(true);
    }


    private void setTitle(){
        if (this.requestType.equals("connect")){
            this.setTitle("Connect to a Bavard");
            this.setButtonText("Log in");
        } else if (this.requestType.equals("disconnect")){
            this.setTitle("Disconnect to a Bavard");
            this.setButtonText("Log out");
        } else {
            this.setTitle("");
            this.setButtonText("");
        }
    }

    // ======================================================
    //   Getters
    // ======================================================

    private String getRequestUsername(){return this.requestUsername;}

    public String getButtonText() {return this.buttonText;}

    // ======================================================
    //   Setters
    // ======================================================

    public void setButtonText(String buttonText) {this.buttonText = buttonText;}

    public void setTitle(String title) {this.title = title;}

    // ======================================================
    //   Action Listener Methods
    // ======================================================

    public void connectionToBavard(){
        // === If the Bavard wants to connect to another Bavard ===
        if (this.requestType.equals("connect")){
            if (usernameInput.getText().equals(this.getRequestUsername())) {
                // If the Bavard is trying to connect to himself
                JOptionPane.showMessageDialog(frame, "You don't want to hear yourself talk, do you?");
            } else if (!ePapotage.getConcierge().isBavardExisting(usernameInput.getText())) {
                // If the Bavard is trying to connect to a Bavard that doesn't exist
                JOptionPane.showMessageDialog(frame, "This bavard doesn't exist");
            } else {
                // Else if all is well
                try {
                    ePapotage.getConcierge().connectToBavard(this.getRequestUsername(), usernameInput.getText());
                } catch (ParserConfigurationException | IOException | SAXException | TransformerException ex) {
                    ex.printStackTrace();
                }
                frame.dispose();
            }
            // === If the Bavard wants to disconnect from another Bavard ===
        } else if (this.requestType.equals("disconnect")){
            if (usernameInput.getText().equals(this.getRequestUsername())) {
                // If the Bavard is trying to disconnect from himself
                JOptionPane.showMessageDialog(frame, "You don't want to hear yourself anymore?");
            } else if (!ePapotage.getConcierge().isBavardExisting(usernameInput.getText())) {
                // If the Bavard is trying to connect to a Bavard that doesn't exist
                JOptionPane.showMessageDialog(frame, "This bavard doesn't exist");
            } else {
                // Else if all is well
                try {
                    ePapotage.getConcierge().disconnectToBavard(this.getRequestUsername(), usernameInput.getText());
                } catch (TransformerException | IOException | SAXException | ParserConfigurationException ex) {
                    ex.printStackTrace();
                }
                frame.dispose();
            }
        }
    }

}


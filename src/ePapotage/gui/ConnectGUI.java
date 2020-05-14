package ePapotage.gui;

import ePapotage.Bavard;
import ePapotage.ePapotage;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
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
        connectButton.addActionListener(e -> {});

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
        connectButton.addActionListener(e -> {
            // We get the name and if the Bavard already exists we log to him, otherwise we deny the log in
            if (this.requestType.equals("connect")){
                ePapotage.getConcierge().connectToBavard(this.getRequestUsername(), usernameInput.getText());
                frame.dispose();
            } else if (this.requestType.equals("disconnect")){
                ePapotage.getConcierge().disconnectToBavard(this.getRequestUsername(), usernameInput.getText());
                frame.dispose();
            }
        });

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

    private String getRequestUsername(){return this.requestUsername;}

    public String getButtonText() {return this.buttonText;}

    public void setButtonText(String buttonText) {this.buttonText = buttonText;}

    public String getTitle() {return this.title;}

    public void setTitle(String title) {this.title = title;}
}


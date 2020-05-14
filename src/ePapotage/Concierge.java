package ePapotage;

import ePapotage.gui.ConciergeFrame;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class Concierge {
	
	//variables
	private String name;
	private ConciergeFrame conciergeFrame;
	
	//A list of PapotageListener that will receive the message of one of the PapotageListener has sent
	private ArrayList<PapotageListener> listeners;
	
	//Constructor
	public Concierge () {}

	// ======================================================
	//   Main methods
	// ======================================================

	public static void addBavard(String username, String password) throws ParserConfigurationException, TransformerException, IOException, SAXException {

		File file = new File("src/ePapotage/data/accounts.xml");
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
		document.getDocumentElement().normalize();

		// Root Element
		Element rootElement = (Element) document.getElementsByTagName("accounts").item(0);

		Element accountElement = document.createElement("account");
		rootElement.appendChild(accountElement);

		Element usernameElement = document.createElement("username");
		usernameElement.appendChild(document.createTextNode(username));
		accountElement.appendChild(usernameElement);

		Element passwordElement = document.createElement("password");
		passwordElement.appendChild(document.createTextNode(password));
		accountElement.appendChild(passwordElement);

		Element permissionElement = document.createElement("permission");
		permissionElement.appendChild(document.createTextNode("bavard"));
		accountElement.appendChild(permissionElement);

		Element listenElement = document.createElement("listen");
		listenElement.appendChild(document.createTextNode(""));
		accountElement.appendChild(listenElement);

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		// Beautifier of the new xml entry
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

		DOMSource source = new DOMSource(document);

		StreamResult result = new StreamResult("src/ePapotage/data/accounts.xml");
		transformer.transform(source, result);
	}

	public void addListener(PapotageListener listener) {
		listeners.add(listener);
	}

	public void removeListener(PapotageListener listener) {
		listeners.remove(listener);
	}

	public void sendMessage(String username, String message){
		// get listeners of username
		// send to listeners if connected
		// keep message in xml
	}

	public void connectToBavard(String requestUsername, String listenerUsername){

	}

	public void disconnectToBavard(String requestUsername, String listenerUsername){

	}

	// ======================================================
	//   Getters
	// ======================================================

	public String getName() {return name;}

	public ArrayList<PapotageListener> getListeners() {return listeners;}

	public ArrayList<PapotageListener> getListenersConnected() {return listeners;}

	public ConciergeFrame getConciergeFrame(){return this.conciergeFrame;}


	// ======================================================
	//   Setters
	// ======================================================

	public void setName(String name) {this.name = name;}

	public void setListeners(ArrayList<PapotageListener> listeners) {this.listeners = listeners;}

	public void setConciergeFrame(){
		this.conciergeFrame = new ConciergeFrame();
		this.conciergeFrame.setVisible(true);
	}

}

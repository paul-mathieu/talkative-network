package ePapotage;

import ePapotage.gui.BavardFrame;
import ePapotage.gui.ConciergeFrame;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;


public class Concierge {
	
	//variables
	private String name;
	private ConciergeFrame conciergeFrame;
	public Concierge concierge;
	
	//A list of PapotageListener that will receive the message of one of the PapotageListener has sent
	private ArrayList<PapotageListener> listeners;
	
	//Constructor
	public Concierge () {
		concierge = this;
		this.setConciergeFrame();
	}

	// ======================================================
	//   Main methods
	// ======================================================

	// === Part Add Bavard ===

	public static void addBavard(String username, String password) throws ParserConfigurationException, TransformerException, IOException, SAXException {

		Document document = getAccountsFile();

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

		transfromToNewXML(document, "src/ePapotage/data/accounts.xml");

	}

	public boolean isBavardExisting(String username){
		return true;
	}


	// === Part Send Message ===

	public void sendMessage(String username, String message){
		// date
		String strDate = ePapotage.getDate();

		// get listeners of username

		// send to listeners if connected
		try {
			for (String name: this.getBavardsWhoListen(username)){
				if (ePapotage.isBavardConnected(name)){
					Objects.requireNonNull(ePapotage.getBavardFromName(name)).getBavardFrame().writeMessage(username, strDate, message);
				}
			}
			Objects.requireNonNull(ePapotage.getBavardFromName(username)).getBavardFrame().writeMessage("you", strDate, message);
		} catch (IOException | SAXException | ParserConfigurationException e) {
			e.printStackTrace();
		}

		// keep message in the xml file
		try {
			this.storeMessage(username, strDate, message);
		} catch (TransformerException | IOException | SAXException | ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	private void storeMessage(String username, String date, String content) throws TransformerException, IOException, SAXException, ParserConfigurationException {
		Document document = getMessagesFile();

		// Root Element
		Element rootElement = (Element) document.getElementsByTagName("messages").item(0);

		Element accountElement = document.createElement("message");
		rootElement.appendChild(accountElement);

		Element usernameElement = document.createElement("username");
		usernameElement.appendChild(document.createTextNode(username));
		accountElement.appendChild(usernameElement);

		Element dateElement = document.createElement("date");
		dateElement.appendChild(document.createTextNode(date));
		accountElement.appendChild(dateElement);

		Element listenersElement = document.createElement("listeners");
		Element listenerElement;
		for (String name : this.getBavardsWhoListen(username)){
			listenerElement = document.createElement("listener");
			listenerElement.appendChild(document.createTextNode(name));
			listenersElement.appendChild(listenerElement);
		}
		accountElement.appendChild(listenersElement);

		Element contentElement = document.createElement("content");
		contentElement.appendChild(document.createTextNode(content));
		accountElement.appendChild(contentElement);

		transfromToNewXML(document, "src/ePapotage/data/messages.xml");

	}

	public void loadOldMessages(String username, BavardFrame bavardFrame) throws IOException, SAXException, ParserConfigurationException {
		Document document = getMessagesFile();

		NodeList nodeList = document.getElementsByTagName("message");
		NodeList nodeListListeners;
		boolean wasListened;

		String message;
		String date;
		String bavardUsername;

		Element element;
		Element elementListener;
		Element elementUsername;

		for (int index = 0; index < nodeList.getLength(); index++) {
			element = (Element) nodeList.item(index);
			elementUsername = (Element) element.getElementsByTagName("username").item(0);
			//If the message comes from the user
			if (elementUsername.getTextContent().equals(username)) {
				message = element.getElementsByTagName("content").item(0).getTextContent();
				date = element.getElementsByTagName("date").item(0).getTextContent();
				bavardFrame.writeMessage("you", date, message);
			} else {
				wasListened = false;
				nodeListListeners = element.getElementsByTagName("listener");
				for (int indexListener = 0; indexListener < nodeListListeners.getLength(); indexListener++) {
					elementListener = (Element) nodeListListeners.item(indexListener);
					if (elementListener.getTextContent().equals(username)){
						wasListened = true;
						break;
					}
				}
				//If the message was heard by the user
				if (wasListened) {
					message = element.getElementsByTagName("content").item(0).getTextContent();
					date = element.getElementsByTagName("date").item(0).getTextContent();
					bavardUsername = elementUsername.getTextContent();
					bavardFrame.writeMessage(bavardUsername, date, message);
				}
			}
		}

	}


	// === Part Connect Listeners ===

	public void connectToBavard(String requestUsername, String listenerUsername) throws ParserConfigurationException, IOException, SAXException, TransformerException {

		Document document = getAccountsFile();

		// Root Element
		Element rootElement = (Element) document.getElementsByTagName("accounts").item(0);
		NodeList nodeList = document.getElementsByTagName("account");
		// nodeList is not iterable, so we are using for loop
		Element element;
		for (int index = 0; index < nodeList.getLength(); index++) {
			element = (Element) nodeList.item(index);
			if (element.getElementsByTagName("username").item(0).getTextContent().equals(requestUsername)) {
				Element usernameElement = document.createElement("listener");
				usernameElement.appendChild(document.createTextNode(listenerUsername));
				element.getElementsByTagName("listen").item(0).appendChild(usernameElement);
				break;
			}
		}
		transfromToNewXML(document, "src/ePapotage/data/accounts.xml");

		String log = "The Bavard " + requestUsername + " started listening to " + listenerUsername + ".";
		ePapotage.getConcierge().getConciergeFrame().writeMessage(log);

	}

	public void disconnectToBavard(String requestUsername, String listenerUsername) throws TransformerException, IOException, SAXException, ParserConfigurationException {

		Document document = getAccountsFile();

		// Root Element
		Element rootElement = (Element) document.getElementsByTagName("accounts").item(0);
		NodeList nodeList = document.getElementsByTagName("account");
		// nodeList is not iterable, so we are using for loop
		Element element;
		Element listenerElement;
		Element listenElement;
		String username;
		// for each account
		for (int indexAccount = 0; indexAccount < nodeList.getLength(); indexAccount++) {
			element = (Element) nodeList.item(indexAccount);
			username = element.getElementsByTagName("username").item(0).getTextContent();
			// if it's the research account
			if (username.equals(requestUsername)) {
				listenElement = (Element) element.getElementsByTagName("listen").item(0);
				// For each listeners
				for (int indexListener = 0; indexListener < nodeList.getLength(); indexListener++) {
					listenerElement = (Element) listenElement.getElementsByTagName("listener").item(indexListener);
					// if it's the research username to delete
					if (listenerElement.getTextContent().equals(listenerUsername)){
						listenerElement.getParentNode().removeChild(listenerElement);
						break;
					}
				}
				break;
			}
		}
		transfromToNewXML(document, "src/ePapotage/data/accounts.xml");

		String log = "The Bavard " + requestUsername + " stopped listening to " + listenerUsername + ".";
		ePapotage.getConcierge().getConciergeFrame().writeMessage(log);

	}

	// ======================================================
	//   Getters
	// ======================================================

	public String getName() {return name;}

	public ArrayList<PapotageListener> getListeners() {return this.listeners;}

	public ArrayList<PapotageListener> getListenersConnected() {return listeners;}

	public ConciergeFrame getConciergeFrame(){return this.conciergeFrame;}

	// Function which returns the Bavards who listen to the Bavard whose username is given in parameters
	// (fr: liste des bavards qu'il écoute)
	public ArrayList<String> getBavardsWhoListen(String username) throws IOException, SAXException, ParserConfigurationException {

		ArrayList<String> bavardsWhoListenList = new ArrayList<>();
		NodeList nodeList = getAccountsList();

		Element element;
		Element listenElement;
		NodeList listenList;
		Element usernameElement;
		// for each account
		for (int indexAccount = 0; indexAccount < nodeList.getLength(); indexAccount++) {
			element = (Element) nodeList.item(indexAccount);
			usernameElement = (Element) element.getElementsByTagName("username").item(0);
			if (!usernameElement.getTextContent().equals("admin")) {
				listenElement = (Element) element.getElementsByTagName("listen").item(0);
				listenList = listenElement.getElementsByTagName("listener");
				for (int indexListener = 0; indexListener < listenList.getLength(); indexListener++) {
					if (listenList.item(indexListener).getTextContent().equals(username)) {
						bavardsWhoListenList.add(usernameElement.getTextContent());
						break;
					}
				}
			}
		}

		return bavardsWhoListenList;
	}

	// Function which returns the Bavards who are listened to by the Bavard whose username is given in parameters
	// (fr: liste des bavards qui l'écoutent)
	public ArrayList<String> getBavardsWhoAreListenedTo(String username) throws IOException, SAXException, ParserConfigurationException {

		ArrayList<String> bavardsWhoAreListenedToList = new ArrayList<>();
		NodeList nodeList = getAccountsList();

		Element element;
		// for each account
		for (int indexAccount = 0; indexAccount < nodeList.getLength(); indexAccount++) {
			element = (Element) nodeList.item(indexAccount);
			if (element.getElementsByTagName("username").item(0).getTextContent().equals(username)){
				Element listenElement = (Element) element.getElementsByTagName("listen").item(0);
				NodeList listenList = listenElement.getElementsByTagName("listener");
				for (int indexListener = 0; indexListener < listenList.getLength(); indexListener++) {
					bavardsWhoAreListenedToList.add(listenList.item(indexListener).getTextContent());
				}
				break;
			}
		}

		return bavardsWhoAreListenedToList;
	}

	public int getNumberMessages() throws IOException, SAXException, ParserConfigurationException {
		Document document = getMessagesFile();
		Element rootElement = (Element) document.getElementsByTagName("messages").item(0);
		NodeList nodeList = document.getElementsByTagName("message");

		return nodeList.getLength();
	}

	public int getNumberMessages(String username) throws IOException, SAXException, ParserConfigurationException {
		int total = 0;
		Document document = getMessagesFile();
		Element rootElement = (Element) document.getElementsByTagName("messages").item(0);
		NodeList nodeList = document.getElementsByTagName("message");
		Element element;
		for (int index = 0; index < nodeList.getLength(); index++) {
			element = (Element) nodeList.item(index);
			if (element.getElementsByTagName("username").item(0).getTextContent().equals(username)) {
				total++;
			}
		}
		return total;
	}


	public NodeList getAccountsList() throws IOException, SAXException, ParserConfigurationException {
		Document document = getAccountsFile();
//		System.out.println("Root element: " + document.getDocumentElement().getNodeName());
		NodeList nodeList = document.getElementsByTagName("account");
		return nodeList;
	}


	// ======================================================
	//   Setters
	// ======================================================

	public void setName(String name) {this.name = name;}

	public void setListeners(ArrayList<PapotageListener> listeners) {this.listeners = listeners;}

	public void setConciergeFrame(){
		this.conciergeFrame = new ConciergeFrame(this.concierge);
		this.conciergeFrame.setVisible(false);
	}


	// ======================================================
	//   Other methods
	// ======================================================

	private static Document getAccountsFile() throws ParserConfigurationException, IOException, SAXException {
		File file = new File("src/ePapotage/data/accounts.xml");
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
		document.getDocumentElement().normalize();
		return document;
	}

	private static Document getMessagesFile() throws ParserConfigurationException, IOException, SAXException {
		File file = new File("src/ePapotage/data/messages.xml");
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
		document.getDocumentElement().normalize();
		return document;
	}

	private static void transfromToNewXML(Document document, String path) throws TransformerException {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		// Beautifier of the new xml entry
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(OutputKeys.INDENT, "no");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		document.getDocumentElement().normalize();
		// Export
		DOMSource source = new DOMSource(document);
		StreamResult result = new StreamResult(path);
		transformer.transform(source, result);
	}

}

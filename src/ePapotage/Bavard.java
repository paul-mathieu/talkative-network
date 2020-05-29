package ePapotage;

import ePapotage.gui.BavardFrame;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;

public class Bavard implements PapotageListener {
	
	//Variables
	private String name;
	BavardFrame bavardFrame;

	//Constructor
	public Bavard (String name) {
		this.name = name;
		this.bavardFrame = new BavardFrame(this.name);
		this.bavardFrame.setVisible(true);
		// load old messages
		try {
			ePapotage.getConcierge().loadOldMessages(this.getName(), this.getBavardFrame());
		} catch (IOException | SAXException | ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	public Bavard(){}

	// ======================================================
	//   Main methods
	// ======================================================

	//Overridden methods inherited from the PapotageListener Interface
	@Override
	public String getName() {return this.name;}

	@Override
	public void sendMessage(String message) {
		// Send the message to the concierge
		ePapotage.getConcierge().sendMessage(new PapotageEvent(this, message));
	}

	public String getBavardPassword(String bavardName) throws ParserConfigurationException, IOException, SAXException {
		if (this.isBavard(bavardName)){
			Element element = (Element) this.getBavardNode(bavardName);
			return element.getElementsByTagName("password").item(0).getTextContent();
		} else {
			return null;
		}
	}

	public ArrayList<String> getBavardListeners(String bavardName) throws ParserConfigurationException, SAXException, IOException {
		ArrayList<String> bavardListenList = new ArrayList<>();
		if (!this.isBavard(bavardName)){
			Element element = (Element) this.getBavardNode(bavardName);
			NodeList bavardListenNodes = element.getElementsByTagName("listen");
			Node node;
			for (int i = 0; i < bavardListenNodes.getLength(); i++) {
				node = bavardListenNodes.item(i);
				bavardListenList.add(element.getElementsByTagName("username").item(0).getTextContent());
			}
		}
		return bavardListenList;
	}


	// ======================================================
	//   Getters
	// ======================================================

	public Node getBavardNode(String bavardName) throws IOException, SAXException, ParserConfigurationException {
		NodeList nodeListName = ePapotage.getAccountsFile().getElementsByTagName("account");
		Node node;
		for (int i = 0; i < nodeListName.getLength(); i++) {
			node = nodeListName.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE){
				Element element = (Element) node;
				if (element.getElementsByTagName("username").item(0).getTextContent().equals(bavardName)){
					return element;
				}
			}
		}
		return null;
	}

	public BavardFrame getBavardFrame() {return this.bavardFrame;}

	// ======================================================
	//   Setters
	// ======================================================

	public void setName(String name) {
		this.name = name;
	}


	// ======================================================
	//   Other Methods
	// ======================================================

	public boolean isExisting(String bavardName) throws IOException, SAXException, ParserConfigurationException {
		return !(this.getBavardNode(bavardName) == null);
	}

	public boolean isBavard(String bavardName) throws IOException, SAXException, ParserConfigurationException {
		if (this.isExisting(bavardName)) {
			Element bavardElement = (Element) this.getBavardNode(bavardName);
			return bavardElement.getElementsByTagName("permission").item(0).getTextContent().equals("bavard");
		} else {
			return false;
		}
	}


}

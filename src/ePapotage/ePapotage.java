package ePapotage;

import java.io.IOException;
import java.util.ArrayList;

import ePapotage.gui.AdministratorGUI;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.File;


public class ePapotage {

	//The admin GUI which is the first menu we encounter
	private static AdministratorGUI administratorGUI;
	
	//	A list of BavardFrame
	private static ArrayList<Bavard> bavardsConnected;

	// The ConciergeFrame
	private static Concierge concierge = new Concierge();

	public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {

		//We initialize an empty set of ConciergeFrame and BavardFrame
		setBavardFrames(new ArrayList<>());

		//We create the administrator menu
		setAdministratorGUI(new AdministratorGUI());

//		System.out.println(new Bavard().isExisting("paul"));
	}

	// ======================================================
	//   Main methods
	// ======================================================

	public static void addBavard(Bavard bavard){
		ePapotage.bavardsConnected.add(bavard);
	}

	public static void removeBavard(String bavardUsername){
		ArrayList<Bavard> bavardsConnected = ePapotage.getBavardsConnected();
		ArrayList<Bavard> newBavardsConnected = new ArrayList<>();
		for (Bavard b: bavardsConnected){
			if (!b.getName().equals(bavardUsername)){
				newBavardsConnected.add(b);
			} else {
				// on prévient le concierge que le bavard est parti
			}
		}
		ePapotage.setBavardsConnected(newBavardsConnected);
	}

	public static Document getAccountsFile() throws ParserConfigurationException, IOException, SAXException {
		File file = new File("src/ePapotage/data/accounts.xml");
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
		doc.getDocumentElement().normalize();
		return doc;
	}

	public static Bavard getBavardFrameFromName(String bavardUsername) {
		for (Bavard b : getBavardsConnected()) {
			if (b.getName().equalsIgnoreCase(bavardUsername)) {
				return b;
			}
		}
		return null;
	}


	// ======================================================
	//   Getters
	// ======================================================

	public static AdministratorGUI getAdministratorGUI() {return administratorGUI;}

	public static ArrayList<Bavard> getBavardsConnected() {return bavardsConnected;}

	public static Concierge getConcierge() {return concierge;}


	// ======================================================
	//   Setters
	// ======================================================

	public static void setAdministratorGUI(AdministratorGUI administratorGUI) {
		ePapotage.administratorGUI = administratorGUI;
	}

	public static void setBavardFrames(ArrayList<Bavard> bavards) {
		ePapotage.bavardsConnected = bavards;
	}

	public static void setBavardsConnected(ArrayList<Bavard> newBavardsConnected){
		ePapotage.bavardsConnected = newBavardsConnected;
	}

}

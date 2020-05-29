package ePapotage;

/**
 * This object is papotage listener.
 * This is the interface that allows you to manage listeners.
 */
public interface PapotageListener {
	// The method of the Interface which will be overridden
	void sendMessage (String text);
	String getName();

}
	
package ePapotage;

import java.util.EventObject;

/**
 * This object is the papotage event.
 * It is the class of the event which makes it possible to manage the messages.
 * He super a bavard object.
 * It takes as a parameter a bavard and the content of the message he wants to send.
 */
public class PapotageEvent extends EventObject {

    private String content;

    // ======================================================
    //   Main methods
    // ======================================================

    public PapotageEvent(Object source, String content) {
        super(source);
        this.content = content;
    }

    // ======================================================
    //   Getters
    // ======================================================

    public String getBavardName() {
        Bavard bavard = (Bavard) this.source;
        return bavard.getName();
    }

    public String getContent() {
        return this.content;
    }
}

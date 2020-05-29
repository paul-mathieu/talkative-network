package ePapotage;

import java.util.EventObject;

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

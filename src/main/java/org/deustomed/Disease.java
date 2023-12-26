package org.deustomed;

public class Disease {
    private String name;
    private boolean chronic;
    private boolean hereditary;

    public Disease(String name, boolean chronic, boolean hereditary) {
        this.name = name;
        this.chronic = chronic;
        this.hereditary = hereditary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChronic() {
        return chronic;
    }

    public void setChronic(boolean chronic) {
        this.chronic = chronic;
    }

    public boolean isHereditary() {
        return hereditary;
    }

    public void setHereditary(boolean hereditary) {
        this.hereditary = hereditary;
    }
}

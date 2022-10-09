package de.unisaarland.cs.se.selab.datapackage;

public class Tunnel {
    private boolean conquered;

    public Tunnel() {
        conquered = false;
    }

    public boolean isConquered() {
        return conquered;
    }

    public void setConquered() {
        conquered = true;
    }

    public boolean isRoom() {
        return false;
    }
}
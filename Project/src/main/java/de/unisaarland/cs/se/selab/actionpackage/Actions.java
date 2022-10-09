package de.unisaarland.cs.se.selab.actionpackage;

import de.unisaarland.cs.se.selab.gamelogic.State;

/**
 * Superclass for Actions
 **/
public abstract class Actions {

    final int commID;

    public Actions(final int commID) {
        this.commID = commID;
    }

    public int getCommID() {
        return commID;
    }

    /**
     * method that applies visitor pattern
     */
    public abstract void accept(State state);
}
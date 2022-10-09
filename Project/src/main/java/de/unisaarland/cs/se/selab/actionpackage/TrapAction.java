package de.unisaarland.cs.se.selab.actionpackage;

import de.unisaarland.cs.se.selab.gamelogic.State;

public class TrapAction extends Actions {

    private final int trapID;

    public TrapAction(final int commID, final int trapID) {
        super(commID);
        this.trapID = trapID;
    }

    public int getTrapID() {
        return trapID;
    }

    @Override
    public void accept(final State state) {
        state.actOn(this);
    }
}
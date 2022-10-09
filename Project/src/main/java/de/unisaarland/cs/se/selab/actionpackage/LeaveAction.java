package de.unisaarland.cs.se.selab.actionpackage;

import de.unisaarland.cs.se.selab.gamelogic.State;

public class LeaveAction extends Actions {

    public LeaveAction(final int commID) {
        super(commID);
    }

    @Override
    public void accept(final State state) {
        state.actOn(this);
    }
}
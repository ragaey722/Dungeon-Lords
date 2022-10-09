package de.unisaarland.cs.se.selab.actionpackage;

import de.unisaarland.cs.se.selab.gamelogic.State;

public class EndTurnAction extends Actions {

    public EndTurnAction(final int commID) {
        super(commID);
    }

    @Override
    public void accept(final State state) {
        state.actOn(this);
    }
}
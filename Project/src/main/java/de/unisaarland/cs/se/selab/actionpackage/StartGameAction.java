package de.unisaarland.cs.se.selab.actionpackage;

import de.unisaarland.cs.se.selab.gamelogic.State;

public class StartGameAction extends Actions {

    public StartGameAction(final int commID) {
        super(commID);
    }

    @Override
    public void accept(final State state) {
        state.actOn(this);
    }
}
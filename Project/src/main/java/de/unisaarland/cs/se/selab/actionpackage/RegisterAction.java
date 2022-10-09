package de.unisaarland.cs.se.selab.actionpackage;

import de.unisaarland.cs.se.selab.gamelogic.State;

public class RegisterAction extends Actions {

    private final String playerName;

    public RegisterAction(final int commID, final String playerName) {
        super(commID);
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    @Override
    public void accept(final State state) {
        state.actOn(this);
    }
}
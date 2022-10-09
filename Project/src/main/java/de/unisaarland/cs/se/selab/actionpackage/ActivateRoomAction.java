package de.unisaarland.cs.se.selab.actionpackage;

import de.unisaarland.cs.se.selab.gamelogic.State;

public class ActivateRoomAction extends Actions {

    private final int room;

    public ActivateRoomAction(final int commID, final int room) {
        super(commID);
        this.room = room;
    }

    public int getRoom() {
        return room;
    }

    @Override
    public void accept(final State state) {
        state.actOn(this);
    }
}
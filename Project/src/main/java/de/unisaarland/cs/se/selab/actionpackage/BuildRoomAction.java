package de.unisaarland.cs.se.selab.actionpackage;

import de.unisaarland.cs.se.selab.gamelogic.State;

public class BuildRoomAction extends Actions {

    private final int positionX;
    private final int positionY;
    private final int roomID;

    public BuildRoomAction(final int commID, final int x, final int y, final int roomID) {
        super(commID);
        this.positionX = x;
        this.positionY = y;
        this.roomID = roomID;
    }

    public int getX() {
        return positionX;
    }

    public int getY() {
        return positionY;
    }

    public int getRoomID() {
        return roomID;
    }

    @Override
    public void accept(final State state) {
        state.actOn(this);
    }
}
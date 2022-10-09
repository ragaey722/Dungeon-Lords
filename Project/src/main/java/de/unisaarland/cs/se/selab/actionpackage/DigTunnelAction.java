package de.unisaarland.cs.se.selab.actionpackage;

import de.unisaarland.cs.se.selab.gamelogic.State;

public class DigTunnelAction extends Actions {

    private final int positionX;
    private final int positionY;

    public DigTunnelAction(final int commID, final int x, final int y) {
        super(commID);
        this.positionX = x;
        this.positionY = y;
    }

    public int getX() {
        return positionX;
    }

    public int getY() {
        return positionY;
    }

    @Override
    public void accept(final State state) {
        state.actOn(this);
    }
}
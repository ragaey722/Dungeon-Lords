package de.unisaarland.cs.se.selab.actionpackage;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.gamelogic.State;

public class PlaceBidAction extends Actions {

    private final BidType bid;
    private final int number;

    public PlaceBidAction(final int commID, final BidType bid, final int number) {
        super(commID);
        this.bid = bid;
        this.number = number;
    }

    public BidType getBid() {
        return bid;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public void accept(final State state) {
        state.actOn(this);
    }
}
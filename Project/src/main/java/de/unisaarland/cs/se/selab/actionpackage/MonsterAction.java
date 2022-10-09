package de.unisaarland.cs.se.selab.actionpackage;

import de.unisaarland.cs.se.selab.gamelogic.State;

public class MonsterAction extends Actions {

    private final int monster;

    public MonsterAction(final int commID, final int monster) {
        super(commID);
        this.monster = monster;
    }

    public int getMonster() {
        return monster;
    }

    @Override
    public void accept(final State state) {
        state.actOn(this);
    }
}
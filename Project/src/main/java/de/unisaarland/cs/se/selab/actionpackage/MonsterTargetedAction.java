package de.unisaarland.cs.se.selab.actionpackage;

import de.unisaarland.cs.se.selab.gamelogic.State;

public class MonsterTargetedAction extends Actions {

    private final int monster;
    private final int position;

    public MonsterTargetedAction(final int commID, final int moster, final int position) {
        super(commID);
        this.monster = moster;
        this.position = position;
    }

    public int getMonster() {
        return monster;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public void accept(final State state) {
        state.actOn(this);
    }
}
package de.unisaarland.cs.se.selab.actionpackage;

import de.unisaarland.cs.se.selab.comm.ActionFactory;
import de.unisaarland.cs.se.selab.comm.BidType;

public class ConcreteActionFactory implements ActionFactory<Actions> {


    @Override
    public Actions createActivateRoom(final int i, final int i1) {
        return new ActivateRoomAction(i, i1);
    }

    @Override
    public Actions createBattleGround(final int i, final int i1, final int i2) {
        return new BattleGroundAction(i, i1, i2);
    }

    @Override
    public Actions createBuildRoom(final int i, final int i1, final int i2, final int i3) {
        return new BuildRoomAction(i, i1, i2, i3);
    }

    @Override
    public Actions createEndTurn(final int i) {
        return new EndTurnAction(i);
    }

    @Override
    public Actions createHireMonster(final int i, final int i1) {
        return new HireMonsterAction(i, i1);
    }

    @Override
    public Actions createLeave(final int i) {
        return new LeaveAction(i);
    }

    @Override
    public Actions createMonster(final int i, final int i1) {
        return new MonsterAction(i, i1);
    }

    @Override
    public Actions createMonsterTargeted(final int i, final int i1, final int i2) {
        return new MonsterTargetedAction(i, i1, i2);
    }

    @Override
    public Actions createPlaceBid(final int i, final BidType bidType, final int i1) {
        return new PlaceBidAction(i, bidType, i1);
    }

    @Override
    public Actions createDigTunnel(final int i, final int i1, final int i2) {
        return new DigTunnelAction(i, i1, i2);
    }

    @Override
    public Actions createRegister(final int i, final String s) {
        return new RegisterAction(i, s);
    }

    @Override
    public Actions createStartGame(final int i) {
        return new StartGameAction(i);
    }

    @Override
    public Actions createTrap(final int i, final int i1) {
        return new TrapAction(i, i1);
    }
}
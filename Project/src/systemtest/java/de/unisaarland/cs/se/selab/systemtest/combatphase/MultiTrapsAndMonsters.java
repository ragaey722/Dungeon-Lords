package de.unisaarland.cs.se.selab.systemtest.combatphase;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.HelperClass;

public class MultiTrapsAndMonsters extends HelperClass {

    public MultiTrapsAndMonsters() {
        super(MultiTrapsAndMonsters.class, false);
    }

    @Override
    public void run() throws TimeoutException {
        wrapperBeforeCombat();
        // round 1 for player 0
        nextRound(1);
        assertSetBattleGround(0);
        leave(0, 0);
        // round 1 for player 2
        nextRound(1);
        assertSetBattleGround(1);
        assertActNow(1);
        leave(2, 2);
        sendBattleGround(1, 0, 0);
        battleGroundSet(1, 0, 0);
        assertDefendYourself(1);
        assertActNow(1);
        sendEndTurn(1);
        adventurerDamaged(2, 2);
        adventurerDamaged(18, 2);
        adventurerDamaged(9, 2);
        tunnelConquered(2, 0, 0);
        evilnessChanged(-1, 1);
        adventurerHealed(1, 9, 2);
        // round 2  for player 2
        nextRound(2);
        assertSetBattleGround(1);
        assertActNow(1);
        sendBattleGround(1, 0, 1);
        battleGroundSet(1, 0, 1);
        assertDefendYourself(1);
        assertActNow(1);
        sendEndTurn(1);
        adventurerDamaged(2, 2);
        adventurerImprisoned(18);
        adventurerImprisoned(9);
        tunnelConquered(2, 0, 1);
        evilnessChanged(-1, 1);
        // round 3  for player 2
        nextRound(3);
        assertSetBattleGround(1);
        assertActNow(1);
        sendBattleGround(1, 3, 0);
        assertActionFailed(1);
        assertActNow(1);
        sendBattleGround(1, 1, 0);
        battleGroundSet(1, 1, 0);
        assertDefendYourself(1);
        assertActNow(1);
        sendEndTurn(1);
        adventurerImprisoned(2);
        nextYear(2);
        leave();
    }
}
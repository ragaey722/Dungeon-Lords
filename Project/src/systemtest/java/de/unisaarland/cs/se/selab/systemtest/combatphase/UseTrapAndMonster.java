package de.unisaarland.cs.se.selab.systemtest.combatphase;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.HelperClass;

public class UseTrapAndMonster extends HelperClass {

    public UseTrapAndMonster() {
        super(MultiTrapsAndMonsters.class, false);
    }

    @Override
    public void run() throws TimeoutException {
        wrapperBeforeCombat();
        // round 1 for player 0
        nextRound(1);
        assertSetBattleGround(0);
        assertActNow(0);
        // player 1,2 leave
        leave(1, 1);
        leave(2, 2);
        // they leaved
        sendBattleGround(0, 0, 0);
        battleGroundSet(0, 0, 0);
        assertDefendYourself(0);
        assertActNow(0);
        sendTrap(0, 6);
        trapPlaced(0, 6);
        assertActNow(0);
        sendTrap(0, 26);
        assertActionFailed(0);
        assertActNow(0);
        sendMonster(0, 9);
        monsterPlaced(9, 0);
        assertActNow(0);
        sendMonster(0, 7);
        assertActionFailed(0);
        assertActNow(0);
        sendEndTurn(0);
        adventurerDamaged(11, 1);
        adventurerDamaged(6, 2);
        adventurerDamaged(23, 2);
        adventurerDamaged(11, 2);
        adventurerDamaged(6, 2);
        adventurerDamaged(23, 2);
        adventurerImprisoned(11);
        tunnelConquered(6, 0, 0);
        evilnessChanged(-1, 0);
        adventurerHealed(2, 6, 6);
        adventurerHealed(2, 23, 6);
        adventurerHealed(1, 23, 23);
        // round 2
        nextRound(2);
        assertSetBattleGround(0);
        assertActNow(0);
        sendActivateRoom(0, 0);
        assertActionFailed(0);
        assertActNow(0);
        sendBattleGround(0, 8, 0);
        assertActionFailed(0);
        assertActNow(0);
        sendBattleGround(0, 3, 0);
        assertActionFailed(0);
        assertActNow(0);
        sendBattleGround(0, 0, 0);
        assertActionFailed(0);
        assertActNow(0);
        sendBattleGround(0, 1, 0);
        battleGroundSet(0, 1, 0);
        assertDefendYourself(0);
        assertActNow(0);
        sendTrap(0, 26);
        trapPlaced(0, 26);
        assertActNow(0);
        sendMonster(0, 7);
        monsterPlaced(7, 0);
        assertActNow(0);
        sendEndTurn(0);
        adventurerDamaged(6, 1);
        adventurerDamaged(6, 1);
        adventurerDamaged(23, 1);
        adventurerDamaged(6, 2);
        adventurerImprisoned(23);
        tunnelConquered(6, 1, 0);
        evilnessChanged(-1, 0);
        adventurerHealed(2, 6, 6);
        // round 3
        nextRound(3);
        assertSetBattleGround(0);
        assertActNow(0);
        sendBattleGround(0, 2, 0);
        battleGroundSet(0, 2, 0);
        assertDefendYourself(0);
        assertActNow(0);
        sendEndTurn(0);
        adventurerDamaged(6, 2);
        tunnelConquered(6, 2, 0);
        evilnessChanged(-1, 0);
        adventurerHealed(2, 6, 6);
        // round 4
        nextRound(4);
        assertSetBattleGround(0);
        assertActNow(0);
        leave(0, 0);
        // everyone leave
    }
}
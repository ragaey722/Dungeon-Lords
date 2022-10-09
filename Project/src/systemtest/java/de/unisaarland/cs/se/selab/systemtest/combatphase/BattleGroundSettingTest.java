package de.unisaarland.cs.se.selab.systemtest.combatphase;

import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.HelperClass;

public class BattleGroundSettingTest extends HelperClass {
    /* need to test:
     * BattlegroundFarFarAway
     * BattlegroundOutOfBounds
     * NoTileNoBattleground
     * SimpleBattleground*/

    public BattleGroundSettingTest() {
        super(BattleGroundSettingTest.class, false);
    }

    @Override
    public void run() throws TimeoutException {
        wrapperBeforeCombat();
        nextRound(1);
        assertSetBattleGround(0);
        assertActNow(0);
        // BattlegroundFarFarAway
        sendBattleGround(0, 0, 3);
        assertActionFailed(0);
        assertActNow(0);
        // BattlegroundOutOfBounds
        sendBattleGround(0, 4, 5);
        assertActionFailed(0);
        assertActNow(0);
        // SimpleBattleground
        sendBattleGround(0, 0, 0);
        battleGroundSet(0, 0, 0);
        leave(0, 0);

        nextRound(1);
        assertSetBattleGround(1);
        leave(1, 1);

        nextRound(1);
        assertSetBattleGround(2);
        assertActNow(2);
        sendBattleGround(2, 0, 0);
        battleGroundSet(2, 0, 0);
        assertDefendYourself(2);
        assertActNow(2);
        // try wrong trap
        sendTrap(2, 6);
        assertActionFailed(2);
        assertActNow(2);
        // try wrong monster
        sendMonster(2, 4);
        assertActionFailed(2);
        assertActNow(2);
        // set right monster but without target
        sendMonster(2, 13);
        assertActionFailed(2);
        // right set
        assertActNow(2);
        sendMonsterTargeted(2, 13, 2);
        monsterPlaced(13, 2);
        assertActNow(2);
        sendEndTurn(2);

        // start execute damage
        // monster damage
        adventurerImprisoned(0);
        // fatigue damage
        adventurerDamaged(20, 2);
        adventurerDamaged(29, 2);
        // conquer
        tunnelConquered(20, 0, 0);
        evilnessChanged(-1, 2);
        // heal execute
        adventurerHealed(2, 29, 20);

        nextRound(2);
        adventurerFled(0);
        evilnessChanged(-1, 2);

        nextRound(3);
        nextRound(4);
        nextYear(2);

        leave();
    }
}

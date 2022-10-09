package de.unisaarland.cs.se.selab.systemtest.combatphase;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.HelperClass;
import java.util.List;

public class FullBattleAdaptationForOneBattlePhase extends HelperClass {

    public FullBattleAdaptationForOneBattlePhase() {
        super(FullBattleAdaptationForOneBattlePhase.class, false);
    }

    @Override
    public void run() throws TimeoutException {
        wrapperBeforeCombat();
        // round 1 for player 0
        nextRound(1);
        assertSetBattleGround(0);
        assertActNow(0);
        leave(0, 0);
        // round 1 for palyer 1
        nextRound(1);
        assertSetBattleGround(1);
        assertActNow(1);
        leave(1, 1);
        // round 1 for player 2
        nextRound(1);
        assertSetBattleGround(2);
        assertActNow(2);
        sendBattleGround(2, 0, 0);
        battleGroundSet(2, 0, 0);
        assertDefendYourself(2);
        assertActNow(2);
        sendMonster(2, 3);
        monsterPlaced(3, 2);
        assertActNow(2);
        sendMonster(2, 13);
        assertActionFailed(2);
        assertActNow(2);
        sendEndTurn(2);
        adventurerDamaged(20, 1);
        adventurerDamaged(20, 2);
        adventurerDamaged(0, 2);
        adventurerDamaged(29, 2);
        tunnelConquered(20, 0, 0);
        evilnessChanged(-1, 2);
        adventurerHealed(2, 29, 20);
        // round 2 for player 2
        nextRound(2);
        nextRound(3);
        nextRound(4);
        // end
        nextYear(2);
        bidRetrieved(List.of(BidType.IMPS, BidType.NICENESS), 2);
        nextRound(1);
        broadcaster(1 + 3 + 2);
        leave();
    }
}
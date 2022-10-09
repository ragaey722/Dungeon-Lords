package de.unisaarland.cs.se.selab.systemtest.combatphase;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.HelperClass;
import java.util.List;

public class FullBattleAdaptationToCatchMonsterAbuser extends HelperClass {

    public FullBattleAdaptationToCatchMonsterAbuser() {
        super(FullBattleAdaptationToCatchMonsterAbuser.class, false);
    }

    @Override
    public void run() throws TimeoutException {
        wrapperBeforeCombat();

        // player 0
        combatRound1();
        combatRound2();
        combatRound3();
        combatRound4();


        // round 1 for player 1
        nextRound(1);
        assertSetBattleGround(1);
        assertActNow(1);
        leave(1, 1);
        // round 1 for player 2
        nextRound(1);
        assertSetBattleGround(2);
        assertActNow(2);
        leave(2, 2);

        // end
        nextYear(2);
        bidRetrieved(List.of(BidType.NICENESS, BidType.TUNNEL), 0);
        nextRound(1);
        broadcaster(1 + 3 + 2);
        leave();
    }

    private void combatRound1() throws TimeoutException {
        log("round 1 ---------------------------------------------------------");
        // round 1 for player 0
        nextRound(1);
        assertSetBattleGround(0);
        assertActNow(0);
        sendBattleGround(0, 0, 0);
        battleGroundSet(0, 0, 0);
        assertDefendYourself(0);
        assertActNow(0);

        sendMonster(0, 7);
        monsterPlaced(7, 0);
        assertActNow(0);
        // can't place 2 monsters in Tunnel
        sendMonster(0, 9);
        assertActionFailed(0);
        assertActNow(0);
        sendTrap(0, 6);
        trapPlaced(0, 6);
        assertActNow(0);
        // can't place 2 traps in Tunnel
        sendTrap(0, 26);
        assertActionFailed(0);
        assertActNow(0);
        sendEndTurn(0);

        // Trap 6 did 3 * 1 damage, Adventurer 6 has defuse value of 2
        adventurerDamaged(11, 1);

        // Monster 7 does 3 * 1 damage
        adventurerDamaged(6, 1);
        adventurerDamaged(23, 1);
        adventurerDamaged(11, 1);

        // Fatigue damages everyone by 2, Adv. 11 gets imprisoned (hp->0)
        adventurerDamaged(6, 2);
        adventurerDamaged(23, 2);
        adventurerImprisoned(11);

        // capture Tunnel
        tunnelConquered(6, 0, 0);
        evilnessChanged(-1, 0);

        // heal
        adventurerHealed(2, 6, 6);
        adventurerHealed(1, 23, 6);
        adventurerHealed(2, 23, 23);
    }

    private void combatRound2() throws TimeoutException {
        log("round 2 ---------------------------------------------------------");
        // Round 2
        nextRound(2);
        assertSetBattleGround(0);
        assertActNow(0);
        sendBattleGround(0, 1, 0);
        battleGroundSet(0, 1, 0);
        assertDefendYourself(0);
        assertActNow(0);
        // Trap 6 is used up from first round
        sendTrap(0, 6);
        assertActionFailed(0);
        assertActNow(0);
        sendTrap(0, 26);
        trapPlaced(0, 26);
        assertActNow(0);
        // Monster 7 was used in Battle this year already and has to rest
        sendMonster(0, 7);
        assertActionFailed(0);
        assertActNow(0);
        sendMonster(0, 9);
        monsterPlaced(9, 0);
        assertActNow(0);
        // just one monster max in tunnel and monster already in use
        sendMonster(0, 9);
        assertActionFailed(0);
        // Can't dig tunnels or set battleground now
        assertActNow(0);
        sendDigTunnel(0, 2, 1);
        assertActionFailed(0);
        assertActNow(0);
        sendBattleGround(0, 2, 0);
        assertActionFailed(0);
        assertActNow(0);
        sendEndTurn(0);

        // Trap does 3 dmg - 2 defuse of Adv 6 is 1 damage
        adventurerDamaged(6, 1);

        // Monster does 2 * 2 dmg
        adventurerDamaged(6, 2);
        adventurerDamaged(23, 2);

        // Fatigue damage (2)
        adventurerDamaged(6, 2);
        adventurerDamaged(23, 2);

        // capture Tunnel
        tunnelConquered(6, 1, 0);
        evilnessChanged(-1, 0);

        // heal
        adventurerHealed(2, 6, 6);
        adventurerHealed(3, 23, 6);
    }

    private void combatRound3() throws TimeoutException {
        log("round 3 ---------------------------------------------------------");
        // round 3
        nextRound(3);
        assertSetBattleGround(0);
        assertActNow(0);
        // battleground already conquered
        sendBattleGround(0, 0, 0);
        assertActionFailed(0);
        assertActNow(0);
        sendBattleGround(0, 2, 0);
        battleGroundSet(0, 2, 0);
        assertDefendYourself(0);
        assertActNow(0);
        // can't place any more monsters or traps (none left)
        sendMonster(0, 7);
        assertActionFailed(0);
        assertActNow(0);
        sendMonster(0, 13);
        assertActionFailed(0);
        assertActNow(0);
        sendTrap(0, 26);
        assertActionFailed(0);
        assertActNow(0);
        sendEndTurn(0);

        // Fatigue damage 2
        adventurerDamaged(6, 2);
        adventurerImprisoned(23);

        // capture Tunnel
        tunnelConquered(6, 2, 0);
        evilnessChanged(-1, 0);

        // heal
        adventurerHealed(2, 6, 6);
    }

    private void combatRound4() throws TimeoutException {
        log("round 4 ---------------------------------------------------------");
        // Round 4
        nextRound(4);
        assertSetBattleGround(0);
        assertActNow(0);
        sendBattleGround(0, 3, 0);
        battleGroundSet(0, 3, 0);
        assertDefendYourself(0);
        assertActNow(0);
        sendEndTurn(0);

        // Fatigue Damage
        adventurerDamaged(6, 2);

        // capture Tunnel
        tunnelConquered(6, 3, 0);
        evilnessChanged(-1, 0);

        // heal
        adventurerHealed(2, 6, 6);
    }
}
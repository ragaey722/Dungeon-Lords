package de.unisaarland.cs.se.selab.systemtest.combatphase;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.HelperClass;
import java.util.List;

public class GoToNextYear extends HelperClass {

    public GoToNextYear() {
        super(GoToNextYear.class, false);
    }

    @Override
    public void run() throws TimeoutException {
        wrapper2YearBidding();
        combat2();
    }

    protected void combat2() throws TimeoutException {
        // round 1
        nextRound(1);
        assertSetBattleGround(0);
        assertActNow(0);
        sendBattleGround(0, 4, 0);
        battleGroundSet(0, 4, 0);
        assertDefendYourself(0);
        assertActNow(0);
        sendTrap(0, 6);
        assertActionFailed(0);
        assertActNow(0);
        sendTrap(0, 19);
        goldChanged(-1, 0);
        trapPlaced(0, 19);
        assertActNow(0);
        sendMonster(0, 9);
        monsterPlaced(9, 0);
        assertActNow(0);
        sendMonster(0, 0);
        monsterPlaced(0, 0);
        assertActNow(0);
        sendMonster(0, 4);
        assertActionFailed(0);
        assertActNow(0);
        sendEndTurn(0);
        // fight start
        // trap dmg
        adventurerDamaged(15, 1);
        adventurerDamaged(26, 1);
        // monster 9 dmg
        adventurerDamaged(16, 2);
        adventurerDamaged(15, 2);
        adventurerDamaged(26, 2);
        // monster 0 dmg
        adventurerImprisoned(16);
        // tunnel dmg
        adventurerDamaged(15, 2);
        adventurerImprisoned(26);
        // tunnel conquer
        tunnelConquered(15, 4, 0);
        evilnessChanged(-1, 0);
        adventurerHealed(3, 15, 15);
        // round 2
        nextRound(2);
        assertSetBattleGround(0);
        assertActNow(0);
        sendBattleGround(0, 4, 1);
        battleGroundSet(0, 4, 1);
        assertDefendYourself(0);
        assertActNow(0);
        sendMonsterTargeted(0, 10, 1);
        monsterPlaced(10, 0);
        assertActNow(0);
        sendEndTurn(0);
        // fight start
        adventurerDamaged(15, 1);
        adventurerDamaged(15, 2);
        tunnelConquered(15, 4, 1);
        evilnessChanged(-1, 0);
        adventurerHealed(3, 15, 15);
        // round 3
        nextRound(3);
        assertSetBattleGround(0);
        assertActNow(0);
        sendBattleGround(0, 4, 2);
        battleGroundSet(0, 4, 2);
        assertDefendYourself(0);
        assertActNow(0);
        sendEndTurn(0);
        adventurerDamaged(15, 2);
        tunnelConquered(15, 4, 2);
        evilnessChanged(-1, 0);
        adventurerHealed(3, 15, 15);
        // round 4
        nextRound(4);
        adventurerFled(11);
        evilnessChanged(-1, 0);
        // end game
        assertGameEnd(0, 0, 22);
    }

    protected void combatWrapper1() throws TimeoutException {
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
        next2Round();
    }

    protected void next2Round() throws TimeoutException {
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
        sendBattleGround(0, 3, 0);
        battleGroundSet(0, 3, 0);
        assertDefendYourself(0);
        assertActNow(0);
        sendEndTurn(0);
        adventurerDamaged(6, 2);
        tunnelConquered(6, 3, 0);
        evilnessChanged(-1, 0);
        adventurerHealed(2, 6, 6);
        nextYear(2);
    }

    protected void wrapper2YearBidding() throws TimeoutException {
        combatWrapper1();
        // retriev bid for player 0(only left in game)
        bidRetrieved(List.of(BidType.NICENESS), 0);
        bidRetrieved(List.of(BidType.TUNNEL), 0);
        // round 1
        nextRound(1);
        broadcaster(1); //AdventurerDrawn
        broadcaster(3); //MonsterDrawn
        broadcaster(2); //RoomDrawn
        biddingStarted();
        actNowBroadcast();
        placeOne();
        evalOne();
        // retriev
        bidRetrieved(List.of(BidType.TRAP), 0);
        adventurerArrived(15, 0);
        //round 2
        nextRound(2);
        broadcaster(1); //AdventurerDrawn
        broadcaster(3); //MonsterDrawn
        broadcaster(2); //RoomDrawn
        biddingStarted();
        actNowBroadcast();
        placeTwo();
        evalTwo();
        // retriev
        bidRetrieved(List.of(BidType.MONSTER), 0);
        bidRetrieved(List.of(BidType.ROOM), 0);
        bidRetrieved(List.of(BidType.TRAP), 0);
        adventurerArrived(26, 0);
        // round 3
        nextRound(3);
        broadcaster(1); //AdventurerDrawn
        broadcaster(3); //MonsterDrawn
        broadcaster(2); //RoomDrawn
        biddingStarted();
        actNowBroadcast();
        placeThree();
        evalThree();
        // retriev
        bidRetrieved(List.of(BidType.FOOD), 0);
        bidRetrieved(List.of(BidType.IMPS), 0);
        bidRetrieved(List.of(BidType.MONSTER), 0);
        adventurerArrived(16, 0);
        // round 4
        nextRound(4);
        broadcaster(3); //MonsterDrawn
        broadcaster(2); //RoomDrawn
        biddingStarted();
        actNowBroadcast();
        placeFour();
        evalFour();
        // retriev
        bidRetrieved(List.of(BidType.TRAP), 0);
        bidRetrieved(List.of(BidType.ROOM), 0);
        bidRetrieved(List.of(BidType.MONSTER), 0);
        impsChanged(4, 0);
        goldChanged(2, 0);
    }

    protected void placeOne() throws TimeoutException {
        sendPlaceBid(0, BidType.TRAP, 1);
        bidPlaced(BidType.TRAP, 0, 1);
        assertActNow(0);
        sendPlaceBid(0, BidType.MONSTER, 2);
        bidPlaced(BidType.MONSTER, 0, 2);
        assertActNow(0);
        sendPlaceBid(0, BidType.ROOM, 3);
        bidPlaced(BidType.ROOM, 0, 3);
    }

    protected void evalOne() throws TimeoutException {
        //trap
        goldChanged(-1, 0);
        trapAcquired(0, 19);
        // monster
        assertSelectMonster(0);
        assertActNow(0);
        sendHireMonster(0, 0);
        foodChanged(-1, 0);
        evilnessChanged(1, 0);
        monsterHired(0, 0);
        // room
        goldChanged(-1, 0);
        assertPlaceRoom(0);
        assertActNow(0);
        sendBuildRoom(0, 1, 0, 12);
        assertActionFailed(0);
        assertActNow(0);
        sendBuildRoom(0, 4, 0, 12);
        roomBuilt(0, 12, 4, 0);
    }

    protected void placeTwo() throws TimeoutException {
        sendPlaceBid(0, BidType.TRAP, 1);
        bidPlaced(BidType.TRAP, 0, 1);
        assertActNow(0);
        sendPlaceBid(0, BidType.FOOD, 2);
        bidPlaced(BidType.FOOD, 0, 2);
        assertActNow(0);
        sendPlaceBid(0, BidType.IMPS, 3);
        bidPlaced(BidType.IMPS, 0, 3);
    }

    protected void evalTwo() throws TimeoutException {
        // food nothing
        // imps
        foodChanged(-1, 0);
        impsChanged(1, 0);
        // trap nothing
    }

    protected void placeThree() throws TimeoutException {
        sendPlaceBid(0, BidType.MONSTER, 1);
        bidPlaced(BidType.MONSTER, 0, 1);
        assertActNow(0);
        sendPlaceBid(0, BidType.TRAP, 2);
        bidPlaced(BidType.TRAP, 0, 2);
        assertActNow(0);
        sendPlaceBid(0, BidType.ROOM, 3);
        bidPlaced(BidType.ROOM, 0, 3);
    }

    protected void evalThree() throws TimeoutException {
        // trap nothing
        // monster
        assertSelectMonster(0);
        assertActNow(0);
        sendHireMonster(0, 10);
        foodChanged(-2, 0);
        monsterHired(10, 0);
        // room nothing
    }

    protected void placeFour() throws TimeoutException {
        sendPlaceBid(0, BidType.MONSTER, 1);
        bidPlaced(BidType.MONSTER, 0, 1);
        assertActNow(0);
        sendPlaceBid(0, BidType.TUNNEL, 2);
        bidPlaced(BidType.TUNNEL, 0, 2);
        assertActNow(0);
        sendPlaceBid(0, BidType.GOLD, 3);
        bidPlaced(BidType.GOLD, 0, 3);
    }

    protected void evalFour() throws TimeoutException {
        // tunnel
        assertDigTunnel(0);
        assertActNow(0);
        sendDigTunnel(0, 4, 1);
        impsChanged(-1, 0);
        tunnelDug(0, 4, 1);
        assertActNow(0);
        sendDigTunnel(0, 4, 2);
        impsChanged(-1, 0);
        tunnelDug(0, 4, 2);
        // gold
        impsChanged(-2, 0);
        // monster
        assertSelectMonster(0);
        assertActNow(0);
        sendHireMonster(0, 4);
        foodChanged(-3, 0);
        monsterHired(4, 0);
    }
}
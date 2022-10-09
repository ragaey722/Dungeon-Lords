package de.unisaarland.cs.se.selab.systemtest.combatphase;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import java.util.List;

public class ConqueredRoomOrTunnelAdaptationWithRoomActivation extends SetBattleGroundOnRoom {

    public ConqueredRoomOrTunnelAdaptationWithRoomActivation() {
        super(ConqueredRoomOrTunnelAdaptationWithRoomActivation.class, false);
    }

    @Override
    public void run() throws TimeoutException {
        firstYearRun();
        /**
         * player 0 : food = 14, evilness = 8, coins = 14, imps = 10
         * room : ID = 10, coordinate = 1,0
         * tunnel = 0,0; 1,0; 2,0; 3,0 (all conquered)
         * player 1 : food = 11, evilness = 2, coins = 10, imps 11
         * room : ID = 0, coordinate = 0,1
         * tunnel = 0,0; 0,1; 0,2
         */

        placeBidFirstSeasonOfSecondYear();
        //evaluate Tunnel
        //first slot
        assertDigTunnel(2);
        assertActNow(2);
        //check handling dig in conquered tile
        sendDigTunnel(2, 0, 1);
        assertActionFailed(2);
        assertActNow(2);
        //check handling activating conquered room
        sendActivateRoom(2, 10);
        assertActionFailed(2);
        assertActNow(2);
        //check handling place bid action in tunnel phase
        sendPlaceBid(2, BidType.FOOD, 1);
        assertActionFailed(2);
        assertActNow(2);
        sendEndTurn(2);
        //second slot
        assertDigTunnel(0);
        assertActNow(0);
        sendEndTurn(0);
        //evaluate monster
        assertSelectMonster(2);
        assertActNow(2);
        //check handling place bid action in monster phase
        sendPlaceBid(2, BidType.FOOD, 1);
        assertActionFailed(2);
        assertActNow(2);
        //player 0 leaves
        leave(0, 0);
        sendEndTurn(2);
        //evaluate room
        goldChanged(-1, 2);
        assertPlaceRoom(2);
        assertActNow(2);
        //check handling placing room in conquered tiles
        sendBuildRoom(2, 0, 2, 12);
        assertActionFailed(2);
        assertActNow(2);
        //check handling place bid in room phase
        sendPlaceBid(2, BidType.FOOD, 1);
        assertActionFailed(2);
        assertActNow(2);
        leave();

    }

    protected void placeBidFirstSeasonOfSecondYear() throws TimeoutException {
        //player 0 place bid
        sendPlaceBid(0, BidType.ROOM, 1);
        bidPlaced(BidType.ROOM, 0, 1);
        assertActNow(0);
        sendPlaceBid(0, BidType.MONSTER, 2);
        bidPlaced(BidType.MONSTER, 0, 2);
        assertActNow(0);
        sendPlaceBid(0, BidType.TUNNEL, 3);
        bidPlaced(BidType.TUNNEL, 0, 3);
        //player 2 place bid
        sendPlaceBid(2, BidType.ROOM, 1);
        bidPlaced(BidType.ROOM, 2, 1);
        assertActNow(2);
        sendPlaceBid(2, BidType.MONSTER, 2);
        bidPlaced(BidType.MONSTER, 2, 2);
        assertActNow(2);
        sendPlaceBid(2, BidType.TUNNEL, 3);
        bidPlaced(BidType.TUNNEL, 2, 3);
    }


    protected void firstYearRun() throws TimeoutException {
        final int numberOfPlayer = 3;
        registrationEventWrapper(numberOfPlayer);
        // round 1 already started
        place1();
        eval1();
        endOfSeason1();
        // round 2
        nextRound(2);
        broadcaster(numberOfPlayer); //AdventurerDrawn
        broadcaster(3); //MonsterDrawn
        broadcaster(2); //RoomDrawn
        biddingStarted();
        actNowBroadcast();
        place2();
        eval2();
        endOfSeason2();
        // round 3
        nextRound(3);
        broadcaster(numberOfPlayer); //AdventurerDrawn
        broadcaster(3); //MonsterDrawn
        broadcaster(2); //RoomDrawn
        biddingStarted();
        actNowBroadcast();

        //obtaining room

        placeBid3();
        evalBid3();
        // bid re , imps, gold, adv arr

        bidRetrieved(List.of(BidType.NICENESS, BidType.GOLD, BidType.ROOM), 0);
        bidRetrieved(List.of(BidType.GOLD, BidType.FOOD, BidType.ROOM), 1);
        bidRetrieved(List.of(BidType.NICENESS, BidType.IMPS, BidType.ROOM), 2);

        impsChanged(2, 2);

        // 4th round
        adventurerArrived(9, 2);
        adventurerArrived(20, 1);
        adventurerArrived(6, 0);

        nextRound(4);

        broadcaster(3);
        broadcaster(2);

        biddingStarted();
        actNowBroadcast();

        placeBid4();
        evalBid4();

        //bid retrieved
        bidRetrieved(List.of(BidType.FOOD, BidType.MONSTER, BidType.TUNNEL), 0);
        bidRetrieved(List.of(BidType.IMPS, BidType.MONSTER, BidType.FOOD), 1);
        bidRetrieved(List.of(BidType.FOOD, BidType.TUNNEL, BidType.MONSTER), 2);

        //tunnel aftermath
        impsChanged(3, 0);
        goldChanged(2, 0);

        impsChanged(3, 1);
        goldChanged(3, 1);

        impsChanged(2, 2);
        goldChanged(2, 2);

        //room aftermath

        impsChanged(3, 0);
        goldChanged(1, 0);

        impsChanged(3, 2);
        goldChanged(1, 2);

        //nextround
        nextRound(1);

        //setbattleground
        setBattleGround();

        nextYear(2);
        //bid retrieved
        bidRetrieved(List.of(BidType.NICENESS, BidType.GOLD), 0);
        bidRetrieved(List.of(BidType.NICENESS, BidType.GOLD), 2);
        //next round
        nextRound(1);
        broadcaster(2);
        broadcaster(3);
        broadcaster(2);
        biddingStarted();
        actNowBroadcast();
    }

    @Override
    protected void setBattleGround() throws TimeoutException {
        subsetBattleGround(0, 0, 0);
        leave(1, 1);
        sendEndTurn(0);

        //adv damage usw
        advenDamagePlayer0();

        tunnelConquered(6, 0, 0);
        evilnessChanged(-1, 0);
        advenPlayer0Healed();

        nextRound(2);
        assertSetBattleGround(0);
        assertActNow(0);
        // ------------------------------------- FAULTY ROOM
        sendActivateRoom(0, 10);
        assertActionFailed(0);
        assertActNow(0);
        sendBattleGround(0, 1, 0);
        battleGroundSet(0, 1, 0);
        assertDefendYourself(0);
        assertActNow(0);
        // ------------------------------------- FAULTY ROOM
        sendActivateRoom(0, 10);
        assertActionFailed(0);
        assertActNow(0);
        sendEndTurn(0);

        advenDamagePlayer0();

        tunnelConquered(6, 1, 0);
        evilnessChanged(-1, 0);
        advenPlayer0Healed();

        nextRound(3);
        subsetBattleGround(0, 2, 0);
        sendEndTurn(0);

        advenDamagePlayer0();
        tunnelConquered(6, 2, 0);
        evilnessChanged(-1, 0);
        advenPlayer0Healed();

        nextRound(4);
        subsetBattleGround(0, 3, 0);
        sendEndTurn(0);

        advenDamagePlayer0();
        tunnelConquered(6, 3, 0);
        evilnessChanged(-1, 0);
        advenPlayer0Healed();

        nextRound(1);
        assertSetBattleGround(2);
        assertActNow(2);
        //check handling action failed for place bid action in set battle ground
        sendPlaceBid(2, BidType.FOOD, 1);
        assertActionFailed(2);
        assertActNow(2);
        sendBattleGround(2, 0, 0);
        battleGroundSet(2, 0, 0);
        assertDefendYourself(2);
        assertActNow(2);
        //check handling action failed for place bid action in combat phase
        sendPlaceBid(2, BidType.FOOD, 1);
        assertActionFailed(2);
        assertActNow(2);
        sendEndTurn(2);
        adventurerDamagePlayer2();
        tunnelConquered(0, 0, 0);
        evilnessChanged(-1, 2);
        adventurerHealedPlayer2();

        nextRound(2);
        subsetBattleGround(2, 0, 1);
        sendEndTurn(2);
        adventurerDamagePlayer2();
        tunnelConquered(0, 0, 1);
        evilnessChanged(-1, 2);
        adventurerHealedPlayer2();

        nextRound(3);
        subsetBattleGround(2, 0, 2);
        sendEndTurn(2);
        adventurerDamagePlayer2();
        tunnelConquered(0, 0, 2);
        adventurerHealedPlayer2();

        nextRound(4);
    }

    protected void adventurerDamagePlayer2() throws TimeoutException {
        adventurerDamaged(0, 2);
        adventurerDamaged(29, 2);
        adventurerDamaged(9, 2);
    }

    protected void adventurerHealedPlayer2() throws TimeoutException {
        adventurerHealed(2, 29, 0);
        adventurerHealed(2, 29, 29);
        adventurerHealed(2, 29, 9);
        //second priest skipped
    }
}

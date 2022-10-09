package de.unisaarland.cs.se.selab.systemtest.combatphase;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.HelperClass;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import de.unisaarland.cs.se.selab.systemtest.registration.RegistrationTest;
import java.util.List;

public class SetBattleGroundOnRoom extends HelperClass {

    public SetBattleGroundOnRoom() {
        super(SetBattleGroundOnRoom.class, false);
    }

    protected SetBattleGroundOnRoom(final Class<?> subclass, final boolean bool) {
        super(subclass, bool);
    }

    @Override
    public String createConfig() {
        return Utils.loadResource(RegistrationTest.class, "allouterringroom.json");
    }


    @Override
    public void run() throws TimeoutException {
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

        //bidretrieve
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
        //next round
        nextRound(1);
        assertSendEvents();
        broadcaster(3);
        broadcaster(2);
        biddingStarted();
        actNowBroadcast();
        leave();
    }

    protected void endOfSeason2() throws TimeoutException {
        // bid re , imps, gold, adv arr
        bidRetrieved(List.of(BidType.TUNNEL), 0);
        bidRetrieved(List.of(BidType.FOOD), 0);
        bidRetrieved(List.of(BidType.MONSTER), 0);
        bidRetrieved(List.of(BidType.IMPS), 1);
        bidRetrieved(List.of(BidType.TUNNEL), 1);
        bidRetrieved(List.of(BidType.NICENESS), 1);
        bidRetrieved(List.of(BidType.GOLD), 2);
        bidRetrieved(List.of(BidType.MONSTER), 2);
        bidRetrieved(List.of(BidType.FOOD), 2);
        impsChanged(3, 0);
        goldChanged(3, 0);
        impsChanged(2, 1);
        goldChanged(2, 1);
        adventurerArrived(0, 2);
        adventurerArrived(18, 1);
        adventurerArrived(11, 0);
    }

    protected void endOfSeason1() throws TimeoutException {
        // bid re , imps, gold, adv arr
        bidRetrieved(List.of(BidType.MONSTER), 0);
        bidRetrieved(List.of(BidType.FOOD), 1);
        bidRetrieved(List.of(BidType.NICENESS), 2);
        impsChanged(2, 0);
        impsChanged(3, 1);
        impsChanged(1, 2);
        goldChanged(1, 2);
        adventurerArrived(2, 1);
        adventurerArrived(29, 2);
        adventurerArrived(23, 0);
    }

    protected void placeBid3() throws TimeoutException {
        sendPlaceBid(0, BidType.ROOM, 1);
        bidPlaced(BidType.ROOM, 0, 1);
        assertActNow(0);

        sendPlaceBid(1, BidType.ROOM, 1);
        bidPlaced(BidType.ROOM, 1, 1);
        assertActNow(1);

        sendPlaceBid(2, BidType.ROOM, 1);
        bidPlaced(BidType.ROOM, 2, 1);
        assertActNow(2);

        sendPlaceBid(0, BidType.FOOD, 2);
        bidPlaced(BidType.FOOD, 0, 2);
        assertActNow(0);

        sendPlaceBid(1, BidType.IMPS, 2);
        bidPlaced(BidType.IMPS, 1, 2);
        assertActNow(1);

        sendPlaceBid(2, BidType.FOOD, 2);
        bidPlaced(BidType.FOOD, 2, 2);
        assertActNow(2);

        sendPlaceBid(0, BidType.MONSTER, 3);
        bidPlaced(BidType.MONSTER, 0, 3);

        sendPlaceBid(1, BidType.MONSTER, 3);
        bidPlaced(BidType.MONSTER, 1, 3);

        sendPlaceBid(2, BidType.TUNNEL, 3);
        bidPlaced(BidType.TUNNEL, 2, 3);

    }

    protected void evalBid3() throws TimeoutException {

        //food
        goldChanged(-1, 2);
        foodChanged(2, 2);

        evilnessChanged(1, 0);
        foodChanged(3, 0);

        //tunnel
        assertDigTunnel(2);
        assertActNow(2);
        sendDigTunnel(2, 0, 1);
        impsChanged(-1, 2);
        tunnelDug(2, 0, 1);

        assertActNow(2);
        sendDigTunnel(2, 0, 2);
        impsChanged(-1, 2);
        tunnelDug(2, 0, 2);

        //imps
        foodChanged(-1, 1);
        impsChanged(1, 1);

        assertSelectMonster(0);
        assertActNow(0);
        sendHireMonster(0, 3);
        foodChanged(-2, 0);
        evilnessChanged(1, 0);
        monsterHired(3, 0);

        //monster
        assertSelectMonster(1);
        assertActNow(1);
        sendHireMonster(1, 14);
        foodChanged(-1, 1);
        evilnessChanged(1, 1);
        monsterHired(14, 1);

        //room
        goldChanged(-1, 2);
        assertPlaceRoom(2);
        assertActNow(2);
        sendBuildRoom(2, 0, 1, 0);
        roomBuilt(2, 0, 0, 1);

        goldChanged(-1, 0);
        assertPlaceRoom(0);
        assertActNow(0);
        sendBuildRoom(0, 0, 1, 10);
        assertActionFailed(0);

        assertActNow(0);
        sendBuildRoom(0, 1, 0, 10);
        roomBuilt(0, 10, 1, 0);
    }

    protected void placeBid4() throws TimeoutException {
        sendPlaceBid(0, BidType.TUNNEL, 1);
        bidPlaced(BidType.TUNNEL, 0, 1);
        assertActNow(0);

        sendPlaceBid(0, BidType.NICENESS, 2);
        bidPlaced(BidType.NICENESS, 0, 2);
        assertActNow(0);

        sendPlaceBid(0, BidType.GOLD, 3);
        bidPlaced(BidType.GOLD, 0, 3);

        sendPlaceBid(1, BidType.NICENESS, 2);
        bidPlaced(BidType.NICENESS, 1, 2);
        assertActNow(1);

        sendPlaceBid(1, BidType.GOLD, 3);
        bidPlaced(BidType.GOLD, 1, 3);
        assertActNow(1);

        sendPlaceBid(1, BidType.FOOD, 1);
        bidPlaced(BidType.FOOD, 1, 1);

        sendPlaceBid(2, BidType.MONSTER, 1);
        bidPlaced(BidType.MONSTER, 2, 1);
        assertActNow(2);

        sendPlaceBid(2, BidType.NICENESS, 2);
        bidPlaced(BidType.NICENESS, 2, 2);
        assertActNow(2);

        sendPlaceBid(2, BidType.GOLD, 3);
        bidPlaced(BidType.GOLD, 2, 3);

    }

    protected void evalBid4() throws TimeoutException {
        goldChanged(-1, 1);
        foodChanged(2, 1);

        //Nice
        evilnessChanged(-1, 0);

        evilnessChanged(-2, 1);

        goldChanged(-1, 2);
        evilnessChanged(-2, 2);

        //Tunnel activate room
        assertDigTunnel(0);
        assertActNow(0);
        sendActivateRoom(0, 10);
        impsChanged(-3, 0);
        roomActivated(0, 10);

        assertActNow(0);
        sendDigTunnel(0, 3, 0);
        impsChanged(-1, 0);
        tunnelDug(0, 3, 0);

        assertActNow(0);
        sendEndTurn(0);

        //Gold
        impsChanged(-2, 0);
        impsChanged(-3, 1);
        impsChanged(-2, 2);

        //monster activate room
        assertSelectMonster(2);
        assertActNow(2);
        sendActivateRoom(2, 0);
        impsChanged(-3, 2);
        roomActivated(2, 0);

        assertActNow(2);
        sendHireMonster(2, 6);
        foodChanged(-1, 2);
        evilnessChanged(1, 2);
        monsterHired(6, 2);

    }

    protected void setBattleGround() throws TimeoutException {
        subsetBattleGround(0, 0, 0);
        leave(1, 1);
        leave(2, 2);
        sendEndTurn(0);

        //adv damage usw
        advenDamagePlayer0();

        tunnelConquered(6, 0, 0);
        evilnessChanged(-1, 0);
        advenPlayer0Healed();

        nextRound(2);
        subsetBattleGround(0, 1, 0);
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
    }

    /**
     * from set battleground event until act now for defen yourself
     */
    protected void subsetBattleGround(final int socket, final int x, final int y)
            throws TimeoutException {
        assertSetBattleGround(socket);
        assertActNow(socket);
        sendBattleGround(socket, x, y);
        battleGroundSet(socket, x, y);
        assertDefendYourself(socket);
        assertActNow(socket);
    }

    protected void advenDamagePlayer0() throws TimeoutException {
        adventurerDamaged(6, 2);
        adventurerDamaged(23, 2);
        adventurerDamaged(11, 2);
    }

    protected void advenPlayer0Healed() throws TimeoutException {
        adventurerHealed(2, 6, 6);
        adventurerHealed(2, 23, 23);
        adventurerHealed(1, 23, 11);
        adventurerHealed(1, 11, 11);
    }


}

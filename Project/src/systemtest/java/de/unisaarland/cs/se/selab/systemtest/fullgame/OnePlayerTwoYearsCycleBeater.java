package de.unisaarland.cs.se.selab.systemtest.fullgame;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.HelperClass;
import java.util.List;

public class OnePlayerTwoYearsCycleBeater extends HelperClass {

    public OnePlayerTwoYearsCycleBeater() {
        super(OnePlayerTwoYearsCycleBeater.class, false);
    }

    @Override
    public void run() throws TimeoutException {
        this.registrationEventWrapper(1); //max players
        bidding1();
        bidding2();
        bidding3();
        bidding4();
        /**
         * CURRENT STATE:
         * --- player 0 ---
         * dungeon:
         * unused → ■
         * tunnel → ▢
         * captured → ▣
         * room → <roomID>
         *                       ← Y →
         *   |---------------------------------------------|
         *   | ▢  ▢  ▢  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■ |
         *   | ▢  ■  ▢  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■ |
         *   | ▢  ▢  ▢  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■ |
         *   | ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■ |
         *   | ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■ |
         *   | ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■ |
         * ↑ | ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■ |
         * X | ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■ |
         * ↓ | ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■ |
         *   | ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■ |
         *   | ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■ |
         *   | ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■ |
         *   | ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■ |
         *   | ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■ |
         *   | ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■ |
         *   |---------------------------------------------|
         **/

        combat1();
        combat2();
        combat3();
        /*
        combat4();
        transition();
        bidding5();
        bidding6();
        bidding7();
        bidding8();
        combat5();
        combat6();
        combat7();
        combat8();
        end();
         */
    }

    public void combat3() throws TimeoutException {
        nextRound(3);
        // set battleground
        assertSetBattleGround(0);
        assertActNow(0);
        leave();
    }

    public void combat2() throws TimeoutException {
        nextRound(2);
        // set battleground
        assertSetBattleGround(0);
        assertActNow(0);
        sendBattleGround(0, 0, 1);
        battleGroundSet(0, 0, 1);

        // defense
        assertDefendYourself(0);
        assertActNow(0);
        sendEndTurn(0);

        // fatigue damage
        adventurerDamaged(29, 2);
        adventurerDamaged(23, 2);
        adventurerDamaged(2, 2);

        // conquer
        tunnelConquered(29, 0, 1);
        evilnessChanged(-1, 0);

        // healing
        adventurerHealed(2, 29, 29);
        adventurerHealed(2, 23, 23);
        adventurerHealed(1, 23, 2);
    }

    public void combat1() throws TimeoutException {
        nextRound(1);
        // set battleground
        assertSetBattleGround(0);
        assertActNow(0);
        sendBattleGround(0, 0, 0);
        battleGroundSet(0, 0, 0);

        // defense
        assertDefendYourself(0);
        assertActNow(0);
        sendEndTurn(0);

        // fatigue damage
        adventurerDamaged(29, 2);
        adventurerDamaged(23, 2);
        adventurerDamaged(2, 2);

        // conquer
        tunnelConquered(29, 0, 0);
        evilnessChanged(-1, 0);

        // healing
        adventurerHealed(2, 29, 29);
        adventurerHealed(2, 23, 23);
        adventurerHealed(1, 23, 2);
    }

    public void bidding1() throws TimeoutException {
        // bidding
        placeBidSuccessful(BidType.TUNNEL, 1, 0);
        assertActNow(0);
        placeBidSuccessful(BidType.FOOD, 2, 0);
        assertActNow(0);
        placeBidSuccessful(BidType.IMPS, 3, 0);

        //evaluation
        evaluateFood(1, 0);

        // tunnel
        assertDigTunnel(0);
        assertActNow(0);
        sendDigTunnel(0, 1, 0); // dig tunnel
        impsChanged(-1, 0);
        tunnelDug(0, 1, 0);
        assertActNow(0);
        sendEndTurn(0); // no tunnel to dig

        evaluateImps(1, 0);

        // bid Retrieval
        bidRetrieved(List.of(BidType.TUNNEL), 0);

        // imps return
        impsChanged(1, 0);

        // arrival
        adventurerArrived(29, 0);
    }

    public void bidding2() throws TimeoutException {

        // start phase
        nextRound(2);
        drawAll(1);
        biddingStarted();
        actNowBroadcast();

        // bidding
        placeBidSuccessful(BidType.TUNNEL, 1, 0);
        assertActNow(0);
        placeBidSuccessful(BidType.GOLD, 2, 0);
        assertActNow(0);
        placeBidSuccessful(BidType.ROOM, 3, 0);

        //evaluation
        // tunnel
        assertDigTunnel(0);
        assertActNow(0);
        sendDigTunnel(0, 2, 0); // dig tunnel
        impsChanged(-1, 0);
        tunnelDug(0, 2, 0);
        assertActNow(0);
        sendDigTunnel(0, 2, 1); // dig tunnel
        impsChanged(-1, 0);
        tunnelDug(0, 2, 1);

        // gold
        evaluateGold(1, 0, 2);

        // room
        goldChanged(-1, 0);
        assertPlaceRoom(0);
        assertActNow(0);
        sendEndTurn(0);

        // bid Retrieval
        bidRetrieved(List.of(BidType.FOOD, BidType.IMPS, BidType.TUNNEL), 0);

        // imps return
        impsChanged(4, 0);
        goldChanged(2, 0);

        // arrival
        adventurerArrived(23, 0);
    }

    public void bidding3() throws TimeoutException {

        // start phase
        nextRound(3);
        drawAll(1);
        biddingStarted();
        actNowBroadcast();

        // bidding
        placeBidSuccessful(BidType.TUNNEL, 1, 0);
        assertActNow(0);
        placeBidSuccessful(BidType.IMPS, 2, 0);
        assertActNow(0);
        placeBidSuccessful(BidType.TRAP, 3, 0);

        //evaluation

        // tunnel
        assertDigTunnel(0);
        assertActNow(0);
        sendDigTunnel(0, 2, 2); // dig tunnel
        impsChanged(-1, 0);
        tunnelDug(0, 2, 2);
        assertActNow(0);
        sendDigTunnel(0, 1, 2); // dig tunnel
        impsChanged(-1, 0);
        tunnelDug(0, 1, 2);

        evaluateImps(1, 0);
        evaluateTraps(1, 0);

        // bid Retrieval
        bidRetrieved(List.of(BidType.GOLD, BidType.ROOM, BidType.TUNNEL), 0);

        // imps return
        impsChanged(2, 0);

        // arrival
        adventurerArrived(2, 0);
    }

    public void bidding4() throws TimeoutException {

        // start phase
        nextRound(4);
        drawAllMinusAdventurers();
        biddingStarted();
        actNowBroadcast();

        // bidding
        placeBidSuccessful(BidType.TUNNEL, 1, 0);
        assertActNow(0);
        placeBidSuccessful(BidType.GOLD, 2, 0);
        assertActNow(0);
        placeBidSuccessful(BidType.MONSTER, 3, 0);

        //evaluation
        // tunnel
        assertDigTunnel(0);
        assertActNow(0);
        sendDigTunnel(0, 0, 2); // dig tunnel
        impsChanged(-1, 0);
        tunnelDug(0, 0, 2);
        assertActNow(0);
        sendDigTunnel(0, 0, 1); // dig tunnel
        impsChanged(-1, 0);
        tunnelDug(0, 0, 1);

        evaluateGold(1, 0, 6);

        //monster
        assertSelectMonster(0);
        assertActNow(0);
        sendHireMonster(0, 6);
        foodChanged(-1, 0);
        evilnessChanged(1, 0);
        monsterHired(6, 0);

        // bid Retrieval
        bidRetrieved(List.of(BidType.IMPS, BidType.TRAP, BidType.TUNNEL), 0);

        // imps return
        impsChanged(4, 0);
        goldChanged(2, 0);
    }
}

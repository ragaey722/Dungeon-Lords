package de.unisaarland.cs.se.selab.systemtest.fullgame;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.HelperClass;
import java.util.List;

public class OnePlayerTwoYears extends HelperClass {

    public OnePlayerTwoYears() {
        super(OnePlayerTwoYears.class, false);
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
         * gold : 5
         * imps : 4
         * food : 5
         * evilness : 6
         *
         * monster : 6
         * traps : ???
         *
         * dungeon:
         * unused → ■
         * tunnel → ▢
         * captured → ▣
         * room → <roomID>
         *                       ← Y →
         *   |---------------------------------------------|
         *   | ▢  ▢  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■ |
         *   | ▢  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■ |
         *   | ▢  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■ |
         *   | ▢  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■ |
         *   | ▢  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■  ■ |
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
        /*
        combat2();
        combat3();
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

    public void combat1() throws TimeoutException {
        nextRound(1);
        // set battleground
        assertSetBattleGround(0);
        assertActNow(0);
        sendBattleGround(0, 1, 1); // not closest / tunnel
        assertActionFailed(0);
        assertActNow(0);
        sendEndTurn(0); // not a valid action
        assertActionFailed(0);
        assertActNow(0);
        sendBattleGround(0, 0, 1); // not closest
        assertActionFailed(0);
        assertActNow(0);
        sendBattleGround(0, 0, -1); // not in dungeon
        assertActionFailed(0);
        assertActNow(0);
        sendBattleGround(0, 0, 0);
        battleGroundSet(0, 0, 0);

        // defense
        assertDefendYourself(0);
        assertActNow(0);
        leave();
    }

    public void bidding1() throws TimeoutException {
        // bidding
        placeBidSuccessful(BidType.GOLD, 1, 0);
        assertActNow(0);
        placeBidSuccessful(BidType.FOOD, 2, 0);
        assertActNow(0);
        placeBidSuccessful(BidType.IMPS, 3, 0);

        //evaluation
        evaluateFood(1, 0);
        evaluateGold(1, 0, 1);
        evaluateImps(1, 0);

        // bid Retrieval
        bidRetrieved(List.of(BidType.GOLD), 0);

        // imps return
        impsChanged(1, 0);
        goldChanged(1, 0);

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
        sendBuildRoom(0, 0, 0, 8); // wrong phase
        assertActionFailed(0);
        assertActNow(0);
        sendActivateRoom(0, 15); // no rooms yet
        assertActionFailed(0);
        assertActNow(0);
        sendDigTunnel(0, 1, 0); // dig tunnel
        impsChanged(-1, 0);
        tunnelDug(0, 1, 0);
        assertActNow(0);
        sendEndTurn(0); // no second tunnel to dig

        // gold
        evaluateGold(1, 0, 2);

        // room
        goldChanged(-1, 0);
        assertPlaceRoom(0);
        assertActNow(0);
        sendActivateRoom(0, 15); // no rooms yet
        assertActionFailed(0);
        assertActNow(0);
        sendBuildRoom(0, 0, 0, 8); // lower half restriction
        assertActionFailed(0);
        assertActNow(0);
        sendBuildRoom(0, 0, 0, 15); // lower half restriction
        assertActionFailed(0);
        assertActNow(0);
        sendEndTurn(0);

        // bid Retrieval
        bidRetrieved(List.of(BidType.FOOD, BidType.IMPS, BidType.TUNNEL), 0);

        // imps return
        impsChanged(3, 0);
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
        placeBidSuccessful(BidType.FOOD, 2, 0);
        assertActNow(0);
        placeBidSuccessful(BidType.TRAP, 3, 0);

        //evaluation
        evaluateFood(1, 0);
        // tunnel
        assertDigTunnel(0);
        assertActNow(0);
        sendDigTunnel(0, 0, 1); // dig tunnel
        impsChanged(-1, 0);
        tunnelDug(0, 0, 1);
        assertActNow(0);
        sendDigTunnel(0, 0, -1); // tunnel not in dungeon
        assertActionFailed(0);
        assertActNow(0);
        sendDigTunnel(0, 0, 0); // tunnel already there
        assertActionFailed(0);
        assertActNow(0);
        sendDigTunnel(0, 5, 5); // tunnel not connected
        assertActionFailed(0);
        assertActNow(0);
        sendDigTunnel(0, 0, 1); // tunnel is already dug this round
        assertActionFailed(0);
        assertActNow(0);
        sendDigTunnel(0, 1, 1); // tunnel would loop
        assertActionFailed(0);
        assertActNow(0);
        sendDigTunnel(0, 2, 0); // dig tunnel
        impsChanged(-1, 0);
        tunnelDug(0, 2, 0);

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
        sendDigTunnel(0, 1, 1); // cycles are forbidden
        assertActionFailed(0);
        assertActNow(0);
        sendDigTunnel(0, 3, 0); // dig tunnel
        impsChanged(-1, 0);
        tunnelDug(0, 3, 0);
        assertActNow(0);
        sendDigTunnel(0, 4, 0); // dig tunnel
        impsChanged(-1, 0);
        tunnelDug(0, 4, 0);

        evaluateGold(1, 0, 6);

        //monster
        assertSelectMonster(0);
        assertActNow(0);
        sendDigTunnel(0, 0, 0); // wrong action
        assertActionFailed(0);
        assertActNow(0);
        sendHireMonster(0, 6);
        foodChanged(-1, 0);
        evilnessChanged(1, 0);
        monsterHired(6, 0);

        // bid Retrieval
        bidRetrieved(List.of(BidType.FOOD, BidType.TRAP, BidType.TUNNEL), 0);

        // imps return
        impsChanged(4, 0);
        goldChanged(2, 0);
    }
}

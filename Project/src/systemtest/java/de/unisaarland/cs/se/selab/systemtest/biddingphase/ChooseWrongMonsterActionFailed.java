package de.unisaarland.cs.se.selab.systemtest.biddingphase;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.HelperClass;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import de.unisaarland.cs.se.selab.systemtest.registration.RegistrationTest;
import java.util.List;

public class ChooseWrongMonsterActionFailed extends HelperClass {

    public ChooseWrongMonsterActionFailed() {
        super(ChooseWrongMonsterActionFailed.class, false);
    }

    @Override
    public String createConfig() {
        return Utils.loadResource(RegistrationTest.class, "configmonsterexpensive.json");
    }

    @Override
    public void run() throws TimeoutException {
        final int numberOfPlayer = 2;
        registrationEventWrapper(numberOfPlayer);
        firstSeasonChoseWrongMonster();

        bidRetrieved(List.of(BidType.FOOD), 0);
        bidRetrieved(List.of(BidType.FOOD), 1);
        //adventurer arrived
        adventurerArrived(29, 0);
        adventurerArrived(23, 1);

        nextRound(2);
        broadcaster(2);
        broadcaster(3);
        broadcaster(2);
        biddingStarted();

        actNowBroadcast();
        secondSeasonOptionLocked();
        evaluateSecondRound();

        bidRetrieved(List.of(BidType.MONSTER, BidType.IMPS, BidType.NICENESS), 0);
        bidRetrieved(List.of(BidType.IMPS, BidType.MONSTER, BidType.NICENESS), 1);

        impsChanged(1, 0);
        goldChanged(1, 0);

        impsChanged(1, 1);
        goldChanged(1, 1);

        adventurerArrived(0, 0);
        adventurerArrived(2, 1);

        nextRound(3);
        broadcaster(2);
        broadcaster(3);
        broadcaster(2);
        biddingStarted();

        actNowBroadcast();
        thirdRoundMonsterExpensive();
        evalThirdRoundMonsterExpensive();

        bidRetrieved(List.of(BidType.GOLD, BidType.TRAP, BidType.MONSTER), 0);
        bidRetrieved(List.of(BidType.GOLD, BidType.TRAP, BidType.MONSTER), 1);

        leave();


    }

    void firstSeasonChoseWrongMonster() throws  TimeoutException {
        sendPlaceBid(0, BidType.FOOD, 1);
        bidPlaced(BidType.FOOD, 0, 1);
        assertActNow(0);
        sendPlaceBid(1, BidType.FOOD, 1);
        bidPlaced(BidType.FOOD, 1, 1);
        assertActNow(1);
        sendPlaceBid(0, BidType.MONSTER, 2);
        bidPlaced(BidType.MONSTER, 0, 2);
        assertActNow(0);
        sendPlaceBid(1, BidType.MONSTER, 3);
        bidPlaced(BidType.MONSTER, 1, 3);
        assertActNow(1);
        //third bid
        sendPlaceBid(0, BidType.IMPS, 3);
        bidPlaced(BidType.IMPS, 0, 3);
        sendPlaceBid(1, BidType.IMPS, 2);
        bidPlaced(BidType.IMPS, 1, 2);

        //evaluation food options
        goldChanged(-1, 0);
        foodChanged(2, 0);
        evilnessChanged(1, 1);
        foodChanged(3, 1);

        //evaluation imps options
        foodChanged(-1, 1);
        impsChanged(1, 1);
        foodChanged(-2, 0);
        impsChanged(2, 0);

        //evaluation monster options
        assertSelectMonster(0);
        assertActNow(0);
        sendHireMonster(0, 10);
        assertActionFailed(0);
        assertActNow(0);
        sendEndTurn(0);

        assertSelectMonster(1);
        assertActNow(1);
        sendHireMonster(1, 10);
        assertActionFailed(1);
        assertActNow(1);
        sendEndTurn(1);

    }

    void secondSeasonOptionLocked() throws TimeoutException {
        sendPlaceBid(0, BidType.MONSTER, 1);
        assertActionFailed(0);
        assertActNow(0);

        sendPlaceBid(1, BidType.MONSTER, 1);
        assertActionFailed(1);
        assertActNow(1);

        sendPlaceBid(0, BidType.NICENESS, 1); //playerid 0 get the second slot
        bidPlaced(BidType.NICENESS, 0, 1);
        assertActNow(0);

        sendPlaceBid(1, BidType.NICENESS, 1); //playerID 1 get the first slot
        bidPlaced(BidType.NICENESS, 1, 1);
        assertActNow(1);

        sendPlaceBid(0, BidType.MONSTER, 2);
        assertActionFailed(0);
        assertActNow(0);

        sendPlaceBid(1, BidType.MONSTER, 2);
        assertActionFailed(1);
        assertActNow(1);

        sendPlaceBid(0, BidType.GOLD, 2);
        bidPlaced(BidType.GOLD, 0, 2);
        assertActNow(0);

        sendPlaceBid(1, BidType.GOLD, 2);
        bidPlaced(BidType.GOLD, 1, 2);
        assertActNow(1);

        sendPlaceBid(0, BidType.MONSTER, 3);
        assertActionFailed(0);
        assertActNow(0);

        sendPlaceBid(1, BidType.MONSTER, 3);
        assertActionFailed(1);
        assertActNow(1);

        sendPlaceBid(0, BidType.TRAP, 3);
        bidPlaced(BidType.TRAP, 0, 3);

        sendPlaceBid(1, BidType.TRAP, 3);
        bidPlaced(BidType.TRAP, 1, 3);
    }

    void evaluateSecondRound() throws TimeoutException {
        //eval niceness
        evilnessChanged(-1, 1);
        evilnessChanged(-2, 0);

        //eval gold
        impsChanged(-1, 1);
        impsChanged(-1, 0);

        //eval trap
        goldChanged(-1, 1);
        trapAcquired(1, 26);
        trapAcquired(0, 6);
    }

    void thirdRoundMonsterExpensive() throws TimeoutException {
        sendPlaceBid(0, BidType.MONSTER, 1);
        bidPlaced(BidType.MONSTER, 0, 1);
        assertActNow(0);

        sendPlaceBid(1, BidType.MONSTER, 1);
        bidPlaced(BidType.MONSTER, 1, 1);
        assertActNow(1);

        sendPlaceBid(0, BidType.IMPS, 2);
        bidPlaced(BidType.IMPS, 0, 2);
        assertActNow(0);

        sendPlaceBid(1, BidType.IMPS, 2);
        bidPlaced(BidType.IMPS, 1, 2);
        assertActNow(1);

        sendPlaceBid(0, BidType.NICENESS, 3);
        bidPlaced(BidType.NICENESS, 0, 3);

        sendPlaceBid(1, BidType.NICENESS, 3);
        bidPlaced(BidType.NICENESS, 1, 3);

    }

    void evalThirdRoundMonsterExpensive() throws TimeoutException {
        //eval niceness
        evilnessChanged(-1, 0);
        evilnessChanged(-2, 1);

        //imps
        foodChanged(-1, 0);
        impsChanged(1, 0);

        foodChanged(-2, 1);
        impsChanged(2, 1);

        //eval monster
        assertSelectMonster(0);
        assertActNow(0);
        sendHireMonster(0, 22);
        assertActionFailed(0);
        assertActNow(0);

        sendHireMonster(0, 14);
        assertActionFailed(0);
        assertActNow(0);

        sendHireMonster(0, 3);
        assertActionFailed(0);
        assertActNow(0);

        sendEndTurn(0);

        assertSelectMonster(1);
        assertActNow(1);
        sendHireMonster(1, 22);
        assertActionFailed(1);
        assertActNow(1);

        sendHireMonster(1, 14);
        assertActionFailed(1);
        assertActNow(1);

        sendHireMonster(1, 20);
        assertActionFailed(1);
        assertActNow(1);

        sendEndTurn(1);
    }
}

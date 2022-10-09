package de.unisaarland.cs.se.selab.systemtest.fullgame;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.HelperClass;
import de.unisaarland.cs.se.selab.systemtest.registration.RegistrationTest;
import java.util.List;

public class MaxPlayersTwoYearsStandard extends HelperClass {

    public MaxPlayersTwoYearsStandard() {
        super(RegistrationTest.class, false);
    }

    @Override
    public void run() throws TimeoutException {
        this.registrationEventWrapper(4); //max players
        bidding1();
        bidding2();
        /*
        bidding3();
        bidding4();
        combat1();
        transition();
        bidding5();
        bidding6();
        bidding7();
        bidding8();
        combat2();
        end();
         */
    }

    public void bidding1() throws TimeoutException {
        // bidding
        // Bids on FOOD
        placeBidSuccessful(BidType.FOOD, 1, 0);
        assertActNow(0);
        placeBidSuccessful(BidType.FOOD, 1, 1);
        assertActNow(1);
        placeBidSuccessful(BidType.FOOD, 1, 2);
        assertActNow(2);
        // Bids on NICENESS
        placeBidSuccessful(BidType.NICENESS, 2, 0);
        assertActNow(0);
        placeBidSuccessful(BidType.NICENESS, 2, 1);
        assertActNow(1);
        placeBidSuccessful(BidType.NICENESS, 2, 3);
        assertActNow(3);
        // Bids on IMPS
        placeBidSuccessful(BidType.IMPS, 3, 0);
        placeBidSuccessful(BidType.IMPS, 3, 2);
        assertActNow(2);
        placeBidSuccessful(BidType.IMPS, 3, 3);
        assertActNow(3);
        // Bids on TRAPS
        placeBidSuccessful(BidType.TRAP, 1, 3);
        placeBidSuccessful(BidType.TRAP, 3, 1);
        placeBidSuccessful(BidType.TRAP, 2, 2);

        //evaluation
        // evaluate FOOD
        evaluateFood(1, 0);
        evaluateFood(2, 1);
        evaluateFood(3, 2);
        // evaluate NICENESS
        evaluateEvilness(1, 0);
        evaluateEvilness(2, 1);
        evaluateEvilness(3, 3);
        // evaluate IMPS
        evaluateImps(1, 0);
        evaluateImps(2, 2);
        evaluateImps(3, 3);
        // evaluate TRAPS
        evaluateTraps(1, 3);
        evaluateTraps(2, 2);
        evaluateTraps(3, 1);

        // bid Retrieval
        bidRetrieved(List.of(BidType.FOOD), 0);
        bidRetrieved(List.of(BidType.FOOD), 1);
        bidRetrieved(List.of(BidType.FOOD), 2);
        bidRetrieved(List.of(BidType.TRAP), 3);

        // adventurers arrive
        adventurerArrived(0, 3);
        adventurerArrived(2, 0);
        adventurerArrived(29, 1);
        adventurerArrived(23, 2);
    }

    public void bidding2() throws TimeoutException {

        // start phase
        nextRound(2);
        drawAll(4);
        biddingStarted();
        actNowBroadcast();

        // bidding
        leave();


        // evaluation


        // bid retrieval


        // adventurers arrive


    }
}

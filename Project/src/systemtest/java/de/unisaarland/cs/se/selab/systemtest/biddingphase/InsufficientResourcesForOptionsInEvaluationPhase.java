package de.unisaarland.cs.se.selab.systemtest.biddingphase;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.HelperClass;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import de.unisaarland.cs.se.selab.systemtest.registration.RegistrationTest;
import java.util.List;

public class InsufficientResourcesForOptionsInEvaluationPhase extends HelperClass {

    public InsufficientResourcesForOptionsInEvaluationPhase() {
        super(InsufficientResourcesForOptionsInEvaluationPhase.class, false);
    }

    @Override
    public String createConfig() {
        return Utils.loadResource(RegistrationTest.class,
                "insufficientResourcesConfiguration.json");
    }

    /**
     * missing insufficient evilness initial resources set to 1 except evilness (somehow fails if
     * the initial evilness < 5)
     */

    @Override
    public void run() throws TimeoutException {
        final int numberOfPlayer = 3;
        registrationEventWrapper(numberOfPlayer);
        //first player place bid
        sendPlaceBid(0, BidType.FOOD, 1);
        bidPlaced(BidType.FOOD, 0, 1);
        assertActNow(0);
        sendPlaceBid(0, BidType.IMPS, 2);
        bidPlaced(BidType.IMPS, 0, 2);
        assertActNow(0);
        sendPlaceBid(0, BidType.TRAP, 3);
        bidPlaced(BidType.TRAP, 0, 3);
        //second player place bid
        sendPlaceBid(1, BidType.IMPS, 1);
        bidPlaced(BidType.IMPS, 1, 1);
        assertActNow(1);
        sendPlaceBid(1, BidType.GOLD, 2);
        bidPlaced(BidType.GOLD, 1, 2);
        assertActNow(1);
        sendPlaceBid(1, BidType.TRAP, 3);
        bidPlaced(BidType.TRAP, 1, 3);
        //third player place bid
        sendPlaceBid(2, BidType.IMPS, 1);
        bidPlaced(BidType.IMPS, 2, 1);
        assertActNow(2);
        sendPlaceBid(2, BidType.GOLD, 2);
        bidPlaced(BidType.GOLD, 2, 2);
        assertActNow(2);
        sendPlaceBid(2, BidType.TRAP, 3);
        bidPlaced(BidType.TRAP, 2, 3);
        //evaluate food option
        goldChanged(-1, 0);
        foodChanged(2, 0);
        //evaluate gold option
        impsChanged(-1, 1);
        impsChanged(-1, 2);
        //evaluate imps option
        foodChanged(-1, 1);
        impsChanged(1, 1);
        //insufficient for player 0 and 2
        // evaluate trap option
        //insufficient for player 0
        assertSendEvents();
        //insufficient for player 2
        //Bid retrieved
        bidRetrieved(List.of(BidType.FOOD), 0);
        bidRetrieved(List.of(BidType.IMPS), 1);
        bidRetrieved(List.of(BidType.IMPS), 2);
        // return imps and receive gold
        impsChanged(1, 1);
        goldChanged(1, 1);
        impsChanged(1, 2);
        goldChanged(1, 2);
        leave();
    }
}

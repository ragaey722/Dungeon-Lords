package de.unisaarland.cs.se.selab.systemtest.biddingphase;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.HelperClass;
import java.util.List;

public class TooManyPlayerBidsOnMonster extends HelperClass {

    public TooManyPlayerBidsOnMonster() {
        super(TooManyPlayerBidsOnMonster.class, false);
    }

    public TooManyPlayerBidsOnMonster(final Class<?> subclass, final boolean bool) {
        super(subclass, bool);
    }

    @Override
    public void run() throws TimeoutException {
        final int numberOfPlayer = 4;
        registrationEventWrapper(numberOfPlayer);
        firstSeasonAllPlayerBidOnOne();
        evalFirstSeason();

        bidRetrieved(List.of(BidType.MONSTER), 0);
        bidRetrieved(List.of(BidType.MONSTER), 1);
        bidRetrieved(List.of(BidType.MONSTER), 2);
        bidRetrieved(List.of(BidType.MONSTER), 3);

        //gold mining acquired
        impsChanged(1, 0);
        goldChanged(1, 0);
        impsChanged(1, 1);
        goldChanged(1, 1);
        impsChanged(1, 2);
        goldChanged(1, 2);

        broadcaster(4);
        nextRound(2);
        broadcaster(4);
        broadcaster(3);
        broadcaster(2);

        biddingStarted();
        actNowBroadcast();

        leave();
    }

    /**
     * 4 players bid always on one same option.
     * Pay attention to their different priorities:)
     *
     */
    void firstSeasonAllPlayerBidOnOne() throws TimeoutException {
        sendPlaceBid(0, BidType.MONSTER, 1);
        bidPlaced(BidType.MONSTER, 0, 1);
        assertActNow(0);

        sendPlaceBid(1, BidType.MONSTER, 1);
        bidPlaced(BidType.MONSTER, 1, 1);
        assertActNow(1);

        sendPlaceBid(2, BidType.MONSTER, 1);
        bidPlaced(BidType.MONSTER, 2, 1);
        assertActNow(2);

        sendPlaceBid(3, BidType.MONSTER, 1);
        bidPlaced(BidType.MONSTER, 3, 1);
        assertActNow(3);

        //all players bid on one on the different priority order
        sendPlaceBid(0, BidType.GOLD, 2);
        bidPlaced(BidType.GOLD, 0, 2);
        assertActNow(0);

        sendPlaceBid(1, BidType.GOLD, 2);
        bidPlaced(BidType.GOLD, 1, 2);
        assertActNow(1);

        sendPlaceBid(2, BidType.GOLD, 3);
        bidPlaced(BidType.GOLD, 2, 3);
        assertActNow(2);

        sendPlaceBid(3, BidType.GOLD, 3);
        bidPlaced(BidType.GOLD, 3, 3);
        assertActNow(3);

        sendPlaceBid(0, BidType.TRAP, 3);
        bidPlaced(BidType.TRAP, 0, 3);

        sendPlaceBid(1, BidType.TRAP, 3);
        bidPlaced(BidType.TRAP, 1, 3);

        sendPlaceBid(2, BidType.TRAP, 2);
        bidPlaced(BidType.TRAP, 2, 2);

        sendPlaceBid(3, BidType.TRAP, 2);
        bidPlaced(BidType.TRAP, 3, 2);

    }

    /**
     * evaluation for the firstSeasonAllPlayerBidOnOne method
     *
     */
    void evalFirstSeason() throws TimeoutException {
        //gold
        impsChanged(-1, 0);
        impsChanged(-1, 1);
        impsChanged(-1, 2);

        //trap
        goldChanged(-1, 2);
        trapAcquired(2, 26);

        trapAcquired(3, 6);

        goldChanged(-2, 0);
        trapAcquired(0, 19);
        trapAcquired(0, 5);

        //monster
        assertSelectMonster(0);
        assertActNow(0);
        sendHireMonster(0, 23);
        monsterHired(23, 0);

        assertSelectMonster(1);
        assertActNow(1);
        sendHireMonster(1, 13);
        foodChanged(-1, 1);
        evilnessChanged(1, 1);
        monsterHired(13, 1);

        foodChanged(-1, 2);
        assertSelectMonster(2);
        assertActNow(2);
        sendHireMonster(2, 9);
        evilnessChanged(3, 2);
        monsterHired(9, 2);
    }

}

package de.unisaarland.cs.se.selab.datapackage;

import de.unisaarland.cs.se.selab.comm.BidType;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BiddingSquare {

    private final  Map<BidType, BidOption> bidOptions = new EnumMap<>(BidType.class);
    /**
     * the order of the dungeon lord that will be evaluated / placed in each bid option
     */
    private final List<Integer> dungeonLordIDs = new LinkedList<>();

    public BiddingSquare() {
        final BidFactory bidFactory = new BidFactory();
        bidOptions.put(BidType.FOOD, bidFactory.createFoodOption());
        bidOptions.put(BidType.NICENESS, bidFactory.createNicenessOption());
        bidOptions.put(BidType.TUNNEL, bidFactory.createTunnelOption());
        bidOptions.put(BidType.GOLD, bidFactory.createCoinOption());
        bidOptions.put(BidType.IMPS, bidFactory.createImpOption());
        bidOptions.put(BidType.TRAP, bidFactory.createTrapOption());
        bidOptions.put(BidType.MONSTER, bidFactory.createMonsterOption());
        bidOptions.put(BidType.ROOM, bidFactory.createRoomOption());
    }


    public Map<BidType, BidOption> getAllOptions() {
        return bidOptions;
    }

    public BidOption getOption(final BidType bt) {
        return bidOptions.get(bt);
    }


    public void addDungeonLord(final int id) {
        dungeonLordIDs.add(id);
    }

    public List<Integer> getAllDungeonLords() {
        return dungeonLordIDs;
    }
}
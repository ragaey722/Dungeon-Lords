package de.unisaarland.cs.se.selab.datapackage;

import de.unisaarland.cs.se.selab.comm.BidType;
import java.util.ArrayList;
import java.util.List;

public class BidOption {

    final List<Integer> dungeonLordIDs = new ArrayList<>();
    final List<Resources> costs;
    final List<Resources> rewards;
    final BidType bidType;

    public BidOption(final BidType bidType, final List<Resources> costs,
            final List<Resources> rewards) {
        this.bidType = bidType;
        this.costs = costs;
        this.rewards = rewards;
    }

    public void addDungeonLord(final int dungeonLord) {
        dungeonLordIDs.add(dungeonLord);
    }


    public List<Integer> getAllDungeonLords() {
        return dungeonLordIDs;
    }

    public void resetDungeonLord() {
        dungeonLordIDs.clear();
    }


    public Resources getCosts(final int index) {
        return costs.get(index);
    }

    public Resources getRewards(final int index) {
        return rewards.get(index);
    }

    public int getNumberOfDungeonLord() {
        return dungeonLordIDs.size();
    }

    public BidType getBidType() {
        return this.bidType;
    }


}
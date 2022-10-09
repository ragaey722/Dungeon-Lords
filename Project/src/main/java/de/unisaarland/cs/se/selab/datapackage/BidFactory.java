package de.unisaarland.cs.se.selab.datapackage;

import de.unisaarland.cs.se.selab.comm.BidType;
import java.util.ArrayList;
import java.util.List;

public class BidFactory {

    /**
     * this method creates Food Option
     *
     * @return a BidOption with Type Food
     */
    public BidOption createFoodOption() {
        final List<Resources> costs = new ArrayList<>();
        final Resources slot1Cost = new Resources(1, 0, 0, 0);
        final Resources slot2Cost = new Resources(0, 1, 0, 0);
        final Resources slot3Cost = new Resources(0, 2, 0, 0);
        costs.add(0, slot1Cost);
        costs.add(1, slot2Cost);
        costs.add(2, slot3Cost);

        final List<Resources> rewards = new ArrayList<>();
        final Resources slot1Reward = new Resources(0, 0, 0, 2);
        final Resources slot2Reward = new Resources(0, 0, 0, 3);
        final Resources slot3Reward = new Resources(1, 0, 0, 3);
        rewards.add(0, slot1Reward);
        rewards.add(1, slot2Reward);
        rewards.add(2, slot3Reward);

        return new BidOption(BidType.FOOD, costs, rewards);
    }

    /**
     * this method creates Niceness Option
     *
     * @return a BidOption with Type Niceness
     */
    public BidOption createNicenessOption() {
        final List<Resources> costs = new ArrayList<>();
        final Resources slot1Cost = new Resources(0, 0, 0, 0);
        final Resources slot2Cost = new Resources(0, 0, 0, 0);
        final Resources slot3Cost = new Resources(1, 0, 0, 0);
        costs.add(0, slot1Cost);
        costs.add(1, slot2Cost);
        costs.add(2, slot3Cost);

        final List<Resources> rewards = new ArrayList<>();
        final Resources slot1Reward = new Resources(0, 1, 0, 0);
        final Resources slot2Reward = new Resources(0, 2, 0, 0);
        final Resources slot3Reward = new Resources(0, 2, 0, 0);
        rewards.add(0, slot1Reward);
        rewards.add(1, slot2Reward);
        rewards.add(2, slot3Reward);

        return new BidOption(BidType.NICENESS, costs, rewards);
    }

    /**
     * this method creates Tunnel Option
     *
     * @return a BidOption with Type Tunnel
     */
    public BidOption createTunnelOption() {
        final List<Resources> costs = new ArrayList<>();
        final Resources slot1Cost = new Resources(0, 0, 2, 0);
        final Resources slot2Cost = new Resources(0, 0, 3, 0);
        final Resources slot3Cost = new Resources(0, 0, 5, 0);
        costs.add(0, slot1Cost);
        costs.add(1, slot2Cost);
        costs.add(2, slot3Cost);

        final List<Resources> rewards = new ArrayList<>();
        final Resources slot1Reward = new Resources(0, 0, 2, 0);
        final Resources slot2Reward = new Resources(0, 0, 3, 0);
        final Resources slot3Reward = new Resources(0, 0, 4, 0);
        rewards.add(0, slot1Reward);
        rewards.add(1, slot2Reward);
        rewards.add(2, slot3Reward);

        return new BidOption(BidType.TUNNEL, costs, rewards);
    }

    /**
     * this method creates Coin Option
     *
     * @return a BidOption with Type Gold
     */
    public BidOption createCoinOption() {
        final List<Resources> costs = new ArrayList<>();
        final Resources slot1Cost = new Resources(0, 0, 2, 0);
        final Resources slot2Cost = new Resources(0, 0, 3, 0);
        final Resources slot3Cost = new Resources(0, 0, 5, 0);
        costs.add(0, slot1Cost);
        costs.add(1, slot2Cost);
        costs.add(2, slot3Cost);

        final List<Resources> rewards = new ArrayList<>();
        final Resources slot1Reward = new Resources(2, 0, 0, 0);
        final Resources slot2Reward = new Resources(3, 0, 0, 0);
        final Resources slot3Reward = new Resources(4, 0, 0, 0);
        rewards.add(0, slot1Reward);
        rewards.add(1, slot2Reward);
        rewards.add(2, slot3Reward);

        return new BidOption(BidType.GOLD, costs, rewards);
    }

    /**
     * this method creates Imps Option
     *
     * @return a BidOption with Type Imps
     */
    public BidOption createImpOption() {
        final List<Resources> costs = new ArrayList<>();
        final Resources slot1Cost = new Resources(0, 0, 0, 1);
        final Resources slot2Cost = new Resources(0, 0, 0, 2);
        final Resources slot3Cost = new Resources(1, 0, 0, 1);
        costs.add(0, slot1Cost);
        costs.add(1, slot2Cost);
        costs.add(2, slot3Cost);

        final List<Resources> rewards = new ArrayList<>();
        final Resources slot1Reward = new Resources(0, 0, 1, 0);
        final Resources slot2Reward = new Resources(0, 0, 2, 0);
        final Resources slot3Reward = new Resources(0, 0, 2, 0);
        rewards.add(0, slot1Reward);
        rewards.add(1, slot2Reward);
        rewards.add(2, slot3Reward);

        return new BidOption(BidType.IMPS, costs, rewards);
    }

    /**
     * this method creates Trap Option
     *
     * @return a BidOption with Type Trap
     */
    public BidOption createTrapOption() {
        final List<Resources> costs = new ArrayList<>();
        final Resources slot1Cost = new Resources(1, 0, 0, 0);
        final Resources slot2Cost = new Resources(0, 0, 0, 0);
        final Resources slot3Cost = new Resources(2, 0, 0, 0);
        costs.add(0, slot1Cost);
        costs.add(1, slot2Cost);
        costs.add(2, slot3Cost);

        final List<Resources> rewards = new ArrayList<>();
        final Resources slot1Reward = new Resources(0, 0, 0, 0);
        final Resources slot2Reward = new Resources(0, 0, 0, 0);
        final Resources slot3Reward = new Resources(0, 0, 0, 0);
        rewards.add(0, slot1Reward);
        rewards.add(1, slot2Reward);
        rewards.add(2, slot3Reward);

        return new BidOption(BidType.TRAP, costs, rewards);
    }

    /**
     * this method creates Monster Option
     *
     * @return a BidOption with Type Monster
     */
    public BidOption createMonsterOption() {
        final List<Resources> costs = new ArrayList<>();
        final Resources slot1Cost = new Resources(0, 0, 0, 0);
        final Resources slot2Cost = new Resources(0, 0, 0, 0);
        final Resources slot3Cost = new Resources(0, 0, 0, 1);
        costs.add(0, slot1Cost);
        costs.add(1, slot2Cost);
        costs.add(2, slot3Cost);

        final List<Resources> rewards = new ArrayList<>();
        final Resources slot1Reward = new Resources(0, 0, 0, 0);
        final Resources slot2Reward = new Resources(0, 0, 0, 0);
        final Resources slot3Reward = new Resources(0, 0, 0, 0);
        rewards.add(0, slot1Reward);
        rewards.add(1, slot2Reward);
        rewards.add(2, slot3Reward);

        return new BidOption(BidType.MONSTER, costs, rewards);
    }

    /**
     * this method creates Room Option
     *
     * @return a BidOption with Type Room
     */
    public BidOption createRoomOption() {
        final List<Resources> costs = new ArrayList<>();
        final Resources slot1Cost = new Resources(1, 0, 0, 0);
        final Resources slot2Cost = new Resources(1, 0, 0, 0);
        final Resources slot3Cost = new Resources(0, 0, 0, 0);
        costs.add(0, slot1Cost);
        costs.add(1, slot2Cost);
        costs.add(2, slot3Cost);

        final List<Resources> rewards = new ArrayList<>();
        final Resources slot1Reward = new Resources(0, 0, 0, 0);
        final Resources slot2Reward = new Resources(0, 0, 0, 0);
        final Resources slot3Reward = new Resources(0, 0, 0, 0);
        rewards.add(0, slot1Reward);
        rewards.add(1, slot2Reward);
        rewards.add(2, slot3Reward);

        return new BidOption(BidType.ROOM, costs, rewards);
    }

}
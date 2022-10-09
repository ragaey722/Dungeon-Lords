package de.unisaarland.cs.se.selab.datapackage;


public class Resources {

    private int coins;
    private int evilness;

    private int imps;

    private int foods;


    /**
     * number of imps that were send to mine tunnels and coins
     */
    private int busyImpsMining;


    /**
     * number of imps that were sent to rooms
     */
    private int busyImpsRoom;

    public Resources(final int coins, final int evilness, final int imps, final int foods) {
        this.coins = coins;
        this.evilness = evilness;
        this.imps = imps;
        this.foods = foods;
        this.busyImpsRoom = 0;
        this.busyImpsMining = 0;
    }


    public int getCoins() {
        return coins;
    }

    public int getEvilness() {
        return evilness;
    }

    public int getImps() {
        return imps;
    }

    public int getFoods() {
        return foods;
    }

    public int getBusyImpsMining() {
        return busyImpsMining;
    }

    public int getAvailableImps() {
        return this.imps - this.busyImpsRoom - this.busyImpsMining;
    }


    public void setImps(final int imps) {
        this.imps = imps;
    }

    public void setFoods(final int foods) {
        this.foods = foods;
    }

    public void subtractBusyImpsRoom(final int amount) {
        this.busyImpsRoom -= amount;
    }

    public void addBusyImpsMining(final int amount) {
        this.busyImpsMining += amount;
    }

    public void addImps(final int amount) {
        this.imps += amount;
    }

    public void addFood(final int amount) {
        setFoods(this.foods + amount);
    }

    public void addCoins(final int amount) {
        this.coins += amount;
    }

    public void resetBusyImpsMining() {
        this.busyImpsMining = 0;
    }


    /**
     * should check whether the Dungeon Lord can pay the bidding cost
     *
     * @param cost is resource to be subtracted
     * @return true if the current resources are enough to pay for the costs
     */
    public boolean canResourceBeSubtracted(final Resources cost) {
        return (cost.getCoins() <= this.coins
                && (cost.getImps() <= this.imps)
                && cost.getEvilness() + this.evilness <= 15
                && cost.getFoods() <= this.foods);
    }


    /**
     * this method subtracts the Dungeon Lord's coin with corresponding cost if possible
     *
     * @param amount is amount of subtraction
     * @return true if the Coins could be and was subtracted
     */

    public boolean subtractCoins(final int amount) {
        if (this.coins - amount < 0) {
            return false;
        } else {
            this.coins -= amount;
            return true;
        }
    }


    /**
     * checks if the number of busy imps + required amount is more than the imps we have
     *
     * @return true if we have enough available imps to send to a room
     */
    public boolean addBusyImpsRoom(final int amount) {
        if ((this.busyImpsMining + this.busyImpsRoom + amount) > this.imps) {
            return false;
        } else {
            this.busyImpsRoom += amount;
            return true;
        }
    }


    /**
     * @param amount is amount of evilness to be added
     * @return true if evilness could be and was incremented by the given amount
     */
    public boolean upEvilness(final int amount) {
        if (this.evilness + amount > 15) {
            return false;
        } else {
            this.evilness += amount;
            return true;
        }
    }

    /**
     * @param amount is the amount of niceness to reduce the evilness
     * @return true if evilness could be and was decremented by the given amount
     */
    public boolean downEvilness(final int amount) {
        if (this.evilness - amount < 0) {
            return false;
        } else {
            this.evilness -= amount;
            return true;
        }
    }


    /**
     * @param amount is the amount of used food
     * @return true if the given amount could be and was subtracted from the food
     */

    public boolean subtractFood(final int amount) {
        if (this.foods - amount < 0) {
            return false;
        } else {
            this.foods -= amount;
            return true;
        }
    }


    /**
     * this method creates new Resource instance with the same values to be used inside the creation
     * of the DungeonLord
     *
     * @return new Resource object with the same values
     */
    public Resources cloneResource() {
        return new Resources(this.coins, this.evilness, this.imps, this.foods);
    }


    @Override
    public String toString() {
        return "food = %d, evilness = %d, coins = %d, imps = %d"
                .formatted(this.foods, this.evilness, this.coins, this.imps);

    }


}
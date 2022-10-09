package de.unisaarland.cs.se.selab.datapackage;

import de.unisaarland.cs.se.selab.comm.BidType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class DungeonLord {

    private final int playerID;
    private final Dungeon dungeon;
    /**
     * map from monsterID to monsters that the player owns
     */
    private final Map<Integer, Monster> monsters;
    /**
     * set that keeps track of monsters that were used in the current combat phase
     */
    private final Set<Integer> tiredMonstersID;
    /**
     * map from trapID to traps that the player owns
     */
    private final Map<Integer, Trap> traps;
    /**
     * map from roomID to rooms that the player owns
     */
    private final Map<Integer, Room> rooms;
    /**
     * map from Slot to biddings that the player already bid in this place bid phase
     */
    private final Map<Integer, BidType> biddings;
    /**
     * list of locked biddings
     */
    private final List<BidType> lockedBiddings;
    /**
     * resources contains the Gold, Food, Evilness and Imps of the dungeon lord
     */
    private final Resources resources;

    public DungeonLord(final int playerID, final int maxSideLength, final Resources resources) {
        this.playerID = playerID;
        dungeon = new Dungeon(maxSideLength);
        monsters = new HashMap<>();
        tiredMonstersID = new HashSet<>();
        traps = new HashMap<>();
        rooms = new HashMap<>();
        biddings = new HashMap<>();
        lockedBiddings = new ArrayList<>();
        this.resources = resources;
    }

    public int getPlayerID() {
        return playerID;
    }

    public Dungeon getDungeon() {
        return dungeon;
    }

    public Resources getResources() {
        return resources;
    }

    public List<BidType> getLockedBiddings() {
        return this.lockedBiddings;
    }

    public Map<Integer, Trap> getTraps() {
        return traps;
    }

    public Map<Integer, Room> getRooms() {
        return rooms;
    }

    public Map<Integer, Monster> getMonsters() {
        return monsters;
    }

    public Map<Integer, BidType> getBiddings() {
        return biddings;
    }

    public int getAlreadyBid() {
        return biddings.size();
    }

    public int getNumberOfRooms() {
        return rooms.size();
    }

    public int getNumberOfMonsters() {
        return monsters.size();
    }

    public void addLockedBiddings(final BidType bid) {
        lockedBiddings.add(bid);
    }

    public void addBidding(final BidType bid, final int slot) {
        biddings.put(slot, bid);
    }

    public BidType getBiddingBySlot(final int slot) {
        return biddings.get(slot);
    }

    public void resetBiddingsList() {
        biddings.clear();
    }

    public void resetLockedBiddingsList() {
        lockedBiddings.clear();
    }

    public void addMonster(final Monster monster) {
        monsters.put(monster.id(), monster);
    }

    public void addRoom(final Room room) {
        rooms.put(room.getId(), room);
    }

    public void addTrap(final Trap trap) {
        traps.put(trap.id(), trap);
    }

    /**
     * @return true if the given id match a monster that the dungeon lord owns,
     * has a basic or multi target strat and was not used in the current combat yet
     */
    public boolean canUseMonster(final int monsterID) {
        final Monster monster = monsters.get(monsterID);
        return monster != null && !(tiredMonstersID.contains(monsterID))
                && monster.attackStrategy() != AttackStrategy.TARGETED;
    }

    /**
     * @return true if the given id match a monster that the dungeon lord owns,
     * has a targeted strat and was not used in the current combat yet
     * and the given target is in boundaries
     */
    public boolean canUseMonsterTargeted(final int monsterID, final int position) {
        if (position < 1 || position > 3) {
            return false;
        }
        final Monster monster = monsters.get(monsterID);
        return monster != null && !(tiredMonstersID.contains(monsterID))
                && monster.attackStrategy() == AttackStrategy.TARGETED;

    }

    /**
     * @return the specified monster and set it to tired for the rest of this combat phase
     */
    public Monster useMonster(final int monsterID) {
        tiredMonstersID.add(monsterID);
        return monsters.get(monsterID);

    }

    /**
     * resets all monsters to be ready to use again
     */
    public void monstersAreRested() {
        tiredMonstersID.clear();
    }

    /**
     * @return true if the dungeon lord owns this trap
     */
    public boolean canUseTrap(final int trapID) {
        return traps.containsKey(trapID);
    }

    /**
     * @return the specified trap and remove it from the owned traps by the dungeon lord
     */
    public Trap useTrap(final int trapID) {
        return traps.remove(trapID);
    }


}
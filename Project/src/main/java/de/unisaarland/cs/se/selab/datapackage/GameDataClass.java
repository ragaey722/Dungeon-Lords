package de.unisaarland.cs.se.selab.datapackage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameDataClass {

    private int season;
    private int year;
    private final Map<Integer, DungeonLord> dungeonLords;
    private final Map<Integer, Adventurer> adventurers;
    private final List<Adventurer> adventurerPool;
    private final List<Trap> trapPool;
    private final Map<Integer, Room> rooms;
    private final List<Room> roomPool;
    private final Map<Integer, Monster> monsters;
    private final List<Monster> monsterPool;
    private final BiddingSquare biddingSquare;
    private final int maxSideLength;

    private final Resources initialResources;

    public GameDataClass(final List<Adventurer> adventurerPool, final List<Trap> trapPool,
                         final List<Room> roomPool,
                         final List<Monster> monsterPool, final int maxSideLength,
                         final Resources initialResources) {
        this.adventurerPool = adventurerPool;
        this.trapPool = trapPool;
        this.roomPool = roomPool;
        this.monsterPool = monsterPool;
        this.maxSideLength = maxSideLength;
        this.initialResources = initialResources;
        season = 1;
        year = 1;
        dungeonLords = new HashMap<>();
        adventurers = new HashMap<>();
        rooms = new HashMap<>();
        monsters = new HashMap<>();
        biddingSquare = new BiddingSquare();
    }

    public int getMaxSideLength() {
        return this.maxSideLength;
    }

    public void removeDungeonLord(final int playerID) {
        this.dungeonLords.remove(playerID);
    }

    public void addDungeonLord(final int playerID, final DungeonLord dungeonLord) {
        this.dungeonLords.put(playerID, dungeonLord);
    }

    public void addMonster(final int id, final Monster monster) {
        this.monsters.put(id, monster);
    }

    public void addAdventurer(final int id, final Adventurer adventurer) {
        this.adventurers.put(id, adventurer);
    }

    public void addRoom(final int id, final Room room) {
        this.rooms.put(id, room);
    }

    public DungeonLord getDungeonLord(final int id) {
        return this.dungeonLords.get(id);
    }

    public Map<Integer, DungeonLord> getAllDungeonLords() {
        return this.dungeonLords;
    }

    public Map<Integer, Adventurer> getAllAdventurers() {
        return this.adventurers;
    }

    public Monster getMonster(final int id) {
        return this.monsters.get(id);
    }

    public void removeMonster(final int id) {
        this.monsters.remove(id);
    }

    public Room getRoom(final int id) {
        return this.rooms.get(id);
    }

    public Map<Integer, Room> getAllRooms() {
        return this.rooms;
    }

    public boolean isRoomAvailable(final int id) {
        return this.rooms.containsKey(id);
    }

    public void removeRoom(final int id) {
        this.rooms.remove(id);
    }

    public BiddingSquare getBiddingSquare() {
        return biddingSquare;
    }

    public boolean isMonsterAvailable(final int id) {
        return this.monsters.containsKey(id);
    }

    public void setSeason(final int season) {
        this.season = season;
    }

    public int getSeason() {
        return this.season;
    }

    public void setYear(final int year) {
        this.year = year;
    }

    public int getYear() {
        return this.year;
    }

    public boolean isDungeonLordAvailable(final int id) {
        return this.dungeonLords.containsKey(id);
    }

    public List<Adventurer> getAdventurerPool() {
        return adventurerPool;
    }

    public List<Monster> getMonsterPool() {
        return monsterPool;
    }

    public List<Room> getRoomPool() {
        return roomPool;
    }

    public List<Trap> getTrapPool() {
        return trapPool;
    }

    public void resetAdventurer() {
        adventurers.clear();
    }

    public void resetRoom() {
        rooms.clear();
    }

    public void resetMonster() {
        monsters.clear();
    }

    public Resources getInitialResources() {
        return initialResources;
    }

}
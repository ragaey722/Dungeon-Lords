package de.unisaarland.cs.se.selab.systemtest;

import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.api.SystemTest;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import de.unisaarland.cs.se.selab.systemtest.registration.RegistrationTest;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.LoggerFactory;

public abstract class HelperClass extends SystemTest {

    protected static final int MAX_PLAYER = 4;
    protected Set<Integer> listOfCommID = new LinkedHashSet<>();

    protected Set<Integer> listOfPlayerID = new LinkedHashSet<>();

    protected Boolean gameStartedBoolean = Boolean.FALSE;

    protected Integer seed = 42;

    protected String configPath = "configuration.json";

    protected ParserInvocation parserInvocation;

    protected List<Integer> monsters;
    protected List<Integer> adventurers;
    protected List<Integer> rooms;


    public HelperClass(final Class<?> subclass, final boolean fail) {
        super(subclass, fail);
        this.parserInvocation = new ParserInvocation(
                Utils.loadResource(RegistrationTest.class, this.configPath), this.seed);
    }

    @Override
    public String createConfig() {
        return Utils.loadResource(RegistrationTest.class, this.configPath);
    }

    @Override
    public long createSeed() {
        return this.seed;
    }

    @Override
    public abstract void run() throws TimeoutException;

    @Override
    protected Set<Integer> createSockets() {
        final Set<Integer> sockets = new LinkedHashSet<>(MAX_PLAYER);
        for (int i = 0; i < MAX_PLAYER; i++) {
            sockets.add(i);
        }
        return sockets;
    }

    private void addCommIDs(final int numberOfPlayer) {
        for (int i = 0; i < numberOfPlayer; i++) {
            listOfCommID.add(i);
        }
    }

    private void addPlayerIDs(final int numberOfPlayer) {
        for (int i = 0; i < numberOfPlayer; i++) {
            listOfPlayerID.add(i);
        }
    }


    protected void register(final int numberOfPlayer)
            throws TimeoutException {
        addCommIDs(numberOfPlayer);
        addPlayerIDs(numberOfPlayer);
        final String config = createConfig();
        for (final int socket : listOfCommID) {
            this.sendRegister(socket, "Player %d".formatted(socket));
            this.assertConfig(socket, config);
        }
    }

    protected void register(final int commID, final int playerID) throws TimeoutException {
        final String config = createConfig();
        listOfCommID.add(commID);
        listOfPlayerID.add(playerID);
        this.sendRegister(commID, "Player %d".formatted(commID));
        this.assertConfig(commID, config);
    }

    protected void leave() {
        for (final int socket : listOfCommID) {
            sendLeave(socket);
        }
    }

    protected void leave(final int socket, final int playerID) throws TimeoutException {
        listOfCommID.remove(socket);
        listOfPlayerID.remove(playerID);
        this.sendLeave(socket);
        if (this.gameStartedBoolean) {
            for (final int id : listOfCommID) {
                this.assertLeft(id, playerID);
            }
        }
    }


    protected void gameStarted() throws TimeoutException {
        for (final int socket : listOfCommID) {
            this.assertGameStarted(socket);
        }
        this.gameStartedBoolean = Boolean.TRUE;
    }

    protected void player() throws TimeoutException {
        assert listOfCommID.size() == listOfPlayerID.size();
        final Iterator<Integer> it = listOfPlayerID.iterator();
        for (final int socket : listOfCommID) {
            final int playerID = it.next();
            for (final int id : listOfCommID) {
                this.assertPlayer(id, "Player %d".formatted(socket), playerID);
            }
        }
    }

    protected void nextYear(final int year) throws TimeoutException {
        for (final int socket : listOfCommID) {
            this.assertNextYear(socket, year);
        }
    }

    protected void registrationEventWrapper(final int numberOfPlayer)
            throws TimeoutException {
        assert numberOfPlayer > 0 && numberOfPlayer < MAX_PLAYER;
        register(numberOfPlayer);
        if (numberOfPlayer < MAX_PLAYER) {
            this.sendStartGame(0);
        }
        gameStarted();
        player();
        nextYear(1);
        nextRound(1);
        drawAll(numberOfPlayer);
        biddingStarted();
        actNowBroadcast();
    }

    protected void nextRound(final int round) throws TimeoutException {
        for (final int socket : listOfCommID) {
            this.assertNextRound(socket, round);
        }
    }

    protected void adventureDrawn(final int adventurerID) throws TimeoutException {
        for (final int socket : listOfCommID) {
            this.assertAdventurerDrawn(socket, adventurerID);
        }
    }

    protected void monsterDrawn(final int monsterID) throws TimeoutException {
        for (final int socket : listOfCommID) {
            this.assertMonsterDrawn(socket, monsterID);
        }
    }

    protected void roomDrawn(final int roomID) throws TimeoutException {
        for (final int socket : listOfCommID) {
            this.assertRoomDrawn(socket, roomID);
        }
    }

    protected void biddingStarted() throws TimeoutException {
        for (final int socket : listOfCommID) {
            this.assertBiddingStarted(socket);
        }
    }

    protected void log(final String s) {
        LoggerFactory.getLogger(this.toString()).info(s);
    }

    protected void logAdventurer() {
        LoggerFactory.getLogger(this.toString()).info("Adventurer IDs : %s"
                .formatted(this.adventurers.toString()));
    }

    protected void logMonster() {
        LoggerFactory.getLogger(this.toString()).info("Monster IDs : %s"
                .formatted(this.monsters.toString()));
    }

    protected void logRooms() {
        LoggerFactory.getLogger(this.toString()).info("Room IDs : %s"
                .formatted(this.rooms.toString()));
    }

    /**
     * this method doesn't use the attribute listOfCommID instead we take as an input another set of
     * commID In which we can specify which sockets will get the actNow events.
     */
    protected void actNowBroadcast() throws TimeoutException {
        for (final int socket : this.listOfCommID) {
            this.assertActNow(socket);
        }
    }

    protected void bidPlaced(final BidType bid, final int playerID, final int slot)
            throws TimeoutException {
        for (final int socket : listOfCommID) {
            this.assertBidPlaced(socket, bid, playerID, slot);
        }
    }

    protected void goldChanged(final int amount, final int playerID) throws TimeoutException {
        for (final int socket : listOfCommID) {
            this.assertGoldChanged(socket, amount, playerID);
        }
    }

    protected void foodChanged(final int amount, final int playerID) throws TimeoutException {
        LoggerFactory.getLogger(this.toString()).info("------------------------------------");
        for (final int socket : listOfCommID) {
            this.assertFoodChanged(socket, amount, playerID);
        }
    }

    protected void evilnessChanged(final int amount, final int playerID) throws TimeoutException {
        for (final int socket : listOfCommID) {
            this.assertEvilnessChanged(socket, amount, playerID);
        }
    }

    protected void impsChanged(final int amount, final int playerID) throws TimeoutException {
        for (final int socket : listOfCommID) {
            this.assertImpsChanged(socket, amount, playerID);
        }
    }

    protected void roomActivated(final int playerID, final int roomID) throws TimeoutException {
        for (final int socket : listOfCommID) {
            this.assertRoomActivated(socket, playerID, roomID);
        }
    }

    protected void tunnelDug(final int playerID, final int x, final int y) throws TimeoutException {
        for (final int socket : listOfCommID) {
            this.assertTunnelDug(socket, playerID, x, y);
        }
    }

    protected void trapAcquired(final int playerID, final int trapID) throws TimeoutException {
        for (final int socket : listOfCommID) {
            this.assertTrapAcquired(socket, playerID, trapID);
        }
    }

    protected void monsterHired(final int monsterID, final int playerID) throws TimeoutException {
        for (final int socket : listOfCommID) {
            this.assertMonsterHired(socket, monsterID, playerID);
        }
    }

    protected void roomBuilt(final int playerID, final int roomID, final int x, final int y)
            throws TimeoutException {
        for (final int socket : listOfCommID) {
            this.assertRoomBuilt(socket, playerID, roomID, x, y);
        }
    }

    protected void bidRetrieved(final List<BidType> lockedBids, final int playerID)
            throws TimeoutException {
        for (final BidType bid : lockedBids) {
            for (final int socket : listOfCommID) {
                this.assertBidRetrieved(socket, bid, playerID);
            }
        }
    }

    protected void adventurerArrived(final int adventurerID, final int playerID)
            throws TimeoutException {
        for (final int socket : listOfCommID) {
            this.assertAdventurerArrived(socket, adventurerID, playerID);
        }
    }

    protected void broadcaster(final int numberOfLoops) throws TimeoutException {
        for (int i = 0; i < numberOfLoops; i++) {
            assertSendEvents();
        }
    }

    protected void drawAllMinusAdventurers() throws TimeoutException {
        this.monsters = this.parserInvocation.getNextMonsters(3);
        this.rooms = this.parserInvocation.getNextRooms(2);
        for (final Integer i : this.monsters) {
            this.monsterDrawn(i);
        }
        for (final Integer i : this.rooms) {
            this.roomDrawn(i);
        }
    }

    protected void drawAll(final Integer numberOfPlayer) throws TimeoutException {
        this.adventurers = this.parserInvocation.getNextAdventurers(numberOfPlayer);
        for (final Integer i : adventurers) {
            adventureDrawn(i);
        }
        drawAllMinusAdventurers();
    }

    protected void evaluateFood(final Integer slot, final Integer playerID)
            throws TimeoutException {
        switch (slot) {
            case 2 -> {
                this.evilnessChanged(1, playerID);
                this.foodChanged(3, playerID);
            }
            case 3 -> {
                this.evilnessChanged(2, playerID);
                this.foodChanged(3, playerID);
                this.goldChanged(1, playerID);
            }
            default -> {
                // if wrong input assume first bid
                this.goldChanged(-1, playerID);
                this.foodChanged(2, playerID);
            }
        }
    }

    protected void evaluateEvilness(final Integer slot, final Integer playerID)
            throws TimeoutException {
        switch (slot) {
            case 2 -> {
                this.evilnessChanged(-2, playerID);
            }
            case 3 -> {
                this.goldChanged(-1, playerID);
                this.evilnessChanged(-2, playerID);
            }
            default -> {
                // if wrong input assume first bid
                this.evilnessChanged(-1, playerID);
            }
        }
    }

    protected void evaluateImps(final Integer slot, final Integer playerID)
            throws TimeoutException {
        switch (slot) {
            case 2 -> {
                this.foodChanged(-2, playerID);
                this.impsChanged(2, playerID);
            }
            case 3 -> {
                this.foodChanged(-1, playerID);
                this.goldChanged(-1, playerID);
                this.impsChanged(2, playerID);
            }
            default -> {
                // if wrong input assume first bid
                this.foodChanged(-1, playerID);
                this.impsChanged(1, playerID);
            }
        }
    }

    protected void evaluateTraps(final Integer slot, final Integer playerID)
            throws TimeoutException {
        switch (slot) {
            case 2 -> {
                trapAcquired(playerID, this.parserInvocation.getNextTrap());
            }
            case 3 -> {
                this.goldChanged(-2, playerID);
                this.trapAcquired(playerID, this.parserInvocation.getNextTrap());
                this.trapAcquired(playerID, this.parserInvocation.getNextTrap());
            }
            default -> {
                // if wrong input assume first bid
                this.goldChanged(-1, playerID);
                this.trapAcquired(playerID, this.parserInvocation.getNextTrap());
            }
        }
    }

    protected void evaluateGold(final Integer slot, final Integer playerID,
            final Integer availableTunnelLength) throws TimeoutException {
        Integer miningPermits;
        switch (slot) {
            case 2 -> {
                miningPermits = 3;
            }
            case 3 -> {
                miningPermits = 5;
            }
            default -> {
                // if wrong input assume first bid
                miningPermits = 2;
            }
        }
        if (availableTunnelLength < miningPermits) {
            miningPermits = availableTunnelLength;
        }
        this.impsChanged(-miningPermits, playerID);
    }

    protected void placeBidSuccessful(final BidType bid, final Integer slot, final Integer socket)
            throws TimeoutException {
        sendPlaceBid(socket, bid, slot);
        bidPlaced(bid, socket, slot); // socket should be player id here
    }


    protected void assertSendEvents() throws TimeoutException {
        for (final int socket : listOfCommID) {
            this.assertEvent(socket);
        }
    }


    // Broadcast event for Combat phase
    // Individual Events: SetBattleground(), DefendYourself()
    protected void battleGroundSet(final int player, final int x, final int y)
            throws TimeoutException {
        for (final int socket : listOfCommID) {
            this.assertBattleGroundSet(socket, player, x, y);
        }
    }

    protected void trapPlaced(final int player, final int trap) throws TimeoutException {
        for (final int socket : listOfCommID) {
            this.assertTrapPlaced(socket, player, trap);
        }
    }

    protected void monsterPlaced(final int monster, final int player) throws TimeoutException {
        for (final int socket : listOfCommID) {
            this.assertMonsterPlaced(socket, monster, player);
        }
    }

    protected void adventurerDamaged(final int adventurer, final int amount)
            throws TimeoutException {
        for (final int socket : listOfCommID) {
            this.assertAdventurerDamaged(socket, adventurer, amount);
        }
    }

    protected void adventurerImprisoned(final int adventurer) throws TimeoutException {
        for (final int socket : listOfCommID) {
            this.assertAdventurerImprisoned(socket, adventurer);
        }
    }

    protected void adventurerFled(final int adventurer) throws TimeoutException {
        for (final int socket : listOfCommID) {
            this.assertAdventurerFled(socket, adventurer);
        }
    }

    protected void adventurerHealed(final int amount, final int priest, final int target)
            throws TimeoutException {
        for (final int socket : listOfCommID) {
            this.assertAdventurerHealed(socket, amount, priest, target);
        }
    }

    protected void tunnelConquered(final int adventurer, final int x, final int y)
            throws TimeoutException {
        for (final int socket : listOfCommID) {
            this.assertTunnelConquered(socket, adventurer, x, y);
        }
    }


    protected void wrapperBeforeCombat() throws TimeoutException {
        final int numberOfPlayer = 3;
        registrationEventWrapper(numberOfPlayer);
        // round 1 already started
        place1();
        eval1();
        // bid re , imps, gold, adv arr
        bidRetrieved(List.of(BidType.MONSTER), 0);
        bidRetrieved(List.of(BidType.FOOD), 1);
        bidRetrieved(List.of(BidType.NICENESS), 2);
        impsChanged(2, 0);
        impsChanged(3, 1);
        impsChanged(1, 2);
        goldChanged(1, 2);
        adventurerArrived(2, 1);
        adventurerArrived(29, 2);
        adventurerArrived(23, 0);
        // round 2
        nextRound(2);
        broadcaster(numberOfPlayer); //AdventurerDrawn
        broadcaster(3); //MonsterDrawn
        broadcaster(2); //RoomDrawn
        biddingStarted();
        actNowBroadcast();
        place2();
        eval2();
        // bid re , imps, gold, adv arr
        bidRetrieved(List.of(BidType.TUNNEL), 0);
        bidRetrieved(List.of(BidType.FOOD), 0);
        bidRetrieved(List.of(BidType.MONSTER), 0);
        bidRetrieved(List.of(BidType.IMPS), 1);
        bidRetrieved(List.of(BidType.TUNNEL), 1);
        bidRetrieved(List.of(BidType.NICENESS), 1);
        bidRetrieved(List.of(BidType.GOLD), 2);
        bidRetrieved(List.of(BidType.MONSTER), 2);
        bidRetrieved(List.of(BidType.FOOD), 2);
        impsChanged(3, 0);
        goldChanged(3, 0);
        impsChanged(2, 1);
        goldChanged(2, 1);
        adventurerArrived(0, 2);
        adventurerArrived(18, 1);
        adventurerArrived(11, 0);
        // round 3
        nextRound(3);
        broadcaster(numberOfPlayer); //AdventurerDrawn
        broadcaster(3); //MonsterDrawn
        broadcaster(2); //RoomDrawn
        biddingStarted();
        actNowBroadcast();
        place3();
        eval3();
        // bid re , imps, gold, adv arr
        bidRetrieved(List.of(BidType.NICENESS), 0);
        bidRetrieved(List.of(BidType.GOLD), 0);
        bidRetrieved(List.of(BidType.TRAP), 0);
        bidRetrieved(List.of(BidType.GOLD), 1);
        bidRetrieved(List.of(BidType.FOOD), 1);
        bidRetrieved(List.of(BidType.IMPS), 1);
        bidRetrieved(List.of(BidType.NICENESS), 2);
        bidRetrieved(List.of(BidType.IMPS), 2);
        bidRetrieved(List.of(BidType.FOOD), 2);
        impsChanged(2, 1);
        impsChanged(1, 2);
        goldChanged(1, 2);
        adventurerArrived(9, 1);
        adventurerArrived(20, 2);
        adventurerArrived(6, 0);
        // round 4
        nextRound(4);
        broadcaster(3); //MonsterDrawn
        broadcaster(2); //RoomDrawn
        biddingStarted();
        actNowBroadcast();
        place4();
        eval4();
        // bid re , imps, gold, adv arr
        bidRetrieved(List.of(BidType.ROOM), 0);
        bidRetrieved(List.of(BidType.FOOD), 0);
        bidRetrieved(List.of(BidType.TRAP), 0);
        bidRetrieved(List.of(BidType.NICENESS), 1);
        bidRetrieved(List.of(BidType.TUNNEL), 1);
        bidRetrieved(List.of(BidType.FOOD), 1);
        bidRetrieved(List.of(BidType.GOLD), 2);
        bidRetrieved(List.of(BidType.MONSTER), 2);
        bidRetrieved(List.of(BidType.FOOD), 2);
        impsChanged(2, 0);
        impsChanged(2, 1);
        goldChanged(2, 1);
    }

    protected void place1() throws TimeoutException {
        // player 0
        sendPlaceBid(0, BidType.MONSTER, 1);
        bidPlaced(BidType.MONSTER, 0, 1);
        assertActNow(0);
        sendPlaceBid(0, BidType.TUNNEL, 2);
        bidPlaced(BidType.TUNNEL, 0, 2);
        assertActNow(0);
        sendPlaceBid(0, BidType.FOOD, 3);
        bidPlaced(BidType.FOOD, 0, 3);
        // player 1
        sendPlaceBid(1, BidType.FOOD, 1);
        bidPlaced(BidType.FOOD, 1, 1);
        assertActNow(1);
        sendPlaceBid(1, BidType.IMPS, 2);
        bidPlaced(BidType.IMPS, 1, 2);
        assertActNow(1);
        sendPlaceBid(1, BidType.TUNNEL, 3);
        bidPlaced(BidType.TUNNEL, 1, 3);
        // player 2
        sendPlaceBid(2, BidType.NICENESS, 1);
        bidPlaced(BidType.NICENESS, 2, 1);
        assertActNow(2);
        sendPlaceBid(2, BidType.GOLD, 2);
        bidPlaced(BidType.GOLD, 2, 2);
        assertActNow(2);
        sendPlaceBid(2, BidType.MONSTER, 3);
        bidPlaced(BidType.MONSTER, 2, 3);
    }

    protected void eval1() throws TimeoutException {
        // eval food
        goldChanged(-1, 1);
        foodChanged(2, 1);
        evilnessChanged(1, 0);
        foodChanged(3, 0);
        // nice
        evilnessChanged(-1, 2);
        // dig
        assertDigTunnel(0);
        assertActNow(0);
        sendDigTunnel(0, 1, 0);
        impsChanged(-1, 0);
        tunnelDug(0, 1, 0);
        assertActNow(0);
        sendDigTunnel(0, 2, 0);
        impsChanged(-1, 0);
        tunnelDug(0, 2, 0);
        // dig next
        assertDigTunnel(1);
        assertActNow(1);
        sendDigTunnel(1, 1, 0);
        impsChanged(-1, 1);
        tunnelDug(1, 1, 0);
        assertActNow(1);
        sendDigTunnel(1, 2, 0);
        impsChanged(-1, 1);
        tunnelDug(1, 2, 0);
        assertActNow(1);
        sendDigTunnel(1, 3, 0);
        impsChanged(-1, 1);
        tunnelDug(1, 3, 0);
        // gold
        impsChanged(-1, 2);
        // imps
        foodChanged(-1, 1);
        impsChanged(1, 1);
        // monster
        assertSelectMonster(0);
        assertActNow(0);
        sendHireMonster(0, 9);
        evilnessChanged(3, 0);
        monsterHired(9, 0);
        // next
        assertSelectMonster(2);
        assertActNow(2);
        sendHireMonster(2, 13);
        foodChanged(-1, 2);
        evilnessChanged(1, 2);
        monsterHired(13, 2);
    }


    protected void place2() throws TimeoutException {
        // player 0
        sendPlaceBid(0, BidType.MONSTER, 1);
        bidPlaced(BidType.MONSTER, 0, 1);
        assertActNow(0);
        sendPlaceBid(0, BidType.NICENESS, 2);
        bidPlaced(BidType.NICENESS, 0, 2);
        assertActNow(0);
        sendPlaceBid(0, BidType.GOLD, 3);
        bidPlaced(BidType.GOLD, 0, 3);
        // player 1
        sendPlaceBid(1, BidType.NICENESS, 1);
        bidPlaced(BidType.NICENESS, 1, 1);
        assertActNow(1);
        sendPlaceBid(1, BidType.GOLD, 2);
        bidPlaced(BidType.GOLD, 1, 2);
        assertActNow(1);
        sendPlaceBid(1, BidType.FOOD, 3);
        bidPlaced(BidType.FOOD, 1, 3);
        // player 2
        sendPlaceBid(2, BidType.FOOD, 1);
        bidPlaced(BidType.FOOD, 2, 1);
        assertActNow(2);
        sendPlaceBid(2, BidType.NICENESS, 2);
        bidPlaced(BidType.NICENESS, 2, 2);
        assertActNow(2);
        sendPlaceBid(2, BidType.IMPS, 3);
        bidPlaced(BidType.IMPS, 2, 3);
    }

    protected void eval2() throws TimeoutException {
        // food
        goldChanged(-1, 2);
        foodChanged(2, 2);
        evilnessChanged(1, 1);
        foodChanged(3, 1);
        // nice
        evilnessChanged(-1, 1);
        evilnessChanged(-2, 2);
        goldChanged(-1, 0);
        evilnessChanged(-2, 0);
        // gold
        impsChanged(-2, 1);
        impsChanged(-3, 0);
        //imps
        foodChanged(-1, 2);
        impsChanged(1, 2);
        // monster
        assertSelectMonster(0);
        assertActNow(0);
        sendHireMonster(0, 7);
        monsterHired(7, 0);
    }

    protected void place3() throws TimeoutException {
        // player 0
        sendPlaceBid(0, BidType.TRAP, 1);
        bidPlaced(BidType.TRAP, 0, 1);
        assertActNow(0);
        sendPlaceBid(0, BidType.ROOM, 2);
        bidPlaced(BidType.ROOM, 0, 2);
        assertActNow(0);
        sendPlaceBid(0, BidType.FOOD, 3);
        bidPlaced(BidType.FOOD, 0, 3);
        // player 1
        sendPlaceBid(1, BidType.IMPS, 1);
        bidPlaced(BidType.IMPS, 1, 1);
        assertActNow(1);
        sendPlaceBid(1, BidType.NICENESS, 2);
        bidPlaced(BidType.NICENESS, 1, 2);
        assertActNow(1);
        sendPlaceBid(1, BidType.TUNNEL, 3);
        bidPlaced(BidType.TUNNEL, 1, 3);
        // player 2
        sendPlaceBid(2, BidType.FOOD, 1);
        bidPlaced(BidType.FOOD, 2, 1);
        assertActNow(2);
        sendPlaceBid(2, BidType.GOLD, 2);
        bidPlaced(BidType.GOLD, 2, 2);
        assertActNow(2);
        sendPlaceBid(2, BidType.MONSTER, 3);
        bidPlaced(BidType.MONSTER, 2, 3);
    }

    protected void eval3() throws TimeoutException {
        // food
        goldChanged(-1, 2);
        foodChanged(2, 2);
        evilnessChanged(1, 0);
        foodChanged(3, 0);
        // nice
        evilnessChanged(-1, 1);
        //dig
        assertDigTunnel(1);
        assertActNow(1);
        sendDigTunnel(1, 0, 1);
        impsChanged(-1, 1);
        tunnelDug(1, 0, 1);
        assertActNow(1);
        sendDigTunnel(1, 0, 2);
        impsChanged(-1, 1);
        tunnelDug(1, 0, 2);
        // gold
        impsChanged(-1, 2);
        // imps
        foodChanged(-1, 1);
        impsChanged(1, 1);
        // traps
        goldChanged(-1, 0);
        trapAcquired(0, 26);
        // monster
        assertSelectMonster(2);
        assertActNow(2);
        sendHireMonster(2, 3);
        foodChanged(-2, 2);
        evilnessChanged(1, 2);
        monsterHired(3, 2);
        //room
        goldChanged(-1, 0);
        assertPlaceRoom(0);
        assertActNow(0);
        sendBuildRoom(0, 1, 0, 0);
        assertActionFailed(0);
        assertActNow(0);
        sendEndTurn(0);
    }

    protected void place4() throws TimeoutException {
        // player 0
        sendPlaceBid(0, BidType.TRAP, 1);
        bidPlaced(BidType.TRAP, 0, 1);
        assertActNow(0);
        sendPlaceBid(0, BidType.NICENESS, 2);
        bidPlaced(BidType.NICENESS, 0, 2);
        assertActNow(0);
        sendPlaceBid(0, BidType.TUNNEL, 3);
        bidPlaced(BidType.TUNNEL, 0, 3);
        // player 1
        sendPlaceBid(1, BidType.FOOD, 1);
        bidPlaced(BidType.FOOD, 1, 1);
        assertActNow(1);
        sendPlaceBid(1, BidType.IMPS, 2);
        bidPlaced(BidType.IMPS, 1, 2);
        assertActNow(1);
        sendPlaceBid(1, BidType.GOLD, 3);
        bidPlaced(BidType.GOLD, 1, 3);
        // player 2
        sendPlaceBid(2, BidType.FOOD, 1);
        bidPlaced(BidType.FOOD, 2, 1);
        assertActNow(2);
        sendPlaceBid(2, BidType.IMPS, 2);
        bidPlaced(BidType.IMPS, 2, 2);
        assertActNow(2);
        sendPlaceBid(2, BidType.NICENESS, 3);
        bidPlaced(BidType.NICENESS, 2, 3);
    }

    protected void eval4() throws TimeoutException {
        // food
        goldChanged(-1, 1);
        foodChanged(2, 1);
        evilnessChanged(1, 2);
        foodChanged(3, 2);
        // nice
        evilnessChanged(-1, 0);
        evilnessChanged(-2, 2);
        //dig
        assertDigTunnel(0);
        assertActNow(0);
        sendDigTunnel(0, 3, 0);
        impsChanged(-1, 0);
        tunnelDug(0, 3, 0);
        assertActNow(0);
        sendDigTunnel(0, 4, 0);
        impsChanged(-1, 0);
        tunnelDug(0, 4, 0);
        //gold
        impsChanged(-2, 1);
        //imps
        foodChanged(-1, 1);
        impsChanged(1, 1);
        foodChanged(-2, 2);
        impsChanged(2, 2);
        //trap
        goldChanged(-1, 0);
        trapAcquired(0, 6);
    }

    // tunnel conquered for player id = 1 at (0,0) and (0,1)
    protected void combatWrapper() throws TimeoutException {
        wrapperBeforeCombat();
        // round 1 for player 0
        nextRound(1);
        assertSetBattleGround(0);
        leave(0, 0);
        // round 1 for player 2
        nextRound(1);
        assertSetBattleGround(1);
        assertActNow(1);
        leave(2, 2);
        sendBattleGround(1, 0, 0);
        battleGroundSet(1, 0, 0);
        assertDefendYourself(1);
        assertActNow(1);
        sendEndTurn(1);
        adventurerDamaged(2, 2);
        adventurerDamaged(18, 2);
        adventurerDamaged(9, 2);
        tunnelConquered(2, 0, 0);
        evilnessChanged(-1, 1);
        adventurerHealed(1, 9, 2);
        // round 2  for player 2
        nextRound(2);
        assertSetBattleGround(1);
        assertActNow(1);
        sendBattleGround(1, 0, 1);
        battleGroundSet(1, 0, 1);
        assertDefendYourself(1);
        assertActNow(1);
        sendEndTurn(1);
        adventurerDamaged(2, 2);
        adventurerImprisoned(18);
        adventurerImprisoned(9);
        tunnelConquered(2, 0, 1);
        evilnessChanged(-1, 1);
        // round 3  for player 2
        nextRound(3);
        assertSetBattleGround(1);
        assertActNow(1);
        sendBattleGround(1, 3, 0);
        assertActionFailed(1);
        assertActNow(1);
        sendBattleGround(1, 1, 0);
        battleGroundSet(1, 1, 0);
        assertDefendYourself(1);
        assertActNow(1);
        sendEndTurn(1);
        adventurerImprisoned(2);
        nextYear(2);
    }
}

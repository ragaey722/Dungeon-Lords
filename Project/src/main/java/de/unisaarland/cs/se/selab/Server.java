package de.unisaarland.cs.se.selab;


import de.unisaarland.cs.se.selab.actionpackage.Actions;
import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.datapackage.GameDataClass;
import de.unisaarland.cs.se.selab.gamelogic.RegistrationPhase;
import de.unisaarland.cs.se.selab.gamelogic.State;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * this class is used for communication and running of the game
 */
public class Server {

    private final String configJSONString;

    private final int maxPlayers;
    private final int maxYears;
    private final ServerConnection<Actions> serverConnection;
    private final GameDataClass gameData;
    private final List<Integer> playerIDs;
    private final Map<Integer, Integer> commIDs;
    private final Map<Integer, String> hashPlayerName;
    private State currentState;
    private boolean terminate;
    private boolean isRegistrationPhase;

    /**
     * a method that instantiates the Server object and sets internal values
     *
     * @param game a GameDataClass containing all data of the Game
     * @param sc a ServerConnection class used for communication with the clients
     * @param maxPlayers the maximum amount of players allowed in the game
     * @param maxYears the years after which the game ends
     * @param configJSONString the config in a string to send to the clients
     */
    public Server(final GameDataClass game, final ServerConnection<Actions> sc,
            final int maxPlayers, final int maxYears, final String configJSONString) {
        this.currentState = new RegistrationPhase(this);
        this.gameData = game;
        this.maxPlayers = maxPlayers;
        this.maxYears = maxYears;
        this.serverConnection = sc;
        this.configJSONString = configJSONString;
        this.playerIDs = new ArrayList<>();
        this.commIDs = new HashMap<>();
        this.hashPlayerName = new HashMap<>();
        this.isRegistrationPhase = true;
    }

    /**
     * the method in which the entire game runs
     * when we run into a scenario where the server needs to terminate
     * we set the terminate boolean to true and this will break the loop
     * and return from this the method which terminates the server
     * if the loop was broken after max years was reached then we evaluate scores
     * and broadcast gameEnd events
     */
    public void run() {
        while (!this.terminate && this.gameData.getYear() <= this.maxYears) {
            this.currentState.run();
        }
        if (gameData.getYear() > maxYears) {
            evaluateGame();
        }
    }

    /**
     * a method that is called, when the game ends,
     * it evaluates the scores and sends the gameEndBroadcast
     */
    public void evaluateGame() {
        final GameEvaluator gameEvaluator = new GameEvaluator(this.gameData.getAllDungeonLords());
        final List<Integer> dg = gameEvaluator.evaluate();
        final int highScore = gameEvaluator.getHighScore();
        for (final int id : dg) {
            gameEndBroadcast(id, highScore);
        }
    }

    public boolean getIsRegistrationPhase() {
        return isRegistrationPhase;
    }

    public void setIsRegistrationPhase(final boolean isRegistrationPhase) {
        this.isRegistrationPhase = isRegistrationPhase;
    }

    public Boolean getTerminate() {
        return this.terminate;
    }

    public void setTerminate(final boolean terminate) {
        this.terminate = terminate;
    }

    /**
     * ignores actions from unregistered players and only return actions from registered players
     * unless it is registration phase where we return actions from any player
     *
     * @return  an action from a player who is registered
     * or action from any player if we are still reregistration phase
     */
    public Actions getNextAction() {
        try {
            final Actions na = this.serverConnection.nextAction();
            if (this.commIDs.containsKey(na.getCommID()) || this.isRegistrationPhase) {
                return na;
            }
            return getNextAction();
        } catch (TimeoutException e) {
            return null;
        }
    }


    /**
     * updater for "state machine"
     *
     * @param state is the next state to be used
     */
    public void updateCurrentState(final State state) {
        currentState = state;
    }


    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getMaxYears() {
        return maxYears;
    }

    public GameDataClass getGameDataClass() {
        return gameData;
    }

    public List<Integer> getAllPlayerIDs() {
        return playerIDs;
    }

    public Map<Integer, Integer> getAllCommIDs() {
        return commIDs;
    }

    public int getPlayerIDByCommID(final int commID) {
        return this.commIDs.get(commID);
    }

    /**
     * a method to get the commID for a corresponding playerID
     *
     * @param playerID is a valid id of a player as Integer
     * @return a valid commID for the given playerID
     */
    public int getCommIDByPlayerID(final int playerID) {
        final Set<Integer> keySet = this.commIDs.keySet();
        final List<Integer> keys = new ArrayList<>(keySet);
        for (final Integer key : keys) {
            if (this.commIDs.get(key).equals(playerID)) {
                return key;
            }
        }
        throw new IllegalArgumentException("Player not found");
    }

    public int getNumberOfPlayers() {
        return playerIDs.size();
    }

    public void removePlayerID(final int id) {
        this.playerIDs.remove(Integer.valueOf(id));
    }

    public void removeCommID(final int commID) {
        this.commIDs.remove(commID);
    }



    //methods that send private events
    public void actNow(final int commID) {
        this.serverConnection.sendActNow(commID);
    }

    public void actionFailed(final int commID, final String s) {
        this.serverConnection.sendActionFailed(commID, s);
    }

    public void config(final int commID) {
        this.serverConnection.sendConfig(commID, this.configJSONString);
    }

    public void defendYourself(final int commID) {
        this.serverConnection.sendDefendYourself(commID);
    }

    public void battleGround(final int commID) {
        this.serverConnection.sendSetBattleGround(commID);
    }

    public void digTunnel(final int commID) {
        this.serverConnection.sendDigTunnel(commID);
    }

    public void placeRoom(final int commID) {
        this.serverConnection.sendPlaceRoom(commID);
    }

    public void selectMonster(final int commID) {
        this.serverConnection.sendSelectMonster(commID);
    }

    /**
     * return a list of commIDs in the order of player IDs,
     * so a broadcast can just iterate over a list of commIDs,
     * and send events in the right order
     *
     * @return commIDs which is a sorted list of the commIDs in the game
     */
    private List<Integer> commIDs() {
        final List<Integer> commIDs = new ArrayList<>(this.playerIDs.size());
        for (final Integer id : this.playerIDs) {
            final int commID = getCommIDByPlayerID(id);
            commIDs.add(commID);
        }
        return commIDs;
    }

    /**
     * a helper function for broadcasting
     * takes a lambda and acts on it for all registered commIDs
     *
     * @param lambda is a function, that calls a ServerConnection function,
     *               which gets called with all registered commIDs
     */
    private void broadcaster(final Consumer<Integer> lambda) {
        for (final Integer id : commIDs()) {
            lambda.accept(id);
        }
    }


    //methods that broadcast events to all players
    public void actNowBroadCast() {
        broadcaster(this.serverConnection::sendActNow);
    }

    public void adventurerArrivedBroadcast(final int adventurer, final int player) {
        broadcaster((Integer id) ->
                this.serverConnection.sendAdventurerArrived(id, adventurer, player));
    }

    public void adventurerDamagedBroadcast(final int adventurer, final int amount) {
        broadcaster((Integer id) ->
                this.serverConnection.sendAdventurerDamaged(id, adventurer, amount));
    }

    public void adventurerDrawnBroadcast(final int adventurer) {
        broadcaster((Integer id) -> this.serverConnection.sendAdventurerDrawn(id, adventurer));
    }

    public void adventurerFledBroadcast(final int adventurer) {
        broadcaster((Integer id) -> this.serverConnection.sendAdventurerFled(id, adventurer));
    }

    public void adventurerHealedBroadcast(final int amount, final int priest, final int target) {
        broadcaster((Integer id) ->
                this.serverConnection.sendAdventurerHealed(id, amount, priest, target));
    }

    public void adventurerImprisonedBroadcast(final int adventurer) {
        broadcaster((Integer id) -> this.serverConnection.sendAdventurerImprisoned(id, adventurer));
    }

    public void battleGroundSetBroadcast(final int player, final int x, final int y) {
        broadcaster((Integer id) -> this.serverConnection.sendBattleGroundSet(id, player, x, y));
    }

    public void biddingStartedBroadcast() {
        broadcaster(this.serverConnection::sendBiddingStarted);
    }

    public void evilnessChangedBroadcast(final int amount, final int player) {
        broadcaster((Integer id) -> this.serverConnection.sendEvilnessChanged(id, amount, player));
    }

    public void foodChangedBroadcast(final int amount, final int player) {
        broadcaster((Integer id) -> this.serverConnection.sendFoodChanged(id, amount, player));
    }

    public void gameEndBroadcast(final int player, final int points) {
        broadcaster((Integer id) -> this.serverConnection.sendGameEnd(id, player, points));
    }

    public void gameStartedBroadcast() {
        broadcaster(this.serverConnection::sendGameStarted);
    }

    public void goldChangedBroadcast(final int amount, final int player) {
        broadcaster((Integer id) -> this.serverConnection.sendGoldChanged(id, amount, player));
    }

    public void impsChangedBroadcast(final int amount, final int player) {
        broadcaster((Integer id) -> this.serverConnection.sendImpsChanged(id, amount, player));
    }

    public void leftBroadcast(final int player) {
        broadcaster((Integer id) -> this.serverConnection.sendLeft(id, player));
    }

    public void monsterDrawnBroadcast(final int monster) {
        broadcaster((Integer id) -> this.serverConnection.sendMonsterDrawn(id, monster));
    }

    public void monsterHiredBroadcast(final int monster, final int player) {
        broadcaster((Integer id) -> this.serverConnection.sendMonsterHired(id, monster, player));
    }

    public void monsterPlacedBroadcast(final int monster, final int player) {
        broadcaster((Integer id) -> this.serverConnection.sendMonsterPlaced(id, monster, player));
    }

    public void nextRoundBroadcast(final int round) {
        broadcaster((Integer id) -> this.serverConnection.sendNextRound(id, round));
    }

    public void nextYearBroadcast(final int year) {
        broadcaster((Integer id) -> this.serverConnection.sendNextYear(id, year));
    }

    public void bidPlacedBroadcast(final BidType bid, final int player, final int slot) {
        broadcaster((Integer id) -> this.serverConnection.sendBidPlaced(id, bid, player, slot));
    }

    public void bidRetrievedBroadcast(final BidType bid, final int player) {
        broadcaster((Integer id) -> this.serverConnection.sendBidRetrieved(id, bid, player));
    }

    public void playerBroadcast(final String name, final int player) {
        broadcaster((Integer id) -> this.serverConnection.sendPlayer(id, name, player));
    }

    public void registrationAbortedBroadcast() {
        broadcaster(this.serverConnection::sendRegistrationAborted);
    }

    public void roomActivatedBroadcast(final int player, final int room) {
        broadcaster((Integer id) -> this.serverConnection.sendRoomActivated(id, player, room));
    }

    public void roomBuiltBroadcast(final int player, final int room, final int x, final int y) {
        broadcaster((Integer id) -> this.serverConnection.sendRoomBuilt(id, player, room, x, y));
    }

    public void roomDrawnBroadcast(final int room) {
        broadcaster((Integer id) -> this.serverConnection.sendRoomDrawn(id, room));
    }

    public void trapAcquiredBroadcast(final int player, final int trap) {
        broadcaster((Integer id) -> this.serverConnection.sendTrapAcquired(id, player, trap));
    }

    public void trapPlacedBroadcast(final int player, final int trap) {
        broadcaster((Integer id) -> this.serverConnection.sendTrapPlaced(id, player, trap));
    }

    public void tunnelConqueredBroadcast(final int adventurer, final int x, final int y) {
        broadcaster((Integer id) ->
                this.serverConnection.sendTunnelConquered(id, adventurer, x, y));
    }

    public void tunnelDugBroadcast(final int player, final int x, final int y) {
        broadcaster((Integer id) -> this.serverConnection.sendTunnelDug(id, player, x, y));
    }

    public void addCommID(final int commID, final int playerID) {
        this.commIDs.put(commID, playerID);
    }

    public void addPlayerID(final int playerID) {
        this.playerIDs.add(playerID);
    }

    public void addPlayerName(final int playerID, final String playerName) {
        this.hashPlayerName.put(playerID, playerName);
    }

    public Map<Integer, String> getHashPlayerName() {
        return this.hashPlayerName;
    }


    public String getConfigJSONString() {
        return configJSONString;
    }
}
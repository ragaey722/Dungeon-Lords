package de.unisaarland.cs.se.selab.gamelogic;

import de.unisaarland.cs.se.selab.Server;
import de.unisaarland.cs.se.selab.actionpackage.ActivateRoomAction;
import de.unisaarland.cs.se.selab.actionpackage.BattleGroundAction;
import de.unisaarland.cs.se.selab.actionpackage.BuildRoomAction;
import de.unisaarland.cs.se.selab.actionpackage.DigTunnelAction;
import de.unisaarland.cs.se.selab.actionpackage.EndTurnAction;
import de.unisaarland.cs.se.selab.actionpackage.HireMonsterAction;
import de.unisaarland.cs.se.selab.actionpackage.LeaveAction;
import de.unisaarland.cs.se.selab.actionpackage.MonsterAction;
import de.unisaarland.cs.se.selab.actionpackage.MonsterTargetedAction;
import de.unisaarland.cs.se.selab.actionpackage.PlaceBidAction;
import de.unisaarland.cs.se.selab.actionpackage.RegisterAction;
import de.unisaarland.cs.se.selab.actionpackage.StartGameAction;
import de.unisaarland.cs.se.selab.actionpackage.TrapAction;
import de.unisaarland.cs.se.selab.datapackage.Dungeon;
import de.unisaarland.cs.se.selab.datapackage.DungeonLord;
import de.unisaarland.cs.se.selab.datapackage.GameDataClass;
import de.unisaarland.cs.se.selab.datapackage.Resources;
import de.unisaarland.cs.se.selab.datapackage.Room;
import java.util.Map;

public abstract class State {

    protected final Server server;

    public State(final Server server) {
        this.server = server;
    }

    public abstract void run();

    /**
     * default behaviour that send action failed for all states
     */
    public void actOn(final RegisterAction action) {
        server.actionFailed(action.getCommID(), "cannot register!");
    }

    /**
     * default behaviour that send action failed for all states
     */
    public void actOn(final StartGameAction action) {
        server.actionFailed(action.getCommID(), "game already started!");
    }

    /**
     * default behaviour that send action failed for all states
     */
    public void actOn(final EndTurnAction action) {
        server.actionFailed(action.getCommID(), "cannot do end turn!");
    }

    /**
     * default behaviour that send action failed for all states
     */
    public void actOn(final HireMonsterAction action) {
        server.actionFailed(action.getCommID(), "cannot hire a monster!");
    }

    /**
     * default behaviour of handling player leave which calls the method playerLeave
     *
     * @param action this object contains the commID of the player
     */
    public void actOn(final LeaveAction action) {
        playerLeave(action.getCommID());
    }

    /**
     * default behaviour that send action failed for all states
     */
    public void actOn(final MonsterAction action) {
        server.actionFailed(action.getCommID(), "cannot place a monster!");
    }

    /**
     * default behaviour that send action failed for all states
     */
    public void actOn(final PlaceBidAction action) {
        server.actionFailed(action.getCommID(), "cannot place bid!");
    }

    /**
     * default behaviour that activates the room for the player for all states, if there are no
     * restriction violations for activating the room, imps changed and room activated events would
     * be sent respectively, otherwise action failed would be sent
     *
     * @param action this object contains the roomID as well as the commID
     */
    public void actOn(final ActivateRoomAction action) {
        final int commID = action.getCommID();
        final int roomID = action.getRoom();
        final int playerID = server.getPlayerIDByCommID(commID);
        final DungeonLord dl = server.getGameDataClass().getDungeonLord(playerID);
        final Map<Integer, Room> rooms = dl.getRooms();
        if (!rooms.containsKey(roomID)) {
            server.actionFailed(commID, "room doesn't exist!");
            return;
        }
        final Room selectedRoom = rooms.get(roomID);
        if (selectedRoom.isActive()) {
            server.actionFailed(commID, "room already active!");
            return;
        }
        if (selectedRoom.isConquered()) {
            server.actionFailed(commID, "room already conquered");
            return;
        }
        final int impsCost = selectedRoom.getImpsCost();
        final Resources resources = dl.getResources();
        if (!resources.addBusyImpsRoom(impsCost)) {
            server.actionFailed(commID, "insufficient amount of available imps!");
            return;
        }
        selectedRoom.activate();
        server.impsChangedBroadcast(-impsCost, playerID);
        server.roomActivatedBroadcast(playerID, roomID);
    }

    /**
     * default behaviour that send action failed for all states
     */
    public void actOn(final BattleGroundAction action) {
        server.actionFailed(action.getCommID(), "cannot set battle ground!");
    }

    /**
     * default behaviour that send action failed for all states
     */
    public void actOn(final DigTunnelAction action) {
        server.actionFailed(action.getCommID(), "cannot dig tunnel!");
    }

    /**
     * default behaviour that send action failed for all states
     */
    public void actOn(final TrapAction action) {
        server.actionFailed(action.getCommID(), "cannot set Trap!");
    }

    /**
     * default behaviour that send action failed for all states
     */
    public void actOn(final BuildRoomAction action) {
        server.actionFailed(action.getCommID(), "cannot build a room!");
    }

    /**
     * default behaviour that send action failed for all states
     */
    public void actOn(final MonsterTargetedAction action) {
        server.actionFailed(action.getCommID(), "cannot place monster targeted!");
    }

    /**
     * removing the comm id, player id, and player name from the server and also removing the
     * dungeon lord from the game data class. The event left would be sent if only the current phase
     * is not registration and following that the event adventurerFled would be sent if the player
     * has imprisoned adventurers. If the there are no player left the server would terminate.
     *
     * @param commID used to fetch the player id from the server for the dungeon lord in game data
     *               class
     */
    protected void playerLeave(final int commID) {
        final int playerID = server.getPlayerIDByCommID(commID);
        final GameDataClass gd = server.getGameDataClass();
        final DungeonLord dl = gd.getDungeonLord(playerID);
        final Dungeon dungeon = dl.getDungeon();
        //delete playerID and commID in server
        server.getHashPlayerName().remove(playerID);
        server.removePlayerID(playerID);
        server.removeCommID(commID);
        if (!server.getIsRegistrationPhase()) {
            server.leftBroadcast(playerID);
        }
        //release imprisoned adventurer
        while (dungeon.isThereCapturedAdventurers()) {
            server.adventurerFledBroadcast(dungeon.releaseAdventurer());
        }
        //delete dungeonLord in gameDataClass
        gd.removeDungeonLord(playerID);
        if (server.getNumberOfPlayers() == 0) {
            server.setTerminate(true);
        }
    }

    /**
     * subtract the resources that is fetch from the dungeon lord with the cost, if it can be
     * subtracted a series of events in the order of evilness -> food -> coin if only the changes
     * are non-zero would be sent.
     *
     * @param dl   dungeon lord that contains the player id and the resources
     * @param cost resources that used to subtract the resources of the dungeon lord
     * @return true if any fields of the resources of the dungeon lord can be subtracted
     */
    protected boolean subtractResources(final DungeonLord dl, final Resources cost) {
        final Resources res = dl.getResources();
        if (!res.canResourceBeSubtracted(cost)) {
            return false;
        }
        final int playerID = dl.getPlayerID();
        final int costCoins = cost.getCoins();
        final int costEvilness = cost.getEvilness();
        final int costFoods = cost.getFoods();
        if (res.upEvilness(costEvilness) && costEvilness != 0) {
            server.evilnessChangedBroadcast(costEvilness, playerID);
        }
        if (res.subtractFood(costFoods) && costFoods != 0) {
            server.foodChangedBroadcast(-costFoods, playerID);
        }
        if (res.subtractCoins(costCoins) && costCoins != 0) {
            server.goldChangedBroadcast(-costCoins, playerID);
        }
        return true;
    }

    /**
     * add the resources that is fetch from the dungeon lord with the cost, a series of events in
     * the order of  food -> evilness -> coin -> imps if only the changes are non-zero would be
     * sent.
     *
     * @param dl     dungeon lord that contains the player id and the resources
     * @param reward resources that used to add the resources of the dungeon lord
     */
    protected void addResources(final DungeonLord dl, final Resources reward) {
        final Resources res = dl.getResources();
        final int playerID = dl.getPlayerID();
        final int rewardImps = reward.getImps();
        final int rewardCoins = reward.getCoins();
        final int rewardEvilness = reward.getEvilness();
        final int rewardFoods = reward.getFoods();
        res.addFood(rewardFoods);
        if (rewardFoods != 0) {
            server.foodChangedBroadcast(rewardFoods, playerID);
        }
        if (res.downEvilness(rewardEvilness) && rewardEvilness != 0) {
            server.evilnessChangedBroadcast(-rewardEvilness, playerID);
        }
        res.addCoins(rewardCoins);
        if (rewardCoins != 0) {
            server.goldChangedBroadcast(rewardCoins, playerID);
        }
        res.addImps(rewardImps);
        if (rewardImps != 0) {
            server.impsChangedBroadcast(rewardImps, playerID);
        }


    }
}



package de.unisaarland.cs.se.selab.gamelogic;

import de.unisaarland.cs.se.selab.Server;
import de.unisaarland.cs.se.selab.actionpackage.Actions;
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
import de.unisaarland.cs.se.selab.datapackage.BiddingSquare;
import de.unisaarland.cs.se.selab.datapackage.DungeonLord;
import de.unisaarland.cs.se.selab.datapackage.GameDataClass;

public class RegistrationPhase extends State {

    private int lastPlayerID;
    private boolean isStartGame;

    public RegistrationPhase(final Server server) {
        super(server);
        lastPlayerID = 0;
    }

    /**
     * updates the state of the server to bidding phase and some series of events will be sent
     * namely game started, player, and next year. All player ids would be added in ascending order
     * to the linked list in the bidding square.
     */
    private void startGame() {
        final BiddingSquare bs = server.getGameDataClass().getBiddingSquare();
        server.updateCurrentState(new BiddingPhase(server));
        server.gameStartedBroadcast();
        for (final int playerID : server.getAllPlayerIDs()) {
            final String playerName = server.getHashPlayerName().get(playerID);
            server.playerBroadcast(playerName, playerID);
        }
        for (final int id : server.getAllPlayerIDs()) {
            bs.addDungeonLord(id);
        }
        server.nextYearBroadcast(server.getGameDataClass().getYear());
        isStartGame = true;
        server.setIsRegistrationPhase(false);
    }

    /**
     *
     */
    @Override
    public void run() {
        while (!isStartGame && !server.getTerminate()) {
            final Actions action = server.getNextAction();
            if (action == null) {
                server.setTerminate(true);
                server.registrationAbortedBroadcast();
            } else {
                action.accept(this);
            }
            if (server.getNumberOfPlayers() == server.getMaxPlayers()) {
                startGame();
            }
        }
    }

    @Override
    public void actOn(final RegisterAction action) {
        final int commID = action.getCommID();
        if (server.getAllCommIDs().containsKey(commID)) {
            server.actionFailed(commID, "you have already registered!");
            return;
        }
        final int newPlayerID = lastPlayerID;
        final GameDataClass gd = server.getGameDataClass();
        server.addCommID(commID, newPlayerID);
        server.addPlayerID(newPlayerID);
        server.addPlayerName(newPlayerID, action.getPlayerName());
        final DungeonLord dl = new DungeonLord(newPlayerID, gd.getMaxSideLength(),
                gd.getInitialResources().cloneResource());
        gd.addDungeonLord(dl.getPlayerID(), dl);
        server.config(commID);
        lastPlayerID++;
    }

    @Override
    public void actOn(final StartGameAction action) {
        if (!server.getAllCommIDs().containsKey(action.getCommID())) {
            return;
        }
        startGame();
    }

    @Override
    public void actOn(final LeaveAction action) {
        if (!server.getAllCommIDs().containsKey(action.getCommID())) {
            return;
        }
        playerLeave(action.getCommID());
    }

    @Override
    public void actOn(final ActivateRoomAction action) {
        if (!server.getAllCommIDs().containsKey(action.getCommID())) {
            return;
        }
        server.actionFailed(action.getCommID(),
                "You are not allowed to activate Room, the game has not started yet!");
    }

    @Override
    public void actOn(final EndTurnAction action) {
        if (!server.getAllCommIDs().containsKey(action.getCommID())) {
            return;
        }
        server.actionFailed(action.getCommID(), "cannot do end turn!");
    }

    @Override
    public void actOn(final HireMonsterAction action) {
        if (!server.getAllCommIDs().containsKey(action.getCommID())) {
            return;
        }
        server.actionFailed(action.getCommID(), "cannot hire a monster!");
    }

    @Override
    public void actOn(final MonsterAction action) {
        if (!server.getAllCommIDs().containsKey(action.getCommID())) {
            return;
        }
        server.actionFailed(action.getCommID(), "cannot place a monster!");
    }

    @Override
    public void actOn(final PlaceBidAction action) {
        if (!server.getAllCommIDs().containsKey(action.getCommID())) {
            return;
        }
        server.actionFailed(action.getCommID(), "cannot place bid!");
    }

    @Override
    public void actOn(final BattleGroundAction action) {
        if (!server.getAllCommIDs().containsKey(action.getCommID())) {
            return;
        }
        server.actionFailed(action.getCommID(), "cannot set battle ground!");
    }

    @Override
    public void actOn(final DigTunnelAction action) {
        if (!server.getAllCommIDs().containsKey(action.getCommID())) {
            return;
        }
        server.actionFailed(action.getCommID(), "cannot dig tunnel!");
    }

    @Override
    public void actOn(final TrapAction action) {
        if (!server.getAllCommIDs().containsKey(action.getCommID())) {
            return;
        }
        server.actionFailed(action.getCommID(), "cannot set Trap!");
    }

    @Override
    public void actOn(final BuildRoomAction action) {
        if (!server.getAllCommIDs().containsKey(action.getCommID())) {
            return;
        }
        server.actionFailed(action.getCommID(), "cannot build a room!");
    }

    @Override
    public void actOn(final MonsterTargetedAction action) {
        if (!server.getAllCommIDs().containsKey(action.getCommID())) {
            return;
        }
        server.actionFailed(action.getCommID(), "cannot place monster targeted!");
    }

}

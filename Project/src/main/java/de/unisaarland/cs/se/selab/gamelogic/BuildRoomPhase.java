package de.unisaarland.cs.se.selab.gamelogic;

import de.unisaarland.cs.se.selab.Server;
import de.unisaarland.cs.se.selab.actionpackage.Actions;
import de.unisaarland.cs.se.selab.actionpackage.BuildRoomAction;
import de.unisaarland.cs.se.selab.actionpackage.EndTurnAction;
import de.unisaarland.cs.se.selab.actionpackage.LeaveAction;
import de.unisaarland.cs.se.selab.datapackage.BidOption;
import de.unisaarland.cs.se.selab.datapackage.DungeonLord;
import de.unisaarland.cs.se.selab.datapackage.GameDataClass;
import de.unisaarland.cs.se.selab.datapackage.Resources;
import de.unisaarland.cs.se.selab.datapackage.Room;

public class BuildRoomPhase extends State {

    private int currentPlayerID;
    private boolean currentPlayerActs;
    private boolean expectedAction;

    final BidOption bo;

    public BuildRoomPhase(final Server server, final BidOption bo) {
        super(server);
        this.bo = bo;
    }

    @Override
    public void run() {
        final GameDataClass gd = server.getGameDataClass();
        int index = 0;
        for (final int id : bo.getAllDungeonLords()) {
            if (!gd.isDungeonLordAvailable(id)) {
                index++;
                continue;
            }
            currentPlayerID = id;
            //third slot special case
            if (index == 2 && gd.getAllRooms().isEmpty()) {
                return;
            }
            final DungeonLord dl = gd.getDungeonLord(currentPlayerID);
            final Resources res = dl.getResources();
            final int costCoins = bo.getCosts(index).getCoins();
            if (!canPayRoom(res, costCoins)) {
                index++;
                continue;
            }
            server.placeRoom(server.getCommIDByPlayerID(id));
            final int currentCommID = server.getCommIDByPlayerID(id);
            server.actNow(currentCommID);

            while (!(currentPlayerActs && expectedAction) && !server.getTerminate()) {
                final Actions action = server.getNextAction();
                nextAction(action, currentCommID);
                sendActNow(currentCommID);
            }
            currentPlayerActs = false;
            expectedAction = false;
            index++;
        }
    }

    private boolean canPayRoom(final Resources res, final int costCoins) {
        if (res.subtractCoins(costCoins)) {
            if ((costCoins != 0)) {
                server.goldChangedBroadcast(-costCoins, currentPlayerID);
            }
            return true;
        } else {
            return false;
        }
    }

    private void nextAction(final Actions action, final int currentCommID) {
        if (action == null) {
            currentPlayerActs = true;
            expectedAction = true;
            playerLeave(currentCommID);
        } else {
            action.accept(this);
            if (action.getCommID() == currentCommID) {
                currentPlayerActs = true;
            } else {
                currentPlayerActs = false;
            }
        }
    }

    private void sendActNow(final int currentCommID) {
        if (currentPlayerActs && !expectedAction) {
            server.actNow(currentCommID);
        }
    }

    @Override
    public void actOn(final BuildRoomAction action) {
        final int commID = action.getCommID();
        if ((server.getPlayerIDByCommID(commID) != currentPlayerID)) {
            server.actionFailed(commID, "this " + commID + " is not allowed to dig a tunnel");
            return;
        }
        final GameDataClass gd = server.getGameDataClass();
        final int roomID = action.getRoomID();
        if (!gd.isRoomAvailable(roomID)) {
            server.actionFailed(commID, "room is not available");
            return;
        }
        final Room room = gd.getRoom(roomID);
        final DungeonLord dl = gd.getDungeonLord(currentPlayerID);
        final int x = action.getX();
        final int y = action.getY();
        if (!dl.getDungeon().buildRoom(x, y, room)) {
            server.actionFailed(commID,
                    "you are not allowed to build this room in coordinate x = " + x + " and y = "
                            + y);
            return;
        }
        expectedAction = true;
        dl.addRoom(room);
        gd.removeRoom(roomID);
        server.roomBuiltBroadcast(currentPlayerID, roomID, x, y);

    }

    @Override
    public void actOn(final EndTurnAction action) {
        final int commID = action.getCommID();
        if ((server.getPlayerIDByCommID(commID) != currentPlayerID)) {
            server.actionFailed(commID, "this " + commID + " is not allowed to end turn");
            return;
        }
        expectedAction = true;
    }

    @Override
    public void actOn(final LeaveAction action) {
        final int commID = action.getCommID();
        if (server.getPlayerIDByCommID(commID) == currentPlayerID) {
            expectedAction = true;
        }
        playerLeave(commID);
    }
}

package de.unisaarland.cs.se.selab.gamelogic;

import de.unisaarland.cs.se.selab.Server;
import de.unisaarland.cs.se.selab.actionpackage.Actions;
import de.unisaarland.cs.se.selab.actionpackage.DigTunnelAction;
import de.unisaarland.cs.se.selab.actionpackage.EndTurnAction;
import de.unisaarland.cs.se.selab.actionpackage.LeaveAction;
import de.unisaarland.cs.se.selab.datapackage.BidOption;
import de.unisaarland.cs.se.selab.datapackage.Dungeon;
import de.unisaarland.cs.se.selab.datapackage.DungeonLord;
import de.unisaarland.cs.se.selab.datapackage.GameDataClass;
import de.unisaarland.cs.se.selab.datapackage.Resources;

public class DigTunnelPhase extends State {

    private int currentPlayerID;
    private boolean currentPlayerActs;
    private int allAllowedTunnelDug;
    private int index;
    final BidOption bo;

    public DigTunnelPhase(final Server server, final BidOption bo) {
        super(server);
        this.bo = bo;
    }

    private void nextAction(final Actions action, final int currentCommID) {
        if (action == null) {
            currentPlayerActs = true;
            allAllowedTunnelDug = bo.getRewards(index).getImps();
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
        if (currentPlayerActs && allAllowedTunnelDug != bo.getRewards(index).getImps()) {
            server.actNow(currentCommID);
        }
    }

    @Override
    public void run() {
        final GameDataClass gd = server.getGameDataClass();
        for (final int id : bo.getAllDungeonLords()) {
            if (gd.isDungeonLordAvailable(id)) {
                final int currentCommID = server.getCommIDByPlayerID(id);
                server.digTunnel(currentCommID);
                server.actNow(currentCommID);
                currentPlayerID = id;
                while (!(currentPlayerActs
                        && allAllowedTunnelDug == bo.getRewards(index).getImps())
                        && !server.getTerminate()) {
                    final Actions action = server.getNextAction();
                    nextAction(action, currentCommID);
                    sendActNow(currentCommID);
                }
            }
            currentPlayerActs = false;
            allAllowedTunnelDug = 0;
            index++;
        }
    }

    @Override
    public void actOn(final DigTunnelAction action) {
        final int commID = action.getCommID();
        if (server.getPlayerIDByCommID(commID) != currentPlayerID) {
            server.actionFailed(commID, "this " + commID + " is not allowed to dig a tunnel");
            return;
        }
        final GameDataClass gd = server.getGameDataClass();
        final DungeonLord dl = gd.getDungeonLord(currentPlayerID);
        final Resources res = dl.getResources();
        final int availableImps = res.getAvailableImps();
        int costImps = 1;
        // third slot special case
        if (index == 2 && allAllowedTunnelDug == bo.getRewards(index).getImps() - 1) {
            costImps++;
        }
        //check whether we have enough imps
        if (availableImps < costImps) {
            if (costImps > 1) {
                server.actionFailed(server.getCommIDByPlayerID(currentPlayerID),
                        "insufficient amount of imps: you need 2 imps for a miner and supervisor");
            } else {
                server.actionFailed(server.getCommIDByPlayerID(currentPlayerID),
                        "insufficient amount of imps : tunnel cannot be dug ");
            }
            return;
        }
        final Dungeon dungeon = dl.getDungeon();
        final int x = action.getX();
        final int y = action.getY();
        //check if we can dig in the given coordinate
        if (!dungeon.digTunnel(x, y)) {
            server.actionFailed(commID,
                    "you cannot dig tunnel in coordinate x = " + x + " and y = " + y);
            return;
        }
        res.addBusyImpsMining(costImps);
        server.impsChangedBroadcast(-costImps, currentPlayerID);
        server.tunnelDugBroadcast(currentPlayerID, x, y);
        allAllowedTunnelDug++;
    }

    @Override
    public void actOn(final EndTurnAction action) {
        final int commID = action.getCommID();
        if (server.getPlayerIDByCommID(commID) != currentPlayerID) {
            server.actionFailed(commID, "this " + commID + " is not allowed to end turn");
            return;
        }
        allAllowedTunnelDug = bo.getRewards(index).getImps();
    }

    @Override
    public void actOn(final LeaveAction action) {
        final int commID = action.getCommID();
        if (server.getPlayerIDByCommID(commID) == currentPlayerID) {
            allAllowedTunnelDug = bo.getRewards(index).getImps();
        }
        playerLeave(commID);
    }
}


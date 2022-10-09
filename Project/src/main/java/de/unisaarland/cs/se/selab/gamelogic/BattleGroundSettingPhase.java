package de.unisaarland.cs.se.selab.gamelogic;

import de.unisaarland.cs.se.selab.Server;
import de.unisaarland.cs.se.selab.actionpackage.Actions;
import de.unisaarland.cs.se.selab.actionpackage.ActivateRoomAction;
import de.unisaarland.cs.se.selab.actionpackage.BattleGroundAction;
import de.unisaarland.cs.se.selab.actionpackage.LeaveAction;
import de.unisaarland.cs.se.selab.datapackage.Dungeon;
import de.unisaarland.cs.se.selab.datapackage.DungeonLord;
import de.unisaarland.cs.se.selab.datapackage.GameDataClass;
import java.util.ArrayList;
import java.util.List;

public class BattleGroundSettingPhase extends State {

    /**
     * the commandID of the current player
     */
    private final int currentCommID;
    /**
     * list of leaved playerIDs
     */
    private final List<Integer> leavePlayers = new ArrayList<>();
    /**
     * Shows whether the expected action has been received
     */
    private boolean expectedActOn;

    public BattleGroundSettingPhase(final Server server, final int commID) {
        super(server);
        this.currentCommID = commID;
    }

    /**
     * Start BattleGroundSettingPhase
     */
    @Override
    public void run() {
        this.server.actNow(this.currentCommID);
        while (!this.expectedActOn) {
            final Actions a = this.server.getNextAction();
            a.accept(this);
            if (this.currentCommID == a.getCommID() && !this.expectedActOn) {
                this.server.actNow(this.currentCommID);
            }
        }
    }

    /**
     * receive BattleGroundAction
     * check if the location is available to set BattleGround
     */
    @Override
    public void actOn(final BattleGroundAction ac) {
        if (ac.getCommID() == this.currentCommID) {
            final int playerId = this.server.getPlayerIDByCommID(ac.getCommID());
            final GameDataClass gd = this.server.getGameDataClass();
            final DungeonLord dgl = gd.getAllDungeonLords().get(playerId);
            final Dungeon dg = dgl.getDungeon();
            if (dg.checkSetBattleGround(ac.getX(), ac.getY())) {
                this.server.battleGroundSetBroadcast(
                        this.server.getPlayerIDByCommID(ac.getCommID()), ac.getX(), ac.getY());
                this.expectedActOn = true;
            } else {
                this.server.actionFailed(ac.getCommID(),
                        "can not set Battleground at this position");
            }
        } else {
            this.server.actionFailed(ac.getCommID(), "wrong player is acting");
        }
    }

    /**
     * receive LeaveAction
     * add the playerID who left to in the leavePlayers list
     */
    @Override
    public void actOn(final LeaveAction ac) {
        final int commID = ac.getCommID();
        this.leavePlayers.add(this.server.getPlayerIDByCommID(commID));
        if (commID == this.currentCommID) {
            playerLeave(commID);
            this.expectedActOn = true;
        } else {
            playerLeave(commID);
        }
    }

    /**
     * receive ActivateRoomAction
     * return actionFailed
     */
    @Override
    public void actOn(final ActivateRoomAction ac) {
        this.server.actionFailed(ac.getCommID(),
                "it is not allowed to activate a Room in battleGroundSetting phase");
    }

    /**
     *
     * @return list of leave players
     */
    public List<Integer> getLeavePlayersThisBattleSetTurn() {
        return leavePlayers;
    }
}

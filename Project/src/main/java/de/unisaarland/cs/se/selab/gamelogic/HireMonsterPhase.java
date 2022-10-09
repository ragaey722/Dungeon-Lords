package de.unisaarland.cs.se.selab.gamelogic;

import de.unisaarland.cs.se.selab.Server;
import de.unisaarland.cs.se.selab.actionpackage.Actions;
import de.unisaarland.cs.se.selab.actionpackage.EndTurnAction;
import de.unisaarland.cs.se.selab.actionpackage.HireMonsterAction;
import de.unisaarland.cs.se.selab.actionpackage.LeaveAction;
import de.unisaarland.cs.se.selab.datapackage.BidOption;
import de.unisaarland.cs.se.selab.datapackage.DungeonLord;
import de.unisaarland.cs.se.selab.datapackage.GameDataClass;
import de.unisaarland.cs.se.selab.datapackage.Monster;
import de.unisaarland.cs.se.selab.datapackage.Resources;

public class HireMonsterPhase extends State {

    private int currentPlayerID;
    private boolean currentPlayerActs;
    private boolean expectedAction;
    final BidOption bo;

    public HireMonsterPhase(final Server server, final BidOption bo) {
        super(server);
        this.bo = bo;
    }

    private boolean checkThirdSlot(final int index) {
        final GameDataClass gd = server.getGameDataClass();
        final DungeonLord dl = gd.getDungeonLord(currentPlayerID);
        final Resources res = dl.getResources();
        final int additionalFood = bo.getCosts(2).getFoods();
        if (index == 2) {
            if (res.subtractFood(additionalFood)) {
                server.foodChangedBroadcast(-additionalFood, currentPlayerID);
                return true;
            } else {
                return false;
            }
        }
        return true;
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
    public void run() {
        final GameDataClass gd = server.getGameDataClass();
        int index = 0;
        for (final int id : bo.getAllDungeonLords()) {
            if (gd.isDungeonLordAvailable(id)) {
                currentPlayerID = id;
                //third slot special case
                if (!checkThirdSlot(index)) {
                    return;
                }
                final int currentCommID = server.getCommIDByPlayerID(id);
                server.selectMonster(currentCommID);
                server.actNow(currentCommID);
                while (!(currentPlayerActs && expectedAction) && !server.getTerminate()) {
                    final Actions action = server.getNextAction();
                    nextAction(action, currentCommID);
                    sendActNow(currentCommID);
                }
            }
            currentPlayerActs = false;
            expectedAction = false;
            index++;
        }
    }

    @Override
    public void actOn(final HireMonsterAction action) {
        final int commID = action.getCommID();
        if (server.getPlayerIDByCommID(commID) != currentPlayerID) {
            server.actionFailed(commID, "this " + commID + " is not allowed to dig a tunnel");
            return;
        }
        final int monsterID = action.getMonster();
        final GameDataClass gd = server.getGameDataClass();

        if (!gd.isMonsterAvailable(monsterID)) {
            server.actionFailed(commID, "monster is not available");
            return;
        }

        final DungeonLord dl = gd.getDungeonLord(currentPlayerID);
        final Resources res = dl.getResources();
        final Monster monster = gd.getMonster(monsterID);
        final int availableFood = res.getFoods();
        final int availableEvilness = res.getEvilness();
        final int costFood = monster.hunger();
        final int costEvilness = monster.evilness();
        if (availableFood < costFood || availableEvilness + costEvilness > 15) {
            server.actionFailed(commID,
                    "insufficient amount of food or insufficient amount of niceness");
            return;
        }

        res.upEvilness(costEvilness);
        res.subtractFood(costFood);
        dl.addMonster(monster);
        gd.removeMonster(monsterID);
        if (costFood != 0) {
            server.foodChangedBroadcast(-costFood, currentPlayerID);
        }
        if (costEvilness != 0) {
            server.evilnessChangedBroadcast(costEvilness, currentPlayerID);
        }
        server.monsterHiredBroadcast(monsterID, currentPlayerID);
        expectedAction = true;
    }

    @Override
    public void actOn(final EndTurnAction action) {
        final int commID = action.getCommID();
        if (server.getPlayerIDByCommID(commID) != currentPlayerID) {
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


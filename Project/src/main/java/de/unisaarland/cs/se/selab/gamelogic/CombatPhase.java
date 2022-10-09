package de.unisaarland.cs.se.selab.gamelogic;

import de.unisaarland.cs.se.selab.Server;
import de.unisaarland.cs.se.selab.actionpackage.Actions;
import de.unisaarland.cs.se.selab.actionpackage.ActivateRoomAction;
import de.unisaarland.cs.se.selab.actionpackage.EndTurnAction;
import de.unisaarland.cs.se.selab.actionpackage.LeaveAction;
import de.unisaarland.cs.se.selab.actionpackage.MonsterAction;
import de.unisaarland.cs.se.selab.actionpackage.MonsterTargetedAction;
import de.unisaarland.cs.se.selab.actionpackage.TrapAction;
import de.unisaarland.cs.se.selab.datapackage.Adventurer;
import de.unisaarland.cs.se.selab.datapackage.Dungeon;
import de.unisaarland.cs.se.selab.datapackage.DungeonLord;
import de.unisaarland.cs.se.selab.datapackage.GameDataClass;
import de.unisaarland.cs.se.selab.datapackage.Monster;
import de.unisaarland.cs.se.selab.datapackage.Trap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CombatPhase extends State {

    /**
     * the commandID of the current player doing combat
     */
    private int currentCommandID;
    /**
     * number of monsters that can still be placed in this round
     */
    private int numberMonsters;
    /**
     * number of traps that can still be placed in this round
     */
    private int numberTraps;
    /**
     * list of monsters already be placed in this round
     */
    private final List<Monster> monster = new ArrayList<>();
    /**
     * list of traps already be placed in this round
     */
    private final List<Trap> trap = new ArrayList<>();
    /**
     * show if the player already placed all the defense and sent an end turn or left the game
     */
    private boolean defenseEnd;
    /**
     * current round
     */
    private int round;
    /**
     * list of available playerIDs, help to record and handle players who left during combat phase
     */
    private final List<Integer> listCopy = new ArrayList<>();
    /**
     * a mapping from monsterID to target position
     * that saves the targets of already placed monsters
     */
    private final Map<Integer, Integer> monsterTarget = new HashMap<>();

    public CombatPhase(final Server server) {
        super(server);
        this.numberMonsters = 0;
        this.numberTraps = 0;
        this.round = 0;
    }

    /**
     * Start COMBAT PHASE
     * the outermost LOOP to go to the next player and reset
     */
    @Override
    public void run() {
        final GameDataClass gd = this.server.getGameDataClass();
        setListCopy();
        for (final Integer playerID : this.listCopy) {
            if (playerID != null) {
                // player exist
                this.currentCommandID = this.server.getCommIDByPlayerID(playerID);
                do4rounds(playerID, gd);
            }
            // reset everything before going to the next player combat
            resetEverything(playerID, gd);
        }
        // next year or end game
        nextYear(gd);
    }

    /**
     * a loop for 4 rounds of one player,
     * one round:
     * checkDungeon -> setBattleGround -> setDefense -> executeDefense
     *
     * @param playerID current playerID
     * @param gd       the GameDataClass from the server
     */
    private void do4rounds(final Integer playerID, final GameDataClass gd) {
        while (round < 4) {
            // do one round //
            round++;
            this.server.nextRoundBroadcast(this.round);
            // if no unconquered tiles
            if (noTileLeft()) {
                // check dungeon if we can release an adventurer, if so send event
                checkDungeon();
            } else {
                // set battleground
                this.server.battleGround(this.currentCommandID);
                setBattleground();
                // if current player leave in battleground setting
                if (isLeave(playerID)) {
                    break;
                }
                // defense
                this.server.defendYourself(this.currentCommandID);
                final DungeonLord dl = gd.getDungeonLord(playerID);
                final Dungeon d = dl.getDungeon();
                setDefense(d);
                this.server.actNow(this.currentCommandID);
                putAction(playerID);
                // execute
                if (execute(playerID)) {
                    break;
                }

            } // one round end
        }
    }

    /**
     * new a BattleGroundSettingPhase class, and run it
     */
    private void setBattleground() {
        final BattleGroundSettingPhase bsp = new BattleGroundSettingPhase(this.server,
                this.currentCommandID);
        bsp.run();
        setLeave(bsp.getLeavePlayersThisBattleSetTurn());
    }

    /**
     * check if the BattleGround is a room
     * to set limit on the number of monsters and traps
     */
    private void setDefense(final Dungeon d) {
        if (d.isBattleGroundRoom()) {
            // room: 2 monster , 1 trap
            this.numberMonsters = 2;
        } else {
            // normal tunnel: 1 monster, 1 trap
            this.numberMonsters = 1;
        }
        this.numberTraps = 1;
    }

    /**
     * receive an action and handle it if it is null which means
     * a timeout otherwise delegate it to helper method
     */
    private void putAction(final Integer playerID) {
        while (!this.defenseEnd) {
            final Actions a = this.server.getNextAction();
            if (a == null) {
                //timeout
                playerLeave(this.currentCommandID);
                for (int i = 0; i < this.listCopy.size(); i++) {
                    if (playerID.equals(this.listCopy.get(i))) {
                        this.listCopy.set(i, null);
                    }
                }
                break;
            } else {
                actOnAction(a);
            }
        }
        this.defenseEnd = false;
    }

    /**
     * receive actions, and send actNow if the action was sent
     * by the current player until the player send an endTurn
     */
    private void actOnAction(final Actions a) {
        a.accept(this);
        if (a.getCommID() == this.currentCommandID && !this.defenseEnd) {
            this.server.actNow(this.currentCommandID);
        }
    }

    /**
     * this method call helper methods that execute damage and heal
     *
     * @return true in case all the adventurers are defeated or the player left
     * and the combat phase should be finished for the current player
     */
    private boolean execute(final Integer playerID) {
        if (isLeave(playerID)) {
            // current player leave in combat
            return true;
        }

        boolean isBreak = false;
        if (!trap.isEmpty()) {
            trapDamageExecute();
        }
        if (noAdvLeft()) {
            isBreak = true;
        }
        if (!monster.isEmpty()) {
            monsterDamageExecute();
        }
        if (noAdvLeft()) {
            isBreak = true;
        }
        fatigueDamageExecute();
        if (noAdvLeft()) {
            isBreak = true;
        }
        if (!isBreak) {
            conquerExecute();
        }
        healExecute();

        // reset traps, monsters and go next round
        this.numberTraps = 0;
        this.numberMonsters = 0;
        this.trap.clear();
        this.monster.clear();
        return isBreak;
    }

    /**
     * reset and clear all the data in the combat, which is stored for one player
     */
    private void resetEverything(final Integer playerID, final GameDataClass gd) {
        if (playerID != null) {
            if (!isLeave(playerID)) {
                final DungeonLord dl = gd.getDungeonLord(playerID);
                dl.monstersAreRested();
                dl.getDungeon().resetAdventurersQueue();
            }
            this.monster.clear();
            this.trap.clear();
            this.round = 0;
            this.numberTraps = 0;
            this.numberMonsters = 0;
        }
    }

    /**
     * go to next year and update state
     * if it's the last year, set Terminate true in server
     */
    private void nextYear(final GameDataClass gd) {
        if (gd.getYear() < this.server.getMaxYears()) {
            this.server.updateCurrentState(new BiddingPhase(this.server));
            final int year = this.server.getGameDataClass().getYear() + 1;
            this.server.getGameDataClass().setYear(year);
            this.server.nextYearBroadcast(year);
            //reset locked bids from slot 2 and 3 of the last 4th seasons
            resetBidLock();
        } else {
            this.server.setTerminate(true);
            final int year = this.server.getGameDataClass().getYear() + 1;
            this.server.getGameDataClass().setYear(year);
        }

    }

    /**
     * store the monster which was placed by current player
     */
    private void useMonster(final DungeonLord dl, final int monsterId) {
        this.monster.add(dl.useMonster(monsterId));
        this.server.monsterPlacedBroadcast(monsterId,
                this.server.getPlayerIDByCommID(this.currentCommandID));
        this.numberMonsters--;
    }

    /**
     * receive endTurnAction, set defenseEnd
     */
    @Override
    public void actOn(final EndTurnAction ac) {
        if (this.currentCommandID == ac.getCommID()) {
            this.defenseEnd = true;
        } else {
            server.actionFailed(ac.getCommID(), "its not your turn to end");
        }
    }

    /**
     * receive MonsterTargetedAction
     * Check if it can be placed, if so save the target
     */
    @Override
    public void actOn(final MonsterTargetedAction ac) {
        if (this.currentCommandID == ac.getCommID()) {
            if (this.numberMonsters > 0) {
                final DungeonLord dl = this.server.getGameDataClass()
                        .getDungeonLord(this.server.getPlayerIDByCommID(ac.getCommID()));
                if (dl.canUseMonsterTargeted(ac.getMonster(), ac.getPosition())) {
                    this.monsterTarget.put(ac.getMonster(), ac.getPosition());
                    useMonster(dl, ac.getMonster());
                } else {
                    this.server.actionFailed(ac.getCommID(), "can not use monster");
                }
            } else {
                this.server.actionFailed(ac.getCommID(), "can not set monster anymore");
            }
        } else {
            this.server.actionFailed(ac.getCommID(), "can not act now");
        }
    }

    /**
     * receive MonsterAction
     * check if it can be placed
     */
    @Override
    public void actOn(final MonsterAction ac) {
        if (this.currentCommandID == ac.getCommID()) {
            if (this.numberMonsters > 0) {
                final DungeonLord dl = this.server.getGameDataClass()
                        .getDungeonLord(this.server.getPlayerIDByCommID(ac.getCommID()));
                if (dl.canUseMonster(ac.getMonster())) {
                    useMonster(dl, ac.getMonster());
                } else {
                    this.server.actionFailed(ac.getCommID(), "can not use monster");
                }
            } else {
                this.server.actionFailed(ac.getCommID(), "can not set monster anymore");
            }
        } else {
            this.server.actionFailed(ac.getCommID(), "can not act now");
        }
    }

    /**
     * receive TrapAction
     * check if it can be placed
     */
    @Override
    public void actOn(final TrapAction ac) {
        if (this.currentCommandID == ac.getCommID()) {
            if (this.numberTraps > 0) {
                final DungeonLord dl = this.server.getGameDataClass()
                        .getDungeonLord(this.server.getPlayerIDByCommID(ac.getCommID()));
                useTrap(dl, ac.getCommID(), ac.getTrapID());
            } else {
                this.server.actionFailed(ac.getCommID(), "can not set trap anymore");
            }
        } else {
            this.server.actionFailed(ac.getCommID(), "can not act now");
        }
    }

    /**
     * receive ActivateRoomAction
     * return actionFailed
     */
    @Override
    public void actOn(final ActivateRoomAction ac) {
        this.server.actionFailed(ac.getCommID(),
                "it is not allowed to activate a Room in combat phase");
    }

    /**
     * receive LeaveAction
     * set the player who left to null in the player list
     * and call helper method playerLeave to remove the players data
     */
    @Override
    public void actOn(final LeaveAction ac) {
        final int commID = ac.getCommID();
        final Integer playerID = this.server.getPlayerIDByCommID(commID);
        for (int i = 0; i < this.listCopy.size(); i++) {
            if (playerID.equals(this.listCopy.get(i))) {
                this.listCopy.set(i, null);
            }
        }
        if (commID == this.currentCommandID) {
            this.defenseEnd = true;
            playerLeave(commID);
        } else {
            playerLeave(commID);
        }
    }

    /**
     * check if the trap can be used or not, if so store it
     * if the BattleGround is room, One gold coin deducted
     */
    private void useTrap(final DungeonLord dl, final int commId, final int trapId) {
        if (dl.canUseTrap(trapId)) {
            if (dl.getDungeon().isBattleGroundRoom()) {
                if (dl.getResources().getCoins() >= 1) {
                    dl.getResources().subtractCoins(1);
                    this.server.goldChangedBroadcast(-1,
                            this.server.getPlayerIDByCommID(commId));
                    addTrap(dl, trapId);
                } else {
                    this.server.actionFailed(commId, "has not coins");
                }
            } else {
                addTrap(dl, trapId);
            }
        } else {
            this.server.actionFailed(commId, "can not use trap");
        }
    }

    /**
     * store the trap which was placed by current player
     */
    private void addTrap(final DungeonLord dl, final int trapId) {
        this.trap.add(dl.useTrap(trapId));
        this.server.trapPlacedBroadcast(
                this.server.getPlayerIDByCommID(this.currentCommandID),
                trapId);
        this.numberTraps--;
    }

    /**
     * execute the tarp damage
     */
    private void trapDamageExecute() {
        final Trap t = trap.get(0);
        final int damage = t.damage();
        final List<Adventurer> adventurerQueue = getAdventurerQueue();
        int defuseSum = 0;
        for (final Adventurer a : adventurerQueue) {
            defuseSum += a.getDefuseValue();
        }
        switch (t.attackStrategy()) {
            case BASIC -> advTakeDamage(adventurerQueue.get(0), damage - defuseSum);
            case TARGETED -> {
                final int target = t.target();
                if (target <= adventurerQueue.size()) {
                    advTakeDamage(adventurerQueue.get(target - 1), damage - defuseSum);
                }
            }
            case MULTI -> {
                for (final Adventurer a : adventurerQueue) {
                    final int damageAfterDefuse = damage - defuseSum;
                    if (damageAfterDefuse <= 0) {
                        defuseSum = -damageAfterDefuse;
                    } else {
                        advTakeDamageMulti(a, damageAfterDefuse);
                        defuseSum = 0;
                    }
                }
                this.server.getGameDataClass()
                        .getDungeonLord(this.server.getPlayerIDByCommID(this.currentCommandID))
                        .getDungeon().captureAfterMulti();
            }
            default -> {
            }
        }
    }

    /**
     * execute the monster damage
     */
    private void monsterDamageExecute() {
        for (final Monster m : monster) {
            final int damage = m.damage();
            final List<Adventurer> adventurerQueue = getAdventurerQueue();
            switch (m.attackStrategy()) {
                case BASIC -> advTakeDamage(adventurerQueue.get(0), damage);
                case TARGETED -> {
                    final int target = monsterTarget.get(m.id());
                    advTakeDamage(adventurerQueue.get(target - 1), damage);
                }
                case MULTI -> {
                    for (final Adventurer a : adventurerQueue) {
                        advTakeDamageMulti(a, damage);
                    }
                    this.server.getGameDataClass()
                            .getDungeonLord(this.server.getPlayerIDByCommID(this.currentCommandID))
                            .getDungeon().captureAfterMulti();
                }
                default -> {
                }
            }
        }
    }

    /**
     * execute the fatigue damage
     */
    private void fatigueDamageExecute() {
        final List<Adventurer> adventurerQueue = getAdventurerQueue();
        for (final Adventurer a : adventurerQueue) {
            advTakeDamageMulti(a, 2);
        }
        this.server.getGameDataClass()
                .getDungeonLord(this.server.getPlayerIDByCommID(this.currentCommandID))
                .getDungeon().captureAfterMulti();
    }

    /**
     * attackStrategy is BASIC & TARGET:
     * let one adventurer takes damage
     */
    private void advTakeDamage(final Adventurer currentAdv, final int damage) {
        if (damage > 0) {
            final int actualDamage = currentAdv.takeDamage(damage);
            if (!captureAdv(currentAdv)) {
                this.server.adventurerDamagedBroadcast(currentAdv.getId(),
                        actualDamage);
            }
        }
    }

    /**
     * attackStrategy is MULTI:
     * let one adventurer takes damage
     */
    private void advTakeDamageMulti(final Adventurer currentAdv, final int damage) {
        if (damage > 0) {
            final int actualDamage = currentAdv.takeDamage(damage);
            if (!captureAdvMulti(currentAdv)) {
                this.server.adventurerDamagedBroadcast(currentAdv.getId(),
                        actualDamage);
            }
        }
    }

    /**
     * attackStrategy is BASIC & TARGET:
     * check Adventurer HP
     * capture if HP <= 0
     *
     * @return true, if the Adventurer be captured
     */
    private boolean captureAdv(final Adventurer adv) {
        if (adv.getHP() <= 0) {
            this.server.getGameDataClass()
                    .getDungeonLord(this.server.getPlayerIDByCommID(this.currentCommandID))
                    .getDungeon().captureAdventurer(adv);
            this.server.adventurerImprisonedBroadcast(adv.getId());
            return true;
        }
        return false;
    }

    /**
     * attackStrategy is MULTI:
     * check Adventurer HP
     * capture if HP <= 0
     *
     * @return true, if the Adventurer be captured
     */
    private boolean captureAdvMulti(final Adventurer adv) {
        if (adv.getHP() <= 0) {
            this.server.adventurerImprisonedBroadcast(adv.getId());
            return true;
        }
        return false;
    }

    /**
     * @return true, if the Adventurer list is empty
     */
    private boolean noAdvLeft() {
        final List<Adventurer> adventurers = getAdventurerQueue();
        return adventurers.isEmpty();
    }

    /**
     * execute conquer current tile
     */
    // execute conquer and downEvilness
    private void conquerExecute() {
        final Dungeon d = this.server.getGameDataClass()
                .getDungeonLord(this.server.getPlayerIDByCommID(this.currentCommandID))
                .getDungeon();
        d.conquerCurrentTile();
        this.server.tunnelConqueredBroadcast(d.getAdventurerQueue().get(0).getId(),
                d.getCurrentX(), d.getCurrentY());
        downEvilness1();
    }

    /**
     * execute heal
     */
    private void healExecute() {
        final List<Adventurer> adventurerQueue = getAdventurerQueue();
        for (final Adventurer a : adventurerQueue) {
            // if currentAdventurer is a priest
            int healValue = a.getHealValue();
            if (healValue != 0) {
                for (final Adventurer b : adventurerQueue) {
                    final int actualHeal = b.takeHeal(healValue);
                    if (actualHeal > 0) {
                        this.server.adventurerHealedBroadcast(actualHeal, a.getId(), b.getId());
                    }
                    healValue -= actualHeal;
                    if (healValue <= 0) {
                        break;
                    }
                }
            }
        }
    }

    /**
     * @return true, if current dungeonLord has no unconquered Tunnels and Rooms
     */
    private boolean noTileLeft() {
        final Dungeon d = this.server.getGameDataClass()
                .getDungeonLord(this.server.getPlayerIDByCommID(this.currentCommandID))
                .getDungeon();
        return (d.getActiveDungeonLength() == 0);
    }

    /**
     * check imprisoned adventurers, and release one imprisoned Adventurer
     */
    private void checkDungeon() {
        final Dungeon d = this.server.getGameDataClass()
                .getDungeonLord(this.server.getPlayerIDByCommID(this.currentCommandID))
                .getDungeon();
        if (d.isThereCapturedAdventurers()) {
            final int adv = d.releaseAdventurer();
            this.server.adventurerFledBroadcast(adv);
            downEvilness1();
        }
    }

    /**
     * reduces one evilness of current dungeonLord
     */
    private void downEvilness1() {
        final DungeonLord dl = this.server.getGameDataClass()
                .getDungeonLord(this.server.getPlayerIDByCommID(this.currentCommandID));
        if (dl.getResources().downEvilness(1)) {
            this.server.evilnessChangedBroadcast(-1,
                    this.server.getPlayerIDByCommID(currentCommandID));
        }
    }

    /**
     * copy the playerIDs from server
     */
    private void setListCopy() {
        final List<Integer> playerIDS = this.server.getAllPlayerIDs();
        this.listCopy.addAll(playerIDS);
    }

    /**
     * handle the players who leaved in BattleGroundSettingPhase
     */
    private void setLeave(final List<Integer> leaver) {
        for (final Integer integer : leaver) {
            final List<Integer> playerIDsCopy = this.listCopy;
            for (int j = 0; j < playerIDsCopy.size(); j++) {
                if (Objects.equals(integer, playerIDsCopy.get(j))) {
                    playerIDsCopy.set(j, null);
                }
            }
        }
    }

    /**
     * @return ture, if the player already leaved
     */
    private boolean isLeave(final int playerID) {
        return !listCopy.contains(playerID);
    }

    /**
     * fetch the AdventurerQueue in current dungeonLord's Dungeon through server
     */
    private List<Adventurer> getAdventurerQueue() {
        final int currentPlayerID = this.server.getPlayerIDByCommID(currentCommandID);
        return this.server.getGameDataClass().getDungeonLord(currentPlayerID).getDungeon()
                .getAdventurerQueue();
    }

    /**
     * reset the BidLock at the end of a year
     */
    private void resetBidLock() {
        final List<Integer> dls = this.server.getAllPlayerIDs();
        for (final Integer integer : dls) {
            final DungeonLord dl = this.server.getGameDataClass().getAllDungeonLords().get(integer);
            this.server.bidRetrievedBroadcast(dl.getLockedBiddings().get(0), dl.getPlayerID());
            this.server.bidRetrievedBroadcast(dl.getLockedBiddings().get(1), dl.getPlayerID());
            dl.resetLockedBiddingsList();
        }
    }

}

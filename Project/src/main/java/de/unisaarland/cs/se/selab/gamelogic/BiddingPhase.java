package de.unisaarland.cs.se.selab.gamelogic;

import de.unisaarland.cs.se.selab.Server;
import de.unisaarland.cs.se.selab.actionpackage.Actions;
import de.unisaarland.cs.se.selab.actionpackage.LeaveAction;
import de.unisaarland.cs.se.selab.actionpackage.PlaceBidAction;
import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.datapackage.Adventurer;
import de.unisaarland.cs.se.selab.datapackage.DungeonLord;
import de.unisaarland.cs.se.selab.datapackage.GameDataClass;
import de.unisaarland.cs.se.selab.datapackage.Monster;
import de.unisaarland.cs.se.selab.datapackage.Room;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BiddingPhase extends State {

    private int numberOfBids;

    public BiddingPhase(final Server server) {
        super(server);
        this.numberOfBids = 0;
    }

    @Override
    public void run() {
        final GameDataClass gd = server.getGameDataClass();
        final int currentSeason = gd.getSeason();
        server.nextRoundBroadcast(currentSeason);
        if (currentSeason != 4) {
            drawNewAdventurer(gd);
        }
        drawNewMonster(gd);
        drawNewRoom(gd);
        server.biddingStartedBroadcast();
        server.actNowBroadCast();
        while ((numberOfBids < 3 * server.getNumberOfPlayers()) && !server.getTerminate()) {
            placeBids(gd);
        }
        server.updateCurrentState(new EvaluationPhase(server));
    }

    private void placeBids(final GameDataClass gd) {
        final Actions action = server.getNextAction();
        if (action == null) {
            final List<DungeonLord> timeOutDungeonLords = new ArrayList<>();
            for (final int id : server.getAllPlayerIDs()) {
                final DungeonLord dl = gd.getDungeonLord(id);
                if (dl.getAlreadyBid() < 3) {
                    timeOutDungeonLords.add(dl);
                }
            }
            if (timeOutDungeonLords.size() == server.getNumberOfPlayers()) {
                server.setIsRegistrationPhase(true);
            }
            for (final DungeonLord dl : timeOutDungeonLords) {
                numberOfBids = numberOfBids - dl.getAlreadyBid();
                playerLeave(server.getCommIDByPlayerID(dl.getPlayerID()));
            }
            return;
        }
        action.accept(this);
        final int commID = action.getCommID();
        if (!server.getAllCommIDs().containsKey(commID)) {
            return;
        }
        final DungeonLord dl = server.getGameDataClass()
                .getDungeonLord(server.getPlayerIDByCommID(commID));
        if (dl.getAlreadyBid() < 3) {
            server.actNow(commID);
        }
    }


    @Override
    public void actOn(final PlaceBidAction action) {
        final int commID = action.getCommID();
        final int bidNumber = action.getNumber();
        if (bidNumber < 1 || bidNumber > 3) {
            server.actionFailed(commID, "slot number must be between 1 and 3");
            return;
        }
        final GameDataClass gd = server.getGameDataClass();
        final int playerID = server.getPlayerIDByCommID(commID);
        final DungeonLord dl = gd.getDungeonLord(playerID);
        final int alreadyBid = dl.getAlreadyBid();
        if (alreadyBid == 3) {
            server.actionFailed(commID, "already bid 3 times!");
            return;
        }
        final BidType bid = action.getBid();
        if (dl.getLockedBiddings().contains(bid)) {
            server.actionFailed(commID, "this option is locked");
            return;
        }
        final Map<Integer, BidType> biddings = dl.getBiddings();
        if (biddings.containsKey(bidNumber)) {
            server.actionFailed(commID, "you have already bid in this slot!");
            return;
        }
        if (biddings.containsValue(bid)) {
            server.actionFailed(commID, "you have already chosen this option!");
            return;
        }
        dl.addBidding(bid, bidNumber);
        server.bidPlacedBroadcast(bid, playerID, bidNumber);
        numberOfBids++;
    }

    @Override
    public void actOn(final LeaveAction action) {
        final int commID = action.getCommID();
        final DungeonLord dl = server.getGameDataClass()
                .getDungeonLord(server.getPlayerIDByCommID(commID));
        numberOfBids = numberOfBids - dl.getAlreadyBid();
        playerLeave(commID);
    }

    private void drawNewAdventurer(final GameDataClass gd) {
        final List<Adventurer> adventurers = gd.getAdventurerPool();
        for (int i = 0; i < server.getNumberOfPlayers(); i++) {
            final Adventurer adventurer = adventurers.get(0);
            final int id = adventurer.getId();
            gd.addAdventurer(id, adventurer);
            server.adventurerDrawnBroadcast(id);
            adventurers.remove(0);
        }
    }

    private void drawNewMonster(final GameDataClass gd) {
        final List<Monster> monsters = gd.getMonsterPool();
        for (int i = 0; i < 3; i++) {
            final Monster monster = monsters.get(0);
            final int id = monster.id();
            gd.addMonster(id, monster);
            server.monsterDrawnBroadcast(id);
            monsters.remove(0);
        }
    }

    private void drawNewRoom(final GameDataClass gd) {
        final List<Room> rooms = gd.getRoomPool();
        for (int i = 0; i < 2; i++) {
            final Room room = rooms.get(0);
            final int id = room.getId();
            gd.addRoom(id, room);
            server.roomDrawnBroadcast(id);
            rooms.remove(0);
        }
    }
}

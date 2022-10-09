package de.unisaarland.cs.se.selab.gamelogic;


import de.unisaarland.cs.se.selab.Server;
import de.unisaarland.cs.se.selab.comm.BidType;
import de.unisaarland.cs.se.selab.datapackage.Adventurer;
import de.unisaarland.cs.se.selab.datapackage.BidOption;
import de.unisaarland.cs.se.selab.datapackage.BiddingSquare;
import de.unisaarland.cs.se.selab.datapackage.Dungeon;
import de.unisaarland.cs.se.selab.datapackage.DungeonLord;
import de.unisaarland.cs.se.selab.datapackage.GameDataClass;
import de.unisaarland.cs.se.selab.datapackage.Monster;
import de.unisaarland.cs.se.selab.datapackage.Resources;
import de.unisaarland.cs.se.selab.datapackage.Room;
import de.unisaarland.cs.se.selab.datapackage.Trap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.LoggerFactory;


public class EvaluationPhase extends State {


    public EvaluationPhase(final Server server) {
        super(server);
    }

    @Override
    public void run() {
        final GameDataClass gd = server.getGameDataClass();
        final BiddingSquare bs = gd.getBiddingSquare();
        //place bid in all options
        placeBidInAllOptions(gd);
        //evaluating every options
        evaluateDefault(bs.getOption(BidType.FOOD));
        evaluateDefault(bs.getOption(BidType.NICENESS));
        evaluateTunnel(bs.getOption(BidType.TUNNEL));
        if (server.getTerminate()) {
            return;
        }
        evaluateCoin(bs.getOption(BidType.GOLD));
        evaluateDefault(bs.getOption(BidType.IMPS));
        evaluateTrap(bs.getOption(BidType.TRAP));
        evaluateMonster(bs.getOption(BidType.MONSTER));
        if (server.getTerminate()) {
            return;
        }
        evaluateRoom(bs.getOption(BidType.ROOM));
        if (server.getTerminate()) {
            return;
        }
        //preparing for next season
        final Map<Integer, DungeonLord> dungeonLords = gd.getAllDungeonLords();
        changeOrderPlayer(bs);
        resetDungeonLords(dungeonLords);
        returnImpsAndCoins(bs.getOption(BidType.GOLD), bs.getOption(BidType.TUNNEL));
        resetOptions(bs.getAllOptions());
        giveRewardFromRoom(dungeonLords);
        nextSeason(gd);
    }

    private void placeBidInAllOptions(final GameDataClass gd) {
        final BiddingSquare bs = gd.getBiddingSquare();
        final List<Integer> dungeonLordIds = bs.getAllDungeonLords();

        for (final Iterator<Integer> it = dungeonLordIds.iterator(); it.hasNext(); ) {
            final int dungeonLordID = it.next();
            if (!gd.isDungeonLordAvailable(dungeonLordID)) {
                it.remove();
            }
        }
        for (int i = 1; i < 4; i++) {
            for (final int id : dungeonLordIds) {
                final DungeonLord dl = gd.getDungeonLord(id);
                final BidType bid = dl.getBiddingBySlot(i);
                final BidOption bo = bs.getOption(bid);
                if (bo.getNumberOfDungeonLord() != 3) {
                    bo.addDungeonLord(id);
                }
            }
        }
    }

    private void evaluateDefault(final BidOption bo) {
        final GameDataClass gd = server.getGameDataClass();
        int index = 0;
        for (final int id : bo.getAllDungeonLords()) {
            if (gd.isDungeonLordAvailable(id)) {
                final DungeonLord dl = gd.getDungeonLord(id);
                final Resources cost = bo.getCosts(index);
                final Resources reward = bo.getRewards(index);
                if (index == 2 && bo.getBidType() == BidType.NICENESS) {
                    if (dl.getResources().getEvilness() - reward.getEvilness() >= 0) {
                        takeCostAndGiveReward(dl, cost, reward);
                    }
                } else {
                    takeCostAndGiveReward(dl, cost, reward);
                }
            }
            index++;
        }
    }

    private void takeCostAndGiveReward(final DungeonLord dl, final Resources cost,
            final Resources reward) {
        if (subtractResources(dl, cost)) {
            addResources(dl, reward);
        }
    }

    private void evaluateTunnel(final BidOption bo) {
        final State newState = new DigTunnelPhase(server, bo);
        newState.run();
    }

    private void evaluateCoin(final BidOption bo) {
        final GameDataClass gd = server.getGameDataClass();
        int index = 0;
        for (final int id : bo.getAllDungeonLords()) {
            if (gd.isDungeonLordAvailable(id)) {
                final DungeonLord dl = gd.getDungeonLord(id);
                final Resources res = dl.getResources();
                final Resources cost = bo.getCosts(index);
                final int costImps = cost.getImps();
                final int availableImps = res.getAvailableImps();
                final int tunnelThatCanBeMined = dl.getDungeon().getTunnelsThatCanBeMined();
                if (availableImps == 0) {
                    index++;
                    continue;
                }
                int minImps = findMinImps(costImps, availableImps, tunnelThatCanBeMined);
                //third slot special case
                if (index == 2) {
                    minImps += checkThirdSlot(minImps, availableImps, tunnelThatCanBeMined);
                }
                final Resources reward = bo.getRewards(index);
                res.addBusyImpsMining(minImps);
                reward.addImps(minImps);
                if (minImps != 0) {
                    server.impsChangedBroadcast(-minImps, id);
                }
            }
            index++;
        }
    }

    private int findMinImps(final int costImps, final int availableImps,
            final int tunnelThatCanBeMined) {
        int minImps = costImps;
        if (availableImps < costImps || tunnelThatCanBeMined < costImps) {
            if (availableImps < tunnelThatCanBeMined) {
                minImps = availableImps;
            } else {
                minImps = tunnelThatCanBeMined;
            }
        }
        return minImps;
    }

    private int checkThirdSlot(final int minImps, final int availableImps,
            final int tunnelThatCanBeMined) {
        if (minImps == availableImps && availableImps == 4) {
            return -1;
        } else if (minImps == tunnelThatCanBeMined && tunnelThatCanBeMined == 4) {
            return 1;
        }
        return 0;
    }

    private void evaluateTrap(final BidOption bo) {
        final GameDataClass gd = server.getGameDataClass();
        int index = 0;
        for (final int id : bo.getAllDungeonLords()) {
            if (gd.isDungeonLordAvailable(id)) {
                final DungeonLord dl = gd.getDungeonLord(id);
                final Resources cost = bo.getCosts(index);
                if (subtractResources(dl, cost)) {
                    final List<Trap> traps = drawNewTrap(index);
                    for (final Trap trap : traps) {
                        dl.addTrap(trap);
                        server.trapAcquiredBroadcast(dl.getPlayerID(), trap.id());
                    }
                }
            }
            index++;
        }

    }

    private void evaluateMonster(final BidOption bo) {
        final State newState = new HireMonsterPhase(server, bo);
        newState.run();
    }

    private void evaluateRoom(final BidOption bo) {
        final State newState = new BuildRoomPhase(server, bo);
        newState.run();
    }

    private List<Trap> drawNewTrap(final int number) {
        final List<Trap> trapPool = server.getGameDataClass().getTrapPool();
        final List<Trap> traps = new ArrayList<>();
        if (number == 2) {
            traps.add(trapPool.get(0));
            traps.add(trapPool.get(1));
            trapPool.remove(0);
            trapPool.remove(1);
        } else {
            traps.add(trapPool.get(0));
            trapPool.remove(0);
        }
        return traps;
    }

    private void returnImpsAndCoins(final BidOption coinOption, final BidOption tunnelOption) {
        final GameDataClass gd = server.getGameDataClass();
        final List<Integer> coinList = coinOption.getAllDungeonLords();
        final Set<Integer> dungeonLordInBothOptions = new HashSet<>(coinList);
        dungeonLordInBothOptions.addAll(tunnelOption.getAllDungeonLords());
        final List<Integer> sortedDungeonLord = dungeonLordInBothOptions.stream().sorted().collect(
                Collectors.toList());
        for (final int id : sortedDungeonLord) {
            if (gd.isDungeonLordAvailable(id)) {
                final DungeonLord dl = gd.getDungeonLord(id);
                final Resources res = dl.getResources();
                final int busyImpsMining = res.getBusyImpsMining();
                if (busyImpsMining == 0) {
                    continue;
                }
                server.impsChangedBroadcast(busyImpsMining, id);
                res.resetBusyImpsMining();
                if (coinList.contains(id)) {
                    final int index = coinList.indexOf(id);
                    final Resources reward = coinOption.getRewards(index);
                    final int rewardCoins = reward.getCoins();
                    final int busyImps = reward.getImps();
                    if (busyImps < rewardCoins) {
                        res.addCoins(busyImps);
                        server.goldChangedBroadcast(busyImps, id);
                    } else {
                        res.addCoins(rewardCoins);
                        server.goldChangedBroadcast(rewardCoins, id);
                    }
                    reward.setImps(0);
                }
            }
        }
    }

    private void resetOptions(final Map<BidType, BidOption> bidOptions) {
        for (final BidOption bo : bidOptions.values()) {
            bo.resetDungeonLord();
        }
    }

    private void resetDungeonLords(final Map<Integer, DungeonLord> dungeonLords) {

        for (final int playerID : server.getAllPlayerIDs()) {
            final DungeonLord dl = dungeonLords.get(playerID);
            final BidType secondBid = dl.getBiddingBySlot(2);
            final BidType thirdBid = dl.getBiddingBySlot(3);
            final List<BidType> lockedBids = dl.getLockedBiddings();
            if (lockedBids.isEmpty()) {
                lockedBids.add(secondBid);
                lockedBids.add(thirdBid);
                server.bidRetrievedBroadcast(dl.getBiddingBySlot(1), playerID);
            } else {
                final BidType firstFreedBid = lockedBids.get(0);
                final BidType secondFreedBid = lockedBids.get(1);
                lockedBids.set(0, secondBid);
                lockedBids.set(1, thirdBid);
                server.bidRetrievedBroadcast(firstFreedBid, playerID);
                server.bidRetrievedBroadcast(secondFreedBid, playerID);
                server.bidRetrievedBroadcast(dl.getBiddingBySlot(1), playerID);
            }
            dl.resetBiddingsList();
        }
    }

    private void giveRewardFromRoom(final Map<Integer, DungeonLord> dungeonLords) {
        final GameDataClass gd = server.getGameDataClass();
        for (final int playerID : server.getAllPlayerIDs()) {
            if (!gd.isDungeonLordAvailable(playerID)) {
                continue;
            }
            final DungeonLord dl = dungeonLords.get(playerID);
            final List<Room> rooms = dl.getRooms().values().stream().sorted(new RoomComparator())
                    .collect(
                            Collectors.toList());
            for (final Room room : rooms) {
                if (!room.isActive()) {
                    continue;
                }
                room.deActivate();
                final int costImps = room.getImpsCost();
                dl.getResources().subtractBusyImpsRoom(costImps);
                server.impsChangedBroadcast(costImps, dl.getPlayerID());
                addResources(dl, room.getRewards());
            }
        }
    }

    private void spreadAdventurer(final Map<Integer, Adventurer> adventurers,
            final Map<Integer, DungeonLord> dungeonLords) {
        final List<Adventurer> listOfAdventurers = new ArrayList<>(adventurers.values());
        final List<Adventurer> sortedAdventurers = listOfAdventurers.stream()
                .sorted(new AdventurerComparator()).collect(Collectors.toList());
        final List<DungeonLord> listOfDungeonLords = new ArrayList<>(dungeonLords.values());
        final List<DungeonLord> sortedDungeonLords = listOfDungeonLords.stream()
                .sorted(new DungeonLordComparator()).collect(Collectors.toList());
        int index = 0;
        for (final DungeonLord dl : sortedDungeonLords) {
            final Adventurer adventurer = sortedAdventurers.get(index);
            final Dungeon dungeon = dl.getDungeon();
            dungeon.queueAdventurer(adventurer);
            server.adventurerArrivedBroadcast(adventurer.getId(), dl.getPlayerID());
            index++;
        }
    }

    private void changeOrderPlayer(final BiddingSquare bs) {
        final List<Integer> dungeonLords = bs.getAllDungeonLords();
        final int head = dungeonLords.remove(0);
        dungeonLords.add(head);
    }

    private void nextSeason(final GameDataClass gd) {
        if (server.getGameDataClass().getSeason() != 4) {
            spreadAdventurer(gd.getAllAdventurers(), gd.getAllDungeonLords());
            gd.setSeason(gd.getSeason() + 1);
            server.updateCurrentState(new BiddingPhase(server));
        } else {
            gd.setSeason(1);
            server.updateCurrentState(new CombatPhase(server));
        }
        // print
        for (final Map.Entry<Integer, DungeonLord> entry : gd.getAllDungeonLords().entrySet()) {
            final DungeonLord dl = entry.getValue();
            LoggerFactory.getLogger(this.toString())
                    .info("ID = %d".formatted(entry.getKey())
                            + dl.getResources().toString());
            for (final Adventurer adv : dl.getDungeon().getAdventurerQueue()) {
                LoggerFactory.getLogger(this.toString())
                        .info("adv ID = %d of dl %d".formatted(adv.getId(), dl.getPlayerID()));
            }
            for (final Map.Entry<Integer, Monster> m : dl.getMonsters().entrySet()) {
                LoggerFactory.getLogger(this.toString())
                        .info("monster ID = %d of dl %d".formatted(m.getKey(), dl.getPlayerID()));
            }
            for (final Map.Entry<Integer, Trap> t : dl.getTraps().entrySet()) {
                LoggerFactory.getLogger(this.toString())
                        .info("trap ID = %d of dl %d".formatted(t.getKey(), dl.getPlayerID()));
            }
        }
        gd.resetAdventurer();
        gd.resetMonster();
        gd.resetRoom();
    }

    public static class DungeonLordComparator implements Comparator<DungeonLord> {

        @Override
        public int compare(final DungeonLord o1, final DungeonLord o2) {
            if (o1.getResources().getEvilness() - o2.getResources().getEvilness() == 0) {
                return o1.getPlayerID() - o2.getPlayerID();
            }
            return o1.getResources().getEvilness() - o2.getResources().getEvilness();
        }
    }

    public static class AdventurerComparator implements Comparator<Adventurer> {

        @Override
        public int compare(final Adventurer o1, final Adventurer o2) {
            if (o1.getDifficulty() - o2.getDifficulty() == 0) {
                return o1.getId() - o2.getId();
            }
            return o1.getDifficulty() - o2.getDifficulty();
        }
    }

    public static class RoomComparator implements Comparator<Room> {

        @Override
        public int compare(final Room o1, final Room o2) {
            return o1.getId() - o2.getId();
        }
    }
}

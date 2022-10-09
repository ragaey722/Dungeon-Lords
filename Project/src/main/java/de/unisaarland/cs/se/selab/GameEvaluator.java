package de.unisaarland.cs.se.selab;

import de.unisaarland.cs.se.selab.datapackage.Dungeon;
import de.unisaarland.cs.se.selab.datapackage.DungeonLord;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class GameEvaluator {

    /**
     * map from DungeonLords to their Scores
     */
    private final Map<DungeonLord, Integer> lordPoints;

    /**
     * this should save the highScore at the end of the evaluation
     */
    private int highScore;
    /**
     * number of the highest evilness for a dungeon lord
     */
    private int mostEvil;
    /**
     * number of the most rooms for a dungeon lord
     */
    private int mostRooms;
    /**
     * number of the longest tunnel length for a dungeon lord
     */
    private int longestTunnel;

    /**
     * number of the most monsters of a dungeon lord
     */
    private int mostEmployedMonsters;
    /**
     * number of the most imps for a dungeon lord
     */
    private int mostImps;

    /**
     * number of the most food and gold for a dungeon lord
     */
    private int mostFoodGold;

    /**
     * number of the most unconquered tiles for a dungeon lord
     */
    private int mostUnconqueredTiles;

    /**
     * List of dungeon lords who have most evilness
     */
    private final List<DungeonLord> lordsHaveMaxEvil = new LinkedList<>();

    /**
     * List of dungeon lords who have most rooms
     */
    private final List<DungeonLord> lordsHaveMostRooms = new LinkedList<>();

    /**
     * List of dungeon lords who have the longest tunnels
     */
    private final List<DungeonLord> lordsHaveLongestTunnel = new LinkedList<>();

    /**
     * List of dungeon lords who have most monsters
     */
    private final List<DungeonLord> lordsHaveMostMonsters = new LinkedList<>();

    /**
     * List of dungeon lords who have most imps
     */
    private final List<DungeonLord> lordsHaveMostImps = new LinkedList<>();

    /**
     * List of dungeon lords who have most food and gold
     */
    private final List<DungeonLord> lordsHaveMostFoodGold = new LinkedList<>();

    /**
     * List of dungeon lords who have most unconquered tiles
     */
    private final List<DungeonLord> lordsHaveMostUnconquered = new LinkedList<>();


    public GameEvaluator(final Map<Integer, DungeonLord> dungeonLords) {
        lordPoints = new HashMap<>(dungeonLords.size());
        mostEvil = 0;
        mostRooms = 0;
        longestTunnel = 0;
        mostEmployedMonsters = 0;
        mostImps = 0;
        mostFoodGold = 0;
        mostUnconqueredTiles = 0;
        // put all the players from the given player lists into the DungeonLord->Score mapping
        // while doing so check their stats and change the maximum variables accordingly
        for (final DungeonLord dl : dungeonLords.values()) {
            lordPoints.put(dl, 0);
            mostEvil = Math.max(mostEvil, dl.getResources().getEvilness());
            mostRooms = Math.max(mostRooms, dl.getNumberOfRooms());
            longestTunnel = Math.max(longestTunnel,
                    dl.getDungeon().getActiveDungeonLength()
                            + dl.getDungeon().getConqueredDungeonLength()
                            - dl.getNumberOfRooms());
            mostEmployedMonsters = Math.max(mostEmployedMonsters, dl.getNumberOfMonsters());
            mostImps = Math.max(mostImps, dl.getResources().getImps());
            mostFoodGold = Math.max(mostFoodGold, dl.getResources().getCoins()
                    + dl.getResources().getFoods());
            mostUnconqueredTiles = Math.max(mostUnconqueredTiles,
                    dl.getDungeon().getActiveDungeonLength());
        }
        highScore = Integer.MIN_VALUE;
    }


    public int getHighScore() {
        return this.highScore;
    }


    /**
     * wrapper that calls all helper methods to calculate score of each dungeon lord
     * and then checks for all the lords with the highest score
     *
     * @return a list of dungeon lords who have the highest score
     */
    public List<Integer> evaluate() {

        //eval all the basic evaluations before titles
        evaluateBasic();

        //add lords who have the maximum number of something to the corresponding haveMost list
        addLordToTitleLists();

        // evaluate the scores for the titles
        evaluateLordOfDarkDeeds();
        evaluateLordOfHalls();
        evaluateTunnelLord();
        evaluateMonsterLord();
        evaluateLordOfImps();
        evaluateLordOfRiches();
        evaluateBattleLord();

        //get the highest score
        final List<Integer> highestScorePlayers = new LinkedList<>();
        for (final int score : lordPoints.values()) {
            highScore = Math.max(highScore, score);
        }

        // add all dungeon lords with the highest score to the highest score players list
        for (final Map.Entry<DungeonLord, Integer> dlEntry : lordPoints.entrySet()) {
            if (dlEntry.getValue() == highScore) {
                highestScorePlayers.add(dlEntry.getKey().getPlayerID());
            }
        }

        Collections.sort(highestScorePlayers);
        // return the list of highestScorePlayers
        return highestScorePlayers;
    }

    /**
     * checks if the given dungeon lord has a maximum number of anything and add him
     * to the corresponding haveMost list of that thing
     */
    private void lordsAdder(final DungeonLord dl) {
        // add most evil lords to the according list
        if (dl.getResources().getEvilness() == mostEvil) {
            lordsHaveMaxEvil.add(dl);
        }
        //add lords with most rooms to the according list
        if (dl.getNumberOfRooms() == mostRooms) {
            lordsHaveMostRooms.add(dl);
        }
        // add lords with the longest tunnel to the according list
        if (dl.getDungeon().getActiveDungeonLength()
                + dl.getDungeon().getConqueredDungeonLength()
                - dl.getNumberOfRooms() == longestTunnel) {
            lordsHaveLongestTunnel.add(dl);
        }
        //add lords with the most monsters to the according list
        if (dl.getNumberOfMonsters() == mostEmployedMonsters) {
            lordsHaveMostMonsters.add(dl);
        }
        // add lords with most imps to the according list
        if (dl.getResources().getImps() == mostImps) {
            lordsHaveMostImps.add(dl);
        }
        // add lords with most food and gold to the according list
        if (dl.getResources().getCoins() + dl.getResources().getFoods() == mostFoodGold) {
            lordsHaveMostFoodGold.add(dl);
        }
        //add lords with the most unconquered tiles to the according list
        if (dl.getDungeon().getActiveDungeonLength() == mostUnconqueredTiles) {
            lordsHaveMostUnconquered.add(dl);
        }

    }

    /**
     * goes over all the dungeon lords and pass them to
     * the lord adder to be added to the haveMost lists
     */
    private void addLordToTitleLists() {
        for (final DungeonLord dl : lordPoints.keySet()) {
            lordsAdder(dl);
        }
    }

    /**
     * evaluates all the not-titles scores
     */

    private void evaluateBasic() {
        for (final DungeonLord dl : lordPoints.keySet()) {
            final Dungeon dungeon = dl.getDungeon();
            // eval monsters
            final int scoreOfMonsters = dl.getNumberOfMonsters();
            adjustScore(dl, scoreOfMonsters);
            //eval rooms
            final int scoreOfRooms = dl.getNumberOfRooms() * 2;
            adjustScore(dl, scoreOfRooms);
            //eval conquered tunnels
            final int scoreOfConqueredTunnels = (dungeon.getConqueredDungeonLength()
                    - dungeon.getConqueredRooms()) * -2;
            adjustScore(dl, scoreOfConqueredTunnels);
            //eval imprisoned adventurers
            final int scoreCapturedAdventurers = dungeon.numberOfCapturedAdventurers() * 2;
            adjustScore(dl, scoreCapturedAdventurers);
        }
    }

    /**
     * check the HaveMostEvilness List and adjust scores
     * based on the number of players who won the title
     */
    private void evaluateLordOfDarkDeeds() {
        if (lordsHaveMaxEvil.size() == 1) {
            adjustScore(lordsHaveMaxEvil.get(0), 3);
        } else {
            for (final DungeonLord dl : lordsHaveMaxEvil) {
                adjustScore(dl, 2);
            }
        }
    }

    /**
     * check the HaveMostRooms List and adjust scores
     * based on the number of players who won the title
     */
    private void evaluateLordOfHalls() {
        if (lordsHaveMostRooms.size() == 1) {
            adjustScore(lordsHaveMostRooms.get(0), 3);
        } else {
            for (final DungeonLord dl : lordsHaveMostRooms) {
                adjustScore(dl, 2);
            }
        }
    }

    /**
     * check the HaveLongestTunnel List and adjust scores
     * based on the number of players who won the title
     */

    private void evaluateTunnelLord() {
        if (lordsHaveLongestTunnel.size() == 1) {
            adjustScore(lordsHaveLongestTunnel.get(0), 3);
        } else {
            for (final DungeonLord dl : lordsHaveLongestTunnel) {
                adjustScore(dl, 2);
            }
        }
    }

    /**
     * check the HaveMostMonsters List and adjust scores
     * based on the number of players who won the title
     */
    private void evaluateMonsterLord() {

        if (lordsHaveMostMonsters.size() == 1) {
            adjustScore(lordsHaveMostMonsters.get(0), 3);
        } else {
            for (final DungeonLord dl : lordsHaveMostMonsters) {
                adjustScore(dl, 2);
            }
        }
    }

    /**
     * check the HaveMostImps List and adjust scores
     * based on the number of players who won the title
     */

    private void evaluateLordOfImps() {
        if (lordsHaveMostImps.size() == 1) {
            adjustScore(lordsHaveMostImps.get(0), 3);
        } else {
            for (final DungeonLord dl : lordsHaveMostImps) {
                adjustScore(dl, 2);
            }
        }
    }

    /**
     * check the HaveMostFoodGold List and adjust scores
     * based on the number of players who won the title
     */
    private void evaluateLordOfRiches() {
        if (lordsHaveMostFoodGold.size() == 1) {
            adjustScore(lordsHaveMostFoodGold.get(0), 3);
        } else {
            for (final DungeonLord dl : lordsHaveMostFoodGold) {
                adjustScore(dl, 2);
            }
        }
    }

    /**
     * check the HaveMostUnconquered List and adjust scores
     * based on the number of players who won the title
     */
    private void evaluateBattleLord() {
        if (lordsHaveMostUnconquered.size() == 1) {
            adjustScore(lordsHaveMostUnconquered.get(0), 3);
        } else {
            for (final DungeonLord dl : lordsHaveMostUnconquered) {
                adjustScore(dl, 2);
            }
        }
    }

    /**
     * changes the Score of the given Dungeon lord by the given amount
     */
    private void adjustScore(final DungeonLord dl, final int addedScore) {
        final int curScore = lordPoints.get(dl);
        lordPoints.put(dl, curScore + addedScore);
    }
}

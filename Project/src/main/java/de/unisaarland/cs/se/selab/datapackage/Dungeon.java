package de.unisaarland.cs.se.selab.datapackage;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class Dungeon {

    private final List<Adventurer> adventurerQueue;

    private final Queue<Adventurer> capturedAdventures;

    /**
     * a 2d array of Tunnels that has 3 possible values
     * -null which means space is empty
     * -Tunnel which means this position is a tunnel
     * -Room which is a subclass of Tunnel which means this position is a room
     * is initialized with nulls everywhere except with a Tunnel on point (0,0)
     */
    private final Tunnel[][] dungeonGraph;

    private final int maxSideLength;


    /**
     * currentX represent the current x of the current battleground
     */
    private int currentX;


    /**
     * currentY represent the current y of the current battleground
     */
    private int currentY;


    /**
     * number of Tunnels and Rooms unconquered
     */
    private int activeDungeonLength;


    /**
     * number of Tunnels unconquered
     */
    private int tunnelsThatCanBeMined;


    /**
     * number of conquered Tunnels and Rooms
     */
    private int conqueredDungeonLength;


    /**
     * number of Conquered Rooms
     */
    private int conqueredRooms;

    public Dungeon(final int maxSideLength) {
        this.maxSideLength = maxSideLength;
        dungeonGraph = new Tunnel[maxSideLength][maxSideLength];
        dungeonGraph[0][0] = new Tunnel();
        adventurerQueue = new LinkedList<>();
        capturedAdventures = new LinkedList<>();
        currentX = 0;
        currentY = 0;
        activeDungeonLength = 1;
        tunnelsThatCanBeMined = 1;
        conqueredDungeonLength = 0;
        conqueredRooms = 0;

    }

    public List<Adventurer> getAdventurerQueue() {
        return adventurerQueue;
    }


    public int numberOfCapturedAdventurers() {
        return capturedAdventures.size();
    }


    public void resetAdventurersQueue() {
        adventurerQueue.clear();
    }

    public int getActiveDungeonLength() {
        return activeDungeonLength;
    }

    public int getTunnelsThatCanBeMined() {
        return tunnelsThatCanBeMined;
    }

    public int getConqueredRooms() {
        return conqueredRooms;
    }

    public int getConqueredDungeonLength() {
        return conqueredDungeonLength;
    }

    public int getCurrentX() {
        return currentX;
    }

    public int getCurrentY() {
        return currentY;
    }

    /**
     * removes adventurer from the adventurer queue and add to the captured adventurer queue
     *
     * @param adventurer adventurer who just got imprisoned in combat phase
     */
    public void captureAdventurer(final Adventurer adventurer) {
        capturedAdventures.add(adventurer);
        adventurerQueue.remove(adventurer);
    }

    /**
     * @return true if there are imprisoned adventures
     */
    public boolean isThereCapturedAdventurers() {
        return capturedAdventures.size() > 0;
    }

    /**
     * release the first adventurer in the imprisoned queue
     *
     * @return the id of the released adventurer
     */
    public int releaseAdventurer() {
        if (!capturedAdventures.isEmpty()) {
            return capturedAdventures.poll().getId();
        } else {
            return 0;
        }
    }

    /**
     * queue the adventurer in the front, if he is a knight, otherwise in the back
     *
     * @param adventurer adventurer that just got distributed to this dungeon lord
     */
    public void queueAdventurer(final Adventurer adventurer) {
        if (adventurer.getCharge()) {
            adventurerQueue.add(0, adventurer);
        } else {
            adventurerQueue.add(adventurer);
        }
    }


    /**
     * checks if the player provided valid coordinates for digging and then
     * dig the tunnel there and change variables accordingly
     *
     * @param x coordinates provided by the player
     * @param y coordinates provided by the player
     * @return true if the tunnel could and was dug in the given coordinates
     */
    public boolean digTunnel(final int x, final int y) {
        if (!inBoundaries(x, y) || dungeonGraph[x][y] != null) {
            return false;
        } else if (canWeDigHere(x, y)) {
            dungeonGraph[x][y] = new Tunnel();
            activeDungeonLength++;
            tunnelsThatCanBeMined++;
            return true;
        } else {
            return false;
        }

    }

    /**
     * helper method that checks if any of the surrounding nodes
     * to the given coordinates form a square and checks that
     * there is actually a tile connected to this point
     *
     * @return true if the tunnel can be dug in this position
     */
    private boolean canWeDigHere(final int x, final int y) {

        //check the four surrounding squares to see
        // if digging this tunnel would create a square of tunnels

        // check upper left square
        if (isTile(x - 1, y) && isTile(x, y - 1) && isTile(x - 1, y - 1)) {
            return false;
        }
        // check upper right square
        if (isTile(x + 1, y) && isTile(x, y - 1) && isTile(x + 1, y - 1)) {
            return false;
        }
        // check lower left square
        if (isTile(x - 1, y) && isTile(x, y + 1) && isTile(x - 1, y + 1)) {
            return false;
        }
        // check lower right square
        if (isTile(x + 1, y) && isTile(x, y + 1) && isTile(x + 1, y + 1)) {
            return false;
        }
        // check if the current point is connected to some Tunnel
        return isTile(x - 1, y) || isTile(x + 1, y) || isTile(x, y - 1) || isTile(x, y + 1);


    }

    /**
     * checks if the coordinates is on an unconquered tunnel and that
     * there are no adjacent rooms and the restriction is fulfilled and then
     * set the room in this position or do nothing and return false
     *
     * @param x    coordinates provided by the player
     * @param y    coordinates provided by the player
     * @param room room to be built
     * @return true if the room can be and was built in the given coordinates
     */

    public boolean buildRoom(final int x, final int y, final Room room) {
        if (isTile(x, y)
                && !dungeonGraph[x][y].isConquered()
                && !dungeonGraph[x][y].isRoom()
                && !isThereRoomAdjacent(x, y)
                && isRestrictionFulfilled(x, y, room.getRestriction())) {
            dungeonGraph[x][y] = room;
            tunnelsThatCanBeMined--;
            return true;
        }
        return false;
    }

    /**
     * @return true if there are any adjacent room to the given coordinates
     */
    public boolean isThereRoomAdjacent(final int x, final int y) {
        return isRoom(x + 1, y) || isRoom(x - 1, y) || isRoom(x, y + 1) || isRoom(x, y - 1);
    }

    /**
     * @return true if the given coordinates fulfill the given restriction
     */

    private boolean isRestrictionFulfilled(final int x, final int y, final RoomRestriction rest) {
        return switch (rest) {
            case UPPER_HALF -> y <= (maxSideLength / 2) - 1;
            case LOWER_HALF -> y > (maxSideLength / 2) - 1;
            case INNER -> (x != 0 && x != maxSideLength - 1 && y != 0 && y != maxSideLength - 1);
            case OUTER -> (x == 0 || x == maxSideLength - 1) || (y == 0 || y == maxSideLength - 1);
        };
    }

    /**
     * checks if the battleground can be set here and set it if so,
     * change the currentX and currentY to the given x and y
     *
     * @return true if the battleground can be and was set in this position
     */
    public boolean checkSetBattleGround(final int x, final int y) {
        if ((!inBoundaries(x, y)) || dungeonGraph[x][y] == null || activeDungeonLength == 0) {
            return false;
        }
        if (bfsDungeon(x, y)) {
            currentX = x;
            currentY = y;
            return true;
        }

        return false;
    }

    /**
     * this helper method runs bfs search to find the closest
     * level to the entrance with unconquered tiles
     * and check if the player provided one of these points on that level
     *
     * @param givenX the provided x by the player
     * @param givenY the provided y by the player
     * @return true if the given coordinates are in the closest level to the entrance
     */
    private boolean bfsDungeon(final int givenX, final int givenY) {

        final List<XYPair> validPoints = new ArrayList<>();

        final Queue<XYPair> bfsQue = new LinkedList<>();

        //boolean array that keeps track of which node was already visited
        boolean[][] visited = new boolean[maxSideLength][maxSideLength];

        // initialize the bfs Queue with the entrance points
        bfsQue.add(new XYPair(0, 0));
        int currentLevel = 1;
        visited[0][0] = true;
        int nextLevel = 0;

        while (!bfsQue.isEmpty()) {
            final int x = bfsQue.peek().coordinateX();
            final int y = bfsQue.peek().coordinateY();

            //check for and add the next level tunnels of the current point
            // and adjust the size of the next level accordingly
            nextLevel += bfsNextLevel(x, y, visited, bfsQue);

            // check if the current point is unconquered Tile, if so add it to valid points
            if (!dungeonGraph[x][y].isConquered()) {
                validPoints.add(bfsQue.peek());
            }
            //remove the current point from the queue and decrement current level
            bfsQue.remove();
            currentLevel--;

            //if current level is finished then break the loop if we have valid points
            // otherwise proceed to next level
            if (currentLevel == 0) {
                if (!validPoints.isEmpty()) {
                    break;
                } else {
                    currentLevel = nextLevel;
                    nextLevel = 0;
                }
            }
        }

        //now that we have list of valid points compare them to the given givenX and givenY
        //if there is a match return true
        for (final XYPair xyPair : validPoints) {
            if (xyPair.coordinateX() == givenX && xyPair.coordinateY() == givenY) {
                return true;
            }
        }

        //if there was no match return false
        return false;

    }

    /**
     * this  helper method checks the surrounding nodes of the given
     * coordinates and if any is an unconquered tile it is added in the queue
     *
     * @param visited boolean array that keeps track of which node was already visited
     * @param bfsQue  the bfs Queue
     * @return the number of added nodes in the next level
     */
    private int bfsNextLevel(final int x, final int y,
                             final boolean[][] visited, final Queue<XYPair> bfsQue) {
        //check the tunnel on the right
        int result = 0;
        if (isTile((x + 1), y) && !visited[x + 1][y]) {
            visited[x + 1][y] = true;
            result++;
            bfsQue.add(new XYPair((x + 1), y));
        }

        //check the tunnel on the left
        if (isTile((x - 1), y) && !visited[x - 1][y]) {
            visited[x - 1][y] = true;
            result++;
            bfsQue.add(new XYPair((x - 1), y));
        }

        //check the tunnel beneath
        if (isTile(x, (y + 1)) && !visited[x][y + 1]) {
            visited[x][y + 1] = true;
            result++;
            bfsQue.add(new XYPair(x, (y + 1)));
        }

        //check the tunnel above
        if (isTile(x, (y - 1)) && !visited[x][y - 1]) {
            visited[x][y - 1] = true;
            result++;
            bfsQue.add(new XYPair(x, (y - 1)));
        }

        return result;
    }

    /**
     * @return true if the given coordinates are in boundaries of the dungeon
     */
    private boolean inBoundaries(final int x, final int y) {
        return (x >= 0 && x < maxSideLength && y >= 0 && y < maxSideLength);
    }

    /**
     * @return true if the given coordinates is a tunnel or a room
     */
    private boolean isTile(final int x, final int y) {
        return inBoundaries(x, y) && dungeonGraph[x][y] != null;
    }

    /**
     * @return true if the given coordinates is a room
     */
    public boolean isRoom(final int x, final int y) {
        return isTile(x, y) && dungeonGraph[x][y].isRoom();
    }


    /**
     * @return true if the current battleground is a room
     */
    public boolean isBattleGroundRoom() {
        return dungeonGraph[currentX][currentY].isRoom();
    }

    /**
     * this method set the current battleground to conquered and change other variables accordingly
     */
    public void conquerCurrentTile() {
        conqueredDungeonLength++;
        activeDungeonLength--;
        if (!isBattleGroundRoom()) {
            tunnelsThatCanBeMined--;
        } else {
            conqueredRooms++;
        }
        dungeonGraph[currentX][currentY].setConquered();
    }


    /**
     * This method checks if after using the multi target attack strategy
     * there are actually adventurers whom should get imprisoned then remove them
     * from the adventurer queue and add them to captured adventurers queue
     * this is called after all the events are handled in the combat phase
     */
    public void captureAfterMulti() {
        final Iterator<Adventurer> advItr = adventurerQueue.iterator();
        while (advItr.hasNext()) {
            final Adventurer adv = advItr.next();
            if (adv.getHP() <= 0) {
                advItr.remove();
                this.capturedAdventures.add(adv);
            }

        }


    }
}

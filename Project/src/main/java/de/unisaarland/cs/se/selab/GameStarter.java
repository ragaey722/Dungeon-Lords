package de.unisaarland.cs.se.selab;

import de.unisaarland.cs.se.selab.actionpackage.Actions;
import de.unisaarland.cs.se.selab.actionpackage.ConcreteActionFactory;
import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.datapackage.Adventurer;
import de.unisaarland.cs.se.selab.datapackage.GameDataClass;
import de.unisaarland.cs.se.selab.datapackage.Monster;
import de.unisaarland.cs.se.selab.datapackage.Resources;
import de.unisaarland.cs.se.selab.datapackage.Room;
import de.unisaarland.cs.se.selab.datapackage.Trap;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.json.JSONException;

/**
 * Handles instantiation of GameObjects and calls run on server
 **/
public class GameStarter {

    /**
     * Instantiates all needed objects, seeds the RNG, shuffles the lists and calls run on server
     *
     * @param path is a string that represents the filepath of the JSONConfig file
     * @param port is an integer for the communication of server and server connection
     * @param seed is an integer and seeds the random number generator
     * @param timeout is an integer signaling the time the server waits before closing
     **/
    public void gameStarterWrapper(final String path, final int port, final long seed,
                                   final int timeout) {


        final Random randomNumberGenerator = new Random(seed);

        final Parser parser = new Parser(path);

        try {
            if (!parser.parseCheckConfig()) {
                throw new IllegalArgumentException(
                        "the config file doesn't fulfill the game requirements (wrong values)");
            } else {

                final List<Adventurer> adventurerList = parser.getAdventurers();
                final List<Monster> monsterList = parser.getMonsters();
                final List<Trap> trapList = parser.getTraps();
                final List<Room> roomList = parser.getRooms();

                Collections.shuffle(monsterList, randomNumberGenerator);
                Collections.shuffle(adventurerList, randomNumberGenerator);
                Collections.shuffle(trapList, randomNumberGenerator);
                Collections.shuffle(roomList, randomNumberGenerator);

                final GameDataClass gdc = createGameDataClass(adventurerList, monsterList, trapList,
                        roomList, parser.getDungeonSideLength(), parser.getResources());
                final Server server = createNewServer(gdc,
                        new ServerConnection<>(port, timeout * 1000,
                                new ConcreteActionFactory()), parser.getMaxPlayers(),
                        parser.getMaxYears(), parser.readConfig());

                server.run();

            }
        } catch (JSONException ex) {
            throw (IllegalArgumentException) new IllegalArgumentException().initCause(ex);
        }
    }

    /**
     * Handles instantiation of the GameDataClass
     *
     * @param advList is the list of all adventurers
     * @param monList is the list of all monsters
     * @param trapList is the list of all traps
     * @param roomList is the list of all rooms
     * @param maxSideLength is the side maximum side-length the dungeon grid can have
     * @param resources is the starter resources object
     * @return a GameDataClass object, holding all data of the game
     **/
    private GameDataClass createGameDataClass(final List<Adventurer> advList,
                                              final List<Monster> monList,
                                              final List<Trap> trapList, final List<Room> roomList,
                                              final int maxSideLength, final Resources resources) {
        return new GameDataClass(advList, trapList, roomList, monList, maxSideLength, resources);
    }

    /**
     * Handles instantiation of the Server
     *
     * @param gdc is a GameDataClass, holding all data of the game
     * @param sc is a ServerConnection object, through which the server communicates to the clients
     * @param maxPlayer is the maximum amount of players that can join the game as an integer
     * @param maxYears is the number of years, after which the game ends as an integer
     * @param configJSONString is a string of the config-file, to send to the clients
     * @return a Server object, handling communication and running the whole game
     **/
    private Server createNewServer(final GameDataClass gdc, final ServerConnection<Actions> sc,
            final int maxPlayer, final int maxYears, final String configJSONString) {
        return new Server(gdc, sc, maxPlayer, maxYears,
                 configJSONString);
    }
}

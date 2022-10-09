package de.unisaarland.cs.se.selab.systemtest;

import de.unisaarland.cs.se.selab.Parser;
import de.unisaarland.cs.se.selab.datapackage.Adventurer;
import de.unisaarland.cs.se.selab.datapackage.Monster;
import de.unisaarland.cs.se.selab.datapackage.Room;
import de.unisaarland.cs.se.selab.datapackage.Trap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.slf4j.LoggerFactory;

public class ParserInvocation {
    private final List<Integer> adventurerIDList;
    private final List<Integer> monsterIDList;
    private final List<Integer> trapIDList;
    private final List<Integer> roomIDList;
    private Integer apointer;
    private Integer mpointer;
    private Integer tpointer;
    private Integer rpointer;

    public ParserInvocation(final String jsonString, final Integer seed) {
        final Parser parser = new Parser(jsonString);
        parser.interpretPathAsJSON();
        if (!parser.parseCheckConfig()) {
            LoggerFactory.getLogger(this.toString())
                    .info("Parser couldn't read the given Filepath`s Config");
        }
        final Random randomNumberGenerator = new Random(seed);

        final List<Adventurer> adventurerList = parser.getAdventurers();
        final List<Monster> monsterList = parser.getMonsters();
        final List<Trap> trapList = parser.getTraps();
        final List<Room> roomList = parser.getRooms();

        this.adventurerIDList = new ArrayList<>(adventurerList.size());
        this.monsterIDList = new ArrayList<>(monsterList.size());
        this.trapIDList = new ArrayList<>(trapList.size());
        this.roomIDList = new ArrayList<>(roomList.size());

        this.apointer = 0;
        this.mpointer = 0;
        this.tpointer = 0;
        this.rpointer = 0;

        // !!!ORDER VERY IMPORTANT!!!
        Collections.shuffle(monsterList, randomNumberGenerator);
        Collections.shuffle(adventurerList, randomNumberGenerator);
        Collections.shuffle(trapList, randomNumberGenerator);
        Collections.shuffle(roomList, randomNumberGenerator);

        for (final Adventurer a : adventurerList) {
            this.adventurerIDList.add(a.getId());
        }
        for (final Monster m : monsterList) {
            this.monsterIDList.add(m.id());
        }
        for (final Trap t : trapList) {
            this.trapIDList.add(t.id());
        }
        for (final Room r : roomList) {
            this.roomIDList.add(r.getId());
        }
    }

    public List<Integer> getNextAdventurers(final Integer number) {
        this.apointer += number;
        return this.adventurerIDList.subList(this.apointer - number, this.apointer);
    }

    public List<Integer> getNextMonsters(final Integer number) {
        this.mpointer += number;
        return this.monsterIDList.subList(this.mpointer - number, this.mpointer);
    }

    public Integer getNextTrap() {
        this.tpointer += 1;
        return this.trapIDList.subList(this.tpointer - 1, this.tpointer).get(0);
    }

    public List<Integer> getNextRooms(final Integer number) {
        this.rpointer += number;
        return this.roomIDList.subList(this.rpointer - number, this.rpointer);
    }

}

package de.unisaarland.cs.se.selab;

import de.unisaarland.cs.se.selab.datapackage.Adventurer;
import de.unisaarland.cs.se.selab.datapackage.AttackStrategy;
import de.unisaarland.cs.se.selab.datapackage.Monster;
import de.unisaarland.cs.se.selab.datapackage.Resources;
import de.unisaarland.cs.se.selab.datapackage.Room;
import de.unisaarland.cs.se.selab.datapackage.RoomRestriction;
import de.unisaarland.cs.se.selab.datapackage.Trap;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONParser;

/**
 * Can read JSON files,
 * check them for validity
 * and convert values into GameDataObjects.
 **/
public class Parser {

    private final String path;
    private int maxPlayers;
    private int maxYears;
    private int dungeonSideLength;
    private final List<Monster> monsters;
    private final List<Adventurer> adventurers;
    private final List<Trap> traps;
    private final List<Room> rooms;
    private Resources resources;
    private String jsonDataString;

    /**
     * Constructor for Parser Object.
     * (just initializes internal variables)
     *
     * @param path is a String to Config.json file,
     *             or can be used to pass the configJSON string directly
     **/
    public Parser(final String path) {
        this.path = path;
        this.monsters = new ArrayList<>();
        this.adventurers = new ArrayList<>();
        this.traps = new ArrayList<>();
        this.rooms = new ArrayList<>();
    }

    /**
     * Getter.
     *
     * @return Integer for maxPlayers
     **/
    public int getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * Getter.
     *
     * @return Integer for years
     **/
    public int getMaxYears() {
        return maxYears;
    }

    /**
     * Getter.
     *
     * @return Integer for dungeonSideLength
     **/
    public int getDungeonSideLength() {
        return dungeonSideLength;
    }

    /**
     * Getter.
     *
     * @return List of all monsters
     **/
    public List<Monster> getMonsters() {
        return monsters;
    }

    /**
     * Getter.
     *
     * @return List of all adventurers
     **/
    public List<Adventurer> getAdventurers() {
        return adventurers;
    }

    /**
     * Getter.
     *
     * @return List of all traps
     **/
    public List<Trap> getTraps() {
        return traps;
    }

    /**
     * Getter.
     *
     * @return List of all rooms
     **/
    public List<Room> getRooms() {
        return rooms;
    }

    /**
     * Getter.
     *
     * @return Starting resources object
     **/
    public Resources getResources() {
        return resources;
    }

    /**
     * If not null, returns current jsonDataString
     * else tries to read it from path first and then returns it.
     *
     * @return jsonDataString
     **/
    public String readConfig() {
        if (this.jsonDataString == null) {
            try {
                this.jsonDataString = Files.readString(Paths.get(this.path),
                        StandardCharsets.UTF_8);
                return this.jsonDataString;
            } catch (IOException ex) {
                return null;
            }
        } else {
            return this.jsonDataString;
        }
    }

    /**
     * Sets jsonDataString to path value.
     * (This enables directly passing the JSON String to the Parser
     * and still use parseCheckConfig() successfully.)
    **/
    public void interpretPathAsJSON() {
        this.jsonDataString = this.path;
    }

    /**
     * Takes path from object Reads all information in JSON file,
     * checks for validity and completeness of the data,
     * writes it into object variables that can be retrieved with getters
     *
     * @return a boolean, that is true, if the read was successful (else false)
     **/
    public boolean parseCheckConfig() {
        final JSONObject jsonConfig = (JSONObject) JSONParser.parseJSON(readConfig());

        try {
            // maxPlayers given
            this.maxPlayers = jsonConfig.getInt("maxPlayers");
            if (this.maxPlayers < 1) {
                return false;
            }
            // maxYears given
            this.maxYears = jsonConfig.getInt("years");
            if (this.maxYears < 1) {
                return false;
            }
            this.dungeonSideLength = (Integer) jsonConfig.get("dungeonSideLength");
            // dungeonSideLength 1 <= s <= 15
            if (this.dungeonSideLength < 1 || this.dungeonSideLength > 15) {
                return false;
            }


            // check if resources are given and if insert them instead of default values
            final int food = jsonConfig.optInt("initialFood", 3);
            final int gold = jsonConfig.optInt("initialGold", 3);
            final int imps = jsonConfig.optInt("initialImps", 3);
            if (checkSmallerZero(food, gold, imps)) {
                return false;
            }
            final int evilness = jsonConfig.optInt("initialEvilness", 5);
            if (evilness < 0 | evilness > 15) {
                return false;
            }
            this.resources = new Resources(gold, evilness, imps, food);

            //parse and check Monsters
            if (!createMonsters(jsonConfig.getJSONArray("monsters"))) {
                return false;
            }
            //parse and check Adventurers
            if (!createAdventurers(jsonConfig.getJSONArray("adventurers"))) {
                return false;
            }
            //parse and check Traps
            if (!createTraps(jsonConfig.getJSONArray("traps"))) {
                return false;
            }
            //parse and check Rooms
            return createRooms(jsonConfig.getJSONArray("rooms"));
        } catch (JSONException ex) {
            return false;
        }
    }

    /**
     * Checks for validity of an uniqueID, given a list of already present IDs
     *
     * @param id is the id that is to be tested
     * @param ids is the collection of ids that shouldn't contain the id
     * @return a boolean that is true, if the id is invalid (else false)
     **/
    private boolean idDoubleNotPresent(final int id, final Collection<Integer> ids) {
        return (id < 0 || ids.contains(id));
    }

    /**
     * Tests if all given arguments are greater than zero
     *
     * @param args are Integer varargs that are tested to be above 0
     * @return a boolean that is true, if an argument is smaller than zero (else false)
     **/
    private boolean checkSmallerZero(final Integer... args) {
        for (final Integer arg : args) {
            if (arg < 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates monster objects from given JSONArray and saves them in object variables
     * to be retrieved by getters
     *
     * @param jsonData is a JSONArray containing all monsters in the JSON file
     * @return a boolean that is true, if the JSONArray contained just valid monsters,
     * and they were successfully read and translated into GameObjects
     **/
    private boolean createMonsters(final JSONArray jsonData) {
        final List<Integer> monsterIDs = new ArrayList<>();
        AttackStrategy as;

        for (final Object m1 : jsonData) {
            final JSONObject m = (JSONObject) m1;
            final int id = m.getInt("id");
            // ID >= 0 & unique
            if (idDoubleNotPresent(id, monsterIDs)) {
                return false;
            }
            final int hunger = m.optInt("hunger", 0);
            // hunger >= 0
            if (hunger < 0) {
                return false;
            }
            final int evilness = m.optInt("evilness", 0);
            // evilness >= 0
            if (evilness < 0) {
                return false;
            }
            final int damage = m.getInt("damage");
            // damage >= 1
            if (damage < 1) {
                return false;
            }
            // attack strategy is set and correct
            switch ((String) m.get("attackStrategy")) {
                case "BASIC":
                    as = AttackStrategy.BASIC;
                    break;
                case "MULTI":
                    as = AttackStrategy.MULTI;
                    break;
                case "TARGETED":
                    as = AttackStrategy.TARGETED;
                    break;
                default:
                    return false;
            }

            // add monster
            monsterIDs.add(id);
            this.monsters.add(new Monster(id, hunger, evilness, damage, as));
        }
        // number of adventurers >= maxYears * 4 * 3
        return this.monsters.size() >= this.maxYears * 4 * 3;
    }

    /**
     * Checks if charge is specified correctly (it can be true, false, or not present,
     * in which case it defaults to false. But it can't be present as a non-boolean variable)
     *
     * @param adventurer is a JSONObject of the adventurer to be tested
     * @return a boolean that is true, if the JSONObject has a charge variable specified,
     * that is non-boolean (else false)
     **/
    private boolean chargeCatcher(final JSONObject adventurer) {
        try {
            adventurer.getBoolean("charge");
        } catch (JSONException ex) {
            if (null != adventurer.opt("charge")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates adventurer objects from given JSONArray and saves them in object variables
     * to be retrieved by getters
     *
     * @param jsonData is a JSONArray containing all adventurers in the JSON file
     * @return a boolean that is true, if the JSONArray contained just valid adventurers,
     * and they were successfully read and translated into GameObjects
     **/
    private boolean createAdventurers(final Iterable<Object> jsonData) {
        final List<Integer> adventurerIDs = new ArrayList<>();

        for (final Object a1 : jsonData) {
            final JSONObject a = (JSONObject) a1;
            final int id = a.getInt("id");
            // ID >= 0 & unique
            if (idDoubleNotPresent(id, adventurerIDs)) {
                return false;
            }
            final int difficulty = a.getInt("difficulty");
            // 1 <= difficulty <= 8
            if (difficulty < 1 || difficulty > 8) {
                return false;
            }
            final int healthPoints = a.getInt("healthPoints");
            // healthPoints >= 1
            if (healthPoints < 1) {
                return false;
            }
            final int healValue = a.optInt("healValue", 0);
            final int defuseValue = a.optInt("defuseValue", 0);
            // values >= 0
            if (checkSmallerZero(healValue, defuseValue)) {
                return false;
            }
            // charge is given, but it's not true, or false
            if (chargeCatcher(a)) {
                return false;
            }
            final boolean charge = a.optBoolean("charge", false);
            // add adventurer
            adventurerIDs.add(id);
            this.adventurers.add(new Adventurer(id, difficulty, healthPoints,
                    healValue, defuseValue, charge));
        }
        // number of adventurers >= maxYears * maxPlayers * 3
        return this.adventurers.size() >= this.maxYears * this.maxPlayers * 3;
    }

    /**
     * Creates traps objects from given JSONArray and saves them in object variables
     * to be retrieved by getters
     *
     * @param jsonData is a JSONArray containing all traps in the JSON file
     * @return a boolean that is true, if the JSONArray contained just valid traps,
     * and they were successfully read and translated into GameObjects
     **/
    private boolean createTraps(final JSONArray jsonData) {
        final List<Integer> trapIDs = new ArrayList<>();
        AttackStrategy as;
        int targetValue = 42;

        for (final Object t1 : jsonData) {
            final JSONObject t = (JSONObject) t1;
            final int id = t.getInt("id");
            // ID >= 0 & unique
            if (idDoubleNotPresent(id, trapIDs)) {
                return false;
            }
            final int damage = t.getInt("damage");
            // damage >= 1
            if (damage < 1) {
                return false;
            }
            // check for attack strategy and in case of TARGETED 1 <= target <= 3 has to be set
            switch ((String) t.get("attackStrategy")) {
                case "BASIC":
                    as = AttackStrategy.BASIC;
                    break;
                case "MULTI":
                    as = AttackStrategy.MULTI;
                    break;
                case "TARGETED":
                    as = AttackStrategy.TARGETED;
                    // 1 <= target <= 3
                    if ((Integer) t.get("target") < 1 || (Integer) t.get("target") > 3) {
                        return false;
                    } else {
                        targetValue = (Integer) t.get("target");
                    }
                    break;
                default:
                    return false;
            }

            trapIDs.add(id);
            this.traps.add(new Trap(id, damage, targetValue, as));
        }
        // number of traps >= 4 * 4 * maxYears
        return this.traps.size() >= 4 * 4 * this.maxYears;
    }

    /**
     * Creates rooms objects from given JSONArray and saves them in object variables
     * to be retrieved by getters
     *
     * @param jsonData is a JSONArray containing all rooms in the JSON file
     * @return a boolean that is true, if the JSONArray contained just valid rooms,
     * and they were successfully read and translated into GameObjects
     **/
    private boolean createRooms(final JSONArray jsonData) {
        final List<Integer> roomIDs = new ArrayList<>();
        RoomRestriction rr;

        for (final Object r1 : jsonData) {
            final JSONObject r = (JSONObject) r1;
            final int id = r.getInt("id");
            // ID >= 0 & unique
            if (idDoubleNotPresent(id, roomIDs)) {
                return false;
            }
            final int activation = r.getInt("activation");
            // activation >= 1
            if (activation < 1) {
                return false;
            }
            final int food = r.optInt("food", 0);
            final int gold = r.optInt("gold", 0);
            final int imps = r.optInt("imps", 0);
            final int niceness = r.optInt("niceness", 0);
            // 0 <= values
            if (checkSmallerZero(food, gold, imps, niceness)) {
                return false;
            }
            // valid and given restrictions
            switch ((String) r.get("restriction")) {
                case "UPPER_HALF":
                    rr = RoomRestriction.UPPER_HALF;
                    break;
                case "LOWER_HALF":
                    rr = RoomRestriction.LOWER_HALF;
                    break;
                case "OUTER_RING":
                    rr = RoomRestriction.OUTER;
                    break;
                case "INNER_RING":
                    rr = RoomRestriction.INNER;
                    break;
                default:
                    return false;
            }

            roomIDs.add(id);
            final Resources resources = new Resources(gold, niceness, imps, food);
            this.rooms.add(new Room(id, rr, activation, resources));
        }
        // number of rooms >= 2 * 4 * maxYears
        return this.rooms.size() >= 2 * 4 * this.maxYears;
    }
}

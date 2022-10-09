package starterparsertest;

import de.unisaarland.cs.se.selab.Parser;
import de.unisaarland.cs.se.selab.datapackage.GameDataClass;
import de.unisaarland.cs.se.selab.datapackage.Resources;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import phasestest.DummyServer;

class StartupTest {

    @Test
    void testReadConfigAndGiveToServer() {
        final String path = "./src/test/java/starterparsertest/configtest.json";
        final Parser parser = new Parser(path);

        final Resources resources = parser.getResources();
        final GameDataClass gdc = new GameDataClass(parser.getAdventurers(), parser.getTraps(),
                parser.getRooms(), parser.getMonsters(), parser.getDungeonSideLength(), resources);

        final DummyServer serverpleasework = new DummyServer(gdc, null, parser.getMaxPlayers(),
                parser.getMaxYears(), parser.readConfig());
        Assertions.assertNotEquals("{}", serverpleasework.getConfigJSONString());
    }




}
package starterparsertest;

import de.unisaarland.cs.se.selab.datapackage.GameDataClass;
import de.unisaarland.cs.se.selab.datapackage.Resources;
import java.util.ArrayList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import phasestest.DummyServer;

class ServerTest {

    @Test
    void serverGetterTest() {
        final Resources resources = new Resources(3, 3, 3, 3);
        final GameDataClass gdc = new GameDataClass(new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), 3, resources);
        final DummyServer server3 = new DummyServer(gdc, null, 4, 3,
                 "jsonString");
        Assertions.assertEquals(0, server3.getNumberOfPlayers());
        server3.addPlayerID(0);
        server3.addCommID(0, 0);
        Assertions.assertEquals(1, server3.getNumberOfPlayers());
    }




}

package phasestest;

import de.unisaarland.cs.se.selab.Server;
import de.unisaarland.cs.se.selab.actionpackage.Actions;
import de.unisaarland.cs.se.selab.actionpackage.ConcreteActionFactory;
import de.unisaarland.cs.se.selab.datapackage.Adventurer;
import de.unisaarland.cs.se.selab.datapackage.GameDataClass;
import de.unisaarland.cs.se.selab.datapackage.Monster;
import de.unisaarland.cs.se.selab.datapackage.Resources;
import de.unisaarland.cs.se.selab.datapackage.Room;
import de.unisaarland.cs.se.selab.datapackage.Trap;
import de.unisaarland.cs.se.selab.gamelogic.RegistrationPhase;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RegisterPhaseTest {

    List<Monster> monsterList;
    List<Adventurer> advList;
    List<Room> roomList;
    List<Trap> trapList;

    ConcreteActionFactory actionFactory;
    GameDataClass mockGdc;

    Server mockServer;

    Resources initialResource;

    RegistrationPhase rp;


    @BeforeEach
    public void setup() {
        monsterList = new ArrayList<>();
        advList = new ArrayList<>();
        roomList = new ArrayList<>();
        trapList = new ArrayList<>();
        Collections.shuffle(monsterList);
        Collections.shuffle(advList);
        Collections.shuffle(roomList);
        Collections.shuffle(trapList);
        initialResource = new Resources(3, 5, 3, 3);
        mockGdc = new GameDataClass(advList, trapList, roomList, monsterList, 2,
                initialResource);
        mockServer = new DummyServer(mockGdc, null, 2, 2,
                "MockString");
        actionFactory = new ConcreteActionFactory();
        rp = new RegistrationPhase(mockServer);
    }

    @Test
    void registerTest() {
        rp.run();
        Assertions.assertEquals(2, mockServer.getAllPlayerIDs().size());
    }

    @Test
    void registerTestActionFailed() {
        final Actions actionFailed = actionFactory.createActivateRoom(0, 0);
        actionFailed.accept(rp);

    }

    @Test
    void registerTestActionFailed2() {
        final Actions actionFailed2 = actionFactory.createBattleGround(0, 0, 0);
        actionFailed2.accept(rp);
    }

    @Test
    void registerTestActionFailed3() {
        final Actions actionFailed3 = actionFactory.createBuildRoom(0, 1, 0, 0);
        actionFailed3.accept(rp);
    }

    @Test
    void registerTestLeave() {
        final RegistrationPhase rp = new RegistrationPhase(mockServer);
        final Actions leave = actionFactory.createLeave(0);
        leave.accept(rp);
    }

    @Test
    void registerTestEndTurn() {
        final RegistrationPhase rp = new RegistrationPhase(mockServer);
        final Actions actionFailed = actionFactory.createActivateRoom(0, 0);
        actionFailed.accept(rp);
    }


}

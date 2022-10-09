package phasestest;

import de.unisaarland.cs.se.selab.Server;
import de.unisaarland.cs.se.selab.actionpackage.Actions;
import de.unisaarland.cs.se.selab.actionpackage.ConcreteActionFactory;
import de.unisaarland.cs.se.selab.comm.ServerConnection;
import de.unisaarland.cs.se.selab.datapackage.GameDataClass;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.platform.commons.logging.LoggerFactory;

public class DummyServer extends Server {

    final List<Integer> registeredPlayer;

    public DummyServer(final GameDataClass game, final ServerConnection<Actions> sc,
            final int maxPlayers, final int maxYears, final String configJSONString) {
        super(game, sc, maxPlayers, maxYears, configJSONString);
        registeredPlayer = new ArrayList<>();
        registeredPlayer.add(0);
    }

    @Override
    public Actions getNextAction() {
        final ConcreteActionFactory actionFactory = new ConcreteActionFactory();
        final int lastPlayerID = Collections.max(registeredPlayer);
        final Actions ra = actionFactory.createRegister(lastPlayerID, "Player0");
        registeredPlayer.add(lastPlayerID + 1);
        return ra;
    }

    private String logGetter(final String s) {
        return s + " event has been sent";
    }

    @Override
    public void actionFailed(final int commID, final String s) {
        LoggerFactory.getLogger(this.getClass()).info(() -> logGetter(s));
    }

    @Override
    public void config(final int commID) {
        LoggerFactory.getLogger(this.getClass()).info(() -> logGetter("config"));
    }

    @Override
    public void gameStartedBroadcast() {
        LoggerFactory.getLogger(this.getClass()).info(() -> logGetter("game is started"));
    }

    @Override
    public void playerBroadcast(final String name, final int player) {
        LoggerFactory.getLogger(this.getClass()).info(() -> logGetter(name + player));
    }

    @Override
    public void nextYearBroadcast(final int year) {
        LoggerFactory.getLogger(this.getClass()).info(() -> logGetter("year" + year));
    }



}

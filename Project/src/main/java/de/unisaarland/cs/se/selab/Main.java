package de.unisaarland.cs.se.selab;


import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Main Class in which everything happens.
 **/
public class Main {

    /**
     * The main constructor, that runs everything
     *
     * @param args is a String List of the provided arguments
     **/
    public static void main(final String[] args) {

        String config = "";
        int port = 0;
        int seed = 0;
        int timeout = 0;
        final var mainParser = new DefaultParser();
        final var options = new Options();
        options.addOption("c", "config", true, "config file path");
        options.addOption("p",  "port", true, "port");
        options.addOption("s", "seed", true, "random seed");
        options.addOption("t", "timeout", true, "server timeout in sec");

        try {
            final CommandLine line = mainParser.parse(options, args);
            config = line.getOptionValue("config");
            try {
                port = Integer.parseInt(line.getOptionValue("port"));
                seed = Integer.parseInt(line.getOptionValue("seed"));
                timeout = Integer.parseInt(line.getOptionValue("timeout"));
            } catch (NumberFormatException e) {
                System.exit(1);
            }
        } catch (ParseException e) {
            System.exit(1);
        }

        final GameStarter gameStarter = new GameStarter();
        try {
            gameStarter.gameStarterWrapper(config, port, seed, timeout);
        } catch (IllegalArgumentException exception) {
            System.exit(1);
        }
    }


}

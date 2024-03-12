package org.Utils;

import org.Constants.AllTheConstants;
import org.Controller.GameEngine;
import org.Exceptions.InvalidCommand;
import org.Exceptions.MapInvalidException;
import org.Model.Map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


/**
 * Utility class to handle the Command received from players.
 */
public class Command implements Serializable {
    /**
     * Command string received from user
     */
    private String d_receivedCommand;

    /**
     * Helper data member for returning the result of handling each command separately
     */
    private HashMap<String, ArrayList<String>> d_returnType;

    /**
     * parameterized constructor
     *
     * @param l_command : Command string
     */
    public Command(String l_command) {
        this.d_receivedCommand = l_command.replaceAll(" -", " ");
    }

    /**
     * Method returns the Main Operation of the command.
     *
     * @return String : Main operation substring of the command
     * @throws InvalidCommand : In case wrong operation is provided
     */
    public String getMainOperation() throws InvalidCommand {
        String l_mainOperation = d_receivedCommand.split(" ")[0].trim();
        if (!AllTheConstants.allMapCommands.contains(l_mainOperation)) {
            throw new InvalidCommand("Invalid command provided.");
        }
        return l_mainOperation;
    }

    /**
     * Method handles command string related to editing continents
     *
     * @return HashMap : of the all the add and remove based arguments
     * @throws InvalidCommand :  In case wrong operation is provided.
     */
    public HashMap<String, ArrayList<String>> handleEditContinent() throws InvalidCommand {
        d_returnType = new HashMap<>();
        ArrayList<String> l_allAddOperations = new ArrayList<>();
        ArrayList<String> l_allRemoveOperations = new ArrayList<>();
        String[] l_commandSplit = d_receivedCommand.split(" ");

        if (l_commandSplit.length < 3) {
            GameEngine.log("Command", LogLevel.BASICLOG, "Invalid number of arguments provided for editcontinent");
            throw new InvalidCommand("Invalid command for editcontinent.");
        }
        int i = 1;
        while (i < l_commandSplit.length) {
            if (l_commandSplit[i].equalsIgnoreCase("add")) {
                l_allAddOperations.add(l_commandSplit[i + 1]);
                l_allAddOperations.add(l_commandSplit[i + 2]);
                i += 3;
            } else if (l_commandSplit[i].equalsIgnoreCase(("remove"))) {
                l_allRemoveOperations.add(l_commandSplit[i + 1]);
                i += 2;
            }
        }
        d_returnType.put("add", l_allAddOperations);
        d_returnType.put("remove", l_allRemoveOperations);
        return d_returnType;
    }

    /**
     * Method handles command string related to editing country
     *
     * @return HashMap : of the all the add and remove based arguments
     * @throws InvalidCommand :  In case wrong operation is provided.
     */
    public HashMap<String, ArrayList<String>> handleEditCountry() throws InvalidCommand {
        d_returnType = new HashMap<>();
        ArrayList<String> l_allAddOperations = new ArrayList<>();
        ArrayList<String> l_allRemoveOperations = new ArrayList<>();
        String[] l_commandSplit = d_receivedCommand.split(" ");

        if (l_commandSplit.length < 3) {
            GameEngine.log("Command", LogLevel.BASICLOG, "Invalid number of arguments provided for editcountry");
            throw new InvalidCommand("Invalid command for editcountry.");
        }

        int i = 1;
        while (i < l_commandSplit.length) {
            if (l_commandSplit[i].equalsIgnoreCase("add")) {
                l_allAddOperations.add(l_commandSplit[i + 1]);
                l_allAddOperations.add(l_commandSplit[i + 2]);
                i += 3;
            } else if (l_commandSplit[i].equalsIgnoreCase(("remove"))) {
                l_allRemoveOperations.add(l_commandSplit[i + 1]);
                i += 2;
            }
        }
        d_returnType.put("add", l_allAddOperations);
        d_returnType.put("remove", l_allRemoveOperations);
        return d_returnType;
    }

    /**
     * Method handles command string related to editing neighbours
     *
     * @return HashMap : of the all the add and remove based arguments
     * @throws InvalidCommand :  In case wrong operation is provided.
     */
    public HashMap<String, ArrayList<String>> handleEditNeighbour() throws InvalidCommand {
        d_returnType = new HashMap<>();
        ArrayList<String> l_allAddOperations = new ArrayList<>();
        ArrayList<String> l_allRemoveOperations = new ArrayList<>();
        String[] l_commandSplit = d_receivedCommand.split(" ");

        if (l_commandSplit.length < 4) {
            GameEngine.log("Command", LogLevel.BASICLOG, "Invalid number of arguments provided for editneighbour");
            throw new InvalidCommand("Invalid command for editneighbour.");
        }
        int i = 1;
        while (i < l_commandSplit.length) {
            if (l_commandSplit[i].equalsIgnoreCase("add")) {
                l_allAddOperations.add(l_commandSplit[i + 1]);
                l_allAddOperations.add(l_commandSplit[i + 2]);
            } else if (l_commandSplit[i].equalsIgnoreCase(("remove"))) {
                l_allRemoveOperations.add(l_commandSplit[i + 1]);
                l_allRemoveOperations.add(l_commandSplit[i + 2]);
            }
            i += 3;
        }
        d_returnType.put("add", l_allAddOperations);
        d_returnType.put("remove", l_allRemoveOperations);
        return d_returnType;
    }

    /**
     * Method handles command string related to loadmap command
     *
     * @return String : filename which needs to be loaded
     * @throws InvalidCommand :  In case wrong operation is provided.
     */
    public String handleLoadMap() throws InvalidCommand {
        String[] l_commandSplit = d_receivedCommand.split(" ");
        if (l_commandSplit.length != 2) {
            GameEngine.log("Command", LogLevel.BASICLOG, "Invalid number of arguments provided for loadmap");
            throw new InvalidCommand("Invalid command for loadmap");
        }
        return l_commandSplit[1].trim();
    }

    /**
     * Method handles command string related to editing map
     *
     * @return String : filename which needs to be loaded
     * @throws InvalidCommand :  In case wrong operation is provided.
     */
    public String handleEditMap() throws InvalidCommand {
        String[] l_commandSplit = d_receivedCommand.split(" ");
        if (l_commandSplit.length != 2) {
            GameEngine.log("Command", LogLevel.BASICLOG, "Invalid number of arguments provided for editmap");
            throw new InvalidCommand("Invalid command for editmap");
        }
        return l_commandSplit[1].trim();
    }

    /**
     * Method handles command string related to saving map
     *
     * @return String : filename with which map needs to be saved
     * @throws InvalidCommand :  In case wrong operation is provided.
     */
    public String handleSaveMap() throws InvalidCommand {
        String[] l_commandSplit = d_receivedCommand.split(" ");
        if (l_commandSplit.length != 2) {
            GameEngine.log("Command", LogLevel.BASICLOG, "Invalid number of arguments provided for savemap");
            throw new InvalidCommand("Invalid command for savemap");
        }
        return l_commandSplit[1].trim();
    }

    /**
     * Method handles command string related to adding and removing of player
     *
     * @return HashMap : of the all the add and remove based arguments
     * @throws InvalidCommand :  In case wrong operation is provided.
     */
    public HashMap<String, ArrayList<String>> handleAddAndRemovePlayer() throws InvalidCommand {
        d_returnType = new HashMap<>();
        ArrayList<String> l_allAddOperations = new ArrayList<>();
        ArrayList<String> l_allRemoveOperations = new ArrayList<>();
        String[] l_commandSplit = d_receivedCommand.split(" ");
        if (l_commandSplit.length < 3) {
            GameEngine.log("Command", LogLevel.BASICLOG, "Invalid number of arguments provided for gameplayer");
            throw new InvalidCommand("Invalid arguments for gameplayer");
        }
        int i = 1;


        while (i < l_commandSplit.length) {
            if (l_commandSplit[i].equalsIgnoreCase("add")) {
                l_allAddOperations.add(l_commandSplit[i + 1]);
            } else if (l_commandSplit[i].equalsIgnoreCase("remove")) {
                l_allRemoveOperations.add(l_commandSplit[i + 1]);
            }
            i += 1;
        }
        d_returnType.put("add", l_allAddOperations);
        d_returnType.put("remove", l_allRemoveOperations);
        return d_returnType;
    }

    /**
     * Method handles command string related to handling of deploy command
     *
     * @return ArrayList : of the all the country based arguments provided
     * @throws InvalidCommand :  In case wrong operation is provided.
     */
    public ArrayList<String> handleDeployArmies() throws InvalidCommand {
        ArrayList<String> l_returnArguments = new ArrayList<>();
        String[] l_commandSplit = d_receivedCommand.split(" ");
        int i = 1;
        if (l_commandSplit.length != 3) {
            GameEngine.log("Command", LogLevel.BASICLOG, "Invalid number of arguments provided for deploy");
            throw new InvalidCommand("Not a valid Deploy command.");
        } else {
            l_returnArguments.add(l_commandSplit[1]);
            l_returnArguments.add(l_commandSplit[2]);
        }
        return l_returnArguments;
    }

    /**
     * Method handles command string related to handling of advance command
     *
     * @return ArrayList : of the all the country based arguments provided
     * @throws InvalidCommand :  In case wrong operation is provided.
     */
    public ArrayList<String> handleAdvanceArmies() throws InvalidCommand {
        ArrayList<String> l_returnArguments = new ArrayList<>();
        String[] l_commandSplit = d_receivedCommand.split(" ");
        int i = 1;
        if (l_commandSplit.length != 4) {
            GameEngine.log("Command", LogLevel.BASICLOG, "Invalid number of arguments provided for advance");
            throw new InvalidCommand("Not a valid Deploy command.");
        } else {
            l_returnArguments.add(l_commandSplit[1]);
            l_returnArguments.add(l_commandSplit[2]);
            l_returnArguments.add(l_commandSplit[3]);
        }
        return l_returnArguments;
    }

    /**
     * Method handles command string related to handling of blockade command
     *
     * @return String : Returns the name of the country, on which bloakade has to be applied
     * @throws InvalidCommand :  In case wrong operation is provided.
     */
    public String handleBlockadeCommand() throws InvalidCommand {
        String[] l_commandSplit = d_receivedCommand.split(" ");
        if (l_commandSplit.length != 2) {
            GameEngine.log("Command", LogLevel.BASICLOG, "Invalid number of arguments provided for blockade");
            throw new InvalidCommand("Not a valid Blockade Command");
        } else {
            return l_commandSplit[1];
        }

    }

    /**
     * Method handles command string related to handling of deploy Airlift
     *
     * @return ArrayList : of the all the country based arguments provided
     * @throws InvalidCommand :  In case wrong operation is provided.
     */
    public ArrayList<String> handleAirliftCommand() throws InvalidCommand {
        ArrayList<String> l_returnArguments = new ArrayList<>();
        String[] l_commandSplit = d_receivedCommand.split(" ");
        int i = 1;
        if (l_commandSplit.length != 4) {
            GameEngine.log("Command", LogLevel.BASICLOG, "Invalid number of arguments provided for airlift");
            throw new InvalidCommand("Not a valid airlift command.");
        } else {
            l_returnArguments.add(l_commandSplit[1]);
            l_returnArguments.add(l_commandSplit[2]);
            l_returnArguments.add(l_commandSplit[3]);
        }
        return l_returnArguments;
    }

    /**
     * Method handles command string related to handling of Bomb command
     *
     * @return String : Returns the name of the country, on which bomb has to be applied
     * @throws InvalidCommand :  In case wrong operation is provided.
     */
    public String handleBombCommand() throws InvalidCommand {
        String[] l_commandSplit = d_receivedCommand.split(" ");

        if (l_commandSplit.length != 2) {
            GameEngine.log("Command", LogLevel.BASICLOG, "Invalid number of arguments provided for bomb");
            throw new InvalidCommand("Not a valid Bomb Command");
        } else {
            return l_commandSplit[1];
        }

    }

    /**
     * Method handles command string related to handling of negotiate command
     *
     * @return String : Returns the name of the player , with which current player wants to negotiate
     * @throws InvalidCommand :  In case wrong operation is provided.
     */
    public String handleNegotiateCommand() throws InvalidCommand {
        String[] l_commandSplit = d_receivedCommand.split(" ");
        if (l_commandSplit.length > 2) {
            GameEngine.log("Command", LogLevel.BASICLOG, "Invalid number of arguments provided for negotiate");
            throw new InvalidCommand("Not a valid Negotiate Command.");
        } else {
            return l_commandSplit[1];
        }
    }

    /**
     * TO handle the Tournament Command.
     * @param p_command
     * @return
     * @throws InvalidCommand
     * @throws MapInvalidException
     */
    public HashMap<String, ArrayList<String>> handleTournamentCommand(String p_command) throws InvalidCommand, MapInvalidException {
        d_returnType = new HashMap<>();
        ArrayList<String> l_maps = new ArrayList<>();
        ArrayList<String> l_players = new ArrayList<>();
        ArrayList<String> l_games = new ArrayList<>();
        ArrayList<String> l_turns = new ArrayList<>();
        String[] l_commandSplit = p_command.split(" -");

        int i = 1;

        while (i < l_commandSplit.length) {
            String[] l_options = l_commandSplit[i].split(" ");
            if (l_options[0].equalsIgnoreCase("M")) {
                for (int j = 1; j < l_options.length; j++) {
                    Map l_map = new Map();
                    l_map.loadMap(l_options[j]);
                    if(l_map.validateMap()) {
                        l_maps.add(l_options[j]);
                    }
                    else {
                        throw new InvalidCommand("Invalid Map Provided");
                    }
                }
                d_returnType.put("M", l_maps);
            } else if (l_options[0].equalsIgnoreCase("P")) {
                for (int j = 1; j < l_options.length; j++) {
                    if(AllTheConstants.TOURNAMENT_PLAYER_BEHAVIORS.contains(l_options[j])) {
                        l_players.add(l_options[j]);
                    }
                    else {
                        throw new InvalidCommand("Invalid Behaviour Strategy Provided");
                    }
                }
                d_returnType.put("P", l_players);
            } else if (l_options[0].equalsIgnoreCase("G")) {
                if(Integer.parseInt(l_options[1]) >= 1 && Integer.parseInt(l_options[1]) <= 5){
                    l_games.add(l_options[1]);
                }
                else {
                    throw new InvalidCommand("Invalid Number of Games Provided");
                }
                d_returnType.put("G", l_games);
            } else if (l_options[0].equalsIgnoreCase("D")) {
                if(Integer.parseInt(l_options[1]) >= 10 && Integer.parseInt(l_options[1]) <= 50){
                    l_turns.add(l_options[1]);
                }
                else {
                    throw new InvalidCommand("Invalid Number of Turns Provided");
                }
                d_returnType.put("D", l_turns);
            }
            i += 1;
        }
        return d_returnType;
    }

    /**
     * TO handle the Save Game.
     * @return
     * @throws InvalidCommand
     */
    public String handleSaveGame() throws InvalidCommand {
        String[] l_commandSplit = d_receivedCommand.split(" ");

        if (l_commandSplit.length != 2) {
            GameEngine.log("Command", LogLevel.BASICLOG, "Invalid number of arguments provided for savegame command");
            throw new InvalidCommand("Not a valid savegame Command");
        } else {
            return l_commandSplit[1];
        }
    }

    /**
     * To handle the Load game.
     * @return
     * @throws InvalidCommand
     */
    public String handleLoadGame() throws InvalidCommand {
        String[] l_commandSplit = d_receivedCommand.split(" ");

        if (l_commandSplit.length != 2) {
            GameEngine.log("Command", LogLevel.BASICLOG, "Invalid number of arguments provided for loadgame command");
            throw new InvalidCommand("Not a valid loadgame Command");
        } else {
            return l_commandSplit[1];
        }
    }
}

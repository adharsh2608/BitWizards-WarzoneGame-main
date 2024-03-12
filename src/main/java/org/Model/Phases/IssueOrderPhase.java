package org.Model.Phases;

import org.Controller.GameEngine;
import org.Exceptions.InvalidCommand;
import org.Exceptions.InvalidState;
import org.Exceptions.MapInvalidException;
import org.Model.GameState;
import org.Model.Player;
import org.Utils.Command;
import org.Utils.LogLevel;
import org.Utils.SaveGame;
import org.Views.MapView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;

/**
 * This class handles the issue order phase of the GAME. Described the ISSUE ORDER STATE of the PHASE class
 */
public class IssueOrderPhase extends Phase  implements Serializable {


    public IssueOrderPhase(GameEngine l_gameEngine, GameState l_currentGameState) {
        super(l_gameEngine, l_currentGameState);
    }

    /**
     * Helper function to check if the player has more orders left
     * @param l_player : Provided player object
     */
    public void checkForMoreOrders(Player l_player, boolean p_isTournamentMode) throws IOException {
        if(!p_isTournamentMode && l_player.getPlayerStrategy().getPlayerStrategy().equalsIgnoreCase("Human")) {
            System.out.println("\nDo you still want to give order for player : " + l_player.getPlayerName()
                    + " in next turn ? \nPress Y for Yes or N for No");
            d_bufferReader = new BufferedReader(new InputStreamReader(System.in));
            String l_nextOrderCheck = d_bufferReader.readLine();
            if (l_nextOrderCheck.equalsIgnoreCase("Y") || l_nextOrderCheck.equalsIgnoreCase("N")) {
                l_player.setMoreOrderFlag(l_nextOrderCheck.equalsIgnoreCase("Y"));
            } else {
                System.out.println("Invalid input provided. ");
                checkForMoreOrders(l_player, p_isTournamentMode);
            }
        }
        else {
            l_player.setMoreOrderFlag(false);
        }
    }

    /**
     * Helper function to ask user for extra prompt for continuing the turn
     * @param p_player : Player on behalf of whom prompt should be asked.
     */
    public void askForOrder(Player p_player) throws IOException, InvalidCommand {
        String l_commandGenerated = p_player.getPlayerStrategy().createOrder(p_player, getCurrentGameState());
        if(l_commandGenerated == null)
            return;
        commandHandler(l_commandGenerated,p_player);
    }

    /**
     * Helper function which helps in accepting order from the player playing
     * @throws InvalidState : If the user enteres wrong input
     */
    public void orderAcceptingBlock(boolean p_isTournament) throws InvalidState, IOException, InvalidCommand {
        do {
            for (Player l_player : d_currentGameState.getPlayerController().getAllPlayers()) {

                if(l_player.getCountryCaptured().size() == 0){
                    l_player.setMoreOrderFlag(false);
                }

                if (l_player.getMoreOrderFlag() && !l_player.getPlayerName().equals("Neutral")) {
                    this.askForOrder(l_player);
                    this.checkForMoreOrders(l_player, p_isTournament);
                }
            }
        } while (d_currentGameState.getPlayerController().checkForMoreOrders());
        d_gameEngine.setOrderExecutionPhase();
    }

    /**
     * TO Print the Starting Options of the game.
     * @param p_isTournament
     */
    @Override
    public void printStartingOptions(boolean p_isTournament){
        if(!p_isTournament) {
            System.out.println("""
                                    
                    *********************************************************************************
                                        WARZONE GAME ( ISSUE ORDERS ) 
                    *********************************************************************************
                    01. deploy countryID numarmies
                    02. advance countrynamefrom countynameto numarmies
                    03. bomb countryID ( Only if the player has card BOMB )
                    04. blockade countryID ( Only if the player has card BLOCKADE )
                    05. airlift sourcecountryID targetcountryID numarmies ( Only if the player has card AIRLIFT )
                    06. negotiate playerID ( Only if the player has card NEGOTIATE )
                    07. showmap
                    08. savegame fileName
                    """);
        }
        else {
            System.out.println("""
                    *********************************************************************************
                                   Tournament Mode - WARZONE GAME ( ISSUE ORDERS )
                    *********************************************************************************
                    
                    *********************************************************************************
                                                    Issuing Orders
                    *********************************************************************************
                    """);
        }
    }

    /**
     * initPhase of the IssueOrderPhase.
     * @param p_isTournament
     * @throws InvalidState
     * @throws IOException
     * @throws InvalidCommand
     */
    @Override
    public void initPhase(boolean p_isTournament) throws InvalidState, IOException, InvalidCommand {
        printStartingOptions(p_isTournament);
        GameEngine.log("IssueOrderPhase::initPhase", LogLevel.HEADING, "Issue Order Phase");
        while (d_gameEngine.getCurrentPhase() instanceof IssueOrderPhase) {
            orderAcceptingBlock(p_isTournament);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void performDeployArmies(Command l_cmd, Player p_player) throws InvalidCommand, InvalidState {
        GameEngine.log("IssueOrderPhase::performDeployArmies",LogLevel.BASICLOG,p_player.getPlayerName() +
                " trying to perform Deploy order");
        p_player.createDeployOrder(l_cmd);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void performAdvanceArmies(Command l_cmd, Player p_player) throws InvalidCommand, InvalidState {
        GameEngine.log("IssueOrderPhase::performAdvanceArmies",LogLevel.BASICLOG,p_player.getPlayerName() +
                " trying to perform Advance Armies order");
        p_player.createAdvanceOrder(l_cmd,d_currentGameState);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void performCardHandle(Command l_cmd, Player p_player) throws InvalidCommand, InvalidState {
        if(p_player.getAllCards().contains(l_cmd.getMainOperation())){
            GameEngine.log("IssueOrderPhase::performCardHandle",LogLevel.BASICLOG,p_player.getPlayerName() +
                    " trying to perform Card based order");
            p_player.createCardOrder(l_cmd,d_currentGameState);
        }
        else {
            GameEngine.log("IssueOrderPhase::performCardHandle",LogLevel.BASICLOG,"Player doesn't " +
                    "have the before mentioned card.");
            throw new InvalidCommand("Player doesn't have the before mentioned card.");
        }
    }

    @Override
    public void performSaveGame(Command l_cmd, Player p_player) throws InvalidCommand, InvalidState, IOException {
        String l_saveGameFileName = l_cmd.handleSaveGame();

        //Thread.setDefaultUncaughtExceptionHandler(new ExceptionLogHandler(d_gameState));
        System.out.println(l_saveGameFileName);
        if (l_saveGameFileName == null || l_saveGameFileName.isEmpty()) {
            throw new InvalidCommand("Invalid savegame command provided");
        }
        SaveGame.saveGame(this, l_saveGameFileName);
        //d_gameEngine.setD_gameEngineLog("Game Saved Successfully to " + l_filename, "effect");
    }

    @Override
    public void performLoadGame(Command l_cmd, Player p_player) throws InvalidCommand, InvalidState, IOException {
        System.out.println("Invalid Operation.");
    }

    @Override
    public void performTournamentGame(String l_cmd) throws InvalidCommand, InvalidState {
        System.out.println("Invalid Operation.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performEditContinent(Command l_cmd, Player p_player) throws MapInvalidException, InvalidCommand, IOException {
        System.out.println("Invalid Operation..");
        askForOrder(p_player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performEditCountry(Command l_cmd, Player p_player) throws MapInvalidException, InvalidCommand, IOException {
        System.out.println("Invalid Operation..");
        askForOrder(p_player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performEditNeighbour(Command l_cmd, Player p_player) throws MapInvalidException, InvalidCommand {
        System.out.println("Invalid Operation..");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performLoadMap(Command l_cmd, Player p_player) throws InvalidCommand, IOException, MapInvalidException {
        System.out.println("Invalid Operation..");
        askForOrder(p_player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performShowMap(Player p_player) throws InvalidCommand, IOException {
        MapView l_showColorMap = new MapView(d_currentGameState);
        l_showColorMap.showMap();
        askForOrder(p_player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performValidateMap(Player p_player) throws MapInvalidException, InvalidState, IOException, InvalidCommand {
        System.out.println("Invalid Operation..");
        askForOrder(p_player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performSaveMap(Command l_cmd, Player p_player) throws IOException, MapInvalidException, InvalidCommand {
        System.out.println("Invalid Operation..");
        askForOrder(p_player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performEditMap(Command l_cmd, Player p_player) throws IOException, MapInvalidException, InvalidCommand {
        System.out.println("Invalid Operation..");
        askForOrder(p_player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performAssignCountries(Command p_command, Player p_player, boolean isTournamentMode, GameState p_gameState) throws InvalidCommand, MapInvalidException, InvalidState, IOException {
        System.out.println("Invalid Operation");
        askForOrder(p_player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performCreatePlayers(Command l_cmd, Player p_player) throws InvalidCommand, IOException {
        System.out.println("Invalid Operation");
        askForOrder(p_player);
    }


}

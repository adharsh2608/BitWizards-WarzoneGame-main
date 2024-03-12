package org.Model.Phases;

import org.Controller.GameEngine;
import org.Controller.PlayerController;
import org.Exceptions.InvalidCommand;
import org.Exceptions.InvalidState;
import org.Exceptions.MapInvalidException;
import org.Model.GameState;
import org.Model.Player;
import org.Model.PlayerStrategies.AggressiveStrategy;
import org.Model.PlayerStrategies.BenevolentStrategy;
import org.Model.PlayerStrategies.CheaterStrategy;
import org.Model.PlayerStrategies.RandomStrategy;
import org.Utils.Command;
import org.Utils.LoadGame;
import org.Utils.LogLevel;
import org.Model.Map;
import org.Utils.SaveGame;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class denotes the start up phase STATE of the game
 */
public class StartUpPhase extends Phase implements Serializable {

    public StartUpPhase(GameEngine l_gameEngine, GameState l_currentGameState) {
        super(l_gameEngine, l_currentGameState);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performEditContinent(Command l_cmd, Player p_player) throws MapInvalidException, InvalidCommand {
        ArrayList<String> l_addContinents = l_cmd.handleEditContinent().get("add");
        ArrayList<String> l_removeContinents = l_cmd.handleEditContinent().get("remove");
        if(!l_addContinents.isEmpty()){
            for(int i = 0 ; i < l_addContinents.size(); i += 2){
                this.getCurrentGameState().getCurrentMap().addContinent(l_addContinents.get(i),
                        Integer.parseInt(l_addContinents.get(i+1)));
            }
        }
        if(!l_removeContinents.isEmpty()){
            for(String l_continent : l_removeContinents){
                this.getCurrentGameState().getCurrentMap().removeContinent(l_continent);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performEditCountry(Command l_cmd, Player p_player) throws MapInvalidException, InvalidCommand {
        ArrayList<String> l_addCountries = l_cmd.handleEditCountry().get("add");
        ArrayList<String> l_removeCountries = l_cmd.handleEditCountry().get("remove");
        if(!l_addCountries.isEmpty()){
            for(int i = 0 ; i < l_addCountries.size(); i += 2){
                this.getCurrentGameState().getCurrentMap().addCountry(l_addCountries.get(i),
                        l_addCountries.get(i+1));
            }
        }
        if(!l_removeCountries.isEmpty()){
            for(String l_country : l_removeCountries){
                this.getCurrentGameState().getCurrentMap().removeCountry(l_country);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performEditNeighbour(Command l_cmd, Player p_player) throws MapInvalidException, InvalidCommand {
        ArrayList<String> l_addNeighbours = l_cmd.handleEditNeighbour().get("add");
        ArrayList<String> l_removeNeighbours = l_cmd.handleEditNeighbour().get("remove");
        if(!l_addNeighbours.isEmpty()){
            for(int i = 0 ; i < l_addNeighbours.size(); i += 2){
                this.getCurrentGameState().getCurrentMap().addNeighbour(l_addNeighbours.get(i),
                        l_addNeighbours.get(i+1));
            }
        }
        if(!l_removeNeighbours.isEmpty()){
            for(int i = 0 ; i < l_removeNeighbours.size(); i += 2){
                this.getCurrentGameState().getCurrentMap().removeNeighbour(l_removeNeighbours.get(i),
                        l_removeNeighbours.get(i+1));
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performLoadMap(Command l_cmd, Player p_player) throws InvalidCommand, FileNotFoundException, MapInvalidException {
        GameEngine.log("StartUpPhase::peformLoadMap",LogLevel.SUBHEADING,"MAP section starts");
        getCurrentGameState().getCurrentMap().loadMap(l_cmd.handleLoadMap());
        if(Map.d_isMapLoaded){
            System.out.println("Map loaded successfully!");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performShowMap(Player p_player) throws InvalidCommand {
        getCurrentGameState().getCurrentMap().showMap();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performValidateMap(Player p_player) throws MapInvalidException, InvalidState {
        if(!Map.d_isMapLoaded)
            throw new InvalidState("Please load the map first.");
        Boolean l_result = getCurrentGameState().getCurrentMap().validateMap();
        if(l_result){
            GameEngine.log("StartUpPhase::performValidateMap",LogLevel.BASICLOG,"Map Validation " +
                    "Successful.");
            System.out.println("Map Validation Successful!");
        } else{
            GameEngine.log("StartUpPhase::performValidateMap",LogLevel.BASICLOG,"Map Validation" +
                    " Unsuccessful.");
            System.out.println("Map is Invalid!");
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performSaveMap(Command l_cmd , Player p_player) throws IOException, MapInvalidException, InvalidCommand {
        getCurrentGameState().getCurrentMap().saveMap(l_cmd.handleSaveMap());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performEditMap(Command l_cmd, Player p_player) throws IOException, MapInvalidException, InvalidCommand {
        GameEngine.log("StartUpPhase::performMapEdit",LogLevel.SUBHEADING,"MAP section starts");
        getCurrentGameState().getCurrentMap().editMap(l_cmd.handleEditMap());

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performAssignCountries(Command p_command, Player p_player, boolean p_isTournamentMode, GameState p_gameState) throws InvalidCommand, MapInvalidException, InvalidState {
        // Two mandatory checks has to be done here
        // 1. If the map is loaded
        // 2. If the players are added
        if(p_isTournamentMode){
            d_currentGameState = p_gameState;
            d_gameEngine.setIsTournament(true);
            d_gameEngine.setGameState(p_gameState);
        }
        if(!Map.d_isMapLoaded){
            GameEngine.log("StartUpPhase::performAssignCountries",LogLevel.BASICLOG,"Map not " +
                    "loaded, assigncountries failed");
            throw new MapInvalidException("Please load the map before peforming assigncountries");
        }

        if(d_currentGameState.getPlayerController().getAllPlayers().isEmpty()){
            GameEngine.log("StartUpPhase::performAssignCountries",LogLevel.BASICLOG,"No players" +
                    " added, assigncountries failed");
            throw new InvalidState("Please add the players before performing assigncountries");
        }

        if(d_currentGameState.getPlayerController().getAllPlayers().size() > d_currentGameState.getCurrentMap().getAllCountriesAsList().size()){
            GameEngine.log("StartUpPhase::performAssignCountries", LogLevel.BASICLOG," Game should "
                    + "have more countries than players ");
            throw new InvalidState("Game should have less players than the number of countries.");
        }

        if(d_currentGameState.getPlayerController().getAllPlayers().size() < 2){
            GameEngine.log("StartUpPhase::performAssignCountries",LogLevel.BASICLOG,"Number of " +
                    "players are less than 1, assigncountries failed");
            throw new InvalidState("We need at least two players , to play this game. Kindly add more players....");
        }

        d_currentGameState.getPlayerController().assignCountries(d_currentGameState);
        d_currentGameState.getPlayerController().assignArmies(d_currentGameState);
        GameEngine.log("StartUpPhase::performAssignCountries",LogLevel.BASICLOG," changing the " +
                "phase to Issue Orde Phase");
        d_gameEngine.setIssueOrderPhase(p_isTournamentMode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performCreatePlayers(Command l_cmd, Player p_player) throws InvalidCommand {
        if(!Map.d_isMapLoaded){
            System.out.println("Please load the map before creating players ...");
            return;
        }
        GameEngine.log("StartUpPhase::performCreatePlayers",LogLevel.SUBHEADING,"Player Section Starts");
        d_currentGameState.getPlayerController().addRemovePlayers(l_cmd.handleAddAndRemovePlayer());
        // Player validity check happens before the assigncountries

    }

    /**
     * {@inheritDoc}
     */
    public void printStartingOptions(boolean p_isTournament){
        System.out.println("""
                *********************************************************************************
                                    WARZONE GAME ( START UP PHASE ) 
                *********************************************************************************
                01. editcontinent -add continentID continentvalue -remove continentID
                02. editcountry -add countryID continentID -remove countryID
                03. editneighbor -add countryID neighborcountryID -remove countryID neighborcountryID
                04. showmap
                05. savemap filename
                06. editmap filename
                07. validatemap
                08. gameplayer -add playername -remove playername
                09. assigncountries
                10. loadmap
                11. tournament -M listOfMapFiles -P listOfPlayerStrategies -G numberOfGames -D noOfTurnsPerGame
                12. savegame fileName
                13. loadgame fileName
                """);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initPhase(boolean p_isTournament) throws InvalidState, IOException {
        GameEngine.log("StartUpPhase : initPhase", LogLevel.HEADING,"Start Up Phase");
        printStartingOptions(p_isTournament);
        while(d_gameEngine.getCurrentPhase() instanceof StartUpPhase){
            System.out.println(System.lineSeparator() + "Enter game commands or type exit for quitting....");
            String l_commandEntered = this.d_bufferReader.readLine();
            GameEngine.log("StartUpPhase::initPhase",LogLevel.BASICLOG,"Command Entered \""+ l_commandEntered + "\"");
            commandHandler(l_commandEntered,null);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void performDeployArmies(Command l_cmd, Player p_player) throws InvalidCommand {
        System.out.println("Invalid Operation");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void performAdvanceArmies(Command l_cmd, Player p_player) throws InvalidCommand, InvalidState {
        System.out.println("Invalid Operation");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void performCardHandle(Command l_cmd, Player p_player) throws InvalidCommand, InvalidState {
        System.out.println("Invalid Operation");
    }

    @Override
    public void performSaveGame(Command l_cmd, Player p_player) throws InvalidCommand, InvalidState, IOException {
        System.out.println("I've been invoked");
        String l_saveGameFileName = l_cmd.handleSaveGame();

        //Thread.setDefaultUncaughtExceptionHandler(new ExceptionLogHandler(d_gameState));

        if (l_saveGameFileName == null || l_saveGameFileName.isEmpty()) {
            throw new InvalidCommand("Invalid savegame command provided");
        }
        SaveGame.saveGame(this, l_saveGameFileName);
        //d_gameEngine.setD_gameEngineLog("Game Saved Successfully to " + l_filename, "effect");
    }

    @Override
    public void performLoadGame(Command l_cmd, Player p_player) throws InvalidCommand, InvalidState, IOException {
        String l_loadGameFileName = l_cmd.handleLoadGame();

        //Thread.setDefaultUncaughtExceptionHandler(new ExceptionLogHandler(d_gameState));

        if (l_loadGameFileName == null || l_loadGameFileName.isEmpty()) {
            throw new InvalidCommand("Invalid loadgame command provided");
        }
        try{
            Phase l_phase= LoadGame.loadGame(l_loadGameFileName);
            System.out.println("Game loaded");
            this.d_gameEngine.loadPhase(l_phase);
        } catch (Exception l_e) {
            l_e.printStackTrace();
        }
    }

    @Override
    public void performTournamentGame(String l_cmd) throws InvalidCommand, InvalidState, MapInvalidException {
        Command l_command = new Command(l_cmd);
        HashMap<String, ArrayList<String>> l_commandOptions = l_command.handleTournamentCommand(l_cmd);
        List<GameState> l_gameStates = new ArrayList<>();
        for(int i = 0; i < Integer.parseInt(l_commandOptions.get("G").get(0)); i++){
            for(int j = 0; j < l_commandOptions.get("M").size(); j++){
                System.out.println("Game Number: " + (i+1) + "." + (j+1) + " Started");
                GameState l_currentGameState = createGameState(l_commandOptions.get("P"), l_commandOptions.get("M").get(j).trim(), Integer.parseInt(l_commandOptions.get("D").get(0)));
                performAssignCountries(new Command("assigncountries"), null, true, l_currentGameState);
                l_gameStates.add(l_currentGameState);
                System.out.println("Game Number: " + (i+1) + "." + (j+1) + " Ended");
            }
        }
        System.out.println("Tournament Completed!\nPrinting Results of the Tournament...\n");
        printResult(l_gameStates, l_commandOptions.get("M").size(), Integer.parseInt(l_commandOptions.get("G").get(0)));
    }

    private GameState createGameState(ArrayList<String> p_players, String p_mapName, int p_noOfTurns) throws MapInvalidException {

        //Initialize GameState
        GameState l_gameState = new GameState();

        //Create Player Strtegies
        PlayerController l_playerController = new PlayerController();
        for(String l_strategy : p_players){
            Player l_tempPlayer = new Player(l_strategy);
            switch (l_strategy){
                case "Aggressive" -> {
                    l_tempPlayer.setPlayerStrategy(new AggressiveStrategy());
                    l_playerController.addPlayer(l_tempPlayer);
                }
                case "Benevolent" -> {
                    l_tempPlayer.setPlayerStrategy(new BenevolentStrategy());
                    l_playerController.addPlayer(l_tempPlayer);
                }
                case "Cheater" -> {
                    l_tempPlayer.setPlayerStrategy(new CheaterStrategy());
                    l_playerController.addPlayer(l_tempPlayer);
                }
                case "Random" -> {
                    l_tempPlayer.setPlayerStrategy(new RandomStrategy());
                    l_playerController.addPlayer(l_tempPlayer);
                }
            }
        }

        //LoadMap
        Map l_map = new Map();
        System.out.println(p_mapName);
        l_map.loadMap(p_mapName);

        //Setup the game
        l_gameState.setPlayerController(l_playerController);
        l_gameState.setCurrentMap(l_map);
        l_gameState.setMaxnumberofturns(p_noOfTurns);
        l_gameState.setNumberOfTurnsLeft(p_noOfTurns);
        return l_gameState;
    }

    private void printResult(List<GameState> p_gameStates, int p_mapCount, int p_gameCount){
        ArrayList<String> l_results = new ArrayList<>();
        int x = 0;
        for(int i = 0; i < p_mapCount+1; i++){
            for(int j = 0; j < p_gameCount+1; j++){
                if(i == 0 && j == 0){
                    l_results.add("Results");
                }
                else if (i == 0) {
                    l_results.add("Game" + j);
                }
                else if(j == 0) {
                    l_results.add("Map" + i);
                }
                else {
                    l_results.add(p_gameStates.get(x).getWinnerPlayer());
                    x++;
                }
            }
        }

        x = 0;
        for(int i = 0; i < p_mapCount+1; i++) {
            // Print row separator
            for (int j = 0; j < p_gameCount + 1; j++) {
                System.out.print("--------");
            }
            System.out.println();
            for (int j = 0; j < p_gameCount + 1; j++) {
                System.out.print(l_results.get(x) + "\t");
                x++;
            }
            System.out.println("|");
        }
    }
}

package org.Model.Phases;

import org.Controller.GameEngine;
import org.Controller.PlayerController;
import org.Exceptions.InvalidCommand;
import org.Exceptions.InvalidState;
import org.Exceptions.MapInvalidException;
import org.Model.Continent;
import org.Model.Country;
import org.Model.GameState;
import org.Model.Player;
import org.Model.PlayerStrategies.AggressiveStrategy;
import org.Utils.Command;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

public class StartUpPhaseTest {
    GameEngine d_gameEngine;
    GameState d_gameState;
    Phase d_phase;

    @Before
    public void beforeStartUpPhaseTest(){
        d_gameEngine = new GameEngine();
        d_gameState = new GameState();
        d_phase = new StartUpPhase(d_gameEngine,d_gameState);
        GameEngine.setLoggerContext("testLog.txt");
    }

    @Test
    public void testPerformConquestLoadMap(){
        Command l_loadMapCommand  = new Command("loadmap asia.map");
        try{
            d_phase.performLoadMap(l_loadMapCommand,null);
            assertEquals(48, d_gameState.getCurrentMap().getAllCountriesAsList().size());
        } catch(MapInvalidException | FileNotFoundException | InvalidCommand ex){
            assertNull(ex);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    @Test
    public void testPerformDefaultLoadMap(){
        Command l_loadMapCommand  = new Command("loadmap canada.map");
        try{
            d_phase.performLoadMap(l_loadMapCommand,null);
            assertEquals(31, d_gameState.getCurrentMap().getAllCountriesAsList().size());
        } catch(MapInvalidException | InvalidCommand | IOException ex){
            assertNull(ex);
        }
    }

    @Test
    public void testPerformDefaultLoadMapInvalid(){
        Command l_loadMapCommand  = new Command("loadmap Invalid_candaMap1.map");
        try{
            d_phase.performLoadMap(l_loadMapCommand,null);
        } catch(MapInvalidException | InvalidCommand | IOException ex){
            assertEquals(ex.getMessage(), "Invalid Map Provided.");
        }
    }



    @Test
    public void testPerformConquestLoadMapInvalid(){
        Command l_loadMapCommand  = new Command("loadmap asia_invalid.map");
        try{
            d_phase.performLoadMap(l_loadMapCommand,null);
        } catch(MapInvalidException | InvalidCommand | IOException ex){
            assertEquals(ex.getMessage(), "Invalid Map Provided.");
        }
    }


//    @Test
//    public void testCreatePlayers(){
//        Command l_playerCommand  = new Command("gameplayer -add p1 -add p2");
//        Command l_loadMapCommand  = new Command("loadmap canada.map");
//        try {
//            d_phase.performLoadMap(l_loadMapCommand,null);
//            d_phase.performCreatePlayers(l_playerCommand, null);
//            assertEquals(d_gameState.getPlayerController().getAllPlayers().size() , 2);
//        } catch(InvalidCommand | MapInvalidException | IOException ex){
//            assertNull(ex);
//        }
//
//    }



    @Test
    public void testAssignCountries(){
        Command l_loadMapCommand  = new Command("loadmap canada.map");
        PlayerController l_playerController = new PlayerController();
        Player l_player = new Player("p1");
        l_player.setPlayerStrategy(new AggressiveStrategy());
        l_playerController.addPlayer(l_player);
        l_player = new Player("p2");
        l_player.setPlayerStrategy(new AggressiveStrategy());
        l_playerController.addPlayer(l_player);

        try{
            GameEngine d_gameEngineMock = mock(GameEngine.class);
            GameState d_gameStateNew = new GameState();
            Phase d_newPhase = new StartUpPhase(d_gameEngineMock,d_gameStateNew);
            d_newPhase.performLoadMap(l_loadMapCommand,null);
            d_gameStateNew.setPlayerController(l_playerController);
            doNothing().when(d_gameEngineMock).setIssueOrderPhase(false);
            d_newPhase.performAssignCountries(null, null, false, null);

            Boolean checkCondition =
                    d_gameStateNew.getPlayerController().getPlayerByName("p1").getCountryCaptured().size() != 0 &&
                            d_gameStateNew.getPlayerController().getPlayerByName("p2").getCountryCaptured().size() != 0;
            assertTrue(checkCondition);
        } catch(InvalidCommand | MapInvalidException | InvalidState | IOException ex){
            assertNull(ex);
        }
    }

    @Test
    public void testPerformEditCountry(){
        Command l_editMapCommand = new Command("editmap testCountry.map");
        Command l_editContinentCommand = new Command("editcontinent -add Asia 4");
        Command l_editCountryCommand = new Command("editcountry -add India Asia -add China Asia");
        Command l_editNeighbourCommand = new Command("editneighbor -add India China");

        try{
            d_phase.performEditMap(l_editMapCommand,null);
            d_phase.performEditContinent(l_editContinentCommand,null);
            d_phase.performEditCountry(l_editCountryCommand,null);
            d_phase.performEditNeighbour(l_editNeighbourCommand,null);

            List<Country> countriesMade = d_gameState.getCurrentMap().getAllCountriesAsList();
            assertEquals("India",countriesMade.get(0).getCountryName());
            assertEquals("China",countriesMade.get(1).getCountryName());
        }catch(IOException | MapInvalidException | InvalidCommand  ex){
            assertNull(ex);
        }
    }

    @Test
    public void testPerformEditContinent(){
        Command l_editMapCommand = new Command("editmap testContinent.map");
        Command l_editContinentCommand = new Command("editcontinent -add Asia 4");
        Command l_editCountryCommand = new Command("editcountry -add India Asia -add China Asia");
        Command l_editNeighbourCommand = new Command("editneighbor -add India China");

        try{
            d_phase.performEditMap(l_editMapCommand,null);
            d_phase.performEditContinent(l_editContinentCommand,null);
            d_phase.performEditCountry(l_editCountryCommand,null);
            d_phase.performEditNeighbour(l_editNeighbourCommand,null);

            List<Continent> continentsMade = d_gameState.getCurrentMap().getAllContinentsList();
            assertEquals("Asia",continentsMade.get(0).getContinentName());
        }catch(IOException | MapInvalidException | InvalidCommand  ex){
            assertNull(ex);
        }
    }

//    @Test
//    public void testPerformEditNeighbors(){
//        Command l_editMapCommand = new Command("editmap testNeighbor.map");
//        Command l_editContinentCommand = new Command("editcontinent -add Asia 4");
//        Command l_editCountryCommand = new Command("editcountry -add India Asia -add China Asia");
//        Command l_editNeighbourCommand = new Command("editneighbor -add India China");
//
//        try{
//            d_phase.performEditMap(l_editMapCommand,null);
//            d_phase.performEditContinent(l_editContinentCommand,null);
//            d_phase.performEditCountry(l_editCountryCommand,null);
//            d_phase.performEditNeighbour(l_editNeighbourCommand,null);
//
//            Integer neighBourCountry = d_gameState.getCurrentMap().getCountryByName("India").getNeighbourCountries().get(0);
//            Country neighbourCountry = d_gameState.getCurrentMap().getCountryById(neighBourCountry);
//
//            assertEquals("China",neighbourCountry.getCountryName());
//        }catch(IOException | MapInvalidException | InvalidCommand  ex){
//            assertNull(ex);
//        }
//    }
//    @Test
//    public void checkValidParseOfTournamentCommand() throws InvalidCommand, MapInvalidException {
//        String l_TournamentCommand = "tournament -M canada.map -P Aggressive Benevolent -G 2 -D 3";
//        Command l_command=new Command(l_TournamentCommand);
//        HashMap<String, ArrayList<String>> l_commandOptions = l_command.handleTournamentCommand(l_TournamentCommand);
//        String l_str[]=String.valueOf(l_commandOptions.get(0)).split(" ");
//
//        assertEquals(2,l_str[14] );
//        assertEquals(3, l_str[18]);
//        assertEquals(1, l_commandOptions.get("M").size());
//        //assertEquals("Australia.map", d_Options.getMap().get(0));
//
//    }

    @Test
    public void testPerformSaveMapAsConquest(){
        Command l_editMapCommand = new Command("editmap cqAsia.map");
        Command l_editContinentCommand = new Command("editcontinent -add Asia 4");
        Command l_editCountryCommand = new Command("editcountry -add India Asia -add China Asia");
        Command l_editNeighbourCommand = new Command("editneighbor -add India China -add China India");

        try{
            d_phase.performEditMap(l_editMapCommand,null);
            d_phase.performEditContinent(l_editContinentCommand,null);
            d_phase.performEditCountry(l_editCountryCommand,null);
            d_phase.performEditNeighbour(l_editNeighbourCommand,null);

            d_gameState.getCurrentMap().showMap();
            d_gameState.getCurrentMap().saveMap("cqAsia.map");
        }catch(IOException | MapInvalidException | InvalidCommand  ex){

        }
    }

    @Test
    public void testPerformDefaultShowMap(){
        Command l_loadMapCommand  = new Command("loadmap canada.map");
        try {
            d_phase.performLoadMap(l_loadMapCommand, null);
            d_phase.performShowMap(null);
            assertTrue(true);
        } catch(InvalidCommand | FileNotFoundException | MapInvalidException ex){
            assertNull(ex);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void testPerformConquestShowMap(){
        Command l_loadMapCommand  = new Command("loadmap asia.map");
        try {
            d_phase.performLoadMap(l_loadMapCommand, null);
            d_phase.performShowMap(null);
            assertTrue(true);
        } catch(InvalidCommand | FileNotFoundException | MapInvalidException ex){
            assertNull(ex);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    /**
     * Testing if the command entered is valid or not
     */
    @Test
    public void test_validCommand_getRootCommand() throws InvalidCommand {
        Command l_command = new Command("editcontinent -add continentID continentvalue");
        String l_rootCommand = l_command.getMainOperation();

        assertEquals("editcontinent",l_rootCommand);
    }

    /**
     * Testing the single word commands
     */
    @Test
    public void test_singleWord_getRootCommand() throws InvalidCommand {
        Command l_command = new Command("validatemap");
        String l_rootCommand = l_command.getMainOperation();

        assertEquals("validatemap", l_rootCommand);
    }

    /**
     * testing the commands
     */
    @Test
    public void test_noFlagCommand_getRootCommand() throws InvalidCommand {
        Command l_command = new Command("loadmap abc.txt");
        String l_rootCommand = l_command.getMainOperation();

        assertEquals("loadmap", l_rootCommand);
    }

}
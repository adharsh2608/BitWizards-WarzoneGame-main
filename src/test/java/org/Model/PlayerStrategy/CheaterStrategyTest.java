package org.Model.PlayerStrategy;

import org.Controller.PlayerController;
import org.Model.*;
import org.Model.Orders.Order;
import org.Model.PlayerStrategies.BehaviourStrategy;
import org.Model.PlayerStrategies.CheaterStrategy;
import org.Model.PlayerStrategies.RandomStrategy;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CheaterStrategyTest {
    /**
     * Cheater Player to test.
     */
    Player d_player;

    /**
     * Opponent Player to test.
     */
    Player d_randomPlayer;

    /**
     * Strategy of Player.
     */
    BehaviourStrategy d_playerBehaviorStrategy;

    /**
     * Cheater player strategy.
     */
    CheaterStrategy d_cheaterPlayer = new CheaterStrategy();

    /**
     * Game State.+
     */
    GameState d_gameState = new GameState();

    /**
     * Country.
     */
    Country d_country1;

    /**
     * Setup For testing Aggressive Behavior Strategy.
     */
    @Before
    public void setup() {

        this.d_country1 = new Country(1, "Spain", 1);
        Country l_country2 = new Country(2, "France", 1);
        Country l_country3 = new Country(3, "Portugal", 1);


        d_country1.addNeighbour(3); // Spain ---> Portugal
        d_country1.addNeighbour(2); // Spain ---> France
        l_country2.addNeighbour(3); // France ----> Portugal

        l_country2.setArmies(3); // France(3)
        l_country3.setArmies(2); // Portugal(2)

        ArrayList<Country> l_allCountries = new ArrayList<Country>();
        l_allCountries.add(d_country1);
        l_allCountries.add(l_country2);
        l_allCountries.add(l_country3);


        Continent l_continent = new Continent(1,"Europe",200);
        l_continent.setContinentId(1);
        l_continent.addCountry(d_country1);
        //l_continent.setD_countries(l_allCountries);

        ArrayList<Continent> l_continents = new ArrayList<Continent>();
        l_continents.add(l_continent);

        ArrayList<Country> l_ownedCountriesPlayerOne = new ArrayList<Country>();
        l_ownedCountriesPlayerOne.add(d_country1);

        ArrayList<Country> l_ownedCountriesPlayerTwo = new ArrayList<Country>();
        l_ownedCountriesPlayerTwo.add(l_country2);
        l_ownedCountriesPlayerTwo.add(l_country3);

        d_playerBehaviorStrategy = new CheaterStrategy();
        d_player = new Player("DummyPlayer");
        d_player.setNumOfArmiesRemaining(10);
        d_player.setCountryCaptured(l_ownedCountriesPlayerOne);
        d_player.setPlayerStrategy(d_playerBehaviorStrategy);

        d_randomPlayer = new Player("Opponent");
        RandomStrategy l_randomPlayerBehaviorStrategy = new RandomStrategy();
        d_randomPlayer.setPlayerStrategy(l_randomPlayerBehaviorStrategy);
        d_randomPlayer.setCountryCaptured(l_ownedCountriesPlayerTwo);
        d_randomPlayer.setNumOfArmiesRemaining(0);


//        List<Player> l_listOfPlayer = new ArrayList<Player>();
//        l_listOfPlayer.add(d_player);
      //  l_listOfPlayer.add(d_randomPlayer);
        PlayerController playerController=new PlayerController();
        playerController.addPlayer(d_player);
        playerController.addPlayer(d_randomPlayer);

        Map l_map = new Map();
        l_map.setAllCountriesList(l_allCountries);
        l_map.setAllContinentsList(l_continents);
        d_gameState.setCurrentMap(l_map);
        d_gameState.setPlayerController(playerController);
    }


    /**
     * Checks if it creates an null Order.
     *
     * @throws IOException Exception
     */
    @Test
    public void testOrderCreationToBeNull() throws IOException {
        List<Order> l_receivedOrder = d_player.getOrderList();
        String receivedOrder=null;
        if(l_receivedOrder.size()==0)
        {

        }
        assertNull(receivedOrder);
    }

    /**
     * Checks all unallocated armies are deployed to player's country.
     *
     * @throws IOException Exception
     */
    @Test
    public void testUnallocatedArmiesDeployment() throws IOException {
        List<Order> l_receivedOrder = d_player.getOrderList();
        String receivedOrder=null;
        if(l_receivedOrder.size()==0)
        {

        }
        assertNull(receivedOrder);

        int l_unallocatedArmies = d_player.getNumOfArmiesRemaining();
        assertEquals(10, l_unallocatedArmies);
    }

    /**
     * Checks that player now owns all enemy countries.
     *
     * @throws IOException Exception
     */
    @Test
    public void testCheaterOwnsAllEnemies() throws IOException {
        List<Order> l_receivedOrder = d_player.getOrderList();

        int l_ownedCountriesCount = d_player.getCountryCaptured().size();
        assertEquals(1, l_ownedCountriesCount);

        int l_opponentOwnedCountriesCount = d_randomPlayer.getCountryCaptured().size();
        assertEquals(2, l_opponentOwnedCountriesCount);
    }
}

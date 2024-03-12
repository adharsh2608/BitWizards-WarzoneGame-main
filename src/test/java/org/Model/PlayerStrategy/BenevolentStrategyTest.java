package org.Model.PlayerStrategy;

import org.Controller.PlayerController;
import org.Model.Country;
import org.Model.GameState;
import org.Model.Map;
import org.Model.Player;
import org.Model.PlayerStrategies.BehaviourStrategy;
import org.Model.PlayerStrategies.BenevolentStrategy;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class BenevolentStrategyTest {

    /**
     * Benevolent Player to test.
     */
    Player d_player;

    /**
     * Strategy of Player.
     */
    BehaviourStrategy d_playerBehaviorStrategy;

    /**
     * Benevolent player strategy.
     */
    BenevolentStrategy d_benevolentPlayer = new BenevolentStrategy();

    /**
     * Game State.
     */
    GameState d_gameState = new GameState();

    /**
     * Country India.
     */
    Country d_country1;

    /**
     * Setup For testing Benevolent Behavior Strategy.
     */
    @Before
    public void setup() {
        this.d_country1 = new Country(1, "India", 1);
        Country l_country2 = new Country(1, "China", 1);
        Country l_country3 = new Country(1, "Pakistan", 1);

        l_country2.setCountryId(3);
        d_country1.addNeighbour(3);

        l_country3.setCountryId(2);
        d_country1.addNeighbour(2);

        this.d_country1.setArmies(10);
        l_country2.setArmies(3);
        l_country3.setArmies(2);

        ArrayList<Country> l_list = new ArrayList<Country>();
        l_list.add(d_country1);
        l_list.add(l_country2);
        l_list.add(l_country3);

        d_playerBehaviorStrategy = new BenevolentStrategy();
        d_player = new Player("Avneet");
        d_player.setCountryCaptured(l_list);
        d_player.setPlayerStrategy(d_playerBehaviorStrategy);
        d_player.setNumOfArmiesRemaining(5);

//        List<Player> l_listOfPlayer = new ArrayList<Player>();
//        l_listOfPlayer.add(d_player);
        PlayerController playerController=new PlayerController();
        playerController.addPlayer(d_player);

        Map l_map = new Map();
        l_map.setAllCountriesList(l_list);
        d_gameState.setCurrentMap(l_map);
        d_gameState.setPlayerController(playerController);

    }

    /**
     * Check if benevolent player deploy armies on weakest country or not.
     */
    @Test
    public void testWeakestCountry() {
        assertEquals("Pakistan", d_benevolentPlayer.getWeakestCountry(d_player).getCountryName());
    }

    /**
     * Check if benevolent player attacks to weakest neighbor or not.
     */
    @Test
    public void testWeakestNeighbor() {
        assertEquals("Pakistan", d_benevolentPlayer.getWeakestNeighbor(d_country1, d_gameState, d_player).getCountryName());
    }

}


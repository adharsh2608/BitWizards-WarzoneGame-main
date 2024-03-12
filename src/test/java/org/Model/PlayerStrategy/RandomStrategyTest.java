package org.Model.PlayerStrategy;

import org.Controller.PlayerController;
import org.Model.*;
import org.Model.PlayerStrategies.BehaviourStrategy;
import org.Model.PlayerStrategies.RandomStrategy;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class RandomStrategyTest {

    /**
     * Random Player to test.
     */
    Player d_player;

    /**
     * Strategy of Player.
     */
    BehaviourStrategy d_playerBehaviorStrategy;

    /**
     * Game State.
     */
    GameState d_gameState = new GameState();

    /**
     * Setup For testing Random Behavior Strategy.
     */
    @Before
    public void setup() {

        Continent l_continent = new Continent(1,"Africa",200);

        Country l_country1 = new Country(1, "India", 1);
        Country l_country2 = new Country(2, "China", 1);
        ArrayList<Country> l_list = new ArrayList<Country>();
        l_list.add(l_country1);
        l_list.add(l_country2);

        d_playerBehaviorStrategy = new RandomStrategy();
        d_player = new Player();
        d_player.setCountryCaptured(l_list);
        d_player.setPlayerStrategy(d_playerBehaviorStrategy);
        d_player.setNumOfArmiesRemaining(1);
       PlayerController d_playerController=new PlayerController();
       d_playerController.addPlayer(d_player);

        Map l_map = new Map();
        l_map.setAllCountriesList(l_list);
        d_gameState.setCurrentMap(l_map);
        d_gameState.setPlayerController(d_playerController);
    }

    /**
     * Checks if it creates an Order String and first order is deploy.
     *
     * @throws IOException Exception
     */
    @Test
    public void testOrderCreation() throws IOException {
        assertEquals(d_playerBehaviorStrategy.createOrder(d_player,d_gameState).split(" ")[0], "deploy");
    }





}

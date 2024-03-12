package org.Model;

import org.Controller.PlayerController;

import java.io.Serializable;

/**
 * Common data structure of the game , contains the state of the game.
 * Contains the common functions and data structure to store the MAP , PLAYERS and PHASES etc.
 */
public class GameState implements Serializable {

    /**
     * Stores the current map on which players are playing
     */
    private Map d_currentMap = new Map();

    /**
     * Stores the current player controller , which contains all the player based operations
     */
    private PlayerController d_playerController = new PlayerController();

    private String d_winnerPlayer;

    public String getWinnerPlayer() {
        return d_winnerPlayer;
    }

    public void setWinnerPlayer(String p_winnerPlayer) {
        this.d_winnerPlayer = p_winnerPlayer;
    }
    /**
     * Getter for the current map
     * @return Map : the current map
     */
    public Map getCurrentMap(){return this.d_currentMap;}

    /**
     * Setter for the current map
     * @param l_currentMap : The map which needs to be set
     */
    public void setCurrentMap(Map l_currentMap){ this.d_currentMap = l_currentMap; }

    /**
     * Getter for te player controller.
     * @return PlayerController : Contains the player based features and data members
     */
    public PlayerController getPlayerController(){return this.d_playerController; }

    public void setPlayerController(PlayerController d_playerController) {
        this.d_playerController = d_playerController;
    }

    int d_maxnumberofturns = 0;

    int d_numberOfTurnsLeft = 0;

    public int getD_maxnumberofturns() {
        return d_maxnumberofturns;
    }

    public void setMaxnumberofturns(int d_maxnumberofturns) {
        this.d_maxnumberofturns = d_maxnumberofturns;
    }

    public int getD_numberOfTurnsLeft() {
        return d_numberOfTurnsLeft;
    }

    public void setNumberOfTurnsLeft(int d_numberOfTurnsLeft) {
        this.d_numberOfTurnsLeft = d_numberOfTurnsLeft;
    }

    /**
     * Helper function to generate rando numbers
     * @param p_max : Maximum range
     * @param p_min : Minimum range
     * @return Integer : Number generated
     */
    public int getRandomInteger(int p_max, int p_min){
        return ((int)(Math.random()*(p_max - p_min)) + p_min);
    }

}

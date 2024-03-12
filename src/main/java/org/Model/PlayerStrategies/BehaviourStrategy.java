package org.Model.PlayerStrategies;

import org.Model.GameState;
import org.Model.Player;

import java.io.IOException;
import java.io.Serializable;

/**
 * An abstract class representing the behavior strategy for a player in a game.
 * This class provides methods for creating different types of orders based on the player's strategy.
 */
public abstract class BehaviourStrategy implements Serializable {

    /**
     * The player associated with this behavior strategy.
     */
    Player d_player;

    /**
     * The game state associated with this behavior strategy.
     */
    GameState d_gameState;

    /**
     * Creates a specific type of order based on the player's strategy.
     *
     * @param p_player      The player for whom the order is created.
     * @param p_gameState   The current game state.
     * @return              A string representing the created order.
     * @throws IOException If an I/O error occurs while creating the order.
     */
    public abstract String createOrder(Player p_player, GameState p_gameState) throws IOException;

    /**
     * Generates a deployment order based on the player's strategy.
     *
     * @param p_player      The player for whom the deployment order is generated.
     * @param p_gameState   The current game state.
     * @return              A string representing the generated deployment order.
     */
    public abstract String generateDeployOrder(Player p_player, GameState p_gameState);

    /**
     * Generates an advance order based on the player's strategy.
     *
     * @param p_player      The player for whom the advance order is generated.
     * @param p_gameState   The current game state.
     * @return              A string representing the generated advance order.
     */
    public abstract String generateAdvanceOrder(Player p_player, GameState p_gameState);

    /**
     * Generates a card-related order based on the player's strategy.
     *
     * @param p_player      The player for whom the card-related order is generated.
     * @param p_gameState   The current game state.
     * @param p_cardName    The name of the card associated with the order.
     * @return              A string representing the generated card-related order.
     */
    public abstract String generateCardOrder(Player p_player, GameState p_gameState, String p_cardName);

    /**
     * Gets the player's strategy as a string.
     *
     * @return A string representing the player's strategy.
     */
    public abstract String getPlayerStrategy();

}

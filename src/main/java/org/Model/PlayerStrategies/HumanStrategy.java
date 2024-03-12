package org.Model.PlayerStrategies;

import org.Model.GameState;
import org.Model.Player;

import java.io.IOException;
import java.io.Serializable;
import java.util.Scanner;

public class HumanStrategy extends BehaviourStrategy implements Serializable {
    /**
     * Creates a string representation of the order issued by the human player based on user input.
     *
     * @param p_player    The player for whom the order is being created.
     * @param p_gameState The current state of the game.
     * @return A string representing the order entered by the human player.
     * @throws IOException If an I/O error occurs.
     */
    @Override
    public String createOrder(Player p_player, GameState p_gameState) throws IOException {

        Scanner scanner = new Scanner(System.in);
        System.out.println("\nPlease enter command to issue order for player : " + p_player.getPlayerName()
                + " or give showmap command to view current state of the game.");
        return scanner.nextLine();
    }

    /**
     * Generates a deploy order for the human player. This method is not implemented for the HumanStrategy class.
     *
     * @param p_player    The player for whom the deploy order is being generated.
     * @param p_gameState The current state of the game.
     * @return Always returns null since deploy orders are not generated in this strategy.
     */
    @Override
    public String generateDeployOrder(Player p_player, GameState p_gameState) {
        return null;
    }

    /**
     * Generates an advance order for the human player. This method is not implemented for the HumanStrategy class.
     *
     * @param p_player    The player for whom the advance order is being generated.
     * @param p_gameState The current state of the game.
     * @return Always returns null since advance orders are not generated in this strategy.
     */
    @Override
    public String generateAdvanceOrder(Player p_player, GameState p_gameState) {
        return null;
    }

    /**
     * Generates a card-related order for the human player. This method is not implemented for the HumanStrategy class.
     *
     * @param p_player    The player for whom the card order is being generated.
     * @param p_gameState The current state of the game.
     * @param p_cardName  The name of the card to be used in the order.
     * @return Always returns null since card orders are not generated in this strategy.
     */
    @Override
    public String generateCardOrder(Player p_player, GameState p_gameState, String p_cardName) {
        return null;
    }

    /**
     * Gets the player strategy name for the HumanStrategy.
     *
     * @return A string representing the player strategy, which is "Human" in this case.
     */
    @Override
    public String getPlayerStrategy() {
        return "Human";
    }
}

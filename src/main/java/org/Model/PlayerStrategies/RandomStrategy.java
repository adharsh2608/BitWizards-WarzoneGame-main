package org.Model.PlayerStrategies;

import org.Model.Country;
import org.Model.GameState;
import org.Model.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * RandomStrategy class represents a random strategy for player behavior in the game.
 */
public class RandomStrategy extends BehaviourStrategy implements Serializable {

    /**
     * List containing deploy order countries.
     */
    ArrayList<Country> d_deployCountries = new ArrayList<Country>();

    /**
     * This method creates a new order.
     *
     * @param p_player    object of Player class
     * @param p_gameState object of GameState class
     *
     * @return Order object of order class
     */
    @Override
    public String createOrder(Player p_player, GameState p_gameState) {
        System.out.println("Generating Order for : " + p_player.getPlayerName());
        String l_command;
        boolean l_deployCheck = p_player.getCountryCaptured().stream().noneMatch(l_country -> l_country.getArmies() > 0);
        if (l_deployCheck) {
            if(p_player.getNumOfArmiesRemaining()>0) {
                l_command = generateDeployOrder(p_player, p_gameState);
            }else{
                l_command = generateAdvanceOrder(p_player, p_gameState);
            }
        } else {
            if(!p_player.getAllCards().isEmpty()){
                int l_index = (int) (Math.random() * 3) +1;
                switch (l_index) {
                    case 1:
                        l_command = generateDeployOrder(p_player, p_gameState);
                        break;
                    case 2:
                        l_command = generateAdvanceOrder(p_player, p_gameState);
                        break;
                    case 3:
                        if (p_player.getAllCards().size() == 1) {
                            l_command = generateCardOrder(p_player, p_gameState, p_player.getAllCards().get(0));
                            break;
                        } else {
                            Random l_random = new Random();
                            int l_randomIndex = l_random.nextInt(p_player.getAllCards().size());
                            l_command = generateCardOrder(p_player, p_gameState, p_player.getAllCards().get(l_randomIndex));
                            break;
                        }
                    default:
                        l_command = generateAdvanceOrder(p_player, p_gameState);
                        break;
                }
            } else{
                Random l_random = new Random();
                Boolean l_randomBoolean = l_random.nextBoolean();
                if(l_randomBoolean){
                    l_command = generateDeployOrder(p_player, p_gameState);
                }else{
                    l_command = generateAdvanceOrder(p_player, p_gameState);
                }
            }
        }
        return l_command;
    }

    /**
     * TO generate the Deploy Order.
     * @param p_player      The player for whom the deployment order is generated.
     * @param p_gameState   The current game state.
     * @return
     */
    @Override
    public String generateDeployOrder(Player p_player, GameState p_gameState){
        if (p_player.getNumOfArmiesRemaining()>0) {
            Random l_random = new Random();
            System.out.println(p_player.getCountryCaptured().size());
            Country l_randomCountry = getRandomCountry(p_player.getCountryCaptured());
            d_deployCountries.add(l_randomCountry);
            int l_armiesToDeploy = 1;
            if (p_player.getNumOfArmiesRemaining()>1) {
                l_armiesToDeploy = l_random.nextInt(p_player. getNumOfArmiesRemaining() - 1) + 1;
            }
            return String.format("deploy %s %d", l_randomCountry.getCountryName(), l_armiesToDeploy);
        } else {
            return generateAdvanceOrder(p_player,p_gameState);
        }
    }

    /**
     * To generate the Advance order
     * @param p_player      The player for whom the advance order is generated.
     * @param p_gameState   The current game state.
     * @return
     */
    @Override
    public String generateAdvanceOrder(Player p_player, GameState p_gameState){
        int l_armiesToSend;
        Random l_random = new Random();
        Country l_randomOwnCountry = getRandomCountry(d_deployCountries);
        int l_randomIndex = l_random.nextInt(l_randomOwnCountry.getNeighbourCountries().size());
        Country l_randomNeighbor;
        if (l_randomOwnCountry.getNeighbourCountries().size()>1) {
            l_randomNeighbor = p_gameState.getCurrentMap().getCountryById(l_randomOwnCountry.getNeighbourCountries().get(l_randomIndex));
        } else {
            l_randomNeighbor = p_gameState.getCurrentMap().getCountryById(l_randomOwnCountry.getNeighbourCountries().get(0));
        }

        if (l_randomOwnCountry.getArmies()>1) {
            l_armiesToSend = l_random.nextInt(l_randomOwnCountry.getArmies() - 1) + 1;
        } else {
            l_armiesToSend = 1;
        }
        return "advance "+l_randomOwnCountry.getCountryName()+" "+l_randomNeighbor.getCountryName()+" "+ l_armiesToSend;
    }

    /**
     * Generate card Order for the Player.
     * @param p_player      The player for whom the card-related order is generated.
     * @param p_gameState   The current game state.
     * @param p_cardName    The name of the card associated with the order.
     * @return
     */
    @Override
    public String generateCardOrder(Player p_player, GameState p_gameState, String p_cardName){
        int l_armiesToSend;
        Random l_random = new Random();
        Country l_randomOwnCountry = getRandomCountry(p_player.getCountryCaptured());

        Country l_randomNeighbour = p_gameState.getCurrentMap().getCountryById(l_randomOwnCountry.getNeighbourCountries().get(l_random.nextInt(l_randomOwnCountry.getNeighbourCountries().size())));
        Player l_randomPlayer = getRandomPlayer(p_player, p_gameState);

        if (l_randomOwnCountry.getArmies()>1) {
            l_armiesToSend = l_random.nextInt(l_randomOwnCountry.getArmies() - 1) + 1;
        } else {
            l_armiesToSend = 1;
        }
        switch(p_cardName){
            case "bomb":
                return "bomb "+ l_randomNeighbour.getCountryName();
            case "blockade":
                return "blockade "+ l_randomOwnCountry.getCountryName();
            case "airlift":
                return "airlift "+ l_randomOwnCountry.getCountryName()+" "+getRandomCountry(p_player.getCountryCaptured()).getCountryName()+" "+l_armiesToSend;
            case "negotiate":
                return "negotiate"+" "+l_randomPlayer.getPlayerName();
        }
        return null;
    }

    @Override
    public String getPlayerStrategy() {
        return "Random";
    }


    /**
     * Returns a random country owned by the player.
     *
     * @param p_listOfCountries list of countries owned by the player
     * @return a random country from the list
     */
    private Country getRandomCountry(List<Country> p_listOfCountries){
        Random l_random = new Random();
        return p_listOfCountries.get(l_random.nextInt(p_listOfCountries.size()));
    }

    /**
     * Chooses a random player to negotiate.
     *
     * @param p_player    player object
     * @param p_gameState current game state.
     * @return player object
     */
    private Player getRandomPlayer(Player p_player, GameState p_gameState){
        ArrayList<Player> l_playerList = new ArrayList<>();
        Random l_random = new Random();

        for(Player l_player : p_gameState.getPlayerController().getAllPlayers()){
            if(!l_player.equals(p_player))
                l_playerList.add(p_player);
        }
        return l_playerList.get(l_random.nextInt(l_playerList.size()));
    }
}

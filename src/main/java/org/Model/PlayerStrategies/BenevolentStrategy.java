package org.Model.PlayerStrategies;

import org.Model.Country;
import org.Model.GameState;
import org.Model.Player;

import java.io.Serializable;
import java.util.*;

/**
 * The BenevolentStrategy class represents a benevolent strategy for a player in a Risk-style game.
 * This strategy focuses on deploying armies to the weakest countries and avoiding aggressive actions.
 */
public class BenevolentStrategy extends BehaviourStrategy implements Serializable {

    /**
     * List containing countries for deploy orders.
     */
    ArrayList<Country> d_deployCountries = new ArrayList<Country>();

    /**
     * Creates a new order based on the benevolent strategy.
     *
     * @param p_player    The player for whom the order is generated.
     * @param p_gameState The current state of the game.
     * @return A string representing the generated order.
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
                System.out.println("Enters Card Logic");
                int l_index = (int) (Math.random() * 3) +1;
                switch (l_index) {
                    case 1:
                        System.out.println("Deploy!");
                        l_command = generateDeployOrder(p_player, p_gameState);
                        break;
                    case 2:
                        System.out.println("Advance!");
                        l_command = generateAdvanceOrder(p_player, p_gameState);
                        break;
                    case 3:
                        if (p_player.getAllCards().size() == 1) {
                            System.out.println("Cards!");
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
                    System.out.println("Deploy Order Generated");
                    l_command = generateDeployOrder(p_player, p_gameState);
                }else{
                    System.out.println("Advance Order Generated");
                    l_command = generateAdvanceOrder(p_player, p_gameState);
                }
            }
        }
        return l_command;
    }

    /**
     * Generates a deploy order based on the benevolent strategy.
     *
     * @param p_player    The player deploying armies.
     * @param p_gameState The current state of the game.
     * @return A string representing the deploy order.
     */
    @Override
    public String generateDeployOrder(Player p_player, GameState p_gameState) {
        if (p_player.getNumOfArmiesRemaining()>0) {
            Country l_weakestCountry = getWeakestCountry(p_player);
            d_deployCountries.add(l_weakestCountry);

            Random l_random = new Random();
            int l_armiesToDeploy = 1;
            if (p_player.getNumOfArmiesRemaining()>1) {
                l_armiesToDeploy = l_random.nextInt(p_player.getNumOfArmiesRemaining() - 1) + 1;
            }
            System.out.println("deploy " + l_weakestCountry.getCountryName() + " " + l_armiesToDeploy);
            return String.format("deploy %s %d", l_weakestCountry.getCountryName(), l_armiesToDeploy);
        }else{
            return generateAdvanceOrder(p_player, p_gameState);
        }
    }

    /**
     * Generates an advance order based on the benevolent strategy.
     *
     * @param p_player    The player advancing armies.
     * @param p_gameState The current state of the game.
     * @return A string representing the advance order.
     */
    @Override
    public String generateAdvanceOrder(Player p_player, GameState p_gameState) {
        // advance on weakest country
        int l_armiesToSend;
        Random l_random = new Random();

        Country l_randomSourceCountry = getRandomCountry(d_deployCountries);
        System.out.println("Source country : "+ l_randomSourceCountry.getCountryName());

        Country l_weakestTargetCountry = getWeakestNeighbor(l_randomSourceCountry, p_gameState, p_player);
        if(l_weakestTargetCountry == null)
            return null;

        System.out.println("Target Country : "+l_weakestTargetCountry.getCountryName());
        if (l_randomSourceCountry.getArmies() > 1) {
            l_armiesToSend = l_random.nextInt(l_randomSourceCountry.getArmies() - 1) + 1;
        } else {
            l_armiesToSend = 1;
        }

        System.out.println("advance " + l_randomSourceCountry.getCountryName() + " "
                + l_weakestTargetCountry.getCountryName() + " " + l_armiesToSend);
        return "advance " + l_randomSourceCountry.getCountryName() + " " + l_weakestTargetCountry.getCountryName()
                + " " + l_armiesToSend;
    }

    /**
     * Generates a card-related order based on the benevolent strategy.
     *
     * @param p_player    The player using the card.
     * @param p_gameState The current state of the game.
     * @param p_cardName  The name of the card being used.
     * @return A string representing the card-related order.
     */
    @Override
    public String generateCardOrder(Player p_player, GameState p_gameState, String p_cardName) {
        int l_armiesToSend;
        Random l_random = new Random();
        Country l_randomOwnCountry = getRandomCountry(p_player.getCountryCaptured());

        if (l_randomOwnCountry.getArmies() > 1) {
            l_armiesToSend = l_random.nextInt(l_randomOwnCountry.getArmies() - 1) + 1;
        } else {
            l_armiesToSend = 1;
        }

        switch (p_cardName) {
            case "bomb":
                System.err.println("I am benevolent player, I don't hurt anyone.");
                return "bomb" + " " + "false";
            case "blockade":
                return "blockade " + l_randomOwnCountry.getCountryName();
            case "airlift":
                return "airlift " + l_randomOwnCountry.getCountryName() + " "
                        + getRandomCountry(p_player.getCountryCaptured()).getCountryName() + " " + l_armiesToSend;
            case "negotiate":
                return "negotiate " + getRandomEnemyPlayer(p_player, p_gameState).getPlayerName();
        }
        return null;
    }

    @Override
    public String getPlayerStrategy() {
        return "Benevolent";
    }

    /**
     * This method returns random country.
     *
     * @param p_listOfCountries list of countries
     * @return return country
     */
    private Country getRandomCountry(List<Country> p_listOfCountries) {
        Random l_random = new Random();
        return p_listOfCountries.get(l_random.nextInt(p_listOfCountries.size()));
    }

    /**
     * This method return weakest Country where benevolent player can deploy armies.
     *
     * @param p_player Player
     * @return weakest country
     */
    public Country getWeakestCountry(Player p_player) {
        List<Country> l_countriesOwnedByPlayer = p_player.getCountryCaptured();
        Country l_Country = calculateWeakestCountry(l_countriesOwnedByPlayer);
        return l_Country;
    }

    /**
     * This method return weakest neighbor where Source country can advance armies
     * to this weakest country.
     *
     * @param l_randomSourceCountry Source country
     * @param p_gameState           GameState
     * @param p_player benevolent player
     * @return weakest neighbor
     */
    public Country getWeakestNeighbor(Country l_randomSourceCountry, GameState p_gameState, Player p_player) {
        List<Integer> l_adjacentCountryIds = l_randomSourceCountry.getNeighbourCountries();
        List<Country> l_listOfNeighbors = new ArrayList<Country>();
        for (int l_index = 0; l_index < l_adjacentCountryIds.size(); l_index++) {
            Country l_country = p_gameState.getCurrentMap()
                    .getCountryById(l_randomSourceCountry.getNeighbourCountries().get(l_index));
            if(p_player.getCountryCaptured().contains(l_country))
                l_listOfNeighbors.add(l_country);
        }
        if(!l_listOfNeighbors.isEmpty())
            return calculateWeakestCountry(l_listOfNeighbors);

        return null;
    }

    /**
     * This method calculates weakest country.
     *
     * @param l_listOfCountries list of countries
     * @return weakest country
     */
    public Country calculateWeakestCountry(List<Country> l_listOfCountries) {
        LinkedHashMap<Country, Integer> l_CountryWithArmies = new LinkedHashMap<Country, Integer>();

        int l_smallestNoOfArmies;
        Country l_Country = null;

        // return weakest country from owned countries of player.
        for (Country l_country : l_listOfCountries) {
            l_CountryWithArmies.put(l_country, l_country.getArmies());
        }
        l_smallestNoOfArmies = Collections.min(l_CountryWithArmies.values());
        for (Map.Entry<Country, Integer> entry : l_CountryWithArmies.entrySet()) {
            if (entry.getValue().equals(l_smallestNoOfArmies)) {
                return entry.getKey();
            }
        }
        return l_Country;

    }

    /**
     * Get random enemy player.
     *
     * @param p_player    Player
     * @param p_gameState Gamestate
     * @return Player
     */
    private Player getRandomEnemyPlayer(Player p_player, GameState p_gameState) {
        ArrayList<Player> l_playerList = new ArrayList<Player>();
        Random l_random = new Random();

        for (Player l_player : p_gameState.getPlayerController().getAllPlayers()) {
            if (!l_player.equals(p_player))
                l_playerList.add(p_player);
        }
        return l_playerList.get(l_random.nextInt(l_playerList.size()));
    }
}

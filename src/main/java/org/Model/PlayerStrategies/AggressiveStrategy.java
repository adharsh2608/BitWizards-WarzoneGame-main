package org.Model.PlayerStrategies;

import org.Model.Country;
import org.Model.GameState;
import org.Model.Player;

import java.io.Serializable;
import java.util.*;

/**
 * The AggressiveStrategy class represents a strategy where the player tends to be aggressive in deploying and advancing armies.
 */
public class AggressiveStrategy extends BehaviourStrategy implements Serializable {

    /**
     * List containing deploy order countries.
     */
    ArrayList<Country> d_deployCountries = new ArrayList<Country>();

    /**
     * Creates a new order based on the aggressive strategy.
     *
     * @param p_player    An object of the Player class.
     * @param p_gameState An object of the GameState class.
     * @return String representation of the generated order.
     */
    @Override
    public String createOrder(Player p_player, GameState p_gameState) {
        try {
            System.out.println("Generating Order for : " + p_player.getPlayerName());
            String l_command;
            boolean l_deployCheck = p_player.getCountryCaptured().stream().noneMatch(l_country -> l_country.getArmies() > 0);
            if (l_deployCheck) {
                if (p_player.getNumOfArmiesRemaining() > 0) {
                    l_command = generateDeployOrder(p_player, p_gameState);
                } else {
                    l_command = generateAdvanceOrder(p_player, p_gameState);
                }
            } else {
                if (!p_player.getAllCards().isEmpty()) {
                    System.out.println("Using Card");
                    int l_index = (int) (Math.random() * 3) + 1;
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
                } else {
                    Random l_random = new Random();
                    Boolean l_randomBoolean = l_random.nextBoolean();
                    if (l_randomBoolean) {
                        System.out.println("Deploy Order Generated");
                        l_command = generateDeployOrder(p_player, p_gameState);
                    } else {
                        System.out.println("Advance Order Generated");
                        l_command = generateAdvanceOrder(p_player, p_gameState);
                    }
                }
            }
            return l_command;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Generates a deploy order based on the aggressive strategy.
     *
     * @param p_player    An object of the Player class.
     * @param p_gameState An object of the GameState class.
     * @return String representation of the generated deploy order.
     */
    @Override
    public String generateDeployOrder(Player p_player, GameState p_gameState) {
        Random l_random = new Random();
        //Finding the strongest country to deploy
        Country l_strongestCountry = getStrongestCountry(p_player);
        d_deployCountries.add(l_strongestCountry);
        int l_armiesToDeploy = 1;
        if (p_player.getNumOfArmiesRemaining()>1) {
            l_armiesToDeploy = l_random.nextInt(p_player.getNumOfArmiesRemaining() - 1) + 1;
        }
        return String.format("deploy %s %d", l_strongestCountry.getCountryName(), l_armiesToDeploy);
    }

    /**
     * Generates an advance order based on the aggressive strategy.
     *
     * @param p_player    An object of the Player class.
     * @param p_gameState An object of the GameState class.
     * @return String representation of the generated advance order.
     */
    @Override
    public String generateAdvanceOrder(Player p_player, GameState p_gameState) {
        // move armies from its neighbors to maximize armies on source country

        Random l_random = new Random();
        Country l_randomSourceCountry = d_deployCountries.get(l_random.nextInt(d_deployCountries.size()));

        moveArmiesFromItsNeighbors(p_player, l_randomSourceCountry, p_gameState);

        Country l_randomTargetCountry = p_gameState.getCurrentMap()
                .getCountryById(l_randomSourceCountry.getNeighbourCountries()
                        .get(l_random.nextInt(l_randomSourceCountry.getNeighbourCountries().size())));

        int l_armiesToSend = l_randomSourceCountry.getArmies() > 1 ? l_randomSourceCountry.getArmies() : 1;

        // attacks with strongest country
        return "advance " + l_randomSourceCountry.getCountryName() + " " + l_randomTargetCountry.getCountryName()
                + " " + l_armiesToSend;

    }

    /**
     * Move armies from neighbor to maximize aggregation of forces.
     *
     * @param p_player              Player
     * @param p_randomSourceCountry Source country
     * @param p_gameState           Game state
     */
    public void moveArmiesFromItsNeighbors(Player p_player, Country p_randomSourceCountry, GameState p_gameState) {
        List<Integer> l_adjacentCountryIds = p_randomSourceCountry.getNeighbourCountries();
        List<Country> l_listOfNeighbors = new ArrayList<>();
        for (int l_index = 0; l_index < l_adjacentCountryIds.size(); l_index++) {
            Country l_country = p_gameState.getCurrentMap()
                    .getCountryById(p_randomSourceCountry.getNeighbourCountries().get(l_index));
            // check if neighbor belongs to player and then add to list
            if (p_player.getCountryCaptured().contains(l_country)) {
                l_listOfNeighbors.add(l_country);
            }
        }

        int l_ArmiesToMove = 0;
        // send armies from neighbor to source country
        for (Country l_con : l_listOfNeighbors) {
            l_ArmiesToMove += p_randomSourceCountry.getArmies() > 0
                    ? p_randomSourceCountry.getArmies() + (l_con.getArmies())
                    : (l_con.getArmies());

        }
        p_randomSourceCountry.setArmies(l_ArmiesToMove);
    }

    /**
     * Returns a random country from the given list of countries.
     *
     * @param p_listOfCountries List of countries.
     * @return A random country.
     */
    private Country getRandomCountry(List<Country> p_listOfCountries) {
        Random l_random = new Random();
        return p_listOfCountries.get(l_random.nextInt(p_listOfCountries.size()));
    }

    /**
     *
     * Generates a card order based on the aggressive strategy.
     * @param p_player    An object of the Player class.
     */
    @Override
    public String generateCardOrder(Player p_player, GameState p_gameState, String p_cardName) {
        Random l_random = new Random();
        Country l_StrongestSourceCountry = getStrongestCountry(p_player);

        Country l_randomTargetCountry = p_gameState.getCurrentMap()
                .getCountryById(l_StrongestSourceCountry.getNeighbourCountries()
                        .get(l_random.nextInt(l_StrongestSourceCountry.getNeighbourCountries().size())));

        int l_armiesToSend = l_StrongestSourceCountry.getArmies() > 1 ? l_StrongestSourceCountry.getArmies() : 1;
        p_player.getCountryCaptured().get(l_random.nextInt(p_player.getCountryCaptured().size()));
        switch (p_cardName) {
            case "bomb":
                return "bomb " + l_randomTargetCountry.getCountryName();
            case "blockade":
                return "blockade " + l_StrongestSourceCountry.getCountryName();
            case "airlift":
                Country l_tempRandomCountry = p_player.getCountryCaptured().get(l_random.nextInt(p_player.getCountryCaptured().size()));
                return "airlift " + l_StrongestSourceCountry.getCountryName() + " "
                        + l_tempRandomCountry.getCountryName() + " " + l_armiesToSend;
            case "negotiate":
                return "negotiate" + " " + getRandomEnemyPlayer(p_player, p_gameState).getPlayerName();
        }
        return null;
    }

    @Override
    public String getPlayerStrategy() {
        return "Aggressive";
    }

    /**
     * Get random enemy player.
     *
     * @param p_player    Player
     * @param p_gameState Game state
     * @return random enemy player
     */
    private Player getRandomEnemyPlayer(Player p_player, GameState p_gameState) {
        ArrayList<Player> l_playerList = new ArrayList<>();
        Random l_random = new Random();

        for (Player l_player : p_gameState.getPlayerController().getAllPlayers()) {
            if (!l_player.equals(p_player))
                l_playerList.add(p_player);
        }
        return l_playerList.get(l_random.nextInt(l_playerList.size()));
    }


    /**
     * Get strongest country.
     *
     * @param p_player    Player
     * @return Strongest country
     */
    public Country getStrongestCountry(Player p_player) {
        List<Country> l_countriesOwnedByPlayer = p_player.getCountryCaptured();
        Country l_Country = calculateStrongestCountry(l_countriesOwnedByPlayer);
        return l_Country;
    }

    /**
     * This method finds the  strongest country.
     *
     * @param l_listOfCountries List of countries
     * @return strongest country
     */
    public Country calculateStrongestCountry(List<Country> l_listOfCountries) {
        LinkedHashMap<Country, Integer> l_CountryWithArmies = new LinkedHashMap<>();

        int l_largestNoOfArmies;
        Country l_Country = null;
        // return strongest country from owned countries of player.
        for (Country l_country : l_listOfCountries) {
            l_CountryWithArmies.put(l_country, l_country.getArmies());
        }
        l_largestNoOfArmies = Collections.max(l_CountryWithArmies.values());
        for (Map.Entry<Country, Integer> entry : l_CountryWithArmies.entrySet()) {
            if (entry.getValue().equals(l_largestNoOfArmies)) {
                return entry.getKey();
            }
        }
        return l_Country;

    }

}

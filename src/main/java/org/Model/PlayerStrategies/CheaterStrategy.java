package org.Model.PlayerStrategies;

import org.Model.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;


/**
 * The CheaterStrategy class represents the behavior of a cheater player in the game.
 * This strategy involves doubling armies, conquering neighboring enemies, and performing other cheater actions.
 */
public class CheaterStrategy extends BehaviourStrategy implements Serializable {

    /**
     * This method creates a new order for the cheater player.
     *
     * @param p_player      object of Player class
     * @param p_gameState   object of GameState class
     * @return Order object of order class
     */
    @Override
    public String createOrder(Player p_player, GameState p_gameState){
        try {

            if(p_player.getNumOfArmiesRemaining() != 0) {
                while(p_player.getNumOfArmiesRemaining() > 0) {
                    Random l_random = new Random();
                    Country l_randomCountry = getRandomCountry(p_player.getCountryCaptured());
                    int l_armiesToDeploy = l_random.nextInt(p_player.getNumOfArmiesRemaining()) + 1;

                    l_randomCountry.setArmies(l_armiesToDeploy);
                    p_player.setNumOfArmiesRemaining(p_player.getNumOfArmiesRemaining() - l_armiesToDeploy);

                    String l_logMessage = "Cheater Player: " + p_player.getPlayerName() +
                            " assigned " + l_armiesToDeploy +
                            " armies to  " + l_randomCountry.getCountryName();

                    //p_gameState.log(l_logMessage, "effect");
                }
            }
            List<Country> l_newCountriesCaptured = new ArrayList<>();

            for (Country l_country : p_player.getCountryCaptured()) {
                for (Integer l_neighbor : l_country.getNeighbourCountries()) {
                    for (Player l_player : p_gameState.getPlayerController().getAllPlayers()) {
                        List<Country> l_playerCountries = l_player.getCountryCaptured();
                        for (int i = l_playerCountries.size()-1;i >= 0;i--) {
                            Country l_playerCountry = l_playerCountries.get(i);
                            if (!l_player.getPlayerName().equals(p_player.getPlayerName()) && Objects.equals(l_playerCountry.getCountryId(), l_neighbor)) {
                                l_player.removeCountryCaptured(l_playerCountry);
                                l_newCountriesCaptured.add(l_playerCountry);
                            }
                        }
                    }
                }
            }

            p_player.d_countryCaptured.addAll(l_newCountriesCaptured);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }

        //double the army of a country if it has an enemy
        for (Country l_Country : p_player.getCountryCaptured()) {
            for(Integer l_neighbor : l_Country.getNeighbourCountries()) {
                for (Country l_neighborCheck : p_player.getCountryCaptured()) {
                    if (Objects.equals(l_neighborCheck.getCountryId(), l_neighbor)) {
                        l_Country.setArmies(l_Country.getArmies()*2);
                    }
                }
            }
        }
        return null;
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
     * Doubles the number of armies on countries that are neighbors to enemies.
     *
     * @param p_player    object of Player class
     * @param p_gameState object of GameState class
     */
    private void doubleArmyOnEnemyNeighboredCounties(Player p_player, GameState p_gameState){
        List<Country> l_countriesOwned = p_player.getCountryCaptured();

        for(Country l_ownedCountry : l_countriesOwned) {
            ArrayList<Integer> l_countryEnemies = getEnemies(p_player, l_ownedCountry);

            if(l_countryEnemies.isEmpty()) continue;

            Integer l_arimiesInTerritory = l_ownedCountry.getArmies();

            if(l_arimiesInTerritory == 0) continue;

            l_ownedCountry.setArmies(l_arimiesInTerritory*2);

            String l_logMessage = "Cheater Player: " + p_player.getPlayerName() +
                    " doubled the armies ( Now: " + l_arimiesInTerritory*2 +
                    ") in " + l_ownedCountry.getCountryName();

            //p_gameState.updateLog(l_logMessage, "effect");

        }
    }

    /**
     * Conquer all enemies that are neighbors to the country owned by the player.
     *
     * @param p_player    object of Player class
     * @param p_gameState object of GameState class
     */
    private void conquerNeighboringEnemies(Player p_player, GameState p_gameState){
        List<Country> l_countriesOwned = p_player.getCountryCaptured();

        for(Country l_ownedCountry : l_countriesOwned) {
            ArrayList<Integer> l_countryEnemies = getEnemies(p_player, l_ownedCountry);

            for(Integer l_enemyId: l_countryEnemies) {
                Map l_loadedMap =  p_gameState.getCurrentMap();
                Player l_enemyCountryOwner = this.getCountryOwner(p_gameState, l_enemyId);
                Country l_enemyCountry = l_loadedMap.getCountryById(l_enemyId);
                this.conquerTargetCountry(p_gameState, l_enemyCountryOwner ,p_player, l_enemyCountry);

                String l_logMessage = "Cheater Player: " + p_player.getPlayerName() +
                        " Now owns " + l_enemyCountry.getCountryName();

                //p_gameState.updateLog(l_logMessage, "effect");
            }

        }
    }

    /**
     * Gets the owner of the country based on the game state and country ID.
     *
     * @param p_gameState  Current state of the game
     * @param p_countryId  id of the country whose neighbor is to be searched
     * @return Owner of the Country
     */

    private Player getCountryOwner(GameState p_gameState, Integer p_countryId){
        List<Player> l_players = p_gameState.getPlayerController().getAllPlayers();
        Player l_owner = null;

        for(Player l_player: l_players){
            List<Integer> l_countriesOwned = new ArrayList<>();
            for(Country l_country:l_player.getCountryCaptured()){
                l_countriesOwned.add(l_country.getCountryId());
            }
            if(l_countriesOwned.contains(p_countryId)){
                l_owner = l_player;
                break;
            }
        }

        return l_owner;
    }

    /**
     * Conquers the target country when the target country doesn't have any army.
     *
     * @param p_gameState       Current state of the game
     * @param p_cheaterPlayer   player owning source country
     * @param p_targetCPlayer   player owning the target country
     * @param p_targetCountry   target country of the battle
     */
    private void conquerTargetCountry(GameState p_gameState, Player p_targetCPlayer, Player p_cheaterPlayer, Country p_targetCountry) {
        p_targetCPlayer.getCountryCaptured().remove(p_targetCountry);
        p_cheaterPlayer.getCountryCaptured().add(p_targetCountry);
        // Add Log Here
        this.updateContinents(p_cheaterPlayer, p_targetCPlayer, p_gameState);
    }

    /**
     * Updates continents of players based on battle results.
     *
     * @param p_cheaterPlayer player owning source country
     * @param p_targetCPlayer player owning target country
     * @param p_gameState             current state of the game
     */
    private void updateContinents(Player p_cheaterPlayer, Player p_targetCPlayer,
                                  GameState p_gameState) {
        List<Player> l_playesList = new ArrayList<>();
        p_cheaterPlayer.setCountryCaptured(new ArrayList<>());
        p_targetCPlayer.setCountryCaptured(new ArrayList<>());
        l_playesList.add(p_cheaterPlayer);
        l_playesList.add(p_targetCPlayer);

        performContinentAssignment(l_playesList, p_gameState.getCurrentMap().getAllContinentsList());
    }


    /**
     * Checks if player is having any continent as a result of random country
     * assignment.
     *
     * @param p_players    list of all available players
     * @param p_continents list of all available continents
     */
    public void performContinentAssignment(List<Player> p_players, List<Continent> p_continents) {
        for (Player l_player : p_players) {
            List<String> l_countriesOwned = new ArrayList<>();
            if (!l_player.getCountryCaptured().isEmpty()) {
                l_player.getCountryCaptured().forEach(l_country -> l_countriesOwned.add(l_country.getCountryName()));

                for (Continent l_continent : p_continents) {
                    List<String> l_countriesOfContinent = new ArrayList<>();
                    l_continent.getCountries().forEach(l_count -> l_countriesOfContinent.add(l_count.getCountryName()));
                    if (l_countriesOwned.containsAll(l_countriesOfContinent)) {
                        if (l_player.getContinentsOwned() == null)
                            l_player.getContinentsOwned(new ArrayList<>());

                        l_player.getContinentsOwned().add(l_continent);
                        System.out.println("Player : " + l_player.getPlayerName() + " is assigned with continent : "
                                + l_continent.getContinentName());

                        //d_assignmentLog += "\n Player : " + l_player.getPlayerName() + " is assigned with continent : "
                        //      + l_continent.getContinentName();
                    }
                }
            }
        }
    }

    /**
     * Gets enemies of the player.
     *
     * @param p_player cheater player
     * @param p_country player country
     * @return list of enemy neighbors
     */
    private ArrayList<Integer> getEnemies(Player p_player, Country p_country){
        ArrayList<Integer> l_enemyNeighbors = new ArrayList<>();

        List<Integer> l_countriesOwned = new ArrayList<>();
        for(Country l_country:p_player.getCountryCaptured()){
            l_countriesOwned.add(l_country.getCountryId());
        }
        for(Integer l_countryID : p_country.getNeighbourCountries()){
            if(!l_countriesOwned.contains(l_countryID))
                l_enemyNeighbors.add(l_countryID);
        }
        return l_enemyNeighbors;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String generateDeployOrder(Player p_player, GameState p_gameState) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String generateAdvanceOrder(Player p_player, GameState p_gameState) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String generateCardOrder(Player p_player, GameState p_gameState, String p_cardName) {
        return null;
    }

    @Override
    public String getPlayerStrategy() {
        return "Cheater";
    }

}

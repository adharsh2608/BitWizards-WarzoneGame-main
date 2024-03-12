package org.Utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import org.Constants.AllTheConstants;
import org.Model.*;
import org.Model.Map;

/**
 * The ConquestMapReaderWriter class is responsible for reading and writing Conquest maps.
 * It provides methods to read map data from a file and write map data to a file.
 */
public class ConquestMapReaderWriter implements Serializable {

    /**
     * Reads Conquest map data from a list of lines and populates a Map object.
     *
     * @param p_map          The Map object to be populated with the read data.
     * @param p_linesOfFile  The list of lines containing Conquest map data.
     */
    public void readFromConquestMapFile(Map p_map, List<String> p_linesOfFile) {
        //Step:1 Reading the continents data from file and extract the continents.
        List<String> l_initContinentData = p_linesOfFile.subList(p_linesOfFile.indexOf(AllTheConstants.conquestContinentSeparator) + 1, p_linesOfFile.indexOf(AllTheConstants.conquestCountrySeparator) - 2);
        List<Continent> l_continents = new ArrayList<>();

        int l_continentId = 1;
        for (String l_continent : l_initContinentData) {
            String[] l_tempContinent = l_continent.split("=");
            l_continents.add(new Continent(l_continentId, l_tempContinent[0], Integer.parseInt(l_tempContinent[1])));
            l_continentId++;
        }

        //Step:2 Reading the countries data from file and extract the countries
        List<String> l_initCountryData = p_linesOfFile.subList(p_linesOfFile.indexOf(AllTheConstants.conquestCountrySeparator) + 1, p_linesOfFile.size());
        List<Country> l_countries = new ArrayList<>();

        int l_country_id = 1;
        for (String country : l_initCountryData) {
            if(!country.isEmpty()) {
                String[] l_tempCountries = country.split(",");
                Continent l_continent = l_continents.stream().filter(l_country -> l_country.getContinentName().equalsIgnoreCase(l_tempCountries[3]))
                        .findFirst().orElse(null);
                Country l_countryObj = new Country(l_country_id, l_tempCountries[0],
                        l_continent.getContinentId());
                l_countries.add(l_countryObj);
                l_country_id++;
            }
        }

        //Step3: Using the unedited countries data with edited countries data to set boundaries
        List<Country> l_countriesWithBorders = new ArrayList<>(l_countries);
        String l_matchedCountry = null;
        for (Country l_currCountry : l_countriesWithBorders) {
            for (String l_contStr : l_initCountryData) {
                if ((l_contStr.split(",")[0]).equalsIgnoreCase(l_currCountry.getCountryName())) {
                    l_matchedCountry = l_contStr;
                    break;
                }
            }
            if (l_matchedCountry.split(",").length > 4) {
                for (int i = 4; i < l_matchedCountry.split(",").length; i++) {
                    String l_tempMatchedCountry = l_matchedCountry;
                    int l_tempI = i;
                    Country l_country = l_countries.stream().filter(l_findCountry -> l_findCountry.getCountryName().equalsIgnoreCase(l_tempMatchedCountry.split(",")[l_tempI]))
                            .findFirst().orElse(null);
                    l_currCountry.getNeighbourCountries().add(l_country.getCountryId());
                }
            }
        }

        //Step:4 Using the countries data with borders to connect continents.
        for (Country c : l_countriesWithBorders) {
            for (Continent cont : l_continents) {
                if (cont.getContinentId().equals(c.getContinentId())) {
                    cont.addCountry(c);
                }
            }
        }

        //Set the current maps data
        p_map.setAllContinentsList(l_continents);
        p_map.setAllCountriesList(l_countries);
    }

    /**
     * Writes Conquest map data to a file with the specified file name.
     *
     * @param p_fileName The name of the file to write the map data to.
     * @param p_map      The Map object containing the map data to be written.
     * @throws IOException If an I/O error occurs while writing to the file.
     */
    public void writeToConquestMapFile(String p_fileName, Map p_map) throws IOException {
        Files.deleteIfExists(Paths.get(AllTheConstants.defaultMapLocationAppendString + p_fileName));
        FileWriter l_writer = new FileWriter(AllTheConstants.defaultMapLocationAppendString + p_fileName);

        if (p_map.getAllContinentsList() != null && !p_map.getAllContinentsList().isEmpty()) {
            l_writer.write(System.lineSeparator() + AllTheConstants.conquestContinentSeparator + System.lineSeparator());
            for (Continent l_continent : p_map.getAllContinentsList()) {
                l_writer.write(
                        l_continent.getContinentName().concat("=").concat(l_continent.getContinentBonusValue().toString())
                                + System.lineSeparator());
            }

        }

        if (p_map.getAllCountriesAsList() != null && !p_map.getAllCountriesAsList().isEmpty()) {
            String l_countryMetaData = new String();
            // Writes Country Objects to File And Organizes Border Data for each of them
            l_writer.write(System.lineSeparator() + AllTheConstants.conquestCountrySeparator + System.lineSeparator());
            for (Country l_country : p_map.getAllCountriesAsList()) {
                l_countryMetaData = new String();
                l_countryMetaData = l_country.getCountryName().concat(",")
                        .concat(p_map.getContinentById(l_country.getContinentId()).getContinentName());

                if (l_country.getNeighbourCountries() != null && !l_country.getNeighbourCountries().isEmpty()) {
                    for (Integer l_adjCountry : l_country.getNeighbourCountries()) {
                        l_countryMetaData = l_countryMetaData.concat(",")
                                .concat(p_map.getCountryById(l_adjCountry).getCountryName());
                    }
                }
                l_writer.write(l_countryMetaData + System.lineSeparator());
            }
        }
        l_writer.close();
    }
}

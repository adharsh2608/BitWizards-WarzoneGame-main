package org.Utils;

import org.Constants.AllTheConstants;
import org.Exceptions.MapInvalidException;
import org.Model.Continent;
import org.Model.Country;
import org.Model.Map;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * DefaultMapReaderWriter class for reading from and writing to a default map file.
 */
public class DefaultMapReaderWriter implements Serializable {
    /**
     * Reads map data from a list of lines representing a default map file and populates the given map object.
     *
     * @param p_map          The map object to populate.
     * @param p_linesOfFile  The list of lines representing the content of a default map file.
     * @throws MapInvalidException Thrown if the map data is invalid.
     */
    public void readFromDefaultMapFile(Map p_map, List<String> p_linesOfFile) throws MapInvalidException {

        // Step 1 : Create sub list for dividing operation
        List<String> l_readContinents = p_linesOfFile.subList(p_linesOfFile.indexOf(AllTheConstants.defaultContinentsSeparator) + 1,
                p_linesOfFile.indexOf(AllTheConstants.defaultCountrySeparator) - 1);
        List<String> l_readCountries = p_linesOfFile.subList(p_linesOfFile.indexOf(AllTheConstants.defaultCountrySeparator) + 1,
                p_linesOfFile.indexOf(AllTheConstants.defaultBordersSeparator) - 1);
        List<String> l_readNeighbours = p_linesOfFile.subList(p_linesOfFile.indexOf(AllTheConstants.defaultBordersSeparator) + 1,
                p_linesOfFile.size());


        // Step 2: Add all the continents to the main list
        for (String l_continent : l_readContinents) {
            String[] l_continentLineSplit = l_continent.split(" ");
            String l_continentName = l_continentLineSplit[0];
            Integer l_continentBonusValue = Integer.parseInt(l_continentLineSplit[1]);
            p_map.addContinent(l_continentName, l_continentBonusValue);
        }

        // Step 3: Add all the countries to the main list
        for (String l_country : l_readCountries) {
            String[] l_countryLineSplit = l_country.split(" ");
            Integer l_continentIndex = Integer.parseInt(l_countryLineSplit[2]);
            String l_countryName = l_countryLineSplit[1];
            Continent l_continent = p_map.getContinentById(l_continentIndex);
            p_map.addCountry(l_countryName, l_continent.getContinentName());
        }

        // Step 4 : Add Neighbour country
        for (String l_border : l_readNeighbours) {
            String[] l_borderLineSplit = l_border.split(" ");
            Integer l_countryId = Integer.parseInt(l_borderLineSplit[0]);
            Country l_mainCountry = p_map.getCountryById(l_countryId);
            for (int i = 1; i < l_borderLineSplit.length; i++) {
                Integer l_neighbourCountryId = Integer.parseInt(l_borderLineSplit[i]);
                Country l_neighbourCountry = p_map.getCountryById(l_neighbourCountryId);
                p_map.addNeighbour(l_mainCountry.getCountryName(), l_neighbourCountry.getCountryName());
            }
        }
    }

    /**
     * Write the Data into the Default Map File.
     * @param p_fileName
     * @param p_map
     * @throws IOException
     */

    public void writeToDefaultMapFile(String p_fileName, Map p_map) throws IOException {
        Files.deleteIfExists(Paths.get(AllTheConstants.defaultMapLocationAppendString + p_fileName));
        FileWriter l_writer = new FileWriter(AllTheConstants.defaultMapLocationAppendString + p_fileName);

        // Write the continent data
        l_writer.write(System.lineSeparator() + AllTheConstants.defaultContinentsSeparator + System.lineSeparator());
        for (Continent l_continent : p_map.getAllContinentsList()) {
            l_writer.write(
                    l_continent.getContinentName().concat(" ").concat(l_continent.getContinentId().toString())
                            + System.lineSeparator());
        }
        String l_countryMetaData , l_bordersMetaData;
        List<String> l_bordersList = new ArrayList<>();

        // Writes Country Objects to File And Organizes Border Data for each of them
        l_writer.write(System.lineSeparator() + AllTheConstants.defaultCountrySeparator + System.lineSeparator());
        for (Country l_country : p_map.getAllCountriesAsList()) {
            l_countryMetaData = l_country.getCountryId().toString().concat(" ").concat(l_country.getCountryName())
                    .concat(" ").concat(l_country.getContinentId().toString());
            l_writer.write(l_countryMetaData + System.lineSeparator());

            if (null != l_country.getNeighbourCountries() && !l_country.getNeighbourCountries().isEmpty()) {
                l_bordersMetaData = l_country.getCountryId().toString();
                for (Integer l_adjCountry : l_country.getNeighbourCountries()) {
                    l_bordersMetaData = l_bordersMetaData.concat(" ").concat(l_adjCountry.toString());
                }
                l_bordersList.add(l_bordersMetaData);
            }
        }

        // Writes Border data to the File
        if (!l_bordersList.isEmpty()) {
            l_writer.write(System.lineSeparator() + AllTheConstants.defaultBordersSeparator + System.lineSeparator());
            for (String l_borderStr : l_bordersList) {
                l_writer.write(l_borderStr + System.lineSeparator());
            }
        }
        l_writer.close();
    }
}

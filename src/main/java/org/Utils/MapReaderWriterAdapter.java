package org.Utils;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

import org.Model.Map;

/**
 * The MapReaderWriterAdapter class is an adapter that allows reading from and writing to a default map file
 * using the ConquestMapReaderWriter implementation.
 *
 * This class extends DefaultMapReaderWriter and implements Serializable.
 */
public class MapReaderWriterAdapter extends DefaultMapReaderWriter implements Serializable {
    /**
     * The ConquestMapReaderWriter object used for reading and writing conquest map files.
     */
    private ConquestMapReaderWriter d_conqMapObj;

    /**
     * Constructs a new MapReaderWriterAdapter with the specified ConquestMapReaderWriter object.
     *
     * @param d_conqMapObj The ConquestMapReaderWriter object to be used for reading and writing.
     */
    public MapReaderWriterAdapter(ConquestMapReaderWriter d_conqMapObj) {
        this.d_conqMapObj = d_conqMapObj;
    }

    /**
     * Reads map data from a default map file using the ConquestMapReaderWriter implementation.
     *
     * @param p_map The Map object to store the read map data.
     * @param p_linesOfFile The list of strings representing the lines of the map file.
     */
    public void readFromDefaultMapFile(Map p_map, List<String> p_linesOfFile) {
        d_conqMapObj.readFromConquestMapFile(p_map, p_linesOfFile);
    }

    /**
     * Writes map data to a default map file using the ConquestMapReaderWriter implementation.
     *
     * @param p_fileName The name of the file to write the map data to.
     * @param p_map The Map object containing the data to be written to the file.
     * @throws IOException If an I/O error occurs during the writing process.
     */
    @Override
    public void writeToDefaultMapFile(String p_fileName, Map p_map) throws IOException {
        d_conqMapObj.writeToConquestMapFile(p_fileName, p_map);
    }
}

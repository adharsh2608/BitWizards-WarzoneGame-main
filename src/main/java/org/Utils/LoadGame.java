package org.Utils;

import org.Constants.AllTheConstants;
import org.Model.Phases.Phase;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

/**
 * The {@code LoadGame} class provides functionality to load a game phase from a serialized file.
 */
public class LoadGame implements Serializable {

    /**
     * Loads a game phase from a serialized file.
     *
     * @param p_filename The name of the file to load the game phase from.
     * @return The loaded game phase, or {@code null} if an error occurs during the loading process.
     */
    public static Phase loadGame(String p_filename) {
        try {
            ObjectInputStream l_loadSaveFile = new ObjectInputStream(new FileInputStream(AllTheConstants.defaultSaveGameLocationPrependString + "/" + p_filename));
            Phase l_phase = (Phase) l_loadSaveFile.readObject();
            l_loadSaveFile.close();
            return l_phase;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}

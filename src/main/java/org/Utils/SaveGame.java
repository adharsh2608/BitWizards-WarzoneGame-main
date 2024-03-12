package org.Utils;

import org.Constants.AllTheConstants;
import org.Model.Phases.Phase;

import java.io.*;

public class SaveGame {
    /**
     * To Save the Game.
     * @param p_phase
     * @param p_filename
     */
    public static void saveGame(Phase p_phase, String p_filename){
        try {
            FileOutputStream l_saveFile =new FileOutputStream(AllTheConstants.defaultSaveGameLocationPrependString + "/" + p_filename);
            ObjectOutputStream l_saveGameOutputStream = new ObjectOutputStream(l_saveFile);
            l_saveGameOutputStream.writeObject(p_phase);
            l_saveGameOutputStream.flush();
            l_saveGameOutputStream.close();
        } catch (Exception l_e) {
            l_e.printStackTrace();
        }
    }
}

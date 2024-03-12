package org.Controller;

import java.io.Serializable;

/**
 * A main class to signify the starting of the game.
 * Usually creates a GameController object and defines the context for the logging.
 */
public class Main implements Serializable {


    public static void main(String[] args) {

        GameEngine l_game  = new GameEngine();
        GameEngine.setLoggerContext("logFile.txt");
        try {
            l_game.getCurrentPhase().initPhase(false);
        } catch (Exception ex){
            System.out.println(ex.getMessage());
        }

    }
}
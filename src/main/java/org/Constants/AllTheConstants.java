package org.Constants;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Constants of the file which should be initialized during the runtime of the Game.
 */
public class AllTheConstants implements Serializable {

    public static final HashSet<String> allMapCommands = new HashSet<>(Arrays.asList("editcontinent","editcountry",
            "editneighbor","showmap","savemap","editmap","loadmap","validatemap","gameplayer","assigncountries","deploy"
            ,"advance","bomb","blockade","airlift","negotiate","exit", "tournament", "savegame", "loadgame"));
    public static final String defaultMapLocationAppendString = "src/main/resources/Map/";
    public static final String defaultLogLocationAppendString = "src/logs/";
    public static final String defaultSaveGameLocationPrependString = "src/main/resources/SavedGames";
    public static final String defaultContinentsSeparator = "[continents]";
    public static final String defaultCountrySeparator = "[countries]";
    public static final String defaultBordersSeparator = "[borders]";
    public static final String conquestContinentSeparator = "[Continents]";
    public static final String conquestCountrySeparator = "[Territories]";
    public static final String RED = "\033[0;31m";
    public static final String GREEN = "\033[0;32m";
    public static final String YELLOW = "\033[0;33m";
    public static final String BLUE = "\033[0;34m";
    public static final String PURPLE = "\033[0;35m";
    public static final String CYAN = "\033[0;36m";
    public static final String WHITE = "\u001B[47m";
    public static final String consoleSepartorString = "*";
    public static final int CONSOLE_WIDTH = 90;
    public static final List<String> COLORS = Arrays.asList(RED, GREEN, YELLOW, BLUE, PURPLE, CYAN);
    public static final List<String> CARDS = Arrays.asList("bomb", "blockade", "airlift", "negotiate");


    public static final List<String> PLAYER_BEHAVIORS = Arrays.asList("Human", "Aggressive", "Random", "Benevolent", "Cheater");
    public static final List<String> TOURNAMENT_PLAYER_BEHAVIORS = Arrays.asList("Aggressive", "Random", "Benevolent", "Cheater");
}


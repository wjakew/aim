
/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package pl.jakubwawak.aim.aim_terminal_engine;

import java.util.ArrayList;

/**
 * Function for parsing user input
 */
public class AIMInputParser {

    public String userInput;

    public ArrayList<String> userInputHistory;

    /**
     * Constructor
     */
    public AIMInputParser(){
        userInputHistory = new ArrayList<>();
    }
}

/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package pl.jakubwawak.aim.aim_dataengine.aim_terminal_engine;

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

    /**
     * Function for setting userInput
     * @param userInput
     */
    public void setUserInput(String userInput){
        this.userInput = userInput;
    }

    /**
     * Function for parsing user input
     */
    public void parse(){

    }
}

/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package pl.jakubwawak.aim.aim_dataengine.aim_terminal_engine;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Task;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_User;
import pl.jakubwawak.aim.website_ui.dialog_windows.obiect_windows.task_windows.InsertTaskWindow;

import java.util.ArrayList;

/**
 * Function for parsing user input
 * Based on document: aim_functionality.drawio
 */
public class AIMInputParser {

    public String userInput;

    public ArrayList<String> userInputHistory;

    VerticalLayout secondaryLayout;

    /**
     * Constructor
     */
    public AIMInputParser(VerticalLayout secondaryLayout){
        this.secondaryLayout = secondaryLayout;
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
        String[] user_word_collection = userInput.split(" ");
        if ( user_word_collection.length > 0 ){
            switch(user_word_collection[0]){
                case "task":
                {
                    task_mind_creator(userInput);
                    break;
                }
            }
        }
        else{
            response_nonKeyWord();
        }
    }

    /**
     * Function for task operation
     * @param user_input
     */
    public void task_mind_creator(String user_input){
        String[] user_word_collection = user_input.split(" ");
        // task -create
        if ( user_input.contains("-create")){
            // task -create
            if( user_word_collection.length == 2 ){
                InsertTaskWindow itw = new InsertTaskWindow(null);
                secondaryLayout.add(itw.main_dialog);
                itw.main_dialog.open();
            }
            // task -create -n name -d description
            else if (user_word_collection.length > 6){
                AIM_Task task = new AIM_Task();
                task.aim_task_name = user_word_collection[3];
                //todo terminal parser creator
            }
        }
        else if (user_input.contains("-link")){

        }
        else if (user_input.contains("-status")){

        }
        else if (user_input.contains("-remove")){

        }
        else if (user_input.contains("-list")){

        }
        else if (user_input.contains("-history")){

        }
    }

    /**
     * Response function for
     * @return String
     */
    public String response_nonKeyWord(){
        return "nothing to procedure - no key words!";
    }
}

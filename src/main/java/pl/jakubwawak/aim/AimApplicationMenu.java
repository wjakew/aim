/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim;

import pl.jakubwawak.maintanance.ConsoleColors;

import java.util.Scanner;

/**
 * Object for creating menu
 */
public class AimApplicationMenu {

    boolean menuStopFlag;

    /**
     * Constructor
     */
    public AimApplicationMenu(){
        System.out.println("aim - menu - application");
        menuStopFlag = true;
    }

    /**
     * Function for printing data in terminal
     * @param content
     */
    void menuPrint(String content){
        System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT+content+ConsoleColors.RESET);
    }

    /**
     * Function for running
     */
    public void run(){
        while(menuStopFlag){
            System.out.print(ConsoleColors.YELLOW_BOLD_BRIGHT+"aim_menu>"+ConsoleColors.RESET);
            Scanner scanner = new Scanner(System.in);
            String userInput = scanner.nextLine();
            mind(userInput);
        }
    }

    /**
     * Function for deciding
     * @param userInput
     */
    void mind(String userInput){
        String[] words = userInput.split(" ");
        if ( words.length > 0 ){
            switch(words[0]){
                case "exit":{
                    menuPrint("Exiting, stopping services!");
                    System.exit(0);
                    break;
                }
                case "mkadmin":
                {
                    if ( words.length == 2 ){
                        String login = words[1];
                        menuPrint("Adding "+login+" to admin role!");
                        int ans = AimApplication.database.makeAdmin(login);
                        if( ans == 1 ){
                            menuPrint("Created "+login+" as admin!");
                        }
                        else if ( ans == 0 ){
                            menuPrint("No such login: "+login);
                        }
                        else{
                            menuPrint("Cannot create admin, check log!");
                        }
                    }
                    break;
                }
            }
        }
        else{
            menuPrint("Wrong input - empty!");
        }
    }
}

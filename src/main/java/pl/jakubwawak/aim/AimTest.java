/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim;

import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Project;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Task;
import pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers.aim_objects_viewers_board.CurrentBoardComposer;
import pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers.aim_objects_viewers_task.TaskColumnLayout;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMProject;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMUser;
import pl.jakubwawak.maintanance.Password_Validator;

/**
 * Object for testing application objects
 */
public class AimTest {

    /**
     * Constructor
     */
    public AimTest(){
        System.out.println("test_flag = 1 - running tests");
        run();
    }

    /**
     * Function for running tests
     */
    void run(){
        try{
            // connection to database
            AimApplication.database.setDatabase_url(AimApplication.connectionStringDebug);
            AimApplication.database.connect();
            if ( AimApplication.database.connected ){
                // run tests here
                Database_AIMUser dau = new Database_AIMUser(AimApplication.database);
                Password_Validator pv = new Password_Validator("Vigor2710Vn");
                dau.loginAIMUser("kubawawak@gmail.com",pv.hash());

                Database_AIMProject dip = new Database_AIMProject(AimApplication.database);
                CurrentBoardComposer cbc = new CurrentBoardComposer();

            }
        }catch(Exception ex){ex.printStackTrace();}
        // closing application
        System.exit(0);
    }
}

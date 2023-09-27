/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim;

import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_BoardTask;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Project;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Task;
import pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers.aim_objects_viewers_board.CurrentBoardComposer;
import pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers.aim_objects_viewers_task.TaskColumnLayout;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMBoard;
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

                Database_AIMBoard dib = new Database_AIMBoard(AimApplication.database);
                Database_AIMProject dip = new Database_AIMProject(AimApplication.database);
                AIM_BoardTask abt = new AIM_BoardTask();
                abt.aim_task_name = "test";
                abt.aim_task_desc = "test desc";
                abt.aim_user_assigned = null;
                abt.status = "IN PROGRESS";
                abt.aim_task_history.add("TEST");
                int ans = dib.insertTaskToBoard(dib.getUserBoardList().get(0),abt);
                System.out.println(ans);
            }
        }catch(Exception ex){ex.printStackTrace();}
        // closing application
        System.exit(0);
    }
}

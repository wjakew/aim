/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim;

import org.bson.Document;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.*;
import pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers.aim_objects_viewers_board.AIM_BoardTaskListLayout;
import pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers.aim_objects_viewers_board.CurrentBoardComposer;
import pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers.aim_objects_viewers_task.TaskColumnLayout;
import pl.jakubwawak.aim.aim_dataengine.database_engine.*;
import pl.jakubwawak.aim.website_ui.widgets.WidgetPanel;
import pl.jakubwawak.maintanance.Password_Validator;

import java.util.ArrayList;

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
                Database_AIMWidgetPanel dawp = new Database_AIMWidgetPanel(AimApplication.database);
                Password_Validator pv = new Password_Validator("Vigor2710Vn");
                dau.loginAIMUser("kubawawak@gmail.com",pv.hash());

                AimApplication.globalConfiguration = AimApplication.database.getGlobalConfiguration();
                AIM_WidgetPanel aimWidgetPanel = dawp.getPanelData();
                AimApplication.currentWidgetPanel = new WidgetPanel(dawp.getPanelData());
                // test data here

                // end of tests
                System.out.println("Test ended!");
            }
        }catch(Exception ex){ex.printStackTrace();}
        // closing application
        System.exit(0);
    }
}

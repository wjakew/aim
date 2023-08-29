/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim;

import pl.jakubwawak.aim.aim_objects.AIM_ApplicationLog;

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
        // connection to database
        AimApplication.database.setDatabase_url(AimApplication.connectionStringDebug);
        // run tests here





        // closing application
        System.exit(0);
    }
}

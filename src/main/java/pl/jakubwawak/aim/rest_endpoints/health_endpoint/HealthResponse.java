/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.rest_endpoints.health_endpoint;

import pl.jakubwawak.aim.AimApplication;

/**
 * Function for creating health response
 */
public class HealthResponse {

    public String applicationBuild;
    public String startupTime;
    public String connectionStatus;

    public String appStatus;


    /**
     * Health response answer
     */
    public HealthResponse(){
        applicationBuild = AimApplication.build;
        startupTime = AimApplication.applicationStartup;
        connectionStatus = Boolean.toString(AimApplication.database.connected);
        appStatus = AimApplication.database.database_url.substring(0,AimApplication.database.database_url.length()/2);
    }

}

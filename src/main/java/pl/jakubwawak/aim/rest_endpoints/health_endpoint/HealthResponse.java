/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.rest_endpoints.health_endpoint;

import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_APIUserKey;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_User;

/**
 * Function for creating health response
 */
public class HealthResponse {

    public String applicationBuild;
    public String startupTime;
    public String connectionStatus;

    public String appStatus;

    public String aim_user_id;

    public String apiActiveFlag;


    /**
     * Health response answer
     */
    public HealthResponse(AIM_User aimUser, AIM_APIUserKey apiUserKey){
        applicationBuild = AimApplication.build;
        startupTime = AimApplication.applicationStartup;
        connectionStatus = Boolean.toString(AimApplication.database.connected);
        appStatus = AimApplication.database.database_url.substring(0,AimApplication.database.database_url.length()/2);
        if ( aimUser != null ){
            aim_user_id = aimUser.aim_user_id.toString();
        }
        else{
            aim_user_id = "wrong_apikey";
        }

        if( apiUserKey != null){
            switch(apiUserKey.apiuserkey_activeflag){
                case 1:
                {
                    apiActiveFlag = "api_key_active";
                    break;
                }
                case 0:
                {
                    apiActiveFlag = "api_key_inactive";
                    break;
                }
                default:
                {
                    apiActiveFlag = "api_key_valuenotfound";
                    break;
                }
            }
        }

    }

    /**
     * Health response answer without logged user
     */
    public HealthResponse(){
        applicationBuild = AimApplication.build;
        startupTime = AimApplication.applicationStartup;
        connectionStatus = Boolean.toString(AimApplication.database.connected);
        appStatus = AimApplication.database.database_url.substring(0,AimApplication.database.database_url.length()/2);
        aim_user_id = "no_apikey";
        apiActiveFlag = "no_apikey";
    }
}

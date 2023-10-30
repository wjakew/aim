/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
*/
package pl.jakubwawak.aim.rest_endpoints.health_endpoint;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_APIUserKey;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_User;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMUser;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_APIKey;

/**
 * Object for creating health endpoint
 */
@RestController
public class HealthEndpoint {

    /**
     * /rest/health endpoint
     * @return HealthResponse
     */
    @GetMapping("/api/health/{aim_apikey}")
    public HealthResponse rest_health(@PathVariable String aim_apikey) {
        try{
            Database_APIKey dak = new Database_APIKey(AimApplication.database);
            Database_AIMUser dau = new Database_AIMUser(AimApplication.database);
            if ( dak.validateUserAPIKey(aim_apikey)!= null ){
                AIM_User user = dak.validateUserAPIKey(aim_apikey);
                AIM_APIUserKey userKey = dak.getUserAPIKey(user);
                if ( userKey.apiuserkey_activeflag == 1 ){
                    return new HealthResponse(user,userKey);
                }
            }
            return new HealthResponse();
        }
        catch(Exception ex){
            AimApplication.database.log("API-FAILED","Api failed: "+ex.toString());
            HealthResponse hr = new HealthResponse();
            hr.appStatus = ex.toString();
            return hr;
        }
    }
}

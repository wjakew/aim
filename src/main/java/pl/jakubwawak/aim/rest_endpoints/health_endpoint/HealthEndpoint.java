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
    @GetMapping("/rest/health/{aim_apikey}")
    public HealthResponse rest_health(@PathVariable String aim_apikey) {
        Database_APIKey dak = new Database_APIKey(AimApplication.database);
        AIM_User user = dak.validateUserAPIKey(aim_apikey);
        if ( user!=null ){
            AIM_APIUserKey userKey = dak.getUserAPIKey(user.aim_user_id);
            if ( userKey.apiuserkey_activeflag == 1 ){
                return new HealthResponse(user,userKey);
            }
        }
        return new HealthResponse();
    }
}

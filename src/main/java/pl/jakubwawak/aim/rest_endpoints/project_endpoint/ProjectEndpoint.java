/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.rest_endpoints.project_endpoint;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Project;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_User;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMProject;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_APIKey;
import pl.jakubwawak.aim.rest_endpoints.Response;
import pl.jakubwawak.maintanance.RandomWordGeneratorEngine;

/**
 * Object for project endpoint response
 */
@RestController
public class ProjectEndpoint {

    @GetMapping("/api/project/project-list/{aim_apikey}")
    public Response listUserProjects(@PathVariable String aim_apikey){
        RandomWordGeneratorEngine rwge = new RandomWordGeneratorEngine();
        String response_api_code = rwge.generateRandomString(20,true,false);
        try{
            Response response = new Response();
            Database_APIKey dak = new Database_APIKey(AimApplication.database);
            AIM_User user = dak.validateUserAPIKey(aim_apikey);
            if (user!=null){
                Database_AIMProject dap = new Database_AIMProject(AimApplication.database);
                for(AIM_Project ap : dap.getUserProjects(user)){
                    ap.aim_owner = null;
                    response.response_content.add(ap.prepareDocument());
                }
                response.response_status = "collection_loaded";
                response.response_title = "project/project-list";
                response.response_owner_email = user.aim_user_email;
                response.response_status_info = "successfully selected objects";
                response.response_api_code = response_api_code;
                AimApplication.database.log("API-RESPONSE","Created response for "+response.response_title+" for "+user.aim_user_email+" code: "+response.response_api_code);
                return response;
            }
            else{
                response.response_status = "bad_apikey";
                response.response_title = "project/project-list";
                response.response_status_info = "Provided key:"+aim_apikey+" is not recognized.";
                response.response_api_code = response_api_code;
                AimApplication.database.log("API-BADAPIKEY","Got request for "+response.response_title+" with key: "+aim_apikey);
                return  response;
            }
        }catch(Exception ex){
            Response response = new Response();
            response.response_status = "api_error";
            response.response_title = "project/project-list";
            response.response_status_info = ex.toString();
            response.response_api_code = response_api_code;
            AimApplication.database.log("API-FAILED","Failed to get project list data ("+ex.toString()+") code: "+response_api_code);
            return response;
        }
    }
}

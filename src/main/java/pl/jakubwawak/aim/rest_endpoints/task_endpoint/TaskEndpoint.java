/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.rest_endpoints.task_endpoint;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Task;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_User;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMTask;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_APIKey;
import pl.jakubwawak.aim.rest_endpoints.Response;
import pl.jakubwawak.maintanance.RandomWordGeneratorEngine;

import javax.xml.crypto.Data;
import java.util.ArrayList;

/**
 * Object for task endpoint response
 */
@RestController
public class TaskEndpoint {

    /*
    Default response body

    RandomWordGeneratorEngine rwge = new RandomWordGeneratorEngine();
        String response_api_code = rwge.generateRandomString(20,true,false);
        try{
            Database_APIKey dak = new Database_APIKey(AimApplication.database);
            AIM_User user = dak.validateUserAPIKey(aim_apikey);
            if (user!=null){
                // api key found - realize the get

            }
            else{
                Response response = new Response();
                response.response_status = "bad_apikey";
                response.response_status_info = "Provided key:"+aim_apikey+" is not recognized.";
                response.response_api_code = response_api_code;
                return  response;
            }
        }catch(Exception ex){
            Response response = new Response();
            response.response_status = "api_error";
            response.response_status_info = ex.toString();
            response.response_api_code = response_api_code;
            AimApplication.database.log("API-FAILED","Failed to get task list data ("+ex.toString()+") code: "+response_api_code);
            return response;
        }
     */

    /**
     * Rest endpoint for listing user tasks
     * /api/task/task-list/{list_type}/{aim_apikey}
     * @param list_type - type of the task NEW, IN PROGRESS, DONE
     * @param aim_apikey
     * @return
     */
    @GetMapping("/api/task/task-list/{list_type}/{aim_apikey}")
    public Response listUserTasks(@PathVariable String list_type,@PathVariable String aim_apikey){
        RandomWordGeneratorEngine rwge = new RandomWordGeneratorEngine();
        String response_api_code = rwge.generateRandomString(20,true,false);
        try{
            Response response = new Response();
            Database_APIKey dak = new Database_APIKey(AimApplication.database);
            AIM_User user = dak.validateUserAPIKey(aim_apikey);
            if (user!=null){
                // api key found - realize the get
                Database_AIMTask dat = new Database_AIMTask(AimApplication.database);
                ArrayList<AIM_Task> data = dat.getUserTaskCollection(user.aim_user_id,list_type);
                for(AIM_Task task : data){
                    task.aim_task_owner = null;
                    response.response_content.add(task.prepareDocument());
                }
                response.response_status = "collection_loaded";
                response.response_title = "task/task-list";
                response.response_owner_email = user.aim_user_email;
                response.response_status_info = "successfully selected objects";
                response.response_api_code = response_api_code;
                AimApplication.database.log("API-RESPONSE","Created response for "+response.response_title+" for "+user.aim_user_email+" code: "+response.response_api_code);
                return response;
            }
            else{
                response.response_status = "bad_apikey";
                response.response_title = "task/task-list";
                response.response_status_info = "Provided key:"+aim_apikey+" is not recognized.";
                response.response_api_code = response_api_code;
                AimApplication.database.log("API-BADAPIKEY","Got request for "+response.response_title+" with key: "+aim_apikey);
                return  response;
            }
        }catch(Exception ex){
            Response response = new Response();
            response.response_status = "api_error";
            response.response_title = "task/task-list";
            response.response_status_info = ex.toString();
            response.response_api_code = response_api_code;
            AimApplication.database.log("API-FAILED","Failed to get task list data ("+ex.toString()+") code: "+response_api_code);
            return response;
        }
    }
}

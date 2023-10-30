/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.rest_endpoints;

import org.bson.Document;

import java.util.ArrayList;

/**
 * Object for storing response information for the api
 */
public class Response {

    public String response_status;                    // response status
    public String response_status_info;               // response status code desc
    public String response_title;                     // name of the response object (endpoint name)
    public ArrayList<Document> response_content;       // collection for storing documents

    public String response_owner_email;               // field for storing user email

    public String response_api_code;                  // field for storing unique code

    /**
     * Constructor
     */
    public Response(){
        response_status = "no_status";
        response_status_info = "response init";
        response_title = "no_title";
        response_content = new ArrayList<>();
        response_owner_email = "no_owner";
        response_api_code = "blank_code";
    }

}

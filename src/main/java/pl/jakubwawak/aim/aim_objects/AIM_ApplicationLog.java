/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package pl.jakubwawak.aim.aim_objects;

import org.bson.Document;
import pl.jakubwawak.aim.AimApplication;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Object for storing application log
 */
public class AIM_ApplicationLog {

    public String log_code;
    public String log_desc;
    public Date log_timestamp;
    public String currentlyLoggedUser;

    public String category;

    /**
     * Constructor
     */
    public AIM_ApplicationLog(){
        log_code = "";
        log_desc = "";
        log_timestamp = new Date();
        category = "NRL";
        if (AimApplication.loggedUser != null){
            currentlyLoggedUser = AimApplication.loggedUser.aim_user_email;
        }
        else{
            currentlyLoggedUser = "non_logged";
        }
    }

    /**
     * Construction with params
     * @param code
     * @param desc
     */
    public AIM_ApplicationLog(String code, String desc){
        log_code = code;
        log_desc = desc;
        log_timestamp = new Date();
        if (code.contains("FAILED") || code.contains("ERROR")){
            category = "ERR";
        }
        else{
            category = "NRL";
        }
        if (AimApplication.loggedUser != null){
            currentlyLoggedUser = AimApplication.loggedUser.aim_user_email;
        }
        else{
            currentlyLoggedUser = "non_logged";
        }
    }

    /**
     * Constructor with database support
     */
    public AIM_ApplicationLog(Document log_document){
        log_code = log_document.getString("log_code");
        log_desc = log_document.getString("log_desc");
        log_timestamp = log_document.get("log_timestamp",Date.class);
        currentlyLoggedUser = log_document.getString("currentlyLoggedUser");
        category = log_document.getString(category);
    }

    /**
     * Function for preparing document containing application log data
     * @return Document
     */
    public Document prepareDocument(){
        Document document = new Document();
        document.append("log_code",log_code);
        document.append("log_desc",log_desc);
        document.append("log_timestamp",log_timestamp);
        document.append("currentlyLoggedUser",currentlyLoggedUser);
        document.append("category",category);
        return document;
    }
}

/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package pl.jakubwawak.aim.aim_dataengine.aim_objects;

import org.bson.Document;
import org.bson.types.ObjectId;
import pl.jakubwawak.aim.AimApplication;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Object for storing AIM_Task data
 */
public class AIM_Task {

    public ObjectId aim_task_id;
    public String aim_task_name;
    public String aim_task_desc;
    public Document aim_task_owner;

    public String status; // NEW, IN PROGRESS, DONE
    public Date aim_task_timestamp;
    public Date aim_task_deadline;

    public List<String> aim_task_history;

    /**
     * Constructor
     */
    public AIM_Task() {
        this.aim_task_id = null;
        this.aim_task_name = "";
        this.aim_task_desc = "";
        this.status = "NEW";
        this.aim_task_owner = AimApplication.loggedUser.prepareDocument();
        this.aim_task_history = new ArrayList<>();
        this.aim_task_timestamp = new Date();
        this.aim_task_deadline = new Date();
    }

    /**
     * Constructor with database support
     */
    public AIM_Task(Document task_document) {
        this.aim_task_id = task_document.getObjectId("_id");
        this.aim_task_name = task_document.getString("aim_task_name");
        this.aim_task_desc = task_document.getString("aim_task_desc");
        this.status = task_document.getString("status");
        this.aim_task_owner = task_document.get("aim_task_owner",Document.class);
        this.aim_task_history = task_document.getList("aim_task_history",String.class);
        this.aim_task_timestamp = task_document.get("aim_task_timestamp",Date.class);
        this.aim_task_deadline = task_document.get("aim_task_deadline",Date.class);
    }

    /**
     * Function for preparing document storing AIM_Task data
     */
    public Document prepareDocument(){
        Document task_document = new Document();
        task_document.append("aim_task_name",aim_task_name);
        task_document.append("aim_task_desc",aim_task_desc);
        task_document.append("status",status);
        task_document.append("aim_task_owner",aim_task_owner);
        task_document.append("aim_task_history",aim_task_history);
        task_document.append("aim_task_timestamp",aim_task_timestamp);
        task_document.append("aim_task_deadline",aim_task_deadline);
        return task_document;
    }

    /**
     * Function for getting AIM_UserData
     * @return AIM_User
     */
    public AIM_User getTaskOwner(){
        return new AIM_User(aim_task_owner);
    }
}

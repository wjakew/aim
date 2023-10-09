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
     * Constructor for coping objects
     * @param taskToCopy
     */
    public AIM_Task(AIM_Task taskToCopy){
        this.aim_task_id = taskToCopy.aim_task_id;
        this.aim_task_name = taskToCopy.aim_task_name;
        this.aim_task_desc = taskToCopy.aim_task_desc;
        this.status = taskToCopy.status;
        this.aim_task_owner = taskToCopy.aim_task_owner;
        this.aim_task_history = taskToCopy.aim_task_history;
        this.aim_task_timestamp = taskToCopy.aim_task_timestamp;
        this.aim_task_deadline = taskToCopy.aim_task_deadline;
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

    public ObjectId getAim_task_id() {
        return aim_task_id;
    }

    public String getAim_task_name() {
        return aim_task_name;
    }

    public String getAim_task_desc() {
        return aim_task_desc;
    }

    public Document getAim_task_owner() {
        return aim_task_owner;
    }

    public String getAim_task_owner_glance(){
        return aim_task_owner.getString("aim_user_email");
    }

    public String getStatus() {
        return status;
    }

    public Date getAim_task_timestamp() {
        return aim_task_timestamp;
    }

    public Date getAim_task_deadline() {
        return aim_task_deadline;
    }
}

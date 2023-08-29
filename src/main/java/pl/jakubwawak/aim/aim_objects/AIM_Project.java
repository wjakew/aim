/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package pl.jakubwawak.aim.aim_objects;

import org.bson.Document;
import org.bson.types.ObjectId;
import pl.jakubwawak.aim.AimApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Object for storing project infomation
 */
public class AIM_Project {

    public ObjectId aim_project_id;

    public String aim_project_name;
    public String aim_project_desc;
    public AIM_User aim_owner;
    public List<AIM_Task> task_list;

    public List<String> project_history;

    /**
     * Constructor
     */
    public AIM_Project() {
        this.aim_project_id = null;
        this.aim_project_name = "";
        this.aim_project_desc = "";
        this.aim_owner = AimApplication.loggedUser;
        this.task_list = new ArrayList<>();
        this.project_history = new ArrayList<>();
    }

    /**
     * Constructor with database support
     */
    public AIM_Project(Document project_document) {
        this.aim_project_id = project_document.getObjectId("_id");
        this.aim_project_name = project_document.getString("aim_project_name");
        this.aim_project_desc = project_document.getString("aim_project_desc");
        this.aim_owner = project_document.get("aim_owner",AIM_User.class);
        this.task_list = project_document.getList("task_list", AIM_Task.class);
        this.project_history = project_document.getList("project_history",String.class);
    }

    /**
     * Function for preparing document data with AIM_Project data
     * @return Document
     */
    public Document prepareDocument(){
        Document project_document = new Document();
        project_document.append("aim_project_name",aim_project_name);
        project_document.append("aim_project_desc",aim_project_desc);
        project_document.append("aim_owner",aim_owner);
        project_document.append("task_list",task_list);
        project_document.append("project_history",project_history);
        return project_document;
    }
}

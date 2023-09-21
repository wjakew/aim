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
import java.util.List;

/**
 * Object for storing project infomation
 */
public class AIM_Project {

    public ObjectId aim_project_id;

    public String aim_project_name;
    public String aim_project_desc;
    public Document aim_owner;
    public List<Document> task_list;

    public List<String> project_history;

    public List<Document> project_members;

    /**
     * Constructor
     */
    public AIM_Project() {
        this.aim_project_id = null;
        this.aim_project_name = "";
        this.aim_project_desc = "";
        this.aim_owner = AimApplication.loggedUser.prepareDocument();
        this.task_list = new ArrayList<>();
        this.project_history = new ArrayList<>();
        this.project_members = new ArrayList<>();
    }

    /**
     * Constructor with database support
     */
    public AIM_Project(Document project_document) {
        this.aim_project_id = project_document.getObjectId("_id");
        this.aim_project_name = project_document.getString("aim_project_name");
        this.aim_project_desc = project_document.getString("aim_project_desc");
        this.aim_owner = project_document.get("aim_owner",Document.class);
        this.task_list = project_document.getList("task_list", Document.class);
        this.project_history = project_document.getList("project_history",String.class);
        this.project_members = project_document.getList("project_members",Document.class);
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
        project_document.append("project_members",project_members);
        return project_document;
    }

    /**
     * Function for loading tasks from documents
     * @return ArrayList
     */
    public ArrayList<AIM_Task> getTaskCollection(){
        ArrayList<AIM_Task> data = new ArrayList<>();
        for(Document taskDocument : task_list){
            data.add(new AIM_Task(taskDocument));
        }
        return data;
    }

    /**
     * Function for opening task amount
     * @return Integer
     */
    public int openTaskAmount(){
        int amount = 0;
        for(AIM_Task task : getTaskCollection()){
            if ( task.status.equals("NEW") || task.status.equals("IN PROGRESS")){
                amount++;
            }
        }
        return amount;
    }
}

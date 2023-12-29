/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.aim_dataengine.aim_objects.codingproject;

import org.bson.Document;
import org.bson.types.ObjectId;
import pl.jakubwawak.aim.AimApplication;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Object for storing coding project data
 */
public class AIM_CodingTask {

    public ObjectId aim_codingtask_id;
    public ObjectId aim_codingproject_id;

    public ObjectId aim_user_id; // owner ID

    public String aim_codingtask_status; // NEW, IN PROGRESS, FROZEN, DONE

    public String aim_codingtask_tag; // tag field for eg version number or category

    public String aim_codingtask_timestamp;

    public String aim_codingtask_name;

    public String aim_codingtask_desc;

    /*
        document layout
        comment_text: info
        comment_time: time
        comment_email: email
     */
    public List<Document> aim_codingtask_comments;

    /*
        document layout
        history_category: CATEGORY
        history_time: TIME
        history_text: TEXT
        history_user: EMAIL
     */
    public List<Document> aim_codingtask_history;

    /**
     * Constructor
     */
    public AIM_CodingTask() {
        this.aim_codingtask_id = null;
        this.aim_codingproject_id = null;
        this.aim_codingtask_status = "NEW";
        this.aim_user_id = AimApplication.loggedUser.aim_user_id;
        this.aim_codingtask_tag = "";
        this.aim_codingtask_timestamp = LocalDateTime.now().toString();
        this.aim_codingtask_name = "";
        this.aim_codingtask_desc = "";
        this.aim_codingtask_comments = new ArrayList<>();
        this.aim_codingtask_history = new ArrayList<>();
    }

    /**
     * Constructor with document support
     * @param codingDocument
     */
    public AIM_CodingTask(Document codingDocument){
        this.aim_codingtask_id = codingDocument.getObjectId("_id");
        this.aim_codingproject_id = codingDocument.getObjectId("aim_codingproject_id");
        this.aim_user_id = codingDocument.getObjectId("aim_user_id");
        this.aim_codingtask_status = codingDocument.getString("aim_codingtask_status");
        this.aim_codingtask_tag = codingDocument.getString("aim_codingtask_tag");
        this.aim_codingtask_timestamp = codingDocument.getString("aim_codingtask_timestamp");
        this.aim_codingtask_name = codingDocument.getString("aim_codingtask_name");
        this.aim_codingtask_desc = codingDocument.getString("aim_codingtask_desc");
        this.aim_codingtask_comments = codingDocument.getList("aim_codingtask_comments",Document.class);
        this.aim_codingtask_history = codingDocument.getList("aim_codingtask_history",Document.class);
    }

    /**
     * Function for loading simple short description
     * @return String
     */
    public String getCodingTaskData(){
        return getAim_codingtask_status()+"/"+getAim_codingproject_id();
    }

    /**
     * Function for adding history to the record
     * @param historyCategory
     * @param historyText
     */
    public void addHistory(String historyCategory, String historyText){
        /*
        document layout
        history_category: CATEGORY
        history_time: TIME
        history_text: TEXT
        history_user: EMAIL
     */
        Document historyDocument = new Document();
        historyDocument.append("history_category",historyCategory);
        historyDocument.append("history_time",LocalDateTime.now());
        historyDocument.append("history_text",historyText);
        historyDocument.append("history_user",AimApplication.loggedUser.aim_user_email);
        aim_codingtask_history.add(historyDocument);
    }

    /**
     * getter for codingtask_name
     * @return String
     */
    public String getAim_codingtask_name(){return aim_codingtask_name;}

    /**
     * getter for codingtask_timestamp
     * @return String
     */
    public String getAim_codingtask_timestamp(){return aim_codingtask_timestamp;}

    public String getAim_codingtask_status(){return aim_codingtask_status;}

    /**
     * Function for getting coding project id
     * @return String
     */
    public String getAim_codingproject_id(){
        if ( aim_codingproject_id == null ){
            return "STANDALONE";
        }
        else{
            return aim_codingproject_id.toString();
        }
    }
    /**
     * Function for checking if object is empty
     * @return boolean
     */
    public boolean isEmpty(){
        return aim_codingtask_name.isEmpty();
    }

    /**
     * Function for preparing documents
     * @return Document
     */
    public Document prepareDocument(){
        Document codingDocument = new Document();
        codingDocument.append("aim_codingproject_id",aim_codingproject_id);
        codingDocument.append("aim_user_id",aim_user_id);
        codingDocument.append("aim_codingtask_tag",aim_codingtask_tag);
        codingDocument.append("aim_codingtask_status",aim_codingtask_status);
        codingDocument.append("aim_codingtask_timestamp",aim_codingtask_timestamp);
        codingDocument.append("aim_codingtask_name",aim_codingtask_name);
        codingDocument.append("aim_codingtask_desc",aim_codingtask_desc);
        codingDocument.append("aim_codingtask_comments",aim_codingtask_comments);
        codingDocument.append("aim_codingtask_history",aim_codingtask_history);
        return codingDocument;
    }
}

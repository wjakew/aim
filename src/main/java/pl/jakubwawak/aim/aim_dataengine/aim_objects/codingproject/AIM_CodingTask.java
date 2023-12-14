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


    public List<Document> aim_codingtask_history;

    /**
     * Constructor
     */
    public AIM_CodingTask() {
        this.aim_codingtask_id = null;
        this.aim_codingproject_id = null;
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
        this.aim_codingtask_id = codingDocument.getObjectId("aim_codingtask_id");
        this.aim_codingproject_id = codingDocument.getObjectId("aim_codingproject_id");
        this.aim_user_id = codingDocument.getObjectId("aim_user_id");
        this.aim_codingtask_tag = codingDocument.getString("aim_codingtask_tag");
        this.aim_codingtask_timestamp = codingDocument.getString("aim_codingtask_timestamp");
        this.aim_codingtask_name = codingDocument.getString("aim_codingtask_name");
        this.aim_codingtask_desc = codingDocument.getString("aim_codingtask_desc");
        this.aim_codingtask_comments = codingDocument.getList("aim_codingtask_comments",Document.class);
        this.aim_codingtask_history = codingDocument.getList("aim_codingtask_history",Document.class);
    }

    /**
     * Function for preparing documents
     * @return Document
     */
    public Document prepareDocument(){
        Document codingDocument = new Document();
        codingDocument.append("aim_codingtask_id",aim_codingtask_id);
        codingDocument.append("aim_codingproject_id",aim_codingproject_id);
        codingDocument.append("aim_user_id",aim_user_id);
        codingDocument.append("aim_codingtask_tag",aim_codingtask_tag);
        codingDocument.append("aim_codingtask_timestamp",aim_codingtask_timestamp);
        codingDocument.append("aim_codingtask_name",aim_codingtask_name);
        codingDocument.append("aim_codingtask_desc",aim_codingtask_desc);
        codingDocument.append("aim_codingtask_comments",aim_codingtask_comments);
        codingDocument.append("aim_codingtask_history",aim_codingtask_history);
        return codingDocument;
    }
}

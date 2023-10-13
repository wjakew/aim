/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.aim_dataengine.aim_objects;

import org.bson.Document;

public class AIM_BoardTask extends AIM_Task{

    public Document aim_user_assigned;

    /**
     * Constructor
     */
    public AIM_BoardTask(){
        super();
        aim_user_assigned = null;
    }

    /**
     * Constuctor from AIM_Task
     * @param task
     */
    public AIM_BoardTask(AIM_Task task){
        super(task);
        aim_user_assigned = null;
    }

    /**
     * Constructor from document
     * @param taskBDocument
     */
    public AIM_BoardTask(Document taskBDocument){
        super(taskBDocument);
        aim_user_assigned = taskBDocument.get("aim_user_assigned",Document.class);
    }

    /**
     * Function for loading document for database usage
     * @return Document
     */
    public Document prepareDocument(){
        Document taskDocument = super.prepareDocument();
        taskDocument.append("aim_user_assigned",aim_user_assigned);
        return taskDocument;
    }

    public String getAssignedUserGlance(){
        if (aim_user_assigned != null){
            return aim_user_assigned.getString("aim_user_email");
        }
        return "All";
    }
}

/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package pl.jakubwawak.database_engine;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.conversions.Bson;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_objects.AIM_Task;
import org.bson.Document;
import pl.jakubwawak.aim.aim_objects.AIM_User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Object for maintaining task data on database
 */
public class Database_AIMTask {

    public Database_Connector database;

    /**
     * Constructor
     * @param database
     */
    public Database_AIMTask(Database_Connector database){
        this.database = database;
    }

    /**
     * Function for getting task collection from database based on logged user
     * @return ArrayList
     */
    public ArrayList<AIM_Task> getTaskCollection(){
        ArrayList<AIM_Task> data = new ArrayList<>();
        try{
            MongoCollection<Document> task_collection = database.get_data_collection("aim_task");
            FindIterable<Document> userTaskCollection = task_collection.find();
            for(Document task_document: userTaskCollection){
                AIM_User aimUser = task_document.get("aim_task_owner",AIM_User.class);
                if ( aimUser.aim_user_email.equals(AimApplication.loggedUser.aim_user_email)){
                    data.add(new AIM_Task(task_document));
                }
            }
            database.log("DB-TASK-LOADCOLLECTION","Loaded "+data.size()+" objects from database");
        }catch(Exception ex){
            database.log("DB-TASK-LOADCOLLECTION-FAILED","Failed to get user task collection ("+ex.toString()+")");
        }
        return data;
    }

    /**
     * Function for getting  task object from database based on name
     * @return AIM_Task
     */
    public AIM_Task getTask(String aimTaskName){
        AIM_Task data = null;
        try{
            MongoCollection<Document> task_collection = database.get_data_collection("aim_task");
            FindIterable<Document> userTaskCollection = task_collection.find();
            for(Document task_document : userTaskCollection){
                if ( task_document.getString("aim_task_name").equals(aimTaskName) ){
                    data = new AIM_Task(task_document);
                    break;
                }
            }

            if ( data == null ){
                for(Document task_document : userTaskCollection){
                    if ( task_document.getString("aim_task_name").contains(aimTaskName) ){
                        data = new AIM_Task(task_document);
                        break;
                    }
                }
            }
        }catch(Exception ex){
            database.log("DB-GETTASK-FAILED","Failed to get task object ("+ex.toString()+")");
        }
        return data;
    }

    /**
     * Function for updating task status
     * @param taskToUpdate
     * @return String
     */
    public int updateAIMTaskStatus(AIM_Task taskToUpdate, String newStatus){
        try{
            MongoCollection<Document> task_collection = database.get_data_collection("aim_task");
            Bson updates = Updates.combine(
                    Updates.set("status",newStatus)
                    // TODO need to add record to task history
            );
            UpdateResult result = task_collection.updateOne(taskToUpdate.prepareDocument(), updates);
            database.log("DB-TASK-UPDATESTATUS","Updated task ("+taskToUpdate.aim_task_id.toString()+"), set: "+newStatus);
            return 1;
        }catch(Exception ex){
            database.log("DB-TASK-UPDATEFAILED","Failed to update task ("+ex.toString()+")");
            return -1;
        }
    }

    /**
     * Function for loading task data
     * @param aimTask
     * @return List
     */
    public List<String> getTaskHistory(AIM_Task aimTask){
        return aimTask.aim_task_history;
    }

    /**
     * Function for setting new task owner
     * @param taskToUpdate
     * @return Integer
     */
    public String remove(AIM_Task taskToUpdate){
        String sources = "";
        try{
            MongoCollection<Document> task_collection = database.get_data_collection("aim_task");
            DeleteResult workDone = task_collection.deleteOne(taskToUpdate.prepareDocument());
            if (workDone.wasAcknowledged()){
                sources = sources + "general ";
            }
            // TODO remove task from boards and projects
            return sources;
        }catch(Exception ex){
            database.log("DB-TASK-REMOVEFAILED","Failed to remove task ("+ex.toString()+")");
            return "error";
        }
    }

    /**
     * Function for adding new task to application
     * @param newaimTask
     * @return Integer
     */
    public int insertAIMTask(AIM_Task newaimTask){
        try{
            MongoCollection<Document> task_collection = database.get_data_collection("aim_task");
            task_collection.insertOne(newaimTask.prepareDocument());
            return 1;
        }catch(Exception ex){
            database.log("DB-AIM-TASK-INSERT","Failed to insert task on database ("+ex.toString()+")");
            return -1;
        }
    }
}

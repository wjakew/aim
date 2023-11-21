/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package pl.jakubwawak.aim.aim_dataengine.database_engine;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Project;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Task;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_User;
import org.bson.Document;
import pl.jakubwawak.maintanance.RandomWordGeneratorEngine;

import java.time.LocalDateTime;
import java.time.ZoneId;
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
                AIM_User aimUser = new AIM_User(task_document.get("aim_task_owner",Document.class));
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
     * Function for loading all tasks from database
     * @return ArrayList
     */
    public ArrayList<AIM_Task> getAllTaskCollection(){
        ArrayList<AIM_Task> data = new ArrayList<>();
        try{
            MongoCollection<Document> task_collection = database.get_data_collection("aim_task");
            FindIterable<Document> userTaskCollection = task_collection.find();
            for(Document task_document: userTaskCollection){
                data.add(new AIM_Task(task_document));
            }
            database.log("DB-TASK-LOADCOLLECTION","Loaded "+data.size()+" objects from database");
        }catch(Exception ex){
            database.log("DB-TASK-LOADCOLLECTION-FAILED","Failed to get user task collection ("+ex.toString()+")");
        }
        return data;
    }

    /**
     * Function for loading task collection for given user
     * @param aim_user - user object from database
     * @param source - NEW IN PROGRESS DONE
     * @return ArrayList
     */
    public ArrayList<AIM_Task> getUserTaskCollection(AIM_User aim_user, String source){
        ArrayList<AIM_Task> data = new ArrayList<>();
        Database_AIMUser dau = new Database_AIMUser(AimApplication.database);
        try{
            MongoCollection<Document> task_collection = database.get_data_collection("aim_task");
            FindIterable<Document> userTaskCollection = task_collection.find();
            for(Document task_document: userTaskCollection){
                Document userDocument = task_document.get("aim_task_owner",Document.class);
                if ( userDocument.getString("aim_user_email").equals(aim_user.aim_user_email)){
                    data.add(new AIM_Task(task_document));
                }
            }
            database.log("DB-TASK-USRTASKCLC","Loaded "+data.size()+" tasks with source "+source+" for user ("+aim_user.aim_user_email+")");
        }catch(Exception ex){
            database.log("DB-TASK-USRTASKCLC-FAILED","Failed to get user task collection ("+ex.toString()+")");
        }
        return data;
    }

    /**
     * Function for sharing task
     * @param taskToShare
     * @return String
     */
    public String shareTask(AIM_Task taskToShare){
        try{
            MongoCollection<Document> sharing_collection = database.get_data_collection("aim_share");
            Document sharing_document = sharing_collection.find(new Document("task_id", taskToShare.aim_task_id)).first();
            RandomWordGeneratorEngine rwge = new RandomWordGeneratorEngine();
            if ( sharing_document == null ){
                String sharingCode = rwge.generateRandomString(10,true,false);
                Document document = new Document();
                document.append("task_id",taskToShare.aim_task_id);
                document.append("type","task");
                document.append("sharing_code",sharingCode);
                InsertOneResult result = sharing_collection.insertOne(document);
                if ( result.wasAcknowledged() ){
                    database.log("DB-SHARING-TASK","Shared task ("+taskToShare.aim_task_id.toString()+") sharing code: "+sharingCode);
                    return sharingCode;
                }
                database.log("DB-SHARING-TASK","Nothing to update on table, probably ");
                return null;
            }
            database.log("DB-SHARING-ALREADY","Task already shared ("+sharing_document.getString("sharing_code")+")");
            return null;
        }catch(Exception ex){
            database.log("DB-TASK-SHARE-FAILED","Failed to share task ("+ex.toString()+")");
            return null;
        }
    }

    /**
     * Function for removing task share
     * @param taskToShare
     * @return Integer
     */
    public int removeShareTask(AIM_Task taskToShare){
        try {
            MongoCollection<Document> sharing_collection = database.get_data_collection("aim_share");
            Document sharing_document = sharing_collection.find(new Document("task_id", taskToShare.aim_task_id)).first();
            if ( sharing_document != null ){
                DeleteResult result = sharing_collection.deleteOne(sharing_document);
                if (result.getDeletedCount() > 0){
                    database.log("DB-TASK-SHARE-REMOVE","Share of the task("+taskToShare.aim_task_id.toString()+") removed!");
                    return 1;
                }
            }
            return 0;
        }catch(Exception ex){
            database.log("DB-TASK-SHARE-REMOVE-FAILED","Failed to remove sharing on task ("+ex.toString()+")");
            return -1;
        }
    }

    /**
     * Function for checking if project is shared
     * @param taskToShare
     * @return String
     */
    public String checkShare(AIM_Task taskToShare){
        try{
            MongoCollection<Document> sharing_collection = database.get_data_collection("aim_share");
            Document sharing_document = sharing_collection.find(new Document("task_id", taskToShare.aim_task_id)).first();
            if ( sharing_document == null ){
                return null;
            }
            database.log("DB-SHARING-ALREADY","Task already shared ("+sharing_document.getString("sharing_code")+")");
            return sharing_document.getString("sharing_code");
        }
        catch(Exception ex){
            database.log("DB-TASK-SHARE-FAILED","Failed to share task ("+ex.toString()+")");
            return null;
        }
    }

    /**
     * Function for getting taskCollection
     * @return ArrayList
     */
    public ArrayList<AIM_Task> getNewTaskCollection(){
        ArrayList<AIM_Task> allUserTasks = getTaskCollection();
        ArrayList<AIM_Task> data = new ArrayList<>();
        for(AIM_Task task : allUserTasks){
            if ( task.status.equals("NEW")){
                data.add(task);
            }
        }
        database.log("DB-LOAD-NEWTASKCOLLECTION","Loaded new "+data.size()+" tasks!");
        return data;
    }

    /**
     * Function for getting taskCollection
     * @return ArrayList
     */
    public ArrayList<AIM_Task> getInProgressTaskCollection(){
        ArrayList<AIM_Task> allUserTasks = getTaskCollection();
        ArrayList<AIM_Task> data = new ArrayList<>();
        for(AIM_Task task : allUserTasks){
            if ( task.status.equals("IN PROGRESS")){
                data.add(task);
            }
        }
        database.log("DB-LOAD-INPROGRESSTASKCOLLECTION","Loaded new "+data.size()+" tasks!");
        return data;
    }

    /**
     * Function for getting taskCollection
     * @return ArrayList
     */
    public ArrayList<AIM_Task> getDoneTaskCollection(){
        ArrayList<AIM_Task> allUserTasks = getTaskCollection();
        ArrayList<AIM_Task> data = new ArrayList<>();
        for(AIM_Task task : allUserTasks){
            if ( task.status.equals("DONE")){
                data.add(task);
            }
        }
        database.log("DB-LOAD-DONECOLLECTION","Loaded new "+data.size()+" tasks!");
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
                    database.log("BD-GETTASK","Found task ("+aimTaskName+") as ("+task_document.getObjectId("_id").toString()+")");
                    data = new AIM_Task(task_document);
                    break;
                }
            }
            if ( data == null ){
                for(Document task_document : userTaskCollection){
                    if ( task_document.getString("aim_task_name").contains(aimTaskName) ){
                        data = new AIM_Task(task_document);
                        database.log("BD-GETTASK","Found task ("+aimTaskName+") as ("+task_document.getObjectId("_id").toString()+")");
                        break;
                    }
                }
            }
        }catch(Exception ex){
            database.log("DB-GETTASK-FAILED","Failed to get task object ("+ex.toString()+")");
        }
        if ( data == null ){
            database.log("DB-GETTASK", "No task found for name ("+aimTaskName+")");
        }
        return data;
    }

    /**
     * Function for getting task object from database based on ID
     * @param task_id
     * @return AIM_Task
     */
    public AIM_Task getTask(ObjectId task_id){
        AIM_Task data = null;
        try{
            MongoCollection<Document> task_collection = database.get_data_collection("aim_task");
            FindIterable<Document> userTaskCollection = task_collection.find();
            for(Document task_document : userTaskCollection){
                if ( task_document.getObjectId("_id").equals(task_id) ){
                    database.log("BD-GETTASK","Found task ID("+task_id.toString()+") as ("+task_document.getObjectId("_id").toString()+")");
                    data = new AIM_Task(task_document);
                    break;
                }
            }
        }catch(Exception ex){
            database.log("DB-GETTASK-FAILED","Failed to get task object ("+ex.toString()+")");
        }
        if ( data == null ){
            database.log("DB-GETTASK", "No task found for name ID("+task_id.toString()+")");
        }
        return data;
    }

    /**
     * Function for getting task object from database based on sharing code
     * @param sharing_code
     * @return AIM_Task
     */
    public AIM_Task getTaskBySharedCode(String sharing_code){
        Database_ShareCode dsc = new Database_ShareCode(AimApplication.database);
        Document sharingDocument = dsc.getShareCode(sharing_code);
        if ( sharingDocument != null ){
            try{
                MongoCollection<Document> task_collection = database.get_data_collection("aim_task");
                Document task_document = task_collection.find(new Document("_id",sharingDocument.getObjectId("task_id"))).first();
                if (task_document != null) {
                    database.log("DB-GETTASK-SHARED", "Found task for sharing code, ID(" + task_document.getObjectId("_id").toString());
                    return new AIM_Task(task_document);
                }
            }catch(Exception ex){
                database.log("DB-GETTASK-FAILED","Failed to get task object ("+ex.toString()+")");
                return null;
            }
        }
        database.log("DB-GETTASK-SHARED","No task under given sharing code ("+sharing_code+")");
        return null;
    }

    /**
     * Function for updating task status
     * @param taskToUpdate
     * @return String
     */
    public int updateAIMTaskStatus(AIM_Task taskToUpdate, String newStatus){
        try{
            MongoCollection<Document> task_collection = database.get_data_collection("aim_task");
            Document task_document = task_collection.find(new Document("_id",taskToUpdate.aim_task_id)).first();
            if ( task_document != null ){
                List<String> newHistory = taskToUpdate.aim_task_history;
                newHistory.add(LocalDateTime.now(ZoneId.of("Europe/Warsaw"))+"-"+AimApplication.loggedUser.aim_user_email+" changed status to: "+newStatus);
                Bson updates = Updates.combine(
                        Updates.set("status",newStatus),
                        Updates.set("aim_task_history",newHistory)
                );
                UpdateResult result = task_collection.updateOne(task_document, updates);
                if ( result.getModifiedCount() >= 1){
                    database.log("DB-TASK-UPDATESTATUS","Updated task ("+taskToUpdate.aim_task_id.toString()+"), set: "+newStatus);
                    return 1;
                }
                else{
                    database.log("DB-TASK-UPDATESTATUS","Update with no changes! ("+taskToUpdate.aim_task_id.toString()+") - (code:"+result.getModifiedCount()+")");
                    return 0;
                }
            }
            else{
                database.log("DB-TASK-UPDATESTATUS","Cannot find task with _id ("+taskToUpdate.aim_task_id.toString()+")");
                return -1;
            }
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
            database.log("DB-AIM-TASK-INSERT","New task inserted for "+newaimTask.aim_task_owner.getString("aim_user_email"));
            return 1;
        }catch(Exception ex){
            database.log("DB-AIM-TASK-INSERT","Failed to insert task on database ("+ex.toString()+")");
            return -1;
        }
    }

    /**
     * Function for adding new task to application for specific owner
     * @param newaimTask
     * @param owner
     * @return Integer
     */
    public int insertAIMTask(AIM_Task newaimTask, AIM_User owner){
        try{
            newaimTask.aim_task_owner = owner.prepareDocument();
            return insertAIMTask(newaimTask);
        }catch(Exception ex){
            database.log("DB-AIM-TASK-INSERT","Failed to insert task on database ("+ex.toString()+")");
            return -1;
        }
    }

    /**
     * Function for updating aim task data
     * @param aimTaskUpdate
     * @return Integer
     */
    public int updateAIMTask(AIM_Task aimTaskUpdate){
        try{
            MongoCollection<Document> task_collection = database.get_data_collection("aim_task");
            Document currentTaskDocument = task_collection.find(new Document("_id",aimTaskUpdate.aim_task_id)).first();
            List<String> taskHistory = aimTaskUpdate.aim_task_history;
            taskHistory.add(LocalDateTime.now(ZoneId.of("Europe/Warsaw"))+"-"+AimApplication.loggedUser.aim_user_email+" updated the task");
            Bson updates = Updates.combine(
                    Updates.set("aim_task_name",aimTaskUpdate.aim_task_name),
                    Updates.set("aim_task_desc",aimTaskUpdate.aim_task_desc),
                    Updates.set("aim_task_deadline",aimTaskUpdate.aim_task_deadline),
                    Updates.set("aim_task_history",taskHistory)
            );
            UpdateResult result = task_collection.updateOne(currentTaskDocument, updates);
            database.log("DB-TASK-UPDATESTATUS","Updated task ("+currentTaskDocument.getObjectId("_id").toString()+")");
            return 1;
        }catch(Exception ex){
            database.log("DB-TASK-UPDATEFAILED","Failed to update task ("+ex.toString()+")");
            return -1;
        }
    }

    /**
     * Function for changing owner
     * @param aimTaskUpdate
     * @param ownerEmail
     * @return
     */
    public int changeOwnerAIMTask(AIM_Task aimTaskUpdate, String ownerEmail){
        Database_AIMUser daiu = new Database_AIMUser(AimApplication.database);
        if ( daiu.checkIfUserExists(ownerEmail) ){
            MongoCollection<Document> task_collection = database.get_data_collection("aim_task");
            Document currentTaskDocument = task_collection.find(new Document("_id",aimTaskUpdate.aim_task_id)).first();
            List<String> taskHistory = aimTaskUpdate.aim_task_history;
            taskHistory.add(LocalDateTime.now(ZoneId.of("Europe/Warsaw"))+"- Changed owner to "+ownerEmail);
            Bson updates = Updates.combine(
                    Updates.set("aim_task_owner",daiu.getAIMUser(ownerEmail).prepareDocument()),
                    Updates.set("aim_task_history",taskHistory)
            );
            UpdateResult result = task_collection.updateOne(currentTaskDocument, updates);
            database.log("DB-TASK-OWN-CHANGE","Changed task owner ("+ownerEmail+")");
            return 1;
        }
        else
            return 0;
    }
}

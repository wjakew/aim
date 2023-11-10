/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package pl.jakubwawak.aim.aim_dataengine.database_engine;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.cglib.core.Local;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Project;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Task;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_User;
import pl.jakubwawak.maintanance.RandomWordGeneratorEngine;

import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 *Object for maintaining projects on database
 */
public class Database_AIMProject {

    Database_Connector database;

    /**
     * Constructor
     * @param database
     */
    public Database_AIMProject(Database_Connector database){
        this.database = database;
    }

    /**
     * Function for loading AIM_Project collection of logged user
     * @return ArrayList
     */
    public ArrayList<AIM_Project> getUserProjects(){
        ArrayList<AIM_Project> data = new ArrayList<>();
        try{
            MongoCollection<Document> project_collection = database.get_data_collection("aim_project");
            FindIterable<Document> project_documents = project_collection.find();
            for(Document project_document : project_documents) {
                if (project_document.get("aim_owner", Document.class).get("aim_user_email").equals(AimApplication.loggedUser.aim_user_email))
                    data.add(new AIM_Project(project_document));
            }
            database.log("DB-PROJECT-LIST","Loaded "+data.size()+" projects for "+AimApplication.loggedUser.aim_user_email);
        }catch(Exception ex){
            database.log("DB-PROJECT-GET-LIST-FAILED","Failed to get projects list ("+ex.toString()+")");
        }
        return data;
    }

    /**
     * Function for
     * @param user
     * @return
     */
    public ArrayList<AIM_Project> getUserProjects(AIM_User user){
        ArrayList<AIM_Project> data = new ArrayList<>();
        try{
            MongoCollection<Document> project_collection = database.get_data_collection("aim_project");
            FindIterable<Document> project_documents = project_collection.find(new Document("aim_owner",user.prepareDocument()));
            for(Document project_document : project_documents) {
                if (project_document.get("aim_owner", Document.class).get("aim_user_email").equals(user.aim_user_email))
                    data.add(new AIM_Project(project_document));
            }
            database.log("DB-PROJECT-LIST","Loaded "+data.size()+" projects for "+AimApplication.loggedUser.aim_user_email);
        }catch(Exception ex){
            database.log("DB-PROJECT-GET-LIST-FAILED","Failed to get projects list ("+ex.toString()+")");
        }
        return data;
    }

    /**
     * Function for loading all projects from database
     * @return ArrayList
     */
    public ArrayList<AIM_Project> getAllProjects(){
        ArrayList<AIM_Project> data = new ArrayList<>();
        try{
            MongoCollection<Document> project_collection = database.get_data_collection("aim_project");
            FindIterable<Document> project_documents = project_collection.find();
            for(Document project_document : project_documents){
                data.add(new AIM_Project(project_document));
            }
            database.log("DB-PROJECT-LIST","Loaded "+data.size()+" projects for "+AimApplication.loggedUser.aim_user_email);
        }catch(Exception ex){
            database.log("DB-PROJECT-GET-LIST-FAILED","Failed to get projects list ("+ex.toString()+")");
        }
        return data;
    }

    /**
     * Function for listing projects by name
     * @param projectName
     * @return AIM_Project
     */
    public AIM_Project getProjectByName(String projectName){
        try{
            MongoCollection<Document> project_collection = database.get_data_collection("aim_project");
            Document project_documents = project_collection.find(new Document("aim_project_name",projectName)).first();
            if ( project_documents != null ){
                database.log("DB-PROJECT-GETSINGLE","Found project with given name ("+projectName+")");
                return new AIM_Project(project_documents);
            }
            database.log("DB-PROJECT-GETSINGLE","Project with given name ("+projectName+") not found..");
            return null;
        }catch(Exception ex){
            database.log("DB-PROJECT-GETSINGLE-FAILED","Failed to get project by name ("+ex.toString()+")");
            return null;
        }
    }

    /**
     * Function for loading project data by shared code
     * @param sharing_code
     * @return AIM_Project
     */
    public AIM_Project getProjectBySharedCode(String sharing_code){
        try{
            MongoCollection<Document> sharing_collection = database.get_data_collection("aim_share");
            Document sharing_document = sharing_collection.find(new Document("sharing_code", sharing_code)).first();
            if ( sharing_document == null ){
                for(AIM_Project project : getAllProjects()){
                    if (project.aim_project_id.equals(sharing_document.getObjectId("project_id"))){
                        database.log("DB-SHARING-PROJECT","Found project ("+project.aim_project_id.toString()+") for sharing code!");
                        return project;
                    }
                }
                database.log("DB-SHARING-PROJECT-NOPROJECT","Found code but cannot find project with ID: "+sharing_document.getObjectId("project_id"));
                return null;
            }
            database.log("DB-SHARING-GETPROJECT-NOCODE","Cannot find sharing_code: "+sharing_code);
            return null;
        }
        catch(Exception ex){
            database.log("DB-PROJECT-SHARE-FAILED","Failed to share project ("+ex.toString()+")");
            return null;
        }
    }

    /**
     * Function for inserting project object to database
     * @param projectToInsert
     * @return Integer
     */
    public int insertProject(AIM_Project projectToInsert){
        try{
            MongoCollection<Document> project_collection = database.get_data_collection("aim_project");
            project_collection.insertOne(projectToInsert.prepareDocument());
            database.log("DB-PROJECT-INSERT","New project added for ("+ AimApplication.loggedUser.aim_user_email);
            return 1;
        }catch(Exception ex){
            database.log("DB-PROJECT-INSERT-FAILED","Failed to add project ("+ex.toString()+")");
            return -1;
        }
    }

    /**
     * Function for linking task to project
     * @param projectToLink
     * @param taskObject
     * @return Integer
     */
    public int linkTaskProject(AIM_Project projectToLink, AIM_Task taskObject){
        try{
            taskObject.aim_task_id = null;
            taskObject.aim_task_history.add(AimApplication.loggedUser.aim_user_email+" - "+ LocalDateTime.now().toString()+" - task linked to project");
            int ans = insertTaskToProject(projectToLink,taskObject);
            if (ans == 1){
                Database_AIMTask dat = new Database_AIMTask(database);
                dat.remove(taskObject);
                database.log("DB-PROJECT-LINK-TASK","Task ("+taskObject.aim_task_id.toString()+") linked to project ("+projectToLink.aim_project_id.toString()+")");
                return 1;
            }
            else{
                database.log("DB-PROJECT-LINK-TASK","Cannot link task, check other log!");
                return 0;
            }
        }catch(Exception ex){
            database.log("DB-PROJECT-LINK-FAILED","Failed to link task to project ("+ex.toString()+")");
            return -1;
        }
    }

    /**
     * Function for removing project
     * @param projectToRemove
     * @return Integer
     */
    public int removeProject(AIM_Project projectToRemove){
        try{
            MongoCollection<Document> project_collection = database.get_data_collection("aim_project");
            Document project_document = project_collection.find(new Document("_id",projectToRemove.aim_project_id)).first();
            DeleteResult result = project_collection.deleteOne(project_document);
            if ( result.getDeletedCount() > 0 ){
                database.log("DB-PROJECT-DELETE","Project ("+projectToRemove.aim_project_id.toString()+") removed!");
                return 1;
            }
            database.log("DB-PROJECT-DELETE-EMPTY","Project cannot be deleted, not found!");
            return 0;
        }catch(Exception ex){
            database.log("DB-PROJECT-DELETE-FAILED","Failed to delete project ("+ex.toString()+")");
            return -1;
        }
    }

    /**
     * Function for inserting task to project
     * @param projectToUpdate
     * @param taskToAdd
     * @return Integer
     */
    public int insertTaskToProject(AIM_Project projectToUpdate, AIM_Task taskToAdd){
        try{
            taskToAdd.aim_task_id = projectToUpdate.aim_project_id;
            MongoCollection<Document> project_collection = database.get_data_collection("aim_project");
            Document project_document = project_collection.find(new Document("_id",projectToUpdate.aim_project_id)).first();
            if ( project_document != null ){
                List<Document> taskList = project_document.getList("task_list",Document.class);
                taskList.add(taskToAdd.prepareDocument());
                List<String> projectHistory = project_document.getList("project_history",String.class);
                projectHistory.add(LocalDateTime.now(ZoneId.of("Europe/Warsaw"))+"- Added new task to project "+taskToAdd.aim_task_name);
                Bson updates = Updates.combine(
                        Updates.set("task_list",taskList),
                        Updates.set("project_history",projectHistory)
                );
                UpdateResult result = project_collection.updateOne(projectToUpdate.prepareDocument(), updates);
                if ( result.getModifiedCount() > 0) {
                    database.log("DB-PROJECT-TASK-INSERT", "Added new task to project (" + projectToUpdate.aim_project_id.toString() + ") code (" + result.getModifiedCount() + ")");
                    return 1;
                }
            }
            database.log("DB-PROJECT-TASK-INSERT","Task cannot be added, result is 0 or project is empty!");
            return 0;
        }catch(Exception ex){
            database.log("DB-PROJECT-TASK-INSERT-FAILED","Failed to insert task to project ("+ex.toString()+")");
            return -1;
        }
    }

    /**
     * Function for updating project
     * @param projectToUpdate
     * @return Integer
     */
    public int updateProject(AIM_Project projectToUpdate){
        try {
            MongoCollection<Document> project_collection = database.get_data_collection("aim_project");
            Document project_document = project_collection.find(new Document("_id", projectToUpdate.aim_project_id)).first();
            AIM_Project currentProject = new AIM_Project(project_document);
            currentProject = projectToUpdate;
            currentProject.project_history.add(LocalDateTime.now(ZoneId.of("Europe/Warsaw"))+" - updated project data");
            // loading search query
            Bson query = Filters.eq("_id", projectToUpdate.aim_project_id);
            // replacing project data
            ReplaceOptions opts = new ReplaceOptions().upsert(true);
            UpdateResult result = project_collection.replaceOne(query,currentProject.prepareDocument(), opts);

            if ( result.getModifiedCount() > 0 ){
                database.log("DB-PROJECT-UPDATE","Updated project data ("+result.getModifiedCount()+")");
                return 1;
            }
            database.log("DB-PROJECT-UPDATE","Nothing was updated on project ("+result.getModifiedCount()+")");
            return 0;
        }catch(Exception ex){
            database.log("DB-PROJECT-UPDATE-FAILED","Failed to update project ("+ex.toString()+")");
            return -1;
        }
    }

    /**
     * Function for creating sharing code for project
     * @return Integer
     */
    public String shareProject(AIM_Project projectToShare){
        try{
            MongoCollection<Document> sharing_collection = database.get_data_collection("aim_share");
            Document sharing_document = sharing_collection.find(new Document("project_id", projectToShare.aim_project_id)).first();
            RandomWordGeneratorEngine rwge = new RandomWordGeneratorEngine();
            if ( sharing_document == null ){
                String sharingCode = rwge.generateRandomString(10,true,false);
                Document document = new Document();
                document.append("project_id",projectToShare.aim_project_id);
                document.append("type","project");
                document.append("sharing_code",sharingCode);
                InsertOneResult result = sharing_collection.insertOne(document);
                if ( result.wasAcknowledged() ){
                    database.log("DB-SHARING-PROJECT","Shared project ("+projectToShare.aim_project_id.toString()+") sharing code: "+sharingCode);
                    return sharingCode;
                }
                database.log("DB-SHARING-FAILED","Nothing to update on table, probably ");
                return null;
            }
            database.log("DB-SHARING-ALREADY","Project already shared ("+sharing_document.getString("sharing_code")+")");
            return null;
        }
        catch(Exception ex){
            database.log("DB-PROJECT-SHARE-FAILED","Failed to share project ("+ex.toString()+")");
            return null;
        }
    }

    /**
     * Function for removing project share
     * @param projectToshare
     * @return Integer
     */
    public int removeShareProject(AIM_Project projectToshare){
        try {
            MongoCollection<Document> sharing_collection = database.get_data_collection("aim_share");
            Document sharing_document = sharing_collection.find(new Document("project_id", projectToshare.aim_project_id)).first();
            if ( sharing_document != null ){
                DeleteResult result = sharing_collection.deleteOne(sharing_document);
                if (result.getDeletedCount() > 0){
                    database.log("DB-PROJECT-SHARE-REMOVE","Share of the project("+projectToshare.aim_project_id.toString()+") removed!");
                    return 1;
                }
            }
            return 0;
        }catch(Exception ex){
            database.log("DB-PROJECT-SHARE-REMOVE-FAILED","Failed to remove sharing on project ("+ex.toString()+")");
            return -1;
        }

    }

    /**
     * Function for checking if project is shared
     * @param projectToShare
     * @return String
     */
    public String checkShare(AIM_Project projectToShare){
        try{
            MongoCollection<Document> sharing_collection = database.get_data_collection("aim_share");
            Document sharing_document = sharing_collection.find(new Document("project_id", projectToShare.aim_project_id)).first();
            if ( sharing_document == null ){
                return null;
            }
            database.log("DB-SHARING-ALREADY","Project already shared ("+sharing_document.getString("sharing_code")+")");
            return sharing_document.getString("sharing_code");
        }
        catch(Exception ex){
            database.log("DB-PROJECT-SHARE-FAILED","Failed to share project ("+ex.toString()+")");
            return null;
        }
    }

    /**
     * Function for removing task from project
     * @param projectToUpdate
     * @param taskToRemove
     * @return Integer
     */
    public int removeTaskFromProject(AIM_Project projectToUpdate, AIM_Task taskToRemove){
        try{
            MongoCollection<Document> project_collection = database.get_data_collection("aim_project");
            Document project_document = project_collection.find(new Document("_id",projectToUpdate.aim_project_id)).first();
            if ( project_document != null ){
                List<Document> taskList = project_document.getList("task_list",Document.class);
                taskList.remove(taskToRemove.prepareDocument());
                List<String> projectHistory = project_document.getList("project_history",String.class);
                Bson updates = Updates.combine(
                        Updates.set("task_list",taskList),
                        Updates.set("project_history",projectHistory)
                );
                projectHistory.add(LocalDateTime.now(ZoneId.of("Europe/Warsaw"))+"- Removed task from project "+taskToRemove.aim_task_name);
                UpdateResult result = project_collection.updateOne(projectToUpdate.prepareDocument(), updates);
                if ( result.getModifiedCount() > 0) {
                    database.log("DB-PROJECT-TASK-REMOVE", "Removed task from project (" + projectToUpdate.aim_project_id.toString() + ") code (" + result.getModifiedCount() + ")");
                    return 1;
                }
            }
            database.log("DB-PROJECT-TASK-REMOVE","Task cannot be removed, result is 0 or project is empty!");
            return 0;
        }catch(Exception ex){
            database.log("DB-PROJECT-TASK-REMOVE-FAILED","Failed to remove task from project ("+ex.toString()+")");
            return -1;
        }
    }

    /**
     * Function for updating status on given task
     * @param projectToUpdate
     * @param taskToUpdate
     * @param newStatus
     * @return Integer
     */
    public int updateTaskStatus(AIM_Project projectToUpdate, AIM_Task taskToUpdate,String newStatus){
        try{
            MongoCollection<Document> project_collection = database.get_data_collection("aim_project");
            // loading project object
            Document project_document = project_collection.find(new Document("_id",projectToUpdate.aim_project_id)).first();
            AIM_Project currentProject = new AIM_Project(project_document);
            // removing task from object
            currentProject.task_list.remove(taskToUpdate.prepareDocument());
            // setting new task status and adding history
            taskToUpdate.status = newStatus;
            taskToUpdate.aim_task_history.add(AimApplication.loggedUser.aim_user_email+" - "+LocalDateTime.now(ZoneId.of("Europe/Warsaw"))+" - changed status to: "+newStatus);
            currentProject.task_list.add(taskToUpdate.prepareDocument());
            // adding data to project history
            currentProject.project_history.add(LocalDateTime.now(ZoneId.of("Europe/Warsaw"))+" - updated status of task");
            // loading replacing query
            Bson query = Filters.eq("_id",projectToUpdate.aim_project_id);
            // replacing project data
            ReplaceOptions opts = new ReplaceOptions().upsert(true);
            UpdateResult result = project_collection.replaceOne(query,currentProject.prepareDocument(),opts);
            if (result.getModifiedCount() > 0){
                database.log("DB-PROJECT-TASK-UPDATE","Task status on project ("+projectToUpdate.aim_project_id
                        +") updated code ("+result.getModifiedCount()+","+result.getUpsertedId()+")");
                return 1;
            }
            database.log("DB-PROJECT-TASK-UPDATE","Task status cannot be updated, result is 0 or project is empty!");
            return 0;
        }catch(Exception ex){
            database.log("DB-PROJECT-TASK-UPDATE-FAILED","Failed to update task status from project ("+ex.toString()+")");
            return -1;
        }
    }

}

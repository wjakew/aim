/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package pl.jakubwawak.aim.aim_dataengine.database_engine;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Project;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Task;

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
            FindIterable<Document> project_documents = project_collection.find(new Document("aim_owner",AimApplication.loggedUser.prepareDocument()));
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
     * Function for inserting project object to database
     * @param projectToInsert
     * @return
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
        // todo doesn't work correctly, return 0
        try {
            MongoCollection<Document> project_collection = database.get_data_collection("aim_project");
            Document project_document = project_collection.find(new Document("_id", projectToUpdate.aim_project_id)).first();
            projectToUpdate.project_history.add("Updated project name and desc");
            Bson updates = Updates.combine(
                    Updates.set("aim_project_name",projectToUpdate.aim_project_name),
                    Updates.set("aim_project_desc",projectToUpdate.aim_project_desc),
                    Updates.set("project_history",projectToUpdate.project_history)
            );
            UpdateResult result = project_collection.updateOne(projectToUpdate.prepareDocument(), updates);
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
        // todo repair updating status on project
        try{
            MongoCollection<Document> project_collection = database.get_data_collection("aim_project");
            Document project_document = project_collection.find(new Document("_id",projectToUpdate.aim_project_id)).first();
            if ( project_document != null ){
                List<Document> taskList = project_document.getList("task_list",Document.class);
                taskList.remove(taskToUpdate.prepareDocument());
                taskToUpdate.status = newStatus;
                taskToUpdate.aim_task_history.add("Status changed to: "+newStatus);
                taskList.add(taskToUpdate.prepareDocument());
                List<String> projectHistory = project_document.getList("project_history",String.class);
                Bson updates = Updates.combine(
                        Updates.set("task_list",taskList),
                        Updates.set("project_history",projectHistory)
                );
                projectHistory.add(LocalDateTime.now(ZoneId.of("Europe/Warsaw"))+"- Updated Status of "+taskToUpdate.aim_task_name);
                UpdateResult result = project_collection.updateOne(projectToUpdate.prepareDocument(), updates);
                database.log("DB-PROJECT-TASK-UPDATE", "Updated task status from project (" + projectToUpdate.aim_project_id.toString() + ") code (" + result.getModifiedCount() + ")");
                return 1;

            }
            database.log("DB-PROJECT-TASK-UPDATE","Task status cannot be updated, result is 0 or project is empty!");
            return 0;
        }catch(Exception ex){
            database.log("DB-PROJECT-TASK-UPDATE","Failed to update task status from project ("+ex.toString()+")");
            return -1;
        }
    }

}

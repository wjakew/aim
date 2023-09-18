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
                UpdateResult result = project_collection.updateOne(project_document, updates);
                database.log("DB-PROJECT-TASK-INSERT","Added new task to project ("+projectToUpdate.aim_project_id.toString()+")");
                return 1;
            }
            return 0;
        }catch(Exception ex){
            database.log("DB-PROJECT-TASK-INSERT-FAILED","Failed to insert task to project ("+ex.toString()+")");
            return -1;
        }
    }

}

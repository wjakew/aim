/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.aim_dataengine.database_engine;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.codingproject.AIM_CodingTask;

import java.util.ArrayList;

/**
 * Database object for maintaining coding task objects
 */
public class Database_AIMCodingTask {

    Database_Connector database;

    /**
     * Constructor
     * @param database
     */
    public Database_AIMCodingTask(Database_Connector database){
        this.database = database;
    }

    /**
     * Function for inserting coding task to database
     * @param taskToInsert
     * @return Integer
     */
    public int insertCodingTask(AIM_CodingTask taskToInsert){
        try{
            MongoCollection<Document> ctask_collection = database.get_data_collection("aim_codingtask");
            InsertOneResult result = ctask_collection.insertOne(taskToInsert.prepareDocument());
            if ( result.wasAcknowledged() ){
                database.log("DB-CODINGTASK-INSERT","Coding task was added to database!");
                return 1;
            }
            database.log("DB-CODINGTASK-INSERT-NULL","Coding task not added, empty object!");
            return 0;
        }catch(Exception ex){
            database.log("DB-CODINGTASK-INSERT-FAILED","Failed to insert task ("+ex.toString()+")");
            return -1;
        }
    }

    /**
     * Function for getting coding task list for logged user.
     * @return ArrayList
     */
    public ArrayList<AIM_CodingTask> getCodingTaskList(){
        ArrayList<AIM_CodingTask> data = new ArrayList<>();
        try{
            MongoCollection<Document> ctask_collection = database.get_data_collection("aim_codingtask");
            FindIterable<Document> ctask_documents = ctask_collection.find();
            for(Document ctask_document : ctask_documents){
                if ( ctask_document.getObjectId("aim_user_id").equals(AimApplication.loggedUser.aim_user_id)){
                    data.add(new AIM_CodingTask(ctask_document));
                }
            }
            database.log("DB-CODINGTASK-GET-EMPTY","Nothing to show,empty ctask collection!");
        }catch(Exception ex){
            database.log("DB-CODINGTASK-GET-FAILED","Failed to show coding task list colllection ("+ex.toString()+")");
        }
        return data;
    }

    /**
     * Function for updating coding task
     * @param ctaskToUpdate
     * @return Integer
     */
    public int updateCodingTask(AIM_CodingTask ctaskToUpdate){
        try{
            MongoCollection<Document> ctask_collection = database.get_data_collection("aim_codingtask");
            Document ctask_document = ctask_collection.find(new Document("aim_codingtask_id",ctaskToUpdate.aim_codingtask_id)).first();
            Bson updates = Updates.combine(
                    Updates.set("aim_codingtask_tag",ctaskToUpdate.aim_codingtask_tag),
                    Updates.set("aim_codingtask_name",ctaskToUpdate.aim_codingtask_name),
                    Updates.set("aim_codintask_desc",ctaskToUpdate.aim_codingtask_desc),
                    Updates.set("aim_codingtask_comments",ctaskToUpdate.aim_codingtask_comments),
                    Updates.set("aim_codingtask_history",ctaskToUpdate.aim_codingtask_history));

            UpdateResult result = ctask_collection.updateOne(updates,ctask_document);
            if ( result.wasAcknowledged() ){
                database.log("DB-CTASK-UPDATE","Updated ctask data ID ("+ctaskToUpdate.aim_codingproject_id.toString()+")");
                return 1;
            }
            database.log("DB-CTASK-UPDATE","Nothing to update, object probably empty or no changes!");
            return 0;
        }catch(Exception ex){
            database.log("DB-CTASK-UPDATE-FAILED","Failed to update coding task ("+ex.toString()+")");
            return -1;
        }
    }

    /**
     * Function for getting coding task from database by given ID
     * @param aim_codingtask_id
     * @return Integer
     */
    public AIM_CodingTask getCodingTask(ObjectId aim_codingtask_id){
        try{
            MongoCollection<Document> ctask_collection = database.get_data_collection("aim_codingtask");
            Document ctask_document = ctask_collection.find(new Document("aim_codingtask_id",aim_codingtask_id)).first();
            if ( ctask_document != null ){
                database.log("DB-CODINGTASK-GET","Found coding task with ID ("+aim_codingtask_id.toString()+")");
                return new AIM_CodingTask(ctask_document);
            }
            database.log("DB-CODINGTASK-GET","Cannot find coding task with ID ("+aim_codingtask_id.toString()+")");
            return null;
        }catch (Exception ex){
            database.log("DB-CODINGTASK-GET-FAILED", "Failed to find coding task ("+ex.toString()+")");
            return null;
        }
    }
}
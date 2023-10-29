package pl.jakubwawak.aim.aim_dataengine.database_engine;
/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import com.vaadin.flow.data.provider.ArrayUpdater;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_APIUserKey;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_User;

/**
 * Object for maintaining API keys on database
 */
public class Database_APIKey {

    public Database_Connector database;

    /**
     * Constructor
     */
    public Database_APIKey(Database_Connector database){
        this.database = database;
    }

    /**
     * Function for checking API key for logged user
     * @return AIM_APIUserKey
     */
    public AIM_APIUserKey checkloggedUserAPIKey(){
        try{
            MongoCollection<Document> apikey_collection = database.get_data_collection("aim_apikey");
            FindIterable<Document> apikey_documents = apikey_collection.find();
            for(Document apikey_document : apikey_documents){
                if (apikey_document.getObjectId("aim_user_id").equals(AimApplication.loggedUser.aim_user_id)){
                    database.log("DB-APIKEY-FOUND","Found key for logged user! ("+AimApplication.loggedUser.aim_user_id.toString()+")");
                    return new AIM_APIUserKey(apikey_document);
                }
            }
            database.log("DB-APIKEY-EMPTY","Cannot find key for logged user! ("+AimApplication.loggedUser.aim_user_id.toString()+")");
            return null;
        }catch (Exception ex){
            database.log("DB-APIKEY-ERROR","Failed to check api key ("+ex.toString()+")");
            return null;
        }
    }

    /**
     * Function for checking API key for  user
     * @return AIM_APIUserKey
     */
    public AIM_APIUserKey getUserAPIKey(ObjectId aim_user_id){
        try{
            MongoCollection<Document> apikey_collection = database.get_data_collection("aim_apikey");
            FindIterable<Document> apikey_documents = apikey_collection.find(new Document("aim_user_id",aim_user_id));
            for(Document apikey_document : apikey_documents){
                if (apikey_document.getObjectId("aim_user_id").equals(AimApplication.loggedUser.aim_user_id)){
                    database.log("DB-APIKEY-FOUND","Found key for logged user! ("+AimApplication.loggedUser.aim_user_id.toString()+")");
                    return new AIM_APIUserKey(apikey_document);
                }
            }
            database.log("DB-APIKEY-EMPTY","Cannot find key for logged user! ("+AimApplication.loggedUser.aim_user_id.toString()+")");
            return null;
        }catch (Exception ex){
            database.log("DB-APIKEY-ERROR","Failed to check api key ("+ex.toString()+")");
            return null;
        }
    }

    /**
     * Function for inserting new api key
     * @return Integer
     */
    public int insertNewApiKey(AIM_APIUserKey newUserAPIkey){
        try{
            MongoCollection<Document> apikey_collection = database.get_data_collection("aim_apikey");
            InsertOneResult result =apikey_collection.insertOne(newUserAPIkey.prepareDocument());
            if ( result.wasAcknowledged() ){
                database.log("DB-APIKEYNEW","Added new api key for user ("+AimApplication.loggedUser.aim_user_id.toString()+")");
                return 1;
            }
            database.log("DB-APIKEYNEW-NOINSERT","Nothing to add to api data for user ("+AimApplication.loggedUser.aim_user_id.toString()+")");
            return 0;
        }catch (Exception ex){
            database.log("DB-APIKEYNEW-FAILED","Failed to insert new api key ("+ex.toString()+")");
            return -1;
        }
    }

    /**
     * Function for changing API key status
     * @param userKey
     * @param newStatus
     * @return Integer
     */
    public int changeAPIKeyStatus(AIM_APIUserKey userKey, int newStatus){
        MongoCollection<Document> apikey_collection = database.get_data_collection("aim_apikey");
        try{
            Bson updates = Updates.combine(Updates.set("apiuserkey_activeflag",newStatus));
            UpdateResult updateResult = apikey_collection.updateOne(userKey.prepareDocument(),updates);
            if ( updateResult.getModifiedCount() > 0 ){
                database.log("DB-APIKEYUPDATE","Updated api status for ("+userKey.aim_user_id.toString()+")");
                return 1;
            }
            database.log("DB-APIKEYUPDATE","Nothing to update on, result: "+updateResult.getModifiedCount());
            return 0;
        }catch(Exception ex){
            database.log("DB-APIKEYUPDATE-FAILED","Failed to update apikey ("+ex.toString()+")");
            return -1;
        }
    }

    /**
     * Function for removing api key data
     * @param aim_user_id
     * @return int
     */
    public int removeUserAPIKey(ObjectId aim_user_id){
        try{
            MongoCollection<Document> apikey_collection = database.get_data_collection("aim_apikey");
            DeleteResult result =apikey_collection.deleteOne(new Document("aim_user_id",aim_user_id));
            if  (result.getDeletedCount() > 0){
                database.log("DB-APIKEYREMOVE","Removed API key for user ("+aim_user_id.toString()+")");
                return 1;
            }
            database.log("DB-APIKEYREMOVE-NOREMOVE","Nothing to remove, no result of removing.");
            return 0;
        }catch(Exception ex){
            database.log("DB-APIKEYREMOVE-FAILED","Failed to remove apikey ("+ex.toString()+")");
            return -1;
        }
    }

    /**
     * Function for validating api user key
     * @param apiuserkey_value
     * @return AIM_User
     */
    public AIM_User validateUserAPIKey(String apiuserkey_value){
        try{
            MongoCollection<Document> apikey_collection = database.get_data_collection("aim_apikey");
            Document apikey_document = apikey_collection.find(new Document("apiuserkey_value",apiuserkey_value)).first();
            //todo bug with finding apikey documents
            if( apikey_document != null ){
                Database_AIMUser dau = new Database_AIMUser(database);
                database.log("DB-VALIDATE-APIKEY","Validated api key |"+apiuserkey_value+"| user ("+apikey_document.getObjectId("aim_user_id").toString()+")");
                return dau.getAIMUser(apikey_document.getObjectId("aim_user_id"));
            }
            else{
                database.log("DB-VALIDATE-APIKEY-NOUSER","Cannot find user for api key |"+apiuserkey_value+"|");
                return null;
            }
        }catch(Exception ex){
            database.log("DB-VALIDATE-APIKEY-FAILED","Failed to validate user by api key ("+ex.toString()+")");
            return null;
        }
    }
}

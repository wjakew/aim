/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.aim_dataengine.database_engine;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.bson.types.ObjectId;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_APIUserKey;

/**
 * Object for maintaining API keys on database
 */
public class Database_AIMAPI {

    public Database_Connector database;

    /**
     * Constructor
     */
    public Database_AIMAPI(Database_Connector database){
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
}

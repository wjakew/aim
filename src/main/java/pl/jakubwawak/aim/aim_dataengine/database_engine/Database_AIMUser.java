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
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Task;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_User;
import pl.jakubwawak.maintanance.Password_Validator;

import java.util.ArrayList;

/**
 * Object for database
 */
public class Database_AIMUser {
    Database_Connector database;

    /**
     * Constructing
     * @param database
     */
    public Database_AIMUser(Database_Connector database){
        this.database = database;
    }

    /**
     * Function for loading user collection from database
     * @return ArrayList
     */
    public ArrayList<AIM_User> getUserCollection(){
        ArrayList<AIM_User> data = new ArrayList<>();
        try{
            MongoCollection<Document> user_collection = database.get_data_collection("aim_user");
            FindIterable<Document> user_documents = user_collection.find();
            for(Document user_document : user_documents){
                data.add(new AIM_User(user_document));
            }
            database.log("DB-USER-LIST","Loaded list of users ("+data.size()+")");
        }catch (Exception ex){
            database.log("DB-USER-LIST-FAILED","Failed to load list of users ("+ex.toString()+")");
        }
        return data;
    }

    /**
     * Function for checking if user exists
     * @param email
     * @return boolean
     */
    public boolean checkIfUserExists(String email){
        try{
            MongoCollection<Document> user_collection = database.get_data_collection("aim_user");
            long amount = user_collection.countDocuments(new Document("aim_user_email",email));
            database.log("DB-AIMUSER-CHECKMAIL","Loaded "+amount+" documents with email ("+email+")");
            return amount!=0;
        }catch(Exception ex){
            database.log("DB-AIMUSER-CHECKMAIL-FAILED","Failed to check if user exists with that email ("+ex.toString()+")");
            return false;
        }
    }

    /**
     * Function for getting AIM_User from database
     * @param email
     * @return AIM_User
     */
    public AIM_User getAIMUser(String email){
        try{
            MongoCollection<Document> user_collection = database.get_data_collection("aim_user");
            Document user_document = user_collection.find(new Document("aim_user_email",email)).first();
            if ( user_document != null ){
                database.log("DB-AIMUSER-GET","Found user with email: "+email);
                return new AIM_User(user_document);
            }
            database.log("DB-AIMUSER-GET","Cannot find user with email: "+email);
            return null;
        }catch(Exception ex){
            database.log("DB-AIMUSER-GET-FAILED","Failed to get user with that email ("+ex.toString()+")");
            return null;
        }
    }

    /**
     * Function for creating new application user
     * @param newUser
     * @return Integer
     */
    public int createAIMUser(AIM_User newUser){
        try{
            if ( !checkIfUserExists(newUser.aim_user_email)){
                MongoCollection<Document> user_collection = database.get_data_collection("aim_user");
                user_collection.insertOne(newUser.prepareDocument());
                database.log("DB-AIMUSER-INSERT","Created new user ("+newUser.aim_user_email+")");
                return 1;
            }
            return 0;
        }catch(Exception ex){
            database.log("DB-AIMUSER-INSERT-FAILED","Failed to insert new user ("+ex.toString()+")");
            return -1;
        }
    }

    /**
     * Function for removing users
     * @param userToDelete
     * @return Integer
     */
    public int removeAIMUser(AIM_User userToDelete){
        try{
            MongoCollection<Document> user_collection = database.get_data_collection("aim_user");
            DeleteResult result = user_collection.deleteMany(userToDelete.prepareDocument());
            if ( result.getDeletedCount() > 0 ){
                database.log("DB-AIMUSER-DELETE","Removed user ("+userToDelete.aim_user_id.toString()+")");
                return 1;
            }
            database.log("DB-AIMUSER-DELETE","Cannot remove. No deletion data!");
            return 0;
        }catch(Exception ex){
            database.log("DB-AIMUSER-DELETE-FAILED","Failed to remove user ("+userToDelete.aim_user_id.toString()+")");
            return -1;
        }
    }


    /**
     * Function for setting user admin data
     * @param userToChange
     * @return Integer
     */
    public int setUserAdmin(AIM_User userToChange){
        try{
            MongoCollection<Document> user_collection = database.get_data_collection("aim_user");
            Document user_document = user_collection.find(new Document("_id",userToChange.aim_user_id)).first();
            if (user_document != null){
                Bson updates = Updates.combine(
                        Updates.set("aim_user_type","SERVERADM")
                );
                UpdateResult result = user_collection.updateOne(user_document,updates);
                if ( result.getModifiedCount() > 0 ){
                    database.log("DB-AIMUSER-ADMUPDATE","Updated user ("+userToChange.aim_user_id.toString()+") to ADM.");
                    return 1;
                }
                database.log("DB-AIMUSER-ADMUPDATE","Cannot update user ("+userToChange.aim_user_id.toString()+"), no changes!");
                return 0;
            }
            database.log("DB-AIMUSER-ADMUPDATE","Cannot find user ("+userToChange.aim_user_id.toString()+")");
            return 0;
        }catch(Exception ex){
            database.log("DB-AIMUSER-ADMUPDATE-FAILED","Failed to set admin for user ("+ex.toString()+")");
            return -1;
        }
    }

    /**
     * Function for setting user normal data
     * @param userToChange
     * @return Integer
     */
    public int setUserNRM(AIM_User userToChange){
        try{
            MongoCollection<Document> user_collection = database.get_data_collection("aim_user");
            Document user_document = user_collection.find(new Document("_id",userToChange.aim_user_id)).first();
            if (user_document != null){
                Bson updates = Updates.combine(
                        Updates.set("aim_user_type","NRLUSER")
                );
                UpdateResult result = user_collection.updateOne(user_document,updates);
                if ( result.getModifiedCount() > 0 ){
                    database.log("DB-AIMUSER-ADMUPDATE","Updated user ("+userToChange.aim_user_id.toString()+") to ADM.");
                    return 1;
                }
                database.log("DB-AIMUSER-ADMUPDATE","Cannot update user ("+userToChange.aim_user_id.toString()+"), no changes!");
                return 0;
            }
            database.log("DB-AIMUSER-ADMUPDATE","Cannot find user ("+userToChange.aim_user_id.toString()+")");
            return 0;
        }catch(Exception ex){
            database.log("DB-AIMUSER-ADMUPDATE-FAILED","Failed to set admin for user ("+ex.toString()+")");
            return -1;
        }
    }

    /**
     * Function for logging application user
     * @param login
     * @param hash_password
     * @return Integer
     */
    public int loginAIMUser(String login, String hash_password){
        try{
            MongoCollection<Document> user_collection = database.get_data_collection("aim_user");
            FindIterable<Document> user_documents = user_collection.find(new Document("aim_user_email",login));
            for(Document user_document : user_documents){
                if ( user_document.getString("aim_user_password").equals(hash_password)){
                    AimApplication.loggedUser = new AIM_User(user_document);
                    database.log("DB-AIMUSER-LOGIN","Logged user to the app ("+login+")");
                    return 1;
                }
            }
            database.log("DB-AIMUSER-LOGIN-USNF","Cannot find user in application database ("+login+")");
            return 0;
        }catch(Exception ex){
            database.log("DB-AIMUSER-LOGIN-FAILED","Failed to login user to app ("+ex.toString()+")");
            return -1;
        }
    }

    /**
     * Function for resetting password for user
     * @param aim_user_id
     * @param newPassword
     * @return Integer
     */
    public int resetAIMUserPassword(ObjectId aim_user_id, String newPassword){
        try{
            MongoCollection<Document> user_collection = database.get_data_collection("aim_user");
            Document user_document = user_collection.find(new Document("_id",aim_user_id)).first();
            if ( user_document != null ){
                Password_Validator pv = new Password_Validator(newPassword);
                Bson updates = Updates.combine(
                        Updates.set("aim_user_password",pv.hash())
                );
                UpdateResult result = user_collection.updateOne(user_document,updates);
                if ( result.getModifiedCount() > 0 ){
                    database.log("DB-AIMUSER-PASSCHANGE","Password for user ("+aim_user_id.toString()+") changed!");
                    return 1;
                }
                database.log("DB-AIMUSER-PASSCHANGE","Nothing to change for user ("+aim_user_id.toString()+") changed!");
                return 0;
            }
            database.log("DB-AIMUSER-PASSCHANGE-NOUSER","Cannot find user ("+aim_user_id.toString()+") changed!");
            return 0;
        }catch(Exception ex){
            database.log("DB-AIMUSER-PASSCHANGE-FAILED","Failed to change user password ("+ex.toString()+")");
            return -1;
        }
    }
}

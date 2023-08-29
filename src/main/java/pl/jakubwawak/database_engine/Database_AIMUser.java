/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package pl.jakubwawak.database_engine;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_objects.AIM_User;

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
     * Function for creating new application user
     * @param newUser
     * @return Integer
     */
    public int createAIMUser(AIM_User newUser){
        try{
            MongoCollection<Document> user_collection = database.get_data_collection("aim_user");
            user_collection.insertOne(newUser.prepareDocument());
            database.log("DB-AIMUSER-INSERT","Created new user ("+newUser.aim_user_email+")");
            return 1;
        }catch(Exception ex){
            database.log("DB-AIMUSER-INSERT-FAILED","Failed to insert new user ("+ex.toString()+")");
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
                    return 1;
                }
            }
            return 0;
        }catch(Exception ex){
            database.log("DB-AIMUSER-LOGIN-FAILED","Failed to login user to app ("+ex.toString()+")");
            return -1;
        }
    }
}

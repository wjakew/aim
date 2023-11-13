/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.aim_dataengine.database_engine;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

/**
 * Object for maintaing sharing codes on database
 */
public class Database_ShareCode {

    Database_Connector database;

    /**
     * Constructor
     * @param database
     */
    public Database_ShareCode(Database_Connector database){
        this.database = database;
    }

    /**
     * Function for getting share code from database
     * @param sharing_code
     * @return Document
     */
    public Document getShareCode(String sharing_code){
        try{
            MongoCollection<Document> sharing_collection = database.get_data_collection("aim_share");
            FindIterable<Document> sharingDocuments = sharing_collection.find();
            for(Document sharing_document : sharingDocuments){
                if ( sharing_document.getString("sharing_code").equals(sharing_code)){
                    //todo bug - cannot find string
                    database.log("DB-GET-SHARE","Loaded share code with code: "+sharing_code);
                    return sharing_document;
                }
            }
            database.log("DB-GET-SHARE-FAILED","No sharing document based on code: "+sharing_code);
            return null;
        } catch(Exception ex){
            database.log("DB-GET-SHARE-FAILED","Failed to share project ("+ex.toString()+")");
            return null;
        }
    }
}

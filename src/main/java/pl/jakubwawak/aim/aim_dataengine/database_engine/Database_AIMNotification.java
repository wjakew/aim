/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.aim_dataengine.database_engine;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Notification;

/**
 * Object for maitaining Notifications on database
 */
public class Database_AIMNotification {


    Database_Connector database;

    /**
     * Constructor
     * @param database
     */
    public Database_AIMNotification(Database_Connector database){
        this.database = database;
    }

}

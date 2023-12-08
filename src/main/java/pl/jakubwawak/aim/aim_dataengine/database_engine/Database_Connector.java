/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package pl.jakubwawak.aim.aim_dataengine.database_engine;

import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.Document;
import org.bson.conversions.Bson;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_ApplicationLog;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_GlobalConfiguration;
import pl.jakubwawak.maintanance.ConsoleColors;
import pl.jakubwawak.maintanance.GridElement;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

/**
 * Object for connecting to database
 */
public class Database_Connector {
    public String database_url;

    public boolean connected;

    MongoClient mongoClient;
    MongoDatabase mongoDatabase;

    ArrayList<String> error_collection;

    /**
     * Constructor
     */
    public Database_Connector(){
        this.database_url = "";
        connected = false;
        error_collection = new ArrayList<>();
    }

    public void setDatabase_url(String database_url){
        this.database_url = database_url;
    }

    /**
     * Function for connecting to database
     * @return boolean
     */
    public void connect(){
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(database_url))
                .serverApi(serverApi)
                .build();
        try{
            mongoClient = MongoClients.create(settings);
            MongoDatabase database = mongoClient.getDatabase("admin");
            // Send a ping to confirm a successful connection
            Bson command = new BsonDocument("ping", new BsonInt64(1));
            Document commandResult = database.runCommand(command);
            connected = true;
            mongoDatabase = mongoClient.getDatabase("db_aim");
            log("DB-CONNECTION","Connected succesffully with database - running application");
        }catch(MongoException ex){
            // catch error
            log("DB-CONNECTION-ERROR", "Failed to connect to database ("+ex.toString()+")");
            connected = false;
        }
    }

    /**
     * Function for loading global configuration from database
     * @return AIM_GlobalConfiguration
     */
    public AIM_GlobalConfiguration getGlobalConfiguration(){
        MongoCollection<Document> configuration_collection = get_data_collection("aim_globalconfiguration");
        Document configuration_document = configuration_collection.find().first();
        if ( configuration_document == null ){
            AIM_GlobalConfiguration agc = new AIM_GlobalConfiguration();
            configuration_collection.insertOne(agc.prepareDocument());
            return agc;
        }
        else{
            return new AIM_GlobalConfiguration(configuration_document);
        }
    }

    /**
     * Function for disabling account creation
     * @return Integer
     */
    public int disableAccountCreation(){
        try{
            MongoCollection<Document> configuration_collection = get_data_collection("aim_globalconfiguration");
            Document configuration_document = configuration_collection.find().first();
            if ( configuration_document != null ){
                Bson updates = Updates.combine(
                        Updates.set("userCreationFlag",0)
                );
                AimApplication.globalConfiguration.userCreationFlag =0;
                UpdateResult result = configuration_collection.updateOne(configuration_document,updates);
                if (result.getModifiedCount() > 0){
                    log("DB-DISABLEACCCREATION","Updated account creation flag. Set to 0");
                    return 1;
                }
                AimApplication.globalConfiguration = new AIM_GlobalConfiguration(configuration_document);
                log("DB-DISABLEACCOUNTCREATION","Cannot update global configuration, nothing to update");
                return 0;
            }
            log("DB-DISABLEACCREATION-NULL","Nothing to change, no document data!");
            return 0;
        }catch(Exception ex){
            log("DB-DISABLEACCOUNTCREATION-FAILED","Failed to disable account creation ("+ex.toString()+")");
            return -1;
        }
    }

    /**
     * Function for enabling account creation
     * @return Integer
     */
    public int enableAccountCreation(){
        try{
            MongoCollection<Document> configuration_collection = get_data_collection("aim_globalconfiguration");
            Document configuration_document = configuration_collection.find().first();
            if ( configuration_document != null ){
                Bson updates = Updates.combine(
                        Updates.set("userCreationFlag",1)
                );
                AimApplication.globalConfiguration.userCreationFlag =1;
                UpdateResult result = configuration_collection.updateOne(configuration_document,updates);
                if (result.getModifiedCount() > 0){
                    log("DB-ENABLEACCCREATION","Updated account creation flag. Set to 1");
                    return 1;
                }
                AimApplication.globalConfiguration = new AIM_GlobalConfiguration(configuration_document);
                log("DB-ENABLEACCOUNTCREATION","Cannot update global configuration, nothing to update");
                return 0;
            }
            log("DB-ENABLEACCREATION-NULL","Nothing to change, no document data!");
            return 0;
        }catch(Exception ex){
            log("DB-ENABLEACCOUNTCREATION-FAILED","Failed to disable account creation ("+ex.toString()+")");
            return -1;
        }
    }

    /**
     * Function for inserting log data to database
     * @param applicationLog
     * @return Integer
     */
    public int insertLog(AIM_ApplicationLog applicationLog){
        try{
            MongoCollection<Document> log_collection = get_data_collection("aim_applog");
            log_collection.insertOne(applicationLog.prepareDocument());
            return 1;
        }catch(Exception ex){
            log("DB-INSERTLOG-FAILED","Failed to insert log ("+ex.toString()+")");
            return -1;
        }
    }

    /**
     * Function for loading collections
     * @param collection_name
     * @return MongoCollection<Document>
     */
    MongoCollection<Document> get_data_collection(String collection_name){
        return mongoDatabase.getCollection(collection_name);
    }

    /**
     * Function for story log data
     * @param log_category
     * @param log_text
     */
    public void log(String log_category, String log_text){
        error_collection.add(log_category+"("+ LocalDateTime.now(ZoneId.of("Europe/Warsaw")).toString()+") - "+log_text);
        if ( log_category.contains("FAILED") || log_category.contains("ERROR")){
            System.out.println(ConsoleColors.RED_BRIGHT+log_category+"["+ LocalDateTime.now(ZoneId.of("Europe/Warsaw")).toString()+") - "+log_text+"]"+ConsoleColors.RESET);
            try{
                Notification noti = Notification.show(log_text);
                noti.addThemeVariants(NotificationVariant.LUMO_ERROR);

            }catch(Exception ex){}
        }
        else{
            System.out.println(ConsoleColors.GREEN_BRIGHT+log_category+"["+ LocalDateTime.now(ZoneId.of("Europe/Warsaw")).toString()+") - "+log_text+"]"+ConsoleColors.RESET);
        }
        // inserting log to database
        if (AimApplication.log_database_dump_flag == 1){
            if ( !log_category.equals("DB-INSERTLOG-FAILED")){
                insertLog(new AIM_ApplicationLog(log_category,log_text));
            }
        }
    }

    /**
     * Function for loading log data from database
     * @return ArrayList
     */
    public ArrayList<GridElement> getLog(){
        ArrayList<GridElement> data = new ArrayList<>();
        try{
            MongoCollection<Document> log_collection = get_data_collection("aim_applog");
            FindIterable<Document> log_documents = log_collection.find();
            int logID = 0;
            for(Document log : log_documents ){
                data.add(new GridElement(log.getString("log_desc"),logID,log.getString("log_code")));
                logID++;
            }
        }catch(Exception ex){
            log("GET-LOG-FAILED","Failed to get log data ("+ex.toString()+")");
        }
        return data;
    }
}

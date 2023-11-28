/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.aim_dataengine.database_engine;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_WidgetPanel;

/**
 * Object for maintaining widget panel data on database
 */
public class Database_AIMWidgetPanel {

    public Database_Connector database;

    /**
     * Constructor
     * @param database
     */
    public Database_AIMWidgetPanel(Database_Connector database){
        this.database = database;
    }

    /**
     * Function for loading panel collection from database
     * @return AIM_WidgetPanel
     */
    public AIM_WidgetPanel getPanelData(){
        try{
            MongoCollection panelCollection = database.get_data_collection("aim_widgetpanel");
            FindIterable<Document> panelDocuments = panelCollection.find();
            for(Document panelDocument : panelDocuments){
                if ( panelDocument.getObjectId("aim_user_id").equals(AimApplication.loggedUser.aim_user_id)){
                    // user correct
                    database.log("DB-WIDGET-PANEL","Loaded widget panel from database!");
                    return new AIM_WidgetPanel(panelDocument);
                }
            }
            database.log("DB-WIDGET-PANEL","Nothing found on database, created new panel record!");
            insertPanelData(new AIM_WidgetPanel());
            return new AIM_WidgetPanel();
        }catch(Exception ex){
            database.log("DB-WIDGET-PANEL-FAILED","Failed to get panel data from database ("+ex.toString()+")");
            return null;
        }
    }

    /**
     * Function for updating panel data
     * @param panelData
     * @return Integer
     */
    public int updatePanelData(AIM_WidgetPanel panelData){
        try{
            MongoCollection panelCollection = database.get_data_collection("aim_widgetpanel");
            DeleteResult result = panelCollection.deleteOne(new Document("aim_user_id",AimApplication.loggedUser.aim_user_id));
            if (result.wasAcknowledged()){
                int ans = insertPanelData(panelData);
                if ( ans == 1){
                    database.log("DB-WIDGET-UPDATE","Updated widget panel data!");
                    return 1;
                }
                else{
                    database.log("DB-WIDGET-UPDATE-FAILED","Failed to insert new panel data. Check log!");
                    return 0;
                }
            }
            database.log("DB-WIDGET-UPDATE","Nothing to update, check widget panel changes!");
            return 0;
        }catch(Exception ex){
            database.log("DB-WIDGET-UPDATE-FAILED","Failed to update widget panel data ("+ex.toString()+")");
            return -1;
        }
    }


    /**
     * Function for inserting panel data into database
     * @param panelData
     * @return Integer
     */
    public int insertPanelData(AIM_WidgetPanel panelData){
        try{
            MongoCollection panelCollection = database.get_data_collection("aim_widgetpanel");
            InsertOneResult result = panelCollection.insertOne(panelData.prepareDocument());
            if ( result.wasAcknowledged() ){
                database.log("DB-WGPANEL-INSERT","Inserted panel data for user ID("+AimApplication.loggedUser.aim_user_id.toString()+")");
                return 1;
            }
            database.log("DB-WGPANEL-PASSED","Panel document not inserted. No change / no data.");
            return 0;
        }catch(Exception ex){
            database.log("DB-WGPANEL-FAILED","Failed to insert panel document do database ("+ex.toString()+")");
            return -1;
        }
    }
}

/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.aim_dataengine.database_engine;

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
}

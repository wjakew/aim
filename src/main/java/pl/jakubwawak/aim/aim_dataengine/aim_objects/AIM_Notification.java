/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.aim_dataengine.aim_objects;

import org.bson.types.ObjectId;

/**
 * Object for storing notification data
 */
public class AIM_Notification {

    public String notificationTitle;
    public int notificationServerity;
    public int notificationReadFlag;
    public String notificationDesc;

    public ObjectId notificationTarget;

    /**
     * Constructor
     */
    public AIM_Notification(){
        notificationTitle = "test";
        notificationServerity = -1;
        notificationDesc = "";
        notificationTarget = null;
        notificationReadFlag = 0;
    }

}

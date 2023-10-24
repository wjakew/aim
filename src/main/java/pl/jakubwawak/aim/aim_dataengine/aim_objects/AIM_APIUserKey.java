/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.aim_dataengine.aim_objects;

import org.bson.Document;
import org.bson.types.ObjectId;
import pl.jakubwawak.aim.AimApplication;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Object for creating random user api keys
 */
public class AIM_APIUserKey {

    ObjectId aim_user_id;
    public String apiuserkey_value;

    public String apiuserkey_timegenerated;

    public int apiuserkey_activeflag;

    /**
     * Constructor
     */
    public AIM_APIUserKey(){
        aim_user_id = AimApplication.loggedUser.aim_user_id;
        apiuserkey_value = UUID.randomUUID().toString();
        apiuserkey_timegenerated = LocalDateTime.now().toString();
        apiuserkey_activeflag = 1;
    }

    /**
     * Constructor with database support
     * @param document
     */
    public AIM_APIUserKey(Document document){
        aim_user_id = document.getObjectId("aim_user_id");
        apiuserkey_value = document.getString("apiuserkey_value");
        apiuserkey_timegenerated = document.getString("apiuserkey_timegenerated");
        apiuserkey_activeflag = document.getInteger("apiuserkey_activeflag");
    }

    /**
     * Function for preparing document
     * @return
     */
    public Document prepareDocument(){
        Document document = new Document();
        document.append("aim_user_id",aim_user_id);
        document.append("apiuserkey_value",apiuserkey_value);
        document.append("apiuserkey_timegenerated",apiuserkey_timegenerated);
        document.append("apiuserkey_activeflag",apiuserkey_activeflag);
        return document;
    }
}

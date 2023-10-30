/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.aim_dataengine.aim_objects;

import org.bson.Document;
import org.bson.types.ObjectId;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.maintanance.RandomWordGeneratorEngine;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Object for creating random user api keys
 */
public class AIM_APIUserKey {

    public AIM_User aim_user;
    public String apiuserkey_value;

    public String apiuserkey_timegenerated;

    public int apiuserkey_activeflag;

    /**
     * Constructor
     */
    public AIM_APIUserKey(){
        aim_user = AimApplication.loggedUser;
        RandomWordGeneratorEngine rwge = new RandomWordGeneratorEngine();
        apiuserkey_value = rwge.generateRandomString(20,true,false);
        apiuserkey_timegenerated = LocalDateTime.now().toString();
        apiuserkey_activeflag = 1;
    }

    /**
     * Constructor with database support
     * @param document
     */
    public AIM_APIUserKey(Document document){
        aim_user = new AIM_User(document.get("aim_user",Document.class));
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
        document.append("aim_user",aim_user.prepareDocument());
        document.append("apiuserkey_value",apiuserkey_value);
        document.append("apiuserkey_timegenerated",apiuserkey_timegenerated);
        document.append("apiuserkey_activeflag",apiuserkey_activeflag);
        return document;
    }
}

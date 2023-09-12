/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package pl.jakubwawak.aim.aim_dataengine.aim_objects;

import org.bson.Document;
import org.bson.types.ObjectId;

/**
 * Object for storing user information from database
 */
public class AIM_User {

    public ObjectId aim_user_id;
    public String aim_user_email;
    public String aim_user_password;
    public String aim_user_name;
    public String aim_user_surname;

    public String aim_user_type; // type of user SERVERADM, NRLUSER

    public String aim_user_telephone;

    public String aim_user_configuration1;
    public String aim_user_configuration2;
    public String aim_user_configuration3;
    public String aim_user_configuration4;


    /**
     * Constructor
     */
    public AIM_User() {
        aim_user_id = null;
        aim_user_email = "";
        aim_user_password = "";
        aim_user_name = "";
        aim_user_surname = "";
        aim_user_type = "";
        aim_user_telephone = "";
        aim_user_configuration1 = "";
        aim_user_configuration2 = "";
        aim_user_configuration3 = "";
        aim_user_configuration4 = "";
    }

    /**
     * Constructor with database support
     */
    public AIM_User(Document user_document) {
        aim_user_id = user_document.getObjectId("_id");
        aim_user_email = user_document.getString("aim_user_email");
        aim_user_password = user_document.getString("aim_user_password");
        aim_user_name = user_document.getString("aim_user_name");
        aim_user_surname = user_document.getString("aim_user_surname");
        aim_user_type = user_document.getString("aim_user_type");
        aim_user_telephone = user_document.getString("aim_user_telephone");
        aim_user_configuration1 = user_document.getString("aim_user_configuration1");
        aim_user_configuration2 = user_document.getString("aim_user_configuration2");
        aim_user_configuration3 = user_document.getString("aim_user_configuration3");
        aim_user_configuration4 = user_document.getString("aim_user_configuration4");
    }

    /**
     * Function for preparing document data to post on database
     * @return Document
     */
    public Document prepareDocument(){
        Document user_document = new Document();
        user_document.append("aim_user_email",aim_user_email);
        user_document.append("aim_user_password",aim_user_password);
        user_document.append("aim_user_name",aim_user_name);
        user_document.append("aim_user_email",aim_user_email);
        user_document.append("aim_user_surname",aim_user_surname);
        user_document.append("aim_user_type",aim_user_type);
        user_document.append("aim_user_telephone",aim_user_telephone);
        user_document.append("aim_user_configuration1",aim_user_configuration1);
        user_document.append("aim_user_configuration2",aim_user_configuration2);
        user_document.append("aim_user_configuration3",aim_user_configuration3);
        user_document.append("aim_user_configuration4",aim_user_configuration4);
        return user_document;
    }
}

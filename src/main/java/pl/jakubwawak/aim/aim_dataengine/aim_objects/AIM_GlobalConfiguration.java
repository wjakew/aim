/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package pl.jakubwawak.aim.aim_dataengine.aim_objects;

import org.bson.Document;
import pl.jakubwawak.aim.AimApplication;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Object for storing application global configuration
 */
public class AIM_GlobalConfiguration {

    public String version, build;
    public Date startuptime;

    public int userCreationFlag; // 1 - create new user, 0 - disabled

    public String globalConfigurationFeature1;
    public String globalConfigurationFeature2;
    public String globalConfigurationFeature3;
    public String globalConfigurationFeature4;

    /**
     * Constructor
     */
    public AIM_GlobalConfiguration(){
        version = AimApplication.version;
        build = AimApplication.build;
        startuptime = new Date();
        userCreationFlag = 1;
        globalConfigurationFeature1 = "";
        globalConfigurationFeature2 = "";
        globalConfigurationFeature3 = "";
        globalConfigurationFeature4 = "";
    }

    /**
     * Constructor with database support
     */
    public AIM_GlobalConfiguration(Document globalConfiguration){
        version = AimApplication.version;
        build = AimApplication.build;
        startuptime = globalConfiguration.get("startuptime", Date.class);
        userCreationFlag = globalConfiguration.getInteger("userCreationFlag");
        globalConfigurationFeature1 = globalConfiguration.getString("globalConfigurationFeature1");
        globalConfigurationFeature2 = globalConfiguration.getString("globalConfigurationFeature2");
        globalConfigurationFeature3 = globalConfiguration.getString("globalConfigurationFeature3");
        globalConfigurationFeature4 = globalConfiguration.getString("globalConfigurationFeature4");
    }

    /**
     * Function for loading global configuration
     * @return Document
     */
    public Document prepareDocument(){
        Document globalConfiugration = new Document();
        globalConfiugration.append("version",version);
        globalConfiugration.append("build",build);
        globalConfiugration.append("startuptime",startuptime);
        globalConfiugration.append("userCreationFlag",userCreationFlag);
        globalConfiugration.append("globalConfigurationFeature1",globalConfigurationFeature1);
        globalConfiugration.append("globalConfigurationFeature2",globalConfigurationFeature2);
        globalConfiugration.append("globalConfigurationFeature3",globalConfigurationFeature3);
        globalConfiugration.append("globalConfigurationFeature4",globalConfigurationFeature4);
        return globalConfiugration;
    }

}

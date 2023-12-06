/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.aim_dataengine.aim_objects.codingproject;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Object for storing coding project data
 */
public class AIM_CodingTask {

    public ObjectId aim_codingtask_id;

    public ObjectId aim_user_id; // owner ID

    public String aim_codingtask_tag; // tag field for eg version number or category

    public String aim_codingtask_timestamp;

    public String aim_codingtask_name;

    public String aim_codingtask_desc;

    public List<Document> aim_codingtask_comments;

    public List<Document> aim_codingtask_history;
}

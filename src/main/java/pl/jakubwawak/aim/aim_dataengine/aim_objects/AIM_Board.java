/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package pl.jakubwawak.aim.aim_dataengine.aim_objects;

import org.bson.Document;
import org.bson.types.ObjectId;
import pl.jakubwawak.aim.AimApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Object for storing board data
 */
public class AIM_Board {

    public ObjectId board_id;

    public String board_name;
    public Document board_owner;
    public List<Document> board_members;
    public String board_desc;
    public List<Document> task_list;
    public List<String> board_history;

    /**
     * Constructor
     */
    public AIM_Board() {
        this.board_id = null;
        this.board_name = "";
        this.board_owner = AimApplication.loggedUser.prepareDocument();
        this.board_members = new ArrayList<>();
        this.board_desc = "";
        this.task_list = new ArrayList<>();
        this.board_history = new ArrayList<>();
    }

    /**
     * Constructor with database support
     */
    public AIM_Board(Document board_document) {
        this.board_id = board_document.getObjectId("_id");
        this.board_name = board_document.getString("board_name");
        this.board_owner = board_document.get("board_owner",Document.class);
        this.board_members = board_document.getList("board_members",Document.class);
        this.board_desc = board_document.getString("board_desc");
        this.task_list = board_document.getList("task_list",Document.class);
        this.board_history = board_document.getList("board_history",String.class);
    }

    /**
     * Function for checking if board isEmpty
     * @return boolean
     */
    public boolean isEmpty(){
        return board_name.isEmpty() && board_desc.isEmpty();
    }

    /**
     * Function for loading object data to Document
     * @return Document
     */
    public Document prepareDocument(){
        Document board_document = new Document();
        board_document.append("board_id",board_id);
        board_document.append("board_name",board_name);
        board_document.append("board_owner",board_owner);
        board_document.append("board_members",board_members);
        board_document.append("board_desc",board_desc);
        board_document.append("task_list",task_list);
        board_document.append("board_history",board_history);
        return board_document;
    }

    /**
     * Function for generating owner label
     * @return String
     */
    public String ownerLabel(){
        if (board_owner.getString("aim_user_email").equals(AimApplication.loggedUser.aim_user_email)){
            return "Owner";
        }
        return "Member";
    }

    /**
     * Function for loading task list
     * @return ArrayList
     */
    public ArrayList<AIM_BoardTask> getTaskList(){
        ArrayList<AIM_BoardTask> data = new ArrayList<>();
        for(Document taskboard_document : task_list){
            data.add(new AIM_BoardTask(taskboard_document));
        }
        return data;
    }

    public String getBoard_name() {
        return board_name;
    }

    public Document getBoard_owner() {
        return board_owner;
    }

    public String getBoard_owner_glance(){
        return board_owner.getString("aim_user_email");
    }

    public List<Document> getBoard_members() {
        return board_members;
    }

    public int getBoard_members_size(){
        return board_members.size();
    }
}

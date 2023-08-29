/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package pl.jakubwawak.aim.aim_objects;

import org.bson.Document;
import pl.jakubwawak.aim.AimApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Object for storing board data
 */
public class AIM_Board {

    public String board_name;
    public AIM_User board_owner;
    public List<AIM_User> board_members;
    public String board_desc;
    public List<AIM_Task> task_list;
    public ArrayList<String> board_history;


    /**
     * Constructor
     */
    public AIM_Board() {
        this.board_name = "";
        this.board_owner = AimApplication.loggedUser;
        this.board_members = new ArrayList<>();
        this.board_desc = "";
        this.task_list = new ArrayList<>();
        this.board_history = new ArrayList<>();
    }

    /**
     * Constructor with database support
     */
    public AIM_Board(Document board_document) {
        this.board_name = board_document.getString("board_name");
        this.board_owner = board_document.get("board_owner",AIM_User.class);
        this.board_members = new ArrayList<>();
        this.board_desc = "";
        this.task_list = new ArrayList<>();
        this.board_history = new ArrayList<>();
    }

    /**
     * Function for loading object data to Document
     * @return Document
     */
    public Document prepareDocument(){
        Document board_document = new Document();
        board_document.append("board_name",board_name);
        board_document.append("board_owner",board_owner);
        board_document.append("board_members",board_members);
        board_document.append("board_desc",board_desc);
        board_document.append("task_list",task_list);
        board_document.append("board_history",board_history);
        return board_document;
    }
}

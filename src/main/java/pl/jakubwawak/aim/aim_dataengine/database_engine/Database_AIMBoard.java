/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.aim_dataengine.database_engine;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Board;

import java.util.ArrayList;

/**
 * Object for maintaining board data on database
 */
public class Database_AIMBoard {

    Database_Connector database;

    /**
     * Constructor
     * @param database
     */
    public Database_AIMBoard(Database_Connector database){
        this.database = database;
    }

    /**
     * Function for getting board collection
     * @return ArrayList
     */
    public ArrayList<AIM_Board> getUserBoardList(){
        ArrayList<AIM_Board> data = new ArrayList<>();
        try{
            MongoCollection<Document> board_collection = database.get_data_collection("aim_board");
            FindIterable<Document> board_documents = board_collection.find();
            for(Document board_document : board_documents){
                AIM_Board board = new AIM_Board(board_document);
                if ( board.board_owner.equals(AimApplication.loggedUser.prepareDocument()) || board.board_members.contains(AimApplication.loggedUser.prepareDocument())){
                    data.add(board);
                }
            }
            database.log("DB-LIST-BOARD","Loaded "+data.size()+" objects from database for "+AimApplication.loggedUser.aim_user_id.toString());
        }catch(Exception ex){
            database.log("DB-LIST-BOARD-FAILED","Failed to list board objects for user ("+ex.toString()+")");
        }
        return data;
    }

    /**
     * Function for inserting board to database
     * @param boardToAdd
     * @return Integer
     */
    public int insertBoard(AIM_Board boardToAdd){
        try{
            MongoCollection<Document> board_collection = database.get_data_collection("aim_board");
            if ( boardToAdd != null ){
                board_collection.insertOne(boardToAdd.prepareDocument());
                database.log("DB-INSERT-BOARD","Board inserted from "+ AimApplication.loggedUser.aim_user_email);
                return 1;
            }
            return 0;
        }catch(Exception ex){
            database.log("DB-INSERT-BOARD-FAILED","Failed to insert board ("+ex.toString()+")");
            return -1;
        }
    }
}

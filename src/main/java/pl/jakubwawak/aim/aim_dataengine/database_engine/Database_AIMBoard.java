/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.aim_dataengine.database_engine;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.cglib.core.Local;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Board;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_BoardTask;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * Function for updating board data
     * @param boardToUpdate
     * @return Integer
     */
    public int updateBoard(AIM_Board boardToUpdate){
        try{
            MongoCollection<Document> board_collection = database.get_data_collection("aim_board");
            Document board_document = board_collection.find(new Document("_id",boardToUpdate.board_id)).first();
            boardToUpdate.board_history.add(LocalDateTime.now().toString()+" - Updated board data");
            if ( board_document != null ){
                Bson updates = Updates.combine(
                        Updates.set("board_name",boardToUpdate.board_name),
                        Updates.set("board_members",boardToUpdate.board_members),
                        Updates.set("board_desc",boardToUpdate.board_desc)
                );
                UpdateResult result = board_collection.updateOne(board_document,updates);
                if ( result.getModifiedCount() > 0 ){
                    database.log("DB-BOARD-UPDATE","Board ("+boardToUpdate.board_id.toString()+") was updated (code:"+result.getModifiedCount()+")");
                    return 1;
                }
                database.log("DB-BOARD-UPDATE","Board ("+boardToUpdate.board_id.toString()+") wasn't updated, (code:0)");
                return 0;
            }
            database.log("DB-BOARD-UPDATE","Board ("+boardToUpdate.board_id.toString()+") wasn't updated, cannot find board");
            return 0;
        }catch(Exception ex){
            database.log("DB-BOARD-UPDATE-FAILED","Failed to update board ("+boardToUpdate.board_id.toString()+") error("+ex.toString()+")");
            return -1;
        }
    }

    /**
     * Function for changing board user owner
     * @param board
     * @param usernewOwner
     * @return Integer
     */
    public int changeBoardOwner(AIM_Board board, Document usernewOwner){
        try{
            MongoCollection<Document> board_collection = database.get_data_collection("aim_board");
            Document board_document = board_collection.find(new Document("_id",board.board_id)).first();
            if ( board_document != null ){
                board.board_owner = usernewOwner;
                board.board_history.add(LocalDateTime.now().toString()+" - Changed owner to: "+usernewOwner.getString("aim_user_email"));
                Bson updates = Updates.combine(
                        Updates.set("board_owner",board.board_owner),
                        Updates.set("board_history",board.board_history)
                );
                UpdateResult result = board_collection.updateOne(board_document,updates);
                if ( result.getModifiedCount() > 0 ){
                    database.log("DB-BOARD-CHNGOWNER","Board ("+board.board_id.toString()+") changed owner (code:"+result.getModifiedCount()+")");
                    return 1;
                }
                database.log("DB-BOARD-CHNGOWNER","Board ("+board.board_id.toString()+") wasn't updated to new owner data, (code:0)");
                return 0;
            }
            database.log("DB-BOARD-CHNGOWNER","Board ("+board.board_id.toString()+") wasn't updated, cannot find board");
            return 0;
        }catch(Exception ex){
            database.log("DB-BOARD-CHNGOWNER-FAILED","Failed to change owner of board ("+board.board_id.toString()+") error("+ex.toString()+")");
            return -1;
        }
    }

    /**
     * Function for inserting task to board
     * @param board
     * @param task
     * @return Integer
     */
    public int insertTaskToBoard(AIM_Board board, AIM_BoardTask task){
        try {
            MongoCollection<Document> board_collection = database.get_data_collection("aim_board");
            Document board_document = board_collection.find(new Document("_id",board.board_id)).first();
            if ( board_document != null ){
                // adding to collection
                board.task_list.add(task.prepareDocument());
                board.board_history.add(LocalDateTime.now().toString()+" - Added new task ("+task.aim_task_name+")");
                // setting updates
                Bson updates = Updates.combine(
                        Updates.set("task_list",board.task_list),
                        Updates.set("board_history",board.board_history)
                );
                UpdateResult result = board_collection.updateOne(board_document,updates);
                if ( result.getModifiedCount() > 0){
                    database.log("DB-BOARD-INSERT-TASK","Task ("+task.aim_task_name+") inserted to ("+board.board_id.toString()+")");
                    return 1;
                }
                database.log("DB-BOARD-INSERT-TASK","No updates found during task addition to board ("+board.board_id+")");
                return 0;
            }
            database.log("DB-BOARD-INSERT-TASK","Board is empty on database ("+board.board_id+")");
            return 0;

        }catch (Exception ex){
            database.log("DB-INSERT-BOARD-TASK-INSERT-FAILED","Failed to insert task to board ("+ex.toString()+")");
            return -1;
        }
    }
}

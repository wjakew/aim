/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.aim_dataengine.database_engine;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.cglib.core.Local;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.*;

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
     * Function for getting all board collection
     * @return ArrayList
     */
    public ArrayList<AIM_Board> getAllBoardList(){
        ArrayList<AIM_Board> data = new ArrayList<>();
        try{
            MongoCollection<Document> board_collection = database.get_data_collection("aim_board");
            FindIterable<Document> board_documents = board_collection.find();
            for(Document board_document : board_documents){
                data.add(new AIM_Board(board_document));

            }
            database.log("DB-LIST-BOARD","Loaded "+data.size()+" objects from database for "+AimApplication.loggedUser.aim_user_id.toString());
        }catch(Exception ex){
            database.log("DB-LIST-BOARD-FAILED","Failed to list board objects for user ("+ex.toString()+")");
        }
        return data;
    }

    /**
     * Function for loading board object by ID
     * @param board_id
     * @return AIM_Board
     */
    public AIM_Board getBoard(ObjectId board_id){
        try{
            MongoCollection<Document> board_collection = database.get_data_collection("aim_board");
            Document board_document = board_collection.find(new Document("_id",board_id)).first();
            if (board_document != null){
                database.log("DB-GET-BOARD","Found board for given id ("+board_id.toString()+") task size: "+board_document.getList("task_list",Document.class).size());
                return new AIM_Board(board_document);
            }
            database.log("DB-GET-BOARD","Cannot find board for id ("+board_id.toString()+")");
            return null;
        }catch(Exception ex){
            database.log("DB-GET-BOARD-FAILED","Failed to get board data ("+ex.toString()+")");
            return null;
        }
    }

    /**
     * Function for loading board object by name
     * @param board_name
     * @return AIM_Board
     */
    public AIM_Board getBoard(String board_name){
        try{
            MongoCollection<Document> board_collection = database.get_data_collection("aim_board");
            Document board_document = board_collection.find(new Document("board_name",board_name)).first();
            if (board_document != null){
                database.log("DB-GET-BOARD","Found board for given name ("+board_name+") task size: "+board_document.getList("task_list",Document.class).size());
                return new AIM_Board(board_document);
            }
            database.log("DB-GET-BOARD","Cannot find board for id ("+board_name+")");
            return null;
        }catch(Exception ex){
            database.log("DB-GET-BOARD-FAILED","Failed to get board data ("+ex.toString()+")");
            return null;
        }
    }

    /**
     * Function for getting board by name
     * @param boardName
     * @return
     */
    public AIM_Board getBoardByName(String boardName){
        try{
            MongoCollection<Document> board_collection = database.get_data_collection("aim_board");
            Document board_document = board_collection.find(new Document("board_name",boardName)).first();
            if ( board_document != null ){
                database.log("DB-GET-BOARD-SINGLE","Found board for given name ("+boardName+")");
                return new AIM_Board(board_document);
            }
            database.log("DB-GET-BOARD","Cannot find board for id ("+boardName+")");
            return null;
        }catch(Exception ex) {
            database.log("DB-GET-BOARD-FAILED", "Failed to get board data (" + ex.toString() + ")");
            return null;
        }
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

    /**
     * Function for linking task to board
     * @param board
     * @param task
     * @return Integer
     */
    public int linkTaskToBoard(AIM_Board board, AIM_Task task){
        try{
            task.aim_task_id = null;
            task.aim_task_history.add(AimApplication.loggedUser.aim_user_email+" - "+ LocalDateTime.now().toString()+" - task linked to board");
            int ans = insertTaskToBoard(board,new AIM_BoardTask(task));
            if ( ans == 1 ){
                Database_AIMTask dat = new Database_AIMTask(database);
                dat.remove(task);
                database.log("DB-BOARD-LINK-TASK","Task ("+task.aim_task_name+") linked to board ("+board.board_id.toString()+")");
                return 1;
            }
            else{
                database.log("DB-BOARD-LINK-TASK-FAILED","Failed to link task to board, check log.");
                return 0;
            }
        }catch(Exception ex){
            database.log("DB-BOARD-LINK-TASK-FAILED","Failed to link task to board ("+ex.toString()+")");
            return -1;
        }
    }

    /**
     * Function for changing task status
     * @param board
     * @param taskToUpdate
     * @param newStatus
     * @return Integer
     */
    public int changeTaskStatusOnBoard(AIM_Board board, AIM_BoardTask taskToUpdate, String newStatus){
        try{
            MongoCollection<Document> board_collection = database.get_data_collection("aim_board");
            // loading board object
            Document board_document = board_collection.find(new Document("_id",board.board_id)).first();
            AIM_Board currentBoard = new AIM_Board(board_document);
            // removing task from object
            currentBoard.task_list.remove(taskToUpdate.prepareDocument());
            // setting new task status and adding history
            taskToUpdate.status = newStatus;
            taskToUpdate.aim_task_history.add(AimApplication.loggedUser.aim_user_email+" - "+LocalDateTime.now(ZoneId.of("Europe/Warsaw"))+" - changed status to: "+newStatus);
            currentBoard.task_list.add(taskToUpdate.prepareDocument());
            // adding data to board history
            currentBoard.board_history.add(AimApplication.loggedUser.aim_user_email+" - "+LocalDateTime.now().toString()+" - updated task ("+taskToUpdate.aim_task_name+")");
            // loading replacing query
            Bson query = Filters.eq("_id",board.board_id);
            // replacing board data
            ReplaceOptions opts = new ReplaceOptions().upsert(true);
            UpdateResult result = board_collection.replaceOne(query,currentBoard.prepareDocument(),opts);
            if ( result.getModifiedCount() > 0 ){
                database.log("DB-BOARD-TASK-UPDATE","Task status on board ("+board.board_id.toString()+") updated!");
                return 1;
            }
            database.log("DB-BOARD-TASK-UPDATE","Nothing on update while changing status ("+taskToUpdate.aim_task_id.toString()+") on board ("+board.board_id+")");
            return 0;
        }catch(Exception ex){
            database.log("DB-BOARD-TASK-UPDATE-FAILED","Failed to update task status on project ("+ex.toString()+")");
            return -1;
        }
    }

    /**
     * Function for changing assigned user to task
     * @param board
     * @param taskToUpdate
     * @param newAssignedEmail
     * @return Integer
     */
    public int changeAssinedUserToTask(AIM_Board board, AIM_BoardTask taskToUpdate, String newAssignedEmail){
        Document newAssignedUserDocument = null;
        if ( !newAssignedEmail.equals("All") ){
            Database_AIMUser dau = new Database_AIMUser(AimApplication.database);
            newAssignedUserDocument = dau.getAIMUser(newAssignedEmail).prepareDocument();
        }
        try{
            MongoCollection<Document> board_collection = database.get_data_collection("aim_board");
            // loading board object
            Document board_document = board_collection.find(new Document("_id",board.board_id)).first();
            AIM_Board currentBoard = new AIM_Board(board_document);
            // removing task from object
            currentBoard.task_list.remove(taskToUpdate.prepareDocument());
            // setting new task status and adding history
            taskToUpdate.aim_user_assigned = newAssignedUserDocument;
            taskToUpdate.aim_task_history.add(AimApplication.loggedUser.aim_user_email+" - "+LocalDateTime.now(ZoneId.of("Europe/Warsaw"))+" - assigned user changed to: "+newAssignedEmail);
            currentBoard.task_list.add(taskToUpdate.prepareDocument());
            // adding data to board history
            currentBoard.board_history.add(AimApplication.loggedUser.aim_user_email+" - "+LocalDateTime.now().toString()+" - updated task ("+taskToUpdate.aim_task_name+")");
            // loading replacing query
            Bson query = Filters.eq("_id",board.board_id);
            // replacing board data
            ReplaceOptions opts = new ReplaceOptions().upsert(true);
            UpdateResult result = board_collection.replaceOne(query,currentBoard.prepareDocument(),opts);
            if ( result.getModifiedCount() > 0 ){
                database.log("DB-BOARD-TASK-CHANGEASSIGNED","Task assigned on board ("+board.board_id.toString()+") updated!");
                return 1;
            }
            database.log("DB-BOARD-TASK-CHANGEASSIGNED","Nothing on update while changing assigned user ("+taskToUpdate.aim_task_id.toString()+") on board ("+board.board_id+")");
            return 0;
        }catch(Exception ex){
            database.log("DB-BOARD-TASK-CHANGEASSIGNED-FAILED","Failed to update task assigned user on project ("+ex.toString()+")");
            return -1;
        }
    }

    /**
     * Function for removing task from board
     * @param taskToRemove
     * @return Integer
     */
    public int removeTaskFromBoard(AIM_Board board, AIM_BoardTask taskToRemove){
        try{
            MongoCollection<Document> board_collection = database.get_data_collection("aim_board");
            Document board_document = board_collection.find(new Document("_id",board.board_id)).first();
            if (board_document != null){
                List<Document> taskList = board_document.getList("task_list",Document.class);
                taskList.remove(taskToRemove.prepareDocument());
                List<String> board_history = board_document.getList("board_history",String.class);
                board_history.add(AimApplication.loggedUser.aim_user_email+" - "+LocalDateTime.now().toString()+" - removed task ("+taskToRemove.aim_task_name+")");
                Bson updates = Updates.combine(
                        Updates.set("task_list",taskList),
                        Updates.set("board_history",board_history)
                );
                UpdateResult result = board_collection.updateOne(board.prepareDocument(), updates);
                if ( result.getModifiedCount() > 0) {
                    database.log("DB-BOARD-TASK-REMOVE", "Removed task from board (" + board.board_id.toString() + ") code (" + result.getModifiedCount() + ")");
                    return 1;
                }
            }
            database.log("DB-BOARD-TASK-REMOVE","Task cannot be removed, result is 0 or board is empty!");
            return 0;
        }catch(Exception ex){
            database.log("DB-BOARD-TASK-REMOVE-FAILED","Failed to remove task from board ("+ex.toString()+")");
            return -1;
        }
    }

    /**
     * Function for removing board from database
     * @param boardToRemove
     * @return Integer
     */
    public int removeBoard(AIM_Board boardToRemove){
        try{
            MongoCollection<Document> board_collection = database.get_data_collection("aim_board");
            DeleteResult result = board_collection.deleteOne(boardToRemove.prepareDocument());
            if ( result.getDeletedCount() > 0 ){
                database.log("DB-BOARD-REMOVE","Removed board ("+boardToRemove.board_id.toString()+")");
                return 1;
            }
            else{
                database.log("DB-BOARD-REMOVE","Cannot remove board, nothing to find!");
                return 0;
            }
        }catch(Exception ex){
            database.log("DB-BOARD-TASK-REMOVE-FAILED","Failed to remove board ("+ex.toString()+")");
            return -1;
        }
    }

    /**
     * Function for removing user boards
     * @param userToRemove
     * @return Integer
     */
    public int removeUserBoards(AIM_User userToRemove){
        MongoCollection<Document> board_collection = database.get_data_collection("aim_board");
        DeleteResult result = board_collection.deleteMany(new Document("board_owner",userToRemove));
        if ( result.getDeletedCount() > 0 ){
            database.log("DB-BOARD-USER-REMOVE","User ("+userToRemove.aim_user_id.toString()+") boards removed!");
            return 1;
        }
        else {
            database.log("DB-BOARD-USER-REMOVE", "Nothing to remove from user boards!");
            return 0;
        }
    }

}

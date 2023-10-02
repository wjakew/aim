/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.aim_dataengine.database_engine;

import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Board;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Project;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Task;

import java.util.ArrayList;

/**
 * Function for loading user telemetry from database
 */
public class Database_UserTelemetry {

    Database_Connector database;

    /**
     * Constructor
     * @param database
     */
    public Database_UserTelemetry(Database_Connector database){
        this.database = database;
    }

    /**
     * Function for loading user task statistic
     * @return Arraylist
     * {allTasks,openTasks,openTasksInBoard,openTasksInProjects}
     */
    public ArrayList<Integer> userTaskStatistic(){
        int allTasks,openTasks,openTasksInBoard,openTasksInProjects;
        Database_AIMTask dat = new Database_AIMTask(AimApplication.database);
        Database_AIMProject dap = new Database_AIMProject(AimApplication.database);
        Database_AIMBoard dab = new Database_AIMBoard(AimApplication.database);

        allTasks = dat.getTaskCollection().size();
        openTasks = 0;
        openTasksInBoard = 0;
        openTasksInProjects = 0;

        for(AIM_Task userTask : dat.getTaskCollection()){
            if ( !userTask.status.equals("DONE") ){
                openTasks++;
                openTasksInProjects++;
            }
        }
        for(AIM_Project userProject : dap.getUserProjects()){
            allTasks = allTasks + userProject.getTaskCollection().size();
            for(AIM_Task userTaskInProject : userProject.getTaskCollection()){
                if (!userTaskInProject.status.equals("DONE")){
                    openTasks++;
                }
            }
        }
        for(AIM_Board userBoard : dab.getUserBoardList()){
            allTasks = allTasks + userBoard.getTaskList().size();
            for(AIM_Task userTaskInBoard : userBoard.getTaskList()){
                if (!userTaskInBoard.status.equals("DONE")){
                    openTasks++;
                    openTasksInBoard++;
                }
            }
        }
        ArrayList<Integer> data = new ArrayList<>();
        data.add(allTasks);
        data.add(openTasks);
        data.add(openTasksInBoard);
        data.add(openTasksInProjects);
        return data;
    }

    /**
     * Function for loading user objects statistics
     * @return ArrayList
     * {projectCount, boardCount}
     */
    public ArrayList<Integer> userObjectStatistic(){
        int projectCount, boardCount;
        Database_AIMProject dap = new Database_AIMProject(AimApplication.database);
        Database_AIMBoard dab = new Database_AIMBoard(AimApplication.database);
        projectCount = dap.getUserProjects().size();
        boardCount = dab.getUserBoardList().size();
        ArrayList<Integer> data = new ArrayList<Integer>();
        data.add(projectCount);
        data.add(boardCount);
        return data;
    }
}

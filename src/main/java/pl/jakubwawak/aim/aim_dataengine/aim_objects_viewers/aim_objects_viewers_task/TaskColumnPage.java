/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers.aim_objects_viewers_task;

import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Task;

/**
 * Function for storing task column page
 */
public class TaskColumnPage {

    AIM_TaskLayout taskObject1;
    AIM_TaskLayout taskObject2;
    AIM_TaskLayout taskObject3;
    AIM_TaskLayout taskObject4;

    int taskColumnPageNumber;

    /**
     * Constructor
     * @param taskColumnPageNumber
     */
    public TaskColumnPage(int taskColumnPageNumber) {
        this.taskObject1 = null;
        this.taskObject2 = null;
        this.taskObject3 = null;
        this.taskObject4 = null;
        this.taskColumnPageNumber = taskColumnPageNumber;
    }

    /**
     * Function for adding task to page
     * @param taskToAdd
     * @return Integer
     */
    public int addTask(AIM_Task taskToAdd){
        if ( taskObject1 == null ){
            taskObject1 = new AIM_TaskLayout(taskToAdd);
            return 1;
        }
        else if ( taskObject2 == null ){
            taskObject2 = new AIM_TaskLayout(taskToAdd);
            return 2;
        }
        else if ( taskObject3 == null ){
            taskObject3 = new AIM_TaskLayout(taskToAdd);
            return 3;
        }
        else if ( taskObject4 == null ){
            taskObject4 = new AIM_TaskLayout(taskToAdd);
            return 4;
        }
        return 5;
    }

    /**
     * Function for checking if object is empty
     * @return boolean
     */
    public boolean isEmpty(){
        return taskObject1 == null && taskObject2 == null && taskObject3 == null && taskObject4 == null;
    }
}

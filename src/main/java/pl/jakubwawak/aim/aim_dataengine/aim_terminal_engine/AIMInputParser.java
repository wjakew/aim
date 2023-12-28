/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package pl.jakubwawak.aim.aim_dataengine.aim_terminal_engine;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import org.apache.commons.lang3.StringUtils;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Board;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_BoardTask;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Project;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Task;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMBoard;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMProject;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMTask;
import pl.jakubwawak.aim.website_ui.ProjectListGlanceWindow;
import pl.jakubwawak.aim.website_ui.dialog_windows.DashboardWindow;
import pl.jakubwawak.aim.website_ui.dialog_windows.FloatingWindow;
import pl.jakubwawak.aim.website_ui.dialog_windows.PictureViewerWindow;
import pl.jakubwawak.aim.website_ui.dialog_windows.UserWindow;
import pl.jakubwawak.aim.website_ui.dialog_windows.obiect_windows.board_windows.*;
import pl.jakubwawak.aim.website_ui.dialog_windows.obiect_windows.coding_task_windows.CTaskListGlanceWindow;
import pl.jakubwawak.aim.website_ui.dialog_windows.obiect_windows.coding_task_windows.InsertCTaskWindow;
import pl.jakubwawak.aim.website_ui.dialog_windows.obiect_windows.project_windows.DetailsProjectWindow;
import pl.jakubwawak.aim.website_ui.dialog_windows.obiect_windows.project_windows.InsertProjectWindow;
import pl.jakubwawak.aim.website_ui.dialog_windows.obiect_windows.project_windows.ProjectHistoryGlanceWindow;
import pl.jakubwawak.aim.website_ui.dialog_windows.obiect_windows.project_windows.ProjectTaskListGlanceWindow;
import pl.jakubwawak.aim.website_ui.dialog_windows.obiect_windows.task_windows.DetailsTaskWindow;
import pl.jakubwawak.aim.website_ui.dialog_windows.obiect_windows.task_windows.InsertTaskWindow;
import pl.jakubwawak.aim.website_ui.dialog_windows.obiect_windows.task_windows.TaskHistoryGlanceWindow;
import pl.jakubwawak.aim.website_ui.dialog_windows.obiect_windows.task_windows.TaskListGlanceWindow;
import pl.jakubwawak.aim.website_ui.views.TerminalView;

import java.util.ArrayList;
import java.util.stream.IntStream;

/**
 * Function for parsing user input
 * Based on document: aim_functionality.drawio
 */
public class AIMInputParser {

    public String userInput;

    public ArrayList<String> userInputHistory;

    VerticalLayout secondaryLayout;

    public int successParsingFlag;
    public int simpleViewFlagNeed;

    ArrayList<String> allCommandsCollection;

    // objects for current glance layout component
    public VerticalLayout currentGlanceLayout;
    Grid<AIM_Task> task_grid;
    Grid<AIM_Project> project_grid;
    Grid<AIM_Board> board_grid;

    /**
     * Constructor
     */
    public AIMInputParser(VerticalLayout terminalView){
        successParsingFlag = 0;
        simpleViewFlagNeed = 0;
        this.secondaryLayout = terminalView;
        userInputHistory = new ArrayList<>();
        allCommandsCollection = new ArrayList<>();
        createAllCommandsCollection();
        currentGlanceLayout = new VerticalLayout();
        currentGlanceLayout.setVisible(false);
        prepareLayout();
    }

    /**
     * Function for preparing current glance layout
     */
    void prepareLayout(){

        // task_tab
        Database_AIMTask dat = new Database_AIMTask(AimApplication.database);
        ArrayList<AIM_Task> taskCollection = dat.getTaskCollection();
        task_grid = new Grid<>(AIM_Task.class,false);
        task_grid.addColumn(AIM_Task::getAim_task_name).setHeader("Task Name");
        task_grid.addColumn(AIM_Task::getAim_task_owner_glance).setHeader("Task Owner");
        task_grid.addColumn(AIM_Task::getAim_task_timestamp).setHeader("Time Created");
        task_grid.addColumn(AIM_Task::getStatus).setHeader("Task Status");
        task_grid.setItems(taskCollection);
        task_grid.setWidth("100%");task_grid.setHeight("100%");

        // projects_tab
        Database_AIMProject dap = new Database_AIMProject(AimApplication.database);
        ArrayList<AIM_Project> projectCollection = dap.getUserProjects();
        project_grid = new Grid<>(AIM_Project.class,false);
        project_grid.addColumn(AIM_Project::getAim_project_name).setHeader("Project Name");
        project_grid.addColumn(AIM_Project::getAim_owner_glance).setHeader("Project Owner");
        project_grid.setItems(projectCollection);
        project_grid.setWidth("100%");project_grid.setHeight("100%");

        // board_tab
        Database_AIMBoard dab = new Database_AIMBoard(AimApplication.database);
        ArrayList<AIM_Board> boardCollection = dab.getUserBoardList();
        board_grid = new Grid<>(AIM_Board.class,false);
        board_grid.addColumn(AIM_Board::getBoard_name).setHeader("Board Name");
        board_grid.addColumn(AIM_Board::getBoard_owner_glance).setHeader("Board Owner");
        board_grid.addColumn(AIM_Board::getBoard_members_size).setHeader("Members Amount");
        board_grid.setItems(boardCollection);
        board_grid.setWidth("100%");board_grid.setHeight("100%");

        task_grid.addItemClickListener(e->{
            for(AIM_Task selected_task : task_grid.getSelectedItems()){
                DetailsTaskWindow dtw = new DetailsTaskWindow(selected_task);
                secondaryLayout.add(dtw.main_dialog);
                dtw.main_dialog.open();
                break;
            }
        });

        project_grid.addItemClickListener(e->{
            for(AIM_Project selected_project : project_grid.getSelectedItems()){
                DetailsProjectWindow dpw = new DetailsProjectWindow(selected_project);
                secondaryLayout.add(dpw.main_dialog);
                dpw.main_dialog.open();
                break;
            }
        });

        board_grid.addItemClickListener(e->{
            for(AIM_Board selected_board : board_grid.getSelectedItems()){
                DetailsBoardWindow dbw = new DetailsBoardWindow(selected_board);
                secondaryLayout.add(dbw.main_dialog);
                dbw.main_dialog.open();;
                break;
            }
        });

        TabSheet tabSheet_center = new TabSheet();
        tabSheet_center.add("Task",task_grid);
        tabSheet_center.add("Project",project_grid);
        tabSheet_center.add("Board",board_grid);
        tabSheet_center.setSizeFull();

        currentGlanceLayout.add(tabSheet_center);

        currentGlanceLayout.setSizeFull();
        currentGlanceLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        currentGlanceLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        currentGlanceLayout.getStyle().set("text-align", "center");

        currentGlanceLayout.getStyle().set("border-radius","25px");
        currentGlanceLayout.getStyle().set("background-color","grey");
        currentGlanceLayout.getStyle().set("--lumo-font-family","Monospace");
    }

    /**
     * Function for creating all commands collection for search
     */
    void createAllCommandsCollection(){
        allCommandsCollection.add("task -help");
        allCommandsCollection.add("task -list");
        allCommandsCollection.add("task -viewer");
        allCommandsCollection.add("task -create");
        allCommandsCollection.add("task -create -n task_name -d task_desc");
        allCommandsCollection.add("task -link -t task_name -s board_name/project_name");
        allCommandsCollection.add("task -status -t task_name -st new/in progress/done");
        allCommandsCollection.add("task -remove -t task_name");
        allCommandsCollection.add("task -history -t task_name");
        allCommandsCollection.add("task -details -t task_name");
        allCommandsCollection.add("project -create");
        allCommandsCollection.add("project -list");
        allCommandsCollection.add("project -help");
        allCommandsCollection.add("project -remove -n project_name");
        allCommandsCollection.add("project -create -n project_name -d project_desc");
        allCommandsCollection.add("project -history -n project_name");
        allCommandsCollection.add("project -listtask -n project_name");
        allCommandsCollection.add("project -rmtask -n project_name -t task_name");
        allCommandsCollection.add("project -addtask -n task_name");
        allCommandsCollection.add("board -create");
        allCommandsCollection.add("board -create -n board_name -d board_desc");
        allCommandsCollection.add("board -remove -n board_name");
        allCommandsCollection.add("board -list");
        allCommandsCollection.add("board -tasks -n board_name");
        allCommandsCollection.add("board -history -n board_name");
        allCommandsCollection.add("board -addtask -n board_name -t task_name");
        allCommandsCollection.add("board -rmtask -n board_name -t task_name");
        allCommandsCollection.add("aim");
        allCommandsCollection.add("aim -dashboard");
        allCommandsCollection.add("aim -focus");
        allCommandsCollection.add("aim -options");
    }

    /**
     * Function for loading input suggestion
     * @param userInput
     */
    public ArrayList<String> getCommandSuggestion(String userInput){
        if ( userInput.equals("") ){
            return allCommandsCollection;
        }
        else{
            ArrayList<String> data = new ArrayList<>();
            for(String command : allCommandsCollection){
                if ( command.contains(userInput) ){
                    data.add(command);
                }
            }
            return data;
        }
    }

    /**
     * Function for setting userInput
     * @param userInput
     */
    public void setUserInput(String userInput){
        this.userInput = userInput;
    }

    /**
     * Function for parsing user input
     */
    public void parse(){
        successParsingFlag = 0;
        String[] user_word_collection = userInput.split(" ");
        if ( user_word_collection.length > 0 ){
            switch(user_word_collection[0]){
                case "task":
                {
                    task_mind_creator(userInput);
                    break;
                }
                case "project":
                {
                    project_mind_creator(userInput);
                    break;
                }
                case "board":
                {
                    board_mind_creator(userInput);
                    break;
                }
                case "aim":
                {
                    aimp_commands_creator(userInput);
                    break;
                }
                case "ctask":
                {
                    ctask_commands_creator(userInput);
                    break;
                }
                case "help":
                {
                    PictureViewerWindow pvw = new PictureViewerWindow("images/usage_schema.png","AIM Terminal Usage Schema");
                    secondaryLayout.add(pvw.main_dialog);
                    pvw.main_dialog.open();
                    break;
                }
                default:
                {
                    createNotificationResponse("Nothing to show, wrong command. Press ? for help",3);
                    break;
                }
            }
        }
        else{
            response_nonKeyWord();
        }
    }

    /**
     * Function for coding task operation
     * @param userInput
     */
    public void ctask_commands_creator(String userInput){
        String[] user_word_collection = userInput.split(" ");
        if ( user_word_collection.length == 2){
            if ( userInput.contains("-create")){
                InsertCTaskWindow ictw = new InsertCTaskWindow(null);
                secondaryLayout.add(ictw.main_dialog);
                ictw.main_dialog.open();
            }
            else if ( userInput.contains("-list")){
                CTaskListGlanceWindow ctlgw = new CTaskListGlanceWindow();
                secondaryLayout.add(ctlgw.main_dialog);
                ctlgw.main_dialog.open();
            }
        }
    }

    /**
     * Function for aim operation
     * @param user_input
     */
    public void aimp_commands_creator(String user_input){
        String[] user_word_collection = user_input.split(" ");
        if( user_word_collection.length == 2 ){
            if (user_input.contains("-focus")) {
                if (currentGlanceLayout.isVisible()) {
                    currentGlanceLayout.setVisible(false);
                    Notification.show("AIM Glance hidden");
                    simpleViewFlagNeed = 0;

                } else {
                    currentGlanceLayout.setVisible(true);
                    Notification.show("AIM Glance set to visible!");
                    simpleViewFlagNeed = 1;
                }
                successParsingFlag = 1;
            }
            else if (user_input.contains("-options")){
                UserWindow uw = new UserWindow();
                secondaryLayout.add(uw.main_dialog);
                uw.main_dialog.open();
                successParsingFlag = 1;
            }
            else if (user_input.contains("-dashboard")){
                DashboardWindow dw = new DashboardWindow();
                secondaryLayout.add(dw.main_dialog);
                dw.main_dialog.open();
                successParsingFlag = 1;
            }
            else if ( user_input.contains("-pin")){
                FloatingWindow fw = new FloatingWindow();
                secondaryLayout.add(fw.main_dialog);
                fw.main_dialog.open();
                successParsingFlag = 1;
            }
        }
        else{
            createNotificationResponse("Nothing to show, wrong command. Press ? for help",3);
            successParsingFlag = 0;
        }
    }

    /**
     * Function for task operation
     * @param user_input
     */
    public void task_mind_creator(String user_input){
        Database_AIMTask dat = new Database_AIMTask(AimApplication.database);
        Database_AIMProject dap = new Database_AIMProject(AimApplication.database);
        Database_AIMBoard dab = new Database_AIMBoard(AimApplication.database);
        String[] user_word_collection = user_input.split(" ");
        // task -help
        if ( user_input.contains("-help")){
            PictureViewerWindow pvw = new PictureViewerWindow("images/task_schema.png","Task Terminal Command");
            secondaryLayout.add(pvw.main_dialog);
            pvw.main_dialog.open();
            successParsingFlag = 1;
        }
        // task -viewer
        else if ( user_input.contains("-viewer") ){
            if ( user_word_collection.length == 2 ){
                TaskListGlanceWindow tgw = new TaskListGlanceWindow();
                secondaryLayout.add(tgw.main_dialog);
                tgw.main_dialog.open();
                successParsingFlag = 1;
            }
            else{
                createNotificationResponse("Wrong input for the task, check help!",3);
                successParsingFlag = 0;
            }
        }
        // task -create
        if ( user_input.contains("-create")){
            // task -create
            if( user_word_collection.length == 2 ){
                InsertTaskWindow itw = new InsertTaskWindow(null);
                secondaryLayout.add(itw.main_dialog);
                itw.main_dialog.open();
                successParsingFlag = 1;
            }
            // task -create -n name -d description
            else if (user_word_collection.length > 6){
                AIM_Task task = new AIM_Task();
                task.aim_task_name = StringUtils.substringBetween(user_input,"-n","-d").strip().stripLeading().stripTrailing();
                String []  subarray = IntStream.range(5, user_word_collection.length)
                        .mapToObj(i -> user_word_collection[i])
                        .toArray(String[]::new);
                task.aim_task_desc = String.join(" ",subarray);
                int ans = dat.insertAIMTask(task);
                if (ans == 1){
                    createNotificationResponse("Task "+ task.aim_task_name+ " created!",2);
                    if ( AimApplication.session_ctc!= null ){
                        AimApplication.session_ctc.updateLayout();
                    }
                    successParsingFlag = 1;
                }
                else{
                    createNotificationResponse("Failed to create task, check log",1);
                    successParsingFlag = 0;
                }
            }
            else{
                createNotificationResponse("Wrong input for the task, check help!",3);
                successParsingFlag = 0;
            }
        }
        // task -link -t task_name -s board/project
        else if (user_input.contains("-link")){
            if ( user_word_collection.length == 6 ){
                String sourceObject = user_word_collection[5];
                String taskName = StringUtils.substringBetween(user_input,"-t","-s").strip().stripLeading().stripTrailing();
                AIM_Board board = dab.getBoardByName(sourceObject);
                AIM_Project project = dap.getProjectByName(sourceObject);
                AIM_Task task = dat.getTask(taskName);
                if (task!=null){
                    if ( board != null ){
                        // board is selected
                        int ans = dab.linkTaskToBoard(board,task);
                        if ( ans == 1 ){
                            createNotificationResponse("Task linked to board ("+board.board_id.toString()+")",2);
                            successParsingFlag = 1;
                        }
                        else{
                            createNotificationResponse("Cannot linked, check log!",1);
                            successParsingFlag = 0;
                        }
                    }
                    else if (project != null){
                        // project is selected
                        int ans = dap.linkTaskProject(project,task);
                        if ( ans == 1 ){
                            createNotificationResponse("Task linked to project ("+project.aim_project_id.toString()+")",2);
                            successParsingFlag = 1;
                        }
                        else{
                            createNotificationResponse("Cannot linked, check log!",1);
                            successParsingFlag = 0;
                        }
                    }
                    else{
                        // nothing is selected
                        createNotificationResponse("Cannot find task named "+taskName,3);
                        successParsingFlag = 0;
                    }
                }
                else{
                    createNotificationResponse("No task named ("+taskName+")",3);
                    successParsingFlag = 0;
                }
            }
            else{
                createNotificationResponse("Wrong input for the task, check help!",3);
                successParsingFlag = 0;
            }
        }
        //task -status -t task_name -st new/in progress/done
        else if (user_input.contains("-status")){
            if ( user_word_collection.length >= 5 ){
                String taskName = StringUtils.substringBetween(user_input,"-t","-st");
                String []  subarray = IntStream.range(4, user_word_collection.length)
                        .mapToObj(i -> user_word_collection[i])
                        .toArray(String[]::new);
                String taskStatus = String.join(" ",subarray);
                ArrayList<String> statusCollection = new ArrayList<>();
                statusCollection.add("NEW");
                statusCollection.add("IN PROGRESS");
                statusCollection.add("DONE");
                if (statusCollection.contains(taskStatus)){
                    AIM_Task task = dat.getTask(taskName);
                    if (task!=null){
                        // task found
                        int ans = dat.changeOwnerAIMTask(task,taskStatus);
                        if ( ans == 1 ){
                            createNotificationResponse("Task ("+task.aim_task_id.toString()+") status updated to"+taskStatus,1);
                            successParsingFlag = 1;
                        }
                        else{
                            createNotificationResponse("No task found with name "+taskName,3);
                            successParsingFlag = 0;
                        }
                    }
                }
                else{
                    createNotificationResponse("Status "+taskStatus+" is not accepted!",3);
                    successParsingFlag = 0;
                }
            }
            else{
                createNotificationResponse("Wrong command usage, check -help",3);
                successParsingFlag = 0;
            }
        }
        // task -remove -t task_name
        else if (user_input.contains("-remove")){
            String []  subarray = IntStream.range(3, user_word_collection.length)
                    .mapToObj(i -> user_word_collection[i])
                    .toArray(String[]::new);
            String taskName = String.join(" ",subarray);
            if ( user_word_collection.length >= 4 ){
                AIM_Task task = dat.getTask(taskName);
                if ( task != null ){
                    String ans = dat.remove(task);
                    if (ans!=null){
                        createNotificationResponse("Task removed, sources: "+ans,1);
                        successParsingFlag = 1;
                    }
                    else{
                        createNotificationResponse("Error removing task, check help",2);
                        successParsingFlag = 0;
                    }
                }
                else{
                    createNotificationResponse("Cannot find task ("+taskName+")",3);
                    successParsingFlag = 0;
                }
            }
            else{
                createNotificationResponse("Wrong input for the task, check help!",3);
                successParsingFlag = 0;
            }
        }
        //task -list
        else if (user_input.contains("-list")){
            if ( user_word_collection.length == 2 ){
                TaskListGlanceWindow tgw = new TaskListGlanceWindow();
                secondaryLayout.add(tgw.main_dialog);
                tgw.main_dialog.open();
                successParsingFlag = 1;
            }
            else{
                createNotificationResponse("Wrong input for the task, check help!",3);
                successParsingFlag = 0;
            }
        }
        // task -history -t task_name
        else if (user_input.contains("-history")){
            if ( user_word_collection.length >= 3 ){
                String []  subarray = IntStream.range(3, user_word_collection.length)
                        .mapToObj(i -> user_word_collection[i])
                        .toArray(String[]::new);
                String taskName = String.join(" ",subarray);
                AIM_Task task = dat.getTask(taskName);
                if ( task != null ){
                    TaskHistoryGlanceWindow thgw = new TaskHistoryGlanceWindow(task);
                    secondaryLayout.add(thgw.main_dialog);
                    thgw.main_dialog.open();
                    successParsingFlag = 1;
                }
                else{
                    createNotificationResponse("Cannot find task ("+taskName+")",3);
                    successParsingFlag = 0;
                }
            }
        }
        // task -details -t task_name
        else if ( user_input.contains("-details")){
            if ( user_word_collection.length >= 3 ){
                String []  subarray = IntStream.range(3, user_word_collection.length)
                        .mapToObj(i -> user_word_collection[i])
                        .toArray(String[]::new);
                String taskName = String.join(" ",subarray);
                AIM_Task task = dat.getTask(taskName);
                if ( task != null ){
                    DetailsTaskWindow dtw = new DetailsTaskWindow(task);
                    secondaryLayout.add(dtw.main_dialog);
                    dtw.main_dialog.open();
                    successParsingFlag = 1;
                }
                else{
                    createNotificationResponse("Cannot find task ("+taskName+")",3);
                    successParsingFlag = 0;
                }
            }
        }
        else{
            createNotificationResponse("Wrong command usage, check help",3);
            successParsingFlag = 0;
        }
    }

    /**
     * Function for project operation
     * @param user_input
     */
    public void project_mind_creator(String user_input){
        Database_AIMTask dat = new Database_AIMTask(AimApplication.database);
        Database_AIMProject dap = new Database_AIMProject(AimApplication.database);
        Database_AIMBoard dab = new Database_AIMBoard(AimApplication.database);
        String[] user_word_collection = user_input.split(" ");
        //project -create
        if (user_input.contains("-create")){
            if (user_word_collection.length == 2){
                // open creation window
                InsertProjectWindow ipw = new InsertProjectWindow(null);
                secondaryLayout.add(ipw.main_dialog);
                ipw.main_dialog.open();
                successParsingFlag = 1;
            }
            //project -create -n project_name
            else if (user_word_collection.length > 3){
                String []  subarray = IntStream.range(3, user_word_collection.length)
                        .mapToObj(i -> user_word_collection[i])
                        .toArray(String[]::new);
                String projectName = String.join(" ",subarray);
                AIM_Project project = new AIM_Project();
                project.aim_project_name = projectName;
                project.aim_project_desc = "Created with terminal";
                int ans = dap.insertProject(project);
                if (ans == 1){
                    createNotificationResponse("Project ("+projectName+") created!",1);
                    successParsingFlag = 1;
                }
                else{
                    createNotificationResponse("Cannot create project, check application log!",2);
                    successParsingFlag = 0;
                }
            }
            else{
                createNotificationResponse("Wrong command usage, check -help",2);
                successParsingFlag = 0;
            }
        }
        else if ( user_input.contains("-help")){
            if ( user_word_collection.length == 2){
                PictureViewerWindow pvw = new PictureViewerWindow("images/project_schema.png","Projects Terminal Command");
                secondaryLayout.add(pvw.main_dialog);
                pvw.main_dialog.open();
                successParsingFlag = 1;
            }
            else{
                createNotificationResponse("Wrong command usage, check -help",2);
                successParsingFlag = 0;
            }
        }
        // project -remove -n project_name
        else if (user_input.contains("-remove")){
            if ( user_word_collection.length > 3){
                String []  subarray = IntStream.range(3, user_word_collection.length)
                        .mapToObj(i -> user_word_collection[i])
                        .toArray(String[]::new);
                String projectName = String.join(" ",subarray);
                AIM_Project project = dap.getProjectByName(projectName);
                if ( project != null ){
                    int ans = dap.removeProject(project);
                    if ( ans == 1 ){
                        createNotificationResponse("Project ("+projectName+") removed!",1);
                        successParsingFlag = 1;
                    }
                    else{
                        createNotificationResponse("Cannot remove project, check application log!",2);
                        successParsingFlag = 0;
                    }
                }
                else{
                    createNotificationResponse("Cannot find project named ("+projectName+")",3);
                    successParsingFlag = 0;
                }
            }
            else{
                createNotificationResponse("Wrong command usage, check -help",2);
                successParsingFlag = 0;
            }
        }
        // project -history -n project_name
        else if (user_input.contains("-history")){
            if ( user_word_collection.length >3 ){
                String []  subarray = IntStream.range(3, user_word_collection.length)
                        .mapToObj(i -> user_word_collection[i])
                        .toArray(String[]::new);
                String projectName = String.join(" ",subarray);
                AIM_Project project = dap.getProjectByName(projectName);
                if ( project != null ){
                    ProjectHistoryGlanceWindow phgw = new ProjectHistoryGlanceWindow(project);
                    secondaryLayout.add(phgw.main_dialog);
                    phgw.main_dialog.open();
                    successParsingFlag = 1;
                }
                else{
                    createNotificationResponse("Cannot find project named ("+projectName+")",3);
                    successParsingFlag = 0;
                }
            }
            else{
                createNotificationResponse("Wrong command usage, check -help",2);
                successParsingFlag = 0;
            }
        }
        // project -listtask -n project_name
        else if (user_input.contains("-listtask")){
            if ( user_word_collection.length > 3){
                String []  subarray = IntStream.range(3, user_word_collection.length)
                        .mapToObj(i -> user_word_collection[i])
                        .toArray(String[]::new);
                String projectName = String.join(" ",subarray);
                AIM_Project project = dap.getProjectByName(projectName);
                if ( project != null ){
                    ProjectTaskListGlanceWindow ptlgw = new ProjectTaskListGlanceWindow(project);
                    secondaryLayout.add(ptlgw.main_dialog);
                    ptlgw.main_dialog.open();
                    successParsingFlag = 1;
                }
                else{
                    createNotificationResponse("Cannot find project named ("+projectName+")",3);
                    successParsingFlag = 0;
                }
            }
            else{
                createNotificationResponse("Wrong command usage, check -help",2);
                successParsingFlag = 0;
            }
        }
        // project -list
        else if (user_input.contains("-list")){
            if (user_word_collection.length ==2){
                ProjectListGlanceWindow plgw = new ProjectListGlanceWindow();
                secondaryLayout.add(plgw.main_dialog);
                plgw.main_dialog.open();
                successParsingFlag = 1;
            }
            else{
                createNotificationResponse("Wrong command usage, check -help",2);
                successParsingFlag = 0;
            }
        }
        // project -rmtask -n project_name -t task_name / project -addtask -n task_name
        else if (user_input.contains("-rmtask") || user_input.contains("-addtask")){
            if ( user_word_collection.length >= 6 ){
                String projectName = StringUtils.substringBetween(user_input,"-n","-t").stripLeading().stripTrailing().strip();
                String []  subarray = IntStream.range(5, user_word_collection.length)
                        .mapToObj(i -> user_word_collection[i])
                        .toArray(String[]::new);
                String taskName = String.join(" ",subarray);
                AIM_Project project = dap.getProjectByName(projectName);
                if ( project != null ){
                    AIM_Task task = dat.getTask(taskName);
                    if ( task != null ){
                        // found task and project on database
                        int ans = -3;
                        if ( user_input.contains("-rmtask")){
                            ans = dap.removeTaskFromProject(project,task);
                        }
                        else if ( user_input.contains("-addtask")){
                            ans = dap.insertTaskToProject(project,task);
                        }

                        if (ans == 1){
                            createNotificationResponse("Added task to project ("+project.aim_project_id.toString()+")",1);
                            successParsingFlag = 1;
                        }
                        else{
                            createNotificationResponse("Cannot add task to project, code: ("+ans+")",2);
                            successParsingFlag = 0;
                        }
                    }
                    else{
                        createNotificationResponse("Cannot find task named ("+taskName+")",3);
                        successParsingFlag = 0;
                    }
                }
                else{
                    createNotificationResponse("Cannot find project named ("+projectName+")",3);
                    successParsingFlag = 0;
                }
            }
            else{
                createNotificationResponse("Wrong command usage, check -help",2);
                successParsingFlag = 0;
            }
        }
    }

    /**
     * Function for board operation
     * @param user_input
     */
    public void board_mind_creator(String user_input){
        Database_AIMTask dat = new Database_AIMTask(AimApplication.database);
        Database_AIMProject dap = new Database_AIMProject(AimApplication.database);
        Database_AIMBoard dab = new Database_AIMBoard(AimApplication.database);
        String[] user_word_collection = user_input.split(" ");
        //board -create / board -create -n name -d desc
        if ( user_input.contains("-create")){
            if ( user_word_collection.length == 2 ){
                InsertBoardWindow ibw = new InsertBoardWindow(null);
                secondaryLayout.add(ibw.main_dialog);
                ibw.main_dialog.open();
            }
            else if ( user_word_collection.length >= 6 ){
                String boardName = StringUtils.substringBetween(user_input,"-n","-d").strip().stripTrailing().stripLeading();
                String []  subarray = IntStream.range(5, user_word_collection.length)
                        .mapToObj(i -> user_word_collection[i])
                        .toArray(String[]::new);
                String boardDesc = String.join(" ",subarray);
                AIM_Board board = new AIM_Board();
                board.board_name = boardName;
                board.board_desc = boardDesc;
                int ans = dab.insertBoard(board);
                if ( ans == 1 ){
                    createNotificationResponse("Board ("+boardName+") created!",1);
                    successParsingFlag = 1;
                }
                else{
                    createNotificationResponse("Error adding board, check log!",2);
                    successParsingFlag = 0;
                }
            }
            else{
                createNotificationResponse("Wrong command usage, check -help",3);
                successParsingFlag = 0;
            }
        }
        //board -remove -n name
        else if (user_input.contains("-remove")){
            if ( user_word_collection.length >= 4 ){
                String []  subarray = IntStream.range(5, user_word_collection.length)
                        .mapToObj(i -> user_word_collection[i])
                        .toArray(String[]::new);
                String boardName = String.join(" ",subarray);
                AIM_Board board = dab.getBoard(boardName);
                if (board !=null){
                    int ans = dab.removeBoard(board);
                    if ( ans == 1 ){
                        createNotificationResponse("Board ("+board.board_id.toString()+") removed.",1);
                        successParsingFlag = 1;
                    }
                    else{
                        createNotificationResponse("Failed to remove, check log!",2);
                        successParsingFlag = 0;
                    }
                }
                else{
                    createNotificationResponse("Cannot find board ("+boardName+")",3);
                    successParsingFlag = 0;
                }
            }
        }
        //board -list
        else if (user_input.contains("-list")){
            if ( user_word_collection.length == 2 ){
                BoardListGlanceWindow blgw = new BoardListGlanceWindow();
                secondaryLayout.add(blgw.main_dialog);
                blgw.main_dialog.open();
                successParsingFlag = 1;
            }
            else{
                createNotificationResponse("Wrong command usage, check -help",3);
                successParsingFlag = 0;
            }
        }
        else if (user_input.contains("-help")){
            if ( user_word_collection.length == 2 ){
                PictureViewerWindow pvw = new PictureViewerWindow("images/board_schema.png","Board Command Schema");
                secondaryLayout.add(pvw.main_dialog);
                pvw.main_dialog.open();
            }
        }
        // board -tasks -n board_name
        else if (user_input.contains("-tasks")){
            if ( user_word_collection.length >= 4 ){
                String []  subarray = IntStream.range(3, user_word_collection.length)
                        .mapToObj(i -> user_word_collection[i])
                        .toArray(String[]::new);
                String boardName = String.join(" ",subarray);
                AIM_Board board = dab.getBoard(boardName);
                if ( board != null ){
                    BoardTaskListGlanceWindow btlgw = new BoardTaskListGlanceWindow(board);
                    secondaryLayout.add(btlgw.main_dialog);
                    btlgw.main_dialog.open();
                    successParsingFlag = 1;
                }
                else{
                    createNotificationResponse("Cannot find board ("+boardName+")",3);
                    successParsingFlag = 0;
                }
            }
            else{
                createNotificationResponse("Wrong command usage, check -help, code ("+user_word_collection.length+")",3);
                successParsingFlag = 0;
            }
        }
        // board -history -n board_name
        else if (user_input.contains("-history")){
            if ( user_word_collection.length >= 4 ){
                String []  subarray = IntStream.range(3, user_word_collection.length)
                        .mapToObj(i -> user_word_collection[i])
                        .toArray(String[]::new);
                String boardName = String.join(" ",subarray);
                AIM_Board board = dab.getBoard(boardName);
                if ( board != null ){
                    //open board history window
                    BoardHistoryGlanceWindow bhgw = new BoardHistoryGlanceWindow(board);
                    secondaryLayout.add(bhgw.main_dialog);
                    bhgw.main_dialog.open();
                    successParsingFlag = 1;
                }
                else{
                    createNotificationResponse("Cannot find board ("+boardName+")",3);
                    successParsingFlag = 0;
                }
            }
            else{
                createNotificationResponse("Wrong command usage, check -help, code ("+user_word_collection.length+")",3);
                successParsingFlag = 0;
            }
        }
        // board -addtask -n board_name -t task_name / board -rmtask -n board_name -t task_name
        else if ( user_input.contains("-addtask") || user_input.contains("-rmtask") ){
            String boardName = StringUtils.substringBetween(user_input,"-n","-t").stripLeading().stripTrailing().strip();
            String []  subarray = IntStream.range(4, user_word_collection.length)
                    .mapToObj(i -> user_word_collection[i])
                    .toArray(String[]::new);
            String taskName = String.join(" ",subarray);
            AIM_Board board = dab.getBoard(boardName);
            if ( board != null ){
                AIM_Task task = dat.getTask(taskName);
                if ( task != null ){
                    // task and board found
                    AIM_BoardTask abt = new AIM_BoardTask(task);
                    if ( user_input.contains("-addtask")) {
                        int ans = dab.insertTaskToBoard(board, abt);
                        if (ans == 1) {
                            createNotificationResponse("Task added to board (" + board.board_id + ")", 1);
                            successParsingFlag = 1;
                        } else {
                            createNotificationResponse("Failed to add task, check application log", 2);
                            successParsingFlag = 0;
                        }
                    }
                    else if ( user_input.contains("-rmtask")){
                        int ans = dab.removeTaskFromBoard(board,abt);
                        if (ans == 1) {
                            createNotificationResponse("Task removed from board (" + board.board_id + ")", 1);
                            successParsingFlag = 1;
                        } else {
                            createNotificationResponse("Failed to remove task, check application log", 2);
                            successParsingFlag = 0;
                        }
                    }
                }
                else{
                    createNotificationResponse("Cannot find task with name ("+taskName+")",3);
                    successParsingFlag = 0;
                }
            }
            else{
                createNotificationResponse("Cannot find board with name ("+boardName+")",3);
                successParsingFlag = 0;
            }
        }
        
    }

    /**
     * Function for creating notification response
     * notificationState
     * 1 - error
     * 2 - success
     * 3 - contrast ( information )
     * @param notificationMessage
     * @param notificationState
     */
    void createNotificationResponse(String notificationMessage, int notificationState){

        Notification notification = Notification.show(notificationMessage,5000, Notification.Position.MIDDLE);
        if (notificationState == 1){
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
        else if (notificationState == 2){
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        }
        else if (notificationState == 3){
            notification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
        }
    }

    /**
     * Response function for
     * @return String
     */
    public String response_nonKeyWord(){
        return "nothing to procedure - no key words!";
    }
}

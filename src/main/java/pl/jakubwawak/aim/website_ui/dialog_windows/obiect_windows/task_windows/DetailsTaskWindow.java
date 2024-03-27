/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.website_ui.dialog_windows.obiect_windows.task_windows;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import org.bson.Document;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Board;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_BoardTask;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Project;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Task;
import pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers.aim_objects_viewers_board.AIM_BoardTaskListLayout;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMBoard;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMProject;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMTask;
import pl.jakubwawak.aim.website_ui.dialog_windows.AddElementWindow;
import pl.jakubwawak.aim.website_ui.dialog_windows.MessageComponent;
import pl.jakubwawak.aim.website_ui.dialog_windows.UserWindow;
import pl.jakubwawak.aim.website_ui.dialog_windows.obiect_windows.board_windows.AddTaskBoardWindow;
import pl.jakubwawak.maintanance.GridElement;

import java.util.ArrayList;
import java.util.Set;

/**
 * Window for task details
 */
public class DetailsTaskWindow {

    // variables for setting x and y of window
    public String width = "70%";
    public String height = "80%";
    public String backgroundStyle = "";

    // main login components
    public Dialog main_dialog;
    VerticalLayout main_layout;

    public AIM_Task taskObject;

    H1 taskname_header;
    TextArea taskdesc_area;

    Grid<GridElement> history_grid;

    ComboBox<GridElement> status_combobox;

    MenuBar taskMenuBar;
    MenuItem subItems5; // share

    AIM_Project projectWithTask;
    AIM_Board boardWithTask;
    Document assignedUser;
    AIM_BoardTask boardTask;

    int shareClickCount;

    ComboBox<GridElement> assignedmember_combobox;
    ArrayList<GridElement> data;

    /**
     * Constructor
     */
    public DetailsTaskWindow(AIM_Task taskObject){
        this.taskObject = taskObject;
        main_dialog = new Dialog();
        main_dialog.addClassName("aim-window-normal");
        main_dialog.setDraggable(true);
        main_layout = new VerticalLayout();
        assignedUser = null;
        projectWithTask = null;
        boardWithTask = null;
        shareClickCount = 0;
        prepare_dialog();
    }

    /**
     * Constructor with project
     * @param taskObject
     * @param projectWithTask
     */
    public DetailsTaskWindow(AIM_Task taskObject,AIM_Project projectWithTask){
        this.taskObject = taskObject;
        this.projectWithTask = projectWithTask;
        boardWithTask = null;
        assignedUser = null;
        main_dialog = new Dialog();
        main_layout = new VerticalLayout();
        main_dialog.addClassName("aim-window-normal");
        shareClickCount = 0;
        prepare_dialog();
    }

    /**
     * Constuctor with board
     * @param taskObject
     * @param boardWithTask
     */
    public DetailsTaskWindow(AIM_BoardTask taskObject, AIM_Board boardWithTask){
        this.taskObject = taskObject;
        this.boardTask = taskObject;
        assignedUser = taskObject.aim_user_assigned;
        this.projectWithTask = null;
        this.boardWithTask = boardWithTask;
        main_dialog = new Dialog();
        main_layout = new VerticalLayout();
        main_dialog.addClassName("aim-window-normal");
        shareClickCount = 0;
        prepare_dialog();
    }

    /**
     * Function for preparing components
     */
    void prepare_components(){
        // set components
        taskname_header = new H1(taskObject.aim_task_name);
        taskname_header.getStyle().set("color","white");

        taskdesc_area = new TextArea("Task description");
        taskdesc_area.setValue(taskObject.aim_task_desc);
        taskdesc_area.setWidth("100%");
        taskdesc_area.addClassName("aim-inputfield-bright");

        history_grid = new Grid<>(GridElement.class,false);
        history_grid.addClassName("aim-grid");
        data = new ArrayList<>();

        for(String element : taskObject.aim_task_history){
            data.add(new GridElement(element));
        }

        history_grid.addColumn(GridElement::getGridelement_text).setHeader("Task History");
        history_grid.setItems(data);
        history_grid.setSizeFull();

        status_combobox = new ComboBox<>("Task Status");

        ArrayList<GridElement> statusdata = new ArrayList<>();
        statusdata.add(new GridElement("NEW"));
        statusdata.add(new GridElement("IN PROGRESS"));
        statusdata.add(new GridElement("DONE"));

        status_combobox.setItems(statusdata);
        status_combobox.setItemLabelGenerator(GridElement::getGridelement_text);
        status_combobox.setValue(new GridElement(taskObject.status));
        status_combobox.setWidth("100%");
        status_combobox.setAllowCustomValue(false);
        status_combobox.addClassName("aim-inputfield-bright");

        taskMenuBar = new MenuBar();
        taskMenuBar.addClassName("aim-menu");

        MenuItem taskItem = taskMenuBar.addItem("Actions");
        taskItem.addClassName("aim-button-black");
        SubMenu subItems = taskItem.getSubMenu();

        MenuItem subItems1 = subItems.addItem(new HorizontalLayout(VaadinIcon.REFRESH.create(),new H6("Update Task")));
        subItems1.setCheckable(false);
        subItems1.setChecked(false);

        MenuItem subItems2 = subItems.addItem(new HorizontalLayout(VaadinIcon.GROUP.create(),new H6("Change owner")));
        subItems2.setCheckable(false);
        subItems2.setChecked(false);

        MenuItem subItems3 = subItems.addItem(new HorizontalLayout(VaadinIcon.COMMENT.create(),new H6("Add Comment")));
        subItems3.setCheckable(false);
        subItems3.setChecked(false);

        MenuItem subItems4;
        subItems4 = subItems.addItem(new HorizontalLayout(VaadinIcon.TRAIN.create(),new H6("Delete")));
        subItems4.setCheckable(false);
        subItems4.setChecked(false);

        if ( AimApplication.loggedUser != null){
            if ( AimApplication.loggedUser.aim_user_id.equals(taskObject.aim_task_owner.getObjectId("aim_user_id"))){
                subItems4.setEnabled(true);
            }
            else{
                subItems4.setEnabled(false);
            }
        }
        else{
            subItems4.setEnabled(false);
            status_combobox.setEnabled(false);
        }


        // setting share button text
        Database_AIMTask datask = new Database_AIMTask(AimApplication.database);
        String share = datask.checkShare(taskObject);
        if ( share!= null ){
            subItems5 = subItems.addItem(new HorizontalLayout(VaadinIcon.SHARE.create(),new H6(share)));
            subItems5.setCheckable(false);
            subItems5.setChecked(false);
        }
        else{
            subItems5 = subItems.addItem(new HorizontalLayout(VaadinIcon.SHARE.create(),new H6("Share Task")));
            subItems5.setCheckable(false);
            subItems5.setChecked(false);
        }


        ComponentEventListener<ClickEvent<MenuItem>> listener = event -> {
            MenuItem selectedItem = event.getSource();
            if (selectedItem.equals(subItems1)) {
                updatebutton_action();
            } else if (selectedItem.equals(subItems2)) {
                changeowner_button();
            } else if (selectedItem.equals(subItems3)) {
                setAddcomment_button();
            } else if (selectedItem.equals(subItems4)) {
                deletebutton_action();
            } else if (selectedItem.equals(subItems5)) {
                sharetaskbutton_action();
            }
        };
        subItems1.addClickListener(listener);
        subItems2.addClickListener(listener);
        subItems3.addClickListener(listener);
        subItems4.addClickListener(listener);
        subItems5.addClickListener(listener);

        assignedmember_combobox = new ComboBox<>("Assigned User");
        assignedmember_combobox.addClassName("aim-inputfield-bright");

        if ( boardWithTask != null ){
            ArrayList<GridElement> membersData = new ArrayList<>();

            membersData.add(new GridElement("All"));

            for(Document member: boardWithTask.board_members){
                membersData.add(new GridElement(member.getString("aim_user_email")));
            }

            membersData.add(new GridElement(AimApplication.loggedUser.aim_user_email));
            assignedmember_combobox.setItems(membersData);
            assignedmember_combobox.setItemLabelGenerator(GridElement::getGridelement_text);

            if ( assignedUser!= null ){
                assignedmember_combobox.setValue(new GridElement(assignedUser.getString("aim_user_email")));
            }
            else{
                assignedmember_combobox.setValue(new GridElement("All"));
            }
        }

        // action change listeren on status combobox
        status_combobox.addValueChangeListener(e->{
            if ( projectWithTask == null && boardWithTask == null){
                // task alone - not linked to project
                String newStatus = status_combobox.getValue().getGridelement_text();
                Database_AIMTask dat = new Database_AIMTask(AimApplication.database);
                int ans = dat.updateAIMTaskStatus(taskObject,newStatus);
                if (ans > 0){
                    Notification.show("Task status updated");
                    if ( AimApplication.session_ctc!= null ){
                        AimApplication.session_ctc.updateLayout();
                    }
                }
            }
            else if ( projectWithTask != null && boardWithTask == null){
                // task linked to project
                String newStatus = status_combobox.getValue().getGridelement_text();
                Database_AIMProject dap = new Database_AIMProject(AimApplication.database);
                int ans = dap.updateTaskStatus(projectWithTask,taskObject,newStatus);
                if (ans > 0){
                    Notification.show("Project's task status updated");
                    if ( AimApplication.session_cpc!= null ){
                        AimApplication.session_cpc.updateLayout();
                    }
                }
            }
            else if (projectWithTask == null && boardWithTask != null){
                // task linked to board
                String newStatus = status_combobox.getValue().getGridelement_text();
                Database_AIMBoard dab = new Database_AIMBoard(AimApplication.database);
                int ans = dab.changeTaskStatusOnBoard(boardWithTask,boardTask,newStatus);
                if (ans > 0){
                    Notification.show("Board's task status updated!");
                    AimApplication.currentBoardTaskList.reloadView();
                }
            }
        });

        // action change listener on assigned combobox
        assignedmember_combobox.addValueChangeListener(e ->{
            String email = assignedmember_combobox.getValue().getGridelement_text();
            Database_AIMBoard dab = new Database_AIMBoard(AimApplication.database);
            int ans = dab.changeAssinedUserToTask(boardWithTask,boardTask,email);
            if ( ans > 0 ){
                Notification.show("Board's task assigned changed!");
                AimApplication.currentBoardTaskList.reloadView();
                main_dialog.close();
            }
        });

        history_grid.addSelectionListener(e->{
            Set<GridElement> selected = history_grid.getSelectedItems();
            for(GridElement element : selected){
                MessageComponent mc = new MessageComponent(element.getGridelement_text());
                main_layout.add(mc.main_dialog);
                mc.main_dialog.open();
                break;
            }
        });
    }

    /**
     * Function for reloading history grid
     */
    public void historyReload(){
        data.clear();
        for(String element : taskObject.aim_task_history){
            data.add(new GridElement(element));
        }
        history_grid.getDataProvider().refreshAll();
    }

    /**
     * Function for preparing layout
     */
    void prepare_dialog(){
        main_layout.removeAll();
        prepare_components();
        // set layout
        main_layout.add(taskname_header);

        if ( projectWithTask != null ){
            main_layout.add(new H6("no id (belongs to "+projectWithTask.aim_project_name+")"));
        }
        else if ( boardWithTask != null ){
            main_layout.add(new H6("no id (belongs to "+boardWithTask.board_name+")"));
        }
        else{
            if ( taskObject.aim_task_id != null ){
                main_layout.add(new H6(taskObject.aim_task_id.toString()));
            }
            else{
                main_layout.add(new H6("no id (null)"));
            }
        }
        main_layout.add(taskdesc_area);

        HorizontalLayout hl_down_layout = new HorizontalLayout();

        VerticalLayout vl_left, vl_right;

        vl_left = new VerticalLayout();
        vl_right = new VerticalLayout();

        vl_left.setSizeFull();
        vl_left.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        vl_left.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        vl_left.getStyle().set("text-align", "center");

        vl_right.setSizeFull();
        vl_right.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        vl_right.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        vl_right.getStyle().set("text-align", "center");

        vl_left.add(history_grid);

        vl_right.add(new H6("Created: "+taskObject.aim_task_timestamp),new H6("Deadline: "+taskObject.aim_task_deadline),status_combobox);

        if ( boardWithTask != null ){
            vl_right.add(assignedmember_combobox);
        }

        hl_down_layout.add(vl_left,vl_right);
        hl_down_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        hl_down_layout.setSizeFull();

        main_layout.add(hl_down_layout);
        main_layout.add(taskMenuBar);

        main_layout.setSizeFull();
        main_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        main_layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        main_layout.getStyle().set("text-align", "center");

        main_layout.getStyle().set("border-radius","25px");
        main_layout.getStyle().set("background-color",backgroundStyle);
        main_layout.getStyle().set("--lumo-font-family","Monospace");
        main_dialog.add(main_layout);
        main_dialog.setWidth(width);main_dialog.setHeight(height);
        main_dialog.setResizable(true);

        // cannot change owner or update if task is in project
        if ( taskObject.aim_task_id == null ){
            taskMenuBar.setEnabled(false);
        }

        if ( boardWithTask == null ){
            assignedmember_combobox.setEnabled(false);
        }

    }

    /**
     * changeowner_button action
     */
    private void changeowner_button(){
        ChangeTaskOwnerWindow ctow = new ChangeTaskOwnerWindow(taskObject);
        main_layout.add(ctow.main_dialog);
        ctow.main_dialog.open();
        main_dialog.close();
    }

    /**
     * update_button action
     */
    private void updatebutton_action(){
        if ( boardWithTask == null ){
            InsertTaskWindow itw = new InsertTaskWindow(taskObject);
            main_layout.add(itw.main_dialog);
            itw.main_dialog.open();
        }
    }

    /**
     * addcomment_button action
     */
    private void setAddcomment_button(){
        CommentTaskWindow ctw = new CommentTaskWindow(this);
        main_layout.add(ctw.main_dialog);
        ctw.main_dialog.open();
    }

    /**
     * delete_button action
     */
    private void deletebutton_action(){

        if ( taskObject.aim_task_id != null ){
            Database_AIMTask dat = new Database_AIMTask(AimApplication.database);
            String data = dat.remove(taskObject);
            Notification.show("Removed ("+data+")");
            if ( AimApplication.session_ctc!= null ){
                AimApplication.session_ctc.updateLayout();
            }
            main_dialog.close();
        }

        else if (projectWithTask != null){
            Database_AIMProject dap = new Database_AIMProject(AimApplication.database);
            int ans = dap.removeTaskFromProject(projectWithTask,taskObject);
            if ( ans == 1 ){
                Notification.show("Removed from project ("+projectWithTask.aim_project_id+")");
                main_dialog.close();
                if ( AimApplication.session_cpc!= null ){
                    AimApplication.session_cpc.updateLayout();
                }
            }
        }

        else if (boardWithTask != null){
            Database_AIMBoard dab = new Database_AIMBoard(AimApplication.database);
            int ans = dab.removeTaskFromBoard(boardWithTask,boardTask);
            if ( ans == 1 ){
                Notification.show("Removed from board ("+boardWithTask.board_id+")");
                main_dialog.close();
                AimApplication.currentBoardTaskList.reloadView();
            }
        }
    }

    /**
     * sharetask_button action
     */
    private void sharetaskbutton_action(){
        Database_AIMTask dat = new Database_AIMTask(AimApplication.database);
        if ( subItems5.getText().equals("Share Task") ){
            String share = dat.shareTask(taskObject);
            if (share != null){
                shareClickCount = 0;
                subItems5.setText(share);
                Notification.show("Task was shared!");
                UI.getCurrent().getPage().executeJs("window.copyToClipboard($0)", share);
                Notification.show("Share code copied to clipboard!");
            }
            else{
                Notification.show("Failed to share task, check log!");
            }
        }
        else{
            if (shareClickCount == 2){
                int ans = dat.removeShareTask(taskObject);
                if ( ans == 1 ){
                    Notification.show("Share removed!");
                    subItems5.setText("Share Task");
                    shareClickCount = 0;
                }
            }
            else if (shareClickCount == 1){
                Notification.show("Next click remove sharing in the task object!");
                UI.getCurrent().getPage().executeJs("window.copyToClipboard($0)", subItems5.getText());
                Notification.show("Share code copied to clipboard!");
                shareClickCount++;
            }
            else{
                UI.getCurrent().getPage().executeJs("window.copyToClipboard($0)", subItems5.getText());
                Notification.show("Share code copied to clipboard!");
                shareClickCount++;
            }
        }
        shareClickCount++;
    }
}

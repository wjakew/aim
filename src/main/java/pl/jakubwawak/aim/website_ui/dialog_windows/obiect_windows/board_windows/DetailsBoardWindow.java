/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package pl.jakubwawak.aim.website_ui.dialog_windows.obiect_windows.board_windows;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.atmosphere.interceptor.AtmosphereResourceStateRecovery;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Board;
import pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers.aim_objects_viewers_board.AIM_BoardTaskListLayout;

public class DetailsBoardWindow {

    VerticalLayout boardDetailsLayout;
    public Dialog main_dialog;
    AIM_Board board;

    Button members_button,update_button,addtask_button,changeowner_button, boardhistory_button;




    /**
     * Constructor
     */
    public DetailsBoardWindow(AIM_Board boardToShow){
        board = boardToShow;
        main_dialog = new Dialog();
        boardDetailsLayout = new VerticalLayout();
        prepareLayout();
    }

    /**
     * Function for preparing components
     */
    void prepareComponents(){
        members_button = new Button("Members("+board.board_members.size()+")", VaadinIcon.USERS.create(),this::membersbutton_action);
        members_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);

        update_button = new Button("Update Data",VaadinIcon.DATABASE.create(),this::updatebutton_action);
        update_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);

        changeowner_button = new Button("Change Owner",VaadinIcon.USER.create(),this::changeownerbutton_action);
        changeowner_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);

        addtask_button = new Button("Add Task",VaadinIcon.PLUS.create(),this::addtaskbutton_action);
        addtask_button.addThemeVariants(ButtonVariant.LUMO_SUCCESS,ButtonVariant.LUMO_PRIMARY);

        boardhistory_button = new Button("History",VaadinIcon.TIME_BACKWARD.create(),this::historybutton_action);
        boardhistory_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);

        AimApplication.currentBoardTaskList = new AIM_BoardTaskListLayout(board);
   }

    /**
     * Function for preparing layout
     */
    void prepareLayout(){
        boardDetailsLayout.removeAll();
        prepareComponents();
        // creating horizontal button layout
        HorizontalLayout hl = new HorizontalLayout();
        hl.setWidth("100%");
        hl.setMargin(true);
        hl.getStyle().set("color","black");
        hl.getStyle().set("border-radius","15px");

        FlexLayout left_layout = new FlexLayout();
        left_layout.setSizeFull();
        left_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        left_layout.setAlignItems(FlexComponent.Alignment.START);
        left_layout.add(members_button,update_button,changeowner_button);

        FlexLayout center_layout = new FlexLayout();
        center_layout.setSizeFull();
        center_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        center_layout.setAlignItems(FlexComponent.Alignment.CENTER);
        center_layout.add(new H4(board.board_name));

        FlexLayout right_layout = new FlexLayout();
        right_layout.setSizeFull();
        right_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        right_layout.setAlignItems(FlexComponent.Alignment.END);
        right_layout.add(boardhistory_button,addtask_button);

        hl.add(left_layout,center_layout,right_layout);

        // adding upper horizontal layout
        boardDetailsLayout.add(hl);

        // creating lower horizontal data layout
        FlexLayout leftdown_layout = new FlexLayout();
        leftdown_layout.setSizeFull();
        leftdown_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        leftdown_layout.setAlignItems(FlexComponent.Alignment.START);
        leftdown_layout.add(new H6("ID: "+board.board_id.toString()));

        FlexLayout rightdown_layout = new FlexLayout();
        rightdown_layout.setSizeFull();
        rightdown_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        rightdown_layout.setAlignItems(FlexComponent.Alignment.END);
        rightdown_layout.add(new H6("OWNER: "+board.board_owner.getString("aim_user_email")));

        HorizontalLayout hl_down = new HorizontalLayout();
        hl_down.setWidth("100%");
        hl_down.setMargin(true);
        hl_down.getStyle().set("color","black");
        hl_down.getStyle().set("border-radius","15px");
        hl_down.add(leftdown_layout,rightdown_layout);
        // adding lower horizontal layout
        boardDetailsLayout.add(hl_down);

        // add task list layout to window
        boardDetailsLayout.add(AimApplication.currentBoardTaskList.main_layout);

        //setup permission
        if ( !board.board_owner.equals(AimApplication.loggedUser.prepareDocument()) ){
            members_button.setEnabled(false);
            update_button.setEnabled(false);
            changeowner_button.setEnabled(false);
        }

        boardDetailsLayout.setSizeFull();
        boardDetailsLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        boardDetailsLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        boardDetailsLayout.getStyle().set("text-align", "center");
        boardDetailsLayout.getStyle().set("--lumo-font-family","Monospace");
        main_dialog.setWidth("80%");main_dialog.setHeight("80%");

        main_dialog.add(boardDetailsLayout);
    }

    /**
     * history_button action
     * @param ex
     */
    private void historybutton_action(ClickEvent ex){
        BoardHistoryWindow bhw = new BoardHistoryWindow(board);
        boardDetailsLayout.add(bhw.main_dialog);
        bhw.main_dialog.open();
    }

    /**
     * members_button action
     * @param ex
     */
    private void membersbutton_action(ClickEvent ex){
        BoardMembersWindow bmw = new BoardMembersWindow(board);
        boardDetailsLayout.add(bmw.main_dialog);
        bmw.main_dialog.open();
    }

    /**
     * updateboard_button action
     * @param ex
     */
    private void updatebutton_action(ClickEvent ex){
        InsertBoardWindow ibw = new InsertBoardWindow(board);
        boardDetailsLayout.add(ibw.main_dialog);
        ibw.main_dialog.open();
    }

    /**
     * addtask_button action
     * @param ex
     */
    private void addtaskbutton_action(ClickEvent ex){
        AddTaskBoardWindow atbw = new AddTaskBoardWindow(board,null);
        boardDetailsLayout.add(atbw.main_dialog);
        atbw.main_dialog.open();
    }

    /**
     * changeowner_button
     * @param ex
     */
    private void changeownerbutton_action(ClickEvent ex){
        ChangeBoardOwnerWindow cbow = new ChangeBoardOwnerWindow(this);
        boardDetailsLayout.add(cbow.main_dialog);
        cbow.main_dialog.open();
    }
}

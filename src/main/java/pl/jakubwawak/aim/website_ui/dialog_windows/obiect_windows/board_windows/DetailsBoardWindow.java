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
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Board;

public class DetailsBoardWindow {

    VerticalLayout boardDetailsLayout;
    public Dialog main_dialog;
    AIM_Board board;

    Button members_button,update_button,addtask_button,changeowner_button;



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
        members_button = new Button("Members", VaadinIcon.USERS.create());
        members_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);

        update_button = new Button("Update Data",VaadinIcon.DATABASE.create());
        update_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);

        changeowner_button = new Button("Change Owner",VaadinIcon.USER.create());
        changeowner_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);

        addtask_button = new Button("Add Task",VaadinIcon.PLUS.create(),this::addtaskbutton_action);
        addtask_button.addThemeVariants(ButtonVariant.LUMO_SUCCESS,ButtonVariant.LUMO_PRIMARY);
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
        right_layout.add(addtask_button);

        hl.add(left_layout,center_layout,right_layout);
        boardDetailsLayout.add(hl);
        boardDetailsLayout.add(new H6(board.board_id.toString()));

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
     * addtask_button action
     * @param ex
     */
    private void addtaskbutton_action(ClickEvent ex){
        AddTaskBoardWindow atbw = new AddTaskBoardWindow(board,null);
        boardDetailsLayout.add(atbw.main_dialog);
        atbw.main_dialog.open();
    }
}

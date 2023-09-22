/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.website_ui.dialog_windows.obiect_windows.board_windows;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import lombok.extern.java.Log;
import org.bson.Document;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Board;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_User;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMBoard;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMUser;
import pl.jakubwawak.maintanance.GridElement;

import java.util.ArrayList;
import java.util.Set;

/**
 * Window for logging user to the app
 */
public class InsertBoardWindow {

    // variables for setting x and y of window
    public String width = "50%";
    public String height = "70%";
    public String backgroundStyle = "";

    // main login components
    public Dialog main_dialog;
    VerticalLayout main_layout;

    TextField boardname_field;
    TextArea boarddesc_field;

    Grid<GridElement> members_grid;

    AIM_Board boardToAdd;
    ArrayList<GridElement> membersContent;

    Button add_button, addmember_button, removemember_button;

    /**
     * Constructor
     */
    public InsertBoardWindow(AIM_Board boardToAdd){
        main_dialog = new Dialog();
        main_layout = new VerticalLayout();
        if ( boardToAdd!= null ){
            this.boardToAdd = boardToAdd;
        }
        else
            this.boardToAdd = new AIM_Board();
        prepare_dialog();
    }

    /**
     * Function for preparing components
     */
    void prepare_components(){
        // set components
        boardname_field = new TextField("Board Name");
        boardname_field.setPlaceholder("My new amazing board :)");
        boardname_field.setWidth("100%");
        membersContent = new ArrayList<>();
        boarddesc_field = new TextArea("Board Description");
        boarddesc_field.setPlaceholder("Oh my god such a cute board!");
        boarddesc_field.setWidth("100%");
        members_grid = new Grid<>();
        members_grid.addColumn(GridElement::getGridelement_text).setHeader("Members");
        reloadMembersGrid();

        add_button = new Button("Create Board",this::addboardbutton_action);
        add_button.setWidth("100%");
        add_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);

        addmember_button = new Button("", VaadinIcon.PLUS.create(),this::addmemberbutton_action);
        addmember_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);

        removemember_button = new Button("",VaadinIcon.MINUS.create(),this::removememberbutton_action);
        removemember_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);
    }

    /**
     * Function for reloading members grid
     */
    public void reloadMembersGrid(){
        membersContent.clear();
        for(Document data : boardToAdd.board_members){
            membersContent.add(new GridElement(data.getString("aim_user_email")));
        }
        members_grid.setItems(membersContent);
        members_grid.getDataProvider().refreshAll();
    }

    /**
     * Function for preparing layout
     */
    void prepare_dialog(){
        prepare_components();
        // set layout
        HorizontalLayout mainhorizontal_layout = new HorizontalLayout();
        mainhorizontal_layout.setSizeFull();
        mainhorizontal_layout.setAlignItems(FlexComponent.Alignment.CENTER);

        VerticalLayout hl_left = new VerticalLayout();
        hl_left.setWidth("50%");hl_left.setHeight("50%");
        hl_left.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        hl_left.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        hl_left.getStyle().set("text-align", "center");
        hl_left.add(boardname_field,boarddesc_field);

        VerticalLayout hl_right = new VerticalLayout();
        hl_right.setWidth("50%");hl_left.setHeight("50%");
        hl_right.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        hl_right.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        hl_right.getStyle().set("text-align", "center");
        hl_right.add(members_grid,new HorizontalLayout(addmember_button,removemember_button));

        mainhorizontal_layout.add(hl_left,hl_right);
        main_layout.add(new H3("New Board"));
        main_layout.add(mainhorizontal_layout);
        main_layout.add(add_button);

        main_layout.setSizeFull();
        main_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        main_layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        main_layout.getStyle().set("text-align", "center");

        main_layout.getStyle().set("border-radius","25px");
        main_layout.getStyle().set("background-color",backgroundStyle);
        main_layout.getStyle().set("--lumo-font-family","Monospace");
        main_dialog.add(main_layout);
        main_dialog.setWidth(width);main_dialog.setHeight(height);
    }

    /**
     * addmember_button action
     * @param ex
     */
    private void addmemberbutton_action(ClickEvent ex){
        AddMemberBoardWindow ambw = new AddMemberBoardWindow(this);
        main_layout.add(ambw.main_dialog);
        ambw.main_dialog.open();
    }

    /**
     * removemember_button action
     * @param ex
     */
    private void removememberbutton_action(ClickEvent ex){
        Database_AIMUser dau = new Database_AIMUser(AimApplication.database);
        Set<GridElement> selected = members_grid.getSelectedItems();
        for(GridElement element : selected){
            String email  = element.getGridelement_text();
            AIM_User user = dau.getAIMUser(email);
            boardToAdd.board_members.remove(user.prepareDocument());
            reloadMembersGrid();
            Notification.show("Removed "+email+" from members!");
            break;
        }
    }

    /**
     * addboard_button action
     * @param ex
     */
    private void addboardbutton_action(ClickEvent ex){
        boardToAdd.board_name = boardname_field.getValue();
        boardToAdd.board_desc = boarddesc_field.getValue();
        if ( !boardToAdd.isEmpty() ){
            // add board object to database
            Database_AIMBoard dab = new Database_AIMBoard(AimApplication.database);
            int ans = dab.insertBoard(boardToAdd);
            if ( ans == 1 ){
                Notification.show("Board added!");
                main_dialog.close();
            }
            else{
                Notification.show("Failed to add board, check log!");
            }
        }
        else{
            Notification.show("Board object is empty!");
        }
    }
}

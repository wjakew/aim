/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.website_ui.dialog_windows;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.extern.java.Log;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_User;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMUser;
import pl.jakubwawak.maintanance.GridElement;

import java.util.ArrayList;

/**
 * Window for logging user to the app
 */
public class AdminConsoleWindow {

    // variables for setting x and y of window
    public String width = "60%";
    public String height = "60%";
    public String backgroundStyle = "";

    // main login components
    public Dialog main_dialog;
    VerticalLayout main_layout;

    Grid<GridElement> user_grid;
    ArrayList<GridElement> user_content;

    Button adduser_button, remove_button, enableaccountcreation_button;

    /**
     * Constructor
     */
    public AdminConsoleWindow(){
        main_dialog = new Dialog();
        main_layout = new VerticalLayout();
        prepare_dialog();
    }

    /**
     * Function for preparing components
     */
    void prepare_components(){
        // set components
        user_grid = new Grid<>(GridElement.class,false);
        user_grid.addColumn(GridElement::getGridelement_text).setHeader("User Email");
        user_grid.addColumn(GridElement::getGridelement_details).setHeader("ID");

        user_content = new ArrayList<>();
        Database_AIMUser dau = new Database_AIMUser(AimApplication.database);
        ArrayList<AIM_User> userCollection = dau.getUserCollection();
        user_content.clear();
        for(AIM_User user : userCollection){
            user_content.add(new GridElement(user.aim_user_email,0,user.aim_user_id.toString()));
        }
        user_grid.setItems(user_content);
        user_grid.setWidth("100%");user_grid.setHeight("100%");

        adduser_button = new Button("Add User", VaadinIcon.PLUS.create());
        remove_button = new Button("Remove User",VaadinIcon.TRASH.create());
        enableaccountcreation_button = new Button("Enable Account Creation",VaadinIcon.USER_CLOCK.create());
        adduser_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);
    }

    /**
     * Function for reloading user content
     */
    void reloadUserContent(){
        Database_AIMUser dau = new Database_AIMUser(AimApplication.database);
        ArrayList<AIM_User> userCollection = dau.getUserCollection();
        user_content.clear();
        for(AIM_User user : userCollection){
            user_content.add(new GridElement(user.aim_user_email,0,user.aim_user_id.toString()));
        }
        user_grid.getDataProvider().refreshAll();
    }

    /**
     * Function for preparing layout
     */
    void prepare_dialog(){
        prepare_components();
        // set layout
        main_layout.add(new H6("Admin Console"));
        main_layout.add(user_grid);
        main_layout.add(new HorizontalLayout(adduser_button,remove_button,enableaccountcreation_button));

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
}

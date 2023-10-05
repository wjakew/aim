/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.website_ui.dialog_windows;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.extern.java.Log;
import org.atmosphere.interceptor.AtmosphereResourceStateRecovery;
import org.bson.types.ObjectId;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_User;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMUser;
import pl.jakubwawak.maintanance.GridElement;

import java.util.ArrayList;
import java.util.Set;

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

    Button adduser_button, remove_button, enableaccountcreation_button, setadmin_button;
    Button logasuser_button;

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

        adduser_button = new Button("Add User", VaadinIcon.PLUS.create(),this::adduserbutton_action);
        remove_button = new Button("Remove User",VaadinIcon.TRASH.create(),this::removebutton_action);
        setadmin_button = new Button("Set Admin",VaadinIcon.PENCIL.create(),this::setadminbutton_action);
        logasuser_button = new Button("Log as selected user",VaadinIcon.USER.create(),this::logasuserbutton_action);
        enableaccountcreation_button = new Button("Enable Account Creation",VaadinIcon.USER_CLOCK.create(),this::enableaccountcreationbutton_action);
        adduser_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);
        setadmin_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);
        logasuser_button.addThemeVariants(ButtonVariant.LUMO_ERROR,ButtonVariant.LUMO_PRIMARY);
        logasuser_button.setWidth("100%");

        if ( AimApplication.globalConfiguration.userCreationFlag == 0){
            enableaccountcreation_button.setText("Enable Account Creation");
        }
        else{
            enableaccountcreation_button.setText("Disable Account Creation");
        }
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
        main_layout.add(new HorizontalLayout(adduser_button,remove_button,enableaccountcreation_button,setadmin_button));
        main_layout.add(logasuser_button);

        main_layout.setSizeFull();
        main_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        main_layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        main_layout.getStyle().set("text-align", "center");

        main_layout.getStyle().set("border-radius","25px");
        main_layout.getStyle().set("background-color",backgroundStyle);
        main_layout.getStyle().set("--lumo-font-family","Monospace");
        main_dialog.add(main_layout);
        main_dialog.setWidth(width);main_dialog.setHeight(height);

        user_grid.addItemClickListener(e -> {
            Database_AIMUser dau = new Database_AIMUser(AimApplication.database);
            Set<GridElement> selected = user_grid.getSelectedItems();
            for(GridElement selected_user : selected){
                AIM_User user = dau.getAIMUser(selected_user.getGridelement_text());
                if ( user.aim_user_type.equals("SERVERADM")){
                    setadmin_button.setText("Set to normal account");
                }
                else{
                    setadmin_button.setText("Set to admin account");
                }
            }
        });
    }

    /**
     * adduser_button action
     * @param ex
     */
    private void adduserbutton_action(ClickEvent ex){
        CreateAccountWindow caw = new CreateAccountWindow();
        main_layout.add(caw.main_dialog);
        caw.main_dialog.open();
    }

    /**
     * remove_button action
     * @param ex
     */
    private void removebutton_action(ClickEvent ex){
        Set<GridElement> selected = user_grid.getSelectedItems();
        Database_AIMUser dau = new Database_AIMUser(AimApplication.database);
        for(GridElement select_object : selected){
            int ans = dau.removeAIMUser(dau.getAIMUser(select_object.getGridelement_text()));
            if ( ans == 1 ){
                Notification.show("User deleted!");
                main_dialog.close();
            }
            else{
                Notification.show("Cannot delete user, check log!");
            }
            break;
        }
    }

    /**
     * setadmin_button action
     * @param ex
     */
    private void setadminbutton_action(ClickEvent ex){
        Database_AIMUser dau = new Database_AIMUser(AimApplication.database);
        switch(setadmin_button.getText()){
            case "Set to normal account":
            {
                Set<GridElement> selected = user_grid.getSelectedItems();
                for(GridElement selected_user : selected){
                    AIM_User user = dau.getAIMUser(selected_user.getGridelement_text());
                    int ans = dau.setUserNRM(user);
                    if(ans > 0){
                        Notification.show("User account changed to normal");
                    }
                    else{
                        Notification.show("Error, check log!");
                    }
                    break;
                }
                break;
            }
            case "Set to admin account":
            {
                Set<GridElement> selected = user_grid.getSelectedItems();
                for(GridElement selected_user : selected){
                    AIM_User user = dau.getAIMUser(selected_user.getGridelement_text());
                    int ans = dau.setUserAdmin(user);
                    if(ans > 0){
                        Notification.show("User account changed to admin");
                    }
                    else{
                        Notification.show("Error, check log!");
                    }
                    break;
                }
                break;
            }
        }
    }

    /**
     * Function for enabling account creation
     * @param ex
     */
    private void enableaccountcreationbutton_action(ClickEvent ex){
        switch(enableaccountcreation_button.getText()){
            case "Enable Account Creation":
            {
                int ans = AimApplication.database.enableAccountCreation();
                enableaccountcreation_button.setText("Disable Account Creation");
                Notification.show("User account creation enabled! ("+ans+")");
                break;
            }
            case "Disable Account Creation":
            {
                int ans = AimApplication.database.disableAccountCreation();
                enableaccountcreation_button.setText("Enable Account Creation");
                Notification.show("User account creation disabled! ("+ans+")");
                break;
            }
        }
    }

    /**
     * Function for logging as selected user
     * @param ex
     */
    private void logasuserbutton_action(ClickEvent ex){
        Set<GridElement> selected = user_grid.getSelectedItems();
        Database_AIMUser dau = new Database_AIMUser(AimApplication.database);
        for(GridElement select_object : selected){
            AIM_User user = dau.getAIMUser(select_object.getGridelement_text());
            AimApplication.loggedUser = user;
            main_dialog.close();
            logasuser_button.getUI().ifPresent(ui ->
                    ui.navigate("/home"));
            Notification.show("Logged as "+AimApplication.loggedUser.aim_user_email);
            break;
        }
    }
}

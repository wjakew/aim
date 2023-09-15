/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.website_ui.dialog_windows.obiect_windows;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Task;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMTask;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMUser;


/**
 * Window for logging user to the app
 */
public class ChangeTaskOwnerWindow {

    // variables for setting x and y of window
    public String width = "";
    public String height = "";
    public String backgroundStyle = "";

    // main login components
    public Dialog main_dialog;
    VerticalLayout main_layout;

    TextField ownerlogin_field;

    Button change_button;

    AIM_Task taskToUpdate;

    /**
     * Constructor
     */
    public ChangeTaskOwnerWindow(AIM_Task taskToUpdate){
        this.taskToUpdate = taskToUpdate;
        main_dialog = new Dialog();
        main_layout = new VerticalLayout();
        prepare_dialog();
    }

    /**
     * Function for preparing components
     */
    void prepare_components(){
        // set components
        ownerlogin_field = new TextField("User Email");
        ownerlogin_field.setPrefixComponent(VaadinIcon.USER.create());
        ownerlogin_field.setWidth("100%");

        change_button = new Button("Change Owner",this::changebutton_action);
        change_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);
        change_button.setWidth("100%");

    }

    /**
     * Function for preparing layout
     */
    void prepare_dialog(){
        prepare_components();
        // set layout

        main_layout.add(new H6("CHANGE TASK OWNER"));
        main_layout.add(ownerlogin_field);
        main_layout.add(change_button);

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
     * Function for changing user action
     * @param ex
     */
    private void changebutton_action(ClickEvent ex){
        Database_AIMTask dat = new Database_AIMTask(AimApplication.database);
        Database_AIMUser dau = new Database_AIMUser(AimApplication.database);
        if ( !ownerlogin_field.getValue().isBlank() ){
            if (dau.checkIfUserExists(ownerlogin_field.getValue())){
                // user checked on database
                int ans = dat.changeOwnerAIMTask(taskToUpdate,ownerlogin_field.getValue());
                if ( ans == 1 ){
                    Notification.show("Owner changed!");
                    AimApplication.session_ctc.updateLayout();
                    main_dialog.close();
                }
            }
            else{
                Notification.show("User didn't exists");
            }
        }
        else{
            Notification.show("Wrong user email!");
        }
    }
}

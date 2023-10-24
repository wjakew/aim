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
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.extern.java.Log;
import org.atmosphere.interceptor.AtmosphereResourceStateRecovery;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_APIUserKey;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_APIKey;

/**
 * Window for managing API keys
 */
public class APIManagerWindow {

    // variables for setting x and y of window
    public String width = "90%";
    public String height = "40%";
    public String backgroundStyle = "";

    // main login components
    public Dialog main_dialog;

    VerticalLayout main_layout;

    Database_APIKey databaseApiKey;
    Button createapi_button;
    Button blockapi_button;




    /**
     * Constructor
     */
    public APIManagerWindow(){
        main_dialog = new Dialog();
        main_layout = new VerticalLayout();
        databaseApiKey = new Database_APIKey(AimApplication.database);
        prepare_dialog();
    }

    /**
     * Function for preparing components
     */
    void prepare_components(){
        // set components
        AIM_APIUserKey userKey = databaseApiKey.checkloggedUserAPIKey();
        createapi_button = new Button("error", VaadinIcon.PLUG.create(),this::createapibutton_action);
        blockapi_button = new Button("error",VaadinIcon.STOP.create());

        createapi_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);
        blockapi_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);

        if ( userKey != null ){
            createapi_button.setText("Remove key");
            blockapi_button.setText("Block your API");
        }
        else{
            createapi_button.setText("Create API Key");
        }
    }

    /**
     * Function for preparing layout
     */
    void prepare_dialog(){
        prepare_components();
        // set layout
        AIM_APIUserKey userKey = databaseApiKey.checkloggedUserAPIKey();
        if (userKey != null){
            main_layout.add(new H6("API KEY CREATED"));
            main_layout.add(new H6(userKey.apiuserkey_value));
            main_layout.add(new H6(userKey.apiuserkey_timegenerated));
            main_layout.add(new HorizontalLayout(blockapi_button,createapi_button));
        }
        else{
            main_layout.add(new H6("NO API KEY CREATED"));
            main_layout.add(createapi_button);
        }
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
     * createapi_button action
     * @param ex
     */
    private void createapibutton_action(ClickEvent ex){
        switch (createapi_button.getText()){
            case "Create API Key":
            {
                Database_APIKey dak = new Database_APIKey(AimApplication.database);
                int ans = dak.insertNewApiKey(new AIM_APIUserKey());
                if ( ans == 1 ){
                    Notification.show("Created new API key!");
                    main_dialog.close();
                }
                else{
                    Notification.show("Failed to create API key, check application log!");
                    main_dialog.close();
                }
                break;
            }
            case "Remove key":
            {
                Database_APIKey dak = new Database_APIKey(AimApplication.database);
                int ans = dak.removeUserAPIKey(AimApplication.loggedUser.aim_user_id);
                if ( ans == 1 ){
                    Notification.show("Removed your API key!");
                    main_dialog.close();
                }
                else{
                    Notification.show("Failed to remove API key, check application log!");
                    main_dialog.close();
                }
                break;
            }
        }
    }
}

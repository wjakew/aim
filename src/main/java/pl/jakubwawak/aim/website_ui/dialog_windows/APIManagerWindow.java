/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.website_ui.dialog_windows;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.JsModule;
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

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;

/**
 * Window for managing API keys
 */
@JsModule("./recipe/copytoclipboard.js")
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

    Button downloadakey_button;

    Button apikey_button;

    String status;
    AIM_APIUserKey userKey;



    /**
     * Constructor
     */
    public APIManagerWindow(){
        databaseApiKey = new Database_APIKey(AimApplication.database);
        userKey = databaseApiKey.checkloggedUserAPIKey();
        main_dialog = new Dialog();
        main_dialog.addClassName("aim-window-normal");
        main_layout = new VerticalLayout();
        prepare_dialog();
    }

    /**
     * Function for preparing components
     */
    void prepare_components(){
        // set components
        createapi_button = new Button("error", VaadinIcon.PLUG.create(),this::createapibutton_action);
        blockapi_button = new Button("error",VaadinIcon.STOP.create(),this::blockapibutton_action);
        downloadakey_button = new Button("Download aKey",VaadinIcon.DOWNLOAD.create(),this::setDownloadakey_button);

        if ( userKey != null ){
            apikey_button = new Button(userKey.apiuserkey_value,VaadinIcon.KEY.create(),this::apikeybutton_action);
            apikey_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);
        }

        createapi_button.addClassName("aim-button-black");
        blockapi_button.addClassName("aim-button-black");
        downloadakey_button.addClassName("aim-button-black");

        if ( userKey != null ){
            createapi_button.setText("Remove key");
            blockapi_button.setText("Block your API");

            if ( userKey.apiuserkey_activeflag == 1 ){
                status = "API ACTIVE";
            }
            else{
                status = "API INACTIVE";
            }
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
        if (userKey != null){
            main_layout.add(new H6("API KEY CREATED"));
            main_layout.add(apikey_button);
            main_layout.add(new H6(userKey.apiuserkey_timegenerated));
            main_layout.add(new H6(status));
            main_layout.add(new HorizontalLayout(blockapi_button,createapi_button));
            main_layout.add(downloadakey_button);
        }
        else{
            main_layout.add(new H6("NO API KEY CREATED"));
            main_layout.add(createapi_button);
        }
        main_layout.setSizeFull();
        main_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        main_layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
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

    /**
     * apikey_button action
     * @param ex
     */
    private void apikeybutton_action(ClickEvent ex){
        UI.getCurrent().getPage().executeJs("window.copyToClipboard($0)", apikey_button.getText());
        Notification.show("API Key copied to clipboard!");
    }

    /**
     * blockapi_button action
     * @param ex
     */
    private void blockapibutton_action(ClickEvent ex) {
        switch (status) {
            case "API ACTIVE": {
                int ans = databaseApiKey.changeAPIKeyStatus(userKey, 0);
                if (ans == 1) {
                    Notification.show("Status changed");
                    main_dialog.close();
                } else {
                    Notification.show("Failed to change status, check log");
                }
                break;
            }
            case "API INACTIVE": {
                int ans = databaseApiKey.changeAPIKeyStatus(userKey, 1);
                if (ans == 1) {
                    Notification.show("Status changed");
                    main_dialog.close();
                } else {
                    Notification.show("Failed to change status, check log");
                }
                break;
            }
        }
    }

    /**
     * downloadakey_button action
     * @param ex
     */
    private void setDownloadakey_button(ClickEvent ex){
        try{
            FileWriter fw = new FileWriter("aim_instance.akey");
            fw.write("#this is the key to connect to the aim server - don't change\n");
            fw.write("connectionString%"+AimApplication.connectionStringDebug+"\n");
            fw.write("userLogged%"+AimApplication.loggedUser.aim_user_email+"\n");
            fw.write("apiKey%"+apikey_button.getText()+"\n");
            fw.write("time%"+ LocalDateTime.now()+"\n");
            fw.write("#do not change this file, file is validated"+"\n");
            fw.close();
            File file = new File("aim_instance.akey");
            if ( file.exists() ){
                FileDownloaderComponent fdc = new FileDownloaderComponent(file);
                main_dialog.add(fdc.dialog);
                fdc.dialog.open();
            }
        }catch(Exception e){
            Notification.show(e.toString());
        }


    }
}

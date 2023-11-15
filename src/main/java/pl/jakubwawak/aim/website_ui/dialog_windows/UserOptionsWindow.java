/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.website_ui.dialog_windows;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMUser;
import pl.jakubwawak.maintanance.GridElement;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Window for showing options for user
 */
public class UserOptionsWindow {

    // variables for setting x and y of window
    public String width = "30%";
    public String height = "40%";
    public String backgroundStyle = "";

    // main login components
    public Dialog main_dialog;
    VerticalLayout main_layout;

    ComboBox<GridElement> homepage_combobox;

    TextField color1_field, color2_field;

    Button savecolors_button, clear_button;


    /**
     * Constructor
     */
    public UserOptionsWindow(){
        main_dialog = new Dialog();
        main_layout = new VerticalLayout();
        prepare_dialog();
    }

    /**
     * Function for preparing components
     */
    void prepare_components(){
        // set components
        homepage_combobox = new ComboBox<>();
        ArrayList<GridElement> content = new ArrayList<>();
        content.add(new GridElement("/home"));
        content.add(new GridElement("/terminal"));
        content.add(new GridElement("/dashboard"));
        content.add(new GridElement("/aim"));
        content.add(new GridElement("/widgets"));

        homepage_combobox.setItems(content);
        homepage_combobox.setItemLabelGenerator(GridElement::getGridelement_text);

        if (!AimApplication.loggedUser.aim_user_configuration1.isEmpty()){
            homepage_combobox.setValue(new GridElement(AimApplication.loggedUser.aim_user_configuration1));
        }

        homepage_combobox.setWidth("100%");

        homepage_combobox.addValueChangeListener(e->{
            GridElement selected = homepage_combobox.getValue();
            Database_AIMUser dau = new Database_AIMUser(AimApplication.database);
            int ans = dau.setUserHomePage(selected.getGridelement_text());
            if ( ans == 1){
                Notification.show("Home page updated to "+selected.getGridelement_text());
                AimApplication.loggedUser.aim_user_configuration1 = selected.getGridelement_text();
                main_dialog.close();
            }
            else{
                Notification.show("Application error, check log!");
            }
        });

        color1_field = new TextField("");
        color1_field.setPlaceholder("color1 hex");
        color2_field = new TextField("");
        color2_field.setPlaceholder("color2 hex");

        if (!AimApplication.loggedUser.aim_user_configuration2.equals("blank")){
            color1_field.setValue(AimApplication.loggedUser.aim_user_configuration2.split(",")[0]);
            color2_field.setValue(AimApplication.loggedUser.aim_user_configuration2.split(",")[1]);
        }

        savecolors_button = new Button("Save", VaadinIcon.PAINTBRUSH.create(),this::savecolorsbutton_action);
        savecolors_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);
        clear_button = new Button("Clear",VaadinIcon.TRASH.create(),this::clearbutton_action);
        clear_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);


    }

    /**
     * Function for preparing layout
     */
    void prepare_dialog(){
        prepare_components();
        // set layout

        main_layout.add(new H3("User Settings"));
        main_layout.add(new H6("SET  YOUR LANDING PAGE"));
        main_layout.add(homepage_combobox);
        main_layout.add(new H6("SET CUSTOM COLORS"));
        main_layout.add(new HorizontalLayout(color1_field,color2_field));
        main_layout.add(new HorizontalLayout(savecolors_button,clear_button));

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
     * savecolor_button action
     * @param ex
     */
    private void savecolorsbutton_action(ClickEvent ex){
        Pattern pattern = Pattern.compile("^(#[0-9A-F]{6}),(#[0-9A-F]{6})$");
        Database_AIMUser dau = new Database_AIMUser(AimApplication.database);
        if (!color1_field.getValue().isEmpty() && !color2_field.getValue().isEmpty()){
            String value = color1_field.getValue()+","+color2_field.getValue();
            Matcher matcher = pattern.matcher(value);
            if (matcher.matches()){
                int ans = dau.setBackgroundColors(color1_field.getValue(),color2_field.getValue());
                if (ans == 1){
                    Notification.show("Colors changed! Re-login to make difference!");
                    AimApplication.loggedUser.aim_user_configuration2 = value;
                }
                else{
                    Notification.show("Application error! Check log!");
                }
            }
            else{
                Notification.show("Wrong input. Input should be similar to #JW0001");
            }
        }
        else{
            Notification.show("Empty values!");
        }
    }

    /**
     * clear_button action
     * @param ex
     */
    private void clearbutton_action(ClickEvent ex){
        Database_AIMUser dau = new Database_AIMUser(AimApplication.database);
        int ans = dau.clearBackgroundColors();
        if (ans == 0){
            Notification.show("Colors set to default! Re-login to make difference!");
            AimApplication.loggedUser.aim_user_configuration2 = "";
            color1_field.setValue("");
            color2_field.setValue("");
        }
        else{
            Notification.show("Application error! Check log!");
        }
    }
}

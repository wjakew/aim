/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package pl.jakubwawak.aim.website_ui.views;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.Lumo;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.website_ui.PageHeader;
import pl.jakubwawak.aim.website_ui.widgets.widgets.WidgetPickerWindow;

/**
 * Object for showing workspace view
 */
@PageTitle("aim workspace")
@Route(value = "workspace")
public class WorkspaceView extends VerticalLayout {

    PageHeader pageHeader;

    Button addtoworkspace_button;

    VerticalLayout mainLayout;


    /**
     * Constructor
     */
    public WorkspaceView(){
        this.getElement().setAttribute("theme", Lumo.DARK);
        prepare_view();

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
        if ( AimApplication.loggedUser.aim_user_configuration2.equals("blank") ){
            getStyle().set("background-image","radial-gradient(black,gray)");
        }
        else{
            getStyle().set("background-image","radial-gradient("+AimApplication.loggedUser.aim_user_configuration2+")");
        }
        getStyle().set("--lumo-font-family","Monospace");
    }

    /**
     * Function for preparing components
     */
    void prepare_components(){
        pageHeader = new PageHeader();

        mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();
        mainLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        mainLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        mainLayout.getStyle().set("text-align", "center");

        addtoworkspace_button = new Button("Add window to workspace", VaadinIcon.PLUS.create(),this::setAddtoworkspace_button);
        addtoworkspace_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);
    }

    /**
     * Function for preparing view and components
     */
    void prepare_view(){
        if (AimApplication.loggedUser != null){
            prepare_components();
            mainLayout.add(addtoworkspace_button);
            add(pageHeader);
            add(mainLayout);
        }
        else{
            // user not logged
            Notification.show("User not logged!");
            add(new H6("User not logged!"));
            add(new H6("aim"));
        }
    }

    //--button action functions
    private void setAddtoworkspace_button(ClickEvent ex){
        WidgetPickerWindow wpw = new WidgetPickerWindow(this);
        add(wpw.main_dialog);
        wpw.main_dialog.open();
    }
}
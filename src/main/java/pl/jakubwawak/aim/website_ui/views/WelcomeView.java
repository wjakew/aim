/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package pl.jakubwawak.aim.website_ui.views;

import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.Theme;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.website_ui.dialog_windows.LoginWindow;


/**
 * Object for showing welcome view
 */
@PageTitle("aim by Jakub Wawak")
//@PWA(name = "AimApplication", shortName = "Aim")
@Route(value = "/welcome")
@RouteAlias(value = "/")
public class WelcomeView extends VerticalLayout {

    /**
     * Constructor
     */
    public WelcomeView(){
        prepare_view();
        setSizeFull();
        setSpacing(true);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
        getStyle().set("background-image","radial-gradient(white,gray)");
        getStyle().set("--lumo-font-family","Monospace");
    }

    /**
     * Function for preparing components
     */
    void prepare_components(){

    }

    /**
     * Function for preparing view and components
     */
    void prepare_view(){
        prepare_components();
        prepare_loginview();
    }

    /**
     * Function for showing login view
     */
    void prepare_loginview(){
        this.removeAll();
        StreamResource res = new StreamResource("aim_logo.png", () -> {
            return WelcomeView.class.getClassLoader().getResourceAsStream("images/aim_logo.png");
        });

        Image logo = new Image(res,"aim logo");
        logo.setHeight("15rem");
        logo.setWidth("15rem");

        logo.addClickListener(e -> {
            LoginWindow lw = new LoginWindow();
            add(lw.main_dialog);
            lw.main_dialog.open();
        });

        add(logo);
        add(new H6("aim /"+AimApplication.version+"/"+AimApplication.build));
        add(new H6("by Jakub Wawak"));
        AimApplication.globalConfiguration = AimApplication.database.getGlobalConfiguration();
    }

}

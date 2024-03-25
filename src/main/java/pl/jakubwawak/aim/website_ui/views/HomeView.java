/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package pl.jakubwawak.aim.website_ui.views;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.Lumo;
import org.atmosphere.interceptor.AtmosphereResourceStateRecovery;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers.aim_objects_viewers_board.CurrentBoardComposer;
import pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers.aim_objects_viewers_projects.CurrentProjectComposer;
import pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers.aim_objects_viewers_task.CurrentTaskComposer;
import pl.jakubwawak.aim.aim_dataengine.aim_terminal_engine.AIMInputParser;
import pl.jakubwawak.aim.website_ui.PageHeader;
import pl.jakubwawak.aim.website_ui.dialog_windows.AddElementWindow;
import pl.jakubwawak.aim.website_ui.dialog_windows.FloatingWindow;
import pl.jakubwawak.aim.website_ui.dialog_windows.UserWindow;
import pl.jakubwawak.aim.website_ui.style.ButtonStyler;

import java.util.Date;

/**
 * Object for showing welcome view
 */
@PageTitle("aim home")
@Route(value = "/home")
public class HomeView extends VerticalLayout {

    HorizontalLayout navigationLayout;

    Button taskview_button, projectview_button, boardview_button;

    TextField terminal_field;

    PageHeader pageHeader;



    /**
     * Constructor
     */
    public HomeView(){
        this.getElement().setAttribute("theme", Lumo.DARK);
        addClassName("home-view");
        prepare_view();
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }


    /**
     * Function for preparing navigation bar
     */
    void prepareNavigationBar(){
        taskview_button = new Button("Tasks",VaadinIcon.TASKS.create(),this::taskviewbutton_action);
        projectview_button = new Button("Projects",VaadinIcon.BOOK.create(),this::projectviewbutton_action);
        boardview_button = new Button("Boards", VaadinIcon.DASHBOARD.create(),this::boardviewbutton_action);

        projectview_button.addClassName("aim-button-black");
        boardview_button.addClassName("aim-button-black");
        taskview_button.addClassName("aim-button-black");

        navigationLayout = new HorizontalLayout(taskview_button,projectview_button,boardview_button);
        navigationLayout.setMargin(true);
        navigationLayout.setAlignItems(Alignment.CENTER);
    }

    /**
     * Function for loading main page component
     */
    void loadMainPageComponent(int viewIndex){
        removeAll();
        prepare_components();
        prepareNavigationBar();

        add(pageHeader);
        add(terminal_field);
        add(navigationLayout);

        switch(viewIndex){
            case 0: {
                // task component
                // creating new task composer on main page
                AimApplication.session_ctc = new CurrentTaskComposer();
                add(AimApplication.session_ctc.mainLayout);
                Notification.show("Reload page view - tasks");
                break;
            }
            case 1:{
                // projects component
                AimApplication.session_cpc = new CurrentProjectComposer();
                add(AimApplication.session_cpc.mainLayout);
                Notification.show("Reload page view - projects");
                break;
            }
            default:{
                AimApplication.session_cbc = new CurrentBoardComposer();
                add(AimApplication.session_cbc.mainLayout);
                Notification.show("Reload page view - boards");
                break;
            }
        }
    }

    /**
     * Function for preparing components
     */
    void prepare_components(){
        pageHeader = new PageHeader();

        terminal_field = new TextField();
        terminal_field.setPrefixComponent(VaadinIcon.TERMINAL.create());
        terminal_field.setPlaceholder("type command...");
        terminal_field.setWidth("50%");
        terminal_field.addClassName("aim-inputfield-bright");

        terminal_field.addKeyPressListener(Key.ENTER, e->
        {
            AIMInputParser aip = new AIMInputParser(this);
            aip.setUserInput(terminal_field.getValue());
            aip.parse();
            terminal_field.setValue("");
        });
    }

    /**
     * Function for preparing view and components
     */
    void prepare_view(){
        if (AimApplication.loggedUser != null){
            // loading selected object on main page
            loadMainPageComponent(0);
        }
        else{
            // user not logged
            Notification.show("User not logged!");
            add(new H6("User not logged!"));
            add(new H6("aim"));
        }
    }

    //--button action functions

    /**
     * taskview_button action
     * @param ex
     */
    private void taskviewbutton_action(ClickEvent ex){
        loadMainPageComponent(0);
    }

    private void projectviewbutton_action(ClickEvent ex){
        loadMainPageComponent(1);
    }

    private void boardviewbutton_action(ClickEvent ex){
        loadMainPageComponent(2);
    }

    private void floatingbutton_action(ClickEvent ex){
        FloatingWindow fw = new FloatingWindow();
        add(fw.main_dialog);
        fw.main_dialog.open();
    }

}
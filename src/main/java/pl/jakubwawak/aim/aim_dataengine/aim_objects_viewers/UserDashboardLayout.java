/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.atmosphere.interceptor.AtmosphereResourceStateRecovery;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Task;
import pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers.aim_objects_viewers_task.AIM_TaskLayout;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMTask;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_UserTelemetry;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Layout for storing user dashboard data
 */
public class UserDashboardLayout {

    public VerticalLayout main_dashboard_layout;
    Database_UserTelemetry database_telemetry;

    HorizontalLayout headerLayout;
    VerticalLayout percentLayout;
    VerticalLayout dataLayout;

    VerticalLayout center_layout;

    Button getrandomtask_button;
    ArrayList<AIM_TaskLayout> taskContentData;

    int mode;

    /**
     * Constructor
     */
    public UserDashboardLayout(int mode){
        this.mode = mode;
        main_dashboard_layout = new VerticalLayout();
        database_telemetry = new Database_UserTelemetry(AimApplication.database);
        prepareLayout();
    }

    /**
     * Function for preparing components
     */
    void prepareComponents(){

        headerLayout = new HorizontalLayout();
        FlexLayout left_layout = new FlexLayout();
        left_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        left_layout.setAlignItems(FlexComponent.Alignment.CENTER);
        left_layout.setWidth("100%");
        left_layout.add(new H5("Welcome back, "+AimApplication.loggedUser.aim_user_email));

        FlexLayout right_layout = new FlexLayout();
        right_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        right_layout.setAlignItems(FlexComponent.Alignment.CENTER);
        right_layout.setWidth("100%");

        center_layout = new VerticalLayout();
        center_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        center_layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        center_layout.getStyle().set("text-align", "center");

        VerticalLayout nestedCenterLayout = new VerticalLayout();
        nestedCenterLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        nestedCenterLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        nestedCenterLayout.getStyle().set("text-align", "center");

        ArrayList<Integer> databaseUserObjectData = database_telemetry.userObjectStatistic();

        right_layout.add(new H6(databaseUserObjectData.get(0)+" project(s) and "+databaseUserObjectData.get(1)+" board(s)"));

        headerLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        headerLayout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        headerLayout.getStyle().set("background-image","radial-gradient(#eeaeca, #94bbe9)");

        headerLayout.getStyle().set("text-align", "center");
        headerLayout.getStyle().set("border-radius","25px");
        headerLayout.getStyle().set("--lumo-font-family","Monospace");
        headerLayout.setSizeFull();

        percentLayout = new VerticalLayout();
        percentLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        percentLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        percentLayout.getStyle().set("text-align", "center");
        percentLayout.getStyle().set("background-image","linear-gradient(#e79268, #b156d5)");
        percentLayout.getStyle().set("border-radius","25px");
        percentLayout.getStyle().set("--lumo-font-family","Monospace");

        percentLayout.add(new H1(database_telemetry.calculateTaskPercent()));
        percentLayout.add(new H6("OPENED TASKS"));

        dataLayout = new VerticalLayout();
        dataLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        dataLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        dataLayout.getStyle().set("text-align", "center");
        dataLayout.getStyle().set("background-image","linear-gradient(#e79268, #b156d5)");
        dataLayout.getStyle().set("border-radius","25px");
        dataLayout.getStyle().set("--lumo-font-family","Monospace");

        ArrayList<Integer> taskStatistics = database_telemetry.userTaskStatistic();

        dataLayout.add(new H6("ALL TASKS: "+taskStatistics.get(0)));
        dataLayout.add(new H6("OPENED TASKS: "+taskStatistics.get(1)));
        dataLayout.add(new H6("TASKS IN BOARD: "+taskStatistics.get(2)));
        dataLayout.add(new H6("TASKS IN PROJECTS: "+taskStatistics.get(3)));

        Database_AIMTask dat = new Database_AIMTask(AimApplication.database);

        taskContentData = new ArrayList<>();
        for(AIM_Task task : dat.getTaskCollection()){
            taskContentData.add(new AIM_TaskLayout(task));
        }

        if ( taskContentData.size() > 0)
            headerLayout.add(left_layout,nestedCenterLayout,right_layout);
        else{
            headerLayout.add(left_layout,new H6("NO TASKS TO SHOW"),right_layout);
        }

        if (taskContentData.size() > 0){
            int randomIndex = ThreadLocalRandom.current().nextInt(0, taskContentData.size());
            center_layout.add(new H6("LET'S MAKE SOMETHING TOGETHER"));
            center_layout.add(taskContentData.get(randomIndex).main_layout);
        }

        getrandomtask_button = new Button("FIND OTHER", VaadinIcon.REFRESH.create(),this::getrandomtaskbutton_action);
        getrandomtask_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);
        nestedCenterLayout.add(center_layout,getrandomtask_button);
    }

    /**
     * Function for preparing layout
     */
    void prepareLayout(){
        prepareComponents();
        main_dashboard_layout.add(headerLayout);
        HorizontalLayout midLayout = new HorizontalLayout(percentLayout,dataLayout);
        midLayout.setWidth("100%");midLayout.setHeight("50%");
        main_dashboard_layout.add(midLayout);
        main_dashboard_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        main_dashboard_layout.setWidth("80%");
        main_dashboard_layout.setHeight("50%");
        main_dashboard_layout.getStyle().set("text-align", "center");
        main_dashboard_layout.getStyle().set("border-radius","25px");
        main_dashboard_layout.getStyle().set("margin","75px");
        if ( mode == 1 ){
            main_dashboard_layout.getStyle().set("background-color","black");
        }
        main_dashboard_layout.getStyle().set("--lumo-font-family","Monospace");
    }

    /**
     * getrandomtask_button action
     * @param ex
     */
    private void getrandomtaskbutton_action(ClickEvent ex){
        center_layout.removeAll();
        int randomIndex = ThreadLocalRandom.current().nextInt(0, taskContentData.size());
        center_layout.add(new H6("LET'S MAKE SOMETHING TOGETHER"));
        center_layout.add(taskContentData.get(randomIndex).main_layout);
        center_layout.add(getrandomtask_button);
    }
}

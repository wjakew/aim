/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers;

import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_UserTelemetry;

import java.util.ArrayList;

/**
 * Layout for storing user dashboard data
 */
public class UserDashboardLayout {

    public HorizontalLayout main_dashboard_layout;
    Database_UserTelemetry database_telemetry;

    HorizontalLayout headerLayout;

    /**
     * Constructor
     */
    public UserDashboardLayout(){
        main_dashboard_layout = new HorizontalLayout();
        database_telemetry = new Database_UserTelemetry(AimApplication.database);
        prepareLayout();
    }

    /**
     * Function for preparing components
     */
    void prepareComponents(){

        headerLayout = new HorizontalLayout();
        FlexLayout left_layout = new FlexLayout();
        left_layout.setWidth("100%");
        left_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        left_layout.setAlignItems(FlexComponent.Alignment.START);
        left_layout.add(new H5("Welcome back, "+AimApplication.loggedUser.aim_user_email));

        FlexLayout right_layout = new FlexLayout();
        right_layout.setWidth("100%");
        right_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        right_layout.setAlignItems(FlexComponent.Alignment.END);

        ArrayList<Integer> databaseUserObjectData = database_telemetry.userObjectStatistic();

        right_layout.add(new H6(databaseUserObjectData.get(0)+" project(s) and "+databaseUserObjectData.get(1)+" board(s)"));

        headerLayout.add(left_layout,right_layout);
        headerLayout.setWidth("100%");headerLayout.setHeight("10%");
        headerLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        headerLayout.getStyle().set("background-image","radial-gradient(#eeaeca, #94bbe9)");
        headerLayout.getStyle().set("text-align", "center");
        headerLayout.getStyle().set("border-radius","25px");
        headerLayout.getStyle().set("margin","75px");
        headerLayout.getStyle().set("--lumo-font-family","Monospace");

    }

    /**
     * Function for preparing layout
     */
    void prepareLayout(){
        prepareComponents();
        main_dashboard_layout.add(headerLayout);
        main_dashboard_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        main_dashboard_layout.setWidth("80%");
        main_dashboard_layout.setHeight("50%");
        main_dashboard_layout.getStyle().set("text-align", "center");
        main_dashboard_layout.getStyle().set("border-radius","25px");
        main_dashboard_layout.getStyle().set("margin","75px");
        main_dashboard_layout.getStyle().set("background-color","black");
        main_dashboard_layout.getStyle().set("--lumo-font-family","Monospace");
    }
}

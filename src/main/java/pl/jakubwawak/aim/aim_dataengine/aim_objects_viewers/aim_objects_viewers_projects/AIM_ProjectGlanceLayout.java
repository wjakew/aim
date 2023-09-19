/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers.aim_objects_viewers_projects;

import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Project;

/**
 * Object for showing project data in layout
 */
public class AIM_ProjectGlanceLayout {

    AIM_Project projectToView;
    public VerticalLayout main_layout;

    boolean selected;
    H6 selected_header;

    /**
     * Constructor
     */
    public AIM_ProjectGlanceLayout(AIM_Project projectToView){
        this.projectToView = projectToView;
        main_layout = new VerticalLayout();
        selected = false;
        selected_header = new H6("CURRENT");
        prepareProjectLayout();

        main_layout.addClickListener(e->{
            selected_header.setVisible(true);
            setSelected();
        });
    }

    /**
     * Function for preparing project layout
     */
    void prepareProjectLayout(){
        main_layout.add(new H6(projectToView.aim_project_name));
        main_layout.add(selected_header);
        main_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        main_layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        main_layout.getStyle().set("text-align", "center");
        main_layout.setWidth("100%");
        main_layout.getStyle().set("text-align", "center");
        main_layout.getStyle().set("border-radius","25px");
        main_layout.getStyle().set("background-color","black");
        main_layout.getStyle().set("color","#FFFFFF");
        main_layout.getStyle().set("--lumo-font-family","Monospace");
        selected_header.setVisible(false);
    }

    /**
     * Function for changing selection of the project glance layout
     */
    public void setSelected(){
        selected = true;
        main_layout.getStyle().set("background-color","green");
    }

    /**
     * Function for removing selection
     */
    public void unSelect(){
        selected = false;
        main_layout.getStyle().set("background-color","black");
        selected_header.setVisible(false);
    }


}

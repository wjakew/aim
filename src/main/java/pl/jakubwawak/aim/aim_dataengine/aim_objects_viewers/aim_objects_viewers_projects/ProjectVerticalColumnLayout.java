/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers.aim_objects_viewers_projects;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Project;

import java.util.ArrayList;

/**
 * Object for creating layout that contains user project in vertical layout
 */
public class ProjectVerticalColumnLayout {

    ArrayList<AIM_Project> projectContent;
    ArrayList<ProjectVerticalColumnPage> projectPages;

    public VerticalLayout projectVerticalLayout;

    Button next_button, previous_button;

    int currentPageNumber;

    public ProjectVerticalColumnLayout(ArrayList<AIM_Project> projectContent, VerticalLayout parent){
        this.projectContent = projectContent;
        projectVerticalLayout = new VerticalLayout();
        projectPages = new ArrayList<>();

        previous_button = new Button("", VaadinIcon.ARROW_LEFT.create(),this::previousbutton_action);
        previous_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);
        next_button = new Button("",VaadinIcon.ARROW_RIGHT.create(),this::nextbutton_action);
        next_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);

        prepareLayout();

        projectVerticalLayout.setSizeFull();
        projectVerticalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        projectVerticalLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        projectVerticalLayout.getStyle().set("text-align", "center");
    }

    /**
     * Function for loading layouts
     */
    void reloadProjectContent(){
        int page = 1;
        int flag = 0;
        ProjectVerticalColumnPage phcp = new ProjectVerticalColumnPage(page);
        for(AIM_Project project : projectContent){
            int ans = phcp.addProject(project);
            flag = 0;
            if ( ans == 3 ){
                projectPages.add(phcp);
                page++;
                phcp = new ProjectVerticalColumnPage(page);
                phcp.addProject(project);
                flag = 1;
            }
        }
        if ( flag != 1 ){
            projectPages.add(phcp);
            phcp = new ProjectVerticalColumnPage(99);
        }
        if (!phcp.isEmpty()){
            projectPages.add(phcp);
        }
    }

    /**
     * Function for loading page in Layout
     * @param pageNumber
     */
    void loadPage(int pageNumber){
        if ( pageNumber <= projectPages.size() ){
            projectVerticalLayout.removeAll();
            projectVerticalLayout.add(new H6("YOUR PROJECTS"));
            ProjectVerticalColumnPage currentphcp = projectPages.get(pageNumber);
            if (!currentphcp.isEmpty()){
                addProjectToLayout(currentphcp.projectLayout1);
                addProjectToLayout(currentphcp.projectLayout2);
                if ( pageNumber == 0 ){
                    previous_button.setEnabled(false);
                }
                else{
                    previous_button.setEnabled(true);
                }
                if (pageNumber < projectPages.size()-1){
                    next_button.setEnabled(true);
                }
                else{
                    next_button.setEnabled(false);
                }
            }
            else{
                projectVerticalLayout.add(new H6("No projects!"));
                previous_button.setEnabled(false);
                next_button.setEnabled(false);
                currentPageNumber = 0;
            }
        }
        else{
            projectVerticalLayout.add(new H6("No projects!"));
        }
        projectVerticalLayout.add(new HorizontalLayout(previous_button,next_button));
    }

    /**
     * Function for adding project layout to horizontal column
     * @param projectLayoutToAdd
     */
    void addProjectToLayout(AIM_ProjectGlanceLayoutExtended projectLayoutToAdd){
        if (projectLayoutToAdd != null)
            projectVerticalLayout.add(projectLayoutToAdd.main_layout);
    }

    /**
     * Function for preparing layout
     */
    void prepareLayout(){
        reloadProjectContent();
        loadPage(currentPageNumber);
    }

    /**
     * previous_button action
     * @param ex
     */
    private void previousbutton_action(ClickEvent ex){
        currentPageNumber--;
        loadPage(currentPageNumber);
    }

    /**
     * previous_button action
     * @param ex
     */
    private void nextbutton_action(ClickEvent ex){
        currentPageNumber++;
        loadPage(currentPageNumber);
    }
}

/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers.aim_objects_viewers_projects;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Project;
import java.util.ArrayList;

/**
 * Object for showing project horizontal layout
 */
public class ProjectHorizontalColumnLayout {

    ArrayList<AIM_Project> projectContent;

    ArrayList<ProjectHorizontalColumnPage> projectPages;

    public HorizontalLayout projectHorizontalColumnLayout;

    Button next_button, previous_button;

    int currentPageNumber;

    /**
     * Constructor
     * @param projectContent
     */
    public ProjectHorizontalColumnLayout(ArrayList<AIM_Project> projectContent,CurrentProjectComposer cpc){
        this.projectContent = projectContent;
        projectHorizontalColumnLayout = new HorizontalLayout();
        projectPages = new ArrayList<>();
        currentPageNumber = 0;

        previous_button = new Button("", VaadinIcon.ARROW_LEFT.create(),this::previousbutton_action);
        next_button = new Button("",VaadinIcon.ARROW_RIGHT.create(),this::nextbutton_action);
        prepareLayout();

        projectHorizontalColumnLayout.setWidth("100%");
        projectHorizontalColumnLayout.setMargin(true);
        projectHorizontalColumnLayout.getStyle().set("background-color","gray");
        projectHorizontalColumnLayout.getStyle().set("color","black");
        projectHorizontalColumnLayout.getStyle().set("border-radius","15px");

        projectHorizontalColumnLayout.setMargin(true);
        projectHorizontalColumnLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        // engine for selecting projects in horizontal layout
        projectHorizontalColumnLayout.addClickListener(e->{
            for(ProjectHorizontalColumnPage phcl : projectPages){
                AIM_ProjectGlanceLayout selectedProjectLayout = phcl.getSelectedProject();
                if ( selectedProjectLayout != null ){
                    Notification.show("Selected "+selectedProjectLayout.projectToView.aim_project_id);
                    // load project layout
                    cpc.updateLayout(new AIM_ProjectLayout(selectedProjectLayout.projectToView));
                    break;
                }
            }
            // removing selection
            for(ProjectHorizontalColumnPage phcl : projectPages){
                phcl.removeSelection();
            }
        });
    }

    /**
     * Function for loading layouts
     */
    void reloadProjectContent(){
        int page = 1;
        int flag = 0;
        ProjectHorizontalColumnPage phcp = new ProjectHorizontalColumnPage(page);
        for(AIM_Project project : projectContent){
            int ans = phcp.addProject(project);
            flag = 0;
            if ( ans == 3 ){
                projectPages.add(phcp);
                page++;
                phcp = new ProjectHorizontalColumnPage(page);
                phcp.addProject(project);
                flag = 1;
            }
        }
        if ( flag != 1 ){
            projectPages.add(phcp);
            phcp = new ProjectHorizontalColumnPage(99);
        }
        if (!phcp.isEmpty()){
            projectPages.add(phcp);
        }
    }

    /**
     * Function for adding project layout to horizontal column
     * @param projectLayoutToAdd
     */
    void addProjectToLayout(AIM_ProjectGlanceLayout projectLayoutToAdd){
        if (projectLayoutToAdd != null)
            projectHorizontalColumnLayout.add(projectLayoutToAdd.main_layout);
    }

    /**
     * Function for loading page in Layout
     * @param pageNumber
     */
    void loadPage(int pageNumber){
        if ( pageNumber <= projectPages.size() ){
            projectHorizontalColumnLayout.removeAll();
            ProjectHorizontalColumnPage currentphcp = projectPages.get(pageNumber);
            projectHorizontalColumnLayout.add(previous_button);
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
                projectHorizontalColumnLayout.add(new H6("No projects!"));
                currentPageNumber = 0;
            }
        }
        else{
            projectHorizontalColumnLayout.add(new H6("No projects!"));
        }
        projectHorizontalColumnLayout.add(next_button);
    }

    /**
     * Function for preparing layout
     */
    void prepareLayout(){
        reloadProjectContent();
        loadPage(currentPageNumber);
    }

    /**
     * Function for loading default layout of first data
     * @return
     */
    public VerticalLayout getDefaultLayout(){
        if ( projectContent.size() > 0 ){
            AIM_ProjectLayout apl = new AIM_ProjectLayout(projectContent.get(0));
            return apl.projectLayout;
        }
        else{
            VerticalLayout hl = new VerticalLayout();
            hl.add(new H6("NO PROJECT DATA"));
            return hl;
        }
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

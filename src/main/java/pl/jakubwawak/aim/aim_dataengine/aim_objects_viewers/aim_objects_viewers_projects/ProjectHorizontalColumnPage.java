/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers.aim_objects_viewers_projects;

import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Project;

/**
 * Object for creating horizontal column page with projects
 */
public class ProjectHorizontalColumnPage {
    public AIM_ProjectGlanceLayout projectLayout1;
    public AIM_ProjectGlanceLayout projectLayout2;

    public int pageIndex;

    /**
     * Constructor
     */
    public ProjectHorizontalColumnPage(int pageIndex){
        this.pageIndex = pageIndex;
        projectLayout1 = null;
        projectLayout2 = null;
    }

    /**
     * Function for adding project to horizontal column
     * @return Integer
     */
    public int addProject(AIM_Project projectToAdd){
        if ( projectLayout1 == null){
            projectLayout1 = new AIM_ProjectGlanceLayout(projectToAdd);
            return 1;
        }
        else if ( projectLayout2 == null ){
            projectLayout2 = new AIM_ProjectGlanceLayout(projectToAdd);
            return 2;
        }
        return 3;
    }

    /**
     * Function for checking if column page is empty
     * @return boolean
     */
    public boolean isEmpty(){
        return projectLayout1 == null && projectLayout2 == null;
    }

    /**
     * Function for returning selected component
     * @return AIM_ProjectLayout
     */
    public AIM_ProjectGlanceLayout getSelectedProject() {
        if (projectLayout1.selected) {
            return projectLayout1;
        } else if (projectLayout2 != null && projectLayout2.selected) {
            return projectLayout2;
        }
        return null;
    }

    /**
     * Function for removing selection from page components
     */
    public void removeSelection(){
        try{
            projectLayout1.selected = false;
            projectLayout2.selected = false;
        }catch(Exception ex){}
    }
}

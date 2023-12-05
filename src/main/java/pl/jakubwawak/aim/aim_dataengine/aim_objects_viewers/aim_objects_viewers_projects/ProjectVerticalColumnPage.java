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
public class ProjectVerticalColumnPage {
    public AIM_ProjectGlanceLayoutExtended projectLayout1;
    public AIM_ProjectGlanceLayoutExtended projectLayout2;

    public int pageIndex;

    /**
     * Constructor
     */
    public ProjectVerticalColumnPage(int pageIndex){
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
            projectLayout1 = new AIM_ProjectGlanceLayoutExtended(projectToAdd);
            return 1;
        }
        else if ( projectLayout2 == null ){
            projectLayout2 = new AIM_ProjectGlanceLayoutExtended(projectToAdd);
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
     * Function for removing selection from page components
     */
    public void removeSelection(){
        try{
            projectLayout1.unSelect();
            projectLayout2.unSelect();
        }catch(Exception ex){}
    }
}

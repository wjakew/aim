/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package pl.jakubwawak.aim.website_ui.views;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.Lumo;
import org.atmosphere.interceptor.AtmosphereResourceStateRecovery;
import org.bson.Document;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Project;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Task;
import pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers.aim_objects_viewers_projects.AIM_ProjectLayoutShare;
import pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers.aim_objects_viewers_task.AIM_TaskLayoutShare;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMProject;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMTask;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_ShareCode;
import pl.jakubwawak.aim.website_ui.dialog_windows.AddElementWindow;
import pl.jakubwawak.aim.website_ui.dialog_windows.UserWindow;
import pl.jakubwawak.aim.website_ui.style.ButtonStyler;
import pl.jakubwawak.aim.website_ui.views.WelcomeView;

/**
 * Object for showing welcome view
 */
@PageTitle("Aim Object Viewer")
@Route(value = "/object-viewer")
public class ObjectViewerView extends VerticalLayout implements HasUrlParameter<String> {

    /**
     * Constructor
     */
    public ObjectViewerView(){
        this.getElement().setAttribute("theme", Lumo.LIGHT);
        prepare_view();

        setSizeFull();
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

    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String parameter) {
        if ( parameter == null ){
            StreamResource res = new StreamResource("aim_logo.png", () -> {
                return WelcomeView.class.getClassLoader().getResourceAsStream("images/aim_logo.png");
            });

            Image logo = new Image(res,"aim logo");
            logo.setHeight("5rem");
            logo.setWidth("5rem");
            add(logo);
            add(new H6("NO SHARING CODE PROVIDED!"));
        }
        else{
            Database_ShareCode dsc = new Database_ShareCode(AimApplication.database);
            Document sharingDocument = dsc.getShareCode(parameter);
            if (sharingDocument == null){
                // nothing to show - sharing code is wrong
                StreamResource res = new StreamResource("aim_logo.png", () -> {
                    return WelcomeView.class.getClassLoader().getResourceAsStream("images/aim_logo.png");
                });

                Image logo = new Image(res,"aim logo");
                logo.setHeight("5rem");
                logo.setWidth("5rem");
                add(logo);
                add(new H6(parameter));
                add(new H6("NOTHING FOUND - WRONG SHARING CODE"));
            }
            else{
                StreamResource res = new StreamResource("aim_logo.png", () -> {
                    return WelcomeView.class.getClassLoader().getResourceAsStream("images/aim_logo.png");
                });

                Image logo = new Image(res,"aim logo");
                logo.setHeight("5rem");
                logo.setWidth("5rem");
                add(logo);
                HorizontalLayout hl_spacer = new HorizontalLayout();
                hl_spacer.setHeight("10%");
                // sharing code correct - add object viewer based on type
                switch(sharingDocument.getString("type")){
                    case "project":
                    {
                        // add project viewer to layout
                        Database_AIMProject dap = new Database_AIMProject(AimApplication.database);
                        AIM_Project project = dap.getProjectBySharedCode(parameter);
                        if ( project != null ){
                            add(new H6("PROJECT ID: "+project.aim_project_id.toString()));
                            add(new H6("OWNER: "+project.aim_owner.getString("aim_user_email")));
                            add(hl_spacer);
                            AIM_ProjectLayoutShare apls = new AIM_ProjectLayoutShare(project);
                            add(apls.layout);
                        }
                        else{
                            add(new H6("SHARED PROJECT IS NULL, CONTACT ADMINISTRATOR"));
                        }
                        break;
                    }
                    case "task":
                    {
                        // add task viewer to layout
                        Database_AIMTask dat = new Database_AIMTask(AimApplication.database);
                        AIM_Task task = dat.getTaskBySharedCode(parameter);
                        if (task != null){
                            add(new H6("TASK ID "+task.aim_task_id.toString()));
                            add(new H6("OWNER: "+task.aim_task_owner.getString("aim_user_email")));
                            add(hl_spacer);
                            AIM_TaskLayoutShare atls = new AIM_TaskLayoutShare(task);
                            add(atls.layout);
                        }
                        else{
                            add(new H6("SHARED TASK IS NULL, CONTACT ADMINISTRATOR"));
                        }
                        break;
                    }
                    default:
                    {
                        add(new H6(parameter));
                        add(new H6("WRONG SHARING TYPE, CHECK APPLICATION LOG"));
                        break;
                    }
                }
            }
        }
    }

    //--button action functions

}
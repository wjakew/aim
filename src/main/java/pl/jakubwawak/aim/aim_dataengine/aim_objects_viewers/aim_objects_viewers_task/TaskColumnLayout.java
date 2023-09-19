/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers.aim_objects_viewers_task;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Project;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Task;

import java.util.ArrayList;

/**
 * Object for creating task column
 */
public class TaskColumnLayout {
    public VerticalLayout columnLayout;
    ArrayList<AIM_Task> taskCollection;

    ArrayList<TaskColumnPage> taskContent;
    String hexColor;
    String header;

    Button next_button, previous_button;
    String width,height;

    int currentPageNumber;
    AIM_Project projectLinked;

    /**
     * Constructor
     * @param taskCollection
     */
    public TaskColumnLayout(ArrayList<AIM_Task> taskCollection, String hexColor, String header, AIM_Project projectLinked,String width,String height){
        this.taskCollection = taskCollection;
        this.hexColor = hexColor;
        columnLayout = new VerticalLayout();
        taskContent = new ArrayList<>();
        this.header = header;
        this.projectLinked = projectLinked;
        currentPageNumber = 0;
        this.width = width;
        this.height = height;

        previous_button = new Button("", VaadinIcon.ARROW_LEFT.create(),this::previousbutton_action);
        next_button = new Button("",VaadinIcon.ARROW_RIGHT.create(),this::nextbutton_action);

        prepareLayout();
    }

    /**
     * Function for preparing layout
     */
    void prepareLayout(){
        if ( width.isEmpty() && height.isEmpty()){
            columnLayout.setWidth("30%");
            columnLayout.setHeight("85%");
        }
        else{
            columnLayout.setWidth(width);
            columnLayout.setHeight(height);
        }
        columnLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        columnLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        columnLayout.getStyle().set("text-align", "center");
        columnLayout.getStyle().set("border-radius","25px");
        columnLayout.getStyle().set("margin","25px");
        columnLayout.getStyle().set("background-color",hexColor);
        columnLayout.getStyle().set("--lumo-font-family","Monospace");

        reloadTaskContent(); // reloading task content

        // adding taskPage to layout
        loadPage(currentPageNumber);

    }

    /**
     * Function for reloading task content
     */
    void reloadTaskContent(){
        int page = 1;
        int flag = 0;
        TaskColumnPage tcp = new TaskColumnPage(page);
        for(AIM_Task task : taskCollection){
            flag = 0;
            int ans = 5;
            if ( projectLinked != null ){
                ans = tcp.addTask(task,projectLinked);
            }
            else{
                ans = tcp.addTask(task);
            }

            if ( ans == 5 ){
                taskContent.add(tcp);
                page++;
                tcp = new TaskColumnPage(page);
                tcp.addTask(task);
                flag = 1;
            }
        }
        if ( flag != 1) {
            taskContent.add(tcp);
            tcp = new TaskColumnPage(99);
        }
        if ( !tcp.isEmpty() ){
            taskContent.add(tcp);
        }
    }

    void addTaskLayout(AIM_TaskLayout taskLayout){
        if ( taskLayout != null ){
            columnLayout.add(taskLayout.main_layout);
        }
    }

    /**
     * Function for loading pageNumber
     * @param pageNumber
     */
    void loadPage(int pageNumber){
        if ( pageNumber < taskContent.size()){
            columnLayout.removeAll();
            TaskColumnPage currentTCP = taskContent.get(pageNumber);
            if (!currentTCP.isEmpty()){
                columnLayout.add(new H6(header));
                addTaskLayout(currentTCP.taskObject1);
                addTaskLayout(currentTCP.taskObject2);
                addTaskLayout(currentTCP.taskObject3);
                addTaskLayout(currentTCP.taskObject4);
                columnLayout.add(new HorizontalLayout(previous_button,new H6(pageNumber+1 + "/"+taskContent.size()),next_button));

                if ( pageNumber == 0 ){
                    previous_button.setEnabled(false);
                }
                else{
                    previous_button.setEnabled(true);
                }
                if (pageNumber < taskContent.size()-1){
                    next_button.setEnabled(true);
                }
                else{
                    next_button.setEnabled(false);
                }
            }
            else{
                columnLayout.add(new H6("No tasks!"));
            }
        }
        else{
            columnLayout.add(new H6("No tasks!"));
        }
    }

    /**
     * Function for updating layout
     */
    public void update(ArrayList<AIM_Task> newTaskCollection){
        this.taskCollection.clear();
        this.taskCollection.addAll(newTaskCollection);
        reloadTaskContent();
        loadPage(0);
    }

    /**
     * next_button action
     * @param ex
     */
    private void nextbutton_action(ClickEvent ex){
        currentPageNumber++;
        loadPage(currentPageNumber);
    }

    private  void previousbutton_action(ClickEvent ex){
        currentPageNumber--;
        loadPage(currentPageNumber);
    }
}

/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers.aim_objects_viewers_ctask;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.codingproject.AIM_CodingTask;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMCodingTask;
import pl.jakubwawak.aim.website_ui.dialog_windows.obiect_windows.coding_task_windows.InsertCTaskWindow;

import java.util.ArrayList;

/**
 * Object for creating layout for showing all coding tasks
 */
public class AIM_CTaskListViewer {
    public HorizontalLayout mainLayout;

    Button createnewtask_button;
    Grid<AIM_CodingTask> grid;

    ArrayList<AIM_CodingTask> gridContent;
    AIM_CTaskViewer taskViewer;
    Database_AIMCodingTask dact;

    VerticalLayout rightLayout;

    /**
     * Constructor
     */
    public AIM_CTaskListViewer(){
        mainLayout = new HorizontalLayout();
        mainLayout.addClassName("home-view");
        gridContent = new ArrayList<>();
        dact = new Database_AIMCodingTask(AimApplication.database);
        prepareLayout();
    }

    /**
     * Function for preparing components
     */
    void prepareComponents(){
        createnewtask_button = new Button("Create new CTask", VaadinIcon.PLUS.create(),this::setCreatenewtask_button);
        createnewtask_button.addClassName("aim-button-black");
        createnewtask_button.setWidth("100%");

        grid = new Grid<>(AIM_CodingTask.class,false);
        grid.addClassName("aim-grid");
        grid.addColumn(createCTaskRenderer()).setHeader("Task");
        grid.setSizeFull();
        gridContent = dact.getCodingTaskList();
        grid.setItems(gridContent);

        grid.addItemClickListener(event -> {
            AIM_CodingTask act = event.getItem();
            if ( act != null ){
                AIM_CTaskViewer taskViewer1 = new AIM_CTaskViewer(act);
                rightLayout.removeAll();
                taskViewer = taskViewer1;
                rightLayout.add(taskViewer.ctaskviewer_layout);
            }
        });
    }

    /**
     * Renderer for aim coding object
     * @return Renderer
     */
    private static Renderer<AIM_CodingTask> createCTaskRenderer() {
        return LitRenderer.<AIM_CodingTask> of(
                        "<vaadin-horizontal-layout style=\"align-items: center;\" theme=\"spacing\">"
                                + "  <vaadin-vertical-layout style=\"line-height: var(--lumo-line-height-m);\">"
                                + "    <span> ${item.fullName} </span>"
                                + "    <span style=\"font-size: var(--lumo-font-size-s); color: var(--lumo-secondary-text-color);\">"
                                + "      ${item.email}" + "    </span>"
                                + "  </vaadin-vertical-layout>"
                                + "</vaadin-horizontal-layout>")
                .withProperty("fullName", AIM_CodingTask::getAim_codingtask_name)
                .withProperty("email", AIM_CodingTask::getCodingTaskData);
    }

    /**
     * Function for updating grid
     */
    public void updateGrid(){
        gridContent.clear();
        gridContent.addAll(dact.getCodingTaskList());
        grid.getDataProvider().refreshAll();
    }

    /**
     * Function for preparing layout
     */
    void prepareLayout(){
        prepareComponents();
        mainLayout.setSizeFull();

        VerticalLayout leftLayout;
        leftLayout = new VerticalLayout(); rightLayout = new VerticalLayout();

        leftLayout.setHeight("100%");leftLayout.setWidth("40%");
        leftLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        leftLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        leftLayout.getStyle().set("text-align", "center");

        leftLayout.add(createnewtask_button);
        leftLayout.add(grid);

        rightLayout.setSizeFull();
        rightLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        rightLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        rightLayout.getStyle().set("text-align", "center");

        taskViewer = new AIM_CTaskViewer(gridContent.get(0));

        rightLayout.add(taskViewer.ctaskviewer_layout);

        mainLayout.add(leftLayout,rightLayout);
    }

    /**
     * createnewtask_button action
     * @param ex
     */
    private void setCreatenewtask_button(ClickEvent ex){
        InsertCTaskWindow icw = new InsertCTaskWindow(null,this);
        mainLayout.add(icw.main_dialog);
        icw.main_dialog.open();
    }

}

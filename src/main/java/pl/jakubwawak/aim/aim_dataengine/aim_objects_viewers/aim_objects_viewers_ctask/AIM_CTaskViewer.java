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
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.function.SerializableBiConsumer;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.codingproject.AIM_CodingTask;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMCodingTask;
import pl.jakubwawak.aim.website_ui.dialog_windows.obiect_windows.coding_task_windows.InsertCTaskWindow;

import java.util.ArrayList;

/**
 * Object for creating layout for showing all coding tasks
 */
public class AIM_CTaskViewer {
    public HorizontalLayout mainLayout;

    Button createnewtask_button;
    Grid<AIM_CodingTask> grid;

    ArrayList<AIM_CodingTask> gridContent;
    Database_AIMCodingTask dact;

    /**
     * Constructor
     */
    public AIM_CTaskViewer(){
        mainLayout = new HorizontalLayout();
        gridContent = new ArrayList<>();
        dact = new Database_AIMCodingTask(AimApplication.database);
        prepareLayout();
    }

    /**
     * Function for preparing components
     */
    void prepareComponents(){
        createnewtask_button = new Button("Create new CTask", VaadinIcon.PLUS.create(),this::setCreatenewtask_button);
        createnewtask_button.addThemeVariants(ButtonVariant.LUMO_SUCCESS,ButtonVariant.LUMO_PRIMARY);
        createnewtask_button.setWidth("100%");

        grid = new Grid<>(AIM_CodingTask.class,false);
        grid.addColumn(createCTaskRenderer()).setHeader("Task").setAutoWidth(true).setFlexGrow(0);
        grid.setSizeFull();
        gridContent = dact.getCodingTaskList();
        grid.setItems(gridContent);
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

        VerticalLayout leftLayout,rightLayout;
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

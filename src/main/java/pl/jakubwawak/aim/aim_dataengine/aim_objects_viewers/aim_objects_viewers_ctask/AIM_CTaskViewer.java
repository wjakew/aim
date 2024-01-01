/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers.aim_objects_viewers_ctask;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.codingproject.AIM_CodingTask;
import pl.jakubwawak.maintanance.GridElement;

/**
 * Object for viewing and editing single coding task in layout
 */
public class AIM_CTaskViewer {

    public VerticalLayout ctaskviewer_layout;

    HorizontalLayout header_layout;
    ComboBox<GridElement> status_combobox;

    public AIM_CodingTask act;

    /**
     * Object for creating view
     */
    public AIM_CTaskViewer(AIM_CodingTask act){
        ctaskviewer_layout = new VerticalLayout();
        prepareLayout();
    }

    /**
     * Function for preparing components
     */
    void prepareComponents(){
        header_layout = new HorizontalLayout();
        header_layout.setWidth("100%");


        status_combobox = new ComboBox<>();
        status_combobox.setWidth("30%");
        status_combobox

        FlexLayout left_layout = new FlexLayout();
        left_layout.setSizeFull();
        left_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        left_layout.setAlignItems(FlexComponent.Alignment.CENTER);
        left_layout.setWidth("80%");
        left_layout.add(new H3(act.aim_codingtask_name));

        FlexLayout right_layout = new FlexLayout();
        right_layout.setSizeFull();
        right_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        right_layout.setAlignItems(FlexComponent.Alignment.END);
        right_layout.add(logout_button);
        right_layout.setWidth("80%");
    }

    /**
     * Function for preparing layout
     */
    void prepareLayout(){
        prepareComponents();
    }
}

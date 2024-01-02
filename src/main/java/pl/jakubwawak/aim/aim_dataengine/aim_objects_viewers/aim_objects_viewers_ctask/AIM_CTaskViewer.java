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

import java.util.ArrayList;

/**
 * Object for viewing and editing single coding task in layout
 */
public class AIM_CTaskViewer {

    public VerticalLayout ctaskviewer_layout;

    HorizontalLayout header_layout;
    HorizontalLayout center_layout;
    HorizontalLayout bottom_layout;
    ComboBox<GridElement> status_combobox;
    ArrayList<GridElement> combobox_content;

    public AIM_CodingTask act;

    /**
     * Object for creating view
     */
    public AIM_CTaskViewer(AIM_CodingTask act){
        this.act = act;
        ctaskviewer_layout = new VerticalLayout();
        prepareLayout();
    }

    /**
     * Function for preparing components
     */
    void prepareComponents(){
        header_layout = new HorizontalLayout();
        header_layout.setWidth("100%");

        center_layout = new HorizontalLayout();
        center_layout.setWidth("100%");
        center_layout.setHeight("100%");

        bottom_layout = new HorizontalLayout();
        bottom_layout.setWidth("100%");
        bottom_layout.setHeight("100%");

        combobox_content = new ArrayList<>();
        combobox_content.add(new GridElement("NEW"));
        combobox_content.add(new GridElement("IN PROGRESS"));
        combobox_content.add(new GridElement("FROZEN"));
        combobox_content.add(new GridElement("DONE"));

        status_combobox = new ComboBox<>();
        status_combobox.setAllowCustomValue(false);
        status_combobox.setWidth("100%");
        status_combobox.setItems(combobox_content);
        status_combobox.setItemLabelGenerator(GridElement::getGridelement_text);
        status_combobox.setValue(new GridElement(act.aim_codingtask_status));

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
        right_layout.add(status_combobox);
        right_layout.setWidth("80%");

        header_layout.add(left_layout,right_layout);
    }

    /**
     * Function for preparing layout
     */
    void prepareLayout(){
        prepareComponents();

        ctaskviewer_layout.add(header_layout);
        ctaskviewer_layout.add(center_layout,bottom_layout);

        ctaskviewer_layout.setSizeFull();
        ctaskviewer_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        ctaskviewer_layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        ctaskviewer_layout.getStyle().set("text-align", "center");
        ctaskviewer_layout.getStyle().set("background-color","black");

    }
}

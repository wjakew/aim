/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers.aim_objects_viewers_ctask;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import org.bson.Document;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.codingproject.AIM_CodingTask;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMCodingTask;
import pl.jakubwawak.aim.website_ui.dialog_windows.obiect_windows.coding_task_windows.AddCommentCTaskWindow;
import pl.jakubwawak.aim.website_ui.dialog_windows.obiect_windows.coding_task_windows.InsertCTaskWindow;
import pl.jakubwawak.aim.website_ui.dialog_windows.obiect_windows.coding_task_windows.ShowFullHistoryWindow;
import pl.jakubwawak.maintanance.GridElement;

import javax.swing.text.html.HTMLDocument;
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
    Grid<GridElement> history_grid;
    Grid<GridElement> comments_grid;
    ArrayList<GridElement> comment_content;

    Button addcomment_button, update_button, showfullhistory_button,removetask_button; // TODO create actions for that buttons

    TextArea description_area;

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

        addcomment_button = new Button("Add Comment", VaadinIcon.COMMENT.create(),this::setAddcomment_button);
        addcomment_button.setWidth("100%"); addcomment_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);

        update_button = new Button("Update Task", VaadinIcon.REFRESH.create(),this::Setupdate_button);
        update_button.setWidth("100%"); update_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);

        showfullhistory_button = new Button("Show Full History", VaadinIcon.ARCHIVE.create(),this::Setshowfullhistory_button);
        showfullhistory_button.setWidth("100%"); showfullhistory_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);

        removetask_button = new Button("Send to trash!", VaadinIcon.TRASH.create());
        removetask_button.setWidth("100%"); removetask_button.addThemeVariants(ButtonVariant.LUMO_ERROR,ButtonVariant.LUMO_PRIMARY);

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

        status_combobox.addValueChangeListener(event -> {
            String value = status_combobox.getValue().getGridelement_text();
            if ( !value.equals(act.aim_codingtask_status) ){
                Database_AIMCodingTask dact = new Database_AIMCodingTask(AimApplication.database);
                int ans = dact.updateCodingTaskStatus(act,value);
                if ( ans == 1 ){
                    Notification.show("Updated status!");
                    act.aim_codingtask_status = value;
                }
            }
        });

        description_area = new TextArea("Coding Task Description");
        description_area.setSizeFull();
        description_area.setValue(act.aim_codingtask_desc);

        // create description update
        description_area.addBlurListener(event -> {
            //update descrytption on database
            String newDesc = description_area.getValue();
            if ( !newDesc.equals(act.aim_codingtask_desc) ){
                Database_AIMCodingTask dact = new Database_AIMCodingTask(AimApplication.database);
                int ans = dact.updateDescriptionTask(act,newDesc);
                if ( ans == 1 ){
                    Notification.show("Updated description!");
                    act.aim_codingtask_desc = newDesc;
                }
            }
        });

        history_grid = new Grid<>(GridElement.class,false);
        history_grid.addColumn(GridElement::getGridelement_details).setHeader("Category").setAutoWidth(true);
        history_grid.addColumn(GridElement::getGridelement_text).setHeader("Description").setAutoWidth(true);
        ArrayList<GridElement> historyContent = new ArrayList<>();
        for(Document document : act.aim_codingtask_history){
            GridElement element = new GridElement(document.getString("history_text"),document.getString("history_category"),document.getString("history_user"));
            historyContent.add(element);
        }
        history_grid.setItems(historyContent);
        history_grid.setSizeFull();

        comments_grid = new Grid<>(GridElement.class,false);
        comments_grid.addColumn(GridElement::getGridelement_text).setHeader("Comment");
        /*
        document layout
        comment_text: info
        comment_time: time
        comment_email: email
        */
        comment_content = new ArrayList<>();
        for(Document document : act.aim_codingtask_comments){
            comment_content.add(new GridElement(document.getString("comment_text"),document.getString("comment_email"),document.getString("comment_time")));
        }
        comments_grid.setItems(comment_content);
        comments_grid.setSizeFull();

        VerticalLayout right_vl_center = new VerticalLayout();
        right_vl_center.setSizeFull();
        right_vl_center.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        right_vl_center.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        right_vl_center.getStyle().set("text-align", "center");
        right_vl_center.getStyle().set("background-color","black");

        right_vl_center.add(comments_grid,addcomment_button,update_button,showfullhistory_button);

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

        FlexLayout left_layout_center = new FlexLayout();
        left_layout_center.setSizeFull();
        left_layout_center.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        left_layout_center.setAlignItems(FlexComponent.Alignment.CENTER);
        left_layout_center.setWidth("50%");
        left_layout_center.add(history_grid);

        FlexLayout right_layout_center = new FlexLayout();
        right_layout_center.setSizeFull();
        right_layout_center.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        right_layout_center.setAlignItems(FlexComponent.Alignment.END);
        right_layout_center.add();
        right_layout_center.setWidth("50%");
        right_layout_center.add(right_vl_center);

        header_layout.add(left_layout,right_layout);

        // add components to layouts
        center_layout.add(left_layout_center,right_layout_center);
        bottom_layout.add(description_area);
    }

    /**
     * Function for reloading comment content
     */
    public void reloadCommentContent(){
        comment_content.clear();
        for(Document document : act.aim_codingtask_comments){
            comment_content.add(new GridElement(document.getString("comment_text"),document.getString("comment_email"),document.getString("comment_time")));
        }
        comments_grid.getDataProvider().refreshAll();
    }

    /**
     * Function for preparing layout
     */
    void prepareLayout(){
        prepareComponents();

        ctaskviewer_layout.add(header_layout);
        ctaskviewer_layout.add(center_layout,bottom_layout);
        ctaskviewer_layout.add(removetask_button);

        ctaskviewer_layout.setSizeFull();
        ctaskviewer_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        ctaskviewer_layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        ctaskviewer_layout.getStyle().set("text-align", "center");
        ctaskviewer_layout.getStyle().set("background-color","black");
    }

    /**
     * update_button action
     * @param ex
     */
    private void Setupdate_button(ClickEvent ex){
        InsertCTaskWindow insertCTaskWindow = new InsertCTaskWindow(act);
        ctaskviewer_layout.add(insertCTaskWindow.main_dialog);
        insertCTaskWindow.main_dialog.open();
    }

    /**
     * showfullhistory_button action
     * @param ex
     */
    private void Setshowfullhistory_button(ClickEvent ex){
        ShowFullHistoryWindow sfhw = new ShowFullHistoryWindow(act);
        ctaskviewer_layout.add(sfhw.main_dialog);
        sfhw.main_dialog.open();
    }

    /**
     * addcomment_button action
     * @param ex
     */
    private void setAddcomment_button(ClickEvent ex){
        AddCommentCTaskWindow acctw = new AddCommentCTaskWindow(this);
        ctaskviewer_layout.add(acctw.main_dialog);
        acctw.main_dialog.open();
    }
}

/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package pl.jakubwawak.aim.website_ui.widgets.widgets;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.atmosphere.interceptor.AtmosphereResourceStateRecovery;

/**
 * Main Widget object
 */
public class Widget {

    // widget layout
    public VerticalLayout widget;

    // max widget size
    public final int widthMAX = 60;
    public final int heightMAX = 60;

    // styling widget
    public String backgroundType = "background-color";
    public String backgroundStyle = "black";

    // set size
    public int width,height;

    // clear widget button
    Button addToWidget_button;

    /**
     * Constructor
     * @param width
     * @param height
     */
    public Widget(int width,int height){
        if ( widthMAX > width )
            this.width = width;
        else
            this.width = widthMAX;
        if ( heightMAX > height )
            this.height = height;
        else
            this.height = height;
        widget = new VerticalLayout();

        widget.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        widget.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        widget.getStyle().set("text-align", "center");

        widget.getStyle().set("border-radius","25px");
        widget.getStyle().set(backgroundType,backgroundStyle);
        widget.getStyle().set("--lumo-font-family","Monospace");
        widget.setHeight(height+"%");widget.setWidth(width+"%");

        // creating empty widget as default
        // createEmptyWidget();
    }

    /**
     * Function for clearing widget data
     */
    public void clearWidget(){
        widget.removeAll();
    }

    /**
     * Function for adding to widget
     * @param component
     */
    public void addToWidget(Component component){
        widget.add(component);
    }

    /**
     * Function for reloading background
     */
    public void reloadBackground(){
        widget.getStyle().set(backgroundType,backgroundStyle);
    }

    /**
     * Function for creating empty widget
     */
    void createEmptyWidget(){
        addToWidget_button = new Button("", VaadinIcon.PLAY_CIRCLE.create(),this::addToWidgetbutton_action);
        addToWidget_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);
        widget.add(addToWidget_button);
    }

    // button actions
    private void addToWidgetbutton_action(ClickEvent ex){
        Notification.show("Open widget drawer!");
    }

}

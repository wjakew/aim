/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package pl.jakubwawak.aim.website_ui.widgets;

import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import pl.jakubwawak.aim.website_ui.widgets.widgets.Widget;

/**
 * Layout panel for storing widgets
 */
public class WidgetPanel {

    public HorizontalLayout mainLayout;

    Widget widget1,widget2,widget3,widget4,widget5,widget6,widget7,widget8,widget9;
    VerticalLayout vl_left,vl_center,vl_right;

    /**
     * Constructor
     */
    public WidgetPanel(){
        mainLayout = new HorizontalLayout();
        preparePanel();
    }

    /**
     * Function for preparing widgets
     */
    void prepareWidgets(){
        widget1 = new Widget(60,60);
        widget2 = new Widget(60,30);
        widget3 = new Widget(30,30);
        widget4 = new Widget(30,30);
        widget5 = new Widget(60,60);
        widget6 = new Widget(60,30);
        widget7 = new Widget(60,30);
        widget8 = new Widget(30,30);
        widget9 = new Widget(30,30);

        vl_left = new VerticalLayout();
        vl_left.setSizeFull();
        vl_left.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        vl_left.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        vl_left.getStyle().set("text-align", "center");

        vl_center = new VerticalLayout();
        vl_center.setSizeFull();
        vl_center.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        vl_center.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        vl_center.getStyle().set("text-align", "center");

        vl_right = new VerticalLayout();
        vl_right.setSizeFull();
        vl_right.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        vl_right.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        vl_right.getStyle().set("text-align", "center");

        vl_left.add(widget1.widget,widget2.widget);

        HorizontalLayout widgetCenterLayout = new HorizontalLayout();
        widgetCenterLayout.setWidth("100%");
        widgetCenterLayout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        widgetCenterLayout.add(widget3.widget,widget4.widget);

        vl_center.add(widgetCenterLayout,widget5.widget);

        HorizontalLayout widgetRightLayout = new HorizontalLayout();
        widgetRightLayout.setWidth("100%");
        widgetRightLayout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        widgetRightLayout.add(widget8.widget,widget9.widget);

        vl_right.add(widget6.widget,widget7.widget,widgetRightLayout);


    }

    /**
     * Function for preparing panel
     */
    void preparePanel(){
        mainLayout.setWidth("80%");mainLayout.setHeight("80%");
        mainLayout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        mainLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        mainLayout.getStyle().set("background-image","radial-gradient(purple,gray)");
        mainLayout.getStyle().set("text-align", "center");
        mainLayout.getStyle().set("border-radius","25px");
        mainLayout.add(vl_left,vl_center,vl_right);
        prepareWidgets();
    }
}

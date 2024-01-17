/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package pl.jakubwawak.aim.website_ui.widgets.widgets.widgets;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_terminal_engine.AIMInputParser;
import pl.jakubwawak.aim.website_ui.widgets.widgets.Widget;
import java.io.Serializable;

/**
 * Widget for
 */
public class TerminalWidget extends Widget implements Serializable {

    String contentString;

    boolean contentStringCorrect; // flag for checking if string is correct

    TextField terminalArea;

    Button createrequest_button;

    AIMInputParser aip;

    /**
     * Constructor
     * @param width
     * @param height
     * @param contentString
     */
    public TerminalWidget(int width,int height, String contentString,int widgetID){
        super(width,height,widgetID);
        this.contentString = contentString;
        super.widgetName = "terminal";
        super.widgetDesc = "Terminal window for using AIM Terminal in the Widget. Type terminal to create!";
        contentStringCorrect = checkContentStringCorrect();
        if (contentString.isEmpty()){
            prepareDemo();
            AimApplication.database.log("WIDGET","Prepared demo!");
        }
        else{
            if (contentStringCorrect)
                prepareWidget();
            else
                AimApplication.database.log("WIDGET","Widget empty! Wrong contentString");
        }
    }

    /**
     * Function for checking contentstring value
     * @return boolean
     */
    public boolean checkContentStringCorrect(){
        // logic for checking content string logic
        return true;
    }

    /**
     * Function for adding component
     * @param component
     */
    void addComponent(Component component){
        super.addToWidget(component);
    }


    /**
     * Function for preparing widget content
     */
    void prepareContent(){
        // prepare content layout
        terminalArea = new TextField("Your Command Line");
        terminalArea.setWidth("80%");terminalArea.setHeight("50%");
        aip = new AIMInputParser(widget);
        createrequest_button = new Button("Create Request", VaadinIcon.ARROW_RIGHT.create(),this::setCreaterequest_button);
        createrequest_button.setWidth("80%");
        createrequest_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);

    }

    /**
     * Function for preparing widget layout
     */
    void prepareWidget(){
        prepareContent();
        super.reloadBackground();
        addComponent(new H6("AIM TERMINAL"));
        addComponent(terminalArea);
        addComponent(createrequest_button);
    }

    /**
     * Function for preparing demo content of the widget
     */
    public void prepareDemo(){
        // prepare demo content
        prepareContent();
        super.widget.removeAll();
        addComponent(new H6("AIM TERMINAL"));
        addComponent(terminalArea);
        addComponent(createrequest_button);

        terminalArea.setEnabled(false);
        createrequest_button.setEnabled(false);
    }

    /**
     * createrequest_button action
     * @param ex
     */
    private void setCreaterequest_button(ClickEvent ex){
        String command = terminalArea.getValue();
        if ( !command.isEmpty() ){
            aip.setUserInput(command);
            aip.parse();
            if ( aip.successParsingFlag == 1)
                terminalArea.setValue("");
        }
        else{
            Notification.show("Terminal field is empty!");
        }
    }
}

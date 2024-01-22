/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package pl.jakubwawak.aim.website_ui;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import pl.jakubwawak.aim.aim_dataengine.aim_terminal_engine.AIMInputParser;

/**
 * Object for creating new terminal data for different objects
 */
public class TerminalUI extends VerticalLayout {

    String width, lenght;
    VerticalLayout parent;

    public TextField terminal_field;

    AIMInputParser aip;

    /**
     * Constructor
     * @param width - width of the terminal
     * @param lenght - length of the terminal
     * @param parent - parent object
     */
    public TerminalUI(String width, String lenght, VerticalLayout parent){
        this.width = width;
        this.lenght = lenght;
        this.parent = parent;
        aip = new AIMInputParser(this);
        //prepare terminal
        prepareTerminal();
    }

    /**
     * Function for preparing components
     */
    void prepareComponents(){
        terminal_field = new TextField();
        terminal_field.setPrefixComponent(VaadinIcon.TERMINAL.create());
        terminal_field.setPlaceholder("let's create something...");
        terminal_field.setWidth("100%");

        terminal_field.addKeyPressListener(e->
        {
            if (e.getKey().equals(Key.ENTER)){
                aip.setUserInput(terminal_field.getValue());
                aip.parse();
                if ( aip.successParsingFlag == 1)
                    terminal_field.setValue("");
                if ( aip.simpleViewFlagNeed == 1){
                    // simple view invoke
                }
                else{
                    // extended view invoke
                }
            }
        });
    }

    /**
     * Function for preparing terminal object
     */
    void prepareTerminal(){
        prepareComponents();


        setSizeFull();
        getStyle().set("background-image","radial-gradient(pink, gray)");
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }
}

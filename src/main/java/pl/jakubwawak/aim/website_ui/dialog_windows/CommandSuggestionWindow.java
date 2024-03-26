/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.website_ui.dialog_windows;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import lombok.extern.java.Log;
import pl.jakubwawak.aim.aim_dataengine.aim_terminal_engine.AIMInputParser;
import pl.jakubwawak.aim.website_ui.views.TerminalView;
import pl.jakubwawak.maintanance.GridElement;

import java.util.ArrayList;

/**
 * Window for showing suggestions for user input
 */
public class CommandSuggestionWindow {

    // variables for setting x and y of window
    public String width = "40%";
    public String height = "70%";
    public String backgroundStyle = "";

    // main login components
    public Dialog main_dialog;
    VerticalLayout main_layout;

    TextField terminal_field;
    Button acceptsuggestion_button;

    Grid<GridElement> sugessted_grid;
    ArrayList<GridElement> commandContent;

    String userInput;
    TerminalView secondaryView;

    /**
     * Constructor
     */
    public CommandSuggestionWindow(String userInput, TerminalView secondaryView){
        this.userInput = userInput.replaceAll("\\?","");
        this.secondaryView = secondaryView;
        main_dialog = new Dialog();
        main_dialog.addClassName("aim-window-normal");
        main_layout = new VerticalLayout();
        commandContent = new ArrayList<>();
        main_dialog.setResizable(true);
        main_dialog.setModal(true);
        prepare_dialog();
    }

    /**
     * Function for reloading grid content
     */
    void reloadGridContent(){
        commandContent.clear();
        AIMInputParser aip = new AIMInputParser(main_layout);
        ArrayList<String> commands = aip.getCommandSuggestion(userInput);

        for(String command : commands){
            commandContent.add(new GridElement(command));
        }

        sugessted_grid.getDataProvider().refreshAll();
        sugessted_grid.addClassName("aim-grid");

    }

    /**
     * Function for preparing components
     */
    void prepare_components(){
        // set components
        terminal_field = new TextField();
        terminal_field.setPrefixComponent(VaadinIcon.TERMINAL.create());
        terminal_field.setPlaceholder("let's create something...");
        terminal_field.setWidth("100%");
        terminal_field.setValue(userInput);

        terminal_field.addKeyPressListener(e->{
            userInput = terminal_field.getValue();
            reloadGridContent();
        });

        sugessted_grid = new Grid<>(GridElement.class,false);
        sugessted_grid.addColumn(GridElement::getGridelement_text).setHeader("Suggested Commands");
        sugessted_grid.setSizeFull();
        sugessted_grid.setItems(commandContent);

        sugessted_grid.addItemClickListener(e->{
            for(GridElement clickedItem : sugessted_grid.getSelectedItems()){
                terminal_field.setValue(clickedItem.getGridelement_text());
            }
        });

        acceptsuggestion_button = new Button("Use this command!",VaadinIcon.ARROW_RIGHT.create(),this::acceptsuggestionbutton_action);
        acceptsuggestion_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);
        acceptsuggestion_button.setWidth("100%");

        reloadGridContent();
    }

    /**
     * Function for preparing layout
     */
    void prepare_dialog(){
        prepare_components();

        // set layout
        main_layout.add(VaadinIcon.QUESTION_CIRCLE.create());
        main_layout.add(new H6("TERMINAL COMMAND HELPER"));
        main_layout.add(terminal_field);
        main_layout.add(sugessted_grid);
        main_layout.add(acceptsuggestion_button);

        main_layout.setSizeFull();
        main_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        main_layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        main_dialog.add(main_layout);
        main_dialog.setWidth(width);main_dialog.setHeight(height);
    }

    /**
     * acceptsuggestion_button action
     * @param ex
     */
    private void acceptsuggestionbutton_action(ClickEvent ex){
        if (terminal_field.getValue().contains("-n") || terminal_field.getValue().contains("-t") || terminal_field.getValue().contains("_")){
            String property1 = null;
            String property2 = null;
            ArrayList<String> propertiesCollection = new ArrayList<>();
            for(String word : terminal_field.getValue().split(" ")){
                if (word.contains("_")){
                    propertiesCollection.add(word);
                }
            }
            if (propertiesCollection.size() == 1 ){
                property1 = propertiesCollection.get(0);
            }
            else if (propertiesCollection.size() == 2 ){
                property1 = propertiesCollection.get(0);
                property2 = propertiesCollection.get(1);
            }

            if (property1 != null){
                CommandSuggestionInsertWindow csiw = new CommandSuggestionInsertWindow(terminal_field.getValue(),property1,property2,secondaryView);
                main_layout.add(csiw.main_dialog);
                csiw.main_dialog.open();
                main_dialog.close();
            }
            else{
                secondaryView.terminal_field.setValue(terminal_field.getValue());
                main_dialog.close();
            }

        }
        else{
            secondaryView.terminal_field.setValue(terminal_field.getValue());
            main_dialog.close();
        }
    }
}

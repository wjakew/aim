/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.website_ui.style;

import com.vaadin.flow.component.button.Button;

/**
 * Object for styling buttons
 */
public class ButtonStyler {

    /**
     * Function for styling button as primary
     * @param width
     * @param height
     * @param source_button
     */
    public void primaryButtonStyle(Button source_button, String width, String height){
        source_button.setWidth(width);
        source_button.setHeight(height);
        source_button.getStyle().set("background-color","black");
        source_button.getStyle().set("color","#FFFFFF");
    }
}

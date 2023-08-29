/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com / j.wawak@usp.pl
 */
package pl.jakubwawak.maintanance;

import oshi.jna.platform.windows.NtDll;

import java.io.Serializable;

/**
 * Object for creating grid element data
 */
public class GridElement implements Serializable {

    String gridelement_text;
    int gridelement_id;

    String gridelement_details;
    String gridelement_details2;

    /**
     * Constructor
     * @param gridelement_text
     */
    public GridElement(String gridelement_text){
        this.gridelement_text = gridelement_text;
    }

    /**
     * Constructor with ID
     * @param gridelement_text
     * @param gridelement_id
     */
    public GridElement(String gridelement_text,int gridelement_id){
        this.gridelement_text = gridelement_text;
        this.gridelement_id = gridelement_id;
        gridelement_details2 = "";
    }

    /**
     * Constructor with ID and details
     * @param gridelement_text
     * @param gridelement_id
     * @param gridelement_details
     */
    public GridElement(String gridelement_text,int gridelement_id,String gridelement_details){
        this.gridelement_text = gridelement_text;
        this.gridelement_id = gridelement_id;
        this.gridelement_details = gridelement_details;
        this.gridelement_details2 = "";
    }

    /**
     * Constructor with more details
     * @param gridelement_text
     * @param gridelement_details
     * @param gridelement_details2
     */
    public GridElement(String gridelement_text, String gridelement_details, String gridelement_details2){
        this.gridelement_text = gridelement_text;
        this.gridelement_id = 0;
        this.gridelement_details = gridelement_details;
        this.gridelement_details2 = gridelement_details2;
    }

    public String getGridelement_text() {
        return gridelement_text;
    }
    public int getGridelement_id(){return gridelement_id;}

    public String getGridelement_details(){return gridelement_details;}

    public String getGridelement_details2(){return gridelement_details2;}
}
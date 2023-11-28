/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.aim_dataengine.aim_objects;

import org.bson.Document;
import org.bson.types.ObjectId;
import pl.jakubwawak.aim.AimApplication;

/**
 * Object for representing WidgetPanel on database
 */
public class AIM_WidgetPanel {

    public ObjectId aim_user_id;

    /**
     * widgetContent document
     * widgetType: x
     * widgetContentString: x
     */
    public Document widget1Content;
    public Document widget2Content;
    public Document widget3Content;
    public Document widget4Content;
    public Document widget5Content;
    public Document widget6Content;

    /**
     * Constructor
     */
    public AIM_WidgetPanel(){
        aim_user_id = AimApplication.loggedUser.aim_user_id;
    }

    /**
     * Constructor with database support
     * @param widgetPanelDocument
     */
    public AIM_WidgetPanel(Document widgetPanelDocument){
        aim_user_id = widgetPanelDocument.getObjectId("aim_user_id");
        try{
            widget1Content = widgetPanelDocument.get("widget1Content", Document.class);
        }catch(Exception ex){
            widget1Content = null;
        }
        try{
            widget2Content = widgetPanelDocument.get("widget2Content", Document.class);
        }catch(Exception ex){
            widget2Content = null;
        }
        try{
            widget3Content = widgetPanelDocument.get("widget3Content", Document.class);
        }catch(Exception ex){
            widget3Content = null;
        }
        try{
            widget4Content = widgetPanelDocument.get("widget4Content", Document.class);
        }catch(Exception ex){
            widget4Content = null;
        }
        try{
            widget5Content = widgetPanelDocument.get("widget5Content", Document.class);
        }catch(Exception ex){
            widget5Content = null;
        }
        try{
            widget6Content = widgetPanelDocument.get("widget6Content", Document.class);
        }catch(Exception ex){
            widget6Content = null;
        }
    }

    /**
     * Function for preparing document for database
     * @return Document
     */
    public Document prepareDocument(){
        Document document = new Document();
        document.append("aim_user_id",aim_user_id);
        document.append("widget1Content",widget1Content);
        document.append("widget2Content",widget2Content);
        document.append("widget3Content",widget3Content);
        document.append("widget4Content",widget4Content);
        document.append("widget5Content",widget5Content);
        document.append("widget6Content",widget6Content);
        return document;
    }
}

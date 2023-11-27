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
        widget1Content = widgetPanelDocument.get("widget1Content", Document.class);
        widget2Content = widgetPanelDocument.get("widget2Content", Document.class);
        widget3Content = widgetPanelDocument.get("widget3Content", Document.class);
        widget4Content = widgetPanelDocument.get("widget4Content", Document.class);
        widget5Content = widgetPanelDocument.get("widget5Content", Document.class);
        widget6Content = widgetPanelDocument.get("widget6Content", Document.class);
    }

    /**
     * Function for preparing document for database
     * @return Document
     */
    public Document prepareDocument(){
        Document document = new Document();
        document.append("widget1Content",widget1Content);
        document.append("widget2Content",widget2Content);
        document.append("widget3Content",widget3Content);
        document.append("widget4Content",widget4Content);
        document.append("widget5Content",widget5Content);
        document.append("widget6Content",widget6Content);
        return document;
    }
}

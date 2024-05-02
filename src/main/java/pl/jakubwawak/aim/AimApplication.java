/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.spring.annotation.EnableVaadin;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_GlobalConfiguration;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_User;
import pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers.aim_objects_viewers_board.AIM_BoardTaskListLayout;
import pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers.aim_objects_viewers_board.CurrentBoardComposer;
import pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers.aim_objects_viewers_projects.CurrentProjectComposer;
import pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers.aim_objects_viewers_task.CurrentTaskComposer;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMWidgetPanel;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_Connector;
import pl.jakubwawak.aim.website_ui.views.HomeView;
import pl.jakubwawak.aim.website_ui.widgets.WidgetPanel;
import pl.jakubwawak.maintanance.ConsoleColors;

import java.time.LocalDateTime;
import java.util.Scanner;

@SpringBootApplication
@EnableVaadin({"pl.jakubwawak"})
@PWA(
		name = "Aim Application",
		shortName = "Aim"
)
@Theme(value="aim_theme")
public class AimApplication extends SpringBootServletInitializer implements AppShellConfigurator {

	public static String version = "v1.1.5";
	public static String build = "aim020524REV01";
	public static String applicationStartup;
	public static int test_flag = 0; // flag for enabling testing
	public static int log_database_dump_flag = 1; // flag for enabling database log dump

	public static Database_Connector database;
	public static AIM_GlobalConfiguration globalConfiguration;
	public static String connectionStringDebug = "";

	public static AIM_User loggedUser;

	public static CurrentTaskComposer session_ctc;
	public static CurrentProjectComposer session_cpc;
	public static CurrentBoardComposer session_cbc;
	public static AIM_BoardTaskListLayout currentBoardTaskList;

	public static WidgetPanel currentWidgetPanel;


	/**
	 * Main application function
	 * @param args
	 */
	public static void main(String[] args) {
		applicationStartup = LocalDateTime.now().toString();
		show_header();
		Scanner scanner = new Scanner(System.in);
		loggedUser = null;
		session_ctc = null;
		session_cpc = null;
		session_cbc = null;
		currentWidgetPanel = null;
		currentBoardTaskList = null;
		database = new Database_Connector();
		AimApplicationMenu aam = new AimApplicationMenu();
		if ( test_flag == 0 ){
			// run application in normal mode
			if ( connectionStringDebug.isBlank() ){
				if ( args.length == 1 ){
					database.setDatabase_url(args[0]);
					database.connect();
					if(database.connected){
						SpringApplication.run(AimApplication.class, args);
						aam.run();
					}
					else{
						System.out.println(ConsoleColors.RED_BACKGROUND+"Cannot connect to database. Check connection string!");
					}
				}
				else{
					System.out.print(ConsoleColors.BLUE_BRIGHT+"ConnectionURL: "+ConsoleColors.RESET);
					String connectionString = scanner.nextLine();
					database.setDatabase_url(connectionString);
					database.connect();
					if(database.connected){
						SpringApplication.run(AimApplication.class, args);
						aam.run();
					}
					else{
						System.out.println(ConsoleColors.RED_BACKGROUND+"Cannot connect to database. Check connection string!");
					}
				}
			}
			// running application with debug connection string
			else{
				database.setDatabase_url(connectionStringDebug);
				database.connect();
				if(database.connected){
					SpringApplication.run(AimApplication.class, args);
					aam.run();
				}
				else{
					System.out.println(ConsoleColors.RED_BACKGROUND+"Cannot connect to database. Check connection string!");
				}
			}
		}
		else{
			// run tests;
			new AimTest();
		}
	}

	/**
	 * Function for showing header
	 */
	public static void show_header(){
		String header = "       _\n" +
				"  __ _(_)_ __ ___\n" +
				" / _` | | '_ ` _ \\\n" +
				"| (_| | | | | | | |\n" +
				" \\__,_|_|_| |_| |_|";
		header = header + version+"/"+build;
		System.out.println(ConsoleColors.YELLOW_BRIGHT+header+ConsoleColors.RESET);
	}

	/**
	 * Function for loading connection string
	 * @return String
	 */
	public String getConnectionString(){
		return connectionStringDebug;
	}

}

# aim

![icon](https://github.com/wjakew/aim/blob/master/readme_resources/aim_logo.png)

### Web application for task scheduling and project maintaining. More advanced “todo application”.

> Current version in Proof Of Concept state - do not use that in mission-critical tasks.
> 

### Roadmap

- Application log viewer - IN FUTURE
- Notes widget - IN FUTURE
- Coding projects and task support - IN FUTURE

![screenshot1](https://github.com/wjakew/aim/blob/master/readme_resources/aim_screenshot1.png)

### Application Database Schema

![database_schema](https://github.com/wjakew/aim/blob/master/readme_resources/aim_database_schema.png)

### Application Schema

Coming soon!

![screenshot1](https://github.com/wjakew/aim/blob/master/readme_resources/aim_screenshot10.png)

### Core functionality.

1. Creating tasks, projects and boards.
2. Maintaining tasks by changing owners, adding comments. Check task history right in the task window.
3. Create projects with full history and API support, add tasks, add comments and check the completion level.
4. Create collaborative boards  - add task, delegate users. Create members and add privileges.
5. Two UI modes, simple, normal one and terminal - interact with the app using only the keyboard.
6. Dashboard view with engaging task list.
7. Simple and easy deployment - you only need MongoDB instance and machine with Java 21.
8. Ready for full API support for task automation.
9. UI Customization.
10. Sharing objects and public viewer.
11. Page/View with full customization - new widgets - custom floating window for your quick actions! 
12. Coding projects and task support - new category of object - COMING SOON.

### Prototype boards

Whole UI.

![screenshot11](https://github.com/wjakew/aim/blob/master/readme_resources/aim_screenshot11.png)

Widgets

![screenshot12](https://github.com/wjakew/aim/blob/master/readme_resources/aim_screenshot12.png)

### UI Screenshots

Standard View

![screenshot2](https://github.com/wjakew/aim/blob/master/readme_resources/aim_screenshot5.png)

Terminal View

![screenshot3](https://github.com/wjakew/aim/blob/master/readme_resources/aim_screenshot7.png)

### Technology stack:

1. MongoDB
2. Java 21
3. Spring-Boot + Vaadin

### API 

Application contains restfull API. You can reach that on /api
Current endpoints:

```html
/api/health/{aim_apikey}
```
```html
/api/task/task-list/{list_type}/{aim_apikey}
```
```html
/api/task/add/{aim_apikey}/{task_name}
```
```html
/api/project/project-list/{aim_apikey}
```
```html
/api/board/board-list/{aim_apikey}
```

You can turn on or off API and create your API key in application settings

### Deployment

1. Download the latest zip archive from the Release section.
2. Unzip in desired location.
3. Run the app with 

```jsx
java -jar aim-1.0.0.jar
```

4. Insert the connection string from your MongoDB tenant.
5. Create the admin account!

Brief Description
This project is a simple Java Spring Boot application to store booksin different categories and numbers

Steps to run the application

1. Pull or download this code from the git repository
2. Create a schema named bookstore on MySQL workbench on your local system
3. Open the application.yaml file, set the username to “dot_user” and password to “dotpassword” or you can put your database username and password in these fields.
4. Now you can run your application. The table “book” will be automatically created in “bookstore” schema created before now. 
5. By default, it runs on port 8088. 
6. You can check the documentation on swagger ui from following url: http://localhost:8088/swagger-ui.html
7. Once the db has been set up, we can also run the unit tests.
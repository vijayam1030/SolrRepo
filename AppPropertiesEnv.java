In this example, the active environment is obtained from the system property "myapp.environment". 
    You can set this property when running the application, for example, using the -D option: java -Dmyapp.environment=dev MainClass.
    Alternatively, you can use an environment variable (System.getenv) instead of a system property.



import java.io.InputStream;
import java.util.Properties;

public class MainClass {
    public static void main(String[] args) {
        // Get the active environment (e.g., from system property or environment variable)
        String environment = System.getProperty("myapp.environment"); // or use System.getenv("MYAPP_ENVIRONMENT")

        // Define the properties file based on the active environment
        String propertiesFile = "application-" + environment + ".properties";

        try {
            // Load the properties file
            InputStream input = MainClass.class.getClassLoader().getResourceAsStream(propertiesFile);
            Properties properties = new Properties();
            properties.load(input);

            // Access the properties
            String host = properties.getProperty("server.host");
            String port = properties.getProperty("server.port");

            // Use the properties as needed
            System.out.println("Server host: " + host);
            System.out.println("Server port: " + port);

            // Close the input stream
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

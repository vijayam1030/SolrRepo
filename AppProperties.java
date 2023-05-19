import java.io.InputStream;
import java.util.Properties;

public class MainClass {
    public static void main(String[] args) {
        try {
            // Load the properties file
            InputStream input = MainClass.class.getClassLoader().getResourceAsStream("application.properties");
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

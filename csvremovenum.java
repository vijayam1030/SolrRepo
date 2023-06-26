import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CSVExtractor {
    public static void main(String[] args) {
        String csvString = "John,Doe,30,New York";
        String[] values = csvString.split(",");
        
        String outputPath = "path/to/output.csv";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            for (String value : values) {
                String filteredValue = value.trim().replaceAll("\\d", "");
                writer.write(filteredValue);
                writer.write(",");
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

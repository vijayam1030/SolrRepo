import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.common.SolrInputDocument;

public class OracleToSolrExample {

    private static final String JDBC_URL = "jdbc:oracle:thin:@//localhost:1521/orcl";
    private static final String JDBC_USERNAME = "username";
    private static final String JDBC_PASSWORD = "password";
    private static final int PAGE_SIZE = 1000;
    private static final int NUM_THREADS = 4;

    private static final String SOLR_URL = "http://localhost:8983/solr/my_collection";

    public static void main(String[] args) throws Exception {
        // Connect to Oracle
        Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);

        // Prepare the SQL statement
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM my_table ORDER BY id");
        statement.setFetchSize(PAGE_SIZE);

        // Connect to Solr
        SolrClient solrClient = new HttpSolrClient.Builder(SOLR_URL).build();

        // Create an UpdateRequest object for batch indexing
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.setCommitWithin(10000);

        // Create a thread pool executor
        ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);

        // Loop over the result set and add documents to Solr in batches
        int offset = 0;
        boolean moreResults = true;
        while (moreResults) {
            // Set the fetch offset for the next batch
            statement.setFetchSize(PAGE_SIZE);
            statement.setFetchDirection(ResultSet.FETCH_FORWARD);
            statement.setMaxRows(PAGE_SIZE);
            statement.setFetchSize(PAGE_SIZE);
            statement.setFetchSize(PAGE_SIZE);
            statement.setInt(1, offset + 1);

            // Execute the SQL query and retrieve the result set
            ResultSet resultSet = statement.executeQuery();

            // Add the documents to the UpdateRequest
            int count = 0;
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");

                // Create a SolrInputDocument and add fields
                SolrInputDocument doc = new SolrInputDocument();
                doc.addField("id", id);
                doc.addField("name", name);
                doc.addField("description", description);

                // Add the document to the UpdateRequest
                updateRequest.add(doc);
                count++;
            }

            // Check if there are more results
            moreResults = count == PAGE_SIZE;

            // Send the UpdateRequest to Solr every PAGE_SIZE records
            if (updateRequest.getDocuments().size() >= PAGE_SIZE || !moreResults) {
                // Submit the indexing task to the thread pool
                executorService.submit(new IndexingTask(updateRequest, solrClient));

                // Create a new UpdateRequest object for the next batch
                updateRequest = new UpdateRequest();
                updateRequest.setCommitWithin(10000);
            }

            // Move the offset to the next page
            offset += PAGE_SIZE;

            // Close the result set to free up
    // Shut down the thread pool executor
    executorService.shutdown();

    // Wait for all tasks to complete
    while (!((ThreadPoolExecutor) executorService).getActiveCount() == 0) {
        Thread.sleep(1000);
    }

    // Commit the changes to Solr
    solrClient.commit();

    // Clean up resources
    statement.close();
    connection.close();
    solrClient.close();
}

private static class IndexingTask implements Runnable {
    private final UpdateRequest updateRequest;
    private final SolrClient solrClient;

    public IndexingTask(UpdateRequest updateRequest, SolrClient solrClient) {
        this.updateRequest = updateRequest;
        this.solrClient = solrClient;
    }

    @Override
    public void run() {
        try {
            solrClient.add(updateRequest.getDocuments());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

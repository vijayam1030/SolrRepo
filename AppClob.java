import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.common.SolrInputDocument;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

public class SolrClobCompressionExample {

    public static void main(String[] args) {
        // Solr server URL
        String solrUrl = "http://localhost:8983/solr/my_collection";

        // Create a Solr client
        SolrClient solrClient = new HttpSolrClient.Builder(solrUrl).build();

        // Retrieve CLOB content (replace with your own logic to retrieve the content)
        String clobContent = retrieveClobContent();

        // Compress the CLOB content
        byte[] compressedContent = compressContent(clobContent);

        // Create a Solr document
        SolrInputDocument solrDocument = new SolrInputDocument();
        solrDocument.addField("id", "your_document_id");
        solrDocument.addField("compressed_content_field", compressedContent);

        // Create an update request and add the document
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.setAction(SolrRequest.ACTION.COMMIT, true, true);
        updateRequest.add(solrDocument);

        try {
            // Send the update request to Solr
            solrClient.request(updateRequest);
            System.out.println("Document indexed successfully!");
        } catch (Exception e) {
            System.err.println("Error indexing document: " + e.getMessage());
        } finally {
            // Close the Solr client
            solrClient.close();
        }
    }

    private static String retrieveClobContent() {
        // Replace with your own logic to retrieve the CLOB content
        // For this example, we'll simulate the content retrieval with a simple string
        return "This is the content of the CLOB.";
    }

    private static byte[] compressContent(String content) {
        byte[] compressedBytes;

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(outputStream, new Deflater(), true)) {

            byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
            deflaterOutputStream.write(contentBytes, 0, contentBytes.length);
            deflaterOutputStream.finish();

            compressedBytes = outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error compressing content: " + e.getMessage());
        }

        return compressedBytes;
    }
}

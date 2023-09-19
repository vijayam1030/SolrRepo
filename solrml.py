from pyspark.sql import SparkSession

# Initialize a Spark session
spark = SparkSession.builder \
    .appName("SolrReadExample") \
    .getOrCreate()

# Solr connection parameters
solr_url = "http://localhost:8983/solr"
solr_collection = "your_collection_name"  # Replace with your Solr collection name

# Read data from Solr into Spark DataFrame
df = spark.read.format("solr") \
    .option("zkhost", solr_url) \
    .option("collection", solr_collection) \
    .load()

# Show the first few rows of the DataFrame
df.show()

# You can perform Spark operations on the DataFrame as needed
# For example, filtering and displaying specific columns
filtered_df = df.filter(df["some_field"] == "some_value")
filtered_df.select("some_field", "another_field").show()

# Stop the Spark session
spark.stop()

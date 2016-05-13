package com.packex.connector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.bigquery.Dataset;
import com.google.cloud.bigquery.DatasetInfo;
import com.google.cloud.bigquery.Field;
import com.google.cloud.bigquery.InsertAllRequest;
import com.google.cloud.bigquery.InsertAllResponse;
import com.google.cloud.bigquery.Schema;
import com.google.cloud.bigquery.StandardTableDefinition;
import com.google.cloud.bigquery.Table;
import com.google.cloud.bigquery.TableId;
import com.google.cloud.bigquery.TableInfo;
import com.packex.Constants;
import com.google.cloud.bigquery.InsertAllRequest.Builder;

public class BigQueryConnector {
    private static final Logger logger = Logger.getLogger(BigQueryConnector.class.getName());
    
    private static BigQueryConnector instance;
    private String datasetName;
    private String tableName;
    private BigQuery bigQuery;
    private Builder insertRequestBuilder;
    
    private BigQueryConnector() {
        this.bigQuery = BigQueryOptions.defaultInstance().service();
    }
    
    public static BigQueryConnector getInstance() {
        if (instance == null) {
            instance = new BigQueryConnector();
        }
        
        return instance;
    }
    
    public void createDataset(String datasetName) {
        Dataset dataset = this.bigQuery.getDataset(datasetName);
        if (dataset != null) {
            logger.log(Level.WARNING, String.format("Dataset \"%s\" already exists so NOT creating it", datasetName));
            return;
        }
        
        // Create the dataset
        this.bigQuery.create(DatasetInfo.builder(datasetName).build());
        logger.log(Level.INFO, String.format("Created dataset \"%s\"", datasetName));
    }
    
    public void createTable(String datasetName, String tableName) {
        TableId tableId = TableId.of(datasetName, tableName);
        Table table = this.bigQuery.getTable(tableId);
        if (table != null) {
            logger.log(Level.WARNING, String.format("Table \"%s\" already exists so NOT creating it", tableName));
            return;
        }
        
        ArrayList<Field> fields = new ArrayList<Field>();
        
        // Core fields
        Field dateField = Field.of(Constants.DATE_FIELD, Field.Type.timestamp());
        fields.add(dateField);
        Field languageField = Field.of(Constants.LANGUAGE_FIELD, Field.Type.string());
        fields.add(languageField);
        Field nameField = Field.of(Constants.PACKAGE_NAME_FIELD, Field.Type.string());
        fields.add(nameField);
        Field versionField = Field.of(Constants.VERSION_FIELD, Field.Type.string());
        fields.add(versionField);
        Field categoryField = Field.of(Constants.CATEGORY_FIELD, Field.Type.string());
        fields.add(categoryField);
        
        // Fetched download data fields
        Field totalDownloadsField = Field.of(Constants.TOTAL_DOWNLOADS_FIELD, Field.Type.integer());
        fields.add(totalDownloadsField);
        Field monthDownloadsField = Field.of(Constants.MONTHLY_DOWNLOADS_FIELD, Field.Type.integer());
        fields.add(monthDownloadsField);
        Field weekDownloadsField = Field.of(Constants.WEEKLY_DOWNLOADS_FIELD, Field.Type.integer());
        fields.add(weekDownloadsField);
        Field dayDownloadsField = Field.of(Constants.DAILY_DOWNLOADS_FIELD, Field.Type.integer());
        fields.add(dayDownloadsField);
        
        // Calculated download data fields
        Field monthCalculatedDownloadsField = Field.of(Constants.MONTHLY_CALCULATED_DOWNLOADS_FIELD, Field.Type.integer());
        fields.add(monthCalculatedDownloadsField);
        Field weekCalculatedDownloadsField = Field.of(Constants.WEEKLY_CALCULATED_DOWNLOADS_FIELD, Field.Type.integer());
        fields.add(weekCalculatedDownloadsField);
        Field dayCalculatedDownloadsField = Field.of(Constants.DAILY_CALCULATED_DOWNLOADS_FIELD, Field.Type.integer());
        fields.add(dayCalculatedDownloadsField);
        
        Schema schema = Schema.of(fields);
        StandardTableDefinition tableDefinition = StandardTableDefinition.of(schema);
        
        // Create the table
        this.bigQuery.create(TableInfo.of(tableId, tableDefinition));
        logger.log(Level.INFO, String.format("Create table \"%s\"", tableName));
    }
    
    public void begin(String datasetName, String tableName) {
        this.datasetName = datasetName;
        this.tableName = tableName;
        this.insertRequestBuilder = null;
    }
    
    public void addRow(Map<String, Object> row) {
        if (this.insertRequestBuilder == null) {
            TableId tableId = TableId.of(this.datasetName, this.tableName);
            this.insertRequestBuilder = InsertAllRequest.builder(tableId);
        }
        
        this.insertRequestBuilder = this.insertRequestBuilder.addRow(row);
    }
    
    public void commit() {
        InsertAllRequest insertRequest = this.insertRequestBuilder.build();
        
        InsertAllResponse insertResponse = this.bigQuery.insertAll(insertRequest);
        if (insertResponse.hasErrors()) {
            logger.log(Level.SEVERE, String.format("Hit an error when inserting into table \"%s\" : %s", this.tableName, insertResponse));
        } else {
            logger.log(Level.INFO, String.format("Successfully inserted the data into table \"%s\"", this.tableName));
        }
        
        this.insertRequestBuilder = null;
    }
    
    public static void main(String[] args) {
        String datasetName = "package_downloads";
        String tableName = "google_package_downloads";
        BigQueryConnector bq = BigQueryConnector.getInstance();
        bq.createDataset(datasetName);
        bq.createTable(datasetName, tableName);
        
        bq.begin(datasetName, tableName);
        
        Map<String, Object> row = new HashMap<String, Object>();
        row.put(Constants.LANGUAGE_FIELD, "JAVA");
        bq.addRow(row);
        bq.commit();
    }
}

package com.els.services;


import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.client.Client;

import java.util.List;

public class IngestDataService {
	final static Logger log = Logger.getLogger(IngestDataService.class.getName());
	String index = "test_index";
	String el_type = "search";
	
    Client client;

    public IngestDataService(Client client,String index,String type) {
        this.client = client;
        this.index = index;
        this.el_type = type;
  
    }

    public void ingest(String doc,long count) {
   
        String id = client.prepareIndex(index, el_type ).setSource(doc).get().getId();
       log.log(Level.INFO, "ELS document id  : "+id);

    }


    public boolean ingest(String type, List<String> docs) {
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        return bulkRequest.get().hasFailures();

    }


}

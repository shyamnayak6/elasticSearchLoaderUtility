package com.els.els_uploader;

import com.els.services.IngestDataService;
import com.els.util.ESManager;
import com.els.util.IndexTestDataUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.elasticsearch.client.Client;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ELApp {
	final static Logger log = Logger.getLogger(ELApp.class.getName());

	public static void main(String[] args) throws FileNotFoundException, IOException {

		Properties properties = new Properties();
		properties.load(new FileInputStream("D:\\Projects\\conf\\els.properties"));
		properties.load(new FileInputStream("D:\\Projects\\conf\\log4j.properties"));
		PropertyConfigurator.configure(properties);

		String els_host = properties.getProperty("serverName");
		String clusterName = properties.getProperty("clusterName");
		int port = Integer.parseInt(properties.getProperty("port"));
		String indexName = properties.getProperty("index");
		String type = properties.getProperty("type");
		int count = Integer.parseInt(properties.getProperty("recordCount"));

		log.log(Level.INFO, "ELS ServerName : " + els_host + " ," + "port : " + port);
		log.log(Level.INFO, "ELS index : " + indexName + " ," + "type : " + type);
		ESManager esManager = new ESManager();
		Client client = esManager.getClient(els_host, port, clusterName);

		IngestDataService ingestService = new IngestDataService(client, indexName, type);


		JSONParser parser = new JSONParser();
		String document = null;
	 	
    	for(int i=0;i<count;i++){
		try {

			Object obj = parser.parse(new FileReader("D:\\Projects\\test_data\\els_template_test_data.txt"));
					JSONObject jsonObject = (JSONObject) obj;
					
			Object timestamp = jsonObject.get("timestamp");

			if (null != timestamp) {
				String index_date = indexName.split("-", 2)[1];
				String date_details[] = index_date.split("-");

				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.DATE, Integer.parseInt(date_details[2]));
				cal.set(Calendar.MONTH, Integer.parseInt(date_details[1]));
				cal.set(Calendar.YEAR, Integer.parseInt(date_details[0]));
				Date calender_date = cal.getTime();
				long epoch = calender_date.getTime();
				jsonObject.replace("timestamp", epoch);

			}

			if (indexName.startsWith("nuage_dpi_flowstats")) {
				String egressPackets = IndexTestDataUtil.getEgressPackets();
				String egressMB = IndexTestDataUtil.getEgressMB();
				jsonObject.replace("EgressPackets", egressPackets);
				jsonObject.replace("TotalPacketsCount", egressPackets);
				jsonObject.replace("EgressBytes", IndexTestDataUtil.getEgressBytes());
				jsonObject.replace("EgressMB", egressMB);
				jsonObject.replace("TotalMB", egressMB);

			} else if (indexName.startsWith("nuage_vlan")) {
				JSONArray jsonObj = (JSONArray) jsonObject.get("metric_info");
				
				int length = jsonObj.size();
				for(int j=0; j<length; j++) {
				  JSONObject jsonArryObj = (JSONObject) jsonObj.get(j);
				  jsonArryObj.replace("tx_bytes", IndexTestDataUtil.getTxRxBytes());
				  jsonArryObj.replace("tx_pkt_count", IndexTestDataUtil.getTxRxPacketCounts());
				  jsonArryObj.replace("rx_bytes", IndexTestDataUtil.getTxRxBytes());
				  jsonArryObj.replace("rx_pkt_count", IndexTestDataUtil.getTxRxPacketCounts());
				}
				

			} else if (indexName.startsWith("nuage_vport_qos")) {

				JSONArray jsonObj = (JSONArray) jsonObject.get("metric_info");
				
				int length = jsonObj.size();
				for(int j=0; j<length; j++) {
				  JSONObject jsonArryObj = (JSONObject) jsonObj.get(j);
				  jsonArryObj.replace("q10_bytes", IndexTestDataUtil.getTxRxBytes());
				  jsonArryObj.replace("q10_pkt_count", IndexTestDataUtil.getTxRxPacketCounts());
				  jsonArryObj.replace("q10_lended", IndexTestDataUtil.getTxRxBytes());
				  jsonArryObj.replace("q0_bytes", IndexTestDataUtil.getTxRxPacketCounts());
				  jsonArryObj.replace("q0_pkt_count", IndexTestDataUtil.getTxRxPacketCounts());
				}
			}

			document = jsonObject.toJSONString();

		} catch (Exception e) {
			log.log(Level.ERROR, e.getMessage());
		}
		ingestService.ingest(document, count);
		log.log(Level.INFO, "ELS document  : " + document);
    	}
    	log.log(Level.INFO, "All test data successfully uploaded to ELS ");
		client.close();

	}

}

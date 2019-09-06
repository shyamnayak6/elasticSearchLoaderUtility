package com.els.util;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.IOException;
import java.net.InetAddress;

public class ESManager {
	final static Logger log = Logger.getLogger(ESManager.class.getName());

	public Client getClient(String host, int port, String clusterName) {

		try {
			// Settings.Builder setting =
			// Settings.builder().put("client.transport.sniff", false);

			Client client = new PreBuiltTransportClient(
					Settings.builder().put("client.transport.sniff", true).put("cluster.name", clusterName).build())
							.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), port));

			// return Optional.of(client);
			return client;
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			return null;
		}
	}

	private String getElsDetails(String fileName) {
		ClassLoader classLoader = getClass().getClassLoader();
		String result = null;
		try {
			result = IOUtils.toString(classLoader.getResourceAsStream(fileName));
		} catch (IOException e) {
			log.error(e.getLocalizedMessage());
		}

		return result;
	}

}

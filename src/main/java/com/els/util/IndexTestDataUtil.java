package com.els.util;

import java.util.Random;
import java.util.UUID;

public class IndexTestDataUtil {
   
	
	public static String getEgressPackets(){
		int leftLimit = 600000;
	    int rightLimit = 700000;
	    int generatedInteger = leftLimit + (int) (new Random().nextFloat() * (rightLimit - leftLimit));
		return String.valueOf(generatedInteger);
	}
	
	public static String getEgressBytes(){
		int leftLimit = 600000;
	    int rightLimit = 800000;
	    int generatedInteger = leftLimit + (int) (new Random().nextFloat() * (rightLimit - leftLimit));
		return String.valueOf(generatedInteger);
	}
	
	public static String getEgressMB(){
		double leftLimit = 300D;
	    double rightLimit = 400D;
	    double generatedDouble = leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
		return String.valueOf(generatedDouble);
	}
	
	
	public static String getVlanId(){
		UUID uniqueKey = UUID.randomUUID();
		return uniqueKey.toString();
	}
	
	public static String getTxRxBytes(){
		int leftLimit = 20000;
	    int rightLimit = 30000;
	    int generatedInteger = leftLimit + (int) (new Random().nextFloat() * (rightLimit - leftLimit));
		return String.valueOf(generatedInteger);
	}
	
	public static String getTxRxPacketCounts(){
		int leftLimit = 200;
	    int rightLimit = 300;
	    int generatedInteger = leftLimit + (int) (new Random().nextFloat() * (rightLimit - leftLimit));
		return String.valueOf(generatedInteger);
	}
}

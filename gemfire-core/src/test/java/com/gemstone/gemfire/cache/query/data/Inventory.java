/*=========================================================================
 * Copyright (c) 2010-2014 Pivotal Software, Inc. All Rights Reserved.
 * This product is protected by U.S. and international copyright
 * and intellectual property laws. Pivotal products are covered by
 * one or more patents listed at http://www.pivotal.io/patents.
 *=========================================================================
 */
package com.gemstone.gemfire.cache.query.data;

import java.sql.*;
import java.io.*;

/*
 * author: Prafulla Chaudhari
 */

public class Inventory implements Serializable{
	
	public String cusip;//        CHAR(9)
	public String dealer_code;//  VARCHAR(10)
	public String price_type;//   CHAR(3)
	public double quote_price;//  DOUBLE
	public Timestamp quote_timestamp;// TIMESTAMP
	public int min_order_qty;// INTEGER
	public int max_order_qty;// INTEGER
	public int lower_qty;//    INTEGER
	public int upper_qty;//    INTEGER
	public int inc_order_qty;// INTEGER
	public int retail_price;// INTEGER
	public String is_benchmark_flag;// CHAR(1)
	public double yield_spread;// DOUBLE
	public String treasury_cusip;// VARCHAR(9)
	
	
	////////constructor of class Inventory
	
	protected String  [] tempArr;
	protected int i=0;
	protected String tempStr;
	protected int tempInt;
	protected double tempDouble;
	
	public Inventory(String inputStr){
		tempArr = inputStr.split(",");
		
		cusip = tempArr[i++].replaceAll("\"", " ").trim();//        CHAR(9)
		dealer_code = tempArr[i++].replaceAll("\"", " ").trim();//  VARCHAR(10)
		price_type = tempArr[i++].replaceAll("\"", " ").trim();//   CHAR(3)
		quote_price = (Double.valueOf(tempArr[i++].replaceAll("\"", " ").trim())).doubleValue();//  DOUBLE
		
		tempStr = tempArr[i++];
		if(!tempStr.equalsIgnoreCase("NULL")){
			quote_timestamp = Timestamp.valueOf(tempStr.replaceAll("\"", " ").trim());// TIMESTAMP
		}		
		
		tempStr = tempArr[i++];
		if(tempStr.equalsIgnoreCase("NULL")){
			tempInt = 0;
		} else{
			tempInt = (Integer.valueOf(tempStr.replaceAll("\"", " ").trim())).intValue();
		}
		min_order_qty = tempInt;// INTEGER
		
		tempStr = tempArr[i++];
		if(tempStr.equalsIgnoreCase("NULL")){
			tempInt = 0;
		} else{
			tempInt = (Integer.valueOf(tempStr.replaceAll("\"", " ").trim())).intValue();
		}		
		max_order_qty = tempInt;// INTEGER
		
		tempStr = tempArr[i++];
		if(tempStr.equalsIgnoreCase("NULL")){
			tempInt = 0;
		} else{
			tempInt = (Integer.valueOf(tempStr.replaceAll("\"", " ").trim())).intValue();
		}		
		lower_qty = tempInt;//    INTEGER		
		
		tempStr = tempArr[i++];
		if(tempStr.equalsIgnoreCase("NULL")){
			tempInt = 0;
		} else{
			tempInt = (Integer.valueOf(tempStr.replaceAll("\"", " ").trim())).intValue();
		}
		upper_qty = tempInt;//    INTEGER
		
		tempStr = tempArr[i++];
		if(tempStr.equalsIgnoreCase("NULL")){
			tempInt = 0;
		} else{
			tempInt = (Integer.valueOf(tempStr.replaceAll("\"", " ").trim())).intValue();
		}
		inc_order_qty = tempInt;// INTEGER
		
		tempStr = tempArr[i++];
		if(tempStr.equalsIgnoreCase("NULL")){
			tempInt = 0;
		} else{
			tempInt = (Integer.valueOf(tempStr.replaceAll("\"", " ").trim())).intValue();
		}
		retail_price = tempInt;// INTEGER
		
		is_benchmark_flag = tempArr[i++].replaceAll("\"", " ").trim();;// CHAR(1)
		
		tempStr = tempArr[i++];
		if(tempStr.equalsIgnoreCase("NULL")){
			tempDouble = 0;
		} else{
			tempDouble = (Double.valueOf(tempStr.replaceAll("\"", " ").trim())).doubleValue();
		}
		yield_spread = tempDouble;// DOUBLE
		
		treasury_cusip = tempArr[i++].replaceAll("\"", " ").trim();// VARCHAR(9)
		
	}//end of Inventory constructor

}//end of class

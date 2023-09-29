package utils;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.swing.JTextArea;

public class Constants {
	public static String Format="querystring";
	public static String ReadFileName="";
	public static String Url="";
	public static String Sender="";
	public static String Text="";
	public static int BatchSize=100;
	public static String EntityId="";
	public static String Template="";
	public static String TemplateId="";
	public static String Other="";
	public static int RetryCount =0;
	public static double deliveredPercent=0;
	public static double failedPercent =0;
	public static String Urltrack ="";
	public static String CustRef ="";
	public static String Tag ="";
	public static String DlrReq ="";
	public static String TemplateValues="";
	public static String Jsontemplateid="";
	public static String MsgType="Plain";
	public static String Key="";
	public static HashMap<String,String> mapConInfo=new HashMap<String,String>();
	public static HashMap<String,String> errorcodeMap=new HashMap<String,String>();
	public static List<String> errorcodes = new ArrayList<String>();
	public static ConcurrentHashMap<Integer, Connection>connection=new ConcurrentHashMap<Integer, Connection>(1,0.9f,1);
	public static ConcurrentHashMap<Integer, Connection>DbConnection=new ConcurrentHashMap<Integer, Connection>(1,0.9f,1);
	public static ConcurrentLinkedQueue<String> consolelog=new ConcurrentLinkedQueue<String>();
	public static ConcurrentLinkedQueue<String> updateBucket=new ConcurrentLinkedQueue<String>();
	public static ConcurrentHashMap<String, ConcurrentHashMap<String , Integer> > stats = new ConcurrentHashMap<String, ConcurrentHashMap<String , Integer> >();
	public static ConcurrentHashMap<String, ConcurrentHashMap<String , Integer> > senderStats = new ConcurrentHashMap<String, ConcurrentHashMap<String , Integer> >();
	//public static ConcurrentHashMap<String, ConcurrentLinkedQueue<ShnPromoTranssmppSmsDetails>> DataBucket = new ConcurrentHashMap<String, ConcurrentLinkedQueue<ShnPromoTranssmppSmsDetails>>();
	public static int dbConNum =10;
	public static JTextArea textAreaResponse;
	public static String postfix ="";
	public static String UserName="";
	public static int totalcount=0;
	public static int profileid =0;
	public static int  conNum=5;
	public static boolean keepRunning;
	public static String jsontype1format ="{\n"
			+ " \"ver\": \"2.0\",\n"
			+ " \"key\": \"xxxxxxxxxxxxx999xxxxx\",\n"
			+ " \"encrypt\": \"0\",\n"
			+ " \"sch_at\": \"yyyy-MM-dd HH:mm:ss\",\n"
			+ " \"messages\": [\n"
			+ " {\n"
			+ " \"dest\": [ \n"
			+ " \"12345\",\n"
			+ " \"67890\",\n"
			+ " \"43567646\",\n"
			+ " \"5437548\"\n"
			+ " ],\n"
			+ " \"text\": \"Sample Text 1\",\n"
			+ " \"send\": \"Send 1\",\n"
			+ " \"type\": \"PM\",\n"
			+ " \"dlt_entity_id\":\"1234567\",\n"
			+ " \"dlt_template_id\":\"987654321\",\n"
			+ " \"dlr_req\": \"1\",\n"
			+ " \"template_id\": \"12\",\n"
			+ " \"template_values\": [\"4617\",\"OTP\"],\n"
			+ " \"urltrack\": \"1\",\n"
			+ " \"cust_ref\": \"34343\",\n"
			+ " \"tag\": \"tag\",\n"
			+ " }\n"
			+ " ]\n"
			+ "}";
	public static String jsontype2Format="{\n"
			+ "    \"request\": [\n"
			+ "        {\n"
			+ "            \"destination\": \"9359353838\",\n"
			+ "            \"sender\": \"MSEDCL\",\n"
			+ "            \"priority\": \"1\",\n"
			+ "            \"cust-ref-number\": \"nsve3-r63wsu6-e272he8-7\",\n"
			+ "            \"templateid\": \"477031\",\n"
			+ "            \"F1\": \"330243211701\"\n"
			+ "        },\n"
			+ "        {\n"
			+ "            \"destination\": \"7052711365\",\n"
			+ "            \"sender\": \"MSEDCL\",\n"
			+ "            \"priority\": \"1\",\n"
			+ "            \"cust-ref-number\": \"shau-w64873ee-272382i-u827323\",\n"
			+ "            \"templateid\": \"476728\",\n"
			+ "            \"F1\": \"330243211701\",\n"
			+ "            \"F2\": \"1000\",\n"
			+ "            \"F3\": \"20/03/2023\"\n"
			+ "        }\n"
			+ "    ]\n"
			+ "}";
	

}

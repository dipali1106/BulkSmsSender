package org.threads;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.main.SenderUi;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import utils.Constants;

public class FileReaderJsonThread extends Thread{
	SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public boolean ServiceStatus=false;
	public boolean isRunning = true;
	String mobile="";
	int stopCount =0;
	List <String> batchNumbers;
	int PoolSize = Constants.mapConInfo.get("PoolSize") != null ? Integer.parseInt(Constants.mapConInfo.get("PoolSize")):5;
	OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).build();	
	public static AtomicInteger processCount = new AtomicInteger(0);
	private  ExecutorService executor = Executors.newFixedThreadPool(PoolSize);
	int dataCount=0;
	int retry=0;
	String key;
	String encodedKey;
	@SuppressWarnings("unchecked")
	
	public void run() {
		while (isRunning) {
			File file = new File(Constants.ReadFileName);
			String str[] = Constants.TemplateValues.split(",");
				
			JSONObject object = new JSONObject();
			
			JSONArray requestArray = new JSONArray();
			if(Constants.Format.equalsIgnoreCase("jsontype1")) {
				key= Constants.UserName+":"+Constants.Key;
				System.out.println(key);
				encodedKey = Base64.getEncoder().encodeToString(key.getBytes());
				object.put("ver", "2.0");
				object.put("key", encodedKey);
				object.put("encrypt", "0");
				object.put("sch_at", dtFormat.format(new Date()));
				JSONArray message = new JSONArray();
				JSONObject messageOneElement = new JSONObject();
				messageOneElement.put("text", Constants.Text);
				messageOneElement.put("send", Constants.Sender);
				messageOneElement.put( "type", Constants.MsgType);
				messageOneElement.put("dlt_entity_id", String.valueOf(Constants.EntityId));
				messageOneElement.put("dlt_template_id",String.valueOf( Constants.Template));
				messageOneElement.put( "dlr_req", Constants.DlrReq);		
				messageOneElement.put("urltrack", Constants.Urltrack);
				messageOneElement.put("cust_ref", Constants.CustRef);
				messageOneElement.put( "tag", Constants.Tag);	
				JSONArray messagetemplatevalues = new JSONArray();
				for(int i=0; i< str.length;i++) {
					messagetemplatevalues.add(str[i]);
				}
				messageOneElement.put("template_values", messagetemplatevalues);
				message.add(messageOneElement);
				object.put("messages", message);
			}
			else {
				object.put("sender", Constants.Sender);
				object.put("priority", 5);
				object.put("templateid", String.valueOf(Constants.Template));
				JSONArray messagetemplatevalues = new JSONArray();
				int j=1;
				for(int i=0; i< str.length;i++) {
					object.put("F"+j, str[0]);
					j++;
				}				
			}			
			//System.out.println(object.toJSONString());
			JSONArray destination = new JSONArray();
			try (BufferedReader br = new BufferedReader(new FileReader(file)))
			{
		        List<String> lines = br.lines().skip(0).collect(Collectors.toList());
		        if(lines.size()>0) {
		        	//System.out.println();
		        	while(retry<= Constants.RetryCount) {
		        		batchNumbers = new ArrayList<String>();
		        		for(String number: lines) {
			        		if(processCount.get()>PoolSize+2) {
			        			Thread.sleep(1000);
			        		}
			        		if(dataCount== Constants.BatchSize ) {        			
			        			dataCount=0;
			        			if(Constants.Format.equalsIgnoreCase("jsontype1")) {
			        				batchNumbers.add(number);
			        				destination.addAll(batchNumbers);
			        				object.put("dest", destination);
			        				SenderUi.txtpnJsonpayload.setText(object.toJSONString().replace(",", ",\n"));
				        			System.out.println(object.toJSONString());
				        			executor.execute(PostJsonToUrl(object.toJSONString(), Constants.Url, 0));
				        			processCount.incrementAndGet();
				        			//System.out.println(url);
				        			batchNumbers.clear();
			        			}
			        			else {
			        				object.put("destination", number);
			        				object.put("cust-ref-number", UUID.randomUUID());
			        				requestArray.add(object);
			        				JSONObject objecttype2 = new JSONObject();
			        				objecttype2.put("request", requestArray);
			        				executor.execute(PostJsonToUrl(objecttype2.toJSONString(), Constants.Url, 0));
			        				SenderUi.txtpnJsonpayload.setText(objecttype2.toJSONString().replace(",", ",\n").replace("{", "{\n").replace("[", "[\n"));
				        			processCount.incrementAndGet();
				        			requestArray.clear();
			        			}
			        		}
			        		else {
			        			if(Constants.Format.equalsIgnoreCase("jsontype1")) {
			        				batchNumbers.add(number);
				        			dataCount++;
			        			}
			        			else {
			        				object.put("destination", number);
			        				object.put("cust-ref-number", UUID.randomUUID());
			        				requestArray.add(object);
			        				dataCount++;
			        			}
			        		}
			        	}
		        		if(dataCount>0) {
		        			dataCount=0;
		        			destination.addAll(batchNumbers);
		        			object.put("dest", destination);
		        			JSONObject Obj = new JSONObject();
		        			Obj.put("request", object);
		        			executor.execute(PostJsonToUrl(object.toJSONString(),Constants.Url,  0));
		        			processCount.incrementAndGet();
		        			//System.out.println(url);
		        			batchNumbers.clear();
		        		}
		        		retry++;
		        	}
		        	
		        	lines.clear();
		        }
		        isRunning =false;
		        SenderUi.txtrLog.append("Mobile number Data Pushed.......");
	        } catch (IOException e) {
		            e.printStackTrace();
		        } catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
		}
		
	}
	
	
	private Runnable PostJsonToUrl(String jsonString, String baseUrl, final int c) {
		return new Runnable() {
			private int count;
			@Override
			public void run() {
				 count =c;
				// TODO Auto-generated method stub
				//int ProfileId = Integer.parseInt(str[0]);
				if(count<2 ) {
										
					RequestBody body = RequestBody.create(
					MediaType.parse("application/json"), jsonString);
					Request get = new Request.Builder().url(baseUrl ).post(body).build();
					long timebeforeRequest = System.currentTimeMillis();		
					client.newCall(get).enqueue(new Callback() {
							@Override
							public void onFailure(Call call, IOException e) {
								
				                apiError( 0,e.getMessage()); 
							}

							@Override
							public void onResponse(Call call, Response response) {
								
								try {
									 ResponseBody responseBody = response.body();						
									 if (!response.isSuccessful()) {
					                        throw new IOException("Unexpected code " + response);
					                     }
									 if(responseBody!= null) {
										 String myData = responseBody.string().trim();
										 long timeAfterResponse = System.currentTimeMillis();
					                     int responseTime = (int) (timeAfterResponse - timebeforeRequest);
					                     //System.out.println(" Sr No here is "+SrNo);
					                     apiResponse(baseUrl,  myData,responseTime);
									 }
								}
								catch(IOException io) {
									executor.execute(PostJsonToUrl( jsonString , baseUrl, ++count ));
									// apiError(Key, 0,io.getMessage()); 
								}catch(Exception e) {
									e.printStackTrace();
									Constants.consolelog.add(new StringBuilder().append("Exception Occured On FileReaderJsonThread.class (PostJsonToUrl Runnable) : ").append(e.getMessage()).toString());
								}

							}
				        });
				}
				else {
					
					processCount.decrementAndGet();
				}
			}
			
			public void apiResponse(  String apiUrl, String responsData, int responseme ) {
				
				try {
					Constants.textAreaResponse.append("responseTime: "+responseme+" Response: " +responsData+"\n");
						
					processCount.decrementAndGet();
				}
				catch(Exception e) {
					e.printStackTrace();
					Constants.consolelog.add(new StringBuilder().append("Exception Occured On FileReaderJsonThread.class (apiResponse Method) : ").append(e.getMessage()).toString());
				}	
			}
			
			public void apiError(  int code, String message) {
				try {
					//long currentTime = System.currentTimeMillis();
					System.out.println(" In Error Code"+ code);		
					processCount.decrementAndGet();
				}
				catch(Exception e) {
					e.printStackTrace();
					Constants.consolelog.add(new StringBuilder().append("Exception Occured On FileReaderJsonThread.class (apiError Method) : ").append(e.getMessage()).toString());
				}			
			}
		
		};
		
	
	}
}

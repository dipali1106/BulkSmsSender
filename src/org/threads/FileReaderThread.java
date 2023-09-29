package org.threads;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.main.SenderUi;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import utils.Constants;

public class FileReaderThread extends Thread {
	public boolean ServiceStatus=false;
	public boolean isRunning = true;
	String mobile="";
	int stopCount =0;
	StringBuilder sb=null;
	int retry=0;
	public static AtomicInteger processCount = new AtomicInteger(0);
	int PoolSize = Constants.mapConInfo.get("PoolSize") != null ? Integer.parseInt(Constants.mapConInfo.get("PoolSize")):5;
	private  ExecutorService executor = Executors.newFixedThreadPool(PoolSize);
	OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(15, TimeUnit.SECONDS).readTimeout(15, TimeUnit.SECONDS).build();						

	int dataCount=0;
	public void run() {
		while(isRunning) {
			if(ServiceStatus) {
				stopCount=0;
				
				if(stopCount==0) {
					isRunning=false;
				}
			}
			sb= new StringBuilder();
			File file = new File(Constants.ReadFileName);
			Constants.Url = Constants.Url+ Constants.Other;
			String url = Constants.Url.replace( "##sender##" , Constants.Sender ).replace("##text##", Constants.Text).replace("##entityid##", String.valueOf(Constants.EntityId)).replace("##templateid##", String.valueOf(Constants.Template));			
	        try (BufferedReader br = new BufferedReader(new FileReader(file)))
	        {
		        List<String> lines = br.lines().skip(0).collect(Collectors.toList());
		        if(lines.size()>0) {
		        	//System.out.println();
		        	while(retry<= Constants.RetryCount) {
		        		for(String number: lines) {
			        		if(processCount.get()>=PoolSize+3) {
			        			Thread.sleep(1000);
			        		}
			        		if(ServiceStatus) {
			        			lines.clear();
			        			break;
			        		}
			        		if(dataCount== Constants.BatchSize  ) {        			
			        			sb.append(number);
			        			dataCount=0;
			        			
			        			url = url.replace("##mobile##", sb.toString());
			        			executor.execute(PostQueryStringUrl(url, 0));
			        			//System.out.println(url);
			        			processCount.incrementAndGet();
			        		}
			        		else {
			        			sb.append(number).append(",");
			        			dataCount++;
			        		}
			        	}
		        		if(dataCount>0) {
		        			
		        			dataCount=0;
		        			url = url.replace("##mobile##", sb.toString());
		        			executor.execute(PostQueryStringUrl(url, 0));
		        			System.out.println(url);
		        			processCount.incrementAndGet();
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
	

	public Runnable PostQueryStringUrl(String apiUrl,    int loopCount ) {
		return new Runnable() {
			int count =loopCount;
			@Override
			public void run() { 
				//int ProfileId = Integer.parseInt(str[0]);
				
				if(count<2 ) {
					Request get = new Request.Builder().url(apiUrl ).build();
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
										 response.close();
					                     responseBody.close();
					                       throw new IOException("Unexpected code " );
					                     }
									 if(responseBody!= null) {
										 String myData = responseBody.string().trim();
										 long timeAfterResponse = System.currentTimeMillis();
					                     int responseTime = (int) (timeAfterResponse - timebeforeRequest);
					                     //System.out.println(" Sr No here is "+SrNo);
					                     apiResponse(apiUrl,  myData,responseTime);
					                     response.close();
					                     responseBody.close();
									 }
								}
								catch(IOException io) {
									executor.execute(PostQueryStringUrl( apiUrl, ++count ));
									// apiError(Key, 0,io.getMessage()); 
								}catch(Exception e) {
									e.printStackTrace();
									Constants.consolelog.add(new StringBuilder().append("Exception Occured On FileReaderThread.class (PostUrlThread Runnable) : ").append(e.getMessage()).toString());
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
					//System.out.println(" Response is==="+responsData);
					//Constants.responseLog.add(new StringBuilder(apiUrl).append(";   Response: ").append(responsData).append("\n").toString());
						
					processCount.decrementAndGet();
				}
				catch(Exception e) {
					e.printStackTrace();
					Constants.consolelog.add(new StringBuilder().append("Exception Occured On FileReaderThread.class (apiResponse Method) : ").append(e.getMessage()).toString());
				}	
			}
			
			public void apiError(  int code, String message) {
				
				//int ProfileId = Integer.parseInt(str[0]);
				try {
					//long currentTime = System.currentTimeMillis();
					System.out.println(" In Error Code"+ code);		
					
						//Constants.TotalBufferedRecord.decrementAndGet();
						processCount.decrementAndGet();
				}
				catch(Exception e) {
					e.printStackTrace();
					Constants.consolelog.add(new StringBuilder().append("Exception Occured On PushReportThread.class (apiError Method) : ").append(e.getMessage()).toString());
				}			
			}
		
		};
	}


}

package org.threads;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.main.SenderUi;

import utils.Constants;

public class LoggingThread extends Thread {
	SimpleDateFormat dtFormat1 = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat dtFormat = new SimpleDateFormat("HH");	
	long timestamp;
	String filePath="";
	String updatefilePath="";
	public void run() {
//		/System.out.println("LogFileUpdationThread started");
		timestamp=System.currentTimeMillis();
		while(Constants.keepRunning) {
			try {
				Thread.sleep(500);				
				if(Constants.consolelog.size()>0) {					
					//System.out.println("In generate error");
					generateErrorReport();
				}
				
		
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private Boolean checkEmpty() {
		if(Constants.consolelog.size()==0) {	
			
			return true;
		}
		else {
			
			return false;
		}
			
		}
	

	private void generateErrorReport() {
		String data="";
		
		 Date date = new Date();
		 String projPath="";
		try {
			projPath = new StringBuilder().append(new File("").getCanonicalPath()).append(File.separator).append("errorlogs").append(File.separator).append(dtFormat1.format(date)).append(File.separator).toString();
		} catch (IOException e1) {
			e1.printStackTrace();
		}		 		
		if(checkEmpty()) {
		}
		else {				
			 File directory = new File(projPath); 
			 if (!directory.exists()) {
				// System.out.println("Directory not exist");
				 directory.mkdirs();
			 }				  
			 try {				 
				 File file = new File(projPath+"LogHour_"+dtFormat.format(date)+".txt") ;
				 if(!file.exists()){			
						try {
							file.createNewFile();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}						
					}					 				 
				 BufferedWriter out;										
				 out = new BufferedWriter(new FileWriter(file,true));
					while(!Constants.consolelog.isEmpty()) {
						 data = Constants.consolelog.peek();
						SenderUi.txtrLog.append(data+"\n");//setText(data);
						out.write(data+" \n\n");
						Constants.consolelog.remove(data);
					}

					out.close();	
			 }
			 catch(IOException ie) {
				 System.out.println("Error in creating errorlog file.");
					 ie.printStackTrace();
			 }
		
		}
	
	}
	
}

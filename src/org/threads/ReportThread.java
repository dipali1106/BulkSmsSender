package org.threads;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.main.SenderUi;

import connection.MasterConnection;
import utils.Constants;

public class ReportThread extends Thread {
	public boolean ServiceStatus=false;
	public boolean isRunning = true;
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
	String insertPrefix ="INSERT INTO `sms_statsmaster`(`ProcessDate`, `HourNum`, `DayNum`, `MonthNum`, `YearNum`, `DeliveredSmsCount`, `DeliveredSmsCredit`, `FailedSmsCount`, `FailedSmsCredit`) VALUES ('";
	String countQuery = "SELECT count(MessageSrNo) as c FROM `shnpromotranssmppsmsdetails_"+Constants.postfix+"` WHERE ReportCode =0 AND SenderId ='"+Constants.Sender+"'";
	String deliveredQuery ="";//"UPDATE `shnpromotranssmppsmsdetails_"+Constants.postfix+"` SET `DelCode`='000',`DelStatus`='DELIVERED',`DelReceipt_Time`='nbv',`DlrDoneDate`='',`ErrorDescription`='DELIVERED',`ReportCode`=1 WHERE `ReportCode`=0 ORDER BY ReportCode LIMIT 2";
	String failedQuery ="UPDATE `shnpromotranssmppsmsdetails_"+Constants.postfix+"` SET `DelCode`='000',`DelStatus`='Failed',`DelReceipt_Time`='',`DlrDoneDate`='',`ErrorDescription`='DELIVERED',`ReportCode`=2 WHERE `ReportCode`=0 AND SenderId ='"+Constants.Sender +"' AND ORDER BY ReportCode LIMIT 2";   
	StringBuilder deliveredSb = null;
	StringBuilder failedSb = null;
	StringBuilder insertsb = null;
	public void run() {
		Statement stmt =null;
		ResultSet rs = null;
		int count =0;
		int maxCount=50;
		int deliveredCount=0;
		int failedCount=0;
		int randomNum=0;
		int dCount=0;
		String Key;
		Random r = new Random();
		int fCount=0;
		int MonthNum=0;
		String StatsKey="";
		String SenderKey ="";
		getErrorCode();
		while(Constants.DbConnection.get(0)== null) {
			MasterConnection.getDataSqlConnection(0);
		}
		try {
			stmt = Constants.DbConnection.get(0).createStatement();
			//System.out.println(countQuery);
			rs = stmt.executeQuery(countQuery);
			while(rs.next()) {
				count = rs.getInt("c");
			}
			if(count>0) {
				System.out.println(" Total row with status 0 in table="+ count);
				deliveredCount = (int) Math.ceil(count*Constants.deliveredPercent/100);
				failedCount = (int) Math.ceil(count*Constants.failedPercent/100);
				int loopcounter = (int) Math.ceil(deliveredCount/maxCount) ;
				Calendar calendar = Calendar.getInstance();
			    calendar.setTime(new Date());
			    MonthNum = calendar.get(Calendar.MONTH)+1;
			    System.out.println(" delivereedCount  ="+deliveredCount+" failedCount= "+failedCount);
				while(loopcounter >0 ) {
					if(deliveredCount-dCount< maxCount) {
						deliveredSb = new StringBuilder("UPDATE `shnpromotranssmppsmsdetails_").append(Constants.postfix).append("` SET `DelCode`='000',`DelStatus`='DELIVERED',`DelReceipt_Time`='").append(format.format(new Date())).append("',`DlrDoneDate`='").append(format.format(new Date())).append("',`ErrorDescription`='DELIVERED',`ReportCode`=1 WHERE `ReportCode`=0 AND SenderId ='").append(Constants.Sender).append("' ORDER BY ReportCode LIMIT ").append(deliveredCount-dCount);
					}
					else {
						deliveredSb = new StringBuilder("UPDATE `shnpromotranssmppsmsdetails_").append(Constants.postfix).append("` SET `DelCode`='000',`DelStatus`='DELIVERED',`DelReceipt_Time`='").append(format.format(new Date())).append("',`DlrDoneDate`='").append(format.format(new Date())).append("',`ErrorDescription`='DELIVERED',`ReportCode`=1 WHERE `ReportCode`=0 AND SenderId ='").append(Constants.Sender).append("' ORDER BY ReportCode LIMIT ").append(maxCount);
					}			 
				     deliveredQuery =deliveredSb.toString();
					Constants.updateBucket.add(deliveredQuery);				   
					dCount+=maxCount;
					loopcounter--;
				}
				StatsKey = Constants.profileid+"##"+format2.format(new Date())+"##"+calendar.get(Calendar.HOUR_OF_DAY);
				SenderKey =  Constants.profileid+"##"+Constants.Sender+"##"+format2.format(new Date())+"##"+calendar.get(Calendar.HOUR_OF_DAY);
				ConcurrentHashMap<String, Integer> list1 ; 
				
				if(Constants.stats.get(StatsKey)== null) {
					 list1 = new ConcurrentHashMap<String, Integer>();
					list1.put("delivered", deliveredCount);
					list1.put("failed", 0);
					Constants.stats.put(StatsKey, list1 );
				}
				else {
					list1 = Constants.stats.get(StatsKey);
					int tempd = list1.get("delivered");
					list1.put("delivered", tempd+deliveredCount);
					Constants.stats.put(StatsKey, list1 );
				}
				if(Constants.senderStats.get(SenderKey)== null) {
					 list1 = new ConcurrentHashMap<String, Integer>();
					list1.put("delivered", deliveredCount);
					list1.put("failed", 0);
					Constants.senderStats.put(SenderKey, list1 );
				}
				else {
					list1 = Constants.senderStats.get(SenderKey);
					int tempd = list1.get("delivered");
					list1.put("delivered", tempd+deliveredCount);
					Constants.senderStats.put(SenderKey, list1 );
				}
				while(fCount<failedCount ) {
					failedSb = new StringBuilder("UPDATE `shnpromotranssmppsmsdetails_").append(Constants.postfix).append("` SET `DelCode`='" );
					int low=0;
					randomNum = r.nextInt(Constants.errorcodeMap.size()-1 -low) + low;	

					Key = Constants.errorcodes.get(randomNum);
					if(failedCount-fCount>=maxCount) {
						failedSb.append(Key).append("',`DelStatus`='Failed',`DelReceipt_Time`='").append(format.format(new Date())).append("',`DlrDoneDate`='").append(format.format(new Date())).append("',`ErrorDescription`='").append(Constants.errorcodeMap.get(Key)).append("',`ReportCode`=2 WHERE `ReportCode`=0 AND SenderId ='").append(Constants.Sender).append("' ORDER BY ReportCode LIMIT ").append(maxCount);
						Constants.updateBucket.add(failedSb.toString());
						fCount+=maxCount;
					}
					else {
						failedSb.append(Key).append("',`DelStatus`='Failed',`DelReceipt_Time`='").append(format.format(new Date())).append("',`DlrDoneDate`='").append(format.format(new Date())).append("',`ErrorDescription`='").append(Constants.errorcodeMap.get(Key)).append("',`ReportCode`=2 WHERE `ReportCode`=0 AND SenderId ='").append(Constants.Sender).append("' ORDER BY ReportCode LIMIT ").append(failedCount-fCount);
						Constants.updateBucket.add(failedSb.toString());
						fCount+=maxCount;
						break;
					}
				}				
			     calendar.setTime(new Date());
			     MonthNum = calendar.get(Calendar.MONTH)+1;
				 StatsKey = Constants.profileid+"##"+format2.format(new Date())+"##"+calendar.get(Calendar.HOUR_OF_DAY)+"##"+calendar.get(Calendar.DAY_OF_MONTH)+"##"+MonthNum+"##"+calendar.get(Calendar.YEAR);
				 if(Constants.stats.get(StatsKey)== null) {
					list1 = new ConcurrentHashMap<String, Integer>();
					list1.put("delivered", 0);
					list1.put("failed", failedCount);
					Constants.stats.put(StatsKey, list1 );
				}
				else {
					list1 = Constants.stats.get(StatsKey);				
					int tempf = list1.get("failed");
					list1.put("failed", tempf+failedCount);
					Constants.stats.put(StatsKey, list1 );
				}
				 if(Constants.senderStats.get(StatsKey)== null) {
						list1 = new ConcurrentHashMap<String, Integer>();
						list1.put("delivered", 0);
						list1.put("failed", failedCount);
						Constants.senderStats.put(StatsKey, list1 );
					}
					else {
						list1 = Constants.senderStats.get(StatsKey);				
						int tempf = list1.get("failed");
						list1.put("failed", tempf+failedCount);
						Constants.senderStats.put(StatsKey, list1 );
					}
			}
			else {
				SenderUi.txtrLog.setText("No record found to process");
			}
			
			
			
						
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				stmt.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}
	

	public void getErrorCode() {
		String query ="SELECT `ErrorCode`, `ErrorDescription` FROM `sms_errorcodemaster` WHERE 1";
		Statement st=null;
		ResultSet rs=null;
		while(Constants.DbConnection.get(1)== null) {
			MasterConnection.getDataSqlConnection(1);
		}
		try {
			st=  Constants.DbConnection.get(1).createStatement();
			rs = st.executeQuery(query);
			while(rs.next()) {
				Constants.errorcodes.add(rs.getString("ErrorCode"));
				Constants.errorcodeMap.put(rs.getString("ErrorCode"), rs.getString("ErrorDescription"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

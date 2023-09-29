package org.threads;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLNonTransientConnectionException;
import java.sql.Statement;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.main.SenderUi;

import com.mysql.cj.jdbc.exceptions.CommunicationsException;

import connection.MasterConnection;
import utils.Constants;

public class UpdationThread extends Thread{
	public boolean ServiceStatus=false;
	int n=2;
	Statement stmt=null;
	 String query ="";
	int dataCount=0;
	ConcurrentHashMap <String, Integer> smsCountMap;
	public void run() {
		while(Constants.keepRunning) {
			try {
				
				if(Constants.updateBucket.size()>0) {
					System.out.println("Size=="+Constants.updateBucket.size());
					if(n>Constants.conNum)n=0;
					while(Constants.DbConnection.get(n)== null) {
						MasterConnection.getDataSqlConnection(n);
					}
					 while(!Constants.updateBucket.isEmpty()) {	
						 stmt = Constants.DbConnection.get(n).createStatement();
						 query = Constants.updateBucket.peek();	
						// System.out.println(query);
						 stmt.executeUpdate(query);
						 Constants.updateBucket.remove(query);
						 dataCount++;
					 }
					 SenderUi.txtrLog.append("Total "+dataCount +" Query executed..");
					 saveCdrStatsData();
				}
				else {
					Thread.sleep(2000);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			finally {
				try {
					if(stmt!=null) {stmt.close();}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public void saveCdrStatsData() {
		int delivered=0;
		int failed=0;
		if(Constants.stats.size()>0) {
			for(Entry<String, ConcurrentHashMap<String, Integer>> pair : Constants.stats.entrySet() ) {
				 smsCountMap =pair.getValue();
				 delivered = smsCountMap.get("delivered");
				 failed = smsCountMap.get("failed");
				 Constants.textAreaResponse.append("Delivered: "+delivered+" failed : "+failed);
				 System.out.println(delivered+" failed =="+failed);
				if(delivered >0 || failed >0) {
					if(n>=Constants.conNum)n=0;
					if(getInsertOrUpdateStats(pair.getKey(), delivered, failed, n)== true) {
						if(getInsertOrUpdateSenderStats(pair.getKey(), delivered, failed, n) ) {
							//pair.getValue().get("delivered")-successCount;
							pair.getValue().put("delivered", pair.getValue().get("delivered")-delivered);
							pair.getValue().put("failed", pair.getValue().get("failed")-failed);
						}
					}
					
					
				}
			}
		}
	}
	
	public boolean getInsertOrUpdateStats(final String key, int successCount, int failedCount , int x) {
		boolean status =false;
		Statement smt=null;
		ResultSet result=null;
		int srNo=0;
		String query;
		//System.out.println(key);
		String keyStr[] = key.split("##");
		String profileid = keyStr[0];
		String SummaryDate = keyStr[1];
		String dateStr[]= SummaryDate.split("-");
		int HourNum = Integer.parseInt(keyStr[2]);
		int YearNum = Integer.parseInt(dateStr[0]);
		int MonthNum = Integer.parseInt(dateStr[1]);
		int DayNum = Integer.parseInt(dateStr[2]);
		query = new StringBuilder().append("SELECT `SrNo` FROM `sms_statsmaster` WHERE `UserId` = ").append(Integer.parseInt(profileid)).append(" AND `ProcessDate` = '").append(SummaryDate).append("' AND `HourNum` = ").append(HourNum).toString();
		//System.out.println(query);
		try {
			while(Constants.connection.get(x)== null) {
				MasterConnection.getMasterSqlConnection(x);
			}
			smt=Constants.connection.get(x).createStatement();
			result = smt.executeQuery(query);
			if(result.next()) {
				srNo = result.getInt("SrNo");
			}
			result.close();
			if(srNo!= 0) {
				query = "UPDATE `sms_statsmaster` SET  `DeliveredSmsCount` = `DeliveredSmsCount`+"+successCount+", `DeliveredSmsCredit` = `DeliveredSmsCredit`+"+successCount+", `FailedSmsCount` = `FailedSmsCount`+"+failedCount+", `FailedSmsCredit` =  `FailedSmsCredit`+"+failedCount+" WHERE `SrNo` ="+srNo;
			}
			else {
				
				query = new StringBuilder().append("INSERT INTO `sms_statsmaster` (`ProcessDate`, `UserId`, `HourNum`, `DayNum`, `MonthNum`, `YearNum`, `DeliveredSmsCount`, `DeliveredSmsCredit`, `FailedSmsCount`, `FailedSmsCredit`) VALUES ( '").append(SummaryDate).append("',").append(Constants.profileid).append(",").append(HourNum).append(",")
							.append(DayNum).append(",").append(MonthNum).append(",").append(YearNum).append(",").append(successCount).append(",").append(successCount).append(",").append(failedCount).append(",").append(failedCount).append(")").toString();
			}
			//System.out.println(query);
			smt.executeUpdate(query);
			status = true;
			
		} catch(SQLNonTransientConnectionException se){
			try{
				Thread.sleep(1000);
				MasterConnection.getMasterSqlConnection(x);
			}
			catch(Exception e1){
				e1.printStackTrace();
			}
		}
		catch(CommunicationsException com){
			try{
				Thread.sleep(1000);
				Constants.consolelog.add(new StringBuilder().append("CommunicationsException Occured On UpdateMemoryThread.class (getInsertOrUpdateStats Method) : ").append(com.getMessage()).toString());
				MasterConnection.getMasterSqlConnection(x);
			}
			catch(Exception e1){
				e1.printStackTrace();
			}
		}
		catch(SQLException sql){
			if(sql.getMessage().equalsIgnoreCase("No operations allowed after statement closed.") || sql.getMessage().equalsIgnoreCase("No operations allowed after statement closed") || sql.getMessage().equalsIgnoreCase("Could not retrieve transaction read-only status from server")) {
				try{
					Thread.sleep(1000);
					MasterConnection.getMasterSqlConnection(x);
				}
				catch(Exception e1){
					e1.printStackTrace();
				}
			}
			else{
				//sql.printStackTrace();
				Constants.consolelog.add(new StringBuilder().append("SQLException Occured On UpdateMemoryThread.class (getInsertOrUpdateStats Method) : ").append(sql.getMessage()).toString());
			}
		}
		catch(NullPointerException np){
			try{
				Thread.sleep(1000);
				MasterConnection.getMasterSqlConnection(x);
			}
			catch(Exception e1){
				e1.printStackTrace();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			Constants.consolelog.add(new StringBuilder().append("Exception Occured On UpdateMemoryThread.class (getInsertOrUpdateStats Method) : ").append(e.getMessage()).toString());
		}
		finally {
			try {
				if(result!=null) result.close();
				if(smt!=null) smt.close();
			}
			catch(Exception ex) {}
		}
		return status;
		
	}

	public boolean getInsertOrUpdateSenderStats(final String key, int successCount, int failedCount , int x) {
		boolean status =false;
		Statement smt=null;
		ResultSet result=null;
		int srNo=0;
		String query;
		//System.out.println(key);
		String keyStr[] = key.split("##");
		String profileid = keyStr[0];
		String SummaryDate = keyStr[1];
		String dateStr[]= SummaryDate.split("-");
		int HourNum = Integer.parseInt(keyStr[2]);
		int YearNum = Integer.parseInt(dateStr[0]);
		int MonthNum = Integer.parseInt(dateStr[1]);
		int DayNum = Integer.parseInt(dateStr[2]);
		
		query = new StringBuilder().append("SELECT `SrNo` FROM `sms_senderstatsmaster` WHERE `UserId` = ").append(Integer.parseInt(profileid)).append(" AND Sender = '").append(Constants.Sender).append("' AND `ProcessDate` = '").append(SummaryDate).append("' AND `HourNum` = ").append(HourNum).toString();
		//System.out.println(query);
		try {
			while(Constants.connection.get(x)== null) {
				MasterConnection.getMasterSqlConnection(x);
			}
			smt=Constants.connection.get(x).createStatement();
			result = smt.executeQuery(query);
			if(result.next()) {
				srNo = result.getInt("SrNo");
			}
			result.close();
			if(srNo!= 0) {
				query = "UPDATE `sms_senderstatsmaster` SET  `DeliveredSmsCount` = `DeliveredSmsCount`+"+successCount+", `DeliveredSmsCredit` = `DeliveredSmsCredit`+"+successCount+", `FailedSmsCount` = `FailedSmsCount`+"+failedCount+", `FailedSmsCredit` =  `FailedSmsCredit`+"+failedCount+" WHERE `SrNo` ="+srNo;
			}
			else {
				
				query = new StringBuilder().append("INSERT INTO `sms_senderstatsmaster` (`ProcessDate`, `UserId`, `Sender`, `HourNum`, `DayNum`, `MonthNum`, `YearNum`, `DeliveredSmsCount`, `DeliveredSmsCredit`, `FailedSmsCount`, `FailedSmsCredit`) VALUES ( '").append(SummaryDate).append("',").append(Constants.profileid).append(",'").append(Constants.Sender).append("', ").append(HourNum).append(",")
							.append(DayNum).append(",").append(MonthNum).append(",").append(YearNum).append(",").append(successCount).append(",").append(successCount).append(",").append(failedCount).append(",").append(failedCount).append(")").toString();
			}
			//System.out.println(query);
			smt.executeUpdate(query);
			status = true;
			
		} catch(SQLNonTransientConnectionException se){
			try{
				Thread.sleep(1000);
				MasterConnection.getMasterSqlConnection(x);
			}
			catch(Exception e1){
				e1.printStackTrace();
			}
		}
		catch(CommunicationsException com){
			try{
				Thread.sleep(1000);
				Constants.consolelog.add(new StringBuilder().append("CommunicationsException Occured On UpdateMemoryThread.class (getInsertOrUpdateStats Method) : ").append(com.getMessage()).toString());
				MasterConnection.getMasterSqlConnection(x);
			}
			catch(Exception e1){
				e1.printStackTrace();
			}
		}
		catch(SQLException sql){
			if(sql.getMessage().equalsIgnoreCase("No operations allowed after statement closed.") || sql.getMessage().equalsIgnoreCase("No operations allowed after statement closed") || sql.getMessage().equalsIgnoreCase("Could not retrieve transaction read-only status from server")) {
				try{
					Thread.sleep(1000);
					MasterConnection.getMasterSqlConnection(x);
				}
				catch(Exception e1){
					e1.printStackTrace();
				}
			}
			else{
				//sql.printStackTrace();
				Constants.consolelog.add(new StringBuilder().append("SQLException Occured On UpdateMemoryThread.class (getInsertOrUpdateStats Method) : ").append(sql.getMessage()).toString());
			}
		}
		catch(NullPointerException np){
			try{
				Thread.sleep(1000);
				MasterConnection.getMasterSqlConnection(x);
			}
			catch(Exception e1){
				e1.printStackTrace();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			Constants.consolelog.add(new StringBuilder().append("Exception Occured On UpdateMemoryThread.class (getInsertOrUpdateStats Method) : ").append(e.getMessage()).toString());
		}
		finally {
			try {
				if(result!=null) result.close();
				if(smt!=null) smt.close();
			}
			catch(Exception ex) {}
		}
		return status;
		
	}


}

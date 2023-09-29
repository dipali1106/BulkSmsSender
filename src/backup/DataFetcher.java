package backup;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import connection.MasterConnection;
import utils.Constants;

public class DataFetcher extends Thread {
	public boolean ServiceStatus=false;
	public boolean isRunning = true;
	String countQuery = "SELECT count(MessageSrNo) as c FROM `shnpromotranssmppsmsdetails_"+Constants.postfix+"` WHERE ReportCode =0";
	String selectQuery = "SELECT `MessageSrNo`, `DelCode`, `DelStatus`, `DelReceipt_Time`, `DlrSubmitDate`, `DlrDoneDate`, `ErrorDescription`, `ReportCode` as c FROM `shnpromotranssmppsmsdetails_"+Constants.postfix+"` WHERE ReportCode =0";
	public void run() {
		Statement stmt =null;
		ResultSet rs = null;
		ResultSet rs2 =null;
		while(isRunning) {
			while(Constants.DbConnection.get(0)== null) {
				MasterConnection.getDataSqlConnection(0);
			}			
				try {
					stmt = Constants.DbConnection.get(0).createStatement();
					rs = stmt.executeQuery(countQuery);
					while(rs.next()) {
						Constants.totalcount = rs.getInt("c");
					}
					rs2 = stmt.executeQuery(selectQuery);
					while(rs2.next()) {
						//Constants.DataBucket
						
					}
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
		}
	}

}

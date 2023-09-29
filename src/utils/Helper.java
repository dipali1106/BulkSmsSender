package utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import connection.MasterConnection;

public class Helper {
	
	public static String getTodayDate() {
		 DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd_MMM_yyyy");  
		   LocalDateTime now = LocalDateTime.now(); 
		   String today=dtf.format(now).toString();
		   int i = 0;
	        while (i < today.length() && today.charAt(i) == '0')
	            i++;
	        StringBuffer sb = new StringBuffer(today);
	        sb.replace(0, i, "");	 
	        return sb.toString().toLowerCase();
	        //return "1_dec_2022";		 
	}
	
	
	public static int getProfileId(String Username) {
		int profileid=0;
		Statement stmt=null;
		ResultSet rs =null;
		String query = "SELECT ProfileId FROM `ivr_usermaster` WHERE UserName = '"+ Constants.UserName+"'";
		try {
			while(Constants.connection.get(0)== null) {
				MasterConnection.getMasterSqlConnection(0);
				}
			stmt = Constants.connection.get(0).createStatement();
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				profileid= rs.getInt("ProfileId");
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
		return profileid;
		
	}
	public  String getDateWithFormat( String thedate ){
		String retDate="";
		 SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
		 SimpleDateFormat dtFormat2 = new SimpleDateFormat("hh:mm:ss aa");	
		 try {
			retDate = dtFormat2.format(dtFormat.parse(thedate)) ;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return retDate;
		
	}

}

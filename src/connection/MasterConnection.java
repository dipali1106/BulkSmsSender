package connection;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.main.SenderUi;

import utils.Constants;

public class MasterConnection {
	 private static MasterConnection jdbc; 
	 private  Connection connect=null;
	 public static MasterConnection getInstance() {    
        	if (jdbc==null)  {  
        		jdbc=new  MasterConnection();  
              }  
           return jdbc;  
      }
	
	
	public static void readConInfoFile() {
		try {
			String path = new StringBuilder().append(new File("").getCanonicalPath()).append(File.separator).append("dbconfig").append(File.separator).append("dbconfiginfo").toString();
			//File conFile = new File(path);
			Path p = Paths.get(new File(path).getAbsolutePath());
			List<String> arr = Files.readAllLines(p);
			 for(int i=0;i<arr.size();i++) {
				 if( arr.get(i)!=null &&  arr.get(i).trim().length()>0) {
					 String str[] = arr.get(i).split("=");	
					   if(str.length==2) {
						   Constants.mapConInfo.put(str[0],str[1]);
					   }
				 }				 
			 }
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	 public Connection getConnection() 
     {   
		try {
		 if(connect!=null) {
				connect.close();
			}        
			Class.forName("com.mysql.cj.jdbc.Driver");		
			connect= DriverManager.getConnection("jdbc:mysql://"+Constants.mapConInfo.get("MasterHost")+":"+Constants.mapConInfo.get("MasterPort")+"/"+Constants.mapConInfo.get("MasterDb")+"?useSSL=false&useUnicode=yes&characterEncoding=UTF-8&allowPublicKeyRetrieval=true&rewriteBatchedStatement=true",Constants.mapConInfo.get("MasterUser"),Constants.mapConInfo.get("MasterPassword"));
		} catch (SQLException e) {
			Constants.consolelog.add(new StringBuilder().append("SQLException occured in MasterConnection.class -( getConnection method) :").append(e.getMessage()).toString());
			SenderUi.txtrLog.append("Check Connection Details, Error occured:"+e.getMessage()+"\n");
		} catch (ClassNotFoundException e) {
			SenderUi.txtrLog.append("Contact Administrator, Error occured:"+e.getMessage()+"\n");
			Constants.consolelog.add(new StringBuilder().append("ClassNotFoundException occured in MasterConnection.class -( getConnection method) :").append(e.getMessage()).toString());
		}
        return connect;  
           
     }  
	 
	public static synchronized boolean getMasterSqlConnection(int x) {
		Statement smt=null;
		boolean isAlive=true;
		boolean status=false;
		try {
			if(Constants.connection.get(x)==null){
				readConInfoFile();
				Class.forName("com.mysql.cj.jdbc.Driver");
				Constants.connection.put(x, DriverManager.getConnection(new StringBuilder().append("jdbc:mysql://").append(Constants.mapConInfo.get("MasterHost")).append(":").append(Constants.mapConInfo.get("MasterPort")).append("/").append(Constants.mapConInfo.get("MasterDb")).append("?useSSL=false&user=").append(Constants.mapConInfo.get("MasterUser")).append("&password=").append(Constants.mapConInfo.get("MasterPassword")).toString()));
				status=true;
			}
			else if(Constants.connection.get(x)!=null){
				try{
					smt = Constants.connection.get(x).createStatement();
					smt.close();
					status=true;
				}
				catch(Exception e1){
					isAlive=false;
				}
				if(!isAlive){
					Constants.connection.get(x).close();
					Class.forName("com.mysql.cj.jdbc.Driver");
					Constants.connection.put(x, DriverManager.getConnection(new StringBuilder().append("jdbc:mysql://").append(Constants.mapConInfo.get("MasterHost")).append(":").append(Constants.mapConInfo.get("MasterPort")).append("/").append(Constants.mapConInfo.get("MasterDb")).append("?useSSL=false&user=").append(Constants.mapConInfo.get("MasterUser")).append("&password=").append(Constants.mapConInfo.get("MasterPassword")).toString()));
					status=true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			try{
				if(smt!=null) smt.close();
			}
			catch(Exception ex){}
		}
		return status;				
	}
	
	
	public static synchronized boolean getDataSqlConnection(int x) {
		Statement smt=null;
		boolean isAlive=true;
		boolean status=false;
		try {
			if(Constants.DbConnection.get(x)==null){
				Class.forName("com.mysql.cj.jdbc.Driver");
				Constants.DbConnection.put(x, DriverManager.getConnection(new StringBuilder().append("jdbc:mysql://").append(Constants.mapConInfo.get("Host")).append(":").append(Constants.mapConInfo.get("Port")).append("/").append(Constants.mapConInfo.get("DbName")).append("?useSSL=false&user=").append(Constants.mapConInfo.get("User")).append("&password=").append(Constants.mapConInfo.get("Password")).toString()));
				status=true;
			}
			else if(Constants.DbConnection.get(x)!=null){
				try{
					smt = Constants.DbConnection.get(x).createStatement();
					smt.close();
					status=true;
				}
				catch(Exception e1){
					isAlive=false;
				}
				if(!isAlive){
					Constants.DbConnection.get(x).close();
					Class.forName("com.mysql.jdbc.Driver");
					Constants.DbConnection.put(x, DriverManager.getConnection(new StringBuilder().append("jdbc:mysql://").append(Constants.mapConInfo.get("Host")).append(":").append(Constants.mapConInfo.get("Port")).append("/").append(Constants.mapConInfo.get("DbName")).append("?useSSL=false&user=").append(Constants.mapConInfo.get("User")).append("&password=").append(Constants.mapConInfo.get("Password")).toString()));
					status=true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			try{
				if(smt!=null) smt.close();
			}
			catch(Exception ex){}
		}
		return status;				
	}
	

}

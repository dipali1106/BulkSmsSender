package dao;

public class ShnPromoTranssmppSmsDetails {

	private int MessageSrNo=0;
	private String DelCode="";
	private String DelStatus ="";
	private String DelReceipt_Time="";
	private String DlrSubmitDate="";
	private String DlrDoneDate ="";
	private String ErrorDescription="";
	private int ReportCode=0;
	public int getMessageSrNo() {
		return MessageSrNo;
	}
	public void setMessageSrNo(int messageSrNo) {
		MessageSrNo = messageSrNo;
	}
	public String getDelCode() {
		return DelCode;
	}
	public void setDelCode(String delCode) {
		DelCode = delCode;
	}
	public String getDelStatus() {
		return DelStatus;
	}
	public void setDelStatus(String delStatus) {
		DelStatus = delStatus;
	}
	public String getDelReceipt_Time() {
		return DelReceipt_Time;
	}
	public void setDelReceipt_Time(String delReceipt_Time) {
		DelReceipt_Time = delReceipt_Time;
	}
	public String getDlrSubmitDate() {
		return DlrSubmitDate;
	}
	public void setDlrSubmitDate(String dlrSubmitDate) {
		DlrSubmitDate = dlrSubmitDate;
	}
	public String getDlrDoneDate() {
		return DlrDoneDate;
	}
	public void setDlrDoneDate(String dlrDoneDate) {
		DlrDoneDate = dlrDoneDate;
	}
	public String getErrorDescription() {
		return ErrorDescription;
	}
	public void setErrorDescription(String errorDescription) {
		ErrorDescription = errorDescription;
	}
	public int getReportCode() {
		return ReportCode;
	}
	public void setReportCode(int reportCode) {
		ReportCode = reportCode;
	}
}

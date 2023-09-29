package org.main;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import org.threads.FileReaderJsonThread;
import org.threads.FileReaderThread;
import org.threads.LoggingThread;
import org.threads.ReportThread;
import org.threads.UpdationThread;

import connection.MasterConnection;
import utils.Constants;
import utils.Helper;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextPane;


public class SenderUi extends JFrame  implements ActionListener  {

	private static final long serialVersionUID = 1L;
	FileReaderThread fileReaderThread = new FileReaderThread();
	FileReaderJsonThread fileReaderJsonThread = new FileReaderJsonThread();
	ReportThread reportThread=new ReportThread();
	UpdationThread updationThread= new UpdationThread();
	private JPanel contentPane;
	private JTextField textSender = new JTextField();
	private JTextArea txtrMessage = new JTextArea();
	private JLabel lblFileName = new JLabel();
	private JTextField textBatch = new JTextField();
	JLabel lblUrl = new JLabel("URL:");
	JLabel lblSender = new JLabel("Sender:");
	JLabel lblText = new JLabel("Text:");
	JLabel lblMobile = new JLabel("Mobile:");
	JLabel lblBatch = new JLabel("Batch");
	JLabel lblCounter = new JLabel("Retry Counter:");
	JButton btnStart = new JButton("Start");
	JButton btnExit = new JButton("Exit");
	JButton btnBrowse;
	JScrollPane scrollPane = new JScrollPane();
	JScrollPane scrollPane2 = new JScrollPane();
	public static JTextArea txtrLog = new JTextArea();
	private  JLabel lblEntity = new JLabel("Entity Id:");
	private  JTextField textEntity = new JTextField();
	private  JLabel lblTemplate = new JLabel("Dlt Template:");
	private  JTextField textTemplate = new JTextField();
	JSpinner spinnerRetry = new JSpinner();
	private final JButton btnReport = new JButton("Create Report");
	private final JLabel lblNewLabel = new JLabel("Delivered %:");
	private final JTextField textDelivered = new JTextField();
	private final JLabel lblFailed = new JLabel("Failed %:");
	private final JTextField textFailed = new JTextField();
	private JTextField textUsername = new JTextField();
	JButton btnConnect = new JButton("Connect");
	JLabel conStatus = new JLabel("");
	JLabel lblUsername = new JLabel("Username");
	JLabel lblFormat = new JLabel("Format:");
	private JTextField textAreaUrl = new JTextField();
	private final JLabel lblNewLabel_1 = new JLabel("Other:");
	private final JTextField txtOther = new JTextField();
	private final JLabel lblDlrreg = new JLabel("dlr_req");
	private final JLabel lblMsgType = new JLabel("Message Type");
	private final JComboBox<String> comboMsgType = new JComboBox<String>();
	private final JLabel lblTemplateId = new JLabel("Template Id");
	private final JTextField txtJsontemplateid = new JTextField();
	private final JLabel lblUrlTrack = new JLabel("Url Track");
	private final JTextField txtUrltrack = new JTextField();
	private final JLabel lblCustref = new JLabel("Cust Ref");
	private final JTextField textCustRef = new JTextField();
	private final JLabel lbltag = new JLabel("Tag");
	private final JTextField textTag = new JTextField();
	private final JTextField textDlrReq = new JTextField();
	private final JLabel lblTemplateVal = new JLabel("Template Values");
	private final JTextField textTemplateValues = new JTextField();
	JLabel lblConnection = new JLabel("Connection Status:");
	private final JLabel lblKey = new JLabel("Key");
	private JTextField txtKey;
	public static JTextPane txtpnJsonpayload = new JTextPane();
	/**
	 * Launch the application.
	 */	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SenderUi frame = new SenderUi();
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

    /**
	 * Create the frame.
	 */
	public SenderUi() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Bulk Sms Sender 2.0");
		setBounds(100, 100, 920, 950);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
    	txtUrltrack.setText("1");
    	txtUrltrack.setEnabled(false);
    	txtUrltrack.setBounds(30, 400, 90, 25);
    	txtUrltrack.setColumns(10);
    	txtJsontemplateid.setText("12");
    	txtJsontemplateid.setEnabled(false);
    	txtJsontemplateid.setBounds(625, 262, 220, 25);
    	txtJsontemplateid.setColumns(10);
    	txtOther.setBounds(149, 225, 695, 25);
    	txtOther.setColumns(10);
    	textFailed.setText("50");
    	textFailed.setBounds(505, 477, 45, 25);
    	textFailed.setColumns(10);
    	textDelivered.setText("50");
    	textDelivered.setBounds(205, 477, 45, 25);
    	textDelivered.setColumns(10);
    	textEntity.setText("88373737362");
    	textEntity.setBounds(625, 115, 220, 25);
    	textEntity.setColumns(10);
		btnBrowse = new JButton("Browse");
		btnBrowse.setFont(new Font("Dialog", Font.PLAIN, 12));
		
		lblUrl.setFont(new Font("Dialog", Font.PLAIN, 12));
		contentPane.add(lblUrl);
		lblText.setFont(new Font("Dialog", Font.PLAIN, 12));
		contentPane.add(lblText);
		lblMobile.setFont(new Font("Dialog", Font.PLAIN, 12));
		contentPane.add(lblMobile);
		lblCounter.setFont(new Font("Dialog", Font.PLAIN, 12));
		contentPane.add(lblCounter);
		btnStart.setEnabled(false);
		contentPane.add(btnStart);
		lblFileName.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblFileName.setText("No file selected");
		contentPane.add(lblFileName);
		lblSender.setFont(new Font("Dialog", Font.PLAIN, 12));
		contentPane.add(lblSender);
		lblBatch.setFont(new Font("Dialog", Font.PLAIN, 12));
		contentPane.add(lblBatch);
		textBatch.setText("25");
		contentPane.add(textBatch);
		contentPane.add(btnExit);
		contentPane.add(btnBrowse);
		textSender.setText("TOUCHD");
		contentPane.add(textSender);
		contentPane.add(scrollPane);contentPane.add(scrollPane2);
		txtrMessage.setText("This is a test message.");
		contentPane.add(txtrMessage);
		lblEntity.setFont(new Font("Dialog", Font.PLAIN, 12));
		contentPane.add(lblEntity);		
		contentPane.add(textEntity);
		lblTemplate.setFont(new Font("Dialog", Font.PLAIN, 12));
		contentPane.add(lblTemplate);
		textTemplate.setText("1000005667433");
		contentPane.add(textTemplate);
		contentPane.add(spinnerRetry);
		lblNewLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
		contentPane.add(lblNewLabel);
		btnReport.setEnabled(false);
		contentPane.add(btnReport);
		contentPane.add(textDelivered);
		lblFailed.setFont(new Font("Dialog", Font.PLAIN, 12));
		contentPane.add(lblFailed);
		contentPane.add(textFailed);
		contentPane.add(conStatus);
		lblUsername.setFont(new Font("Dialog", Font.PLAIN, 12));
		contentPane.add(lblUsername);
		contentPane.add(btnConnect);
		textUsername.setText("obdsupport");
		contentPane.add(textUsername);
		contentPane.add(textDlrReq);
		contentPane.add(lblTemplateVal);
		textTemplateValues.setText("4617,OTP");
		contentPane.add(textTemplateValues);
		contentPane.add(lblNewLabel_1);
		contentPane.add(txtOther);
		contentPane.add(lblDlrreg);
		contentPane.add(lblMsgType);
		contentPane.add(comboMsgType);
		contentPane.add(lblTemplateId);
		contentPane.add(txtJsontemplateid);
		contentPane.add(textTag);
		contentPane.add(lblUrlTrack);
		contentPane.add(txtUrltrack);
		contentPane.add(lblCustref);
		contentPane.add(lbltag);
		contentPane.add(lblKey);
		
		lblUrl.setBounds(30, 70, 50, 25);
		lblSender.setBounds(31, 115, 100, 25);
		textSender.setBounds(149, 115, 200, 25);
		textSender.setColumns(10);
		lblText.setBounds(484, 152, 100, 25);
		txtrMessage.setBounds(625, 152, 220, 67);
		txtrMessage.setColumns(10);
		lblMobile.setBounds(30, 189, 76, 25);
		lblFileName.setBounds(279, 189, 200, 25);
		lblBatch.setBounds(30, 441, 60, 25);
		textBatch.setBounds(30, 477, 50, 25);
		textBatch.setColumns(10);
		lblCounter.setBounds(350, 441, 120, 25);
		txtrLog.setColumns(20);
		txtrLog.setOpaque(true);
		txtrLog.setBackground(new Color(238, 238, 236));
		txtrLog.setLineWrap(true);
		txtrLog.setBounds(40, 430, 740, 130);
		scrollPane.setBounds(19, 777, 629, 130);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setViewportView(txtrLog);
		Border border = BorderFactory.createLineBorder(Color.BLACK);
		Constants.textAreaResponse = new JTextArea();
		scrollPane2.setBounds(19, 586, 537, 179);		
		scrollPane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		contentPane.add(scrollPane2);
		Constants.textAreaResponse.setBounds(40, 450, 720, 100);
		//Constants.textAreaResponse.setEditable(false);
		//contentPane.add(Constants.textAreaResponse);
		scrollPane2.setViewportView(Constants.textAreaResponse);
		Constants.textAreaResponse.setBorder(BorderFactory.createCompoundBorder(border,
	    BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		
		btnStart.setBounds(156, 528, 80, 25);
		btnExit.setBounds(439, 528, 80, 25);
		btnBrowse.setBounds(149, 189, 101, 25);
		lblEntity.setBounds(484, 115, 100, 25);
		lblTemplate.setBounds(30, 262, 100, 25);
		textTemplate.setColumns(10);
		textTemplate.setBounds(149, 262, 200, 25);
		spinnerRetry.setModel(new SpinnerNumberModel(0, 0, 10, 1));
		spinnerRetry.setBounds(358, 477, 50, 25);
		btnReport.setBounds(268, 528, 140, 25);
		lblNewLabel.setBounds(200, 441, 100, 25);
		lblFailed.setBounds(501, 441, 70, 25);
		btnConnect.setBounds(30, 528, 100, 25);
		conStatus.setBounds(625, 30, 145, 25);
		lblUsername.setBounds(30, 299, 100, 25);
		textUsername.setBounds(149, 299, 200, 25);
		textUsername.setColumns(10);
		lblFormat.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblFormat.setBounds(30, 30, 70, 25);
		contentPane.add(lblFormat);
		
		JComboBox<String> comboFormat = new JComboBox<String>();
		comboFormat.setFont(new Font("Dialog", Font.PLAIN, 12));
		comboFormat.setModel(new DefaultComboBoxModel<String>(new String[] {"query string", "json type-1", "json type-2"}));
		comboFormat.setSelectedIndex(0);
		comboFormat.setBounds(145, 35, 117, 25);
		contentPane.add(comboFormat);
		
		textAreaUrl.setBounds(145, 75, 702, 25);
		contentPane.add(textAreaUrl);
		textAreaUrl.setColumns(10);
		lblNewLabel_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblNewLabel_1.setBounds(30, 225, 70, 25);
		lblDlrreg.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblDlrreg.setBounds(485, 365, 70, 25);
		lblMsgType.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblMsgType.setBounds(30, 147, 95, 25);
		comboMsgType.setFont(new Font("Dialog", Font.PLAIN, 12));
		comboMsgType.setModel(new DefaultComboBoxModel<String>(new String[] {"Plain", "Unicode"}));
		comboMsgType.setSelectedIndex(0);
		comboMsgType.setBounds(149, 147, 100, 25);
		
		
		lblTemplateId.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblTemplateId.setBounds(484, 262, 87, 25);
		lblUrlTrack.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblUrlTrack.setBounds(30, 365, 100, 25);
		lblCustref.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblCustref.setBounds(200, 365, 80, 25);
		textCustRef.setText("34343");
		textCustRef.setEnabled(false);
		textCustRef.setColumns(10);
		textCustRef.setBounds(200, 400, 90, 25);
		contentPane.add(textCustRef);
		lbltag.setFont(new Font("Dialog", Font.PLAIN, 12));
		lbltag.setBounds(350, 365, 70, 25);
			
		textTag.setText("tag");
		textTag.setEnabled(false);
		textTag.setColumns(10);
		textTag.setBounds(350, 400, 70, 25);
		textDlrReq.setText("1");
		textDlrReq.setEnabled(false);
		textDlrReq.setColumns(10);
		textDlrReq.setBounds(485, 400, 70, 25);
		lblTemplateVal.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblTemplateVal.setBounds(644, 364, 120, 25);
		
		textTemplateValues.setEnabled(false);
		textTemplateValues.setColumns(10);
		textTemplateValues.setBounds(620, 400, 220, 25);
		
		lblConnection.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblConnection.setBounds(484, 35, 120, 25);
		contentPane.add(lblConnection);
		lblKey.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblKey.setBounds(484, 304, 70, 25);
				
		txtKey = new JTextField();
		txtKey.setText("34567890987");
		txtKey.setEnabled(false);
		txtKey.setBounds(635, 302, 210, 25);
		contentPane.add(txtKey);
		txtKey.setColumns(10);
		
		JScrollPane scrollPane3 = new JScrollPane();
		
		scrollPane3.setViewportView(txtpnJsonpayload);
		scrollPane3.setBounds(605, 441, 276, 324);
		scrollPane3.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		txtpnJsonpayload.setBackground(new Color(243, 243, 243));
		txtpnJsonpayload.setEditable(false);
		txtpnJsonpayload.setBounds(605, 441, 276, 324);
		contentPane.add(scrollPane3);
		
		btnBrowse.addActionListener(this);  
		btnConnect.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Constants.postfix= Helper.getTodayDate();
				MasterConnection.readConInfoFile();
				Constants.conNum = Constants.mapConInfo.get("TotalConnection")!=null? Integer.parseInt(Constants.mapConInfo.get("TotalConnection")):5;
				Connection con =null;
				
				try {
					con = MasterConnection.getInstance().getConnection();
					if(con==null) {
						conStatus.setText("Not Connected");
						txtrLog.setText("Not Connected.Check Connection file, Error in Master Db Connection.");
					} else if(!con.isClosed() ){		
							con.close();
							if(MasterConnection.getDataSqlConnection(0)) {
								conStatus.setText("Connected");
								btnStart.setEnabled(true);
								btnReport.setEnabled(true);
								Constants.keepRunning=true;
								LoggingThread loggingThread = new LoggingThread();
								loggingThread.start();
							}else {
								conStatus.setText("Not Connected");
							}
							
						}
					else {
						txtrLog.setText("Not Connected.Check Connection file, Error in Master Db Connection.");
						conStatus.setText("Not Connected");
					}
			}
			 catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			 }
			}
		});
		btnStart.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Constants.postfix= Helper.getTodayDate();
				if(conStatus.getText().equalsIgnoreCase("connected")) {
					if(Constants.ReadFileName.length()>1 && Constants.ReadFileName!= null ) {
						Constants.Url =textAreaUrl.getText();
						Constants.BatchSize = Integer.parseInt(textBatch.getText());
						Constants.Text = txtrMessage.getText();
						Constants.Sender = textSender.getText();
						//System.out.println(Constants.Text);
						Constants.RetryCount = (int) spinnerRetry.getValue();
						if(Constants.Url== null || Constants.Url.length()==0) {
							JOptionPane.showMessageDialog(null, "Please enter URL");
						}
						else if(Constants.Text==null || Constants.Text.length()==0 || Constants.Text.equalsIgnoreCase("##text##")) {
							JOptionPane.showMessageDialog(null, "Please enter Messsage text.");
						}
						else if(Constants.Sender== null || Constants.Sender.length()==0 || Constants.Sender.equalsIgnoreCase("##sender##")) {
							JOptionPane.showMessageDialog(null, "Please enter Sender name.");
						}
						else if(textEntity.getText().length()==0) {
							JOptionPane.showMessageDialog(null, "Please enter Entity Id.");
						}
						else if( textTemplate.getText().length()==0 || textTemplate.getText().equalsIgnoreCase("##template##")) {
							JOptionPane.showMessageDialog(null, "Please enter Template id.");
						}
						else if(!textEntity.getText().matches("[0-9]+") ) {
							JOptionPane.showMessageDialog(null, "Please enter valid numeric Entity id.");
						}
						else if(!textTemplate.getText().matches("[0-9]+") ) {
							JOptionPane.showMessageDialog(null, "Please enter nummeric Template id.");
						}
						else if(!textBatch.getText().matches("[0-9]+") ) {
							JOptionPane.showMessageDialog(null, "Please enter valid batch size.");
						}
						else {
							if(Constants.Format.equalsIgnoreCase("querystring")) {
								try {
									Constants.Template = textTemplate.getText();
									Constants.EntityId = textEntity.getText();
									Constants.Other = txtOther.getText();
									Constants.UserName = Constants.Url.split("user=")[1].split("&")[0];
									Constants.profileid = Helper.getProfileId(Constants.UserName);
									if(Constants.profileid!=0) {
										fileReaderThread = new FileReaderThread();
										fileReaderThread.start();
									}
									else {
										txtrLog.append("No user found with username"+Constants.UserName);
									}
									
									}
									catch(Exception e1) {
										txtrLog.setText("Username could not fetched.UserName =="+Constants.UserName);
									}
							}
							else if(Constants.Format.equalsIgnoreCase("jsontype1")){
								Constants.Urltrack = txtUrltrack.getText();
								Constants.CustRef = textCustRef.getText();
								Constants.Tag = textTag.getText();
								Constants.DlrReq = textDlrReq.getText();
								Constants.Template = textTemplate.getText();
								Constants.EntityId = textEntity.getText();
								Constants.TemplateValues = textTemplateValues.getText();
								Constants.Jsontemplateid = txtJsontemplateid.getText();
								Constants.Key = txtKey.getText();
								 if(txtUrltrack.getText()== null || txtUrltrack.getText().length()==0 ) {
									JOptionPane.showMessageDialog(txtUrltrack, "Please enter Url track Status.");
								 }
								 else if(textCustRef.getText()== null || textCustRef.getText().length()==0) {
									 JOptionPane.showMessageDialog(textCustRef, "Please enter Customer Reference Id.");
								 }
								 else if(textTag.getText()== null || textTag.getText().length()==0) {
									 JOptionPane.showMessageDialog(textTag, "Please enter Tag.");
								 }
								 else if(textDlrReq.getText()== null || textDlrReq.getText().length()==0) {
									 JOptionPane.showMessageDialog(textDlrReq, "Please enter Dlr Req.");
								 }
								 else if(textTemplateValues.getText()==null || textTemplateValues.getText().length()==0) {
									 JOptionPane.showMessageDialog(textTemplateValues, "Please enter Template Values.");
								 }
								 else if(txtJsontemplateid.getText()== null || txtJsontemplateid.getText().length()==0) {
									 JOptionPane.showMessageDialog(txtJsontemplateid, "Please enter Template Id.");
								 }
								 else if(textUsername.getText()== null || textUsername.getText().length()==0) {
									 JOptionPane.showMessageDialog(textUsername, "Please enter UserName.");
								 }
								 else {
									 Constants.UserName = textUsername.getText();
									 Constants.Key = txtKey.getText();
									 
										Constants.profileid = Helper.getProfileId(Constants.UserName);
										if(Constants.profileid!=0) {
											 fileReaderJsonThread = new FileReaderJsonThread();
											 fileReaderJsonThread.start();
										}
								 }
							}
							else {
								Constants.Template =textTemplate.getText();
								Constants.TemplateValues = textTemplateValues.getText();
								Constants.Key = txtKey.getText();
								if(textTemplateValues.getText()==null || textTemplateValues.getText().length()==0) {
									 JOptionPane.showMessageDialog(textTemplateValues, "Please enter Template Values.");
								 }
								 
								 else if(textUsername.getText()== null || textUsername.getText().length()==0) {
									 JOptionPane.showMessageDialog(textUsername, "Please enter UserName.");
								 }
								 else {
									 Constants.UserName = textUsername.getText();
									 Constants.Key = txtKey.getText();
									 
										Constants.profileid = Helper.getProfileId(Constants.UserName);
										if(Constants.profileid!=0) {
											 fileReaderJsonThread = new FileReaderJsonThread();
											 fileReaderJsonThread.start();
										}
								 }
							
							}
							
						}
					
					}else {
						txtrLog.setText("File not selected.. Please select a file");
						JOptionPane.showMessageDialog(null, "File not selected.. Please select a file");
					}
				}
				else {
					txtrLog.setText("First check connection.");
				}

			}
		});
		
		btnExit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					Constants.keepRunning =false;
					fileReaderThread.ServiceStatus=true;
					txtrLog.append("Please wait.. exiting..");
					while(true) {
						if(updationThread== null && reportThread ==null && fileReaderThread==null) {
							break;
						}
						else if( !fileReaderThread.isAlive() && !reportThread.isAlive() && !updationThread.isAlive()) {
							break;
						}
						else {
							System.out.println("Please wait");
							Thread.sleep(2000);
						}
					}
					System.exit(0);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		
		btnReport.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(conStatus.getText().equalsIgnoreCase("connected")) {
					Constants.Url =textAreaUrl.getText();
					if(Constants.Url== null || Constants.Url.length()==0) {
						JOptionPane.showMessageDialog(null, "Please enter URL ");
					/*	if(textUsername.getText() == null || textUsername.getText().length()==0) {
							JOptionPane.showMessageDialog(null, "Please enter URL or UserName");
						}
						else {
							Constants.UserName = textUsername.getText();
							Constants.profileid = Helper.getProfileId(Constants.UserName);
							
						}*/
						
					}
					else if(!textDelivered.getText().matches("[0-9]+") ) {
						JOptionPane.showMessageDialog(null, "Please enter valid Delivered Percentage.");
					}
					else if(!textFailed.getText().matches("[0-9]+") ) {
						JOptionPane.showMessageDialog(null, "Please enter valid Failed percentage.");
					}
					else if(textSender.getText()== null || textSender.getText().length()==0 || textSender.getText().equalsIgnoreCase("##sender##")) {
						JOptionPane.showMessageDialog(null, "Please enter Sender name.");
					}
					else {
						Constants.Sender = textSender.getText();
						Constants.deliveredPercent = Double.parseDouble(textDelivered.getText());
						Constants.failedPercent = Double.parseDouble(textFailed.getText());
						Constants.UserName = Constants.Url.split("user=")[1].split("&")[0];
						Constants.profileid = Helper.getProfileId(Constants.UserName);
						
							//Constants.postfix = "22_mar_2023";
						reportThread = new ReportThread();
						reportThread.start();
						
						updationThread=  new UpdationThread();
						updationThread.start();
					}
				}
				else {
					txtrLog.setText("First check connection.");
				}
			}
		});
		
		comboFormat.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				int choice = comboFormat.getSelectedIndex();
			    if (choice == 0) {
			      System.out.println("querystring");
			      Constants.Format = "querystring";
			      txtUrltrack.setEnabled(false);
			      textCustRef.setEnabled(false);
			      textDlrReq.setEnabled(false);
			      textTag.setEnabled(false);
			      txtJsontemplateid.setEnabled(false);
				  textTemplateValues.setEnabled(false);
				  txtKey.setEnabled(false);
				  txtrMessage.setEnabled(true);
				  
			    }
			    else if(choice==1) {
			    	Constants.Format = "jsontype1";
			    	 txtUrltrack.setEnabled(true);
				     textCustRef.setEnabled(true);
				     textDlrReq.setEnabled(true);
				     textTag.setEnabled(true);
				     txtOther.setEnabled(false);
				     txtJsontemplateid.setEnabled(true);
				     textTemplateValues.setEnabled(true);
				     txtKey.setEnabled(true);
				     txtrMessage.setEnabled(true);
				     txtpnJsonpayload.setText(Constants.jsontype1format);
			    }
			    else {
			    	 Constants.Format = "jsontype2";
			    	 txtUrltrack.setEnabled(false);
				     textCustRef.setEnabled(false);
				     textDlrReq.setEnabled(false);
				     txtrMessage.setEnabled(false);
				     textTag.setEnabled(false);
				     txtOther.setEnabled(false);
				     txtJsontemplateid.setEnabled(true);
				     textTemplateValues.setEnabled(true);
				     txtKey.setEnabled(false);
				     txtpnJsonpayload.setText(Constants.jsontype2Format);
			    }				
			}
		});
		
		comboMsgType.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				int choice = comboFormat.getSelectedIndex();
			    if (choice == 0) {
			      System.out.println("querystring");
			      Constants.MsgType = "Plain";
			     
			    }
			    else {
			    	 Constants.MsgType = "Unicode";
			    	 
			    }
				
			}
		});
		
	}
	
	public void stopAllProcess() {
		System.out.println("Please wait..");	
		Constants.keepRunning = false;
		try {
			Thread.sleep(2000);
			
				if(updationThread!=null) {
					updationThread.ServiceStatus=true;
				}
				if(reportThread!=null) {
					reportThread.ServiceStatus=true;
				}
			
							
			while(true) {	
         		 if(updationThread==null && reportThread==null && updationThread==null  ) {
					break;
				}
				else if(!updationThread.isAlive() && !reportThread.isAlive() && !updationThread.isAlive()   ) {
    				break;
    			}
         		 
				System.out.println("Trying to Stop Process....");
				System.out.println(updationThread.isAlive()+" ##"+updationThread.isAlive() );
	        	try {
					Thread.sleep(10000);					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==btnBrowse){    
		    JFileChooser fc=new JFileChooser();    
		    int i=fc.showOpenDialog(this);    
		    if(i==JFileChooser.APPROVE_OPTION){    
		        File f=fc.getSelectedFile();    
		        Constants.ReadFileName = f.getPath(); 
		        lblFileName.setText(f.getName());
		    }    
		}
	}
}

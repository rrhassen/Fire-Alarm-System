/**
 * 
 */
package com.alarm.monitor;

import java.awt.EventQueue;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import java.awt.Color;
import javax.swing.JTextPane;

import com.alarm.server.RMIServer;

import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Component;
import javax.swing.ScrollPaneConstants;
import java.awt.Font;

/**
 * @author Reeshma
 *
 */
public class MonitorGUI   {

	private JFrame frame;
	private JTextArea alertBox;
	private JTextArea co2level;
	private JTextArea temperature;
	private JButton btnClose;
	private JComboBox comboBoxMonitors;
	private JTextArea textAreaCurrentReading;
	private JLabel lblSensors;
	private JLabel lblMonitors;
	private JLabel lblBackground;
	
	private MonitorHandler monitor;
	private JLabel lblTemperature;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_3;
	private JTextArea SmokeLevel;
	private JLabel lblSmokeLevel;
	private JTextArea BatteryLevel;
	private JLabel lblNewLabel_4;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MonitorGUI window = new MonitorGUI();
					window.frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MonitorGUI() throws java.rmi.RemoteException {
		initialize();
		
		
		monitor=new MonitorHandler();
		Thread gui=new Thread(monitor);
		gui.start();
		
		
	}
	
	
	

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setForeground(Color.BLACK);
		frame.setBounds(100, 100, 819, 504);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setContentPane(new JLabel(new ImageIcon("C:\\Users\\Reeshma94\\Desktop\\download.png")));
		
		
		lblSensors = new JLabel("Sensors");
		lblSensors.setBounds(334, 20, 57, 14);
		frame.getContentPane().add(lblSensors);
		
		lblMonitors = new JLabel("Monitors");
		lblMonitors.setBounds(523, 20, 46, 14);
		frame.getContentPane().add(lblMonitors);
		
		co2level = new JTextArea();
		co2level.setEditable(false);
		co2level.setVisible(true);
		//co2level.setBounds(308, 62, 189, 84);
		JScrollPane scroll_co2 = new JScrollPane (co2level, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll_co2.setBounds(374, 86, 198, 116);
		//scroll_co2.setViewportView(co2level);
		frame.getContentPane().add(scroll_co2);
		
		
		
		
		comboBoxMonitors = new JComboBox();
		comboBoxMonitors.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					String val=String.valueOf(comboBoxMonitors.getSelectedItem());
					monitor.getSensorCurrentReading(val, monitor);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		comboBoxMonitors.setBounds(159, 257, 132, 30);
		frame.getContentPane().add(comboBoxMonitors);
		
		textAreaCurrentReading = new JTextArea();
		JScrollPane textArea_current = new JScrollPane (textAreaCurrentReading, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		textArea_current.setBounds(93, 311, 198, 126);
		frame.getContentPane().add(textArea_current);
		
		JLabel lblNewLabel_2 = new JLabel("CO2 Level");
		lblNewLabel_2.setBounds(374, 215, 73, 14);
		frame.getContentPane().add(lblNewLabel_2);
		
		 alertBox = new JTextArea();
		 alertBox.setForeground(Color.RED);
		 alertBox.setLocation(558, 208);
		//alertBox.setBounds(40, 98, 120, 73);
		frame.getContentPane().add(alertBox);
		alertBox.setEditable(false);
		JScrollPane scroll_alert = new JScrollPane (alertBox, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll_alert.setBounds(97, 120, 194, 97);
		frame.getContentPane().add(scroll_alert);
		
		btnClose = new JButton("Close using this");
		btnClose.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnClose.setBackground(new Color(240, 240, 240));
		btnClose.setForeground(Color.RED);
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				monitor.exit();
				
				frame.dispose();
				
			}
		});
		btnClose.setBounds(647, 424, 132, 30);
		frame.getContentPane().add(btnClose);
		
		JLabel lblMonitors_1 = new JLabel("Monitors");
		lblMonitors_1.setBounds(444, 20, 57, 14);
		frame.getContentPane().add(lblMonitors_1);
		
		lblTemperature = new JLabel("Temperature");
		lblTemperature.setBounds(374, 397, 73, 14);
		frame.getContentPane().add(lblTemperature);
		
		temperature = new JTextArea();
		
		JScrollPane scroll_temperature = new JScrollPane (temperature, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll_temperature.setBounds(371, 259, 201, 116);
		frame.getContentPane().add(scroll_temperature);
		
		lblNewLabel = new JLabel("Sensors");
		lblNewLabel.setBounds(257, 16, 57, 23);
		frame.getContentPane().add(lblNewLabel);
		
		lblNewLabel_1 = new JLabel("Current Readings ");
		lblNewLabel_1.setBounds(39, 257, 89, 30);
		frame.getContentPane().add(lblNewLabel_1);
		
		lblNewLabel_3 = new JLabel("Alerts");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_3.setForeground(Color.RED);
		lblNewLabel_3.setBounds(97, 86, 46, 23);
		frame.getContentPane().add(lblNewLabel_3);
		
		SmokeLevel = new JTextArea();
		JScrollPane scroll_SmokeLevel = new JScrollPane (SmokeLevel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll_SmokeLevel.setBounds(624, 87, 169, 115);
		frame.getContentPane().add(scroll_SmokeLevel);
		
		lblSmokeLevel = new JLabel("Smoke Level");
		lblSmokeLevel.setBounds(624, 215, 73, 14);
		frame.getContentPane().add(lblSmokeLevel);
		
		BatteryLevel = new JTextArea();
		JScrollPane scroll_BatteryLevel = new JScrollPane (BatteryLevel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll_BatteryLevel.setBounds(624, 260, 169, 115);
		frame.getContentPane().add(scroll_BatteryLevel);
		
		lblNewLabel_4 = new JLabel("Battery Level");
		lblNewLabel_4.setBounds(624, 397, 73, 14);
		frame.getContentPane().add(lblNewLabel_4);
		
	}

	
	
	
	private class MonitorHandler extends UnicastRemoteObject implements Monitor,Runnable,Serializable{

		
		private RMIServer stub;
		private MonitorHandler client;
		/**
		 * 
		 */
		public MonitorHandler() throws RemoteException {
		
		}
		
		
		@Override
		public void alertGenerator(String type, String value) throws RemoteException {
			
			
			if(type.equals("TEMP")) {
				alertBox.append("Alert "+"Temperature:"+value+"\n");
			}
			
			if(type.equals("CO2")) {
				alertBox.append("Alert "+"CO2 Level:"+value+"\n");
			}
			
			if(type.equals("SENSOR")) {
				alertBox.append("Alert "+"Sensor :"+value+" down \n");
			}
			
			if(type.equals("SMOKE")) {
				alertBox.append("Alert "+"Smoke Level :"+value+"\n");
			}
			
			if(type.equals("BATTERY")) {
				alertBox.append("Alert "+"Batttery Level :"+value+"\n");
			}
			
			
		}
		
		
		

		
		@Override
		public void run() {
			
			try {
				Registry registry = LocateRegistry.getRegistry(null);
				
				stub=(RMIServer)registry.lookup("RMIServer");
				
				stub.addMonitor(monitor);
			} catch (NotBoundException e) {
				
				e.printStackTrace();
			} catch (RemoteException e) {
				
				e.printStackTrace();
			}
			
		
		}
		
		public void exit() {
			try {
				stub.removeMonitor(monitor);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		public void getSensorCurrentReading(String sensor,Monitor monitor) throws RemoteException {
			
			try {
				stub.getReadings(sensor, monitor);
			} catch (RemoteException e) {
				
				e.printStackTrace();
			}
		}
		
		
		
		

		
		@Override
		public void getCO2Reading(String value) throws RemoteException {
		
			co2level.append(value+"\n");
			
		}


		@Override
		public void updateList(String[] sensors) throws RemoteException {
			
			
			DefaultComboBoxModel model = new DefaultComboBoxModel<String>(sensors);
			comboBoxMonitors.setModel(model);
			
		}


		@Override
		public void getReadingByID(String values) throws RemoteException {
		
			String[] readings=values.split("\\|");
			
			textAreaCurrentReading.setText(null);
			
			for (String string : readings) {
				String[] arr=string.split("-");
				textAreaCurrentReading.append(arr[0]+"  "+arr[1]+"\n");
			}
		}


	
		@Override
		public void setSensorCount(String value) throws RemoteException {
		
			lblSensors.setText(value);
		}


		
		@Override
		public void setMonitorCount(String value) throws RemoteException {
			
			lblMonitors.setText(value);
		}


	
		@Override
		public void getTemperatureReading(String value) throws RemoteException {
		
			temperature.append(value+"\n");
		}


		@Override
		public void getSmokeReading(String value) throws RemoteException {
			SmokeLevel.append(value+"\n");
			
		}


		
		@Override
		public void getBatteryReading(String value) throws RemoteException {
			BatteryLevel.append(value+"\n");
			
		}


		
		
	}
}

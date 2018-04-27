
package com.alarm.sensors;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class BatteryLevel implements SensorInterface {
	
	private int battterypercentage;
	private Random randomNumber;
	private String id;
	BufferedReader in;
    PrintWriter out;
    ObjectInputStream is;
    ObjectOutputStream os;
    
    
   
	public BatteryLevel(String ID) {
		 battterypercentage=100;
		 randomNumber= new Random();
		this.id=ID;
	}

	
	@Override
	public double getReading() {
		if( battterypercentage<=0 ||  battterypercentage>100) {
			
			 battterypercentage=100;
			
		}
		else {
			
			int num=randomNumber.nextInt();
			
			if (num < 0) {
				 battterypercentage -= 0.5;
			} 
				
		}
			
		
		return  battterypercentage;
	}
	
	public String getInput(String value) {
		Scanner keyboard = new Scanner(System.in);
		
		if(value.equals("pwd")) {
			System.out.print("enter sensor password and type ('type'-'pwd'):");
		}
		
		else if(value.equals("id")) {
			System.out.print("enter sensor ID ("+id+"):");
		}
		
		String input = keyboard.nextLine();
		
		return input;
    }
	
	private String getServerAddress() {
		
		Scanner keyboard = new Scanner(System.in);
		System.out.println("enter server address");
		String server=keyboard.nextLine();
		return server;
	}

	@Override
	public void run() {

		String serverAddress = getServerAddress();
        Socket socket;
		
			try {
				socket = new Socket(serverAddress, 9001);
				in = new BufferedReader(new InputStreamReader(
			            socket.getInputStream()));
			    out = new PrintWriter(socket.getOutputStream(), true);
			
			
		
		 // Process all messages from server, according to the protocol.
		        while (true) {
		        	String line;
					
						line = in.readLine();
						
						if (line.startsWith("SUBMITNAME")) {
			                out.println(getInput("id"));
			            }
						
						else if (line.startsWith("TYPE-PWD")) {
							out.println(getInput("pwd"));
						}
						else if (line.startsWith("AUTHORISED")) {
							System.out.println("access granted");
							break;
						}
						else if (line.startsWith("UNAUTHORISED")) {
							System.out.println("access deined please continue from beginning");
							continue;
						}
						
	        	
		        }
		        
		        Timer timer = new Timer();
		     
		        TimerTask writeToFile = new TimerTask() {

					@Override
					public void run() {
						
						try {
						    BufferedWriter writer = new BufferedWriter(new FileWriter(System.getProperty("user.dir")+"\\src\\com\\alarm\\sensors\\"+id+".txt", true));
						    String timeStamp = new SimpleDateFormat("yyyy MM dd~HH mm ss").format(Calendar.getInstance().getTime());
						    writer.write(timeStamp+"-"+Double.toString(getReading())+"|");
						    writer.close();
						} catch (IOException e) {
							 e.printStackTrace();
						}
						
						
					}
		        	
		        };
		        
		        
		        TimerTask sendData = new TimerTask() {

					@Override
					public void run() {
					
						try (BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir")+"\\src\\com\\alarm\\sensors\\"+id+".txt"))) {
				            String line;
				            
				            while ((line = br.readLine()) != null) {
				            	out.println("BATTERY:"+line);
				            }
				        } catch (IOException e) {
				            e.printStackTrace();
				        }finally {
							
				        	File file = new File(System.getProperty("user.dir")+"\\src\\com\\alarm\\sensors\\"+id+".txt");
				            
				            if(file.delete())
				            {
				                System.out.println("File deleted successfully");
				            }
				            else
				            {
				                System.out.println("Failed to delete the file");
				            }
				        	
						}
						
					}
		        	
		        };
		        
		        timer.schedule(writeToFile, 0,2000);
		        timer.schedule(sendData, 11000,11000);
		        
        
			} catch (UnknownHostException e) {
		
				e.printStackTrace();
			} catch (IOException e) {
				
				e.printStackTrace();
			}


	}


	public static void main(String[] args) {
		
		BatteryLevel sensor=new BatteryLevel("10-20");
		sensor.run();
	}

}

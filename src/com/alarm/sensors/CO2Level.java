
package com.alarm.sensors;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
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

public class CO2Level implements SensorInterface {
	
	private volatile int co2_level;
	private String id;
	private Random r;
	BufferedReader in;
    PrintWriter out;
    ObjectInputStream is;
    ObjectOutputStream os;
	
	

	public CO2Level(String ID) {
		
		co2_level=300;
		r = new Random();
		id=ID;
	}
	
	public String getInput(String value) {
		Scanner keyboard = new Scanner(System.in);
		
		if(value.equals("pwd")) {
			System.out.print("enter sensor pwd+type ('type'-'pwd'):");
		}
		
		else if(value.equals("id")) {
			System.out.print("enter sensor ID ("+id+"):");
		}
		
		String input = keyboard.nextLine();
		
		return input;
    }
	
	private String getServerAddress() {
		
		Scanner keyboard = new Scanner(System.in);
		System.out.println("enter server");
		String server=keyboard.nextLine();
		return server;
	}


	
	@Override
	public double getReading() {
		
		
        if (co2_level > 450){
        	co2_level = 300;
        }else{
            // Get random number, to decide weather reading goes up or down
            int num = r.nextInt();
            if (num < 0) {
            	co2_level += 1;
            } else {
            	co2_level -= 1;
            }
        }
        return co2_level;
	}
	
	@Override
	public void run() {
		
		
		// The Make connection and initialize streams
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
				            	out.println("CO2:"+line);
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
	
	 public static void main(String arg[]) {

		 CO2Level sensor=new CO2Level("12-30");
		 sensor.run();
	        

	    }
	

}

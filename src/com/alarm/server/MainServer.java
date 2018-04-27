
package com.alarm.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;

import com.alarm.monitor.Monitor;


public class MainServer  implements RMIServer,Runnable{

    private static final int PORT1 = 9001; 
    private static HashSet<String> sensors = new HashSet<String>();
    private static ArrayList<Monitor> list=new ArrayList<Monitor>();
    
 
	public MainServer() throws RemoteException {
		
	}
	
	private static class Handler extends Thread {
        private String ID;
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        
        
      
        public Handler(Socket socket) {
            this.socket = socket;
        }
        
        public boolean sensorAuthenticate(String value) {
        	//Please change the file path respect to yours
        	try (BufferedReader br = new BufferedReader(new FileReader("E:\\Java\\Fire-Alarm System\\Fire-Alarm System\\FireAlarmSystem\\src\\com\\alarm\\server\\sensors.txt"))) {
        	    String line;
        	    String[] lineArgs;
        	    while ((line = br.readLine()) != null) {
        	    
        	    	if(line.equals(value)) {
        	    		return true;
        	    	}
        	    	
        	    	
        	    }
        	} catch (FileNotFoundException e) {
				
				e.printStackTrace();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
        	
        	return false;
        }
        
        public void WriteToFile(String input,String type) {
        	
        	try {
			    BufferedWriter writer = new BufferedWriter(new FileWriter(System.getProperty("user.dir")+"\\src\\com\\alarm\\server\\"+ID+".txt", true));
			    
			    writer.write(input);
			    writer.close();
			} catch (IOException e) {
				 e.printStackTrace();
			}
        	
        	
        }
        
        public void alertFinder(String valueSet,String type) {
        	
        	String [] readings=valueSet.split("\\|");
        	
        	String[] sensorDeatils=ID.split("-");
        	
        	if(!list.isEmpty()) {
        		
	        	for (Monitor monitor : list) {
					
	        		for (int i = 0; i < readings.length; i++) {
	        			
	        			if(type.equals("CO2")) {
	        				
	        				try {
	        					String[] co2lvl=readings[i].split("-");
	        					
	        					if(Double.parseDouble(co2lvl[1])>300) {
	        						monitor.alertGenerator("CO2",co2lvl[1]+" Floor:"+sensorDeatils[0]+" sensor:"+sensorDeatils[1]);
	        					}
	        					
							} catch (Exception e) {
								
								e.printStackTrace();
							}
	        				
	        			}
	        			

	        			if(type.equals("TEMP")) {
	        				
	        				try {
	        					String[] val=readings[i].split("-");
	        					
	        					if(Double.parseDouble(val[1])>50) {
	        						monitor.alertGenerator("TEMP",val[1]+" Floor:"+sensorDeatils[0]+" sensor:"+sensorDeatils[1]);
	        					}
	        					
							} catch (Exception e) {
								
								e.printStackTrace();
							}
	        				
	        			}
	        			
	        			
	        			if(type.equals("SMOKE")) {
	        				
	        				try {
	        					String[] val=readings[i].split("-");
	        					
	        					if(Double.parseDouble(val[1])>7) {
	        						monitor.alertGenerator("SMOKE",val[1]+" Floor:"+sensorDeatils[0]+" sensor:"+sensorDeatils[1]);
	        					}
	        					
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}
	        				
	        			}
	        			
	        			
	        			if(type.equals("BATTERY")) {
	        				
	        				try {
	        					String[] val=readings[i].split("-");
	        					
	        					if(Double.parseDouble(val[1])<=10) {
	        						monitor.alertGenerator("BATTERY",val[1]+" Floor:"+sensorDeatils[0]+" sensor:"+sensorDeatils[1]);
	        					}
	        					
							} catch (Exception e) {
								
								e.printStackTrace();
							}
	        				
	        			}
	        			
	        			
						
					}
				}
        	
        	}
        	
        	
        	
        	
        }
        
        public void sendValue(String valueSet,String type) throws RemoteException {
        	
        	String [] readings=valueSet.split("\\|");
        	
        	if(!list.isEmpty()) {
        		
	        	for (Monitor monitor : list) {
					
	        		for (int i = 0; i < readings.length; i++) {
	        			
	        			if(type.equals("CO2")) {
	        				
	        				try {
	        					String[] co2lvl=readings[i].split("-");
	        					monitor.getCO2Reading(co2lvl[0]+"  "+co2lvl[1]);
							} catch (Exception e) {
								
								e.printStackTrace();
							}
	        				
	        			}
	        			

	        			if(type.equals("TEMP")) {
	        				
	        				try {
	        					String[] val=readings[i].split("-");
	        					monitor.getTemperatureReading(val[0]+"  "+val[1]);
							} catch (Exception e) {
								
								e.printStackTrace();
							}
	        				
	        			}
	        			
	        			
	        			if(type.equals("SMOKE")) {
	        				
	        				try {
	        					String[] val=readings[i].split("-");
	        					monitor.getSmokeReading(val[0]+"  "+val[1]);
							} catch (Exception e) {
							
								e.printStackTrace();
							}
	        				
	        			}
	        			
	        			
	        			if(type.equals("BATTERY")) {
	        				
	        				try {
	        					String[] val=readings[i].split("-");
	        					monitor.getBatteryReading(val[0]+"  "+val[1]);
							} catch (Exception e) {
							
								e.printStackTrace();
							}
	        				
	        			}
						
					}
				}
        	
        	}
        	
        	
        }
        
        
        
        
        public void run() {
        	
        	
        	
        
            try {
				in = new BufferedReader(new InputStreamReader(
				    socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);
				
				
				 // This method requestes a unused name of the client.Checking for existing clients and  
                // adding the new clients should be done while chacking the names
                while (true) {
                    out.println("SUBMITNAME");
                    ID = in.readLine();
                    if (ID == null) {
                        return;
                    }
                    synchronized (sensors) {
                        if (!sensors.contains(ID)) {
                        	
                        	out.println("TYPE-PWD");
                        	String pwd=in.readLine();
                        	if(sensorAuthenticate(pwd)) {
                        		sensors.add(ID);
                                break;
                        	}
                        	else {
                        		out.println("UNAUTHORISED");
                        		continue;
                        	}
                        	
                        	
                        	
                        	
                        	
                        }
                    }
                }
				
				
                out.println("AUTHORISED");
                
                sensorUpdate();
        		
                if(!list.isEmpty()) {
                	
                	for (Monitor monitor : list) {
            			
            		
            			monitor.setSensorCount(Integer.toString(sensors.size()));
            			
            		}
                	
                }
                
                while (true) {
                	
                	String line;
                	String[] input;
					
                	
                	
					line = in.readLine();
					
					if(line.isEmpty() || line.equals("")) {
						
						if(!list.isEmpty()) {
				        	
				        	for (Monitor monitor : list) {
				    			
				    			monitor.alertGenerator("SENSOR", ID);
				    			
				    		}
				        	
				        }
						
					}
					
					
					if (line.startsWith("CO2")) {
						input=line.split(":");
						alertFinder(input[1], "CO2");
						sendValue(input[1], "CO2");
						System.out.println(input[1]);
						WriteToFile(input[1],"CO2");
					}
					
					if (line.startsWith("TEMP")) {
						input=line.split(":");
						alertFinder(input[1], "TEMP");
						sendValue(input[1], "TEMP");
						System.out.println(input[1]);
						WriteToFile(input[1],"TEMP");
					}
					
					
					if (line.startsWith("SMOKE")) {
						input=line.split(":");
						alertFinder(input[1], "SMOKE");
						sendValue(input[1], "SMOKE");
						System.out.println(input[1]);
						WriteToFile(input[1],"SMOKE");
					}
					
					if (line.startsWith("BATTERY")) {
						input=line.split(":");
						alertFinder(input[1], "BATTERY");
						sendValue(input[1], "BATTERY");
						System.out.println(input[1]);
						WriteToFile(input[1],"BATTERY");
					}
                	
                }
				
				
			} catch (IOException e) {
				
				e.printStackTrace();
			}
            
            finally {
                // This sensor is going down!  Remove its name and its print
                
            	
            	if(ID!=null) {
            		
            		try {
            			sensors.remove(ID);
						sensorUpdate();
						
						if(!list.isEmpty()) {
				        	
				        	for (Monitor monitor : list) {
				    			
				    			
				    			monitor.setSensorCount(Integer.toString(sensors.size()));
				    			monitor.alertGenerator("SENSOR", ID);
				    			
				    		}
				        	
				        }
						
						
					} catch (RemoteException e) {
						
						e.printStackTrace();
					}
            	}
            	
            	try {
                    socket.close();
                } catch (IOException e) {
                }
            }
            
        	
        	
        }
        
        
    }
	
	
	public static void main(String[] args) throws Exception {

		
		try {
			
			MainServer server=new MainServer();
			
			RMIServer stub= (RMIServer) UnicastRemoteObject.exportObject(server, 0);
			
			Registry registry = LocateRegistry.createRegistry(1099); 
			registry.rebind("RMIServer", stub);
			
			System.out.println("Server RMI started");
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println("Server exception: " + e.toString()); 
	        e.printStackTrace(); 
		}
		
		
		ServerSocket listener = new ServerSocket(PORT1);
        try {
            while (true) {
                new Handler(listener.accept()).start();
            }
        } finally {
            listener.close();
        }
		
		

	}


	/* (non-Javadoc)
	 * @see com.alarm.server.RMIServer#addMonitor(com.alarm.monitor.Monitor)
	 */
	@Override
	public synchronized void addMonitor(Monitor client) throws RemoteException {
		
		System.out.println("adding monitor -" + client);
		
		
		
		list.add(client);
		
		sensorUpdate();
		
		if(!list.isEmpty()) {
        	
        	for (Monitor monitor : list) {
    			
    			monitor.setMonitorCount(Integer.toString(list.size()));
    			monitor.setSensorCount(Integer.toString(sensors.size()));
    			
    		}
        	
        }
		
	}


	/* (non-Javadoc)
	 * @see com.alarm.server.RMIServer#removeMonitor(com.alarm.monitor.Monitor)
	 */
	@Override
	public synchronized void removeMonitor(Monitor client) throws RemoteException {
		
		System.out.println("removing listener -" + client);
		list.remove(client);
		
		sensorUpdate();
		
		if(!list.isEmpty()) {
        	
        	for (Monitor monitor : list) {
    			
    			monitor.setMonitorCount(Integer.toString(list.size()));
    			
    		}
        	
        }
		
	}
	
	
	
	public static void sensorUpdate() throws RemoteException{
		
		if(!list.isEmpty()) {
        	
        	for (Monitor monitor : list) {
    			
    			monitor.updateList(sensors.toArray(new String[sensors.size()]));
    			
    		}
        	
        }
		
	}


	
	@Override
	public void run() {
	
		
	}

	@Override
	public synchronized void getReadings(String sensor,Monitor client) throws RemoteException {
		
		try (BufferedReader br = new BufferedReader(new FileReader("E:\\Java\\Fire-Alarm System\\Fire-Alarm System\\FireAlarmSystem\\src\\com\\alarm\\server\\"+sensor+".txt"))) {
    	    String line;
    	    String lineArgs="";
    	    while ((line = br.readLine()) != null) {
    	     
    	    	
    	    	lineArgs+=line;
    	    	
    	    	
    	    }
    	    
    	    
    	    System.out.println(client+" "+lineArgs+" "+sensor);
	    	client.getReadingByID(lineArgs);
    	    
    	    
    	} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
		
			e.printStackTrace();
		}
		
	}

}

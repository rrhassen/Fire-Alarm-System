
package com.alarm.server;

import java.rmi.Remote;

import com.alarm.monitor.Monitor;


public interface RMIServer extends Remote {

	public void addMonitor (Monitor client ) throws java.rmi.RemoteException;// add a listner to listner list
	
	public void removeMonitor (Monitor client ) throws java.rmi.RemoteException;// remove a listner to listner list
	
	public void getReadings(String Sensor,Monitor client)throws java.rmi.RemoteException;
	
	
	
}

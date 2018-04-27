
package com.alarm.monitor;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * @author Reeshma
 *
 */
public interface Monitor extends Remote,Serializable {
	
	public void alertGenerator(String type,String value) throws java.rmi.RemoteException;
	
	public void getCO2Reading(String value)throws java.rmi.RemoteException;
	
	public void getTemperatureReading(String value)throws java.rmi.RemoteException;
	
	public void getSmokeReading(String value)throws java.rmi.RemoteException;
	
	public void getBatteryReading(String value)throws java.rmi.RemoteException;
	
	public void getReadingByID(String arr)throws RemoteException;
	
	public void setSensorCount(String value)throws RemoteException;
	
	public void setMonitorCount(String value)throws RemoteException;
	
	public void updateList(String[] sensors) throws RemoteException;
}

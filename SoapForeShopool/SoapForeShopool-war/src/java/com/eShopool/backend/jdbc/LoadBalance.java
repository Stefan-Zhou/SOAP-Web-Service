package com.eShopool.backend.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class is for loadBalance and R/W Splitting between databases,
 * using weighted Round Robin algorithm.
 * 
 * @author Group 10
 * @since 30/10/2019
 */
public class LoadBalance {
    
    private String[] writeIP;
    private String[] readIP;
    private int[] mWeight;
    private int[] mEffectiveWeight;
    private int[] mCurrentWeight;
    private int[] sWeight;
    private int[] sEffectiveWeight;
    private int[] sCurrentWeight;
    
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    
    static final String USER = "admin";  
    static final String PASS = "eshopool"; 
    
    /**
     * Constructor of the LoadBalance class<P/>
     * Every database will be accessed with a latency test.
     * Every database will be given a weight according to its latency.
     * A database that fails the latency test will be treated as a failed node,
     * given a weight of 0.
     * @param writeIp String array with IPs of write databases
     * @param readIP String array with IPs of read databases
     */
    public LoadBalance(String[] writeIp, String[] readIP) {
	this.writeIP = writeIp;
	this.readIP = readIP;
	
	int numOfMaster = this.writeIP.length;
	int numOfSlave = this.readIP.length;
	
	//measure latency between databases
	int[] mLatency = new int[numOfMaster];
	int[] sLatency = new int[numOfSlave];
	mWeight = new int[numOfMaster];
	sWeight = new int[numOfSlave];
	mEffectiveWeight = new int[numOfMaster];
	sEffectiveWeight = new int[numOfSlave];
	mCurrentWeight = new int[numOfMaster];
	sCurrentWeight = new int[numOfSlave];
	
	for(int i = 0; i< numOfMaster; i++) {
            Connection conn = null;
            try {
		// register JDBC driver 
		Class.forName(JDBC_DRIVER);
		
		String DB_URL = "jdbc:mysql://" + writeIp[i] + ":3306/eshopool?serverTimezone=UTC&connectTimeout=3000&socketTimeout=3000";
		
		// try connection
		System.out.println("Start connnection...");
		long start = System.currentTimeMillis();
		//DriverManager.setLoginTimeout(2);
		conn = DriverManager.getConnection(DB_URL,USER,PASS);
		
		mLatency[i] = (int) (System.currentTimeMillis() - start);
		System.out.println(mLatency[i]);
		mCurrentWeight[i] = 0;
		
		conn.close();
            } catch (SQLException e) {
		//if one database is offline...
		mLatency[i] = 99999;
		e.printStackTrace();	    	 
            } catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
            } finally {
		// close resource 
		try {
		    if(conn!=null) conn.close();
		} catch(SQLException se) {
		    se.printStackTrace();
		}
            }
	}
		
	for(int i = 0; i< numOfSlave; i++) {
            Connection conn = null;
            try {
		// register JDBC driver
		Class.forName(JDBC_DRIVER);
		
		String DB_URL = "jdbc:mysql://" + readIP[i] + ":3306/eshopool?serverTimezone=UTC&connectTimeout=3000&socketTimeout=3000";
		
		// try connection
		System.out.println("Start connnection...");
		long start = System.currentTimeMillis();
		//DriverManager.setLoginTimeout(2);
		conn = DriverManager.getConnection(DB_URL,USER,PASS);
		sCurrentWeight[i] = 0;
                
		sLatency[i] = (int) (System.currentTimeMillis() - start);
		
		conn.close();
            } catch (SQLException e) {
		//if one database is offline...
		sLatency[i] = 99999;
		e.printStackTrace();
            } catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
            } finally {
		// close resource 
		try {
		    if (conn!=null)
                        conn.close();
		} catch(SQLException se) {
		    se.printStackTrace();
		}
            }		
	}
	
	int sum = 0;
	for (int i : mLatency) {
            if(i < 90000)
		sum += i;
	}
	
	//give weights to write databases
	for (int i = 0; i < numOfMaster; i++) {
            if (mLatency[i] > 90000) {
		mWeight[i] =  0;
		continue;
            }
            
            if (sum > mLatency[i])
		mWeight[i] = sum - mLatency[i];
            else 
		mWeight[i] = mLatency[i];
        }
        
        mEffectiveWeight = mWeight.clone();
        
        sum = 0;
        for (int i : sLatency) {
            if (i < 90000)
                sum += i;
        }
        
        //give weights to read databases
	for(int i = 0; i < numOfSlave; i++) {
            if (sLatency[i] > 90000) {
		sWeight[i] =  0;
		continue;
            }
            
            if (sum > sLatency[i])
		sWeight[i] = sum - sLatency[i];
            else
		sWeight[i] = sLatency[i];
	}
        
	sEffectiveWeight = sWeight.clone();
    }
    
    
    /**
     * Get the IP index of one database for writing data<P/>
     * 
     * @return IP index of one database for writing
     */
    public int writeIP () {
	int max= 0;
	int maxIndex = 0;
	int totalWeight = 0;
	for (int i = 0; i < this.writeIP.length; i++) {
            mCurrentWeight[i] += mEffectiveWeight[i]; 
            totalWeight += mEffectiveWeight[i];
            if (mCurrentWeight[i] > max) {
		max = mCurrentWeight[i];
		maxIndex = i; 
            }
	}
	
	mCurrentWeight[maxIndex] -=  totalWeight;
	
	return maxIndex;
    }
    
    /**
     * Get the IP index of one database for reading data<P/>
     * 
     * @return IP index of the IP of one database for reading
     */
    public int readIP () {
	int max= 0;
	int maxIndex = 0;
	int totalWeight = 0;
	for (int i = 0; i < this.readIP.length; i++) {
            sCurrentWeight[i] += sEffectiveWeight[i]; 
            totalWeight += sEffectiveWeight[i];
            if (sCurrentWeight[i] > max) {
		max = sCurrentWeight[i];
		maxIndex = i; 
            }
	}
	
	sCurrentWeight[maxIndex] -=  totalWeight;
	
	return maxIndex;
    }
    
    /**
     * Reduce the weight of one write database.<P/>
     * Database that fails three times will be given a weight of 0.
     * 
     * @param index index of the database
     */
    public void mEffectWeightDown (int index) {
	if (mEffectiveWeight[index] > (0.33 * mWeight[index]))
            mEffectiveWeight[index] = (int) (0.69 * mEffectiveWeight[index]);
	else
            mEffectiveWeight[index] = 0;
    }
    
    /**
     * Return to the initial weight for a write database<P/>
     * 
     * @param index index of the database
     */
    public void mEffectWeightUp (int index) {
	if(mEffectiveWeight[index] < mWeight[index])
	mEffectiveWeight[index] = mWeight[index];
    }
    
    /**
     * Reduce the weight of one read database.<P/>
     * Database that fails three times will be given a weight of 0.
     * 
     * @param index index of the database
     */
    public void sEffectWeightDown (int index) {
	if(sEffectiveWeight[index] > 0.33 * sWeight[index])
            sEffectiveWeight[index] = (int) (0.69 * sEffectiveWeight[index]);
	else
            sEffectiveWeight[index] = 0;
    }
    
    /**
     * Return to the initial weight for a read database<P/>
     * 
     * @param index index of the database
     */
    public void sEffectWeightUp (int index) {
	if(sEffectiveWeight[index] < sWeight[index])
            sEffectiveWeight[index] = sWeight[index];
    }
    
    /**
     * Return the IP of a write database<P/>
     * 
     * @param index index of the database
     * @return IP of a write server 
     */
    public String getWIP (int index) { 
	return writeIP[index];
    }
    
    /**
     * Return the IP of a read database<P/>
     * 
     * @param index index of the database
     * @return IP of a read server
     */
    public String getRIP (int index) {
	return readIP[index];
    }

}

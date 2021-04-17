package org.askgaming.emailonshutdown.utils;

public class TpsChecker implements Runnable {

	public static int tickCount = 0;	
	public static long[] tick = new long[600];
	 
	public static double getTPS() {
	    return getTPS(100);
	}
	 
	public static double getTPS(int ticks) {
	    		
	    int target = (tickCount - 1 - ticks) % tick.length;
	    
	    long elapsed = System.currentTimeMillis() - tick[target];
	 
	    return ticks / (elapsed / 1000.0D);
	}
	 	 
	public void run() {
	   
		tick[(tickCount % tick.length)] = System.currentTimeMillis();	
		
		tickCount += 1;
	}
}

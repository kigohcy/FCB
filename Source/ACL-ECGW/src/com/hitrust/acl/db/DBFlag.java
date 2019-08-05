/*
 * @(#)DBFlag.java
 *
 * Copyright (c) 2004 HiTRUST Incorporated. All rights reserved.
 *
 * Modify History:
 *  v1.00, 2004/07/20, Jackie Yang
 *   1) First release
 *
 */
package com.hitrust.acl.db;

/**
 * DB Connection status flag
 *
 * @author  Jackie Yang
 * @version 1.00, 2004/07/20
 */
public class DBFlag {
	// The time in msec to judge this connection is idle
	private static long idleMaxMillis = 60000;  // 60 seconds

	// Current status
	private boolean inUse = false;    // In use flag
	private long lastAccess = 0;      // Time in use

	/**
	 * Constructor with initial status.
	 *
	 * @param   use  Staus of the connection.
	 */
	public DBFlag(boolean use) {
		this.inUse = use;
		this.lastAccess = System.currentTimeMillis();
	}

	/**
	 * Set using flag.
	 *
	 * @param   use  Staus of the connection.
	 */
	public void setInUse(boolean use) {
		// set the in use status & record current time
		this.inUse = use;
		this.lastAccess = System.currentTimeMillis();
	}

	/**
	 * Is in use
	 *
	 * @return  The status of using flag.
	 */
	public boolean isInUse() {
		// return the in use status
		return this.inUse;
	}

	/**
	 * Is in idle cause by user not call returnConnection()
	 *
	 * @return  The status of idle checking.
	 */
	public boolean isIdle() {
		// Compare with current time
		long pass = System.currentTimeMillis() - this.lastAccess;
		if(pass > idleMaxMillis) return true;
		return false;
	}

	/**
	 * Get the last access time in TimeMillis.
	 *
	 * @return  The last access time in msec.
	 */
	public long lastAccessTime() {
		// Compare with current time
		return this.lastAccess;
	}

	/**
	 * Set the idle time in second which is idle
	 *
	 * @param   sec  The max idle time in second.
	 */
	public static void setIdleTime(int sec) {
		// set the idle in msec
		idleMaxMillis = 1000L * sec;
	}
	
}
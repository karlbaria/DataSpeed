package com.example.dataspeed;


import android.net.TrafficStats;

class TrafficRecord {
	long tx=0;
	long rx=0;
	long time;
	
	String tag=null;
	
	TrafficRecord() {
		tx=TrafficStats.getTotalTxBytes();
		rx=TrafficStats.getTotalRxBytes();
		time = System.currentTimeMillis();
	}
	
	TrafficRecord(int uid, String tag) {
		tx=TrafficStats.getUidTxBytes(uid);
		rx=TrafficStats.getUidRxBytes(uid);
		time = System.currentTimeMillis();
		
		this.tag=tag;
	}
}
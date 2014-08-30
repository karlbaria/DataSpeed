package com.example.dataspeed;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class TrafficStat extends Activity {
	TextView latest_rx=null;
	TextView latest_tx=null;
	TextView latest_time=null;
	
	TextView previous_rx=null;
	TextView previous_tx=null;
	TextView previous_time=null;
	
	TextView delta_rx=null;
	TextView delta_tx=null;
	TextView delta_time=null;
	
	TextView download=null;
	TextView upload=null;
	
	TrafficSnapshot latest=null;
	TrafficSnapshot previous=null;
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		latest_rx=(TextView)findViewById(R.id.latest_rx);
		latest_tx=(TextView)findViewById(R.id.latest_tx);
		latest_time=(TextView)findViewById(R.id.latest_time);
		
		previous_rx=(TextView)findViewById(R.id.previous_rx);
		previous_tx=(TextView)findViewById(R.id.previous_tx);
		previous_time=(TextView)findViewById(R.id.previous_time);
		
		delta_rx=(TextView)findViewById(R.id.delta_rx);
		delta_tx=(TextView)findViewById(R.id.delta_tx);
		delta_time=(TextView)findViewById(R.id.delta_time);
		
		download = (TextView)findViewById(R.id.download);
		upload = (TextView)findViewById(R.id.upload);
		
		
		
		takeSnapshot(null);
	}
	
	public void takeSnapshot(View v) {
		previous=latest;
		latest=new TrafficSnapshot(this);
		
				
		latest_rx.setText(String.valueOf((latest.device.rx)/1000));
		latest_tx.setText(String.valueOf((latest.device.tx)/1000));
		
		//latest_time.setText(String.valueOf(latest.device.time));
		
		if (previous!=null) {
			previous_rx.setText(String.valueOf((previous.device.rx)/1000));
			previous_tx.setText(String.valueOf((previous.device.tx)/1000));
		
			//previous_time.setText(String.valueOf(previous.device.time));
			
			delta_rx.setText(String.valueOf((latest.device.rx-previous.device.rx)/1000)); //in KB
			delta_tx.setText(String.valueOf((latest.device.tx-previous.device.tx)/1000)); //in KB
			delta_time.setText(String.valueOf((latest.device.time-previous.device.time)/1000)); //in seconds
			
			
			download.setText(String.valueOf(((latest.device.rx-previous.device.rx)/1000)/((latest.device.time-previous.device.time)/1000)));
			upload.setText(String.valueOf(((latest.device.tx-previous.device.tx)/1000)/((latest.device.time-previous.device.time)/1000)));
		}
		
		ArrayList<String> log=new ArrayList<String>();
		HashSet<Integer> intersection=new HashSet<Integer>(latest.apps.keySet());
		
		if (previous!=null) {
			intersection.retainAll(previous.apps.keySet());
		}
		
		for (Integer uid : intersection) {
			TrafficRecord latest_rec=latest.apps.get(uid);
			TrafficRecord previous_rec=
						(previous==null ? null : previous.apps.get(uid));
			
			emitLog(latest_rec.tag, latest_rec, previous_rec, log);
		}
		
		Collections.sort(log);
		
		for (String row : log) {
			Log.d("TrafficMonitor", row);
		}
	}
	
	private void emitLog(CharSequence name, TrafficRecord latest_rec,
												TrafficRecord previous_rec,
												ArrayList<String> rows) {
		if (latest_rec.rx>-1 || latest_rec.tx>-1) {
			StringBuilder buf=new StringBuilder(name);
			
			buf.append("=");
			buf.append(String.valueOf(latest_rec.rx));
			buf.append(" received");
			
			
			if (previous_rec!=null) {
				buf.append(" (delta=");
				buf.append(String.valueOf(latest_rec.rx-previous_rec.rx));
				buf.append(")");
							
			}
			
			buf.append(", ");
			buf.append(String.valueOf(latest_rec.tx));
			buf.append(" sent");
			
			if (previous_rec!=null) {
				buf.append(" (delta=");
				buf.append(String.valueOf(latest_rec.tx-previous_rec.tx));
				buf.append(")");
				
			}
			
			
			rows.add(buf.toString());
		}
	}
}

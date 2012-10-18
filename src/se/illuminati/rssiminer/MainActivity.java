package se.illuminati.rssiminer;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{
	private static final String TAG = "RSSIMiner";
	WifiManager wifi;
	BroadcastReceiver receiver;

	TextView textStatus;
	Button scanButton;
	Button recordButton;

	boolean toFile = false;
	String fid = "0";

	Timer tim;
	boolean scanning = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Setup UI
		textStatus = (TextView) findViewById(R.id.textStatus);

		// Set up buttons and listeners
		scanButton = (Button) findViewById(R.id.buttonScan);
		scanButton.setOnClickListener(this);

		recordButton = (Button) findViewById(R.id.buttonRecord);
		recordButton.setOnClickListener(this);

		Button clearButton = (Button) findViewById(R.id.buttonClear);
		clearButton.setOnClickListener(this);

		// Setup WiFi
		wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

		// Setup Timer
		tim = new Timer(true);

		// Register Broadcast Receiver
		if (receiver == null)
			receiver = new WiFiScanReceiver(this);

		registerReceiver(receiver, new IntentFilter(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		Log.d(TAG, "onCreate()");
	}


	@Override
	public void onDestroy() {
		Log.d(TAG,"onDestroy()");
		this.unregisterReceiver(receiver);
		super.onDestroy();
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	

	public void onClick(View view) {
		// Scan button
		if (view.getId() == R.id.buttonScan) {
			Log.d(TAG, "onClick() Scan");

			if(scanning) {
				tim.cancel();
				scanButton.setText(R.string.scan_button);
			} else {
				tim.schedule(new TimerTask(){
					@Override
					public void run(){
						wifi.startScan();
					}
				}, 0, 2000);
				
				scanButton.setText(R.string.scan_button_scanning);
			}
			scanning = !scanning;

			// Record button
		} else if (view.getId() == R.id.buttonRecord) {
			Log.d(TAG, "onClick() Record");
			toFile = !toFile; 

			if(toFile) {
				Date d = new Date();
				fid = "data-" + d.getTime() + ".txt";
				recordButton.setText(R.string.record_button_recording);
				textStatus.setText("");
			} else {
				recordButton.setText(R.string.record_button);
				Toast.makeText(this, "File saved to " + fid,
						Toast.LENGTH_LONG).show();
			}
			
			// Clear button
		} else if (view.getId() == R.id.buttonClear) {
			Log.d(TAG, "onClick() Clear");
			textStatus.setText("");
		}
	}

	public void update(String s) {
		// Get WiFi status
		WifiInfo info = wifi.getConnectionInfo();
		textStatus.append("\nCurrent WiFi connection: " + info.toString() + "\n\n");
		textStatus.append(s);
		textStatus.append("-------------------------------");

		ScrollView sv = (ScrollView) findViewById(R.id.ScrollView);
		sv.fullScroll(ScrollView.FOCUS_DOWN);
		Log.d(TAG, "UI updated.");
	}
}

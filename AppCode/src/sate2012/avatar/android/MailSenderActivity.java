package sate2012.avatar.android;

import gupta.ashutosh.avatar.R;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.TextView;
import android.telephony.TelephonyManager;

public class MailSenderActivity extends Activity implements OnClickListener{
	public String subj;
	public String body;
	public String from;
	public String toList;
	public String ptName;
	public String ptDesc;
	public String ptType;
	public String ptURL;
	private String ptURL_noFTP;
	private String ptLat;
	private String ptLng;
	private String item_sep;
	private String LatLong;
	private Context c;
	private Button send;
	private Button button_return;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		c = super.getApplicationContext();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		body = "Body.";
		ptLat = "39.7";
		ptLng = "-84.2";
		final LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		final LocationListener mlocListener = new MyLocationListener();
		mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, (long) 2000, (float) 1.0, mlocListener); // (provider, time (ms), distance (m), listener)
		Intent thisIntent = getIntent();
		ptType = thisIntent.getStringExtra("Type");
		ptURL = "ftp://opensim:widdlyscuds@virtualdiscoverycenter.net/../../var/www/avatar/uploaded" + thisIntent.getStringExtra("Filename");
		ptURL_noFTP = "virtualdiscoverycenter.net/../../var/www/avatar/uploaded" + thisIntent.getStringExtra("Filename");
		ptName = thisIntent.getStringExtra("Filename");
		setContentView(R.layout.mail_prep_apv);
		setLayout(ptType);
		send = (Button) findViewById(R.id.Send);
		send.setOnClickListener(this);
	}
	
	public void onClick(View v) {
		switch(v.getId()){
			case(R.id.Send):
				try {
					from = "sate2012.avatar@gmail.com";
					toList = "sate2012.avatar@gmail.com";
					EditText etName = (EditText) findViewById(R.id.pointName);
					ptName = etName.getText().toString();
					EditText etDesc = (EditText) findViewById(R.id.pointDesc);
					ptDesc = etDesc.getText().toString();
					item_sep = getResources().getString(R.string.item_separator);
					subj = "POINT: " + ptName + item_sep + ptLat + item_sep + ptLng + item_sep + ptType + item_sep + ptDesc;
					GMailSender sender = new GMailSender("sate2012.avatar@gmail.com", "EmbraceChaos");
					sender.sendMail(subj, body, from, toList);
					setContentView(R.layout.sent);
					button_return = (Button) findViewById(R.id.Return);
					button_return.setOnClickListener(this);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "EXCEPTION: " + e, Toast.LENGTH_LONG).show();
					setContentView(R.layout.send_failed);
				}
				break;
			case(R.id.Return):
				finish();
				break;
		}
	}
	
	public void setLayout(String Type) {
		if (Type.equals(getResources().getString(R.string.type_comment)))
			setContentView(R.layout.mail_prep_comment);
		else if (Type.equals(getResources().getString(R.string.type_android))) {
			setContentView(R.layout.mail_prep_android);
			TextView pointDesc = (TextView) findViewById(R.id.pointDesc);
			TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			pointDesc.setText(telephonyManager.getDeviceId());
		} else if (Type.equals(getResources().getString(R.string.type_audio)) || Type.equals(getResources().getString(R.string.type_picture)) || Type.equals(getResources().getString(R.string.type_video))) {
			setContentView(R.layout.mail_prep_apv);
			TextView pointDesc = (TextView) findViewById(R.id.pointDesc);
			pointDesc.setText(ptURL_noFTP);
			TextView pointName = (TextView) findViewById(R.id.pointName);
			pointName.setText(ptName);
		}
	}
	
	private class MyLocationListener implements LocationListener {
		public void onLocationChanged(Location loc) {
			Toast.makeText(c, "Getting Location.", Toast.LENGTH_SHORT).show();
			ptLat = "" + loc.getLatitude();
			ptLng = "" + loc.getLongitude();
		}
		public void onProviderDisabled(String provider) { Toast.makeText(c, "GPS Disabled", Toast.LENGTH_SHORT).show(); }
		public void onProviderEnabled(String provider) { Toast.makeText(c, "GPS Enabled", Toast.LENGTH_SHORT).show(); }
		public void onStatusChanged(String provider, int status, Bundle extras) {}
	}
}
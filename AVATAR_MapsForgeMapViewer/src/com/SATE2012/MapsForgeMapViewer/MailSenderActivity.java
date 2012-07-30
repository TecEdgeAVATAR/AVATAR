package com.SATE2012.MapsForgeMapViewer;


//import sate2012.avatar.android.GPSActivity.MyLocationListener;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.TextView;
import android.telephony.TelephonyManager;

public class MailSenderActivity extends Activity {
	public String subj = "";
	public String body = "Body.";
	public String from = "";
	public String toList = "";
	public String ptName = "";
	public String ptDesc = "";
	public String ptType = "";
	public String ptURL = "";
	private String ptURL_noFTP = "";

	public String ptLat = "39.7";
	public String ptLng = "-84.2";
	public String item_sep = "";
	public String LatLong = "";
	Context conn;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Toast.makeText(getApplicationContext(),
		// "Creating MailSenderActivity", Toast.LENGTH_LONG).show();
		conn = super.getApplicationContext();
		final LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		final LocationListener mlocListener = new MyLocationListener();
		mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				(long) 2000, (float) 1.0, mlocListener); // (provider, time
															// (ms), distance
															// (m), listener)

		Intent thisIntent = getIntent();
		ptType = thisIntent.getStringExtra("Type");
		ptURL = "ftp://opensim:widdlyscuds@virtualdiscoverycenter.net/../../var/www/avatar/uploaded"
				+ thisIntent.getStringExtra("Filename");
		ptURL_noFTP = "virtualdiscoverycenter.net/../../var/www/avatar/uploaded" 
				+ thisIntent.getStringExtra("Filename");
		ptName = thisIntent.getStringExtra("Filename");
		// Toast.makeText(getApplicationContext(), "MSA: ptURL: "+ptURL,
		// Toast.LENGTH_LONG).show();

		setContentView(R.layout.mail_prep_apv);
		setLayout(ptType);

		// TextView pointDesc = new TextView(this);
		// pointDesc.setTextSize(40);
		// pointDesc.setText("Hello");
		// setContentView(pointDesc);

		/*
		 * TextView pointDesc = (TextView)findViewById(R.id.pointDesc);
		 * //retrieve a reference to an instance of TelephonyManager
		 * TelephonyManager telephonyManager =
		 * (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		 * pointDesc.setText(telephonyManager.getDeviceId());
		 */

		final Button send = (Button) this.findViewById(R.id.Send);
		send.setOnClickListener(new View.OnClickListener() {

			public void onClick(View buttonSend) {
				// TODO Auto-generated method stub

				try {

					// Toast.makeText(getApplicationContext(),
					// "You clicked Send", Toast.LENGTH_LONG).show();

					from = "sate2012.avatar@gmail.com";
					/*
					 * EditText etToList = (EditText)
					 * findViewById(R.id.AddlAddress); if
					 * (etToList.getText().toString() != "") {toList =
					 * "jason88k@gmail.com, " + etToList.getText().toString();}
					 * else {toList = "jason88k@gmail.com";}
					 */
					toList = "sate2012.avatar@gmail.com";

					EditText etName = (EditText) findViewById(R.id.pointName);
					ptName = etName.getText().toString();
					EditText etDesc = (EditText) findViewById(R.id.pointDesc);
					ptDesc = etDesc.getText().toString();

					item_sep = getResources()
							.getString(R.string.item_separator);
					subj = "POINT: " + ptName + item_sep + ptLat + item_sep
							+ ptLng + item_sep + ptType + item_sep + ptDesc;

					// Toast.makeText(getApplicationContext(), subj,
					// Toast.LENGTH_SHORT).show();

					// setcontentView(R.layout.confirm_content);

					GMailSender sender = new GMailSender(
							"sate2012.avatar@gmail.com", "EmbraceChaos");
					// Toast.makeText(getApplicationContext(),
					// "Established Connection", Toast.LENGTH_LONG).show();
					sender.sendMail(subj, body, from, toList);
					setContentView(R.layout.sent);
					final Button button_return = (Button) findViewById(R.id.Return);
					button_return
							.setOnClickListener(new View.OnClickListener() {

								public void onClick(View w) {
									// Toast.makeText(getApplicationContext(),
									// "Returning to main screen",
									// Toast.LENGTH_LONG).show();
									// setContentView(R.layout.mail_prep);
									finish();
								}
							});

				} catch (Exception e) {
					Log.e("SendMail", e.getMessage(), e);
					Toast.makeText(getApplicationContext(), "EXCEPTION: " + e,
							Toast.LENGTH_LONG).show();
					setContentView(R.layout.send_failed);
				}

			} // end of public void onClick
		}); // end of send.setonClickListener

	} // end of onCreate

	public void setLayout(String Type) {

		if (Type.equals(getResources().getString(R.string.type_comment))) {
			setContentView(R.layout.mail_prep_comment);
		} else if (Type.equals(getResources().getString(R.string.type_android))) {
			setContentView(R.layout.mail_prep_android);
			TextView pointDesc = (TextView) findViewById(R.id.pointDesc);
			// retrieve a reference to an instance of TelephonyManager
			TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			pointDesc.setText(telephonyManager.getDeviceId());
		} else if (Type.equals(getResources().getString(R.string.type_audio))
				|| Type.equals(getResources().getString(R.string.type_picture))
				|| Type.equals(getResources().getString(R.string.type_video))) {
			setContentView(R.layout.mail_prep_apv);
			// Toast.makeText(getApplicationContext(), "Making layout: "+ptURL,
			// Toast.LENGTH_SHORT).show();
			TextView pointDesc = (TextView) findViewById(R.id.pointDesc);
			pointDesc.setText(ptURL_noFTP);
			TextView pointName = (TextView) findViewById(R.id.pointName);
			pointName.setText(ptName);
		}

	}

	public class MyLocationListener implements LocationListener {
		public void onLocationChanged(Location loc) {
			Toast.makeText(getApplicationContext(), "Getting Location.",
					Toast.LENGTH_SHORT).show();
			// loc.getLatitude();
			// loc.getLongitude();
			// item_sep = getResources().getString(R.string.item_separator);
			ptLat = "" + loc.getLatitude();
			ptLng = "" + loc.getLongitude();
			// LatLong = loc.getLatitude() + item_sep + loc.getLongitude();
			// LatLong = "Your Location"+item_sep + loc.getLatitude() + item_sep
			// + loc.getLongitude() + item_sep
			// +"Android"+item_sep+"http://www.wbi-icc.com/;~~AF Museum"+item_sep+"39.781172"+item_sep+"-84.110317"+item_sep+"Video"+item_sep+"http://www.nationalmuseum.af.mil/;~~Woodman and Burkhardt"+item_sep+"39.7594"+item_sep+"-84.120381"+item_sep+"Picture"+item_sep+"http://www.panoramio.com/photo/55850571?source=wapi&referrer=kh.google.com";
			// Toast.makeText( getApplicationContext(), LatLong,
			// Toast.LENGTH_SHORT).show();
		}

		public void onProviderDisabled(String provider) {
			Toast.makeText(getApplicationContext(), "GPS Disabled",
					Toast.LENGTH_SHORT).show();
		}

		public void onProviderEnabled(String provider) {
			Toast.makeText(getApplicationContext(), "GPS Enabled",
					Toast.LENGTH_SHORT).show();
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

	} // End of Class MyLocationListener

}
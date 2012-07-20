package sate2012.avatar.android;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;



public class GPSActivity extends Activity implements View.OnClickListener{
    LocationManager mlocManager;
    LocationListener mlocListener;
	Context conn;
		
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gps_main);
        conn = super.getApplicationContext();
        
        /* Use the LocationManager class to obtain GPS locations */ 

        mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE); 
        mlocListener = new MyLocationListener(); 
        mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, mlocListener); 

    }  
    
    
    
	public void httpPost(String mess)
    {
     try {
     mess = mess.replaceAll(" ", "%20");     
     String uri = "http://www.wbi-icc.com/students/SL/SmartPhone/smart_save.php?text=" + mess;
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response = httpclient.execute(new HttpGet(uri));
        response.getEntity().getContent();
     }
     catch(Exception e)
     {
        Log.e("log_tag", "Error in http connection "+e.toString());
     }  
    }
    
    /* Class My Location Listener */ 
    public class MyLocationListener implements LocationListener 
    { 	
    	public void onLocationChanged(Location loc) 
    	{ 
    		
    		loc.getLatitude();
    		loc.getLongitude();
    		String LatLong = loc.getLatitude() +  " --- " + loc.getLongitude();
    		//String LatLong = loc.getLatitude() +  " --- " + loc.getLongitude() + ";~~40.12345 --- -85.12345;~~41.54321 --- -83.54321";
    		//Toast.makeText( getApplicationContext(), LatLong, Toast.LENGTH_SHORT).show(); 
    		httpPost(LatLong);
    		
    	} 



    public void onProviderDisabled(String provider) 
    	{ 
    		Toast.makeText( getApplicationContext(), "GPS Disabled", Toast.LENGTH_SHORT ).show(); 
    	} 

    public void onProviderEnabled(String provider) 
    	{ 
    		Toast.makeText( getApplicationContext(), "GPS Enabled",	Toast.LENGTH_SHORT).show(); 
    	} 

    public void onStatusChanged(String provider, int status, Bundle extras) 
    	{ 
    	} 

    }/* End of Class MyLocationListener */ 
    
    @Override
	protected void onStop() {
    	mlocManager.removeUpdates(mlocListener);
    	finish();
		super.onStop();
	    
    }



	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
   

   }
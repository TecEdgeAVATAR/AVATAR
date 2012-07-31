package sate2012.avatar.android;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.mapsforge.android.maps.GeoPoint;
import org.mapsforge.android.maps.MapActivity;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.MapViewMode;
import org.mapsforge.android.maps.Overlay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MapsForgeMapViewer extends MapActivity implements LocationListener
{
    private MapView mapView;
    private Location mostRecentLocation;
    private GeoPoint myCurrentLocation;
    private Location loc;
    private double locLat;
    private double locLon;
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    private static final int MEDIA_SELECTOR_REQUEST_CODE = 1845235;
    private double pointLocLat;
    private double pointLocLon;
    private Button findPosition;
    private Button exit;
    private Button UpdateCoordinates;
    private Button ClearPointsButton;
    MVItemizedOverlay itemizedOverlay = null;
    MVItemizedOverlay userPointOverlay = null;
    PointSetter pointer;
    Drawable locationMarker;
    Drawable newMarker;
    Drawable newPoint;
    private static SensorManager mySensorManager;
    public static String EXTRA_MESSAGE;
    private boolean sensorrunning;
    private Compass myCompassView;
    private Overlay[] overlayHolder = new Overlay[2];
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

    public ArrayList<DataObjectItem> dataList = new ArrayList<DataObjectItem>();

    protected void onCreate(Bundle savedInstanceState)
    {
	Log.d("DEBUG", "Starting program");
	super.onCreate(savedInstanceState);
	
	//Remove title bar
    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	
	this.setContentView(R.layout.map_view);
	conn = super.getApplicationContext();
	myCompassView = (Compass) findViewById(R.id.mycompassview);
	mySensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	List<Sensor> mySensors = mySensorManager
		.getSensorList(Sensor.TYPE_ORIENTATION);

	if (mySensors.size() > 0)
	{
	    mySensorManager.registerListener(mySensorEventListener,
		    mySensors.get(0), SensorManager.SENSOR_DELAY_NORMAL);
	    sensorrunning = true;
	    // TODO: Remove Toast

	}

	else
	{
	    Toast.makeText(this, "No Orientation Sensor", Toast.LENGTH_LONG)
		    .show();
	    sensorrunning = false;
	    finish();
	}

	LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	LocationListener mlocListener = new MyLocationListener();
	/*
	 * This is the code that will be used to change the way that the program
	 * gets the location data. The difference is in
	 * theLocationManager.GPS_PROVIDER. This allows the tablet to use GPS as
	 * opposed to using a location provider (WIFI).
	 */
	// mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
	// 10000,
	// 0, mlocListener);
	mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
		100, 0, mlocListener);
	findPosition = (Button) findViewById(R.id.findPositionButton);
	exit = (Button) findViewById(R.id.Exit);
	ClearPointsButton = (Button) findViewById(R.id.Clear_Points_Button);

	this.findPosition.setOnClickListener(new View.OnClickListener()
	{
	    @Override
	    public void onClick(View v)
	    {
		findPositionButton(myCurrentLocation);
	    }
	});

	this.exit.setOnClickListener(new View.OnClickListener()
	{

	    @Override
	    public void onClick(View v)
	    {
		finish();
	    }
	});

	this.ClearPointsButton.setOnClickListener(new View.OnClickListener()
	{
	    @Override
	    public void onClick(View v)
	    {
		userPointOverlay.clear();
	    }
	});

	mapView = (MapView) findViewById(R.id.mapView);
	// sets the URL where the device downloads the map
	mapView.setMapViewMode(MapViewMode.MAPNIK_TILE_DOWNLOAD);
	Log.d("DEBUG", "setBuiltZoomControls: @ location of");
	mapView.setBuiltInZoomControls(true);
	mapView.setScaleBar(true);
	mapView.setClickable(true);
	// Log.d("DEBUG", "onCreate: At the end of the onCreate method");
	findPositionButton(myCurrentLocation);
	mapView.getController().setZoom(14);
	setCenterlocation();

	locationMarker = getResources().getDrawable(R.drawable.ic_launcher);
	this.pointer = new PointSetter(locationMarker, this);
	newMarker = getResources().getDrawable(R.drawable.ic_launcher);
	this.pointer = new PointSetter(newMarker, this);
	newPoint = getResources().getDrawable(R.drawable.ic_launcher);
	this.pointer = new PointSetter(newPoint, this);
	mapView.getOverlays().add(this.pointer);
	MailSenderActivity MailSender = new MailSenderActivity();

	userPointOverlay = new MVItemizedOverlay(newMarker);
	itemizedOverlay = new MVItemizedOverlay(newMarker);
	mapView.getOverlays().add(userPointOverlay);
	mapView.getOverlays().add(itemizedOverlay);
    }

    /**
     * the SensorEventListener class is one of the classes which runs the
     * compass sensor in the application.
     */
    private SensorEventListener mySensorEventListener = new SensorEventListener()
    {
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy)
	{

	}

	public void onSensorChanged(SensorEvent event)
	{
	    myCompassView.updateDirection((float) event.values[0]);
	}
    };

    @Override
    /**
     * onDestroy stops or "destroys" the program when this actions is called.
     */
    protected void onDestroy()
    {
	super.onDestroy();
	if (sensorrunning)
	{
	    mySensorManager.unregisterListener(mySensorEventListener);
	}
    }

    @Override
    /**
     * onPause pauses the application and saves the data in the savedInstances Bundle. 
     */
    protected void onPause()
    {
	super.onPause();
    }

    @Override
    /**
     * onResume sets the actions that the application runs through when starting the application from pause.
     */
    protected void onResume()
    {
	super.onResume();
    }
    
    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) 
    {
		if ((resultCode == Activity.RESULT_OK) && (requestCode == MEDIA_SELECTOR_REQUEST_CODE)) 
		{
			// Change the icon to that of the chosen media type
			DataObjectItem pointToChange = dataList.get(dataList.size() - 1);
			Drawable newPoint;
			int iconValue = intent.getIntExtra(getString(R.string.iconValueLocation), 0);
						
			switch (iconValue)
			{
			    case 0:
			    	newPoint = getResources().getDrawable(R.drawable.black_questionmark);
			    	break;
			    case 1:
			    	newPoint = getResources().getDrawable(R.drawable.red_video_marker);
			    	break;
			    case 2:
			    	newPoint = getResources().getDrawable(R.drawable.blue_camera_marker);
			    	break;
			    case 3:
			    	newPoint = getResources().getDrawable(R.drawable.yellow_microphone_marker);
			    	break;
			    case 4:
			    	newPoint = getResources().getDrawable(R.drawable.green_document_marker);
			    	break;
			    default:
			    	newPoint = getResources().getDrawable(R.drawable.black_questionmark);
					break;
			}
			pointToChange.setMarker(MVItemizedOverlay.boundCenterBottom(newPoint));
			
			// If cancel was selected, then delete the point
			if(iconValue == 0)
			{
				dataList.remove(dataList.size() - 1);
				userPointOverlay.removeOverlay(userPointOverlay.size() - 1);
			}
			
			// Update the gui
			userPointOverlay.requestRedraw();
		}
		// The user backed out of the dialog without making a selection
		else if (requestCode == MEDIA_SELECTOR_REQUEST_CODE)
		{
			// Delete the point
			dataList.remove(dataList.size() - 1);
			userPointOverlay.removeOverlay(userPointOverlay.size() - 1);
			
			// Update the gui
			userPointOverlay.requestRedraw();
		}
	}

    /**
     * setCenterLocation is the method that sets the center of the screen to a
     * already specified point on the map.
     */

    protected void setCenterlocation()
    {
	if (myCurrentLocation == null)
	{
	    // mapView.getController().setCenter(new GeoPoint(39.00, -100.00));
	}

	else
	{
	    mapView.getController().setCenter(myCurrentLocation);
	}
    }

    /*
     * private Runnable checkConnectionTask = new Runnable() { public void run()
     * { // Do something
     * 
     * tHandler.postAtTime(this, SystemClock.uptimeMillis() + 1000); // Reset //
     * the // timer
     * 
     * } };
     */

    /**
     * findPositionButton takes GeoPoint p as its argument and sets the center
     * of the screen to that point.
     * 
     * @param p
     */
    public void findPositionButton(GeoPoint p)
    {
	mapView.getController().setCenter(p);
    }

    /**
     * This class listens for the location manager. When the location manager
     * sends the location listener the files for the map, it runs through the
     * methods within the class body.
     * 
     * @author William
     * 
     */
    public class MyLocationListener implements LocationListener
    {
	@Override
	public void onLocationChanged(Location loc)
	{
	    GeoPoint gp = new GeoPoint(loc.getLatitude(), loc.getLongitude());

	    if (gp != null)
	    {
		myCurrentLocation = gp;
	    }

	    String LatLong = "Point1 --- "
		    + loc.getLatitude()
		    + " --- "
		    + loc.getLongitude()
		    + ";~~Point2 --- 40.12345 --- -85.12345;~~Point3 --- 41.54321 --- -83.54321";
	    Toast.makeText(getApplicationContext(), "You are here: ",
		    Toast.LENGTH_SHORT).show();

	    DataObject data = new DataObject();

	    Drawable newMarker = getResources().getDrawable(
		    R.drawable.ic_launcher);
	    Drawable locationMarker = getResources().getDrawable(
		    R.drawable.ic_launcher);
	    DataObjectItem overlayItem = new DataObjectItem(gp, data);
	    overlayItem.setMarker(MVItemizedOverlay
		    .boundCenterBottom(newMarker));

	    DataObjectItem myLocationMarker = new DataObjectItem(
		    myCurrentLocation, data);
	    myLocationMarker.setMarker(MVItemizedOverlay
		    .boundCenterBottom(locationMarker));

	    itemizedOverlay.addOverlay(overlayItem);

	    latLongConverter(gp);
	}

	/**
	 * activates when the current provider is disabled, or not available
	 * anymore.
	 */
	public void onProviderDisabled(String provider)
	{
	    Toast.makeText(getApplicationContext(), "GPS Disabled",
		    Toast.LENGTH_LONG).show();
	}

	/**
	 * activates when a provider is found.
	 */
	public void onProviderEnabled(String provider)
	{
	    Toast.makeText(getApplicationContext(), "GPS Enabled",
		    Toast.LENGTH_LONG).show();
	}

	public void onStatusChanged(String provider, int status, Bundle extras)
	{

	}
    }

    /**
     * Determines whether one Location reading is better than the current
     * Location fix
     * 
     * @param location
     *            The new Location that you want to evaluate
     * @param myCurrentLocation
     *            The current Location fix, to which you want to compare the new
     *            one
     */
    protected boolean isBetterLocation(Location location,
	    Location myCurrentLocation)
    {
	if (myCurrentLocation == null)
	{
	    return true;
	}

	// Check whether the new location fix is newer or older
	long timeDelta = location.getTime() - myCurrentLocation.getTime();
	boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
	boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
	boolean isNewer = timeDelta > 0;

	// If it's been more than two minutes since the current location, use
	// the new location because the user has likely moved
	if (isSignificantlyNewer)
	{
	    return true;
	}
	// If the new location is more than two minutes older, it must be
	// worse
	else if (isSignificantlyOlder)
	{
	    return false;
	}

	// Check whether the new location fix is more or less accurate
	int accuracyDelta = (int) (location.getAccuracy() - myCurrentLocation
		.getAccuracy());
	boolean isLessAccurate = accuracyDelta > 0;
	boolean isMoreAccurate = accuracyDelta < 0;
	boolean isSignificantlyLessAccurate = accuracyDelta > 200;

	// Check if the old and new location are from the same provider
	boolean isFromSameProvider = isSameProvider(location.getProvider(),
		myCurrentLocation.getProvider());

	// Determine location quality using a combination of timeliness and
	// accuracy
	if (isMoreAccurate)
	{
	    return true;
	} else if (isNewer && !isLessAccurate)
	{
	    return true;
	} else if (isNewer && !isSignificantlyLessAccurate
		&& isFromSameProvider)
	{
	    return true;
	}
	return false;
    }

    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2)
    {
	if (provider1 == null)
	{
	    return provider2 == null;
	}
	return provider1.equals(provider2);
    }

    public void sendGeoPointToMainClass(Location loc)
    {
	// This method takes the location variable from the location listener
	// and changes it into a GeoPoint variable
	locLat = loc.getLatitude();
	locLon = loc.getLongitude();
	Toast.makeText(getApplicationContext(), locLat + ", " + locLon,
		Toast.LENGTH_LONG).show();
	myCurrentLocation = new GeoPoint((int) (locLat * 1E6),
		(int) (locLon * 1E6));
    }

    private class PointSetter extends MVItemizedOverlay
    {
	Context context;

	public PointSetter(Drawable marker, Context contextIn)
	{
	    super(marker, contextIn);
	    this.context = contextIn;
	}

	@Override
	public boolean onLongPress(GeoPoint point, MapView mapView)
	{
	    // TODO every time that the onLongPress function is called, it will
	    // use the MailSenderActivity to send that new point to the server.
	    DataObject data = new DataObject();
	    if (point != null)
	    {
		pointLocLat = point.getLatitude();
		pointLocLon = point.getLongitude();

		String pointLocLatString = "" + pointLocLat;
		String pointLocLonString = "" + pointLocLon;

		String newPoint1 = pointLocLatString
			+ getResources().getString(R.string.item_separator)
			+ pointLocLonString;
		//this code is what actually does the send function in the Mail Sender Activ
		
		
		Intent senderIntent = new Intent(getApplicationContext(),
			MailSenderActivity.class);
		senderIntent.putExtra("lat", new Double(pointLocLat));
		senderIntent.putExtra("lon", new Double(pointLocLon));
//		senderIntent.putExtra(name, value);
		startActivity(senderIntent);

		DataObjectItem newPointItem = new DataObjectItem(point, data);

		Drawable newPoint = getResources().getDrawable(R.drawable.black_questionmark);
		
		newPointItem.setMarker(MVItemizedOverlay.boundCenterBottom(newPoint));
		userPointOverlay.addOverlay(newPointItem);
		data.setLat(pointLocLat);
		data.setLon(pointLocLon);
		dataList.add(newPointItem);

		// Open up the media selector Dialog
		//Intent intent = new Intent(getApplicationContext(), MediaSelectorDialog.class);
		//startActivityForResult(intent, MEDIA_SELECTOR_REQUEST_CODE);
	    }

	    return true;
	}
    }

    public void latLongConverter(GeoPoint gp)
    {

	// takes the location (in the variable mostRecentLocation)
	// and converts them into lat/long coordinates
	Log.d("DEBUG", "latLongConverter: @ beginning of LatLongConverter");
	if (isBetterLocation(loc, mostRecentLocation) == true)
	{
	    String locationString = gp.getLatitude() + "_***_"
		    + gp.getLongitude();
	    double llConvtLat = gp.getLatitudeE6();
	    double llConvtLong = gp.getLongitudeE6();
	    String lat = Double.toString(llConvtLat);
	    String lon = Double.toString(llConvtLong);
	    Toast.makeText(getApplicationContext(), lat + ", " + lon,
		    Toast.LENGTH_LONG).show();
	    Log.d("DEBUG", "latLongConverter: " + locationString);
	    httpPost("name_***_" + locationString
		    + ";~~name2_***_39.7_***_-84.2;~~");
	    gp = myCurrentLocation;
	} else
	{
	    Toast.makeText(
		    getApplicationContext(),
		    "the application ended at the else statement of latLongConverer",
		    Toast.LENGTH_LONG).show();
	}
    }

    public void httpPost(String mess)
    {
	// sends the lat/long coordinates to the URL (String uri)
	try
	{
	    mess = mess.replaceAll(" ", "%20");
	    String uri = "http://www.wbi-icc.com/students/SL/SmartPhone/smart_save.php?text="
		    + mess;
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpResponse response = httpclient.execute(new HttpGet(uri));
	    response.getEntity().getContent();
	} catch (Exception e)
	{
	    Log.e("log_tag", "Error in http connection " + e.toString());
	}
    }

    public void getEMEIID()
    {
	// to get the IMEID
	// SmartDeviceId = android.telephony.TelephonyManager.getDeviceId();
    }

    @Override
    public void onLocationChanged(Location arg0)
    {
	Log.d("DEBUG",
		"onLocationChanged: @beginning of Method onLocationChanged");
    }

    @Override
    public void onProviderDisabled(String provider)
    {

    }

    @Override
    public void onProviderEnabled(String provider)
    {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {
	Log.d("DEBUG", "onStatusChanged: @ beginning of method onStatusChanged");
    }
}

package com.SATE2012.MapsForgeMapViewer;

import java.util.ArrayList;

import org.mapsforge.android.maps.GeoPoint;
import org.mapsforge.android.maps.MapActivity;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.MapViewMode;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

public class tester extends MapActivity implements LocationListener
{
    /*
     * these two variables are for mapView(locationInstance) itself and the
     * variable for the location(location.getLat()) and location.getLong())
     */
    public String SmartDeviceId;
    private MapView mapView;
    Location mostRecentLocation;
    GeoPoint myCurrentLocation;
    PointSetter createObvOverlay;
    GeoPoint p;
    Location loc;
    double locLat;
    double locLon;
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    double pointLocLat;
    double pointLocLon;
    Button updatePosn;
    // A class variable at the top
    MVItemizedOverlay itemizedOverlay = null;
    public ArrayList<DataObjectItem> dataList = new ArrayList<DataObjectItem>();

    protected void onCreate(Bundle savedInstanceState)
    {
		Log.d("DEBUG", "Starting program");
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.main);
		LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		LocationListener mlocListener = new MyLocationListener();
		mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, mlocListener);
	
		mapView = (MapView) findViewById(R.id.mapView);
		// sets the URL where the device downloads the map
		mapView.setMapViewMode(MapViewMode.MAPNIK_TILE_DOWNLOAD);
		Log.d("DEBUG", "setBuiltZoomControls: @ location of");
		mapView.setBuiltInZoomControls(true);
		mapView.setScaleBar(true);
		mapView.setClickable(true);
		Log.d("DEBUG", "onCreate: At the end of the onCreate method");
		mapView.getController().setCenter(new GeoPoint(39.00, -100.00));
		mapView.getController().setZoom(14);
		populateOverlay();
    }
    
    public class MyLocationListener implements LocationListener
	{
		@Override
		public void onLocationChanged(Location loc)
		{
		    loc.getLatitude();
		    loc.getLongitude();
		    // String LatLong = loc.getLatitude() + " --- " +
		    // loc.getLongitude();
		    String LatLong = "Point1 --- "
			    + loc.getLatitude()
			    + " --- "
			    + loc.getLongitude()
			    + ";~~Point2 --- 40.12345 --- -85.12345;~~Point3 --- 41.54321 --- -83.54321";
		    // Toast.makeText( getApplicationContext(), LatLong,
		    // Toast.LENGTH_SHORT).show();
		    //TODO: send the location data to a wherever it needs to go.
		    Toast.makeText(getApplicationContext(), "I know where you are",
			    Toast.LENGTH_SHORT).show();
	
		    sendGeoPointToMainClass(loc);
		}
	
		public void onProviderDisabled(String provider)
		{
		    Toast.makeText(getApplicationContext(), "GPS Disabled",
			    Toast.LENGTH_SHORT).show();
		}
	
		public void onProviderEnabled(String provider)
		{
		    Toast.makeText(getApplicationContext(), "GPS Enabled",
			    Toast.LENGTH_SHORT).show();
		}
	
		public void onStatusChanged(String provider, int status, Bundle extras)
		{
			
		}
	
	}


    /**
     * Determines whether one Location reading is better than the current
     * Location fix
     * 
     * @param location The new Location that you want to evaluate
     * @param myCurrentLocation The current Location fix, to which you want to compare the new one
     */
    protected boolean isBetterLocation(Location location,
	    Location myCurrentLocation)
	{
		if (myCurrentLocation == null)
		{
		    // A new location is always better than no location
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
		    // If the new location is more than two minutes older, it must be
		    // worse
		} 
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
		} 
		else if (isNewer && !isLessAccurate)
		{
		    return true;
		} 
		else if (isNewer && !isSignificantlyLessAccurate
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

    public GeoPoint sendGeoPointToMainClass(Location loc)
    {
		// This method takes the location variable from the location listener
		// and changes it
		// into a GeoPoint variable
		locLat = loc.getLatitude();
		locLon = loc.getLongitude();
		Toast.makeText(getApplicationContext(), locLat + ", " + locLon,
			Toast.LENGTH_LONG).show();
		myCurrentLocation = new GeoPoint((int) (locLat * 1E6),
			(int) (locLon * 1E6));
		return myCurrentLocation;
	
		// make sure to see if the other methods can see location
    }

    public void populateOverlay()
	{
		// ObservationData observationData = dataIn;
	
		// userZoomLevel = mapView.getZoomLevel();
	
		mapView.getController().setCenter(p);
		mapView.getController().setZoom(12);
	
		DataObject data = new DataObject();
	
		Drawable newMarker = getResources().getDrawable(R.drawable.ic_launcher);
		Drawable locationMarker = getResources().getDrawable(
			R.drawable.ic_launcher);
		// the standard marker for holding the
		DataObjectItem overlayItem = new DataObjectItem(p, data);
		overlayItem.setMarker(MVItemizedOverlay.boundCenterBottom(newMarker));
	
		DataObjectItem myLocationMarker = new DataObjectItem(myCurrentLocation,
			data);
		myLocationMarker.setMarker(MVItemizedOverlay
			.boundCenterBottom(locationMarker));
	
		itemizedOverlay = new MVItemizedOverlay(newMarker,
			getApplicationContext());
		itemizedOverlay = new MVItemizedOverlay(locationMarker,
			getApplicationContext());
	
		itemizedOverlay.addOverlay(overlayItem);
	
		mapView.getOverlays().add(itemizedOverlay);
	
		this.createObvOverlay = new PointSetter(newMarker, this);
		this.createObvOverlay = new PointSetter(locationMarker, this);
		mapView.getOverlays().add(this.createObvOverlay);
	}

    private class PointSetter extends MVItemizedOverlay
	{
		public PointSetter(Drawable marker, Context context)
		{
		    super(marker, context);
		}
	
		// TODO: find error which prohibits the use of the onLongPress function.
		// this error only appears when the user actually onLongPresses the
		// screen
	
		@Override
		public boolean onLongPress(GeoPoint point, MapView mapView)
		{
		    Intent intent = new Intent();
		    DataObject data = new DataObject();
		    data.setLat(pointLocLat = point.getLatitude());
		    data.setLon(pointLocLon = point.getLongitude());
		    // gets the lat long points components of the point pressed by the
		    // user
		    // sets the lat, long components of the point to the marker that it
		    // will use
		    pointLocLat = point.getLatitude();
		    pointLocLon = point.getLongitude();
		    Toast.makeText(getApplicationContext(),
			    pointLocLat + ", " + pointLocLon, Toast.LENGTH_LONG).show();
		    // prints the location components of teh point onto the screen for
		    // the user
	
		    Toast.makeText(getApplicationContext(),
			    "onLongPress: DEBUG: @ class", Toast.LENGTH_LONG).show();
		    // userZoomLevel = mapView.getZoomLevel();
		    // intent.putExtra("i'm here",data);
		    // startActivityForResult(intent, 1);
		    return true;
		}
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

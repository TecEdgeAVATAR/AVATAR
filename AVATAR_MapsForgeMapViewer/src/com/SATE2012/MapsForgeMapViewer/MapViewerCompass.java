package com.SATE2012.MapsForgeMapViewer;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

public class MapViewerCompass extends Activity implements SensorEventListener
{
    private SensorManager mSensorManager;
    private Sensor mOrientation;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
		// Do something here if sensor accuracy changes.
		// You must implement this callback in your code.
    }

    @Override
    protected void onResume()
    {
		super.onResume();
		mSensorManager.registerListener(this, mOrientation,
			SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause()
    {
		super.onPause();
		mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
		float azimuth_angle = event.values[0];
		float pitch_angle = event.values[1];
		float roll_angle = event.values[2];
		// Do something with these orientation angles.
    }
}

package sate2012.avatar.android;

import gupta.ashutosh.avatar.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

// Documentation and formatting for this file is lacking due to time constraints
public class GPSViewer extends Activity
{
	private static final String TAG = "GPSViewer";
	
	private Handler handler = new Handler();
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.gps_viewer);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null)
		{
			TextView latValue = (TextView) findViewById(R.id.latitude_value);
			latValue.setText(extras.getString("sate2012.avatar.android.LAT"));
			TextView lngValue = (TextView) findViewById(R.id.longitude_value);
			lngValue.setText(extras.getString("sate2012.avatar.android.LNG"));
		}
		
		// Set the exit button listener
		ImageButton exitButton = (ImageButton) findViewById(R.id.exit);
		exitButton.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View view)
			{
				handler.post(new Runnable()
				{
					public void run()
					{
						Intent intent = new Intent();
						setResult(RESULT_OK, intent);
						finish();
					}
				});
			}
		});
	}
}

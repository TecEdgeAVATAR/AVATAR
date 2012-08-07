package sate2012.avatar.android;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import gupta.ashutosh.avatar.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.URLUtil;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

// Documentation and formatting for this file is lacking due to time constraints
public class ImageViewer extends Activity
{
	private static final String TAG = "VideoViewer";
	
	private Handler handler = new Handler();
	private ProgressDialog progressDialog;
	private String url = "error";
	private ImageView imageView;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.image_viewer);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null)
		{
			url = extras.getString("sate2012.avatar.android.URL");
			TextView latValue = (TextView) findViewById(R.id.latitude_value);
			latValue.setText(extras.getString("sate2012.avatar.android.LAT"));
			TextView lngValue = (TextView) findViewById(R.id.longitude_value);
			lngValue.setText(extras.getString("sate2012.avatar.android.LNG"));
		}
		
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Loading...");
		
		imageView = (ImageView) findViewById(R.id.image_view);
		
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
						// exit the dialog
						Intent intent = new Intent();
						setResult(RESULT_OK, intent);
						finish();
					}
				});
			}
		});
		
		// Set the image
		progressDialog.show();
		NetworkOperation networkTask = new NetworkOperation();
		networkTask.execute();
	}
	
	private class NetworkOperation extends AsyncTask<Void, Void, Bitmap>
	{
		@Override
		protected Bitmap doInBackground(Void... params)
		{
			if (!URLUtil.isNetworkUrl(url))
			{
				return null;
			}
			else
			{
				Bitmap bitmap = null;
				
				try
				{
					bitmap = BitmapFactory.decodeStream((InputStream)new URL(url).getContent());
				}
				catch (MalformedURLException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				return bitmap;
			}
		}
		
		/* (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object) */
		@Override
		protected void onPostExecute(Bitmap bitmap)
		{
			// Things to be done after the execution of the long running
			// operation
			progressDialog.dismiss();
			imageView.setImageBitmap(bitmap);
		}
		
		/* (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPreExecute() */
		@Override
		protected void onPreExecute()
		{
			// Things to be done before execution of long running operation.
		}
		
		/* (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onProgressUpdate(Progress[]) */
		@Override
		protected void onProgressUpdate(Void... values)
		{
			// Things to be done while execution of long running operation is in
			// progress. For example updating ProgessDialog
		}
	}
}

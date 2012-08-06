package sate2012.avatar.android;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;

import gupta.ashutosh.avatar.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.URLUtil;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

public class VideoViewer extends Activity
{
	private static final String TAG = "VideoViewer";
	
	private Handler handler = new Handler();
	private ProgressDialog progressDialog;
	private String url = "error";
	private VideoView videoView;
	private String current;
	private String videoPath;
	private SeekBar progressBar;
	private ImageButton playPauseButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.video_viewer);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null)
		{
			url = extras.getString("sate2012.avatar.android.URL");
		}
		
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Loading...");
		
		videoView = (VideoView) findViewById(R.id.surface_view);
		
		// This is bad, but it can be fixed with an Asynctask later.
		// I'm doing it this way to save some time.
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		// Put the video on top
		videoView.setZOrderOnTop(true);
		
		handler.removeCallbacks(updateProgressTask);
		
		// Set the play/pause button listener
		playPauseButton = (ImageButton) findViewById(R.id.play_pause);
		playPauseButton.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View view)
			{
				handler.post(new Runnable()
				{
					public void run()
					{
						// Pause the video
						if ((videoView != null) && (videoView.isPlaying()))
						{
							videoView.pause();
							handler.removeCallbacks(updateProgressTask);
							playPauseButton.setImageResource(R.drawable.green_play);
						}
						// Play the video
						else
						{
							playVideo();
							handler.postDelayed(updateProgressTask, 200);
							playPauseButton.setImageResource(R.drawable.blue_pause);
						}
					}
				});
			}
		});
		
		// Set the progress bar
		progressBar = (SeekBar) findViewById(R.id.progress_bar);
		progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
		{
			public void onStopTrackingTouch(SeekBar sizeBar)
			{
				videoView.seekTo(sizeBar.getProgress());
			}
			
			public void onStartTrackingTouch(SeekBar sizeBar)
			{
				// Placeholder
			}
			
			public void onProgressChanged(SeekBar sizeBar, int progress, boolean fromUser)
			{
				// Placeholder
			}
		});
		
		// Set the volume bar
		SeekBar volumeBar = (SeekBar) findViewById(R.id.volume_bar);
		final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
		{
			public void onStopTrackingTouch(SeekBar sizeBar)
			{
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, sizeBar.getProgress(), 0);
			}
			
			public void onStartTrackingTouch(SeekBar sizeBar)
			{
				// Placeholder
			}
			
			public void onProgressChanged(SeekBar sizeBar, int progress, boolean fromUser)
			{
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
			}
		});
		
		// Set the events to occur when the video ends
		videoView.setOnCompletionListener(new OnCompletionListener()
		{
			public void onCompletion(MediaPlayer arg0)
			{
				handler.post(new Runnable()
				{
					public void run()
					{
						playPauseButton.setImageResource(R.drawable.green_play);
						progressBar.setProgress(progressBar.getMax());
						handler.removeCallbacks(updateProgressTask);
					}
				});
			}
		});
		
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
						if (videoView != null)
						{
							current = null;
							videoView.stopPlayback();
						}
						
						Intent intent = new Intent();
						setResult(RESULT_OK, intent);
						finish();
					}
				});
			}
		});
	}
	
	private void playVideo()
	{
		try
		{
			System.out.println("path --> " + url);
			Log.v(TAG, "path: " + url);
			if (url == null || url.length() == 0)
			{
				Toast.makeText(VideoViewer.this, "Error: File URL/path is empty.", Toast.LENGTH_LONG).show();
			}
			else
			{
				// If the path has not changed, just start the media player
				if (url.equals(current) && videoView != null)
				{
					videoView.start();
					videoView.requestFocus();
					return;
				}
				progressDialog.show();
				current = url;
				System.out.println("Current path --> " + url);
				NetworkOperation networkTask = new NetworkOperation();
				networkTask.execute(url);
			}
		}
		catch (Exception e)
		{
			Log.e(TAG, "error: " + e.getMessage(), e);
			if (videoView != null)
			{
				videoView.stopPlayback();
			}
		}
	}
	
	public String getVideoPath()
	{
		return videoPath;
	}
	
	public void setVideoPath(String videoPath)
	{
		this.videoPath = videoPath;
	}
	
	private class NetworkOperation extends AsyncTask<String, Void, String>
	{
		@Override
		protected String doInBackground(String... params)
		{
			String tempPath = null;
			if (!URLUtil.isNetworkUrl(params[0]))
			{
				return params[0];
			}
			else
			{
				try
				{
					URL url = new URL(params[0]);
					URLConnection cn = url.openConnection();
					cn.connect();
					InputStream stream = cn.getInputStream();
					if (stream == null)
						throw new RuntimeException("stream is null");
					File temp = File.createTempFile("mediaplayertmp", "mp4");
					temp.deleteOnExit();
					tempPath = temp.getAbsolutePath();
					
					@SuppressWarnings("resource")
					FileOutputStream out = new FileOutputStream(temp);
					byte buf[] = new byte[128];
					do
					{
						int numread = stream.read(buf);
						if (numread <= 0)
							break;
						out.write(buf, 0, numread);
					}
					while (true);
					try
					{
						stream.close();
					}
					catch (IOException ex)
					{
						Log.e(TAG, "error: " + ex.getMessage(), ex);
					}
				}
				catch (Exception e)
				{
					Log.e(TAG, "error: " + e.getMessage(), e);
				}
				setVideoPath(tempPath);
				return tempPath;
			}
		}
		
		/* (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object) */
		@Override
		protected void onPostExecute(String result)
		{
			// Things to be done after the execution of the long running
			// operation
			progressDialog.dismiss();
			System.out.println("1");
			videoView.setVideoPath(videoPath);
			System.out.println("2");
			videoView.start();
			videoView.requestFocus();
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
	
	private Runnable updateProgressTask = new Runnable()
	{
		public void run()
		{
			// Update the progress bar based on the video status
			progressBar.setProgress(videoView.getCurrentPosition());
			progressBar.setMax(videoView.getDuration());
			
			handler.postAtTime(this, SystemClock.uptimeMillis() + 200);
		}
	};
}

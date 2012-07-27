package sate2012.avatar.android;

import gupta.ashutosh.avatar.R;
import java.io.File;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class VideoCamActivity extends Activity implements
		SurfaceHolder.Callback {
	public double ptLat = 39.780937;
	public double ptLng = -84.117682;
	private MediaRecorder recorder;
	public Camera mCameraDevice;
	private VideoView videoView = null;
	private ImageButton startBtn = null;
	private ImageButton playRecordingBtn = null;
	private ImageButton returnToSubmission;
	private String OUTPUT_FILE = "video_" + System.currentTimeMillis() + ".mp4";
	public static final String VIDEO = "VIDEO";
	private static File videoRecording;
	private Boolean playing = false;
	private Boolean recording = false;
	private TextView mVideoClockUI;
	private Handler mHandler;
	private int mVideoClockTime;
	private Runnable mClockTask;
	private SurfaceHolder mHolder;
	Context conn;
	LocationManager mlocManager;
	LocationListener mlocListener;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		conn = super.getApplicationContext();
		/* Use the LocationManager class to obtain GPS locations */
		mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		mlocListener = new MyLocationListener();
		mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
				mlocListener);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.video);
		mVideoClockTime = 0;
		mHandler = new Handler();
		mClockTask = new Runnable() {
			public void run() {
				mVideoClockTime++;
				int minutes = mVideoClockTime / 60;
				mVideoClockTime = mVideoClockTime % 60;
				mVideoClockUI.setText(String.format("%02d:%02d", minutes,
						mVideoClockTime));
				mHandler.postDelayed(this, 1000); // Every second.
			}
		};
		mVideoClockUI = (TextView) findViewById(R.id.video_clock_ui);
		startBtn = (ImageButton) findViewById(R.id.bgnBtn);
		playRecordingBtn = (ImageButton) findViewById(R.id.playRecordingBtn);
		playRecordingBtn.setEnabled(false);
		videoView = (VideoView) this.findViewById(R.id.videoView);
		returnToSubmission = (ImageButton) findViewById(R.id.returnToForm);
		final SurfaceHolder holder = videoView.getHolder();
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		startBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				if (!VideoCamActivity.this.recording
						& !VideoCamActivity.this.playing) {
					try {
						beginRecording(holder);

						playing = false;
						recording = true;
						startBtn.setImageResource(R.drawable.stop_recording_video);
						mVideoClockUI.setVisibility(View.VISIBLE);
						mVideoClockUI.setText("00:00");
						mHandler.postDelayed(mClockTask, 1000);
					} catch (Exception e) {
						Log.e("ERROR", "Exception caught recording video.", e);
					}
				} else if (VideoCamActivity.this.recording) {
					try {
						stopRecording();
						playing = false;
						recording = false;
						startBtn.setImageResource(R.drawable.record_video);
						playRecordingBtn.setEnabled(true);
						playRecordingBtn
								.setImageResource(R.drawable.green_play_button);
						startBtn.setEnabled(false); // Don't allow any more
													// recording in this
													// session.
						startBtn.setImageResource(R.drawable.stop_recording_video_grey);
						mHandler.removeCallbacks(mClockTask);
						mVideoClockUI.setVisibility(View.INVISIBLE);
					} catch (Exception e) {
						Log.e("ERROR", "Exception caught stopping recording.",
								e);
					}
				}
			}
		});

		playRecordingBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				if (!VideoCamActivity.this.playing
						& !VideoCamActivity.this.recording) {
					try {
						playRecording();
						VideoCamActivity.this.playing = true;
						VideoCamActivity.this.recording = false;
						playRecordingBtn.setEnabled(false);
						playRecordingBtn
								.setImageResource(R.drawable.blue_play_button);
					} catch (Exception e) {
						Log.e("ERROR", "Exception caught playing video.", e);
					}
				} else if (VideoCamActivity.this.playing) {
					try {
						stopPlayingRecording();
						VideoCamActivity.this.playing = false;
						VideoCamActivity.this.recording = false;
					} catch (Exception e) {
						Log.e("ERROR", "Exception caught stopping play.", e);
					}
				}
			}
		});

		returnToSubmission.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent data = new Intent();
				data.putExtra(VIDEO, getVideoRecording());
				if (getVideoRecording() != null) {
					setResult(Activity.RESULT_OK, data);
				} else {
					setResult(Activity.RESULT_CANCELED);
				}
				finish();
			}
		});
	}

	public static String getPath() {
		return videoRecording.getAbsolutePath();
	}

	public void surfaceCreated(SurfaceHolder holder) {
		startBtn.setEnabled(true);
		mCameraDevice = Camera.open();
		try {
			mCameraDevice.setPreviewDisplay(holder);
		} catch (Exception e) {
			Log.e("INFO", "Error in setPreviewDisplay:  ", e);
		}
		try {
			mCameraDevice.startPreview();
		} catch (Exception e) {
			Log.e("INFO", "Error in startPreview:  ", e);
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		try {
			Log.i("INFO", "Width x Height = " + width + "x" + height);
		} catch (Exception e) {
			Log.e("INFO", "Error in surfaceChanged:  ", e);
		}
	}

	private void playRecording() {
		MediaController mc = new MediaController(this);
		videoView.setMediaController(mc);
		videoView.setVideoPath(videoRecording.getAbsolutePath());
		videoView.start();
	}

	private void stopPlayingRecording() {
		videoView.stopPlayback();
	}

	private void stopRecording() throws Exception {
		if (recorder != null) {
			recorder.stop();
			mCameraDevice.release();
			setVideoRecording(videoRecording);

		}
	}

	protected void onDestroy() {
		super.onDestroy();
		if (recorder != null)
			recorder.release();
		mHandler.removeCallbacks(mClockTask);
	}

	private void beginRecording(SurfaceHolder holder) throws Exception {
		mCameraDevice.stopPreview();
		mCameraDevice.unlock();
		videoRecording = new File(Environment.getExternalStorageDirectory(),
				BlueprintConstants.STORAGE_DIRECTORY
						+ BlueprintConstants.MEDIA_DIRECTORY + OUTPUT_FILE);
		if (videoRecording.exists())
			videoRecording.delete();
		try {
			recorder = new MediaRecorder();
			recorder.reset();
			recorder.setCamera(mCameraDevice);
			recorder.setPreviewDisplay(holder.getSurface());
			recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
			recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
			recorder.setAudioEncoder(3); // Eclipse does not recognize
											// MediaRecorder.AudioEncoder.AAC,
											// but using its value (3) does
											// work.
			recorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
			recorder.setVideoSize(720, 432);
			recorder.setOutputFile(videoRecording.getAbsolutePath());
			recorder.setPreviewDisplay(holder.getSurface());
			recorder.prepare();
			recorder.start();
		} catch (Exception e) {
			Log.e("ERROR",
					"Exception caught creating media recorder."
							+ e.getStackTrace(), e);
		}
	}

	/**
	 * @return the videoRecording
	 */
	public File getVideoRecording() {
		return this.videoRecording;
	}

	/**
	 * @param videoRecording
	 *            the videoRecording to set
	 */
	public void setVideoRecording(File videoRecording) {
		this.videoRecording = videoRecording;
	}

	/**
	 * Class My Location Listener
	 */
	public class MyLocationListener implements LocationListener {
		public void onLocationChanged(Location loc) {
			ptLat = loc.getLatitude();
			ptLng = loc.getLongitude();
			String LatLong = loc.getLatitude() + " --- " + loc.getLongitude();
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
	}

	@Override
	protected void onStop() {
		super.onStop();
		mlocManager.removeUpdates(mlocListener);
		finish();
	}
}

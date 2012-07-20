package sate2012.avatar.android;

import java.io.File;
import java.io.IOException;

import sate2012.avatar.android.GPSActivity.MyLocationListener;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

public class RecordSoundActivity extends Activity {

	public double ptLat = 39.780937;
	public double ptLng = -84.117682;
	private MediaRecorder recorder;
	private MediaPlayer player;

	private ImageButton startRecording = null;
	private ImageButton playRecording = null;
	private ImageButton returnToSubmission;

	private String OUTPUT_FILE = "recording_" + System.currentTimeMillis()
			+ ".mp4";
	public static final String VOICE = "VOICE";

	private static File voiceRecording;

	private boolean media = false;
	private boolean playing = false;
	private boolean recording = false;

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

		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.voice);
		this.recorder = new MediaRecorder();

		startRecording = (ImageButton) findViewById(R.id.bgnBtn);
		playRecording = (ImageButton) findViewById(R.id.playRecordingBtn);
		returnToSubmission = (ImageButton) findViewById(R.id.returnToForm);

		startRecording.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startRecording();
			}
		});

		playRecording.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if (!RecordSoundActivity.this.media) {
					Toast.makeText(RecordSoundActivity.this,
							"You haven't recorded any audio to play yet.",
							Toast.LENGTH_LONG).show();
				} else {
					player = new MediaPlayer();
					try {
						player.setDataSource(voiceRecording.getAbsolutePath());
						player.prepare();
						player.start();
					} catch (IOException e) {
						Log.e("ERROR", "error playing recording", e);
					}
				}
			}
		});

		returnToSubmission.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent data = new Intent();
				data.putExtra(VOICE, getVoiceRecording());
				if (getVoiceRecording() != null) {
					setResult(Activity.RESULT_OK, data);
				} else {
					setResult(Activity.RESULT_CANCELED);
				}
				finish();
			}
		});
	}

	public static String getPath() {
		return voiceRecording.getAbsolutePath();
	}

	protected void addToDB() {
		ContentValues values = new ContentValues(3);
		long current = System.currentTimeMillis();

		values.put(MediaColumns.TITLE, "observation_audio");
		values.put(MediaColumns.DATE_ADDED, (int) (current / 1000));
		values.put(MediaColumns.MIME_TYPE, "audio/mp4");
		values.put(MediaColumns.DATA, OUTPUT_FILE);
		ContentResolver contentResolver = getContentResolver();

		Uri base = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		Uri newUri = contentResolver.insert(base, values);

		sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));
	}

	protected void startRecording() {
		voiceRecording = new File(Environment.getExternalStorageDirectory(),
				BlueprintConstants.STORAGE_DIRECTORY
						+ BlueprintConstants.MEDIA_DIRECTORY + OUTPUT_FILE);

		if (!RecordSoundActivity.this.recording
				&& !RecordSoundActivity.this.playing) {
			playing = false;
			recording = true;
			startRecording.setImageResource(R.drawable.stop_recording_video);
			this.recorder = new MediaRecorder();
			this.recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			this.recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
			this.recorder.setAudioEncoder(3);	// Eclipse does not recognize MediaRecorder.AudioEncoder.AAC, but using its value (3) does work.
			this.recorder.setOutputFile(voiceRecording.getAbsolutePath());
			try {
				this.recorder.prepare();
			} catch (IllegalStateException e1) {
				Log.e("ERROR", "Exception caught preparing voice recording.",
						e1);
				e1.printStackTrace();
			} catch (IOException e1) {
				Log.e("ERROR", "Exception caught preparing voice recording.",
						e1);
				e1.printStackTrace();
			}
			this.recorder.start();
		} else if (RecordSoundActivity.this.recording) {
			stopRecording();
			playing = false;
			recording = false;
			addToDB();
		}
	}

	protected void stopRecording() {
		this.recorder.stop();
		this.recorder.release();
		setVoiceRecording(voiceRecording);
		startRecording.setImageResource(R.drawable.record_video);
		playRecording.setEnabled(true);
		playRecording.setImageResource(R.drawable.play_video);
		startRecording.setEnabled(false);
		startRecording.setImageResource(R.drawable.record_video_greyscale);
		this.media = true;
	}

	/**
	 * @return the voiceRecording
	 */
	public File getVoiceRecording() {
		return this.voiceRecording;
	}

	/**
	 * @param voiceRecording
	 *            the voiceRecording to set
	 */
	public void setVoiceRecording(File voiceRecording) {
		this.voiceRecording = voiceRecording;
	}

	/* Class My Location Listener */
	public class MyLocationListener implements LocationListener {
		public void onLocationChanged(Location loc) {

			ptLat = loc.getLatitude();
			ptLng = loc.getLongitude();
			String LatLong = loc.getLatitude() + " --- " + loc.getLongitude();
			// String LatLong = loc.getLatitude() + " --- " + loc.getLongitude()
			// + ";~~40.12345 --- -85.12345;~~41.54321 --- -83.54321";
			// Toast.makeText( getApplicationContext(), LatLong,
			// Toast.LENGTH_SHORT).show();
			// httpPost(LatLong);

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

	}/* End of Class MyLocationListener */

	@Override
	protected void onStop() {
		super.onStop();
		mlocManager.removeUpdates(mlocListener);
		finish();

	}

}

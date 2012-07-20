package sate2012.avatar.android;

import java.io.File;
import java.util.List;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class BlueprintActivity extends Activity {

	// Storage
	private File sd;
	private File storageFolder;
	private File mediaFolder;
	public String dataType;
	public String media_filepath;
	public String media_filename;
	public String media_extension;
	private Intent cameraIntent;
	private CameraAct cameraInst;
	private static String image_filepath;

	public String URL_photoUpload = "http://www.wbi-icc.com/students/SL/SmartPhone/infoinput.html";
	BasicNameValuePair uploadNVP = new BasicNameValuePair("image",
			"/mnt/sdcard/DCIM/Camera/20120705_111336.png");
	List<NameValuePair> uploadParams = new ArrayList<NameValuePair>(3);
	public static Context thisContext;

	Spinner spinner1;

	// EditText editText1;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		thisContext = getApplicationContext();
		Log.i("INFO", "created blueprint activity");
		setContentView(R.layout.upload_menu);
		Log.i("INFO", "set content view");
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		createStorageDirectory();
		Log.i("INFO", "created storage directory");
		//UploadData dataUploader = new UploadData();

		// Toast.makeText(getApplicationContext(),
		// "uploadNVP: '"+uploadNVP+"', uploadParams: '"+uploadParams+"'",
		// Toast.LENGTH_LONG).show();
		Button pictureB = (Button) findViewById(R.id.cameraButton);
		pictureB.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				//media_filepath = "/mnt/sdcard/DCIM/Camera/20120705_111336.jpg";
				//media_extension = "_P.jpg";
				//media_filename = UploadFTP.FTPUpload(media_filepath, media_extension, thisContext);
				
				//dataType = "Photo";
				//Intent MailIntent = new Intent(getApplicationContext(), MailSenderActivity.class);
				//MailIntent.putExtra("Type", dataType);
				//MailIntent.putExtra("Filename", media_filename);
				//startActivity(MailIntent);

				
				dataType = getResources().getString(R.string.type_picture);
				cameraIntent = new Intent(BlueprintActivity.this, CameraAct.class);
				//long time = (System.currentTimeMillis());
				//cameraIntent.putExtra(0,  "/mnt/sdcard/DCIM/Camera/"+time+".png");
				startActivityForResult(cameraIntent, BlueprintConstants.CAMERA_REQUEST);
			}
		});

		Button videoB = (Button) findViewById(R.id.videoButton);
		videoB.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				dataType = getResources().getString(R.string.type_video);
				Intent intent = new Intent(BlueprintActivity.this,
						VideoCamActivity.class);
				startActivityForResult(intent, BlueprintConstants.VIDEO_REQUEST);
			}
		});

		Button audioB = (Button) findViewById(R.id.audioButton);
		audioB.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				dataType = getResources().getString(R.string.type_audio);
				Intent intent = new Intent(BlueprintActivity.this,
						RecordSoundActivity.class);
				startActivityForResult(intent, BlueprintConstants.VOICE_REQUEST);
			}
		});

		Button commentB = (Button) findViewById(R.id.commentButton);
		commentB.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				dataType = getResources().getString(R.string.type_comment);
				Toast.makeText(getApplicationContext(), dataType,
						Toast.LENGTH_LONG).show();
				Intent MailIntent = new Intent(getApplicationContext(),
						MailSenderActivity.class);
				MailIntent.putExtra("Type", dataType);
				startActivity(MailIntent);
			}
		});

		Button gpsB = (Button) findViewById(R.id.gpsButton);
		gpsB.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				dataType = getResources().getString(R.string.type_android);
				Toast.makeText(getApplicationContext(), dataType,
						Toast.LENGTH_LONG).show();
				Intent MailIntent = new Intent(getApplicationContext(),
						MailSenderActivity.class);
				MailIntent.putExtra("Type", dataType);
				startActivity(MailIntent);
				/*
				 * Intent MailIntent = new Intent(getApplicationContext(),
				 * MailSenderActivity.class);
				 * //MailIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				 * startActivity(MailIntent); Intent GPSintent = new
				 * Intent(BlueprintActivity.this, GPSActivity.class);
				 * startActivity(GPSintent);
				 */
			}
		});



	}

	/** Called when the individual activities (picture, video, audio) finish. */

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.i("INFO", "OnActivityResult: result = "+resultCode);
		
		/*if ((resultCode == Activity.RESULT_OK)
				&& (requestCode == BlueprintConstants.CAMERA_REQUEST)) {
			// TODO: do something with picture
			Toast.makeText(getApplicationContext(), "BlueprintActivity - OK",
					Toast.LENGTH_LONG).show();
		}*/

		if ((resultCode == Activity.RESULT_OK)
				&& (requestCode == BlueprintConstants.VIDEO_REQUEST)) {
			// TODO: do something with video
		}
		if ((resultCode == Activity.RESULT_OK)
				&& (requestCode == BlueprintConstants.VOICE_REQUEST)) {
			// TODO: do something with audio
		}

		/*
		 * if (requestCode == BlueprintConstants.CAMERA_REQUEST) { //TODO: do
		 * something with picture Toast.makeText(getApplicationContext(),
		 * "BlueprintActivity - "+dataType, Toast.LENGTH_LONG).show();
		 * GMailSender sender = new GMailSender( "sate2012.avatar@gmail.com" ,
		 * "EmbraceChaos" ) ;
		 * 
		 * Intent MailIntent = new Intent(getApplicationContext(),
		 * MailSenderActivity.class);
		 * //MailIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		 * startActivity(MailIntent);
		 * 
		 * //mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER,
		 * (long)2000, (float)0.1, mlocListener);
		 * //Toast.makeText(getApplicationContext(),
		 * "Starting Activity for Result", Toast.LENGTH_LONG).show();
		 * //startActivityForResult(email_sender, 32);
		 * 
		 * }
		 */
		if (requestCode == BlueprintConstants.VIDEO_REQUEST
				|| requestCode == BlueprintConstants.CAMERA_REQUEST
				|| requestCode == BlueprintConstants.VOICE_REQUEST) {
			// TODO: do something with picture
			Toast.makeText(getApplicationContext(), dataType, Toast.LENGTH_LONG).show();

			if (requestCode == BlueprintConstants.VIDEO_REQUEST) {
				media_filepath = VideoCamActivity.getPath();
				media_extension = "_V.f4v";
			}
			if (requestCode == BlueprintConstants.VOICE_REQUEST) {
				media_filepath = RecordSoundActivity.getPath();
				media_extension = "_A.mp4";
			}

			if (requestCode == BlueprintConstants.CAMERA_REQUEST) {
				Log.i("INFO", "onActivityResult for Camera");
				media_filepath = getImage_filepath();
				Log.i("INFO", "blueprint: picture filepath: "+media_filepath);
				media_extension = "_P.png";
				
				
			}
			
			media_filename = UploadFTP.FTPUpload(media_filepath, media_extension, thisContext);
			// Toast.makeText(getApplicationContext(),
			// "FILENAME: "+media_filename, Toast.LENGTH_LONG).show();

			Intent MailIntent = new Intent(getApplicationContext(),
					MailSenderActivity.class);
			MailIntent.putExtra("Type", dataType);
			MailIntent.putExtra("Filename", media_filename);
			startActivity(MailIntent);

		}

	}

	public static void setImage_filepath(String fp) {
		image_filepath = fp;
	}
	public String getImage_filepath() {
		return image_filepath;
	}
	
	
	@Override
	public void onDestroy() {
		finish();
		super.onDestroy();
	}

	public void createStorageDirectory() {

		sd = Environment.getExternalStorageDirectory();
		storageFolder = new File(sd, BlueprintConstants.STORAGE_DIRECTORY);

		if (sd.canWrite()) {
			if (!storageFolder.exists()) {
				storageFolder.mkdir();
			}

			mediaFolder = new File(sd, BlueprintConstants.STORAGE_DIRECTORY
					+ BlueprintConstants.MEDIA_DIRECTORY);
			if (!mediaFolder.exists()) {
				mediaFolder.mkdir();
			}
		}
	}
}

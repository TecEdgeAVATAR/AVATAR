package sate2012.avatar.android;

import gupta.ashutosh.avatar.R;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class BlueprintActivity extends Activity implements OnClickListener {
	// Storage
	private File sd;
	private File storageFolder;
	private File mediaFolder;
	public String dataType;
	public String media_filepath;
	public String media_filename;
	public String media_extension;
	private Intent cameraIntent;
	private static String image_filepath;
	public String URL_photoUpload = "http://www.wbi-icc.com/students/SL/SmartPhone/infoinput.html";
	BasicNameValuePair uploadNVP = new BasicNameValuePair("image",
			"/mnt/sdcard/DCIM/Camera/20120705_111336.png");
	List<NameValuePair> uploadParams = new ArrayList<NameValuePair>(3);
	public static Context thisContext;

	/** 
	 * Called when the activity is first created. 
	 */
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
		Button pictureB = (Button) findViewById(R.id.cameraButton);
		pictureB.setOnClickListener(this);
		Button videoB = (Button) findViewById(R.id.videoButton);
		videoB.setOnClickListener(this);
		Button audioB = (Button) findViewById(R.id.audioButton);
		audioB.setOnClickListener(this);
		Button commentB = (Button) findViewById(R.id.commentButton);
		commentB.setOnClickListener(this);
		Button gpsB = (Button) findViewById(R.id.gpsButton);
		gpsB.setOnClickListener(this);
	}

	/**
	 * Responds to whatever button is pressed
	 * 
	 * @param View
	 *            v - the button clicked
	 */
	public void onClick(View v) {
		switch (v.getId()) {
		case (R.id.cameraButton):
			dataType = getResources().getString(R.string.type_picture);
			cameraIntent = new Intent(BlueprintActivity.this, CameraAct.class);
			startActivityForResult(cameraIntent,
					BlueprintConstants.CAMERA_REQUEST);
			break;
		case (R.id.videoButton):
			dataType = getResources().getString(R.string.type_video);
			Intent intent = new Intent(BlueprintActivity.this,
					VideoCamActivity.class);
			startActivityForResult(intent, BlueprintConstants.VIDEO_REQUEST);
			break;
		case (R.id.audioButton):
			dataType = getResources().getString(R.string.type_audio);
			Intent intent1 = new Intent(BlueprintActivity.this,
					RecordSoundActivity.class);
			startActivityForResult(intent1, BlueprintConstants.VOICE_REQUEST);
			break;
		case (R.id.commentButton):
			dataType = getResources().getString(R.string.type_comment);
			Toast.makeText(getApplicationContext(), dataType, Toast.LENGTH_LONG)
					.show();
			Intent MailIntent = new Intent(getApplicationContext(),
					MailSenderActivity.class);
			MailIntent.putExtra("Type", dataType);
			startActivity(MailIntent);
			break;
		case (R.id.gpsButton):
			dataType = getResources().getString(R.string.type_android);
			Toast.makeText(getApplicationContext(), dataType, Toast.LENGTH_LONG)
					.show();
			Intent MailIntent1 = new Intent(getApplicationContext(),
					MailSenderActivity.class);
			MailIntent1.putExtra("Type", dataType);
			startActivity(MailIntent1);
			break;
		}
	}

	/**
	 * Called when the individual activities (picture, video, audio) finish.
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.i("INFO", "OnActivityResult: result = " + resultCode);
		if ((resultCode == Activity.RESULT_OK)
				&& (requestCode == BlueprintConstants.VIDEO_REQUEST))
			// TODO: do something with video
		if ((resultCode == Activity.RESULT_OK)
				&& (requestCode == BlueprintConstants.VOICE_REQUEST))
			// TODO: do something with audio
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
				Log.i("INFO", "blueprint: picture filepath: " + media_filepath);
				media_extension = "_P.png";
			}
			media_filename = UploadFTP.FTPUpload(media_filepath,
					media_extension, thisContext);
			Intent MailIntent = new Intent(getApplicationContext(), MailSenderActivity.class);
			MailIntent.putExtra("Type", dataType);
			MailIntent.putExtra("Filename", media_filename);
			startActivity(MailIntent);
		}
	}

	public static void setImage_filepath(String fp) { image_filepath = fp; }

	public String getImage_filepath() {	return image_filepath; }

	@Override
	public void onDestroy() { 
		finish();
		super.onDestroy();
	}

	public void createStorageDirectory() {
		sd = Environment.getExternalStorageDirectory();
		storageFolder = new File(sd, BlueprintConstants.STORAGE_DIRECTORY);
		if (sd.canWrite()) {
			if (!storageFolder.exists()) 
				storageFolder.mkdir();
			mediaFolder = new File(sd, BlueprintConstants.STORAGE_DIRECTORY + BlueprintConstants.MEDIA_DIRECTORY);
			if (!mediaFolder.exists())
				mediaFolder.mkdir();
		}
	}
}

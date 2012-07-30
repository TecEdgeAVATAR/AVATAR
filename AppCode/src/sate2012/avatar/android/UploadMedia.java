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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

/**
 * The Upload Menu Allows the user to select different media types to upload to
 * the server
 */
public class UploadMedia extends Activity implements OnClickListener {
	private File sd;
	private File storageFolder;
	private File mediaFolder;
	private ImageButton pictureB;
	private ImageButton videoB;
	private ImageButton audioB;
	private ImageButton commentB;
	private Button gpsB;
	private String dataType;
	private String media_filepath;
	private String media_filename;
	private String media_extension;
	private static String image_filepath;
	private String URL_photoUpload = "http://www.wbi-icc.com/students/SL/SmartPhone/infoinput.html";
	private BasicNameValuePair uploadNVP = new BasicNameValuePair("image", "/mnt/sdcard/DCIM/Camera/20120705_111336.png");
	private List<NameValuePair> uploadParams = new ArrayList<NameValuePair>(3);
	public static Context thisContext;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		thisContext = getApplicationContext();
		setContentView(R.layout.upload_menu);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		createStorageDirectory();
		pictureB = (ImageButton) findViewById(R.id.cameraButton);
		pictureB.setOnClickListener(this);
		videoB = (ImageButton) findViewById(R.id.videoButton);
		videoB.setOnClickListener(this);
		audioB = (ImageButton) findViewById(R.id.audioButton);
		audioB.setOnClickListener(this);
		commentB = (ImageButton) findViewById(R.id.commentButton);
		commentB.setOnClickListener(this);
		gpsB = (Button) findViewById(R.id.gpsButton);
		gpsB.setOnClickListener(this);
	}

	/**
	 * Responds to whatever button is pressed
	 * 
	 * @param View
	 *            v - the button clicked
	 */
	public void onClick(View v) {
		Intent i;
		switch (v.getId()) {
			case (R.id.cameraButton):
				dataType = getResources().getString(R.string.type_picture);
				i = new Intent(UploadMedia.this, Photographer.class);
				startActivityForResult(i, Constants.CAMERA_REQUEST);
				break;
			case (R.id.videoButton):
				dataType = getResources().getString(R.string.type_video);
				i = new Intent(UploadMedia.this, VideoRecorder.class);
				startActivityForResult(i, Constants.VIDEO_REQUEST);
				break;
			case (R.id.audioButton):
				dataType = getResources().getString(R.string.type_audio);
				i = new Intent(UploadMedia.this, VoiceNotes.class);
				startActivityForResult(i, Constants.VOICE_REQUEST);
				break;
			case (R.id.commentButton):
				dataType = getResources().getString(R.string.type_comment);
				Toast.makeText(getApplicationContext(), dataType, Toast.LENGTH_LONG).show();
				i = new Intent(getApplicationContext(), MailSenderActivity.class);
				i.putExtra("Type", dataType);
				startActivity(i);
				break;
			case (R.id.gpsButton):
				dataType = getResources().getString(R.string.type_android);
				Toast.makeText(getApplicationContext(), dataType, Toast.LENGTH_LONG).show();
				i = new Intent(getApplicationContext(), MailSenderActivity.class);
				i.putExtra("Type", dataType);
				startActivity(i);
				break;
		}
	}

	/**
	 * Called when the individual activities (picture, video, audio) finish.
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == Constants.VIDEO_REQUEST){
				Toast.makeText(UploadMedia.this, "Video Data Stored", Toast.LENGTH_SHORT).show();
				media_filepath = VideoRecorder.getPath();
				media_extension = "_V.f4v";
			}
			if (requestCode == Constants.VOICE_REQUEST){
				Toast.makeText(UploadMedia.this, "Voice Data Stored", Toast.LENGTH_SHORT).show();
				media_filepath = VoiceNotes.getPath();
				media_extension = "_A.mp4";
			}
			if (requestCode == Constants.CAMERA_REQUEST){
				Toast.makeText(UploadMedia.this, "Photo Data Stored", Toast.LENGTH_SHORT).show();
				media_filepath = getImage_filepath();
				media_extension = "_P.png";
			}
		}
		Toast.makeText(UploadMedia.this, "Uploading...", Toast.LENGTH_SHORT).show();
		media_filename = UploadFTP.FTPUpload(media_filepath, media_extension, thisContext);
		Toast.makeText(UploadMedia.this, "Uploaded Data to VDC!", Toast.LENGTH_SHORT).show();
		Intent MailIntent = new Intent(getApplicationContext(), MailSenderActivity.class);
		MailIntent.putExtra("Type", dataType);
		MailIntent.putExtra("Filename", media_filename);
		startActivity(MailIntent);
	}

	public static void setImage_filepath(String fp) { image_filepath = fp; }

	public String getImage_filepath() { return image_filepath; }

	@Override
	public void onDestroy() {
		finish();
		super.onDestroy();
	}

	public void createStorageDirectory() {
		sd = Environment.getExternalStorageDirectory();
		storageFolder = new File(sd, Constants.STORAGE_DIRECTORY);
		if (sd.canWrite()) {
			if (!storageFolder.exists())
				storageFolder.mkdir();
			mediaFolder = new File(sd, Constants.STORAGE_DIRECTORY + Constants.MEDIA_DIRECTORY);
			if (!mediaFolder.exists())
				mediaFolder.mkdir();
		}
	}
}
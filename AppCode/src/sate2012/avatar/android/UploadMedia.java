package sate2012.avatar.android;

import gupta.ashutosh.avatar.R;
import java.io.File;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * The Upload Menu Allows the user to select different media types to upload to
 * the server
 */
public class UploadMedia extends Activity implements OnClickListener
{
	private File sd;
	private File storageFolder;
	private File mediaFolder;
	private ImageButton pictureB;
	private ImageButton videoB;
	private ImageButton audioB;
	private ImageButton commentB;
	private ImageButton gpsB;
	private ImageButton exitB;
	private String dataType;
	private String media_filepath;
	private String media_filename;
	private String media_extension;
	private static String image_filepath;
	public static Context thisContext;
	
	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		thisContext = getApplicationContext();
		setContentView(R.layout.upload_menu);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		createStorageDirectory();
		pictureB = (ImageButton) findViewById(R.id.cameraButton);
		pictureB.setOnClickListener(this);
		videoB = (ImageButton) findViewById(R.id.videoButton);
		videoB.setOnClickListener(this);
		audioB = (ImageButton) findViewById(R.id.microphoneButton);
		audioB.setOnClickListener(this);
		commentB = (ImageButton) findViewById(R.id.commentButton);
		commentB.setOnClickListener(this);
		gpsB = (ImageButton) findViewById(R.id.gpsButton);
		gpsB.setOnClickListener(this);
		exitB = (ImageButton) findViewById(R.id.cancelButton);
		exitB.setOnClickListener(this);
		// This is bad, but it can be fixed with an Asynctask later.
		// I'm doing it this way to save some time.
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
	}
	
	/**
	 * Responds to whatever button is pressed
	 * 
	 * @param View
	 *            v - the button clicked
	 */
	public void onClick(View v)
	{
		Intent i;
		switch (v.getId())
		{
			case (R.id.cameraButton):
				dataType = getResources().getString(R.string.type_picture);
				i = new Intent(UploadMedia.this, Photographer.class);
				startActivityForResult(i, Globals.CAMERA_REQUEST);
				break;
			case (R.id.videoButton):
				dataType = getResources().getString(R.string.type_video);
				i = new Intent(UploadMedia.this, VideoRecorder.class);
				startActivityForResult(i, Globals.VIDEO_REQUEST);
				break;
			case (R.id.microphoneButton):
				dataType = getResources().getString(R.string.type_audio);
				i = new Intent(UploadMedia.this, VoiceNotes.class);
				startActivityForResult(i, Globals.VOICE_REQUEST);
				break;
			case (R.id.commentButton):
				dataType = getResources().getString(R.string.type_comment);
				i = new Intent(getApplicationContext(), MailSenderActivity.class);
				i.putExtra("Type", dataType);
				startActivityForResult(i, Globals.COMMENT_REQUEST);
				break;
			case (R.id.gpsButton):
				dataType = getResources().getString(R.string.type_android);
				i = new Intent(getApplicationContext(), MailSenderActivity.class);
				i.putExtra("Type", dataType);
				startActivityForResult(i, Globals.GPS_REQUEST);
				break;
			case (R.id.cancelButton):
				Intent intent = new Intent();
				setResult(RESULT_CANCELED, intent);
				// exit the dialog
				finish();
				break;
		}
	}
	
	public void onBackPressed()
	{
		Intent intent = new Intent();
		setResult(RESULT_CANCELED, intent);
		finish();
	}
	
	/**
	 * Called when the individual activities (picture, video, audio) finish.
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK)
		{
			if (requestCode == Globals.VIDEO_REQUEST)
			{
				media_filepath = VideoRecorder.getPath();
				media_extension = "_V.f4v";
			}
			if (requestCode == Globals.VOICE_REQUEST)
			{
				media_filepath = VoiceNotes.getPath();
				media_extension = "_A.mp4";
			}
			if (requestCode == Globals.CAMERA_REQUEST)
			{
				media_filepath = getImage_filepath();
				media_extension = "_P.png";
			}
			if (requestCode != Globals.COMMENT_REQUEST && requestCode != Globals.GPS_REQUEST)
			{
				media_filename = UploadFTP.FTPUpload(media_filepath, media_extension, thisContext);
				Intent MailIntent = new Intent(getApplicationContext(), MailSenderActivity.class);
				MailIntent.putExtra("Type", dataType);
				MailIntent.putExtra("Filename", media_filename);
				startActivity(MailIntent);
			}
			finish();
		}
	}
	
	public static void setImage_filepath(String fp)
	{
		image_filepath = fp;
	}
	
	public String getImage_filepath()
	{
		return image_filepath;
	}
	
	@Override
	public void onDestroy()
	{
		finish();
		super.onDestroy();
	}
	
	public void createStorageDirectory()
	{
		sd = Environment.getExternalStorageDirectory();
		storageFolder = new File(sd, Globals.STORAGE_DIRECTORY);
		if (sd.canWrite())
		{
			if (!storageFolder.exists())
				storageFolder.mkdir();
			mediaFolder = new File(sd, Globals.STORAGE_DIRECTORY + Globals.MEDIA_DIRECTORY);
			if (!mediaFolder.exists())
				mediaFolder.mkdir();
		}
	}
}

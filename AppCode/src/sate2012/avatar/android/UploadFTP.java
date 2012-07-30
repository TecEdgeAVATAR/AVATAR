package sate2012.avatar.android;

//import sate2012.avatar.android.GPSActivity.MyLocationListener;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;
/*import android.location.Location;
 import android.location.LocationListener;
 import android.location.LocationManager;

 import android.app.Application;

 import android.content.Intent;
 import android.content.pm.ActivityInfo;
 import android.util.Log; 

 import android.widget.Button; 

 import android.widget.EditText;*/

//import java.util.List;
//import java.io.File;
import java.io.IOException;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.net.InetAddress;

//import sate2012.avatar.android.BlueprintActivity;

import org.apache.commons.net.ftp.FTPClient;

public class UploadFTP extends Activity {

	// public static Context thisContext;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Toast.makeText(getApplicationContext(), "Created UploadFTP Activity",
		// Toast.LENGTH_LONG).show();
		// thisContext = getApplicationContext();

	}

	public static String FTPUpload(String filepath, String extension, Context thisContext) {
		// Toast.makeText(thisContext, "FTPUpload: filepath = "+filepath,
		// Toast.LENGTH_LONG).show();
		// setContentView(R.layout.loading_spinner);
		FTPClient ftpClient = new FTPClient();
		long time = (System.currentTimeMillis());
		String filename = "T" + time;
		try {
			ftpClient.connect(InetAddress.getByName("virtualdiscoverycenter.net"));
			ftpClient.login("opensim", "widdlyscuds");
			ftpClient.changeWorkingDirectory("../../var/www/avatar/Uploaded");
			// Toast.makeText(thisContext,
			// "Found AVATAR directory",Toast.LENGTH_LONG).show();
			if (ftpClient.getReplyString().contains("250")) {
				Handler progressHandler = new Handler();
				ftpClient.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
				// Toast.makeText(thisContext,
				// "set file type to binary",Toast.LENGTH_LONG).show();
				BufferedInputStream buffIn = null;
				// Toast.makeText(thisContext,
				// "buffIn = null",Toast.LENGTH_LONG).show();
				buffIn = new BufferedInputStream(new FileInputStream(filepath));
				// Toast.makeText(thisContext,
				// "buffIn = new",Toast.LENGTH_LONG).show();
				ftpClient.enterLocalPassiveMode();
				// Toast.makeText(thisContext, filename,
				// Toast.LENGTH_LONG).show();
				ProgressInputStream progressInput = new ProgressInputStream(buffIn, progressHandler);
				boolean result = ftpClient.storeFile(filename + extension, progressInput);
				// Toast.makeText(thisContext, "uploaded! " + result,
				// Toast.LENGTH_LONG).show();
				buffIn.close();
				ftpClient.logout();
				ftpClient.disconnect();
			}

			/*
			 * } catch (SocketException e) { Log.e(SorensonApplication.TAG,
			 * e.getStackTrace().toString()); } catch (UnknownHostException e) {
			 * Log.e(SorensonApplication.TAG, e.getStackTrace().toString()); }
			 */
		} catch (IOException e) { // Log.e(SorensonApplication.TAG,
									// e.getStackTrace().toString());
		}
		return filename + extension;
	}
}
package sate2012.avatar.android;

import gupta.ashutosh.avatar.R;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class CameraAct extends Activity implements View.OnClickListener {

	File pic;
	ImageView iv;
	Button ib;
	Intent i;
	final static int cameraData = 0;
	Bitmap bmp;
	private String OUTPUT_FILE = "_P.png";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo);
		initialize();
		Button uploadB = (Button) findViewById(R.id.upload_button);
		uploadB.setOnClickListener(this);
	}

	private void initialize() {
		iv = (ImageView) findViewById(R.id.ivReturnedPicture);
		ib = (Button) findViewById(R.id.ibTakePic);
		ib.setOnClickListener(this);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case (R.id.ibTakePic):
			i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(i, cameraData);
			break;
		case (R.id.upload_button):
			Intent data = new Intent();
			setResult(Activity.RESULT_OK, data);
			BlueprintActivity.setImage_filepath(pic.getAbsolutePath());
			finish();
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.i("INFO", "onActivityResult in CamerAct");
		if (requestCode == cameraData) {
			Bundle extras = data.getExtras();
			bmp = (Bitmap) extras.get("data");
			iv.setImageBitmap(bmp);

			pic = new File(Environment.getExternalStorageDirectory(),
					BlueprintConstants.STORAGE_DIRECTORY
							+ BlueprintConstants.MEDIA_DIRECTORY
							+ System.currentTimeMillis() + OUTPUT_FILE);
			FileOutputStream stream = null;
			try {
				stream = new FileOutputStream(pic);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
			Uri uri = Uri.fromFile(pic);
			Log.i("INFO", "image uri: " + uri);
			Log.i("INFO", "image filepath: " + pic.getAbsolutePath());

		}
	}

	public String getPath() {
		Log.i("INFO", "filepath: " + pic.getAbsolutePath());
		return pic.getAbsolutePath();
	}
}
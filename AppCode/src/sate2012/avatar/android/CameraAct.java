package sate2012.avatar.android;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

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



public class CameraAct extends Activity implements View.OnClickListener{

	File pic;
	ImageView iv;
	ImageButton ib;
	Intent i;
	final static int cameraData = 0;
	Bitmap bmp;
	private int index;
	private String OUTPUT_FILE = "_P.png";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo);
		index = 0;
		initialize();
		Button uploadB = (Button) findViewById(R.id.upload_button);
		uploadB.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				
				Intent data = new Intent();
				setResult(Activity.RESULT_OK, data);
				/*data.putExtra(OUTPUT_FILE, getPath());
				if (getPath() != null) {
					setResult(Activity.RESULT_OK, data);
				} else {
					setResult(Activity.RESULT_CANCELED);
				}*/
				BlueprintActivity.setImage_filepath( pic.getAbsolutePath());
				finish();
			}
		});

	}
	private void initialize() {
	// TODO Auto-generated method stub
	iv = (ImageView) findViewById (R.id.ivReturnedPicture);
	ib = (ImageButton) findViewById (R.id.ibTakePic);
	ib.setOnClickListener(this);
	}
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()){
		case R.id.ibTakePic:
			i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(i, cameraData);
		break;

		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.i("INFO", "onActivityResult in CamerAct");
		if ((requestCode == cameraData) /*&& resultCode == RESULT_OK */){
//			Uri photoPath = data.getData();
			//TODO send to server later
			Bundle extras = data.getExtras();
			bmp = (Bitmap) extras.get("data");
			iv.setImageBitmap(bmp);
			
			
			pic = new File(Environment.getExternalStorageDirectory(), 
			BlueprintConstants.STORAGE_DIRECTORY + BlueprintConstants.MEDIA_DIRECTORY + System.currentTimeMillis() + OUTPUT_FILE); 
			
			FileOutputStream stream = null;
			try {
				stream = new FileOutputStream(pic);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
	
			Uri uri = Uri.fromFile(pic);
			index++;
			Toast.makeText( getApplicationContext(), "URI is: " + uri, Toast.LENGTH_LONG).show(); 
			Log.i("INFO", "image uri: "+uri);
			Log.i("INFO", "image filepath: "+pic.getAbsolutePath());
			
		}
	}
	



	public String getPath() {
		Log.i("INFO", "filepath: "+pic.getAbsolutePath());
		return pic.getAbsolutePath();
	}


}




//package com.gantert.blueprint;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//
//
//
//public class Cameras extends Activity implements OnClickListener {
//	private ImageView iv;		//preview of picture
//	private ImageButton ib;		//button to the camera app
//	private Button gb;			//button to go to the gallery
//	private Bitmap bmp;			//the image that is put into image view
//	private Context conn;		//the context

//	final static int cameraData = 0;
//	
//	protected void onCreate(Bundle savedInstanceState){
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.photo);
//		conn = super.getApplicationContext();

//		iv = (ImageView) findViewById(R.id.ivReturnedPicture);
//		ib = (ImageButton) findViewById(R.id.ibTakePic);
//		ib.setOnClickListener(this);
//		gb = (Button) findViewById(R.id.gallerybutton);
//		gb.setOnClickListener(this);
//	}
//	
//	public void onClick(View v){
//		switch(v.getId()){
//			case(R.id.ibTakePic):
//				Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//				startActivityForResult(i, cameraData);
//				break;
//			case(R.id.gallerybutton):
//				//TODO: connect gallery button to photo gallery of camera
//				break;
//		}
//	}
//	
//	public void onActivityResult(int requestCode, int resultCode, Intent intent){
//		super.onActivityResult(requestCode, resultCode, intent);
//		if(resultCode == RESULT_OK){
//			Bundle extras = intent.getExtras();
//			bmp = (Bitmap) extras.get("data");
//			iv.setImageBitmap(bmp);
//		}
//	}
//	
//}
//








//package com.gantert.blueprint;
//
//
//import java.io.File;
//
////import com.gantert.blueprint.GPSActivity.MyLocationListener;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.Dialog;
//
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.database.Cursor;
//import android.graphics.Bitmap;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
//import android.net.Uri;
//import android.os.Bundle;
////import android.os.Environment;
//import android.provider.MediaStore;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//public class Cameras extends Activity implements OnClickListener{
//
//	
//	ImageView iv;
//	ImageButton ib;
//	Intent i;
//	final static int cameraData = 0;
//	Bitmap bmp;
//	Context conn;
//    private int index;
//    private String OUTPUT_FILE = "picture" + index + ".jpeg";
//    private static final MediaFileManager mediaManager = new MediaFileManager();
//
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState){
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.photo);
////		index = 0;	//-----------------------------------------------
//		conn = super.getApplicationContext();
//	        
//	     /* Use the LocationManager class to obtain GPS locations */ 
//
//		initialize();
//		
//		onCreateDialog(index);
//		
//		
//	}
//	//$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
//	// This function queries for the image from the URI
//	public String getPath(Uri uri) {
//		String[] projection = { MediaStore.Images.Media.DATA };
//		Cursor cursor = managedQuery(uri, projection, null, null, null);
//		if (cursor != null) {
//			// HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
//			// THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
//			int column_index = cursor
//			.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//			cursor.moveToFirst();
//			return cursor.getString(column_index);
//		} else return null;
//	}
//
//    
//
//	
//    
//    
//	private void initialize() {
//	// TODO Auto-generated method stub
//	iv = (ImageView) findViewById (R.id.ivReturnedPicture);
//	ib = (ImageButton) findViewById (R.id.ibTakePic);
//	ib.setOnClickListener(this);
//	} 
//	
//	
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		if (v.getId() == R.id.ibTakePic)
//			i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//			//mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, (long)2000, (float)0.1, mlocListener); 
//			startActivityForResult(i, cameraData);
//			index++;
//			
////			//-----------------------------------------------------------------------
////			File pic = new File(Environment.getExternalStorageDirectory(),
////					BlueprintConstants.STORAGE_DIRECTORY
////					+ BlueprintConstants.MEDIA_DIRECTORY + OUTPUT_FILE); 
//
//	}
//
////	@Override
////	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
////		// TODO Auto-generated method stub
////		super.onActivityResult(requestCode, resultCode, data);
////		if (resultCode == RESULT_OK){
////			Bundle extras = data.getExtras();
////			bmp = (Bitmap) extras.get("data");
////			iv.setImageBitmap(bmp);
////		}
////	}
//	
//	//$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
//	// on activity result that pulls the image from (1) gallery or (2) camera intent
//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
//		if ((resultCode == Activity.RESULT_OK)
//		&& (requestCode == BlueprintConstants.CAMERA_REQUEST)) {
//			File image = mediaManager.getImage();
//			File gImage;
//			String selectedImagePath;
//			if (image == null) {
//				Uri selectedImageUri = intent.getData();
//				selectedImagePath = getPath(selectedImageUri);
//				if (selectedImagePath != null) {
//					gImage = new File(selectedImagePath);
//
////					//remaining lines add to a media list...you won't need
////					attachments.add(gImage);
////					data.setAttachments(attachments);
////					mediaList
////					.add(UFOUtils.getFileName(gImage.getAbsolutePath()));
////					adapter.notifyDataSetChanged();
//				}
//			}
////			}else {   
////		//remaining lines add to a media list...you won't need
////				attachments.add(image);
////				data.setAttachments(attachments);
////				mediaList.add(UFOUtils.getFileName(image.getAbsolutePath()));
////				adapter.notifyDataSetChanged();
////				mediaManager.setImage(null);
////			}
//		}
//	}
//	
//	//$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
//	// This is the dialog that allows for (1) taking pic, or (2) browsing and selecting from gallery
//	@Override
//	protected Dialog onCreateDialog(int id) {
//		// Here just use a default dialog, the custom won't work in your app
//		//final CustomDialog dialog = new CustomDialog(this, R.style.UfoThemeCustomDialog);
//	    final Dialog dialog = new Dialog(conn);
//		switch (id) {
//			case BlueprintConstants.CAMERA_REQUEST:
//			dialog.setTitle("Photos");
//			//dialog.setText("");
//			//dialog.setImage(android.R.drawable.ic_menu_camera);
//		
//			AlertDialog.Builder builder = new AlertDialog.Builder(this);
//			builder.setMessage("Choose whatever you want i dont care")
//			       .setCancelable(false)
//			       .setPositiveButton("Camera", new DialogInterface.OnClickListener() {
//			           public void onClick(DialogInterface dialog, int id) {
////			                MyActivity.this.finish();
//							dialog.cancel();
//							Intent cameraIntent = new Intent(
//							MediaStore.ACTION_IMAGE_CAPTURE);
//							File file = mediaManager.createImageFile();
//							cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
//							Uri.fromFile(file));
//							Log.d("DEBUG", "**********The URI for the image is " + Uri.fromFile(file));
//							startActivityForResult(cameraIntent,
//							BlueprintConstants.CAMERA_REQUEST);
//			           }
//			       })
//			       .setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
//			           public void onClick(DialogInterface dialog, int id) {
//			        	   dialog.cancel();
//							Intent cameraGalleryIntent = new Intent(
//							Intent.ACTION_GET_CONTENT);
//							cameraGalleryIntent.setType("image/*");
//							startActivityForResult(Intent.createChooser(
//							cameraGalleryIntent, "Select Picture"),
//							BlueprintConstants.CAMERA_REQUEST);
//			           }
//			       });
//			AlertDialog alert = builder.create();
//			
//			
////			dialog.setDialogButton1("Capture", new OnClickListener() {
////				public void onClick(View v) {
////					dialog.cancel();
////					Intent cameraIntent = new Intent(
////					MediaStore.ACTION_IMAGE_CAPTURE);
////					File file = mediaManager.createImageFile();
////					cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
////					Uri.fromFile(file));
////					Log.d("DEBUG",
////					"**********The URI for the image is "
////					+ Uri.fromFile(file));
////					startActivityForResult(cameraIntent,
////					BlueprintConstants.CAMERA_REQUEST);
////				}	
////			});
////			dialog.setDialogButton2("Gallery", new OnClickListener() {
////				public void onClick(View v) {
////					dialog.cancel();
////					Intent cameraGalleryIntent = new Intent(
////					Intent.ACTION_GET_CONTENT);
////					cameraGalleryIntent.setType("image/*");
////					startActivityForResult(Intent.createChooser(
////					cameraGalleryIntent, "Select Picture"),
////					BlueprintConstants.CAMERA_REQUEST);
////				}
////			});
//
//
//		}
//		return dialog;
//	}
//}


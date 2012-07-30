package com.SATE2012.MapsForgeMapViewer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

public class MediaSelectorDialog extends Activity
{
	private Handler handler = new Handler();
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.upload_type_selector);
        
        // Set the video button listener
        ImageButton videoButton = (ImageButton)findViewById(R.id.videoButton);
        videoButton.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View view) 
            {
                handler.post(new Runnable() 
                {
                    public void run() 
                    {
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        intent.putExtra(getString(R.string.iconValueLocation), 1);
                        // exit the dialog
                        finish();
                    }
                });
            }
        });
        
        // Set the camera button listener
        ImageButton cameraButton = (ImageButton)findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View view) 
            {
                handler.post(new Runnable() 
                {
                    public void run() 
                    {
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        intent.putExtra(getString(R.string.iconValueLocation), 2);
                        // exit the dialog
                        finish();
                    }
                });
            }
        });
        
        // Set the microphone button listener
        ImageButton microphoneButton = (ImageButton)findViewById(R.id.microphoneButton);
        microphoneButton.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View view) 
            {
                handler.post(new Runnable() 
                {
                    public void run() 
                    {
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        intent.putExtra(getString(R.string.iconValueLocation), 3);
                        // exit the dialog
                        finish();
                    }
                });
            }
        });
        
        // Set the comment button listener
        ImageButton commentButton = (ImageButton)findViewById(R.id.commentButton);
        commentButton.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View view) 
            {
                handler.post(new Runnable() 
                {
                    public void run() 
                    {
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        intent.putExtra(getString(R.string.iconValueLocation), 4);
                        // exit the dialog
                        finish();
                    }
                });
            }
        });
        
        // Set the cancel button listener
        ImageButton cancelButton = (ImageButton)findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View view) 
            {
                handler.post(new Runnable() 
                {
                    public void run() 
                    {
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        intent.putExtra(getString(R.string.iconValueLocation), 0);
                        // exit the dialog
                        finish();
                    }
                });
            }
        });
    }
}
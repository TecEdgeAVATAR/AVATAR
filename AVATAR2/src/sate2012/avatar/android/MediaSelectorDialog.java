package sate2012.avatar.android;

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
                        // exit the dialog
                        finish();                
                    }
                });
            }
        }); 
    }
}
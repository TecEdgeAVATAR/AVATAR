package sate2012.avatar.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


public class AVATARMainMenuActivity extends Activity implements OnClickListener{
	private Button uploadB;
	private Button mapB;
	private Button naoB;
	private Button arB;
	private Button settingB;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        uploadB = (Button) findViewById(R.id.uploadB);
        uploadB.setOnClickListener(this);
        mapB = (Button) findViewById(R.id.mapB);
        mapB.setOnClickListener(this);
        naoB = (Button) findViewById(R.id.naoB);
        naoB.setOnClickListener(this);
        arB = (Button) findViewById(R.id.arB);
        arB.setOnClickListener(this);
        settingB = (Button) findViewById(R.id.settingB);
        settingB.setOnClickListener(this);
    }
    public void onClick(View v){
    	switch(v.getId()){
    		case(R.id.uploadB):
    			Intent intent = new Intent(AVATARMainMenuActivity.this, BlueprintActivity.class);
    			startActivity(intent);
    			break;
    		case(R.id.mapB):
    			Toast.makeText(getApplicationContext(), "These are not the maps you're looking for...", Toast.LENGTH_LONG).show();
    			//Create/call intent to map activity here
    				// Load the MapsForgeMapViewer
    				Toast.makeText(getApplicationContext(), "MapViewer is not yet available.", Toast.LENGTH_LONG).show();
    			break;
    		case(R.id.naoB):
    			Toast.makeText(getApplicationContext(), "Ashu couldn't get this to work.", Toast.LENGTH_LONG).show();
    			//Create/call intent to nao activity here
    			break;
    		case(R.id.arB):
    			Toast.makeText(getApplicationContext(), "Yeah, like WE are ever going to get this to work?", Toast.LENGTH_LONG).show();
    			//Create/call intent to augmented reality activity here
    			break;
    		case(R.id.settingB):
    			Toast.makeText(getApplicationContext(), "There are no settings. You WILL like it the way we made it.", Toast.LENGTH_LONG).show();
    			//Create/call intent to settings activity here
    			break;
    	}
    }
}
package sate2012.avatar.android;

import org.mapsforge.android.maps.GeoPoint;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

public class LocationDataReceiverAVATAR extends Activity
{

    double latitude;
    double longitude;

    private GeoPoint customPoint;

    int start;
    int space;
    String input;
    int last;
    String firstDataPointString;
    double firstDataPointDouble;
    String secondDataPointString;
    double secondDataPointDouble;
    String thirdDataPointString;
    double thirdDataPointDouble;
    String fourthDataPointString;
    double fourthDataPointdouble;
    String fifthDataPointString;
    double fifthDataPointDouble;

    // these second three are to find the latitude of the point.
    int secondStart;
    int secondSpace;
    int secondLast;
    // these third three are to find the longitude of the point.
    int thirdStart;
    int thirdSpace;
    int thirdLast;

    // these fourth three are to find the of the point.
    int fourthStart;
    int fourthSpace;
    int fourthLast;

    // these fifth three are to find the of the point.
    int fifthStart;
    int fifthSpace;
    int fifthLast;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.map_view);
	Intent intent = getIntent();
	String message = intent
		.getStringExtra(MapsForgeMapViewer.EXTRA_MESSAGE);
	CoordinateDataTranslator();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
	// getMenuInflater().inflate(R.upload_menu.activity_location_data_receiver_avatar,
	// menu);
	return true;
    }

    public void latLonConvertor(GeoPoint point)
    {
	latitude = Double.parseDouble(secondDataPointString);
	longitude = Double.parseDouble(thirdDataPointString);
	DataObject data = new DataObject();
	Drawable newPoint = getResources().getDrawable(R.drawable.ic_launcher);
	DataObjectItem newPointItem = new DataObjectItem(point, data);

	newPointItem.setMarker(MVItemizedOverlay.boundCenterBottom(newPoint));
	// data.setLat(pointLocLat);
	// data.setLon(pointLocLon);
	// itemizedOverlay.addOverLay(newPointItem);
    }
    
    

    public void CoordinateDataTranslator()
    {
	/*
	 * This method takes the data that has been downloaded and takes it apart. Then it puts it into a format that the application can read.
	 */

	// this is for the beginning of the point. it finds the name of the
	// point.
	start = input.indexOf("@@POINT:");
	space = input.indexOf(" ", start);
	last = input.indexOf(" ", space);
	if (start >= 0)
	{
	    if (space >= 0)
	    {
		// firstDataPointString is the point data for the name
		firstDataPointString = input.substring(space, last);
		System.out.println(input.substring(space, last));
		Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT)
			.show();

	    }
	}
	// this is for the second part of the point. it finds the latitude of
	// the point.
	secondStart = input.indexOf("_***");
	secondSpace = input.indexOf("_", secondStart);
	secondLast = input.indexOf("_", secondSpace);
	if (secondStart >= 0)
	{
	    if (secondSpace >= 0)
	    {
		secondDataPointString = input
			.substring(secondSpace, secondLast);
		System.out.println(input.substring(secondSpace, secondLast));
		Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT)
			.show();

	    }
	}

	// this is the third part of the point. it finds the longitude of the
	// point.
	thirdStart = input.indexOf("***_");
	thirdLast = input.indexOf("_***", thirdStart);
	if (thirdStart >= 0)
	{
	    if (thirdSpace >= 0)
	    {
		thirdDataPointString = input.substring(thirdSpace, thirdLast);
		System.out.println(input.substring(thirdSpace, thirdLast));
		Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT)
			.show();

	    }
	}

	// this is the fourth part of the point. it finds the type of the point.
	fourthStart = input.indexOf("***_");
	fourthLast = input.indexOf("_***", fourthLast);
	if (fourthStart >= 0)
	{
	    if (fourthSpace >= 0)
	    {
		fourthDataPointString = input
			.substring(fourthSpace, fourthLast);
		System.out.println(input.substring(fourthSpace, fourthLast));
		Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT)
			.show();

	    }
	}
	// this is the fifth part of the point. it finds the dependent on the
	// fourth variable (audio picture or video) of the point.
	fifthStart = input.indexOf("***_");
	fifthLast = input.indexOf("@", fifthStart);
	if (fifthStart >= 0)
	{
	    if (fifthSpace >= 0)
	    {
		fifthDataPointString = input.substring(fifthSpace, fifthLast);
		System.out.println(input.substring(fifthSpace, fifthLast));
		Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT)
			.show();

	    }
	}

	latLonConvertor(customPoint);

    }

    // ***_39.7_
    // ***_-84.2_
    // ***_Photo_
    // ***_virtualdiscoverycenter.net/../../var/www/avatar/uploadedT1342804869789_P.png
    // @@@POINT: Jason's phone _
    // ***_39.744198275730014_
    // ***_-84.06325832940638_
    // ***_Android_
    // ***_358940040608809
    // @@@POINT: fairwood and Andrea w_
    // ***_39.74605109542608_
    // ***_-84.06306638382375_
    // ***_Audio_
    // ***_virtualdiscoverycenter.net/../../var/www/avatar/uploadedT1342575995075_A.mp4
    // @@@POINT: rich and willow creek_
    // ***_39.74753949683275_
    // ***_-84.06054045404257_
    // ***_Photo_
    // ***_virtualdiscoverycenter.net/../../var/www/avatar/uploadedT1342575610631_P.png
    // @@@POINT: fairwood and rich_
    // ***_39.74535351153463_
    // ***_-84.06127944588661_
    // ***_Video_
    // ***_virtualdiscoverycenter.net/../../var/www/avatar/uploadedT1342575338419_V.f4v
    // @@@POINT: fairwood and old north fairfield_
    // ***_39.74523251876235_
    // ***_-84.05700098723173_
    // ***_Comment_
    // ***_intersection
    // @@@POINT: 3349 andrea_
    // ***_39.745109556242824_
    // ***_-84.05778947286308_
    // ***_Photo_
    // ***_virtualdiscoverycenter.net/../../var/www/avatar/uploadedT1342574804267_P.png
    // @@@POINT: home_
    // ***_39.74332961719483_
    // ***_-84.06092271208763_
    // ***_Photo_
    // ***_virtualdiscoverycenter.net/../../var/www/avatar/uploadedT1342573881519_P.png
    // @@@END OF MESSAGES
}

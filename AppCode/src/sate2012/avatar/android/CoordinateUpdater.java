package sate2012.avatar.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;

import org.mapsforge.android.maps.GeoPoint;

public class CoordinateUpdater {
	private String pointID;			//The point's identification
	private String pointLat;		//The point's latitude
	private String pointLng;		//The point's longitude
	private double pointLatD;		
	private double pointLngD;
	private String pointType;		//The point's type (Audio, Video, Photo, Comment)
	private URL pointURL;			//The point's URL
	private String input;			//The string downloaded from the server
	private LinkedList<DataObjectItem> dataList;	//Stores all points downloaded

	public CoordinateUpdater() {
		input = "";
		try {
			URL site = new URL("http://virtualdiscoverycenter.net/avatar/php_files/email_rec_VW.php");
			URLConnection siteConnection = site.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(siteConnection.getInputStream()));
			String inputLine;
			in.readLine(); // Removes the extra '\n' character in the beginning
			while ((inputLine = in.readLine()) != null)
				input += inputLine;
			in.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public void CoordinateDataTranslator() {
		dataList = new LinkedList<DataObjectItem>();
		int startPos = input.indexOf(" ");
		int endPos = input.indexOf("_***");
		while(endPos != input.indexOf("@@@END")){
			pointID = input.substring(startPos, endPos);
			startPos = input.indexOf("*_", endPos);
			endPos = input.indexOf("_***", startPos);
			pointLat = input.substring(startPos + 2, endPos);
			startPos = input.indexOf("*_", endPos);
			endPos = input.indexOf("_***", startPos);
			pointLng = input.substring(startPos + 2, endPos);
			startPos = input.indexOf("*_", endPos);
			endPos = input.indexOf("_***", startPos);
			pointType = input.substring(startPos + 2, endPos);
			startPos = input.indexOf("*_", endPos);
			endPos = input.indexOf("@@@", startPos);
			try { pointURL = new URL("http://" + input.substring(startPos + 2, endPos) + "/");
			} catch (MalformedURLException e) { e.printStackTrace(); }
			input = input.substring(endPos);
			startPos = input.indexOf(" ");
			if(input.indexOf("@@@END") != 0) endPos = input.indexOf("_***");
			else endPos = input.indexOf("@@@END");
			//Convert the latitude and the longitude to doubles for plotting on MapsView
			pointLatD = Double.parseDouble(pointLat);
			pointLngD = Double.parseDouble(pointLng);
			DataObject pointData = new DataObject(pointLatD, pointLngD, pointID, pointType, pointURL);
			GeoPoint pointCoord = new GeoPoint(pointLatD, pointLngD);
			DataObjectItem newPoint = new DataObjectItem(pointCoord, pointData);
			dataList.add(newPoint);
		}
	}

	public LinkedList<DataObjectItem> getDataList() {
		return dataList;
	}

	public void setDataList(LinkedList<DataObjectItem> d) {
		this.dataList = d;
	}
}
//@@@POINT: T1343916460543_P.png_***_39.773986_***_-84.111322_***_Photo_***_virtualdiscoverycenter.net/avatar/Uploaded/T1343916460543_P.png @@@POINT: testing_***_39.781521_***_-84.085315_***_Comment_***_this is a test @@@POINT: T1343878803026_P.png_***_39.783698_***_-84.058364_***_Photo_***_virtualdiscoverycenter.net/avatar/Uploaded/T1343878803026_P.png @@@POINT: T1343866615833_V.f4v_***_39.769966_***_-84.053231_***_Video_***_virtualdiscoverycenter.net/avatar/Uploaded/T1343866615833_V.f4v @@@POINT: T1343866160470_V.f4v_***_39.780695_***_-84.078446_***_Video_***_virtualdiscoverycenter.net/avatar/Uploaded/T1343866160470_V.f4v @@@POINT: T1343866101542_P.png_***_39.780761_***_-84.058877_***_Photo_***_virtualdiscoverycenter.net/avatar/Uploaded/T1343866101542_P.png @@@POINT: T1343835905493_V.f4v_***_39.785456_***_-84.132086_***_Video_***_virtualdiscoverycenter.net/avatar/Uploaded/T1343835905493_V.f4v @@@POINT: T1343835862472_P.png_***_39.774771_***_-84.124189_***_Photo_***_virtualdiscoverycenter.net/avatar/Uploaded/T1343835862472_P.png @@@POINT: alex.png_***_39.782892_***_-84.109673_***_Photo_***_virtualdiscoverycenter.net/avatar/Uploaded/T1343834732775_P.png @@@POINT: ashuuuu_***_39.777659_***_-84.132045_***_Comment_***_wassup yo? @@@POINT: ashuuuuu_***_39.771834_***_-84.134472_***_Comment_***_guuuuupta @@@POINT: woot woot_***_39.778488_***_-84.059351_***_Comment_***_test @@@POINT: T1343757565084_A.mp4_***_39.782208_***_-84.103757_***_Audio_***_virtualdiscoverycenter.net/avatar/Uploaded/T1343757565084_A.mp4 @@@END OF MESSAGES


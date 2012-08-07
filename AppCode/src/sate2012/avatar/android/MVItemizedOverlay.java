package sate2012.avatar.android;

import java.util.ArrayList;

import org.mapsforge.android.maps.ItemizedOverlay;
import org.mapsforge.android.maps.MapActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class MVItemizedOverlay extends ItemizedOverlay<DataObjectItem>
{
	private static final int VIDEO_VIEWER_REQUEST_CODE = 498438;
	private static final int AUDIO_VIEWER_REQUEST_CODE = 752684;
	private static final int IMAGE_VIEWER_REQUEST_CODE = 789543;
	private static final int TEXT_VIEWER_REQUEST_CODE = 148325;
	private MapActivity activity;
	
	public ArrayList<DataObjectItem> mOverlays;
	public MapsForgeMapViewer mActivtyl;
	
	public static Drawable boundCenterBottom(Drawable drawable)
	{
		return ItemizedOverlay.boundCenter(drawable);
	}
	
	public MVItemizedOverlay(Drawable drawable, MapActivity activity)
	{
		super(boundCenter(drawable));
		this.activity = activity;
		mOverlays = new ArrayList<DataObjectItem>();
	}
	
	public void addOverlay(DataObjectItem overlay)
	{
		mOverlays.add(overlay);
		populate();
	}
	
	public void removeOverlay(int index)
	{
		mOverlays.remove(index);
		populate();
	}
	
	public void clear()
	{
		mOverlays.clear();
		populate();
	}
	
	@Override
	protected DataObjectItem createItem(int i)
	{
		return mOverlays.get(i);
	}
	
	@Override
	public int size()
	{
		return mOverlays.size();
	}
	
	@Override
	public boolean onTap(int i)
	{
		DataObjectItem item = mOverlays.get(i);
		final DataObject data = item.getData();
		try
		{
			String url = data.getUrl().toString();
			if (!url.contains("http:///"))
			{
				// Remove the ending "/"
				url = url.substring(0, url.lastIndexOf("/"));
				
				if (url.contains(".f4v"))
				{
					Intent intent = new Intent(activity, VideoViewer.class);
					intent.putExtra("sate2012.avatar.android.URL", url);
					activity.startActivityForResult(intent, VIDEO_VIEWER_REQUEST_CODE);
				}
				else if (url.contains(".mp4"))
				{
					Intent intent = new Intent(activity, AudioViewer.class);
					intent.putExtra("sate2012.avatar.android.URL", url);
					activity.startActivityForResult(intent, AUDIO_VIEWER_REQUEST_CODE);
				}
				else if (url.contains(".png"))
				{
					Intent intent = new Intent(activity, ImageViewer.class);
					intent.putExtra("sate2012.avatar.android.URL", url);
					intent.putExtra("sate2012.avatar.android.LAT", String.valueOf(data.getLat()));
					intent.putExtra("sate2012.avatar.android.LNG", String.valueOf(data.getLon()));
					activity.startActivityForResult(intent, IMAGE_VIEWER_REQUEST_CODE);
				}
				else
				{
					url = url.substring(url.indexOf("/") + 2);
					Intent intent = new Intent(activity, TextViewer.class);
					intent.putExtra("sate2012.avatar.android.URL", url);
					intent.putExtra("sate2012.avatar.android.LAT", String.valueOf(data.getLat()));
					intent.putExtra("sate2012.avatar.android.LNG", String.valueOf(data.getLon()));
					activity.startActivityForResult(intent, TEXT_VIEWER_REQUEST_CODE);
				}
			}
		}
		catch (Exception e)
		{
			Log.d("MVItemizedOverlay", e.toString());
		}
		return true;
	}
}

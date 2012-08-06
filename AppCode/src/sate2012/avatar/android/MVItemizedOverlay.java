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
			// Remove the ending "/"
			url = url.substring(0, url.lastIndexOf("/"));
			
			if (url.contains(".f4v"))
			{
				Intent intent = new Intent(activity, VideoViewer.class);
				intent.putExtra("sate2012.avatar.android.URL", url);
				activity.startActivityForResult(intent, VIDEO_VIEWER_REQUEST_CODE);
			}
		}
		catch (Exception e)
		{
			Log.d("MVItemizedOverlay", e.toString());
		}
		return true;
	}
}

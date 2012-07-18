package com.SATE2012.MapsForgeMapViewer;

import java.io.File;

public class DataObject 
{
    private double lat;
    private double lon;
    private File video;
    private String text;
    private File image;
    
    public File getVideo()
    {
        return video;
    }
    public void setVideo(File video)
    {
        this.video = video;
    }
    public String getText()
    {
        return text;
    }
    public void setText(String text)
    {
        this.text = text;
    }
    public File getImage()
    {
        return image;
    }
    public void setImage(File image)
    {
        this.image = image;
    }
    public double getLat()
    {
        return lat;
    }
    public void setLat(double lat)
    {
        this.lat = lat;
    }
    public double getLon()
    {
        return lon;
    }
    public void setLon(double lon)
    {
        this.lon = lon;
    }
}

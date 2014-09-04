package com.example.texthh;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.util.Base64;
/**
 * Application类
 * @author mason liu
 *
 */
public class Ap extends Application {
	
	private static Ap a;
	private DisplayImageOptions noCacheOptions;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
		.cacheInMemory(true)
        .cacheOnDisc(true)
        .build();
		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
		.defaultDisplayImageOptions(defaultOptions)
		//.imageDownloader(new ImageDownloader(getApplicationContext()))
		.build();
		
		ImageLoader.getInstance().init(config);
		
		
		
	}

	public static Ap getA(Context con) {
		a=(Ap)con.getApplicationContext();
		return a;
	}

	public DisplayImageOptions getNoCacheOptions() {
		if(noCacheOptions==null){
			noCacheOptions = new DisplayImageOptions.Builder()
			.cacheInMemory(false)
	        .cacheOnDisc(false)
	        
	        .build();
		}
		return noCacheOptions;
	}
	
}

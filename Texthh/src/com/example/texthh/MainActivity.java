package com.example.texthh;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.nostra13.universalimageloader.core.ImageLoader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.widget.ImageView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//new  TS().execute();
		ImageView a=(ImageView)findViewById(R.id.test);
		//ImageLoader.getInstance().displayImage("https://img.mingdao.com/ProjectLogo/adccb6a0-99dc-4991-8200-c0531f943dc0.gif?rax=29",a);
		//ImageLoader.getInstance().displayImage("https://img.mingdao.com/UserAvatar/48X48/secretary.gif", a);
		//ImageLoader.getInstance().displayImage("drawable://" + R.drawable.secretary, a);
		ImageLoader.getInstance().displayImage("http://www.baidu.com/img/bdlogo.gif", a,Ap.getA(this).getNoCacheOptions());
		//System.out.println("dd");
	}
	// 将InputStream转换成Bitmap
	public Bitmap InputStream2Bitmap(InputStream is) {
	  return BitmapFactory.decodeStream(is);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	class TS extends AsyncTask<String,Void,Bitmap>{

		@Override
		protected Bitmap doInBackground(String... params) {
			// TODO Auto-generated method stub
			URL url;
			InputStream is=null;
			try {
				//http://www.baidu.com/img/bdlogo.gif
				url = new URL("https://img.mingdao.com/UserAvatar/48X48/secretary.gif");
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();			
				// 取得inputStream，并进行读取
				is = conn.getInputStream();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
//			//InputStream is=HttpUtil.urlByGet2InputStreamSSL("http://www.baidu.com/img/bdlogo.gif");
//			InputStream is=HttpUtil.httpByGet2InputStream("http://www.baidu.com/img/bdlogo.gif");
			if(is==null){
				System.out.println("kkkkkkkkkkkkkkk");
			}else{
				System.out.println("bbbbbbbbbbbbbbbbb");
			}
			Bitmap b=InputStream2Bitmap(is);
			return b;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			ImageView a=(ImageView)findViewById(R.id.test);
			a.setImageBitmap(result);
			System.out.println("ppppppppp");
		}
		
	}
}

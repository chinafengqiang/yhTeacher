package com.yhlearningclient.ui;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yhlearningclient.R;
import com.yhlearningclient.biz.AdviodManager;
import com.yhlearningclient.common.MyApp;
import com.yhlearningclient.model.Video;
import com.yhlearningclient.player.MiniPlayer;
import com.yhlearningclient.task.DownFileTask;
import com.yhlearningclient.utils.DateUtil;
import com.yhlearningclient.utils.FileUtil;
import com.yhlearningclient.utils.ImageService;

public class DetailActivity extends Activity{
	
	private static final String TAG = "DetailActivity";
	
	private Context context = null;
	private AdviodManager videoManager = null;
	private List<Video> videos = null;
	private Video video = null;
//	private Bitmap bitmap = null;
	private ImageView detail_image = null;
	private TextView detail_video_name = null;
	private TextView category = null;
	private TextView teacher = null;
	private TextView director = null;
	private TextView country = null;
	private TextView abstruce = null;
	private Button detail_return_btn = null;
	private Button detail_home_btn = null;
	private Button detail_download = null;//下载
	private Button download = null;
	private MyApp myApp;
	private String videoURL = null; //电影视频路径
	private List<Video> syncVideoList =new ArrayList<Video>();
	private final String pathName = "myVideo";
	ProgressDialog pd = null;
	private SharedPreferences sharedPreferences;
	String ip = "";
	
	/**
	 * 提标框
	 */
	private void  showProgressDialog(){
		if (pd==null){
			pd = new ProgressDialog(this);
			pd.setTitle("系统提示");
			pd.setMessage("数据获取中,请稍候...");
		}
		pd.show();
	}
	
	@Override
	protected void onDestroy() {
		hideProgressDialog();
		super.onDestroy();
	}
	
	/**
	 * 隐藏
	 */
	private void hideProgressDialog(){
		if (pd!=null) pd.dismiss();
	}
	
	private View.OnClickListener clickListener  = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.detail_download:
				detail_download.setFocusableInTouchMode(true);
				Intent intent = new Intent(context, MiniPlayer.class);
				Bundle bundle = new Bundle();
				bundle.putString("videourl", videoURL);
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case R.id.download:
				Log.d(TAG, "dddddddddddddddddx=="+videoURL);
				downloadVideos();
				break;
			default:
				break;
			}
			
		}
	};
	
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
	//		Tool.ShowMessage(context, msg.obj.toString());
			final String fileName = msg.obj.toString();
			//Looper.prepare();
//			new Thread(new Runnable() {
//
//				@Override
//				public void run() {
//					Tool.ShowMessage(context, "正在更新MediaProvider...");
//					Log.d("TestThread", "start");
//				//	Tool.sdCardScan(context);
//					Tool.ShowMessage(context, "更新MediaProvider完毕！");
//					
//					Tool.scanSdCard(context,fileName);
//					Log.d("TestThread", "end");
//				}
//			}).start();
			

		};
	};
	
	//得到当前下载的文件名
	private String getDownLoadFileName(){
		if (video!=null){
			String url = video.getUrl();
			int lastdotIndex = url.lastIndexOf("/");
			String fileName = url.substring(lastdotIndex+1, url.length());
			return fileName;
		}else{
			return "test.3gp";
		}
	}
	
	private void downloadVideos(){
		final String fileName = getDownLoadFileName();
		
//		Tool.ShowMessage(context, fileName);
		if(FileUtil.isExists("/sdcard/"+fileName)){
		     AlertDialog.Builder builder = new AlertDialog.Builder(context);
		     builder.setTitle("提示").setMessage("该视频文件已经存在于SDCard中，确定要覆盖下载吗？")
		     .setPositiveButton("确定", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
					DownFileTask task = new DownFileTask(context,handler,fileName, pathName);
					task.execute(videoURL);
				}
		    	 
		     }).setNegativeButton("取消", null).create().show();
		}else{
			DownFileTask task = new DownFileTask(context,handler,fileName, pathName);
			task.execute(videoURL);
		}
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.video_detail);
		//接收另一个Activity跳转过来的信息
		initView();
		context = this;
		int position = getIntent().getIntExtra("position", 0);
		videoManager = new AdviodManager();
		
		sharedPreferences = getSharedPreferences("serverIpObj", MODE_PRIVATE);
		ip = sharedPreferences.getString("serverIP", null);
		
		String serverIp = "http://"+ ip +":8080";
		
		if (getIntent().getIntExtra("videoid", 0)!=0){
			int id = getIntent().getIntExtra("videoid", 0);
			Videos v = new Videos(serverIp, id);
			Thread thread = new Thread(v);
			thread.start();
		}else{
		    video = videos.get(position);
		}
		
	}

	private void initDataBind(Video video) {
		
		String serverIp = "http://"+ ip +":8080";
		Log.d(TAG, video.toString());
		String url = video.getPic();
		String image_url = serverIp + url; //图片的地址
	//	final Bitmap bitmap = null;
		String urls = "";
		try {
			urls  = URLEncoder.encode(getDownLoadFileName(),"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		videoURL = serverIp+ "/uploadFile/file/" + urls;
	
		images im = new images(image_url);
		Thread thread = new Thread(im);
		thread.start();
		
		detail_video_name.setText(video.getName());
		category.setText(video.getCategoryName());
		director.setText(video.getLectuer());
		country.setText(DateUtil.formateDateToString(video.getCreatedTime()));
		abstruce.setText(video.getDescription());
		teacher.setText(video.getTeacherDescription());
	}

	private void initView() {
		detail_image = (ImageView) this.findViewById(R.id.detail_image);
		detail_video_name = (TextView) this.findViewById(R.id.detail_video_name);
		category = (TextView) this.findViewById(R.id.category);
		director = (TextView) this.findViewById(R.id.director);
		country = (TextView) this.findViewById(R.id.country);	
		abstruce = (TextView) this.findViewById(R.id.abstruct);
		teacher = (TextView) this.findViewById(R.id.teacher);
		detail_download = (Button) this.findViewById(R.id.detail_download);
		download = (Button) this.findViewById(R.id.download);
		
		detail_download.setOnClickListener(clickListener);
	    download.setOnClickListener(clickListener);
		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	class Videos implements Runnable{
	    
		   Video videos = null;
		   private String serverIp = "";
		   private int id;
			
		    public Videos(String serverIp, int id){
		    	this.serverIp = serverIp;
		    	this.id = id;
				showProgressDialog();
			}
			
		    @Override
			public void run() {
		    		videoManager = new AdviodManager();
					try {
						videos = videoManager.getVideosById(serverIp, id);
					} catch (Exception e) {
					}
					Message message = new Message();
					message.obj = videos;
					handleVideos.sendMessage(message);
			
			}
		}
	
	class images implements Runnable{
	    
		   private String image_url = "";
			
		    public images(String image_url){
		    	this.image_url = image_url;
				showProgressDialog();
			}
			
		    @Override
			public void run() {
				  
					Bitmap bitmap = null;
					byte[] result;	
					try {
							result = ImageService.getImage(image_url);
							bitmap = BitmapFactory.decodeByteArray(result, 0,result.length);// 生成图片
					} catch (Exception e) {
					}
					Message message = new Message();
					message.obj = bitmap;
					handleImages.sendMessage(message);
		}
	}
	
	private Handler handleVideos= new Handler(){
		public void handleMessage(android.os.Message msg) {
		
			video = (Video)msg.obj;
			if (video == null) {
				Toast.makeText(DetailActivity.this, "对不起,无数据", Toast.LENGTH_SHORT).show();
			}else{
				pd.setMessage("数据已获取,界面绑定中...");
				initDataBind(video);
				pd.setMessage("数据获取中,请稍候...");
				hideProgressDialog();
			}	
		};
	};
	
	private Handler handleImages= new Handler(){
		public void handleMessage(android.os.Message msg) {
		
			Bitmap bitmap = (Bitmap)msg.obj;
			if (bitmap == null) {
				Toast.makeText(DetailActivity.this, "对不起,无数据", Toast.LENGTH_SHORT).show();
			}else{
				pd.setMessage("数据已获取,界面绑定中...");
				detail_image.setBackgroundDrawable(new BitmapDrawable(bitmap));
				pd.setMessage("数据获取中,请稍候...");
				hideProgressDialog();
			}	
		};
	};
	

}

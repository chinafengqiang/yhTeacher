package com.yhlearningclient.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yhlearningclient.R;
import com.yhlearningclient.biz.AdviodManager;
import com.yhlearningclient.model.Video;
import com.yhlearningclient.task.VideoTask;
import com.yhlearningclient.utils.AsyncTaskImageLoad;

public class VideoCenterActivity extends Activity {

	private static final String TAG = "VideoCenterActivity";
	ProgressDialog pd = null;
	private SharedPreferences sharedPreferences;
	private Context context;
	String ip = "";
	private Button return_btn = null;
	private TextView title = null;
	private ListView listView = null;
	private Button bottom_recommend = null;
	private Button main_bottom_download = null;
	private AdviodManager videoManager = null;
	private LayoutInflater inflater = null;
	private VideoAdapter adapter = null;
	private VideoTask videoTask = null;
	private List<Video> videos = null;
	Long classId;

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
	
	/**
	 * 隐藏
	 */
	private void hideProgressDialog(){
		if (pd!=null) pd.dismiss();
	}
	
	@Override
	protected void onDestroy() {
		hideProgressDialog();
		super.onDestroy();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_center);
		
		context = this;
		
		sharedPreferences = getSharedPreferences("serverIpObj", MODE_PRIVATE);
		ip = sharedPreferences.getString("serverIP", null);
		classId = sharedPreferences.getLong("classId", 0);
		
		initView();
		loadData();
		bindClickListener();
	}
	
	public void initView() {
	//	return_btn = (Button) findViewById(R.id.return_btn);
		title = (TextView) findViewById(R.id.title);
		listView = (ListView) findViewById(R.id.listView);
		bottom_recommend = (Button) findViewById(R.id.bottom_recommend);
		main_bottom_download = (Button) findViewById(R.id.main_bottom_download);
		main_bottom_download.setOnClickListener(onClickListenerView);
		
	//	return_btn = (Button) findViewById(R.id.return_btn);
	//	return_btn.setOnClickListener(onClickListenerView);
		
		bottom_recommend.setOnClickListener(onClickListenerView);
		context = this;
		inflater = LayoutInflater.from(context);
	}
	
	/**
	 * 数据初始化加载
	 */
	private void loadData(){
		String serverIp = "http://"+ ip +":8080";
		VideoList listItem = new VideoList(serverIp);
		
		Thread thread = new Thread(listItem);
		thread.start();
		
	}
	
	/**
	 *绑定事件 
	 */
	private void bindClickListener() {
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				int flashback = videos.size() - position - 1;
				Intent intent = new Intent(context, DetailActivity.class);
				Integer ids = Integer.parseInt(videos.get(flashback).getId().toString());
				intent.putExtra("videoid", ids);
				startActivity(intent);
			}
		});
	}
	
	private View.OnClickListener onClickListenerView = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.main_bottom_download:
				Intent intent1 = new Intent(context, MyVideoActivity.class);
				startActivity(intent1);
				break;
//			case R.id.return_btn:
//				Intent intent = new Intent(context, MainActivity.class);
//				startActivity(intent);
//				finish();
//				break;	
			
			}	
		}
	};
	
	private Handler handleVideoList = new Handler(){
		public void handleMessage(android.os.Message msg) {
			
			videos = (ArrayList<Video>)msg.obj;
			if (videos == null) {
				Toast.makeText(VideoCenterActivity.this, "对不起，最新视频还没公布", Toast.LENGTH_SHORT).show();
			}else{
				pd.setMessage("数据已获取,界面绑定中...");
				
				adapter = new VideoAdapter(videos, VideoCenterActivity.this);
				listView.setAdapter(adapter);
				pd.setMessage("数据获取中,请稍候...");
				hideProgressDialog();
				
			}	
		};
	};
	
	class VideoList implements Runnable{
		List<Video> videoList = null;
		private String serverIp = "";
			
	    public VideoList(String serverIp){
	    	this.serverIp = serverIp;
			showProgressDialog();
		}
	    
		@Override
		public void run() {
			videoManager = new AdviodManager();
			try{
				videoList =	videoManager.getVideosByAll(serverIp, Integer.parseInt(classId.toString()));
		//		videoList =	videoManager.getVideosByAll(serverIp, 2);
			} catch(Exception e){
			}
//			Message message = new Message();
			Message message = Message.obtain();
			message.obj = videoList;
			handleVideoList.sendMessage(message);
		}
	}
	
	class VideoAdapter extends BaseAdapter {
		private Context context = null;
		private List<Video> list = null;

		public VideoAdapter(Context context) {
			this.context = context;
			inflater = LayoutInflater.from(context);
		}
		
		public VideoAdapter(List<Video> list, Context context){
			this.list = list;
			this.context = context;
			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			ViewHolder holder = null;
			
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.videocenter_list_item,null);
				
				holder.videoImage = (ImageView) convertView.findViewById(R.id.image);
				holder.name = (TextView) convertView.findViewById(R.id.videoName);
				holder.category = (TextView) convertView.findViewById(R.id.videoCategory);
				holder.attention = (TextView) convertView.findViewById(R.id.lectuer);
				
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
		
			int flashback = list.size() - position - 1;
			String serverIp = "http://"+ ip +":8080";
			String imageUrl = serverIp + list.get(flashback).getPic();
			
			LoadImage(holder.videoImage,imageUrl);
			
			holder.name.setText(list.get(flashback).getName());
			holder.category.setText("分类 ：" + list.get(flashback).getCategoryName());
			holder.attention.setText("主讲人：" + list.get(flashback).getLectuer());
			
			
			return convertView;
		}

	}
	
   private void LoadImage(ImageView img, String path) {
        //异步加载图片资源
        AsyncTaskImageLoad async=new AsyncTaskImageLoad(img);
        //执行异步加载，并把图片的路径传送过去
        async.execute(path);
	        
	 } 
	
	public final class ViewHolder {
		public ImageView videoImage;
		public TextView name;
		public TextView category;
		public TextView attention;
	}
	
}

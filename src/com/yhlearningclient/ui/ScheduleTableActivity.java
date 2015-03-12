package com.yhlearningclient.ui;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
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
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.Toast;

import com.example.yhlearningclient.R;
import com.yhlearningclient.biz.AdviodManager;
import com.yhlearningclient.model.CoursePlan;
import com.yhlearningclient.task.DownFileTask;
import com.yhlearningclient.utils.FileUtil;
import com.yhlearningclient.utils.Tool;

/**
 *进度表
 */
public class ScheduleTableActivity extends Activity {

	private List<CoursePlan> coursePlans = null;
	private Context context = null;
	private String fileName = "";
	private Button main_bottom_download = null;
	private final String pathName = "myCoursePlan";
	private SharedPreferences sharedPreferences;
	ProgressDialog pd = null;
	GridView gridview = null;
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
	
	/**
	 * 隐藏
	 */
	private void hideProgressDialog(){
		if (pd!=null) pd.dismiss();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		
		setContentView(R.layout.tab_schedule_table);
		
		gridview = (GridView) findViewById(R.id.gridview1);
		context = this;
		main_bottom_download = (Button) this.findViewById(R.id.main_bottom_download1);
		main_bottom_download.setOnClickListener(onClickListenerView);
		
		sharedPreferences = getSharedPreferences("serverIpObj", MODE_PRIVATE);
		ip = sharedPreferences.getString("serverIP", null);
		
		try{
			loadData();
		} catch(Exception ex){
			Toast.makeText(this, "网络连接出现问题，请检查！", Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	protected void onDestroy() {
		hideProgressDialog();
		super.onDestroy();
	}
	
	/**
	 * 数据初始化加载
	 */
	private void loadData(){
		String serverIp = "http://"+ ip +":8080";
		CourseTableList listItem = new CourseTableList(serverIp);
		
		Thread thread = new Thread(listItem);
		thread.start();
		
	}

	/**
	 * 适配器
	 * @param lstImageItem
	 * @return
	 */
	private SimpleAdapter ImageAndTextListAdapter(ArrayList<HashMap<String, Object>> lstImageItem) {
		// 生成适配器的ImageItem <====> 动态数组的元素，两者一一对应
			SimpleAdapter saImageItems = new SimpleAdapter(this, // 没什么解释
					lstImageItem,// 数据来源
					R.layout.image_item,// night_item的XML实现
	
					// 动态数组与ImageItem对应的子项
					new String[] { "ItemImage", "ItemText" },
	
					// ImageItem的XML文件里面的一个ImageView,两个TextView ID
					new int[] { R.id.ItemImage, R.id.ItemText });
			
			saImageItems.setViewBinder(new ViewBinder() { 
		            
		            public boolean setViewValue(View view, Object data, String textRepresentation) { 
		                //判断是否为我们要处理的对象  
		                if (view instanceof ImageView)  {
		                	if(data != null){       
                                   if(data instanceof Bitmap )
                                     {
                                             view.setVisibility(View.VISIBLE);
                                             ImageView iv = (ImageView) view;
                                             iv.setImageBitmap((Bitmap) data);
                                     }
                                     else 
                                     {
                                             view.setVisibility(View.VISIBLE);
                                             ((ImageView) view).setImageResource((Integer) data);
                                     }
                                }
                                else
                                {
                                        view.setVisibility(View.GONE);
                                }
                                return true;
                        }
                        else
                        {
                                return false;
                        }
		            }  
		        });
		return saImageItems;
	}
	
	private View.OnClickListener onClickListenerView = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.main_bottom_download1:
				Intent intent3 = new Intent(context, MyCourselistActivity.class);
				startActivity(intent3);
				break;
			}	
		}
	};

	// 当AdapterView被单击(触摸屏或者键盘)，则返回的Item单击事件
	class ItemClickListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> arg0,// The AdapterView where the
													// click happened
				View arg1,// The view within the AdapterView that was clicked
				int arg2,// The position of the view in the adapter
				long arg3// The row id of the item that was clicked
		) {
			
			String fileUrl = "";
			String serverIp = "http://"+ ip +":8080";
			try {
				Log.i("CourseTableActivity", "url=="+ coursePlans.get(arg2).getImageUrl());
				
				fileName = getDownLoadFileName(coursePlans.get(arg2).getImageUrl());
				fileUrl = URLEncoder.encode(fileName,"UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			
			String url = serverIp + "/uploadFile/file/" + fileUrl;
			
			downloadVideos(url);
		}
	}
	
	private void intentForward(final String fileName) {
		Intent intent = new Intent();
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		File file = new File(fileName);
		Uri path = Uri.fromFile(file);
		intent.setDataAndType(path,"application/pdf");
		startActivity(intent);
	};
	
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			Tool.ShowMessage(context, msg.obj.toString());
			final String fileName = msg.obj.toString();
			String extName = FileUtil.getExtName(fileName);
			if (extName.equalsIgnoreCase(".pdf") ) {
				intentForward(fileName);
			}else{
				Tool.ShowMessage(context, "非pdf格式");
			}
			
			intentForward(fileName);
		}

	};
	
	
	private Handler handleCourses= new Handler(){
		public void handleMessage(android.os.Message msg) {
		
			coursePlans = (ArrayList<CoursePlan>)msg.obj;
			if (coursePlans == null) {
				Toast.makeText(ScheduleTableActivity.this, "对不起，这学期课程进度表还没公布", Toast.LENGTH_SHORT).show();
			}else{
				pd.setMessage("数据已获取,界面绑定中...");
				
				ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
				for(int i = 0; i < coursePlans.size(); i ++){
					CoursePlan coursePlan = coursePlans.get(i);
					HashMap<String, Object> map = new HashMap<String, Object>();
					String url = coursePlan.getImageUrl();
					
					Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.kechejidu);
					
					map.put("ItemImage", bitmap);
					map.put("ItemText", coursePlan.getName());
					lstImageItem.add(map);
				}
				
				final SimpleAdapter saImageItems = ImageAndTextListAdapter(lstImageItem); 
				// 添加并且显示
				gridview.setAdapter(saImageItems);
				// 添加消息处理
				gridview.setOnItemClickListener(new ItemClickListener());
				
				pd.setMessage("数据获取中,请稍候...");
				hideProgressDialog();
			}	
		};
	};
	
 /**
  * 取出课程表数据
 */
 class CourseTableList implements Runnable{
	    
	   List<CoursePlan> coursePlanList = null;
	   private String serverIp = "";
		
	    public CourseTableList(String serverIp){
	    	this.serverIp = serverIp;
			showProgressDialog();
		}
		
	    @Override
		public void run() {
			AdviodManager manager = new AdviodManager();
			try {
				coursePlanList = manager.getPlanByAll(this.serverIp);
			} catch (Exception e) {
			}
			Message message = new Message();
			message.obj = coursePlanList;
			handleCourses.sendMessage(message);
		}
	}
	
	
	//得到当前下载的文件名
		private String getDownLoadFileName(String urls){
			if (urls != null){
				String url = urls;
				int lastdotIndex = url.lastIndexOf("/");
				String fileName = url.substring(lastdotIndex+1, url.length());
				return fileName;
			}else{
				return "";
			}
		}
		
		private void downloadVideos(final String url){
			//final String fileName = getDownLoadFileName(url);
			Tool.ShowMessage(context, fileName);
			if(FileUtil.isExists("/sdcard/myCoursePlan/" +fileName)){
			     AlertDialog.Builder builder = new AlertDialog.Builder(context);
			     builder.setTitle("提示").setMessage("该视频文件已经存在于SDCard中，确定要覆盖下载吗？")
			     .setPositiveButton("确定", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						DownFileTask task = new DownFileTask(context,handler,fileName,"myCoursePlan");
						task.execute(url);
					}
			    	 
			     }).setNegativeButton("取消", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						String extName = FileUtil.getExtName(fileName);
						if (extName.equalsIgnoreCase(".pdf") ) {
							intentForward("/sdcard/" +pathName +"/" + fileName);
						} else {
							Tool.ShowMessage(context, "非pdf格式");
						}
						
					}
			    	 
			     }).create().show();
			}else{
				
				DownFileTask task = new DownFileTask(context,handler,fileName, "myCoursePlan");
				task.execute(url);
				
			}
			
		}
}
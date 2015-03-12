package com.yhlearningclient.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.yhlearningclient.model.CoursewareNode;

/**
 * 课程分类
 */
public class BookCategoryActivity extends Activity {

	private List<CoursewareNode> coursePlans = null;
	private Context context = null;
	private String fileName = "";
	private Button main_bottom_download = null;
	private final String pathName = "myCourseTable";
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		
		setContentView(R.layout.tab_course_category);
		
		gridview = (GridView) findViewById(R.id.gridview1);
		context = this;
		
		sharedPreferences = getSharedPreferences("serverIpObj", MODE_PRIVATE);
		ip = sharedPreferences.getString("serverIP", null);
		
		loadData();
		
	}
	
	
	/**
	 * 数据初始化加载
	 */
	private void loadData(){
		String serverIp = "http://"+ ip +":8080";
		CourseCategoryList listItem = null;
		
		try{
			listItem = new CourseCategoryList(serverIp);
		} catch(Exception ex){
			Toast.makeText(BookCategoryActivity.this, "网络连接出现问题，请检查！", Toast.LENGTH_LONG).show();
		}
		
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
					R.layout.category_item,// night_item的XML实现
	
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
	
//	private View.OnClickListener onClickListenerView = new View.OnClickListener() {
//		
//		@Override
//		public void onClick(View v) {
//			switch (v.getId()) {
//			case R.id.main_bottom_download1:
//				Intent intent3 = new Intent(context, MyCourseTableActivity.class);
//				startActivity(intent3);
//				break;
//			}	
//		}
//	};

	// 当AdapterView被单击(触摸屏或者键盘)，则返回的Item单击事件
	class ItemClickListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> arg0,// The AdapterView where the
													// click happened
				View arg1,// The view within the AdapterView that was clicked
				int arg2,// The position of the view in the adapter
				long arg3// The row id of the item that was clicked
		) {
			Intent intent = new Intent(context, TeachingCategoryActivity.class);
			Integer ids = Integer.parseInt(coursePlans.get(arg2).getId().toString());
			intent.putExtra("categoryId", ids);
			startActivity(intent);
		}
	}
	
	private Handler handleCourses= new Handler(){
		public void handleMessage(android.os.Message msg) {
		
			coursePlans = (ArrayList<CoursewareNode>)msg.obj;
			if (coursePlans == null) {
				Toast.makeText(BookCategoryActivity.this, "对不起，这学期课程表还没公布", Toast.LENGTH_SHORT).show();
			}else{
				pd.setMessage("数据已获取,界面绑定中...");
				
				ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
				for(int i = 0; i < coursePlans.size(); i ++){
					CoursewareNode coursePlan = coursePlans.get(i);
					HashMap<String, Object> map = new HashMap<String, Object>();
					
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
  * 取出分类数据
 */
 class CourseCategoryList implements Runnable{
	    
	   List<CoursewareNode> categoryList = null;
	   private String serverIp = "";
		
	    public CourseCategoryList(String serverIp){
	    	this.serverIp = serverIp;
			showProgressDialog();
		}
		
	    @Override
		public void run() {
			AdviodManager manager = new AdviodManager();
				try {
					categoryList = manager.getCategoryByAll(this.serverIp);
				} catch (Exception e) {
				}
				Message message = new Message();
				message.obj = categoryList;
				handleCourses.sendMessage(message);
		
		}
	}
		
}

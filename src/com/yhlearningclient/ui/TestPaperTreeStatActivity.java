package com.yhlearningclient.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.example.yhlearningclient.R;
import com.yhlearningclient.biz.TestPaperManager;
import com.yhlearningclient.factory.UTestPaperFactory;
import com.yhlearningclient.model.TestPaper;
import com.yhlearningclient.model.TestPaperCategory;
import com.yhlearningclient.model.UserTestPaper;
import com.yhlearningclient.tree.TreeElement;
import com.yhlearningclient.tree.TreeView;
import com.yhlearningclient.tree.TreeView.LastLevelItemClickListener;
import com.yhlearningclient.tree.TreeViewAdapter;
import com.yhlearningclient.utils.UploadUtil;
import com.yhlearningclient.utils.UploadUtil.OnUploadProcessListener;

/**
 * 试卷分类
 */
public class TestPaperTreeStatActivity extends Activity implements OnUploadProcessListener{

	private Context context = null;
	private SharedPreferences sharedPreferences;
	ProgressDialog pd = null;
	GridView gridview = null;
	String ip = "";
	private TreeView lvCategorys; // 列表
	private List<TreeElement> lvCategoryData = null;
	private Button inform_refresh;
	Long classId;
	private String[] is = { "查看统计结果", "取消" };
	private UTestPaperFactory uTestPaperFactory = null;
	private int recordCount = 1;
	/**
	 * 上传初始化
	 */
	private static final int UPLOAD_INIT_PROCESS = 4;
	/**
	 * 上传中
	 */
	private static final int UPLOAD_IN_PROCESS = 5;
	
	/**
	 * 去上传文件
	 */
	protected static final int TO_UPLOAD_FILE = 1;  
	/**
	 * 上传文件响应
	 */
	protected static final int UPLOAD_FILE_DONE = 2;  //
	
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
		
		
		setContentView(R.layout.test_paper_category);
		
		context = this;
		lvCategorys = (TreeView) findViewById(R.id.frame_listview_address);
		
		sharedPreferences = getSharedPreferences("serverIpObj", MODE_PRIVATE);
		ip = sharedPreferences.getString("serverIP", "");
		classId = sharedPreferences.getLong("classId", 0);
		
		inform_refresh = (Button) findViewById(R.id.inform_refresh);
		
		inform_refresh.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				
				refreshData();
			}
		});
		
		loadData();
		
	}
	
	
	/**
	 * 数据初始化加载
	 */
	private void loadData(){
		String serverIp = "http://"+ ip +":8082";
		TestPaperCategoryList listItem = null;
		
		try{
			listItem = new TestPaperCategoryList(serverIp);
		} catch(Exception ex){
			Toast.makeText(TestPaperTreeStatActivity.this, "网络连接出现问题，请检查！", Toast.LENGTH_LONG).show();
		}
		
		Thread thread = new Thread(listItem);
		thread.start();
		
	}

	
	/**
	 * 数据初始化加载
	 */
	private void refreshData(){
		String serverIp = "http://"+ ip +":8082";
		TestPaperCategoryRemoteList listItem = null;
		
		try{
			listItem = new TestPaperCategoryRemoteList(serverIp);
		} catch(Exception ex){
			Toast.makeText(TestPaperTreeStatActivity.this, "网络连接出现问题，请检查！", Toast.LENGTH_LONG).show();
		}
		
		Thread thread = new Thread(listItem);
		thread.start();
		
	}

	LastLevelItemClickListener itemClickCallBack = new LastLevelItemClickListener() {
		// 创建节点点击事件监听
		@Override
		public void onLastLevelItemClick(final int position, final TreeViewAdapter adapter) {
			
			AlertDialog.Builder builder = new AlertDialog.Builder(TestPaperTreeStatActivity.this);
			builder.setTitle("请选择操作");
			builder.setItems(is, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case 0:
						TreeElement element = (TreeElement) adapter.getItem(position);
						Integer ids = Integer.parseInt(element.getId().toString());
						
						boolean isExist = UTestPaperFactory.createUTestPaper(TestPaperTreeStatActivity.this).profileExist(ids);
						
						if(isExist){
							Intent intent = new Intent(context, UserTestPaperResult.class);
							intent.putExtra("categoryId", ids);
							startActivity(intent);
						}else{
							Toast.makeText(TestPaperTreeStatActivity.this, "请下载学员作业资料", Toast.LENGTH_SHORT).show();
						}
						break;
					case 1:
						break;
					default:
						break;
					}
				}
			}).create().show();
		}
	};
	
	private Handler handleCourses= new Handler(){
		public void handleMessage(android.os.Message msg) {
		
			lvCategoryData = (ArrayList<TreeElement>)msg.obj;
			if (lvCategoryData == null) {
				Toast.makeText(TestPaperTreeStatActivity.this, "对不起，无数据", Toast.LENGTH_SHORT).show();
			}else{
				pd.setMessage("数据已获取,界面绑定中...");
				
				// 初始化数据
				lvCategorys.initData(context, lvCategoryData);
				// 设置节点点击事件监听
				lvCategorys.setLastLevelItemClickCallBack(itemClickCallBack);
				
				pd.setMessage("数据获取中,请稍候...");
				hideProgressDialog();
			}	
		};
	};
	
 /**
  * 取出分类数据
 */
 class TestPaperCategoryList implements Runnable{
	    
	   List<TreeElement> lvCategoryData = new ArrayList<TreeElement>();
	   private String serverIp = "";
		
	    public TestPaperCategoryList(String serverIp){
	    	this.serverIp = serverIp;
			showProgressDialog();
		}
		
	    @Override
		public void run() {
	    	TestPaperManager manager = new TestPaperManager(TestPaperTreeStatActivity.this);
				try {
					//List<TestPaperCategory> categoryList = manager.getAllTestPaperCategoryList(this.serverIp);
					List<TestPaperCategory> categoryList = manager.getAllTestPaperCategoryDB();
					
					for(TestPaperCategory rootItem : categoryList){
						
						TreeElement root = new TreeElement();
						root.setId(String.valueOf(rootItem.get_id()));
						root.setLevel(1);
						root.setTitle(rootItem.getName());
						root.setFold(false);
						root.setHasChild(true);
						root.setHasParent(false);
						root.setParentId(null);
						lvCategoryData.add(root);
						
				//		List<TestPaper> childList = manager.getAllTestPaperList(this.serverIp, rootItem.get_id(), 1, 12000);
						List<TestPaper> childList = manager.getAllTestPaperDB(rootItem.get_id());
						
						for(TestPaper childItem : childList){
							
							TreeElement childData = new TreeElement();
							childData.setId(String.valueOf(childItem.get_id()));
							childData.setLevel(2);
							childData.setTitle(childItem.getName());
							childData.setFold(false);
							childData.setHasChild(false);
							childData.setHasParent(true);
							childData.setParentId(String.valueOf(childItem.getParent_id()));
							lvCategoryData.add(childData);
							
						}
						
					}
					
					
				} catch (Exception e) {
				}
				Message message = new Message();
				message.obj = lvCategoryData;
				handleCourses.sendMessage(message);
		
		}
	}
 
 
 /**
  * 取出分类数据
 */
 class TestPaperCategoryRemoteList implements Runnable{
	    
	   List<TreeElement> lvCategoryData = new ArrayList<TreeElement>();
	   private String serverIp = "";
		
	    public TestPaperCategoryRemoteList(String serverIp){
	    	this.serverIp = serverIp;
			showProgressDialog();
		}
		
	    @Override
		public void run() {
	    	TestPaperManager manager = new TestPaperManager(TestPaperTreeStatActivity.this);
				try {
					List<TestPaperCategory> categoryList = manager.getAllTestPaperCategoryList(this.serverIp);
					//List<TestPaperCategory> categoryList = manager.getAllTestPaperCategoryDB();
					
					for(TestPaperCategory rootItem : categoryList){
						
						TreeElement root = new TreeElement();
						root.setId(String.valueOf(rootItem.get_id()));
						root.setLevel(1);
						root.setTitle(rootItem.getName());
						root.setFold(false);
						root.setHasChild(true);
						root.setHasParent(false);
						root.setParentId(null);
						lvCategoryData.add(root);
						
						List<TestPaper> childList = manager.getAllTestPaperList(this.serverIp, rootItem.get_id(), 1, 12000,
								Integer.parseInt(classId.toString()));
				//		List<TestPaper> childList = manager.getAllTestPaperDB(rootItem.get_id());
						
						for(TestPaper childItem : childList){
							
							TreeElement childData = new TreeElement();
							childData.setId(String.valueOf(childItem.get_id()));
							childData.setLevel(2);
							childData.setTitle(childItem.getName());
							childData.setFold(false);
							childData.setHasChild(false);
							childData.setHasParent(true);
							childData.setParentId(String.valueOf(childItem.getParent_id()));
							lvCategoryData.add(childData);
							
						}
						
					}
					
					
				} catch (Exception e) {
				}
				Message message = new Message();
				message.obj = lvCategoryData;
				handleCourses.sendMessage(message);
		}
	}
 
	private Handler handleSubmit = new Handler(){
		public void handleMessage(android.os.Message msg) {
			hideProgressDialog();
			boolean result = (Boolean)msg.obj;
			if (result){
				
				Toast.makeText(context, "已成功提交到服务器",Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(context, "上传服务器失败！！",Toast.LENGTH_SHORT).show();
			}
		};
	};
 
	class SubmitUserResult implements Runnable{
		private UserTestPaper userTestPaper=null;
		private String serverIp = "";
		
		public SubmitUserResult(String serverIp, UserTestPaper userTestPaper){
			this.serverIp = serverIp;
			this.userTestPaper = userTestPaper;
		}

		@Override
		public void run() {
			boolean result = false;
			try {
				result = uTestPaperFactory.createUTestPaper(context).saveUserTestPaper(serverIp, userTestPaper);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Message msg = new Message();
			msg.obj = result;
			handleSubmit.sendMessage(msg);
			
		}
		
	}
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case TO_UPLOAD_FILE:
			//	toUploadFile();
				break;
			
			case UPLOAD_INIT_PROCESS:
			//	Toast.makeText(context, msg.arg1,Toast.LENGTH_SHORT).show();
			//	progressBar.setMax(msg.arg1);
				break;
			case UPLOAD_IN_PROCESS:
			//	Toast.makeText(context, msg.arg1,Toast.LENGTH_SHORT).show();
				break;
			case UPLOAD_FILE_DONE:
				String result = "上传成功！耗时："+UploadUtil.getRequestTime()+"秒";
				Toast.makeText(context, result,Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
		
	};

	@Override
	public void onUploadDone(int responseCode, String message) {
		Message msg = Message.obtain();
		msg.what = UPLOAD_FILE_DONE;
		msg.arg1 = responseCode;
		msg.obj = message;
		handler.sendMessage(msg);
		
	}

	@Override
	public void onUploadProcess(int uploadSize) {
		Message msg = Message.obtain();
		msg.what = UPLOAD_IN_PROCESS;
		msg.arg1 = uploadSize;
		handler.sendMessage(msg );
		
	}

	@Override
	public void initUpload(int fileSize) {
		Message msg = Message.obtain();
		msg.what = UPLOAD_INIT_PROCESS;
		msg.arg1 = fileSize;
		handler.sendMessage(msg );
		
	}
		
}

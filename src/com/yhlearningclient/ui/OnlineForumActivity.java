package com.yhlearningclient.ui;

import java.text.MessageFormat;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yhlearningclient.R;
import com.yhlearningclient.biz.BookManager;
import com.yhlearningclient.model.OnlineForum;
import com.yhlearningclient.utils.PaginateResult;

/**
 *在线交流
 */
public class OnlineForumActivity extends Activity {

	/**
	 *业务对象 
	 */
	private BookManager bookManager;
	
	/**
	 * 通知列表
	 */
	private List<OnlineForum> forums = new ArrayList<OnlineForum>();
	ProgressDialog pd = null;
	ImageView inform_unread_img;
	ListView inform_list;
	Button inform_back, inform_refresh, addInform;
	int last_message_id;
	MyAdapter adapter;
	private SharedPreferences sharedPreferences;
	String ip = "";
	Long classId;
	// 定义关于ListView相关常量
	private static final int PAGESIZE = 10; // 每次取几条记录
	private int pageIndex = 0; // 用于保存当前是第几页,0代表第一页
	int totalCount = 0;
	
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
		setContentView(R.layout.activity_fourms);
		
		sharedPreferences = getSharedPreferences("serverIpObj", MODE_PRIVATE);
		ip = sharedPreferences.getString("serverIP", null);
		classId = sharedPreferences.getLong("classId", 0);
		

		
		initView();
		loadData();
		bindClickListener();
	}
	
	
	public void initView() {
		inform_refresh = (Button) findViewById(R.id.inform_refresh);
		inform_list = (ListView) findViewById(R.id.message_list);
		inform_unread_img = (ImageView) findViewById(R.id.inform_unread_img);
		addInform = (Button) findViewById(R.id.add_info);
	}
	
	/**
	 * 数据初始化加载
	 */
	private void loadData(){
		String serverIp = "http://"+ ip +":8080";
		LoadForumList listItem = new LoadForumList(serverIp, classId);
		
		Thread thread = new Thread(listItem);
		thread.start();
		
	}
	
	/**
	 * 刷新方法
	 */
	private void RefreshData(){
		String serverIp = "http://"+ ip +":8080";
//		LoadNewMessageList listItem = new LoadNewMessageList(serverIp);
//		
//		Thread thread = new Thread(listItem);
//		thread.start();
	}
	
	
	/**
	 *绑定事件 
	 */
	private void bindClickListener() {
		
		inform_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				if (id != -1) {
					
					Log.i("position", "position=="+position);
					Log.i("forums.size()", "forums.size()=="+forums.size());
					
					int flashback = position - 1;
					
					OnlineForum onlineForum = forums.get(flashback);
		
					Intent intent = new Intent();
					intent.setClass(OnlineForumActivity.this, ForumShowActivity.class);
					intent.putExtra("forum_id", onlineForum.getId().toString());
					intent.putExtra("name", onlineForum.getName());
					intent.putExtra("content", onlineForum.getQuestion());
					startActivity(intent);
				}
			}
		});

		
		/**
		 * 刷新信息
		 */
		inform_refresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				RefreshData();
				Toast.makeText(OnlineForumActivity.this, "刷新成功", Toast.LENGTH_SHORT).show();
			}
		});
		
		/**
		 * 添加
		 */
		addInform.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Intent intent2 = new Intent(OnlineForumActivity.this, addForumActivity.class);
				startActivity(intent2);

			}
		});

	}
	
	private Handler handleSysMessages = new Handler(){
		public void handleMessage(android.os.Message msg) {
			
			forums = (ArrayList<OnlineForum>)msg.obj;
			if (forums == null) {
				Toast.makeText(OnlineForumActivity.this, "对不起, 在线交流还没公布", Toast.LENGTH_SHORT).show();
			}else{
				pd.setMessage("数据已获取,界面绑定中...");
				adapter = new MyAdapter(forums, OnlineForumActivity.this);
				inform_list.setAdapter(adapter);
				pd.setMessage("数据获取中,请稍候...");
				hideProgressDialog();
				
			}	
		};
	};
	
	private Handler handleNewSysMessages = new Handler(){
		public void handleMessage(android.os.Message msg) {
			
			List<OnlineForum> list = (ArrayList<OnlineForum>)msg.obj;
			if (list == null) {
				Toast.makeText(OnlineForumActivity.this, "对不起，无最新教学通知", Toast.LENGTH_SHORT).show();
			}else{
				pd.setMessage("数据已获取,界面绑定中...");
				
				adapter = new MyAdapter(list, OnlineForumActivity.this);
				inform_list.setAdapter(adapter);
				pd.setMessage("数据获取中,请稍候...");
				hideProgressDialog();
			}	
		};
	};
	
	class LoadForumList implements Runnable{
		List<OnlineForum> forums = null;
		private String serverIp = "";
		private Long classId;
			
	    public LoadForumList(String serverIp,Long classId){
	    	this.serverIp = serverIp;
	    	this.classId = classId;
			showProgressDialog();
		}
	    
		@Override
		public void run() {
			bookManager = new BookManager();
			try{
				PaginateResult paginateResult = bookManager.getOnlineForumByPage(serverIp, pageIndex + 1, PAGESIZE, classId);
				forums = paginateResult.getList();
				totalCount = paginateResult.getRecordCount();
			} catch(Exception e){
			}
			Message message = new Message();
			message.obj = forums;
			handleSysMessages.sendMessage(message);
		}
	}
	
	
//	class LoadNewMessageList implements Runnable{
//		List<SysMessage> messages = null;
//		
//		private String serverIp = "";
//		
//	    public LoadNewMessageList(String serverIp){
//	    	this.serverIp = serverIp;
//			showProgressDialog();
//		}
//	    
//		@Override
//		public void run() {
//			try{
//				messages = userManager.getRefreshSysMessageList(serverIp);
//			} catch(Exception e){
//			}
//			Message message = new Message();
//			message.obj = messages;
//			handleNewSysMessages.sendMessage(message);
//		}
//	}
	
	/*用于获取用于显示的分页信息*/
	private String getPagerInfo(){
		String pagerInfo = "第{0}页 ,共{1}页,共{2}记录";
		int totalPageCount = getByPageCount(PAGESIZE);
		return MessageFormat.format(pagerInfo, pageIndex+1, totalPageCount, totalCount);
	}
	
	
	/**
	 * 页数
	 * @param pageSize
	 * @return
	 */
	public int getByPageCount(int pageSize){
		int pageCount=1; //总页数
		if (totalCount % pageSize==0){
			pageCount = totalCount/pageSize;
		}else{
			pageCount = (totalCount/pageSize)+1;
		}
		return pageCount;
	}
	
	//用于分页存取后台数据并绑定显示在ListView上面
//	private void BindListView() {
//		List<Product> _products = productManager.getProdcutByPager(pageIndex, PAGESIZE," 1=1 ");
//		setTitle(getPagerInfo());
//		adapter = new MyAdapter(context,_products);
//		setListAdapter(adapter);
//		lvProduct = getListView();
//		lvProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				if (id == -2) // 说明点击的是更多
//				{
//					//adapter.more(R.layout.productitem2);
//					// step 2:获取更多的数据
//					List<Product> moreProducts = productManager.getProdcutByPager(
//							pageIndex, PAGESIZE," 1=1 ");
//					if (moreProducts != null) {
//						// step 3:添加到现有的集合中
//						products.addAll(moreProducts);
//					}
//					int res = adapter.isBatchManager()?R.layout.productitem2:R.layout.productitem;
//					adapter = new MyAdapter(context,res);
//					setListAdapter(adapter);
//				} else if (id != -1) {
//					ViewHolder vHollder = (ViewHolder) view.getTag();
//					// 在每次获取点击的item时将对于的checkbox状态改变，同时修改map的值。
//					if (vHollder.cBox != null) {
//						vHollder.cBox.toggle();
//						isSelected.put(position - 1, vHollder.cBox.isChecked());
//					}
//				}
//
//			}
//		});
//	}
	
	/**
	 *适配器
	 */
	class MyAdapter extends BaseAdapter {

		private LayoutInflater inflater = null;
		Context context;
		private List<OnlineForum> onlineForums = null;
		private int item_layout_res = R.layout.activity_forum_list_item;
		
		public MyAdapter(Context context) {
			this.context = context;
			inflater = LayoutInflater.from(context);
		}
		
		public MyAdapter(List<OnlineForum> onlineForums, Context context){
			this.onlineForums = onlineForums;
			this.context = context;
			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return onlineForums.size() + 2;
		}

		@Override
		public Object getItem(int position) {
			return onlineForums.get(position - 1);
		}

		@Override
		public long getItemId(int position) {
			if (position == 0)// 选中第一项
			{
				return -1;// 代表点击的是第一项
			} else if (position > 0 && (position < this.getCount() - 1)) {
				return onlineForums.get(position - 1).getId();// 如果用户选中了中间项
			} else {
				return -2;// 表示用户选中最后一项
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// 说明是第一项
			if (position == 0) {
				convertView = inflater.inflate(R.layout.addproductitem, null);
				return convertView;
			}
			
			// 说明是最后一项
			if (position == this.getCount() - 1 ) {
				convertView = inflater.inflate(R.layout.moreitemsview, null);
				TextView txtPagerInfo = (TextView) convertView.findViewById(R.id.txtPagerInfo);
				txtPagerInfo.setText(getPagerInfo());
				Button btnFirst = (Button) convertView.findViewById(R.id.btnFirst);
				btnFirst.setOnClickListener(new View.OnClickListener() {
					
					public void onClick(View v) {
						pageIndex=0;
						loadData();
						Toast.makeText(OnlineForumActivity.this, "第"+(pageIndex+1)+"页", Toast.LENGTH_SHORT).show();
					}
				});
				
				
				Button btnPrev = (Button) convertView.findViewById(R.id.btnPrev);
				btnPrev.setOnClickListener(new View.OnClickListener() {
					
					public void onClick(View v) {
						if (pageIndex>0){
							 pageIndex--;
							 loadData();
						     Toast.makeText(OnlineForumActivity.this, "第"+(pageIndex+1)+"页", Toast.LENGTH_SHORT).show();
						}else{
							 Toast.makeText(OnlineForumActivity.this, "已经是第一页了", Toast.LENGTH_SHORT).show();
						}
						
						// setTitle(getPagerInfo());
					}
				});
				
				Button btnNext = (Button) convertView.findViewById(R.id.btnNext);
				btnNext.setOnClickListener(new View.OnClickListener() {
					
					public void onClick(View v) {
						int totalPageCount = getByPageCount(PAGESIZE);
						if (pageIndex < totalPageCount-1){
						   pageIndex++;
						   loadData();
						   Toast.makeText(OnlineForumActivity.this, "第"+(pageIndex+1)+"页", Toast.LENGTH_SHORT).show();
						}else{
							 Toast.makeText(OnlineForumActivity.this, "已经是第一页了", Toast.LENGTH_SHORT).show();
						}
					}
				});
				
				Button btnLast = (Button) convertView.findViewById(R.id.btnLast);
				btnLast.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						int totalPageCount = getByPageCount(PAGESIZE);
						pageIndex = totalPageCount-1;
						loadData();
						Toast.makeText(OnlineForumActivity.this, "第"+(pageIndex+1)+"页", Toast.LENGTH_SHORT).show();
					}
				});
				
				
				return convertView;
			}
			
			ViewHolder holder = null;
			
			if(convertView == null 
					|| convertView.findViewById(R.id.addproduct) != null 
					|| convertView.findViewById(R.id.linemore) != null){
				
				holder = new ViewHolder();
				convertView = inflater.inflate(this.item_layout_res, parent, false);
				
				holder.name = (TextView) convertView.findViewById(R.id.inform_list_item_title);
				holder.content = (TextView) convertView.findViewById(R.id.inform_list_item_content);
				holder.createdTime = (TextView) convertView.findViewById(R.id.inform_list_item_time);
				
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
				
			}
			
			int flashback = position - 1;
			holder.name.setText(onlineForums.get(flashback).getName());
			String content = onlineForums.get(flashback).getQuestion();
			if (content.length() > 50)
				content = content.substring(0, 50) + "...";

			holder.content.setText(content);
			holder.createdTime.setText(onlineForums.get(flashback).getCreatedTime());

			return convertView;
		}


	}
	
	public final class ViewHolder {
		public TextView name;
		public TextView content;
		public TextView createdTime;
	}

}

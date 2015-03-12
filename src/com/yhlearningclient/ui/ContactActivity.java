package com.yhlearningclient.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yhlearningclient.R;
import com.yhlearningclient.biz.GroupManager;
import com.yhlearningclient.biz.StudentManager;
import com.yhlearningclient.factory.UTestPaperFactory;
import com.yhlearningclient.model.Group;
import com.yhlearningclient.model.Student;

public class ContactActivity extends Activity {

	/**
	 * requestCode
	 */
	public final static int GROUP_TO_ADD_CONTACT = 0;// 由分组菜单项跳转到新建联系人界面
	public final static int CTREAT_TO_ADDNEW = 1; // 点击右上角新建跳转到新建联系人界面
	public final static int GROUPCONTACT_MOVETO = 2;// 全组成员移动跳转到组别选择
	public final static int CONTACT_MOVETO = 4;// 单人移动跳转到组别选择
	public final static int EDIT_CONTACT = 11;// 点击编辑跳转到修改联系人界面
	public final static int MARK_MOVE_TO = 5;// 点击标记移动..跳转到组别选择
	public final static int INFO = 6;// 点击查看详情

	private ExpandableListAdapter adapter;
	private ExpandableListView expandableListView;
	private Context context;
	private GroupManager groupManager;
	private StudentManager contactManager;

	private ImageView imgContacts;// 左上角切换到联系人列表的ImageView
	private ImageView imgCreate;// 右上角切换到新建联系人的ImageView
	private RelativeLayout rl_mark;

	private boolean[] isOpen;// 用于处理expandableListView箭头小图标拉伸的
	private int moveToGroupId = -1;// 移动操作的目标分组ID
	private int moveReGroupId;// 移动操作的原分组ID
	private int moveReContactId;// 移动操作的联系人ID

	private boolean isMarkedState = false;// 是否是标记状态
	private boolean isFirst = true;// 为了第一进入onResume而不执行其内的操作
	private List<Group> list;// 存放所有的group
	private SharedPreferences sharedPreferences;
	Long classId;

	private GridView bottomMenuGrid;// 屏幕下方主菜单的布局
	
	
	private String[] bottom_menu_itemName = { "工具箱", "全屏", "退出" };// 主菜单文字
	
	private int[] bottom_menu_itemSource = { R.drawable.menu_tool, // 主菜单图片
			R.drawable.full_screen, R.drawable.menu_exit };

	ProgressDialog pd = null;
	String ip = "";
	// 为MyExpandableListAdapter建立的数据源
	private List<String> groupItem = new ArrayList<String>();// 存放所有的组别名称
	private List<List<Student>> contacts = new ArrayList<List<Student>>();// 按组别存放contact
	private List<Integer> groupCount = new ArrayList<Integer>();// 存放各分组内contact的数量
	int testPaperId = 0;
	String userId = "";

	/**
	 * 提标框
	 */
	private void showProgressDialog() {
		if (pd == null) {
			pd = new ProgressDialog(this);
			pd.setTitle("系统提示");
			pd.setMessage("数据获取中,请稍候...");
		}
		pd.show();
	}

	/**
	 * 隐藏
	 */
	private void hideProgressDialog() {
		if (pd != null)
			pd.dismiss();
	}

	@Override
	protected void onDestroy() {
		hideProgressDialog();
		super.onDestroy();
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//是否全屏判断
		isMakeFull();
		
		setContentView(R.layout.activity_contacts);

		context = this;
		groupManager = new GroupManager(context);
		contactManager = new StudentManager(context);

		// 初始化标记状态，false:全部未标记；true:全部标记
		contactManager.initIsMark("false");

		getId();
		
		sharedPreferences = getSharedPreferences("serverIpObj", MODE_PRIVATE);
		ip = sharedPreferences.getString("serverIP", "");
		classId = sharedPreferences.getLong("classId", 0);
		userId = sharedPreferences.getString("user", "");
		
		if (getIntent().getIntExtra("categoryId", 0)!=0)
			testPaperId = getIntent().getIntExtra("categoryId", 0);

		expandableListView.setGroupIndicator(null);// 箭头小图标，不用自带的了
		expandableListView.setCacheColorHint(0);// 防止expandableListView点击变黑

		// 为expandableListView注册上下文菜单SETP.一
		expandableListView.setOnCreateContextMenuListener(this);

		// 绑定数据，刷新界面
		bindView();
		// 头像选择监听

		// Group点击监听
		expandableListView.setOnGroupClickListener(new OnGroupClickListener() {
			public boolean onGroupClick(ExpandableListView mExpandableList,
					View arg1, final int groupPosition, long id) {
				return false;
			}

		});

		// Contact点击监听
		expandableListView.setOnChildClickListener(new OnChildClickListener() {
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {

				Student contact = contacts.get(groupPosition)
						.get(childPosition);
				if (isMarkedState) {

//					ImageView img = (ImageView) v.findViewById(R.id.imgChk);
//					if (img.getVisibility() == View.GONE) {
//						contactManager.initIsMarkById("true", contact.getId());
//						img.setVisibility(View.VISIBLE);
//					} else {
//						contactManager.initIsMarkById("false", contact.getId());
//
//						img.setVisibility(View.GONE);
//					}
				} else {
//					Intent intent = new Intent(context,
//							AddNewContactActivity.class);
//					Bundle bundle = new Bundle();
//					bundle.putInt("requestCode", INFO);
//					bundle.putInt("id", contact.getId());
//					intent.putExtras(bundle);
//					startActivity(intent);

					// Toast.makeText(context, contact.toString(),
					// Toast.LENGTH_LONG).show();
				}
				return false;
			}
		});

		// 处理expandableListView自定义箭头小图标被拉伸SETP.1
		expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

					public void onGroupCollapse(int groupPosition) {
						isOpen[groupPosition] = false;

					}
				});

		// SETP.2
		expandableListView
				.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

					public void onGroupExpand(int groupPosition) {
						isOpen[groupPosition] = true;

					}
				});

		// 切换到联系人视图
		imgContacts.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(context,AllContactShowActivity.class);
				intent.putExtra("categoryId", testPaperId);
				startActivity(intent);

			}
		});
		// 新建联系人
		imgCreate.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				String serverIp = "http://"+ ip +":8080";
				loadStudentList(serverIp, classId);
			}
		});

	}
	
	
	//用于接受loadTestPaperList线程结束后传来的列表，并加以显示
	Handler handlerStudent = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			List<Student> sList = (ArrayList<Student>)msg.obj;
			if (sList == null) {
				Toast.makeText(ContactActivity.this, "对不起, 无数据请下载。", Toast.LENGTH_SHORT).show();
			}else{
				pd.setMessage("数据已获取,界面绑定中...");
				bindView();
				pd.setMessage("数据获取中,请稍候...");
				hideProgressDialog();
			}	
			
		}
	};
	
	//创建新线程从本地试卷列表
	public void loadStudentList(final String serverIP, final Long classId) {
		
		showProgressDialog();
		Thread thread = new Thread() {
			@Override
			public void run() {
				List<Student> sList = contactManager.getAllStudentList(serverIP, classId);
				Message message = new Message();
				message.obj = sList;
				handlerStudent.sendMessage(message);
			}
		};
		
		thread.start();
		thread = null;

	}

	private void isMakeFull() {
		SharedPreferences	sp = getSharedPreferences("parameter", Context.MODE_PRIVATE);
		boolean isFull = sp.getBoolean("isFull", false);
		if(isFull){
			getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		}
	}

	private void getId() {
		rl_mark = (RelativeLayout) findViewById(R.id.rl_mark);
		imgContacts = (ImageView) findViewById(R.id.imgContacts);
		imgCreate = (ImageView) findViewById(R.id.imgCreate);
		expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
	}

	// 调用系统发送EMAIL
	public void sendEmail(String Email) {
		Uri uri = Uri.parse("mailto:" + Email);
		Intent it = new Intent(Intent.ACTION_SENDTO, uri);
		startActivity(it);

	}

	// 调用系统发短信
	public void sendMsg(String phone) {
		if (phone.equals("")) {

		} else {
			Uri uri = Uri.parse("smsto:" + phone);

			Intent it = new Intent(Intent.ACTION_SENDTO, uri);

			it.putExtra("sms_body", "");

			startActivity(it);
		}

	}

	// 调用系统打电话
	public void makeCall(String phone) {
		if (phone.equals("")) {

		} else {
			Intent intent = new Intent(Intent.ACTION_CALL);
			intent.setData(Uri.parse("tel:" + phone));
			startActivity(intent);
		}
	}

	private void bindView() {
		getData();
		if (contacts != null) {
			adapter = new MyExpandableListAdapter(groupCount, groupItem, contacts, isOpen, context);
			expandableListView.setAdapter(adapter);
		}
	}

	private void getData() {
		groupItem.clear();
		contacts.clear();
		groupCount.clear();
		list = groupManager.getAllGroup();
		Log.i("Group", "1111111111");

		for (Group p : list) {
			groupItem.add(p.getGroupName());
			groupCount.add(contactManager.getCountByGroupId(p.getGroupId(), testPaperId));
			contacts.add(contactManager.getContactByGroupId(p.getGroupId(), testPaperId));
		}

		isOpen = new boolean[groupManager.getGroupCount()];
	}

	// 创建上下文菜单内容 STEP.二
//	@Override
//	public void onCreateContextMenu(ContextMenu menu, View v,
//			ContextMenuInfo menuInfo) {
//		ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;
//		int type = ExpandableListView
//				.getPackedPositionType(info.packedPosition);
//		int groupPosition = ExpandableListView
//				.getPackedPositionGroup(info.packedPosition);
//		// int childPosition = ExpandableListView
//		// .getPackedPositionChild(info.packedPosition);
//
//		if (type == 0)// 点击的是GROUP
//
//		{
//			if (groupPosition != 0) {// 默认分组禁止删除
//				menu.add(0, 1, 1, "删除分组");
//			}
//			// 添加菜单项
//			menu.add(0, 0, 0, "添加分组");
//			menu.add(0, 2, 2, "重命名");
//			menu.add(0, 3, 3, "添加联系人");
//			menu.add(0, 4, 5, "取消");
//			menu.add(0, 5, 4, "全组成员移动..");
//		} else if (type == 1) {// 点击的是联系人
//
//			if (isMarkedState) {// 标记状态菜单
//				menu.add(2, 20, 0, "退出标记状态");
//				menu.add(2, 21, 1, "全部标记");
//				menu.add(2, 22, 2, "取消所有标记");
//				menu.add(2, 23, 3, "删除标记");
//				menu.add(2, 24, 4, "移动标记到..");
//				menu.add(2, 25, 5, "取消");
//
//			} else {// 普通状态菜单
//				menu.add(1, 10, 0, "进入标记状态");
//				menu.add(1, 12, 2, "删除");
//				menu.add(1, 13, 4, "移动到..");
//				menu.add(1, 14, 5, "取消");
//				menu.add(1, 15, 3, "编辑");
//			}
//
//		}
//
//		super.onCreateContextMenu(menu, v, menuInfo);
//	}

	// menuItem点击事件响应 STEP.三
//	@Override
//	public boolean onContextItemSelected(MenuItem item) {
//		ExpandableListView.ExpandableListContextMenuInfo menuInfo = (ExpandableListView.ExpandableListContextMenuInfo) item
//				.getMenuInfo();
//		int groupPosition = ExpandableListView
//				.getPackedPositionGroup(menuInfo.packedPosition);
//		int childPosition = ExpandableListView
//				.getPackedPositionChild(menuInfo.packedPosition);
//
//		Student contact = null;
//
//		if (childPosition != -1) {// 有child项显示再得到contact
//			contact = contacts.get(groupPosition).get(childPosition);
//		}
//
//		Group group = list.get(groupPosition);
//		switch (item.getItemId()) {
//		case 0:// 添加分组
////			Intent intent = new Intent(context, AddNewGroupActivity.class);
////			startActivity(intent);
//
//			break;
//		case 1:// 删除分组
//
//			contactManager.changeGroupByGroup(group.getGroupId(), 1);// 将组成员移动到默认分组中
//			groupManager.deleteGroupByID(group.getGroupId());// 删除指定的分组
//			bindView();
//
//			break;
//		case 2:// 重命名
//
////			Intent intent1 = new Intent(context, AddNewGroupActivity.class);
////			Bundle bundle = new Bundle();
////			bundle.putInt("groupId", group.getGroupId());
////			bundle.putString("groupName", group.getGroupName());
////			intent1.putExtras(bundle);
////			startActivity(intent1);
//
//			break;
//		case 3:// 添加联系人
////			Intent intent2 = new Intent(context, AddNewContactActivity.class);
////			Bundle bundle1 = new Bundle();
////			bundle1.putInt("groupId", group.getGroupId());
////			bundle1.putString("groupName", group.getGroupName());
////			bundle1.putInt("requestCode", GROUP_TO_ADD_CONTACT);
////			intent2.putExtras(bundle1);
////			startActivity(intent2);
//
//			break;
//		case 4:// 取消
//
//			break;
//		case 5:// 全组成员移动..
////			moveReGroupId = group.getGroupId();
////			Intent intent3 = new Intent(context, SelectGroupActivity.class);
////			intent3.putExtra("requestCode", GROUPCONTACT_MOVETO);
////			startActivityForResult(intent3, 0);
//
//			break;
//		case 10:// 进入标记状态
////			rl_mark.setVisibility(View.VISIBLE);
////			Toasttool.MyToast(context, "11");
////
////			isMarkedState = true;
//
//			break;
//
//		case 12:// 删除
////			if (contact != null){
////				final int contactId = contact.getId();
////				(new AlertDialog.Builder(context)).setTitle("警告")
////				.setIcon(R.drawable.contact).setMessage("您确定要删除此联系人信息吗？")
////				.setPositiveButton("确定", new DialogInterface.OnClickListener(){
////					@Override
////					public void onClick(DialogInterface dialog, int which) {
////						contactManager.deleteById(contactId);
////						bindView();
////					}}).setNegativeButton("取消", null).create().show();
////				
////			}
//				
//			break;
//		case 13:// 移动到..
//
////			Intent intent4 = new Intent(context, SelectGroupActivity.class);
////			intent4.putExtra("requestCode", CONTACT_MOVETO);
////			intent4.putExtra("groupName",group.getGroupName());
////			startActivityForResult(intent4, 0);
////			if (contact != null)
////				moveReContactId = contact.getId();
//
//			break;
//		case 14:// 取消
//
//			break;
//		case 15:// 編輯
////			Intent intent5 = new Intent(context, AddNewContactActivity.class);
////			Bundle bundle2 = new Bundle();
////			bundle2.putInt("id", contact.getId());
////			bundle2.putInt("requestCode", EDIT_CONTACT);
////			intent5.putExtras(bundle2);
////			startActivity(intent5);
//
//			break;
//		case 20:// 退出标记状态
//			rl_mark.setVisibility(View.GONE);
//			contactManager.initIsMark("false");
//			isMarkedState = false;
//			((MyExpandableListAdapter) adapter).refresh();
//
//			break;
//		case 21:// 全部标记
//			contactManager.initIsMark("true");
//			// bindView();
//			((MyExpandableListAdapter) adapter).refresh();
//
//			break;
//		case 22:// 取消所有标记
//			contactManager.initIsMark("false");
//			((MyExpandableListAdapter) adapter).refresh();
//
//			break;
//		case 23:// 删除标记
//			contactManager.deletByIsMark();
//			bindView();
//
//			break;
//		case 24:// 移动标记到。。
////			Intent intent6 = new Intent(context, SelectGroupActivity.class);
////			intent6.putExtra("requestCode", MARK_MOVE_TO);
////			startActivityForResult(intent6, 0);
//
//			break;
//		case 25:// 取消
//
//			break;
//
//		default:
//			break;
//		}
//
//		return super.onContextItemSelected(item);
//	}

	@Override
	protected void onResume() {
		if (!isFirst) {
			bindView();
		}
		isFirst = false;
		super.onResume();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i("group", "resultCode:"+resultCode);

		if (resultCode == 2 && data != null) {// 全组成员移动..跳转到组别选择的回
			moveToGroupId = data.getExtras().getInt("groupId");
			contactManager.changeGroupByGroup(moveReGroupId, moveToGroupId);
		} else if (resultCode == 4) {// 单人移动..跳转到组别选择的回
			moveToGroupId = data.getExtras().getInt("groupId");
			contactManager.changeGroupByCotact(moveReContactId, moveToGroupId);

		} else if (resultCode == 5) {// 标记移动..跳转到组别选择的回
			moveToGroupId = data.getExtras().getInt("groupId");

			contactManager.changeGroupByMark(moveToGroupId);
			contactManager.initIsMark("false");

		} else if (resultCode == 500) {// 从手机导入的回

//			Log.i("group", "33");
//			List<Object> list = (List<Object>) data.getExtras().get("contacts");
//			List<String> name = (List<String>) list.get(0);
//			List<String> phone = (List<String>) list.get(1);
//			List<Bitmap> photo = Const.mContactsPhonto;//(List<Bitmap>) list.get(2);
//
//			Log.i("Item", name.size() + "");
			
			// List<Bitmap> photo = (List<Bitmap>) list.get(2);

			//Bitmap picture = BitmapFactory.decodeResource(getResources(),
					//R.drawable.bg_photo_default);
			//byte[] photo = ImageTools.getByteFromBitmap(picture);
			// Log.i("Item", "name1:"+name.size() + "");
//			for (int i = 0; i < name.size(); i++) {
//				Contact contact = new Contact(ImageTools.getByteFromBitmap(Const.mContactsPhonto.get(i)), name.get(i), 1,
//						phone.get(i), "", "", "", "", "", "", "false");
//				contactManager.addContact(contact);
//				Log.i("Item", contact.toString());
//			}

			getData();
			//adapter.refresh();
			

			
		} else if (resultCode == 600 || resultCode==700) {// 从SIM卡导入或SDCard还原的

//			List<Object> list = (List<Object>) data.getExtras().get("contacts");
//
//			if (((List<String>)list.get(0)).size()==0) {
//				Toasttool.MyToast(context, "没有联系人！");
//
//			} else {
//				List<String> name = (List<String>) list.get(0);
//				List<String> phone = (List<String>) list.get(1);
//
//				Bitmap picture = BitmapFactory.decodeResource(getResources(),
//						R.drawable.bg_photo_default);
//				byte[] photo = ImageTools.getByteFromBitmap(picture);
//				//Log.i("Item", "name1:"+name.size() + "");
//				for (int i = 0; i < name.size() ; i++) {
//					Contact contact = new Contact(photo, name.get(i), 1,
//							phone.get(i), "", "", "", "", "", "",
//							"false");
//					contactManager.addContact(contact);
//					Log.i("Item", contact.toString());
//				}
//				
//			}
		}
		else if(resultCode ==1000){//设置全屏的回
			getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,                
                    WindowManager.LayoutParams. FLAG_FULLSCREEN);    
			
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {

			// rl_mark.setVisibility(View.VISIBLE);
	//		loadBottomMenu();
	//		Toasttool.MyToast(context, "menu");
			if (bottomMenuGrid.getVisibility() == View.VISIBLE) {
				bottomMenuGrid.setVisibility(View.GONE);
			} else {
				bottomMenuGrid.setVisibility(View.VISIBLE);
			}

		}

		return super.onKeyDown(keyCode, event);
	}

	// button_menu主菜单的Adapter
//	private SimpleAdapter getMenuAdapter(String[] menuNameArray,
//			int[] imageResourceArray) {
//
//		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
//		for (int i = 0; i < menuNameArray.length; i++) {
//			HashMap<String, Object> map = new HashMap<String, Object>();
//			map.put("itemImage", imageResourceArray[i]);
//			map.put("itemText", menuNameArray[i]);
//			data.add(map);
//		}
//		SimpleAdapter simperAdapter = new SimpleAdapter(this, data,
//				R.layout.item_menu, new String[] { "itemImage", "itemText" },
//				new int[] { R.id.item_image, R.id.item_text });
//		return simperAdapter;
//	}

//	private void loadBottomMenu() {
//
//		if (bottomMenuGrid == null) {
//
//			bottomMenuGrid = (GridView) findViewById(R.id.gv_buttom_menu);
//			bottomMenuGrid.setBackgroundResource(R.drawable.channelgallery_bg);// 设置背景
//			bottomMenuGrid.setNumColumns(3);// 设置每行列数
//			bottomMenuGrid.setGravity(Gravity.CENTER);// 位置居中
//			bottomMenuGrid.setVerticalSpacing(10);// 垂直间隔
//			bottomMenuGrid.setHorizontalSpacing(10);// 水平间隔
//			bottomMenuGrid.setAdapter(getMenuAdapter(bottom_menu_itemName,
//					bottom_menu_itemSource));// 设置菜单Adapter
//			/** 监听底部菜单选项 **/
//			bottomMenuGrid.setOnItemClickListener(new OnItemClickListener() {
//				public void onItemClick(AdapterView<?> arg0, View arg1,
//						int item, long arg3) {
//					switch (item) {
//					case 0:// 工具箱
//						Toasttool.MyToast(context, "工具箱");
//						Intent intent = new Intent(context,ToolGriewActivity.class);
//						startActivityForResult(intent, 8);
//						
////						loadMainMenuDialog();
////						mainMenuDialog.show();
//
//						break;
//					case 1:// 全屏
////						Toasttool.MyToast(context, "帮助");
//						Intent intent2 = new Intent(context,FullScreenActivity.class);
//						startActivityForResult(intent2, 6);
//						
//						
//
//						break;
//					case 2:// 退出
//						Toasttool.MyToast(context, "退出");
//						finish();
//
//						break;
//
//					default:
//						break;
//					}
//
//				}
//
//			});
//		}
//	}

	
	class MyExpandableListAdapter extends BaseExpandableListAdapter {
		public PopupWindow popupWindow;
		private Context mcontext = null;
		private boolean[] isOpen = null;
		private LayoutInflater mInflater;
		private StudentManager contactManager;
		private GroupManager groupManager;
		private boolean check = true;
		private View popView;
		private int id;
		private List<Integer> contactCountByGroup;
		private List<String> groupItem = new ArrayList<String>();
		private List<List<Student>> contacts = new ArrayList<List<Student>>();

		

		public MyExpandableListAdapter(List<Integer> contactCountByGroup,List<String> groupItem,
				List<List<Student>> contacts, boolean[] isOpen, Context context) {
			this.contactCountByGroup = contactCountByGroup;
			this.isOpen = isOpen;
			this.mcontext = context;
			this.groupItem = groupItem;
			this.contacts = contacts;
			this.mInflater = LayoutInflater.from(mcontext);
			this.contactManager = new StudentManager(context);
			this.groupManager = new GroupManager(context);

		}

		public MyExpandableListAdapter() {

		}

		public void refresh() {
			this.notifyDataSetChanged();
		}

		public Object getChild(int groupPosition, int childPosition) {
			return contacts.get(groupPosition);
		}

		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		public int getChildrenCount(int groupPosition) {
			return contacts.get(groupPosition).size();
		}

		public View getChildView(int groupPosition, final int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			convertView = mInflater.inflate(R.layout.child, null);
			final List<Student> contacts = (List<Student>) getChild(groupPosition,
					childPosition);
			ImageView imgPhoto = (ImageView) convertView.findViewById(R.id.imgContact);
			TextView childName = (TextView) convertView.findViewById(R.id.childName);
			Bitmap bt = BitmapFactory.decodeResource(mcontext.getResources(),R.drawable.default_avatar_big);
			imgPhoto.setImageBitmap(bt);
		//	imgPhoto.setImageResource(R.drawable.bg_photo_default);
			childName.setTextColor(Color.BLACK);
			childName.setText(contacts.get(childPosition).getName());

//			childPhone.setTextColor(Color.BLACK);
			
		 //  childPhone.setText(contacts.get(childPosition).getPhone() );
			

			ImageView imgChk = (ImageView) convertView.findViewById(R.id.imgChk);
			id = contacts.get(childPosition).getId();
			if (contactManager.getContactById(id).getIsMark().equals("true"))
				imgChk.setVisibility(View.VISIBLE);

			ImageView popImg = (ImageView) convertView.findViewById(R.id.popImg);
			popImg.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
						if (popupWindow == null) {
							popView = mInflater.inflate(R.layout.popupwindow_view,
									null, false);
							popupWindow = new PopupWindow(popView,
									LayoutParams.WRAP_CONTENT,
									LayoutParams.WRAP_CONTENT, true);

							popupWindow.setBackgroundDrawable(new BitmapDrawable());

						}
						popupWindow.showAsDropDown(v);
			//			ImageButton imgCall = (ImageButton) popView.findViewById(R.id.imgCall);
						ImageButton imgMsg = (ImageButton) popView.findViewById(R.id.imgMsg);
			//			ImageButton imgEmail = (ImageButton) popView.findViewById(R.id.imgEmail);

//						imgCall.setOnClickListener(new View.OnClickListener() {
	//
//							public void onClick(View v) {
	//
////								((ContactActivity) mcontext)
////										.makeCall(contactManager.getContactById(id)
////												.getPhone());
	//
//								popupWindow.dismiss();
//							}
	//
//						});
						imgMsg.setOnClickListener(new View.OnClickListener() {

							public void onClick(View v) {
								
								int userIds = contacts.get(childPosition).get_id();
								int getNum = UTestPaperFactory.createUTestPaper(mcontext).getUserCount(String.valueOf(userIds));
								if(getNum == 0){
									Toast.makeText(mcontext, "该学员未完成作业测试。", Toast.LENGTH_SHORT).show();
								} else {
									Intent intent = new Intent(mcontext, TestPaperQuestionByUseActivity.class);
									intent.putExtra("categoryId", testPaperId);
									intent.putExtra("userIds", userIds);
									
									startActivity(intent);
								}
								popupWindow.dismiss();

							}
						});
//						imgEmail.setOnClickListener(new View.OnClickListener() {
	//
//							public void onClick(View v) {
////								Toasttool.MyToast(mcontext, "Email");
////								((ContactActivity) mcontext)
////										.sendEmail(contactManager
////												.getContactById(id).getE_mail()
////												.toString());
	//
//								popupWindow.dismiss();
	//
//							}
//						});

//					} else {
//						Toasttool.MyToast(mcontext, "pop2");
//						check = true;
//						popupWindow.dismiss();
	//
//					}
				}
			});

			return convertView;
		}

		public Object getGroup(int groupPosition) {
			return groupItem.get(groupPosition);
		}

		public int getGroupCount() {
			return groupItem.size();
		}

		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			convertView = mInflater.inflate(R.layout.group, null);
			TextView textView = (TextView) convertView.findViewById(R.id.groupto);
			TextView textCount = (TextView) convertView.findViewById(R.id.txtCount);
	        
		
			textCount.setText("("+contactCountByGroup.get(groupPosition).toString()+")");

			textView.setTextColor(Color.BLACK);
			textView.setText(getGroup(groupPosition).toString());
			Log.i("hubin", "groupPosition" + groupPosition);
			if (isOpen[groupPosition]) {
				Drawable leftDrawable = mcontext.getResources().getDrawable(R.drawable.expend);
				leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(),leftDrawable.getMinimumHeight());
				textView.setCompoundDrawables(leftDrawable, null, null, null);
			} else {
				Drawable leftDrawable = mcontext.getResources().getDrawable(R.drawable.unexpend);
				leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(),
						leftDrawable.getMinimumHeight());
				textView.setCompoundDrawables(leftDrawable, null, null, null);

			}

			return convertView;
		}

		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

		public boolean hasStableIds() {
			return true;
		}

	}

}

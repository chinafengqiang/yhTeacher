package com.yhlearningclient.ui;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yhlearningclient.R;
import com.yhlearningclient.biz.UserManager;
import com.yhlearningclient.model.Manager;
import com.yhlearningclient.model.User;

/**
 * 用户登录Activity
 * @author Administrator
 */
public class LoginActivity extends Activity {
	
	//用户登录按钮
	private Button btnLogin;
	//用户名称
	private EditText editName;
	//密码
	private EditText editPassword;
	//服务地址
	private EditText editServerIP;
	//进度条
	private ProgressDialog progressDialog = null;
	
	String serverIP = "";
	private UserManager manager = null; 
	
	//用于存放账号、密码；以及第一次登录成功之后与账号有关的信息
	private SharedPreferences sharedPreferences;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
     
        //初始化控件
        initWidget();
        // 获取了一个名为"userObj"的sharedPreferences对象
        sharedPreferences = getSharedPreferences("serverIpObj", MODE_PRIVATE);
     	
     	editName.setText(sharedPreferences.getString("name", ""));
     	editServerIP.setText(sharedPreferences.getString("serverIP", ""));
     	
     	//有数据，则取出来
//     	if (sharedPreferences != null) {
//     		
//     	} else{
     		btnLogin.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String userName = editName.getText().toString();
					String passWord = editPassword.getText().toString();
					serverIP = editServerIP.getText().toString();
					login(userName, passWord);

				}
			});
     //	}
    
    }
    
	private ProgressDialog createProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setTitle("系统提示");
			progressDialog.setMessage("登录中...");
		}

		return progressDialog;
	}
	
	@Override
	protected void onDestroy() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
		super.onDestroy();
	}

	/**
	 * 登录
	 * @param username 用户名
	 * @param password 密码
	 */
	private void login(String username, String password) {

		progressDialog = createProgressDialog();
		progressDialog.show();
		
		manager = new UserManager(LoginActivity.this);
		String condition = "user_name='" +username + "'";
		int userRecord = manager.getUserCount(condition);
		
		if(userRecord == 0){
//			List<User> users = manager.getAllUserFromLocal();
//			if(users.size() > 0){
//				Toast.makeText(LoginActivity.this, "只能允许一个帐号登录！", Toast.LENGTH_LONG).show();
//			} else {
//				loginSystem(username, password);
//			}
			loginSystem(username, password);
		} else {
			Log.i("DBAccess", "本地加载。。");
			List<User> users = manager.getAllUserFromLocal();
			if(users != null && users.size() > 0){
			User user = users.get(0);
			if(user != null){
				
				if(username.equals(user.getUser_name()) && password.equals(user.getUser_password())){
					
					SharedPreferences.Editor editor = sharedPreferences.edit();
					Log.i("serverIP=====", serverIP);
					editor.putString("serverIP", serverIP);
					editor.putString("user", user.getUser_id());
					editor.putString("name", user.getUser_name());
					editor.putLong("classId", user.getClassId());
					
					editor.commit();
					
					Intent intent = new Intent(LoginActivity.this, MainActivity.class);
					startActivity(intent);
					finish();
				} else {
					Toast.makeText(LoginActivity.this, "密码错误！", Toast.LENGTH_LONG).show();
				}
				
			}
			}
		}
		
	//	loginSystem(username, password);

	}
	
	//异常返回结果消息
	Handler handleLogin = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Manager userInfo = (Manager) msg.obj;

			if (progressDialog != null)
				progressDialog.hide();
			
			if (userInfo != null) {
				
				if("密码错误".equals(userInfo.getName())){
					Toast.makeText(LoginActivity.this, "密码错误！", Toast.LENGTH_LONG).show();
				} else{
					
				saveUserStauts(userInfo);

				//本地数据
				localInsert(userInfo);
				
				Intent intent = new Intent(LoginActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
				}
			} else {
				Toast.makeText(LoginActivity.this, "网络连接出现问题，请检查！", Toast.LENGTH_LONG).show();
			}

		}

		private void localInsert(Manager userInfo) {
			User user = new User();
			user.setUser_id(userInfo.getId().toString());
			user.setUser_name(userInfo.getName());
			user.setUser_password(userInfo.getPassword());
			user.setClassId(userInfo.getClassId());
			
			manager.insert(user);
			
		}

		private void saveUserStauts(Manager userInfo) {
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putString("serverIP", serverIP);
			editor.putString("user", userInfo.getId().toString());
			editor.putString("name", userInfo.getName());
			editor.putLong("classId", userInfo.getClassId());
			editor.commit();
		};
	};
	
	
	private void loginSystem(final String username, final String password) {

		Runnable runable = new Runnable() {

			@Override
			public void run() {
				UserManager loginManager = new UserManager(LoginActivity.this);
				Manager manager = null;
				try {
					manager = loginManager.loginManager(serverIP, username, password);
				} catch (Exception e) {
					e.printStackTrace();
				}
			
				Message msg = new Message();
				msg.obj = manager;
				handleLogin.sendMessage(msg);

			}
		};

		Thread thread = new Thread(runable);
		thread.start();
	}
	
	// 获得控件
	private void initWidget() {
		editName = (EditText) findViewById(R.id.editName);
		editPassword = (EditText) findViewById(R.id.editPassword);
		editServerIP = (EditText) findViewById(R.id.editServerIP);
		btnLogin = (Button) findViewById(R.id.ivLogin);
	}

}

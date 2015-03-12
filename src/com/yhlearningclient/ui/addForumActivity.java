package com.yhlearningclient.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yhlearningclient.R;
import com.yhlearningclient.biz.LessonManager;
import com.yhlearningclient.model.ForumContent;

/**
 * 添加交流
 */
public class addForumActivity extends Activity {
	
	SharedPreferences sharedPreferences = null;
	ProgressDialog pd = null;
	String username = "";
	String ip = "";
	Long classId;
	
	public void showProgressDialog(){
		if (pd==null){
			pd = new ProgressDialog(this);
			pd.setTitle("系统提示");
			pd.setMessage("数据提交中。。。");
		}
		
		pd.show();
	}
	
	public void hideProgressDialog(){
		if (pd!=null) pd.dismiss();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_forum);
		
		sharedPreferences = getSharedPreferences("serverIpObj", MODE_PRIVATE);
		username = sharedPreferences.getString("user", null);
		ip = sharedPreferences.getString("serverIP", null);
		classId = sharedPreferences.getLong("classId", 0);
		
		//保存
	    Button btnSure = (Button)this.findViewById(R.id.btnSure);
	    btnSure.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					insertValue();
					finish();
				}});
	    //取消 
	    Button btnCancel = (Button)this.findViewById(R.id.btnCancel);
	    btnCancel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				finish();
			}});
		
	}
	
    /**
     * 添加信息
     */
    private void insertValue(){
    	EditText etName = (EditText)this.findViewById(R.id.editTitle);
    	EditText etContent = (EditText)this.findViewById(R.id.edtContent);
    	
    	if("".equals(etName.getText().toString().trim()) ){
    		Toast.makeText(this, "输入主题!", Toast.LENGTH_SHORT).show();
    	}else if("".equals(etContent.getText().toString().trim()) ){
    		Toast.makeText(this, "输入问题!", Toast.LENGTH_SHORT).show();
    	} else {
    		
    		ForumContent sms = new ForumContent();
    		
    		sms.setName(etName.getText().toString().trim());
    		sms.setQuestion(etContent.getText().toString().trim());
    		
    		String serverIp = "http://"+ ip +":8080";
    		AddMessage addMessage = new AddMessage(serverIp, sms);
    		Thread thread = new Thread(addMessage);
			thread.start();
    	}
    }
    
	private Handler handleSubmit = new Handler(){
		public void handleMessage(android.os.Message msg) {
			hideProgressDialog();
			boolean result = (Boolean)msg.obj;
			if (result){
				Toast.makeText(addForumActivity.this, "信息已成功提交到服务器！",Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(addForumActivity.this, "信息提交失败！！",Toast.LENGTH_SHORT).show();
			}
		};
	};
    
    class AddMessage implements Runnable{
		private ForumContent fc=null;
		private String serverIp = "";
		
		public AddMessage(String serverIp, ForumContent fc){
			this.serverIp = serverIp;
			this.fc = fc;
		}

		@Override
		public void run() {
			LessonManager lessonManager = new LessonManager();
			boolean result = false;
			try {
				result = lessonManager.createManagerForum(serverIp, classId, fc.getName(), fc.getQuestion(), Long.valueOf(username));
			} catch (Exception e) {
				e.printStackTrace();
			}
			Message msg = new Message();
			msg.obj = result;
			handleSubmit.sendMessage(msg);
		}
		
	}
    

}

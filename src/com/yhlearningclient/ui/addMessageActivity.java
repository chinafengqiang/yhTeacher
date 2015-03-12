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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yhlearningclient.R;
import com.yhlearningclient.biz.LessonManager;
import com.yhlearningclient.model.SmsContent;

/**
 * 添加通知
 */
public class addMessageActivity extends Activity {
	
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
		setContentView(R.layout.add_message);
		
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
    	CheckBox  cb = (CheckBox)findViewById(R.id.check_all_send);
    	
    	if("".equals(etName.getText().toString().trim()) ){
    		Toast.makeText(this, "输入标题!", Toast.LENGTH_SHORT).show();
    	}else if("".equals(etContent.getText().toString().trim()) ){
    		Toast.makeText(this, "输入内容!", Toast.LENGTH_SHORT).show();
    	} else {
    		
    		SmsContent sms = new SmsContent();
    		
    		sms.setName(etName.getText().toString().trim());
    		sms.setContent(etContent.getText().toString().trim());
    		if(cb.isChecked()){
    			sms.setCanSend(1);
    		} else {
    			sms.setCanSend(0);
    		}
    		
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
				Toast.makeText(addMessageActivity.this, "通知信息已成功提交到服务器！",Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(addMessageActivity.this, "通知信息提交失败！！",Toast.LENGTH_SHORT).show();
			}
		};
	};
    
    class AddMessage implements Runnable{
		private SmsContent sms=null;
		private String serverIp = "";
		
		public AddMessage(String serverIp, SmsContent sms){
			this.serverIp = serverIp;
			this.sms = sms;
		}

		@Override
		public void run() {
			LessonManager lessonManager = new LessonManager();
			boolean result = false;
			try {
				result = lessonManager.createMessage(serverIp, Long.valueOf(username), sms.getName(), sms.getContent(), sms.getCanSend(), classId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Message msg = new Message();
			msg.obj = result;
			handleSubmit.sendMessage(msg);
		}
		
	}
    

}

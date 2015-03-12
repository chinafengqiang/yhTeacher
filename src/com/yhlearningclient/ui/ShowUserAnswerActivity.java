package com.yhlearningclient.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.yhlearningclient.R;
import com.yhlearningclient.factory.UTestPaperFactory;
import com.yhlearningclient.model.UserTestPaper;
import com.yhlearningclient.utils.ImageTools;

/**
 * 显示用户答案
 */
public class ShowUserAnswerActivity extends Activity {

	ProgressDialog pd = null;
	String ip = "";
	ImageView imageView;
	int questionId = 0;
	int userIds = 0;
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

	@Override
	protected void onDestroy() {
		hideProgressDialog();
		super.onDestroy();
	}

	/**
	 * 隐藏
	 */
	private void hideProgressDialog() {
		if (pd != null)
			pd.dismiss();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_user_answer);

		if (getIntent().getIntExtra("questionId", 0)!=0)
			questionId = getIntent().getIntExtra("questionId", 0);
		if (getIntent().getIntExtra("userIds", 0)!=0)
			userIds = getIntent().getIntExtra("userIds", 0);
		
		findView();
		loadUserTestPaperImg();
	}

	private void findView() {
		imageView = (ImageView) findViewById(R.id.imageView);
	}

	//用于接受loadUserTestPaperImg线程结束后传来的列表，并加以显示
	Handler handlerUserTestPaperImg = new Handler() {
		public void handleMessage(android.os.Message msg) {
			UserTestPaper  utp = (UserTestPaper) msg.obj;
			if(utp != null){
				pd.setMessage("数据已获取,界面绑定中...");
				byte[] result = utp.getNoSelectAnswer();
				Bitmap bitmap = ImageTools.getBitmapFromByte(result);
				imageView.setImageBitmap(bitmap);
				pd.setMessage("数据获取中,请稍候...");
				hideProgressDialog();
			} else {
				Toast.makeText(ShowUserAnswerActivity.this, "下载图片失败", Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	//创建新线程从本地图片
	public void loadUserTestPaperImg() {
		
		showProgressDialog();
		Thread thread = new Thread() {
			@Override
			public void run() {
				UserTestPaper  u = UTestPaperFactory.createUTestPaper(ShowUserAnswerActivity.this).getUserTestPaperByQuestionId(questionId, userIds);
				Message message = new Message();
				message.obj = u;
				handlerUserTestPaperImg.sendMessage(message);
			}
		};
		
		thread.start();
		thread = null;

	}
	
	
}

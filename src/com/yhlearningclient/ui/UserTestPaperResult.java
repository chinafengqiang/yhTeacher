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
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yhlearningclient.R;
import com.yhlearningclient.factory.TestPaperFactory;
import com.yhlearningclient.model.TestPaperQuestion;

public class UserTestPaperResult extends Activity {
	private GridView gv = null;
	ProgressDialog pd = null;
	ArrayList<TestPaperQuestion> listObj = null;
	int testPaperId = 0;
	private SharedPreferences sharedPreferences;
	private List<TestPaperQuestion> testPaperQuestion = null;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exam_summary);
		sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
		initData();
	}

	private void initData() {
		gv = (GridView) this.findViewById(R.id.exam_summary_grid);
		if (getIntent().getIntExtra("categoryId", 0)!=0)
			testPaperId = getIntent().getIntExtra("categoryId", 0);
		loadTestPaperList(testPaperId);
	}
	
	//用于接受loadTestPaperList线程结束后传来的列表，并加以显示
	Handler handlerTestPaperRecord = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			testPaperQuestion = (ArrayList<TestPaperQuestion>)msg.obj;
			if (testPaperQuestion == null) {
				Toast.makeText(UserTestPaperResult.this, "对不起, 无数据请下载。", Toast.LENGTH_SHORT).show();
			}else{
				pd.setMessage("数据已获取,界面绑定中...");
//				adapter = new AllCommentAdapter(context, forums);
//				pullToRefreshListView.setAdapter(adapter);
				
				gv.setAdapter(new MyAdapter(testPaperQuestion, UserTestPaperResult.this));
				// 添加消息处理
				gv.setOnItemClickListener(new ItemClickListener());
				
				pd.setMessage("数据获取中,请稍候...");
				hideProgressDialog();
				
			}	
			
		}
	};
	
	//创建新线程从本地试卷列表
	public void loadTestPaperList(final int testPaperId) {
		
		showProgressDialog();
		Thread thread = new Thread() {
			@Override
			public void run() {
				List<TestPaperQuestion> tpq = TestPaperFactory.createTestPaper(UserTestPaperResult.this).getAllTestPaperQuestionDB(testPaperId);
				Message message = new Message();
				message.obj = tpq;
				handlerTestPaperRecord.sendMessage(message);
			}
		};
		
		thread.start();
		thread = null;

	}
	

	//当AdapterView被单击(触摸屏或者键盘)，则返回的Item单击事件
	class ItemClickListener implements OnItemClickListener {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				
				int questionId = testPaperQuestion.get(arg2).getQuestionId();
				Intent intent = new Intent();
				intent.putExtra("questionId", questionId);
				intent.setClass(UserTestPaperResult.this, QuestionSingleStat.class);
				startActivity(intent);
				
			}
	}
		
	/**
	 *适配器
	 */
	class MyAdapter extends BaseAdapter {

		private LayoutInflater inflater = null;
		Context context;
		private List<TestPaperQuestion> testPaperQuestions = null;
		
		public MyAdapter(Context context) {
			this.context = context;
			inflater = LayoutInflater.from(context);
		}
		
		public MyAdapter(List<TestPaperQuestion> testPaperQuestions, Context context){
			this.testPaperQuestions = testPaperQuestions;
			this.context = context;
			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return testPaperQuestions.size();
		}

		@Override
		public Object getItem(int position) {
			return testPaperQuestions.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.exam_summary_grid_item, null);
				holder = new ViewHolder();
				holder.tv_name = (TextView) convertView.findViewById(R.id.name);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
		//	int flashback = testPaperQuestions.size() - position - 1;
			holder.tv_name.setText(testPaperQuestions.get(position).getName().toString());

			return convertView;
		}

	}
	
	class ViewHolder {
		TextView tv_name;
	}

}
package com.yhlearningclient.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import com.example.yhlearningclient.R;
import com.yhlearningclient.utils.FileUtil;

/**
 * ui主页面显示
 */
public class MainActivity extends Activity {
	
	/**
	 * 上下文
	 */
	private Context context = null;
	
	/**
	 * 我的信息
	 */
	private Button btnMe = null;
	
	/**
	 * 教学安排
	 */
	private Button btnTeachingMaker = null;
	
	/**
	 * 课堂资源
	 */
	private Button btnLearningFile = null;
	
	/**
	 * 学生管理
	 */
	private Button btnStudentManager = null;
	
	/**
	 * 教学资料
	 */
	private Button btnTeachingFile = null;
	
	/**
	 * 作业测试
	 */
	private Button btnHomework = null;
	
	/**
	 * 更多应用
	 */
	private Button btnMoreApp = null;
	
	/**
	 * 初始化主页面显示
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        
        initDirectoryPath();
        
        //获得我的信息编号
        btnMe = (Button)this.findViewById(R.id.btnMe);
        btnMe.setOnClickListener(onClickListenerView);
        
        //获得教学安排编号
        btnTeachingMaker = (Button)this.findViewById(R.id.btnTeachingMaker);
        btnTeachingMaker.setOnClickListener(onClickListenerView);
        
        //获得课堂资源编号
        btnLearningFile = (Button)this.findViewById(R.id.btnLearningFile);
        btnLearningFile.setOnClickListener(onClickListenerView);
        
        //获得学生管理编号
        btnStudentManager = (Button)this.findViewById(R.id.btnStudentManager);
        btnStudentManager.setOnClickListener(onClickListenerView);
        
        //获得教学资料编号
        btnTeachingFile = (Button)this.findViewById(R.id.btnTeachingFile);
        btnTeachingFile.setOnClickListener(onClickListenerView);
  
        //获得作业测试编号
        btnHomework = (Button)this.findViewById(R.id.btnHomework);
        btnHomework.setOnClickListener(onClickListenerView);
        
        //获得更多应用编号
        btnMoreApp = (Button)this.findViewById(R.id.btnMoreApp);
        btnMoreApp.setOnClickListener(onClickListenerView);
        
    }
    
    
	/**
	 * 事件菜单切换
	 */
	private View.OnClickListener onClickListenerView = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btnMe:
				Intent intentLogin = new Intent(context, LoginActivity.class);
				startActivity(intentLogin);
				break;
			case R.id.btnTeachingMaker:
				Intent intentTeaching = new Intent(context, TeacherMakerActivity.class);
				startActivity(intentTeaching);
				break;
			case R.id.btnLearningFile:
				Intent intentLearning = new Intent(context, ClassroomFileActivity.class);
				startActivity(intentLearning);
				break;
			case R.id.btnStudentManager:
				Intent intentStudent = new Intent(context, StudentManagerActivity.class);
				startActivity(intentStudent);
				break;
			case R.id.btnTeachingFile:
				Intent intentFile = new Intent(context, LearnFileActivity.class);
				startActivity(intentFile);
				break;
			case R.id.btnHomework:
				Intent intentHomework = new Intent(context, HomeworkActivity.class);
				startActivity(intentHomework);
				break;
			case R.id.btnMoreApp:
//				Intent intentMore = new Intent(context, MoreActivity.class);
//				startActivity(intentMore);
				break;
			}
		}
	};
	
	private void initDirectoryPath(){
		String rootPath = Environment.getExternalStorageDirectory().getPath() + "/classroomBook";
		FileUtil.getFileDir(rootPath);
		
		String rootPath1 = Environment.getExternalStorageDirectory().getPath() + "/myCourseTable";
		FileUtil.getFileDir(rootPath1);
		
		String rootPath2 = Environment.getExternalStorageDirectory().getPath() + "/myCoursePlan";
		FileUtil.getFileDir(rootPath2);
		
		String rootPath3 = Environment.getExternalStorageDirectory().getPath() + "/myVideo";
		FileUtil.getFileDir(rootPath3);
		
		String rootPath4 = Environment.getExternalStorageDirectory().getPath() + "/myCourseBook";
		FileUtil.getFileDir(rootPath4);
		
		String rootPath5 = Environment.getExternalStorageDirectory().getPath() + "/myAdditionalFile";
		FileUtil.getFileDir(rootPath5);
		
		
		String rootPath6 = Environment.getExternalStorageDirectory().getPath() + "/myLocalFile";
		FileUtil.getFileDir(rootPath6);
	}

}

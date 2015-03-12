package com.yhlearningclient.ui;

import android.app.ActivityGroup;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.yhlearningclient.R;
import com.yhlearningclient.common.SlidingMenuView;

/**
 * 课堂资源
 */
public class ClassroomFileActivity extends ActivityGroup {

	SlidingMenuView slidingMenuView;

	ViewGroup tabcontent;

	private Button btnReturnPage = null;
	private Context context = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_classroom_file);
		slidingMenuView = (SlidingMenuView) findViewById(R.id.sliding_menu_view);
		context = this;
		tabcontent = (ViewGroup) slidingMenuView.findViewById(R.id.sliding_body);

		btnReturnPage = (Button) this.findViewById(R.id.btnReturnPage);
		btnReturnPage.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, MainActivity.class);
				startActivity(intent);
			}
		});

		showDefaultTab();
	}

	public void hideMenu(View view) {
		slidingMenuView.scrollRight();
	}

	public void showMenu(View view) {
		slidingMenuView.scrollLeft();
	}

//	public void changeTab1(View view) {
//		Intent i = new Intent(this, TeachingMessageActivity.class);
//		View v = getLocalActivityManager().startActivity(CourseTableActivity.class.getName(), i).getDecorView();
//		tabcontent.removeAllViews();
//		tabcontent.addView(v);
//	}

	public void changeTab2(View view) {
		Intent i = new Intent(this, VideoCenterActivity.class);
		View v = getLocalActivityManager().startActivity(ScheduleTableActivity.class.getName(), i).getDecorView();
		tabcontent.removeAllViews();
		tabcontent.addView(v);
	}

	public void changeTab3(View view) {
		Intent i = new Intent(this, ClassroomBookActivity.class);
		View v = getLocalActivityManager().startActivity(ScheduleTableActivity.class.getName(), i).getDecorView();
		tabcontent.removeAllViews();
		tabcontent.addView(v);
	}

	void showDefaultTab() {
		Intent i = new Intent(this, ClassroomBookActivity.class);
		View v = getLocalActivityManager().startActivity(CourseTableActivity.class.getName(), i).getDecorView();
		tabcontent.removeAllViews();
		tabcontent.addView(v);
	}

}

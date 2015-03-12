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
 * 教学资料
 * @author Administrator
 */
public class LearnFileActivity extends ActivityGroup {

	SlidingMenuView slidingMenuView;

	ViewGroup tabcontent;

	private Button btnReturnPage = null;
	private Context context = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_learn_file);
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

	public void changeTab1(View view) {
		Intent i = new Intent(this, TeachingLocalActivity.class);
		View v = getLocalActivityManager().startActivity(CourseTableActivity.class.getName(), i).getDecorView();
		tabcontent.removeAllViews();
		tabcontent.addView(v);
	}

	public void changeTab2(View view) {
		Intent i = new Intent(this, TeachingAdditionalActivity.class);
		View v = getLocalActivityManager().startActivity(ScheduleTableActivity.class.getName(), i).getDecorView();
		tabcontent.removeAllViews();
		tabcontent.addView(v);
	}

	public void changeTab3(View view) {
		Intent i = new Intent(this, TeachingAssistActivity.class);
		View v = getLocalActivityManager().startActivity(ScheduleTableActivity.class.getName(), i).getDecorView();
		tabcontent.removeAllViews();
		tabcontent.addView(v);
	}

	void showDefaultTab() {
		Intent i = new Intent(this, TeachingAssistActivity.class);
		View v = getLocalActivityManager().startActivity(CourseTableActivity.class.getName(), i).getDecorView();
		tabcontent.removeAllViews();
		tabcontent.addView(v);
	}

}

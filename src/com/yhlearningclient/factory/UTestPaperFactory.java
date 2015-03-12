package com.yhlearningclient.factory;

import android.content.Context;

import com.yhlearningclient.biz.UserTestPaperManager;

/**
 *简单工厂
 */
public class UTestPaperFactory {

	private UTestPaperFactory(){
	}
	
	public static UserTestPaperManager createUTestPaper(Context context){
		return new UserTestPaperManager(context);
	}
	
}

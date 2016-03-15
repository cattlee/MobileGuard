package com.itheima62.mobileguard.activities;


import com.itheima62.mobileguard.R;

import android.app.Activity;
import android.os.Bundle;

/**
 * @author Administrator
 * 主界面
 */
public class HomeActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//
		super.onCreate(savedInstanceState);
		initView();//初始化界面
	}

	private void initView() {
		// 
		setContentView(R.layout.activity_home);
	}
}

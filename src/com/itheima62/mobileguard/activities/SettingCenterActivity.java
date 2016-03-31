package com.itheima62.mobileguard.activities;

import com.itheima62.mobileguard.R;

import android.app.Activity;
import android.os.Bundle;

public class SettingCenterActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		initView();//初始化界面
	}

	private void initView() {
		setContentView(R.layout.activity_settingcenter);
	}
}

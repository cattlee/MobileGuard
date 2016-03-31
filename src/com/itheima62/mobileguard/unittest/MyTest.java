package com.itheima62.mobileguard.unittest;

import com.itheima62.mobileguard.engine.ReadContantsEngine;
import com.itheima62.mobileguard.utils.ServiceUtils;

import android.test.AndroidTestCase;

public class MyTest extends AndroidTestCase {
	public void testReadContants(){
		ReadContantsEngine.readContants(getContext());//获取虚拟的上下文
	}
	
	public void testRunningService(){
		ServiceUtils.isServiceRunning(getContext(), "");
	}
}

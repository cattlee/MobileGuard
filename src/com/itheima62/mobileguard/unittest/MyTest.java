package com.itheima62.mobileguard.unittest;

import com.itheima62.mobileguard.engine.ReadContantsEngine;
import com.itheima62.mobileguard.utils.ServiceUtils;

import android.test.AndroidTestCase;
//单元测试类
public class MyTest extends AndroidTestCase {
	public void testReadContants(){
		ReadContantsEngine.readContants(getContext());//获取虚拟的上下文  getContext()方法主要用在单元测试中
		}
	
	public void testRunningService(){
		ServiceUtils.isServiceRunning(getContext(), "");
	}
}


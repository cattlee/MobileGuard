package com.itheima62.mobileguard.utils;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class ServiceUtils {
/*
 *判断服务的状态
 * @parm servicename 
 * service完整的包名+类名
 * @return
 * 该service  是否运行
 * */
	
	public static Boolean isServiceRunning(Context context,String serviceName){
		boolean isRunning=false;
		
		//判断运行中的服务状态，需要ActivityManader
		ActivityManager am =(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		//获取android手机中运行的所有服务
		List<RunningServiceInfo> runningServices = am.getRunningServices(50);
		
		for (RunningServiceInfo runningServiceInfo : runningServices) {
			//System.out.println(runningServiceInfo.service.getClassName());
			//判断服务的名字是否包含我们指定的服务名
			if (runningServiceInfo.service.getClassName().equals(serviceName)){
				//名字一直，该服务在运行中 
				isRunning = true;
				//已经找到 退出循环
				break;
			}
		}

		return isRunning;
		
	}

}

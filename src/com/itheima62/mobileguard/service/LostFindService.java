package com.itheima62.mobileguard.service;

import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsMessage;

public class LostFindService extends Service {
	
	private SmsReceiver receiver;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		
		return null;
	}
	/**
	 * @author Administrator
	 * 短信的广播接收者
	 */
	private class SmsReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			//实施短信拦截功能
			
			Bundle extras = intent.getExtras();
			
			Object datas[] = (Object[]) extras.get("pdus");
			for (Object data:datas){
				SmsMessage sm = SmsMessage.createFromPdu((byte[]) data);
				//System.out.println(sm.getMessageBody() + ":" + sm.getOriginatingAddress());
				String mess=sm.getMessageBody();//获取短信内容
				if (mess.equals("#*gps*#")) {//由短信内容  判断需要获取定位信息
					//手机需要定位一段时间才能获取定位信息，则 耗时操作应该服务中完成
					Intent service = new Intent(context,LocationService.class);
					startService(service);//启动定位的服务
					System.out.println("gps intent ok");
					abortBroadcast();//终止广播
					}
				else if(mess.equals("#*lockscreen*#")){
					//获得设备管理器
					DevicePolicyManager dpm=(DevicePolicyManager)getSystemService(DEVICE_POLICY_SERVICE);
					//加密设置密码
					dpm.resetPassword("123", 0);
					//一键锁屏
					dpm.lockNow();
					abortBroadcast();//终止广播
				}
				}
		}
		
	}
	
	@Override
	public void onCreate() {
		//短信广播接收者
		receiver = new SmsReceiver();
		IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(Integer.MAX_VALUE);//设置优先级   级别一样 清单文件谁先注册谁先执行，如果级别一样，代码比清单级别高
		//注册短信监听
		registerReceiver(receiver, filter );
		super.onCreate();
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		//
		unregisterReceiver(receiver);
		super.onDestroy();
	}

}

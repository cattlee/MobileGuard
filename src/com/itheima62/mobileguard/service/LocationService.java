package com.itheima62.mobileguard.service;

import java.util.List;

import com.itheima62.mobileguard.utils.MyConstants;
import com.itheima62.mobileguard.utils.SpTools;

import android.app.Service;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
/*
 * 定位的服务管理器，获取定位的信息
 * */
import android.telephony.gsm.SmsManager;

public class LocationService extends Service {
	private LocationManager lm;
	private LocationListener listener;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		System.out.print("Gps service start !");
		lm = (LocationManager) getSystemService(LOCATION_SERVICE);
		listener = new LocationListener() {
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			/* (non-Javadoc)
			 * 位置变化就触发此方法调用  覆盖此方法可以追踪回调结果信息
			 * @see android.location.LocationListener#onLocationChanged(android.location.Location)
			 */
			@Override
			public void onLocationChanged(Location location) {
				// 获取位置变化的结果
				System.out.print("Gps  changed !");
				float accuracy = location.getAccuracy();//精确度
				double altitude = location.getAltitude();//  海拔高度
				double longitude = location.getLongitude();// 经度  
				double latitude = location.getLatitude();// 纬度
				float speed = location.getSpeed();// 速度
				StringBuilder tv_mess=new StringBuilder();
				
				tv_mess.append("accuracy:" + accuracy + "\n");
				tv_mess.append("altitude:" + altitude + "\n");
				tv_mess.append("longitude:" + longitude + "\n");
				tv_mess.append("latitude:" + latitude + "\n");
				tv_mess.append("speed:" + speed + "\n");
				//完成发送短信功能
				String safeNumber=SpTools.getString(LocationService.this, MyConstants.SAFENUMBER,"");
				//发送短信个安全号码,添加发送权限   android.permission.SEND_SMS
				SmsManager sm=SmsManager.getDefault();
				sm.sendTextMessage(safeNumber, "", tv_mess+"", null, null);
				//关闭gps
				stopSelf();//服务中  service关闭自己

			}
		};
		//获取所有定位方式,后利用最佳的定位方式进行定位   模拟器仅支持  gps 和 被动（3g、4g）
		List<String> allProviders = lm.getAllProviders();
		for (String string : allProviders) {
			System.out.println(string+"》》定位方式");
		}
		Criteria criteria=new Criteria();
		criteria.setCostAllowed(true);//产生费用
		criteria.setAccuracy(Criteria.ACCURACY_FINE);//经度
		//动态获取手机的最佳定位方式  Criteria查询
		String bestProvider = lm.getBestProvider(criteria, true);
		/*lm.requestLocationUpdates(provider, minTime, minDistance, listener)  注册监听回调 完成定位信息的更新
		*provider 定位方式   gps  wifi  基站
		*minTime 定位时间差
		*minDistance 定位距离差
		*listener 定位的监听回调
		*/
		lm.requestLocationUpdates(bestProvider, 0, 0, listener);
		super.onCreate();
	}
	@Override
	public void onDestroy() {
		// 取消定位的监听
		lm.removeUpdates(listener);
		lm=null;
		super.onDestroy();
	}

}

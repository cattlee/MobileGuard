package com.itheima62.mobileguard.receiver;

import com.itheima62.mobileguard.utils.EncryptTools;
import com.itheima62.mobileguard.utils.MyConstants;
import com.itheima62.mobileguard.utils.SpTools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.telephony.gsm.SmsManager;
import android.telephony.gsm.SmsMessage;

/*
 * 开机启动的广播接收者
 * 本广播接收者通过清单文件进行注册
 * 由于需要加载广播接收者需要开机  就执行  需加载权限   android.permission.RECEIVE_BOOT_COMPLETED
 * */
public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// 手机启动完成，检测sim卡 是否变化
		//取出保存的sim卡信息  
		//SpTools.getString(context, MyConstants.SIM, "")第二个参数为key  第三个参数为默认值
		String oldSim = SpTools.getString(context, MyConstants.SIM, "");
		//获取当前sim卡信息
		TelephonyManager tm =(TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
		//getSimSerialNumber()获取sim卡序列号
		String simSerialNumber=tm.getSimSerialNumber();
		
		//判断sim卡是否发生变化
		//为了测试 故意更改sim卡 序列号
		if(!oldSim.equals(simSerialNumber+"2")){
			//sim卡发生变化 发送报警短信
			//取出安全号码,号码肯定有
			String safeNumber=SpTools.getString(context, MyConstants.SAFENUMBER,"");
			//解密安全号码
			safeNumber=EncryptTools.decryption(MyConstants.MUSIC, safeNumber);
			//发送短信个安全号码,添加发送权限   android.permission.SEND_SMS
			SmsManager sm=SmsManager.getDefault();
			sm.sendTextMessage(safeNumber, "", "wo shi xiao tou", null, null);
		}
		

	}
	

}

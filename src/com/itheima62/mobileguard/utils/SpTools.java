package com.itheima62.mobileguard.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SpTools {
	//保存字符串数据
	public static void putString(Context context,String key,String value){
		SharedPreferences sp=context.getSharedPreferences(MyConstants.SPFILE, Context.MODE_PRIVATE);
		sp.edit().putString(key, value).commit();//保存数据
	}
	public static String getString(Context context,String key,String deFvalue){
			SharedPreferences sp=context.getSharedPreferences(MyConstants.SPFILE, Context.MODE_PRIVATE);
			return sp.getString(key, deFvalue);//保存数据
	}
	public static void putBoolean(Context context,String key,Boolean value){
		SharedPreferences sp = context.getSharedPreferences(MyConstants.SPFILE, Context.MODE_PRIVATE);
		sp.edit().putBoolean(key, value).commit();//保存数据
	}
	
	public static boolean getBoolean(Context context,String key,boolean defValue){
		SharedPreferences sp = context.getSharedPreferences(MyConstants.SPFILE, Context.MODE_PRIVATE);
		return sp.getBoolean(key, defValue);
	}

}

package com.itheima62.mobileguard.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
//自定义类
public class MyTextView extends TextView {

	public MyTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// context上下文 defstyle 样式    attributeset 属性
	}

	public MyTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	//（category="focus"）  获取焦点
	public boolean isFocused(){
		return true;
	}

}

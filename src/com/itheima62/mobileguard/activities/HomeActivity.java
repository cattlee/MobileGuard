package com.itheima62.mobileguard.activities;


import com.itheima62.mobileguard.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Administrator
 * 主界面
 */
public class HomeActivity extends Activity {
	private GridView gv_menus;//主界面的按钮
	private int icons[] = {R.drawable.safe,R.drawable.callmsgsafe,R.drawable.item_gv_selector_app
			,R.drawable.taskmanager,R.drawable.netmanager,R.drawable.trojan
			,R.drawable.sysoptimize,R.drawable.atools,R.drawable.settings};
	
	private String names[]={"手机防盗","通讯卫士","软件管家","进程管理","流量统计","病毒查杀","缓存清理","高级工具","设置中心"};

	private MyAdapter adapter;//gridview的适配器

	private AlertDialog dialog;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//
		super.onCreate(savedInstanceState);
		initView();//初始化界面
		initData();//给GridView设置数据
	}
/*
 * 初始化组件数据
 * */
	private void initData() {
		adapter = new MyAdapter();
		gv_menus.setAdapter(adapter);//设置gridview适配器数据
	}
	public class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// 指定该adapter包含多少选项
			return icons.length;//数组长度（图标个数）
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// 返回列表项的id 
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			//该方法所返回的view 将作为列表项    
			//第一个参数为上下文，第二个参数为第三个参数为列表项提供数据，这里先初始化为null  让后续通过iv_icon 和 tv_name 完成设置
			View view = View.inflate(getApplicationContext(), R.layout.item_home_gridview, null);
			//获取组件
			ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_item_home_gv_icon);
			TextView tv_name = (TextView) view.findViewById(R.id.tv_item_home_gv_name);
			//设置数据
			//图片
			iv_icon.setImageResource(icons[position]);
			//文字
			tv_name.setText(names[position]);
			
			return view;

		}
		
	}

	private void initView() {
		// 
		setContentView(R.layout.activity_home);
		gv_menus = (GridView) findViewById(R.id.gv_home_menus);
	}
}

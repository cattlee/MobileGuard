package com.itheima62.mobileguard.activities;

import com.itheima62.mobileguard.R;
import com.itheima62.mobileguard.utils.MyConstants;
import com.itheima62.mobileguard.utils.SpTools;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class LostFindActivity extends Activity {

	private AlertDialog dialog;
	private LinearLayout ll_botton_menue;
	private boolean isShowMenue=false;//定义显示menue标记

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//如果第一次访问该界面，要先进入设置向导界面
		if (SpTools.getBoolean(getApplicationContext(), MyConstants.ISSETUP, false)){
			// 进入过设置向导界面，直接显示本界面
			
			initView();//手机防盗界面
			
		} else {
			//要进入设置向导界面
			Intent intent = new Intent(this,Setup1Activity.class);
			startActivity(intent);
			finish();//关闭自己  如果操作取消 则直接进入主界面
		}
	}
	
	
/*
 * 重新进入设置向导界面
 * */
	public void enterSetup(View view){
		Intent setup1=new Intent(this,Setup1Activity.class);
		startActivity(setup1);
		finish();
	}
	
	private void initView() {
		setContentView(R.layout.activity_lostfind);
		ll_botton_menue = (LinearLayout) findViewById(R.id.ll_lostfind_menu_bottom);
	}
	/*
	 * 创建菜单的方法
	 * */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub

		return true;
	}
	
	/*
	 * 处理菜单事件
	 * */
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.mn_modify_name:
			Toast.makeText(getApplicationContext(), "修改菜单名", 1).show();
			//弹出对话框，让用户输入新的手机防盗名
			showModifyNameDialog();
			break;
		case R.id.mn_test_menu:
			Toast.makeText(getApplicationContext(), "测试菜单", 1).show();
		default:
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

/*
 * 修改手机防盗名的对话框
 * */
	private void showModifyNameDialog() {
		AlertDialog.Builder ab = new AlertDialog.Builder(this);
		//对话框显示的界面
		View view=View.inflate(getApplicationContext(),R.layout.dialog_modify_name,null);
		ab.setView(view);//设置view
		
		//处理界面的事件
		final EditText et_name=(EditText)view.findViewById(R.id.et_dialog_lostfind_modify_name);
		Button bt_modify=(Button) view.findViewById(R.id.bt_dialog_lostfind_modify); 
		Button bt_cancel=(Button) view.findViewById(R.id.bt_dialog_enter_password_cancel);
		
		//处理按钮事件
		bt_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();//对话框消失
				
			}
		});
		bt_modify.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v) {
				//获取修改的名字
				String name =et_name.getText().toString().trim();
				if(TextUtils.isEmpty(name)){
				Toast.makeText(getApplicationContext(), "名字不可为空", 1).show();
				return;
				
			}
			//保存新名字到sp中
			SpTools.putString(getApplicationContext(),MyConstants.LOSTFINDNAME,name);
			dialog.dismiss();
			Toast.makeText(getApplicationContext(), "名字修改成功", 1).show();
			}
		});
		
		dialog = ab.create();//创建对话框
		dialog.show();

	}
	//处理menue键的事件
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_MENU){
			if (isShowMenue) {
			//显示菜单
				ll_botton_menue.setVisibility(View.VISIBLE);
			}
			else{
				//不显示菜单
				ll_botton_menue.setVisibility(View.GONE);
			}
			isShowMenue=!isShowMenue;
		}
		return super.onKeyDown(keyCode, event);
	}
}


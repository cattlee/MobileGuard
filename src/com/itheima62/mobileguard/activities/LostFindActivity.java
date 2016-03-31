package com.itheima62.mobileguard.activities;

import com.itheima62.mobileguard.R;
import com.itheima62.mobileguard.utils.MyConstants;
import com.itheima62.mobileguard.utils.SpTools;
import com.lidroid.xutils.view.annotation.ContentView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class LostFindActivity extends Activity {

	private AlertDialog dialog;
	private LinearLayout ll_botton_menue;
	private boolean isShowMenue=false;//定义显示menue标记
	private View popupview;
	private ScaleAnimation sa;
	private PopupWindow pw;
	private RelativeLayout rl_root;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//如果第一次访问该界面，要先进入设置向导界面
		if (SpTools.getBoolean(getApplicationContext(), MyConstants.ISSETUP, false)){
			// 进入过设置向导界面，直接显示本界面
			
			initView();//手机防盗界面
			initPopupView();//初始化修改名字的界面
			initPopupWindow();//初始化弹出窗口
			
		} else {
			//要进入设置向导界面
			Intent intent = new Intent(this,Setup1Activity.class);
			startActivity(intent);
			finish();//关闭自己  如果操作取消 则直接进入主界面
		}
	}
	
	
/**
 * 初始化弹出窗口
 */
private void initPopupWindow() {
	//弹出窗体
		PopupWindow pw=new PopupWindow(popupview,-2,-2);
		pw.setFocusable(true);//获取焦点
		//窗口显示动画
		sa = new ScaleAnimation(1, 1,0, 1,Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0f);
		sa.setDuration(1000);
		
		
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
		//根部局
		rl_root=(RelativeLayout) findViewById(R.id.rl_lostfind_root);
		
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
		
		
		dialog = ab.create();//创建对话框
		dialog.show();

	}
	private void initPopupView(){
		popupview = View.inflate(getApplicationContext(),R.layout.dialog_modify_name,null);
				
				//处理界面的事件
				final EditText et_name=(EditText)popupview.findViewById(R.id.et_dialog_lostfind_modify_name);
				Button bt_modify=(Button) popupview.findViewById(R.id.bt_dialog_lostfind_modify); 
				Button bt_cancel=(Button) popupview.findViewById(R.id.bt_dialog_enter_password_cancel);
				
				//处理按钮事件
				bt_cancel.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						pw.dismiss();//让弹出的窗口消失
						
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
					pw.dismiss();
					Toast.makeText(getApplicationContext(), "名字修改成功", 1).show();
					}
				});
	}
	//处理menue键的事件
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			if (pw != null && pw.isShowing()) {
				pw.dismiss();
			} else {

				// 设置弹出窗体的背景
				pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
				popupview.startAnimation(sa);
				// 设置弹出窗体显示的位置
				int height = getWindowManager().getDefaultDisplay().getHeight();
				int width = getWindowManager().getDefaultDisplay().getWidth();
				//设置弹出窗口的位置
				pw.showAtLocation(rl_root, Gravity.LEFT | Gravity.TOP,
						width / 4, height / 4);

			}
		}

		/*if(keyCode==KeyEvent.KEYCODE_MENU){
			if (isShowMenue) {
			//显示菜单
				ll_botton_menue.setVisibility(View.VISIBLE);
			}
			else{
				//不显示菜单
				ll_botton_menue.setVisibility(View.GONE);
			}
			isShowMenue=!isShowMenue;
		}*/
		return super.onKeyDown(keyCode, event);
	}
}


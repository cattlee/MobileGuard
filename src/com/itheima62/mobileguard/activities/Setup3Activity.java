package com.itheima62.mobileguard.activities;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.itheima62.mobileguard.R;
import com.itheima62.mobileguard.utils.EncryptTools;
import com.itheima62.mobileguard.utils.MyConstants;
import com.itheima62.mobileguard.utils.SpTools;

/**
 * @author Administrator
 * 第一个设置向导界面
 */
public class Setup3Activity extends BaseSetupActivity {
	private EditText et_safeNumber;//安全号码的编辑框
	
	/* (non-Javadoc)
	 * 子类覆盖此方法来完成组件数据的初始化
	 * @see com.itheima62.mobileguard.activities.BaseSetupActivity#initData()
	 */
	@Override
	public void initData() {
		//解密安全号码 并显示出来
		String safenumber=SpTools.getString(getApplicationContext(), MyConstants.SAFENUMBER, "");
		et_safeNumber.setText(EncryptTools.decryption(MyConstants.MUSIC, safenumber));
		super.initData();  
	}

	/**
	 * 子类需要覆盖此方法，来完成界面的显示
	 */
	public void initView() {
		setContentView(R.layout.activity_setup3);
		//安全号码的编辑框
		et_safeNumber = (EditText) findViewById(R.id.et_setup3_safenumber);
		
	}
	
	/**
	 * 从手机联系人里获取安全号码
	 * @param v
	 */
	public void selectSafeNumber(View v){
		//弹出新的Activity来显示所有好友信息
		Intent friends = new Intent(this,FriendsActivity.class);
		//一次性完成  跳转到目标页面 完成目标界面任务后，自动跳转到当前页面
		startActivityForResult(friends,1);//启动显示好友界面
		//获取好友数据关闭界面 回到获取联系人界面
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (data != null) {//用户选择数据来关闭联系人界面,而不是直接点击返回按钮
			//取数据
			String phone = data.getStringExtra(MyConstants.SAFENUMBER);
			//显示安全号码
			et_safeNumber.setText(phone);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/* (non-Javadoc)
	 * 覆盖父类的方法，完成业务 
	 * @see com.itheima62.mobileguard.activities.BaseSetupActivity#next(android.view.View)
	 */
	@Override
	public void next(View v) {
		//保存安全号码
		
		//获取安全号码
		String safeNumber = et_safeNumber.getText().toString().trim();
		
		//如果安全号码，下一步不进行页面的跳转
		if (TextUtils.isEmpty(safeNumber)){
			//为空
			Toast.makeText(getApplicationContext(), "安全号码不能为空", 1).show();
			//不调用父类的功能来进行页面的切换
			return;
		} else {
			//对安全号码加密
			safeNumber=EncryptTools.encrypt(MyConstants.MUSIC, safeNumber);
			//保存安全号码   将加密以后的安全号码 以键值对的形式保存在utils  myconstants.java 中 
			SpTools.putString(getApplicationContext(), MyConstants.SAFENUMBER, safeNumber);
		}
		
		//调用父类功能完成页面的切换
		super.next(v);
	}

	@Override
	public void nextActivity() {
		// TODO Auto-generated method stub
		startActivity(Setup4Activity.class);
	}

	@Override
	public void prevActivity() {
		// TODO Auto-generated method stub
		startActivity(Setup2Activity.class);
	}
}

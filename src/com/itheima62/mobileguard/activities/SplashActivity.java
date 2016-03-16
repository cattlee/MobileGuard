package com.itheima62.mobileguard.activities;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.Menu;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima62.mobileguard.R;
import com.itheima62.mobileguard.domain.UrlBean;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

public class SplashActivity extends Activity {

	private static final int LOADMAIN = 1;//加载主界面
	private static final int SHOWUPDATEDIALOG = 2;//显示是否更新的对话框
	private RelativeLayout rl_root;// 界面的根布局组件
	private int versionCode;// 版本号
	private String versionName;// 版本名
	private TextView tv_versionName;// 显示版本名的组件
	private UrlBean parseJson;//url信息封装bean
	private long startTimeMillis;//记录开始访问网络的时间

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 初始化界面
		initView();
		// 初始化数据
		initData();
		// 初始化动画
		initAnimation();
		// 检查服务器的版本
		checkVerion();

	}

	private void initData() {
		// 获取自己的版本信息
		PackageManager pm = getPackageManager();
		try {
			PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
			// 版本号			
			versionCode = packageInfo.versionCode;
			// 版本名			
			versionName = packageInfo.versionName;

			// 设置textview
			tv_versionName.setText(versionName);
		} catch (NameNotFoundException e) {
			// can not reach 异常不会发生
		}
	}

	private void checkVerion() {
		// 耗时操作都要放到子线程中执行
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					startTimeMillis = System.currentTimeMillis();//毫秒显示当前时间
					URL url = new URL("http://10.0.2.2:8080/guardversion.json");
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					// 读取数据的超时
					conn.setReadTimeout(5000);
					// 网络连接超时
					conn.setConnectTimeout(5000);
					// 设置请求方式
					conn.setRequestMethod("GET");
					// 获取相应结果
					int code = conn.getResponseCode();
					if (code == 200) {// 数据获取成功
						// 获取读取的字节流
						InputStream is = conn.getInputStream();
						// 把字节流转换成字符流
						BufferedReader bfr = new BufferedReader(
								new InputStreamReader(is));
						// 读取一行信息
						String line = bfr.readLine();
						// json字符串数据的封装
						StringBuilder json = new StringBuilder();
						while (line != null) {
							json.append(line);
							line = bfr.readLine();
						}
						//解析json数据，处理JSON异常
						parseJson = parseJson(json);//返回数据的封装信息
						isNewVersion(parseJson);// 是否有新版本
						System.out.println(parseJson.getVersionCode()+"版本号");

						bfr.close();
						conn.disconnect();
					}
				} catch (MalformedURLException e) {//异常提示4002
					e.printStackTrace();
					System.out.println("4002 URL格式错误" );
				} catch (IOException e) {//异常提示4001
					e.printStackTrace();
					System.out.println("4001 网络连接错误" );
				} catch (JSONException e) {
					// 4003
					e.printStackTrace();
					System.out.println("4003 JSON格式错误" );
				}

			}
		}).start();
	}
	
	
	/*
	 * Handler主要接受子线程发送的数据， 并用此数据配合主线程更新UI。
	 * */
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			//处理消息
			switch (msg.what) {
			case LOADMAIN://加载主界面
				loadMain();
				break;
			case SHOWUPDATEDIALOG://显示更新版本的对话框
				showUpdateDialog();
				break;
			default:
				break;
			}
		}

	};

	private void loadMain() {
		Intent intent = new Intent(SplashActivity.this,HomeActivity.class);
		startActivity(intent);//进入主界面
		finish();//关闭自己
	};
	/**
	 * 在子线程中执行，不可做ui操作，故由handler传输数据
	 * @param parseJson
	 */
	protected void isNewVersion(UrlBean parseJson) {
		int serverCode = parseJson.getVersionCode();// 获取服务器的版本
		long endTimeMillis = System.currentTimeMillis();//isNewVersion（）执行结束的时间
		if (endTimeMillis - startTimeMillis < 3000) {
			//设置休眠的时间，保证至少休眠3秒
			SystemClock.sleep(3000 - (endTimeMillis - startTimeMillis));
		}
		if (serverCode == versionCode){//版本一致
			
			//进入主界面
			Message msg = Message.obtain();//获取msg  或者通过new 获取
			msg.what = LOADMAIN;
			handler.sendMessage(msg);
		} else {//有新版本  
			//弹出对话框，显示新版本的描述信息，让用户点击是否更新
			Message msg = Message.obtain();
			msg.what = SHOWUPDATEDIALOG;
			handler.sendMessage(msg);
		}
	}
	
	/**
	 * 显示是否更新新版本的对话框
	 */
	protected void showUpdateDialog() {
		//对话框的上下文 是Activity的class,AlertDialog是Activity的一部分
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		//让用户禁用取消操作(防止用户取消退出更新对话框出现bug)处理方法1
		//builder.setCancelable(false);
		//处理方法2 设置用户取消事件
		builder.setOnCancelListener(new OnCancelListener(){
		
			@Override
			public void onCancel(DialogInterface dialog) {
				// 有对话框时，用户取消，事件处理——》 进入主界面
				loadMain();
			}
		})     .setTitle("提醒")
		       .setMessage("是否更新新版本？新版本的具有如下特性：" + parseJson.getDesc())
		       .setPositiveButton("更新", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					//更新apk
					System.out.println("更新apk");
					//访问网络，下载新的apk
					downLoadNewApk();//下载新版本
				}
			}).setNegativeButton("取消", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					//进入主界面
					loadMain();
				}
			});
		builder.show();//显示对话框
	}
	/**
	 * 新版本的下载安装 利用xutil工具类
	 */
	protected void downLoadNewApk() {
		HttpUtils utils = new HttpUtils();//，httputils封装了子线程，获取了回调结果，故不需要再设置线程再设置handler处理消息
		//parseJson.getUrl() 下载的url
		// target  apk下载到的位置（本地路径）第三个参数 回调结果
		System.out.println(parseJson.getUrl());
		utils.download(parseJson.getUrl(), "/mnt/sdcard/xx.apk", new RequestCallBack<File>() {
			
			@Override
			public void onSuccess(ResponseInfo<File> arg0) {
				//下载成功
				//在主线程中执行
				Toast.makeText(getApplicationContext(), "下载新版本成功", 1).show();
				//安装apk
				installApk();//安装apk
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				//下载失败
				Toast.makeText(getApplicationContext(), "下载新版本失败", 1).show();
			}
		});
	}
/*
 * 安装的新版本
 * */
	protected void installApk() {
		/*<intent-filter>
        <action android:name="android.intent.action.VIEW" />
        <category android:name="android.intent.category.DEFAULT" />
        <data android:scheme="content" />
        <data android:scheme="file" />
        <data android:mimeType="application/vnd.android.package-archive" />
    </intent-filter>*/
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		String type = "application/vnd.android.package-archive";
		Uri data = Uri.fromFile(new File("/mnt/sdcard/xx.apk"));
		intent.setDataAndType(data , type);
		startActivityForResult(intent,0);

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//如果用户取消更新apk，那么直接进入主界面
		loadMain();
		super.onActivityResult(requestCode, resultCode, data);
	}


	/**
	 * @param jsonString
	 *            url的json数据,throws JSONException 则谁调用该方法 谁处理异常
	 * @return url信息封装对象
	 */
	protected UrlBean parseJson(StringBuilder jsonString) throws JSONException{
		UrlBean bean = new UrlBean();
		JSONObject jsonObj;
			// {"version":"2","url":"http://10.0.2.2:8080/xxx.apk","desc":"增加了防病毒功能"}
			jsonObj = new JSONObject(jsonString + "");
			int versionCode = jsonObj.getInt("version");
			String url = jsonObj.getString("url");
			String desc = jsonObj.getString("desc");
			// 封装结果数据
			bean.setDesc(desc);
			bean.setUrl(url);
			bean.setVersionCode(versionCode);

		return bean;
	}

	/**
	 * 初始化界面
	 */
	private void initView() {
		setContentView(R.layout.activity_main);
		rl_root = (RelativeLayout) findViewById(R.id.rl_splash_root);
		tv_versionName = (TextView) findViewById(R.id.tv_splash_version_name);
	}

	/**
	 * 动画显示
	 */
	private void initAnimation() {
		// showAlpha();
		// Alpha动画0.0完全透明
		AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
		// 设置动画播放的时间毫秒为3000?
		aa.setDuration(3000);
		// 界面停留在动画结束状态 
		aa.setFillAfter(true);
		RotateAnimation rotate = new RotateAnimation(0, 360,
				// 设置锚点
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		// 显示时间
		rotate.setDuration(3000);
		// 界面停留在结束状 
		rotate.setFillAfter(true);
		ScaleAnimation sa = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
				// 设置锚点
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		// 显示时间
		sa.setDuration(3000);
		// 界面停留在结束状态
		sa.setFillAfter(true);
		// 创建动画
		AnimationSet as = new AnimationSet(true);
		as.addAnimation(sa);
		as.addAnimation(rotate);
		as.addAnimation(aa);
		// 同时播放动画
		rl_root.startAnimation(as);

	}

	/**
	 * Alpha显示
	 */
	private void showAlpha() {
		// Alpha动画0.0完全透明
		AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
		// 设置动画播放的时间3000毫秒
		aa.setDuration(3000);
		// 界面停留在动画结束状态
		aa.setFillAfter(true);
		// 给组件设置动画
		rl_root.startAnimation(aa);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

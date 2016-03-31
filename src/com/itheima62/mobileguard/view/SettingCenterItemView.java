package com.itheima62.mobileguard.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itheima62.mobileguard.R;

/**
 * @author Administrator
 * �Զ�����Ͽؼ�
 */
public class SettingCenterItemView extends LinearLayout {

	

	private TextView tv_title;
	private TextView tv_content;
	private CheckBox cb_check;
	private String[] contents;
	private View item;
	/**
	 * �����ļ��У�����ʵ�����������Բ���
	 * @param context
	 * @param attrs
	 */
	public SettingCenterItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
		initEvent();
		String content = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.itheima62.mobileguard", "content");
		
		String title = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.itheima62.mobileguard", "title");
		
		tv_title.setText(title);
		
		contents = content.split("-");
		
	}
	
	
	

	/**
	 * ��ʼ����ѡ���¼�
	 */
	private void initEvent() {
		//item ��Բ���
		item.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				cb_check.setChecked(!cb_check.isChecked());
			}
		});
		
		// TODO Auto-generated method stub
		cb_check.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					//����ѡ�е���ɫΪ��ɫ
					tv_content.setTextColor(Color.GREEN);
					tv_content.setText(contents[1]);
				} else {
					//����ѡ�е���ɫΪ��ɫ
					tv_content.setTextColor(Color.RED);
					tv_content.setText(contents[0]);
				}
			}
		});
	}



	/**
	 * ��ʼ��LinearLayout�������
	 */
	private void initView(){
		item = View.inflate(getContext(), R.layout.item_settingcenter_view, null);
		//��ʾ����
		tv_title = (TextView) item.findViewById(R.id.tv_settingcenter_autoupdate_title);
		//��ʾ������
		tv_content = (TextView) item.findViewById(R.id.tv_settingcenter_autoupdate_content);
		//���ø�ѡ��
		cb_check = (CheckBox) item.findViewById(R.id.cb_settingcenter_autoupdate_checked);
		
		addView(item);
	}
	/**
	 * ����ʵ�������øù��캯��
	 * @param context
	 */
	public SettingCenterItemView(Context context) {
		super(context);
		initView();
	}

}
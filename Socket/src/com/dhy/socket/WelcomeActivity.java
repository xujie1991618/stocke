package com.dhy.socket;

import com.cn.util.Const;
import com.zxing.activity.CaptureActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class WelcomeActivity extends Activity{
	
	Button server;//选择服务器
	Button client;//选择客户端
	ImageView question;//问号提示
	EditText login_name;//用户名
	private long exitTime=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose);
		findById();
		initseting();
	}

	private void initseting() {
		// TODO Auto-generated method stub
		server.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent(WelcomeActivity.this,ServerActivity.class);
				if(login_name.getText().toString().trim().equals(""))
					Const.nickname="111";
				else
					Const.nickname=login_name.getText().toString().trim();
				startActivity(i);
			}
		});
		client.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(login_name.getText().toString().trim().equals(""))
					Const.nickname="222";
				else
					Const.nickname=login_name.getText().toString().trim();
				Intent openCameraIntent = new Intent(WelcomeActivity.this,CaptureActivity.class);
				startActivityForResult(openCameraIntent, 0);
				// TODO Auto-generated method stub
				
			}
		});
		question.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(WelcomeActivity.this, R.string.Default, Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//处理扫描结果（在界面上显示）
		if (resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");
			
			Intent i= new Intent(WelcomeActivity.this,ClientActivity.class);
			i.putExtra("ipAdd", scanResult);
			startActivity(i);
//			resultTextView.setText(scanResult);
		}
	}
	
	private void findById() {
		// TODO Auto-generated method stub
		server=(Button)findViewById(R.id.server);
		client=(Button)findViewById(R.id.client);
		question=(ImageView)findViewById(R.id.question);
		login_name=(EditText)findViewById(R.id.login_name);
	}
	
@Override
public boolean onKeyDown(int keyCode, KeyEvent event) {
//	if (keyCode == KeyEvent.KEYCODE_BACK) {
//	moveTaskToBack(true);  
//    return true; 
//}
	
if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){  
	
    if((System.currentTimeMillis()-exitTime) > 2000){  
        Toast.makeText(WelcomeActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();                                
        exitTime = System.currentTimeMillis();   
    } else {
        finish();
        System.exit(0);
    }
    return true;   
}
return super.onKeyDown(keyCode, event);
}

}

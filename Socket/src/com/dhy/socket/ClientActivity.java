package com.dhy.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cn.util.Const;
import com.dhy.adapter.MyAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ClientActivity extends Activity {

	private EditText edi;
	private TextView ipEt;
	private Button but;
	private Button startbut;
	private Button file;
//	Button Connect;
	private Button cls;
	private ListView list;
	private PrintWriter out;
	private BufferedReader in;
	private String out_content;
	private String input_content;
	private Socket socket;
	
	private String ipAdd;
	
	private String defaultIp="";
	private int defaultPort=0;
	
	private TextView tx;
	private LinearLayout lyImage;
	private ImageView image;
//	ArrayList<String> slist = new ArrayList<String>();
	
	private List<Map<String, String>> mData= new ArrayList<Map<String, String>>();
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {

			switch (msg.what) {

			case 0:
				ipEt.setText(ipAdd+":"+defaultPort);
				ipEt.setEnabled(false);
				startbut.setVisibility(View.GONE);
//				Connect.setVisibility(View.GONE);
				
				// msg.obj是获取handler发送信息传来的数据
//				slist = (ArrayList<String>) msg.obj;
				// 给ListView绑定数据
				initseting();
				break;
			case 1:

				edi.setText("");
				// edi.setHint("")
				// msg.obj是获取handler发送信息传来的数据
//				slist = (ArrayList<String>) msg.obj;
				// 给ListView绑定数据
				initseting();
				break;
			case 2:

				// msg.obj是获取handler发送信息传来的数据
//				slist = (ArrayList<String>) msg.obj;
				// 给ListView绑定数据
				
				
				initseting();
				break;
			default:
				break;
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// getSocket();
		findById();
		initsending();
	}

	private void initseting() {
		
		MyAdapter adapter=new MyAdapter(this, mData);
		list.setAdapter(adapter);
//		listBaseAdapter listAdapter = new listBaseAdapter();
//		list.setAdapter(listAdapter);
//		listAdapter.notifyDataSetChanged();
	}

	private void initsending() {
		
		
		String ip=getIntent().getStringExtra("ipAdd");
		System.out.println(ip);
		Log.e("out", ip);
//		ipEt.setText(ip);
		if(!ip.equals("")){
			String[] str=new String[2];
			str=ip.split(":");
			
			defaultIp=str[0];
			defaultPort=Integer.parseInt(str[1]);
		new connectThreah().start();
		}
		// TODO Auto-generated method stub
//		Connect.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				new connectThreah().start();
//			}
//		});
		but.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new clientThreah().start();

			}
		});
		file.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new clientfileThreah().start();
			}
		});
		cls.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
					if (!mData.isEmpty()) {
						mData.clear();
					Message msg2 = mHandler.obtainMessage();
					msg2.what = 2;
					mHandler.sendMessage(msg2);// 发送数据

				}

			}
		});

	}

	/**
	 * 发送数据到服务器
	 * 
	 * @author
	 * 
	 */
	class clientThreah extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				// Socket = new Socket("192.168.1.108", 10230);
				out_content = edi.getText().toString().trim();
				out_content=Const.nickname+"----"+out_content;
				if (!out_content.equals("")){
//					slist.add("客户端：" + out_content);
					Map<String , String> map=new HashMap<String, String>();
					map.put("name","me");
					map.put("content", out_content);
					mData.add(map);	
				}
				System.out.println("客户端连接成功");
				// 向服务发送数据
				out = new PrintWriter(socket.getOutputStream(), true);
				out.println(out_content);

				Message msg = mHandler.obtainMessage();
				msg.what = 1;
				mHandler.sendMessage(msg);// 发送数据
			} catch (IOException e) {
				e.printStackTrace();
				out.close();
			}
			super.run();
		}

	}
	/**
	 * 发送文件数据到服务器
	 * 
	 * @author
	 * 
	 */
	class clientfileThreah extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				// Socket = new Socket("192.168.1.108", 10230);
				
				if (!out_content.equals("")){
//					slist.add("客户端：" + out_content);
					Map<String , String> map=new HashMap<String, String>();
					map.put("name","me");
					map.put("content", out_content);
					mData.add(map);	
				}
				System.out.println("客户端连接成功");
				// 向服务发送数据
				out = new PrintWriter(socket.getOutputStream(), true);
				out.println(out_content);

				Message msg = mHandler.obtainMessage();
				msg.what = 1;
				mHandler.sendMessage(msg);// 发送数据
			} catch (IOException e) {
				e.printStackTrace();
				out.close();
			}
			super.run();
		}

	}
	/**
	 * 连接服务器
	 * 
	 * @author
	 * 
	 */
	class connectThreah extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			
			try {
				String ipstring = ipEt.getText().toString().trim();
				int portint =0;
//				Integer
//						.parseInt(portEt.getText().toString().trim());
				
				System.out.println(ipstring + portint);
				ipstring=defaultIp;
				portint=defaultPort;
				socket = new Socket(ipstring, portint);
				System.out.println("建立连接成功");
//				slist.add("建立连接成功");
				Map<String , String> map=new HashMap<String, String>();
				map.put("name","sys");
				map.put("content", "建立连接成功");
				mData.add(map);
				ipAdd=socket.getInetAddress().toString();
				Message msg1 = mHandler.obtainMessage();
				msg1.what = 0;
				mHandler.sendMessage(msg1);// 发送数据
				while (true) {
					if(socket.getInputStream().available()!=0){
						in = new BufferedReader(new InputStreamReader(
								socket.getInputStream()));
						input_content = in.readLine();
						System.out.println("Server回复的消息:  " + input_content);
						if (!input_content.equals("")){
	//						slist.add("服务器：" + input_content);
							Map<String , String> map2=new HashMap<String, String>();
							map2.put("name","him");
							map2.put("content", input_content);
							mData.add(map2);
						}
							
						Message msg3 = mHandler.obtainMessage();
						msg3.what = 2;
						mHandler.sendMessage(msg3);// 发送数据
					}
				}
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					if(in!=null)
						in.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			super.run();
		}

	}

	private void findById() {
		// TODO Auto-generated method stub
		edi = (EditText) findViewById(R.id.edi);
		ipEt = (TextView) findViewById(R.id.ip_et);
//		portEt = (EditText) findViewById(R.id.port);
		but = (Button) findViewById(R.id.send);
//		Connect = (Button) findViewById(R.id.Connect);
		
		startbut = (Button) findViewById(R.id.start);
		file=(Button)findViewById(R.id.file);
		cls = (Button) findViewById(R.id.btn_clear);
		list = (ListView) findViewById(R.id.list);
		
		tx = (TextView) findViewById(R.id.image_tx);
		image = (ImageView) findViewById(R.id.iv_qr_image);
		lyImage = (LinearLayout) findViewById(R.id.layout_image);
		
		
		lyImage.setVisibility(View.GONE);
	}

	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

			if (!socket.isClosed()) {
				try {
					socket.close();

				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		}
		finish();
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	final static class ViewHolder {
		TextView tTitle;
	}
}

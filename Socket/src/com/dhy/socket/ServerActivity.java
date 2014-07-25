package com.dhy.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.conn.util.InetAddressUtils;

import com.cn.util.Const;
import com.dhy.adapter.MyAdapter;
import com.google.zxing.WriterException;
import com.zxing.encoding.EncodingHandler;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
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

public class ServerActivity extends Activity {
	private PrintWriter out;
	private int defaultPort = 10230;
	private ServerSocket serverSocket = null;// 声明
	private InputStreamReader input;
	private BufferedReader in;
	private Button startbut;
	private ListView list;
	private EditText edi;
	private TextView ipEt;
	private Button but, cls;
	private String input_content;
	private String out_content;

	private TextView tx;
	private LinearLayout lyImage;
	private ImageView image;
	private Socket socket;
	private String ipAdd = "";
	ArrayList<String> slist = new ArrayList<String>();
	
	private List<Map<String, String>> mData= new ArrayList<Map<String, String>>();
//	private Map<String , String> map=new HashMap<String, String>();
	
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {

			switch (msg.what) {

			case -1:

				// port.setEnabled(false);
				// 生成本机的IP地址和随机设定的端口号 的 二维码图片
				tx.setVisibility(View.VISIBLE);
				Bitmap bt = initIpImage();
				if (bt != null) {
					image.setImageBitmap(initIpImage());
				} else {
					tx.setText("生成二维码失败！！");
				}

				// msg.obj是获取handler发送信息传来的数据
//				slist = (ArrayList<String>) msg.obj;
//				mData = (List<Map<String, String>>) msg.obj;
				// 给ListView绑定数据
				initseting();
				break;

			case 0:
				ipEt.setText(ipAdd + ":" + defaultPort);
				startbut.setVisibility(View.GONE);
				lyImage.setVisibility(View.GONE);
				// msg.obj是获取handler发送信息传来的数据
//				slist = (ArrayList<String>) msg.obj;
//				mData = (List<Map<String, String>>) msg.obj;
				// 给ListView绑定数据
				initseting();
				break;
			case 1:

				edi.setText("");
				// edi.setHint("")
				// msg.obj是获取handler发送信息传来的数据
//				slist = (ArrayList<String>) msg.obj;
//				mData = (List<Map<String, String>>) msg.obj;
				// 给ListView绑定数据
				initseting();
				break;

			case 2:

				// msg.obj是获取handler发送信息传来的数据
//				slist = (ArrayList<String>) msg.obj;
//				mData = (List<Map<String, String>>) msg.obj;
				// 给ListView绑定数据
				initseting();
				break;
			default:
				break;
			}
		}
	};

	public Bitmap initIpImage() {

		Bitmap bt = null;
		String str = "";

		str = getLocalHostIp();
		str += ":" + defaultPort;
		// edi.setText(str);
		try {
			bt = EncodingHandler.createQRCode(str, 350);
		} catch (WriterException e) {
			e.printStackTrace();
		}

		return bt;

	}

	
	private String getLocalHostIp(){  
	    WifiManager wm=(WifiManager)getSystemService(Context.WIFI_SERVICE);  
	    //检查Wifi状态    
	    if(!wm.isWifiEnabled())  
	        wm.setWifiEnabled(true);  
	    WifiInfo wi=wm.getConnectionInfo();  
	    //获取32位整型IP地址    
	    int ipAdd=wi.getIpAddress();  
	    //把整型地址转换成“*.*.*.*”地址    
	    String ip=intToIp(ipAdd);  
	    return ip;  
	}  
	private String intToIp(int i) {  
	    return (i & 0xFF ) + "." +  
	    ((i >> 8 ) & 0xFF) + "." +  
	    ((i >> 16 ) & 0xFF) + "." +  
	    ( i >> 24 & 0xFF) ;  
	} 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		startbut = (Button) findViewById(R.id.start);
		startbut.setVisibility(View.VISIBLE);
		edi = (EditText) findViewById(R.id.edi);
		but = (Button) findViewById(R.id.send);
		cls = (Button) findViewById(R.id.btn_clear);
		list = (ListView) findViewById(R.id.list);

		ipEt = (TextView) findViewById(R.id.ip_et);

		tx = (TextView) findViewById(R.id.image_tx);
		image = (ImageView) findViewById(R.id.iv_qr_image);
		lyImage = (LinearLayout) findViewById(R.id.layout_image);
		// Bitmap qrCodeBitmap = EncodingHandler.createQRCode(contentString,
		// 350);

		// lyImage.setVisibility(visibility)

		startbut.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new serverThreah().start();
			}
		});
		but.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (socket != null)
					new sendThreah().start();
			}
		});

		cls.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				if (!slist.isEmpty()) {
				if (!mData.isEmpty()) {
//					slist.clear();
					mData.clear();
					Message msg2 = mHandler.obtainMessage();
					msg2.what = 2;
//					msg2.obj = slist;
//					msg2.obj=mData;
					mHandler.sendMessage(msg2);// 发送数据

				}

			}
		});

	}

	private void initseting() {
		
		MyAdapter adapter=new MyAdapter(this, mData);
		list.setAdapter(adapter);
//		adapter.notifyDataSetChanged();
//		listBaseAdapter listAdapter = new listBaseAdapter();
//		list.setAdapter(listAdapter);
	}

	/**
	 * 发送数据到客户端
	 * 
	 * @author
	 * 
	 */
	class sendThreah extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub

			try {
				out_content = edi.getText().toString().trim();
				out_content=Const.nickname+"----"+out_content;
				if (!out_content.equals("")){
//					slist.add("服务端：" + out_content);
					Map<String , String> map=new HashMap<String, String>();
					map.put("name","me");
					map.put("content", out_content);
					mData.add(map);
					}
				out = new PrintWriter(socket.getOutputStream(), true);
				out.println(out_content); // 信息写入到输出流， 向客户端发信息

				Message msg2 = mHandler.obtainMessage();
				msg2.what = 1;
//				msg2.obj = slist;
//				msg2.obj=mData;
				mHandler.sendMessage(msg2);// 发送数据
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			super.run();
		}

	}

	/**
	 * 启动服务
	 * **/
	class serverThreah extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				// int portint = 0;
				// Integer.parseInt(port.getText().toString().trim());
				serverSocket = new ServerSocket(defaultPort);// 创建
				System.out.println("等待客户端连接");
//				slist.add("等待客户端连接...");
				Map<String , String> map=new HashMap<String, String>();
				map.put("name","sys");
				map.put("content", "等待客户端连接...");
				mData.add(map);
				Message msg0 = mHandler.obtainMessage();
				msg0.what = -1;
//				msg0.obj = slist;
//				msg0.obj=mData;
				mHandler.sendMessage(msg0);// 等待客户端连接

				socket = serverSocket.accept();// 阻塞的方法
//				slist.add("服务器连接成功");
				Map<String , String> map2=new HashMap<String, String>();
				map2.put("name","sys");
				map2.put("content", "服务器连接成功");
				mData.add(map2);
				ipAdd = socket.getInetAddress().toString();
				Message msg1 = mHandler.obtainMessage();
				msg1.what = 0;
//				msg1.obj = slist;
//				msg1.obj=mData;
				mHandler.sendMessage(msg1);// 发送数据
				while (true) {
					if(socket.getInputStream().available()!=0){
						input = new InputStreamReader(socket.getInputStream());
						in = new BufferedReader(input);
						input_content = in.readLine();// 获取输入流中信息（接收客户端发来数据）
						if (!input_content.equals("")){
	//						slist.add("客户端：" + input_content);
							Map<String , String> map3=new HashMap<String, String>();
							map3.put("name","him");
							map3.put("content", input_content);
							mData.add(map3);
						
						}
						Message msg2 = mHandler.obtainMessage();
						msg2.what = 2;
	//					msg2.obj = slist;
	//					msg2.obj=mData;
						mHandler.sendMessage(msg2);// 发送数据
						System.out.println("Client发送的消息是:  " + input_content);
					}
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			super.run();
		}

	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if(serverSocket!=null)
			if (!serverSocket.isClosed()) {
				try {
					serverSocket.close();
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

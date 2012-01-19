package com.ztm;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.sonyericsson.zoom.ImageTextButton;
import com.sonyericsson.zoom.ImageZoomView;
import com.sonyericsson.zoom.SimpleZoomListener;
import com.sonyericsson.zoom.ZoomState;
import com.sonyericsson.zoom.SimpleZoomListener.ControlType;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;
import android.widget.AdapterView.OnItemClickListener;

public class BlogActivity extends Activity {


	String blogUrl = "http://bbs.nju.edu.cn/vd59879/blogdoc?userid=";
//	private String dataUrl = "";
//	private int datamsg = -1;
//	int runningTasks = 0;
//	private String data;
//	private ProgressDialog progressDialog = null;
	private ListView listView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		Resources res = getResources();
		Drawable drawable = res.getDrawable(R.drawable.bkcolor);
		this.getWindow().setBackgroundDrawable(drawable);
		
		setContentView(R.layout.blogarea);
		
		if(ConstParam.isFull.equals("2"))
		{
			//�����ޱ���  
	        requestWindowFeature(Window.FEATURE_NO_TITLE);  
		}
		else if(ConstParam.isFull.equals("3"))
		{
			//�����ޱ���  
	        requestWindowFeature(Window.FEATURE_NO_TITLE);  
	        //����ȫ��  
	        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
	                WindowManager.LayoutParams.FLAG_FULLSCREEN);  
		}
		else if(ConstParam.isFull.equals("4"))
		{
			
	        //����ȫ��  
	        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
	                WindowManager.LayoutParams.FLAG_FULLSCREEN);  
		}
		
		if(ConstParam.isChange)
		{
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		
		LinearLayout mLoadingLayout=(LinearLayout)findViewById(R.id.linearLayout1);
		
		mLoadingLayout.setVisibility(btnBarVis);
		
		Intent intent = getIntent();
		String result = intent.getStringExtra("name");
		if(result!=null)
		{
			setTitle(result+"�Ĳ���");
			curAreaName = result;
			String url = blogUrl+result;
			NetTraffic.getUrlHtml(BlogActivity.this,url, Const.BLOGAREA,handler);
		}
		else
		{
			String blogUserName = intent.getStringExtra("blogUserName");
			curAreaName = blogUserName;
			setTitle(blogUserName+"�Ĳ���");
			String data = intent.getStringExtra("data");
			chaToArea(data);
		}
	}
	
	
	int btnBarVis = View.GONE;
	private int getBtnRevtVis()
	{
		if(btnBarVis ==  View.VISIBLE)
		{
			btnBarVis = View.GONE;
			
		}
		else
		{
			btnBarVis = View.VISIBLE;
		}
		return btnBarVis;
			
	}
	
	/**
	 * ���񰴼��¼�
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		/*
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(btnBarVis == View.VISIBLE)
			{
				LinearLayout mLoadingLayout=(LinearLayout)findViewById(R.id.linearLayout1);
				mLoadingLayout.setVisibility(getBtnRevtVis());
				return true;
			}
		} 
		
		
		// ����Ƿ��ؼ�,ֱ�ӷ��ص�����
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			LinearLayout mLoadingLayout=(LinearLayout)findViewById(R.id.linearLayout1);
			if(mLoadingLayout!=null)
			{
				mLoadingLayout.setVisibility(getBtnRevtVis());
				return true;
			}
			else
			{
				return super.onKeyDown(keyCode, event);
			}
		}
		*/
		
			return super.onKeyDown(keyCode, event);
		
	}
	
	
	
	/**
	 *  ��Ϣ���������������½��棬��Ϊ����ͨ�߳����޷��������½����
	 */
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			NetTraffic.runningTasks--;
			if (msg.what != Const.MSGPSTNEW && NetTraffic.data.equals("error")) {
				displayMsg("�������ò���е�С����~");
			} else {
				switch (msg.what) {
				case Const.BLOGAREA:
					chaToArea(NetTraffic.data);
					break;
					
					
				case Const.BLOGTOPIC:
					chaToTopic(NetTraffic.data);
					break;
					
					
				case Const.BLOGPAGE:
					chaToPage(NetTraffic.data);
					break;
					
				case 12345:
					if(moreTextView!=null)
					{
					 moreTextView.setVisibility(View.VISIBLE);
			         loadProgressBar.setVisibility(View.GONE);
					}
					break;
					
				default:
					break;
				}
			}
			if (NetTraffic.runningTasks < 1) {
				NetTraffic.runningTasks = 0;
				NetTraffic.progressDialog.dismiss();
			}

		}

		
	};
	
	private void chaToPage(String data) {
		// TODO Auto-generated method stub
		
		 List<TopicInfo> areaTopicNew = getAreaTopic(data);
		 
		 TopicInfo topicInfo2 = areaTopic.get(areaTopic.size()-1);
		 int s = Integer.parseInt(topicInfo2.getNums());
		 for (TopicInfo topicInfo : areaTopicNew) {
			 int n = Integer.parseInt(topicInfo.getNums());
			if(s>n)
			{
				areaTopic.add(topicInfo);
			}
		}
		 while(areaTopic.size()>200)
		 {
			 for(int i=0;i<20;i++)
			 {
				 areaTopic.remove(0);
			 }
			 scrollY-=20;
		 }
		 //areaTopic.re
		
		 chaToArea(null);
		
		
	}
	
	String curAreaName;
	/**
	 * ��ת��blog����
	 * 
	 * @param AreaData
	 */
	private void chaToArea(String AreaData) {
		
		/*
		ImageTextButton btnLike = (ImageTextButton) findViewById(R.id.btn_like);
		
		if (ConstParam.blogNamList!=null&&ConstParam.blogNamList.contains(curAreaName)) {
			// btnLike.setBackgroundDrawable(drawableDis);
			btnLike.setText("�� ��");
			btnLike.setIcon(R.drawable.fav);
		} else {
			// btnLike.setBackgroundDrawable(drawableFav);
			btnLike.setText("�� ��");
			btnLike.setIcon(R.drawable.unfav);
			
		}
		btnLike.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ImageTextButton btnLike = (ImageTextButton) findViewById(R.id.btn_like);
				if (ConstParam.blogNamList.contains(curAreaName)) {
					ConstParam.blogNamList.remove(curAreaName);
					// btnLike.setBackgroundDrawable(drawableFav);
					btnLike.setText("�� ��");
					btnLike.setIcon(R.drawable.unfav);
					

				} else {
					ConstParam.blogNamList.add(curAreaName);
					// btnLike.setBackgroundDrawable(drawableDis);
					btnLike.setText("�� ��");
					btnLike.setIcon(R.drawable.fav);
				}
				storeAreaName();
			}

		});
		*/
		listView = (ListView) findViewById(R.id.topicList);
		
		 
		if(moreTextView==null)
		
			{
			addPageMore();
			}
		
		
		if (AreaData != null) {
			areaTopic = getAreaTopic(AreaData);
		}
		convtAreaTopics();
		
		if(moreTextView!=null)
		{
			 moreTextView.setVisibility(View.VISIBLE);
	         loadProgressBar.setVisibility(View.GONE);
	         listView.setSelection(scrollY);
	       
		}
		
		
		
		

	}
	int scrollY;
	TextView moreTextView=null;
	LinearLayout loadProgressBar;
	/**
     * ��ListView�����"���ظ���"
     */
    private void addPageMore(){
        View view=LayoutInflater.from(this).inflate(R.layout.list_page_load, null);
        moreTextView=(TextView)view.findViewById(R.id.more_id);
        loadProgressBar=(LinearLayout)view.findViewById(R.id.load_id);
        
        moreTextView.setOnClickListener(new OnClickListener() {
          
            public void onClick(View v) {
                //����"���ظ���"
            	System.out.println(areaNowTopic+"");
            	if(areaNowTopic<2)
            	{
            		displayMsg("�ò���û�и�������");
            		return;
            	}
            	//userid=tiztm&start=4
            	
            	String url = blogUrl+curAreaName+"&start="+(areaNowTopic-21);
            	NetTraffic.getUrlHtml(BlogActivity.this,url, Const.BLOGPAGE,handler);
            	
            	scrollY = listView.getFirstVisiblePosition()+1;
                moreTextView.setVisibility(View.GONE);
                //��ʾ������
                loadProgressBar.setVisibility(View.VISIBLE);
                
                
               
            }
        });
        listView.addFooterView(view);
    }
	
	
	
	private void storeAreaName() {
		String areaName = "";
		for (String name : ConstParam.blogNamList) {
			areaName += name + ",";
		}
		if (areaName.length() > 1) {
			areaName = areaName.substring(0, areaName.length() - 1);
		}
		SharedPreferences sharedPreferences = getSharedPreferences("LilyDroid",
				Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();// ��ȡ�༭��
		editor.putString("blogName", areaName);
		editor.commit();
	}
	
	
	private void chaToTopic(String topicData) {
		char s = 10;
		String backS = s + "";
		String nbs = "<br>";
		topicData = topicData.replaceAll(backS,nbs );
		Document doc = Jsoup.parse(topicData);
		Elements scs = doc.getElementsByTag("textarea");
		if (scs.size() != 1) {
			displayMsg("��ȡ��������ʧ��!");
		} else {
			Element textArea = scs.get(0);
			String infoView = nbs + textArea.text();
			
			int indexOf = topicData.indexOf(">��������");
			String comt="";
			if(indexOf>0)
			{
				String substring = topicData.substring(indexOf+5);
				int indexOf2 = substring.indexOf("��<");
				if(indexOf2>0)
				{
					comt=substring.substring(0,indexOf2);
				}
				else
				{
					comt="0";
				}
			}
			else
			{
				comt="0";
			}
			
			infoView = StringUtil.getBetterTopic(infoView);
			
			infoView+=nbs+"-"+nbs+"[1;34m(��������  "+comt+" ��)[m "+nbs;

			String withSmile = StringUtil.addSmileySpans(infoView,null);
			
			Intent intent = new Intent(BlogActivity.this,
					BlogTopicActivity.class);
			intent.putExtra("withSmile", withSmile);
			intent.putExtra("topicUrl", topicUrl);
			//intent.putExtra("comt", topicUrl);
			
			
			startActivity(intent);
		}

	}
	
	
	
	
	int areaNowTopic = 0;
	/**
	 * ������ȡ��ҳ�� �����������Ļ����б�
	 * 
	 * @param data
	 * @return
	 */
	private List<TopicInfo> getAreaTopic(String data) {
		List<TopicInfo> tiList = new ArrayList<TopicInfo>();
		//��δ����blog
		
			Document doc = Jsoup.parse(data);
			Elements tds = doc.getElementsByTag("td");
			int curPos = 0;
			int line = 4;
			int getTopicNo = 0;
			boolean isOK = false;
			while (curPos < tds.size()-3) {
				 	if(!isOK)
				 	{
				 		String text = tds.get(curPos).text();
				 		
					    if(text.equals("����"))
					    {
					    	isOK = true;
					    	//�ж����Ƿ���<nobr>��ǩ
					    }
					    curPos++;
					    continue;
				 	}
				
				
					TopicInfo ti = new TopicInfo();
					ti.setLink((tds.get(curPos + 2).getElementsByTag("a")).get(0)
							.attr("href"));// ����title
					ti.setTitle("�� "+tds.get(curPos + 2).text());// ����title
	
					String date = DateUtil.formatDateToStrNoWeek(DateUtil
							.getDatefromStrNoWeek(tds.get(curPos + 1).text()));
					if (date == null || date.equals("null"))
						ti.setPubDate(tds.get(curPos + 1).text());
					else
						ti.setPubDate(date);
					//ti.setAuthor(tds.get(curPos + 2).text());
					ti.setHot(tds.get(curPos + 3).text());
					String notext = tds.get(curPos).text();
					ti.setNums(notext);
					tiList.add(ti);
					if (getTopicNo == 0) {
	
						if (notext != "" && Character.isDigit(notext.charAt(0))) {
							areaNowTopic = Integer.parseInt(notext);
							getTopicNo = 1;
						}
						if(curPos < tds.size()-4)
						{
						String del = tds.get(curPos+4).text();
						if (del!=null&&del != "" && !Character.isDigit(del.charAt(0))) {
							line = 5;
						}
						}
						
					}
				curPos += line;
			}											
		Collections.reverse(tiList);
		return tiList;
	}
	
	
	List<TopicInfo> areaTopic = null;
	private List<String> LinkAdr;
	/**
	 * ����HTMLҳ��ת��������ת��ΪListView�ɶ�����ʽ ��BLOGʹ��
	 */
	private void convtAreaTopics() {

		LinkAdr = new ArrayList<String>();

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		for (TopicInfo topicInfo : areaTopic) {

			Map<String, Object> map = new HashMap<String, Object>();

			String title = topicInfo.getTitle();
			
			int indexOf = title.indexOf('(');
			if(indexOf>-1)
			{
				title = title.substring(0,indexOf);
			}
			
			map.put("topictitle", title);

			map.put("topicau", topicInfo.getNums()+" ����:" + topicInfo.getHot());
			map.put("topicother", topicInfo.getPubDate());

			list.add(map);

			LinkAdr.add("http://bbs.nju.edu.cn/" + topicInfo.getLink());

		}
		if (list.size() > 0) {

			SimpleAdapter adapter = new SimpleAdapter(this, list,
					R.layout.vlist, new String[] { "topictitle", "topicau",
							"topicother"}, new int[] { R.id.topictitle,
							R.id.topicau, R.id.topicother});
			listView.setAdapter(adapter);
			// ��ӵ��
			listView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {

					if(arg2>=LinkAdr.size()) return;
					topicUrl = LinkAdr.get(arg2);

					if (topicUrl == null)
						return;

					
					NetTraffic.getUrlHtml(BlogActivity.this,topicUrl, Const.BLOGTOPIC,handler);

				}
			});
		}
	}
	
	String topicUrl ="";
	
	
	
	
	private void displayMsg(String msg) {
		Toast.makeText(BlogActivity.this, msg, Toast.LENGTH_SHORT)
				.show();
	}
	

	

}
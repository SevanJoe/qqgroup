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
import android.content.Intent;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
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
	private String dataUrl = "";
	private int datamsg = -1;
	int runningTasks = 0;
	private String data;
	private ProgressDialog progressDialog = null;
	private ListView listView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		Resources res = getResources();
		Drawable drawable = res.getDrawable(R.drawable.bkcolor);
		this.getWindow().setBackgroundDrawable(drawable);
		
		//setContentView(R.layout.blogarea);
		Intent intent = getIntent();
		String result = intent.getStringExtra("name");
		setTitle(result+"�Ĳ���");
		String url = blogUrl+result;
		getUrlHtml(url, Const.BLOGAREA);
		
	}
	
	
	/**
	 *  ��Ϣ���������������½��棬��Ϊ����ͨ�߳����޷��������½����
	 */
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			runningTasks--;
			if (msg.what != Const.MSGPSTNEW && data.equals("error")) {
				displayMsg("�������ò���е�С����~");
			} else {
				switch (msg.what) {
				case Const.BLOGAREA:
					chaToArea(data);
					break;
					
					
				case Const.BLOGTOPIC:
					chaToTopic(data);
					break;
				default:
					break;
				}
			}
			if (runningTasks < 1) {
				runningTasks = 0;
				progressDialog.dismiss();
			}

		}
	};
	
	
	/**
	 * ��ת������������
	 * 
	 * @param AreaData
	 */
	private void chaToArea(String AreaData) {

		setContentView(R.layout.blogarea);
		listView = (ListView) findViewById(R.id.topicList);
		
		if (AreaData != null) {
			areaTopic = getAreaTopic(AreaData);
		}
		convtAreaTopics();

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
			
			infoView = StringUtil.getBetterTopic(infoView);

			String withSmile = StringUtil.addSmileySpans(infoView,null);
			
			Intent intent = new Intent(BlogActivity.this,
					BlogTopicActivity.class);
			intent.putExtra("withSmile", withSmile);
			intent.putExtra("topicUrl", topicUrl);
			
			
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
		
		
			Document doc = Jsoup.parse(data);
			Elements tds = doc.getElementsByTag("td");
			int curPos = 0;
			int getTopicNo = 0;
			boolean isOK = false;
			while (curPos < tds.size()) {
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
					}
				curPos += 5;
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
			
			map.put("topictitle", title);

			map.put("topicau", "����:" + topicInfo.getHot());
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

					topicUrl = LinkAdr.get(arg2);

					if (topicUrl == null)
						return;

					
					getUrlHtml(topicUrl, Const.BLOGTOPIC);

				}
			});
		}
	}
	
	String topicUrl ="";
	
	
	
	
	private void displayMsg(String msg) {
		Toast.makeText(BlogActivity.this, msg, Toast.LENGTH_SHORT)
				.show();
	}
	
	private void getUrlHtml(String url, int msg) {
		if (msg == 123 || progressDialog == null || !progressDialog.isShowing()) {
			progressDialog = ProgressDialog.show(BlogActivity.this,
					"���Ե�...", "ץȡ��ҳ��Ϣ��...", true);
		}
		runningTasks++;

		dataUrl = url;
		datamsg = msg;
		new Thread() {

			@Override
			public void run() {
				// ��Ҫ��ʱ�����ķ���
				try {
					data = NetTraffic.getHtmlContent(dataUrl);
				} catch (Exception e) {
					data = "error";
				}
				sendMsg(datamsg);
			}
		}.start();

	}
	
	private void sendMsg(int meg) {
		Message msg = new Message();
		msg.what = meg;
		handler.sendMessage(msg);
	}
	

}
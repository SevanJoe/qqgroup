package com.ztm;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.NameValuePair;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ztm.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.text.ClipboardManager;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class TestAndroidActivity extends Activity implements OnTouchListener,
OnGestureListener {

	private GestureDetector mGestureDetector;
	// �ؼ�

	private TextView textView;

	private ListView listView;

	
	private Button btnLink;

	// ȫ�ֱ���

	private List<String> LinkAdr;

	private String data;

	private List<TopicInfo> top10TopicList;

	private String topicUrl;

	private String newUrl;

	private String huifuUrl;

	
	// 1 ��ʾ��10����ת��ȥ�ģ�2��ʾ����������ת��ȥ�ģ�3��ʾ�Ӹ����ȵ�����ȥ
	int curStatus = 0;

	List<TopicInfo> areaTopic;

	int areaNowTopic = 0;
	
	boolean isWifi = false;

	private int nowPos;

	String urlString = "";

	String curAreaName = "";

	String curTopicId = "";
	
	String isPic;
	String isRem = "false";
	boolean isTouch ;
	boolean isIP;
	
	String loginId = "";
	String loginPwd = "";
	
	String androidmanufacturer;
	String androidmodel;
	
	
	String backWords = "";//
	boolean isBackWord = true;
	
	int runningTasks = 0;

	private ProgressDialog progressDialog = null;

	SharedPreferences sharedPreferences;

	List<String> areaNamList;
	
	String[] fastReList;

	Drawable drawableFav;

	Drawable drawableDis;

	boolean isLogin = false;



	Spanned topicData;

	int scrollY = 0;
	
	boolean topicWithImg = false;
	
	HashMap<String, String> bbsAll;
	
	HashMap<String, Integer> smilyAll;
	
	ArrayAdapter<String> bbsAlladapter;

	String loginURL = "http://bbs.nju.edu.cn/bbslogin?type=2";

	String loginoutURL = "http://bbs.nju.edu.cn/bbslogout";
	
	String synUrl = "http://bbs.nju.edu.cn/bbsleft";
	
	String bbsTop10String = "http://bbs.nju.edu.cn/bbstop10";

	
	Drawable xianDraw;
	int sWidth = 480;
	int sLength = 800;
	
	
	//TODO:����ȫ�ֱ���
	/**
	 * Called when the activity is first created.
	 * */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mGestureDetector = new GestureDetector(this);

		Resources res = getResources();
		Drawable drawable = res.getDrawable(R.drawable.bkcolor);
		xianDraw = res.getDrawable(R.drawable.xian);
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		sWidth = metric.widthPixels - 30; // ��Ļ��ȣ����أ�
		sLength = metric.heightPixels - 40; // ��Ļ��ȣ����أ�

		this.getWindow().setBackgroundDrawable(drawable);
		bbsAll = BBSAll.getBBSAll();
		
		smilyAll = BBSAll.getSmilyAll();
		String[] bbsAllArray = StringUtil.getArray(bbsAll);
		bbsAlladapter = new ArrayAdapter<String>(TestAndroidActivity.this,
				android.R.layout.simple_dropdown_item_1line, bbsAllArray);
		initPhoneState();
		initAllParams();
		
		StringUtil.initAll();
		
		
		chaToLogin();
	}

	private void InitMain() {
		chaToMain();
		getUrlHtml(bbsTop10String, Const.MSGWHAT);

	}

	private void initPhoneState()
	{
          
		try {
			
			

           Class<android.os.Build> build_class = android.os.Build.class;

           //ȡ������

           java.lang.reflect.Field manu_field = build_class.getField("MANUFACTURER");

           androidmanufacturer = (String) manu_field.get(new android.os.Build());

           //ȡ����̖

           java.lang.reflect.Field field2 = build_class.getField("MODEL");

           androidmodel = (String) field2.get(new android.os.Build());

          
		} catch (Exception e) {
		
			e.printStackTrace();
		} 
	}

	private void chaToLogin() {
		setContentView(R.layout.login);
		Button btnlog = (Button) findViewById(R.id.btn_login);
		btnlog.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {

				EditText textName = (EditText) findViewById(R.id.textName);
				EditText textPwd = (EditText) findViewById(R.id.textPwd);
				CheckBox cb = (CheckBox) findViewById(R.id.cb_rem);
				if (cb.isChecked()) {
					isRem = "true";
				} else {
					isRem = "false";
				}
				loginId = textName.getText().toString();
				loginPwd = textPwd.getText().toString();
				String url = loginURL + "&id=" + loginId + "&pw=" + loginPwd;
				getUrlHtml(url, Const.MSGLOGIN);
			}

		});
		Button btnno = (Button) findViewById(R.id.btn_nolog);
		btnno.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				isLogin = false;
				InitMain();
			}

		});
		if (isRem.equals("true")) {
			EditText text = (EditText) findViewById(R.id.textName);
			text.setText(loginId);
			text = (EditText) findViewById(R.id.textPwd);
			text.setText(loginPwd);
			CheckBox cb = (CheckBox) findViewById(R.id.cb_rem);
			cb.setChecked(true);
		}
	}

	/**
	 * ��ת��������
	 */
	private void chaToMain() {
		setContentView(R.layout.main);

		curStatus = 0;
		setTitle("ȫվʮ��");
		// ע�����ؼ��ĳ�ʼ����λ��,��Ҫ����setContentView()ǰ��
		listView = (ListView) findViewById(R.id.topicList);
		btnLink = (Button) findViewById(R.id.btn_link);

		btnLink.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				// ���Դ�һ�����߳�����ȡ�������������
				getUrlHtml(bbsTop10String, Const.MSGWHAT);
			}

		});

		Button btnArea = (Button) findViewById(R.id.btn_all);

		btnArea.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				// ���Դ�һ�����߳�����ȡ�������������

				LayoutInflater factory = LayoutInflater
						.from(TestAndroidActivity.this);
				final View textEntryView = factory.inflate(R.layout.dialog,
						null);
				AlertDialog dlg = new AlertDialog.Builder(
						TestAndroidActivity.this)

				.setTitle("������������������").setView(textEntryView)
						.setPositiveButton("����",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										EditText secondPwd = (EditText) textEntryView
												.findViewById(R.id.username_edit);
										String inputPwd = secondPwd.getText()
												.toString();
										String areaText = bbsAll.get(inputPwd);
										areaText = areaText == null ? inputPwd
												: areaText;
										areaText = areaText.toLowerCase();
										areaText = areaText.replaceFirst(
												areaText.substring(0, 1),
												areaText.substring(0, 1)
														.toUpperCase());
										urlString = getResources().getString(
												R.string.areaStr)
												+ areaText;
										curAreaName = "" + areaText;

										getUrlHtml(urlString, Const.MSGAREA);

									}
								}).setNegativeButton("ȡ��",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
									}
								}).create();

				dlg.show();

				AutoCompleteTextView secondPwd = (AutoCompleteTextView) textEntryView
						.findViewById(R.id.username_edit);
				if (secondPwd.getAdapter() == null) {
					secondPwd.setAdapter(bbsAlladapter);
					secondPwd.setThreshold(1);
				}

			}

		});

		Button btnLike = (Button) findViewById(R.id.btn_like);

		btnLike.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						TestAndroidActivity.this);

				builder.setTitle("ѡ������ȥ����������");
				if (areaNamList == null || areaNamList.size() < 1) {

					return;
				}
				String[] a = new String[areaNamList.size()];
				int i = 0;
				for (String areName : areaNamList) {
					a[i] = areName;
					i++;
				}

				builder.setSingleChoiceItems(a, 0,
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialoginterface, int i) {

								String areaText = areaNamList.get(i);
								urlString = getResources().getString(
										R.string.areaStr)
										+ areaText;
								curAreaName = "" + areaText;
								dialoginterface.dismiss();
								getUrlHtml(urlString, Const.MSGAREA);

							}
						});

				builder.setPositiveButton("ȡ��",
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialoginterface, int i) {

							}
						});
				builder.create().show();

			}

		});

		Button btnSet = (Button) findViewById(R.id.btn_set);

		btnSet.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {

				exitPro();
			}

		});

	}
	
	
	// �˵���   
    final private int menuSettings=Menu.FIRST;  
    final private int menuLogout=Menu.FIRST+2;
    final private int menuSyn=Menu.FIRST+1;  
    private static final int REQ_SYSTEM_SETTINGS = 0;    

    //�����˵�   
    @Override    
    public boolean onPrepareOptionsMenu(Menu menu) 
    {        
    	return true;
    }

    @Override  
    public boolean onCreateOptionsMenu(Menu menu)  
    {  
        // �����˵�   
        menu.add(Menu.NONE, menuSettings, 2, "����");  
        menu.add(Menu.NONE, menuSyn, 2, "ͬ���ղ�");  
        menu.add(Menu.NONE, menuLogout, 2, "ע��");  
        return super.onCreateOptionsMenu(menu);  
    }  
    //�˵�ѡ���¼�����   
    @Override  
    public boolean onMenuItemSelected(int featureId, MenuItem item)  
    {  
        switch (item.getItemId())  
        {  
            case menuSettings:  
                //ת��Settings���ý���   
                startActivityForResult(new Intent(this, Settings.class), REQ_SYSTEM_SETTINGS);  
                break;  
            case menuLogout:  
                //ת����¼����   
            	getUrlHtml(loginoutURL,123);
                chaToLogin();
                break;
            case menuSyn:  
                //ת����¼����   
            	if(isLogin)
            		getUrlHtml(synUrl,Const.MSGSYN);
            	else
            	{
            		displayMsg("�㻹û��½��~");
            	}
                break; 
                
            default:  
                break;  
        }  
        return super.onMenuItemSelected(featureId, item);  
    }  
    //Settings���ý��淵�صĽ��   
    protected  void onActivityResult(int requestCode, int resultCode, Intent data) {  
    	myParams();
    }  
    
    


	/**
	 * ���񰴼��¼�
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// ����Ƿ��ؼ�,ֱ�ӷ��ص�����
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (curStatus == 1) {
				chaToMain();
				if (top10TopicList != null) {
					setTopics();
				}
			} else if (curStatus == 2) {
				chaToArea(null);
			} else if (curStatus == 0) {
				exitPro();
			}
			return true;

		}
		else
		{
		return  super.onKeyDown(keyCode, event);
		}
	}



	private void initAllParams() {
		sharedPreferences = getSharedPreferences("LilyDroid",
				Context.MODE_PRIVATE);
		String name = sharedPreferences.getString("areaName", "");
		areaNamList = new ArrayList<String>();
		isRem = sharedPreferences.getString("isRem", "false");
		loginId = sharedPreferences.getString("loginId", "");
		loginPwd = sharedPreferences.getString("loginPwd", "");
		
		 myParams();
		WifiManager mWiFiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
		if(mWiFiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED )
		{
			isWifi = true;
		}
		
		if (name == null || name.length() < 1)
			return;

		String[] split = name.split(",");
		for (String string : split) {
			areaNamList.add(string);
		}
	}
	
	private void myParams()
	{
		SharedPreferences sp = getSharedPreferences("com.ztm_preferences",
				Context.MODE_PRIVATE);
		isPic = sp.getString("picDS", "1");
		isTouch = sp.getBoolean("isTouch", true);
		isBackWord =  sp.getBoolean("isBackWord", true);
		backWords = sp.getString("backWords", "������ �ҵ�С�ٺ�Android�ͻ��� by ${model}");
		isIP = sp.getBoolean("isIP", false);
		backWords = backWords.replaceAll("\\$\\{model\\}", androidmodel).replaceAll("\\$\\{manufa\\}", androidmanufacturer);
		String fastRe = sp.getString("fastRe", "ɳ��");
		if ( fastRe.length() < 1)
		{
			fastReList = null;
			return;
		}
		
		fastReList= fastRe.split("##");
		
		
		
	}
	
	private void displayMsg(String msg)
	{
		Toast.makeText(TestAndroidActivity.this, msg,
				Toast.LENGTH_SHORT).show();
	}
	

	/**
	 * TODO:��Ϣ������
	 * ��Ϣ���������������½��棬��Ϊ����ͨ�߳����޷��������½����
	 */
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			
			runningTasks--;
			
			if (  msg.what!=Const.MSGPSTNEW && data.equals("error")) {
				displayMsg("�������ò���е�С����~");
				
			}
			else
			{
			switch (msg.what) {
			case Const.MSGWHAT:
				// ������ʾ�ı�
				// �������data����
				top10TopicList = StringUtil.getTop10Topic(data);
				setTopics();
				break;
			case Const.MSGTOPIC:
				// ������ʾ�ı�

				chaToTopic(topicData);
				break;
			case Const.MSGTOPICNEXT:

				textView = (TextView) findViewById(R.id.label);
				ScrollView sv = (ScrollView) findViewById(R.id.scrollView);
				sv.scrollTo(0, 0);
				textView.setText( getURLChanged(topicData));

				break;
			case Const.MSGTOPICREFREASH:
				textView = (TextView) findViewById(R.id.label);
				
				if(textView!=null)
					textView.setText(getURLChanged(topicData));
				break;
			case Const.MSGAREA:
				chaToArea(data);
				break;

			case Const.MSGAREAPAGES:
				areaPages(data);
				break;

			case Const.MSGLOGIN:
				checkLogin(data);
				break;
			case Const.MSGAUTOLOGIN:
				checkAutoLogin(data);
				break;
			case Const.MSGPST:
				checkForm(data);
				break;
			case Const.MSGPSTNEW:
				// ���Ŀ��ܻ�ʧ�ܣ�ע�Ᵽ������
				checkRst(data);
				break;

			case Const.MSGVIEWUSER:
				getUserData(data);
				break;
				
			case Const.MSGSYN:
				checkSyn(data);
				break;
				
				
				
			default:
				break;

			}
			}
			if(runningTasks<1)
			{
				runningTasks = 0;
				progressDialog.dismiss();
			}
			
		}

		

		

	};
	
	//ͬ���ղؼ�
	private void checkSyn(String data) {
		Document doc = Jsoup.parse(data);

		Elements as = doc.getElementsByTag("a");
		int ll = areaNamList.size();
		for (Element aTag : as) {
			String href = aTag.attr("href");
			if(href.contains("board?board="))
			{
				String tempAreaName = aTag.text().trim().toLowerCase();
			
				tempAreaName = tempAreaName.replaceFirst(
						tempAreaName.substring(0, 1),
						tempAreaName.substring(0, 1)
									.toUpperCase());
				
				if (areaNamList.contains(tempAreaName)) {
					continue;
				}
				areaNamList.add(tempAreaName);
				
			}
		}
		if(ll<areaNamList.size())
		{
			int ii= areaNamList.size()-ll;
			storeAreaName();
			displayMsg("ͬ��WEB�ղؼгɹ�!����"+ii+"�����ղذ���");
		}
		else
		{
			displayMsg("��ı����ղؼ��Ѿ������µ��ˣ�");
		}
		
	}
	
	/**
	 * 
	 * ��ȡ�û���Ϣ
	 * @param data
	 */
	private void getUserData(String data) {
		//TODO:
		char s = 10;
		String backS = s + "";
		
		data = data.replaceAll(backS, "<br>");
		Document doc = Jsoup.parse(data);

		Elements scs = doc.getElementsByTag("textarea");
		
		if (scs.size() != 1) {
			Toast.makeText(TestAndroidActivity.this, "��ȡ�û���Ϣʧ��",
					Toast.LENGTH_SHORT).show();
		}
		else
		{
			Element textArea = scs.get(0);
			String infoView = textArea.text();
			
			
			String withSmile = StringUtil.addSmileySpans(infoView);
			LayoutInflater factory = LayoutInflater
			.from(TestAndroidActivity.this);
			final View info = factory.inflate(R.layout.infodlg, null);
			AlertDialog dlg = new AlertDialog.Builder(TestAndroidActivity.this)
			.setTitle("�û���Ϣ��ѯ").setView(info).setNegativeButton("ȷ��",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
						}
					}).create();
			//�����չ���
			textView = (TextView) info.findViewById(R.id.tvInfo);
			ScrollView sv = (ScrollView) info.findViewById(R.id.svInfo);
			sv.scrollTo(0, 0);
			textView.setText(Html.fromHtml(withSmile));
			
			dlg.show();

		}

	}

	/**
	 * ��鷢�Ľ��
	 */
	private void checkRst(String data) {

		if (data.contains("http-equiv='Refresh'")) {
			
			if(reid.equals("0"))
			{
				//�����������
				getUrlHtml(urlString, Const.MSGAREAPAGES);
			}
			else
			{
				//�ظ����
				getUrlHtml(topicUrl + "&start="
						+ nowPos, Const.MSGTOPICREFREASH);
			}
			
			
			Toast.makeText(TestAndroidActivity.this, "���ĳɹ���",
					Toast.LENGTH_SHORT).show();
		}

		else // if(data.contains("javascript:history.go(-1)"))
		{
			Toast.makeText(TestAndroidActivity.this, "����ʧ������~�������ݱ����ڼ�������",
					Toast.LENGTH_SHORT).show();
			ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

			clipboard.setText(cont);
		}

	}

	/**
	 * ����Ƿ��¼�ɹ�
	 * 
	 * @param data
	 */
	private void checkLogin(String data) {

		Document doc = Jsoup.parse(data);

		Elements scs = doc.getElementsByTag("script");

		if (scs.size() == 3) {
			String element = scs.get(1).toString();

			setCookies(element.substring(27, element.length() - 12));

			Toast.makeText(TestAndroidActivity.this, "��¼�ɹ���",
					Toast.LENGTH_SHORT).show();
			isLogin = true;

			Editor editor = sharedPreferences.edit();// ��ȡ�༭��
			editor.putString("isRem", isRem);
			if (isRem.equals("true")) {
				editor.putString("loginId", loginId);
				editor.putString("loginPwd", loginPwd);
			} else {
				editor.putString("loginId", "");
				editor.putString("loginPwd", "");
			}
			editor.commit();
			// progressDialog.dismiss();
			InitMain();

		} else if (scs.size() == 1) {
			if (data.contains("�������") || data.contains("�����ʹ�����ʺ�")) {
				Toast.makeText(TestAndroidActivity.this, "�û����������",
						Toast.LENGTH_SHORT).show();
			} else if (data.contains("���ʺű���login��������")) {
				Toast.makeText(TestAndroidActivity.this, "���ʺű���login�������࣡",
						Toast.LENGTH_SHORT).show();
			}

			else {
				Toast.makeText(TestAndroidActivity.this, "��½ʧ�ܣ�",
						Toast.LENGTH_SHORT).show();
			}

			isLogin = false;

		}
		return;
	}
	
	
	/**
	 * ����Ƿ��Զ�����¼�ɹ�
	 * 
	 * @param data
	 */
	private void checkAutoLogin(String data) {
		Document doc = Jsoup.parse(data);
		Elements scs = doc.getElementsByTag("script");
		if (scs.size() == 3) {
			String element = scs.get(1).toString();

			setCookies(element.substring(27, element.length() - 12));

			Toast.makeText(TestAndroidActivity.this, "��¼�ɹ���",
					Toast.LENGTH_SHORT).show();
			isLogin = true;
		} else if (scs.size() == 1) {
			if (data.contains("�������") || data.contains("�����ʹ�����ʺ�")) {
				Toast.makeText(TestAndroidActivity.this, "�û����������",
						Toast.LENGTH_SHORT).show();
			} else if (data.contains("���ʺű���login��������")) {
				Toast.makeText(TestAndroidActivity.this, "���ʺű���login�������࣡",
						Toast.LENGTH_SHORT).show();
			}
			else {
				Toast.makeText(TestAndroidActivity.this, "��½ʧ�ܣ�",
						Toast.LENGTH_SHORT).show();
			}
			isLogin = false;

		}
		return;
	}
	
	
	

	String pid;
	String reid;
	String cont;

	/**
	 * ��ȡ���ĵĴ���
	 * @param formData
	 */
	protected void checkForm(String formData) {
		Document doc = Jsoup.parse(formData);
		Elements ins = doc.getElementsByTag("input");
		// progressDialog.dismiss();
		if (ins.size() != 12) {
			// ��¼ʧ�ܣ�Ҫ�����µ�¼
			if (formData.contains("�Ҵҹ���")) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						TestAndroidActivity.this);
				builder.setMessage("�㻹û��½��~���µ�¼?").setCancelable(false)
						.setPositiveButton("��¼",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										if(isRem.equals("true"))
										{
											//�Զ���¼�Ļ����Զ���¼
											String url = loginURL + "&id=" + loginId + "&pw=" + loginPwd;
											getUrlHtml(url, Const.MSGAUTOLOGIN);
										}
										else
										{
											chaToLogin();
										}
										
										
									}
								}).setNegativeButton("����",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel();
									}
								});
				AlertDialog alert = builder.create();
				alert.show();
			} else if (formData.contains("����Ȩ�ڴ�������")) {
				Toast.makeText(TestAndroidActivity.this, "����Ȩ�ڴ�����������",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(TestAndroidActivity.this, "����δ֪������ʧ��",
						Toast.LENGTH_SHORT).show();
			}

		} else {

			String title = ins.get(0).attr("value");
			pid = ins.get(1).attr("value");
			reid = ins.get(2).attr("value");
			 String recont = "" ;
			try
			{
				recont = formData.substring(formData.indexOf("<textarea name=text rows=20 cols=80 wrap=physicle>")+50,formData.indexOf("</textarea>"));
			}
			catch(Exception e)
			{
				
			}
			final String extraRecont = recont;
			LayoutInflater factory = LayoutInflater
					.from(TestAndroidActivity.this);
			final View acdlgView = factory.inflate(R.layout.acdlg, null);
			 Builder altDlg = new AlertDialog.Builder(TestAndroidActivity.this)
					.setTitle("����").setView(acdlgView).setPositiveButton("����",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									EditText titleEdit = (EditText) acdlgView
											.findViewById(R.id.edt_title);
									String title = titleEdit.getText()
											.toString();
									titleEdit = (EditText) acdlgView
											.findViewById(R.id.edt_cont);
									cont = StringUtil.getStrBetter(titleEdit.getText()
											.toString());
											//����ԭ��
											CheckBox cb = (CheckBox) acdlgView.findViewById(R.id.cb_recont);
											if(cb.isChecked()&&extraRecont!=null&&extraRecont.length()>1)
											{
												cont+=extraRecont.substring(2);
											}
											sendTopic(title,cont);
								}

							});
							
			 if(extraRecont.length()<4)
				{
				 altDlg.setNegativeButton("ȡ��",
							new DialogInterface.OnClickListener() {
						public void onClick(
								DialogInterface dialoginterface, int i) {

						}
					});
				}
			 else
			 {
				 altDlg.setNegativeButton("���ٻظ�",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
										if(fastReList.length<1)
											return;
										if(fastReList.length==1)
										{
											//TODO
											EditText titleEdit = (EditText) acdlgView
											.findViewById(R.id.edt_title);
											String title = titleEdit.getText().toString();
											String reText = fastReList[0];
											sendTopic(title,reText);
											return;
										}
										AlertDialog.Builder builder = new AlertDialog.Builder(
												TestAndroidActivity.this);

										builder.setTitle("ѡ��Ҫʹ�õĿ�ݻظ���");
										
										builder.setSingleChoiceItems(fastReList, 0,
												new DialogInterface.OnClickListener() {
													public void onClick(
															DialogInterface dialoginterface, int i) {

														EditText titleEdit = (EditText) acdlgView
														.findViewById(R.id.edt_title);
														String title = titleEdit.getText().toString();
														String reText = fastReList[i]+"\n";
														dialoginterface.dismiss();
														sendTopic(title,reText);
													}
												});

										builder.setPositiveButton("ȡ��",
												new DialogInterface.OnClickListener() {
													public void onClick(
															DialogInterface dialoginterface, int i) {

													}
												});
										builder.create().show();

									}
							});
			 }
			 
							
				AlertDialog dlg =			altDlg.create();
			EditText titleEdit = (EditText) acdlgView
					.findViewById(R.id.edt_title);
			CheckBox cb = (CheckBox) acdlgView.findViewById(R.id.cb_recont);
			if(extraRecont.length()<4)
			{
				
				cb.setChecked(false);
				cb.setVisibility(CheckBox.INVISIBLE);
			}
			else
			{
				cb.setChecked(true);
			}
			titleEdit.setText(title);
			dlg.show();

		}
	}

	private void sendTopic(String title,String cont)
	{
		//�ֻ�ǩ��
		if(isBackWord&&backWords!=null&&backWords.length()>0)
		{
			cont+="\n-\n[1;32m"+backWords+"[m\n";
		}
		
		try {
			title = URLEncoder.encode(title,
					"GB2312"); // new
								// String((title.replace(" ",
								// "%20")).getBytes("UTF-8"),"gb2312");
			String url = "http://bbs.nju.edu.cn/bbssnd?board="
					+ curAreaName
					+ "&title="
					+ title
					+ "&pid="
					+ pid
					+ "&reid="
					+ reid
					+ "&signature=1";
			// +"&text="+;

			NameValuePair[] newVp = { new NameValuePair(
					"text", cont) };

			nvpCont = newVp;

			getUrlHtml(url, Const.MSGPSTNEW);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	private void setCookies(String cookStr) {

		char[] charArray = cookStr.toCharArray();
		int i = 0;
		int sp1 = 0;
		int sp2 = 0;
		for (char c : charArray) {
			if (sp1 == 0 && !Character.isDigit(c)) {
				sp1 = i;

			} else if (c == '+') {
				sp2 = i;
				break;
			}
			i++;
		}
		String NUM = (Integer.parseInt(cookStr.substring(0, sp1)) + 2) + "";
		String id = cookStr.substring(sp1 + 1, sp2);
		String KEY = (Integer.parseInt(cookStr.substring(sp2 + 1)) - 2) + "";
		//saveMyCookie( NUM, id , KEY);
		NetTraffic.setMyCookie( NUM, id , KEY);
	}


	
	
	

	/**
	 * ��������ת��ΪListView�ɶ�����ʽ ��10��ʹ��
	 */
	private void setTopics() {

		LinkAdr = new ArrayList<String>();

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		for (TopicInfo topicInfo : top10TopicList) {

			Map<String, Object> map = new HashMap<String, Object>();

			map.put("topictitle", " " + topicInfo.getTitle());

			map.put("topicau", " ����:" + topicInfo.getAuthor() + "  ����:"
					+ topicInfo.getArea() + "  �ظ�:" + topicInfo.getNums());

			list.add(map);

			LinkAdr.add("http://bbs.nju.edu.cn/" + topicInfo.getLink());

		}
		if (list.size() > 0) {

			SimpleAdapter adapter = new SimpleAdapter(this, list,
					R.layout.vlist, new String[] { "topictitle", "topicau" },
					new int[] { R.id.topictitle, R.id.topicau });
			listView.setAdapter(adapter);
			// ��ӵ��
			listView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					topicUrl = LinkAdr.get(arg2);

					if (topicUrl == null)
						return;
					huifuUrl = topicUrl.replace("bbstcon?", "bbspst?");
					curStatus = 1;
					nowPos = 0;
					getUrlHtml(topicUrl, Const.MSGTOPIC);

				}
			});
		}
	}

	/**
	 * ����HTMLҳ��ת��������ת��ΪListView�ɶ�����ʽ ��������ʹ��
	 */
	private void setAreaTopics() {

		LinkAdr = new ArrayList<String>();

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		for (TopicInfo topicInfo : areaTopic) {

			Map<String, Object> map = new HashMap<String, Object>();

			map.put("topictitle", " " + topicInfo.getTitle());
			if (topicInfo.getNums() == null || topicInfo.getNums().equals("")) {
				map
						.put("topicau", " �ö�   ����:" + topicInfo.getAuthor()
								+ " - " + topicInfo.getPubDate() + "  ����:"
								+ topicInfo.getHot());
			} else {
				map
						.put("topicau", " " + topicInfo.getNums() + "   ����:"
								+ topicInfo.getAuthor() + " ��"
								+ topicInfo.getPubDate() + "  ����:"
								+ topicInfo.getHot());
			}

			list.add(map);

			LinkAdr.add("http://bbs.nju.edu.cn/" + topicInfo.getLink());

		}
		if (list.size() > 0) {

			SimpleAdapter adapter = new SimpleAdapter(this, list,
					R.layout.vlist, new String[] { "topictitle", "topicau" },
					new int[] { R.id.topictitle, R.id.topicau });
			listView.setAdapter(adapter);
			// ��ӵ��
			listView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {

					topicUrl = LinkAdr.get(arg2);

					if (topicUrl == null)
						return;

					huifuUrl = topicUrl.replace("bbstcon?", "bbspst?");
					curStatus = 2;
					nowPos = 0;
					scrollY = listView.getFirstVisiblePosition();

					getUrlHtml(topicUrl, Const.MSGTOPIC);

				}
			});
		}
	}

	boolean isNext = true;
	boolean isPrev = true;

	

	
	/**
	 * ��ת������������
	 * 
	 * @param AreaData
	 */
	private void chaToArea(String AreaData) {
		
		if(AreaData!=null&&AreaData.contains("����! �����������"))
				{
			Toast.makeText(TestAndroidActivity.this, "�������������ڣ�",
					Toast.LENGTH_SHORT).show();
			return;
				}
		
		setContentView(R.layout.area);
		curStatus = 1;
		Button btnBack = (Button) findViewById(R.id.btn_back);
		btnBack.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				/**
				 * �ĳɷ����»���
				 * 
				 * chaToMain(); if(top10TopicList!=null) { setTopics(); }
				 * 
				 */
				getUrlHtml(newUrl, Const.MSGPST);

			}
		});
		Button btnPre = (Button) findViewById(R.id.btn_pre);
		btnPre.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				goToPage(-21);
			}
		});

		Button btnNext = (Button) findViewById(R.id.btn_next);
		btnNext.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				goToPage(21);
			}
		});

		setTitle("��ǰ��������" + curAreaName);

		Button btnLike = (Button) findViewById(R.id.btn_like);

		if (areaNamList.contains(curAreaName)) {
			// btnLike.setBackgroundDrawable(drawableDis);
			btnLike.setText("�˶�");
		} else {
			// btnLike.setBackgroundDrawable(drawableFav);
			btnLike.setText("�ղ�");
		}

		btnLike.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Button btnLike = (Button) findViewById(R.id.btn_like);
				if (areaNamList.contains(curAreaName)) {
					areaNamList.remove(curAreaName);

					// btnLike.setBackgroundDrawable(drawableFav);
					btnLike.setText("�ղ�");

				} else {
					areaNamList.add(curAreaName);
					// btnLike.setBackgroundDrawable(drawableDis);
					btnLike.setText("�˶�");
				}
				storeAreaName();
			}

		});

		if (AreaData != null) {
			newUrl = "http://bbs.nju.edu.cn/bbspst?board=" + curAreaName;
			areaTopic = getAreaTopic(AreaData);
		}
		listView = (ListView) findViewById(R.id.topicList);

		setAreaTopics();
		if (AreaData == null) {
			listView.setSelection(scrollY);
		} else {

			listView.setSelection(areaTopic.size() - 1);
		}

	}

	private void storeAreaName() {
		String areaName = "";
		for (String name : areaNamList) {
			areaName += name + ",";
		}
		if (areaName.length() > 1) {
			areaName = areaName.substring(0, areaName.length() - 1);
		}
		Editor editor = sharedPreferences.edit();// ��ȡ�༭��
		editor.putString("areaName", areaName);
		editor.commit();
	}

	/**
	 * ���������淭ҳ
	 * 
	 * @param AreaData
	 */
	private void goToPage(int pageNo) {
		int startPage = areaNowTopic + pageNo;
		if (startPage < 0) {
			startPage = 0;
		}

		getUrlHtml(urlString + "&start=" + startPage, Const.MSGAREAPAGES);

	}

	private void areaPages(String AreaData) {
		areaTopic = getAreaTopic(AreaData);
		listView = (ListView) findViewById(R.id.topicList);
		setAreaTopics();
		listView.setSelection(areaTopic.size() - 1);
	}
	
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
		while (curPos < tds.size()) {
			if (curPos != 0) {
				TopicInfo ti = new TopicInfo();
				ti.setLink((tds.get(curPos + 4).getElementsByTag("a")).get(0)
						.attr("href"));// ����title
				ti.setTitle(tds.get(curPos + 4).text());// ����title
				
				String date = DateUtil.formatDateToStrNoWeek(DateUtil.getDatefromStrNoWeek(tds.get(curPos + 3).text()));
				if(date == null||date.equals("null"))
					
					ti.setPubDate(tds.get(curPos + 3).text());
				else
					ti.setPubDate(date);
				ti.setAuthor(tds.get(curPos + 2).text());
				ti.setHot(tds.get(curPos + 5).text());
				String notext = tds.get(curPos).text();
				ti.setNums(notext);
				tiList.add(ti);
				if (getTopicNo == 0) {

					if (notext != "" && Character.isDigit(notext.charAt(0))) {
						areaNowTopic = Integer.parseInt(notext);
						getTopicNo = 1;
					}
				}

			}

			curPos += 6;
		}

		return tiList;
	}
	class MyURLSpan extends ClickableSpan {
		
		  private String mUrl;
		   private boolean underline = false;
		     MyURLSpan(String url) {
		      mUrl = url;
		     }


		     
		     @Override
		  public void updateDrawState(TextPaint ds) {
		   super.updateDrawState(ds);
		    ds.setUnderlineText(underline);//һ�������涼��һ���ߣ�ͦ���ģ� �÷�������ȥ��������
		   // ds.setColor(Color.rgb(0, 0, 237));// �ı����ӵ���ɫ����
		  }

		  @Override
		     public void onClick(View widget) {
		            //�˴�д��Ĵ����߼�
			  //System.out.println("123123");
			  // processHyperLinkClick(text); //���������ʱ����
			  if(mUrl.contains("bbsqry?userid"))
			  {
				  //�鿴�û�
				  getUrlHtml(mUrl, Const.MSGVIEWUSER);
			  }
			  else if(mUrl.toLowerCase().startsWith("http:")
						&& (mUrl.toLowerCase().endsWith(".jpg") 
								|| mUrl.toLowerCase().endsWith(".png")
								||mUrl.toLowerCase().endsWith(".jpeg")
								||mUrl.toLowerCase().endsWith(".gif")
								))
			  {
				  Intent intent = new Intent(TestAndroidActivity.this, ImageActivity.class);
				  intent.putExtra("mUrl", mUrl); 
				 
				  startActivity(intent);
			  }
			  else if(mUrl.toLowerCase().startsWith("http:"))
			  {
				  //super.
			  }

		   }
		 }

	 public  SpannableStringBuilder getURLChanged(Spanned topicData)
	 {
		 URLSpan[] spans = topicData.getSpans(0, topicData.length(), URLSpan.class);  
			SpannableStringBuilder style = new SpannableStringBuilder(topicData);
			for (URLSpan url : spans) {
				 style.removeSpan(url);
			     MyURLSpan myURLSpan = new MyURLSpan(url.getURL());
			     style.setSpan(myURLSpan, topicData.getSpanStart(url), topicData
			       .getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
			    }
			return style;
	 }

	/**
	 * ��ת��ĳ���������
	 * 
	 * @param AreaData
	 */
	private void chaToTopic(Spanned topicData) {

		setContentView(R.layout.topic);
		
		//topicData.getSpans(arg0, arg1, arg2);
		SpannableStringBuilder urlChanged = getURLChanged(topicData);

		textView = (TextView) findViewById(R.id.label);
		textView.setText(urlChanged);
		textView.setMovementMethod(LinkMovementMethod.getInstance());

//		
//		//textView.
		if(isTouch)
		{
			textView.setOnTouchListener(this);
			textView.setFocusable(true);
			textView.setLongClickable(true);
		}
		
		// WebView mWebView = (WebView) findViewById(R.id.label);
		// mWebView.loadData(getTopicInfo(data), "text/html", "iso-8859-1");

		Button btnBack = (Button) findViewById(R.id.btn_back);
		btnBack.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				if (curStatus == 1) {
					chaToMain();
					if (top10TopicList != null) {
						setTopics();
					}
				} else {
					chaToArea(null);
				}
			}
		});

		Button btnHuifu = (Button) findViewById(R.id.btn_huifu);
		btnHuifu.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				getUrlHtml(huifuUrl, Const.MSGPST);
			}
		});

		Button btnPre = (Button) findViewById(R.id.btn_pre);
		btnPre.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (nowPos < 1) {
					Toast.makeText(TestAndroidActivity.this, "��ǰΪ��һҳ��",
							Toast.LENGTH_SHORT).show();
					return;

				}
				nowPos = nowPos - 30;
				getUrlHtml(topicUrl + "&start=" + nowPos, Const.MSGTOPICNEXT);

			}
		});

		Button btnNext = (Button) findViewById(R.id.btn_next);
		btnNext.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				
				if (isNext) {
					nowPos = nowPos + 30;
					getUrlHtml(topicUrl + "&start=" + nowPos, Const.MSGTOPICNEXT);
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							TestAndroidActivity.this);
					builder.setMessage("�������һҳ���Ƿ�ˢ�µ�ǰҳ?").setCancelable(false)
							.setPositiveButton("ˢ��",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											getUrlHtml(topicUrl + "&start="
													+ nowPos, Const.MSGTOPICREFREASH);
										}
									}).setNegativeButton("����",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											dialog.cancel();
										}
									});
					AlertDialog alert = builder.create();
					alert.show();
				}

			}
		});

	}


	private String dataUrl = "";
	private int datamsg = -1;
	NameValuePair[] nvpCont = null;
	Thread imageTrd;

	private void getUrlHtml(String url, int msg) {
		if (msg ==123 ||progressDialog == null || !progressDialog.isShowing()) {
			progressDialog = ProgressDialog.show(TestAndroidActivity.this,
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
					if (nvpCont == null) {
						data = NetTraffic.getHtmlContent(dataUrl);
					} else {
						data = NetTraffic.postHtmlContent(dataUrl, nvpCont);
						nvpCont = null;
					}
					// Thread.sleep(5000);
				} catch (Exception e) {
					data = "error";
				}

				if (datamsg == Const.MSGTOPIC || datamsg == Const.MSGTOPICNEXT
						|| datamsg == Const.MSGTOPICREFREASH)
				{
					
					if(imageTrd!=null&&imageTrd.isAlive())
					{
						imageTrd.setName("NoUse");
					}
					topicWithImg = false;
					final  String topicDataInfo = StringUtil.getTopicInfo(data,nowPos,isIP,isWifi,isPic);

					isNext = StringUtil.isNext;
					if(StringUtil.curAreaName!=null&&!StringUtil.curAreaName.equals("byztm"))
						curAreaName =  StringUtil.curAreaName;
					topicWithImg = StringUtil.topicWithImg;
					
					topicData = Html.fromHtml(topicDataInfo,
							new Html.ImageGetter() {
								public Drawable getDrawable(String source) {
									
									Drawable drawable = null;
									if ("xian".equals(source)) {
										drawable = xianDraw;
										drawable.setBounds(0, 0, sWidth, 2);
									}
									else if (source.startsWith("[")) 
									{
										try {
											drawable = fetchDrawable(source); 
										} catch (Exception e) {
											return null;
										}
										if (drawable==null) return null;
										int iw = drawable.getIntrinsicWidth();
										drawable.setBounds(0, 0, iw, drawable
												.getIntrinsicHeight());
									}
									return drawable;

								}
							}, null);
					if(topicWithImg)
					{
						
						imageTrd = new Thread(topicDataInfo) {

						@Override
						public void interrupt() {
							this.stop();
						}

						@Override
						public void run() {
							// ��Ҫ��ʱ�����ķ���
							topicData = Html.fromHtml(topicDataInfo,
									new Html.ImageGetter() {

										public Drawable getDrawable(String source) {
											
											Drawable drawable = null;
											if ("xian".equals(source)) {
												drawable = xianDraw;
												drawable.setBounds(0, 0, sWidth, 2);
											} 
											
											else if (source.startsWith("http")||source.startsWith("[")) {
												try {
													drawable = fetchDrawable(source); 
												} catch (Exception e) {
													return null;
												}
												if (drawable==null) return null;
												int iw = drawable.getIntrinsicWidth();
												drawable.setBounds(0, 0, iw, drawable
														.getIntrinsicHeight());
											}
											return drawable;

										}
									}, null);
							if(this.getName()!=null&&!this.getName().equals("NoUse"))
							{
								sendMsg(Const.MSGTOPICREFREASH);
							}

						}
					};
					imageTrd.start();
					}
					
				}
				sendMsg(datamsg);
			}
		}.start();

	}

	HashMap<String, SoftReference<Drawable>> drawableMap = new HashMap<String, SoftReference<Drawable>>();

	public Drawable fetchDrawable(String source) {
		SoftReference<Drawable> drawableRef = drawableMap.get(source);
		if (drawableRef != null) {
			Drawable drawable = drawableRef.get();
			if (drawable != null)
				return drawable;

			drawableMap.remove(source);
		}
		Drawable drawable =null;
		if(source.startsWith("["))
		{
			Resources res = getResources();
			Integer i = smilyAll.get(source);
			if(i!=null)
			{
				drawable = res.getDrawable( i);
			}
			else
				return null;
		}
		else if(source.startsWith("http"))
		{
			 drawable = zoomDrawable(source);
		}
		else
		{
			return null;
		}

		drawableRef = new SoftReference<Drawable>(drawable);
		drawableMap.put(source, drawableRef);

		return drawable;

	}

	/**
	 * ����ͼƬ�����ַ��ȡͼƬ��byte[]��������
	 * 
	 * @param urlPath
	 *            ͼƬ�����ַ
	 * @return ͼƬ����
	 */
	public byte[] getImageFromURL(String urlPath) {
		byte[] data = null;
		InputStream is = null;
		HttpURLConnection conn = null;
		try {
			URL url = new URL(urlPath);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			// conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(6000);
			is = conn.getInputStream();
			if (conn.getResponseCode() == 200) {
				data = readInputStream(is);
			} else {
				data = null;
				return data;
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			conn.disconnect();
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return data;
	}

	/**
	 * ��ȡInputStream���ݣ�תΪbyte[]��������
	 * 
	 * @param is
	 *            InputStream����
	 * @return ����byte[]����
	 */
	public byte[] readInputStream(InputStream is) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length = -1;
		try {
			while ((length = is.read(buffer)) != -1) {
				baos.write(buffer, 0, length);
			}
			baos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] data = baos.toByteArray();
		try {
			is.close();
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * ��������ͼƬ��ַ��������ȡ����ͼƬ
	 * 
	 * @param urlPath
	 *            ����ͼƬ��ַ����
	 * @return ����Bitmap�������͵�����
	 */
	public Drawable zoomDrawable(String urlPath) {

		Bitmap bitmaps;

		byte[] imageByte = getImageFromURL(urlPath.trim());

		// �����ǰ�ͼƬת��Ϊ����ͼ�ټ���
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeByteArray(imageByte, 0,
				imageByte.length, options);

		options.inJustDecodeBounds = false;
		options.inPurgeable = true;
		options.inInputShareable = true;

		int widthRatio = (int) Math.ceil(options.outWidth / sWidth);
		int heightRatio = (int) Math.ceil(options.outHeight / sLength);
		if (widthRatio > 1 || heightRatio > 1) {
			if (widthRatio > heightRatio) {
				options.inSampleSize = widthRatio;
			} else {
				options.inSampleSize = heightRatio;
			}
		}


		bitmaps = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length,
				options);
		return new BitmapDrawable(null, bitmaps);

	}

	

	private void exitPro() {
		new AlertDialog.Builder(TestAndroidActivity.this).setTitle("��ʾ")
				.setMessage("ȷ���˳���").setPositiveButton("ȷ��",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								getUrlHtml(loginoutURL,123);
								
								try {
									Thread.sleep(500);
								} catch (InterruptedException e) {
	
									e.printStackTrace();
								}
								android.os.Process
										.killProcess(android.os.Process.myPid());
							}

						}).setNegativeButton("ȡ��",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {

							}
						}).show();
	}

	private void sendMsg(int meg) {
		Message msg = new Message();
		msg.what = meg;
		handler.sendMessage(msg);
	}

	

	@Override
	protected void onDestroy() {

		super.onDestroy();

		getUrlHtml(loginoutURL,123);
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}

		System.gc();

		System.exit(0);

	}

	public boolean onTouch(View arg0, MotionEvent arg1) {
		return mGestureDetector.onTouchEvent(arg1);
	}

	public boolean onDown(MotionEvent arg0) {
		return false;
	}
	
	
	private static final int SWIPE_MIN_DISTANCE = 120;   
	private static final int SWIPE_THRESHOLD_VELOCITY = 200; 
	

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		
		if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE ) { 
			
			getUrlHtml(huifuUrl, Const.MSGPST);
			
			} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE ) {   
				
				if (curStatus == 1) {
					chaToMain();
					if (top10TopicList != null) {
						setTopics();
					}
				} else {
					chaToArea(null);
				}
			}  

		
		
		return false;
	}

	public void onLongPress(MotionEvent arg0) {
		
	}

	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		return false;
	}

	public void onShowPress(MotionEvent arg0) {

		
	}

	public boolean onSingleTapUp(MotionEvent arg0) {
		ScrollView sv = (ScrollView) findViewById(R.id.scrollView);
		float x = arg0.getRawX();
		float y = arg0.getRawY();
		//����Ϸ��͵���·�
		if(y>sv.getHeight()-sLength/6&&x>(sWidth*3/4))
		{
			sv.scrollBy(0, sv.getHeight()-20);
		}
		if(y<sLength/3&&x>(sWidth*3/4))
		{
			sv.scrollBy(0, 20-sv.getHeight());
		}
	return false;
	}

}
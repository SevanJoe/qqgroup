package com.ztm;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import com.sonyericsson.zoom.ImageTextButton;
import com.ztm.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ExpandableListActivity;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Media;

import android.text.ClipboardManager;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;

public class TestAndroidActivity extends Activity implements OnTouchListener,
		OnGestureListener {

	private GestureDetector mGestureDetector;
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
	String camwidth;
	String isFull;
	String barStat;
	
	
	String isRem = "false";
	
	boolean isAuto;
	boolean isIP;
	boolean isMoreFast;
	String loginId = "";
	String loginPwd = "";

	String androidmanufacturer;
	String androidmodel;

	
	

	int runningTasks = 0;

	private ProgressDialog progressDialog = null;

	SharedPreferences sharedPreferences;

	List<String> areaNamList;

	String[] fastReList;

	Drawable drawableFav;

	Drawable drawableDis;
	boolean isLogin = false;
	String nowLoginId = "";
	
	int backAlpha;
	Spanned topicData;
	int scrollY = 0;
	boolean topicWithImg = false;
	HashMap<String, String> bbsAll;
	HashMap<String, String> bbsAllName;
	HashMap<String, Integer> smilyAll;
	ArrayAdapter<String> bbsAlladapter;
	String bbsURL = "http://bbs.nju.edu.cn/";
	String loginURL = "http://bbs.nju.edu.cn/bbslogin?type=2";
	String loginoutURL = "http://bbs.nju.edu.cn/bbslogout";
	String synUrl = "http://bbs.nju.edu.cn/bbsleft";
	String forumUrl ="http://bbs.nju.edu.cn/cache/t_forum.js";
	String recbrdUrl ="http://bbs.nju.edu.cn/cache/t_recbrd.js";
	String bbsTop10String = "http://bbs.nju.edu.cn/bbstop10";
	String mailURL = "http://bbs.nju.edu.cn/bbsmail";
	String upUrl = "http://lilydroid.co.cc/manage/1.txt";
	
	
	HashMap<String, Integer> fbAll = new HashMap<String, Integer>();
	HashMap<String, String> fbNameAll = new HashMap<String, String>();
	String bbsHotString = "http://bbs.nju.edu.cn/bbstopall";
	List<Map<String, Object>> parentList = null;
	List<List<Map<String, Object>>> allChildList = null;
	ImageSpan hotTopicSpan;
	ImageSpan mailSpan;
	Drawable xianDraw;
	
	int sdensity;
	
	ForegroundColorSpan listColorSpan;
	List<TopicInfo> hotList;
	AbsoluteSizeSpan absoluteSizeSpan;
	List<TopicInfo> mailList = null;
	
	private String dataUrl = "";
	private int datamsg = -1;
	NameValuePair[] nvpCont = null;
	Thread imageTrd;
	Thread acTrd;
	int pageNum;
	String actitle;

	// ���յ���Ƭ�洢λ��
	private static final File PHOTO_DIR = new File(Environment
			.getExternalStorageDirectory()
			+ "/lilyDroid/Photos");
	
	// ��ʱ�ļ�λ��
	private static final File TEMP_DIR = new File(Environment
			.getExternalStorageDirectory()
			+ "/lilyDroid/Temp");
	
	 String TMStr;
	// TODO:����ȫ�ֱ���
	 
	 
	 
	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mGestureDetector = new GestureDetector(this);
		//this.setPersistent(true);
		Resources res = getResources();
		String color = res.getString(R.string.listColor);
		listColorSpan = new ForegroundColorSpan(Color.parseColor(color));
		absoluteSizeSpan = new AbsoluteSizeSpan(13, true);

		Drawable drawable = res.getDrawable(R.drawable.bkcolor);

		this.getWindow().setBackgroundDrawable(drawable);
		Drawable d = getResources().getDrawable(R.drawable.hottopcic);
		d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());

		hotTopicSpan = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);

		d = getResources().getDrawable(R.drawable.unread);
		d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());

		mailSpan = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
		TMStr = "�Y";

		xianDraw = res.getDrawable(R.drawable.xian);
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		ConstParam.sWidth = metric.widthPixels - 30; // ��Ļ��ȣ����أ�
		ConstParam.sLength = metric.heightPixels - 40; // ��Ļ��ȣ����أ�
		sdensity =(int) metric.density;

		bbsAll = BBSAll.getBBSAll();
		bbsAllName = BBSAll.getBBSRightName();
		smilyAll = BBSAll.getSmilyAll();
		String[] bbsAllArray = StringUtil.getArray(bbsAll);
		bbsAlladapter = new ArrayAdapter<String>(TestAndroidActivity.this,
				android.R.layout.simple_dropdown_item_1line, bbsAllArray);
		initPhoneState();
		initAllParams();
		
		
		if(ConstParam.isChange)
		{
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		
		
		if(!TEMP_DIR.exists())
			TEMP_DIR.mkdirs();
		
		
		if(isFull.equals("2"))
		{
			//�����ޱ���  
	        requestWindowFeature(Window.FEATURE_NO_TITLE);  
		}
		else if(isFull.equals("3"))
		{
			//�����ޱ���  
	        requestWindowFeature(Window.FEATURE_NO_TITLE);  
	        //����ȫ��  
	        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
	                WindowManager.LayoutParams.FLAG_FULLSCREEN);  
		}
		else if(isFull.equals("4"))
		{
			
	        //����ȫ��  
	        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
	                WindowManager.LayoutParams.FLAG_FULLSCREEN);  
		}
	
		
		
		initfbAll();
		StringUtil.initAll();

		chaToLogin();
		
		if(isAuto)
		{
		getUpdateInfo(upUrl);
		}
		
		
	}

	private void getUpdateInfo(String upUrl2) {
		final String url = upUrl2;
			acTrd = new Thread() {

				@Override
				public void run() {
					// ��Ҫ��ʱ�����ķ���
					try {
						data = NetTraffic.getHtmlContent(url);
					} catch (Exception e) {
						data = "error";
					}
					sendMsg(Const.MSGUPDATE);
					
				}
			};
			acTrd.start();
	}

	private void InitMain() {
		chaToMain();
		getUrlHtml(bbsTop10String, Const.MSGWHAT);
	}

	private void initfbAll() {
		fbAll.put("fb1", R.drawable.fb1);
		fbAll.put("fb2", R.drawable.fb2);
		fbAll.put("fb3", R.drawable.fb3);
		fbAll.put("fb4", R.drawable.fb4);
		fbAll.put("fb5", R.drawable.fb5);
		fbAll.put("fb6", R.drawable.fb6);
		fbAll.put("fb7", R.drawable.fb7);
		fbAll.put("fb8", R.drawable.fb8);
		fbAll.put("fb9", R.drawable.fb9);
		fbAll.put("fb10", R.drawable.fb10);
		fbAll.put("fb11", R.drawable.fb11);
		fbAll.put("fb12", R.drawable.fb12);
		
		
		
		fbNameAll.put("1", "����վϵͳ��");
		fbNameAll.put("2", "���Ͼ���ѧ��");
		fbNameAll.put("3", "������У���");
		fbNameAll.put("4", "�����Լ�����");
		fbNameAll.put("5", "��ѧ����ѧ��");
		fbNameAll.put("6", "���Ļ�������");
		fbNameAll.put("7", "���������֡�");
		fbNameAll.put("8", "���������С�");
		fbNameAll.put("9", "��������Ϣ��");
		fbNameAll.put("10", "���ٺϹ�ǡ�");
		fbNameAll.put("11", "��У�������");
		fbNameAll.put("12", "������Ⱥ���");
		fbNameAll.put("13", "��������������");
		

	}

	private void initPhoneState() {

		try {

			Class<android.os.Build> build_class = android.os.Build.class;

			// ȡ������

			java.lang.reflect.Field manu_field = build_class
					.getField("MANUFACTURER");

			androidmanufacturer = (String) manu_field
					.get(new android.os.Build());

			// ȡ����̖

			java.lang.reflect.Field field2 = build_class.getField("MODEL");

			androidmodel = (String) field2.get(new android.os.Build());

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	private void chaToLogin() {
		setContentView(R.layout.login);
		isLogin = false;
		nowLoginId = null;
		setTitle("С�ٺ�");
		curStatus = 0;
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

	private void getLikeDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				TestAndroidActivity.this);

		builder.setTitle("ѡ����棺");
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
					public void onClick(DialogInterface dialoginterface, int i) {

						String areaText = areaNamList.get(i);
						urlString = getResources().getString(R.string.areaStr)
								+ areaText;
						curAreaName = "" + areaText;
						dialoginterface.dismiss();
						getUrlHtml(urlString, Const.MSGAREA);

					}
				});

		builder.setPositiveButton("ȡ��", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialoginterface, int i) {

			}
		});
		builder.create().show();
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
		setIndexBtns(1);

	}

	private void setIndexBtns(int i) {
		btnLink = (Button) findViewById(R.id.btn_link);

		btnLink.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				// ���Դ�һ�����߳�����ȡ�������������
				chaToMain();
				getUrlHtml(bbsTop10String, Const.MSGWHAT);
			}

		});

		if (i != 2) {
			Button btnArea = (Button) findViewById(R.id.btn_all);

			btnArea.setOnClickListener(new OnClickListener() {

				public void onClick(View arg0) {
					// ���Դ�һ�����߳�����ȡ�������������
					if (parentList == null || parentList.size() < 3) {
						String url = "http://bbs.nju.edu.cn/bbstopb10";
						getUrlHtml(url, Const.TOP20BOARD);
					} else
						chaToAreaToGo();

				}

			});
		}

		if (i != 3) {
			Button btnLike = (Button) findViewById(R.id.btn_like);

			btnLike.setOnClickListener(new OnClickListener() {

				public void onClick(View arg0) {
					if (isLogin)
						getUrlHtml(mailURL, Const.MSGMAIL);
					else {
						displayMsg("�㻹û��½��~");
					}

				}

			});
		}

		if (i != 4) {
			Button btnSet = (Button) findViewById(R.id.btn_set);

			btnSet.setOnClickListener(new OnClickListener() {

				public void onClick(View arg0) {

					getUrlHtml(bbsHotString, Const.MSGHOT);
				}

			});
		}
	}

	// �˵���
	final private int menuSettings = Menu.FIRST;
	final private int menuLogout = Menu.FIRST + 2;
	final private int menuSyn = Menu.FIRST + 1;
	final private int menuReport = Menu.FIRST + 3;
	final private int menuJump = Menu.FIRST + 4;
	private static final int REQ_SYSTEM_SETTINGS = 0;

	// �����˵�
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// �����˵�
		menu.add(Menu.NONE, menuSettings, 2, "����").setIcon(R.drawable.set);
		menu.add(Menu.NONE, menuSyn, 2, "ͬ���ղ�").setIcon(R.drawable.syn);
		menu.add(Menu.NONE, menuJump, 2, "������ת").setIcon(R.drawable.fast);
		menu.add(Menu.NONE, menuReport, 2, "�������").setIcon(R.drawable.info);
		menu.add(Menu.NONE, menuLogout, 2, "ע��").setIcon(R.drawable.key);
		return super.onCreateOptionsMenu(menu);
	}

	// �˵�ѡ���¼�����
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case menuSettings:
			// ת��Settings���ý���
			startActivityForResult(new Intent(this, Settings.class),
					REQ_SYSTEM_SETTINGS);
			break;
		case menuLogout:
			// ת����¼����
			getUrlHtml(loginoutURL, 123);
			chaToLogin();
			break;

		case menuJump:
			getLikeDialog();
			break;
		case menuSyn:
			// ת����¼����
			if (isLogin)
				getUrlHtml(synUrl, Const.MSGSYN);
			else {
				displayMsg("�㻹û��½��~");
			}
			break;
		case menuReport:

			if (isLogin) {

				String cont = "\n\n\n�ҵĻ��ͣ�" + androidmodel + "\n��Ļ��߱ȣ�"
						+ (ConstParam.sWidth + 30) + "x" + (ConstParam.sLength + 40);

				beginMail("tiztm", "Android��С�ٺ��������", cont, null);
			} else {
				displayMsg("�㻹û��½��~");
			}
			break;
		default:
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	// Settings���ý��淵�صĽ��
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK)
		{
			myParams();
			return;
		}
		switch (requestCode) {
		case PHOTO_PICKED_WITH_DATA: {// ����Gallery���ص�
			doGetPhoto( data.getData()); 
			break;
		}
		case CAMERA_WITH_DATA: {// ��������򷵻ص�,�ٴε���ͼƬ��������ȥ�޼�ͼƬ
			doCropPhoto(mCurrentPhotoFile);
			break;
		}
		//�����ļ��ϴ���uploadFileBBS
		case REQ_SYSTEM_SETTINGS:
			myParams();
			break;
		
		}
	}
	
	protected void doGetPhoto(Uri f) {
		ContentResolver cr = this.getContentResolver(); 
		try {
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor actualimagecursor = managedQuery(f,proj,null,null,null);
			if(actualimagecursor==null)
			{
				FileInputStream fileInputStream = new FileInputStream(f.getPath()); 
				getPhotoBitMap(fileInputStream,f.getPath());
			}
			else
			{
			int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

			actualimagecursor.moveToFirst();

			String img_path = actualimagecursor.getString(actual_image_column_index);
			
			getPhotoBitMap(cr.openInputStream(f),img_path);
			}
			
		
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	protected void doCropPhoto(String f) {
		File file = new File(f);
		try {
			InputStream is =new FileInputStream(file);
			getPhotoBitMap(is,f);
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
	}
	String bbsUploadURL = "http://bbs.nju.edu.cn/bbsdoupload";
	
	public static byte[] getBytes(InputStream is)
   
    {
		 ByteArrayOutputStream baos = new ByteArrayOutputStream();
         byte[] b = new byte[1024];
         int len = 0;

         try {
			while ((len = is.read(b, 0, 1024)) != -1) 
			 {
			  baos.write(b, 0, len);
			  baos.flush();
			 }
		} catch (IOException e) {
			
			e.printStackTrace();
		}
         byte[] bytes = baos.toByteArray();
         return bytes;

    }

	public static int getExifOrientation(String filepath) {
		int degree = 0;
		ExifInterface exif = null;
		try {
			exif = new ExifInterface(filepath);
		} catch (IOException ex) {
		
		}
		if (exif != null) {
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION, -1);
			if (orientation != -1) {
				// We only recognize a subset of orientation tag values.
				switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
				}
			}
		}
		return degree;
	}
	
	
	  public static Bitmap rotate(Bitmap b, int degrees) {
	         if (degrees != 0 && b != null) {
	             Matrix m = new Matrix();
	             m.setRotate(degrees,
	                     (float) b.getWidth() / 2, (float) b.getHeight() / 2);
	             try {
	                 Bitmap b2 = Bitmap.createBitmap(
	                         b, 0, 0, b.getWidth(), b.getHeight(), m, true);
	                 if (b != b2) {
	                     b.recycle();  //�ٴ���ʾBitmap������Ӧ����ʾ���ͷ�
	                     b = b2;
	                 }
	             } catch (OutOfMemoryError ex) {
	                 
	             }
	         }
	         return b;
	     }
	
	
	
	
	protected void getPhotoBitMap(InputStream is,String filePath) 
	{
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
	    byte imageByte[]=getBytes(is);
		@SuppressWarnings("unused")
		Bitmap bitmap = BitmapFactory.decodeByteArray(imageByte, 0,
				imageByte.length, options);
		options.inJustDecodeBounds = false;
		options.inPurgeable = true;
		options.inInputShareable = true;
		int parseInt = 800;
		int widthRatio = (int) Math.ceil(options.outWidth * 1.0 / (parseInt*0.75));
		int heightRatio = (int) Math.ceil(options.outHeight * 1.0 / parseInt);
		if (widthRatio > 1 || heightRatio > 1) {
			if (widthRatio > heightRatio) {
				options.inSampleSize = widthRatio;
			} else {
				options.inSampleSize = heightRatio;
			}
		}
		if(options.inSampleSize <2)
		{
			uploadFileBBS(filePath);
		}
		else
		{
		try {
			Bitmap photo =  BitmapFactory.decodeByteArray(imageByte, 0,
					imageByte.length, options);
			int exifOrientation = getExifOrientation(filePath);
			if(exifOrientation!=0)
				photo=rotate(photo,90);
			String outFilePath = PHOTO_DIR+File.separator+getUpFileName();
			FileOutputStream out;
			out = new FileOutputStream(outFilePath);
			photo.compress(Bitmap.CompressFormat.JPEG, 90, out);
			uploadFileBBS(outFilePath);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		}
		
	}
	
	File uploadFile = null;
	private void uploadFileBBS(String outFilePath) {
			String url = bbsUploadURL ;
			
			uploadFile = new File(outFilePath);

			getUrlHtml(url, Const.MSGPSTFILE);
	}

	private String getUpFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'LilyDroid'MMddHHmmss");
		return dateFormat.format(date) + ".jpg";
	}
	

	

	private void beginMail(String to, String title, String cont, String action) {
		final String thisAction = action;
		LayoutInflater factory = LayoutInflater.from(TestAndroidActivity.this);
		final View acdlgView = factory.inflate(R.layout.maildlg, null);
		Builder altDlg = new AlertDialog.Builder(TestAndroidActivity.this)
				.setTitle("�����ż�").setView(acdlgView).setPositiveButton("ȷ��",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

								EditText titleEdit = (EditText) acdlgView
										.findViewById(R.id.edt_cont);
								String cont = titleEdit
										.getText().toString();

								titleEdit = (EditText) acdlgView
										.findViewById(R.id.edt_to);
								String to = titleEdit.getText().toString();

								titleEdit = (EditText) acdlgView
										.findViewById(R.id.edt_title);
								String title = titleEdit.getText().toString();

								sendMail(to, title, cont, thisAction);
							}
						});

		altDlg.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialoginterface, int i) {

			}
		});

		AlertDialog dlg = altDlg.create();
		EditText titleEdit;
		if (to != null) {
			titleEdit = (EditText) acdlgView.findViewById(R.id.edt_to);
			titleEdit.setText(to);
		}
		if (title != null) {
			titleEdit = (EditText) acdlgView.findViewById(R.id.edt_title);
			titleEdit.setText(title);
		}
		if (cont != null) {
			titleEdit = (EditText) acdlgView.findViewById(R.id.edt_cont);
			titleEdit.setText(cont);
		}
		if (ConstParam.sLength < 300) {
			titleEdit = (EditText) acdlgView.findViewById(R.id.edt_cont);

			LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) titleEdit
					.getLayoutParams(); // ȡ�ؼ�mGrid��ǰ�Ĳ��ֲ���
			linearParams.height = 70;// ���ؼ��ĸ�ǿ�����75����

			titleEdit.setLayoutParams(linearParams);

		}

		dlg.show();
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
		// ����Ƿ��ؼ�,ֱ�ӷ��ص�����
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			retBtn();
			return true;

		} else if (keyCode == KeyEvent.KEYCODE_MENU) {
			LinearLayout mLoadingLayout=(LinearLayout)findViewById(R.id.topicll);
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
		
			return super.onKeyDown(keyCode, event);
		
	}

	
	private void retBtn()
	{
	
		
		if (curStatus == 1) {
			chaToMain();
			if (top10TopicList != null) {
				convtTopics();
			}
		} else if (curStatus == 2) {
			chaToArea(null);
		} else if (curStatus == 3) {
			chaToHot(null);
		} else if (curStatus == 4) {
			chaToAreaToGo();
		} else if (curStatus == 5) {
			chaToMailBox(null);
		}

		else if (curStatus == 0) {
			exitPro();
		}
	}
	private void initAllParams() {
		sharedPreferences = getSharedPreferences("LilyDroid",
				Context.MODE_PRIVATE);
		String name = sharedPreferences.getString("areaName", "");
		String blogName = sharedPreferences.getString("blogName", "");

		areaNamList = new ArrayList<String>();
		ConstParam.blogNamList = new ArrayList<String>();
		
		isRem = sharedPreferences.getString("isRem", "false");
		loginId = sharedPreferences.getString("loginId", "");
		loginPwd = sharedPreferences.getString("loginPwd", "");

		myParams();
		WifiManager mWiFiManager = (WifiManager) this
				.getSystemService(Context.WIFI_SERVICE);
		if (mWiFiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
			isWifi = true;
		}
		
		//�ղص�������
		if (name != null &&name.length() > 1)
		{
		String[] split = name.split(",");
		for (String string : split) {
			String string2 = bbsAllName.get(string.toLowerCase());
			if(string2==null)
				areaNamList.add(string);
			else
				areaNamList.add(string2);
		}
		}
		
		
		//�ղص�BLOG
		if (blogName != null  && blogName.length() > 1)
		{
			String[] split = blogName.split(",");
			for (String string : split) {
				ConstParam.blogNamList.add(string);
			}
		}
		
		
	}
	
	

	private void myParams() {
		SharedPreferences sp = getSharedPreferences("com.ztm_preferences",
				Context.MODE_PRIVATE);
		isPic = sp.getString("picDS", "1");
		isFull = sp.getString("isFull", "1");
		barStat = sp.getString("barStat", "1");
		
		ConstParam.isTouch = sp.getBoolean("isTouch", true);
		ConstParam.isBackWord = sp.getBoolean("isBackWord", true);
		ConstParam.isChange = sp.getBoolean("isChange", true);
		isAuto  = sp.getBoolean("isAuto", true);
		
		
		ConstParam.backWords = sp
				.getString("backWords", "������ �ҵ�С�ٺ�Android�ͻ��� by ${model}");
		isIP = sp.getBoolean("isIP", false);
		ConstParam.signColor = sp.getString("signColor", "[1;32m");
		camwidth =  sp.getString("camwidth", "800");
		backAlpha = sp.getInt("backAlpha", 20);

		backAlpha = (int) ((int) (100 - backAlpha) * 2.55);
		ConstParam.backWords = ConstParam.backWords.replaceAll(TMStr, "");
		ConstParam.backWords = ConstParam.backWords.replaceAll("\\$\\{model\\}", androidmodel+TMStr)
				.replaceAll("\\$\\{manufa\\}", androidmanufacturer);
		isMoreFast = sp.getBoolean("isMoreMoreFast", false);
		ConstParam.txtFonts = sp.getInt("txtFonts", 18);

		String fastRe = sp.getString("fastRe", "ɳ��");
		if (fastRe.length() < 1) {
			fastReList = null;
			return;
		}

		fastReList = fastRe.split("##");

	}

	private void displayMsg(String msg) {
		Toast.makeText(TestAndroidActivity.this, msg, Toast.LENGTH_SHORT)
				.show();
	}

	/**
	 *  ��Ϣ���������������½��棬��Ϊ����ͨ�߳����޷��������½����
	 */
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			
			if(msg.what != Const.MSGUPDATE &&!ConstParam.isLoading)
			{
				return;
			}
			if(msg.what != Const.MSGUPDATE)
			{
			runningTasks--;
			}

			if (msg.what != Const.MSGPSTNEW && data.equals("error")) {
				displayMsg("�������ò���е�С����~");

			} else {
				switch (msg.what) {
				case Const.MSGWHAT:
					// ����ʮ��
					top10TopicList = StringUtil.getTop10Topic(data);
					convtTopics();
					break;
				case Const.MSGTOPIC:
					// ������������
					chaToTopic(topicData);
					break;
				case Const.MSGTOPICNEXT:
					// �������·�ҳ
					textView = (TextView) findViewById(R.id.label);
					ScrollView sv = (ScrollView) findViewById(R.id.scrollView);
					sv.scrollTo(0, 0);
					textView.setText(getURLChanged(topicData));

					break;
				case Const.MSGTOPICREFREASH:
					// ��������ˢ��
					textView = (TextView) findViewById(R.id.label);
					if (textView != null)
						textView.setText(getURLChanged(topicData));
					break;
				case Const.MSGAREA:
					//������
					//getAreaCount();�����ȥ
					chaToArea(data);
					break;

				case Const.MSGAREAPAGES:
					//��������ҳ
					areaPages(data);
					break;

				case Const.MSGLOGIN:
					//��¼
					checkLogin(data);
					break;
				case Const.MSGAUTOLOGIN:
					//�ڵ�ǰҳ���¼������ת
					checkAutoLogin(data);
					break;
				case Const.MSGPST:
					//��ȡ���ĵ�form
					checkForm(data);
					break;
				case Const.MSGPSTNEW:
					// ���� - ���ܻ�ʧ�ܣ�ע�Ᵽ������
					checkRst(data);
					break;

				case Const.MSGVIEWUSER:
					//�鿴�û���Ϣ
					getUserData(data);
					break;

				case Const.MSGSYN:
					//ͬ���ղؼ�
					checkSyn(data);
					break;
				case Const.MSGSYNFIRST:
					//�״ε�¼ͬ���ղؼ�
					checkSyn(data);
					InitMain();
					break;

				case Const.MSGHOT:
					//����������
					bbsHot();
					chaToHot("New");
					break;

				case Const.TOP20BOARD:
					//����20����
					convtTOP20Area(data);
					getUrlHtml(forumUrl, Const.MSGFORUM);
					break;
				case Const.MSGFORUM:
					//����
					convtForum(data);
					getUrlHtml(recbrdUrl, Const.MSGRECBRD);
					break;
				case Const.MSGRECBRD:
					//��������������
					convtRecbrd(data);
					//initAllAreas();
					chaToAreaToGo();
					break;
				case Const.MSGMAIL:
					//���ʼ�
					getMailCont();
					chaToMailBox("");
					break;
				case Const.MSGMAILTOPIC:
					//�ʼ�����鿴
					chaToMailTopic();
					break;
				case Const.MSGREMAIL:
					//�ظ��ʼ�
					checkMailForm(data);
					break;
				case Const.MSGPSTFILE:
					//�ϴ��ļ�
					checkRst(data);
					break;
					
				case Const.BLOGAREA:
					chaToBlog(data);
					break;
					
				case Const.MSGUPDATE:
					checkUpdate(data);
					
					break;
				default:
					break;
				}
			}
			if (msg.what != Const.MSGUPDATE && runningTasks < 1) {
				runningTasks = 0;
				progressDialog.dismiss();
			}

		}

		
	
		

	};
	
	private void checkUpdate(String data) {
		// TODO Auto-generated method stub
		
		/*��ȡ��ǰӦ�õİ汾��*/

		int vc = 99999;
		try {
			vc = TestAndroidActivity.this.getPackageManager().getPackageInfo(TestAndroidActivity.this.getPackageName(), 0).versionCode;
		} catch (Exception e) {


		e.printStackTrace();

		}
		int newvc = 0;
		
		final String[] split = data.replaceAll("\n", "").split("##");
		if(split==null||split.length!=2)
		{
			return;
		}
		
		try {
			newvc = Integer.parseInt(split[0]);
		} catch (Exception e) {

		}
		
		if(vc<newvc)
		{
			
			new AlertDialog.Builder(TestAndroidActivity.this).setTitle("��ʾ")
			.setMessage("�����°汾����Ҫ������").setPositiveButton("ȷ��",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog,
								int which) {
							
							Uri uri = Uri.parse(split[1]); 

							Intent intent =new Intent(Intent.ACTION_VIEW, uri);
							
							startActivity(intent);
							
						}

					}).setNegativeButton("ȡ��",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog,
								int which) {

						}
					}).show();
			
		}
		
		
	}

	
	
	String blogUserName = "";
	private void chaToBlog(String data) {
		
		if(data.contains("��δ����blog"))
		{
			displayMsg("���û���δ����blog");
			return;
		}
		else
			if(data.contains("Ŀǰ�޷�����"))
			{
				displayMsg("���û���blogĿǰ�޷�����");
				return;
			}
			
			else
				if(data.contains("������û�"))
				{
					displayMsg("������û�");
					return;
				}
				
		else
		{
			Intent intent = new Intent(TestAndroidActivity.this,
			BlogActivity.class);
			intent.putExtra("data", data);
			intent.putExtra("blogUserName", blogUserName);
			runningTasks = 0;
			progressDialog.dismiss();

			startActivity(intent);
		}
		
	}

	
	/**
	 * �ʼ����淭ҳ
	 */
	private void goToMailPage(int pageNo) {
		int startPage = areaNowTopic + pageNo;
		if (startPage < 0) {
			startPage = 0;
		}
		getUrlHtml(mailURL + "?start=" + startPage, Const.MSGMAIL);

	}
	/*
	private String getBetterTopic(String infoView)
	{
		String nbs = "<br>";
		String content="";
		String[] split = infoView.split(nbs);
		StringBuffer sb = new StringBuffer();
		int tempBr=0;
		for (String sconA : split) {
			if (sconA.length() < 1) {
				if (tempBr == 0) {
					tempBr = 1;
					sb.append(sconA).append(nbs);
				}
				continue;
			}
			
			sconA = sconA.trim();
//			if(isPic.equals(Const.AllPic)||(isWifi &&isPic.equals(Const.WIFIPic)))
//
//			{
//				if (sconA.toLowerCase().startsWith("http:")
//						&& (sconA.toLowerCase().endsWith(".jpg") 
//								|| sconA.toLowerCase().endsWith(".png")
//								||sconA.toLowerCase().endsWith(".jpeg")
//								||sconA.toLowerCase().endsWith(".gif")
//								))
//				{
//					sb.append("<a href='"+sconA+"'><img src='").append(sconA).append("'></a><br>");
//					continue;
//				}
//			}

			tempBr = 0;
			String scon = "";
			try {
				scon = new String(sconA.getBytes("gb2312"),
						"iso-8859-1");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			if (scon.length() < 71 || scon.length() > 89) {
				sb.append(sconA + nbs);
			} else {
				sb.append(sconA);
			}

		}
		
		if(sb.length()>0)
			content = sb.toString();

	
		return content;
		
	}
*/
	private void chaToMailTopic() {
		char s = 10;
		String backS = s + "";
		String nbs = "<br>";
		data = data.replaceAll(backS,nbs );
		Document doc = Jsoup.parse(data);
		Elements scs = doc.getElementsByTag("textarea");

		if (scs.size() != 1) {
			Toast.makeText(TestAndroidActivity.this, "��ȡ�ʼ�ʧ��",
					Toast.LENGTH_SHORT).show();
		} else {
			Element textArea = scs.get(0);
			String infoView = nbs + textArea.text();
			
			infoView = StringUtil.getBetterTopic(infoView);

			String withSmile = StringUtil.addSmileySpans(infoView,null);
			setContentView(R.layout.mailtopic);

			textView = (TextView) findViewById(R.id.label);
			textView.setText(getSmilyStr(withSmile));
			textView.setTextSize(ConstParam.txtFonts);
			textView.getBackground().setAlpha(backAlpha);
			
			if(barStat.equals("1"))
			{

				btnBarVis = View.GONE;
				
			}		
			else if(barStat.equals("2"))
			{
				btnBarVis = View.VISIBLE; 
			}
			
			LinearLayout mLoadingLayout=(LinearLayout)findViewById(R.id.topicll);
			
			mLoadingLayout.setVisibility(btnBarVis);

			Elements as = doc.getElementsByTag("a");
			for (Element element : as) {
				if (element.text().equals("����")) {
					String ss = (element.getElementsByTag("a")).get(0).attr(
							"href");
					int lastIndexOf = ss.lastIndexOf("Re:");

					String sFir = ss.substring(0, lastIndexOf);
					String sSec = ss.substring(lastIndexOf);

					try {
						sSec = URLEncoder.encode(sSec, "GB2312");
					} catch (UnsupportedEncodingException e) {
						
						e.printStackTrace();
					}
					final String attr = bbsURL + sFir + sSec;

					Button btnPre = (Button) findViewById(R.id.btn_huifu);
					btnPre.setOnClickListener(new OnClickListener() {

						public void onClick(View v) {
							getUrlHtml(attr, Const.MSGREMAIL);
						}
					});

				}
			}

		}

	}

	private void chaToMailBox(String place) {

		setTitle("�ҵ�����");
		setContentView(R.layout.mailbox);
		curStatus = 1;
		LinkAdr = new ArrayList<String>();

		listView = (ListView) findViewById(R.id.topicList);

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		for (TopicInfo topicInfo : mailList) {

			Map<String, Object> map = new HashMap<String, Object>();

			String title = topicInfo.getTitle();
			
			
			int indexOf = title.indexOf('(');
			if(indexOf>-1)
			{
				title = title.substring(0,indexOf);
			}
			
			
			SpannableString ss = null;

			if (topicInfo.getMark().length() > 0) {

				if (topicInfo.getMark().equals("unread")) {

					ss = new SpannableString("[sm]" + title);
					ss.setSpan(mailSpan, 0, 4,
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					map.put("topicm", "");
					
				} else {
					map.put("topicm", "[" + topicInfo.getMark() + "] ");
				}
			} else {
				map.put("topicm", "");
			}
			map.put("topicau", topicInfo.getNums() + " ������:"
					+ topicInfo.getAuthor());
			if (ss == null)
				ss = new SpannableString(title);
			map.put("topictitle", ss);
			map.put("topicother", topicInfo.getPubDate());
			list.add(map);
			LinkAdr.add("http://bbs.nju.edu.cn/" + topicInfo.getLink());

		}
		if (list.size() > 0) {
			MyListAdapter adapter = new MyListAdapter(this, list,
					R.layout.vlist, new String[] { "topictitle", "topicau",
							"topicother","topicm" }, new int[] { R.id.topictitle,
							R.id.topicau, R.id.topicother , R.id.topicm});
			listView.setAdapter(adapter);
			// ��ӵ��
			listView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {

					topicUrl = LinkAdr.get(arg2);

					if (topicUrl == null)
						return;

					// huifuUrl = topicUrl.replace("bbstcon?", "bbspst?");
					curStatus = 5;

					scrollY = listView.getFirstVisiblePosition() + 1;

					getUrlHtml(topicUrl, Const.MSGMAILTOPIC);

				}
			});
		}

		Button btnPre = (Button) findViewById(R.id.btn_pre);
		btnPre.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				goToMailPage(-20);
			}
		});

		Button btnNext = (Button) findViewById(R.id.btn_next);
		btnNext.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				goToMailPage(20);
			}
		});

		Button btnMail = (Button) findViewById(R.id.btn_mail);
		btnMail.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				beginMail(null, null, null, null);
			}
		});

		if (place != null) {
			listView.setSelection(mailList.size() - 1);
		} else {
			listView.setSelection(scrollY);
		}

	}

	private void getMailCont() {
		
		
		
		
		
		Document doc = Jsoup.parse(data);
		Elements tds = doc.getElementsByTag("td");
		int getTopicNo = 0;
		mailList = new ArrayList<TopicInfo>();
		if (tds.size() < 7) {
			
			if (data.contains("����δ��¼")) {
				displayMsg("����δ��¼");
			}
			else
			{
			Toast.makeText(TestAndroidActivity.this, "��������ǿյ�",
					Toast.LENGTH_SHORT).show();
			}
		} else {
			int i = 6;
			while (i < tds.size()) {

				String no = tds.get(i).text();
				int thisPos = 0;
				try {
					thisPos = Integer.parseInt(no);
				} catch (Exception e) {
					return;
				}

				if (getTopicNo == 0) {
					areaNowTopic = thisPos;
					getTopicNo = 1;
				}

				TopicInfo ti = new TopicInfo();
				ti.setNums(no);
				// ����title
				ti.setTitle(tds.get(i + 5).text());
				ti.setLink((tds.get(i + 5).getElementsByTag("a")).get(0).attr(
						"href"));

				String date = DateUtil.formatDateToStrNoWeek(DateUtil
						.getDatefromStrNoWeek(tds.get(i + 4).text()));
				if (date == null || date.equals("null"))
					ti.setPubDate(tds.get(i + 4).text());
				else
					ti.setPubDate(date);

				ti.setAuthor(tds.get(i + 3).text());
				ti.setMark(tds.get(i + 2).text());
				Elements element = (tds.get(i + 2).getElementsByTag("img"));
				if (element != null && element.size() > 0) {
					ti.setMark("unread");
				}
				mailList.add(ti);
				i += 6;
			}
		}
		// return tiList;

	}

	private void bbsHot() {
		Document doc = Jsoup.parse(data);
		Elements tds = doc.getElementsByTag("td");
		int curPos = 0;
		if (!tds.get(0).text().equals("")) {
			displayMsg("�����ȵ��ʽ�б仯���޷�������");
			return;
		}
		hotList = new ArrayList<TopicInfo>();
		;
		for (int i = 0; i < tds.size(); i++) {
			Element element = tds.get(i);
			String text = element.text();
			if (text.equals("")) {
				// img
				Elements elementsByTag = element.getElementsByTag("img");
				if (elementsByTag.size() < 1)
					continue;
				curPos++;
				TopicInfo ti = new TopicInfo();
				ti.setTitle("fb" + curPos);
				hotList.add(ti);

				continue;
			}
			TopicInfo ti = new TopicInfo();
			hotList.add(ti);
			ti.setLink((element.getElementsByTag("a")).get(0).attr("href"));// ����title
			int lastIndexOf = text.lastIndexOf('[');
			ti.setTitle("�� " + text.substring(1, lastIndexOf - 1));// ����title
			ti.setArea(text.substring(lastIndexOf, text.length()));
		}
		hotList.size();
	}

	// ͬ���ղؼ�
	private void checkSyn(String data) {
		Document doc = Jsoup.parse(data);

		Elements as = doc.getElementsByTag("a");
		int ll = areaNamList.size();
		for (Element aTag : as) {
			String href = aTag.attr("href");
			if (href.contains("board?board=")) {
				String tempAreaName = aTag.text().trim().toLowerCase();

				tempAreaName = tempAreaName.toLowerCase();
				String string = bbsAllName.get(tempAreaName);
				if(string !=null)
					tempAreaName = string;
				if (areaNamList.contains(tempAreaName)) {
					continue;
				}
				areaNamList.add(tempAreaName);

			}
		}
		if (ll < areaNamList.size()) {
			int ii = areaNamList.size() - ll;
			storeAreaName();
			// initAllAreas();
			displayMsg("ͬ��WEB�ղؼгɹ�!����" + ii + "�����ղذ���");
		} else {
			displayMsg("��ı����ղؼ��Ѿ������µ��ˣ�");
		}

	}

	private void convtTOP20Area(String areaData) {
		Document doc = Jsoup.parse(data);

		Elements tds = doc.getElementsByTag("td");

		top20List = new ArrayList<String>();
		if (tds.size() < 12) {
			Toast.makeText(TestAndroidActivity.this, "��ȡ����������ʧ��",
					Toast.LENGTH_SHORT).show();
		} else {
			int i = 6;
			while (i < tds.size()) {
				
				// ti.setNums(tds.get(i+4).text());
				top20List.add(tds.get(i + 1).text() + " ("
						+ tds.get(i + 2).text() + ")");
				i += 6;
			}
		}
		// return null;

	}
	
	
	private void convtRecbrd(String data) {
		// 
		String[] split = data.split("\\{brd:");
		recbrdList = new ArrayList<String>();
		if(split.length<2)
		{
			displayMsg("��ȡ�Ƽ�������ʧ��");
		}
		else
		{
			int i = 1;
			while(i<split.length)
			{
				//'JobExpress',bm:'SYSOP yika1985',title:'��ҵ�ؿ� '},
				String[] split2 = split[i].split("'");
				if(split2.length>5)
				{
					recbrdList.add(split2[1]+" ("+split2[5]+")");
				}
				i++;
			}
			
		}
	}
	
	

	private void convtForum(String data) {
		// 
		String[] split = data.split("\\{d:");
		forumList = new ArrayList<String>();
		if(split.length<2)
		{
			displayMsg("��ȡ���ྫ��������ʧ��");
		}
		else
		{
			int i = 1;
			while(i<split.length)
			{
				String nowStr = split[i];
				String[] split2 = nowStr.split("'");
				forumList.add(fbNameAll.get(i+""));
				int j=1;
				while(j<split2.length-3)
				{
					forumList.add(split2[j]+" ("+split2[j+2]+")");
					j+=4;
				}
				i+=1;
			}
		}
	}
	

	/**
	 * ��ȡ�û���Ϣ
	 * 
	 * @param data
	 */
	private void getUserData(String data) {
		char s = 10;
		String backS = s + "";

		data = data.replaceAll(backS, "<br>");
		Document doc = Jsoup.parse(data);

		Elements scs = doc.getElementsByTag("textarea");

		if (scs.size() != 1) {
			Toast.makeText(TestAndroidActivity.this, "��ȡ�û���Ϣʧ��",
					Toast.LENGTH_SHORT).show();
		} else {
			Element textArea = scs.get(0);
			final String infoView = textArea.text();

			
			String withSmile = StringUtil.addSmileySpans(infoView,"��[m]");
			LayoutInflater factory = LayoutInflater
					.from(TestAndroidActivity.this);
			final View info = factory.inflate(R.layout.infodlg, null);
			Builder dlg = new AlertDialog.Builder(TestAndroidActivity.this)
					.setTitle("�û���Ϣ��ѯ").setView(info).setNegativeButton("����",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							String to = infoView.substring(0, infoView
									.indexOf(" ("));
							getToBlogWithName(to);
						}
					});

			

			if (isLogin) {
				dlg.setPositiveButton("д��",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								String to = infoView.substring(0, infoView
										.indexOf(" ("));
								beginMail(to, null, null, null);

							}
						});
				
				
			}

			AlertDialog ad = dlg.create();
			// �����չ���
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
		//System.out.print(data);
		if (data.contains("�ż��Ѽĸ�")) {
			displayMsg("�����ż��ɹ���");
		} else if (data.contains("������������ʺ�")) {
			displayMsg("������������ʺ�! ");
		} else if (data.contains("http-equiv='Refresh'")) {

			if (data.contains("bbsupload2")) {

				int indexOf = data.indexOf("bbsupload2");
				String substring = data.substring(indexOf, data.length()-3);
				String replaceAll = substring.replaceAll("\n", "");
				String url = "http://bbs.nju.edu.cn/"+replaceAll;
				getUrlHtml(url, Const.MSGPSTFILE);
			}
			else if (data.contains("bbsfexp")) {

				int indexOf = data.indexOf("bbsfexp");
				String substring = data.substring(indexOf, data.length()-3);
				String url = "http://bbs.nju.edu.cn/"+substring;
				getUrlHtml(url, Const.MSGPSTFILE);
			}
			
			else
			{
			if (reid.equals("0")) {
				// �����������
				getUrlHtml(urlString, Const.MSGAREAPAGES);
			} else {
				// �ظ����
				getUrlHtml(topicUrl + "&start=" + nowPos,
						Const.MSGTOPICREFREASH);
			}
			displayMsg("���ĳɹ���");
			}
		}else if (data.contains("class=hand>[����URL��ַ]")) {
			// �ϴ��ɹ�,���ϴ����ļ���ַ�ش��������
			data = data.substring(data.indexOf("target=_blank>"));
			String picurl = data.substring(14,data.indexOf("</a>"));
			
			if(acdlgView!=null)
			{
			EditText titleEdit = (EditText) acdlgView
			.findViewById(R.id.edt_cont);
			if(titleEdit!=null)
			{
				titleEdit.append("\r\n"+picurl);
			}
			}
			
		} else if (data.contains("�޸����³ɹ�")) {

			// �޸����
			getUrlHtml(topicUrl + "&start=" + nowPos, Const.MSGTOPICREFREASH);

			displayMsg("�޸ĳɹ���");

		} else if (data.contains("���ر�������")) {

			// �ظ����
			getUrlHtml(topicUrl + "&start=" + nowPos, Const.MSGTOPICREFREASH);

			displayMsg("ɾ���ɹ���");

		}

		else {
			if (data.contains("���η��ļ������")) {
				displayMsg("���η��ļ������, ����Ϣ���������! ");
			} else {
				displayMsg("����ʧ��~�������ݱ����ڼ�������");
			}

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
			nowLoginId = loginId;

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

			// ����ղؼ�Ϊ�գ���ȥ��������ȡ��
			if (areaNamList.size() < 1) {
				getUrlHtml(synUrl, Const.MSGSYNFIRST);
			} else {
				//getUrlHtml(upUrl, Const.MSGUPDATE);
				InitMain();
			}
			// progressDialog.dismiss();

		} else if (scs.size() == 1) {
			if (data.contains("�������") || data.contains("�����ʹ�����ʺ�")) {
				Toast.makeText(TestAndroidActivity.this, "�û����������",
						Toast.LENGTH_SHORT).show();
			} else if (data.contains("���ʺű���login��������")) {
				Toast.makeText(TestAndroidActivity.this, "���ʺű���login�������࣡",
						Toast.LENGTH_SHORT).show();
			}
			else if (data.contains("���ε�¼�������")) {
				Toast.makeText(TestAndroidActivity.this, "���ε�¼�������!!��",
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
			nowLoginId = loginId;
		} else if (scs.size() == 1) {
			if (data.contains("�������") || data.contains("�����ʹ�����ʺ�")) {
				Toast.makeText(TestAndroidActivity.this, "�û����������",
						Toast.LENGTH_SHORT).show();
			} else if (data.contains("���ʺű���login��������")) {
				Toast.makeText(TestAndroidActivity.this, "���ʺű���login�������࣡",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(TestAndroidActivity.this, "��½ʧ�ܣ�",
						Toast.LENGTH_SHORT).show();
			}
			isLogin = false;

		}
		return;
	}

	protected void checkMailForm(String formData) {
		Document doc = Jsoup.parse(formData);
		Elements ins = doc.getElementsByTag("input");
		// progressDialog.dismiss();
		if (ins.size() != 12) {

			// ��¼ʧ�ܣ�Ҫ�����µ�¼
			if (formData.contains("�Ҵҹ���")) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						TestAndroidActivity.this);
				builder.setMessage("�Ҵҹ��Ͳ���д��~���µ�¼?")
						.setPositiveButton("��¼",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										if (isRem.equals("true")) {
											// �Զ���¼�Ļ����Զ���¼
											String url = loginURL + "&id="
													+ loginId + "&pw="
													+ loginPwd;
											getUrlHtml(url, Const.MSGAUTOLOGIN);
										} else {
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
			} else {
				Toast.makeText(TestAndroidActivity.this, "����δ֪������ʧ��",
						Toast.LENGTH_SHORT).show();
			}

		} else {
			String action = ins.get(0).attr("value");
			String title = ins.get(1).attr("value");
			String userId = ins.get(2).attr("value");
			String recont = "";
			try {
				recont = formData
						.substring(
								formData
										.indexOf("<textarea name=text id=text rows=20 cols=80 wrap=physicle>") + 58,
								formData.indexOf("</textarea>"));
			} catch (Exception e) {

			}
			beginMail(userId, title, recont, action);

		}
	}

	String pid;
	String reid;
	String cont;
	View acdlgView;
	/**
	 * ��ȡ���ĵĴ���
	 * 
	 * @param formData
	 */
	protected void checkForm(String formData) {
		Document doc = Jsoup.parse(formData);
		Elements ins = doc.getElementsByTag("input");
		// progressDialog.dismiss();
		if (ins.size() != 12) {

			if (ins.size() != 5) {
				// ��¼ʧ�ܣ�Ҫ�����µ�¼
				if (formData.contains("�Ҵҹ���")) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							TestAndroidActivity.this);
					builder.setMessage("�㻹û��½��~���µ�¼?")
							.setPositiveButton("��¼",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											if (isRem.equals("true")) {
												// �Զ���¼�Ļ����Զ���¼
												String url = loginURL + "&id="
														+ loginId + "&pw="
														+ loginPwd;
												getUrlHtml(url,
														Const.MSGAUTOLOGIN);
											} else {
												chaToLogin();
											}

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
				} else if (formData.contains("����Ȩ�ڴ�������")) {
					Toast.makeText(TestAndroidActivity.this, "����Ȩ�ڴ�����������",
							Toast.LENGTH_SHORT).show();
				} else if (formData.contains("���Ĳ��ɻظ�")) {
					Toast.makeText(TestAndroidActivity.this, "���Ĳ��ɻظ�",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(TestAndroidActivity.this, "����δ֪������ʧ��",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				final String type = ins.get(0).attr("value");
				final String board = ins.get(1).attr("value");
				final String file = ins.get(2).attr("value");
				String recont = formData
						.substring(
								formData
										.indexOf("<textarea name=text rows=20 cols=80 wrap=physicle>") + 50,
								formData.indexOf("</textarea>"));

				LayoutInflater factory = LayoutInflater
						.from(TestAndroidActivity.this);
				final View acdlgView = factory.inflate(R.layout.editdlg, null);
				Builder altDlg = new AlertDialog.Builder(
						TestAndroidActivity.this).setTitle("�޸�����").setView(
						acdlgView).setPositiveButton("ȷ��",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

								EditText titleEdit = (EditText) acdlgView
										.findViewById(R.id.edt_cont);
								String cont = StringUtil.getStrBetter(titleEdit
										.getText().toString());

								sendEdit(cont, type, board, file);
							}

						});

				altDlg.setNegativeButton("ȡ��",
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialoginterface, int i) {

							}
						});

				AlertDialog dlg = altDlg.create();
				// EditText titleEdit = (EditText) acdlgView
				// .findViewById(R.id.edt_title);

				EditText titleEdit = (EditText) acdlgView
						.findViewById(R.id.edt_cont);
				titleEdit.setText(recont);

				dlg.show();

			}

		} else {

			String title = ins.get(0).attr("value");
			if(title==null||title.length()<1)
				title = "�ޱ���";
			pid = ins.get(1).attr("value");
			reid = ins.get(2).attr("value");
			String recont = "";
			try {
				recont = formData
						.substring(
								formData
										.indexOf("<textarea name=text rows=20 cols=80 wrap=physicle>") + 50,
								formData.indexOf("</textarea>"));
			} catch (Exception e) {

			}
			final String extraRecont = recont;
			LayoutInflater factory = LayoutInflater
					.from(TestAndroidActivity.this);
			acdlgView = factory.inflate(R.layout.acdlg, null);
			Builder altDlg = new AlertDialog.Builder(TestAndroidActivity.this)
					.setTitle("����").setView(acdlgView).setPositiveButton("����",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									EditText titleEdit = (EditText) acdlgView
											.findViewById(R.id.edt_title);
									String title = titleEdit.getText()
											.toString();
									
									if(title==null||title.length()<1)
									{
										displayMsg("���������±���~");
										return;
									}
									
									titleEdit = (EditText) acdlgView
											.findViewById(R.id.edt_cont);
									cont = StringUtil.getStrBetter(titleEdit
											.getText().toString());
									// ����ԭ��
									String toCont = cont;
									CheckBox cb = (CheckBox) acdlgView
											.findViewById(R.id.cb_recont);
									if (cb.isChecked() && extraRecont != null
											&& extraRecont.length() > 1) {
										toCont += " " + extraRecont.substring(2);
									}
									sendTopic(title, toCont);
								}

							});

			if (extraRecont.length() < 4) {
				altDlg.setNegativeButton("ȡ��",
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialoginterface, int i) {

							}
						});
			} else {
				altDlg.setNegativeButton("���ٻظ�",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								EditText titleEdit = (EditText) acdlgView
								.findViewById(R.id.edt_cont);
								String ss = StringUtil.getStrBetter(titleEdit
								.getText().toString());
								
								if(ss!=null&&ss.length()>1)
								{
									displayMsg("��������һЩ���ݣ��ݴ��ڼ�������");
									ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
									clipboard.setText(ss);
								}
								
								
								
								if (fastReList.length < 1)
									return;
								if (isMoreFast) {
									titleEdit = (EditText) acdlgView
											.findViewById(R.id.edt_title);
									String title = titleEdit.getText()
											.toString();
									String reText = fastReList[0];
									sendTopic(title, reText);
									return;
								}
								AlertDialog.Builder builder = new AlertDialog.Builder(
										TestAndroidActivity.this);

								builder.setTitle("ѡ��Ҫʹ�õĿ�ݻظ���");

								builder.setSingleChoiceItems(fastReList, 0,
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialoginterface,
													int i) {

												EditText titleEdit = (EditText) acdlgView
														.findViewById(R.id.edt_title);
												String title = titleEdit
														.getText().toString();
												String reText = fastReList[i]
														+ "\n";
												dialoginterface.dismiss();
												sendTopic(title, reText);
											}
										});

								builder.setPositiveButton("ȡ��",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialoginterface,
													int i) {

											}
										});
								builder.create().show();

							}
						});
			}

			AlertDialog dlg = altDlg.create();
			EditText titleEdit = (EditText) acdlgView
					.findViewById(R.id.edt_title);
			CheckBox cb = (CheckBox) acdlgView.findViewById(R.id.cb_recont);
			
			ImageButton btnlog = (ImageButton) acdlgView.findViewById(R.id.btn_cam);
			btnlog.setOnClickListener(new OnClickListener() {

				public void onClick(View arg0) {
					doPickPhotoAction();
				}

			});
			
			btnlog = (ImageButton) acdlgView.findViewById(R.id.btn_smy);
			btnlog.setOnClickListener(new OnClickListener() {

				public void onClick(View arg0) {
					
					getSmilyGrid();
				}
			});
			if (extraRecont.length() < 4) {

				cb.setChecked(false);
				cb.setVisibility(CheckBox.INVISIBLE);
			} else {
				cb.setChecked(true);
			}
			titleEdit.setText(title);

			if (ConstParam.sLength < 300) {
				titleEdit = (EditText) acdlgView.findViewById(R.id.edt_cont);

				LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) titleEdit
						.getLayoutParams(); // ȡ�ؼ�mGrid��ǰ�Ĳ��ֲ���
				linearParams.height = 90;// ���ؼ��ĸ�ǿ�����75����

				titleEdit.setLayoutParams(linearParams);

			}

			dlg.show();

		}
	}
	MyGridAdapter saImageItems;
	private void getSmilyGrid() {
		// TODO Auto-generated method stub
		
		   final Dialog dialog = new Dialog(TestAndroidActivity.this, R.style.FullHeightDialog);  
		   dialog.setContentView(R.layout.smilydlg);  
		   
		   GridView   findViewById =(GridView ) dialog.findViewById(R.id.updater_faceGrid);
		   if(saImageItems==null)
		   {
		   Set<String> keySet = smilyAll.keySet();
		   ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();  
           
		   for (String string : keySet) {
			   HashMap<String, Object> map = new HashMap<String, Object>();  
			   map.put("ItemImage", smilyAll.get(string));//���ͼ����Դ��ID    
			   map.put("ItemText", string);//���ͼ����Դ��ID    
			   lstImageItem.add(map);
		   }

           //������������ImageItem <====> ��̬�����Ԫ�أ�����һһ��Ӧ    
             saImageItems = new MyGridAdapter(this,   
                                                       lstImageItem,//������Դ     
                                                       R.layout.gridview_emotion_item,  
                                                       //��̬������ImageItem��Ӧ������            
                                                       new String[] {"ItemImage","ItemText"},     
                                                           
                                                       //ImageItem��XML�ļ������һ��ImageView  
                                                       new int[] {R.id.imageview_iv});    
		   }
               
             findViewById.setAdapter(saImageItems);    
             findViewById.setOnItemClickListener(new OnItemClickListener() {  
                 public void onItemClick(AdapterView<?> parent, View view,  
                         int position, long id) {  
                     // TODO Auto-generated method stub  
                	 
                     EditText titleEdit = (EditText) acdlgView
						.findViewById(R.id.edt_cont);
                     if(titleEdit!=null)
                     {
                    	 titleEdit.append(view.getTag().toString());
                     }
                     dialog.dismiss();
                 }  
             });  
             
             dialog.show();  
		   
		
	}
	
	private void doPickPhotoAction() {
		//Context context = TestAndroidActivity.this;
		String[] choices;
		choices = new String[2];
		choices[0] = "����";
		choices[1] = "��ͼ��ѡ��";
		final ListAdapter adapter = new ArrayAdapter<String>( TestAndroidActivity.this,
				android.R.layout.simple_dropdown_item_1line, choices);

		final AlertDialog.Builder builder = new AlertDialog.Builder(
				 TestAndroidActivity.this);
		
		builder.setSingleChoiceItems(adapter, -1,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						switch (which) {
						case 0:{
							String status=Environment.getExternalStorageState();
							if(status.equals(Environment.MEDIA_MOUNTED)){//�ж��Ƿ���SD��
								doTakePhoto();// �û�����˴��������ȡ
							}
							else{
								displayMsg("û��SD��");
							}
							break;
							
						}
						case 1:
							doPickPhotoFromGallery();// �������ȥ��ȡ
							break;
						}
					}
				});
		builder.setNegativeButton("����", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
			
			}

			
		});
		builder.create().show();
	}
	
	private String mCurrentPhotoFile;//��������յõ���ͼƬ
	/**
	 * ���ջ�ȡͼƬ
	 * 
	 */
	protected void doTakePhoto() {
		try {
			// Launch camera to take photo for selected contact
			if(!PHOTO_DIR.exists())
				PHOTO_DIR.mkdirs();// ������Ƭ�Ĵ洢Ŀ¼
			mCurrentPhotoFile = PHOTO_DIR.getAbsolutePath()+File.separator+ getPhotoFileName("IMG");// �����յ���Ƭ�ļ�����
			Intent intent = getTakePickIntent(mCurrentPhotoFile);
			startActivityForResult(intent, CAMERA_WITH_DATA);
		} catch (ActivityNotFoundException e) {
			displayMsg("�����û�ĵ���Ƭ");
		}
	}

	public  Intent getTakePickIntent(String ss) {
		
		File imageDirectory = PHOTO_DIR;
		String path = imageDirectory.toString().toLowerCase();
		String name = imageDirectory.getName().toLowerCase();
		ContentValues values = new ContentValues();
		values.put(Media.TITLE, "Image");
		values.put(Images.Media.BUCKET_ID, path.hashCode());
		values.put(Images.Media.BUCKET_DISPLAY_NAME, name);

		values.put(Images.Media.MIME_TYPE, "image/jpeg");
		values.put(Media.DESCRIPTION, "Image capture by camera");
		values.put("_data", mCurrentPhotoFile);
		Uri uri = getContentResolver().insert(Media.EXTERNAL_CONTENT_URI,
				values);
		Intent it = new Intent("android.media.action.IMAGE_CAPTURE");
		it.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		return it;
	}
	
	 	@Override 
	    public void onSaveInstanceState(Bundle savedInstanceState) { 
	      super.onSaveInstanceState(savedInstanceState); 
	      
	    } 

	    
	    @Override 
	    public void onRestoreInstanceState(Bundle savedInstanceState) { 
	      super.onRestoreInstanceState(savedInstanceState); 
	    } 
	    
	@Override
	public void onConfigurationChanged(Configuration newConfig) {

		
		//newConfig.
		super.onConfigurationChanged(newConfig);
		
		
		if(ConstParam.isChange)
		{
			
		}
		else
		
		{
		int s = ConstParam.sWidth;
		ConstParam.sWidth = ConstParam.sLength;
		
		ConstParam.sLength = s;
		}
	}


	

	/**
	 * �õ�ǰʱ���ȡ�õ�ͼƬ����
	 * 
	 */
	private String getPhotoFileName(String beginName) {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'"+beginName+"'yyyyMMddHHmmss");
		return dateFormat.format(date) + ".jpg";
	}

	// ������ʶ�������๦�ܵ�activity
	private static final int CAMERA_WITH_DATA = 3023;

	// ������ʶ����gallery��activity
	private static final int PHOTO_PICKED_WITH_DATA = 3021;

	// ����Gallery����
	protected void doPickPhotoFromGallery() {
		try {
			// Launch picker to choose photo for selected contact
			final Intent intent = getPhotoPickIntent();
			startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
		} catch (ActivityNotFoundException e) {
			displayMsg("�ֻ�����û����Ƭ");
		}
	}
	

	// ��װ����Gallery��intent
	public static Intent getPhotoPickIntent() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		return intent;
	}
	
	
	

	private void sendEdit(String cont, String type, String board, String file) {

		String url = "http://bbs.nju.edu.cn/bbsedit?board=" + board + "&file="
				+ file + "&type=" + type;

		// +"&text="+;

		NameValuePair[] newVp = { new NameValuePair("text", cont) };

		nvpCont = newVp;

		getUrlHtml(url, Const.MSGPSTNEW);

	}

	private void sendMail(String to, String title, String cont, String action) {
		// cont = StringUtil.getStrBetter(cont);
		// �ֻ�ǩ��
		if (ConstParam.isBackWord && ConstParam.backWords != null && ConstParam.backWords.length() > 0) {
			cont += "\n-\n" + ConstParam.signColor + ConstParam.backWords + "[m\n";
		}

		try {
			title = URLEncoder.encode(title, "GB2312");
			String url = "http://bbs.nju.edu.cn/bbssndmail?pid=0" + "&title="
					+ title + "&userid=" + to + "&signature=1";
			if (action != null) {
				//
				url += "&action=" + action;
			}

			// +"&text="+;

			NameValuePair[] newVp = { new NameValuePair("text", cont) };

			nvpCont = newVp;

			getUrlHtml(url, Const.MSGPSTNEW);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

	private void sendTopic(String title, String cont) {
		// �ֻ�ǩ��
		if (ConstParam.isBackWord && ConstParam.backWords != null && ConstParam.backWords.length() > 0) {
			cont += "\n-\n" + ConstParam.signColor + ConstParam.backWords + "[m\n";
		}

		try {
			title = URLEncoder.encode(title, "GB2312"); // new
			// String((title.replace(" ",
			// "%20")).getBytes("UTF-8"),"gb2312");
			String url = "http://bbs.nju.edu.cn/bbssnd?board=" + curAreaName
					+ "&title=" + title + "&pid=" + pid + "&reid=" + reid
					+ "&signature=1";
			// +"&text="+;

			NameValuePair[] newVp = { new NameValuePair("text", cont) };

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
		// saveMyCookie( NUM, id , KEY);
		NetTraffic.setMyCookie(NUM, id, KEY);
	}

	/**
	 * ��������ת��ΪListView�ɶ�����ʽ ��10��ʹ��
	 */
	private void convtTopics() {

		LinkAdr = new ArrayList<String>();

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		for (TopicInfo topicInfo : top10TopicList) {

			Map<String, Object> map = new HashMap<String, Object>();

			String title = topicInfo.getTitle();
			

			map.put("topictitle", title);

			map.put("topicau", "����:" + topicInfo.getAuthor());

			map.put("topicother", "����:" + topicInfo.getArea());

			list.add(map);

			LinkAdr.add("http://bbs.nju.edu.cn/" + topicInfo.getLink());

		}
		if (list.size() > 0) {

			SimpleAdapter adapter = new SimpleAdapter(this, list,
					R.layout.vlist, new String[] { "topictitle", "topicau",
							"topicother" }, new int[] { R.id.topictitle,
							R.id.topicau, R.id.topicother });
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
	private void convtAreaTopics() {

		LinkAdr = new ArrayList<String>();

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		for (TopicInfo topicInfo : areaTopic) {

			Map<String, Object> map = new HashMap<String, Object>();

			String title = topicInfo.getTitle();
			int begin = title.length();
			title += "  (" + topicInfo.getHot() + ")";
			String[] split = topicInfo.getHot().split("/");
			SpannableString sp = null;

			if (split.length == 2) {
				int re = Integer.parseInt(split[0]);
				int watch = Integer.parseInt(split[1]);
				if (re > 9 || watch > 500) {
					sp = new SpannableString(title + "[sm]");
					sp.setSpan(hotTopicSpan, title.length(),
							title.length() + 4,
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			}
			if (sp == null) {
				sp = new SpannableString(title);
			}
			sp.setSpan(listColorSpan, begin, title.length(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			sp.setSpan(absoluteSizeSpan, begin, title.length(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

			map.put("topictitle", sp);
			String place = "";
			if (topicInfo.getNums() == null || topicInfo.getNums().equals("")) {
				place = "";
			} else {
				place = topicInfo.getNums();
			}

			if (topicInfo.getMark().length() > 0) {
				map.put("topicm", "[" + topicInfo.getMark() + "] ");
			} else {
				map.put("topicm", "");

			}
			map.put("topicau", place + " ����:" + topicInfo.getAuthor());
			map.put("topicother", topicInfo.getPubDate());

			list.add(map);

			LinkAdr.add("http://bbs.nju.edu.cn/" + topicInfo.getLink());

		}
		if (list.size() > 0) {

			MyListAdapter adapter = new MyListAdapter(this, list,
					R.layout.vlist, new String[] { "topictitle", "topicau",
							"topicother","topicm" }, new int[] { R.id.topictitle,
							R.id.topicau, R.id.topicother, R.id.topicm  });
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
					scrollY = listView.getFirstVisiblePosition() + 1;

					getUrlHtml(topicUrl, Const.MSGTOPIC);

				}
			});
		}
	}

	/**
	 * ����HTMLҳ��ת��������ת��ΪListView�ɶ�����ʽ �������ȵ�ʹ��
	 */
	private void convtHotTopics() {

		LinkAdr = new ArrayList<String>();

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		for (TopicInfo topicInfo : hotList) {
			Map<String, Object> map = new HashMap<String, Object>();
			if (topicInfo.getArea() == null || topicInfo.getArea().length() < 1) {
				map.put("img", fbAll.get(topicInfo.getTitle()));
			} else {

				String title = topicInfo.getTitle();

				map.put("topictitle", title);

				map.put("topicau", topicInfo.getArea());

			}
			list.add(map);

			LinkAdr.add("http://bbs.nju.edu.cn/" + topicInfo.getLink());

		}
		if (list.size() > 0) {

			MyImageListAdapter adapter = new MyImageListAdapter(this, list,
					R.layout.hotlist, new String[] { "topictitle", "img",
							"topicau" }, new int[] { R.id.topictitle,
							R.id.itemImage, R.id.topicau });
			listView.setAdapter(adapter);
			// ��ӵ��
			listView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {

					topicUrl = LinkAdr.get(arg2);

					if (topicUrl == null || topicUrl.length() < 27)
						return;

					huifuUrl = topicUrl.replace("bbstcon?", "bbspst?");
					curStatus = 3;
					nowPos = 0;
					scrollY = listView.getFirstVisiblePosition();
					getUrlHtml(topicUrl, Const.MSGTOPIC);

				}
			});
		}
	}
	int lastPar = -1;
	int lastChd = -1;
	private void chaToAreaToGo() {

		setTitle("��ת������");

		curStatus = 1;
		setContentView(R.layout.gotoarea);

		AutoCompleteTextView secondPwd = (AutoCompleteTextView) findViewById(R.id.area_edit);
		if (secondPwd.getAdapter() == null) {
			secondPwd.setAdapter(bbsAlladapter);
			secondPwd.setThreshold(1);
		}
		Button btnBack = (Button) findViewById(R.id.btn_go);
		btnBack.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				EditText secondPwd = (EditText) findViewById(R.id.area_edit);
				String inputPwd = secondPwd.getText().toString();
				//�ж�b ��ͷ��ȥblog   g ��ͷ��ȥ������
				if(inputPwd.startsWith("b "))
				{
					getToBlogWithName(inputPwd.substring(2));
				}
				else if(inputPwd.startsWith("g "))
				{
					getToAreaWithName(inputPwd.substring(2));
				}
				else
				{
					getToAreaORBlogWithName(inputPwd);
				}
				
				//getToAreaWithName(inputPwd);
			}
		});
		//if (parentList == null || parentList.size() < 3)
			initAllAreas();

		android.widget.SimpleExpandableListAdapter adapter = new android.widget.SimpleExpandableListAdapter(
				this, parentList, R.layout.explistparent,
				new String[] { "TITLE" }, new int[] { android.R.id.text1 },
				allChildList, R.layout.explistchild, new String[] { "TITLE" },
				new int[] { android.R.id.text1 });
		// create child's OnChildClickListener
		android.widget.ExpandableListView listView = (android.widget.ExpandableListView) findViewById(R.id.area_view);

		// Adapter set
		listView.setAdapter(adapter);
		listView.setOnChildClickListener(new android.widget.ExpandableListView.OnChildClickListener() {

					public boolean onChildClick(
							android.widget.ExpandableListView parent, View v,
							int groupPosition, int childPosition, long id) {
						
						//parent.get
						
						Map<String, Object> childMap = allChildList.get(
								groupPosition).get(childPosition);
						
						Map<String, Object> parentMap = parentList.get(groupPosition);
						String pareName = (String) parentMap.get("TITLE");
						if(pareName.equals("�����ղ�"))
						{
							String name = (String) childMap.get("TITLE");
							String blogName = "";
							if(name.equals("�ҵĲ���"))
							{
								if(isLogin)
								{
									blogName = loginId;
								}
								else
								{
									displayMsg("�㻹û�е�¼~");
								}
							}
							else
								blogName = name;
							
							getToBlogWithName(blogName);
							
						}
						else
						{
						
						
							String name = (String) childMap.get("TITLE");
							if (name == null || name.length() < 1||name.contains("��"))
								return false;
							int indexOf = name.indexOf('(');
							if (indexOf > 0) {
								name = name.substring(0, indexOf);
							}
							getToAreaWithName(name);
						
						}
						
						
						lastPar = groupPosition;
						lastChd=childPosition;
						return false;
					}
				});
		setIndexBtns(2);
		
		if(lastPar>-1)
		{
			
			listView.expandGroup(lastPar);
			
			
			listView.setSelectedChild(lastPar, lastChd, true);
			lastPar = -1;
			lastChd = -1;
		}

	}
	
	
	private void getToAreaORBlogWithName(String name) {
		if (name == null || name.length() < 1)
			return;
		name = name.trim();
		String areaText = bbsAll.get(name);
		if(areaText == null)
		{
			areaText = name;
			areaText = areaText.toLowerCase();
			String string = bbsAllName.get(areaText);
			if(string !=null)
				areaText = string;
			else
			{
				getToBlogWithName(areaText);
				return;
			}
		}
		
		urlString = getResources().getString(R.string.areaStr) + areaText;
		curAreaName = "" + areaText;
		getUrlHtml(urlString, Const.MSGAREA);
	}

	private void getToAreaWithName(String name) {
		if (name == null || name.length() < 1)
			return;
		name = name.trim();
		String areaText = bbsAll.get(name);
		if(areaText == null)
		{
			areaText = name;
			areaText = areaText.toLowerCase();
			String string = bbsAllName.get(areaText);
			if(string !=null)
				areaText = string;
		}
		
		
		urlString = getResources().getString(R.string.areaStr) + areaText;
		curAreaName = "" + areaText;
		getUrlHtml(urlString, Const.MSGAREA);
	}
	
	private void getToBlogWithName(String name) {
		if (name == null || name.length() < 1)
			return;
		name = name.trim();
		
		String url = blogUrl+name;
		blogUserName = name;
		getUrlHtml(url, Const.BLOGAREA);
	}
	
	
	String blogUrl = "http://bbs.nju.edu.cn/vd59879/blogdoc?userid=";
	
	
	
	

	List<String> top20List;
	List<String> forumList;
	List<String> recbrdList;

	private void initAllAreas() {
		parentList = new ArrayList<Map<String, Object>>();
		allChildList = new ArrayList<List<Map<String, Object>>>();
		Map<String, Object> childData ;
		Map<String, Object> parentData = new HashMap<String, Object>();
		parentData.put("TITLE", "�����ղ�");
		parentList.add(parentData);

		List<Map<String, Object>> childList = new ArrayList<Map<String, Object>>();

		for (String s : areaNamList) {
			 childData = new HashMap<String, Object>();
			childData.put("TITLE", s);
			childList.add(childData);
		}
		allChildList.add(childList);

		if (top20List != null) {
			parentData = new HashMap<String, Object>();
			parentData.put("TITLE", "��������");
			parentList.add(parentData);

			childList = new ArrayList<Map<String, Object>>();
			for (String topicInfo : top20List) {
				 childData = new HashMap<String, Object>();
				childData.put("TITLE",topicInfo);
				childList.add(childData);
			}
			allChildList.add(childList);
		}
		
		if (forumList != null) {
			parentData = new HashMap<String, Object>();
			parentData.put("TITLE", "���ྫ��");
			parentList.add(parentData);

			childList = new ArrayList<Map<String, Object>>();
			for (String topicInfo : forumList) {
				childData = new HashMap<String, Object>();
				childData.put("TITLE",topicInfo);
				childList.add(childData);
			}
			allChildList.add(childList);
		}
		
		if (recbrdList != null) {
			parentData = new HashMap<String, Object>();
			parentData.put("TITLE", "��ҳ�Ƽ�");
			parentList.add(parentData);

			childList = new ArrayList<Map<String, Object>>();
			for (String topicInfo : recbrdList) {
				childData = new HashMap<String, Object>();
				childData.put("TITLE",topicInfo);
				childList.add(childData);
			}
			allChildList.add(childList);
		}
		
		
		parentData = new HashMap<String, Object>();
		parentData.put("TITLE", "�����ղ�");
		parentList.add(parentData);

		childList = new ArrayList<Map<String, Object>>();
		for (String s : ConstParam.blogNamList) {
			childData = new HashMap<String, Object>();
			childData.put("TITLE", s);
			childList.add(childData);
		}
		allChildList.add(childList);
		
		
		
	}

	boolean isNext = true;
	boolean isPrev = true;

	private void chaToHot(String HotData) {

		setTitle("�����ȵ�");

		curStatus = 1;
		setContentView(R.layout.hot);

		listView = (ListView) findViewById(R.id.topicList);
		convtHotTopics();
		if (HotData == null) {
			listView.setSelection(scrollY);
		}
		setIndexBtns(4);

	}

	/**
	 * ��ת������������
	 * 
	 * @param AreaData
	 */
	private void chaToArea(String AreaData) {

		if (AreaData != null && AreaData.contains("����! �����������")) {
			Toast.makeText(TestAndroidActivity.this, "�������������ڣ�",
					Toast.LENGTH_SHORT).show();
			return;
		}

		setContentView(R.layout.area);
		curStatus = 4;
		Button btnBack = (Button) findViewById(R.id.btn_back);
		btnBack.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
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

		ImageTextButton btnLike = (ImageTextButton) findViewById(R.id.btn_like);

		if (areaNamList.contains(curAreaName)) {
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
				if (areaNamList.contains(curAreaName)) {
					areaNamList.remove(curAreaName);
					// btnLike.setBackgroundDrawable(drawableFav);
					btnLike.setText("�� ��");
					btnLike.setIcon(R.drawable.unfav);
					

				} else {
					areaNamList.add(curAreaName);
					// btnLike.setBackgroundDrawable(drawableDis);
					btnLike.setText("�� ��");
					btnLike.setIcon(R.drawable.fav);
				}
				storeAreaName();
			}

		});

		if (AreaData != null) {
			newUrl = "http://bbs.nju.edu.cn/bbspst?board=" + curAreaName;
			areaTopic = getAreaTopic(AreaData);
		}
		listView = (ListView) findViewById(R.id.topicList);

		convtAreaTopics();
		if (AreaData == null) {
			// listView.requestFocusFromTouch();

			listView.setSelection(scrollY);
			//listView.getSelectedView().
			//listView.p
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

		//initAllAreas();
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
		convtAreaTopics();
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

				String date = DateUtil.formatDateToStrNoWeek(DateUtil
						.getDatefromStrNoWeek(tds.get(curPos + 3).text()));
				if (date == null || date.equals("null"))
					ti.setPubDate(tds.get(curPos + 3).text());
				else
					ti.setPubDate(date);
				ti.setAuthor(tds.get(curPos + 2).text());
				ti.setMark(tds.get(curPos + 1).text());
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
			ds.setUnderlineText(underline);// һ�������涼��һ���ߣ�ͦ���ģ� �÷�������ȥ��������
			// ds.setColor(Color.rgb(0, 0, 237));// �ı����ӵ���ɫ����
		}

		@Override
		public void onClick(View widget) {
			// �˴�д��Ĵ����߼�
			// System.out.println("123123");
			// processHyperLinkClick(text); //���������ʱ����
			if (mUrl.toLowerCase().startsWith("http:")
					&& (mUrl.toLowerCase().endsWith(".jpg")
							|| mUrl.toLowerCase().endsWith(".png")
							|| mUrl.toLowerCase().endsWith(".jpeg") || mUrl
							.toLowerCase().endsWith(".gif"))) {
				Intent intent = new Intent(TestAndroidActivity.this,
						ImageActivity.class);
				intent.putExtra("mUrl", mUrl);

				startActivity(intent);
			} else if (mUrl.contains("bbsqry?userid")) {
				// �鿴�û�
				getUrlHtml(mUrl, Const.MSGVIEWUSER);
			} else if (mUrl.contains("bbspst?board")) {
				// �ظ�
				getUrlHtml(mUrl, Const.MSGPST);
			}

			else if (mUrl.contains("bbsedit?board")) {
				// �޸�
				getUrlHtml(mUrl, Const.MSGPST);
			}

			else if (mUrl.contains("bbsdel?board")) {
				// ɾ��
				AlertDialog.Builder builder = new AlertDialog.Builder(
						TestAndroidActivity.this);

				builder.setTitle("��ʾ").setMessage("ȷ��ɾ�����ģ�").setPositiveButton(
						"ȷ��", new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								getUrlHtml(mUrl, Const.MSGPSTNEW);
							}

						}).setNegativeButton("ȡ��",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {

							}
						}).show();

			}

		}
	}

	public SpannableStringBuilder getURLChanged(Spanned topicData) {
		URLSpan[] spans = topicData.getSpans(0, topicData.length(),
				URLSpan.class);
		SpannableStringBuilder style = new SpannableStringBuilder(topicData);
		for (URLSpan url : spans) {
			style.removeSpan(url);
			MyURLSpan myURLSpan = new MyURLSpan(url.getURL());
			style.setSpan(myURLSpan, topicData.getSpanStart(url), topicData
					.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		}
		return style;
	}
	
	
	
	private void doTopicJump() {
		String[] choices;
		choices = new String[3];
		choices[0] = "���ʸ�������";
		choices[1] = "�鿴������ȫ��";
		choices[2] = "��ת��������ĳһҳ";
		final ListAdapter adapter = new ArrayAdapter<String>( TestAndroidActivity.this,
				android.R.layout.simple_dropdown_item_1line, choices);

		final AlertDialog.Builder builder = new AlertDialog.Builder(
				 TestAndroidActivity.this);
		
		builder.setSingleChoiceItems(adapter, -1,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						switch (which) {
						case 0:{
							getToAreaWithName( curAreaName);
							break;
						}
						case 1:{
							nowPos = -1;
							isNext = false;
							getUrlHtml(topicUrl + "&start=-1",
									Const.MSGTOPICNEXT);
							break;
						}
						case 2:{
							int no = pageNum/30+1;
							if(no>1)
							{
								String[] choices;
								choices = new String[no];
								for (int i=0;i<no;i++) {
									choices[i] = "�� "+(i+1)+" ҳ";
								}
								final ListAdapter adapter = new ArrayAdapter<String>( TestAndroidActivity.this,
										android.R.layout.simple_dropdown_item_1line, choices);

								final AlertDialog.Builder builder = new AlertDialog.Builder(
										 TestAndroidActivity.this);
								
								builder.setSingleChoiceItems(adapter, -1,
										new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog, int which) {
												dialog.dismiss();
												nowPos = 30*(which);
												getUrlHtml(topicUrl + "&start=" + nowPos, Const.MSGTOPICNEXT);
											}});
								
								builder.setNegativeButton("����", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {
										
									}
								});
								builder.create().show();
								
							}
							else
							{
								displayMsg("�������һҳ~");
							}
							
							
							
							break;
						}
						}
					}
				});
		builder.setNegativeButton("����", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
			
			}

			
		});
		builder.create().show();
	}

	/**
	 * ��ת��ĳ���������
	 * 
	 * @param AreaData
	 */
	private void chaToTopic(Spanned topicData) {
		
		if(topicData.toString().length()<2)
		{
			displayMsg("�������ѱ�ɾ����");
			return;
		}

		setContentView(R.layout.topic);

		SpannableStringBuilder urlChanged = getURLChanged(topicData);

		textView = (TextView) findViewById(R.id.label);
		textView.setText(urlChanged);
		textView.setTextSize(ConstParam.txtFonts);
		textView.setMovementMethod(LinkMovementMethod.getInstance());

		if(actitle!=null&&actitle.length()>1)
		{
			setTitle(curAreaName+" - "+actitle);
		}
		textView.getBackground().setAlpha(backAlpha);
		if (ConstParam.isTouch) {
			textView.setOnTouchListener(this);
			textView.setFocusable(true);
			textView.setLongClickable(true);
		}
		
		if(barStat.equals("1"))
		{

			btnBarVis = View.GONE;
			
		}		
		else if(barStat.equals("2"))
		{
			btnBarVis = View.VISIBLE; 
		}
		
		
		
		LinearLayout mLoadingLayout=(LinearLayout)findViewById(R.id.topicll);
		mLoadingLayout.setVisibility(btnBarVis);
		


		Button btnBack = (Button) findViewById(R.id.btn_back);
		btnBack.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				doTopicJump();
				
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

				if (isNext&&nowPos!=-1) {
					nowPos = nowPos + 30;
					getUrlHtml(topicUrl + "&start=" + nowPos,
							Const.MSGTOPICNEXT);
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							TestAndroidActivity.this);
					builder.setMessage("�������һҳ���Ƿ�ˢ�µ�ǰҳ?")
							.setPositiveButton("ˢ��",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											getUrlHtml(topicUrl + "&start="
													+ nowPos,
													Const.MSGTOPICREFREASH);
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



	private void getUrlHtml(String url, int msg) {
		runningTasks++;
		dataUrl = url;
		datamsg = msg;
		acTrd = new Thread() {

			@Override
			public void run() {
				// ��Ҫ��ʱ�����ķ���
				try {
					 if (uploadFile !=null)
					{
						data = NetTraffic.postFile(dataUrl, uploadFile,curAreaName);
						uploadFile = null;
					}
					else if (nvpCont !=null){
						data = NetTraffic.postHtmlContent(dataUrl, nvpCont);
						nvpCont = null;
					}
					else {
							data = NetTraffic.getHtmlContent(dataUrl);
						} 
					 
					// Thread.sleep(5000);
				} catch (Exception e) {
					data = "error";
				}
				
				if(!ConstParam.isLoading )
				{
					return;
				}
				if (this.getName() != null
						&& this.getName().equals("NoUse")) {
					return;
				}
				

				if (datamsg == Const.MSGTOPIC || datamsg == Const.MSGTOPICNEXT
						|| datamsg == Const.MSGTOPICREFREASH) {

					if (imageTrd != null && imageTrd.isAlive()) {
						imageTrd.setName("NoUse");
					}
					topicWithImg = false;
					final String topicDataInfo = StringUtil.getTopicInfo(data,
							nowPos, isIP, isWifi, isPic, nowLoginId);
					if (topicDataInfo != null) {
						isNext = StringUtil.isNext;
						if (StringUtil.curAreaName != null
								&& !StringUtil.curAreaName.equals("byztm"))
						{
							curAreaName = StringUtil.curAreaName;
							String string = bbsAllName.get(curAreaName.toLowerCase());
							if(string!=null)
								curAreaName = string;
						}
						topicWithImg = StringUtil.topicWithImg;
						pageNum = StringUtil.pageNum;

						actitle = StringUtil.actitle;
						topicData =getSmilyStr(topicDataInfo);
						if (topicWithImg) {

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

												public Drawable getDrawable(
														String source) {

													Drawable drawable = null;
													if ("xian".equals(source)) {
														drawable = xianDraw;
														drawable.setBounds(0,
																0, ConstParam.sWidth, 2);
													}

													else if (source
															.startsWith("http")
															|| source
																	.startsWith("[")) {
														try {
															drawable = fetchDrawable(source);
														} catch (Exception e) {
															return null;
														}
														if (drawable == null)
															return null;
														int iw = drawable
																.getIntrinsicWidth();
														drawable
																.setBounds(
																		0,
																		0,
																		iw,
																		drawable
																				.getIntrinsicHeight());
													}
													return drawable;

												}
											}, null);
									if (this.getName() == null
											|| !this.getName().equals("NoUse")) {
										sendMsg(Const.MSGTOPICREFREASH);
									}

								}
							};
							imageTrd.start();
						}
					}

				}
				sendMsg(datamsg);
			}
		};
		
		
		if (msg == 123 || progressDialog == null || !progressDialog.isShowing()) {
			progressDialog = ProgressDialog.show(TestAndroidActivity.this,
					"���Ե�...", "ץȡ��ҳ��Ϣ��...", true);
			progressDialog.setCancelable(true);
			 progressDialog.setOnCancelListener(new OnCancelListener(){
			     public void onCancel(DialogInterface arg0) {
			    		ConstParam.isLoading = false;
			    		runningTasks--;
			    		acTrd.setName("NoUse");
			      
			     }});
			
			
			ConstParam.isLoading = true;
		}
	
		
		acTrd.start();


	}

	HashMap<String, SoftReference<Drawable>> drawableMap = new HashMap<String, SoftReference<Drawable>>();
	

	
	public Spanned getSmilyStr(String string) {
		 return Html.fromHtml(string,
					new Html.ImageGetter() {
						public Drawable getDrawable(String source) {

							Drawable drawable = null;
							if ("xian".equals(source)) {
								drawable = xianDraw;
								drawable.setBounds(0, 0, ConstParam.sWidth, 2);
							} else	if (source.startsWith("[")) {
								try {
									drawable = fetchDrawable(source);
								} catch (Exception e) {
									return null;
								}
								if (drawable == null)
									return null;
								int iw = drawable
										.getIntrinsicWidth();
								drawable
										.setBounds(
												0,
												0,
												iw,
												drawable
														.getIntrinsicHeight());
							}
							return drawable;

						}
					}, null);
		}
	
	
	

	public Drawable fetchDrawable(String source) {
		SoftReference<Drawable> drawableRef = drawableMap.get(source);
		if (drawableRef != null) {
			Drawable drawable = drawableRef.get();
			if (drawable != null)
				return drawable;

			drawableMap.remove(source);
		}
		Drawable drawable = null;
		if (source.startsWith("[")) {
			Resources res = getResources();
			Integer i = smilyAll.get(source);
			if (i != null) {
				drawable = res.getDrawable(i);
			} else
				return null;
		} else if (source.startsWith("http")) {
			//�ȳ��ԴӴ��̶�ȡ
			
			String  path =StringUtil.picTempFiles.get(source);
			if(path==null) drawable = zoomDrawable(source);
			else
			{
			byte[] file=null;
			try {
				file = getFile(path);
			} catch (Exception e) {
				
			}
			if(file!=null)
			{
				drawable = getDrawFromByte(file);
			}
			else
			{
			drawable = zoomDrawable(source);
			}
			}
		} else {
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
			
			//getPhotoFileName("");
			
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

	int fileNamei=0;
	
	
	/**
	 * ��������ͼƬ��ַ��������ȡ����ͼƬ
	 * 
	 * @param urlPath
	 *            ����ͼƬ��ַ����
	 * @return ����Bitmap�������͵�����
	 */
	public Drawable zoomDrawable(String urlPath) {

		
		byte[] imageByte = getImageFromURL(urlPath.trim());
		
		
		String filePath = TEMP_DIR+File.separator+"TEMP"+fileNamei;
		fileNamei++;
		StringUtil.picTempFiles.put(urlPath, filePath);
		
		try {
			saveFile(imageByte,filePath);
		} catch (Exception e) {
			
		}
		return getDrawFromByte(imageByte);
	}
	
	
	public static byte[] getFile(String path) throws Exception {  
        byte[] b = null;  
        File file = new File(path);  
  
        FileInputStream fis = null;  
        ByteArrayOutputStream ops = null;  
        try {  
  
            if (!file.exists()) {  
               
                return null;
            }  
            if (file.isDirectory()) {  
            	   return null;
            }  
  
            byte[] temp = new byte[2048];  
  
            fis = new FileInputStream(file);  
            ops = new ByteArrayOutputStream(2048);  
  
            int n;  
            while ((n = fis.read(temp)) != -1) {  
                ops.write(temp, 0, n);  
            }  
            b = ops.toByteArray();  
        } catch (Exception e) {  
        	b=null;
            throw new Exception();  
        } finally {  
            if (ops != null) {  
                ops.close();  
            }  
            if (fis != null) {  
                fis.close();  
            }  
        }  
        return b;  
    }  
	

	public static void saveFile(byte[] b, String path) throws Exception {
		File file = new File(path);
		//file.createNewFile();
		FileOutputStream fis = null;
		BufferedOutputStream bos = null;
		try {
			fis = new FileOutputStream(file);
			bos = new BufferedOutputStream(fis);
			bos.write(b);
		} catch (Exception e) {
			System.out.println("asdsd");
			throw new Exception(e);
		} finally {
			if (bos != null) {
				bos.close();
			}
			if (fis != null) {
				fis.close();
			}
		}
	}
	
	
	
	
	public Drawable getDrawFromByte(byte[] imageByte )
	{
		Bitmap bitmaps;


		// �����ǰ�ͼƬת��Ϊ����ͼ�ټ���
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeByteArray(imageByte, 0,
				imageByte.length, options);

		options.inJustDecodeBounds = false;
		options.inPurgeable = true;
		options.inInputShareable = true;
		//options.inDensity = sdensity;

		int widthRatio = (int) Math.ceil(options.outWidth * 1.0 / ConstParam.sWidth);
		int heightRatio = (int) Math.ceil(options.outHeight * 1.0 / ConstParam.sLength);
		if (widthRatio > 1 || heightRatio > 1) {
			if (widthRatio > heightRatio) {
				options.inSampleSize = widthRatio;
			} else {
				options.inSampleSize = heightRatio;
			}
		}
		if (ConstParam.sWidth < 260)
			options.inSampleSize = options.inSampleSize * 2;
		
		bitmaps = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length,
				options);
	
		if(options.inSampleSize>1)
		return new BitmapDrawable(null, bitmaps);
		else
			return new BitmapDrawable(this.getResources(), bitmaps);
	}

	private void exitPro() {
		new AlertDialog.Builder(TestAndroidActivity.this).setTitle("��ʾ")
				.setMessage("ȷ���˳���").setPositiveButton("ȷ��",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								getUrlHtml(loginoutURL, 123);

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

		getUrlHtml(loginoutURL, 123);

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}

		System.gc();

		System.exit(0);

	}

	public boolean onTouch(View arg0, MotionEvent arg1) {
		try {
			if(arg1!=null)
			{
			return mGestureDetector.onTouchEvent(arg1);
			}
			
		} catch (Exception e) {
			
		}
		
			return false;
		
		
	}

	public boolean onDown(MotionEvent arg0) {
		return false;
	}

	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 50;

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {

		System.out.println("y:"+(e1.getY() - e2.getY())+" and x:"+(e2.getX() - e1.getX()));
		if (Math.abs(e1.getY() - e2.getY()) <= SWIPE_MAX_OFF_PATH) 
		{ 
		if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) 
		{ 
			retBtn();
		} 
		if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) 
		{ 
			getUrlHtml(huifuUrl, Const.MSGPST);
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
		// ����Ϸ��͵���·�
		if (y > sv.getHeight() - ConstParam.sLength / 6 && x > (ConstParam.sWidth * 3 / 4)) {
			sv.scrollBy(0, sv.getHeight() - 20);
			return true;
		}
		if (y < ConstParam.sLength / 3 && x > (ConstParam.sWidth * 3 / 4)) {
			sv.scrollBy(0, 20 - sv.getHeight());
			return true;
		}
		return false;
	}

}
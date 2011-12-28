package com.ztm;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;

public class NetTraffic {
	
	public static Cookie[] cookies = null;
	
	
	
	public static void setMyCookie(String NUM,String id ,String KEY)
	{
		cookies = new Cookie[3];

		cookies[0] = new Cookie();
		cookies[0].setDomain("bbs.nju.edu.cn");
		cookies[0].setPath("/");
		cookies[0].setName("_U_NUM");
		cookies[0].setValue(NUM);

		cookies[1] = new Cookie();
		cookies[1].setDomain("bbs.nju.edu.cn");
		cookies[1].setPath("/");
		cookies[1].setName("_U_UID");
		cookies[1].setValue(id);

		cookies[2] = new Cookie();
		cookies[2].setDomain("bbs.nju.edu.cn");
		cookies[2].setPath("/");
		cookies[2].setName("_U_KEY");
		cookies[2].setValue(KEY);
	}
	
	public static HttpClient getClient() {
		// ����HttpClient��ʵ��
		HttpClient httpClient = new HttpClient();

		if (cookies != null) {
			for (Cookie cc : cookies) {
				httpClient.getState().addCookie(cc);
			}
		}

		httpClient.getParams().setCookiePolicy(
				CookiePolicy.BROWSER_COMPATIBILITY);
		httpClient.getParams().setParameter(
				"http.protocol.single-cookie-header", true);
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(
				5000);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(10000);

		return httpClient;
	}

	static HttpClient httpClient = null;

	/**
	 * ����urlȡ�����Ӧ��response
	 */
	public static String getHtmlContent(String url) {
		if (!url.startsWith("http"))
			url = "http:////" + url;
		String result = "";// ���صĽ��
		StringBuffer resultBuffer = new StringBuffer();

		httpClient = getClient();
		// ����GET������ʵ��
		GetMethod getMethod = new GetMethod(url);
		// getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
		// new DefaultHttpMethodRetryHandler());
		getMethod.getParams().setContentCharset("GB2312");
		try {
			// ִ��getMethod
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: "
						+ getMethod.getStatusLine());
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(
					getMethod.getResponseBodyAsStream(), getMethod
							.getResponseCharSet()));
			String inputLine = null;
			while ((inputLine = in.readLine()) != null) {
				resultBuffer.append(inputLine);
				resultBuffer.append("\n");
			}
			result = new String(resultBuffer);
			return result;
		} catch (HttpException e) {
			// �����������쳣��������Э�鲻�Ի��߷��ص�����������
			result = "error";
			e.printStackTrace();
		} catch (IOException e) {
			// ���������쳣
			result = "error";
			e.printStackTrace();
		} finally {
			// �ͷ�����
			getMethod.releaseConnection();
		}
		return result;
	}

	/**
	 * POST ���ݵ���������
	 */
	public static String postHtmlContent(String url, NameValuePair[] nvp) {
		if (!url.startsWith("http"))
			url = "http:////" + url;
		String result = "";// ���صĽ��
		StringBuffer resultBuffer = new StringBuffer();

		httpClient = getClient();
		// ����GET������ʵ��
		PostMethod post = new PostMethod(url);
		// getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
		// new DefaultHttpMethodRetryHandler());
		post.getParams().setContentCharset("GB2312");
		post.setRequestBody(nvp);
		try {
			// ִ��getMethod
			int statusCode = httpClient.executeMethod(post);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + post.getStatusLine());
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(post
					.getResponseBodyAsStream(), post.getResponseCharSet()));
			String inputLine = null;
			while ((inputLine = in.readLine()) != null) {
				resultBuffer.append(inputLine);
				resultBuffer.append("\n");
			}
			result = new String(resultBuffer);
			return result;
		} catch (HttpException e) {
			// �����������쳣��������Э�鲻�Ի��߷��ص�����������
			result = "error";
			e.printStackTrace();
		} catch (IOException e) {
			// ���������쳣
			result = "error";
			e.printStackTrace();
		} finally {
			// �ͷ�����
			post.releaseConnection();
		}
		return result;
	}
	
	
	/**
	 * POST ���ݵ���������
	 */
	public static String postFile(String url,File file,String board) {
		if (!url.startsWith("http"))
			url = "http:////" + url;
		String result = "";// ���صĽ��
		StringBuffer resultBuffer = new StringBuffer();

		httpClient = getClient();
		
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(20000);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(20000);
		
		// ����GET������ʵ��
		PostMethod post = new PostMethod(url);
		
		//PostMethod filePost = new PostMethod(targetURL);  


		
	
		try {
		
			
			FilePart part1 = new FilePart("up",file.getName(),file);
			StringPart sp = new StringPart("board",board);
			StringPart sp1 = new StringPart("ptext","");
			StringPart sp2 = new StringPart("exp","UploadByLilyDroid");
			
			
			Part[] parts = {part1,sp2,sp1,sp };
			//post.getParams().setContentCharset("GB2312");
			post.setRequestEntity(new MultipartRequestEntity(parts,	post.getParams()));
			
			post.addRequestHeader("Content-Type", "multipart/form-data");
			// ִ��getMethod
			
			
			int statusCode = httpClient.executeMethod(post);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + post.getStatusLine());
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(post
					.getResponseBodyAsStream(), post.getResponseCharSet()));
			String inputLine = null;
			while ((inputLine = in.readLine()) != null) {
				resultBuffer.append(inputLine);
				resultBuffer.append("\n");
			}
			result = new String(resultBuffer);
			return result;
		}  catch (Exception e) {
			// ���������쳣
			result = "error";
			e.printStackTrace();
		} finally {
			// �ͷ�����
			post.releaseConnection();
		}
		return result;
	}

}

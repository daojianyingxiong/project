package com.enlightent.util;

import java.net.URI;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class HttpUtil {
	
//	static HttpClient client = new DefaultHttpClient();
	
	public static String sendPost(String url, Map<String, String> params) {

		HttpClient client = new DefaultHttpClient();
		String result = "";
		try {
			HttpPost post = new HttpPost(url);
			
			List<NameValuePair> nvps = new ArrayList <NameValuePair>();  
			Set<Entry<String,String>> entrySet = params.entrySet();
			for (Entry<String, String> entry : entrySet) {
				String value = entry.getValue();
				if (StringUtils.isNotEmpty(value)) {
					nvps.add(new BasicNameValuePair(entry.getKey(), value));
				}
			}
	          
	        post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));  
			HttpResponse response = client.execute(post);
			result = EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return result;
	}
	
	public static String sendPost(String url, List<NameValuePair> nvps) {

		HttpClient client = new DefaultHttpClient();
		try {
			HttpPost post = new HttpPost(url);
			if (nvps != null) {
				post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			}
			HttpResponse response = client.execute(post);
			return EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static BasicHttpResponse sendPostTest(String url, Map<String, String[]> params) {
		
		HttpClient client = new DefaultHttpClient();
		try {
			HttpPost post = new HttpPost(url);
			
			List<NameValuePair> nvps = new ArrayList <NameValuePair>();  
			Set<Entry<String,String[]>> entrySet = params.entrySet();
			for (Entry<String, String[]> entry : entrySet) {
				String[] value = entry.getValue();
				if (value.length > 0) {
					nvps.add(new BasicNameValuePair(entry.getKey(), value[0]));
				}
			}
			
			post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));  
			HttpResponse response = client.execute(post);
			BasicHttpResponse b = (BasicHttpResponse) response;
			return b;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String sendPost(String url, Map<String, String> params, String charset) {
		HttpClient client = new DefaultHttpClient();
		String result = "";
		try {
			HttpPost post = new HttpPost(url);
			
			List<NameValuePair> nvps = new ArrayList <NameValuePair>();  
			Set<Entry<String,String>> entrySet = params.entrySet();
			for (Entry<String, String> entry : entrySet) {
				String value = entry.getValue();
				if (StringUtils.isNotEmpty(value)) {
					nvps.add(new BasicNameValuePair(entry.getKey(), value));
				}
			}
			
			post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));  
			HttpResponse response = client.execute(post);
			result = EntityUtils.toString(response.getEntity(), charset);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return result;
	}
	
	/**
	* 获取可信任https链接，以避免不受信任证书出现peer not authenticated异常
	*
	* @param base
	* @return
	*/
	public static HttpClient wrapClient(HttpClient base) {
		try {
			SSLContext ctx = SSLContext.getInstance("TLS");
			X509TrustManager tm = new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] xcs, String string) {
				}

				public void checkServerTrusted(X509Certificate[] xcs, String string) {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};
			ctx.init(null, new TrustManager[] { tm }, null);
			SSLSocketFactory ssf = new SSLSocketFactory(ctx);
			ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			ClientConnectionManager ccm = base.getConnectionManager();
			SchemeRegistry sr = ccm.getSchemeRegistry();
			sr.register(new Scheme("https", ssf, 443));
			return new DefaultHttpClient(ccm, base.getParams());
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	/**  
	 * 发送Get请求  
	 * @param url  
	 * @param params  
	 * @return  
	 */  
	public static String sendGet2(String url, Map<String, String> params) {  
		HttpClient client = new DefaultHttpClient();
		client = wrapClient(client);
		String body = null;  
		try {  
			// Get请求  
			HttpGet httpget = new HttpGet(url);
			List<NameValuePair> nvps = new ArrayList <NameValuePair>();  
			Set<Entry<String,String>> entrySet = params.entrySet();
			for (Entry<String, String> entry : entrySet) {
				String value = entry.getValue();
				if (StringUtils.isNotEmpty(value)) {
					nvps.add(new BasicNameValuePair(entry.getKey(), value));
				}
			}
			// 设置参数  
			String str = EntityUtils.toString(new UrlEncodedFormEntity(nvps));  
			httpget.setURI(new URI(httpget.getURI().toString() + "?" + str));  
			// 发送请求  
			HttpResponse httpresponse = client.execute(httpget); 
			
			StatusLine statusLine = httpresponse.getStatusLine();
			if (statusLine.getStatusCode() != 403) {
				System.out.println(statusLine.getStatusCode());
			}
			// 获取返回数据  
			HttpEntity entity = httpresponse.getEntity();  
			body = EntityUtils.toString(entity);  
		} catch (Exception e) {  
			e.printStackTrace();  
		} 
		return body;  
	}
	 /**  
     * 发送Get请求  
     * @param url  
     * @param params  
     * @return  
     */  
    public static String sendGet(String url, Map<String, String> params) {  
    	HttpClient client = new DefaultHttpClient();
        String body = null;  
        try {  
            // Get请求  
            HttpGet httpget = new HttpGet(url);
            List<NameValuePair> nvps = new ArrayList <NameValuePair>();  
			Set<Entry<String,String>> entrySet = params.entrySet();
			for (Entry<String, String> entry : entrySet) {
				String value = entry.getValue();
				if (StringUtils.isNotEmpty(value)) {
					nvps.add(new BasicNameValuePair(entry.getKey(), value));
				}
			}
            // 设置参数  
            String str = EntityUtils.toString(new UrlEncodedFormEntity(nvps));  
            httpget.setURI(new URI(httpget.getURI().toString() + "?" + str));  
            // 发送请求  
            HttpResponse httpresponse = client.execute(httpget); 
            
            // 获取返回数据  
            HttpEntity entity = httpresponse.getEntity();  
            body = EntityUtils.toString(entity);  
        } catch (Exception e) {  
            e.printStackTrace();  
        } 
        return body;  
    }
	
	public static String sendPostJson(String url, String parameters) {
		String body = null;
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost method = new HttpPost(url);
		try {
			method.addHeader("Accept", "application/json, text/plain, */*");
			method.addHeader("Content-type", "application/json; charset=utf-8");
			method.setEntity(new StringEntity(parameters, HTTP.UTF_8));
			HttpResponse response = httpClient.execute(method);
			body = EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return body;
	}
	
	public static void main(String[] args) {
		String url = "https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token=ExxzoakmW9ccSXGzDADNh-ws3ZHlX1w1kCjVNR0EkWxZvoxdnil2AR4-7GwrZox36ZpYTWMkGx1NnEONCbN5CrzXH9fgSIQGnx5eRACdXiUKYRdAHASGZ";
		String parameters = "{\"type\":\"image\",\"offset\":0,\"count\":20}";
		String sendPost = HttpUtil.sendPostJson(url, parameters);
		System.out.println(sendPost);
	}
}

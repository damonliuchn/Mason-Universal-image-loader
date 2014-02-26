package com.nostra13.universalimageloader.core.download;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class HttpUtil {

	private static final String CHARSET = "UTF-8";

	private static final String PREFIX = "--", LINEND = "\r\n";
	private static final String MULTIPART_FROM_DATA = "multipart/form-data";

	// cmwap 设置代理 不过现在有些地方 不用设置代理也可以，不好判断，所以默认不设置代理
	public static boolean IS_CMWAP = false;
	private static final String CMWAP_HOST = "10.0.0.172";
	private static final int CMWAP_PORT = 80;

	/**
	 * Get方式提交
	 * 
	 * @param url
	 *            提交地址
	 * @param paramsCharset
	 *            参数提交编码集 可为null、默认UTF-8
	 * 
	 * @return byte[]
	 */
	public static byte[] httpByGet2Bytes(String url, String paramsCharset) {
		if (url == null || "".equals(url)) {
			return null;
		}
		byte[] a = null;
		HttpClient httpClient = null;
		HttpGet hg = null;
		try {
			httpClient = getDefaultHttpClient();
			hg = new HttpGet(url);
			// 发送请求，得到响应
			HttpResponse response = httpClient.execute(hg);
			a = EntityUtils.toByteArray(response.getEntity());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			abortConnection(hg, httpClient);
		}
		return a;
	}

	/**
	 * Get方式提交
	 * 
	 * @param url
	 *            提交地址
	 * @param params
	 *            提交参数集, 键/值对
	 * @param paramsCharset
	 *            参数提交编码集 可为null、默认UTF-8
	 * @return byte[]
	 */
	public static byte[] httpByGet2Bytes(String url,
			Map<String, String> params, String paramsCharset) {
		if (url == null || "".equals(url)) {
			return null;
		}
		byte[] a = null;
		HttpClient httpClient = null;
		HttpGet hg = null;
		try {
			List<NameValuePair> qparams = getParamsList(params);
			if (qparams != null && qparams.size() > 0) {
				paramsCharset = (paramsCharset == null ? CHARSET
						: paramsCharset);
				String formatParams = URLEncodedUtils.format(qparams,
						paramsCharset);// URLEncodeUtils
				url = (url.indexOf("?")) < 0 ? (url + "?" + formatParams)
						: (url.substring(0, url.indexOf("?") + 1) + formatParams);
			}
			httpClient = getDefaultHttpClient();
			hg = new HttpGet(url);
			// 发送请求，得到响应
			HttpResponse response = httpClient.execute(hg);
			a = EntityUtils.toByteArray(response.getEntity());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			abortConnection(hg, httpClient);
		}
		return a;
	}

	/**
	 * Get方式提交
	 * 
	 * @param url
	 *            提交地址
	 * @param paramsCharset
	 *            参数提交编码集 可为null、默认UTF-8
	 * @param resultCharset
	 *            返回结果编码集可为null、默认UTF-8
	 * @return String
	 */
	public static String httpByGet2String(String url, String paramsCharset,
			String resultCharset) {
		if (url == null || "".equals(url)) {
			return null;
		}
		String responseStr = null;
		HttpClient httpClient = null;
		HttpGet hg = null;
		try {
			httpClient = getDefaultHttpClient();
			hg = new HttpGet(url);
			// 发送请求，得到响应
			HttpResponse response = httpClient.execute(hg);
			if (resultCharset == null || "".equals(resultCharset)) {
				responseStr = EntityUtils.toString(response.getEntity(),
						CHARSET);
			} else {
				responseStr = EntityUtils.toString(response.getEntity(),
						resultCharset);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			abortConnection(hg, httpClient);
		}
		return responseStr;
	}

	/**
	 * Get方式提交
	 * 
	 * @param url
	 *            提交地址
	 * @param params
	 *            提交参数集, 键/值对
	 * @param paramsCharset
	 *            参数提交编码集 可为null、默认UTF-8
	 * @param resultCharset
	 *            返回结果编码集可为null、默认UTF-8
	 * @return String
	 */
	public static String httpByGet2String(String url,
			Map<String, String> params, String paramsCharset,
			String resultCharset) {
		if (url == null || "".equals(url)) {
			return null;
		}
		String responseStr = null;
		HttpClient httpClient = null;
		HttpGet hg = null;
		try {
			List<NameValuePair> qparams = getParamsList(params);
			if (qparams != null && qparams.size() > 0) {
				paramsCharset = (paramsCharset == null ? CHARSET
						: paramsCharset);
				String formatParams = URLEncodedUtils.format(qparams,
						paramsCharset);// URLEncodeUtils
				url = (url.indexOf("?")) < 0 ? (url + "?" + formatParams)
						: (url.substring(0, url.indexOf("?") + 1) + formatParams);
			}
			httpClient = getDefaultHttpClient();
			hg = new HttpGet(url);
			// 发送请求，得到响应
			HttpResponse response = httpClient.execute(hg);
			if (resultCharset == null || "".equals(resultCharset)) {
				responseStr = EntityUtils.toString(response.getEntity(),
						CHARSET);
			} else {
				responseStr = EntityUtils.toString(response.getEntity(),
						resultCharset);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			abortConnection(hg, httpClient);
		}
		return responseStr;
	}

	/**
	 * Post方式提交 返回结果是byte[]
	 * 
	 * @param url
	 *            提交地址
	 * @param params
	 *            提交参数集, 键/值对
	 * @param paramsCharset
	 *            参数提交编码集 可为null、默认UTF-8
	 * @param resultCharset
	 *            返回结果编码集可为null、默认UTF-8
	 * @return String
	 * 
	 */
	public static byte[] httpByPost2Bytes(String url,
			Map<String, String> params, String paramsCharset) {
		if (url == null || "".equals(url)) {
			return null;
		}
		byte[] a = null;
		HttpClient httpClient = null;
		HttpPost hp = null;
		try {
			httpClient = getDefaultHttpClient();
			hp = new HttpPost(url);
			UrlEncodedFormEntity entity = null;
			if (paramsCharset == null || "".equals(paramsCharset)) {
				entity = new UrlEncodedFormEntity(getParamsList(params),
						CHARSET);
			} else {
				entity = new UrlEncodedFormEntity(getParamsList(params),
						paramsCharset);
			}
			hp.setEntity(entity);
			// 发送请求，得到响应
			HttpResponse response = httpClient.execute(hp);
			a = EntityUtils.toByteArray(response.getEntity());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			abortConnection(hp, httpClient);
		}
		return a;
	}

	/**
	 * Post方式提交 返回结果是String
	 * 
	 * @param url
	 *            提交地址
	 * @param params
	 *            提交参数集, 键/值对
	 * @param paramsCharset
	 *            参数提交编码集 可为null、默认UTF-8
	 * @param resultCharset
	 *            返回结果编码集可为null、默认UTF-8
	 * @return String
	 * 
	 */
	public static String httpByPost2String(String url,
			Map<String, String> params, String paramsCharset,
			String resultCharset) {
		if (url == null || "".equals(url)) {
			return null;
		}
		String responseStr = null;
		HttpClient httpClient = null;
		HttpPost hp = null;
		try {
			httpClient = getDefaultHttpClient();
			hp = new HttpPost(url);

			UrlEncodedFormEntity entity = null;
			if (paramsCharset == null || "".equals(paramsCharset)) {
				entity = new UrlEncodedFormEntity(getParamsList(params),
						CHARSET);
			} else {
				entity = new UrlEncodedFormEntity(getParamsList(params),
						paramsCharset);
			}
			hp.setEntity(entity);
			// 发送请求，得到响应
			HttpResponse response = httpClient.execute(hp);
			if (resultCharset == null || "".equals(resultCharset)) {
				responseStr = EntityUtils.toString(response.getEntity(),
						CHARSET);
			} else {
				responseStr = EntityUtils.toString(response.getEntity(),
						resultCharset);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			abortConnection(hp, httpClient);
		}
		return responseStr;
	}
	/**
	 * Post方式提交 返回结果是String
	 * 
	 * @param url
	 *            提交地址
	 * @param params
	 *            提交参数集, 键/值对
	 * @param paramsCharset
	 *            参数提交编码集 可为null、默认UTF-8
	 * @param resultCharset
	 *            返回结果编码集可为null、默认UTF-8
	 * @return String
	 * 
	 */
	public static String httpByPost2StringSSL(String url,
			Map<String, String> params, String paramsCharset,
			String resultCharset) {
		if (url == null || "".equals(url)) {
			return null;
		}
		String responseStr = null;
		HttpClient httpClient = null;
		HttpPost hp = null;
		try {
			httpClient = getSSLHttpClient();
			hp = new HttpPost(url);

			UrlEncodedFormEntity entity = null;
			if (paramsCharset == null || "".equals(paramsCharset)) {
				entity = new UrlEncodedFormEntity(getParamsList(params),
						CHARSET);
			} else {
				entity = new UrlEncodedFormEntity(getParamsList(params),
						paramsCharset);
			}
			hp.setEntity(entity);
			// 发送请求，得到响应
			HttpResponse response = httpClient.execute(hp);
			if (resultCharset == null || "".equals(resultCharset)) {
				responseStr = EntityUtils.toString(response.getEntity(),
						CHARSET);
			} else {
				responseStr = EntityUtils.toString(response.getEntity(),
						resultCharset);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			abortConnection(hp, httpClient);
		}
		return responseStr;
	}
	// ******************************************************************************************************//

	/**
	 * 将传入的键/值对参数转换为NameValuePair参数集
	 * 
	 * @param paramsMap
	 *            参数集, 键/值对
	 * @return NameValuePair参数集
	 */
	public static List<NameValuePair> getParamsList(
			Map<String, String> paramsMap) {
		if (paramsMap == null || paramsMap.size() == 0) {
			return null;
		}
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> map : paramsMap.entrySet()) {
			params.add(new BasicNameValuePair(map.getKey(), map.getValue()));
		}
		return params;
	}

	/**
	 * 将传入的键/值对参数转换为String
	 * 
	 * @param paramsMap
	 *            参数集, 键/值对
	 * @return NameValuePair参数集
	 */
	public static String getParamsString(Map<String, String> paramsMap) {
		if (paramsMap == null || paramsMap.size() == 0) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> map : paramsMap.entrySet()) {
			try {
				sb.append(URLEncoder.encode(map.getKey(), HTTP.UTF_8) + "="
						+ URLEncoder.encode(map.getValue(), HTTP.UTF_8) + "&");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String a = sb.toString();
		a = a.substring(0, a.length() - 1);
		return a;
	}

	/**
	 * 释放HttpClient连接
	 * 
	 * @param hrb
	 *            请求对象
	 * @param httpclient
	 *            client对象
	 */
	private static void abortConnection(final HttpRequestBase hrb,
			final HttpClient httpclient) {
		if (hrb != null) {
			try {
				hrb.abort();
			} catch (Exception e) {
			}
		}
		if (httpclient != null) {
			try {
				httpclient.getConnectionManager().shutdown();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 判断当前网络
	 * 
	 * @param
	 * @return 0 :无网络 1：wifi 2：cmwap 3:cmnet
	 * @author liumeng 2012-8-17下午01:06:10
	 */
	public static int checkNet(Context context) {
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				// 获取网络连接管理的对象
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				// 判断当前网络是否已经连接
				if (info != null && info.isConnected()) {
					// 判断当前网络是否是WIFI
					if (info.getTypeName().equalsIgnoreCase("WIFI")) {
						IS_CMWAP = false;
						return 1;
					} else if (info.getTypeName().equalsIgnoreCase("MOBILE")) {
						String apn = info.getExtraInfo();
						if (apn != null && apn.equals("cmwap")) {
							IS_CMWAP = true;
							return 2;
						} else {
							IS_CMWAP = false;
							return 3;
						}
					} else {// 未知网络类型统一识别为wifi
						IS_CMWAP = false;
						return 1;
					}
				} else {
					IS_CMWAP = false;
					return 0;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

			IS_CMWAP = false;
			return 0;
		}
		return 0;
	}

	/**
	 * 获取DefaultHttpClient实例
	 * 
	 * @return DefaultHttpClient 对象
	 */
	private static DefaultHttpClient getDefaultHttpClient() {
		HttpParams httpParams = new BasicHttpParams();

		// DefaultHttpClient httpclient = new DefaultHttpClient();

		// 设置连接超时和 Socket 超时，以及 Socket 缓存大小
		HttpConnectionParams.setConnectionTimeout(httpParams, 20 * 1000);
		HttpConnectionParams.setSoTimeout(httpParams, 20 * 1000);
		HttpConnectionParams.setSocketBufferSize(httpParams, 8192);

		// 设置重定向，缺省为 true
		HttpClientParams.setRedirecting(httpParams, true);

		// 设置 user agent
		String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6";
		HttpProtocolParams.setUserAgent(httpParams, userAgent);
		DefaultHttpClient httpclient = new DefaultHttpClient(httpParams);

		// 设置cmwap代理
		if (IS_CMWAP) {
			HttpHost proxy = new HttpHost(CMWAP_HOST, CMWAP_PORT);
			httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
					proxy);
		}

		return httpclient;
	}

	/************************************* 已下用于ssl get httpclient方式访问网络 *******************************************/
	/**
	 * Get方式提交
	 * 
	 * @param url
	 *            提交地址
	 * @param paramsCharset
	 *            参数提交编码集 可为null、默认UTF-8
	 * @param resultCharset
	 *            返回结果编码集可为null、默认UTF-8
	 * @return String
	 */
	public static String httpByGet2StringSSL(String url, String paramsCharset,
			String resultCharset) {
		if (url == null || "".equals(url)) {
			return null;
		}
		String responseStr = null;
		HttpClient httpClient = null;
		HttpGet hg = null;
		int statusCode=1;
		try {
			httpClient = getSSLHttpClient();
			hg = new HttpGet(url);
			// 发送请求，得到响应
			HttpResponse response = httpClient.execute(hg);
			if(response.getStatusLine()!=null){
				statusCode=response.getStatusLine().getStatusCode();
			}
			
			if (resultCharset == null || "".equals(resultCharset)) {
				responseStr = EntityUtils.toString(response.getEntity(),
						CHARSET);
			} else {
				responseStr = EntityUtils.toString(response.getEntity(),
						resultCharset);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			abortConnection(hg, httpClient);
		}
		//判断404
//		if(responseStr!=null&&responseStr.startsWith("<!DOCTYPE")&&responseStr.contains("404")){
//			U.check404("404");
//		}
		if(statusCode==404&&!url.contains("api.mingdao.com")){//这个地方得测试下
			//U.check404("404");
		}
		return responseStr;
	}
	/**
	 * urlByGet2InputStreamSSL
	 * **/
	private static javax.net.ssl.SSLSocketFactory socketFactory;
	private static void initSSLSocketFactory() {
		if (socketFactory != null) {
			if (HttpsURLConnection.getDefaultSSLSocketFactory().equals(
					socketFactory)) {
				return;
			} else {
				HttpsURLConnection.setDefaultSSLSocketFactory(socketFactory);
				return;
			}
		}
		try {
			SSLContext sslContext = SSLContext.getInstance("TLS");
			TrustManager trustManager = new X509TrustManager() {
				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				@Override
				public void checkServerTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
				}

				@Override
				public void checkClientTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
				}
			};
			sslContext.init(null, new TrustManager[] { trustManager }, null);
			socketFactory = sslContext.getSocketFactory();
			HttpsURLConnection.setDefaultSSLSocketFactory(socketFactory);
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	/**
	 * HttpsURLConnection Get方式提交
	 * 
	 * @param url
	 *            提交地址
	 */
	public static InputStream urlByGet2InputStreamSSL(String urlstr) {
		
		initSSLSocketFactory();
		URL mUrl;
		try {
			mUrl = new URL(urlstr);
			HttpsURLConnection urlConn = (HttpsURLConnection) mUrl.openConnection();
			urlConn.setConnectTimeout(10 * 1000);
			urlConn.setReadTimeout(10 * 1000);

			urlConn.connect();
			if (urlConn.getResponseCode() == 200) {
				return urlConn.getInputStream();
				//return inputstream;
			} else {
				return null;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * Get方式提交
	 * 
	 * 这个函数在2.3下  大数据时会报  socket close，所以建议使用 urlByGet2InputStreamSSL 这个方法
	 * 
	 * @param url
	 *            提交地址
	 */
	public static InputStream httpByGet2InputStreamSSL(String url) {
		if (url == null || "".equals(url)) {
			return null;
		}
		HttpClient httpClient = null;
		HttpGet hg = null;
		try {
			httpClient = getSSLHttpClient();
			hg = new HttpGet(url);
			// 发送请求，得到响应
			HttpResponse response = httpClient.execute(hg);
			return response.getEntity().getContent();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			abortConnection(hg, httpClient);
		}
		return null;
	}

	/**
	 * 
	 * javax.net.ssl.SSLPeerUnverifiedException: No peer certificate
	 * 
	 * @author Administrator
	 * 
	 */
	public static class SSLSocketFactoryEx extends SSLSocketFactory {

		SSLContext sslContext = SSLContext.getInstance("TLS");

		public SSLSocketFactoryEx(KeyStore truststore)
				throws NoSuchAlgorithmException, KeyManagementException,
				KeyStoreException, UnrecoverableKeyException {
			super(truststore);

			TrustManager tm = new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				@Override
				public void checkClientTrusted(
						java.security.cert.X509Certificate[] chain,
						String authType)
						throws java.security.cert.CertificateException {
				}

				@Override
				public void checkServerTrusted(
						java.security.cert.X509Certificate[] chain,
						String authType)
						throws java.security.cert.CertificateException {
				}
			};
			sslContext.init(null, new TrustManager[] { tm }, null);
		}

		@Override
		public Socket createSocket(Socket socket, String host, int port,
				boolean autoClose) throws IOException, UnknownHostException {
			return sslContext.getSocketFactory().createSocket(socket, host,
					port, autoClose);
		}

		@Override
		public Socket createSocket() throws IOException {
			return sslContext.getSocketFactory().createSocket();
		}
	}

	public static HttpClient getSSLHttpClient() {
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore
					.getDefaultType());
			trustStore.load(null, null);

			SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
			
			//设置超时
			int timeoutConnection = 20000;
			HttpConnectionParams.setConnectionTimeout(params, timeoutConnection);
			int timeoutSocket = 20000;
			HttpConnectionParams.setSoTimeout(params, timeoutSocket);
			
			// 设置 user agent
			String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6";
			HttpProtocolParams.setUserAgent(params, userAgent);
			
			
			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));

			ClientConnectionManager ccm = new ThreadSafeClientConnManager(
					params, registry);
			DefaultHttpClient httpClient = new DefaultHttpClient(ccm, params);
			// 设置cmwap代理
			if (IS_CMWAP) {
				HttpHost proxy = new HttpHost(CMWAP_HOST, CMWAP_PORT);
				httpClient.getParams().setParameter(
						ConnRoutePNames.DEFAULT_PROXY, proxy);
			}
			return new DefaultHttpClient(ccm, params);
		} catch (Exception e) {
			return new DefaultHttpClient();
		}
	}

	/******************** 发送文件 *************************************************************/
	/**
	 * . 通过拼接的方式构造请求内容，实现参数传输以及文件传输(post 方式)
	 * 
	 * @param actionUrl
	 * @param params
	 * @param files
	 * @return
	 * @throws SsnException
	 */
	public static String httpUrlByPostUpFileSSL(String actionUrl, Map<String, String> params,
			FormFile[] files) {

		String BOUNDARY = java.util.UUID.randomUUID().toString();

		URL uri = null;

		HttpsURLConnection conn = null;
		DataOutputStream outStream = null;
		String result = "";
		try {
			/*************ssl 设置开始  如果不用ssl的话 把下边代码删掉 然后把httpsurlconnection改为httpurlconnection*************/
			// 有时候网站采用的是 HTTPS
			// 协议，因此我们需要相应的证书，我们考虑是让程序接受所有证书，就像浏览器上设置接受所有证书，不管签名与否。实际是我们也无需证书，服务器也是我们自己的，我当然相信自己编写的客户端当然也相信自己的服务端了。
			// 要接受所有服务器上的证书，我们需要实现两个接口
			// javax.net.ssl.X509TrustManager 和 javax.net.ssl.HostnameVerifier
			// 接口中的函数都以空函数覆盖即可。其中 javax.net.ssl.HostnameVerifier 的 verify 返回
			// true 即可。下面是我们连接服务器的函数
			X509TrustManager xtm = new X509TrustManager() {

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				@Override
				public void checkClientTrusted(X509Certificate[] arg0,
						String arg1) throws CertificateException {
					// TODO Auto-generated method stub

				}

				@Override
				public void checkServerTrusted(X509Certificate[] arg0,
						String arg1) throws CertificateException {
					// TODO Auto-generated method stub

				}
			};
			HostnameVerifier hnv = new HostnameVerifier() {
				@Override
				public boolean verify(String arg0, SSLSession arg1) {
					// TODO Auto-generated method stub
					return true;
				}
			};
			SSLContext sslContext = null;
			try {
				sslContext = SSLContext.getInstance("TLS");
				X509TrustManager[] xtmArray = new X509TrustManager[] { xtm };
				sslContext.init(null, xtmArray,
						new java.security.SecureRandom());
			} catch (GeneralSecurityException gse) {
			}

			if (sslContext != null) {
				HttpsURLConnection.setDefaultSSLSocketFactory(sslContext
						.getSocketFactory());
			}
			HttpsURLConnection.setDefaultHostnameVerifier(hnv);
			/*************ssl 设置结束********************************************/
			uri = new URL(actionUrl);
			// 设置代理
			if (IS_CMWAP) {
				InetSocketAddress addr = new InetSocketAddress(CMWAP_HOST,
						CMWAP_PORT);
				Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);
				conn = (HttpsURLConnection) uri.openConnection(proxy);
			} else {
				conn = (HttpsURLConnection) uri.openConnection();
			}
			conn.setInstanceFollowRedirects(true);
			// HttpURLConnection.setFollowRedirects(true);
			conn.setReadTimeout(30 * 1000); // 缓存的最长时间
			conn.setDoInput(true);// 允许输入
			conn.setDoOutput(true);// 允许输出
			conn.setUseCaches(false); // 不允许使用缓存

			conn.setRequestMethod("POST");
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Charsert", CHARSET);
			conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
					+ ";boundary=" + BOUNDARY);
			conn.connect();
			// 首先组拼文本类型的参数
			StringBuilder sb = new StringBuilder();
			if (params != null)
				for (Map.Entry<String, String> entry : params.entrySet()) {
					sb.append(PREFIX);
					sb.append(BOUNDARY);
					sb.append(LINEND);
					sb.append("Content-Disposition: form-data; name=\""
							+ entry.getKey() + "\"" + LINEND);
					sb.append("Content-Type: text/plain; charset=" + CHARSET
							+ LINEND);
					sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
					sb.append(LINEND);
					sb.append(entry.getValue());
					sb.append(LINEND);
				}

			outStream = new DataOutputStream(conn.getOutputStream());
			outStream.write(sb.toString().getBytes());
			// 发送文件数据
			for (FormFile file : files) {
				StringBuilder split = new StringBuilder();
				split.append("--");
				split.append(BOUNDARY);
				split.append(LINEND);
				split.append("Content-Disposition: form-data;name=\""
						+ file.getFormname() + "\";filename=\""
						+ file.getFilename() + "\"" + LINEND);
				split.append("Content-Type: " + file.getContentType() + LINEND
						+ LINEND);
				outStream.write(split.toString().getBytes());
				outStream.write(file.getData(), 0, file.getData().length);
				outStream.write(LINEND.getBytes());
			}

			// 请求结束标志
			byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
			outStream.write(end_data);
			outStream.flush();
			// 得到响应码
			int res = conn.getResponseCode();
			if (res != HttpStatus.SC_OK
					&& res != HttpStatus.SC_MOVED_PERMANENTLY
					&& res != HttpStatus.SC_MOVED_TEMPORARILY)
				if (res == HttpStatus.SC_MOVED_PERMANENTLY
						|| res == HttpStatus.SC_MOVED_TEMPORARILY) {

					result = httpByGet2StringSSL(
							(conn.getURL().getProtocol() + "://"
									+ conn.getURL().getHost() + "/" + conn
									.getHeaderField("Location")),
							null, null);
					return result;
				}
			ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
			InputStream in = conn.getInputStream();
			byte[] buffer = new byte[1024 * 4];
			int read_len = 0;
			while ((read_len = in.read(buffer)) != -1) {
				out.write(buffer, 0, read_len);
			}
			result = new String(out.toByteArray(), CHARSET);
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				outStream.close();
			} catch (Exception e) {
			}
			try {
				conn.disconnect();
			} catch (Exception e) {
			}
		}
		return result;
	}
	/******************** 发送文件 *************************************************************/
	/**
	 * . 通过拼接的方式构造请求内容，实现参数传输以及文件传输(post 方式)
	 * 
	 * @param actionUrl
	 * @param params
	 * @param files
	 * @return
	 * @throws SsnException
	 */
	public static String httpUrlByPostUpFile(String actionUrl, Map<String, String> params,
			FormFile[] files) {

		String BOUNDARY = java.util.UUID.randomUUID().toString();

		URL uri = null;

		HttpURLConnection conn = null;
		DataOutputStream outStream = null;
		String result = "";
		try {
//			/*************ssl 设置开始  如果不用ssl的话 把下边代码删掉 然后把httpsurlconnection改为httpurlconnection*************/
//			// 有时候网站采用的是 HTTPS
//			// 协议，因此我们需要相应的证书，我们考虑是让程序接受所有证书，就像浏览器上设置接受所有证书，不管签名与否。实际是我们也无需证书，服务器也是我们自己的，我当然相信自己编写的客户端当然也相信自己的服务端了。
//			// 要接受所有服务器上的证书，我们需要实现两个接口
//			// javax.net.ssl.X509TrustManager 和 javax.net.ssl.HostnameVerifier
//			// 接口中的函数都以空函数覆盖即可。其中 javax.net.ssl.HostnameVerifier 的 verify 返回
//			// true 即可。下面是我们连接服务器的函数
//			X509TrustManager xtm = new X509TrustManager() {
//
//				public X509Certificate[] getAcceptedIssuers() {
//					return null;
//				}
//
//				@Override
//				public void checkClientTrusted(X509Certificate[] arg0,
//						String arg1) throws CertificateException {
//					// TODO Auto-generated method stub
//
//				}
//
//				@Override
//				public void checkServerTrusted(X509Certificate[] arg0,
//						String arg1) throws CertificateException {
//					// TODO Auto-generated method stub
//
//				}
//			};
//			HostnameVerifier hnv = new HostnameVerifier() {
//				@Override
//				public boolean verify(String arg0, SSLSession arg1) {
//					// TODO Auto-generated method stub
//					return true;
//				}
//			};
//			SSLContext sslContext = null;
//			try {
//				sslContext = SSLContext.getInstance("TLS");
//				X509TrustManager[] xtmArray = new X509TrustManager[] { xtm };
//				sslContext.init(null, xtmArray,
//						new java.security.SecureRandom());
//			} catch (GeneralSecurityException gse) {
//			}
//
//			if (sslContext != null) {
//				HttpsURLConnection.setDefaultSSLSocketFactory(sslContext
//						.getSocketFactory());
//			}
//			HttpsURLConnection.setDefaultHostnameVerifier(hnv);
//			/*************ssl 设置结束********************************************/
			uri = new URL(actionUrl);
			// 设置代理
			if (IS_CMWAP) {
				InetSocketAddress addr = new InetSocketAddress(CMWAP_HOST,
						CMWAP_PORT);
				Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);
				conn = (HttpURLConnection) uri.openConnection(proxy);
			} else {
				conn = (HttpURLConnection) uri.openConnection();
			}
			conn.setInstanceFollowRedirects(true);
			// HttpURLConnection.setFollowRedirects(true);
			conn.setReadTimeout(30 * 1000); // 缓存的最长时间
			conn.setDoInput(true);// 允许输入
			conn.setDoOutput(true);// 允许输出
			conn.setUseCaches(false); // 不允许使用缓存

			conn.setRequestMethod("POST");
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Charsert", CHARSET);
			conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
					+ ";boundary=" + BOUNDARY);
			conn.connect();
			// 首先组拼文本类型的参数
			StringBuilder sb = new StringBuilder();
			if (params != null)
				for (Map.Entry<String, String> entry : params.entrySet()) {
					sb.append(PREFIX);
					sb.append(BOUNDARY);
					sb.append(LINEND);
					sb.append("Content-Disposition: form-data; name=\""
							+ entry.getKey() + "\"" + LINEND);
					sb.append("Content-Type: text/plain; charset=" + CHARSET
							+ LINEND);
					sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
					sb.append(LINEND);
					sb.append(entry.getValue());
					sb.append(LINEND);
				}

			outStream = new DataOutputStream(conn.getOutputStream());
			outStream.write(sb.toString().getBytes());
			// 发送文件数据
			for (FormFile file : files) {
				StringBuilder split = new StringBuilder();
				split.append("--");
				split.append(BOUNDARY);
				split.append(LINEND);
				split.append("Content-Disposition: form-data;name=\""
						+ file.getFormname() + "\";filename=\""
						+ file.getFilename() + "\"" + LINEND);
				split.append("Content-Type: " + file.getContentType() + LINEND
						+ LINEND);
				outStream.write(split.toString().getBytes());
				outStream.write(file.getData(), 0, file.getData().length);
				outStream.write(LINEND.getBytes());
			}

			// 请求结束标志
			byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
			outStream.write(end_data);
			outStream.flush();
			// 得到响应码
			int res = conn.getResponseCode();
			if (res != HttpStatus.SC_OK
					&& res != HttpStatus.SC_MOVED_PERMANENTLY
					&& res != HttpStatus.SC_MOVED_TEMPORARILY)
				if (res == HttpStatus.SC_MOVED_PERMANENTLY
						|| res == HttpStatus.SC_MOVED_TEMPORARILY) {

					result = httpByGet2StringSSL(
							(conn.getURL().getProtocol() + "://"
									+ conn.getURL().getHost() + "/" + conn
									.getHeaderField("Location")),
							null, null);
					return result;
				}
			ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
			InputStream in = conn.getInputStream();
			byte[] buffer = new byte[1024 * 4];
			int read_len = 0;
			while ((read_len = in.read(buffer)) != -1) {
				out.write(buffer, 0, read_len);
			}
			result = new String(out.toByteArray(), CHARSET);
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				outStream.close();
			} catch (Exception e) {
			}
			try {
				conn.disconnect();
			} catch (Exception e) {
			}
		}
		return result;
	}
	/**
	 * HttpsURLConnection Get方式提交
	 * 
	 * @param url
	 *            提交地址
	 */
	public static InputStream urlByGet2InputStream(String urlstr) {
		URL mUrl;
		try {
			mUrl = new URL(urlstr);
			HttpURLConnection urlConn = (HttpURLConnection) mUrl.openConnection();			
			urlConn.setConnectTimeout(10 * 1000);
			urlConn.setReadTimeout(10 * 1000);

			return urlConn.getInputStream();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
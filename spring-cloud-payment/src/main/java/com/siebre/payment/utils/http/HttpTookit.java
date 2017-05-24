package com.siebre.payment.utils.http;

import static org.apache.http.HttpStatus.SC_OK;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Title: Class HttpTookit 
 * Description: http tookit
 *
 *  
 * @email   
*  @version 1.0 2017年3月14日 
 */
public class HttpTookit {

	private static final Logger logger = LoggerFactory
			.getLogger(HttpTookit.class);

	
	public static String doPost(String url, String data){
		return doPost(url, data, null);
	}
	
	public static String doPost(String url, String data,HttpTookitConfig config) {
		String content = null;
		HttpClient httpClient = null;
		HttpResponse response = null;
		try {
			config = ConnectionManger.newInstance().getConfig(config);
			httpClient = ConnectionManger.newInstance().newHttpClient();
			HttpUriRequest request = buildPostEntityMethod(url, data, config);
			response = httpClient.execute(request);
 			content = getResponseContent(content, response,config);
			logger.debug("content={}",content);
		} catch (Exception ingore) {
			logger.error("发送请求失败，url={},data={}", url, data, ingore);
		} finally {
			ConnectionManger.newInstance().release(response);
		}
		return content;
	}

	public static InputStream doPostInputStream(String url, String data) {
		return doPostInputStream(url, data,null);
	}
	
	
	public static InputStream doPostInputStream(String url, String data,HttpTookitConfig config) {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		try {
			config = ConnectionManger.newInstance().getConfig(config);
			httpClient = ConnectionManger.newInstance().newHttpClient();
			HttpUriRequest request = buildPostEntityMethod(url, data, config);
			response = httpClient.execute(request);
			if (response.getStatusLine().getStatusCode() == SC_OK) {
				return response.getEntity().getContent();
			}
			logger.info("发送请求响应失败!{}",response.getStatusLine());
			return null;
		}catch (Exception e){
			logger.error("发送请求失败，url={},data={}", url, data, e); 
		}finally {
			ConnectionManger.newInstance().release(response,false);
		}
		return null;
	}

	public static String doPostFile(String url,byte[] fileContent,String fileName, Map<String, String> params){
		return doPostFile(url, fileContent, fileName, params,null);
	}
	
	public static String doPostFile(String url,byte[] fileContent,String fileName, Map<String, String> params,HttpTookitConfig config) {
		String content = null;
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		try {
			config = ConnectionManger.newInstance().getConfig(config);
			httpClient = ConnectionManger.newInstance().newHttpClient();
			HttpUriRequest request = buildPostFileMethod(url, fileContent, fileName, params, config);
			response = httpClient.execute(request);
 			content = getResponseContent(content, response,config);
			logger.debug("content={}",content);
		} catch (Exception ingore) {
			logger.error("发送请求失败，url={},params={}", url, params, ingore);
		} finally {
			ConnectionManger.newInstance().release(response);
		}

		return content;
	}
	
	public static String doPost(String url, Map<String, String> params) {
		return doPost(url, params,null);
	}
	
	
	public static String doPost(String url, Map<String, String> params,HttpTookitConfig config) {
		String content = null;
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		try {
			config = ConnectionManger.newInstance().getConfig(config);
			httpClient = ConnectionManger.newInstance().newHttpClient();
			HttpUriRequest request = buildPostMethod(url, params, config);
			response = httpClient.execute(request);
 			content = getResponseContent(content, response,config);
			logger.debug("content={}",content);
		} catch (Exception ingore) {
			logger.error("发送请求失败，url={},params={}", url, params, ingore);
		} finally {
			ConnectionManger.newInstance().release(response);
		}

		return content;
	}

	public static String doGet(String url, Map<String, String> params) {
		return doGet(url, params, null);
	}
	
	public static String doGet(String url, Map<String, String> params,HttpTookitConfig config) {
		String content = null;
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		try {
			config = ConnectionManger.newInstance().getConfig(config);
			httpClient = ConnectionManger.newInstance().newHttpClient();
			HttpUriRequest request = buildGetMethod(url, params, config);
			response = httpClient.execute(request);
			content = getResponseContent(content, response,config);
			logger.debug("content={}",content);
		} catch (Exception ingore) {
			logger.error("发送请求失败，url={},params={}", url, params, ingore);
		} finally {
			ConnectionManger.newInstance().release(response);
		}
		return content;
	}

	private static String getResponseContent(String content,
			HttpResponse response,HttpTookitConfig config) throws IOException {
		if (response.getStatusLine().getStatusCode()==SC_OK) {
			HttpEntity entity = response.getEntity();
			content = entity == null ? null : EntityUtils.toString(entity,config.getCharset());
			logger.debug("response content :{}", content);
		}
		return content;
	}

	 private static HttpUriRequest buildGetMethod(String url,Map<String, String> params,
			 HttpTookitConfig config) {
		 return buildRequestMethod(url, params, null, null, null, RequestMethod.GET,config);
	 }

	 private static HttpUriRequest buildPostMethod(String url,Map<String, String> params,
			 HttpTookitConfig config) {
		 return buildRequestMethod(url, params, null, null, null, RequestMethod.POST,config);
	 }
	
	 private static HttpUriRequest buildPostEntityMethod(String url,String data,
			 HttpTookitConfig config) {
		 return buildRequestMethod(url, null, data, null, null, RequestMethod.POST_ENTITY,config);
	 }
	 
	 
	 private static HttpUriRequest buildPostFileMethod(String url,byte[] fileContent,String fileName, 
			 Map<String, String> params,HttpTookitConfig config) {
		 return buildRequestMethod(url, params, null, fileContent, fileName, RequestMethod.FILE,config);
	 }
	
	 private static HttpUriRequest buildRequestMethod(String url,Map<String, String> params,
			 String data,byte[] fileContent,String fileName,RequestMethod method,HttpTookitConfig config) {
		HttpUriRequest request = null;
		// config request 
		config = ConnectionManger.newInstance().getConfig(config);
		RequestConfig requestConf = RequestConfig
					.custom()
					.setSocketTimeout(config.getDefaultSoTimeout())
					.setConnectTimeout(config.getDefaultConnectionTimeout())
					.setConnectionRequestTimeout(config.getConnectionRequestTimeout())
					.build();
		 
		 if(method == RequestMethod.FILE){
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			builder.addBinaryBody("file", fileContent, ContentType.DEFAULT_BINARY, fileName);
			if(params!=null){
				for(String key:params.keySet())
					builder.addTextBody(key, params.get(key));
			}
			HttpEntity entity = builder.build();
			request = RequestBuilder
						.post()
						.setUri(url)
						.setConfig(requestConf)
						.setEntity(entity)
						.build();
		 }else if(method == RequestMethod.POST_ENTITY){
			 RequestBuilder builder= RequestBuilder
						.post()
						.setUri(url)
						.setConfig(requestConf);
						
			 if(StringUtils.isNoneBlank(data))
					builder.setEntity(new StringEntity(data,config.getCharset()));
			 request = 	builder.build();
		 }else {
			 List<NameValuePair> paramList = null;
			 if(params != null){
				 paramList = new ArrayList<NameValuePair>(params.size());
				 for(String key :params.keySet()){
					 paramList.add(new BasicNameValuePair(key, params.get(key)));
				 }
			 }
			 if(method == RequestMethod.GET){
				 RequestBuilder builder= RequestBuilder
							.get()
							.setUri(url)
							.setConfig(requestConf);
				 if(paramList != null){
					 builder.addParameters(paramList.toArray(new BasicNameValuePair[paramList.size()]));
				 }
				 request = builder.build();
			 }else if(method == RequestMethod.POST){
				 RequestBuilder builder= RequestBuilder
							.post()
							.setUri(url)
							.setConfig(requestConf);
				 if(paramList != null){
					 try {
						HttpEntity paramEntity  = new UrlEncodedFormEntity(paramList, config.getCharset());
						builder.setEntity(paramEntity);
					} catch (UnsupportedEncodingException ingore) {
						
					};
					
				 }
				 request = builder.build();
			 }
			
		 }
		 
		 return request;
	 }
	
	 enum RequestMethod{
		POST_ENTITY,POST,GET,FILE;
	}
	
	
	final static class ConnectionManger {

		/**
		 *  http pool manager
		 */
		private  PoolingHttpClientConnectionManager connManager;

		/**
		 * 默认的http 配置参数
		 */
		private final HttpTookitConfig defaultConfig = new HttpTookitConfig();

		private final static ConnectionManger manger = new ConnectionManger();

		public static ConnectionManger newInstance() {
			return manger;
		}

		private ConnectionManger() {
			try {
				//信任所有的https证书
				
				
				
				SSLContext sslcontext = SSLContexts.custom()
						.loadTrustMaterial(null, new TrustStrategy() {
							@Override
							public boolean isTrusted(X509Certificate[] chain,
									String authType)
							
									throws CertificateException {
								return true;
							}
						}).build();
				SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
						sslcontext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
				Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
						.<ConnectionSocketFactory> create()
						.register("http",
								PlainConnectionSocketFactory.getSocketFactory())
						.register("https", sslsf)
						.build();
				connManager = new PoolingHttpClientConnectionManager(
						socketFactoryRegistry);
				// Increase max total connection to 200
				connManager.setMaxTotal(defaultConfig.getDefaultMaxTotalConn());
				// Increase default max connection per route to 20
				connManager.setDefaultMaxPerRoute(defaultConfig
						.getDefaultMaxConnPerHost());
			} catch (Exception ingore) {
				logger.error("获取连接异常，cause：{}",ingore);
			}

		}

		/**
		 * 
		 * 获取http tookit config
		 * Created on 2016年11月2日
		 *  
*  @version 1.0 2017年3月14日 
		 * @param config
		 * 	config 配置参数
		 * @return
		 */
		public HttpTookitConfig getConfig(HttpTookitConfig config){
			return config==null?
					defaultConfig:config;
		}
		
		/**
		 * 
		 * 构建httpclient。<br>
		 * <p>
		 * 	从连接池中获取httpclient连接，默认https走连接池，http走实例对象的模式。
		 * 	每个host默认200http连接，最大连接数1000。
		 * </p>
		 * Created on 2016年11月2日
		 *  
*  @version 1.0 2017年3月14日 
		 * @param config
		 * 	http client 配置参数
		 * @return
		 */
		public CloseableHttpClient newHttpClient() {
			return HttpClients
					.custom()
					.setConnectionManager(connManager)
					.build();
		}
		
		
		/**
		 * 
		 * 释放连接资源<br>
		 * <p>
		 * 	conn manger 统一管理连接的释放。
		 * </p>
		 * Created on 2016年11月2日
		 *  
*  @version 1.0 2017年3月14日 
		 * @param response
		 * 	响应内容
		 */
		public void release(HttpResponse response){
			release(response,true);
		}

		public void release(HttpResponse response,boolean closeStream){
			try {
				if (response != null && closeStream){
					EntityUtils.consume(response.getEntity());
				}
				//每次释放资源时监测需要关闭的空闲和过期连接
				connManager.closeIdleConnections(
						defaultConfig.getDefaultIdleConnTimeout(), TimeUnit.MILLISECONDS);
				connManager.closeExpiredConnections();
			} catch (Exception ingore) {
				logger.error("关闭连接失败", ingore);
			}
		}
		
		

	}

	public static void main1(String[] args){

	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		
		//DOMConfigurator.configure("E:/build/log4j.xml");
		ExecutorService service = Executors.newFixedThreadPool(250);
		final String url = 	"https://www.baidu.com";
	
		
		final HttpTookitConfig config = new HttpTookitConfig();
		config.setDefaultConnectionTimeout(2*1000);
		config.setDefaultSoTimeout(1*1000);
		config.setConnectionRequestTimeout(50);
		final Map<String, String> params = new HashMap<String, String>();
		params.put("ie","utf-8");
		params.put("f","8");
		params.put("rsv_bp","1");
		params.put("rsv_idx","1");
		params.put("tn","baidu");
		params.put("wd","爱奇艺");
		params.put("oq","爱奇艺");
		String content = HttpTookit.doPost(url, params,config);
		System.out.println(content);
		logger.info(content);
		for(int i=0;i<Integer.MAX_VALUE;i++){
			try {
				Thread.sleep(200L);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("thread id:"+Thread.currentThread().getId()+"\tname: "+Thread.currentThread().getName());
		Future ff=service.submit(new Runnable() {
			@Override
			public void run() {
				//logger.info("thread id:{}",Thread.currentThread().getId());
				long time = System.currentTimeMillis();
				System.out.println("thread id:"+Thread.currentThread().getId()+"\tname: "+Thread.currentThread().getName());
				String content = HttpTookit.doPost(url, params, config);
				time = System.currentTimeMillis() - time;
				System.out.println("content:\t"+content);
				System.out.println("thread id:"+Thread.currentThread().getId()+"\t cost time(s):"+time);
				logger.info("response content:{}",content);
				
			}
		});
		}		
		}


}

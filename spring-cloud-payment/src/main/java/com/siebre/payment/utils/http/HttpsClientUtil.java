package com.siebre.payment.utils.http;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.security.KeyStore;

public class HttpsClientUtil {
	public static CloseableHttpClient getHttpsClient(byte[] cerFile, String keys) {
		CloseableHttpClient httpClient = null;
		try {
			KeyStore keyStore = KeyStore.getInstance("PKCS12");
			ByteArrayInputStream bais = new ByteArrayInputStream(cerFile);
			keyStore.load(bais, keys.toCharArray());
			SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, keys.toCharArray()).build();
			@SuppressWarnings("unused")
			SSLContext ctx = SSLContext.getInstance("SSL");
			// Allow TLSv1 protocol only
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" },
					null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);

			httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return httpClient;
	}

	public static CloseableHttpClient getHttpsClient(String cerFile, String keys) {
		CloseableHttpClient httpClient = null;
		try {
			FileInputStream fio = new FileInputStream(new File(cerFile));
            httpClient = getHttpsClient(fio,keys);
		} catch (Exception ex) {
		}
        return httpClient;
	}

    public static CloseableHttpClient getHttpsClient(InputStream fio, String keys) {
		CloseableHttpClient httpClient = null;
		try {
			KeyStore keyStore = KeyStore.getInstance("PKCS12");
			keyStore.load(fio, keys.toCharArray());
			SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, keys.toCharArray()).build();
			// Allow TLSv1 protocol only
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" },
					null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);

			httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
		} catch (Exception ex) {
		}finally {
			if (fio != null){
				try {
					fio.close();
				} catch (IOException e) {
				}
			}
		}
		return httpClient;
	}
}

package com.siebre.payment.paymenthandler.allinpay.sdk;

import com.google.common.base.Stopwatch;
import com.siebre.basic.applicationcontext.SpringContextUtil;
import com.siebre.payment.paymentway.entity.PaymentWay;
import com.siebre.payment.paymentway.service.PaymentWayService;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateCrtKey;
import java.util.Enumeration;
import java.util.concurrent.TimeUnit;

public class XmlUtils {

    private static Logger log = LoggerFactory.getLogger(XmlUtils.class);

    private static final SSLHandler simpleVerifier = new SSLHandler();
    private static SSLSocketFactory sslFactory;

    private static final BouncyCastleProvider provider = new BouncyCastleProvider();
    private static byte[] keyFilebytes;
    private static char[] keyPassword;
    private static KeyStore keyStore;
    private static String sslProtocol;
    private static String sign_position = "<SIGNED_MSG></SIGNED_MSG>";
    private static boolean isInitialized = false;

    public static void init() {
        PaymentWayService paymentWayService = (PaymentWayService) SpringContextUtil.getBean("paymentWayService");
        PaymentWay paymentWay = paymentWayService.getPaymentWay(AllinpayConfig.WAY_ALLIN_REALTIME_PAY);
        keyFilebytes = paymentWay.getApiClientCertPkcs();
        keyPassword = "111111".toCharArray();
        sslProtocol = "TLS";
        initKeyStore();
        initSSLSocketFactory();
        isInitialized = true;
    }

    public static String sign(String message) throws Exception {
        if (!isInitialized)
            init();
        Stopwatch stopwatch = Stopwatch.createStarted();
        String sign = generateSign(message.replaceAll(sign_position, ""));
        log.debug("sign:" + stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return message.replaceAll(sign_position, "<SIGNED_MSG>" + sign + "</SIGNED_MSG>");
    }

    public static String generateSign(String message) throws Exception {
        if (!isInitialized)
            init();
        String result = "";

        Enumeration<String> myEnum = keyStore.aliases();
        String keyAlias = null;
        RSAPrivateCrtKey prikey = null;
        // IBM JDK必须使用While循环取最后一个别名，才能得到个人私钥别名
        while (myEnum.hasMoreElements()) {
            keyAlias = (String) myEnum.nextElement();
            if (keyStore.isKeyEntry(keyAlias)) {
                prikey = (RSAPrivateCrtKey) keyStore.getKey(keyAlias, keyPassword);
                break;
            }
        }
        if (prikey != null) {
            Signature sign = Signature.getInstance("SHA1withRSA", provider);
            sign.initSign(prikey);
            sign.update(message.getBytes("GBK"));
            byte signed[] = sign.sign();
            byte signAsc[] = new byte[signed.length * 2];
            hex2Ascii(signed.length, signed, signAsc);
            result = new String(signAsc);
        } else {
            throw new Exception("没有找到匹配私钥");
        }
        return result;
    }

    private static void hex2Ascii(int len, byte[] dataIn, byte[] dataOut) {
        byte temp1[] = new byte[1];
        byte temp2[] = new byte[1];
        for (int i = 0, j = 0; i < len; i++) {
            temp1[0] = dataIn[i];
            temp1[0] = (byte) (temp1[0] >>> 4);
            temp1[0] = (byte) (temp1[0] & 0x0f);
            temp2[0] = dataIn[i];
            temp2[0] = (byte) (temp2[0] & 0x0f);
            if (temp1[0] >= 0x00 && temp1[0] <= 0x09) {
                (dataOut[j]) = (byte) (temp1[0] + '0');
            } else if (temp1[0] >= 0x0a && temp1[0] <= 0x0f) {
                (dataOut[j]) = (byte) (temp1[0] + 0x57);
            }

            if (temp2[0] >= 0x00 && temp2[0] <= 0x09) {
                (dataOut[j + 1]) = (byte) (temp2[0] + '0');
            } else if (temp2[0] >= 0x0a && temp2[0] <= 0x0f) {
                (dataOut[j + 1]) = (byte) (temp2[0] + 0x57);
            }
            j += 2;
        }
    }

    public static String post(String url, String message) throws Exception {
        if (!isInitialized)
            init();
        Stopwatch stopwatch = Stopwatch.createStarted();
        OutputStream os = null;
        InputStream is = null;
        URLConnection connection = null;
        String responseMessage = null;

        String connectTimeout = AllinpayConfig.CONNECT_TIMEOUT;
        String readTimeout = AllinpayConfig.READ_TIMEOUT;

        byte[] bytes;
        try {
            bytes = message.getBytes("GBK");
            log.debug("createHttpConnection");
            connection = createHttpConnection(url, "POST");

            connection.setRequestProperty("Content-length", String.valueOf(bytes.length));
            connection.setRequestProperty("Keep-alive", "false");

            if (StringUtils.isNotBlank(connectTimeout)) {
                connection.setConnectTimeout(Integer.parseInt(connectTimeout));
            }

            if (StringUtils.isNotBlank(readTimeout)) {
                connection.setReadTimeout(Integer.parseInt(readTimeout));
            }


            log.debug("getOutputStream");
            os = connection.getOutputStream();
            os.write(bytes);
            os.close();

            log.debug("getInputStream");
            is = connection.getInputStream();

            ByteArrayOutputStream baos = new ByteArrayOutputStream(is.available());
            byte[] buf = new byte[4096];
            int read;
            while ((read = is.read(buf)) != -1) {
                baos.write(buf, 0, read);
            }
            log.debug("baos.toByteArray");
            responseMessage = new String(baos.toByteArray(), "GBK");
        } catch (Exception e) {
            throw e;
        } finally {
            log.debug("closeStream");
            close(os, is);
        }
        log.debug("send:" + stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return responseMessage;
    }

    public static void downloadFile(String url, String localDir) throws Exception {
        if (!isInitialized)
            init();

        Stopwatch stopwatch = Stopwatch.createStarted();
        InputStream is = null;
        URLConnection connection = null;
        try {
            log.debug("createHttpConnection");
            connection = createHttpConnection(url, "GET");

            connection.setRequestProperty("Content-type", "application/tlt-notify");
            connection.setRequestProperty("Keep-alive", "false");

            connection.connect();

            log.debug("getInputStream");
            is = connection.getInputStream();

            FileOutputStream fos = new FileOutputStream(localDir);
            byte[] buf = new byte[4096];
            int read;
            while ((read = is.read(buf)) != -1) {
                fos.write(buf, 0, read);
            }
            log.debug("close(fos)");
            close(fos);
        } catch (Exception e) {
            throw e;
        } finally {
            log.debug("closeStream");
            close(is);
        }
        log.debug("send:" + stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

    private static void close(Closeable... streams) {
        for (Closeable stream : streams) {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private static URLConnection createHttpConnection(String strUrl, String method) throws IOException {
        URL url = new URL(strUrl);
        URLConnection conn = url.openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        if (conn instanceof HttpsURLConnection) {
            HttpsURLConnection httpsConn = (HttpsURLConnection) conn;
            httpsConn.setRequestMethod(method);
            httpsConn.setReadTimeout(60000);
            httpsConn.setSSLSocketFactory(sslFactory);
            httpsConn.setHostnameVerifier(simpleVerifier);
        } else if (conn instanceof HttpURLConnection) {
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setRequestMethod(method);
            httpConn.setReadTimeout(60000);
        }
        return conn;
    }

    private static void initKeyStore() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        InputStream keyFile = new ByteArrayInputStream(keyFilebytes);
        try {
            keyStore = KeyStore.getInstance("PKCS12", provider);
            keyStore.load(keyFile, keyPassword);
        } catch (Exception ex) {
            try {
                keyFile.close();
            } catch (IOException e) {
            }
        }
        log.debug("initKeyStore:" + stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

    private static void initSSLSocketFactory() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance(sslProtocol);
            sslContext.init(null, new TrustManager[]{simpleVerifier}, null);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        sslFactory = sslContext.getSocketFactory();
        log.debug("initSSLSocketFactory:" + stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

    private static class SSLHandler implements X509TrustManager, HostnameVerifier {
        private SSLHandler() {
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        /**
         * 可接受的 CA 发行者证书的非 null（可能为空）的数组
         */
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        /**
         * @param hostname 主机名
         * @param session  到主机的连接上使用的 SSLSession
         * @return 如果主机名是可接受的，则返回 true
         */
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

}
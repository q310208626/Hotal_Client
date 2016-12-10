package com.lsj.httpmethod;

import com.sun.org.apache.xpath.internal.operations.String;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by hdmi on 16-12-2.
 */
public class LoginHttp {
    public Boolean doPost(java.lang.String url, Map<java.lang.String,java.lang.String> map,java.lang.String charset) {
        Boolean result = false;
        HttpClient httpClient = null;
        HttpPost httpPost = null;
        try {

            httpClient=getSecuredHttpClient(new DefaultHttpClient());

            httpPost = new HttpPost(url);
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            Iterator iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<java.lang.String, java.lang.String> elem = (Map.Entry<java.lang.String, java.lang.String>) iterator.next();
                list.add(new BasicNameValuePair(elem.getKey(),elem.getValue()));
            }
            if (list.size() > 0) {
                System.out.print(list.size());
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, charset);
                httpPost.setEntity(entity);
            }

            HttpResponse httpResponse=httpClient.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode()== 200){
                HttpEntity httpEntity=httpResponse.getEntity();
                result=Boolean.valueOf(EntityUtils.toString(httpEntity));
                System.out.print(result);
                return result;
            }else {
                System.out.print("链接失败:"+httpResponse.getStatusLine()+"\n");
                return result;
            }



        }
        catch (Exception e){
            e.printStackTrace();
        }
        return result;

    }

    private static DefaultHttpClient getSecuredHttpClient(HttpClient httpClient) {
        SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance("SSL");
            // set up a TrustManager that trusts everything
            try {
                sslContext.init(null,
                        new TrustManager[] { new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(X509Certificate[] x509Certificates, java.lang.String s) throws CertificateException {

                            }

                            @Override
                            public void checkServerTrusted(X509Certificate[] x509Certificates, java.lang.String s) throws CertificateException {

                            }

                            public X509Certificate[] getAcceptedIssuers() {
                                return null;
                            }

                            public void checkClientTrusted(
                                    X509Certificate[] certs, String authType) {
                            }

                            public void checkServerTrusted(
                                    X509Certificate[] certs, String authType) {
                            }
                        } }, new SecureRandom());
            } catch (KeyManagementException e) {
            }
            SSLSocketFactory ssf = new SSLSocketFactory(sslContext)
            {
                // non-javadoc, see interface LayeredSocketFactory
                public Socket createSocket(
                        final Socket socket,
                        final java.lang.String host,
                        final int port,
                        final boolean autoClose
                ) throws IOException, UnknownHostException {
                    SSLSocket sslSocket = (SSLSocket) getSocketFactory().createSocket(
                            socket,
                            host,
                            port,
                            autoClose
                    );
                    sslSocket.setEnabledProtocols(new java.lang.String[]{"SSLv3"});
                    return sslSocket;
                }
            };
            ClientConnectionManager ccm = httpClient.getConnectionManager();
            SchemeRegistry sr = ccm.getSchemeRegistry();
            sr.register(new Scheme("https", 443, ssf));
            ThreadSafeClientConnManager mgr = new ThreadSafeClientConnManager(sr);
            return new DefaultHttpClient(mgr, httpClient.getParams());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


}

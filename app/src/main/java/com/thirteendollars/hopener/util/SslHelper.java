package com.thirteendollars.hopener.util;

import android.content.Context;
import android.util.Log;

import com.thirteendollars.hopener.R;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

/**
 * Created by Damian Nowakowski on 06/05/2017.
 * mail: thirteendollars.com@gmail.com
 */

public class SslHelper {


    private static TrustManager[] getManagers(Context context) throws KeyStoreException,CertificateException,NoSuchAlgorithmException,IOException {
        // Loading CAs from an InputStream
        CertificateFactory cf;
        cf = CertificateFactory.getInstance("X.509");

        Certificate ca;
        try ( InputStream cert = context.getResources().openRawResource(R.raw.hopener) ) {
            ca = cf.generateCertificate(cert);
        }
        // Creating a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore   = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

       // Creating a TrustManager that trusts the CAs in our KeyStore.
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        return tmf.getTrustManagers();

    }

    public static OkHttpClient getCustomHttpClient(Context context) {
        try {
                TrustManager[] trustManagers = getManagers(context);
                SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustManagers, new java.security.SecureRandom());
                SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                builder.sslSocketFactory(sslSocketFactory,(X509TrustManager)trustManagers[0]);
                builder.hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return hostname.equals(session.getPeerHost());
                    }
                });

                return builder.build();
            }
            catch (Exception e) {
                e.printStackTrace();
                return new OkHttpClient();
            }
    }

}

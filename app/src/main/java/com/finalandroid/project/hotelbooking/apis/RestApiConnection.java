package com.finalandroid.project.hotelbooking.apis;

import android.annotation.SuppressLint;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestApiConnection {

    public  static String PHOTO_BASE_URL = "http://192.168.1.111/HotelBooking/";

    private static RestApiConnection instance;
    private final ApiServices myApiServices;

    private RestApiConnection(){
        Gson gson = new GsonBuilder()
              .setLenient().create();

        //    The General Url OR The Base Url code for all links.
        String BASE_URL = "https://192.168.1.111/HotelBooking/";

        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getUnsafeOkHttpClient())
                .build();

        myApiServices = retrofit.create(ApiServices.class);
    }

    public static RestApiConnection getInstance(){

        if (instance == null) {
            synchronized(RestApiConnection.class){

                instance = new RestApiConnection();
            }
        }
        return instance;
    }

    public ApiServices getMyApi(){

        return myApiServices;
    }

//    To print all events and all things happened in RestApiConnection class. And print the response
//    if founded and the onFailure message if their no any response founded.
    public static Interceptor logHttpRequest(HttpLoggingInterceptor.Level level) {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

//        set your desired log level
        logging.setLevel(level);

        return logging;
    }


//    The certificate for SSL (Secure protocol/link).
    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            @SuppressLint("CustomX509TrustManager") final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @SuppressLint("TrustAllX509TrustManager")
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @SuppressLint("TrustAllX509TrustManager")
                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };
            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory);
            builder.hostnameVerifier((hostname, session) -> true);

            return builder
                    .addInterceptor(logHttpRequest(HttpLoggingInterceptor.Level.BODY))// To see the messages on logcat.
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }// handle exception of HttpHandShake exception
}
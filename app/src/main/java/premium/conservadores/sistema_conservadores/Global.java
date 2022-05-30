package premium.conservadores.sistema_conservadores;

import android.annotation.SuppressLint;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Global {

    /*
    *cuenta de google firebase
    * correo: conservadores.acerlum@gmail.com
    * clave: 2020..1tch4ng3
    *
    * url: carpeta del apk
    * https://www.mediafire.com/folder/95jvt10i3t3w9/Conservador+Android
    * */

    //evitar que envie dos veces la misma peticion
    //getRequest.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    //public static String url="http://192.168.12.110:12345/";
    //public static String url="http://192.168.100.8:12345/";
    //public static String url="http://192.168.1.68:80/";
    public static String url="https://pruebas.premiumice.es/";
    //public static String url="https://conservadores.premiumice.es/";
    //public static String url="https://pruebas.premiumice.es/";

    //variable global donde se guarda la informacion del usuario logeado


    //hacer peticiones a cualquier host sin seguridad
    @SuppressLint("TrulyRandom")
    public static void handleSSLHandshake(){

        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        } catch (Exception ignored) {
        }
    }




    public static Double formatearDecimales(Double numero, Integer numeroDecimales) {
        return Math.round(numero * Math.pow(10, numeroDecimales)) / Math.pow(10, numeroDecimales);
    }


}

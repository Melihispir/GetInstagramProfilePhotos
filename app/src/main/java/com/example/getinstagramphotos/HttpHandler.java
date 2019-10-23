package com.example.getinstagramphotos;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpHandler {

    private  static final String TAG = "HardwareAndro";

    public HttpHandler() {
    }

    //servisimizi çağırdığımız metodumuz
    public String makeServiceCall (String reqUrl){

        String response=null;

        try {
            URL url= new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //burada gelen url get isteğinde bulunuyoruz
            conn.setRequestMethod("GET");
            //gelen veriyide okuyup response içinde saklıyoruz
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response= convertStreamToString(in);

        }catch (Exception e) {
            Log.e(TAG,"Exception"+e.getMessage());
        }

        return  response;
    }

    //gelen veriler bir bütün içinde geliyor bunları parçaladığımız yer burası
    private String convertStreamToString(InputStream in) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        String line;

        try {

            //gelen satırları okuyor null gormedikce yeni bir stringe alt alta geçirerek atıyor
            while ((line=reader.readLine())!=null) {
                sb.append(line).append('\n');
            }
        }catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        //yeni nesneyi en son geri döndürüyor
        return sb.toString();
    }
}
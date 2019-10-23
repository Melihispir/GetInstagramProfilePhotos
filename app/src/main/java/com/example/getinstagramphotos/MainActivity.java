package com.example.getinstagramphotos;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    Bitmap bitmapImage=null;
    ImageView imageView;
    Button btn;
    EditText edt;
    String dosya="";
    String username="";
    String fotoLink="";
    ProgressDialog progressDialog;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edt = findViewById(R.id.editText);
        btn = findViewById(R.id.button);
        imageView = findViewById(R.id.imageView);

        btn.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = edt.getText().toString();
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected void onPreExecute() {
                        progressDialog = new ProgressDialog(MainActivity.this);
                        progressDialog.setMessage("Loading..");
                        progressDialog.show();
                    }

                    @Override
                    protected Void doInBackground(Void... voids) {
                        HttpHandler hh = new HttpHandler();
                        dosya = hh.makeServiceCall("https://www.instagram.com/"+username+"/?__a=1");
                        if(dosya!=null) {
                            try {
                                JSONObject jo = new JSONObject(dosya);
                                JSONObject jo2 = jo.getJSONObject("graphql").getJSONObject("user");
                                fotoLink = jo2.getString("profile_pic_url_hd");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            bitmapImage = getBitmapFromURL(fotoLink);
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        progressDialog.dismiss();
                        if(dosya!=null)
                        imageView.setImageBitmap(bitmapImage);
                        else{
                            Toast.makeText(getApplicationContext(), "hatalı giriş", Toast.LENGTH_LONG).show();
                        }

                    }
                }.execute();
            }
        });



    }
    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

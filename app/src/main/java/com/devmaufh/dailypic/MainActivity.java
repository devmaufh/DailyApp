package com.devmaufh.dailypic;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import  com.android.volley.Request.Method.*;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private String url="https://api.nasa.gov/planetary/apod?api_key=9YUjcWGektOEtjiwK1QlXAMbNsg3aKdSaDdO6v73";
    private TextView tvTitle,tvDescription;
    private ImageView img;
    private RequestQueue queue;
    private String apodTitle,apodDescription,apodImageUrl;
    private String credential="AIzaSyDq3tTjY-znQIHnqF85R_X_JkOqPpqhrJY";
    CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindUI();
        requestNASA(url);
        Toast.makeText(this, apodImageUrl, Toast.LENGTH_SHORT).show();
    }
    private void setValues(){
        tvTitle.setText(apodTitle);
        tvDescription.setText(apodDescription);
        Picasso.with(getApplicationContext()).load(apodImageUrl).into(img);
        cardView.setVisibility(View.VISIBLE);

    }
    private void bindUI(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        tvTitle=(TextView)findViewById(R.id.tvTitle);
        tvDescription=(TextView)findViewById(R.id.tvDescription);
        img=(ImageView)findViewById(R.id.imgFondo);
        tvDescription.setMovementMethod(new ScrollingMovementMethod());
        queue= Volley.newRequestQueue(this);
        cardView= (CardView)findViewById(R.id.card);

    }
    private void requestNASA(String url){
        JsonRequest request= new JsonObjectRequest(com.android.volley.Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    apodTitle=response.getString("title");
                    apodDescription=response.getString("explanation");
                    apodImageUrl=response.getString("hdurl");
                    //getTranslatedText(apodDescription);
                    //tvDescription.setText(getTranslatedText(apodDescription));
                    setValues();

                } catch (JSONException e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Verifica tu conexión a internet ☹", Toast.LENGTH_SHORT).show();
                apodTitle=apodImageUrl=apodDescription="";
            }
        });
        queue.add(request);
    }
    private String getTranslatedText(String text){
        Translate translate= TranslateOptions.getDefaultInstance().getService();
        Translation translation=translate.translate(text, Translate.TranslateOption.sourceLanguage("en"),Translate.TranslateOption.targetLanguage("es"));
        return translation.getTranslatedText();
    }

}
